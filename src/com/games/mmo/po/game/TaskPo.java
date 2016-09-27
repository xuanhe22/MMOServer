
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
import com.storm.lib.vo.IdNumberVo;

	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_task")
	public class TaskPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*任务名无
	**/

	private String name;
	/**
	*描述无
	**/

	private String taskDesc;
	/**
	*类型表达式无
	**/

	private String taskCondition;
	/**
	*奖励包ID无
	**/

	private String taskAwardId;
	/**
	*图标无
	**/

	private String taskIcon;
	/**
	*是任务还是成就无
	**/

	private Integer isTask;
	/**
	    类型1.日常任务 每日激活
		2.成长任务 等级激活
		3.特殊任务 等级激活
		4.战斗成就 创角激活
		5.成长成就 创角激活
		6.挑战成就 创角激活
	**/

	private Integer taskType;
	/**
	*激活等级激活等级
	**/
	@JSONField(serialize=false)
	private String activeLv;
	/**
	*成就类型
	**/

	private Integer achieveType;
	
	/**
	 * 前置完成任务
	 */
	private String activeTasks;
	
	/**
	 * 日常任务获得积分
	 */
	private Integer dailyScore;
	
	/**
	 * 链接
	 */
	private Integer linkMethod;
	
	private Integer startNpcId;
	
	private Integer endNpcId;
	

	private String taskDelivery;
	
	private Integer isAuto;
	
	
	private String taskStartDialog;
	
	private String taskEndDialog;
	
	/**
	 * 完成后开启系统系统id|系统图标|名字图标|说明文字|方向
		1.翅膀 
		2.寄售
		3.升星 
		4.洗练 
		5.宝石 
		6.伙伴 
		7.伙伴获得
		8.伙伴升级
		9.伙伴技能
		10.伙伴培养
		11.伙伴装备
		12.伙伴天赋
		13.公会
		101.解锁技能1
		102.解锁技能2
		103.解锁技能3
		104.解锁技能4
		105.解锁技能5
		106.解锁技能6
	 */
	private String openSystem;
	/**
	 * 任务限时(秒)任务超时会失败（运镖任务专用）
	 */
	private Integer timeLimit;

	private String showText;
	
	private Integer copyId;
	
	/** 新手引导*/
	private Integer leastNewbieStep;
	
	private Integer treasureStart;
	
	private Integer treasureEnd;
	/** 判断活跃度任务是否开启(客户端) */
	private Integer activeTaskCondition;
	
	public List<List<Integer>> listTaskAwardId=new ArrayList<List<Integer>>();
	public List<IdNumberVo> listTaskDelivery=new ArrayList<IdNumberVo>();
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

	@Column(name="task_desc")
	 public String getTaskDesc() {
		return this.taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	@Column(name="task_condition")
	 public String getTaskCondition() {
		return this.taskCondition;
	}
	public void setTaskCondition(String taskCondition) {
		this.taskCondition = taskCondition;
	}

	@Column(name="task_award_id")
	 public String getTaskAwardId() {
		return this.taskAwardId;
	}
	public void setTaskAwardId(String taskAwardId) {
		this.taskAwardId = taskAwardId;
	}

	@Column(name="task_icon")
	 public String getTaskIcon() {
		return this.taskIcon;
	}
	public void setTaskIcon(String taskIcon) {
		this.taskIcon = taskIcon;
	}

	@Column(name="is_task")
	 public Integer getIsTask() {
		return this.isTask;
	}
	public void setIsTask(Integer isTask) {
		this.isTask = isTask;
	}

	@Column(name="task_type")
	 public Integer getTaskType() {
		return this.taskType;
	}
	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	@Column(name="active_lv")
	 public String getActiveLv() {
		return this.activeLv;
	}
	public void setActiveLv(String activeLv) {
		this.activeLv = activeLv;
	}

	@Column(name="achieve_type")
	public Integer getAchieveType() {
		return achieveType;
	}
	public void setAchieveType(Integer achieveType) {
		this.achieveType = achieveType;
	}

	
	@Column(name="daily_score")
	public Integer getDailyScore() {
		return dailyScore;
	}

	public void setDailyScore(Integer dailyScore) {
		this.dailyScore = dailyScore;
	}
	
	@Column(name="link_method")
	public Integer getLinkMethod() {
		return linkMethod;
	}
	public void setLinkMethod(Integer linkMethod) {
		this.linkMethod = linkMethod;
	}
	
	
	@Column(name="active_tasks")
	public String getActiveTasks() {
		return activeTasks;
	}
	public void setActiveTasks(String activeTasks) {
		this.activeTasks = activeTasks;
	}
	
	@Column(name="start_npc_id")
	public Integer getStartNpcId() {
		return startNpcId;
	}
	public void setStartNpcId(Integer startNpcId) {
		this.startNpcId = startNpcId;
	}
	
	@Column(name="end_npc_id")
	public Integer getEndNpcId() {
		return endNpcId;
	}
	public void setEndNpcId(Integer endNpcId) {
		this.endNpcId = endNpcId;
	}
	
	@Column(name="task_delivery")
	public String getTaskDelivery() {
		return taskDelivery;
	}
	public void setTaskDelivery(String taskDelivery) {
		this.taskDelivery = taskDelivery;
	}

	

	@Column(name="is_auto")
	public Integer getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(Integer isAuto) {
		this.isAuto = isAuto;
	}
	
	
	
	@Column(name="task_start_dialog")
	public String getTaskStartDialog() {
		return taskStartDialog;
	}
	public void setTaskStartDialog(String taskStartDialog) {
		this.taskStartDialog = taskStartDialog;
	}
	
	@Column(name="task_end_dialog")
	public String getTaskEndDialog() {
		return taskEndDialog;
	}
	public void setTaskEndDialog(String taskEndDialog) {
		this.taskEndDialog = taskEndDialog;
	}



	@Column(name="open_system")
	public String getOpenSystem() {
		return openSystem;
	}
	public void setOpenSystem(String openSystem) {
		this.openSystem = openSystem;
	}

	@Column(name="time_limit")
	public Integer getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Integer timeLimit) {
		this.timeLimit = timeLimit;
	}

	@Column(name="show_text")
	public String getShowText() {
		return showText;
	}
	public void setShowText(String showText) {
		this.showText = showText;
	}

	@Column(name="copy_id")
	public Integer getCopyId() {
		return copyId;
	}
	public void setCopyId(Integer copyId) {
		this.copyId = copyId;
	}

	@Column(name="least_newbie_step")
	public Integer getLeastNewbieStep() {
		return leastNewbieStep;
	}
	public void setLeastNewbieStep(Integer leastNewbieStep) {
		this.leastNewbieStep = leastNewbieStep;
	}


	@Column(name="treasure_start")
	public Integer getTreasureStart() {
		return treasureStart;
	}
	public void setTreasureStart(Integer treasureStart) {
		this.treasureStart = treasureStart;
	}
	
	@Column(name="treasure_end")
	public Integer getTreasureEnd() {
		return treasureEnd;
	}
	public void setTreasureEnd(Integer treasureEnd) {
		this.treasureEnd = treasureEnd;
	}

	@Column(name="active_task_condition")
	public Integer getActiveTaskCondition() {
		return activeTaskCondition;
	}
	public void setActiveTaskCondition(Integer activeTaskCondition) {
		this.activeTaskCondition = activeTaskCondition;
	}


	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	@JSONField(serialize=false)
	
	/**
	 * 条件参数
	 */
	public List<Integer> conditionVals = new ArrayList<Integer>();
	@JSONField(serialize=false)
	public List<String> openSystemVals = new ArrayList<String>();
	@JSONField(serialize=false)
	public List<Integer> listActiveTasks = new ArrayList<Integer>();
	
	@JSONField(serialize=false)
	public List<Integer> listActiveLv = new ArrayList<Integer>();
	
	public void loadData(BasePo basePo) {
		
		if(loaded==false){
			unChanged();
			conditionVals=DBFieldUtil.getIntegerListBySplitter(taskCondition,"|");
			listTaskAwardId=ExpressUtil.buildBattleExpressList(taskAwardId);
			listActiveTasks=DBFieldUtil.getIntegerListByCommer(activeTasks);
			listTaskDelivery=IdNumberVo.createList(taskDelivery);
			openSystemVals=DBFieldUtil.getStringListBySplitter(openSystem,",");
			listActiveLv = DBFieldUtil.getIntegerListBySplitter(activeLv,"|");
			loaded =true;
		}

	}
	
	public static TaskPo findEntity(Integer id){
		return findRealEntity(TaskPo.class,id);
	}
}
