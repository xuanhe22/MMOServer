package com.games.mmo.vo;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.storm.lib.base.BasePropertyVo;

public class SummaryHourPo extends BasePropertyVo{
	private static final long serialVersionUID = 7497906944534220226L;
	private Integer id;
	private Integer type;
	private String channel_key;
	private Long summary_time;
	private Integer hour_user_old;
	private Integer hour_user_new;
	private Integer pu;
	private Integer pu_new;
	private Integer charge_currency;
	private Integer remain;
	private Integer total;
	
	public static String getTableName()
	{
		return "summary_hour";
	}
	
	public SummaryHourPo(SqlRowSet rs){
		this.id = rs.getInt(1);
		this.type = rs.getInt(2);
		this.channel_key = rs.getString(3);
		this.summary_time = rs.getLong(4);
		this.hour_user_old = rs.getInt(5);
		this.hour_user_new = rs.getInt(6);
		this.pu = rs.getInt(7);
		this.pu_new = rs.getInt(8);
		this.charge_currency = rs.getInt(9);
		this.remain = rs.getInt(10);
		this.total = rs.getInt(11);
	}
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{};
	}
	@Override
	public void loadProperty(String val, String spliter) {
	}
	
	public static String getProperyStr()
	{
		return "id,type,channel_key,summary_time,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,total";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getChannel_key() {
		return channel_key;
	}

	public void setChannel_key(String channel_key) {
		this.channel_key = channel_key;
	}

	public Long getSummary_time() {
		return summary_time;
	}

	public void setSummary_time(Long summary_time) {
		this.summary_time = summary_time;
	}

	public Integer getPu() {
		return pu;
	}

	public void setPu(Integer pu) {
		this.pu = pu;
	}

	public Integer getPu_new() {
		return pu_new;
	}

	public void setPu_new(Integer pu_new) {
		this.pu_new = pu_new;
	}

	public Integer getCharge_currency() {
		return charge_currency;
	}

	public void setCharge_currency(Integer charge_currency) {
		this.charge_currency = charge_currency;
	}

	public Integer getHour_user_old() {
		return hour_user_old;
	}

	public void setHour_user_old(Integer hour_user_old) {
		this.hour_user_old = hour_user_old;
	}

	public Integer getHour_user_new() {
		return hour_user_new;
	}

	public void setHour_user_new(Integer hour_user_new) {
		this.hour_user_new = hour_user_new;
	}

	public Integer getRemain() {
		return remain;
	}

	public void setRemain(Integer remain) {
		this.remain = remain;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
