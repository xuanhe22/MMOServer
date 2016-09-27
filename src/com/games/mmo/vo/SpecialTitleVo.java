package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class SpecialTitleVo extends BasePropertyVo {
	/**
	 * 称号Id
	 */
	public int id;
	
	/**
	 * 称号lv
	 */
	public int lv;
	
	/**
	 * 称号类型
	 */
	public int type;
	/**
	 * 到期时间
	 */
	public long endTime;
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,lv,type,endTime};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		id=DBFieldUtil.fetchImpodInt(vals[0]);
		lv=DBFieldUtil.fetchImpodInt(vals[1]);
		type=DBFieldUtil.fetchImpodInt(vals[2]);
		endTime=DBFieldUtil.fetchImpodLong(vals[3]);
		
	}
}
