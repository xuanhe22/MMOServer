package com.games.mmo.po.game;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.service.MailService;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.RankType;
import com.games.mmo.type.RoleType;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.RankVo;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
	/**
	 *
	 * 类功能: 运营活动
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_static_live_activity")

	public class StaticLiveActivityPo extends BaseGameDBPo{
		
	/**
	*主键
	**/

	private Integer id;
	
	/**
	 * 名字
	 */
	private String name;
	
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 可完成次数
	 */
	private String loopFinishTimes;
	
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 起始时间
	 */
	
	private Long startTime=0l;
	
	/**
	 * 结束时间
	 */
	private Long endTime=0l;
	
	/**
	 * 时效文本描述
	 */
	private String timeTxt="";
	
	/**
	 * 目录
	 */
	private Integer category=0;
	
	/**
	 * 0=未结算 1=已结算
	 */
	private Integer status=0; 
	
	private Integer rank=0;
	/**
	 * 完成条目列表,文本，进度,道具奖励
	 */
	@JSONField(serialize=false)
	private String conditionItems;
	
	@JSONField(serialize=false)
	private String exchangeItems;
	
	@JSONField(serialize=false)
	private String awardItems;
	
	/**
	 * 角色ID,角色名字,排名
	 */
	@JSONField(serialize=false)
	private String rankItems;
	
	/**
	 * 调节率类型,调节率数值
	 */
	@JSONField(serialize=false)
	private String rateItems;
	
	/**
	 * 条件模式
	 */
	private String conditionModes;
	
	/**
	 * 1=总数 2=每日 3=每周
	 */
	private Integer loopWay;
	
	/**
	 * 奖励列表
	 */
	public List<List<IdNumberVo>> listAwardItems=new ArrayList<List<IdNumberVo>>();
	
	/**
	 * 完成条件
	 */
	public List<IdNumberVo> listConditions=new ArrayList<IdNumberVo>();	
	
	/**
	 * 兑换条件
	 */
	public List<List<IdNumberVo>> listExchangeItems=new ArrayList<List<IdNumberVo>>();	
	
	/**
	 * 排名列表
	 */
	@JSONField(serialize=false)
	public List<RankVo> listRankItems=new ArrayList<RankVo>();
	
	/**
	 * 调节率列表
	 */

	public List<IdNumberVo> listRateItems=new ArrayList<IdNumberVo>();
	
	//以上为前端需要关心的字段
	
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
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="loop_finish_times")
	public String getLoopFinishTimes() {
		return loopFinishTimes;
	}
	public void setLoopFinishTimes(String loopFinishTimes) {
		this.loopFinishTimes = loopFinishTimes;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name="start_time")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	@Column(name="end_time")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="time_txt")
	public String getTimeTxt() {
		return timeTxt;
	}
	public void setTimeTxt(String timeTxt) {
		this.timeTxt = timeTxt;
	}
	
	@Column(name="category")
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	
	
	@Column(name="condition_items")
	public String getConditionItems() {
		return conditionItems;
	}
	public void setConditionItems(String conditionItems) {
		this.conditionItems = conditionItems;
	}
	
	@Column(name="exchange_items")
	public String getExchangeItems() {
		return exchangeItems;
	}
	public void setExchangeItems(String exchangeItems) {
		this.exchangeItems = exchangeItems;
	}
	
	
	@Column(name="award_items")
	public String getAwardItems() {
		return awardItems;
	}
	public void setAwardItems(String awardItems) {
		this.awardItems = awardItems;
	}
	@Column(name="rank_items")
	public String getRankItems() {
		return rankItems;
	}
	public void setRankItems(String rankItems) {
		this.rankItems = rankItems;
	}
	
	@Column(name="rate_items")
	public String getRateItems() {
		return rateItems;
	}
	public void setRateItems(String rateItems) {
		this.rateItems = rateItems;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	@Column(name="rank")
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	
	
	@Column(name="condition_modes")
	public String getConditionModes() {
		return conditionModes;
	}
	
	public void setConditionModes(String conditionModes) {
		this.conditionModes = conditionModes;
	}

	
	@Column(name="loop_way")
	public Integer getLoopWay() {
		return loopWay;
	}
	public void setLoopWay(Integer loopWay) {
		this.loopWay = loopWay;
	}
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loaded =true;
		}
	}


}
