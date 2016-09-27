package com.games.mmo.po;

import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.games.mmo.po.game.ItemPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.DBFieldUtil;
	/**
	 *
	 * 类功能: 角色装备
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_role_infor")
	public class RoleInforPo extends BaseUserDBPo {
		
	/**
	*主键 
	**/
	private Integer id=1;

	/**
	 * 角色编号
	 */
	private Integer roleId;

	/**
	 * 角色名字
	 */
	private String roleName;

	/**
	 * 角色等级
	 */
	private Integer roleLv;
	
	/**
	 * 最近一次登录时间
	 */
	private Long lastLoginTime=System.currentTimeMillis();
	
	private Integer battlePower;
	
	private Integer career;
	
	public Integer onlineStatus;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	@Column(name="role_id")
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		changed("role_id",roleId,this.roleId);
		this.roleId = roleId;
	}
	
	@Column(name="role_name")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		changed("role_name",roleName,this.roleName);
		this.roleName = roleName;
	}
	
	@Column(name="role_lv")
	public Integer getRoleLv() {
		return roleLv;
	}
	public void setRoleLv(Integer roleLv) {
		changed("role_lv",roleLv,this.roleLv);
		this.roleLv = roleLv;
	}

	
	@Column(name="last_login_time")
	public Long getLastLoginTime() {
		return lastLoginTime;
	}
	
	public void setLastLoginTime(Long lastLoginTime) {
		changed("last_login_time",lastLoginTime,this.lastLoginTime);
		this.lastLoginTime = lastLoginTime;
	}
	
	
	@Column(name="battle_power")
	public Integer getBattlePower() {
		return battlePower;
	}
	public void setBattlePower(Integer battlePower) {
		changed("battle_power",battlePower,this.battlePower);
		this.battlePower = battlePower;
	}
	
	@Column(name="career")
	public Integer getCareer() {
		return career;
	}
	public void setCareer(Integer career) {
		changed("career",career,this.career);
		this.career = career;
	}
	
	
	@Column(name="online_status")
	public Integer getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(Integer onlineStatus) {
		changed("online_status",onlineStatus,this.onlineStatus);
		this.onlineStatus = onlineStatus;
	}

	public static RoleInforPo findEntity(Integer id){
		return findRealEntity(RoleInforPo.class,id);
	}
	
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loaded =true;
		}
	}
	
	@Override
	public String toString() {
		return "RoleInforPo [id=" + id + ", roleId=" + roleId + ", roleName="
				+ roleName + ", roleLv=" + roleLv + ", lastLoginTime="
				+ lastLoginTime + ", battlePower=" + battlePower + ", career="
				+ career + ", onlineStatus=" + onlineStatus + "]";
	}


}
