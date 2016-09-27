
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.type.RoleType;
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
	@Table(name = "po_lv_config")
	public class LvConfigPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*玩家等级玩家等级为此exp x 2 
	**/

	private Integer playerLevel;
	/**
	*所需经验升级需要的经验
	**/

	private Integer exp;
	
	
	/**
	 * 战士战斗单位id		
	 */
	private String batAttrExp;
	/**
	 * 翅膀经验表
	 */
	private Integer wingExp;
	
	/**
	 * 技能基础技能点
	 */
	private Integer skillBaseSp;
	
	/**
	 * 技能基础金币
	 */
	private Integer skillBaseGold;
	/**
	 * 等级奖励 无奖励时不填
	 */
	private String levelAwrod;
	
	public List<List<Integer>> listBatAttrExp =new ArrayList<List<Integer>>();

	public List<IdNumberVo> listLevelAwrod = new ArrayList<IdNumberVo>();
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	@Column(name="bat_attr_exp")
	public String getBatAttrExp() {
		return batAttrExp;
	}
	
	public void setBatAttrExp(String batAttrExp) {
		this.batAttrExp = batAttrExp;
	}
	
	
	@Column(name="player_level")
	public Integer getPlayerLevel() {
		return playerLevel;
	}
	public void setPlayerLevel(Integer playerLevel) {
		this.playerLevel = playerLevel;
	}
	
	@Column(name="exp")
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	@Column(name="skill_base_sp")
	public Integer getSkillBaseSp() {
		return skillBaseSp;
	}
	public void setSkillBaseSp(Integer skillBaseSp) {
		this.skillBaseSp = skillBaseSp;
	}
	@Column(name="skill_base_gold")
	public Integer getSkillBaseGold() {
		return skillBaseGold;
	}
	public void setSkillBaseGold(Integer skillBaseGold) {
		this.skillBaseGold = skillBaseGold;
	}
	@Column(name="level_awrod")
	public String getLevelAwrod() {
		return levelAwrod;
	}
	public void setLevelAwrod(String levelAwrod) {
		this.levelAwrod = levelAwrod;
	}
	
	public static LvConfigPo findEntity(Integer id){
		return findRealEntity(LvConfigPo.class,id);
	}
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listBatAttrExp=ExpressUtil.buildBattleExpressList(batAttrExp);
			listLevelAwrod = IdNumberVo.createList(levelAwrod);
			unChanged();
			loaded =true;
		}
	}
	
	
}
