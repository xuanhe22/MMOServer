package com.games.mmo.vo.team;

import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.TaskType;
import com.storm.lib.base.BaseVo;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

public class TeamVo extends BaseVo{
	public static Integer idIndex=1;
	public Integer id=0;
	public CopyOnWriteArrayList<TeamMemberVo> teamMemberVos = new CopyOnWriteArrayList<TeamMemberVo>();
	public Long creatTime;
	public Integer currentCopySceneConfPoId;
	/** 加入队伍需要的战力 **/
	public Integer needPower = 0;
	@JSONField(serialize=false)
	public MapRoom targetTeamRoom;
	
	@JSONField(serialize=false)
	public TeamMemberVo captain;
	
	/**
	 * 队伍状态 0：默认(组队状态)； 1：战斗中
	 */
	public Integer teamStatus = 0;
	
	/**
	 * 创建队伍
	 * @param rolePo
	 * @param copySceneConfPoId
	 * @return
	 */
	public static TeamVo createTeamWithCaptain(RolePo rolePo, Integer copySceneConfPoId) {

		TeamVo teamVo = new TeamVo();
		teamVo.id=idIndex++;
		teamVo.creatTime=System.currentTimeMillis();
		TeamMemberVo teamMemberVo = TeamMemberVo.createTeamMember(rolePo,copySceneConfPoId);
		teamVo.addMember(teamMemberVo, copySceneConfPoId);
		teamVo.makeCaptain(teamMemberVo);
		
		teamVo.currentCopySceneConfPoId = copySceneConfPoId;
		
		checkTeamRemove(rolePo.getId());
		if(GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId) == null){
			GlobalCache.teamDungeonIdMaps.put(copySceneConfPoId, new ConcurrentHashMap<Integer, TeamVo>());
		}
		GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).put(teamVo.id, teamVo);

		rolePo.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_726, null, null);
		return teamVo;
	}
	
	/**
	 * 创建副本队伍的时候检查是否有残留队伍
	 * @param roleId
	 */
	public static void checkTeamRemove(int roleId){
		int currentCopySceneConfPoId = 0;
		int currentTeamId = 0;
		Iterator iter = GlobalCache.teamDungeonIdMaps.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Integer copySceneConfPoId = (Integer)entry.getKey();
			ConcurrentHashMap<Integer, TeamVo> teamMap = (ConcurrentHashMap<Integer, TeamVo>)entry.getValue();
			Iterator iterTeam = teamMap.entrySet().iterator();
			while(iterTeam.hasNext()){
				Map.Entry entryTeam = (Map.Entry) iterTeam.next();
				Integer teamId = (Integer)entryTeam.getKey();
				TeamVo teamVo = (TeamVo)entryTeam.getValue();
				if(teamVo.captain != null && teamVo.captain.roleId.intValue() == roleId){
					currentCopySceneConfPoId = copySceneConfPoId;
					currentTeamId = teamId;
					break;
				}
			}
		}
		if(GlobalCache.teamDungeonIdMaps.get(currentCopySceneConfPoId) != null && currentTeamId!=0){
			GlobalCache.teamDungeonIdMaps.get(currentCopySceneConfPoId).remove(currentTeamId);			
		}
	}
	

	/**
	 * 切换队长
	 * @param captain
	 */
	public void makeCaptain(TeamMemberVo captain) {
		boolean matched=false;
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			if(teamMemberVo.roleId.intValue()==captain.roleId.intValue()){
				captain.teamVo.captain=teamMemberVo;
				teamMemberVo.isCaptain=1;
				teamMemberVo.teamReady = 0;
				PrintUtil.print(" teamMemberVo.roleName = " +teamMemberVo.roleName + "; teamMemberVo.isCaptain = " +teamMemberVo.isCaptain);
				matched=true;
			}
			else{
				teamMemberVo.isCaptain=0;
			}
		}
		if(!matched){
			ExceptionUtil.throwConfirmParamException("队长匹配错误");
		}
	}


	/**
	 * 添加队员
	 * @param teamMemberVo
	 */
	public void addMember(TeamMemberVo teamMemberVo, Integer copySceneConfPoId) {
		teamMemberVo.currentCopySceneConfPoId=copySceneConfPoId;
		teamMemberVos.add(teamMemberVo);
		teamMemberVo.teamVo=this;
		// 加入队伍聊天频道
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
		ChannelHandlerContext ioSession = roleTemplate.getSessionById(teamMemberVo.roleId);
		RolePo targetRole = RolePo.findEntity(teamMemberVo.roleId);
		targetRole.setSocialTeamId(id);
		chatService.joinRoomChannel(id, targetRole.getIuid(), ioSession, ChatTempate.chatTeamRooms);
		
		targetRole.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_726, null, null);
		// 广播组队信息
		StringBuffer sb = new StringBuffer();
		if(teamMemberVos.size()==1){
			sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key2700"));
		}
		else{
			sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key2273"));
		}
		chatService.sendTeam(sb.toString(), id);
	}


	/**
	 * 查找队伍
	 * @param teamId
	 * @param copySceneConfPoId
	 * @return
	 */
	public static TeamVo findTeam(Integer teamId, Integer copySceneConfPoId) {
		if(copySceneConfPoId == null || teamId == null){
			return null;
		}
		if(!GlobalCache.teamDungeonIdMaps.containsKey(copySceneConfPoId)){
			return null;
		}
		return GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(teamId);
	}


	/**
	 *  删除队员
	 * @param teamMemberVo
	 */
	public void removeMember(TeamMemberVo teamMemberVo, Integer copySceneConfPoId) {
//		System.out.println("removeMember() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		teamMemberVos.remove(teamMemberVo);
		teamMemberVo.teamVo=this;
		RolePo.findEntity(teamMemberVo.roleId).teamMemberVo=null;
		RolePo.findEntity(teamMemberVo.roleId).setSocialTeamId(null);
		if((teamMemberVo.isCaptain==1) && teamMemberVos.size()>0){
			makeCaptain(teamMemberVos.get(0));
		}
		removeDungeonMemberVo(teamMemberVo.roleId, copySceneConfPoId);
	}

	public static void removeDungeonMemberVo(Integer roleId, Integer copySceneConfPoId){
		if(copySceneConfPoId != null){
			if(GlobalCache.teamDungeonMemberVos.containsKey(copySceneConfPoId)){
				GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).remove(roleId);	
			}
		}
	}
	

	public void startTeamRoomGather(MapRoom mapRoom) {
		targetTeamRoom=mapRoom;
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			teamMemberVo.teamReady=0;
		}
		
	}

	/**
	 * 队伍解散,清楚所有队员
	 */
	public void dismiss() {
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			RolePo.findEntity(teamMemberVo.roleId).teamMemberVo=null;
			RolePo.findEntity(teamMemberVo.roleId).setSocialTeamId(null);
		}
		teamMemberVos.clear();
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		chatService.roomDimissChannel(id, ChatTempate.chatTeamRooms);
		TeamVo.destroyTeam(this, currentCopySceneConfPoId);
		
	}

	/**
	 * 消除队伍
	 * @param teamVo
	 * @param copySceneConfPoId
	 */
	private static void destroyTeam(TeamVo teamVo, Integer copySceneConfPoId) {
		if(teamVo == null || copySceneConfPoId == null){
			return;
		}
		if(GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId) != null){
			GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).remove(teamVo.id);			
		}
	}

	
	
	/**
	 * 查找队员
	 * @param roleId
	 * @return
	 */
	public TeamMemberVo findTeamMember(Integer roleId) {
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			if(teamMemberVo.roleId.intValue()==roleId){
				return teamMemberVo;
			}
		}
		return null;
	}
	
	/**
	 * 查找队长
	 * @return
	 */
	public TeamMemberVo findTeamCaptain(){
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			if(teamMemberVo.isCaptain==1){
				return teamMemberVo;
			}
		}
		return null;
	}
	
	public static TeamVo findTeamByCopySceneAtivityId(Integer copySceneAtivityId){
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneAtivityId);
		CopyScenePo coypSenePo = CopyScenePo.findEntity(cscp.getCopySceneId());
		if(GlobalCache.teamDungeonIdMaps.get(copySceneAtivityId) == null){
			return null;
		}
		for(TeamVo teamVo : GlobalCache.teamDungeonIdMaps.get(copySceneAtivityId).values()){
			if(teamVo.teamMemberVos.size() < coypSenePo.getMaxCount()){
				return teamVo;
			}
		}
		return null;
	}
	

	/**
	 * 推送所有队员信息
	 */
	public void sendAllMemberUpdateTeamInfor() {
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			RolePo.findEntity(teamMemberVo.roleId).sendUpdateTeamInfor(this);
		}
	}

	/**
	 * 推送所有队员信息
	 */
	public void sendAllDungeonTeamMemberUpdateTeamInfor() {
		for (TeamMemberVo teamMemberVo : teamMemberVos) {
			RolePo.findEntity(teamMemberVo.roleId).sendUpdateDungeonTeamInfor(this);
		}
	}
	
	/**
	 * 根据roleId查找队伍中的队员
	 * @param roleId
	 * @return
	 */
	public TeamMemberVo fetchTeamMemberVo(Integer roleId){
		TeamMemberVo teamMemberVo = null;
		for(TeamMemberVo tmv : teamMemberVos){
			if(tmv.roleId.intValue() == roleId){
				teamMemberVo = tmv;
				break;
			}
		}
		return teamMemberVo;
	}


	@Override
	public String toString() {
		return "TeamVo [id=" + id + ", teamMemberVos=" + teamMemberVos
				+ ", creatTime=" + creatTime + ", currentCopySceneConfPoId="
				+ currentCopySceneConfPoId + ", targetTeamRoom="
				+ targetTeamRoom + ", captain=" + captain + "]";
	}
	


	
}
