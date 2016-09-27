package com.games.mmo.util;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.component.socket.netty.NettyType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.ThreadLocalType;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.ThreadLocalUtil;

public class SessionUtil extends BaseSessionUtil{
	
//	public static List<IoSession> getAllLogindSessions() {
//		RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
//		List<IoSession> allsesions =new ArrayList<IoSession>();
//		Map map=roleTemplate.iuidSessionMapping;
//		Iterator iter = map.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry entry = (Map.Entry) iter.next();
//			IoSession session = (IoSession) entry.getValue();
//			allsesions.add(session);
//		}
//		return allsesions;
//	}
	/**
	 * 方法功能:得到当前session Role
	 * 更新时间:2011-8-29, 作者:johnny
	 * @return
	 */
	public static RolePo getCurrentSessionRole() {
		ChannelHandlerContext session =(ChannelHandlerContext) ThreadLocalUtil.getVar(ThreadLocalType.CURRENT_SESSION);
		Object obj = session.attr(NettyType.rolePo).get();
		return (RolePo) obj;
	}

	public static void checkSessionLost(RolePo rolePo) {
		if(rolePo==null){
			ChannelHandlerContext session =(ChannelHandlerContext) ThreadLocalUtil.getVar(ThreadLocalType.CURRENT_SESSION);

			BasePushTemplate pushTemplate = BasePushTemplate.instance();
			pushTemplate.singleSession("PushRemoting.sendSessionLost", session, new Object[]{},null,true);
			
			ExceptionUtil.throwConfirmParamException(null);
		}

	}


	

}
