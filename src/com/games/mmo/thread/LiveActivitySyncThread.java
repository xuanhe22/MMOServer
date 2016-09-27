package com.games.mmo.thread;

import com.games.mmo.cache.GlobalCache;
import com.storm.lib.util.ExceptionUtil;

public class LiveActivitySyncThread extends Thread{

	@Override
	public void run() {
		setName("LiveActivitySyncThread");
		while(true){
			try {
				GlobalCache.syncActiveLiveActivitys();
			} catch (Exception e1) {
				ExceptionUtil.processException(e1);
			}
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				
			}
		}
	}

	
}
