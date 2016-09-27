package com.games.mmo.po;

import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import sun.security.util.Cache.EqualByteArray;

import com.games.mmo.dao.RoleDAO;
import com.games.mmo.po.game.ItemPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.BeanUtil;
	/**
	 *
	 * 类功能: 角色装备
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_auction_item")

	public class AuctionItemPo extends BaseUserDBPo {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	/**
	 * 是否装备
	 */
	private Integer equipId=0;
	
	/**
	 * 道具ID
	 */
	private Integer itemId=0;

	/**
	 * 数量
	 */
	private Integer num=0;
	
	/**
	 * 售卖总价
	 */
	private Integer totalPrice=0;

	/**
	 * 上架时间
	 */
	private Long sellTime;
	
	/**
	 * 出售者ID
	 */
	private Integer sellerRoleId;
	
	/**
	 * 出售者名字
	 */
	private String sellerRoleName;
	
	private String sellItemName;
	
	/**
	 * 出售物品的小类
	 */
	private Integer sellItemCategory;
	
	/**
	 * 出售物品的大类
	 */
	private Integer sellItemType;
	
	/**
	 * 物品使用等级
	 */
	private Integer requireLv;
	
	/** 出售装备强化等级 */
	private Integer sellEquipPowerLv;
	
	/** 出售截止时间 */
	private Long sellExpirationTime;
	
	/**
     * 职业匹配
     */
    private Integer matchClass=0;
	
	public EqpPo eqpPo;
	
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

	@Column(name="equip_id")
	public Integer getEquipId() {
		return equipId;
	}
	public void setEquipId(Integer equipId) {
		changed("equip_id",equipId,this.equipId);
		this.equipId = equipId;
	}
	
	@Column(name="num")
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		changed("num",num,this.num);
		this.num = num;
	}
	
	@Column(name="total_price")
	public Integer getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Integer totalPrice) {
		changed("total_price",totalPrice,this.totalPrice);
		this.totalPrice = totalPrice;
	}
	
	@Column(name="sell_time")
	public Long getSellTime() {
		return sellTime;
	}
	public void setSellTime(Long sellTime) {
		changed("sell_time",sellTime,this.sellTime);
		this.sellTime = sellTime;
	}
	
	@Column(name="seller_role_id")
	public Integer getSellerRoleId() {
		return sellerRoleId;
	}
	public void setSellerRoleId(Integer sellerRoleId) {
		changed("seller_role_id",sellerRoleId,this.sellerRoleId);
		this.sellerRoleId = sellerRoleId;
	}
	
	@Column(name="seller_role_name")
	public String getSellerRoleName() {
		return sellerRoleName;
	}
	public void setSellerRoleName(String sellerRoleName) {
		changed("seller_role_name",sellerRoleName,this.sellerRoleName);
		this.sellerRoleName = sellerRoleName;
	}
	
	
	
	@Column(name="sell_item_name")
	public String getSellItemName() {
		return sellItemName;
	}
	public void setSellItemName(String sellItemName) {
		changed("sell_item_name",sellItemName,this.sellItemName);
		this.sellItemName = sellItemName;
	}
	
	
	@Column(name="sell_item_category")
	public Integer getSellItemCategory() {
		return sellItemCategory;
	}
	public void setSellItemCategory(Integer sellItemCategory) {
		changed("sell_item_category",sellItemCategory,this.sellItemCategory);
		this.sellItemCategory = sellItemCategory;
	}
	
	
	@Column(name="sell_item_type")
	public Integer getSellItemType() {
		return sellItemType;
	}
	public void setSellItemType(Integer sellItemType) {
		changed("sell_item_type",sellItemType,this.sellItemType);
		this.sellItemType = sellItemType;
	}
	
	
	@Column(name="require_lv")
	public Integer getRequireLv() {
		return requireLv;
	}
	public void setRequireLv(Integer requireLv) {
		changed("require_lv",requireLv,this.requireLv);
		this.requireLv = requireLv;
	}

	@Column(name="sell_equip_power_lv")
	public Integer getSellEquipPowerLv() {
		return sellEquipPowerLv;
	}
	public void setSellEquipPowerLv(Integer sellEquipPowerLv) {
		changed("sell_equip_power_lv",sellEquipPowerLv,this.sellEquipPowerLv);
		this.sellEquipPowerLv = sellEquipPowerLv;
	}
	
	
	@Column(name="sell_expiration_time")
	public Long getSellExpirationTime() {
		return sellExpirationTime;
	}
	public void setSellExpirationTime(Long sellExpirationTime) {
		changed("sell_expiration_time",sellExpirationTime,this.sellExpirationTime);
		this.sellExpirationTime = sellExpirationTime;
	}
	
	
	@Column(name="match_class")
	public Integer getMatchClass() {
		return matchClass;
	}
	public void setMatchClass(Integer matchClass) {
		changed("match_class",matchClass,this.matchClass);
		this.matchClass = matchClass;
	}
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loaded =true;
		}
	}
	
	@Override
	public void saveData() {
		
	}
	
	public static AuctionItemPo findEntity(Integer id){
		return findRealEntity(AuctionItemPo.class,id);
	}
	
	
	public ItemPo itemPo(){
		return ItemPo.findEntity(itemId);
	}

	public EqpPo eqpPo(){
		return EqpPo.findEntity(equipId);
	}

}
