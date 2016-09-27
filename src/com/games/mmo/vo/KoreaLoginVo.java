package com.games.mmo.vo;

/**
 * 韩国登录Vo
 * @author Administrator
 *
 */
public class KoreaLoginVo {
	/**
	 * Sdk客户端返回给cp的userId
	 */
	public String userId;
	/**
	 * token Sdk客户端返回给cp的token
	 */
	public String token;
	/**
	 * cpId 合作伙伴id(来源： 签约后由平台发送给合作伙伴)
	 */
	public String cpId;
	/**
	 * appId 游戏id(来源：同cpId)
	 */
	public String appId;
	/**
	 * Sdk客户端返回给cp的channelId
	 */
	public String channelId;
	/**
	 * Sdk客户端返回的channelAction,用于组拼检测登录的URL
	 */
	public String channelAction;
	/**
	 * 检测登录扩展字段，用于适配渠道
	 */
	public String extInfo;
	
	
	public KoreaLoginVo(String userId, String token, String cpId, String appId,
			String channelId, String channelAction, String extInfo) {
		super();
		this.userId = userId;
		this.token = token;
		this.cpId = cpId;
		this.appId = appId;
		this.channelId = channelId;
		this.channelAction = channelAction;
		this.extInfo = extInfo;
	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=").append(userId);
		sb.append("&token=").append(token);
		sb.append("&cpId=").append(cpId);
		sb.append("&appId=").append(appId);
		sb.append("&channelId=").append(channelId);
		sb.append("&channelAction=").append(channelAction);
		sb.append("&extInfo=").append(extInfo);
		return  sb.toString();
	}
	
	
	
}
