
package com.games.mmo.po.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_pet_roll")
	public class PetRollPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*英雄同英雄配置表的英雄ID
	**/
	private Integer petId;
	/**
	*友情普通库权重使用友情点抽卡的普通挑选
	**/

	private Integer goldGroup1;
	/**
	*友情特殊库权重使用友情点抽卡的特殊挑选
	**/

	private Integer goldGroup2;
	/**
	*钻石普通库权重使用钻石抽卡的普通挑选
	**/

	private Integer diamondGroup1;
	/**
	*钻石特殊库权重使用钻石抽卡的特殊挑选
	**/

	private Integer diamondGroup2;
	/**
	*必中库权重补充特殊权重
	**/

	private Integer specialGroup;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="pet_id")
	 public Integer getPetId() {
		return this.petId;
	}
	public void setPetId(Integer petId) {
		this.petId = petId;
	}

	@Column(name="gold_group1")
	 public Integer getGoldGroup1() {
		return this.goldGroup1;
	}
	public void setGoldGroup1(Integer goldGroup1) {
		this.goldGroup1 = goldGroup1;
	}

	@Column(name="gold_group2")
	 public Integer getGoldGroup2() {
		return this.goldGroup2;
	}
	public void setGoldGroup2(Integer goldGroup2) {
		this.goldGroup2 = goldGroup2;
	}

	@Column(name="diamond_group1")
	 public Integer getDiamondGroup1() {
		return this.diamondGroup1;
	}
	public void setDiamondGroup1(Integer diamondGroup1) {
		this.diamondGroup1 = diamondGroup1;
	}

	@Column(name="diamond_group2")
	 public Integer getDiamondGroup2() {
		return this.diamondGroup2;
	}
	public void setDiamondGroup2(Integer diamondGroup2) {
		this.diamondGroup2 = diamondGroup2;
	}

	@Column(name="special_group")
	 public Integer getSpecialGroup() {
		return this.specialGroup;
	}
	public void setSpecialGroup(Integer specialGroup) {
		this.specialGroup = specialGroup;
	}

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
