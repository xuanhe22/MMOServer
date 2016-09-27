package com.games.mmo.vo;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.storm.lib.base.BasePropertyVo;

public class SummaryDayPo extends BasePropertyVo{
	private static final long serialVersionUID = -2541430505545626364L;
	private Integer id;
	private Integer type;
	private String channel_key;
	private Long summary_time;
	private Integer new_user_role_new;
	private Integer new_user_role_exist;
	private Integer dau;
	private Integer day_user_old;
	private Integer day_user_new;
	private Integer pu;
	private Integer pu_new;
	private Integer pu_percentage;
	private Integer darpu;
	private Integer darppu;
	private Integer charge_currency;
	private Integer consume_diamond;
	private Integer avg_online;
	private Integer max_online;
	private Integer remain_diamond;
	
	public static String getTableName()
	{
		return "summary_day";
	}
	
	public SummaryDayPo(SqlRowSet rs){
		this.id = rs.getInt(1);
		this.type = rs.getInt(2);
		this.channel_key = rs.getString(3);
		this.summary_time = rs.getLong(4);
		this.new_user_role_new = rs.getInt(5);
		this.new_user_role_exist = rs.getInt(6);
		this.dau = rs.getInt(7);
		this.day_user_old = rs.getInt(8);
		this.day_user_new = rs.getInt(9);
		this.pu = rs.getInt(10);
		this.pu_new = rs.getInt(11);
		this.pu_percentage = rs.getInt(12);
		this.darpu = rs.getInt(13);
		this.darppu = rs.getInt(14);
		this.charge_currency = rs.getInt(15);
		this.consume_diamond = rs.getInt(16);
		this.avg_online = rs.getInt(17);
		this.max_online = rs.getInt(18);
		this.remain_diamond = rs.getInt(19);
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
		return "id,type,channel_key,summary_time,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,pu_percentage,darpu,darppu,charge_currency,consume_diamond,avg_online,max_online,remain_diamond";
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

	public Integer getNew_user_role_new() {
		return new_user_role_new;
	}

	public void setNew_user_role_new(Integer new_user_role_new) {
		this.new_user_role_new = new_user_role_new;
	}

	public Integer getNew_user_role_exist() {
		return new_user_role_exist;
	}

	public void setNew_user_role_exist(Integer new_user_role_exist) {
		this.new_user_role_exist = new_user_role_exist;
	}

	public Integer getDau() {
		return dau;
	}

	public void setDau(Integer dau) {
		this.dau = dau;
	}

	public Integer getDay_user_old() {
		return day_user_old;
	}

	public void setDay_user_old(Integer day_user_old) {
		this.day_user_old = day_user_old;
	}

	public Integer getDay_user_new() {
		return day_user_new;
	}

	public void setDay_user_new(Integer day_user_new) {
		this.day_user_new = day_user_new;
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

	public Integer getPu_percentage() {
		return pu_percentage;
	}

	public void setPu_percentage(Integer pu_percentage) {
		this.pu_percentage = pu_percentage;
	}

	public Integer getDarpu() {
		return darpu;
	}

	public void setDarpu(Integer darpu) {
		this.darpu = darpu;
	}

	public Integer getDarppu() {
		return darppu;
	}

	public void setDarppu(Integer darppu) {
		this.darppu = darppu;
	}

	public Integer getCharge_currency() {
		return charge_currency;
	}

	public void setCharge_currency(Integer charge_currency) {
		this.charge_currency = charge_currency;
	}

	public Integer getConsume_diamond() {
		return consume_diamond;
	}

	public void setConsume_diamond(Integer consume_diamond) {
		this.consume_diamond = consume_diamond;
	}

	public Integer getAvg_online() {
		return avg_online;
	}

	public void setAvg_online(Integer avg_online) {
		this.avg_online = avg_online;
	}

	public Integer getMax_online() {
		return max_online;
	}

	public void setMax_online(Integer max_online) {
		this.max_online = max_online;
	}

	public Integer getRemain_diamond() {
		return remain_diamond;
	}

	public void setRemain_diamond(Integer remain_diamond) {
		this.remain_diamond = remain_diamond;
	}
}
