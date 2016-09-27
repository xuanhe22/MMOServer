package com.games.mmo.thread;

import java.util.Date;

import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.cell.CellData;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.storm.lib.component.remoting.BaseSocketBusinessThread;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.NetType;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

public class CleanCacheTaskThread extends Thread{
	public static int index=0;
	@Override
	public void run() {
		setName("CleanCacheTaskThread");
		while(true){
			try {
				index++;
				//每过4秒
				if(index%4==0){
					Cell.countMap.clear();
				}
				//每过2秒
				if(index%2==0){
					BattleMsgUtil.msgCheckTime.clear();
				}
				//每过1分钟
				if(index%60==0){
					int onlineNum = RoleTemplate.instance().roleIdIuidMapping.size();
					int nearFilter =10;
					int times=100;
					if(BaseStormSystemType.developMode()){
						times=1;
					}
//					if(onlineNum<=2*times){
//						nearFilter=25;
//					}
//					else if(onlineNum<=5*times){
//						nearFilter=20;
//					}
//					else if(onlineNum<=10*times){
//						nearFilter=15;
//					}
//					else{
//						nearFilter=10;
//					}
//					NetType.FILTER_NEAR_LIMIT=nearFilter;
//					NetType.FILTER_FAR_LIMIT=nearFilter*5/2;
//					PrintUtil.print("广播过滤设置为:near"+NetType.FILTER_NEAR_LIMIT+" far:"+NetType.FILTER_FAR_LIMIT+" 当前人数:"+onlineNum);
				}
				//每过10分钟
				if(index%600==0){
					if(ByteUtil.intCache.size()>100000){
						ByteUtil.intCache.clear();
					}
					if(ByteUtil.stringCache.size()>20000){
						ByteUtil.stringCache.clear();
					}
				}
				//每过半小时
				if(index%1800==0){
					PrintUtil.print("清理CellData.cellCache:"+CellData.cellCache.size());
					CellData.cellCache.clear();
					BaseSocketBusinessThread.requestCache.clear();
				}
				//每过一个小时
				if(index%3600==0){
					PrintUtil.print("清理MapWorld.mapRooms"+MapWorld.mapRooms.size());
					for (Integer roomId : MapWorld.mapRooms.keySet()) {
						try {
							MapRoom mapRoom = MapWorld.findStage(roomId);
							if(mapRoom==null){
								MapWorld.removeMapRoom(roomId);
							}
							else {
								if(mapRoom.isDynamic){
									if(mapRoom.createdTime!=0 && (System.currentTimeMillis()-mapRoom.createdTime)>=(1000*3600*2)){
										PrintUtil.print("强制销毁房间:"+mapRoom.getClass().getName()+" "+new Date().toLocaleString()+" "+roomId);
										mapRoom.destroyMeRoom(false);
									}
								}
							}
						} catch (Exception e) {
							ExceptionUtil.processException(e);
						}
					}
					PrintUtil.print("清理后的MapWorld.mapRooms"+MapWorld.mapRooms.size());
				}

			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
		}
	}
}
