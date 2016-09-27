package com.games.mmo.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeMap;

import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.storm.lib.component.socket.netty.GameNettySocketHandler;
import com.storm.lib.component.socket.netty.NettyType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.ExceptionUtil;
@Controller
public class SocketHandler extends GameNettySocketHandler{

	
    
	@Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			if(ctx.attr(NettyType.roleId)!=null){
				Integer roleId = ctx.attr(NettyType.roleId).get();
				if(roleId!=null && GlobalCache.sessionSaveVos.containsKey(roleId)){
					GlobalCache.sessionSaveVos.get(roleId).lastMsgTime=System.currentTimeMillis();
				}
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		super.channelRead(ctx,msg);
    }

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
		if(!GlobalCache.inited){
			ctx.channel().disconnect();
		}
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

}
