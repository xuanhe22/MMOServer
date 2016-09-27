package com.games.mmo.vo.team;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.ChatService;
import com.storm.lib.base.BaseVo;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;

public class TeamMemberVo extends BaseVo{
	
	public Integer roleId;
	
	public String roleName;
	
	public Integer roleLv;
	/**
	 * 用户战力
	 */
	public Integer roleBattlePower;
	/**
	 * 职业
	 */
	public Integer roleCareer;
	
	public Integer roleRoomId;
	/**
	 * 是否是队长
	 */
	public Integer isCaptain=0;
	/**
	 * 是否在线
	 */
	public Integer isOnline=0;
	/**
	 * 是否是机器人
	 */
	public Integer isRobot=0;
	
	@JSONField(serialize=false)
	public TeamVo teamVo;
	
	public Integer teamReady=0;
	
	/**
	 * 当前组队副本id 0：野外； 其他副本
	 */
	public Integer currentCopySceneConfPoId = 0;
	
	/**
	 * 当前血量
	 */
	public Integer batHp;

	/**
	 * 上限
	 */
	public Integer batMaxHp;
	
	public static TeamMemberVo createTeamMember(RolePo rolePo, Integer copySceneConfPoId) {
		TeamMemberVo teamMemberVo = new TeamMemberVo();
		teamMemberVo.isCaptain=0;
		teamMemberVo.isOnline=1;
		teamMemberVo.isRobot=0;
		teamMemberVo.roleBattlePower=rolePo.getBattlePower();
		teamMemberVo.roleCareer=rolePo.getCareer();
		teamMemberVo.roleId=rolePo.getId();
		teamMemberVo.roleLv=rolePo.getLv();
		teamMemberVo.roleName=rolePo.getName();
		teamMemberVo.roleRoomId=rolePo.getRoomId();
		teamMemberVo.batHp=rolePo.getBatHp();
		teamMemberVo.batMaxHp=rolePo.getBatMaxHp();
		rolePo.teamMemberVo=teamMemberVo;
		// 加入副本队员
		if(GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId) == null){
			GlobalCache.teamDungeonMemberVos.put(copySceneConfPoId, new ConcurrentHashMap<Integer, TeamMemberVo>());
		}
		teamMemberVo.currentCopySceneConfPoId = copySceneConfPoId;
		GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).put(teamMemberVo.roleId, teamMemberVo);			

		return teamMemberVo;
	}
	
	/**
	 * 加入副本组队推送角色集合
	 * @param roleId
	 * @param copySceneConfPoId
	 */
	public static void joinDungeonTeamSendRoleId(Integer roleId, Integer copySceneConfPoId){
		if(copySceneConfPoId != null && copySceneConfPoId !=0){
			// 检查是否在其他副本房间里
			checkAnotherDungeonTeamIsExist(roleId);
			if(GlobalCache.teamDungeonRoleids.get(copySceneConfPoId) == null){
				GlobalCache.teamDungeonRoleids.put(copySceneConfPoId, new ConcurrentHashMap<Integer, Integer>());
			}
			GlobalCache.teamDungeonRoleids.get(copySceneConfPoId).put(roleId, roleId);
		}
	}
	
	/**
	 * 删除角色在房间的信息
	 * @param roleId
	 * @param copySceneConfPoId
	 */
	public static void leaveDungeonTeamSendRoleId(Integer roleId, Integer copySceneConfPoId){
		if(copySceneConfPoId != null && copySceneConfPoId !=0){
			if(GlobalCache.teamDungeonRoleids.get(copySceneConfPoId) == null || GlobalCache.teamDungeonRoleids.get(copySceneConfPoId).size() == 0){
				return;
			}
			if(GlobalCache.teamDungeonRoleids.get(copySceneConfPoId).containsKey(roleId)){
				GlobalCache.teamDungeonRoleids.get(copySceneConfPoId).remove(roleId);				
			}
		}
	}
	/**
	 * 检查其他副本是否存在角色
	 * @param roleId
	 */
	public static void checkAnotherDungeonTeamIsExist(Integer roleId){
		for(Integer i : GlobalCache.teamDungeonRoleids.keySet()){
			Integer currentRoleId = GlobalCache.teamDungeonRoleids.get(i).get(roleId);
			if(currentRoleId != null){
				leaveDungeonTeamSendRoleId(currentRoleId, i);
			}
		}
	}
	
	
	public void updateTeamMember(RolePo rolePo){
//		this.isCaptain=false;
		this.isOnline=(rolePo.fetchRoleOnlineStatus()==true)?1:0;
		this.isRobot=0;
		this.roleBattlePower=rolePo.getBattlePower();
//		this.roleCareer=rolePo.getCareer();
//		this.roleId=rolePo.getId();
		this.roleLv=rolePo.getLv();
		this.roleName=rolePo.getName();
		this.roleRoomId=rolePo.getRoomId();
	}

	public void leaveTeam(Integer copySceneConfPoId) {
//		System.out.println(" leaveTeam() " + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		teamVo.removeMember(this, copySceneConfPoId);
		if(teamVo.teamMemberVos.size()<1){
			teamVo.dismiss();
		}
		// 退出组队聊天频道
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
		RolePo targetRole = RolePo.findEntity(roleId);
		ChannelHandlerContext ioSession = targetRole.fetchSession();
		chatService.leaveRoomChannel(teamVo.id, targetRole.getIuid(), ioSession, ChatTempate.chatTeamRooms);
		targetRole.teamMemberVo = null;
		targetRole.setSocialTeamId(null);
		TeamVo.removeDungeonMemberVo(roleId, teamVo.currentCopySceneConfPoId);
		// 广播队员离开信息
		StringBuffer sb = new StringBuffer();
		sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key193"));					
		chatService.sendTeam(sb.toString(), teamVo.id);
		targetRole.sendUpdateTeamInfor(null);
	}

	public void readyTeamRoomGather() {
		teamReady=1;
		for (TeamMemberVo teamMemberVo : teamVo.teamMemberVos) {
			if(teamMemberVo.teamReady==0){
				return;
			}
		}
		for (TeamMemberVo teamMemberVo : teamVo.teamMemberVos) {
			RolePo.findEntity(teamMemberVo.roleId).sendRequireEnterRoom(teamVo.targetTeamRoom.mapRoomId,teamVo.targetTeamRoom.sceneId,teamVo.currentCopySceneConfPoId);
		}

	}

	@Override
	public String toString() {
		return "TeamMemberVo [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleLv=" + roleLv + ", roleBattlePower=" + roleBattlePower
				+ ", roleCareer=" + roleCareer + ", roleRoomId=" + roleRoomId
				+ ", isCaptain=" + isCaptain + ", isOnline=" + isOnline
				+ ", isRobot=" + isRobot 
				+ ", teamReady=" + teamReady + ", currentCopySceneConfPoId="
				+ currentCopySceneConfPoId + "]";
	}
	
	
	
}
