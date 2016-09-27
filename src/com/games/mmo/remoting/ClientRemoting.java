package com.games.mmo.remoting;

import org.springframework.stereotype.Controller;

import com.storm.lib.base.BaseRemoting;

@Controller
public class ClientRemoting extends BaseRemoting{

	public Object fetchServerList(String channel,String clientVersion){
		
		return 1;
	}
}
