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
	@Table(name = "po_notice")
	public class NoticePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*公告时间时间表达式
	**/

	private String noticeTime;
	/**
	*公告内容中文内容
	**/

	private String noticeInfo;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="notice_time")
	 public String getNoticeTime() {
		return this.noticeTime;
	}
	
	public void setNoticeTime(String noticeTime) {
		this.noticeTime = noticeTime;
	}

	@Column(name="notice_info")
	 public String getNoticeInfo() {
		return this.noticeInfo;
	}
	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}

	public static NoticePo findEntity(Integer id){
		return findRealEntity(NoticePo.class,id);
	}

}
