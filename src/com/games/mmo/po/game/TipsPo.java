package com.games.mmo.po.game;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Table(name = "po_tips")
	public class TipsPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*tips内容无
	**/

	private String tipsContent;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="tips_content")
	 public String getTipsContent() {
		return this.tipsContent;
	}
	public void setTipsContent(String tipsContent) {
		this.tipsContent = tipsContent;
	}

	public static TipsPo findEntity(Integer id){
		return findRealEntity(TipsPo.class,id);
	}
}
