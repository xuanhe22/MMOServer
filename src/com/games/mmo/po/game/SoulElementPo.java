package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.vo.IdNumberVo;

@Entity
@Table(name = "po_soul_element")
public class SoulElementPo extends BaseGameDBPo {
	
	private Integer id;
	private Integer step;
	private Integer lv;
	private Integer exp;
	private String addAtb;
	@Id @GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="step")
	public Integer getStep() {
		return step;
	}
	public void setStep(Integer step) {
		this.step = step;
	}
	
	@Column(name="lv")
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}
	
	@Column(name="exp")
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	
	@Column(name="add_atb")
	public String getAddAtb() {
		return addAtb;
	}
	public void setAddAtb(String addAtb) {
		this.addAtb = addAtb;
	}
	
	@JSONField(serialize = false)
	public ConcurrentHashMap<Integer, List<IdNumberVo>> addAtbMap = new ConcurrentHashMap<Integer, List<IdNumberVo>>();
	
	public void loadData(BasePo basePo){
		if(loaded==false){
			unChanged();
			List<String> addAtbs = DBFieldUtil.getStringListBySplitter(addAtb, ";");
			int cnt = 1;
			for(String addAtb:addAtbs){
				List<IdNumberVo> idNumberVos = IdNumberVo.createList(addAtb);
				addAtbMap.put(cnt, idNumberVos);
				cnt++;
			}
			loaded =true;
		}
	}
	
	public static SoulElementPo findEntityByStepAndLv(Integer step, Integer lv) {
		List<SoulElementPo> soulElementPos = GameDataTemplate.getDataList(SoulElementPo.class);
		SoulElementPo retElementPo = null;
		for(SoulElementPo soulElementPo:soulElementPos){
			if(soulElementPo.getStep().intValue()==step.intValue()&&soulElementPo.getLv().intValue()==lv.intValue()){
				retElementPo = soulElementPo;
				break;
			}
		}
		return retElementPo;
	}
	
	public static List<SoulElementPo> findEntityByStep(Integer step){
		List<SoulElementPo> soulElementPos = GameDataTemplate.getDataList(SoulElementPo.class);
		List<SoulElementPo> retElementPos = new ArrayList<SoulElementPo>();
		for(SoulElementPo soulElementPo:soulElementPos){
			if(soulElementPo.getStep().intValue()==step.intValue()){
				retElementPos.add(soulElementPo);
			}
		}
		return retElementPos;
	}
	@Override
	public String toString() {
		return "SoulElementPo [id=" + id + ", step=" + step + ", lv=" + lv
				+ ", exp=" + exp + ", addAtb=" + addAtb + "]";
	}
	
	
}
