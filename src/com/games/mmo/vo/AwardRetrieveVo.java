package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class AwardRetrieveVo extends BasePropertyVo {

	/**  奖励找回Id*/
	public Integer id;
	
	/** 奖励找回类型 */
	public Integer timesType;
	/** 开启等级 */
	public Integer openLv;

	/** 昨天登录时间 */
	public String yesterdayTime ="0";
	/** 昨天次完成次数 */
	public Integer yesterdayFinishCount=0;

	/** 今天登录时间 */
	public String todayTime="0";
	/** 今天完成次数 */
	public Integer todayFinishCount=0;
	
	/** 可以找回次数 */
	public Integer retrieveCount=0;

	/** 是否开启 0：未开启； 1：开启*/
	public Integer wasOpen=0;
	
	/** 基础次数 */
	public Integer baseCount=0;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,
							timesType,
						    openLv,
						    yesterdayTime,
						    yesterdayFinishCount,
						    todayTime,
						    todayFinishCount,
						    retrieveCount,
						    wasOpen,
						    baseCount};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val, spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		id = DBFieldUtil.fetchImpodInt(vals[0]);
		timesType = DBFieldUtil.fetchImpodInt(vals[1]);
		openLv = DBFieldUtil.fetchImpodInt(vals[2]);
		yesterdayTime = DBFieldUtil.fetchImpodString(vals[3]);
		yesterdayFinishCount = DBFieldUtil.fetchImpodInt(vals[4]);
		todayTime = DBFieldUtil.fetchImpodString(vals[5]);
		todayFinishCount =DBFieldUtil.fetchImpodInt(vals[6]);
		retrieveCount = DBFieldUtil.fetchImpodInt(vals[7]);
		wasOpen=DBFieldUtil.fetchImpodInt(vals[8]);
		baseCount=DBFieldUtil.fetchImpodInt(vals[9]);
	}
	
	public void adjustTodayFinishCountCount(Integer i){
		if(i < 0){
			return;
		}
		todayFinishCount = todayFinishCount +i;
	}
	
	public void adjustRetrieveCount(Integer i){
		if(i < 0){
			return;
		}
		retrieveCount = retrieveCount +i;
	}

	@Override
	public String toString() {
		return "AwardRetrieveVo [id=" + id + ", timesType=" + timesType
				+ ", openLv=" + openLv + ", yesterdayTime=" + yesterdayTime
				+ ", yesterdayFinishCount=" + yesterdayFinishCount
				+ ", todayTime=" + todayTime + ", todayFinishCount="
				+ todayFinishCount + ", retrieveCount=" + retrieveCount
				+ ", wasOpen=" + wasOpen + ", baseCount=" + baseCount + "]";
	}


	
	
}
