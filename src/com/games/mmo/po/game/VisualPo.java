
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
	@Table(name = "po_visual")
	public class VisualPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*模型骨骼路径无
	**/

	private String bonePath;
	/**
	*效果名称无
	**/

	private String name;
	/**
	*profab名字无
	**/

	private String profab;
	/**
	*移动方式1=原地2=跟随骨骼
	**/

	private Integer moveWay;
	
	/**
	 * 延迟
	 */
	private Integer soundDelay;

	/**
	 * 缓存数量
	 */
	private Integer cacheCount;
	
	
	private Integer visualTime;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="bone_path")
	 public String getBonePath() {
		return this.bonePath;
	}
	public void setBonePath(String bonePath) {
		this.bonePath = bonePath;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="profab")
	 public String getProfab() {
		return this.profab;
	}
	public void setProfab(String profab) {
		this.profab = profab;
	}

	@Column(name="move_way")
	 public Integer getMoveWay() {
		return this.moveWay;
	}
	public void setMoveWay(Integer moveWay) {
		this.moveWay = moveWay;
	}

	@Column(name="sound_delay")
	public Integer getSoundDelay() {
		return soundDelay;
	}
	public void setSoundDelay(Integer soundDelay) {
		this.soundDelay = soundDelay;
	}
	
	
	@Column(name="cache_count")
	public Integer getCacheCount() {
		return cacheCount;
	}
	public void setCacheCount(Integer cacheCount) {
		this.cacheCount = cacheCount;
	}
	
	
	@Column(name="visual_time")
	public Integer getVisualTime() {
		return visualTime;
	}
	public void setVisualTime(Integer visualTime) {
		this.visualTime = visualTime;
	}
	public static VisualPo findEntity(Integer id){
		return findRealEntity(VisualPo.class,id);
	}
	
	// visualPo.setBonePath(bonePath)
	// visualPo.setName(name)
	// visualPo.setProfab(profab)
	// visualPo.setMoveWay(moveWay)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
