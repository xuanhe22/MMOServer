
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.po.RpetPo;
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
	@Table(name = "po_pet_upgrade")
	public class PetUpgradePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*等级无
	**/

	private Integer lv;
	/**
	*阶数无
	**/

	private Integer step;
	/**
	*战斗属性无
	**/

	private String batExp;
	/**
	*魔魂无
	**/

	private Integer costPetSoul;
	/**
	*类型无
	**/

	private String costItems;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="lv")
	 public Integer getLv() {
		return this.lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}

	@Column(name="step")
	 public Integer getStep() {
		return this.step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}

	@Column(name="bat_exp")
	 public String getBatExp() {
		return this.batExp;
	}
	public void setBatExp(String batExp) {
		this.batExp = batExp;
	}

	@Column(name="cost_pet_soul")
	 public Integer getCostPetSoul() {
		return this.costPetSoul;
	}
	public void setCostPetSoul(Integer costPetSoul) {
		this.costPetSoul = costPetSoul;
	}

	@Column(name="cost_items")
	 public String getCostItems() {
		return this.costItems;
	}
	public void setCostItems(String costItems) {
		this.costItems = costItems;
	}

	public static PetUpgradePo findEntity(Integer id){
		return findRealEntity(PetUpgradePo.class,id);
	}
	
	/**
	 * 宠物战斗属性列表
	 */
	@JSONField(serialize=false)
	public List<List<Integer>> listPetbatExp =new ArrayList<List<Integer>>();

	@JSONField(serialize=false)
	public List<IdNumberVo> listCostItems = new ArrayList<IdNumberVo>();
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listPetbatExp=ExpressUtil.buildBattleExpressList(batExp);
			listCostItems = IdNumberVo.createComplexList(costItems);
			unChanged();
			loaded =true;
		}
	}
	
}
