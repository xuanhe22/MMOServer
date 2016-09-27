
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
	@Table(name = "po_skill_description")
	public class SkillDescriptionPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*描述表达式callSkillLv（技能编号）
	**/

	private String descriptionExp;
	/**
	*描述类型1 百分数
2 直接取值
	**/

	private Integer descriptionType;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="description_exp")
	 public String getDescriptionExp() {
		return this.descriptionExp;
	}
	public void setDescriptionExp(String descriptionExp) {
		this.descriptionExp = descriptionExp;
	}

	@Column(name="description_type")
	 public Integer getDescriptionType() {
		return this.descriptionType;
	}
	public void setDescriptionType(Integer descriptionType) {
		this.descriptionType = descriptionType;
	}

	// skillDescriptionPo.setDescriptionExp(descriptionExp)
	// skillDescriptionPo.setDescriptionType(descriptionType)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
