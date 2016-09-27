package com.games.mmo.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseUserDBPo;

@Entity
@Table(name = "u_po_forbid")
public class ForbidPo extends BaseUserDBPo{
	
	private Integer id;
	
	private Integer roleId;
	
	private String roleName;
	
	private Long startTime;
	
	private Long endTime;
	
	private String note;
	
	private Integer type;
	
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
	
	@Column(name="start_time")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		changed("start_time",startTime,this.startTime);
		this.startTime = startTime;
	}
	
	@Column(name="end_time")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		changed("end_time",endTime,this.endTime);
		this.endTime = endTime;
	}
	
	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		changed("note",note,this.note);
		this.note = note;
	}
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		changed("type", type, this.type);
		this.type = type;
	}

	public static ForbidPo findEntity(Integer id){
		return findRealEntity(ForbidPo.class,id);
	}
	


}
