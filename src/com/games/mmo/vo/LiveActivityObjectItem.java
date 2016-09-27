package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;

import com.storm.lib.base.BaseVo;
import com.storm.lib.vo.IdNumberVo;

/**
 * 运营活动-目标条目
 * @author Administrator
 *
 */
public class LiveActivityObjectItem extends BaseVo{
	/**
	 * 文本
	 */
	public String text;
	/**
	 * 进度
	 */
	public int targetCount=0;
	/**
	 * 奖励道具列表
	 */
	public List<IdNumberVo> awards=new ArrayList<IdNumberVo>();
}
