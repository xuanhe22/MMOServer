package com.games.mmo.mapserver.bean;

import com.storm.lib.util.ExceptionUtil;

public class ActivityMapRoomThread extends Thread{
	public DynamicMapRoom fightMapRoom;
	public ActivityMapRoomThread(DynamicMapRoom fightMapRoom){
		this.fightMapRoom=fightMapRoom;
	}
	
	public void checkDoCloseAction(){
		if(fightMapRoom.status==fightMapRoom.ACTIVITY_STATUS_CLOSE){
			fightMapRoom.onGameOver();
		}
	}
	
	public void run(){
		setName("ActivityMapRoomThread");
		while(true){
			try {
				Thread.sleep(5*1000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
			try {
				if(fightMapRoom==null || fightMapRoom.status==DynamicMapRoom.ACTIVITY_STATUS_DESTROYING){
					break;
				}
				coreRunning();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
				continue;
			}

		}
	}
	public void coreRunning(){
		fightMapRoom.update();
	}
	
}
