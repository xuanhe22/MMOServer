package com.games.mmo.po.game;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@Table(name = "po_robot")
	public class RobotPo  extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/** 名字 */
	private String name;
	
	private Integer career;
	
	private Integer lv;
	
	private Integer power;
	
	private Integer equipWeaponId;
	
	private Integer equipArmorId;
	
	private Integer wingStar;
	
	
	/**
	*战斗表达式无
	**/

	private String batExp;

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
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	@Column(name="career")
	public Integer getCareer() {
		return career;
	}
	public void setCareer(Integer career) {
		this.career = career;
	}
	@Column(name="equip_weapon_id")
	public Integer getEquipWeaponId() {
		return equipWeaponId;
	}
	public void setEquipWeaponId(Integer equipWeaponId) {
		this.equipWeaponId = equipWeaponId;
	}
	@Column(name="equip_armor_id")
	public Integer getEquipArmorId() {
		return equipArmorId;
	}
	public void setEquipArmorId(Integer equipArmorId) {
		this.equipArmorId = equipArmorId;
	}
	@Column(name="wing_star")
	public Integer getWingStar() {
		return wingStar;
	}
	public void setWingStar(Integer wingStar) {
		this.wingStar = wingStar;
	}
	@Column(name="lv")
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}
	@Column(name="power")
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	@Column(name="bat_exp")
	 public String getBatExp() {
		return this.batExp;
	}
	public void setBatExp(String batExp) {
		this.batExp = batExp;
	}

	public List<List<Integer>> listBatExp =new ArrayList<List<Integer>>();
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listBatExp=ExpressUtil.buildBattleExpressList(batExp);
			unChanged();
			loaded =true;
		}
	}
}
