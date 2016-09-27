package com.games.mmo.remoting;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo2;

@Controller
public class SocialRemoting extends BaseRemoting{
	@Autowired
	private CheckService checkService;	
	@Autowired
	private ChatService chatService;
	/**
	 * 
	 * 方法功能:获取自己队伍信息
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamFetchMyTeamInfor(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamFetchMyTeamInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo==null){	
			SessionUtil.addDataArray(null);
		}
		else{
			rolePo.teamMemberVo.updateTeamMember(rolePo);
			SessionUtil.addDataArray(rolePo.teamMemberVo.teamVo);
		}
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:创建队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamCreateTeam(){ 
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamCreateTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key182"));
		}
		if(rolePo.isPlayingActivityType(CopySceneType.COPY_SCENE_TYPE_KILLING_TOWER)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key183"));
		}
		if(rolePo.isPlayingActivityType(CopySceneType.COPY_SCENE_TYPE_TEAM_DEFEND)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key183"));
		}
		TeamVo teamVo = TeamVo.createTeamWithCaptain(rolePo, 0);
		if(teamVo != null){
			rolePo.setSocialTeamId(teamVo.id);			
		}
		teamVo.sendAllMemberUpdateTeamInfor();
		SessionUtil.addDataArray(1);
		LogUtil.writeLog(rolePo, 349, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:创建队伍并邀请队员
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamCreateTeamWithRole(Integer targetRoleId){ 
		CheckUtil.checkIsNull(targetRoleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamCreateTeamWithRole";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key182"));
		}
		CheckUtil.checkIsNull(targetRoleId);
		RolePo targetRolePo = RolePo.findEntity(targetRoleId);
		if(targetRolePo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key78"));
		}
		if(targetRolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key184"));
		}
		if(rolePo.isPlayingActivityType(CopySceneType.COPY_SCENE_TYPE_KILLING_TOWER)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key183"));
		}
		if(rolePo.isPlayingActivityType(CopySceneType.COPY_SCENE_TYPE_TEAM_DEFEND)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key183"));
		}
		TeamVo teamVo = TeamVo.createTeamWithCaptain(rolePo, 0);
		if(teamVo != null){
			rolePo.setSocialTeamId(teamVo.id);			
		}
		targetRolePo.sendTeamInvitition(rolePo);
		teamVo.sendAllMemberUpdateTeamInfor();
		SessionUtil.addDataArray(1);
		LogUtil.writeLog(rolePo, 349, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:邀请队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamInviteToTeam(Integer targetRoleId){ 
		CheckUtil.checkIsNull(targetRoleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		RolePo targetRolePo = RolePo.findEntity(targetRoleId);
		CheckUtil.checkIsNull(targetRoleId);
		String key = rolePo.getId() +"SocialRemoting.teamInviteToTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(targetRolePo==null){
			return 1;
		}
		if(targetRolePo.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_REFUSAL_TEAM_INVITATION) ==0){
			ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key185"));
		}
		
		if(!targetRolePo.fetchRoleOnlineStatus()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
		
		if(targetRolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key184"));
		}
		if(rolePo.teamMemberVo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key186"));
		}
		targetRolePo.sendTeamInvitition(rolePo);
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:同意加入队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @param teamId
	 * @param 1=同意 2=拒绝
	 * @return
	 */
	public Object teamAgreeJoinTeam(Integer teamId,Integer method,Integer noticeRoleInfor){ 
		CheckUtil.checkIsNull(teamId);
		CheckUtil.checkIsNull(method);
		CheckUtil.checkIsNull(noticeRoleInfor);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamAgreeJoinTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key182"));
		}
		if(method==1){
			TeamVo teamVo = TeamVo.findTeam(teamId, 0);
			if(teamVo == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key187"));
			}
			if(teamVo.teamMemberVos.size() >= 5){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key188"));
			}
			TeamMemberVo teamMemberVo = TeamMemberVo.createTeamMember(rolePo, 0);
			teamVo.addMember(teamMemberVo, 0);
			rolePo.setSocialTeamId(teamVo.id);
			rolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key189"));
			teamVo.sendAllMemberUpdateTeamInfor();
		}
		else{
			RolePo.findEntity(noticeRoleInfor).sendUpdateShowMsg(rolePo.getName()+GlobalCache.fetchLanguageMap("key190"));
		}
		LogUtil.writeLog(rolePo, 349, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:退出队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamLeaveMyTeam(){ 
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamLeaveMyTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key186"));
		}
		rolePo.leaveMyTeam();
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:踢出队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamKickMember(Integer roleId){ 
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamKickMember";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.getId().intValue() == roleId.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key191"));
		}
		
		int teamId = rolePo.teamMemberVo.teamVo.id;
		RolePo targetRolePo = RolePo.findEntity(roleId);
		if(targetRolePo.teamMemberVo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key192"));
		}
		TeamMemberVo teamMemberVo = rolePo.teamMemberVo.teamVo.findTeamMember(roleId);
		rolePo.teamMemberVo.teamVo.removeMember(teamMemberVo, 0);
		
		// 退出组队聊天频道
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		ChannelHandlerContext ioSession = targetRolePo.fetchSession();
		chatService.leaveRoomChannel(teamId, targetRolePo.getIuid(), ioSession, ChatTempate.chatTeamRooms);
		targetRolePo.teamMemberVo = null;
		targetRolePo.setSocialTeamId(null);
		// 广播队员离开信息
		StringBuffer sb = new StringBuffer();
		sb.append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key193"));					
		chatService.sendTeam(sb.toString(), teamId);
		TeamVo.findTeam(teamId, 0).sendAllMemberUpdateTeamInfor();
		targetRolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key194"));
		targetRolePo.sendUpdateTeamInfor(null);
		
		
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:使他成为队长
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamCaptainMember(Integer roleId){ 
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamCaptainMember";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.getId().intValue() == roleId.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key195"));
		}
		
		TeamMemberVo teamMemberVo = rolePo.teamMemberVo.teamVo.findTeamMember(roleId);
		rolePo.teamMemberVo.teamVo.makeCaptain(teamMemberVo);
		TeamVo.findTeam(teamMemberVo.teamVo.id, 0).sendAllMemberUpdateTeamInfor();
		
		// 组队广播
		RolePo targetRolePo = RolePo.findEntity(roleId);
		StringBuffer sb = new StringBuffer();
		sb.append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key196"));					
		chatService.sendTeam(sb.toString(), teamMemberVo.teamVo.id);
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:解散队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamDismissMyTeam(){ 
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamDismissMyTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo==null || rolePo.teamMemberVo.teamVo==null){
			return 1;
		}
		for (TeamMemberVo teamMemberVo : rolePo.teamMemberVo.teamVo.teamMemberVos) {
			RolePo.findEntity(teamMemberVo.roleId).sendUpdateTeamInfor(null);
			RolePo.findEntity(teamMemberVo.roleId).sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key197"));
		}
		rolePo.teamMemberVo.teamVo.dismiss();
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:申请加入队伍
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamApplyToJoinTeam(Integer targetCaptainId){ 
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamApplyToJoinTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		RolePo targetRolePo = RolePo.findEntity(targetCaptainId);
		
		if(targetRolePo.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_REFUSAL_TEAM_INVITATION) ==0){
			ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key185"));
		}
		
		if(rolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key182"));
		}
		if(targetRolePo.teamMemberVo!=null){
			targetRolePo.sendApplyToJoinTeam(rolePo.getId(),rolePo.getName());
		}
		return 1;
	}
	
	/**
	 * 处理队伍加入申请
	 * @param roleId 加入者id
	 * @param way	1=同意 2=不同意
	 * @return
	 */
	public Object teamProcessAgreeJoinApply(Integer roleId,Integer way){ 
		CheckUtil.checkIsNull(roleId);
		CheckUtil.checkIsNull(way);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamProcessAgreeJoinApply";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		RolePo targetRolePo = RolePo.findEntity(roleId);
		if(targetRolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(targetRolePo.getName() +GlobalCache.fetchLanguageMap("key198"));
		}
		TeamVo teamVo = rolePo.teamMemberVo.teamVo;
		if(teamVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key187"));
		}
		if(teamVo.teamMemberVos.size() >= 5){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key188"));
		}
		if(way.intValue() == 1)
		{
			if(targetRolePo.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_REFUSAL_TEAM_INVITATION) ==0){
				ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key185"));
			}
			TeamMemberVo teamMemberVo = TeamMemberVo.createTeamMember(targetRolePo, 0);
			rolePo.teamMemberVo.teamVo.addMember(teamMemberVo, 0);
			targetRolePo.setSocialTeamId(teamVo.id);
			targetRolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key189"));
			TeamVo.findTeam(teamMemberVo.teamVo.id, 0).sendAllMemberUpdateTeamInfor();
			LogUtil.writeLog(targetRolePo, 349, 0, 0, 0, "", "");
		}
		else{
			targetRolePo.sendUpdateShowMsg(rolePo.getName()+GlobalCache.fetchLanguageMap("key199"));
		}
		return 1;
	}
	
	
	/**
	 * 获取附近的队长
	 * @return
	 */
	public Object teamNearByTeam(){ 
//		System.out.println("teamNearByTeam()");
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.teamNearByTeam";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<TeamMemberVo> captains = new ArrayList<TeamMemberVo>();
		if(GlobalCache.teamDungeonIdMaps.get(0)!=null){
			Iterator iter = GlobalCache.teamDungeonIdMaps.get(0).entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				TeamVo teamVo= (TeamVo) entry.getValue();
				//TODO 【业务标记】低优先级-附近的需要及时刷新
				if(teamVo.captain.roleRoomId.intValue()==rolePo.getRoomId()){
					
					if(rolePo.teamMemberVo !=null && rolePo.teamMemberVo.teamVo !=null && teamVo.captain.roleId.intValue()==rolePo.teamMemberVo.teamVo.captain.roleId){
						continue;
					}
					RolePo captainPo = RolePo.findEntity(teamVo.captain.roleId);
					if(captainPo!=null && rolePo.fetchRoleOnlineStatus()){
						captains.add(teamVo.captain);
					}
				}
			}
		}
