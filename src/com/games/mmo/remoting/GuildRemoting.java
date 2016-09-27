package com.games.mmo.remoting;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.map.activity.GuildPriestRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.GuildType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.GuildApplyVo;
import com.games.mmo.vo.GuildInvitionVo;
import com.games.mmo.vo.GuildLandOwnerVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.SiegeTimeVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Buildings.Building;
import com.games.mmo.vo.xml.ConstantFile.Guild.Establish;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildwar.Territory;
import com.games.mmo.vo.xml.ConstantFile.Guild.PriestFresh.FreshQuality;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.QuartzUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.LongStringVo;
import com.storm.lib.vo.TimeExpVo;
@Controller
public class GuildRemoting extends BaseRemoting{
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private CheckService checkService;
	@Autowired
	private ChatService chatService;
	/**
	 * 创建公会
	 * @param guildName
	 * @return
	 */
	public Object createGuild(String guildName){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.createGuild";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.getGuildId().intValue() != 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key82"));
		}
		if(guildName == null && "".equals(guildName)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key83"));
		}
		rolePo.checkGuildExitTime();
		guildName=CheckUtil.checkIllegelName(guildName);
		CheckUtil.checkValidString(guildName, 12);
		CheckUtil.checkContianFiltedWord(guildName, true,null);

		Establish establish =XmlCache.xmlFiles.constantFile.guild.establish;
		
		if(rolePo.getLv().intValue() < establish.lv.intValue() ){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key84"));
		}
		
		if(roleDAO.existGuildName(guildName)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2261"));
		}
		
		if(establish.gold.intValue() > 0){
			rolePo.publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,establish.gold);
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -establish.gold, GlobalCache.fetchLanguageMap("key2477"), "");			
		}
		UserPo user=UserPo.findEntity(rolePo.getUserId());
		if(establish.diamond.intValue() >0 && (rolePo.getBindDiamond()+user.getDiamond())>=establish.diamond){
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -establish.diamond, GlobalCache.fetchLanguageMap("key2478"), "");			
		}
		rolePo.checkHasAndConsumeBindGoldThenGold(establish.gold);
		rolePo.checkHasAndConsumeDiamond(establish.diamond);
		GuildPo guildPo = new GuildPo();
		guildPo.setCreatedTime(System.currentTimeMillis());
		guildPo.setGold(20000);
		guildPo.setLeaderRoleId(rolePo.getId());
		guildPo.setLeaderRoleName(rolePo.getName());
		guildPo.setLv(1);
		guildPo.setMemberCount(1);
		guildPo.setName(guildName);
		guildPo.setAutoJoin(1);
		guildPo.setGuildBossFlushTime(DateUtil.fetchTimesWeekSat());
		guildPo.setBoard(GlobalCache.fetchLanguageMap("key2299"));

		BaseDAO.instance().insert(guildPo);
		
		String sb = MessageFormat.format(GlobalCache.fetchLanguageMap("key2635"), rolePo.getName());
		guildPo.addMember(rolePo.getId());

		 for (Building building: XmlCache.xmlFiles.constantFile.guild.buildings.building) {
			 IdNumberVo idNumberVo = new IdNumberVo();
			 idNumberVo.setId(building.id);
			 idNumberVo.setNum(1);
			 guildPo.listBuildings.add(idNumberVo);
		 }
