
package com.games.mmo.po.game;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.ExceptionUtil;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_upgrade_skill")
	public class UpgradeSkillPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*技能编号无备注
	**/

	private Integer skillId;
	/**
	*职业无备注
	**/

	private Integer career;
	/**
	*最大等级无备注
	**/

	private Integer maxLv;
	/**
	*解锁等级无备注
	**/

	private Integer unlockRoleLv;
	/**
	*最大等级需要角色等级无备注
	**/

	private Integer maxRequireRoleLv;
	/**
	*花费技能点系数无备注
	**/

	private Integer costSpVar;
	/**
	*花费金钱系数无备注
	**/

	private Integer costGoldVar;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="skill_id")
	 public Integer getSkillId() {
		return this.skillId;
	}
	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}

	@Column(name="career")
	 public Integer getCareer() {
		return this.career;
	}
	public void setCareer(Integer career) {
		this.career = career;
	}

	@Column(name="max_lv")
	 public Integer getMaxLv() {
		return this.maxLv;
	}
	public void setMaxLv(Integer maxLv) {
		this.maxLv = maxLv;
	}

	@Column(name="unlock_role_lv")
	 public Integer getUnlockRoleLv() {
		return this.unlockRoleLv;
	}
	public void setUnlockRoleLv(Integer unlockRoleLv) {
		this.unlockRoleLv = unlockRoleLv;
	}

	@Column(name="max_require_role_lv")
	 public Integer getMaxRequireRoleLv() {
		return this.maxRequireRoleLv;
	}
	public void setMaxRequireRoleLv(Integer maxRequireRoleLv) {
		this.maxRequireRoleLv = maxRequireRoleLv;
	}

	@Column(name="cost_sp_var")
	 public Integer getCostSpVar() {
		return this.costSpVar;
	}
	public void setCostSpVar(Integer costSpVar) {
		this.costSpVar = costSpVar;
	}

	@Column(name="cost_gold_var")
	 public Integer getCostGoldVar() {
		return this.costGoldVar;
	}
	public void setCostGoldVar(Integer costGoldVar) {
		this.costGoldVar = costGoldVar;
	}

	public static UpgradeSkillPo findEntity(Integer id){
		return findRealEntity(UpgradeSkillPo.class,id);
	}
	
	
}
