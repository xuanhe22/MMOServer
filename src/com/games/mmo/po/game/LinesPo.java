
package com.games.mmo.po.game;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Table(name = "po_lines")
	public class LinesPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*触发关卡ID在哪一关开始播放
	**/

	private Integer stageId;
	/**
	*说话的NPC头像逗号前面的数字，如果填0，表示固定使用后面那个ID的英雄作为头像
	**/

	private String npcIcon;
	/**
	*说话的NPC名字填名字
	**/

	private String npcName;
	/**
	*说话的队伍顺位表示使用当前英雄队列里，所填数字的那个位置的英雄，作为说话NPC的名字和头像
	**/

	private Integer teamNpc;
	/**
	*台词顺位从哪一条文本开始播
	**/

	private Integer textOrder;
	/**
	*播放文本点1表示开头播放，2表示结尾播放
	**/

	private Integer actionPoint;
	/**
	*NPC头像位置1表示左边，2表示右边
	**/

	private Integer iconLocation;
	/**
	*台词文本具体信息
	**/

	private String lines;
	
	private Integer eventId;
	
	private Integer npcSound;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="stage_id")
	 public Integer getStageId() {
		return this.stageId;
	}
	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	@Column(name="npc_icon")
	 public String getNpcIcon() {
		return this.npcIcon;
	}
	public void setNpcIcon(String npcIcon) {
		this.npcIcon = npcIcon;
	}

	@Column(name="npc_name")
	 public String getNpcName() {
		return this.npcName;
	}
	public void setNpcName(String npcName) {
		this.npcName = npcName;
	}

	@Column(name="team_npc")
	 public Integer getTeamNpc() {
		return this.teamNpc;
	}
	public void setTeamNpc(Integer teamNpc) {
		this.teamNpc = teamNpc;
	}

	@Column(name="text_order")
	 public Integer getTextOrder() {
		return this.textOrder;
	}
	public void setTextOrder(Integer textOrder) {
		this.textOrder = textOrder;
	}

	@Column(name="action_point")
	 public Integer getActionPoint() {
		return this.actionPoint;
	}
	public void setActionPoint(Integer actionPoint) {
		this.actionPoint = actionPoint;
	}

	@Column(name="icon_location")
	 public Integer getIconLocation() {
		return this.iconLocation;
	}
	public void setIconLocation(Integer iconLocation) {
		this.iconLocation = iconLocation;
	}

	@Column(name="lines")
	 public String getLines() {
		return this.lines;
	}
	public void setLines(String lines) {
		this.lines = lines;
	}
	
	@Column(name="event_id")
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	
	@Column(name="npc_sound")
	public Integer getNpcSound() {
		return npcSound;
	}
	public void setNpcSound(Integer npcSound) {
		this.npcSound = npcSound;
	}
	
	
	
	

	// linesPo.setStageId(stageId)
	// linesPo.setNpcIcon(npcIcon)
	// linesPo.setNpcName(npcName)
	// linesPo.setTeamNpc(teamNpc)
	// linesPo.setTextOrder(textOrder)
	// linesPo.setActionPoint(actionPoint)
	// linesPo.setIconLocation(iconLocation)
	// linesPo.setLines(lines)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
