package com.games.backend.vo;

import java.util.Date;

import com.storm.lib.util.DateUtil;

public class MonthKpiVo {
	public Long summary_time;
	public String channel_key;
	public Integer new_user_role_new;
	public Integer new_user_role_exist;
	public Integer dau;
	public Integer day_user_old;
	public Integer day_user_new;
	public Integer pu;
	public Integer pu_new;
	public Integer pu_percentage;
	public Integer darpu;
	public Integer darppu;
	public Integer charge_currency;
	public Integer consume_diamond;
	public MonthKpiVo(DayKpiVo dayKpiVo) {
		summary_time = dayKpiVo.summary_time;
		channel_key = dayKpiVo.channel_key;
		new_user_role_new = dayKpiVo.new_user_role_new;
		new_user_role_exist = dayKpiVo.new_user_role_exist;
		dau = dayKpiVo.dau;
		day_user_old = dayKpiVo.day_user_old;
		day_user_new = dayKpiVo.day_user_new;
		pu = dayKpiVo.pu;
		pu_new = dayKpiVo.pu_new;
		pu_percentage = dayKpiVo.pu_percentage;
		darpu = dayKpiVo.darpu;
		darppu = dayKpiVo.darppu;
		charge_currency = dayKpiVo.charge_currency;
		consume_diamond = dayKpiVo.consume_diamond;
	}
	public Object[] toObjs() {
		String date=DateUtil.formatDate(new Date(summary_time-50000),"yyyy_MM_dd");
		return new Object[]{date,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,1d*pu_percentage/100+"%",1d*darpu/100,1d*darppu/100,1d*charge_currency/100,consume_diamond};
	}
}
