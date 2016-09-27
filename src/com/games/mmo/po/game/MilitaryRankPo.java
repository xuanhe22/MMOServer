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
	@Table(name = "po_military_rank")
	public class MilitaryRankPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*军衔名称无
	**/

	private String name;
	/**
	*军衔所需声望填值
	**/

	private Integer prestige;
	/**
	*军衔图片填图片name
	**/

	private String image;
	/**
	*军衔名称图片填图片name
	**/

	private String nameImage;
	/**
	*军衔属性填属性表达式
	**/

	private String batAttrExp;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="prestige")
	 public Integer getPrestige() {
		return this.prestige;
	}
	public void setPrestige(Integer prestige) {
		this.prestige = prestige;
	}

	@Column(name="image")
	 public String getImage() {
		return this.image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	@Column(name="name_image")
	 public String getNameImage() {
		return this.nameImage;
	}
	public void setNameImage(String nameImage) {
		this.nameImage = nameImage;
	}

	@Column(name="bat_attr_exp")
	 public String getBatAttrExp() {
		return this.batAttrExp;
	}
	public void setBatAttrExp(String batAttrExp) {
		this.batAttrExp = batAttrExp;
	}
	
	public static MilitaryRankPo findEntity(Integer id){
		return findRealEntity(MilitaryRankPo.class,id);
	}
	@JSONField(serialize = false)
	public List<List<Integer>> listBatAttrExp =new ArrayList<List<Integer>>();
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listBatAttrExp=ExpressUtil.buildBattleExpressList(batAttrExp);
			unChanged();
			loaded =true;
		}
	}
	


}
