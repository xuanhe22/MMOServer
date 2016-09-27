package com.games.mmo.type;

/**
 * 运营活动
 * @author Administrator
 *
 */
public class LiveActivityType {
	public static final int ACTIVITY_STATUS_OPEN = 1;
	public static final int ACTIVITY_STATUS_FINISHED = 2;
	
	/**1. 每日充值*/
	public static int LiveActivityDAILY_CHARGE = 1;  //每日充值  1
	/**2. 累计充值*/
	public static int LiveActivityTOTAL_CHARGE = 2;  //累计充值  2
	/**3. 累计消费 */
	public static int LiveActivityTOTAL_COST = 3;  //累计消费  3
	/**4. 开服有礼 */
	public static int LiveActivity_EXCHANGE = 4;  //兑换  4
	/**5. 充值排行 */
	public static int LiveActivity_CHARGE_RANK = 5;  //充值排行  5
	/**6. 消费排行 */
	public static int LiveActivity_COST_RANK = 6;  //消费排行  6
	/**7. 经验翻倍奖励 */
	public static int LiveActivityDESC = 7;  //双倍经验  7
	/**8. 战力排行 */
	public static int LiveActivity_PROWER = 8;  //战力排行 8
	/**9. 等级排行 */
	public static int LiveActivity_ROLE_LV = 9; //等级排行9
	/**10. 累计登陆 */
	public static int LiveActivity_LOGIN = 10;  //登录奖励  10
	
	/**
	 * 竞技场排行
	 */
	public static int LiveActivity_ARENA_RANK = 11;  
	
	/**
	 * 成就排行
	 */
	public static int LiveActivity_ACHIEVE = 12;  
	
	/**
	 * 声望排行
	 */
	public static int LiveActivity_PRESTIGE = 13; 
	/** 伙伴排行*/
	public static int LiveActivity_PET_RANK=14;
	
	public static int LiveActivity_LV_REACH=15;
	/** 经验双倍*/
	public static int RATE_EXP=2;
	/** 金币双倍*/
	public static int RATE_GOLD=3;
	/** 掉落双倍*/
	public static int RATE_DROP=4;
	/** boss刷新*/
	public static int RATE_BOSS_REFRESH=5;
	
	public static int[] LiveActivity_TYPE=new int[]{LiveActivity_CHARGE_RANK,
																				   LiveActivity_COST_RANK,
																				   LiveActivity_PROWER,
																				   LiveActivity_ROLE_LV,
																				   LiveActivity_ARENA_RANK,
																				   LiveActivity_ACHIEVE,
																				   LiveActivity_PRESTIGE,
																				   LiveActivity_PET_RANK};
}
