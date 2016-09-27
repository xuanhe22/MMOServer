package com.games.backend.vo;

import java.io.Serializable;
import java.util.Date;

import com.storm.lib.util.DateUtil;


public class DayKpiVo implements Serializable{
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
	public Integer avg_online;
	public Integer max_online;
	public Integer remainDiamond;

	public Object[] toObjs() {
		String date=DateUtil.formatDate(new Date(summary_time-50000),"yyyy_MM_dd");
		return new Object[]{date,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,1d*pu_percentage/100+"%",(1d*darpu/100),(1d*darppu/100),(long)(charge_currency),consume_diamond,avg_online,max_online,remainDiamond};

	}

	public DayKpiVo add(DayKpiVo dayKpiVo) {
		this.new_user_role_new = this.new_user_role_new + dayKpiVo.new_user_role_new;
		this.new_user_role_exist = this.new_user_role_exist + dayKpiVo.new_user_role_exist;
		this.dau = this.dau + dayKpiVo.dau;
		this.day_user_old = this.day_user_old + dayKpiVo.day_user_old;
		this.day_user_new = this.day_user_new + dayKpiVo.day_user_new;
		this.pu = this.pu + dayKpiVo.pu;
		this.pu_new = this.pu_new + dayKpiVo.pu_new;
		this.charge_currency = this.charge_currency + dayKpiVo.charge_currency;
		this.consume_diamond = this.consume_diamond + dayKpiVo.consume_diamond;
		this.avg_online = this.avg_online + dayKpiVo.avg_online;
		this.max_online = this.max_online + dayKpiVo.max_online;
		this.remainDiamond = this.remainDiamond + dayKpiVo.remainDiamond;
		this.pu_percentage = (int)(this.dau == 0 ? 0 : this.pu * 10000.0 / this.dau);
		this.darpu = (int) (this.dau==0 ? 0: this.charge_currency * 100.0 / this.dau);
		this.darppu = (int) (this.pu==0 ? 0 : this.charge_currency * 100.0/ this.pu);
		return this;
	}
	
	
	
}


