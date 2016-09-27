package com.games.mmo.remoting;

import java.text.MessageFormat;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.TeamDungeonService;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

@Controller
public class TeamDungeonRemoting extends BaseRemoting {
	@Autowired
	private CheckService checkService;	
	@Autowired
	private ChatService chatService;
	@Autowired
	private TeamDungeonService teamDungeonService;
	
	
	/**
	 * 方法功能:打开协作界面
	 * 更新时间:2014-4-30, 作者:johnny
	 * @return
	 */
	public Object joinTeamLobby(Integer copySceneConfPoId){
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		PrintUtil.print(rolePo.getName() + " joinTeamLobby() copySceneConfPoId = " +copySceneConfPoId + "; " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"TeamDungeonService.joinTeamLobby";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(rolePo.teamMemberVo != null){
			rolePo.teamMemberVo.leaveTeam(rolePo.teamMemberVo.currentCopySceneConfPoId);
//			TeamMemberVo.leaveDungeonTeamSendRoleId(rolePo.getId(), copySceneConfPoId);
		}
//		System.out.println("joinTeamLobby() " + rolePo.getName()+ " " + new Date().toLocaleString() + " copySceneConfPoId == " +copySceneConfPoId);
		TeamMemberVo.joinDungeonTeamSendRoleId(rolePo.getId(), copySceneConfPoId);
		teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
		return 1;
	}
	
	/**
	 * 方法功能:离开协作界面，开始接受列表    房间列表
	 * 更新时间:2014-4-30, 作者:johnny
	 * @return
	 */
	public Object leaveTeamLobby(Integer copySceneConfPoId){
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println(rolePo.getName() + " leaveTeamLobby() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("leaveTeamLobby() " + rolePo.getName()+ " " + new Date().toLocaleString() + " copySceneConfPoId == " +copySceneConfPoId);
		TeamMemberVo.leaveDungeonTeamSendRoleId(rolePo.getId(), copySceneConfPoId);
		teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
		return 1;
	}
	
	
	/**
	 * 
	 * 方法功能:创建组队副本
	 * 更新时间:2014-12-3, 作者:peter
	 * @return
	 */
	public Object teamCreateTeamDungenon(Integer copySceneConfPoId, Integer needPower){ 
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		checkService.checkExisScenePo(cscp.getSceneId());
		ScenePo scenePo = ScenePo.findEntity(cscp.getSceneId());
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println(rolePo.getName() + " teamCreateTeamDungenon() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
		
		if(rolePo.fighter!= null && rolePo.fighter.mapRoom !=null){
			ScenePo sceneRed = ScenePo.findEntity(rolePo.fighter.mapRoom.sceneId);
			if(sceneRed!=null && sceneRed.getId().intValue() == 20100005 && rolePo.getPkValue() !=null && rolePo.getPkValue().intValue() >= 10 ){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2294"));
			}
		}
		
		for (IdNumberVo3 idNumberVo3 : rolePo.listCopySceneTodayVisitTimes) {
			if(idNumberVo3.getInt1().intValue()==copySceneConfPoId.intValue() && idNumberVo3.getInt2().intValue()==cscp.getTeamMode().intValue()){
				rolePo.checkSceneTimesBySceneId(idNumberVo3.getInt3(), cscp.getSceneId(), cscp.getAvaTimes());
			}
		}
		
		
		if(rolePo.getLv().intValue() < cscp.getRequireLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key206")+cscp.getRequireLv());
		}
		if(rolePo.teamMemberVo!=null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key215"));
		}
		TeamVo teamVo = TeamVo.createTeamWithCaptain(rolePo, copySceneConfPoId);
		if(teamVo != null){
			rolePo.setSocialTeamId(teamVo.id);
			if(needPower != null){
				teamVo.needPower = needPower;				
			}
			String content =MessageFormat. format(GlobalCache.fetchLanguageMap("key2690"), rolePo.getName() , scenePo.getName(), String.valueOf(cscp.getId()),String.valueOf(teamVo.id) , String.valueOf(teamVo.needPower));
			chatService.sendSystemWorldChat(content);	
		}
		TeamMemberVo.leaveDungeonTeamSendRoleId(rolePo.getId(), copySceneConfPoId);
		teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
		teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();
		
		SessionUtil.addDataArray(teamVo.id);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 
	 * 方法功能:获取自己队伍信息
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamFetchMyTeamInfor(Integer copySceneConfPoId){
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println(rolePo.getName() + " teamFetchMyTeamInfor() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("teamFetchMyTeamInfor()  " + rolePo.getName()+ " " + new Date().toLocaleString() + " copySceneConfPoId == " +copySceneConfPoId);
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
	 * 方法功能:加入房间
	 * 更新时间:2014-6-24, 作者:johnny
	 * @param teamId
	 * @return
	 */
	public Object joinRoom(Integer teamId, Integer copySceneConfPoId){
//		System.out.println("         joinRoom() teamId= "+teamId +"; copySceneConfPoId = " +copySceneConfPoId);
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		checkService.checkExisCopyScenePo(cscp.getCopySceneId());
		CopyScenePo coypSenePo = CopyScenePo.findEntity(cscp.getCopySceneId());
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println(rolePo.getName() + " joinRoom() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("joinRoom()  " + rolePo.getName()+ " " + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) + " copySceneConfPoId == " +copySceneConfPoId);
		
		if(rolePo.fighter!= null && rolePo.fighter.mapRoom !=null){
			ScenePo sceneRed = ScenePo.findEntity(rolePo.fighter.mapRoom.sceneId);
			if(sceneRed!=null && sceneRed.getId().intValue() == 20100005 && rolePo.getPkValue() !=null && rolePo.getPkValue().intValue() >= 10 ){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2294"));
			}
		}
		for (IdNumberVo3 idNumberVo3 : rolePo.listCopySceneTodayVisitTimes) {
			if(idNumberVo3.getInt1().intValue()==copySceneConfPoId.intValue() && idNumberVo3.getInt2().intValue()==cscp.getTeamMode().intValue()){
				if(cscp.getTeamMode()!=1){
					rolePo.checkSceneTimesBySceneId(idNumberVo3.getInt3(), cscp.getSceneId(), cscp.getAvaTimes());
				}
				break;
			}
		}
		
		
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(teamId);
		if(teamVo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key216"));
		}
		
		if(teamVo.teamStatus.intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2637"));
		}
		
		// 判断战力是否达到组队的需求
		if(teamVo.needPower != null && teamVo.needPower != 0){
			if(rolePo.getBattlePower().intValue() < teamVo.needPower){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key217"));
			}
		}
		// 不能超过房间人数
		if(teamVo.teamMemberVos.size()>= coypSenePo.getMaxCount().intValue()&& coypSenePo.getMaxCount().intValue() != 0)
		{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key218"));
		}
		// 要达到组队本等级
		if(rolePo.getLv().intValue() < cscp.getRequireLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key206")+cscp.getRequireLv());
		}
		TeamMemberVo tmv = teamVo.fetchTeamMemberVo(rolePo.getId());
		if(tmv != null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key215"));
//			teamVo.removeMember(tmv, copySceneConfPoId);
		}
		
//		System.out.println("11teamMemberVo="+ rolePo.teamMemberVo);
		TeamMemberVo teamMemberVo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(rolePo.getId());
		if(teamMemberVo ==null || rolePo.teamMemberVo==null){
			teamMemberVo = TeamMemberVo.createTeamMember(rolePo,copySceneConfPoId);
		}
       teamVo.addMember(teamMemberVo, copySceneConfPoId); 
       TeamMemberVo.leaveDungeonTeamSendRoleId(rolePo.getId(), copySceneConfPoId);
       teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
       teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();
//       System.out.println("22teamMemberVo="+ rolePo.teamMemberVo);
       
//        System.out.println("joinRoom() 22 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			ExceptionUtil.processException(e);
		}
//		System.out.println("joinRoom() 33 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		return 1;
	}
	
	
	/**
	 * 
	 * 方法功能:退出房间
	 * 更新时间:2014-4-30, 作者:johnny
	 * @return
	 */
	public Object exitRoom(Integer copySceneConfPoId){
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		if(cscp == null){
			return 1;
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println("TeamDungeonRemoting.exitRoom() rolePo.name +  " + rolePo.getName());
		SessionUtil.checkSessionLost(rolePo);
		TeamMemberVo teamMemberVo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(rolePo.getId());
		if(teamMemberVo == null)
		{
			return 1;
		}
		Integer socialTeamId = rolePo.getSocialTeamId();
		teamMemberVo.leaveTeam(copySceneConfPoId);
		teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
		if(GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId) != null){
			TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(socialTeamId);
			if(teamVo != null){
				teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();			 
			}		
		}
		return 1;
	}
	
	
	/**
	 * 队长踢人
	 * @param targetRoleId
	 * @return
	 */
	public Object captainKickingPlayerExitRoom(Integer targetRoleId,Integer copySceneConfPoId){
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		checkService.checkExisRolePo(targetRoleId);
		RolePo targetRolePo = RolePo.findEntity(targetRoleId);
//		System.out.println("captainKickingPlayerExitRoom()  " + rolePo.getName()+ " " + new Date().toLocaleString() + " copySceneConfPoId == " +copySceneConfPoId);
//		System.out.println(rolePo.getName() + " captainKickingPlayerExitRoom() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		if(rolePo.teamMemberVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key219"));
		}
		if(rolePo.getId().intValue() == targetRoleId.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key191"));
		}
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(rolePo.getSocialTeamId());
		if(teamVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key187"));
		}
		TeamMemberVo teamMemberRolePo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(rolePo.getId());
		if(teamMemberRolePo.isCaptain==0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key220"));
		}
		if(targetRolePo.teamMemberVo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key221"));
		}
		TeamMemberVo teamMemberVoTarget = teamVo.findTeamMember(targetRoleId);
		if(teamMemberVoTarget == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key222"));
		}
		teamVo.removeMember(teamMemberVoTarget, copySceneConfPoId);
		targetRolePo.teamMemberVo = null;
		targetRolePo.setSocialTeamId(null);
		// 退出组队聊天频道
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		ChannelHandlerContext ioSession = targetRolePo.fetchSession();
		chatService.leaveRoomChannel(teamVo.id, targetRolePo.getIuid(), ioSession, ChatTempate.chatTeamRooms);

		// 广播队员离开信息
		StringBuffer sb = new StringBuffer();
		sb.append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key193"));					
		chatService.sendTeam(sb.toString(), teamVo.id);
		teamDungeonService.broadLobbyUpdateInfors(copySceneConfPoId);
		targetRolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key194"));
		targetRolePo.sendUpdateTeamInfor(null);
		teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();
		return 1;
	}
	
	/**
	 * 
	 * 方法功能:改变组队准备状态
	 * 更新时间:2014-6-24, 作者:johnny
	 * @param teamId
	 * @return
	 */
	public Object changeReadyDungeonTeamState(Integer copySceneConfPoId){
//		System.out.println(" copySceneConfPoId = " +copySceneConfPoId);
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println(rolePo.getName() + " changeReadyDungeonTeamState() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		if(rolePo.teamMemberVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key219"));
		}
		checkService.checkExisDungeonTeamVo(rolePo.getSocialTeamId(), copySceneConfPoId);
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(rolePo.getSocialTeamId());

		checkService.checkExisDungeonMemberVo(rolePo.getId(), copySceneConfPoId);
		TeamMemberVo teamMemberVo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(rolePo.getId());

		TeamMemberVo teamMemberVoCaptain = teamVo.findTeamCaptain();
		if(teamMemberVoCaptain == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key223"));
		}
		if(teamMemberVoCaptain.teamReady==1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key224"));
		}
		if(teamMemberVo.isCaptain==0){
			if(teamMemberVo.teamReady==1){
				teamMemberVo.teamReady = 0;
			}else{
				teamMemberVo.teamReady = 1;
			}
			teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();
		}
		return 1;
	}
	
	
	/**
	 * 
	 * 方法功能:使其他人成为队长
	 * 更新时间:2014-12-3, 作者:johnny
	 * @return
	 */
	public Object teamCaptainMember(Integer roleId, Integer copySceneConfPoId){ 
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("teamCaptainMember()  " + rolePo.getName()+ " " + new Date().toLocaleString() + " copySceneConfPoId == " +copySceneConfPoId);
		if(rolePo.getId().intValue() == roleId.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key195"));
		}
		checkService.checkExisDungeonTeamVo(rolePo.getSocialTeamId(), copySceneConfPoId);
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(rolePo.getSocialTeamId());
		checkService.checkExisDungeonMemberVo(roleId, copySceneConfPoId);
		TeamMemberVo teamMemberVo = teamVo.findTeamMember(roleId);
		if(teamMemberVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key225")+"roleId："+roleId);
		}
		teamVo.makeCaptain(teamMemberVo);
		teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();
		
		// 组队广播
		RolePo targetRolePo = RolePo.findEntity(roleId);
		StringBuffer sb = new StringBuffer();
		sb.append(targetRolePo.getName()).append(GlobalCache.fetchLanguageMap("key196"));					
		chatService.sendTeam(sb.toString(), teamVo.id);
		return 1;
	}
	

	/**
	 * 获得活动时间信息
	 * @return
	 */
	public Object openCopySceneActivityLobby(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("openCopySceneActivityLobby() name="+rolePo.getName() + " "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		rolePo.checkResetGuildBossFlushTime();
		SessionUtil.addDataArray(GlobalCache.fetchCopySceneActivityVoList());
		SessionUtil.addDataArray(PVEPVPRecordVo.instance);
		SessionUtil.addDataArray(rolePo.listFightActivityMaxScoreRecords);
		SessionUtil.addDataArray(rolePo.listCopySceneTodayVisitTimes);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 队长广播
	 * @param copySceneConfPoId
	 * @return
	 */
	public Object captainRadio(Integer copySceneConfPoId){
//		System.out.println(" captainRadio() copySceneConfPoId  = " +copySceneConfPoId );
		CheckUtil.checkIsNull(copySceneConfPoId);
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		checkService.checkExisScenePo(cscp.getSceneId());
		ScenePo scenePo = ScenePo.findEntity(cscp.getSceneId());
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"TeamDungeonRemoting.captainRadio";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		if(rolePo.teamMemberVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key219"));
		}
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(rolePo.getSocialTeamId());
		if(teamVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key187"));
		}
		TeamMemberVo teamMemberRolePo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(rolePo.getId());
		if(teamMemberRolePo.isCaptain==0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key220"));
		}

		String content =MessageFormat. format(GlobalCache.fetchLanguageMap("key2690"), rolePo.getName() , scenePo.getName(), String.valueOf(cscp.getId()),String.valueOf(teamVo.id) , String.valueOf(teamVo.needPower));
//		System.out.println("content = " +content);
		chatService.sendSystemWorldChat(content);
		if(rolePo.getGuildId()!=0){
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
			if(guildPo != null){
				chatService.sendGuild(content, guildPo.getId());						
			}
		}
		for(RoleInforPo rp : rolePo.listFriends){
			if(RoleTemplate.roleIdIuidMapping.containsKey(rp.getRoleId())){
				rp.onlineStatus=1;
				chatService.sendSystemMsg(content, rp.getRoleId());					
			}
		}
		return SessionType.MULTYPE_RETURN;
	}
	
}
