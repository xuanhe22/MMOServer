package com.games.mmo.thread;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.service.RoleService;
import com.games.mmo.vo.SessionSaveVo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
/**
 * 
 * 类功能:检查session保存进度
 * @author johnny
 * @version 2014-6-27
 */
public class CheckSessionSaveThread extends Thread{

	@Override
	public void run() {
		setName("CheckSessionSaveThread");
		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
		RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
		
		while(true){
			try {

				List<Integer> toRmoveKeys = new ArrayList<Integer>();
				for (SessionSaveVo sessionSaveVo : GlobalCache.sessionSaveVos.values()) {
					if(System.currentTimeMillis()-sessionSaveVo.lastMsgTime>=(1000*60*2)){
						toRmoveKeys.add(sessionSaveVo.id);
						PrintUtil.print("心跳踢人:"+sessionSaveVo.iuid);
					}
				}
				for (Integer roleId : toRmoveKeys) {
					ChannelHandlerContext session = roleTemplate.getSessionById(roleId);
					roleService.logoff(session,5000);
					if(session!=null){
						((ChannelHandlerContext)session).disconnect();
					}
					GlobalCache.sessionSaveVos.remove(roleId);
				}
			} catch (Exception e1) {
				ExceptionUtil.processException(e1);
			}
			try {	
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
			
		}

	}

	
}
