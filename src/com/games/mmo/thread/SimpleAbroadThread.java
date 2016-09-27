package com.games.mmo.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.po.AbroadPo;
import com.games.mmo.service.ServerService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;

public class SimpleAbroadThread extends Thread{

	public static CopyOnWriteArrayList<AbroadPo> simpleAbroads= new CopyOnWriteArrayList<AbroadPo>();
	
	public void run() {
		setName("SimpleAbroadThread");
		while(true){
			try {
				try {
					checkAbroads();
				} catch (Exception e) {
					ExceptionUtil.processException(e);
				}
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
		}
	}

	private void checkAbroads() {
		for (AbroadPo abroadPo : simpleAbroads) {
			if(abroadPo.getTimeType()==1){
				if(System.currentTimeMillis()>=abroadPo.getStartTime() && System.currentTimeMillis()<=abroadPo.getEndTime()){
					long deltaMs = System.currentTimeMillis()-abroadPo.lastExecuteTime;
					if(deltaMs>=abroadPo.getRepeatMinutes()*1000l){
						abroadPo.executeAbroad();
						abroadPo.lastExecuteTime=System.currentTimeMillis();
					}
				}
			}
		}
	}
}
