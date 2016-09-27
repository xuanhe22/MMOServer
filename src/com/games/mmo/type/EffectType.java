package com.games.mmo.type;

/**
 * buff效果类型
 * @author saly.bao
 *
 */
public enum EffectType {

	/**
	 * 属性值加成
	 */
	VALUE(9),
	/**
	 * 反弹
	 */
	REBOUND(14),
	/**
	 * BUFF触发技能
	 */
	BUFF_SKILL(15),
	/**
	 *传递继承 BUFF
	 */
	EXTEND(16),
	EFFECT_TYPE1(1),
	EFFECT_TYPE2(2),
	EFFECT_TYPE3(3),
	EFFECT_TYPE4(4),
	EFFECT_TYPE5(5),
	EFFECT_TYPE6(6),
	EFFECT_TYPE7(7),
	EFFECT_TYPE8(8),
	EFFECT_TYPE10(10),
	EFFECT_TYPE17(17),
	/**
	 * 经验加成百分比
	 */
	EFFECT_TYPE18(18),
    ;
	/**
	 * 1	百分比值	伤害减少的百分比值，负数就是加成百分比
	 * 2	抵伤次数	死亡触发buff的life减少效果
	 * 3	百分比值	移动速度减少百分比的值，负数就是加速
	 * 4	眩晕	无值
	 * 5	血量变化百分比{参数1}|变化间隔{参数2}	
	 * 6	血量变化固定值{参数1}|变化间隔{参数2}	
	 * 7	魔法变化百分比{参数1}|变化间隔{参数2}	
	 * 8	魔法变化固定值{参数1}|变化间隔{参数2}	
	 * 9	属性值加成
	 * 10	必定暴击	无值
	 * 15	触发概率{参数1}|触发条件{参数2}|触发技能{参数3}	概率出发一个技能（参数2:1=攻击,2=暴击，3=死亡）
	 */
    private int type;
    
    private EffectType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

	public static EffectType parse(int type)
    {
    	EffectType[] items = EffectType.values();
        for (EffectType item : items)
        {
            if (item.getType() == type)
            {
                return item;
            }
        }
        return null;
    }
}
