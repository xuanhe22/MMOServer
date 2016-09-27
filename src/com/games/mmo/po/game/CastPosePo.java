
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
	@Table(name = "po_cast_pose")
	public class CastPosePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*子触发技能编号无备注
	**/

	private Integer subSkillId;
	/**
	*持续总毫秒无备注
	**/

	private Integer durationMs;
	/**
	*触发次数无备注
	**/

	private String trigTimes;
	/**
	*可否移动无备注
	**/

	private Integer avaMove;
	/**
	*视觉效果无备注
	**/

	private String visuals;
	/**
	*模型动作无备注
	**/

	private String modelMotion;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="sub_skill_id")
	 public Integer getSubSkillId() {
		return this.subSkillId;
	}
	public void setSubSkillId(Integer subSkillId) {
		this.subSkillId = subSkillId;
	}

	@Column(name="duration_ms")
	 public Integer getDurationMs() {
		return this.durationMs;
	}
	public void setDurationMs(Integer durationMs) {
		this.durationMs = durationMs;
	}

	@Column(name="trig_times")
	 public String getTrigTimes() {
		return this.trigTimes;
	}
	public void setTrigTimes(String trigTimes) {
		this.trigTimes = trigTimes;
	}

	@Column(name="ava_move")
	 public Integer getAvaMove() {
		return this.avaMove;
	}
	public void setAvaMove(Integer avaMove) {
		this.avaMove = avaMove;
	}

	@Column(name="visuals")
	 public String getVisuals() {
		return this.visuals;
	}
	public void setVisuals(String visuals) {
		this.visuals = visuals;
	}

	@Column(name="model_motion")
	 public String getModelMotion() {
		return this.modelMotion;
	}
	public void setModelMotion(String modelMotion) {
		this.modelMotion = modelMotion;
	}

	// castPosePo.setSubSkillId(subSkillId)
	// castPosePo.setDurationMs(durationMs)
	// castPosePo.setTrigTimes(trigTimes)
	// castPosePo.setAvaMove(avaMove)
	// castPosePo.setVisuals(visuals)
	// castPosePo.setModelMotion(modelMotion)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
