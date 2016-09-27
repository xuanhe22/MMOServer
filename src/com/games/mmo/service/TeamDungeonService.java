package com.games.mmo.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.vo.team.TeamAbroadMemberVo;
import com.games.mmo.vo.team.TeamAbroadRoomInforVo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.storm.lib.base.BaseService;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.RandomUtil;

@Controller
public class TeamDungeonService extends BaseService {
	/**
	 * 
	 * 方法功能:房间广播信息
	 * 更新时间:2014-6-27, 作者:johnny
	 */
	public void broadLobbyUpdateInfors(Integer copySceneConfPoId) {
		List<TeamAbroadRoomInforVo> list =new ArrayList<TeamAbroadRoomInforVo>();
		if(GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId) == null || GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).size() == 0){
			
		}
		else
		{
			Iterator iter = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).entrySet().iterator();
	
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Integer teamId = (Integer)entry.getKey();
				TeamVo teamVo = (TeamVo)entry.getValue();
				TeamAbroadRoomInforVo teamAbroadRoomInforVo = new TeamAbroadRoomInforVo();
				teamAbroadRoomInforVo.teamId=teamVo.id;
				teamAbroadRoomInforVo.currentPlayers=teamVo.teamMemberVos.size();
				teamAbroadRoomInforVo.currentCopySceneConfPoId=teamVo.currentCopySceneConfPoId;
				teamAbroadRoomInforVo.needPower = teamVo.needPower;
				teamAbroadRoomInforVo.teamStatus = teamVo.teamStatus;
				for (TeamMemberVo teamMemberVo : teamVo.teamMemberVos) {
					TeamAbroadMemberVo teamAbroadMemberVo = new TeamAbroadMemberVo();
					teamAbroadMemberVo.name=teamMemberVo.roleName;
					teamAbroadMemberVo.lv=teamMemberVo.roleLv.intValue();		
					teamAbroadMemberVo.roleId=teamMemberVo.roleId;
					teamAbroadMemberVo.name=teamMemberVo.roleName;
					teamAbroadMemberVo.wasCaptain=teamMemberVo.isCaptain;
					teamAbroadRoomInforVo.teamAbroadMemberVos.add(teamAbroadMemberVo);
				}
				list.add(teamAbroadRoomInforVo);	
			}
		}
		
		Iterator iter2 = GlobalCache.teamDungeonRoleids.get(copySceneConfPoId).entrySet().iterator();
		while (iter2.hasNext()) {
			Map.Entry entry = (Map.Entry) iter2.next();
			Integer roleId = (Integer)entry.getKey();
			RolePo rolePo = RolePo.findEntity(roleId);
			if(rolePo!=null){
//					System.out.println("副本推送：" + rolePo.getName() +" "+ new Date().toLocaleString() );
				rolePo.sendUpdateTeamDungeon(list);
			}				
		}
		
	}
}
