
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
	@Table(name = "po_story_trigger")
	public class StoryTriggerPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*剧情id无
	**/

	private Integer storyId;
	/**
	*事件类型无
	**/

	private Integer eventType;
	/**
	*条件无
	**/

	private String conditions;
	/**
	*特殊id无
	**/

	private Integer specialId;
	/**
	*动作id无
	**/

	private Integer actionGroup;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="story_id")
	 public Integer getStoryId() {
		return this.storyId;
	}
	public void setStoryId(Integer storyId) {
		this.storyId = storyId;
	}

	@Column(name="event_type")
	 public Integer getEventType() {
		return this.eventType;
	}
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	@Column(name="conditions")
	 public String getConditions() {
		return this.conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	@Column(name="special_id")
	 public Integer getSpecialId() {
		return this.specialId;
	}
	public void setSpecialId(Integer specialId) {
		this.specialId = specialId;
	}

	@Column(name="action_group")
	 public Integer getActionGroup() {
		return this.actionGroup;
	}
	public void setActionGroup(Integer actionGroup) {
		this.actionGroup = actionGroup;
	}

	// storyTriggerPo.setStoryId(storyId)
	// storyTriggerPo.setEventType(eventType)
	// storyTriggerPo.setConditions(conditions)
	// storyTriggerPo.setSpecialId(specialId)
	// storyTriggerPo.setActionGroup(actionGroup)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
