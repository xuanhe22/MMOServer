package com.games.mmo.mapserver.vo;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.type.BuffModelType;
import com.games.mmo.type.CopySceneType;
import com.storm.lib.base.BaseVo;

public class BufferStatusVo extends BaseVo{
	public BuffPo buffPo;
	public int buffId;
	public Long endTime;
	// buff持续时间
	public Long lifeTime;
	public Integer life;
	@JSONField(serialize=false)
	public int buffType;
	/**
	 * buff触发fighter
	 */
	@JSONField(serialize=false)
	public Fighter triggerFighter;
	/**
	 * buff作用目标列表
	 */
	@JSONField(serialize=false)
	public List<Fighter> receiveFighters;//触发
	
	/**
	 * buff参数
	 */
	@JSONField(serialize=false)
	public List<BufferEffetVo> bufferEffetVos;
	

	
	public BufferStatusVo(BuffPo buffPo, Fighter triggerFighter, List<Fighter> receiveFighters)
	{
		this.buffPo = buffPo;
		this.buffId = buffPo.getId();
		this.endTime = buffPo.getDurationValexp()==CopySceneType.EIKY_TIME?CopySceneType.EIKY_TIME:System.currentTimeMillis()+buffPo.getDurationValexp()*1000;
		this.lifeTime = buffPo.getDurationValexp()==CopySceneType.EIKY_TIME?CopySceneType.EIKY_TIME:buffPo.getDurationValexp()*1000l;
		this.life = buffPo.getLife();
		this.buffType = buffPo.bufferEffetVos.get(0).buffType;
		this.triggerFighter = triggerFighter;
		this.receiveFighters = receiveFighters;
		this.bufferEffetVos = buffPo.bufferEffetVos;
	}

	@Override
	public String toString() {
		return "BufferStatusVo [buffPo=" + buffPo + ", endTime=" + endTime
				+ ", lifeTime=" + lifeTime + ", life=" + life + ", buffType="
				+ buffType + ", triggerFighter=" + triggerFighter
				+ ", receiveFighters=" + receiveFighters + ", bufferEffetVos="
				+ bufferEffetVos + "]";
	}
}
