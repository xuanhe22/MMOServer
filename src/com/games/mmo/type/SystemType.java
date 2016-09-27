package com.games.mmo.type;

import java.util.Properties;

import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.PropertyUtil;

public class SystemType extends BaseStormSystemType {
	public static void additionalTask(){
		Properties properties = fetchPropertiesFile();
		BaseStormSystemType.GAME_SOCKET_PORT= Integer.valueOf(PropertyUtil.getValueIfNotNull(properties, "game_socket_port", BaseStormSystemType.GAME_SOCKET_PORT));
		BaseStormSystemType.ALLOW_CHEAT =PropertyUtil.propertyBoolean(properties,"allow_cheat");
		BaseStormSystemType.ALLOW_CONFUSE =PropertyUtil.propertyBoolean(properties,"allow_confuse");
		BaseStormSystemType.ALLOW_BAIDU_VOICE=PropertyUtil.propertyBoolean(properties,"allow_baidu_voice");
	}
	
	public static boolean showdebug=false;
}
