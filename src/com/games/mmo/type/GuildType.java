package com.games.mmo.type;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.vo.xml.ConstantFile.Global;

public class GuildType {
	/**
	 * 公会成员类型： 会长
	 */
	public static final int GUILD_POSITION_CHAIRMAN = 1;
	
	/**
	 * 公会成员类型： 副会长
	 */
	public static final int GUILD_POSITION_VICE_CHAIRMAN = 2;
	
	/**
	 * 公会成员类型： 长老
	 */
	public static final int GUILD_POSITION_PRESBYTER = 3;
	
	/**
	 * 公会成员类型： 会员
	 */
	public static final int GUILD_POSITION_MEMBER = 4;
	
	
	public static String  fetchNameByMemeberType(Integer position){
		String str = GlobalCache.fetchLanguageMap("key256");
		if(position.intValue() == GUILD_POSITION_CHAIRMAN)
		{
			str = GlobalCache.fetchLanguageMap("key257");
		}
		else if(position.intValue() == GUILD_POSITION_VICE_CHAIRMAN)
		{
			str = GlobalCache.fetchLanguageMap("key258");
		}
		else if(position.intValue() == GUILD_POSITION_PRESBYTER)
		{
			str = GlobalCache.fetchLanguageMap("key259");
		}
		else if(position.intValue() == GUILD_POSITION_MEMBER)
		{
			str = GlobalCache.fetchLanguageMap("key256");
		}
		
		return str;
	}
	
	/**
	 * 公会公告类型：退出公会
	 */
	public static final int GUILD_NOTICE_TYPE_EXIT = 1;
	
	/**
	 * 公会公告类型：公会踢人
	 */
	public static final int GUILD_NOTICE_TYPE_EXPELLED = 2;
	
}
