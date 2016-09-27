package com.games.mmo.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.po.game.ItemPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.StringUtil;
	/**
	 *
	 * 类功能: 角色装备
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_equip")

	public class EqpPo extends BaseUserDBPo {
		
	/**
	*主键
	**/

	private Integer id;
	/**
	*物品ID无备注
	**/

	private Integer itemId;
	
	/**
	 * 绑定状态
	 */
	private Integer bindStatus=0;
	
	/**
	 * 强化等级
	 */
	private Integer powerLv=0;
	
	private Integer randomCombatUnitId=0;
	
	/**
	 * 装备随机附加属性(装备创建时动态生成,格式（属性ID|属性值,属性ID|属性值,...）)
	 */
	private String attach;
	
	/**
	 * 装备随机附加属性
	 */
	public List<List<Integer>> attachList = new ArrayList<List<Integer>>();
	
	//以上为前端需要关心的字段
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="item_id")
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		changed("item_id",itemId,this.itemId);
		this.itemId = itemId;
	}

	
	@Column(name="bind_status")
	public Integer getBindStatus() {
		return bindStatus;
	}
	
	public void setBindStatus(Integer bindStatus) {
		changed("bind_status",bindStatus,this.bindStatus);
		this.bindStatus = bindStatus;
	}
	
	
	@Column(name="power_lv")
	public Integer getPowerLv() {
		return powerLv;
	}
	public void setPowerLv(Integer powerLv) {
		changed("power_lv",powerLv,this.powerLv);
		this.powerLv = powerLv;
	}
	
	@Column(name="random_combat_unit_id")
	public Integer getRandomCombatUnitId() {
		return randomCombatUnitId;
	}
	
	public void setRandomCombatUnitId(Integer randomCombatUnitId) {
		changed("random_combat_unit_id",randomCombatUnitId,this.randomCombatUnitId);
		this.randomCombatUnitId = randomCombatUnitId;
	}
	@Column(name="attach")
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		changed("attach",attach,this.attach);
		this.attach = attach;
	}

	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			if(!StringUtil.isEmpty(attach))
			{
				String[] strs = StringUtil.split(attach, ",");
				for(String str:strs)
				{
					String[] att = StringUtil.split(str, "|");
					List<Integer> list = new ArrayList<Integer>();
					list.add(Integer.parseInt(att[0]));
					list.add(Integer.parseInt(att[1]));
					attachList.add(list);
				}
			}
			loaded =true;
		}
	}
	
	public static EqpPo findEntity(Integer id){
		return findRealEntity(EqpPo.class,id);
	}
	
	
	public ItemPo itemPo(){
		return ItemPo.findEntity(itemId);
	}
	@Override
	public String toString() {
		return "EqpPo [id=" + id + ", itemId=" + itemId + ", bindStatus="
				+ bindStatus + ", powerLv=" + powerLv + ", randomCombatUnitId="
				+ randomCombatUnitId + ", attach=" + attach + "]";
	}


	public void updateAttach(){
		attachList = new ArrayList<List<Integer>>();
		if(StringUtil.isEmpty(this.attach)){
			return;
		}
		String[] strs = StringUtil.split(attach, ",");
		for(String str:strs)
		{
			String[] att = StringUtil.split(str, "|");
			List<Integer> list = new ArrayList<Integer>();
			list.add(Integer.parseInt(att[0]));
			list.add(Integer.parseInt(att[1]));
			attachList.add(list);
		}
	}
}
