package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;

public class PVPPVEActivityStatusVo implements Comparable<PVPPVEActivityStatusVo>{
	public Integer roomCreatedTime=0;
	/**
	 * 积分
	 */
	public Integer score=0;
	/**
	 * 剩余玩家数量
	 */
	public Integer remainPlayerCount=0;
	
	/**
	 * 剩余怪物数量
	 */
	public Integer remainMonsterCount=0;
	/**
	 * 怪物击杀数量
	 */
	public Integer monsterKillCount=0;
	/**
	 * 当前波数
	 */
	public Integer currentWave=0;
	
	/**
	 * 总波数
	 */
	public Integer totalWave=0;
	
	/**
	 * 我当前排名
	 */
	public Integer myRank=0;
	
	public Integer roleId=0;
	
	public String roleName="";
	
	/**
	 * 当前层数
	 */
	public Integer currentFloor=1;
	
	/**
	 * 当前层击杀数
	 */
	public Integer currentFloorKillCount=0;
	
	/**
	 * 自由之战阵营 1=兄弟盟 2=独立盟
	 */
	public Integer militaryForces=0;
	/**
	 * 阵营1积分
	 */
	public Integer militaryForcesScore1=0;
	/**
	 * 阵营2积分
	 */
	public Integer militaryForcesScore2=0;
	
	@Override
	public int compareTo(PVPPVEActivityStatusVo o) {
		return o.score-score;
	}

	public void init() {
		currentWave=0;
		totalWave=0;
		roomCreatedTime=0;
		monsterKillCount=0;
		currentFloor=1;
		currentFloorKillCount=0;
		myRank=0;
		remainMonsterCount=0;
		score=0;
		remainPlayerCount=0;
		militaryForces=0;
		militaryForcesScore1=0;
		militaryForcesScore2=0;
	}

	@Override
	public String toString() {
		return "PVPPVEActivityStatusVo [score=" + score + ", roleId=" + roleId
				+ ", roleName=" + roleName + ", militaryForces="
				+ militaryForces + ", militaryForcesScore1="
				+ militaryForcesScore1 + ", militaryForcesScore2="
				+ militaryForcesScore2 + "]";
	}

	
	
	
}
