
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
	@Table(name = "po_growth")
	public class GrowthPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*内容无
	**/

	private String content;
	
	
	private String name;
	
	/**
	 * 
	 */
	private String growthLv;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="content")
	 public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="growth_lv")
	public String getGrowthLv() {
		return growthLv;
	}
	public void setGrowthLv(String growthLv) {
		this.growthLv = growthLv;
	}
	
	
	

	// growthPo.setContent(content)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