//		System.out.println("captains="+captains);
		SessionUtil.addDataArray(captains);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 附近玩家
	 * @return
	 */
	public Object nearByFreePlayer(){ 
//		System.out.println("nearByFreePlayer()");
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.nearByFreePlayer";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		MapRoom mapRoom=MapRoom.findStage(rolePo.getRoomId());
		List<TeamMemberVo> avas = new ArrayList<TeamMemberVo>();
		List<Fighter> players = mapRoom.cellData.getAllCellPlayers();
		for (Fighter fighter : players) {
			if(fighter.rolePo!=rolePo){
				TeamMemberVo teamMemberVo =new TeamMemberVo();
				teamMemberVo.isCaptain=0;
				teamMemberVo.isOnline=1;
				teamMemberVo.isRobot=0;
				teamMemberVo.roleBattlePower=fighter.rolePo.getBattlePower();
				teamMemberVo.roleCareer=fighter.rolePo.getCareer();
				teamMemberVo.roleId=fighter.rolePo.getId();
				teamMemberVo.roleLv=fighter.rolePo.getLv();
				teamMemberVo.roleName=fighter.rolePo.getName();
				teamMemberVo.roleRoomId=fighter.rolePo.getRoomId();
				teamMemberVo.batHp=fighter.rolePo.getBatHp();
				teamMemberVo.batMaxHp=fighter.rolePo.getBatMaxHp();
				avas.add(teamMemberVo);
			}
		}
//		System.out.println("avas="+avas);
		SessionUtil.addDataArray(avas);
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object findPlayer(String name){
//		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select * from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where name like '%"+name+"%' order by id");
//		Integer roleId = BaseDAO.instance().jdbcTemplate.q("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where name = '"+name+"'");
//		if(roleId != null && roleId != 0)
//		{
//			RolePo find = RolePo.findEntity(roleId);
//			if(find != null)
//				SessionUtil.addDataArray(find.toRoleViewInforVo());
//		}
		
//		queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where name = '"+name+"'");
//		RolePo findRolePo = RolePo.findEntity(id);
//		findRolePo.
		
		List<RoleInforPo> roleInforPos = new ArrayList<RoleInforPo>();
		Iterator it = rows.iterator();      
		while(it.hasNext()) {
			Map userMap = (Map) it.next();  
			RoleInforPo roleInfoPo = new RoleInforPo();
			roleInfoPo.setBattlePower(Integer.valueOf(userMap.get("battle_power").toString()));
			roleInfoPo.setCareer(Integer.valueOf(userMap.get("career").toString()));
			roleInfoPo.setId(Integer.valueOf(userMap.get("id").toString()));
			roleInfoPo.setLastLoginTime(System.currentTimeMillis());
			roleInfoPo.setRoleId(Integer.valueOf(userMap.get("id").toString()));
			roleInfoPo.setRoleLv(Integer.valueOf(userMap.get("lv").toString()));
			roleInfoPo.setRoleName(userMap.get("name").toString());
			
			if(RoleTemplate.roleIdIuidMapping.containsKey(roleInfoPo.getRoleId())){
				roleInfoPo.onlineStatus=1;
			}
			else{
				roleInfoPo.onlineStatus=0;
			}
			roleInforPos.add(roleInfoPo);
		} 
		SessionUtil.addDataArray(roleInforPos);
		return SessionType.MULTYPE_RETURN;		
	}
	
	public Object addBlock(Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		checkService.checkExisRolePo(roleId); 
		RolePo blockRole = RolePo.findEntity(roleId);
		blockRole.syncToInfor();
		checkService.checkExisRoleInforPo(blockRole.getRoleInforId());
		RoleInforPo roleInfoPo = RoleInforPo.findEntity(blockRole.getRoleInforId());
		for (RoleInforPo roleInforPo : rolePo.listBlocks) {
			if(roleInforPo.getRoleId().intValue()==roleId){
				return 1;
			}
		}
		rolePo.listBlocks.add(roleInfoPo);
		
		for (RoleInforPo friendInforPo : rolePo.listFriends) {
			if(friendInforPo.getRoleId()==roleId.intValue()){
				rolePo.listFriends.remove(friendInforPo);
				break;
			}
		}
		
		rolePo.sendUpdateRelations();
		return SessionType.MULTYPE_RETURN;	
	}

	public Object addEnemy(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SocialRemoting.addEnemy";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisRolePo(roleId); 
		RolePo enemyRole = RolePo.findEntity(roleId);
		enemyRole.syncToInfor();
		checkService.checkExisRoleInforPo(enemyRole.getRoleInforId());
		rolePo.addEnemy(roleId);
		return SessionType.MULTYPE_RETURN;	
	}
	
	public Object updateWasNewEnemy(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SocialRemoting.updateWasNewEnemy";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.setWasNewEnemy(0);
		rolePo.sendUpdateWasNewEnemy();
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 添加好友
	 * @param roleId
	 * @return
	 */
	public Object addFriend(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		checkService.checkExisRolePo(roleId); 
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.addFriend";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		RolePo friendRole = RolePo.findEntity(roleId);
		if(rolePo.listFriends.size() >= 99){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key200"));
		}
		
		if(friendRole.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_REFUSAL_GOOD_FRIEND_INVITATION)==0){
			ExceptionUtil.throwConfirmParamException(friendRole.getName()+GlobalCache.fetchLanguageMap("key201"));
		}
		
		
		if(rolePo.getId().intValue() == friendRole.getId().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key202"));
		}
		for (RoleInforPo roleInforPo : rolePo.listFriends) {
			if(roleInforPo.getRoleId().intValue()==roleId){
				SessionUtil.addDataArray(0);
				return 1;
			}
		}
		friendRole.syncToInfor();
		checkService.checkExisRoleInforPo(friendRole.getRoleInforId());
		RoleInforPo roleInfoPo = RoleInforPo.findEntity(friendRole.getRoleInforId());
		rolePo.listFriends.add(roleInfoPo);
		rolePo.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_725, null, null);
		
		for (Fighter fighter : MapRoom.findStage(rolePo.getRoomId()).cellData.getAllCellPlayers()) {
			if(fighter.rolePo.getId()==roleId.intValue()){
				
				boolean matched=false;
				for (RoleInforPo roleInforPo : fighter.rolePo.listFriends) {
					if(roleInforPo.getRoleId().intValue()==rolePo.getId()){
						matched=true;
					}
				}
				if(!matched){
					fighter.rolePo.sendNearByFriendAddRequest(rolePo.getId(),rolePo.getName());
				}

			}
		}
		for (RoleInforPo friendInforPo : rolePo.listBlocks) {
			if(friendInforPo.getRoleId()==roleId.intValue()){
				rolePo.listBlocks.remove(friendInforPo);
				break;
			}
		}
		
		for (RoleInforPo friendInforPo : rolePo.listEnemys) {
			if(friendInforPo.getRoleId()==roleId.intValue()){
				rolePo.listEnemys.remove(friendInforPo);
				break;
			}
		}
		rolePo.sendUpdateRelations();
		LogUtil.writeLog(rolePo, 350, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		SessionUtil.addDataArray(1);
		return 1;	
	}
	
	
	/**
	 * 查看玩家信息
	 * @param roleId
	 * @return [0]RoleInforVo
	 */
	public Object getRoleInfor(Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.getRoleInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		GlobalPo globalPoFTRAC = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyFirstThreeRankAndCastellan);
		IdNumberVo2 idNumberVo2 = globalPoFTRAC.fetchFirstThreeRankAndCastellanById(roleId);
		if(idNumberVo2 != null){
			roleId = idNumberVo2.getInt3();
		}
		
		RolePo targetRolePo = RolePo.findEntity(roleId);
		if(targetRolePo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
//		System.out.println("targetRolePo:"+targetRolePo);
		SessionUtil.addDataArray(targetRolePo.toRoleViewInforVo());
		return SessionType.MULTYPE_RETURN;	
	}
	
	/**
	 * 删除黑友 不需要处理返回值
	 * @param roleId
	 * @return
	 */
	public Object removeBlock(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.removeBlock";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		for (RoleInforPo roleInforPo : rolePo.listBlocks) {
			if(roleInforPo.getRoleId().intValue()==roleId){
				rolePo.listBlocks.remove(roleInforPo);
				break;
			}
		}
		rolePo.sendUpdateRelations();
		return SessionType.MULTYPE_RETURN;	
	}
	
	/**
	 * 删除仇人 不需要处理返回值
	 * @param roleId
	 * @return
	 */
	public Object removeEnemy(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.removeEnemy";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		for (RoleInforPo roleInforPo : rolePo.listEnemys) {
			if(roleInforPo.getRoleId().intValue()==roleId){
				rolePo.listEnemys.remove(roleInforPo);
				break;
			}
		}
		rolePo.sendUpdateRelations();
		return SessionType.MULTYPE_RETURN;	
	}
	
	/**
	 * 更新列表 
	 * @param relationType 1:好友；  2：黑名单;3： 仇人；
	 * @return
	 */ 
	public Object fetchRelationList(Integer relationType){
		CheckUtil.checkIsNull(relationType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.fetchRelationList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(relationType.intValue() == 1){
			for (RoleInforPo roleInfoPo : rolePo.listFriends) {
				if(RoleTemplate.roleIdIuidMapping.containsKey(roleInfoPo.getRoleId())){
					roleInfoPo.onlineStatus=1;
				}
				else{
					roleInfoPo.onlineStatus=0;
				}
			}		
			SessionUtil.addDataArray(relationType);
			SessionUtil.addDataArray(rolePo.listFriends);
		}else if(relationType.intValue() == 2){
			for (RoleInforPo roleInfoPo : rolePo.listBlocks) {
				if(RoleTemplate.roleIdIuidMapping.containsKey(roleInfoPo.getRoleId())){
					roleInfoPo.onlineStatus=1;
				}
				else{
					roleInfoPo.onlineStatus=0;
				}
			}	
			SessionUtil.addDataArray(relationType);
			SessionUtil.addDataArray(rolePo.listBlocks);
		}else if(relationType.intValue() == 3){
			for (RoleInforPo roleInfoPo : rolePo.listEnemys) {
				if(RoleTemplate.roleIdIuidMapping.containsKey(roleInfoPo.getRoleId())){
					roleInfoPo.onlineStatus=1;
				}
				else{
					roleInfoPo.onlineStatus=0;
				}
			}		
			SessionUtil.addDataArray(relationType);
			SessionUtil.addDataArray(rolePo.listEnemys);
		}
		return SessionType.MULTYPE_RETURN;	
	}
	
	
	
	
	/**
	 * 删除好友 不需要处理返回值
	 * @param roleId
	 * @return
	 */
	public Object removeFriend(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SocialRemoting.removeFriend";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		for (RoleInforPo roleInforPo : rolePo.listFriends) {
			if(roleInforPo.getRoleId().intValue()==roleId){
				rolePo.listFriends.remove(roleInforPo);
				break;
			}
		}
		rolePo.sendUpdateRelations();
		return SessionType.MULTYPE_RETURN;	
	}
	

	
}
