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
import com.storm.lib.util.StringUtil;

@Entity
@Table(name = "po_pet_talent")
public class PetTalentPo extends BaseGameDBPo {
	/**
	*主键
	**/
	private Integer id;
	/**
	 * 宠物天赋id
	 */
	public int talentId;
	/**
	 * 天赋图标
	 */
	public String icon;
	/**
	 * 天赋类型
	 */
	public int talentType;
	/**
	 * 天赋等级
	 */
	public int talentLevel;
	/**
	 * 天赋激活所需星数
	 */
	public int talentStar;
	/**
	 * 升级消耗道具(道具ID|道具数量)
	 */
	public String upgradeExpend;
	/**
	 * 天赋描述
	 */
	public String talentDesc;
	
	/**
	 * 天赋名
	 */
	public String name;
	
	/**
	 * 天赋宠物加成属性
	 */
	public String petAttrExp;
	
	/**
	 * 天赋玩家加成属性
	 */
	public String playerAttrExp;
	
	@JSONField(serialize=false)
	public List<Integer> petAttrList = new ArrayList<Integer>();
	
	@JSONField(serialize=false)
	public List<Integer> playerAttrList = new ArrayList<Integer>();
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="talent_id")
	public int getTalentId() {
		return talentId;
	}
	public void setTalentId(int talentId) {
		this.talentId = talentId;
	}
	@Column(name="icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name="talent_type")
	public int getTalentType() {
		return talentType;
	}
	public void setTalentType(int talentType) {
		this.talentType = talentType;
	}
	@Column(name="talent_level")
	public int getTalentLevel() {
		return talentLevel;
	}
	public void setTalentLevel(int talentLevel) {
		this.talentLevel = talentLevel;
	}
	@Column(name="upgrade_expend")
	public String getUpgradeExpend() {
		return upgradeExpend;
	}
	public void setUpgradeExpend(String upgradeExpend) {
		this.upgradeExpend = upgradeExpend;
	}
	@Column(name="talent_desc")
	public String getTalentDesc() {
		return talentDesc;
	}
	public void setTalentDesc(String talentDesc) {
		this.talentDesc = talentDesc;
	}
	@Column(name="talent_star")
	public int getTalentStar() {
		return talentStar;
	}
	public void setTalentStar(int talentStar) {
		this.talentStar = talentStar;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="pet_attr_exp")
	public String getPetAttrExp() {
		return petAttrExp;
	}
	public void setPetAttrExp(String petAttrExp) {
		this.petAttrExp = petAttrExp;
	}
	@Column(name="player_attr_exp")
	public String getPlayerAttrExp() {
		return playerAttrExp;
	}
	public void setPlayerAttrExp(String playerAttrExp) {
		this.playerAttrExp = playerAttrExp;
	}
	
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			if(!StringUtil.isEmpty(petAttrExp))
			{
				String[] strs = StringUtil.split(petAttrExp, "|");
				for(String str:strs)
				{
					petAttrList.add(Integer.parseInt(str));
				}
			}
			if(!StringUtil.isEmpty(playerAttrExp))
			{
				String[] strs = StringUtil.split(playerAttrExp, "|");
				for(String str:strs)
				{
					playerAttrList.add(Integer.parseInt(str));
				}
			}
			loaded =true;
		}

	}
	
}
