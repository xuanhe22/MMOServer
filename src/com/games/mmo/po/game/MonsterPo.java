
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
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo2;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_monster")
	public class MonsterPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*名称无备注 
	**/

	private String name;
	/**
	*模型无备注
	**/

	private Integer model;
	
	

	private String skills;
	
	
	private String itemDrop;
	
	
	public Integer lv;
	
	public Integer exp;
	
	/**
	 * npc类型,0=不是NPC 1=无敌NPC 2=可攻击NPC
	 */
	private Integer npcType;
	/**
	 * 小头像
	 */
	private String smallImg;
	/**
	 * 大头像
	 */
	private String bigImg;
	/**
	 * 默认对话文本
	 */
	private String idleText;
	
	/**
	 * NPC id
	 */
	private Integer npcId;
	
	/**
	 * 主动攻击 1=true 0=false
	 */
	private Integer posstiveAttack;
	
	/**
	 * 视野范围
	 */
	private Integer sightRange;
	
	/**
	 *  巡逻范围
	 */
	private Integer patrolRange;
	
	/***
	 * 追踪距离
	 */
	private Integer traceRange;
	
	/**
	 * 掉落方式
	 */
	private Integer slaughterMethod;
	
	private String itemDropTask;
	
	private String batAttrExp;
	
	private String deadSound;
	
	/**
	 * 怪物速度
	 */
	private Integer speed;
	
	/**
	 * 模型缩放
	 */
	private Integer scaleFactor;
	
	/**
	 * 刷新时是否播跑马灯0不播 1播放
	 */
	private Integer horseAbroad;
	
	/**
	 * 0 NPC 1 小怪 2 boss 3 精英
	 */
	private Integer monsterType;
	
	/**
	 * 推荐战力
	 */
	private Integer recommendBattlePower;
	
	/**
	 * 道具显示
	 */
	private String itemDropShow;
	
	/**
	 * boss用途
	 */
	private Integer bossUsage;
	
	public Integer wasAlly;
	
	public Integer category;
	
	/**
	 * 增加buff（buffId1|buffId2）null
	 */
	private String buffs;
	
	/**
	 * 界面缩放
	 */
	private Integer scaleUI;
	
	/** 客户端寻路*/
	private String pathsWay;
	
	/**
	 * 采集消失
	 */
	private Integer pickDisappear;
	
	/**
	 * 采集时间
	 */
	private Integer pickTime;
	

	@JSONField(serialize=false)
	public List<List<Integer>> listBatAttrExp =new ArrayList<List<Integer>>();
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
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="model")
	 public Integer getModel() {
		return this.model;
	}
	public void setModel(Integer model) {
		this.model = model;
	}

	@Column(name="skills")
	 public String getSkills() {
		return this.skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}
	
	@Column(name="item_drop")
	public String getItemDrop() {
		return itemDrop;
	}
	public void setItemDrop(String itemDrop) {
		this.itemDrop = itemDrop;
	}
	
	@Column(name="lv")
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		this.lv = lv;
	}
	
	@Column(name="exp")
	public Integer getExp() {
		return exp;
	}
	public void setExp(Integer exp) {
		this.exp = exp;
	}
	
	@Column(name="npc_type")
	public Integer getNpcType() {
		return npcType;
	}
	public void setNpcType(Integer npcType) {
		this.npcType = npcType;
	}
	
	@Column(name="small_img")
	public String getSmallImg() {
		return smallImg;
	}
	public void setSmallImg(String smallImg) {
		this.smallImg = smallImg;
	}
	
	@Column(name="big_img")
	public String getBigImg() {
		return bigImg;
	}
	public void setBigImg(String bigImg) {
		this.bigImg = bigImg;
	}
	
	@Column(name="idle_text")
	public String getIdleText() {
		return idleText;
	}
	public void setIdleText(String idleText) {
		this.idleText = idleText;
	}
	
	@Column(name="npc_id")
	public Integer getNpcId() {
		return npcId;
	}
	public void setNpcId(Integer npcId) {
		this.npcId = npcId;
	}

	@Column(name="posstive_attack")
	public Integer getPosstiveAttack() {
		return posstiveAttack;
	}
	public void setPosstiveAttack(Integer posstiveAttack) {
		this.posstiveAttack = posstiveAttack;
	}
	
	@Column(name="sight_range")
	public Integer getSightRange() {
		return sightRange;
	}
	public void setSightRange(Integer sightRange) {
		this.sightRange = sightRange;
	}
	
	@Column(name="patrol_range")
	public Integer getPatrolRange() {
		return patrolRange;
	}
	public void setPatrolRange(Integer patrolRange) {
		this.patrolRange = patrolRange;
	}
	
	@Column(name="trace_range")
	public Integer getTraceRange() {
		return traceRange;
	}
	public void setTraceRange(Integer traceRange) {
		this.traceRange = traceRange;
	}

	
	@Column(name="slaughter_method")
	public Integer getSlaughterMethod() {
		return slaughterMethod;
	}
	public void setSlaughterMethod(Integer slaughterMethod) {
		this.slaughterMethod = slaughterMethod;
	}
	@Column(name="item_drop_task")
	public String getItemDropTask() {
		return itemDropTask;
	}
	public void setItemDropTask(String itemDropTask) {
		this.itemDropTask = itemDropTask;
	}

	private String sellItemList;
	
	@Column(name="sell_item_list")
	public String getSellItemList() {
		return sellItemList;
	}
	public void setSellItemList(String sellItemList) {
		this.sellItemList = sellItemList;
	}
	
	
	@Column(name="bat_attr_exp")
	public String getBatAttrExp() {
		return batAttrExp;
	}
	public void setBatAttrExp(String batAttrExp) {
		this.batAttrExp = batAttrExp;
	}
	
	
	@Column(name="dead_sound")
	public String getDeadSound() {
		return deadSound;
	}
	public void setDeadSound(String deadSound) {
		this.deadSound = deadSound;
	}
	
	@Column(name="speed")
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	
	
	
	@Column(name="scale_factor")
	public Integer getScaleFactor() {
		return scaleFactor;
	}
	public void setScaleFactor(Integer scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	
	@Column(name="horse_abroad")
	public Integer getHorseAbroad() {
		return horseAbroad;
	}
	public void setHorseAbroad(Integer horseAbroad) {
		this.horseAbroad = horseAbroad;
	}
	
	
	@Column(name="monster_type")
	public Integer getMonsterType() {
		return monsterType;
	}
	public void setMonsterType(Integer monsterType) {
		this.monsterType = monsterType;
	}
	
	
	@Column(name="recommend_battle_power")
	public Integer getRecommendBattlePower() {
		return recommendBattlePower;
	}
	public void setRecommendBattlePower(Integer recommendBattlePower) {
		this.recommendBattlePower = recommendBattlePower;
	}
	
	@Column(name="item_drop_show")
	public String getItemDropShow() {
		return itemDropShow;
	}
	public void setItemDropShow(String itemDropShow) {
		this.itemDropShow = itemDropShow;
	}
	
	@Column(name="boss_usage")
	public Integer getBossUsage() {
		return bossUsage;
	}	
	public void setBossUsage(Integer bossUsage) {
		this.bossUsage = bossUsage;
	}

	@Column(name="was_ally")
	public Integer getWasAlly() {
		return wasAlly;
	}
	public void setWasAlly(Integer wasAlly) {
		this.wasAlly = wasAlly;
	}

	
	@Column(name="category")
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	
	@Column(name="buffs")
	public String getBuffs() {
		return buffs;
	}
	public void setBuffs(String buffs) {
		this.buffs = buffs;
	}

	@Column(name="scale_ui")
	public Integer getScaleUI() {
		return scaleUI;
	}
	public void setScaleUI(Integer scaleUI) {
		this.scaleUI = scaleUI;
	}
	
	
	@Column(name="pick_time")
	public Integer getPickTime() {
		return pickTime;
	}
	public void setPickTime(Integer pickTime) {
		this.pickTime = pickTime;
	}
	@Column(name="paths_way")
	public String getPathsWay() {
		return pathsWay;
	}
	public void setPathsWay(String pathsWay) {
		this.pathsWay = pathsWay;
	}
	
	
	@Column(name="pick_disappear")
	public Integer getPickDisappear() {
		return pickDisappear;
	}
	public void setPickDisappear(Integer pickDisappear) {
		this.pickDisappear = pickDisappear;
	}

	public List<List<Integer>> listSellItemList = new ArrayList<List<Integer>>();
	
	

	public List<Integer> skillIds = new ArrayList<Integer>();
	public List<IdNumberVo2> listItemDrop = new ArrayList<IdNumberVo2>();
	public List<List<Integer>> listItemDropTask = new ArrayList<List<Integer>>();
	
	/**
	 * 怪物buffID
	 */
	public List<Integer> buffIds = new ArrayList<Integer>();
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listSellItemList=ExpressUtil.buildBattleExpressList(sellItemList);
			listItemDrop = IdNumberVo2.createList(itemDrop);
			listItemDropTask=ExpressUtil.buildBattleExpressList(itemDropTask);
			skillIds=DBFieldUtil.getIntegerListByCommer(skills);
			listBatAttrExp=ExpressUtil.buildBattleExpressList(batAttrExp);
			if(!StringUtil.isEmpty(buffs))
				buffIds=DBFieldUtil.getIntegerListBySplitter(buffs, "|");
			unChanged();
			loaded =true;
		}
		
		
	}
	
	public static MonsterPo findEntity(Integer id){
		return findRealEntity(MonsterPo.class,id);
	}
	
	
	public List<Integer> findNpcSellItem(Integer itemId) {
		for (List<Integer> val : listSellItemList) {
			if(val.get(0)==itemId.intValue()){
				return val;
			}
		}
		return null;
	}
	
	// monsterPo.setName(name)
	// monsterPo.setModel(model)
	// monsterPo.setWeaponType(weaponType)
	// monsterPo.setWeaponEffect(weaponEffect)
	// monsterPo.setHp(hp)
	// monsterPo.setMp(mp)
	// monsterPo.setAttack(attack)
	// monsterPo.setSkills(skills)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
