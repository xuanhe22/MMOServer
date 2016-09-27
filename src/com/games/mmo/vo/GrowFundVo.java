package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class GrowFundVo extends BasePropertyVo {
	/**
	 * 成长基金1购买的时间
	 */
	public long buyGrowFund1Time = 0l;
	
	/**
	 *  成长基金1最近一次领取时间
	 */
	public long takeGrowFund1Time = 0l;
	
	/**
	 *  成长基金1最近一次领取的id
	 */
	public int takeGrowFund1Index = 0;
	
	/**
	 * 成长基金2购买的时间
	 */
	public long buyGrowFund2Time = 0l;
	
	/**
	 *  成长基金1最近一次领取时间
	 */
	public long takeGrowFund2Time = 0l;
	
	/**
	 *  成长基金1最近一次领取的id
	 */
	public int takeGrowFund2Index = 0;
	
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{buyGrowFund1Time,
							takeGrowFund1Time,
							takeGrowFund1Index,
							buyGrowFund2Time,
							takeGrowFund2Time,
							takeGrowFund2Index};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		buyGrowFund1Time=DBFieldUtil.fetchImpodLong(vals[0]);
		takeGrowFund1Time=DBFieldUtil.fetchImpodLong(vals[1]);
		takeGrowFund1Index=DBFieldUtil.fetchImpodInt(vals[2]);
		buyGrowFund2Time=DBFieldUtil.fetchImpodLong(vals[3]);
		takeGrowFund2Time=DBFieldUtil.fetchImpodLong(vals[4]);
		takeGrowFund2Index=DBFieldUtil.fetchImpodInt(vals[5]);
	}

}
