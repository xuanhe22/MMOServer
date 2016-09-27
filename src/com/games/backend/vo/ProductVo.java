package com.games.backend.vo;

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
import com.games.mmo.po.ProductPo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;


public class ProductVo extends BaseVo{

	/**
	*主键
	**/

	public Integer id;

	/**
	*商品对应的道具ID道具的具体效果
	**/

	public Integer itemId;
	/**
	*上架时间道具上架时间
	**/

	public Long itemSell;
	/**
	*道具下架时间到达这个时间，道具在商店里显示
	**/

	public Long timeOff;
	/**
	*货币种类1.金币。2.钻石。3.绑钻。
	**/

	public Integer moneyType;
	/**
	*道具原始售价原始的货币数量
	**/

	public Integer originalPrice;
	/**
	*道具打折售价打折后的货币数量
	**/

	public Integer discountsPrice;
	
	/**
	*单个玩家购买限制数量0不限
	**/

	public Integer buyCount;
	/**
	*促销标签0.无标签。1.新品。2.热卖。3限时。
	**/

	public Integer promotions;
	/**
	*购买1次的道具数量无
	**/

	public Integer onetimeCount;
	/**
	*类型1.常用类。2.消耗类。3.绑钻类。4.金币类。
	**/

	public Integer shopTab;
	
	public Integer buyViplv;
	
	public Integer totalCount;

	public Integer totalCountBuyed;

	public String dayCount;
	
	public Integer minLv;
	
	public String lotNumber;

	public String servers;

	public Long createTime;
	

	public static List<ProductVo> createFromProducts() {
		List<ProductVo> vos =new ArrayList<ProductVo>();
		for (Object productPo : BaseDAO.instance().getDBList(ProductPo.class)) {
			vos.add(fromProduct((ProductPo)productPo));
		}
		return vos;
	}

	public static ProductVo fromProduct(ProductPo productPo) {
		ProductVo productVo =new ProductVo();
		productVo.buyCount=productPo.getBuyCount();
		productVo.buyViplv=productPo.getBuyViplv();
		productVo.discountsPrice=productPo.getDiscountsPrice();
		productVo.id=productPo.getId();
		productVo.itemId=productPo.getItemId();
		productVo.itemSell=Long.valueOf(productPo.getItemSell())*1000;
		productVo.moneyType=productPo.getMoneyType();
		productVo.onetimeCount=productPo.getOnetimeCount();
		productVo.originalPrice=productPo.getOriginalPrice();
		productVo.promotions=productPo.getPromotions();
		productVo.shopTab=productPo.getShopTab();
		productVo.timeOff=Long.valueOf(productPo.getTimeOff())*1000;
		productVo.totalCount=productPo.getTotalCount();
		productVo.dayCount=productPo.getDayCount()==null?"":productPo.getDayCount();
		productVo.minLv=productPo.getMinLv();
		productVo.lotNumber=productPo.getLotNumber();
		productVo.servers=productPo.getServers();
		productVo.createTime=productPo.getCreateTime();
		return productVo; 
	}
	
}
