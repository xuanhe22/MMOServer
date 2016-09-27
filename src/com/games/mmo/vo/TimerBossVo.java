package com.games.mmo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

/**
 * 定时boss
 * @author Administrator
 *
 */
public class TimerBossVo extends BasePropertyVo {

	/**
	 * 击杀次数
	 */
	public Integer killNum = 0 ;
	
	/**
	 * boss状态 0：死了 ； 1：活着
	 */
	public Integer bossState = 1;
	
	/**
	 * 击杀时间
	 */
	@JSONField(serialize = false)
	public Long killTime = 0L;
	
	/**
	 * 击杀公会id
	 */
	public Integer killGuildId = 0;
	
	/**
	 * 击杀玩家Id
	 */
	@JSONField(serialize = false)
	public Integer killRoleId = 0;
	
	/**
	 * 击杀公会名
	 */
	public String killGuildName = GlobalCache.fetchLanguageMap("key2276");
								  
	
	/**
	 * 击杀人
	 */
	@JSONField(serialize = false)
	public String killName = GlobalCache.fetchLanguageMap("key2276");
	
	
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{killNum,bossState,killTime,killGuildId,killRoleId,killGuildName,killName};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		killNum=DBFieldUtil.fetchImpodInt(vals[0]);
		bossState=DBFieldUtil.fetchImpodInt(vals[1]);
		killTime=DBFieldUtil.fetchImpodLong(vals[2]);
		killGuildId=DBFieldUtil.fetchImpodInt(vals[3]);
		killRoleId=DBFieldUtil.fetchImpodInt(vals[4]);
		killGuildName=DBFieldUtil.fetchImpodString(vals[5]);
		killName = DBFieldUtil.fetchImpodString(vals[6]);
	}
	
	public void adjustkillNum(int num){
		if( num < 0){
			return;
		}
		killNum = killNum + num;
	}

	@Override
	public String toString() {
		return "TimerBossVo [killNum=" + killNum + ", bossState=" + bossState
				+ ", killTime=" + killTime + ", killGuildId=" + killGuildId
				+ ", killRoleId=" + killRoleId + ", killGuildName="
				+ killGuildName + ", killName=" + killName + "]";
	}
	
	
}
