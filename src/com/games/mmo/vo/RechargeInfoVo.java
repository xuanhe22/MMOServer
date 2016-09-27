package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class RechargeInfoVo extends BasePropertyVo{

	/**
	 * 是否是月卡
	 */
	public Integer wasMonthCard = 0;
	/**
	 * 月卡充值开始时间
	 */
	public Long monthCardRechargeBeginTime = 0l;
	/**
	 * 剩余月卡天数
	 */
	public Integer remainMonthCardDay = 0;
	/**
	 * 今天领取月卡时间
	 */
	public Long todayTakeMonthCardTime = 0l;
	/**
	 * 今天是否领取了月卡
	 */
	public Integer todayWasTakeMonthCard = 0;
	/**
	 * 累计充值数量
	 */
	public Integer cumulativeRechargeNum = 0;
	
	/**
	 * 是否首次充值
	 */
	public Integer wasFirstRecharge = 0;
	
	/**
	 * 是否领取首次充值奖励
	 */
	public Integer wasTakeFirstRechargeAwards = 0;
	
	/**
	 * 是否领取每日vip奖励
	 */
	public Integer wasTakeDailyVipAward = 0;

	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{
				wasMonthCard,
				monthCardRechargeBeginTime,
				remainMonthCardDay,
				todayTakeMonthCardTime,
				todayWasTakeMonthCard,
				cumulativeRechargeNum,
				wasFirstRecharge,
				wasTakeFirstRechargeAwards,
				wasTakeDailyVipAward
		};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);	
		if(fetchProperyItems().length != vals.length){
			return;
		}
		wasMonthCard = DBFieldUtil.fetchImpodInt(vals[0]);
		monthCardRechargeBeginTime = DBFieldUtil.fetchImpodLong(vals[1]);
		remainMonthCardDay = DBFieldUtil.fetchImpodInt(vals[2]);
		todayTakeMonthCardTime = DBFieldUtil.fetchImpodLong(vals[3]);
		todayWasTakeMonthCard = DBFieldUtil.fetchImpodInt(vals[4]);
		cumulativeRechargeNum = DBFieldUtil.fetchImpodInt(vals[5]);
		wasFirstRecharge =DBFieldUtil.fetchImpodInt(vals[6]);
		wasTakeFirstRechargeAwards = DBFieldUtil.fetchImpodInt(vals[7]);
		wasTakeDailyVipAward = DBFieldUtil.fetchImpodInt(vals[8]);
	}

	/**
	 * 调整累计充值数量
	 * @param i
	 */
	public void adjustCumulativeRechargeNum(Integer i){
		if(i == null|| i < 0){
			return;
		}
		cumulativeRechargeNum = cumulativeRechargeNum + i.intValue();
	}
	
	/**
	 * 调整月卡剩余天数
	 * @param i
	 */
	public void adjustRemainMonthCardDay(Integer i){
		remainMonthCardDay = remainMonthCardDay + i.intValue();
		if(remainMonthCardDay < 0){
			remainMonthCardDay = 0;
		}
		if(remainMonthCardDay == 0 ){
			wasMonthCard = 0;
		}
	}
	
}
