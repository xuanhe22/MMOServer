package com.games.mmo.vo;

import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * 类功能:断线
 * @author johnny
 * @version 2014-3-21
 */
public class SessionSaveVo {
	public ChannelHandlerContext session;
	/**
	 * session属性
	 */
//	public ConcurrentHashMap<String, Object>  sessionAttrs = new ConcurrentHashMap<String, Object>();
	/**
	 * 账号
	 */
	public String iuid;
	
	public Integer id;
	/**
	 * 最近断线时间
	 */
	public Long lastMsgTime;
}
