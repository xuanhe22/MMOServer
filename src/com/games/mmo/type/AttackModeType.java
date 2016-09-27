package com.games.mmo.type;

import com.games.mmo.cache.GlobalCache;

/**
 * 攻击模式
 * @author saly.bao
 *
 */
public enum AttackModeType {
	PEACE(0, GlobalCache.fetchLanguageMap("key252")),
	ATTACK(1, "PK"),
	GUILD(2, GlobalCache.fetchLanguageMap("key2703")),
	GOOD_EVIL(3, GlobalCache.fetchLanguageMap("key255")),
	MILITARY_FORCES(4, GlobalCache.fetchLanguageMap("key2300")),
	;

	private int type;
	private String name;
    
    private AttackModeType(int type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public int getType()
    {
        return type;
    }

	public String getName() {
		return name;
	}

	public static AttackModeType parse(int type)
    {
		AttackModeType[] items = AttackModeType.values();
        for (AttackModeType item : items)
        {
            if (item.getType() == type)
            {
                return item;
            }
        }
        return null;
    }
}
