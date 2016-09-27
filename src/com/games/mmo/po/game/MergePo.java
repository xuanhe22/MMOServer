
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.util.ExpressUtil;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.vo.IdNumberVo;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_merge")
	public class MergePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*类型无
	**/

	private Integer type;
	/**
	*目标道具ID无
	**/

	private Integer targetItemId;
	/**
	*消耗道具无
	**/

	private String consumItems;
	/**
	*消耗金币无
	**/

	private Integer consumeGold;
	/**
	*成功率无
	**/

	private Integer successRate;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="type")
	 public Integer getType() {
		return this.type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name="target_item_id")
	 public Integer getTargetItemId() {
		return this.targetItemId;
	}
	public void setTargetItemId(Integer targetItemId) {
		this.targetItemId = targetItemId;
	}

	@Column(name="consum_items")
	 public String getConsumItems() {
		return this.consumItems;
	}
	public void setConsumItems(String consumItems) {
		this.consumItems = consumItems;
	}

	@Column(name="consume_gold")
	 public Integer getConsumeGold() {
		return this.consumeGold;
	}
	public void setConsumeGold(Integer consumeGold) {
		this.consumeGold = consumeGold;
	}

	@Column(name="success_rate")
	 public Integer getSuccessRate() {
		return this.successRate;
	}
	public void setSuccessRate(Integer successRate) {
		this.successRate = successRate;
	}

	@JSONField(serialize=false)
	public List<List<Integer>> listConsumItems=new ArrayList<List<Integer>>();
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listConsumItems=ExpressUtil.buildBattleExpressList(consumItems);
			unChanged();
			loaded =true;
		}
	}
	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	
	public static MergePo findEntity(Integer id){
		return findRealEntity(MergePo.class,id);
	}
}
