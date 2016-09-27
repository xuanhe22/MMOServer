package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

/**
 * 活动信息Vo
 * @author peter
 *
 */
public class ActivityInfoVo extends BasePropertyVo {

	/**
	 * 累计登录天数
	 */
	public Integer cumulativeLoginDays = 1;
	
	/**
	 * 累计登录领奖时间
	 */
	public Long takeCumulativeLoginTime = 0l;
	
	/**
	 *  同一天在线时间
	 */
	public Long theSameDayOnlineTime=0l;
	
	/**
	 * 领取在线奖励时间
	 */
	public Long takeOnlineAwardTime = 0l;
	
	/**
	 * 同一天幸运转盘次数
	 */
	public Integer sameDayLuckyWheelNumberOfFree = 0;
	
	/**
	 * 下一次免费领取幸运转盘时间
	 */
	public Long takeLuckyWheelFreeNextTime = System.currentTimeMillis();
	
	/**
	 * 签到奖励次数
	 */
	public Integer signInAwardCount = 0;
	
	/**
	 * 同一天是否领取签到奖励
	 */
	public Integer signInAwardSameDayIsTake = 0;
	
	/**
	 * 每日活跃任务完成积分
	 */
	public Integer dailyLivelyTaskFinishScore = 0;
	
	/**
	 *  每日金币膜拜状态 0:没有膜拜; 1:已经膜拜 
	 */
	public Integer dailyWorshipGoldStatus = 0;
	/**
	 *  每日钻石膜拜状态 0:没有膜拜; 1:已经膜拜 
	 */
	public Integer dailyWorshipDiamondStatus = 0;
	
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{cumulativeLoginDays,
										  takeCumulativeLoginTime,
										  theSameDayOnlineTime,
										  takeOnlineAwardTime,
										  sameDayLuckyWheelNumberOfFree,
										  takeLuckyWheelFreeNextTime,
										  signInAwardCount,
										  signInAwardSameDayIsTake,
										  dailyLivelyTaskFinishScore,
										  dailyWorshipGoldStatus,
										  dailyWorshipDiamondStatus};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		cumulativeLoginDays=DBFieldUtil.fetchImpodInt(vals[0]);
		takeCumulativeLoginTime=DBFieldUtil.fetchImpodLong(vals[1]);
		theSameDayOnlineTime=DBFieldUtil.fetchImpodLong(vals[2]);
		takeOnlineAwardTime=DBFieldUtil.fetchImpodLong(vals[3]);
		sameDayLuckyWheelNumberOfFree=DBFieldUtil.fetchImpodInt(vals[4]);
		takeLuckyWheelFreeNextTime=DBFieldUtil.fetchImpodLong(vals[5]);
		signInAwardCount = DBFieldUtil.fetchImpodInt(vals[6]);
		signInAwardSameDayIsTake = DBFieldUtil.fetchImpodInt(vals[7]);
		dailyLivelyTaskFinishScore = DBFieldUtil.fetchImpodInt(vals[8]);
		dailyWorshipGoldStatus = DBFieldUtil.fetchImpodInt(vals[9]);
		dailyWorshipDiamondStatus = DBFieldUtil.fetchImpodInt(vals[10]);
	}
	
	
	/**
	 * 调节累计登录天数
	 * @param day
	 */
	public void adjustCumulativeLoginDays(Integer day){
		if(day == null){
			return;
		}
		cumulativeLoginDays = cumulativeLoginDays +day.intValue();
		if(cumulativeLoginDays.intValue() > 7){
			cumulativeLoginDays = 1;
		}
	}
	
	public void adjustTheSameDayOnlineTime(Long time){
		if(time == null|| time < 0){
			return;
		}
		theSameDayOnlineTime = theSameDayOnlineTime + time.longValue();
	}

	public void adjustSameDayLuckyWheelNumberOfFree(Integer i){
		if(i < 0){
			return;
		}
		sameDayLuckyWheelNumberOfFree = sameDayLuckyWheelNumberOfFree +i;
	}
	
	public void adjustSignInAwardCount(Integer i){
		if(i < 0){
			return;
		}
		signInAwardCount = signInAwardCount +i;
	}
	

	
	
	
}
