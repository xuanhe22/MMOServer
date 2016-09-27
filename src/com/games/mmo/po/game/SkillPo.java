
package com.games.mmo.po.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.BeanUtil;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_skill")
	public class SkillPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*技能效果暂无
	**/

	private String skillEffectExp;
	/**
	*生命消耗值表达式
	**/

	private Integer hpCostValexp;
	/**
	*法力消耗值表达式
	**/

	private Integer mpCostValexp;
	/**
	*技能类型1,基础伤害技能 2,特殊技能  3，加buff技能
	**/
	private Integer skillType;
	
	/**
	*模型动作无
	**/

	private String modelMotion;
	/**
	*图标无
	**/

	private String icon;
	/**
	*公共组编号无
	**/

	private Integer skillGroup;
	/**
	*名称无
	**/

	private String name;
	/**
	*对施法者自己的技能效果暂无
	**/

	private String skillEffectSelfExp;
	/**
	*技能目标选择表达式 脚本<角色集合>
	**/

	private String skillTargetExp;
	/**
	*音效-触发无
	**/

	private String soundTrig;
	/**
	*音效-命中无
	**/

	private String soundHit;
	/**
	*CD毫秒值表达式
	**/

	private String cdValexp;
	/**
	*视觉特效-触发无
	**/

	private String visualsTrig;
	/**
	*施法距离无
	**/

	private Integer skillRange;
	/**
	*视觉特效-命中无
	**/

	private String visualsHit;
	
	/**
	 * 释放检查目标组	无
	 */
	private Integer skillCheckGroup;
	
	/**
	 * 价值，减益播放受击动作	0=无1=增益2=减益
	 */
	private Integer skillAffact;
	
	/**
	 * 位移速度,否则为0
	 */
	private Integer movementSpeed;
	
	/**
	 * 是否普通攻击
	 */
	private Integer attackSkill=0;
	
	/**
	 * 技能模板
	 */
	private Integer skillTemplateType=0;
	
	private String description;
	
	private String visualsFly;

	/**
	 * 命中特效
	 */
	private String visualsHitTarget;
	
	/**
	 * 播放速度
	 */
	private Integer actionScale;
	
	/**
	 * 位移
	 */
	private String posChange;
	/**
	 * 震屏
	 */
	private String shake;
	/**
	 * 击飞
	 */
	private String requireFly;
	
	/**
	 * 子技能
	 */
	private Integer subSkillId;
	
	private Integer skillLockType;
	
	private Integer poseCastId;
	
	/**
	 * 视觉特效-目标触发特
	 */
	private String visualsTrigTarget;
	
	/**
	 * 等级上限
	 */
	private Integer maxLv;
	
	/**
	 * 不显示动作
	 */
	private Integer notShowMotion;
	
	private Integer blurTime;
	
	private Integer blurStrength;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="skill_effect_exp")
	 public String getSkillEffectExp() {
		return this.skillEffectExp;
	}
	public void setSkillEffectExp(String skillEffectExp) {
		this.skillEffectExp = skillEffectExp;
	}

	@Column(name="hp_cost_valexp")
	 public Integer getHpCostValexp() {
		return this.hpCostValexp;
	}
	public void setHpCostValexp(Integer hpCostValexp) {
		this.hpCostValexp = hpCostValexp;
	}

	@Column(name="mp_cost_valexp")
	 public Integer getMpCostValexp() {
		return this.mpCostValexp;
	}
	public void setMpCostValexp(Integer mpCostValexp) {
		this.mpCostValexp = mpCostValexp;
	}

	@Column(name="skill_type")
	 public Integer getSkillType() {
		return this.skillType;
	}
	public void setSkillType(Integer skillType) {
		this.skillType = skillType;
	}

	@Column(name="model_motion")
	 public String getModelMotion() {
		return this.modelMotion;
	}
	public void setModelMotion(String modelMotion) {
		this.modelMotion = modelMotion;
	}

	@Column(name="icon")
	 public String getIcon() {
		return this.icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name="skill_group")
	 public Integer getSkillGroup() {
		return this.skillGroup;
	}
	public void setSkillGroup(Integer skillGroup) {
		this.skillGroup = skillGroup;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="skill_effect_self_exp")
	 public String getSkillEffectSelfExp() {
		return this.skillEffectSelfExp;
	}
	public void setSkillEffectSelfExp(String skillEffectSelfExp) {
		this.skillEffectSelfExp = skillEffectSelfExp;
	}

	@Column(name="skill_target_exp")
	 public String getSkillTargetExp() {
		return this.skillTargetExp;
	}
	public void setSkillTargetExp(String skillTargetExp) {
		this.skillTargetExp = skillTargetExp;
	}

	@Column(name="sound_trig")
	 public String getSoundTrig() {
		return this.soundTrig;
	}
	public void setSoundTrig(String soundTrig) {
		this.soundTrig = soundTrig;
	}

	@Column(name="sound_hit")
	 public String getSoundHit() {
		return this.soundHit;
	}
	public void setSoundHit(String soundHit) {
		this.soundHit = soundHit;
	}

	@Column(name="cd_valexp")
	 public String getCdValexp() {
		return this.cdValexp;
	}
	public void setCdValexp(String cdValexp) {
		this.cdValexp = cdValexp;
	}

	@Column(name="visuals_trig")
	 public String getVisualsTrig() {
		return this.visualsTrig;
	}
	public void setVisualsTrig(String visualsTrig) {
		this.visualsTrig = visualsTrig;
	}

	@Column(name="skill_range")
	 public Integer getSkillRange() {
		return this.skillRange;
	}
	public void setSkillRange(Integer skillRange) {
		this.skillRange = skillRange;
	}

	@Column(name="visuals_hit")
	 public String getVisualsHit() {
		return this.visualsHit;
	}
	public void setVisualsHit(String visualsHit) {
		this.visualsHit = visualsHit;
	}
	
	
	@Column(name="skill_check_group")
	public Integer getSkillCheckGroup() {
		return skillCheckGroup;
	}
	public void setSkillCheckGroup(Integer skillCheckGroup) {
		this.skillCheckGroup = skillCheckGroup;
	}
	
	@Column(name="skill_affact")
	public Integer getSkillAffact() {
		return skillAffact;
	}
	public void setSkillAffact(Integer skillAffact) {
		this.skillAffact = skillAffact;
	}
	
	
	
	@Column(name="movement_speed")
	public Integer getMovementSpeed() {
		return movementSpeed;
	}
	public void setMovementSpeed(Integer movementSpeed) {
		this.movementSpeed = movementSpeed;
	}
	
	@Column(name="attack_skill")
	public Integer getAttackSkill() {
		return attackSkill;
	}
	public void setAttackSkill(Integer attackSkill) {
		this.attackSkill = attackSkill;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	@Column(name="visuals_fly")
	public String getVisualsFly() {
		return visualsFly;
	}
	public void setVisualsFly(String visualsFly) {
		this.visualsFly = visualsFly;
	}
	
	@Column(name="skill_template_type")
	public Integer getSkillTemplateType() {
		return skillTemplateType;
	}
	public void setSkillTemplateType(Integer skillTemplateType) {
		this.skillTemplateType = skillTemplateType;
	}
	
	@Column(name="visuals_hit_target")
	public String getVisualsHitTarget() {
		return visualsHitTarget;
	}
	public void setVisualsHitTarget(String visualsHitTarget) {
		this.visualsHitTarget = visualsHitTarget;
	}
	
	@Column(name="action_scale")
	public Integer getActionScale() {
		return actionScale;
	}
	public void setActionScale(Integer actionScale) {
		this.actionScale = actionScale;
	}
	
	
	
	@Column(name="pos_change")
	public String getPosChange() {
		return posChange;
	}
	public void setPosChange(String posChange) {
		this.posChange = posChange;
	}
	
	
	@Column(name="shake")
	public String getShake() {
		return shake;
	}
	public void setShake(String shake) {
		this.shake = shake;
	}
	
	@Column(name="require_fly")
	public String getRequireFly() {
		return requireFly;
	}
	public void setRequireFly(String requireFly) {
		this.requireFly = requireFly;
	}
	
	@Column(name="sub_skill_id")
	public Integer getSubSkillId() {
		return subSkillId;
	}
	public void setSubSkillId(Integer subSkillId) {
		this.subSkillId = subSkillId;
	}
	
	
	@Column(name="skill_lock_type")
	public Integer getSkillLockType() {
		return skillLockType;
	}
	public void setSkillLockType(Integer skillLockType) {
		this.skillLockType = skillLockType;
	}
	
	@Column(name="pose_cast_id")
	public Integer getPoseCastId() {
		return poseCastId;
	}
	public void setPoseCastId(Integer poseCastId) {
		this.poseCastId = poseCastId;
	}
	
	@Column(name="max_lv")
	public Integer getMaxLv() {
		return maxLv;
	}
	public void setMaxLv(Integer maxLv) {
		this.maxLv = maxLv;
	}
	
	@Column(name="not_show_motion")
	public Integer getNotShowMotion() {
		return notShowMotion;
	}
	public void setNotShowMotion(Integer notShowMotion) {
		this.notShowMotion = notShowMotion;
	}
	@Column(name="visuals_trig_target")
	public String getVisualsTrigTarget() {
		return visualsTrigTarget;
	}
	public void setVisualsTrigTarget(String visualsTrigTarget) {
		this.visualsTrigTarget = visualsTrigTarget;
	}
	
	
	@Column(name="blur_time")
	public Integer getBlurTime() {
		return blurTime;
	}
	public void setBlurTime(Integer blurTime) {
		this.blurTime = blurTime;
	}
	
	@Column(name="blur_strength")
	public Integer getBlurStrength() {
		return blurStrength;
	}
	public void setBlurStrength(Integer blurStrength) {
		this.blurStrength = blurStrength;
	}
	
	public static SkillPo findEntity(Integer id){
		return findRealEntity(SkillPo.class,id);
	}
	

	
	
	
}
