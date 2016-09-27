package com.games.mmo.vo.role;

import com.games.mmo.po.EqpPo;
import com.games.mmo.po.game.ItemPo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class RolePackItemVo extends BasePropertyVo {
	/**
	 * 背包位置
	 */
	public Integer index=0;
	
	/**
	 * 是否装备
	 */
	public Integer equipId=0;
	
	/**
	 * 道具ID
	 */
	private Integer itemId=0;

	/**
	 * 数量
	 */
	public Integer num=0;
	
	/**
	 * 绑定状态
	 */
	public Integer bindStatus=0;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public Integer getItemId() {
		return itemId;
	}

	public Integer getEquipId() {
		return equipId;
	}

	public void setEquipId(Integer equipId) {
		this.equipId = equipId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getBindStatus() {
		return bindStatus;
	}

	public void setBindStatus(Integer bindStatus) {
		this.bindStatus = bindStatus;
	}

	public boolean wasEquip(){
		return equipId!=0;
	}
	
	public EqpPo eqpPo;
	
	/**
	 * index;equipId;itemId;num;bindStatus;
	 */
	public Object[] fetchProperyItems() {
		return new Object[]{index,equipId,itemId,num,bindStatus};
	}


	@Override
	public void loadProperty(String val,String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		setIndex(DBFieldUtil.fetchImpodInt(vals[0]));
		setEquipId(DBFieldUtil.fetchImpodInt(vals[1]));
		setItemId(DBFieldUtil.fetchImpodInt(vals[2]));
		setNum(DBFieldUtil.fetchImpodInt(vals[3]));
		setBindStatus(DBFieldUtil.fetchImpodInt(vals[4]));
		loadEqp();
	}

	public void loadEqp() {
		eqpPo=EqpPo.findEntity(equipId);
	}

	public ItemPo itemPo() {
		return ItemPo.findEntity(itemId);
	}

	@Override
	public String toString() {
		return "RolePackItemVo [index=" + index + ", equipId=" + equipId
				+ ", itemId=" + itemId + ", num=" + num + ", bindStatus="
				+ bindStatus + "]";
	}
	
}
