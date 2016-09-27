
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_copy_scene_conf")
	public class CopySceneConfPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*副本和掉落描述无
	**/

	private String description;
	/**
	*难度无
	**/

	private Integer difficulty;
	/**
	*副本ID无
	**/

	private Integer copySceneId;
	/**
	*单人/队伍无
	**/

	private Integer teamMode;
	/**
	*可参加次数无
	**/

	private Integer avaTimes;
	/**
	*副本参与等级无
	**/

	private Integer requireLv;
	/**
	*掉落显示表 无
	**/

	private String dropShowItems;
	
	/**
	*脚本ID无
	**/

	private String scriptId;
	
	/**
	 * 体力消耗
	 */
	private Integer staminaCost;

	
	/**
	 * 推荐战力
	 */
	private Integer suggestPower;
	
	/**
	 * 通关奖励
	 */
	private String itemDrop;
	
	/**
	 * 翻牌奖励配置翻牌奖励
	 */
	@JSONField(serialize=false)
	private String itemAwardExp;
	
	/**
	 * 开始结束时间文本
	 */
	private String openCloseTimeTxt;
	
	/**
	 * 开始时间程序表达式
	 */
	private String openTimeExp;
	
	/**
	 * 结束时间程序表达式
	 */
	private String closeTimeExp;
	
	/**
	 * 场景地图
	 */
	private Integer sceneId;
	
	private Integer passTime;
	
	/**
	 * 翻牌奖励显示配置翻牌奖励显示
	 */
	private String itemAwardShow;
	
	/**
	 * 副本显示位置0 特殊用途 1 福利本 2 组队本 3 限时活动 4 名人挑战 5 通天塔
	 */
	private Integer type;
	/**
	 * 结算类型0 其它1 经验本结算2 金币本结算3 材料结算4 竞技场结算5组队本
	 */
	private Integer showType;
	
	/**
	 * 副本信息图片名|奖励信息|奖励星数
	 */
	private String copySceneInfo;
	
	/**
	 * 
	 */
	private String refreshTime;
	
	private Integer refreshWay;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="description")
	 public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="difficulty")
	 public Integer getDifficulty() {
		return this.difficulty;
	}
	public void setDifficulty(Integer difficulty) {
		this.difficulty = difficulty;
	}

	@Column(name="copy_scene_id")
	 public Integer getCopySceneId() {
		return this.copySceneId;
	}
	public void setCopySceneId(Integer copySceneId) {
		this.copySceneId = copySceneId;
	}

	@Column(name="team_mode")
	 public Integer getTeamMode() {
		return this.teamMode;
	}
	public void setTeamMode(Integer teamMode) {
		this.teamMode = teamMode;
	}

	@Column(name="ava_times")
	 public Integer getAvaTimes() {
		return this.avaTimes;
	}
	public void setAvaTimes(Integer avaTimes) {
		this.avaTimes = avaTimes;
	}

	@Column(name="require_lv")
	 public Integer getRequireLv() {
		return this.requireLv;
	}
	public void setRequireLv(Integer requireLv) {
		this.requireLv = requireLv;
	}

	@Column(name="drop_show_items")
	 public String getDropShowItems() {
		return this.dropShowItems;
	}
	public void setDropShowItems(String dropShowItems) {
		this.dropShowItems = dropShowItems;
	}
	
	@Column(name="script_id")
	 public String getScriptId() {
		return this.scriptId;
	}
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}
	
	@Column(name="stamina_cost")
	public Integer getStaminaCost() {
		return staminaCost;
	}
	public void setStaminaCost(Integer staminaCost) {
		this.staminaCost = staminaCost;
	}
	
	@Column(name="suggest_power")
	public Integer getSuggestPower() {
		return suggestPower;
	}
	public void setSuggestPower(Integer suggestPower) {
		this.suggestPower = suggestPower;
	}
	
	@Column(name="item_drop")
	public String getItemDrop() {
		return itemDrop;
	}
	public void setItemDrop(String itemDrop) {
		this.itemDrop = itemDrop;
	}
	
	@Column(name="item_award_exp")
	public String getItemAwardExp() {
		return itemAwardExp;
	}
	public void setItemAwardExp(String itemAwardExp) {
		this.itemAwardExp = itemAwardExp;
	}
	
	
	@Column(name="open_close_time_txt")
	public String getOpenCloseTimeTxt() {
		return openCloseTimeTxt;
	}
	public void setOpenCloseTimeTxt(String openCloseTimeTxt) {
		this.openCloseTimeTxt = openCloseTimeTxt;
	}
	
	@Column(name="open_time_exp")
	public String getOpenTimeExp() {
		return openTimeExp;
	}
	public void setOpenTimeExp(String openTimeExp) {
		this.openTimeExp = openTimeExp;
	}
	
	@Column(name="close_time_exp")
	public String getCloseTimeExp() {
		return closeTimeExp;
	}
	public void setCloseTimeExp(String closeTimeExp) {
		this.closeTimeExp = closeTimeExp;
	}
	
	@Column(name="scene_id")
	public Integer getSceneId() {
		return sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}
	
	
	@Column(name="pass_time")
	public Integer getPassTime() {
		return passTime;
	}
	public void setPassTime(Integer passTime) {
		this.passTime = passTime;
	}

	
	
	@Column(name="item_award_show")
	public String getItemAwardShow() {
		return itemAwardShow;
	}
	public void setItemAwardShow(String itemAwardShow) {
		this.itemAwardShow = itemAwardShow;
	}
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	
	@Column(name="show_type")
	public Integer getShowType() {
		return showType;
	}
	public void setShowType(Integer showType) {
		this.showType = showType;
	}
	
	
	@Column(name="copy_scene_info")
	public String getCopySceneInfo() {
		return copySceneInfo;
	}
	public void setCopySceneInfo(String copySceneInfo) {
		this.copySceneInfo = copySceneInfo;
	}
	
	
	
	@Column(name="refresh_time")
	public String getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}


	
	

	@Column(name="refresh_way")
	public Integer getRefreshWay() {
		return refreshWay;
	}
	public void setRefreshWay(Integer refreshWay) {
		this.refreshWay = refreshWay;
	}






	/**
	 * 真实奖励列表
	 */
	@JSONField(serialize=false)
	public List<List<Integer>> listItemAwardExp = new ArrayList<List<Integer>>();
	
	/**
	 * 作假奖励列表
	 */
	@JSONField(serialize=false)
	public List<List<Integer>> listItemAwardShow = new ArrayList<List<Integer>>();

	@JSONField(serialize=false)
	public List<IdNumberVo> listRefreshTime = new ArrayList<IdNumberVo>();

	@JSONField(serialize=false)
	public List<IdNumberVo2> listItemDrop = new ArrayList<IdNumberVo2>();

	@JSONField(serialize=false)
	public Integer cardRandom = 0;
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listRefreshTime=IdNumberVo.createList(refreshTime);
			listItemDrop = IdNumberVo2.createList(itemDrop);
//			listAwardExp = IdNumberVo2.createList(itemAwardExp);
//			if(listAwardExp.size() > 0)
//			{
//				for(IdNumberVo2 awardExp:listAwardExp)
//					cardRandom+=awardExp.getInt3();
//			}
			
			listItemAwardExp =  StringUtil.buildBattleExpressList(itemAwardExp);
			listItemAwardShow = StringUtil.buildBattleExpressList(itemAwardShow);
			unChanged();
			loaded =true;
		}
	}
	
	
	public static CopySceneConfPo findEntity(Integer id){
		return findRealEntity(CopySceneConfPo.class,id);
	}

}
