
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
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_title")
	public class TitlePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*称号等级无
	**/

	private Integer lv;
	/**
	*称号名无
	**/

	private String name;
	/**
	*称号所需经验填值
	**/

	private Integer exp;
	/**
	*称号图片填图片name
	**/

	private String image;
	/**
	*称号属性填属性表达式
	**/

	private String batAttrExp;
	/**
	 * 称号类型1 成就称号 2 其它称号
	 */
	private Integer type;
	/**
	 * 持续时间时间单位秒
	 */
	private Integer durationVale;

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

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="exp")
	 public Integer getExp() {
		return this.exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}

	@Column(name="image")
	 public String getImage() {
		return this.image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	@Column(name="bat_attr_exp")
	 public String getBatAttrExp() {
		return this.batAttrExp;
	}
	public void setBatAttrExp(String batAttrExp) {
		this.batAttrExp = batAttrExp;
	}
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name="duration_vale")
	public Integer getDurationVale() {
		return durationVale;
	}
	public void setDurationVale(Integer durationVale) {
		this.durationVale = durationVale;
	}

	@JSONField(serialize=false)
	public List<List<Integer>> listBatAttrExp =new ArrayList<List<Integer>>();

	public void loadData(BasePo basePo) {
		if(loaded==false){
			listBatAttrExp=ExpressUtil.buildBattleExpressList(batAttrExp);
			unChanged();
			loaded =true;
		}
	}
	
	public static TitlePo findEntity(Integer id){
		return findRealEntity(TitlePo.class,id);
	}
}
