package com.games.mmo.type;

/**
 * BUFF所属模块
 * @author saly.bao
 *
 */
public enum BuffModelType {
	/**
	 * 连斩
	 */
	KILL_NUM(1),
	/**
	 * 宠物技能
	 */
	PET_BUFF(2),
	
	/**
	 * 玩家创建时
	 */
	ROLE_CREATE(3),
	
	/**
	 * 技能BUFF
	 */
	SKILL_BUFF(4),
	/**
	 * 怪物创建
	 */
	NPC_CREATE(5),
	/**
	 * 副本鼓舞
	 */
	COPY_INSPIRE(6),
	/**
	 * 增加经验百分比
	 */
	EXP_PERCENT(7),
	;
	private int type;
    
    private BuffModelType(int type)
    {
        this.type = type;
    }

    public int getType()
    {
        return type;
    }

	public static BuffModelType parse(int type)
    {
		BuffModelType[] items = BuffModelType.values();
        for (BuffModelType item : items)
        {
            if (item.getType() == type)
            {
                return item;
            }
        }
        return null;
    }
}
