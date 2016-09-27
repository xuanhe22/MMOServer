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
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdStringVo;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_invitation")
	public class InvitationPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*奖励Icon奖励Icon
	**/

	private String invitationIcon;
	/**
	*描述描述
	**/

	private String invitationInfo;
	/**
	*完成条件1=邀请玩家数量，2=玩家贵族等级，3=玩家等级
	**/

	private Integer invitationCondition;
	/**
	*完成数量数量
	**/

	private Integer conditionNumber;
	/**
	*奖励奖励ID|数量
300004054=钻石,300004017=绑钻,300004003=金币，300004002=绑金
	**/

	private String invitationReward;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="invitation_icon")
	 public String getInvitationIcon() {
		return this.invitationIcon;
	}
	public void setInvitationIcon(String invitationIcon) {
		this.invitationIcon = invitationIcon;
	}

	@Column(name="invitation_info")
	 public String getInvitationInfo() {
		return this.invitationInfo;
	}
	public void setInvitationInfo(String invitationInfo) {
		this.invitationInfo = invitationInfo;
	}

	@Column(name="invitation_condition")
	 public Integer getInvitationCondition() {
		return this.invitationCondition;
	}
	public void setInvitationCondition(Integer invitationCondition) {
		this.invitationCondition = invitationCondition;
	}

	@Column(name="condition_number")
	 public Integer getConditionNumber() {
		return this.conditionNumber;
	}
	public void setConditionNumber(Integer conditionNumber) {
		this.conditionNumber = conditionNumber;
	}

	@Column(name="invitation_reward")
	 public String getInvitationReward() {
		return this.invitationReward;
	}
	public void setInvitationReward(String invitationReward) {
		this.invitationReward = invitationReward;
	}

	public static InvitationPo findEntity(Integer id){
		return findRealEntity(InvitationPo.class,id);
	}
	
	@JSONField(serialize=false)
	public List<IdNumberVo2> listInvitationReward = new ArrayList<IdNumberVo2>();
	
	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listInvitationReward = IdNumberVo2.createList(invitationReward);
			unChanged();
			loaded =true;
		}
	}
	
}
