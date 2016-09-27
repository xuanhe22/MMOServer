
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.vo.BufferEffetVo;
import com.games.mmo.type.EffectType;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.StringUtil;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_dic_client")
	public class DicClientPo extends BaseGameDBPo {

	/**
	*主键
	**/

	private Integer id;
	private String key;
	private String zhCn;
	private String zhTw;
	private String vi;
	private String ko;


	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Column(name="zh_cn")
	public String getZhCn() {
		return zhCn;
	}
	public void setZhCn(String zhCn) {
		this.zhCn = zhCn;
	}
	
	@Column(name="zh_tw")
	public String getZhTw() {
		return zhTw;
	}
	public void setZhTw(String zhTw) {
		this.zhTw = zhTw;
	}
	
	@Column(name="vi")
	public String getVi() {
		return vi;
	}
	public void setVi(String vi) {
		this.vi = vi;
	}
	
	@Column(name="ko")
	public String getKo() {
		return ko;
	}
	public void setKo(String ko) {
		this.ko = ko;
	}

	
	
	
}
