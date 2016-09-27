package com.games.mmo.mapserver.vo;

import com.games.mmo.type.EffectType;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class BufferEffetVo extends BasePropertyVo {
	/**
	 * buff类型
	 */
	public int buffType;
	/**
	 * 可以为：百分比，次数，固定值，触发概率
	 */
	public int param1;
	/**
	 * 可以为：变化间隔，触发条件
	 */
	public int param2;
	/**
	 * 可以为：触发技能，转移效果
	 */
	public int param3;
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{buffType,param1,param2,param3};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		buffType=DBFieldUtil.fetchImpodInt(vals[0]);
		param1=DBFieldUtil.fetchImpodInt(vals[1]);
		param2=DBFieldUtil.fetchImpodInt(vals[2]);
		param3=DBFieldUtil.fetchImpodInt(vals[3]);
	}
	@Override
	public String toString() {
		return "BufferEffetVo [buffType=" + buffType + ", param1=" + param1
				+ ", param2=" + param2 + ", param3=" + param3 + "]";
	}
	
	
}
