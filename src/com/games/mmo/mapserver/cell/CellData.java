package com.games.mmo.mapserver.cell;

import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.Treasure;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.type.MonsterType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BaseLogUtil;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.ThreadLocalUtil;

/**
 * 地图的视野块
 * @author saly.bao
 */
public class CellData
{
	public static ConcurrentHashMap<String, List<Cell>> cellCache=new ConcurrentHashMap<String, List<Cell>>();
   
	public List<Cell> allCellList=new ArrayList<Cell>();
    /** 地图上的cell列表 */
    public Cell[][] cellList;

    /** 横向Cell数 */
    private int cellSizeW;

    /** 竖向Cell数 */
    private int cellSizeH;

    /** 9宫格方向 */
    public static int deltax3[] = { -1, 0, 1 };
    public static int deltay3[] = { -1, 0, 1 };

    public static int deltax5[] = { -2,-1, 0, 1 ,2};
    public static int deltay5[] = { -2,-1, 0, 1 ,2};
    
    /** 大地图游戏对象,只有在大地图游戏中才会用到cell管理 */
    private MapRoom mapRoom;

    /** 单个Cell的X长度 */
    private int cellPixelX;
    
    /** 单个Cell的Y长度 */
    private int cellPixelY;
    
    /** 地图总像素宽 */
    private int mapPixelWidth;
    
    /** 地图总像素高 */
    private int mapPixelHeight;

    /**
     * 
     * @param mapPixelWidth 地图总像素宽
     * @param mapPixelHeight 地图总像素高
     * @param cellPixelX 单个Cell像素宽
     * @param cellPixelY 单个Cell像素高
     * @param mapRoom 所属地图
     */
    public CellData(int mapPixelWidth, int mapPixelHeight, int cellPixelX, int cellPixelY, MapRoom mapRoom)
    {
        this.mapRoom = mapRoom;
        this.cellPixelX = cellPixelX;
        this.cellPixelY = cellPixelY;
        this.mapPixelWidth = mapPixelWidth;
        this.mapPixelHeight = mapPixelHeight;
        cellSizeW = (mapPixelWidth*2 + cellPixelX - 1)
                / cellPixelX;
        cellSizeH = (mapPixelHeight*2 + cellPixelY - 1)
                / cellPixelY;
        // 保证Cell数是3的倍数
        if (cellSizeW % 3 != 0)
        {
            cellSizeW += 3 - cellSizeW % 3;
        }

        if (cellSizeH % 3 != 0)
        {
            cellSizeH += 3 - cellSizeH % 3;
        }
        cellList = new Cell[cellSizeW][cellSizeH];
        for (int i = 0; i < cellSizeW; i++)
        {
            for (int j = 0; j < cellSizeH; j++)
            {
                cellList[i][j] = new Cell(mapRoom,i, j);
                allCellList.add(cellList[i][j]);
            }
        }
    }

    public int pixelXToCellX(int pixelX)
    {
        return (pixelX+mapPixelWidth) / cellPixelX;
    }

    public int pixelYToCellY(int pixelY)
    {
        return (pixelY+mapPixelHeight) / cellPixelY;
    }

    public boolean isValidCell(int x, int y)
    {
        return x >= 0 && y >= 0 && x < cellSizeW && y < cellSizeH;
    }

    // 当前大地图上添加生物
    public synchronized boolean addLiving(Entity living)
    {
        if (this == null){
            return false;
        }
    	return living.cellDataAddliving(this);
    }

    public List<Fighter> getAllMapNpcs() {
        List<Fighter> celList = new ArrayList<Fighter>();
		for (Fighter fighter : getAllCellMonsterss()) {
			if(fighter.monsterPo.getMonsterType()==MonsterType.MONSTER_NPC){
				celList.add(fighter);
			}
			else if(fighter.monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE10){
				celList.add(fighter);
			}
		}
        return celList;
	}
    


	/**
     * 移除生物
     * 
     * @param living
     * @param isNotify 是否通知客户端
     */
    public synchronized void removeLiving(Entity living, boolean isNotify)
    {
        if (living == null){
            return;
        }
        living.cellDateRemoveLiving(this,isNotify);

    }


    /**
     * 生物移进新的Cell
     * 
     * @param living
     * @param newPos
     * @return
     */
    public synchronized boolean livingMoveInCell(Entity living, int newX, int newY, int newZ)
    {
    	
//    	newY = newZ;
        if (living == null)
        {
            return false;
        }
        return living.CellDataLivingMoveInCell(this,newX,newY,newZ);

    
    }

