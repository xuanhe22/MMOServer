
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.util.ExpressUtil;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.DBFieldUtil;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_newbie")
	public class NewbiePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*触发条件具体意义见sheet2
	**/


	private String conditions;
	/**
	*步骤组编号如果中间一步跳过，则跳过整个步骤组
	**/

	private Integer newbieStep;
	/**
	*界面序号根据程序约定的界面序号
	**/

	private Integer interfaceOrder;
	/**
	*可点击区域坐标以可点击区域的左下角为起始坐标
	**/


	private String clickCoordinate;
	/**
	*可点击区域宽高单位像素
	**/

	private String widthHeight;
	/**
	*文字信息文本信息
	**/

	private String descTxt;
	
	/**
	 * 触发器
	 */
	private Integer triggering;

	/**
	 * 配音信息
	 */
	private String newbieSound;
	
	private String deviant;
	
	/**
	 * 触发区域
	 */
	public List<List<Integer>> listTriggeringConditions =new ArrayList<List<Integer>>();
	
	/**
	 * 宽高
	 */
	public List<Integer> listWidthHeight =new ArrayList<Integer>();
	
	/**
	 * 点击区域
	 */
	public List<Integer> listClickCoordinate =new ArrayList<Integer>();
	
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}



	@Column(name="newbie_step")
	 public Integer getNewbieStep() {
		return this.newbieStep;
	}
	
	@Column(name="conditions")
	public String getConditions() {
		return conditions;
	}
	public void setConditions(String conditions) {
		this.conditions = conditions;
	}
	public void setNewbieStep(Integer newbieStep) {
		this.newbieStep = newbieStep;
	}

	@Column(name="interface_order")
	 public Integer getInterfaceOrder() {
		return this.interfaceOrder;
	}
	public void setInterfaceOrder(Integer interfaceOrder) {
		this.interfaceOrder = interfaceOrder;
	}

	@Column(name="click_coordinate")
	 public String getClickCoordinate() {
		return this.clickCoordinate;
	}
	public void setClickCoordinate(String clickCoordinate) {
		this.clickCoordinate = clickCoordinate;
	}

	@Column(name="width_height")
	 public String getWidthHeight() {
		return this.widthHeight;
	}
	public void setWidthHeight(String widthHeight) {
		this.widthHeight = widthHeight;
	}

	@Column(name="desc_txt")
	 public String getDescTxt() {
		return this.descTxt;
	}
	public void setDescTxt(String descTxt) {
		this.descTxt = descTxt;
	}
	
	@Column(name="triggering")
	public Integer getTriggering() {
		return triggering;
	}
	public void setTriggering(Integer triggering) {
		this.triggering = triggering;
	}
	@Column(name="newbie_sound")
	public String getNewbieSound() {
		return newbieSound;
	}
	
	@Column(name="deviant")
	public String getDeviant() {
		return deviant;
	}
	public void setDeviant(String deviant) {
		this.deviant = deviant;
	}
	public void setNewbieSound(String newbieSound) {
		this.newbieSound = newbieSound;
	}
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			listWidthHeight=DBFieldUtil.getIntegerListByCommer(widthHeight);
//			listClickCoordinate=DBFieldUtil.getIntegerListByCommer(clickCoordinate);
			listTriggeringConditions=ExpressUtil.buildBattleExpressList(conditions);
			loaded =true;
		}

	}
}
