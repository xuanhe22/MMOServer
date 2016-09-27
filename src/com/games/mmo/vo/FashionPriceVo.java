package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.StringUtil;

public class FashionPriceVo extends BasePropertyVo{

	/**
	 * 购买有效时间
	 */
	public Integer time;
	/**
	 * 消耗道具
	 */
	public Integer itemId;
	/**
	 * 消耗数量
	 */
	public Integer count;
	
	@Override
	public Object[] fetchProperyItems() {
		return null;
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val, spliter);
		time = Integer.parseInt(vals[0]);
		itemId = Integer.parseInt(vals[1]);
		count = Integer.parseInt(vals[2]);
	}

}
