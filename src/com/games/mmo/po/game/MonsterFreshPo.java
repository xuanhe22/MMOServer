
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
	@Table(name = "po_monster_fresh")
	public class MonsterFreshPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*地图场景编号无备注
	**/

	private Integer sceneId;

	/**
	*怪物编号无备注
	**/

	private Integer monsterId;
	/**
	*怪物刷新间隔秒无备注
	**/

	private Integer freshSeconds;
	
	private Integer x;
	
	private Integer y;
	
	private Integer z;
	
	private Integer groupId;
	
	private Integer sceneUniqId;
	
	private Integer rotation;
	
	private Integer tag;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="scene_id")
	 public Integer getSceneId() {
		return this.sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	@Column(name="monster_id")
	 public Integer getMonsterId() {
		return this.monsterId;
	}
	public void setMonsterId(Integer monsterId) {
		this.monsterId = monsterId;
	}

	@Column(name="fresh_seconds")
	 public Integer getFreshSeconds() {
		return this.freshSeconds;
	}
	public void setFreshSeconds(Integer freshSeconds) {
		this.freshSeconds = freshSeconds;
	}
	
	@Column(name="x")
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	
	@Column(name="y")
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	
	@Column(name="z")
	public Integer getZ() {
		return z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}
	
	@Column(name="group_id")
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@Column(name="scene_uniq_id")
	public Integer getSceneUniqId() {
		return sceneUniqId;
	}
	public void setSceneUniqId(Integer sceneUniqId) {
		this.sceneUniqId = sceneUniqId;
	}
	
	@Column(name="rotation")
	public Integer getRotation() {
		return rotation;
	}
	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	@Column(name="tag")
	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	public static MonsterFreshPo findEntity(Integer id){
		return findRealEntity(MonsterFreshPo.class,id);
	}
	
	// monsterFreshPo.setSceneId(sceneId)
	// monsterFreshPo.setSceneFreshId(sceneFreshId)
	// monsterFreshPo.setMonsterId(monsterId)
	// monsterFreshPo.setFreshSeconds(freshSeconds)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
