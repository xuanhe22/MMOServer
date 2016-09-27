package com.games.mmo.type;


/**
 * 生物事件类型
 * @author saly.bao
 */
public enum LivingEventType
{
	/**
     * 攻击
     */
    ATTACK(1),
    /**
     * 暴击
     */
    CRITICAL(2),
    /**
     * 死亡
     */
    DEAD(3),
    /**
     * 受击
     */
    TAKE_A_RAP(4),
    /**
     * 生物加载
     */
    LOAD(100),
    ;
    private final byte value;

    private LivingEventType(int value)
    {
        this.value = (byte)value;
    }

    public byte getValue()
    {
        return value;
    }

    public static LivingEventType parse(int value)
    {
    	LivingEventType[] items = LivingEventType.values();
        for (LivingEventType item : items)
        {
            if (item.getValue() == value)
            {
                return item;
            }
        }
        return null;
    }
}
