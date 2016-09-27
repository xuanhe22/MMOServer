
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.util.ExpressUtil;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_gateway")
	public class GatewayPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*名字无备注
	**/

	private String name;
	/**
	*场景编号无备注
	**/

	private Integer sceneId;
	/**
	*x无备注
	**/

	private Integer x;
	/**
	*y无备注
	**/

	private Integer y;
	/**
	*z无备注
	**/

	private Integer z;
	/**
	*目标场景编号无备注
	**/

	private Integer targetSceneId;
	/**
	*目标场景x无备注
	**/

	private Integer targetSceneX;
	/**
	*目标场景y无备注
	**/

	private Integer targetSceneY;
	/**
	*目标场景z无备注
	**/

	private Integer targetSceneZ;
	
	private Integer type;
	private Integer monsterId;
	private String transferFunction;
	private String color;
	
	private Integer roleToward;


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

	@Column(name="scene_id")
	 public Integer getSceneId() {
		return this.sceneId;
	}
	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	@Column(name="x")
	 public Integer getX() {
		return this.x;
	}
	public void setX(Integer x) {
		this.x = x;
	}

	@Column(name="y")
	 public Integer getY() {
		return this.y;
	}
	public void setY(Integer y) {
		this.y = y;
	}

	@Column(name="z")
	 public Integer getZ() {
		return this.z;
	}
	public void setZ(Integer z) {
		this.z = z;
	}

	@Column(name="target_scene_id")
	 public Integer getTargetSceneId() {
		return this.targetSceneId;
	}
	public void setTargetSceneId(Integer targetSceneId) {
		this.targetSceneId = targetSceneId;
	}

	@Column(name="target_scene_x")
	 public Integer getTargetSceneX() {
		return this.targetSceneX;
	}
	public void setTargetSceneX(Integer targetSceneX) {
		this.targetSceneX = targetSceneX;
	}

	@Column(name="target_scene_y")
	 public Integer getTargetSceneY() {
		return this.targetSceneY;
	}
	public void setTargetSceneY(Integer targetSceneY) {
		this.targetSceneY = targetSceneY;
	}

	@Column(name="target_scene_z")
	 public Integer getTargetSceneZ() {
		return this.targetSceneZ;
	}
	public void setTargetSceneZ(Integer targetSceneZ) {
		this.targetSceneZ = targetSceneZ;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name="monster_id")
	public Integer getMonsterId() {
		return monsterId;
	}
	public void setMonsterId(Integer monsterId) {
		this.monsterId = monsterId;
	}
	
	@Column(name="transfer_function")
	public String getTransferFunction() {
		return transferFunction;
	}
	public void setTransferFunction(String transferFunction) {
		this.transferFunction = transferFunction;
	}
	
	@Column(name="color")
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	@Column(name="role_toward")
	public Integer getRoleToward() {
		return roleToward;
	}
	public void setRoleToward(Integer roleToward) {
		this.roleToward = roleToward;
	}
	
	
	


	// gatewayPo.setName(name)
	// gatewayPo.setSceneId(sceneId)
	// gatewayPo.setX(x)
	// gatewayPo.setY(y)
	// gatewayPo.setZ(z)
	// gatewayPo.setTargetSceneId(targetSceneId)
	// gatewayPo.setTargetSceneX(targetSceneX)
	// gatewayPo.setTargetSceneY(targetSceneY)
	// gatewayPo.setTargetSceneZ(targetSceneZ)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
