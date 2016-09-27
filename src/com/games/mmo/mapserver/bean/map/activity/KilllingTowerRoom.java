package com.games.mmo.mapserver.bean.map.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.MapRoomParVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.games.mmo.vo.xml.ConstantFile.KillingTower.Toweritem;
import com.storm.lib.vo.IdNumberVo;

/**
 * 极限乱斗
 * @author saly.bao
 *
 */
public class KilllingTowerRoom extends DynamicMapRoom {
	public int runTimeIndex=0;
	//极限乱斗
	public static final int SCENE_ID = 20100017;
	//第一层统筹其他层
	public boolean leaderRoom=false;
	public List<KilllingTowerRoom> followRooms = new ArrayList<KilllingTowerRoom>();
	public List<KilllingTowerRoom> totalRooms = new ArrayList<KilllingTowerRoom>();
	public ConcurrentHashMap<Integer, Integer> playerIds =new ConcurrentHashMap<Integer, Integer>();
	public KilllingTowerRoom leaderFightRoom;
	
	public KilllingTowerRoom(ScenePo scenePo,boolean leaderRoom) {
		super(scenePo);
//		System.out.println("scenePo.name="+scenePo.getName() +"; leaderRoom="+leaderRoom);
		this.leaderRoom=leaderRoom;
		leaderFightRoom=this;
		if(leaderRoom){
			totalRooms.add(this);
			for(int i=CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START+1;i<=CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_END;i++)
			{
				MapRoomParVo mapRoomParVo=new MapRoomParVo();
				mapRoomParVo.isLeaderRooom=false;
				MapRoom room = MapWorld.createDynalicMapRoom(CopySceneConfPo.findEntity(i),ScenePo.findEntity(SCENE_ID),mapRoomParVo);
				followRooms.add((KilllingTowerRoom) room);
				totalRooms.add((KilllingTowerRoom) room);
				((KilllingTowerRoom)room).leaderFightRoom=this;
			}
			startThread();
		}
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
		sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key8"));
	}

	public void play(){
		super.play();
		checkAddScore();
		if(leaderRoom){
			checkTimeExpiredGameOver();
		}
	}		
	
	private void checkAddScore() {
	}


	//	2.2.2 积分获得规则		
	//	①角色成功参与活动获得40点积分						
	//	②角色在该场景每10秒获得1点积分						
	//	③角色每击杀1名其他玩家获得积分				小于等于80人		获得5点积分
	//	大于80人		获得1点积分

	/**
	 * 生物死亡扩展实现
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.type == Entity.MOVER_TYPE_PLAYER){
			killer.rolePo.pVPPVEActivityStatusVo.currentFloorKillCount++;
			killer.rolePo.pVPPVEActivityStatusVo.monsterKillCount++;
			killer.rolePo.sendUpdatePVPPVEActivity();
		}

	}
	
	/**
	 * 舞台实体移除
	 */
	@Override
	public void onRemoveMover(Fighter fighter){
		super.onRemoveMover(fighter);
		if(fighter.type==Fighter.MOVER_TYPE_PLAYER){
			fighter.rolePo.calculateBat(1);
		}
	}
	
	public boolean playerJoinRoom(RolePo rolePo){
		
		boolean firstJoin=false;
		if(!leaderFightRoom.joinPlayers.contains(rolePo)){
			leaderFightRoom.joinPlayers.add(rolePo);
			firstJoin=true;
		}
		if(firstJoin){
			rolePo.pVPPVEActivityStatusVo.init();
		}
		return firstJoin;
	}
	
	public void playerLeaveRoom(RolePo rolePo){
		if(leaderFightRoom!=null && leaderFightRoom.joinPlayers!=null && rolePo!=null){
			leaderFightRoom.joinPlayers.remove(rolePo);			
		}
	}	
	
	/**
	 * 实体添加到舞台
	 */
	@Override
	public void onAddMover(Fighter mover){
		super.onAddMover(mover);
		if(mover.type == Entity.MOVER_TYPE_PLAYER){
			mover.rolePo.leaveMyTeam();
			if(leaderRoom){
				if(!playerIds.containsKey(mover.rolePo.getId())){
					playerIds.put(mover.rolePo.getId(), mover.rolePo.getId());
				}
			}
			mover.rolePo.pVPPVEActivityStatusVo.roomCreatedTime=new Long(createdTime/1000).intValue();
			mover.rolePo.sendUpdatePVPPVEActivity();
			mover.changeScore(40);
			
		}
	}
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		if(leaderRoom){
			super.onGameOver();
			RolePo maxKill = null;
			int maxKillCount = 0;
			for (RolePo player : leaderFightRoom.joinPlayers) {
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				for (Toweritem towerItem : XmlCache.xmlFiles.constantFile.killingTower.toweritem) {
					if(towerItem.layer==player.pVPPVEActivityStatusVo.currentFloor){
						List<IdNumberVo> vals = IdNumberVo.createList(towerItem.awards);
						for (IdNumberVo idNumberVo : vals) {
							List<Integer> items=new ArrayList<Integer>();
							items.add(idNumberVo.getId());
							items.add(idNumberVo.getNum());
							battleResultVo.itemList.add(items);
						}
					}
				}
				int score = player.pVPPVEActivityStatusVo.currentFloor*1000+player.pVPPVEActivityStatusVo.monsterKillCount;
				if(score>maxKillCount){
					maxKill=player;
					maxKillCount=score;
				}
				player.battleResultVo=battleResultVo;
				if(player.fighter!=null){
					player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
				}

				player.sendCopySceneFinish(battleResultVo);			
				player.calculateBat(1);
			}
			
			if(maxKill!=null){
				PVEPVPRecordVo.instance.checkHighLiveActivity(maxKill.getName(),maxKill.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KILLING_TOWER,maxKill.pVPPVEActivityStatusVo.currentFloor,maxKill.pVPPVEActivityStatusVo.monsterKillCount);
			}

		}
	}
	
	
}
