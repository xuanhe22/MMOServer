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
	@Table(name = "po_redremind")
	public class RedremindPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*出现时刻1 升级
2 登录
3 定时
4 充值
5 消费
6 获得活跃点
7 收到邮件
	**/

	private String appearTime;
	/**
	*父节点id无
	**/

	private String fatherNodes;
	/**
	*子节点id无
	**/

	private String sonNodes;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="appear_time")
	 public String getAppearTime() {
		return this.appearTime;
	}
	public void setAppearTime(String appearTime) {
		this.appearTime = appearTime;
	}

	@Column(name="father_nodes")
	 public String getFatherNodes() {
		return this.fatherNodes;
	}
	public void setFatherNodes(String fatherNodes) {
		this.fatherNodes = fatherNodes;
	}

	@Column(name="son_nodes")
	 public String getSonNodes() {
		return this.sonNodes;
	}
	public void setSonNodes(String sonNodes) {
		this.sonNodes = sonNodes;
	}
	
	public static RedremindPo findEntity(Integer id){
		return findRealEntity(RedremindPo.class,id);
	}

}
