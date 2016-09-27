package com.games.mmo.type;

/**
 * 
 * 类功能:任务类型
 * @author johnny
 * @version 2014-6-27
 */
public class TaskType {
	
	/**
	 * 任务状态-已激活
	 */
	public static final int TASK_STATUS_ACTIVED = -1;
	/**
	 * 任务状态-已接取
	 */
	public static final int TASK_STATUS_ACCEPTED = 0;
	
	/**
	 * 成就完成
	 */
	public static final int TASK_STATUS_ACHIEVED = -2;
	/**
	 * 主线 
	 */
	public static final int TASK_TYPE_MAIN = 1;
	/**
	 * 支线
	 */
	public static final int TASK_TYPE_SUB = 2;
	
	/**
	 * 环任务
	 */
	public static final int TASK_TYPE_RING = 3;
	/**
	 * 活跃度任务
	 */
	public static final int TASK_TYPE_LIVELY =4;

	/**
	 * 成就任务
	 */
	public static final int TASK_TYPE_ACHIEVE = 5;
	
	/**
	 * 运镖任务
	 */
	public static final int TASK_TYPE_YUN_DART = 6;
	
	/**
	 * 悬赏任务
	 */
	public static final int TASK_TYPE_OFFER_REWARD = 7;
	
	/**
	 * 和指定NPC对话
	 */
	public static final int TASK_TYPE_CONDITION_701=701;
	/**
	 * 杀指定怪
	 */
	public static final int TASK_TYPE_CONDITION_702 = 702;

	/**
	 * 拥有数量
	 */
	public static final int TASK_TYPE_CONDITION_703 = 703;
	
	/**
	 * 强化n次装备 704|次数
	 */
	public static final int TASK_TYPE_CONDITION_704 = 704;
	/**
	 * 强化n次技能   705|次数
	 */
	public static final int TASK_TYPE_CONDITION_705 = 705;
	/**
	 * 进入n次天梯赛 706|次数
	 */
	public static final int TASK_TYPE_CONDITION_706 = 706;
	/**
	 * 进入n次通天塔   707|次数
	 */
	public static final int TASK_TYPE_CONDITION_707 = 707;
	/**
	 *  708| 次数 | 副本Id(0：任意) | 难度(0:任意)
	 */
	public static final int TASK_TYPE_CONDITION_708 = 708;
	
	/**
	 * 客户端用的对话任务
	 */
	public static final int TASK_TYPE_CONDITION_CLICK = 709;
	
	/**
	 * 装备上槽位  710|1  类型：710  槽位id:1 
	 */
	public static final int TASK_TYPE_CONDITION_710= 710;

	/**
	 * 人物达到多少级  711|1|50 类型 711  需要达到等级 50
	 */
	public static final int TASK_TYPE_CONDITION_711 = 711;
	
	/**
	 * 712 升星|件数|达到X星（0位任意）
	 */
	public static final int TASK_TYPE_CONDITION_712 = 712;
	
	/**
	 * 713 洗练|件数|达到X星（0位任意）
	 */
	public static final int TASK_TYPE_CONDITION_713= 713;
	/**
	 * 714 镶嵌宝石|数量|宝石的等级(0:表示任意等级宝石)
	 */
	public static final int TASK_TYPE_CONDITION_714 = 714;
	/**
	 * 715 翅膀|阶数
	 */
	public static final int TASK_TYPE_CONDITION_715= 715;
	/**
	 * 716 获得伙伴|资质|数量
	 */
	public static final int TASK_TYPE_CONDITION_716 = 716;
	/**
	 * 717 伙伴等级|级数
	 */
	public static final int TASK_TYPE_CONDITION_717 = 717;
	/**
	 * 718 杀红名玩家|数量
	 */
	public static final int TASK_TYPE_CONDITION_718 =718;
	/**
	 * 719 观察读条|次数|场景|坐标x|坐标y|坐标z|时间
	 */
	public static final int TASK_TYPE_CONDITION_719= 719;
	/**
	 * 720 装备升星|次数
	 */
	public static final int TASK_TYPE_CONDITION_720 = 720;
	/**
	 * 721 洗练|次数
	 */
	public static final int TASK_TYPE_CONDITION_721 = 721;
	/**
	 * 722 抽取伙伴|次数
	 */
	public static final int TASK_TYPE_CONDITION_722 = 722;
	/**
	 * 723|数量  活跃度任务完成
	 */
	public static final int TASK_TYPE_CONDITION_723 = 723;
	
	/**
	 * 724 强化装备|数量|等级
	 */
	public static final int TASK_TYPE_CONDITION_724 = 724;
	/**
	 * 725 添加好友|次数
	 */
	public static final int TASK_TYPE_CONDITION_725 = 725;
	/**
	 * 726 组队|次数
	 */
	public static final int TASK_TYPE_CONDITION_726 = 726;
	/**
	 * 727 合成|次数|等级
	 */
	public static final int TASK_TYPE_CONDITION_727 = 727;
	/**
	 * 728 翅膀升阶|次数
	 */
	public static final int TASK_TYPE_CONDITION_728 = 728;
	
