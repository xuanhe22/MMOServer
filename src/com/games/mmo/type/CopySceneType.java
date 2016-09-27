package com.games.mmo.type;

public class CopySceneType {
	/**
	 * 副本:dota配置Id
	 */
	public static final int COPYSCENE_DOTA = 20204001;
	
	/**
	 * 副本:经验副本配置Id
	 */
	public static final int COPYSCENE_EXP = 20205001;
	
	/**
	 * 副本:竞技场副本Id
	 */
	public static final int COPYSCENE_ARENA = 20200001;
	
	
	/**
	 * 1 730	运镖任务
	 */
	public static final int AWARD_RETRIEVE_ID1 = 730;
	
	/**
	 * 2	20200010	魔化危机
	 */
	public static final int AWARD_RETRIEVE_ID2 = 20200010;
	/**
	 * 3	20200009	PK之王
	 */
	public static final int AWARD_RETRIEVE_ID3 = 20200009;
	/**
	 * 4	20200012	血魔堡垒
	 */
	public static final int AWARD_RETRIEVE_ID4 = 20200012;
	/**
	 * 5	20200011	恶灵禁地(恶魔广场)
	 */
	public static final int AWARD_RETRIEVE_ID5 = 20200011;
	/**
	 * 6	20200008	守卫水晶
	 */
	public static final int AWARD_RETRIEVE_ID6 = 20200008;
	/**
	 * 7	20200013	经验副本
	 */
	public static final int AWARD_RETRIEVE_ID7 = 20200013;
	/**
	 * 8	20200002	名人挑战(名人挑战赛)
	 */
	public static final int AWARD_RETRIEVE_ID8 = 20200002;
	
	/**
	 * 9	日常任务
	 */
	public static final int AWARD_RETRIEVE_ID9 = 3;
	
	/**
	 * 20206004 PK之王
	 */
	public static final int COPY_SCENE_CONF_KINGOFPK = 20206004;

	/**
	 * 20206005 魔化危机 
	 */
	public static final int COPY_SCENE_CONF_MONSTERCRISIS = 20206005;
	
	/**
	 * 20206006 恶灵禁地
	 */
	public static final int COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE = 20206006;
	
	/**
	 * 20206007 血魔堡垒
	 */
	public static final int COPY_SCENE_CONF_BLOODMAGICFORTRESS = 20206007;

	
	/**
	 * 20206007 极限之塔
	 */
	public static final int COPY_SCENE_CONF_KILLINGTOWER_START = 20206012;
	
	public static final int COPY_SCENE_CONF_KILLINGTOWER_END = 20206031;
	
	/**
	 * 20206039 运镖任务
	 */
	public static final int COPY_SCENE_CONF_YUN_DART = 20206039;
	
	/** 20206040 公会竞标 */
	public static final int COPY_SCENE_CONF_GUILD_BID = 20206040;
	
	/** 20206041 自由之战 */
	public static final int COPY_SCENE_CONF_FREE_WAR = 20206041;
	
	/** 20206042 工会战 */
	public static final int COPY_SCENE_CONF_GUILD_WAR=20206042;
	/** 20206043 沙巴克*/
	public static final int COPY_SCENE_CONF_SIEGE_WAR=20206043;
	
	/** 20206048 定时boss*/
	public static final int COPY_SCENE_CONF_TIMER_BOSS = 20206048;
	
	/** 20206049  公会祭酒*/
	public static final int COPY_SCENE_CONF_GUILD_PRIEST = 20206049;
	
	/** 20206050  收集水晶*/
	public static final int COPY_SCENE_CONF_COLLECT_CRYSTAL =20206050; 
	

	
	public static int[] COPY_SCENE_CONF_ACTIVITY_MAP_ROOM = new int[]{COPY_SCENE_CONF_BLOODMAGICFORTRESS,COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE,COPY_SCENE_CONF_MONSTERCRISIS,COPY_SCENE_CONF_KINGOFPK,COPY_SCENE_CONF_KILLINGTOWER_START,COPY_SCENE_CONF_FREE_WAR};
	
	/** 公会boss副本 */
	public static  int[]  COPY_SCENE_CONF_GOUILD_BOSS = new int[]{20206032,20206033,20206034,20206035,20206036,20206037,20206038, COPY_SCENE_CONF_GUILD_PRIEST};
	
	
	/** 副本结束后增加副本次数 */
	public static int[] COPY_SCENE_CONF_ACTIVITY_LATER = new int[]{COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE, COPY_SCENE_CONF_BLOODMAGICFORTRESS}; 
	
