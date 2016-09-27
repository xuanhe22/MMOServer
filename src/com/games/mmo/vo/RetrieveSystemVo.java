package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class RetrieveSystemVo extends BasePropertyVo {
	public Integer id;
	// 开启时间
	public Long time;
	// 今天状态
	public Integer todayState=0;
	// 昨天状态
	public Integer yesterdayState=0;
	
	
	public RetrieveSystemVo() {
		super();
	}
	
	public RetrieveSystemVo(Integer id, Long time, Integer todayState,
			Integer yesterdayState) {
		super();
		this.id = id;
		this.time = time;
		this.todayState = todayState;
		this.yesterdayState = yesterdayState;
	}

	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,time,todayState,yesterdayState};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		if(val==null){
			return;
		}
		String[] vals = StringUtil.split(val,spliter);
		id=DBFieldUtil.fetchImpodInt(vals[0]);
		time=DBFieldUtil.fetchImpodLong(vals[1]);
		todayState=DBFieldUtil.fetchImpodInt(vals[2]);
		yesterdayState=DBFieldUtil.fetchImpodInt(vals[3]);
	}

	@Override
	public String toString() {
		return "RetrieveSystemVo [id=" + id + ", time=" + time
				+ ", todayState=" + todayState + ", yesterdayState="
				+ yesterdayState + "]";
	}
	
	
}
