package com.games.mmo.po.game;




import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;

	/**
	 *
	 * 类功能: 
	 *
	 * @author null
	 * @version 
	 */
	@Entity
	@Table(name = "po_recharge")
	public class RechargePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*图标名字无
	**/

	private String animName;
	/**
	*渠道类型1.app 2.安卓 3.
	**/

	/**
	 * 充值分组
	 */
	private Integer groupId;
	
	
	private Integer channelType;
	/**
	*rmb金额无
	**/

	private String rechargeRmb;
	/**
	*充值金额单位：钻石
	**/

	private Integer rechargeNum;
	/**
	*赠送金额单位：钻石
	**/

	private Integer attachNum;
	/**
	*商品名称无
	**/

	private String rechargeName;
	
	/**
	 * 苹果商品编号苹果商品编号
	 */
	private String productId;
	
	private String androidId;
	
	private String androidPrice;
	
	private Integer androidNum;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="anim_name")
	 public String getAnimName() {
		return this.animName;
	}
	public void setAnimName(String animName) {
		this.animName = animName;
	}
	@Column(name="group_id")
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	@Column(name="channel_type")
	 public Integer getChannelType() {
		return this.channelType;
	}
	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
	}

	@Column(name="recharge_rmb")
	 public String getRechargeRmb() {
		return this.rechargeRmb;
	}
	public void setRechargeRmb(String rechargeRmb) {
		this.rechargeRmb = rechargeRmb;
	}

	@Column(name="recharge_num")
	 public Integer getRechargeNum() {
		return this.rechargeNum;
	}
	public void setRechargeNum(Integer rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	@Column(name="attach_num")
	 public Integer getAttachNum() {
		return this.attachNum;
	}
	public void setAttachNum(Integer attachNum) {
		this.attachNum = attachNum;
	}

	@Column(name="recharge_name")
	 public String getRechargeName() {
		return this.rechargeName;
	}
	public void setRechargeName(String rechargeName) {
		this.rechargeName = rechargeName;
	}
	@Column(name="product_id")
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	@Column(name="android_id")
	public String getAndroidId() {
		return androidId;
	}
	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}
	@Column(name="android_price")
	public String getAndroidPrice() {
		return androidPrice;
	}
	public void setAndroidPrice(String androidPrice) {
		this.androidPrice = androidPrice;
	}
	@Column(name="android_num")
	public Integer getAndroidNum() {
		return androidNum;
	}
	public void setAndroidNum(Integer androidNum) {
		this.androidNum = androidNum;
	}
	public static RechargePo findEntity(Integer id){
		return findRealEntity(RechargePo.class,id);
	}


	
}
