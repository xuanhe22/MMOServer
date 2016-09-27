
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.flex.roo.addon.asdt.core.internal.antlr.AS3Parser.expression_return;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.EqpPo;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdStringVo;
/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_item")
	public class ItemPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*名字无备注
	**/

	private String name;
	
	/**
	 * 道具描述
	 */
	private String itemDesc;
	
	/**
	 * 职业匹配
	 */
	private Integer matchClass;
	
	/**
	 * 道具等级
	 */
	private Integer itemLv;
	/**
	 * 需要等级
	 */
	private Integer requireLv;
	/**
	*  品质无备注
	**/

	private Integer quality;
	
	/**
	 * 绑定类型
	 */
	private Integer bindType;
	
	/**
	 * 掉落类型
	 */
	private Integer dropType;
	/**
	*  最大重叠无备注
	**/

	private Integer foldMax;
	/**
	*  卖出价格无备注
	**/

	private Integer sellPrice;
	/**
	*  图片无备注
	**/

	private String icon;
	
	/**
	 * 战斗属性表达式
	 */
	private String eqpBatAttrExp;

	/**
	 * 类型
	 */
	private Integer type;
	
	/**
	 * 排序
	 */
	private Integer itemIndex=0;
	
	/**
	 * 小类型
	 */
	private Integer category;
	
	private String itemUseExp;
	private Integer autoUse;

	
	public Integer model;
	
	private String intensifyEffects;
	
	/**
	 * 回收类型
	 */
	private Integer recoveryType;
	
	/**
	 * 怪物ID
	 */
	private Integer monsterId;
	/**
	 * 掉落道具
	 */
	private String dropItems;
	/**
	 * 是否能批量使用
	 */
	private Integer batchUse;
	
	/**
	 * 上电视规则
	 */
	private String dropAnnounce;
	
	/**
	 * 掉落权重
	 */
	private Integer dropWeight;
	
	/** 装备战力（不是数据库读取）*/
	@JSONField(serialize = false)
	public Integer equipPower = 0;
	
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
	
	@Column(name="item_desc")
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	
	@Column(name="match_class")
	public Integer getMatchClass() {
		return matchClass;
	}
	public void setMatchClass(Integer matchClass) {
		this.matchClass = matchClass;
	}
	
	@Column(name="item_lv")
	public Integer getItemLv() {
		return itemLv;
	}
	public void setItemLv(Integer itemLv) {
		this.itemLv = itemLv;
	}
	
	@Column(name="require_lv")
	public Integer getRequireLv() {
		return requireLv;
	}
	public void setRequireLv(Integer requireLv) {
		this.requireLv = requireLv;
	}
	
	@Column(name="quality")
	public Integer getQuality() {
		return quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	
	@Column(name="bind_type")
	public Integer getBindType() {
		return bindType;
	}
	public void setBindType(Integer bindType) {
		this.bindType = bindType;
	}
	
	@Column(name="drop_type")
	public Integer getDropType() {
		return dropType;
	}
	public void setDropType(Integer dropType) {
		this.dropType = dropType;
	}
	
	@Column(name="fold_max")
	public Integer getFoldMax() {
		return foldMax;
	}
	public void setFoldMax(Integer foldMax) {
		this.foldMax = foldMax;
	}
	
	@Column(name="sell_price")
	public Integer getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}
	
	@Column(name="icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Column(name="eqp_bat_attr_exp")
	public String getEqpBatAttrExp() {
		return eqpBatAttrExp;
	}
	public void setEqpBatAttrExp(String eqpBatAttrExp) {
		this.eqpBatAttrExp = eqpBatAttrExp;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	@Column(name="item_index")
	public Integer getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(Integer itemIndex) {
		this.itemIndex = itemIndex;
	}
	
	
	@Column(name="category")
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	

	@Column(name="model")
	public Integer getModel() {
		return model;
	}
	public void setModel(Integer model) {
		this.model = model;
	}


	@Column(name="item_use_exp")
	public String getItemUseExp() {
		return itemUseExp;
	}
	public void setItemUseExp(String itemUseExp) {
		this.itemUseExp = itemUseExp;
	}
	
	@Column(name="auto_use")
	public Integer getAutoUse() {
		return autoUse;
	}
	public void setAutoUse(Integer autoUse) {
		this.autoUse = autoUse;
	}
	
	
	

	@Column(name="intensify_effects")
	public String getIntensifyEffects() {
		return intensifyEffects;
	}
	public void setIntensifyEffects(String intensifyEffects) {
		this.intensifyEffects = intensifyEffects;
	}



	@Column(name="recovery_type")
	public Integer getRecoveryType() {
		return recoveryType;
	}
	public void setRecoveryType(Integer recoveryType) {
		this.recoveryType = recoveryType;
	}


	@Column(name="monster_id")
	public Integer getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(Integer monsterId) {
		this.monsterId = monsterId;
	}


	@Column(name="drop_items")
	public String getDropItems() {
		return dropItems;
	}
	public void setDropItems(String dropItems) {
		this.dropItems = dropItems;
	}

	@Column(name="batch_use")
	public Integer getBatchUse() {
		return batchUse;
	}
	public void setBatchUse(Integer batchUse) {
		this.batchUse = batchUse;
	}

	@Column(name="drop_announce")
	public String getDropAnnounce() {
		return dropAnnounce;
	}
	public void setDropAnnounce(String dropAnnounce) {
		this.dropAnnounce = dropAnnounce;
	}

	@Column(name="drop_weight")
	public Integer getDropWeight() {
		return dropWeight;
	}
	public void setDropWeight(Integer dropWeight) {
		this.dropWeight = dropWeight;
	}


	@JSONField(serialize=false)
	public List<List<Integer>> listEqpRandomCombatUnitIds =new ArrayList<List<Integer>>();

	@JSONField(serialize=false)
	public List<List<Integer>> listItemUseExp =new ArrayList<List<Integer>>();
	@JSONField(serialize=false)
	public List<List<Integer>> listEqpBatAttrExp =new ArrayList<List<Integer>>();
	
	@JSONField(serialize=false)
	public List<IdStringVo> listIntensifyEffects=new ArrayList<IdStringVo>();
	@JSONField(serialize=false)
	public List<IdNumberVo> listDropAnnounce=new ArrayList<IdNumberVo>();
	
	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listEqpBatAttrExp=ExpressUtil.buildBattleExpressList(eqpBatAttrExp);
			listItemUseExp=ExpressUtil.buildBattleExpressList(itemUseExp);
//			listEqpRandomCombatUnitIds = ExpressUtil.buildBattleExpressList(eqpRandomCombatUnitIds);
			listIntensifyEffects=IdStringVo.createList(intensifyEffects);
			listDropAnnounce = IdNumberVo.createList(dropAnnounce);
			unChanged();
			loaded =true;
		}
	}
	public boolean wasEquip() {
		if(type==ItemType.ITEM_TYPE_EQUIP){
			return true;
		}
		return false;
	}
	
	public static ItemPo findEntity(Integer id){
		return findRealEntity(ItemPo.class,id);
	}
	
	
	
	public boolean couldUse() {
		if(listItemUseExp.size()>0){
			return true;
		}
		return false;
	}
	
	public static ItemPo findEntityByName(String string) {
		ItemPo itemPo = GlobalCache.itemNamesMap.get(string);
		if (itemPo == null) {
			System.out.print(string);
		}
		return GlobalCache.itemNamesMap.get(string);
	}
	public boolean wasGem() {
		if(category>=ItemType.ITEM_CATEGORY_GEM_START && category<=ItemType.ITEM_CATEGORY_GEM_END){
			return true;
		}
		return false;
	}
	

	
}
