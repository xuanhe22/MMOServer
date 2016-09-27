package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.vo.FashionPriceVo;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

@Entity
@Table(name = "po_fashion")
public class FashionPo extends BaseGameDBPo {
	/**
	*主键
	**/
	private Integer id;
	/**
	 * 时装名
	 */
	private String name;
	/**
	 * 时装模型ID(职业|模型ID,职业|模型ID,职业|模型ID)
	 */
	private String fashionId;
	/**
	 * 时装属性
	 */
	private String batExp;
	/**
	 * 时装价格(天数|货币名|货币数量，天数|货币名|货币数量)
	 */
	private String price;
	/**
	 * 出处描述
	 */
	private String outputDesc;
	/**
	 * 是否可以直接购买（1可以0不可以）
	 */
	private Integer isBuy;
	/**
	 * 时装icon无
	 */
	private String icon;
	
	/**
	 * 是否显示
	 */
	private Integer isShow;
	
	private Integer type;
	
	/**
	 * 外观模型ID
	 */
	@JSONField(serialize=false)
	public List<IdNumberVo> listFashionId = new ArrayList<IdNumberVo>();
	
	/**
	 * 时装属性加成
	 */
	@JSONField(serialize=false)
	public List<List<Integer>> listBatExp = new ArrayList<List<Integer>>();

	
	/**
	 * 时装价格(key:有效时间)
	 */
	@JSONField(serialize=false)
	public List<IdNumberVo2> listPrice = new ArrayList<IdNumberVo2>();
	

	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="fashion_id")
	public String getFashionId() {
		return fashionId;
	}
	public void setFashionId(String fashionId) {
		this.fashionId = fashionId;
	}
	@Column(name="bat_exp")
	public String getBatExp() {
		return batExp;
	}
	public void setBatExp(String batExp) {
		this.batExp = batExp;
	}
	@Column(name="price")
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	@Column(name="output_desc")
	public String getOutputDesc() {
		return outputDesc;
	}
	public void setOutputDesc(String outputDesc) {
		this.outputDesc = outputDesc;
	}
	@Column(name="is_buy")
	public Integer getIsBuy() {
		return isBuy;
	}
	public void setIsBuy(Integer isBuy) {
		this.isBuy = isBuy;
	}
	@Column(name="icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name="is_show")
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public void loadData(BasePo basePo) {
		
		if(loaded==false){
			unChanged();
			listFashionId = IdNumberVo.createList(fashionId);
			listBatExp = ExpressUtil.buildBattleExpressList(batExp);
			listPrice = IdNumberVo2.createList(price);
			loaded =true;
		}
	}
	
	public static FashionPo findEntity(Integer id){
		return findRealEntity(FashionPo.class,id);
	}
	
	public Integer fecthFashionIdByCareer(int career){
		Integer fashionId = 0;
		IdNumberVo idNumberVo = IdNumberVo.findIdNumber(career, listFashionId);
		if(idNumberVo != null){
			fashionId = idNumberVo.getNum();
		}
		return fashionId;
	}
	
}