	/**
	 * 729|次数|完成任务Id
	 */
	public static final int TASK_TYPE_CONDITION_729 = 729;
	/**
	 * 730|1|420106036 运镖任务 id|次数|任务npcId
	 */
	public static final int TASK_TYPE_CONDITION_730 = 730;
	
	/** 731累计登录*/
	public static final int TASK_TYPE_CONDITION_731 = 731;
	
	/**732 膜拜 */
	public static final int TASK_TYPE_CONDITION_732 = 732;
	
	/** 733翅膀升星*/
	public static final int TASK_TYPE_CONDITION_733 = 733;
	
	/** 734危机边缘*/
	public static final int TASK_TYPE_CONDITION_734 = 734;
	
	/** 735回收装备*/
	public static final int TASK_TYPE_CONDITION_735 = 735;
	
	/** 736绑定Facebook_token*/
	public static final int TASK_TYPE_CONDITION_736 = 736;
	
	/** 737粉丝团*/
	public static final int TASK_TYPE_CONDITION_737 = 737;
	
	/**738 杀怪|个数|怪物id （和702一模一样）*/
	public static final int TASK_TYPE_CONDITION_738 = 738;
	
	/** 参加工会祭祀 739|次数 */
	public static final int TASK_TYPE_CONDITION_739 = 739;
	
	/** 采集水晶 740|次数 */
	public static final int TASK_TYPE_CONDITION_740 = 740;
	
	/** 击杀XX个YY等级以上怪 **/
	public static final int TASK_TYPE_CONDITION_741 = 741;
	
	/**商城购买X个Y道具*/
	public static final int TASK_TYPE_CONDITION_742 = 742;
	
	/**
	 * 1.翅膀
	 */
	public static final int OPEN_SYSTEM_WING = 1;
	/**
	 * 2.寄售
	 */
	public static final int OPEN_SYSTEM_SELL = 2;
	/**
	 * 3.升星
	 */
	public static final int OPEN_SYSTEM_UPSTAR = 3;
	/**
	 * 	4.洗练
	 */
	public static final int OPEN_SYSTEM_EXTRACT = 4;
	/**
	 * 	5.宝石
	 */
	public static final int OPEN_SYSTEM_GEM = 5;
	/**
	 * 6.伙伴
	 */
	public static final int OPEN_SYSTEM_PET = 6;
	/**
	 * 7.伙伴获得
	 */
	public static final int OPEN_SYSTEM_PET_GAIN =7;
	/**
	 * 8.伙伴升级
	 */
	public static final int OPEN_SYSTEM_PET_UP = 8;
	/**
	 * 9.伙伴技能
	 */
	public static final int OPEN_SYSTEM_PET_SKILL = 9;
	/**
	 * 10.伙伴培养
	 */
	public static final int OPEN_SYSTEM_PET_TRAINING =10;
	/**
	 * 11.伙伴装备
	 */
	public static final int OPEN_SYSTEM_PET_EQUIP = 11;
	/**
	 * 12.伙伴天赋
	 */
	public static final int OPEN_SYSTEM_PET_TALENT = 12;
	/**
	 * 13.公会
	 */
	public static final int OPEN_SYSTEM_GUILD = 13;
	/**
	 * 14.装备强化
	 */
	public static final int OPEN_SYSTEM_EQUIP_POWER = 14;
	/**
	 * 15.名人挑战赛
	 */
	public static final int OPEN_SYSTEM_ARENA = 15;
	/**
	 * 16.血色城堡
	 */
	public static final int OPEN_SYSTEM_BLOODCASTLE = 16;
	/**
	 * 17.通天塔
	 */
	public static final int OPEN_SYSTEM_DOTA = 17;
	/**
	 * 18.幽冥之境
	 */
	public static final int OPEN_SYSTEM_EXP_SCENE = 18;	
	/**
	 * 19.遗失圣境
	 */
	public static final int OPEN_SYSTEM_LOSE_SCENE = 19;
	/**
	 * 20.合成
	 */
	public static final int OPEN_SYSTEM_COMPOUND = 20;
	
	/**
	 * 51.天降宝物
	 */
	public static final int OPEN_SYSTEM_CIMELIAS = 51;
	
	/**
	 * 52.灵魂
	 */
	public static final int OPEN_SYSTEM_SOUL = 52;
	
	/**
	 * 101.解锁技能1
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL1 = 101;
	/**
	 * 102.解锁技能2
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL2 = 102;
	/**
	 * 103.解锁技能3
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL3 = 103;
	/**
	 * 104.解锁技能4
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL4 = 104;
	/**
	 * 105.解锁技能5
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL5 = 105;
	/**
	 * 106.解锁技能6
	 */
	public static final int OPEN_SYSTEM_UNLOCK_SKILL6 = 106;

	/**
	 * 107 FaceBook 邀请好友x个好友
	 */
	public static final int FACEBOOK_INVITE_FRIENDS = 107;	
	
	/**
	 * 每日任务
	 */
	public static final int EVERY_DAY_TASK= 1;
	
	

	public static final int TASK_NEW_TASK_NEW = 1;
	public static final int TASK_NEW_TASK_NONE = 0;
	public static final int TREASURE_STATUS_NOT_FINISH = 0;
	public static final int TREASURE_STATUS_NOT_AWARD = 1;
	

}
