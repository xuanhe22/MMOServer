package com.games.mmo.vo.team;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * 类功能:房间信息
 * @author johnny
 * @version 2014-6-27
 */
public class TeamAbroadRoomInforVo {
	/**房间编号 */
	public Integer teamId;
	/** 当前关卡Id */
	public Integer currentCopySceneConfPoId;
	/** 当前人数 */
	public Integer currentPlayers;
	/** 加入队伍需要的战力 **/
	public Integer needPower;
	
	/**
	 * 队伍状态 0：默认(组队状态)； 1：战斗中
	 */
	public Integer teamStatus = 0;
	


	
	/**
	 * 成员清单
	 */
	public List<TeamAbroadMemberVo> teamAbroadMemberVos = new ArrayList<TeamAbroadMemberVo>(); 

	
}