    /**
     * 将英雄周围的环境的上下文发送给玩家
     * 
     * @param player 通知的玩家
     * @param newCells 要添加的Cell中生物
     */
    public void sendNearByContextToPlayer(Fighter player,List<Entity> livings)
    {
    	if(player.rolePo==null){
    		return;
    	}
    	RoleTemplate roleTemplate=RoleTemplate.instance();
    	if(player==null || roleTemplate.iuidSessionMapping.get(player.rolePo.getIuid())==null){
    		return;
    	}
        try
        {
            if (livings!=null && livings.size() > 0)
            {
            	ChannelHandlerContext session = (ChannelHandlerContext) roleTemplate.iuidSessionMapping.get(player.rolePo.getIuid());
            	List<byte[]> list=new ArrayList<byte[]>();
                for(Entity living : livings)
                {
                	if(living instanceof Fighter){
                		if(living.type==Entity.MOVER_TYPE_PLAYER){
                			Fighter fighter =(Fighter) living;
                			if(!fighter.robot){
                        		if(player!=living && !player.farSeeingPlayeFighters.containsKey(living.mapUniqId)){
                        			continue;
                        		}
                			}
                		}
                		else if(living.type==Entity.MOVER_TYPE_PET){
                			int masterId=0;
                			Fighter pet =(Fighter)living;
                			if(pet.master!=null && pet.master.fighter!=null){
                    			masterId=((Fighter) living).master.fighter.mapUniqId;
                			}
                			if(masterId!=0){
                        		if(!player.farSeeingPlayeFighters.containsKey(masterId)){
                        			continue;
                        		}
                			}
                		}
                		list.add(((Fighter)living).fetchMoverStatus());
                	}
                }
                BattleMsgUtil.singleAbroadAppearLivings(session,list,true);
            }            
        }
        catch (Exception e)
        {
    		ExceptionUtil.processException(e);
        }
    }

    /**
     * 通知客户端批量移除生物
     * 
     * @param player 通知的玩家
     * @param oldCells 要移除的Cell中生物
     */
    public void notifyClientRemoveLivings(Fighter player, List<Cell> oldCells)
    {
        try
        {
            if (oldCells.size() > 0 && player != null)
            {
                List<Entity> livings = new ArrayList<Entity>();
                for (Cell cell : oldCells)
                {
                    livings.addAll(cell.getAllLivings());
                }
                if (livings.size() > 0)
                {
                	RoleTemplate roleTemplate=RoleTemplate.instance();
                	if(player.rolePo!=null &&  roleTemplate.iuidSessionMapping.get(player.rolePo.getIuid())!=null){
                		ChannelHandlerContext session = (ChannelHandlerContext) roleTemplate.iuidSessionMapping.get(player.rolePo.getIuid());
                		List<byte[]> items =new ArrayList<byte[]>(livings.size()); 
                		for(int i=0,max=livings.size();i<max;i++)
                		{
                			items.add(ByteUtil.int2Byte(livings.get(i).mapUniqId));
                		}
                		MonsterPo monsterPo = MonsterPo.findEntity(player.itemId);
                		if(monsterPo != null && monsterPo.getMonsterType().intValue() == MonsterType.MONSTER_TYPE8){
                            BattleMsgUtil.singleAbroadDisappearYunCartCat(session,items,true);
                		}else{
                			BattleMsgUtil.singleAbroadDisappearLiving(session,items,true);                   	
                		}                		
                	}
                }
            }
        }
        catch (Exception e)
        {
        	ExceptionUtil.processException(e);
//        	BaseLogUtil.exceptionLogger.error("CellData notifyClientRemoveLivings:", e);
        }
    }

    /**
     * 向周围9宫格广播消息
     * 
     * @param except 屏蔽的对象
     */
//    public void broadcastMsgToNearbyLiving(String msg, int x, int y, int z,RolePo except,boolean flush)
//    {
//    	y=z;
//        int cX = pixelXToCellX(x);
//        int cY = pixelYToCellY(y);
//
//        if (!isValidCell(cX, cY))
//        {
//        	BaseLogUtil.exceptionLogger.error("cellData broadcastMsgToNearbyLiving cX,cY is valid! cX:"+ cX + " cY:" + cY + "(x,y):" + x + ":" + y);
//            return;
//        }
//
//        // 再广播
//        for (int i = 0; i < 3; i++)
//        {
//            for (int j = 0; j < 3; j++)
//            {
//                int tX = cX + deltax[i];
//                int tY = cY + deltay[j];
//                if (!isValidCell(tX, tY))
//                {
//                    continue;
//                }
//                Cell cell = cellList[tX][tY];
//                cell.sendToAll(msg, except,flush);
//            }
//        }
//    }

    /**
     * 清空全图
     */
    public void clearCellList()
    {
        for (int i = 0; i < cellSizeW; i++)
            for (int j = 0; j < cellSizeH; j++)
            {
                cellList[i][j].clear();
            }
    }

    /**
     * 根据坐标获取Cell
     * 
     * @param pos
     * @return
     */
	public Cell getCell(int x, int y, int z)
    {
		y=z;
        int cX = pixelXToCellX(x);
        int cY = pixelYToCellY(y);
        if (!isValidCell(cX, cY))
            return null;
        return cellList[cX][cY];
    }