//		 System.out.println(guildPo.listBuildings);
		 
		 //加载公会背包
		String contribute = XmlCache.xmlFiles.constantFile.guild.contribute.items;
		List<List<Integer>> listContribute = ExpressUtil.buildBattleExpressList(contribute);
		for(List<Integer> list : listContribute){
			guildPo.listItemPack.add(new IdNumberVo(list.get(0), 0));
		}
		
		List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
		
		for(Boss boss : listBoss){
			if(boss.lv == 1){
				guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 1, 0));				
			}else{
				guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 0, 0));	
			}
		}
		
		
		chatService.sendHorse(sb);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateGuildInfor();
		SessionUtil.addDataArray(guildPo);
		LogUtil.writeLog(rolePo, 213, 0, 0, 0, GlobalCache.fetchLanguageMap("key2479")+guildName, "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获得公会信息
	 * @return
	 */
	public Object getGuild(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.getGuild";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("getGuild() rolePo.name =  "+rolePo.getName() );
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.setMemberCount(guildPo.listMembers.size());
		guildPo.setBattlePower(guildPo.fetchGuildBattlePower());
//		System.out.println("公会加入信息"+guildPo.listApplys);
		SessionUtil.addDataArray(guildPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 公会成员信息
	 * @return
	 */
	public Object getGuildMembers(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.getGuildMembers";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<GuildMemberVo> listMembers=new ArrayList<GuildMemberVo>();
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		listMembers = guildPo.fetchListMembers();
		SessionUtil.addDataArray(listMembers);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 邀请成员到公会
	 * @param name
	 * @return
	 */
	public Object invitePlayerToGuild(String name){
//		System.out.println("name =="+name);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.invitePlayerToGuild";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		// 判断有没有用户
		Integer targetRoleId = roleDAO.findRoleIdByName(name);
//		System.out.println("targetRoleId==" +targetRoleId);
		if(targetRoleId!=null){
			RolePo targetRolePo = RolePo.findEntity(targetRoleId);
			if(targetRolePo.getLv().intValue() < 30){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2324"));
			}
			
			targetRolePo.checkGuildExitTime();
			// 判断用户是否在线
			if(targetRolePo.fetchRoleOnlineStatus())
			{
				
				if(targetRolePo.getGuildId().intValue() != 0){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key82"));
				}
				
				for(GuildInvitionVo giv : targetRolePo.listGuildInvitions){
					if(giv.guildId.intValue() == rolePo.getGuildId()){
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key86"));
					}
				}

				int roleJoinGouldStatus = targetRolePo.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_AUTO_JOIN_GUILD);
//				System.out.println("roleJoinGouldStatus == "+roleJoinGouldStatus);
				if(roleJoinGouldStatus == 1)
				{
					guildPo.addMember(targetRolePo.getId());
					targetRolePo.sendUpdateGuildInfor();
					rolePo.sendUpdateGuildPoMembersInfo(guildPo.fetchListMembers());
				}
				else
				{
					GuildInvitionVo guildInvitionVo=new GuildInvitionVo();
					guildInvitionVo.guildBattlePower=0;
					guildInvitionVo.guildId=rolePo.getGuildId();
					guildInvitionVo.guildLeaderName=rolePo.getName();
					guildInvitionVo.guildLeaderRoleId=rolePo.getId();
					guildInvitionVo.guildLv=guildPo.getLv();
					guildInvitionVo.guildMemberCount=guildPo.listMembers.size();
					guildInvitionVo.guildName=guildPo.getName();
					guildInvitionVo.guildAutoJoin = guildPo.getAutoJoin();
					targetRolePo.listGuildInvitions.add(guildInvitionVo);
					targetRolePo.sendUpdateGuildInvititions();
					StringBuffer sb = new StringBuffer();
					sb.append(GlobalCache.fetchLanguageMap("key87")).append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key88")).append(guildPo.getName()).append(GlobalCache.fetchLanguageMap("key89"));
					rolePo.sendUpdateShowMsg(sb.toString());			
				}
				
			}
			else
			{
				StringBuffer sb = new StringBuffer();
				sb.append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key79"));
				rolePo.sendUpdateShowMsg(sb.toString());
			}
		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key78"));
		}
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 申请加入公会
	 * @param type 1:公会Id ； 2目标玩家Id
	 * @param guildIdOrRoleId
	 * @return
	 */
	public Object applyToJoinGuild(Integer type, Integer guildIdOrRoleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.applyToJoinGuild";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkGuildExitTime();	
		if(rolePo.getGuildId() != null &&  rolePo.getGuildId().intValue()!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key90"));
		}
		Integer guildPoId = 0;
		if(type == 1){
			guildPoId = guildIdOrRoleId;
		}
		else if(type == 2)
		{
			checkService.checkExisRolePo(guildIdOrRoleId);
			RolePo targetRole = RolePo.findEntity(guildIdOrRoleId);
			if(targetRole.getGuildId() == null || targetRole.getGuildId().intValue() == 0){
				ExceptionUtil.throwConfirmParamException(targetRole.getName()+GlobalCache.fetchLanguageMap("key91"));
			}
			guildPoId = targetRole.getGuildId();
		}
		
		checkService.checkExisGuildPo(guildPoId);
		GuildPo guildPo = GuildPo.findEntity(guildPoId);
		if(guildPo.getAutoJoin().intValue() == 1){
			guildPo.addMember(rolePo.getId());
			rolePo.sendUpdateGuildInfor();
			rolePo.sendUpdateGuildPoMembersInfo(guildPo.fetchListMembers());
		}else{
			
			for(GuildApplyVo gav : guildPo.listApplys){
				if(gav.roleId.intValue() == rolePo.getId()){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key92"));
				}
			}
			
			GuildApplyVo guildApplyVo = new GuildApplyVo();
			guildApplyVo.roleBattlePower=0;
			guildApplyVo.roleCareer=rolePo.getCareer();
			guildApplyVo.roleId=rolePo.getId();
			guildApplyVo.roleLv=rolePo.getLv();
			guildApplyVo.roleName=rolePo.getName();
			guildPo.listApplys.add(guildApplyVo);
			StringBuffer sb = new StringBuffer();
			sb.append(GlobalCache.fetchLanguageMap("key93")).append(guildPo.getName()).append(GlobalCache.fetchLanguageMap("key94"));
			rolePo.sendUpdateShowMsg(sb.toString());
		}
		
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}


	/**
	 * 公会事件
	 * @return
	 */
	public Object guildEvents(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildEvents";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SessionUtil.checkSessionLost(rolePo);
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listEvents);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 公会列表
	 * @return
	 */
	public Object guildList(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
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
//		System.out.println(guildInvitionVos);
		SessionUtil.addDataArray(guildInvitionVos);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 公会申请列表
	 * @return
	 */
	public Object guildApplyList(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildApplyList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listApplys);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 批准加入公会
	 * @param roleId
	 * @return
	 */
	public Object guildAcceptApplys(Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildAcceptApplys";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		checkService.checkExisRolePo(roleId);
		RolePo targetRole = RolePo.findEntity(roleId);
		if(targetRole.getGuildId() != null &&  targetRole.getGuildId().intValue()!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key82"));
		}
		for (GuildApplyVo guildApplyVo : guildPo.listApplys) {
			if(guildApplyVo.roleId.intValue() == roleId.intValue()){
				guildPo.addMember(guildApplyVo.roleId);
				guildPo.listApplys.remove(guildApplyVo);
				break;
			}
		}
		rolePo.sendUpdateGuildPoMembersInfo(guildPo.fetchListMembers());
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listApplys);
		LogUtil.writeLog(rolePo, 226, roleId, 0, 0, GlobalCache.fetchLanguageMap("key2481"), "");
		return SessionType.MULTYPE_RETURN;
	}
	

	/**
	 * 全部批准加入公会
	 * @param roleId
	 * @return
	 */
	public Object guildAcceptAllApplys(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildAcceptAllApplys";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		for(GuildApplyVo guildApplyVo : guildPo.listApplys){
			checkService.checkExisRolePo(guildApplyVo.roleId);
		}
		for (GuildApplyVo guildApplyVo : guildPo.listApplys) {
			RolePo targetRole = RolePo.findEntity(guildApplyVo.roleId);
			// 已经有公会的跳过
			if(targetRole.getGuildId() != null &&  targetRole.getGuildId().intValue()!=0){
				continue;
			}
			guildPo.addMember(guildApplyVo.roleId);
			LogUtil.writeLog(rolePo, 226, guildApplyVo.roleId, 0, 0, GlobalCache.fetchLanguageMap("key2480"), "");
		}
		guildPo.listApplys.clear();
		rolePo.sendUpdateGuildPoMembersInfo(guildPo.fetchListMembers());
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listApplys);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 拒绝加入公会
	 * @param roleId
	 * @return
	 */
	public Object guildDeclineApplys(Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildDeclineApplys";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		checkService.checkExisRolePo(roleId);
		RolePo targetRole = RolePo.findEntity(roleId);
		checkService.checkExisRolePo(roleId);
		for (GuildApplyVo guildApplyVo : guildPo.listApplys) {
			if(guildApplyVo.roleId.intValue() == roleId.intValue()){
				guildPo.listApplys.remove(guildApplyVo);
				StringBuffer sb = new StringBuffer();
				sb.append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key95")).append(guildPo.getName()).append(GlobalCache.fetchLanguageMap("key96"));
				targetRole.sendUpdateShowMsg(sb.toString());
				break;
			}
		}
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listApplys);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 玩家是否同意加入公会
	 * @param guildId
	 * @param agree 1：加入公会 
	 * @return
	 */
	public Object guildPlayerProcessInvite(Integer guildId,Integer agree){
		CheckUtil.checkIsNull(agree);
		CheckUtil.checkIsNull(guildId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildPlayerProcessInvite";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		for (GuildInvitionVo guildInvitionVo : rolePo.listGuildInvitions) {
			if(guildInvitionVo.guildId==guildId.intValue()){
				if(agree==1)
				{
					checkService.checkExisGuildPo(guildId);
					GuildPo guildPo = GuildPo.findEntity(guildId);
					guildPo.addMember(rolePo.getId());
					rolePo.sendUpdateGuildPoMembersInfo(guildPo.fetchListMembers());
				}
				else
				{
					RolePo targetRole = RolePo.findEntity(guildInvitionVo.guildLeaderRoleId);
					StringBuffer sb = new StringBuffer();
					sb.append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key97")).append(guildInvitionVo.guildName).append(GlobalCache.fetchLanguageMap("key98"));
					targetRole.sendUpdateShowMsg(sb.toString());
				}
				 rolePo.listGuildInvitions.remove(guildInvitionVo);
				 break;
			}
		}
		
		SessionUtil.addDataArray(rolePo.listGuildInvitions);
		SessionUtil.addDataArray(agree);
		SessionUtil.addDataArray(GuildPo.findEntity(guildId));
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 全部拒绝加入公会
	 * @return
	 */
	public Object guildDeclineAllApplys(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildDeclineAllApplys";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		for (GuildApplyVo guildApplyVo : guildPo.listApplys) {
			RolePo targetRolePo = RolePo.findEntity(guildApplyVo.roleId);
			StringBuffer sb = new StringBuffer();
			sb.append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key95")).append(guildPo.getName()).append(GlobalCache.fetchLanguageMap("key96"));
			targetRolePo.sendUpdateShowMsg(sb.toString());
		}
		guildPo.listApplys.clear();
		SessionUtil.addDataArray(GuildPo.findEntity(rolePo.getGuildId()).listApplys);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 退出公会
	 * @return
	 */
	public Object guildExit(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildExit";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		if(guildPo.getLeaderRoleId().intValue() == rolePo.getId().intValue())
		{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key99"));
		}
		guildPo.removeMember(rolePo.getId(), GuildType.GUILD_NOTICE_TYPE_EXIT);
		SessionUtil.addDataArray(1);
		LogUtil.writeLog(rolePo, 227, 0, 0, 0, GlobalCache.fetchLanguageMap("key2483")+guildPo.getName(), "");
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 解散公会
	 * @return
	 */
	public Object guildDimiss(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildDimiss";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		if(guildPo.getLeaderRoleId().intValue() != rolePo.getId().intValue())
		{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key100"));
		}
		LogUtil.writeLog(rolePo, 215, 0, 0, 0, GlobalCache.fetchLanguageMap("key2482")+guildPo.getName(), "");
		guildPo.dimiss();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 兑换道具
	 * @param itemId
	 * @return
	 */
	public Object guildExchangeItem(Integer itemId){
		CheckUtil.checkIsNull(itemId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildExchangeItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExistItemPo(itemId);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildExchangeItem(itemId,rolePo);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateGuildInfor();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 捐献物品
	 * @param itemId
	 * @return
	 */
	public Object guildContributeItems(Integer itemId, Integer num){
		CheckUtil.checkIsNull(itemId);
		CheckUtil.checkIsNull(num);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildContributeItems";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExistItemPo(itemId);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		if(num < 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key146"));
		}
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildContributeItems(itemId, rolePo, num);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateGuildInfor();
		SessionUtil.addDataArray(guildPo.listItemPack);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 升级建筑
	 * @param buildingId
	 * @return
	 */
	public Object guildUpgradeBuilding(Integer buildingId){
		CheckUtil.checkIsNull(buildingId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildUpgradeBuilding";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildUpgradeBuilding( buildingId, rolePo);
		rolePo.sendUpdateGuildInfor();
		SessionUtil.addDataArray(guildPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 捐献金币
	 * @param times
	 * @return
	 */
	public Object guildContributeGold(Integer num){
		CheckUtil.checkIsNull(num);
		if(num == null || num.intValue() <=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key109")+"num："+num);
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildContributeGold";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildContributeGold(num, rolePo);
//		System.out.println("guildPo.getGold()="+guildPo.getGold());
		rolePo.sendUpdateGuildInfor();
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(guildPo.getGold());
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 使用公会技能
	 * @param buildingId
	 * @return
	 */
	public Object guildBuildingSkill(Integer buildingId){
		CheckUtil.checkIsNull(buildingId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildBuildingSkill";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildBuildingSkill( buildingId, rolePo);
		rolePo.sendUpdateGuildInfor();
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 公会公告编辑
	 * @return
	 */
	public Object guildNoticeEdit(String notice){
		CheckUtil.checkIsNull(notice);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildNoticeEdit";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		notice=CheckUtil.checkIllegelName(notice);
		CheckUtil.checkValidString(notice, 40);
		CheckUtil.checkContianFiltedWord(notice, false,null);

		guildPo.setBoard(notice);
		SessionUtil.addDataArray(guildPo.getBoard());
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 自动添加会员
	 * @param autoStatus
	 * @return
	 */
	public Object guildAutoJoin(Integer autoStatus){
		CheckUtil.checkIsNull(autoStatus);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.setAutoJoin(autoStatus);
		SessionUtil.addDataArray(guildPo.getAutoJoin());
		return SessionType.MULTYPE_RETURN;
	}
	

	
	
	/**
	 * 会员任命
	 * @param roldId 玩家Id
 	 * @param position 职位
	 * @return
	 */
	public Object guildAppoint(Integer targetRoleId, Integer position){
		CheckUtil.checkIsNull(targetRoleId);
		CheckUtil.checkIsNull(position);
		if(position == null || position.intValue() < 1 ||  position.intValue() > 4){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key113")+position);
		}
		checkService.checkExisRolePo(targetRoleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildAppoint";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		guildPo.guildAppoint( targetRoleId,  position,rolePo);
		SessionUtil.addDataArray(guildPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 公会踢人
	 * @return
	 */
	public Object guildExpelled(Integer targetRoleId){
		CheckUtil.checkIsNull(targetRoleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildExpelled";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		GuildMemberVo gmv = guildPo.fetchGuildMemberVoInfo(rolePo.getId());
		GuildMemberVo targetGmv = guildPo.fetchGuildMemberVoInfo(targetRoleId);
		if(gmv == null || targetGmv == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"GuildMemberVo："+targetRoleId);
		}
		if(gmv.guildPosition.intValue() >= targetGmv.guildPosition.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key114"));
		}
		guildPo.removeMember(targetRoleId, GuildType.GUILD_NOTICE_TYPE_EXPELLED);
		SessionUtil.addDataArray(guildPo.listMembers);
		LogUtil.writeLog(rolePo, 228, targetRoleId, 0, 0, GlobalCache.fetchLanguageMap("key2484"), "");
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 领土信息
	 * @return
	 */
	public Object guildMapLandOwners(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildMapLandOwners";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<GuildLandOwnerVo> list = new ArrayList<GuildLandOwnerVo>();
		for (FlagPo flagPo : GlobalCache.sceneIdFlagMap.values()) {
			GuildLandOwnerVo guildLandOwnerVo=new GuildLandOwnerVo();
			guildLandOwnerVo.sceneId=flagPo.getSceneId();
			GuildPo guildPo = GuildPo.findEntity(flagPo.getGuildId());
			if(guildPo!=null){
				guildLandOwnerVo.guildName=GuildPo.findEntity(flagPo.getGuildId()).getName();
				guildLandOwnerVo.guildId=flagPo.getGuildId();
			}
			else{
				guildLandOwnerVo.guildName=GlobalCache.fetchLanguageMap("key117");
				guildLandOwnerVo.guildId=0;
			}
			list.add(guildLandOwnerVo);
		}
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object guildSiege(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildSiege";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
		SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
		siegeBidVo.awards= XmlCache.xmlFiles.constantFile.guild.guildwar.territory.get(5).award;
		SessionUtil.addDataArray(siegeBidVo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 沙巴克竞拍
	 * @param slot 1~3
	 * @return 
	 */
	public Object guildBidSiege(Integer slot){
		CheckUtil.checkIsNull(slot);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildBidSiege";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_GUILD_BID);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2302"));
		}
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
		SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
		if(slot==1){
			
			siegeBidVo.maxBid1=siegeBidVo.maxBid1+1000;
			siegeBidVo.guildId1=guildPo.getId();
			siegeBidVo.guildName1=guildPo.getName();
			if(guildPo.getBidCount1().intValue() == 0){
				guildPo.checkHasAndConsumeGold(siegeBidVo.maxBid1);
				guildPo.addBidGold(siegeBidVo.maxBid1);
				guildPo.addBidCount1(1);
			}else{
				guildPo.checkHasAndConsumeGold(1000);
				guildPo.addBidGold(1000);
			}
		}
		if(slot==2){
			siegeBidVo.maxBid2=siegeBidVo.maxBid2+1000;
			siegeBidVo.guildId2=guildPo.getId();
			siegeBidVo.guildName2=guildPo.getName();
			if(guildPo.getBidCount2().intValue() == 0){
				guildPo.checkHasAndConsumeGold(siegeBidVo.maxBid2);
				guildPo.addBidGold(siegeBidVo.maxBid2);
				guildPo.addBidCount2(1);
			}else{
				guildPo.checkHasAndConsumeGold(1000);
				guildPo.addBidGold(1000);
			}
		}
		if(slot==3){
			siegeBidVo.maxBid3=siegeBidVo.maxBid3+1000;
			siegeBidVo.guildId3=guildPo.getId();
			siegeBidVo.guildName3=guildPo.getName();
			if(guildPo.getBidCount3().intValue() == 0){
				guildPo.checkHasAndConsumeGold(siegeBidVo.maxBid3);
				guildPo.addBidGold(siegeBidVo.maxBid3);
				guildPo.addBidCount3(1);
			}else{
				guildPo.checkHasAndConsumeGold(1000);
				guildPo.addBidGold(1000);
			}
		}		
		
		siegeBidVo.save();
		SessionUtil.addDataArray(siegeBidVo);
		SessionUtil.addDataArray(guildPo.getGold());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 沙巴克领奖
	 * @param slot 1~3
	 * @return 
	 */
	public Object guildAwardSiege(){
//		System.out.println("guildAwardSiege()");
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildAwardSiege";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(DateUtil.sameDay(System.currentTimeMillis(), rolePo.getSiegeLastAwardTime())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key70"));
		}
		
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_SIEGE_WAR);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2303"));
		}
		rolePo.guildAwardSiege();
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.setSiegeLastAwardTime(System.currentTimeMillis());
		return 1;
	}
	
	/**
	 * 领地领奖
	 * @param slot
	 * @return
	 */
	public Object guildAwardOwner(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.guildAwardOwner";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
	
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_GUILD_WAR);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2304"));
		}
		rolePo.guildAwardOwner();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateMainPack(false);
		rolePo.setDomainLastAwardTime(System.currentTimeMillis());
		return 1;
	}
	
	/**
	 * 领取公会boss奖励
	 * @param copysceneconfId
	 * @return
	 */
	public Object fetchGuildBossAward(Integer copysceneconfId){
		CheckUtil.checkIsNull(copysceneconfId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_GuildRemoting.fetchGuildBossAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());	
		guildPo.fetchGuildBossAward(copysceneconfId,rolePo);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.listGuildBossAward);
		return 1;
	}
	
	/**
	 * 刷新公会祭祀
	 * @return
	 */
	public Object flushGuildPriest(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("flushGuildPriest() rolePo.name = " +rolePo.getName() + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		String key = rolePo.getId() +"_GuildRemoting.flushGuildPriest";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		GuildMemberVo guildMemberVo = guildPo.fetchGuildMemberVoInfo(rolePo.getId());
//		System.out.println("guildMemberVo.guildPosition "+guildMemberVo.guildPosition);
		if(!(guildMemberVo.guildPosition == 1 || guildMemberVo.guildPosition == 2)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2675"));
		}
		if (!CheckService.checkRequireDailyFresh(guildPo.getPriestFreshStartTime())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2676"));
		}
		
		
		int freeTimes = XmlCache.xmlFiles.constantFile.guild.priestFresh.freeTimes;
		int cost = XmlCache.xmlFiles.constantFile.guild.priestFresh.cost;
		FreshQuality freshQuality = null;
		if(guildPo.getPriestFreshCount().intValue() > freeTimes){
			guildPo.checkHasAndConsumeGold(cost);
			freshQuality = guildPo.fecthFreshQuality(1);
		}else{
			freshQuality = guildPo.fecthFreshQuality(0);
		}
		guildPo.adjustPriestFreshCount(1);
		guildPo.setPriestFreshQuality(freshQuality.id);
		
		if(freshQuality.id > 3){
			String str = GlobalCache.fetchLanguageMap("key2677");
			String content = MessageFormat.format(str, guildPo.getName() , ColourType.fetchColourByQualityStr(freshQuality.id));
			chatService.sendHorse(content);			
		}
		
		SessionUtil.addDataArray(guildPo.getPriestFreshCount());
		SessionUtil.addDataArray(guildPo.getPriestFreshQuality());
		SessionUtil.addDataArray(guildPo.getGold());
		return 1;
	}
	
	/**
	 * 开启公会祭司
	 * @return
	 */
	public Object startGuildPriest(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("startGuildPriest() rolePo.name = " +rolePo.getName() + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		String key = rolePo.getId() +"_GuildRemoting.startGuildPriest";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisGuildPo(rolePo.getGuildId());
		GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
		GuildMemberVo guildMemberVo = guildPo.fetchGuildMemberVoInfo(rolePo.getId());
		
		if(!(guildMemberVo.guildPosition == 1 || guildMemberVo.guildPosition == 2)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2678"));
		}
//		System.out.println(" == " +guildPo.getPriestFreshStartTime());
//		System.out.println(" == " + DateUtil.getFormatDateBytimestamp(guildPo.getPriestFreshStartTime()));
		if(DateUtil.sameDay(guildPo.getPriestFreshStartTime(), System.currentTimeMillis())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2679"));
		}

//		System.out.println("guildPo.getPriestFreshStartTime() =" +  DateUtil.getFormatDateBytimestamp(guildPo.getPriestFreshStartTime()));
//		System.out.println("guildPo.getPriestFreshState() = " + guildPo.getPriestFreshState());
		guildPo.setPriestFreshStartTime(System.currentTimeMillis());
		guildPo.setPriestFreshState(1);
		ScenePo scenePo = ScenePo.findEntity(GuildPriestRoom.SCENE_ID);
		CopySceneConfPo copySceneConfPo = CopySceneConfPo.findEntity(CopySceneType.COPY_SCENE_CONF_GUILD_PRIEST);
		MapRoom mapRoom = MapWorld.createGuildPoDynalicMapRoom(copySceneConfPo,scenePo, guildPo.getId());
		String content = GlobalCache.fetchLanguageMap("key2680");
		guildPo.writeEvent(content);
		chatService.sendGuild(content, guildPo.getId());
		for(GuildMemberVo gmv : guildPo.listMembers){
			RolePo target = RolePo.findEntity(gmv.roleId);
			if(target!=null && target.fetchRoleOnlineStatus()){
				if(target.getGuildPriestState().intValue() != 2){
					target.sendUpdateGuildPriestStart(guildPo);					
				}
				chatService.sendSystemMsg(content, target.getId());
			}
		}
		
		SessionUtil.addDataArray(String.valueOf(guildPo.getPriestFreshStartTime()));
		SessionUtil.addDataArray(String.valueOf(guildPo.getPriestFreshState()));
		return 1;
	}

}

















