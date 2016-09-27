
package com.games.mmo.po.game;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.bean.Entity;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
	/**
	 *
	 * 类功能: 战斗中的对象选择器
	 *
	 * @author Johnny
	 * @version 
	 */
	@javax.persistence.Entity
	@Table(name = "po_battle_range")
	public class BattleRangePo extends BaseGameDBPo {
		

	/**
	*主键
	**/

	private Integer id;
	/**
	*	范围类型，1表示矩形，2表示扇形无
	**/

	private Integer rangeStyle;
	/**
	*	范围参数根据范围的类型，如果是矩形就是四个参数，如果是扇形就是三个参数，中间用逗号隔开,上下左右,起始角度，结束角度，半径
	**/

	@JSONField(serialize=false)
	private String rangeParameter;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="range_style")
	 public Integer getRangeStyle() {
		return this.rangeStyle;
	}
	public void setRangeStyle(Integer rangeStyle) {
		this.rangeStyle = rangeStyle;
	}

	@Column(name="range_parameter")
	 public String getRangeParameter() {
		return this.rangeParameter;
	}
	public void setRangeParameter(String rangeParameter) {
		this.rangeParameter = rangeParameter;
	}

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
	@JSONField(serialize=false)
	public List<Integer> listBattleRangesParas;

	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			listBattleRangesParas=DBFieldUtil.getIntegerListByCommer(rangeParameter);
			loaded =true;
		}
	}
	

	
	public static BattleRangePo findEntity(Integer id){
		return findRealEntity(BattleRangePo.class,id);
	}
 
}
