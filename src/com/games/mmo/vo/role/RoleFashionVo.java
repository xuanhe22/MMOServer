package com.games.mmo.vo.role;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.game.FashionPo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;

/**
 * 玩家时装信息
 * @author saly.bao
 *
 */
public class RoleFashionVo extends BasePropertyVo {
	/**
	 * 时装ID
	 */
	public int id;
	/**
	 * 到期时间
	 */
	public long endTime;
	/**
	 * 是否穿戴
	 */
	public int isUse;
	
	/**
	 * 时装类型
	 */
	public int type;
	
	/**
	 * 时装信息
	 */
	@JSONField(serialize=false)
	public FashionPo fashionPo;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,endTime,isUse};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		this.id = DBFieldUtil.fetchImpodInt(vals[0]);
		this.fashionPo = FashionPo.findEntity(id);
		this.endTime=DBFieldUtil.fetchImpodLong(vals[1]);
		if(endTime > 0 && endTime < Integer.MAX_VALUE){
			this.endTime = 1000L*endTime;
		}
		this.isUse = DBFieldUtil.fetchImpodInt(vals[2]);
		this.type = fashionPo.getType().intValue();
	}

}
