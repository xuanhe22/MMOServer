package com.games.mmo.po;

import java.lang.reflect.Method;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
@Entity
@Table(name = "u_po_copy_scene_activity")
public class CopySceneActivityPo extends BaseUserDBPo {

	private Integer id;
	private Long beginTime;
	private Long endTime;
	private Integer activityWasOpen;
	private Long beginTimeNext;
	private Long endTimeNext;
	/**
	 * 0 特殊用途
	 *	1 福利本
	 *	2 组队本
	 *	3 限时活动
	 *	4 名人挑战
	 *	5 通天塔
	 */
	private Integer activityType;
	
	
	
	@Id
	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="begin_time")
	public Long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Long beginTime) {
		changed("begin_time",beginTime,this.beginTime);
		this.beginTime = beginTime;
	}
	
	@Column(name="end_time")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		changed("end_time",endTime,this.endTime);
		this.endTime = endTime;
	}
	
	@Column(name="begin_time_next")
	public Long getBeginTimeNext() {
		return beginTimeNext;
	}
	public void setBeginTimeNext(Long beginTimeNext) {
		changed("begin_time_next",beginTimeNext,this.beginTimeNext);
		this.beginTimeNext = beginTimeNext;
	}
	
	@Column(name="end_time_next")
	public Long getEndTimeNext() {
		return endTimeNext;
	}
	public void setEndTimeNext(Long endTimeNext) {
		changed("end_time_next",endTimeNext,this.endTimeNext);
		this.endTimeNext = endTimeNext;
	}
	
	
	@Column(name="activity_was_open")
	public Integer getActivityWasOpen() {
		return activityWasOpen;
	}
	public void setActivityWasOpen(Integer activityWasOpen) {
		changed("activity_was_open",activityWasOpen,this.activityWasOpen);
		this.activityWasOpen = activityWasOpen;
	}



	@Column(name="activity_type")
	public Integer getActivityType() {
		return activityType;
	}
	public void setActivityType(Integer activityType) {
		changed("activity_type",activityType,this.activityType);
		this.activityType = activityType;
	}

	
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();

			loaded =true;
		}
	}

	public static CopySceneActivityPo findEntity(Integer id){
		return findRealEntity(CopySceneActivityPo.class,id);
	}


	@Override
	public String toString() {
		return "CopySceneActivityPo [id=" + id + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + ", activityWasOpen="
				+ activityWasOpen + ", beginTimeNext=" + beginTimeNext
				+ ", endTimeNext=" + endTimeNext + ", activityType="
				+ activityType + "]";
	}

	

}
