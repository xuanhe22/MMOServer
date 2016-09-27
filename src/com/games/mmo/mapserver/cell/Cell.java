package com.games.mmo.mapserver.cell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.Treasure;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.AgreementCountVo;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.util.BaseLogUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.ThreadLocalUtil;

/**
 * 服务器做地图视野管理的最小单位
 * @author saly.bao
 */
public class Cell
{
	public MapRoom mapRoom;
	public static long last1Seconds =0l;
	public static long last1SecondTotalMsg=0;
	public static long last1SecondCountMsg=0;
	public static ConcurrentHashMap<Integer,AgreementCountVo> countMap = new ConcurrentHashMap<Integer,AgreementCountVo>();
 	
	private CopyOnWriteArrayList<Entity> livingArrayList;
    /** 当前块的所有生物列表 */
    private ConcurrentHashMap<Integer, Entity> livingList;
    /** 当前块的怪物列表 */
    private ConcurrentHashMap<Integer, Fighter> monsterList;
    /** 当前块的玩家列表 */
    public ConcurrentHashMap<Integer, Fighter> playerList;
    
    public CopyOnWriteArrayList<Fighter> playerArrayList;
    
    public CopyOnWriteArrayList<Fighter> monsterArrayList;

    /** 当前块的X索引 */
    public int X = 0;

    /** 当前块的Y索引 */
    public int Y = 0;

    public Cell(MapRoom mapRoom,int x, int y)
    {
    	this.mapRoom=mapRoom;
    	livingArrayList=new CopyOnWriteArrayList<Entity>();
    	livingList = new ConcurrentHashMap<Integer, Entity>();
        monsterList = new ConcurrentHashMap<Integer, Fighter>();
        playerList = new ConcurrentHashMap<Integer, Fighter>();
        playerArrayList=new CopyOnWriteArrayList<Fighter>();
        monsterArrayList=new CopyOnWriteArrayList<Fighter>();
        X = x;
        Y = y;
    }

    /** 添加生物 */
    public  void addLiving(Entity living)
    {
        if (living != null)
        {
        	if(living instanceof Fighter)
        	{
        		Fighter fighter = (Fighter)living;
        		if(fighter.type == Entity.MOVER_TYPE_PLAYER)
        		{
                	playerList.put(fighter.mapUniqId, fighter);
                	playerArrayList.add(fighter);
        		}
        		if(fighter.type == Entity.MOVER_TYPE_MONSTER)
        		{
        			monsterList.put(fighter.mapUniqId, fighter);
        			monsterArrayList.add(fighter);
        		}
        	}
        	livingList.put(living.mapUniqId, living);
        	livingArrayList.add(living);

        	living.cell = this;
        }
    }

    /** 移除生物 */
    public  void removeLiving(Entity living,boolean roomRemove)
    {
    	
        if (living != null)
        {
        	if(living instanceof Fighter)
        	{
        		if(roomRemove && living.mapRoom!=null){
        			living.mapRoom.fighterIdMaps.remove(living.mapUniqId);
        		}
        		Fighter fighter = (Fighter)living;
        		if(fighter.type == Entity.MOVER_TYPE_PLAYER)
        		{
        			playerList.remove(fighter.mapUniqId);
        			playerArrayList.remove(fighter);
            		fighter.removeAllAgentMonsters();//清除代理怪物
        		}
        		if(fighter.type == Entity.MOVER_TYPE_MONSTER)
        		{
        			monsterList.remove(fighter.mapUniqId);
        			monsterArrayList.remove(fighter);
            		fighter.removeAllAgentMonsters();//清除代理怪物
        		}        		
        	}
        	livingList.remove(living.mapUniqId);
        	livingArrayList.remove(living);
        }
    }


    /**
     * 生物出现
     * @param living
     */
    public void onAppear(Entity living,byte[] results,Cell centerCell)
    {
    	if(living instanceof Fighter){
        	boolean far = farOrNear(centerCell);
            for (Fighter player : getAllPlayers())
            {

            	player.checkPlayerAddSeePlayer(living,far);
            	((Fighter) living).checkPlayerAddSeePlayer(player,far);
            }
            List<Fighter> abroadList=buildAbroadList(living,BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING);
    		BattleMsgUtil.sendToAll(mapRoom,abroadList,living,results, null,true,BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING);
    	}
    }

    /**
     * far 25宫 near 9宫
     * @param centerCell
     * @return
     */
    private boolean farOrNear(Cell centerCell) {
    	if(centerCell==null){
    		return true;
    	}
    	else{
    		int deltaX =Math.abs(centerCell.X-X);
    		int deltaY =Math.abs(centerCell.Y-Y);
    		if(deltaX>1 || deltaY>1){
    			return true;
    		}
    	}
		return false;
	}

