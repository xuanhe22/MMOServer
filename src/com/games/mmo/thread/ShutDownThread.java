package com.games.mmo.thread;

import org.springframework.context.annotation.Bean;

import com.games.mmo.service.ServerService;
import com.storm.lib.util.BeanUtil;

public class ShutDownThread extends Thread{
	public boolean clean=false;
	@Override
	public void run() {
		setName("ShutDownThread");
		ServerService serverService = (ServerService) BeanUtil.getBean("serverService");
		serverService.stopService(clean);
	}
	

}
