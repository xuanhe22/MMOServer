package com.games.mmo.type;

public class BuffType {
	/**
	 * 百分比值 伤害减少的百分比值，负数就是加成百分比
	 */
	public static final int BUFF_EFFECT_1 = 1;
	/**
	 * 抵伤次数,死亡触发buff的life减少效果
	 */
	public static final int BUFF_EFFECT_2 = 2;
	/**
	 * 百分比值 移动速度减少百分比的值，负数就是加速
	 */
	public static final int BUFF_EFFECT_3 = 3;
	/**
	 * 眩晕
	 */
	public static final int BUFF_EFFECT_4 = 4;
	/**
	 * 血量变化百分比{参数1}|变化间隔{参数2}
	 */
	public static final int BUFF_EFFECT_5 = 5;
	/**
	 * 血量变化固定值{参数1}|变化间隔{参数2}
	 */
	public static final int BUFF_EFFECT_6 = 6;
	/**
	 * 魔法变化百分比{参数1}|变化间隔{参数2}
	 */
	public static final int BUFF_EFFECT_7 = 7;
	/**
	 * 魔法变化固定值{参数1}|变化间隔{参数2}
	 */
	public static final int BUFF_EFFECT_8 = 8;
	/**
	 * 类型{参数1}|值{参数2} 属性增加
	 */
	public static final int BUFF_EFFECT_9 = 9;
	/**
	 * 必定暴击
	 */
	public static final int BUFF_EFFECT_10 = 10;
	public static final int BUFF_EFFECT_11 = 11;
	public static final int BUFF_EFFECT_12 = 12;
	public static final int BUFF_EFFECT_13 = 13;
	public static final int BUFF_EFFECT_14 = 14;
	/**
	 * 触发概率{参数1}|触发条件{参数2}|触发技能{参数3} 概率出发一个技能（参数2:1=攻击,2=暴击，3=死亡）
	 */
	public static final int BUFF_EFFECT_15 = 15;
	/**
	 * 触发概率{参数1}|触发条件{参数2}|转移效果{参数3} 概率转移一个效果（参数2:1=死亡）（参数3:100=转移形象
	 */
	public static final int BUFF_EFFECT_16 = 16;
	/**
	 * 百分比值  技能CD减少
	 */
	public static final int BUFF_EFFECT_17 = 17;
	/**
	 * 百分比值 经验值增加
	 */
	public static final int BUFF_EFFECT_18 = 18;
	
	/**
	 * 假死
	 */
	public static final int BUFF_EFFECT_19 = 19;
	
}
