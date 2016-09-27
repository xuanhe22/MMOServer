package com.games.mmo.vo.global;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class MonsterVo  extends BasePropertyVo {
	/** Id*/
	public int id = 0;
	/** 阵营*/
	public int militaryForces = 0;
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,militaryForces};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		if(val==null){
			return;
		}
		String[] vals = StringUtil.split(val,spliter);
		id=DBFieldUtil.fetchImpodInt(vals[0]);
		militaryForces=DBFieldUtil.fetchImpodInt(vals[1]);
	}
	@Override
	public String toString() {
		return "MonsterVo [id=" + id + ", militaryForces=" + militaryForces
				+ "]";
	}
	
	
}
