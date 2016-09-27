package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

@Entity
@Table(name = "po_pet_skill")
public class PetSkillPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	 * 宠物技能id
	 */
	private Integer skillId;
	/**
	 * 品质
	 */
	private Integer quality;
	/**
	 * 技能名称
	 */
	private String name;
	/**
	 * 技能图标
	 */
	private String icon;
	/**
	 * 技能名称
	 */
	private String skillName;
	/**
	 * 技能对应buffID
	 */
	private String buffIds;
	/**
	 * 学习消耗道具
	 */
	private String learnExpend;
	/**
	 * 技能描述
	 */
	private String skillDesc;
	
	@JSONField(serialize=false)
	public List<Integer> buffIdList = new ArrayList<Integer>();
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="skill_id")
	public Integer getSkillId() {
		return skillId;
	}
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}
	@Column(name="quality")
	public Integer getQuality() {
		return quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name="skill_name")
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	@Column(name="buff_ids")
	public String getBuffIds() {
		return buffIds;
	}
	public void setBuffIds(String buffIds) {
		this.buffIds = buffIds;
	}
	@Column(name="learn_expend")
	public String getLearnExpend() {
		return learnExpend;
	}
	public void setLearnExpend(String learnExpend) {
		this.learnExpend = learnExpend;
	}
	@Column(name="skill_desc")
	public String getSkillDesc() {
		return skillDesc;
	}
	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			buffIdList = DBFieldUtil.getIntegerListBySplitter(buffIds,"|");
			loaded =true;
		}
	}
	
	public static PetSkillPo findEntity(int petSkillPoId) {
		return findRealEntity(PetSkillPo.class, petSkillPoId);
	}
}
