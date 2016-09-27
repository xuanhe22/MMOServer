package com.games.mmo.po;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

@Entity
@Table(name = "u_po_product")
public class ProductPo extends BaseUserDBPo{

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

	private Integer totalCountBuyed;
	
	
	private Integer minLv;
	
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 服务器
	 */
	private String servers;
	/**
	 * 创建时间
	 */
	private Long createTime=System.currentTimeMillis();

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
		changed("item_id",itemId,this.itemId);
		this.itemId = itemId;
	}

	@Column(name="item_sell")
	 public String getItemSell() {
		return this.itemSell;
	}
	public void setItemSell(String itemSell) {
		changed("item_sell",itemSell,this.itemSell);
		this.itemSell = itemSell;
	}

	@Column(name="time_off")
	 public String getTimeOff() {
		return this.timeOff;
	}
	public void setTimeOff(String timeOff) {
		changed("time_off",timeOff,this.timeOff);
		this.timeOff = timeOff;
	}

	@Column(name="money_type")
	 public Integer getMoneyType() {
		return this.moneyType;
	}
	public void setMoneyType(Integer moneyType) {
		changed("money_type",moneyType,this.moneyType);
		this.moneyType = moneyType;
	}

	@Column(name="original_price")
	 public Integer getOriginalPrice() {
		return this.originalPrice;
	}
	public void setOriginalPrice(Integer originalPrice) {
		changed("original_price",originalPrice,this.originalPrice);
		this.originalPrice = originalPrice;
	}

	@Column(name="discounts_price")
	 public Integer getDiscountsPrice() {
		return this.discountsPrice;
	}
	public void setDiscountsPrice(Integer discountsPrice) {
		changed("discounts_price",discountsPrice,this.discountsPrice);
		this.discountsPrice = discountsPrice;
	}

	@Column(name="day_count")
	 public String getDayCount() {
		return this.dayCount;
	}
	public void setDayCount(String dayCount) {
		changed("day_count",dayCount,this.dayCount);
		this.dayCount = dayCount;
	}

	@Column(name="buy_count")
	 public Integer getBuyCount() {
		return this.buyCount;
	}
	public void setBuyCount(Integer buyCount) {
		changed("buy_count",buyCount,this.buyCount);
		this.buyCount = buyCount;
	}

	@Column(name="promotions")
	 public Integer getPromotions() {
		return this.promotions;
	}
	public void setPromotions(Integer promotions) {
		changed("promotions",promotions,this.promotions);
		this.promotions = promotions;
	}

	@Column(name="onetime_count")
	 public Integer getOnetimeCount() {
		return this.onetimeCount;
	}
	public void setOnetimeCount(Integer onetimeCount) {
		changed("onetime_count",onetimeCount,this.onetimeCount);
		this.onetimeCount = onetimeCount;
	}

	@Column(name="shop_tab")
	 public Integer getShopTab() {
		return this.shopTab;
	}
	public void setShopTab(Integer shopTab) {
		changed("shop_tab",shopTab,this.shopTab);
		this.shopTab = shopTab;
	}
	
	
	
	@Column(name="buy_viplv")
	public Integer getBuyViplv() {
		return buyViplv;
	}
	public void setBuyViplv(Integer buyViplv) {
		changed("buy_viplv",buyViplv,this.buyViplv);
		this.buyViplv = buyViplv;
	}

	@Column(name="total_count")
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		changed("total_count",totalCount,this.totalCount);
		this.totalCount = totalCount;
	}
	
	

	@Column(name="total_count_buyed")
	public Integer getTotalCountBuyed() {
		return totalCountBuyed;
	}
	public void setTotalCountBuyed(Integer totalCountBuyed) {
		changed("total_count_buyed",totalCountBuyed,this.totalCountBuyed);
		this.totalCountBuyed = totalCountBuyed;
	}
	
	
	
	@Column(name="min_lv")
	public Integer getMinLv() {
		return minLv;
	}
	public void setMinLv(Integer minLv) {
		changed("min_lv",minLv,this.minLv);
		this.minLv = minLv;
	}
	
	@Column(name="lot_number")
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		changed("lot_number", lotNumber, this.lotNumber);
		this.lotNumber = lotNumber;
	}
	
	@Column(name="servers")
	public String getServers() {
		return servers;
	}
	public void setServers(String servers) {
		changed("servers", servers, this.servers);
		this.servers = servers;
	}
	
	@Column(name="create_time")
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		changed("create_time", createTime, this.createTime);
		this.createTime = createTime;
	}

	@JSONField(serialize=false)
	public List<Integer> listDayCount = new ArrayList<Integer>();
	
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			reloadByStrs();
			
			loaded =true;
		}
	}
	
	public void reloadByStrs() {
		listDayCount =DBFieldUtil.getIntegerListBySplitter(dayCount, "|");
	}
	@Override
	public void saveData() {
		setDayCount(DBFieldUtil.implod(listDayCount, "|"));
	}
	
	
	public static ProductPo findEntity(Integer id){
		return findRealEntity(ProductPo.class,id);
	}

	/**
	 * 根据道具id查找商店的商品信息
	 * @param itemId
	 * @return
	 */
	public static ProductPo fetchProductPoByItemId(int itemId, int currencyType) {
		ProductPo productPo = null;
		List<ProductPo> list =BaseDAO.instance().getDBList(ProductPo.class);
		for (ProductPo pp : list) {
			if(pp.getItemId().intValue()==itemId && pp.getMoneyType().intValue() == currencyType){
				productPo = pp;
				break;
			}
		}
		return productPo;
	}

}
