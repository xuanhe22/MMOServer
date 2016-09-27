package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

/**
 * 进阶整体加成信息Vo
 * @author peter
 *
 */
public class AdvanceSuitPlusVo extends BasePropertyVo {
	/**
	 * 强化套装加成到达最大等级
	 */
	public Integer powerSuitPlusArriveMaxLevel = 0;
	
	/**
	 * 当前强化套装加成等级
	 */
	public Integer currentPowerSuitPlusLevel = 0;
	
	/**
	 * 升星套装加成到达最大等级
	 */
	public Integer starSuitPlusArriveMaxLevel = 0;
	
	/**
	 * 当前升星套装加成等级
	 */
	public Integer currentStarSuitPlusArriveLevle = 0;
	
	/**
	 * 洗练套装加成达到最大等级
	 */
	public Integer washSuitPlusMaxLevel = 0;
	
	/**
	 * 当前洗练套装加成等级
	 */
	public Integer washSuitPlusCurrentLevel = 0;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{powerSuitPlusArriveMaxLevel,
										  currentPowerSuitPlusLevel,
										  starSuitPlusArriveMaxLevel,
										  currentStarSuitPlusArriveLevle,
										  washSuitPlusMaxLevel,
										  washSuitPlusCurrentLevel};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		powerSuitPlusArriveMaxLevel=DBFieldUtil.fetchImpodInt(vals[0]);
		currentPowerSuitPlusLevel=DBFieldUtil.fetchImpodInt(vals[1]);
		starSuitPlusArriveMaxLevel=DBFieldUtil.fetchImpodInt(vals[2]);
		currentStarSuitPlusArriveLevle=DBFieldUtil.fetchImpodInt(vals[3]);
		if(fetchProperyItems().length == vals.length){
			washSuitPlusMaxLevel=DBFieldUtil.fetchImpodInt(vals[4]);
			washSuitPlusCurrentLevel=DBFieldUtil.fetchImpodInt(vals[5]);			
		}
	}
	
	public void adjustPowerSuitPlusArriveMaxLevel(Integer i){
		if(i < 0){
			return;
		}
		powerSuitPlusArriveMaxLevel = powerSuitPlusArriveMaxLevel +i;
	}
	
	public void adjustStarSuitPlusArriveMaxLevel(Integer i){
		if(i < 0){
			return;
		}
		starSuitPlusArriveMaxLevel = starSuitPlusArriveMaxLevel +i;
	}

	public void adjustWashSuitPlusMaxLevel(int num){
		if(num < 0){
			return;
		}
		washSuitPlusMaxLevel = washSuitPlusMaxLevel + num;
	}
	
	@Override
	public String toString() {
		return "AdvanceSuitPlusVo [powerSuitPlusArriveMaxLevel="
				+ powerSuitPlusArriveMaxLevel + ", currentPowerSuitPlusLevel="
				+ currentPowerSuitPlusLevel + ", starSuitPlusArriveMaxLevel="
				+ starSuitPlusArriveMaxLevel
				+ ", currentStarSuitPlusArriveLevle="
				+ currentStarSuitPlusArriveLevle + ", washSuitPlusMaxLevel="
				+ washSuitPlusMaxLevel + ", washSuitPlusCurrentLevel="
				+ washSuitPlusCurrentLevel + "]";
	}

	
	
	
}
