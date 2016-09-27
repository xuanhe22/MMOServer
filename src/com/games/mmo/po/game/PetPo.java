
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
	@Table(name = "po_pet")
	public class PetPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*名字无
	**/

	private String name;
	/**
	*描述无
	**/

	private String description;
	/**
	*模型无
	**/

	private String model;
	/**
	*技能无
	**/

	private String skills;
	/**
	*类型无
	**/

	private Integer type;
	/**
	*潜力点无
	**/

	private Integer potential;
	/**
	*品质无
	**/

	private Integer quality;
	
	/**
	 * 模型缩放
	 */
	private Integer scaleFactor;
	
	private String icon;
	
	/**
	 * 基础经验
	 */
	private Integer baseExp;
	
	/**
	 * 宠物速度
	 */
	private Integer speed;
	
	/**
	 * 界面缩放
	 */
	private Integer scaleUi;

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

	@Column(name="description")
	 public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="model")
	 public String getModel() {
		return this.model;
	}
	public void setModel(String model) {
		this.model = model;
	}

	@Column(name="skills")
	 public String getSkills() {
		return this.skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}

	@Column(name="type")
	 public Integer getType() {
		return this.type;
	}
	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name="potential")
	 public Integer getPotential() {
		return this.potential;
	}
	public void setPotential(Integer potential) {
		this.potential = potential;
	}

	@Column(name="quality")
	 public Integer getQuality() {
		return this.quality;
	}
	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	
	@Column(name="scale_factor")
	public Integer getScaleFactor() {
		return scaleFactor;
	}
	public void setScaleFactor(Integer scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	
	
	@Column(name="icon")
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name="base_exp")
	public Integer getBaseExp() {
		return baseExp;
	}
	public void setBaseExp(Integer baseExp) {
		this.baseExp = baseExp;
	}
	
	
	@Column(name="speed")
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	
	
	@Column(name="scale_ui")
	public Integer getScaleUi() {
		return scaleUi;
	}
	public void setScaleUi(Integer scaleUi) {
		this.scaleUi = scaleUi;
	}
	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	public static PetPo findEntity(int petId) {
		return findRealEntity(PetPo.class, petId);
	}
}
