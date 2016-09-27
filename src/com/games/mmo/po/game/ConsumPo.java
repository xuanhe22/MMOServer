package com.games.mmo.po.game;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.po.RolePo;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.vo.IdNumberVo2;

/**
 * 消费点配置
 * @author peter
 *
 */
@Entity
@Table(name = "po_consum")
public class ConsumPo extends BaseGameDBPo {
	private Integer id;
	/**
	 * 描述
	 */
	public String descripe;
	/**
	 * 消费类型
	 */
	private Integer consumType;
	/**
	 * 消费数量
	 */
	private String consumNum;
	/**
	 * 每天免费数量
	 */
	private Integer dayFreeTime;
	/**
	 * 对应道具
	 */
	private Integer item;
	/**
	 * 是否提示
	 */
	private Integer wasWarn;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="descripe")
	public String getDescripe() {
		return descripe;
	}
	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}
	@Column(name="consum_type")
	public Integer getConsumType() {
		return consumType;
	}
	public void setConsumType(Integer consumType) {
		this.consumType = consumType;
	}
	@Column(name="consum_num")
	public String getConsumNum() {
		return consumNum;
	}
	public void setConsumNum(String consumNum) {
		this.consumNum = consumNum;
	}
	@Column(name="day_free_time")
	public Integer getDayFreeTime() {
		return dayFreeTime;
	}
	public void setDayFreeTime(Integer dayFreeTime) {
		this.dayFreeTime = dayFreeTime;
	}
	@Column(name="item")
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	
	@Column(name="wasWarn")
	public Integer getWasWarn() {
		return wasWarn;
	}
	public void setWasWarn(Integer wasWarn) {
		this.wasWarn = wasWarn;
	}
	
	
	public static ConsumPo findEntity(Integer id){
		return findRealEntity(ConsumPo.class,id);
	}
	
}