    /**
     * 得到某个点周围所有的cell
     * 
     * @param pos
     * @return
     */
    public List<Cell> getNearByCells(int x, int y, int z,int limit)
    {
    	y=z;
        int cX = pixelXToCellX(x);
        int cY = pixelYToCellY(y);

        if (!isValidCell(cX, cY))
        {
        	PrintUtil.print("cellData broadcastMsgToNearbyLiving cX,cY is valid! cX:" + cX + " cY:" + cY + "(x,y):" + x + ":" + y);
            return null;
        }

        List<Cell> celList = getNearByCells(cellList[cX][cY],limit);
        return celList;
    }

    /**
     * 根据Cell获取周围Cell
     * 
     * @param cX
     * @param cY
     * @return
     */
    public List<Cell> getNearByCells(Cell tempCell,int limit)
    {

    	if(tempCell == null){
    		return null;
    	}
    	limit=Math.max(3, limit);
    	StringBuilder sb=new StringBuilder(16);
    	sb.append(mapRoom.mapRoomId).append("_").append(limit).append("_").append(tempCell.X).append("_").append(tempCell.Y);
    	String key=sb.toString();
    	if(cellCache.containsKey(key)){
    		return cellCache.get(key);
    	}

        int cX = tempCell.X;
        int cY = tempCell.Y;
        List<Cell> celList = new ArrayList<Cell>();

        // 再广播
        for (int i = 0; i < limit; i++)
        {
            for (int j = 0; j < limit; j++)
            {
                int tX = 0;
                int tY = 0;
            	if(limit==3){
                    tX = cX + deltax3[i];
                    tY = cY + deltay3[j];
            	}
            	else if(limit==5){
                    tX = cX + deltax5[i];
                    tY = cY + deltay5[j];
            	}else{
            		ExceptionUtil.throwConfirmParamException("error delta:"+limit);
            	}

                if (!isValidCell(tX, tY))
                {
                    continue;
                }
                Cell cell = cellList[tX][tY];
                if(!celList.contains(cell)){
                    celList.add(cell);
                }
            }
        }
        cellCache.put(key, celList);
        return celList;
    }
    
//    public List<Cell> fetchAllCells()
//    {
//
//        List<Cell> list = new ArrayList<Cell>();
//        for (Cell[] cell : cellList) {
//			for (Cell cell2 : cell) {
//				list.add(cell2);
//			}
//		}
//
//        return list;
//    }

    /**
     * 初始化
     */
    public void init()
    {
    }

    @Override
    public String toString()
    {
    	StringBuilder stringBuilder = new StringBuilder();
    	for (int j = 0; j < cellSizeH; j++)
        {
        	
        	for (int i = 0; i < cellSizeW; i++)
        	{
        		String str = cellList[i][j].toString();
        		if(!"".equals(str))
        		{
        			stringBuilder.append(str);
            		stringBuilder.append(System.getProperty("line.separator"));
        		}
        	}
        }
        return stringBuilder.toString();
    }

	public MapRoom getMapRoom() {
		return mapRoom;
	}

	public void setMapRoom(MapRoom mapRoom) {
		this.mapRoom = mapRoom;
	}

	/**
	 * 获取地图中所有cell中生物 
	 * @return
	 */
	public List<Entity> getAllCellLivings() {
		List<Entity> allCellLivings = new ArrayList<Entity>();
		for(Cell[] cells:cellList)
		{
			for(Cell cell:cells)
			{
				allCellLivings.addAll(cell.getAllLivings());
			}
		}
		return allCellLivings;
	}
	
	/**
	 * 获取地图中所有玩家 
	 * @return
	 */
	public List<Fighter> getAllCellPlayers() {
		List<Fighter> allCellLivings = new ArrayList<Fighter>();
		for(Cell[] cells:cellList)
		{
			for(Cell cell:cells)
			{
				allCellLivings.addAll(cell.getAllPlayers());
			}
		}
		return allCellLivings;
	}
	
	/**
	 * 获取地图中所有格子（注格子数据只可查不可改）
	 * @return
	 */
	public Cell[][] getAllCells()
	{
		return cellList;
	}

	/**
	 * 获取地图中所有怪物
	 * @return
	 */
	public List<Fighter> getAllCellMonsterss() {
		List<Fighter> allCellLivings = new ArrayList<Fighter>();
		for(Cell[] cells:cellList)
		{
			for(Cell cell:cells)
			{
				allCellLivings.addAll(cell.getAllMonsters());
			}
		}
		return allCellLivings;
	}
	
	/**
	 * 删除所有运镖车
	 * @return
	 */
	public void removeAllYunDartFighter(){
		List<Fighter> yunDartList =  getAllCellMonsterss();
		for(Fighter fighter : yunDartList){
			if(fighter.wasYunDart == 1){
				removeLiving(fighter, true);					
			}
		}
	}

}