	/** 所有动态副本*/
	public static int[] COPY_SCENE_CONF_ALL_DYNAMIC=new int[]{20100002,20100008,20100026,20100003,20101099,20100098,20103002,20100009,20100010,20100011,20100012,20100013,20100014,20100015,20100016,20100017,20100019,20100020,20100021,20100022,20100023,20100024,20100025};
	
	/**
	 * 0 其它
	 */
	public static final int COPY_SCENE_TYPE_OTHER = 0;
	/**
	 * 1 经验本
	 */
	public static final int COPY_SCENE_TYPE_SINGLE_EXP = 1;
	/**
	 * 2  金币本
	 */
	public static final int COPY_SCENE_TYPE_SINGLE_GOLD = 2;
	/**
	 * 3 材料本
	 */
	public static final int COPY_SCENE_TYPE_SINGLE_MATERIAL = 3;
	/**
	 * 4 竞技场结算
	 */
	public static final int COPY_SCENE_TYPE_ARENA = 4;
	/**
	 * 5 活动-恶灵禁地
	 */
	public static final int COPY_SCENE_TYPE_ZaphieHaramRoom= 5;
	
	/**
	 * 6 活动-血魔危机
	 */
	public static final int COPY_SCENE_TYPE_BloodSeekerBastionRoom= 6;
	
	
	/**
	 * 7 活动-魔化危机
	 */
	public static final int COPY_SCENE_TYPE_DemonizationCrisisRoom= 7;
	
	/**
	 * 8 活动-极限乱斗
	 */
	public static final int COPY_SCENE_TYPE_KILLING_TOWER= 8;
	
	/**
	 * 9 活动-pk之王
	 */
	public static final int COPY_SCENE_TYPE_KING_OF_PK= 9;
	
	
	/**
	 * 10 活动-公会副本
	 */
	public static final int COPY_SCENE_TYPE_GUILD_BOSS= 10;
	
	/**
	 * 11-组队本-守护
	 */
	public static final int COPY_SCENE_TYPE_TEAM_DEFEND= 11;
	
	/**
	 * 13 全民
	 */
	public static final int COPY_SCENE_TYPE_TEAM_DUNGEON_ALL_PEOPLE_CHALLENGE = 13;
	
	/**
	 * 角色等级副本
	 */
	public static final int COPY_SCENE_TYPE_ROLE_LEVEL=15;
	
	/**
	 * 16 假死
	 */
	public static final int COPY_SCENE_TYPE_FEIGN_DEATH=16;
	
	/**
	 * 副本鼓舞BUFFID
	 */
	public static final int COPY_INSPIRE_BUFFID = 41;
	
	
	/**
	 * 副本鼓舞所需钻石
	 */
	public static final int COPY_INSPIRE_DIAMOND = 10;
	
	/** BUFF永久有效时间 */
	public static final int EIKY_TIME = -1;
	

	//个人副本
	public static int[] SINGLE_ROOMS_TYPE=new int[]{COPY_SCENE_TYPE_SINGLE_EXP,COPY_SCENE_TYPE_SINGLE_GOLD,COPY_SCENE_TYPE_SINGLE_MATERIAL,COPY_SCENE_TYPE_ARENA,COPY_SCENE_TYPE_ROLE_LEVEL};
	//组队副本组
	public static int[] TEAM_ROOMS_TYPE=new int[]{COPY_SCENE_TYPE_TEAM_DEFEND, COPY_SCENE_TYPE_TEAM_DUNGEON_ALL_PEOPLE_CHALLENGE};
	//活动类
	public static int[] ACTIVITY_TYPE=new int[]{COPY_SCENE_TYPE_ZaphieHaramRoom,COPY_SCENE_TYPE_BloodSeekerBastionRoom,COPY_SCENE_TYPE_DemonizationCrisisRoom,COPY_SCENE_TYPE_KILLING_TOWER,COPY_SCENE_TYPE_KING_OF_PK,COPY_SCENE_TYPE_GUILD_BOSS};
	
	
	/** 阵营0(创建用户默认值) */
	public static final int MILITARY_FORCES_0 = 0;
	/** 阵营1 */
	public static final int MILITARY_FORCES_1 = 1;
	/** 阵营2 */
	public static final int MILITARY_FORCES_2 = 2;
	
}
