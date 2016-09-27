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
	 * @author null
	 * @version 
	 */
	@Entity
	@Table(name = "po_vip")
	public class VipPo extends BaseGameDBPo {
	/**
	*主键
	**/
	private Integer id;
	/**
	*vip等级VIP等级
	**/
	private Integer vipLv;
	/**
	*福利类型 1.竞技场|次数
					2.通天塔|次数
					3.PK之王|次数
					4.魔化危机|次数
					5.恶灵禁地|次数
					6.血魔堡垒|次数
					7.守护水晶|次数
					8.日常任务一键完成|开启
					9.购买商品|商品ID
					10.物资车刷新|次数
					11.每天死亡后免费复活|次数
					12.强化装备成功率|百分比
					13.运镖|次数
					14.每日登录|道具ID
					15.一键橙车|1是可以 0是不可以
	**/
	private String vipPrivilege;
	/**
	*所需充值RMBrmb数量
	**/
	private Integer rmbNeed;
	/**
	*描述无
	**/
	private String tips;
	
	/**
	 * vip礼包
	 */
	private Integer vipAward;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="vip_lv")
	 public Integer getVipLv() {
		return this.vipLv;
	}
	public void setVipLv(Integer vipLv) {
		this.vipLv = vipLv;
	}

	@Column(name="vip_privilege")
	 public String getVipPrivilege() {
		return this.vipPrivilege;
	}
	public void setVipPrivilege(String vipPrivilege) {
		this.vipPrivilege = vipPrivilege;
	}

	@Column(name="rmb_need")
	 public Integer getRmbNeed() {
		return this.rmbNeed;
	}
	public void setRmbNeed(Integer rmbNeed) {
		this.rmbNeed = rmbNeed;
	}

	@Column(name="tips")
	 public String getTips() {
		return this.tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
	
	@Column(name="vip_award")
	public Integer getVipAward() {
		return vipAward;
	}
	public void setVipAward(Integer vipAward) {
		this.vipAward = vipAward;
	}
	public static VipPo findEntity(Integer id){
		return findRealEntity(VipPo.class,id);
	}
	
	/**
	 * Vip特权列表
	 */
	public List<IdNumberVo> listVipPrivilege = new ArrayList<IdNumberVo>();

	public void loadData(BasePo basePo) {
		
		if(loaded==false){
			unChanged();
			listVipPrivilege = IdNumberVo.createList(vipPrivilege);
			loaded =true;
		}
	}
	
	
	/**
	 * 根据类型取得类型对应的数量
	 * @param lv vip等级
	 * @return
	 */
	public Integer fetchTypeNumByType(Integer type){
		int num = 0;
		for(IdNumberVo inv : listVipPrivilege){
			if(type.intValue() == inv.getId().intValue()){
				num = inv.getNum();
				break;
			}
		}
		return num;
	}
}
