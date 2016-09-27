
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.games.mmo.vo.ActivityInfoVo;
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
	@Table(name = "po_buff")
	public class BuffPo extends BaseGameDBPo {
	/**
	 * 伤害百分比减免,参数2:百分比值
	 */
	public static final int BUFFER_EFFECT_TYPE_DAMAGE_PERCENTAGE_REDUCE = 1;	
	
	/**
	 * 减免必死伤害,参数2:允许次数
	 */
	public static final int BUFFER_EFFECT_TYPE_BLOCK_DEATH_DAMAGE = 2;	
	/**
	 * 减速百分比,参数2:百分比值
	 */
	public static final int BUFFER_EFFECT_TYPE_MOVEMENT_PREDUCE = 3;	
	
	/**
	 * 无敌
	 */
	public static final int BUFFER_EFFECT_TYPE_DIVINE = 4;
	/**
	 * 定身
	 */
//	public static final int BUFFER_EFFECT_TYPE_NONE_MOVE = 5;
	
	
	
	//5	血量变化百分比{参数1}|变化间隔{参数2}
	public static final int BUFFER_EFFECT_INTERVAL_HP_CHANGE_PERCENTAGE = 5;
	//6	血量变化固定值{参数1}|变化间隔{参数2}
	public static final int BUFFER_EFFECT_INTERVAL_HP_CHANGE_NUM = 6;
	//7	魔法变化百分比{参数1}|变化间隔{参数2}
	public static final int BUFFER_EFFECT_INTERVAL_MP_CHANGE_PERCENTAGE = 7;
	//8	魔法变化固定值{参数1}|变化间隔{参数2}
	public static final int BUFFER_EFFECT_INTERVAL_MP_CHANGE_NUM = 8;

	/**
	*主键
	**/

	private Integer id;
	/**
	*持续视觉特效特效id1,
	特效id2…
	**/

	private String visualsCarring;
	/**
	*持续时间buff持续时间
	值表达式
	**/

	private Integer durationValexp;
	/**
	*图标buff图标
	**/

	private String icon;
	/**
	*名称buff名
	**/

	private String name;
	/**
	*buff效果表达式效果类型|参与1|参数2|参数3…
	示例：（参数支持值表达式）
	2|2000|-1*（getattack()）
	1 掉血
	2 周期掉血
	3 攻击力固定值变化
	4 攻击力百分比变化
	…
	**/

	private String buffEffectsExp;
	/**
	*重叠效果1 直接取消
	2 直接覆盖
	3 时间追加
	4 值叠加,刷新时间追加
	**/

	private Integer overrideWay;
	/**
	*
	类别
	1 无法使用技能
	2 无法移动
	3 流血
	4 冰冻
	5 中毒
	6 减速
	7 无敌
	**/

	private Integer buffType;
	
	private Integer life;
	
	/**
	 * BUFF作用对象多个|分隔（1玩家2宠物3怪物..）
	 */
	private String effectType;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="visuals_carring")
	 public String getVisualsCarring() {
		return this.visualsCarring;
	}
	public void setVisualsCarring(String visualsCarring) {
		this.visualsCarring = visualsCarring;
	}

	@Column(name="duration_valexp")
	 public Integer getDurationValexp() {
		return this.durationValexp;
	}
	public void setDurationValexp(Integer durationValexp) {
		this.durationValexp = durationValexp;
	}

	@Column(name="icon")
	 public String getIcon() {
		return this.icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Column(name="buff_effects_exp")
	 public String getBuffEffectsExp() {
		return this.buffEffectsExp;
	}
	public void setBuffEffectsExp(String buffEffectsExp) {
		this.buffEffectsExp = buffEffectsExp;
	}

	@Column(name="override_way")
	 public Integer getOverrideWay() {
		return this.overrideWay;
	}
	public void setOverrideWay(Integer overrideWay) {
		this.overrideWay = overrideWay;
	}

	@Column(name="buff_type")
	 public Integer getBuffType() {
		return this.buffType;
	}
	public void setBuffType(Integer buffType) {
		this.buffType = buffType;
	}
	@JSONField(serialize=false)
	public List<BufferEffetVo> bufferEffetVos = new ArrayList<BufferEffetVo>();
	
	@Override
	public void loadData(BasePo basePo) {

		if(loaded==false){
			unChanged();
			String[] items = StringUtil.split(this.buffEffectsExp, ",");
			if(items != null && items.length>0){
				bufferEffetVos = new ArrayList<BufferEffetVo>();			
			}
			for (String itemStr : items) {
				BufferEffetVo bufferEffetVo = new BufferEffetVo();
				bufferEffetVo.loadProperty(itemStr, "|");
				bufferEffetVos.add(bufferEffetVo);
			}
			loaded =true;
		}
		
//		buff效果表达式效果类型|参与1|参数2|参数3…
//		示例：（参数支持值表达式）
//		2|2000|-1*（getattack()）
//		1 掉血
//		2 周期掉血
//		3 攻击力固定值变化
//		4 攻击力百分比变化
//		…
		
	}
	
	public static BuffPo findEntity(Integer id){
		return findRealEntity(BuffPo.class,id);
	}
	
	@Column(name="life")
	public Integer getLife() {
		return life;
	}
	public void setLife(Integer life) {
		this.life = life;
	}
	@Column(name="effect_type")
	public String getEffectType() {
		return effectType;
	}
	public void setEffectType(String effectType) {
		this.effectType = effectType;
	}
	@Override
	public String toString() {
		return "BuffPo [id=" + id + ", visualsCarring=" + visualsCarring
				+ ", durationValexp=" + durationValexp + ", icon=" + icon
				+ ", name=" + name + ", buffEffectsExp=" + buffEffectsExp
				+ ", overrideWay=" + overrideWay + ", buffType=" + buffType
				+ ", life=" + life + ", effectType=" + effectType + "]";
	}
	

	
}