	public List<Fighter> buildAbroadList(Entity target,int msgType) {
        if (playerArrayList.size() <= 0){
            return null;
        }
        boolean dynamic=false;
        boolean targetIsMonster=false;
        boolean targetIsPlayer=false;
        boolean targetIsPet=false;
        boolean targetIsRobot=false;
        boolean targetIsBoss=false;
        boolean targetMonsterRequireFilterMsgType=false;
        boolean far=false;
		if(target.type==Entity.MOVER_TYPE_MONSTER){
			targetIsMonster=true;
			MonsterPo monsterPo =MonsterPo.findEntity(target.itemId);
			if(monsterPo!=null){
				//目标是BOSS不过滤
				if(monsterPo.getMonsterType()!=MonsterType.MONSTER_TYPE1){
					targetIsBoss=true;
				}
			}
			//非移动,血量变化,buff变化不过滤
			if((msgType==BattleMsgUtil.BATTLE_MSG_TYPE_FIGHTER_MOVE || msgType==BattleMsgUtil.BATTLE_MSG_TYPE_FIGHTER_HP_CHANGE || msgType==BattleMsgUtil.BATTLE_MSG_TYPE_BUFF || msgType==BattleMsgUtil.BATTLE_MSG_TYPE_ADD_BUFF )){
				targetMonsterRequireFilterMsgType=true;
			}
		}
		if(target.type==Entity.MOVER_TYPE_PLAYER){
			targetIsPlayer=true;
			if(((Fighter)target).rolePo!=null){
				targetIsRobot=((Fighter)target).robot;
			}
		}
		if(target.type==Entity.MOVER_TYPE_PET){
			targetIsPet=true;
		}
        //副本房间不过滤
		if(mapRoom!=null && mapRoom.isDynamic){
			dynamic=true;
		}
		if(msgType==BattleMsgUtil.BATTLE_MSG_TYPE_FIGHTER_MOVE){
			far=true;
		}
		else if(msgType==BattleMsgUtil.BATTLE_MSG_TYPE_DIE){
			far=true;
		}
		else if(msgType==BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING){
			far=true;
		}
		else if(msgType==BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_LIVING){
			far=true;
		}
        List<Fighter> fighters =new ArrayList<Fighter>();
	        for (Fighter player : getAllPlayers())
	        {
        		if(player.rolePo==null){
        			continue;
        		}
	        	if(player.rolePo.robot)
	        	{
	        		continue;
	        	}
        		if(!dynamic){
        			if(targetIsPlayer){
		        		//过滤不希望看到的玩家消息
		        		if(!targetIsRobot && player.filterTargetPlayer(target,far)){
		        			continue;
		        		}
        			}
        			if(targetIsPet){
        				if(msgType!=BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_LIVING){
    		        		//过滤不希望看到的宠物消息
    		        		if(player.filterTargetPet(target,far)){
    		        			continue;
    		        		}
        				}

        			}
        			//TODO 【性能优化】怪物广播信息需要关心什么呢?不处理的话就怪物全广播
        			if(targetIsMonster){
		        		//过滤不希望看到的怪物信息
			        	if(targetMonsterRequireFilterMsgType && !targetIsBoss && player.filterTargetMonster(target,msgType)){
			        		continue;
			        	}
        			}
        		}
        		fighters.add(player);
	        }
	        return fighters;
	}

	/**
     * 生物消失
     * 
     * @param living
     */
    public void onDisappear(Entity living,byte[] resultBytes,Cell newCell,Cell centerCell)
    {

    	List<Fighter> abroadList=buildAbroadList(living,BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_LIVING);
    	if(abroadList!=null){
    		List<Fighter> removeList=new ArrayList<Fighter>();
    		if(newCell!=null){
            	for (Fighter fighter : abroadList) {
        			int x1=newCell.X;
        			int y1=newCell.Y;
        			int x2=fighter.cell.X;
        			int y2=fighter.cell.Y;
        			int xDelta = Math.abs(x1-x2);
        			int yDelta = Math.abs(y1-y2);
        			if(xDelta>2 || yDelta>2){
        			}
        			else{
           				removeList.add(fighter);
        			}
        		}
    		}
        	abroadList.removeAll(removeList);
    	}
		boolean far = farOrNear(centerCell);
    	BattleMsgUtil.sendToAll(mapRoom,abroadList,living,resultBytes, null,true,BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_LIVING);  
			for(Fighter player : getAllPlayers()){
		    	player.checkPlayerRemoveSeePlayer(living,far);
		    	if(living instanceof Fighter){
		    		((Fighter)living).checkPlayerRemoveSeePlayer(player,far);
		    	}
			}
    }

    /**
     * 得到当前格Cell上所有玩家
     * 
     * @return
     */
    public List<Fighter> getAllPlayers()
    {
    	return playerArrayList;
    }

    /**
     * 得到Cell上的所有怪物
     * 
     * @return
     */
    public List<Fighter> getAllMonsters()
    {
        return monsterArrayList;
    }
    
    /**
     * 得到Cell上的所有生物
     * 
     * @return
     */
    public List<Entity> getAllLivings()
    {
    	return livingArrayList;
    }
    

    /**
     * 清除所有
     */
    public void clear()
    {
    		livingList.clear();
    		livingArrayList.clear();
    		playerList.clear();
    		playerArrayList.clear();
    		monsterList.clear();
    		monsterArrayList.clear();
    }


	@Override
	public String toString() {
		return "Cell [X=" + X + ", Y=" + Y + "]";
	}





}
