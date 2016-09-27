package com.games.backend.vo;

import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DateUtil;

public class UserSummaryVo extends BaseVo{
	public Integer user_id;
	public String user_iuid;
	public String channel_key;
	public Integer id;
	public String name;
	public Integer newbie_step_group;
	public Integer lv;
	public Integer exp;
	public Integer gold;
	public Integer bind_gold;
	public Integer diamond;
	public Integer bind_diamond;
	public Integer career;
	public Integer arena_rank;
	public Integer skill_point;
	public Integer achieve_point;
	public Integer prestige;
	public Integer guild_honor;
	public Integer pk_value;
	public Integer daily_lively_task_finish_score;
	public Integer pet_soul;
	public String guild_name;
	public Integer main_task_id;
	public String main_task_name;
	public Long last_login_time;
	public Long last_logoff_time;
	public Long user_created_time;
	public Long create_time;
	public Integer battle_power;
	public Integer total_diamond_charged;
	public Integer first_charge_diamond;
	public Integer first_charge_role_lv;
	public String first_consume_desc;
	public Integer first_consume_role_lv;
	public Integer vip_lv;
	public String idfa;
	public String package_name;
	public String lastLoginIp;
	public String deviceId;
	public Integer serverId;
	
	@Override
	public String toString() {
		
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<tr>");
		sb.append("<td>").append(user_id).append("</td>");
		sb.append("<td>").append(user_iuid).append("</td>");
		sb.append("<td>").append(serverId).append("</td>");
		sb.append("<td>").append(channel_key).append("</td>");
		sb.append("<td>").append(id).append("</td>");
		sb.append("<td>").append(name).append("</td>");
		sb.append("<td>").append(newbie_step_group).append("</td>");
		sb.append("<td>").append(lv).append("</td>");
		sb.append("<td>").append(exp).append("</td>");
		sb.append("<td>").append(gold).append("</td>");
		sb.append("<td>").append(bind_gold).append("</td>");
		sb.append("<td>").append(diamond).append("</td>");
		sb.append("<td>").append(bind_diamond).append("</td>");
		sb.append("<td>").append(career).append("</td>");
		sb.append("<td>").append(arena_rank).append("</td>");
		sb.append("<td>").append(skill_point).append("</td>");
		sb.append("<td>").append(achieve_point).append("</td>");
		sb.append("<td>").append(prestige).append("</td>");
		
		sb.append("<td>").append(guild_honor).append("</td>");
		sb.append("<td>").append(pk_value).append("</td>");
		sb.append("<td>").append(daily_lively_task_finish_score).append("</td>");
		sb.append("<td>").append(pet_soul).append("</td>");
		sb.append("<td>").append(guild_name).append("</td>");
		sb.append("<td>").append(main_task_id).append("</td>");
		sb.append("<td>").append(main_task_name).append("</td>");
		sb.append("<td>").append(DateUtil.getFormatDateBytimestamp(last_login_time)).append("</td>");
		
		sb.append("<td>").append(DateUtil.getFormatDateBytimestamp(last_logoff_time)).append("</td>");
		sb.append("<td>").append(DateUtil.getFormatDateBytimestamp(user_created_time)).append("</td>");
		sb.append("<td>").append(DateUtil.getFormatDateBytimestamp(create_time)).append("</td>");
		sb.append("<td>").append(battle_power).append("</td>");
		
		sb.append("<td>").append(total_diamond_charged).append("</td>");
		sb.append("<td>").append(first_charge_diamond).append("</td>");
		sb.append("<td>").append(first_charge_role_lv).append("</td>");
		sb.append("<td>").append(first_consume_desc).append("</td>");
		sb.append("<td>").append(first_consume_role_lv).append("</td>");
		sb.append("<td>").append(vip_lv).append("</td>");
		sb.append("<td>").append(idfa).append("</td>");
		sb.append("<td>").append(package_name).append("</td>");
		sb.append("<td>").append(lastLoginIp).append("</td>");
		sb.append("<td>").append(deviceId).append("</td>");

		
		sb.append("</tr>");
		return sb.toString();
	}
	
	
	
}
