package com.games.backend.vo;

import java.io.Serializable;


public class HourKpiVo implements Serializable {
	private static final long serialVersionUID = 1L;
	public Integer type;
	public Long summary_time;
	public String channel_key;
	public Integer hour_user_old;
	public Integer hour_user_new;
	public Integer pu;
	public Integer pu_new;
	public Integer charge_currency;
	public Integer remain;
	public Integer total;
	public HourKpiVo() {
	}
	public HourKpiVo(Integer type, Long summary_time, String channel_key,
			Integer hour_user_old, Integer hour_user_new, Integer pu,
			Integer pu_new, Integer charge_currency, Integer remain,
			Integer total) {
		super();
		this.type = type;
		this.summary_time = summary_time;
		this.channel_key = channel_key;
		this.hour_user_old = hour_user_old;
		this.hour_user_new = hour_user_new;
		this.pu = pu;
		this.pu_new = pu_new;
		this.charge_currency = charge_currency;
		this.remain = remain;
		this.total = total;
	}

	@Override
	public HourKpiVo clone() {
		return new HourKpiVo(type, summary_time, channel_key, hour_user_old, hour_user_new, pu, pu_new, charge_currency, remain, total);
	}
	public HourKpiVo add(HourKpiVo hourKpiVo) {
		this.hour_user_old = this.hour_user_old + hourKpiVo.hour_user_old;
		this.hour_user_new = this.hour_user_new + hourKpiVo.hour_user_new;
		this.pu = this.pu + hourKpiVo.pu;
		this.pu_new = this.pu_new + hourKpiVo.pu_new;
		this.charge_currency = this.charge_currency + hourKpiVo.charge_currency;
		this.remain = this.remain + hourKpiVo.remain;
		this.total = this.total + hourKpiVo.total;
		return this;
	}
}


