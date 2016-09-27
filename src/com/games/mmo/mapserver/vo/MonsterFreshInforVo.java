package com.games.mmo.mapserver.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.MonsterPo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class MonsterFreshInforVo extends BasePropertyVo {
	public int monsterFreshId;
	//怪物对象
	@JSONField(serialize=false)
	public MonsterFreshPo monsterFreshPo;
	/**
	 * 死亡时间
	 */
	@JSONField(serialize=false)
	public long disapperTime=0l;
	//机器人对象
	@JSONField(serialize=false)
	public Fighter robotFighter;
	//上次击杀者名字
	public String lastKiller=GlobalCache.fetchLanguageMap("key2276");
	//boss用途
	public int bossUsage=0;
	//是否刷新
	public int isRefresh;
	//刷新剩余时间
	public int remainingTime;
	@JSONField(serialize=false)
	public MonsterPo monsterPo;
	@JSONField(serialize=false)
	public Integer monsterId;	
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{monsterFreshId,
							disapperTime,
							lastKiller,
							bossUsage,
							remainingTime,
							monsterId};
	}
	
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		monsterFreshId=DBFieldUtil.fetchImpodInt(vals[0]);
		disapperTime=DBFieldUtil.fetchImpodLong(vals[1]);
		lastKiller=DBFieldUtil.fetchImpodString(vals[2]);
		bossUsage=DBFieldUtil.fetchImpodInt(vals[3]);
		remainingTime=DBFieldUtil.fetchImpodInt(vals[4]);
		monsterId=DBFieldUtil.fetchImpodInt(vals[5]);
	}
	
}
