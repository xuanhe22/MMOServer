
package com.games.mmo.po.game;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.po.ProductPo;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.vo.IdNumberVo;

	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_static_product")
	public class StaticProductPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*商品对应的道具ID道具的具体效果
	**/

	private Integer itemId;
	/**
	*上架时间道具上架时间
	**/

	private String itemSell;
	/**
	*道具下架时间到达这个时间，道具在商店里显示
	**/

	private String timeOff;
	/**
	*货币种类1.金币。2.钻石。3.绑钻。
	**/

	private Integer moneyType;
	/**
	*道具原始售价原始的货币数量
	**/

	private Integer originalPrice;
	/**
	*道具打折售价打折后的货币数量
	**/

	private Integer discountsPrice;
	/**
	*道具每日限购次数玩家一天可以购买的次数，凌晨3点刷新
	**/

	private String dayCount;
	/**
	*单个玩家购买限制数量0不限
	**/

	private Integer buyCount;
	/**
	*促销标签0.无标签。1.新品。2.热卖。3限时。
	**/

	private Integer promotions;
	/**
	*购买1次的道具数量无
	**/

	private Integer onetimeCount;
	/**
	*类型1.常用类。2.消耗类。3.绑钻类。4.金币类。
	**/

	private Integer shopTab;
	
	private Integer buyViplv;
	
	private Integer totalCount;
	
	private Integer minLv;

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
		return this.itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@Column(name="item_sell")
	 public String getItemSell() {
		return this.itemSell;
	}
	public void setItemSell(String itemSell) {
		this.itemSell = itemSell;
	}

	@Column(name="time_off")
	 public String getTimeOff() {
		return this.timeOff;
	}
	public void setTimeOff(String timeOff) {
		this.timeOff = timeOff;
	}

	@Column(name="money_type")
	 public Integer getMoneyType() {
		return this.moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	@Column(name="original_price")
	 public Integer getOriginalPrice() {
		return this.originalPrice;
	}
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name="discounts_price")
	 public Integer getDiscountsPrice() {
		return this.discountsPrice;
	}
	public void setDiscountsPrice(Integer discountsPrice) {
		this.discountsPrice = discountsPrice;
	}

	@Column(name="day_count")
	 public String getDayCount() {
		return this.dayCount;
	}
	public void setDayCount(String dayCount) {
		this.dayCount = dayCount;
	}

	@Column(name="buy_count")
	 public Integer getBuyCount() {
		return this.buyCount;
	}
	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}

	@Column(name="promotions")
	 public Integer getPromotions() {
		return this.promotions;
	}
	public void setPromotions(Integer promotions) {
		this.promotions = promotions;
	}

	@Column(name="onetime_count")
	 public Integer getOnetimeCount() {
		return this.onetimeCount;
	}
	public void setOnetimeCount(Integer onetimeCount) {
		this.onetimeCount = onetimeCount;
	}

	@Column(name="shop_tab")
	 public Integer getShopTab() {
		return this.shopTab;
	}
	public void setShopTab(Integer shopTab) {
		this.shopTab = shopTab;
	}
	
	
	
	@Column(name="buy_viplv")
	public Integer getBuyViplv() {
		return buyViplv;
	}
	public void setBuyViplv(Integer buyViplv) {
		this.buyViplv = buyViplv;
	}

	@Column(name="total_count")
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name="min_lv")
	public Integer getMinLv() {
		return minLv;
	}
	public void setMinLv(Integer minLv) {
		this.minLv = minLv;
	}


	@JSONField(serialize=false)
	public List<IdNumberVo> listDayCount = new ArrayList<IdNumberVo>();
	
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			listDayCount =IdNumberVo.createList(dayCount, "|", ",");
			loaded =true;
		}
	}
	public static StaticProductPo findEntity(Integer id){
		return findRealEntity(StaticProductPo.class,id);
	}
	

}
