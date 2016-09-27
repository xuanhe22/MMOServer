
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
	@Table(name = "po_copy_scene")
	public class CopyScenePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*最小开启人数无
	**/

	private Integer minCount;
	/**
	*最大开启人数无
	**/

	private Integer maxCount;
	/**
	*副本名字无
	**/

	private String name;
	/**
	*等待关闭时间无
	**/

	private Integer waitCloseSeconds;
	
	/**
	*副本和掉落描述无
	**/

	private String description;

	/**
	 * 副本类型：1.普通副本 2.剧情副本
	 */
	private Integer copyType;
	
	/**
	 * 是否显示副本信息：1显示； 2不显示
	 */
	private Integer copyInfo;
	
	
	
	private String copyPrompt;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="min_count")
	 public Integer getMinCount() {
		return this.minCount;
	}
	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	@Column(name="max_count")
	 public Integer getMaxCount() {
		return this.maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="wait_close_seconds")
	 public Integer getWaitCloseSeconds() {
		return this.waitCloseSeconds;
	}
	public void setWaitCloseSeconds(Integer waitCloseSeconds) {
		this.waitCloseSeconds = waitCloseSeconds;
	}

	@Column(name="description")
	 public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Column(name="copy_type")
	public Integer getCopyType() {
		return copyType;
	}
	public void setCopyType(Integer copyType) {
		this.copyType = copyType;
	}
	@Column(name="copy_info")
	public Integer getCopyInfo() {
		return copyInfo;
	}
	public void setCopyInfo(Integer copyInfo) {
		this.copyInfo = copyInfo;
	}
	
	
	@Column(name="copy_prompt")
	public String getCopyPrompt() {
		return copyPrompt;
	}
	public void setCopyPrompt(String copyPrompt) {
		this.copyPrompt = copyPrompt;
	}
	public static CopyScenePo findEntity(Integer id){
		return findRealEntity(CopyScenePo.class,id);
	}
	

}
