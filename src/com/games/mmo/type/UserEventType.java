package com.games.mmo.type;


/**
 * 用户事件类型
 * @author saly.bao
 */
public enum UserEventType
{
    /**
     * 宠物升阶
     */
    PET_UP_GRADE(1),
    /**
     * 宠物星数增加事件
     */
    PET_CONSTELL_STAR(2),
    /**
     * 玩家登陆
     */
    LOGIN(3),
    /**
     * 任务完成（奖励领取）
     */
    TASK_SUCCESS(4),
    /**
     * 副本完成（奖励领取）
     */
    COPY_SCENE(5),
    /**
     * 运镖
     */
    YUN_DART(6),
    ;
    private final byte value;

    private UserEventType(int value)
    {
        this.value = (byte)value;
    }

    public byte getValue()
    {
        return value;
    }

    public static UserEventType parse(int value)
    {
    	UserEventType[] items = UserEventType.values();
        for (UserEventType item : items)
        {
            if (item.getValue() == value)
            {
                return item;
            }
        }
        return null;
    }
}
