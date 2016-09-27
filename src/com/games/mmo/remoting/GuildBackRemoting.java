package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.games.mmo.po.GuildPo;
import com.games.mmo.vo.GuildInvitionVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;

@Controller
public class GuildBackRemoting {
	/**
	 * 获取公会列表
	 * @return
	 */
	public List<GuildInvitionVo> getAllGuild() {
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id,name,lv,battle_power,leader_role_id,leader_role_name,member_count,auto_join from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild order by battle_power desc");
		List<GuildInvitionVo> guildInvitionVos = new ArrayList<GuildInvitionVo>();
		Iterator it = rows.iterator();
		while(it.hasNext()) {
			Map userMap = (Map) it.next();  
			GuildInvitionVo guildInvitionVo=new GuildInvitionVo();
			guildInvitionVo.guildBattlePower=Integer.valueOf(userMap.get("battle_power").toString());
			guildInvitionVo.guildId=Integer.valueOf(userMap.get("id").toString());
			guildInvitionVo.guildLeaderName=userMap.get("leader_role_name").toString();
			guildInvitionVo.guildLeaderRoleId=Integer.valueOf(userMap.get("leader_role_id").toString());
			guildInvitionVo.guildLv=Integer.valueOf(userMap.get("lv").toString());
			guildInvitionVo.guildMemberCount=Integer.valueOf(userMap.get("member_count").toString());
			guildInvitionVo.guildName=userMap.get("name").toString();
			guildInvitionVo.guildAutoJoin=Integer.valueOf(userMap.get("auto_join").toString());
			guildInvitionVos.add(guildInvitionVo);
		}
		return guildInvitionVos;
	}
	/**
	 * 修改公会名称
	 * @return 是否成功
	 */
	public boolean rename(Integer id, String newName) {
		GuildPo guildPo = GuildPo.findEntity(id);
		if (guildPo == null) {
			return false;
		}
		guildPo.setName(newName);
		BaseDAO.instance().syncToDB(guildPo);
		return true;
	}
	/**
	 * 获得公会信息
	 * @return
	 */
	public GuildInvitionVo getGuild(Integer id){
		GuildPo guildPo = GuildPo.findEntity(id);
		GuildInvitionVo guildInvitionVo = new GuildInvitionVo();
		guildInvitionVo.guildId = guildPo.getId();
		guildInvitionVo.guildName = guildPo.getName();
		guildInvitionVo.guildLv = guildPo.getLv();
		guildInvitionVo.guildBattlePower = guildPo.getBattlePower();
		guildInvitionVo.guildLeaderName = guildPo.getLeaderRoleName();
		guildInvitionVo.guildLeaderRoleId = guildPo.getLeaderRoleId();
		guildInvitionVo.guildMemberCount = guildPo.getMemberCount();
		guildInvitionVo.guildAutoJoin = guildPo.getAutoJoin();
		guildInvitionVo.guildCreatedTime = guildPo.getCreatedTime();
		guildInvitionVo.guildGold = guildPo.getGold();
		guildInvitionVo.guildBoard = guildPo.getBoard();
		guildInvitionVo.guildEvents = guildPo.getEvents();
		guildInvitionVo.guildMembers = guildPo.getMembers();
		guildInvitionVo.loadGuildEvents();
		guildInvitionVo.loadGuildMemberVos();
		return guildInvitionVo;
	}
}
