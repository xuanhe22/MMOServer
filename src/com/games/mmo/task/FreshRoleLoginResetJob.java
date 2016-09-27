package com.games.mmo.task;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.vo.SessionSaveVo;
import com.storm.lib.component.socket.netty.NettyType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;

public class FreshRoleLoginResetJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
		for (SessionSaveVo sessionSaveVo : GlobalCache.sessionSaveVos.values()) {
			ChannelHandlerContext session = roleTemplate.getSessionById(sessionSaveVo.id);
			if(session==null){
				continue;
			}
			Integer roleId = ((AttributeMap) session).attr(NettyType.roleId).get();
			if(roleId!=null || GlobalCache.sessionSaveVos.containsKey(roleId)){
				continue;
			}
			RolePo rolePo = RolePo.findEntity(roleId);	
			rolePo.checkLoginReset();
		}
	}

}
