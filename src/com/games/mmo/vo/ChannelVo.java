package com.games.mmo.vo;

public class ChannelVo {
	public String channelKey;
	public Integer channelValue;
	public ChannelVo(String channelKey, Integer channelValue) {
		super();
		this.channelKey = channelKey;
		this.channelValue = channelValue;
	}
	@Override
	public String toString() {
		return "ChannelVo [channelKey=" + channelKey + ", channelValue="
				+ channelValue + "]";
	}
	
	
}
