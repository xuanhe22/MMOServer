
package com.games.mmo.po.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_story_action")
	public class StoryActionPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*动作id无
	**/

	private Integer actionGroup;
	/**
	*功能类型无
	**/

	private String funcType;
	/**
	*动作类型无
	**/

	private String actType;
	/**
	*特殊类无
	**/

	private String specialValue;
	/**
	*对话文本无
	**/

	private String talkText;
	/**
	*坐标格式为x,y,z
	**/

	private String slot;
	/**
	*角度格式为x,y,z
	**/

	private String angle;
	/**
	*不等待与上一条同时执行
	**/

	private Integer groupId;
	/**
	*持续时间单位:秒
	**/

	private Integer durationMs;
	
	
	private String storySound;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="action_group")
	 public Integer getActionGroup() {
		return this.actionGroup;
	}
	public void setActionGroup(Integer actionGroup) {
		this.actionGroup = actionGroup;
	}

	@Column(name="func_type")
	 public String getFuncType() {
		return this.funcType;
	}
	public void setFuncType(String funcType) {
		this.funcType = funcType;
	}

	@Column(name="act_type")
	 public String getActType() {
		return this.actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}

	@Column(name="special_value")
	 public String getSpecialValue() {
		return this.specialValue;
	}
	public void setSpecialValue(String specialValue) {
		this.specialValue = specialValue;
	}

	@Column(name="talk_text")
	 public String getTalkText() {
		return this.talkText;
	}
	public void setTalkText(String talkText) {
		this.talkText = talkText;
	}

	@Column(name="slot")
	 public String getSlot() {
		return this.slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}

	@Column(name="angle")
	 public String getAngle() {
		return this.angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}


	@Column(name="group_id")
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@Column(name="duration_ms")
	 public Integer getDurationMs() {
		return this.durationMs;
	}
	public void setDurationMs(Integer durationMs) {
		this.durationMs = durationMs;
	}
	
	@Column(name="story_sound")
	public String getStorySound() {
		return storySound;
	}
	public void setStorySound(String storySound) {
		this.storySound = storySound;
	}
	
	

	// storyActionPo.setActionGroup(actionGroup)
	// storyActionPo.setFuncType(funcType)
	// storyActionPo.setActType(actType)
	// storyActionPo.setSpecialValue(specialValue)
	// storyActionPo.setTalkText(talkText)
	// storyActionPo.setSlot(slot)
	// storyActionPo.setAngle(angle)
	// storyActionPo.setDontWait(dontWait)
	// storyActionPo.setDurationMs(durationMs)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
