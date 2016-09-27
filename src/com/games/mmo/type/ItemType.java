package com.games.mmo.type;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.vo.xml.ConstantFile.Global;

public class ItemType {

	public static final int ITEM_BIG_TYPE_ITEM = 2;
	public static final int ITEM_BIG_TYPE_EQUIP = 1;
	public static final int ITEM_BIG_TYPE_AUCTION = 3;
	
	public static final int ITEM_TYPE_EQUIP = 1;
	
	public static final int ITEM_CATEGORY_GEM_START = 40;
	
	public static final int ITEM_CATEGORY_GEM_END = 48;
	/**
	 * 武器
	 */
	public static final int ITEM_CATEGORY_WEAPON = 1;
	/**
	 * 项链
	 */
	public static final int ITEM_CATEGORY_NECKLACE = 2;
	/**
	 * 戒指
	 */
	public static final int ITEM_CATEGORY_RING = 3;
	/**
	 * 手镯
	 */
	public static final int ITEM_CATEGORY_BRACELET =4;	
	/**
	 * 胸甲
	 */
	public static final int ITEM_CATEGORY_ARMOR = 5;
	/**
	 * 裤子
	 */
	public static final int ITEM_CATEGORY_PANTS = 6;
	/**
	 * 鞋子
	 */
	public static final int ITEM_CATEGORY_SHOE = 7;
	/**
	 * 护腕
	 */
	public static final int ITEM_CATEGORY_BRACER = 8;
	/**
	 * 头盔
	 */
	public static final int ITEM_CATEGORY_HELMET = 9;
	/**
	 * 腰带
	 */
	public static final int ITEM_CATEGORY_BELT = 10;
	
	
	public static final int ITEM_CATEGORY_POWER_STONE_1 = 21;
	public static final int ITEM_CATEGORY_POWER_STONE_2 = 22;
	public static final int ITEM_CATEGORY_POWER_STONE_3 = 23;
	
	/**
	 * 可选择获取指定物品的道具
	 */
	public static final int ITEM_CATEGORY_CHOICE_ITEM = 56;
	
	/**
	 * 101	调用技能ID{参数1}
	 */
	public static final int ITEM_USE_EFFECT_BAT_SKILL=101;
	/**
	 * 102	添加金币数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_GOLD_ADD=102;
	/**
	 * 103	添加钻石数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_DIAMOND_ADD=103;
	/**
	 * 104	添加技能点数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_SKILL_POINT_ADD=104;
	/**
	 * 105	添加绑金金币数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_BINDED_GOLD_ADD=105;
	
	public static final int ITEM_USE_EFFECT_EXP=106;
	
	/**
	 * 107	添加绑钻钻石数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_BINDED_DIAMOND_ADD = 107;
	/**
	 * 107	添加成就点数量{参数1}
	 */
	public static final int ITEM_USE_EFFECT_ACHIVE_POINT_ADD = 108;
	/**
	 * 100	掉落道具{参数1}
	 */
	public static final int ITEM_USE_EFFECT_ITEM_DROP = 100;
	
	/**
	 * 109    声望
	 */
	public static final int ITEM_USE_EFFECT_PRESTIGE = 109;
	public static final int ITEM_GOLD = 300004003;
	
	/**110 经验加成丹 */
	public static final int ITEM_USE_EFFECT_110 =110;
	
	/**111 获得宠物 */
	public static final int ITEM_USE_EFFECT_111 =111;
	
	/**112 获得月卡 */
	public static final int ITEM_USE_EFFECT_112 =112;
	
	/**113 悔过药 */
	public static final int ITEM_USE_EFFECT_113 =113;
	
	/**114 增加VIP经验 */
	public static final int ITEM_USE_EFFECT_114 =114;
	
	/**115 使用时装 */
	public static final int ITEM_USE_EFFECT_115 =115;
	
	/**116 道具充值 */
	public static final int ITEM_USE_EFFECT_116 = 116;
	
	/**激活任务*/
	public static final int ITEM_USE_EFFECT_117 = 117;
	
	/**获得公会功勋*/
	public static final int ITEM_USE_EFFECT_118 = 118;
	
	/**获得宝石碎片*/
	public static final int ITEM_USE_EFFECT_119 = 119;
	
	/**获得灵魂值*/
	public static final int ITEM_USE_EFFECT_120 = 120;
	
	/**指定五行属性增加经验*/
	public static final int ITEM_USE_EFFECT_121 = 121;
	
	/**
	 * 翅膀星级消耗道具编号
	 */
	public static int ITEM_WING_STAR_UPGRADE_COST_ITEM_ID;

	/**
	 * 翅膀阶数消耗道具编号
	 */
	public static int ITEM_WING_STEP_UPGRADE_COST_ITEM_ID;
	
	
	/**
	 * 日志常量（资源获取 1=绑金 2=金币 3=绑钻 4=钻石 5=道具 6=技能点 7=公会荣誉 8=声望 9=成就点 10=宠物魔魂 11=经验 98=先绑金再金币 99=先绑钻再钻石）
	 */
	public static final int LOG_TYPE_BINDGOLD = 1;
	public static final int LOG_TYPE_GOLD = 2;
	public static final int LOG_TYPE_BINDDIMOND = 3;
	public static final int LOG_TYPE_DIMOND = 4;
	public static final int LOG_TYPE_ITEM = 5;
	public static final int LOG_TYPE_SKILLPOINT = 6;
	public static final int LOG_TYPE_GUILDPOINT = 7;
	public static final int LOG_TYPE_PRISTIGE = 8;
	public static final int LOG_TYPE_ACHIVEPOINT = 9;
	public static final int LOG_TYPE_PETSOUL = 10;
	public static final int LOG_TYPE_EXP = 11;
	public static final int LOG_TYPE_BINDGOLDTHENGOLD = 98;
	public static final int LOG_TYPE_BINDDIMONDTHENDIMOND = 99;

	
	/**
	 * 根据槽位获得名称
	 * @param slot
	 * @return
	 */
	public static String fecthNameBySlot(Integer slot){
		String str = GlobalCache.fetchLanguageMap("key260");
		if(slot.intValue() == ITEM_CATEGORY_WEAPON)
		{
			str = GlobalCache.fetchLanguageMap("key260");
		}
		else if(slot.intValue() == ITEM_CATEGORY_NECKLACE)
		{
			str = GlobalCache.fetchLanguageMap("key261");
		}
		else if(slot.intValue() == ITEM_CATEGORY_RING)
		{
			str = GlobalCache.fetchLanguageMap("key262");
		}
		else if(slot.intValue() == ITEM_CATEGORY_BRACELET)
		{
			str = GlobalCache.fetchLanguageMap("key263");
		}
		else if(slot.intValue() == ITEM_CATEGORY_ARMOR)
		{
			str = GlobalCache.fetchLanguageMap("key264");
		}
		else if(slot.intValue() == ITEM_CATEGORY_PANTS)
		{
			str = GlobalCache.fetchLanguageMap("key265");
		}
		else if(slot.intValue() == ITEM_CATEGORY_SHOE)
		{
			str = GlobalCache.fetchLanguageMap("key266");
		}
		else if(slot.intValue() == ITEM_CATEGORY_BRACER)
		{
			str = GlobalCache.fetchLanguageMap("key267");
		}
		else if(slot.intValue() == ITEM_CATEGORY_HELMET)
		{
			str = GlobalCache.fetchLanguageMap("key268");
		}
		else if(slot.intValue() == ITEM_CATEGORY_BELT)
		{
			str = GlobalCache.fetchLanguageMap("key269");
		}
		return str;
	}
}
