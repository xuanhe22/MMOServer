package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.vo.IdNumberVo;

@Entity
@Table(name = "po_soul_step")
public class SoulStepPo extends BaseGameDBPo {

	private Integer id;
	private Integer lv;
	private Integer randomUpCost;
	private Integer stepUpCost;
	private Integer changeExp;
	private String addAtb;
	private String needElementLv;
	private Integer matchValue;
	private Integer randomUpAddExp;
	
	@Id @GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="lv")
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}
	
	@Column(name="random_up_cost")
	public Integer getRandomUpCost() {
		return randomUpCost;
	}
	public void setRandomUpCost(Integer randomUpCost) {
		this.randomUpCost = randomUpCost;
	}
	@Column(name="random_up_add_exp")
	public Integer getRandomUpAddExp() {
		return randomUpAddExp;
	}
	public void setRandomUpAddExp(Integer randomUpAddExp) {
		this.randomUpAddExp = randomUpAddExp;
	}
	@Column(name="step_up_cost")
	public Integer getStepUpCost() {
		return stepUpCost;
	}
	public void setStepUpCost(Integer stepUpCost) {
		this.stepUpCost = stepUpCost;
	}
	@Column(name="change_exp")
	public Integer getChangeExp() {
		return changeExp;
	}
	public void setChangeExp(Integer changeExp) {
		this.changeExp = changeExp;
	}
	@Column(name="add_atb")
	public String getAddAtb() {
		return addAtb;
	}
	public void setAddAtb(String addAtb) {
		this.addAtb = addAtb;
	}
	
	@Column(name="need_element_lv")
	public String getNeedElementLv() {
		return needElementLv;
	}
	public void setNeedElementLv(String needElementLv) {
		this.needElementLv = needElementLv;
	}
	
	@Column(name="match_value")
	public Integer getMatchValue() {
		return matchValue;
	}
	public void setMatchValue(Integer matchValue) {
		this.matchValue = matchValue;
	}
	
	public List<IdNumberVo> addAtbs = new ArrayList<IdNumberVo>();
	public List<Integer> needElementLvs = new ArrayList<Integer>();
	
	public void loadData(BasePo basePo){
		if(loaded==false){
			unChanged();
			addAtbs = IdNumberVo.createList(addAtb);
			needElementLvs = DBFieldUtil.getIntegerListBySplitter(needElementLv, "|");
			loaded = true;
		}
	}
	
	public static SoulStepPo findEntityByStep(Integer step){
		List<SoulStepPo> soulStepPos = GameDataTemplate.getDataList(SoulStepPo.class);
		for(SoulStepPo sp:soulStepPos){
			if(sp.getLv().intValue()==step.intValue()){
				return sp;
			}
		}
		return null;
	}
}
