package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.LongStringVo;

public class GuildInvitionVo extends BasePropertyVo{
	public Integer guildId;
	public String guildName;
	public Integer guildLv;
	public Integer guildBattlePower;
	public String guildLeaderName;
	public Integer guildLeaderRoleId;
	public Integer guildMemberCount;
	public Integer guildAutoJoin;
	public Long guildCreatedTime;
	public Integer guildGold;
	public String guildBoard;
	public String guildEvents;
	public String guildMembers;
	public List<LongStringVo> listEvents = new ArrayList<LongStringVo>();
	public List<GuildMemberVo> listMembers = new ArrayList<GuildMemberVo>();
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{guildId,guildName,guildLv,guildBattlePower,guildLeaderName,guildLeaderRoleId,guildMemberCount,guildAutoJoin};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		guildId=DBFieldUtil.fetchImpodInt(vals[0]);
		guildName=DBFieldUtil.fetchImpodString(vals[1]);
		guildLv=DBFieldUtil.fetchImpodInt(vals[2]);
		guildBattlePower=DBFieldUtil.fetchImpodInt(vals[3]);
		guildLeaderName=DBFieldUtil.fetchImpodString(vals[4]);
		guildLeaderRoleId=DBFieldUtil.fetchImpodInt(vals[5]);
		guildMemberCount=DBFieldUtil.fetchImpodInt(vals[6]);
		guildAutoJoin=DBFieldUtil.fetchImpodInt(vals[7]);
	}
	@Override
	public String toString() {
		return "GuildInvitionVo [guildId=" + guildId + ", guildName="
				+ guildName + ", guildLv=" + guildLv + ", guildBattlePower="
				+ guildBattlePower + ", guildLeaderName=" + guildLeaderName
				+ ", guildLeaderRoleId=" + guildLeaderRoleId
				+ ", guildMemberCount=" + guildMemberCount + ", guildAutoJoin="
				+ guildAutoJoin + "]";
	}
	
	public void loadGuildMemberVos() {
		if(guildMembers==null){
			return;
		}
		String[] items = StringUtil.split(guildMembers,",");
		for (String itemStr : items) {
			GuildMemberVo guildMemberVo = new GuildMemberVo();
			guildMemberVo.loadProperty(itemStr, "|");
			listMembers.add(guildMemberVo);
		}
	}
	public void loadGuildEvents(){
		if(guildEvents == null)
		{
			return;
		}
		listEvents = LongStringVo.createComplexList(guildEvents); 
	}
}
