package com.games.mmo.mapserver.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.cell.CellData;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.LiveActivityObjectItem;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.event.EventSource;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;


public abstract class Entity  extends EventSource{
	public MapRoom mapRoom;
	public Integer type;
	public Integer mapUniqId;
	public Integer itemId = 0;
	public Float x=0.1f;
	public Float y=0.1f;
	public Float z=0.1f;
	public Float targetX=0f;
	public Float targetY=0f;
	public Float targetZ=0f;
	public Float aimX=0f;//目标点X
	public Float aimY=0f;//目标点Y
	public Float aimZ=0f;//目标点Z
	public Cell cell;
	
	public abstract boolean realPlayer();

	/**
	 * 角色类型：怪物
	 */
	public static final int MOVER_TYPE_MONSTER = 1;
	/**
	 * 角色类型：玩家
	 */
	public static final int MOVER_TYPE_PLAYER = 2;
	/**
	 * 角色类型：宠物
	 */
	public static final int MOVER_TYPE_PET = 3;
	/**
	 * 机器人类型：任务机器人
	 */
	public static final int ROBOT_TYPE_TASK = 1;
	/**
	 * 机器人类型：竞技场机器人
	 */
	public static final int ROBOT_TYPE_ARENA = 2;
	
	public static final int BASE_NUMBER = 10000;
	public static final int STAND_BORDER = 3;
	public boolean moverStatusChanged=true;
	public boolean moverStatusChangedNoHead=true;
	public boolean fighterStatusChangedNoHead=true;
	public byte[] moverStatusBytes;


	
	public boolean cellDataAddliving(CellData cellData) {

        int x = (int)(this.x*Entity.BASE_NUMBER);
        int y = (int)(this.z*Entity.BASE_NUMBER);
        int cX = cellData.pixelXToCellX(x);
        int cY = cellData.pixelYToCellY(y);
        if (!cellData.isValidCell(cX, cY))
        {
        	PrintUtil.print("cellData addLiving cX,cY is valid! cX:" + cX + " cY:" + cY + "(x,y):" + x + ":" + y);
            return false;
        }

		byte[] addResults=buildMoverAppearBytes();
		addResults =MMOByteEncoder.buildSocketPackage(addResults,ServerPack.SPECIAL_ORDER_ID_BATTLE);
        
		Cell centerCell=cellData.cellList[cX][cY];
        // 选广播给周边living
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                int tX = cX + cellData.deltax5[i];
                int tY = cY + cellData.deltay5[j];
                if (!cellData.isValidCell(tX, tY))
                {
                    continue;
                }
                Cell cell = cellData.cellList[tX][tY];
                cell.onAppear(this,addResults,centerCell);
            }
        }

        // 最后加到当前块
        Cell oldCell = this.cell;
        if (oldCell != null && oldCell != cellData.cellList[cX][cY])
        {
            oldCell.removeLiving(this,false);
        }
        cellData.cellList[cX][cY].addLiving(this);
        this.cell = cellData.cellList[cX][cY];
        
        // 再广播给自己
        if (this instanceof Fighter && this.realPlayer())
        {

    		List<Entity> list = MapRoom.buildEntityListByAllNpcAndNearByCell(cellData,cellData.cellList[cX][cY],5,true);
        	cellData.sendNearByContextToPlayer((Fighter)this,list); 	
        }
        return true;
	}


	public void cellDateRemoveLiving(CellData cellData, boolean isNotify) {
        Cell cell = this.cell;
        if (cell == null)
        {
            return;
        }
        
        // 先广播给自己
        if (isNotify && this instanceof Fighter && this.type == Entity.MOVER_TYPE_PLAYER)
        {
        	//通知客户端移除老Cell生物
//        	cellData.notifyClientRemoveLivings((Fighter)this, cellData.getNearByCells(cell,Entity.STAND_BORDER));
        }
        // 先删除
        cell.removeLiving(this,true);
        byte[] disappearBytes=buildMoverDisAppearBytes();
        // 再广播给周边living
        if(isNotify)
        {
    		Cell centerCell=cell;
        	for (int i = 0; i < 5; i++)
            {
                for (int j = 0; j < 5; j++)
                {
                    int tX = cell.X + cellData.deltax5[i];
                    int tY = cell.Y + cellData.deltay5[j];
                    if (!cellData.isValidCell(tX, tY))
                    {
                        continue;
                    }
                    Cell rCell = cellData.cellList[tX][tY];
                    rCell.onDisappear(this,disappearBytes,null,centerCell);
                }
            }
        }
	}


	public boolean CellDataLivingMoveInCell(CellData cellData, int newX,int newY, int newZ) {
			int newCellX = cellData.pixelXToCellX(newX);
	        int newCellY = cellData.pixelYToCellY(newZ);
	        Cell newCell = cellData.cellList[newCellX][newCellY];
	        if (!cellData.isValidCell(newCellX, newCellY))
	        {
	        	PrintUtil.print("livingMoveInCell wrong coord tX2,tY2! tX:" + newCellX + " tY:" + newCellY + "(x,y):" + newX + ":" + newY);
	            return false;
	        }

	        // 在同一块地方，那就这样吧
	        if (this.cell == cellData.cellList[newCellX][newCellY])
	        {
	            return true;
	        }

	        if(this.cell==null){
	        	return false;
	        }
	        int oldCellX = this.cell.X;
	        int oldCellY = this.cell.Y;

	        if (!cellData.isValidCell(oldCellX, oldCellY))
	        {
	        	PrintUtil.print("livingMoveInCell wrong coord tX1,tY1! tX:" + oldCellX + " tY:" + oldCellY + "(x,y):" + this.x + ":" + this.y);
	            return false;
	        }

	        List<Cell> oldCells = new ArrayList<Cell>();
	        List<Cell> newCells = new ArrayList<Cell>();

	        Cell centerOldCell=this.cell;
	        for (int i = -2; i < 3; i++)
	        {
	            for (int j = -2; j < 3; j++)
	            {
	                int tempX = oldCellX + i;
	                int tempY = oldCellY + j;
	                if (cellData.isValidCell(tempX, tempY))
//	                        && !(tempX > (newCellX - 3) 
//	                        && tempX < (newCellX + 3) 
//	                        && tempY > (newCellY - 3) 
//	                        && tempY < (newCellY + 3)))
	                {
	                    oldCells.add(cellData.cellList[tempX][tempY]);
	                }
	            }
	        }

//	        System.out.println("now:"+tX2+" "+tY2);
	        for (int i = -2; i < 3; i++)
	        {
	            for (int j = -2; j < 3; j++)
	            {
	                int tx = newCellX + i;
	                int ty = newCellY + j;
	                if (cellData.isValidCell(tx, ty))
//	                        && !(tx > (oldCellX - 3) 
//	                        && tx < (oldCellX + 3) 
//	                        && ty > (oldCellY - 3) 
//	                        && ty < (oldCellY + 3)))
	                {
	                    newCells.add(cellData.cellList[tx][ty]);
	                }
	            }
	        }

	        // 先删除，后广播
	        Cell oldCell = cellData.cellList[oldCellX][oldCellY];
	        if (oldCell != this.cell)
	        {
	        	if(this.cell == null){
	        		return false;
	        	}
	        	PrintUtil.print("oldCell is not uniformity, old:" + oldCell.X + ":"
	                    + oldCell.Y + ";realold:" + this.cell.X + ":"
	                    + this.cell.Y);
	            return false;
	        }
	        
	        //先移除，后广播
	        oldCell = this.cell; // 以这个为准，比较精确
	        oldCell.removeLiving(this,false);
	        if (this instanceof Fighter && this.type == Entity.MOVER_TYPE_PLAYER)
	        {
	        	//通知客户端移除老Cell生物
//	        	cellData.notifyClientRemoveLivings((Fighter)this, oldCells);
	        }
	        
	        byte[] disappearBytes=buildMoverDisAppearBytes();
	        
	        //通知老Cell移出生物
	        for (Cell cell : oldCells)
	        {
	            cell.onDisappear(this,disappearBytes,newCell,centerOldCell);
	        }
	        Fighter thisFighter =(Fighter) this;
	        thisFighter.changeX(1f*newX/Entity.BASE_NUMBER);
	        thisFighter.changeY(1f*newY/Entity.BASE_NUMBER);
	        thisFighter.changeZ(1f*newZ/Entity.BASE_NUMBER);
//	        System.out.println("new  x y z "+thisFighter.name+" "+thisFighter.x+" "+thisFighter.y+" "+thisFighter.z);

			byte[] addResults=buildMoverAppearBytes();
			addResults =MMOByteEncoder.buildSocketPackage(addResults,ServerPack.SPECIAL_ORDER_ID_BATTLE);

	        // 先广播，后增加
	        //通知新Cell添加生物
	        for (Cell cell : newCells)
	        {
	            cell.onAppear(this,addResults,newCell);
	        }
	        if (this instanceof Fighter && this.realPlayer())
	        {
	        	
//	    		List<Entity> list = mapRoom.buildEntityListByAllNpcAndNearByCell(cellData.cellList[cX][cY],5,true);
	        	
	        	
	        	List<Entity> list= new ArrayList<Entity>();
	            if (newCells!=null && newCells.size() > 0)
	            {
	                for (Cell cell : newCells)
	                {
	                	list.addAll(cell.getAllLivings());
	                }
	            }
	        	//通知客户端添加新Cell生物
	        	cellData.sendNearByContextToPlayer((Fighter)this, list);
	        }
//	        Cell newCell = cellData.cellList[tX2][tY2];
	        newCell.addLiving(this);
	        return true;
	}

	public byte[] buildMoverAppearBytes() {
		byte[] moverBytes = ((Fighter)this).fetchMoverStatus();
		byte[] results = new byte[moverBytes.length+5];
		byte[] headers =BattleMsgUtil.appendBattleHeader();
		results[0]=headers[0];
		results[1]=headers[1];
		results[2]=headers[2];
		results[3]=headers[3];
		results[4]=BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING;
		for(int i=0;i<moverBytes.length;i++){
			results[5+i]=moverBytes[i];
		}
		return results;
	}

	
	private byte[] buildMoverDisAppearBytes() {
        MonsterPo monsterPo = MonsterPo.findEntity(itemId);
        ByteArrayOutputStream out=new ByteArrayOutputStream(16);
        if(monsterPo != null && monsterPo.getMonsterType().intValue() == MonsterType.MONSTER_TYPE8){
            try {
				BattleMsgUtil.appendBattleHeader(out);
				out.write(BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT);
				out.write(ByteUtil.int2Byte(1));
				out.write(ByteUtil.int2Byte(mapUniqId));
			} catch (IOException e) {
				ExceptionUtil.processException(e);
			}
        	return MMOByteEncoder.buildSocketPackage(out.toByteArray(),ServerPack.SPECIAL_ORDER_ID_BATTLE); 
        }else{
            try {
				BattleMsgUtil.appendBattleHeader(out);
				out.write(BattleMsgUtil.BATTLE_MSG_TYPE_DISAPPEAR_LIVING);
				out.write(ByteUtil.int2Byte(mapUniqId));
			} catch (IOException e) {
				ExceptionUtil.processException(e);
			}
            return MMOByteEncoder.buildSocketPackage(out.toByteArray(), ServerPack.SPECIAL_ORDER_ID_BATTLE);
        }
	}
}
