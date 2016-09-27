package com.games.mmo.po;

import io.netty.channel.ChannelHandlerContext;

import java.awt.Point;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.Treasure;
import com.games.mmo.mapserver.bean.map.activity.BloodSeekerBastionRoom;
import com.games.mmo.mapserver.bean.map.activity.DemonizationCrisisRoom;
import com.games.mmo.mapserver.bean.map.activity.PKGreatRoom;
import com.games.mmo.mapserver.bean.map.activity.ZaphieHaramRoom;
import com.games.mmo.mapserver.bean.map.single.SingleExpRoom;
import com.games.mmo.mapserver.bean.map.team.TeamMonstertInvadeRoom;
import com.games.mmo.mapserver.bean.map.team.TeamTowerRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.mapserver.vo.BufferEffetVo;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.ConsumPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.po.game.FashionPo;
import com.games.mmo.po.game.InvitationPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.LvConfigPo;
import com.games.mmo.po.game.MergePo;
import com.games.mmo.po.game.MilitaryRankPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.po.game.PetTalentPo;
import com.games.mmo.po.game.PetUpgradePo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.po.game.TitlePo;
import com.games.mmo.po.game.UpgradeSkillPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.ItemService;
import com.games.mmo.service.MailService;
import com.games.mmo.service.PetService;
import com.games.mmo.service.RoleService;
import com.games.mmo.service.SoulService;
import com.games.mmo.type.BuffModelType;
import com.games.mmo.type.BuffType;
import com.games.mmo.type.ChatType;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.InvitationType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.MailType;
import com.games.mmo.type.PetType;
import com.games.mmo.type.PlayTimesType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SceneType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.type.SoulType;
import com.games.mmo.type.SoulType.Type;
import com.games.mmo.type.TaskType;
import com.games.mmo.type.UsuallyType;
import com.games.mmo.type.VipType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.ActivityInfoVo;
import com.games.mmo.vo.AdvanceSuitPlusVo;
import com.games.mmo.vo.AwardRetrieveVo;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.ConstellAttachVo;
import com.games.mmo.vo.GrowFundVo;
import com.games.mmo.vo.GuildInvitionVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.InvitationRoleVo;
import com.games.mmo.vo.LogVo;
import com.games.mmo.vo.PVPPVEActivityStatusVo;
import com.games.mmo.vo.PetConstellVo;
import com.games.mmo.vo.RankVo;
import com.games.mmo.vo.RechargeInfoVo;
import com.games.mmo.vo.RetrieveSystemVo;
import com.games.mmo.vo.SimpleMailHeadVo;
import com.games.mmo.vo.SlotSoulVo;
import com.games.mmo.vo.SpecialTitleVo;
import com.games.mmo.vo.SustainKillVo;
import com.games.mmo.vo.YunDartTaskInfoVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.role.RoleFashionVo;
import com.games.mmo.vo.role.RoleLiveActivityVo;
import com.games.mmo.vo.role.RolePackItemVo;
import com.games.mmo.vo.role.RolePackItemVoCompartar;
import com.games.mmo.vo.role.RoleViewInforVo;
import com.games.mmo.vo.team.TeamAbroadRoomInforVo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.games.mmo.vo.xml.ConstantFile.EquipPower;
import com.games.mmo.vo.xml.ConstantFile.AccumulativeTime.OnlineTime;
import com.games.mmo.vo.xml.ConstantFile.AwardRetrieves.AwardRetrieve;
import com.games.mmo.vo.xml.ConstantFile.DiamondBasins.DiamondBasin;
import com.games.mmo.vo.xml.ConstantFile.Dota.Layer;
import com.games.mmo.vo.xml.ConstantFile.EquipPower.Power;
import com.games.mmo.vo.xml.ConstantFile.GrowFunds.GrowFund;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildwar.Territory;
import com.games.mmo.vo.xml.ConstantFile.LivelyAward.LivelyItem;
import com.games.mmo.vo.xml.ConstantFile.LuckyWheel.Item;
import com.games.mmo.vo.xml.ConstantFile.MonthAward.MonthItem;
import com.games.mmo.vo.xml.ConstantFile.OffLineRewards.OffLineReward;
import com.games.mmo.vo.xml.ConstantFile.OnlineTimeAwrod.OnlineTimes;
import com.games.mmo.vo.xml.ConstantFile.OpenPackage.Consum;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetAttachs.PetAttach;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetConstells.PetConstell;
import com.games.mmo.vo.xml.ConstantFile.PlayTimes.PlayItem;
import com.games.mmo.vo.xml.ConstantFile.PowerSuitPlus.PowerItem;
import com.games.mmo.vo.xml.ConstantFile.ReviveCosts.ReviveCost;
import com.games.mmo.vo.xml.ConstantFile.SoulSlot.Extract.ExtractUpgrade;
import com.games.mmo.vo.xml.ConstantFile.SoulSlot.SoulPower.StarUpgrade;
import com.games.mmo.vo.xml.ConstantFile.StarSuitPlus.StarItem;
import com.games.mmo.vo.xml.ConstantFile.TotalLogin.Day;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.games.mmo.vo.xml.ConstantFile.WashSuitPlus.WashItem;
import com.games.mmo.vo.xml.ConstantFile.Worship.WorshipItem;
import com.storm.lib.bean.CheckcCircleBean;
import com.storm.lib.bean.Null;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.CheckUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.FloatUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdLongVo2;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 
 * 类功能: 角色
 * 
 * @author Johnny
 * @version
 */
@Entity
@Table(name = "u_po_role")
public class RolePo extends BaseUserDBPo {
	
	/** 经验加成*/
	@JSONField(serialize = false)
	public Integer expPercent = 0;
	/**
	 * 运营登录活动状态
	 */
	private Integer activityLoginState = 0;
	
	/**
	 * 是否领取facebook绑定奖励
	 */
	private Integer wasFacebookBind = 0;
	
	/**
	 * 钻石摇一摇有效时间
	 */
	public String diamondBasinsValidTime="0";
	
	/**
	 * 进入副本状态
	 */
	@JSONField(serialize = false)
	public boolean copySceneStartState=true;
	
	/**
	 *  爬塔状态
	 *  0：默认(爬塔结束); 1：爬塔开始；2:爬塔层数增加
	 * */
	@JSONField(serialize = false)
	public Integer towerState=0;
	
	private Integer id;

	private Integer userId;

	@JSONField(serialize = false)
	private Integer roleInforId;

	private Long createTime;
	
	private Long lastLoginTime;

	private Long lastLogoffTime;

	private String name;

	private Integer lv;

	private Integer exp;

	private String iuid;

	private String pssd;

	private Integer bindGold = 0;

	private Integer gold = 0;
	
	private Integer warehouseBindGold=0;
	
	private Integer warehouseGold=0;

	private Integer diamond = 0;

	private Integer bindDiamond = 0;

	private String packs;

	private Integer abandomState;

	private String token;

	private Integer roomId;

	private Integer x = 0;

	private Integer y = 0;

	private Integer z = 0;

	private Integer career;

	private Integer skillPoint = 1000;

	/**
	 * 攻击模式 0=和平模式 1=攻击模式 2=帮派攻击模式
	 */
	private Integer attackMode = 0;

	/**
	 * 攻击模式恢复时间
	 */
	private Long attackRecoverTime = System.currentTimeMillis() - 1000 * 60 * 60;
	/**
	 * pk值
	 */
	private Integer pkValue = 0;

	/**
	 * pk状态 0=白名 1=灰名 2=红名
	 */
	private Integer pkStatus = 0;

	/**
	 * 红名开始时间
	 */
	private Long pkRedBeginTime = 0l;

	/**
	 * 红名恢复时间
	 */
	private Long pkLastRecoverTime = 0l;
	/**
	 * 灰名恢复时间
	 */
	private Long pkGrewRecoverTime = 0l;

	/**
	 * 爬塔当前层
	 */
	private Integer towerCurrentLv = 0;

	/**
	 * 爬塔今日已挑战次数
	 */
	private Integer towerTodayChallengeTimes = 0;

	/**
	 * 爬塔今日扫荡次数
	 */
	private Integer towerTodayWipeOutTimes = 0;
	

	public List<Integer> skills = new ArrayList<Integer>();

	/**
	 * 物攻：影响物理攻击时输出
	 */
	private Integer batMeleeAttackMin = 0;
	/**
	 * 法攻：影响法术攻击时输出
	 */
	private Integer batMagicAttackMin = 0;
	/**
	 * 物防：影响物理攻击对自身的伤害
	 */
	private Integer batMeleeDefenceMin = 0;
	/**
	 * 法防：影响法术攻击队自身的伤害
	 */
	private Integer batMagicDefenceMin = 0;

	/**
	 * 物攻：影响物理攻击时输出
	 */
	private Integer batMeleeAttackMax = 0;
	/**
	 * 法攻：影响法术攻击时输出
	 */
	private Integer batMagicAttackMax = 0;
	/**
	 * 物防：影响物理攻击对自身的伤害
	 */
	private Integer batMeleeDefenceMax = 0;
	/**
	 * 法防：影响法术攻击队自身的伤害
	 */
	private Integer batMagicDefenceMax = 0;
	/**
	 * 生命：生命值为0时角色死亡
	 */
	private Integer batHp = 0;
	/**
	 * 法力：法力值影响技能释放
	 */
	private Integer batMp = 0;
	/**
	 * 暴击：暴击时伤害翻倍，取值范围[0~1]
	 */
	private Integer batCritical = 0;
	/**
	 * 生命恢复：每5秒生命恢复的值
	 */
	private Integer batHpReg = 0;
	/**
	 * 法力恢复：每5秒法力恢复的值
	 */
	private Integer batMpReg = 0;
	/**
	 * 移动速度：每秒钟移动的像素数
	 */
	private Integer batMovement = 0;

	/**
	 * 闪避
	 */
	private Integer batDodge = 0;

	/**
	 * 命中率(比率非数值,说明一下以前的命中属性所用的字段名用成了batHitRate了，所以这里命中率字段进行再加个Rate处理)
	 */
	private Integer batHitRateRate = 0;

	/**
	 * 闪避率(比率非数值)
	 */
	private Integer batDodgeRate = 0;

	/**
	 * 暴击增加率(比率非数值)
	 */
	private Integer batAddCriticalRate = 0;

	/**
	 * 暴击减免率(比率非数值)
	 */
	private Integer batDerateCriticalRate = 0;

	/**
	 * 反弹伤害百分比
	 */
	private Integer batReboundDamage = 0;

	/**
	 * 武器
	 */
	public EqpPo equipWeapon;
	/**
	 * 右戒指
	 */
	public EqpPo equipRing;
	/**
	 * 胸甲
	 */
	public EqpPo equipArmor;
	/**
	 * 项链
	 */
	public EqpPo equipNecklace;
	/**
	 * 护腕
	 */
	public EqpPo equipBracer;

	/**
	 * 头盔
	 */
	public EqpPo equipHelmet;

	/**
	 * 鞋子
	 */
	public EqpPo equipShoe;

	/**
	 * 腰带
	 */
	public EqpPo equipBelt;

	/**
	 * 手镯
	 */
	public EqpPo equipBracelet;

	/**
	 * 裤子
	 */
	public EqpPo equipPants;

	/**
	 * 武器
	 */
	private Integer equipWeaponId;
	/**
	 * 戒指
	 */
	private Integer equipRingId;
	/**
	 * 胸甲
	 */
	private Integer equipArmorId;
	/**
	 * 项链
	 */
	private Integer equipNecklaceId;
	/**
	 * 护腕
	 */
	private Integer equipBracerId;

	/**
	 * 头盔
	 */
	private Integer equipHelmetId;

	/**
	 * 鞋子
	 */
	private Integer equipShoeId;

	/**
	 * 腰带
	 */
	private Integer equipBeltId;

	/**
	 * 手镯
	 */
	private Integer equipBraceletId;

	/**
	 * 裤子
	 */
	private Integer equipPantsId;

	/**
	 * buffer状态
	 */
	private String bufferStatus;

	/**
	 * 翅膀装备状态 0:未装备，1：已装备
	 */
	private Integer wingEquipStatus = 0;

	/**
	 * 翅膀星数
	 */
	private Integer wingStar = 0;

	/**
	 * 翅膀星级经验
	 */
	private Integer wingStarExp = 0;

	/**
	 * 翅膀阶数概率
	 */
	private Integer wingStepPoss = 0;

	/**
	 * 翅膀是否隐藏
	 */
	private Integer wingWasHidden = 0;

	/**
	 * 总战力
	 */
	private Integer battlePower = 0;

	/**
	 * 强化套装加成到达最大等级
	 */
	public Integer powerSuitPlusArriveMaxLevel = 0;

	/**
	 * 当前强化套装加成等级
	 */
	public Integer currentPowerSuitPlusLevel = 0;

	/**
	 * 升星套装加成到达最大等级
	 */
	public Integer starSuitPlusArriveMaxLevel = 0;

	/**
	 * 当前升星套装加成等级
	 */
	public Integer currentStarSuitPlusArriveLevle = 0;
	
	/**
	 * 洗练套装加成达到最大等级
	 */
	public Integer washSuitPlusMaxLevel = 0;
	
	/**
	 * 当前洗练套装加成等级
	 */
	public Integer washSuitPlusCurrentLevel = 0;

	/**
	 * 新手组
	 */
	public Integer newbieStepGroup = 0;

	/**
	 * 是否是月卡
	 */
	public Integer wasMonthCard = 0;

	/**
	 * 剩余月卡天数
	 */
	public Integer remainMonthCardDay = 0;

	/**
	 * 今天是否领取了月卡
	 */
	public Integer todayWasTakeMonthCard = 0;

	/**
	 * 累计充值数量
	 */
	public Integer cumulativeRechargeNum = 0;

	/**
	 * 是否首次充值
	 */
	public Integer wasFirstRecharge = 0;

	/**
	 * 是否领取首次充值奖励
	 */
	public Integer wasTakeFirstRechargeAwards = 0;
	/**
	 * 是否领取每日vip奖励
	 */
	public Integer wasTakeDailyVipAward = 0;

	/**
	 * 每天当前完成次数
	 */
	public int dailyCurrentFinishYunDartCount = 0;
	/**
	 * 每天当前免费刷新了的镖车次数
	 */
	public int dailyCurrentFreeFlushYunDartCarCount = 0;
	/**
	 * 当前运镖车的品质 -1：没有运镖车 0：破损的物资车 1：白色物资车 2：绿色物资车 3：蓝色物资车 4：紫色物资车 5：橙色物资车
	 */
	public int currentYunDartCarQuality = -1;

	/**
	 * 当前运镖车Id
	 */
	public int currentYunDartCarId = -1;

	/**
	 * done 用户iuid
	 */
	private String userIuid;
	
	/**
	 * done 竞技场排行
	 */
	private Integer arenaRank=0;
	
	/**
	 * done 每日活跃任务完成积分
	 */
	private Integer dailyLivelyTaskFinishScore=0;
	/**
	 * done 帮派名字
	 */
	private String guildName;
	/**
	 * done 主线任务ID
	 */
	private Integer mainTaskId=0;
	/**
	 * done 主线任务名称
	 */
	private String mainTaskName;
	/**
	 * done 首充钻石
	 */
	private Integer firstChargeDiamond=0;
	
	/**
	 * done 首充角色等级
	 */
	private Integer firstChargeRoleLv=0;
	
	/**
	 * done 首次消费描述
	 */
	private String firstConsumeDesc;
	
	/**
	 * done 首次消费角色等级
	 */
	private Integer firstConsumeRoleLv=0;
	
	/**
	 * 有未读邮件
	 */
	private Integer mailUnread = 0;
	
	/** 当前军衔Id */
	private Integer currentMilitaryRankId = 0;
	
	/** 军衔记录*/
	private String militaryRankRecord;
	
	/**摇一摇时间**/
	private Integer diamondBasinsTime = 0;
	
	/**第一次膜拜*/
	private Integer worshipDiamondFirst = 0;
	
	/**宝石碎片*/
    private Integer gamstoneFragment = 0;
    
    /**天降宝物步数**/
    private Integer cimeliasId = 0;
	
	/**
	 * 公会祭司状态 0:没有进去过； 1：今天进入过； 2：今天不能进入
	 */
	@JSONField(serialize = false)
	private Integer guildPriestState = 0;
	
	/** 军衔记录*/
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<IdNumberVo> listMilitaryRankRecord = new CopyOnWriteArrayList<IdNumberVo>();
	/** 购买次数*/
	private String buyPlayTimes;	
	/** 购买次数*/
	public CopyOnWriteArrayList<IdNumberVo2> listBuyPlayTimes = new CopyOnWriteArrayList<IdNumberVo2>();
	
	/** 离线掉落列表 */
	public CopyOnWriteArrayList<IdNumberVo> listOffLineReward = new CopyOnWriteArrayList<IdNumberVo>(); 
	
	/** 离线奖励时间(分钟) */
	@JSONField(serialize = false)
	private Integer offLineRewardMinutes=0;
	
	/** 刀塔首次奖励记录*/
	@JSONField(serialize = false)
	private String dotaFirstAward;
	
	/** 刀塔首次奖励记录 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<IdNumberVo> listDotaFirstAward = new CopyOnWriteArrayList<IdNumberVo>();
	
	/**
	 * 技能
	 */
	@JSONField(serialize = false)
	private String skillVos;

	@JSONField(serialize = false)
	private String skillFormation;

	public List<IdNumberVo> listSkillVos = new ArrayList<IdNumberVo>();

	@JSONField(serialize = false)
	private String mainPackItemVos;
	
	@JSONField(serialize = false)
	private String warehousePackItemVos;

	@JSONField(serialize = false)
	public Integer arenaTargetRoleId = 0;

	/**
	 * 竞技场今天已参加次数
	 */
	private Integer arenaTodayPlayedTimes = 0;

	@JSONField(serialize = false)
	public Fighter fighter;

	public TeamMemberVo teamMemberVo;

	/**
	 * 社交队伍Id
	 */
	private Integer socialTeamId = 0;

	/**
	 * 角色任务列表
	 */
	private String roleTasks;

	/**
	 * 角色成就列表
	 */
	private String roleAchieves;

	/**
	 * 
	 */
	private Integer roomLoading=0;
	/**
	 * 静态房间
	 */
	private Integer staticRoomId;

	/**
	 * 副本今天访问次数
	 */
	private String copySceneTodayVisitTimes;
	/**
	 * 角色任务列表
	 */

	private Integer batMaxHp = 0;

	private Integer batMaxMp = 0;

	private Integer resurnowTodayTimes = 0;

	private Integer resurnowContinueTimes = 0;

	private String friends;

	private String blocks;

	private String enemys;

	public PVPPVEActivityStatusVo pVPPVEActivityStatusVo = new PVPPVEActivityStatusVo();
	/**
	 * 角色任务 类型:1,2,3,4
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listRoleTasks = new CopyOnWriteArrayList<IdNumberVo2>();

	/**
	 * 成就任务 类型:5
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listRoleAchievesTasks = new CopyOnWriteArrayList<IdNumberVo2>();

	private Integer batLuckyAttack;
	private Integer batHitRate;
	private Integer batMeleeDefence;
	private Integer batMagicDefence;
	private Integer batLuckyDefence;
	private Integer batTough;

	/**
	 * 伤害增加率
	 */
	public Integer batDamageRate = 0;

	/**
	 * 伤害减免率
	 */
	public Integer batDamageResistRate = 0;

	/**
	 * 暴击伤害增加率
	 */
	public Integer batCriticalDamageRate = 0;

	/**
	 * 攻击力增加率
	 */
	public Integer batAttackRate = 0;

	/**
	 * 攻击力增加率
	 */
	public Integer batDefenceRate = 0;

	/**
	 * 物攻
	 */
	public Integer batMeleeAttack = 0;

	/**
	 * 法功
	 */
	public Integer batMagicAttack = 0;

	/**
	 * 自动接收
	 */
	public Integer guildAutoAccept = 0;
	/**
	 * 战功
	 */
	public Integer guildHonor = 0;

	/**
	 * 今日累计捐献战功
	 */
	public Integer guildTodayContributed = 0;

	/**
	 * 今日已兑换道具次数列表
	 */
	public String guildTodayExchangeItems;

	/**
	 * 所在帮派
	 */
	private Integer guildId = 0;

	private String guildInvitions;

	/**
	 * 公会职务，竞技场机器人用
	 */
	@JSONField(serialize = false)
	public Integer guildPosition = 0;
	
	/**
	 * 退出公会时间
	 */
	@JSONField(serialize = false)
	private Long guildExitTime = 0l;
	/**
	 * 客户端用
	 */
	public String clientGuildExitTime="0";
	
	/**
	 * 宠物列表
	 */
	private String rpets;
	/**
	 * 当前出站宠物
	 */
	private Integer rpetFighterId = 0;

	/**
	 * 宠物钻石抽-今天次数
	 */
	private Integer petRollDiamondTodayTimes = 0;
	/**
	 * 宠物钻石抽-累计次数
	 */
	private Integer petRollDiamondTotalTimes = 0;
	/**
	 * 宠物钻石抽-下次免费时间
	 */
	private Long petRollDiamondFreeNextTime = System.currentTimeMillis();
	/**
	 * 宠物金币抽-今天次数
	 */
	private Integer petRollGoldTodayTimes = 0;
	/**
	 * 宠物金币抽-累计次数
	 */
	private Integer petRollGoldTotalTimes = 0;
	/**
	 * 宠物金币抽-今天次数
	 */
	private Long petRollGoldFreeNextTime = System.currentTimeMillis();

	/**
	 * 宠物星座（星座ID|强化次数|属性ID:属性等级#属性ID:属性等级,）
	 */
	public String petConstell;
	/**
	 * 宠物天赋（天赋ID|天赋ID...）
	 */
	public String petTalent;

	/**
	 * 附魂
	 */
	private String slotSouls;

	/**
	 * 用户活动信息
	 */
	@JSONField(serialize = false)
	private String activityInfo;

	/**
	 * 充值相关信息
	 */
	@JSONField(serialize = false)
	private String rechargeInfo;

	/**
	 * 累计登录奖励记录
	 */
	@JSONField(serialize = false)
	private String cumulativeLoginAwardRecord;

	/**
	 * 领取过得累计登录奖励记录列表
	 */
	@JSONField(serialize = false)
	private String toTakeCumulativeLoginAwardRecord;

	/**
	 * 累计登录奖励记录列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listCumulativeLoginAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();
	/**
	 * 领取过得累计登录奖励记录列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listToTakeCumulativeLoginAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 等级奖励记录
	 */
	private String levelAwardRecord;

	/**
	 * 等级奖励记录列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listLevelAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 在线奖励记录列表
	 */
	private String onlineTimeAwrodRecord;
	/**
	 * 在线奖励记录列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listOnlineTimeAwrodRecord = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 同一天的在线时间 (秒)
	 */
	public Integer currentSameDayOnlineTime = 0;

	/**
	 * 同一天幸运转盘的次数
	 */
	public Integer currentSameDayLuckyWheelNumberOfFree = 0;

	/**
	 * 同一天下一次幸运转盘免费的时间(秒)
	 */
	public Integer currentTakeLuckyWheelFreeNextTime = 0;
	
	/** 获取每天金币膜拜状态 */
	public Integer dailyWorshipGoldStatus = 0;
	/** 获取每天钻石膜拜状态 */
	public Integer dailyWorshipDiamondStatus =0;

	/**
	 * 月签到奖励记录
	 */
	@JSONField(serialize = false)
	private String signInAwardRecord;
	/**
	 * 月签到奖励记录列表 int1 月份； int2 天数； int3：0没有领取，1：普通领取，2vip领取
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listSignInAwardRecord = new CopyOnWriteArrayList<IdNumberVo2>();

	/**
	 * 同一天是否领取签到奖励
	 */
	public Integer signInAwardSameDayIsTake = 0;

	/**
	 * 每日活跃度奖励记录
	 */
	@JSONField(serialize = false)
	private String dailyLivelyAwardRecord;
	/**
	 * 每日活跃度奖励记录列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listDailyLivelyAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();



	/**
	 * vip 等级
	 */
	private Integer vipLv = 0;

	/**
	 * 魔魂
	 */
	private Integer petSoul = 0;

	/**
	 * 每天购买商品数量
	 */
	private String everyDayBuyProductCount;

	/**
	 * 只能购买商品数量
	 */
	private String onlyBuyProductCount;

	/**
	 * 每天购买商品的重置时间
	 */
	private Long everyDayBuyProductResetTime = 0l;

	/**
	 * 每日公会捐献金币次数
	 */
	private Integer dailyGuildContributeGoldCount;

	/**
	 * 每日公会捐献道具次数
	 */
	private Integer dailyGuildContributeItemCount;

	/**
	 * 每日公会捐献的重置时间
	 */
	private Long dailyGuildContributeResetTime;

	/**
	 * 开启系统List
	 */
	private String openSystemList;
	/**
	 * 当前完成任务Id
	 */
	private Integer currentFinishTaskId = 0;

	/**
	 * 角色设置
	 */
	private String roleOptions;
	/**
	 * 在线时间列表
	 */
	private String onlineTime;

	/**
	 * 当前完成环任务数
	 */
	private Integer ringTaskCurrentIndex = 1;
	/**
	 * 当前环任务星级
	 */
	private Integer ringTaskCurrentQuality = 1;

	/**
	 * 成就点数
	 */
	private Integer achievePoint = 0;

	/**
	 * 普通称号等级
	 */
	private Integer titleLv = 0;
	
	/**
	 * 特殊称号等级
	 */
	@JSONField(serialize = false)
	private Integer currentSpecialTitleLv = 0;
	
	/**
     * 客户端显示称号
     */
     public int nowTitleLv=0;
     
     /**
      * 总计充值钻石
      */
     private Integer totalDiamondCharged=0;
     
     /**
      * 灵魂属性等级
      */
     private String soulAtb;
     
     /**
      * 当前灵魂属性
      */
     private Integer soulType = 0;
     
     /**
      * 灵魂值
      */
     private Integer soul = 0;
     
     /**
      * 经验值兑换灵魂值次数
      */
     private Integer soulExchangeTimes = 0;

	/**
	 * 特殊称号(称号等级|称号类型|到期时间,称号等级|称号类型|到期时间..)
	 */
     @JSONField(serialize = false)
	private String specialTitleLv;

     /**
      * 特殊称号列表
      */
    @JSONField(serialize = false)
    public CopyOnWriteArrayList<SpecialTitleVo> listSpecialTitle = new CopyOnWriteArrayList<SpecialTitleVo>();

	@JSONField(serialize = false)
	private Long siegeLastAwardTime = 0l;

	@JSONField(serialize = false)
	private Long domainLastAwardTime = 0l;
	/**
	 * 声望
	 */
	private Integer prestige = 0;
	
	/**
	 * 总声望
	 */
	@JSONField(serialize = false)
	private Integer prestigeTotal = 0;

	/**
	 * 是否是机器人
	 */
	@JSONField(serialize = false)
	public Boolean robot = false;
	@JSONField(serialize = false)
	public Boolean taskRobot = false;

	/**
	 * 竞技场机器人是否战斗中
	 */
	@JSONField(serialize = false)
	public Boolean arenaWasFighting = false;

	/**
	 * 每天购买商品数量List
	 */
	public CopyOnWriteArrayList<IdNumberVo> listEveryDayBuyProductCount = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 只能购买商品数量List
	 */
	public CopyOnWriteArrayList<IdNumberVo> listOnlyBuyProductCount = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 开启系统List
	 */
	public CopyOnWriteArrayList<Integer> openSystemArrayList = new CopyOnWriteArrayList<Integer>();

	/**
	 * 角色设置列表
	 */
	public CopyOnWriteArrayList<IdNumberVo> listRoleOptions = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 在线时间列表 1.红名； 2.灰名； 3.累计在线时间
	 */
	public CopyOnWriteArrayList<IdLongVo2> listOnlineTime = new CopyOnWriteArrayList<IdLongVo2>();

	/**
	 * 奖励找回数据记录
	 */
	@JSONField(serialize = false)
	public String awardRetrieve;
	/**
	 * 奖励找回数据记录列表
	 */
	public CopyOnWriteArrayList<AwardRetrieveVo> listAwardRetrieve = new CopyOnWriteArrayList<AwardRetrieveVo>();
	
	/**
	 * 活动记录列表
	 */
	private String roleLiveActivitys;


	/**
	 * 连斩记录
	 */
	private String sustainKill;

	/**
	 * 连斩记录
	 */
	public SustainKillVo sustainKillVo = new SustainKillVo();

	/**
	 * 玩家时装信息（时装ID|到期时间｜是否穿戴,时装ID|到期时间｜是否穿戴...）
	 */
	private String fashion;

	/**
	 * 玩家当前穿戴的时装
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<RoleFashionVo> roleFashion = new CopyOnWriteArrayList<RoleFashionVo>();

	/**
	 * 玩家拥有时装列表
	 */
	public CopyOnWriteArrayList<RoleFashionVo> roleFashions = new CopyOnWriteArrayList<RoleFashionVo>();
	
	/**
	 * 时装隐藏状况
	 */
	public List<IdNumberVo> hiddenFashions = new ArrayList<IdNumberVo>();

	/**
	 * PVP PVE 活动 最大记录
	 */
	private String fightActivityMaxScoreRecords;
	/**
	 * 每一项第一次充值状态
	 */
	@JSONField(serialize = false)
	private String eachFirstRechargeStatus;

	/**
	 * 每一项第一次充值状态List
	 */
	public CopyOnWriteArrayList<IdNumberVo> listEachFirstRechargeStatus = new CopyOnWriteArrayList<IdNumberVo>();

	/**
	 * 公会boss奖励记录
	 */
	@JSONField(serialize = false)
	private String guildBossAward;

	/**
	 * 公会boss奖励记录
	 */
	public CopyOnWriteArrayList<IdNumberVo> listGuildBossAward = new CopyOnWriteArrayList<IdNumberVo>();

	/** 公会boss奖励列表刷新时间 */
	private  Long guildBossAwardFlushTime = 0l;
	
	/** 下一次被攻击提示时间 */
	@JSONField(serialize = false)
	public long lastWarningTime=0l;
	
	/**
	 * 
	 */
	private String productTodayBuyed;
	
	/**
	 * 
	 */
	private String roleTreasures;
	
	
	public CopyOnWriteArrayList<IdNumberVo> listProductTodayBuyed = new CopyOnWriteArrayList<IdNumberVo>();

	
	/**
	 * 0=未完成  1=未领奖
	 */
	public List<IdNumberVo> listRoleTreasures;
	
	
	private String lastLoginIp;
	/**
	 * 渠道key
	 */
	private String channelKey;
	
	/** 阵营 */
	@JSONField(serialize = false)
	private Integer militaryForces=0;
	
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	/**
	 * 用户创号时间
	 */
	private Long userCreatedTime;
	
	/**
	 * 累计在线时间
	 */
	private Integer totalOnline=0;
	
	/**
	 * 是否隐藏时装
	 */
	private String wasHiddenFashion;
	/**
	 * 首次充值状态
	 */
	@JSONField(serialize = false)
	private Integer firstRechargeState=0;
	
	/**
	 * 宠物最高战力
	 */
	@JSONField(serialize = false)
	private Integer petPrower=0;
	
	/**
	 * 总成就点数
	 */
	@JSONField(serialize = false)
	private Integer achievementSum=0;
	
	/**
	 * 资源场景挂机时间
	 */
	private Integer resourceSceneTime=1000*60*30;
	
	/**
	 * 进入资源场景时间
	 */
	private Long startResourceSceneTime=System.currentTimeMillis();
	
	
	/**
	 * 是否购买成长基金1
	 */
	private Integer wasGrowFunds1=0;
	
	/**
	 * 是否购买成长基金2
	 */
	private Integer wasGrowFunds2=0;
	
	/**
	 * 成长基金2领取天数
	 */
	public Integer growFunds2TakeDay=0;
	
	/**
	 * 成长基金奖励1
	 */
	@JSONField(serialize = false)
	private String growFundsAward1;
	
	/**
	 * 成长基金奖励2
	 */
	@JSONField(serialize = false)
	private String growFundsAward2;
	
	/**
	 * 成长基金记录
	 */
	private String growFundStr;
	
	/**
	 * 成长基金Vo
	 */
	@JSONField(serialize = false)
	public GrowFundVo growFundVo = new GrowFundVo();
	
	/**
	 * 邀请码
	 */
	private String invitationCode="";
	
	/**
	 * 输入的邀请码
	 */
	private String inputInvitationCode="";
	
	/**
	 * 邀请任务
	 */
	@JSONField(serialize = false)
	private String invitationTask;
	
	/**
	 * 邀请的好友列表
	 */
	@JSONField(serialize = false)
	private String invitationFriend;
	
	/**
	 * 是否有新的仇人
	 */
	private Integer wasNewEnemy = 0;
	
	/**
	 * 背包解锁次数
	 */
	private Integer packUnlockTimes =0;
	
	/**
	 * 宠物开启最大格子数
	 */
	private Integer petMaxSkillSize = 0;
	
	private String listConsumCost;
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "user_id")
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		changed("user_id", userId, this.userId);
		this.userId = userId;
	}

	
	@Column(name = "create_time")
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		changed("create_time", createTime, this.createTime);
		this.createTime = createTime;
	}

	@Column(name = "last_login_time")
	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		changed("last_login_time", lastLoginTime, this.lastLoginTime);
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name = "last_logoff_time")
	public Long getLastLogoffTime() {
		return lastLogoffTime;
	}

	public void setLastLogoffTime(Long lastLogoffTime) {
		changed("last_logoff_time", lastLogoffTime, this.lastLogoffTime);
		this.lastLogoffTime = lastLogoffTime;
	}

	
	@Column(name = "off_line_reward_minutes")
	public Integer getOffLineRewardMinutes() {
		return offLineRewardMinutes;
	}

	public void setOffLineRewardMinutes(Integer offLineRewardMinutes) {
		changed("off_line_reward_minutes", offLineRewardMinutes, this.offLineRewardMinutes);
		this.offLineRewardMinutes = offLineRewardMinutes;
	}
	
	@Column(name = "dota_first_award")
	public String getDotaFirstAward() {
		return dotaFirstAward;
	}

	public void setDotaFirstAward(String dotaFirstAward) {
		changed("dota_first_award", dotaFirstAward, this.dotaFirstAward);
		this.dotaFirstAward = dotaFirstAward;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		changed("name", name, this.name);
		this.name = name;
	}

	@Column(name = "lv")
	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		changed("lv", lv, this.lv);
		this.lv = lv;
	}

	@Column(name = "exp")
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		changed("exp", exp, this.exp);
		this.exp = exp;
	}

	@Column(name = "iuid")
	public String getIuid() {
		return iuid;
	}

	public void setIuid(String iuid) {
		changed("iuid", iuid, this.iuid);
		this.iuid = iuid;
	}

	@Column(name = "pssd")
	public String getPssd() {
		return pssd;
	}

	public void setPssd(String pssd) {
		changed("pssd", pssd, this.pssd);
		this.pssd = pssd;
	}

	@Column(name = "gold")
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		gold = CheckUtil.propertiesBelowZero(gold);
		changed("gold", gold, this.gold);
		this.gold = gold;
	}

	@Column(name = "bind_gold")
	public Integer getBindGold() {
		return bindGold;
	}

	public void setBindGold(Integer bindGold) {
		bindGold = CheckUtil.propertiesBelowZero(bindGold);
		changed("bind_gold", bindGold, this.bindGold);
		this.bindGold = bindGold;
	}

	
	
	
	@Column(name = "warehouse_bind_gold")
	public Integer getWarehouseBindGold() {
		return warehouseBindGold;
	}

	public void setWarehouseBindGold(Integer warehouseBindGold) {
		changed("warehouse_bind_gold", warehouseBindGold, this.warehouseBindGold);
		this.warehouseBindGold = warehouseBindGold;
	}

	@Column(name = "warehouse_gold")
	public Integer getWarehouseGold() {
		return warehouseGold;
	}

	public void setWarehouseGold(Integer warehouseGold) {
		changed("warehouse_gold", warehouseGold, this.warehouseGold);
		this.warehouseGold = warehouseGold;
	}

	@Column(name = "diamond")
	public Integer getDiamond() {
		return diamond;
	}

	public void setDiamond(Integer diamond) {
		diamond = CheckUtil.propertiesBelowZero(diamond);
		changed("diamond", diamond, this.diamond);
		this.diamond = diamond;
	}

	@Column(name = "bind_diamond")
	public Integer getBindDiamond() {
		return bindDiamond;
	}

	public void setBindDiamond(Integer bindDiamond) {
		bindDiamond = CheckUtil.propertiesBelowZero(bindDiamond);
		changed("bind_diamond", bindDiamond, this.bindDiamond);
		this.bindDiamond = bindDiamond;
	}

	public String getPacks() {
		return packs;
	}

	public void setPacks(String packs) {
		changed("packs", packs, this.packs);
		this.packs = packs;
	}

	@Column(name = "abandom_state")
	public Integer getAbandomState() {
		return abandomState;
	}

	public void setAbandomState(Integer abandomState) {
		changed("abandom_state", abandomState, this.abandomState);
		this.abandomState = abandomState;
	}

	@Column(name = "token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		changed("token", token, this.token);
		this.token = token;
	}

	@Column(name = "room_id")
	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		changed("room_id", roomId, this.roomId);
		this.roomId = roomId;
	}

	@Column(name = "x")
	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		changed("x", x, this.x);
		this.x = x;
	}

	@Column(name = "y")
	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		changed("y", y, this.y);
		this.y = y;
	}

	@Column(name = "z")
	public Integer getZ() {
		return z;
	}

	public void setZ(Integer z) {
		changed("z", z, this.z);
		this.z = z;
	}

	@Column(name = "bat_melee_attack_min")
	public Integer getBatMeleeAttackMin() {
		return batMeleeAttackMin;
	}

	public void setBatMeleeAttackMin(Integer batMeleeAttackMin) {
		changed("bat_melee_attack_min", batMeleeAttackMin,this.batMeleeAttackMin);
		this.batMeleeAttackMin = batMeleeAttackMin;
	}

	@Column(name = "bat_magic_attack_min")
	public Integer getBatMagicAttackMin() {
		return batMagicAttackMin;
	}

	public void setBatMagicAttackMin(Integer batMagicAttackMin) {
		changed("bat_magic_attack_min", batMagicAttackMin,this.batMagicAttackMin);
		this.batMagicAttackMin = batMagicAttackMin;
	}

	@Column(name = "bat_melee_defence_min")
	public Integer getBatMeleeDefenceMin() {
		return batMeleeDefenceMin;
	}

	public void setBatMeleeDefenceMin(Integer batMeleeDefenceMin) {
		changed("bat_melee_defence_min", batMeleeDefenceMin,this.batMeleeDefenceMin);
		this.batMeleeDefenceMin = batMeleeDefenceMin;
	}

	@Column(name = "bat_magic_defence_min")
	public Integer getBatMagicDefenceMin() {
		return batMagicDefenceMin;
	}

	public void setBatMagicDefenceMin(Integer batMagicDefenceMin) {
		changed("bat_magic_defence_min", batMagicDefenceMin,this.batMagicDefenceMin);
		this.batMagicDefenceMin = batMagicDefenceMin;
	}

	@Column(name = "bat_melee_attack_max")
	public Integer getBatMeleeAttackMax() {
		return batMeleeAttackMax;
	}

	public void setBatMeleeAttackMax(Integer batMeleeAttackMax) {
		changed("bat_melee_attack_max", batMeleeAttackMax,this.batMeleeAttackMax);
		this.batMeleeAttackMax = batMeleeAttackMax;
	}

	@Column(name = "bat_magic_attack_max")
	public Integer getBatMagicAttackMax() {
		return batMagicAttackMax;
	}

	public void setBatMagicAttackMax(Integer batMagicAttackMax) {
		changed("bat_magic_attack_max", batMagicAttackMax,this.batMagicAttackMax);
		this.batMagicAttackMax = batMagicAttackMax;
	}

	@Column(name = "bat_melee_defence_max")
	public Integer getBatMeleeDefenceMax() {
		return batMeleeDefenceMax;
	}

	public void setBatMeleeDefenceMax(Integer batMeleeDefenceMax) {
		changed("bat_melee_defence_max", batMeleeDefenceMax,this.batMeleeDefenceMax);
		this.batMeleeDefenceMax = batMeleeDefenceMax;
	}

	@Column(name = "bat_magic_defence_max")
	public Integer getBatMagicDefenceMax() {
		return batMagicDefenceMax;
	}

	public void setBatMagicDefenceMax(Integer batMagicDefenceMax) {
		changed("bat_magic_defence_max", batMagicDefenceMax,this.batMagicDefenceMax);
		this.batMagicDefenceMax = batMagicDefenceMax;
	}

	@Column(name = "bat_hp")
	public Integer getBatHp() {
		return batHp;
	}

	public void setBatHp(Integer batHp) {
		changed("bat_hp", batHp, this.batHp);
		this.batHp = batHp;
	}

	@Column(name = "bat_mp")
	public Integer getBatMp() {
		return batMp;
	}

	public void setBatMp(Integer batMp) {
		changed("bat_mp", batMp, this.batMp);
		this.batMp = batMp;
	}

	@Column(name = "bat_critical")
	public Integer getBatCritical() {
		return batCritical;
	}

	public void setBatCritical(Integer batCritical) {
		changed("bat_critical", batCritical, this.batCritical);
		this.batCritical = batCritical;
	}

	@Column(name = "bat_dodge")
	public Integer getBatDodge() {
		return batDodge;
	}

	public void setBatDodge(Integer batDodge) {
		changed("bat_dodge", batDodge, this.batDodge);
		this.batDodge = batDodge;
	}

	@Column(name = "bat_hp_reg")
	public Integer getBatHpReg() {
		return batHpReg;
	}

	public void setBatHpReg(Integer batHpReg) {
		changed("bat_hp_reg", batHpReg, this.batHpReg);
		this.batHpReg = batHpReg;
	}

	@Column(name = "bat_mp_reg")
	public Integer getBatMpReg() {
		return batMpReg;
	}

	public void setBatMpReg(Integer batMpReg) {
		changed("bat_mp_reg", batMpReg, this.batMpReg);
		this.batMpReg = batMpReg;
	}

	@Column(name = "bat_movement")
	public Integer getBatMovement() {
		return batMovement;
	}

	public void setBatMovement(Integer batMovement) {
		changed("bat_movement", batMovement, this.batMovement);
		this.batMovement = batMovement;
	}

	@Column(name = "bat_lucky_attack")
	public Integer getBatLuckyAttack() {
		return batLuckyAttack;
	}

	public void setBatLuckyAttack(Integer batLuckyAttack) {
		changed("bat_lucky_attack", batLuckyAttack, this.batLuckyAttack);
		this.batLuckyAttack = batLuckyAttack;
	}

	@Column(name = "bat_hit_rate")
	public Integer getBatHitRate() {
		return batHitRate;
	}

	public void setBatHitRate(Integer batHitRate) {
		changed("bat_hit_rate", batHitRate, this.batHitRate);
		this.batHitRate = batHitRate;
	}

	@Column(name = "bat_melee_defence")
	public Integer getBatMeleeDefence() {
		return batMeleeDefence;
	}

	public void setBatMeleeDefence(Integer batMeleeDefence) {
		changed("bat_melee_defence", batMeleeDefence, this.batMeleeDefence);
		this.batMeleeDefence = batMeleeDefence;
	}

	@Column(name = "bat_magic_defence")
	public Integer getBatMagicDefence() {
		return batMagicDefence;
	}

	public void setBatMagicDefence(Integer batMagicDefence) {
		changed("bat_magic_defence", batMagicDefence, this.batMagicDefence);
		this.batMagicDefence = batMagicDefence;
	}

	@Column(name = "bat_lucky_defence")
	public Integer getBatLuckyDefence() {
		return batLuckyDefence;
	}

	public void setBatLuckyDefence(Integer batLuckyDefence) {
		changed("bat_lucky_defence", batLuckyDefence, this.batLuckyDefence);
		this.batLuckyDefence = batLuckyDefence;
	}

	@Column(name = "bat_tough")
	public Integer getBatTough() {
		return batTough;
	}

	public void setBatTough(Integer batTough) {
		changed("bat_tough", batTough, this.batTough);
		this.batTough = batTough;
	}

	@Column(name = "equip_weapon_id")
	public Integer getEquipWeaponId() {
		return equipWeaponId;
	}

	public void setEquipWeaponId(Integer equipWeaponId) {
		changed("equip_weapon_id", equipWeaponId, this.equipWeaponId);
		this.equipWeaponId = equipWeaponId;
	}

	@Column(name = "equip_armor_id")
	public Integer getEquipArmorId() {
		return equipArmorId;
	}

	public void setEquipArmorId(Integer equipArmorId) {
		changed("equip_armor_id", equipArmorId, this.equipArmorId);
		this.equipArmorId = equipArmorId;
	}

	@Column(name = "equip_necklace_id")
	public Integer getEquipNecklaceId() {
		return equipNecklaceId;
	}

	public void setEquipNecklaceId(Integer equipNecklaceId) {
		changed("equip_necklace_id", equipNecklaceId, this.equipNecklaceId);
		this.equipNecklaceId = equipNecklaceId;
	}

	@Column(name = "equip_bracer_id")
	public Integer getEquipBracerId() {
		return equipBracerId;
	}

	public void setEquipBracerId(Integer equipBracerId) {
		changed("equip_bracer_id", equipBracerId, this.equipBracerId);
		this.equipBracerId = equipBracerId;
	}

	@Column(name = "equip_helmet_id")
	public Integer getEquipHelmetId() {
		return equipHelmetId;
	}

	public void setEquipHelmetId(Integer equipHelmetId) {
		changed("equip_helmet_id", equipHelmetId, this.equipHelmetId);
		this.equipHelmetId = equipHelmetId;
	}

	@Column(name = "equip_shoe_id")
	public Integer getEquipShoeId() {
		return equipShoeId;
	}

	public void setEquipShoeId(Integer equipShoeId) {
		changed("equip_shoe_id", equipShoeId, this.equipShoeId);
		this.equipShoeId = equipShoeId;
	}

	@Column(name = "equip_ring_id")
	public Integer getEquipRingId() {
		return equipRingId;
	}

	public void setEquipRingId(Integer equipRingId) {
		changed("equip_ring_id", equipRingId, this.equipRingId);
		this.equipRingId = equipRingId;
	}

	@Column(name = "equip_bracelet_id")
	public Integer getEquipBraceletId() {
		return equipBraceletId;
	}

	public void setEquipBraceletId(Integer equipBraceletId) {
		changed("equip_bracelet_id", equipBraceletId, this.equipBraceletId);
		this.equipBraceletId = equipBraceletId;
	}

	@Column(name = "equip_pants_id")
	public Integer getEquipPantsId() {
		return equipPantsId;
	}

	public void setEquipPantsId(Integer equipPantsId) {
		changed("equip_pants_id", equipPantsId, this.equipPantsId);
		// System.out.println("2 "+getName() + " setEquipPantsId == " +
		// equipPantsId);
		this.equipPantsId = equipPantsId;
	}

	@Column(name = "equip_belt_id")
	public Integer getEquipBeltId() {
		return equipBeltId;
	}

	public void setEquipBeltId(Integer equipBeltId) {
		changed("equip_belt_id", equipBeltId, this.equipBeltId);
		this.equipBeltId = equipBeltId;
	}

	@Column(name = "main_pack_item_vos")
	public String getMainPackItemVos() {
		return mainPackItemVos;
	}

	public void setMainPackItemVos(String mainPackItemVos) {
		changed("main_pack_item_vos", mainPackItemVos, this.mainPackItemVos);
		this.mainPackItemVos = mainPackItemVos;
	}

	@Column(name = "warehouse_pack_item_vos")
	public String getWarehousePackItemVos() {
		return warehousePackItemVos;
	}

	public void setWarehousePackItemVos(String warehousePackItemVos) {
		changed("warehouse_pack_item_vos", warehousePackItemVos, this.warehousePackItemVos);
		this.warehousePackItemVos = warehousePackItemVos;
	}

	@Column(name = "career")
	public Integer getCareer() {
		return career;
	}

	public void setCareer(Integer career) {
		changed("career", career, this.career);
		this.career = career;
	}

	@Column(name = "arena_today_played_times")
	public Integer getArenaTodayPlayedTimes() {
		return arenaTodayPlayedTimes;
	}

	public void setArenaTodayPlayedTimes(Integer arenaTodayPlayedTimes) {
		changed("arena_today_played_times", arenaTodayPlayedTimes,
				this.arenaTodayPlayedTimes);
		this.arenaTodayPlayedTimes = arenaTodayPlayedTimes;
	}

	@Column(name = "skill_vos")
	public String getSkillVos() {
		return skillVos;
	}

	public void setSkillVos(String skillVos) {
		changed("skill_vos", skillVos, this.skillVos);
		this.skillVos = skillVos;
	}

	@Column(name = "skill_point")
	public Integer getSkillPoint() {
		return skillPoint;
	}

	public void setSkillPoint(Integer skillPoint) {
		skillPoint = CheckUtil.propertiesBelowZero(skillPoint);
		changed("skill_point", skillPoint, this.skillPoint);
		this.skillPoint = skillPoint;
	}

	@Column(name = "skill_formation")
	public String getSkillFormation() {
		return skillFormation;
	}

	public void setSkillFormation(String skillFormation) {
		changed("skill_formation", skillFormation, this.skillFormation);
		this.skillFormation = skillFormation;
	}

	@Column(name = "role_tasks")
	public String getRoleTasks() {
		return roleTasks;
	}

	public void setRoleTasks(String roleTasks) {
		changed("role_tasks", roleTasks, this.roleTasks);
		this.roleTasks = roleTasks;
	}

	@Column(name = "role_achieves")
	public String getRoleAchieves() {
		return roleAchieves;
	}

	public void setRoleAchieves(String roleAchieves) {
		changed("role_achieves", roleAchieves, this.roleAchieves);
		this.roleAchieves = roleAchieves;
	}

	@Column(name = "social_team_id")
	public Integer getSocialTeamId() {
		return socialTeamId;
	}

	public void setSocialTeamId(Integer socialTeamId) {
		changed("social_team_id", socialTeamId, this.socialTeamId);
		this.socialTeamId = socialTeamId;
	}

	@Column(name = "pk_value")
	public Integer getPkValue() {
		return pkValue;
	}

	public void setPkValue(Integer pkValue) {
		changed("pk_value", pkValue, this.pkValue);
		this.pkValue = pkValue;
	}

	@Column(name = "pk_status")
	public Integer getPkStatus() {
		return pkStatus;
	}

	public void setPkStatus(Integer pkStatus) {
		changed("pk_status", pkStatus, this.pkStatus);
		this.pkStatus = pkStatus;
	}

	@Column(name = "pk_red_begin_time")
	public Long getPkRedBeginTime() {
		return pkRedBeginTime;
	}

	public void setPkRedBeginTime(Long pkRedBeginTime) {
		changed("pk_red_begin_time", pkRedBeginTime, this.pkRedBeginTime);
		this.pkRedBeginTime = pkRedBeginTime;
	}

	@Column(name = "pk_last_recover_time")
	public Long getPkLastRecoverTime() {
		return pkLastRecoverTime;
	}

	public void setPkLastRecoverTime(Long pkLastRecoverTime) {
		changed("pk_last_recover_time", pkLastRecoverTime,
				this.pkLastRecoverTime);
		this.pkLastRecoverTime = pkLastRecoverTime;
	}

	@Column(name = "attack_mode")
	public Integer getAttackMode() {
		return attackMode;
	}

	public void setAttackMode(Integer attackMode) {
		changed("attack_mode", attackMode, this.attackMode);
		this.attackMode = attackMode;
	}
	
	@Column(name = "diamond_basins_time")
	public Integer getDiamondBasinsTime(){
		return diamondBasinsTime;
	}
	
	public void setDiamondBasinsTime(Integer diamondBasinsTime){
		changed("diamond_basins_time", diamondBasinsTime, this.diamondBasinsTime);
		this.diamondBasinsTime = diamondBasinsTime;
	}
	
	@Column(name = "worship_diamond_first")
	public Integer getWorshipDiamondFirst() {
		return worshipDiamondFirst;
	}

	public void setWorshipDiamondFirst(Integer worshipDiamondFirst) {
		changed("worship_diamond_first", worshipDiamondFirst, this.worshipDiamondFirst);
		this.worshipDiamondFirst = worshipDiamondFirst;
	}

	@Column(name = "gamstone_fragment")
	public Integer getGamstoneFragment() {
		if(gamstoneFragment==null){
			gamstoneFragment = 0;
		}
		return gamstoneFragment;
	}

	public void setGamstoneFragment(Integer gamstoneFragment) {
		changed("gamstone_fragment", gamstoneFragment, this.gamstoneFragment);
		this.gamstoneFragment = gamstoneFragment;
	}

	@Column(name = "cimelias_id")
	public Integer getCimeliasId() {
		if(cimeliasId ==null){
			cimeliasId = 0;
		}
		return cimeliasId;
	}

	public void setCimeliasId(Integer cimeliasId) {
		changed("cimelias_id", cimeliasId, this.cimeliasId);
		this.cimeliasId = cimeliasId;
	}

	@JSONField()
	@Column(name = "attack_recover_time")
	public Long getAttackRecoverTime() {
		return attackRecoverTime;
	}

	public void setAttackRecoverTime(Long attackRecoverTime) {
		changed("attack_recover_time", attackRecoverTime,
				this.attackRecoverTime);
		this.attackRecoverTime = attackRecoverTime;
	}

	@Column(name = "pk_grew_recover_time")
	public Long getPkGrewRecoverTime() {
		return pkGrewRecoverTime;
	}

	public void setPkGrewRecoverTime(Long pkGrewRecoverTime) {
		changed("pk_grew_recover_time", pkGrewRecoverTime,
				this.pkGrewRecoverTime);
		this.pkGrewRecoverTime = pkGrewRecoverTime;
	}

	@Column(name = "static_room_id")
	public Integer getStaticRoomId() {
		return staticRoomId;
	}

	public void setStaticRoomId(Integer staticRoomId) {
		changed("static_room_id", staticRoomId, this.staticRoomId);
		this.staticRoomId = staticRoomId;
	}

	@Column(name = "tower_current_lv")
	public Integer getTowerCurrentLv() {
		return towerCurrentLv;
	}

	public void setTowerCurrentLv(Integer towerCurrentLv) {
		changed("tower_current_lv", towerCurrentLv, this.towerCurrentLv);
		this.towerCurrentLv = towerCurrentLv;
	}

	@Column(name = "tower_today_challenge_times")
	public Integer getTowerTodayChallengeTimes() {
		return towerTodayChallengeTimes;
	}

	public void setTowerTodayChallengeTimes(Integer towerTodayChallengeTimes) {
		changed("tower_today_challenge_times", towerTodayChallengeTimes,
				this.towerTodayChallengeTimes);
		this.towerTodayChallengeTimes = towerTodayChallengeTimes;
	}

	@Column(name = "tower_today_wipe_out_times")
	public Integer getTowerTodayWipeOutTimes() {
		return towerTodayWipeOutTimes;
	}

	public void setTowerTodayWipeOutTimes(Integer towerTodayWipeOutTimes) {
		changed("tower_today_wipe_out_times", towerTodayWipeOutTimes,
				this.towerTodayWipeOutTimes);
		this.towerTodayWipeOutTimes = towerTodayWipeOutTimes;
	}

	@Column(name = "copy_scene_today_visit_times")
	public String getCopySceneTodayVisitTimes() {
		return copySceneTodayVisitTimes;
	}

	public void setCopySceneTodayVisitTimes(String copySceneTodayVisitTimes) {
		changed("copy_scene_today_visit_times", copySceneTodayVisitTimes,
				this.copySceneTodayVisitTimes);
		this.copySceneTodayVisitTimes = copySceneTodayVisitTimes;
	}

	@Column(name = "buffer_status")
	public String getBufferStatus() {
		return bufferStatus;
	}

	public void setBufferStatus(String bufferStatus) {
		changed("buffer_status", bufferStatus, this.bufferStatus);
		this.bufferStatus = bufferStatus;
	}

	@Column(name = "bat_max_hp")
	public Integer getBatMaxHp() {
		return batMaxHp;
	}

	public void setBatMaxHp(Integer batMaxHp) {
		changed("bat_max_hp", batMaxHp, this.batMaxHp);
		this.batMaxHp = batMaxHp;
	}

	@Column(name = "bat_max_mp")
	public Integer getBatMaxMp() {
		return batMaxMp;
	}

	public void setBatMaxMp(Integer batMaxMp) {
		changed("bat_max_mp", batMaxMp, this.batMaxMp);
		this.batMaxMp = batMaxMp;
	}

	@Column(name = "resurnow_today_times")
	public Integer getResurnowTodayTimes() {
		return resurnowTodayTimes;
	}

	public void setResurnowTodayTimes(Integer resurnowTodayTimes) {
		changed("resurnow_today_times", resurnowTodayTimes,
				this.resurnowTodayTimes);
		this.resurnowTodayTimes = resurnowTodayTimes;
	}

	@Column(name = "resurnow_continue_times")
	public Integer getResurnowContinueTimes() {
		return resurnowContinueTimes;
	}

	public void setResurnowContinueTimes(Integer resurnowContinueTimes) {
		changed("resurnow_continue_times", resurnowContinueTimes,
				this.resurnowContinueTimes);
		this.resurnowContinueTimes = resurnowContinueTimes;
	}

	@Column(name = "friends")
	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		changed("friends", friends, this.friends);
		this.friends = friends;
	}

	@Column(name = "blocks")
	public String getBlocks() {
		return blocks;
	}

	public void setBlocks(String blocks) {
		changed("blocks", blocks, this.blocks);
		this.blocks = blocks;
	}

	@Column(name = "enemys")
	public String getEnemys() {
		return enemys;
	}

	public void setEnemys(String enemys) {
		changed("enemys", enemys, this.enemys);
		this.enemys = enemys;
	}

	@Column(name = "bat_damage_rate")
	public Integer getBatDamageRate() {
		return batDamageRate;
	}

	public void setBatDamageRate(Integer batDamageRate) {
		changed("bat_damage_rate", batDamageRate, this.batDamageRate);
		this.batDamageRate = batDamageRate;
	}

	@Column(name = "bat_damage_resist_rate")
	public Integer getBatDamageResistRate() {
		return batDamageResistRate;
	}

	public void setBatDamageResistRate(Integer batDamageResistRate) {
		changed("bat_damage_resist_rate", batDamageResistRate,
				this.batDamageResistRate);
		this.batDamageResistRate = batDamageResistRate;
	}

	@Column(name = "bat_critical_damage_rate")
	public Integer getBatCriticalDamageRate() {
		return batCriticalDamageRate;
	}

	public void setBatCriticalDamageRate(Integer batCriticalDamageRate) {
		changed("bat_critical_damage_rate", batCriticalDamageRate,
				this.batCriticalDamageRate);
		this.batCriticalDamageRate = batCriticalDamageRate;
	}

	@Column(name = "bat_attack_rate")
	public Integer getBatAttackRate() {
		return batAttackRate;
	}

	public void setBatAttackRate(Integer batAttackRate) {
		changed("bat_attack_rate", batAttackRate, this.batAttackRate);
		this.batAttackRate = batAttackRate;
	}

	@Column(name = "bat_defence_rate")
	public Integer getBatDefenceRate() {
		return batDefenceRate;
	}

	public void setBatDefenceRate(Integer batDefenceRate) {
		changed("bat_defence_rate", batDefenceRate, this.batDefenceRate);
		this.batDefenceRate = batDefenceRate;
	}

	@Column(name = "bat_melee_attack")
	public Integer getBatMeleeAttack() {
		return batMeleeAttack;
	}

	public void setBatMeleeAttack(Integer batMeleeAttack) {
		changed("bat_melee_attack", batMeleeAttack, this.batMeleeAttack);
		this.batMeleeAttack = batMeleeAttack;
	}

	@Column(name = "bat_magic_attack")
	public Integer getBatMagicAttack() {
		return batMagicAttack;
	}

	public void setBatMagicAttack(Integer batMagicAttack) {
		changed("bat_magic_attack", batMagicAttack, this.batMagicAttack);
		this.batMagicAttack = batMagicAttack;
	}

	@Column(name = "guild_auto_accept")
	public Integer getGuildAutoAccept() {
		return guildAutoAccept;
	}

	public void setGuildAutoAccept(Integer guildAutoAccept) {
		changed("guild_auto_accept", guildAutoAccept, this.guildAutoAccept);
		this.guildAutoAccept = guildAutoAccept;
	}

	@Column(name = "guild_honor")
	public Integer getGuildHonor() {
		return guildHonor;
	}

	public void setGuildHonor(Integer guildHonor) {
		changed("guild_honor", guildHonor, this.guildHonor);
		this.guildHonor = guildHonor;
	}

	@Column(name = "guild_today_contributed")
	public Integer getGuildTodayContributed() {
		return guildTodayContributed;
	}

	public void setGuildTodayContributed(Integer guildTodayContributed) {
		changed("guild_today_contributed", guildTodayContributed,
				this.guildTodayContributed);
		this.guildTodayContributed = guildTodayContributed;
	}

	@Column(name = "guild_today_exchange_items")
	public String getGuildTodayExchangeItems() {
		return guildTodayExchangeItems;
	}

	public void setGuildTodayExchangeItems(String guildTodayExchangeItems) {
		changed("guild_today_exchange_items", guildTodayExchangeItems,
				this.guildTodayExchangeItems);
		this.guildTodayExchangeItems = guildTodayExchangeItems;
	}

	@Column(name = "guild_id")
	public Integer getGuildId() {
		return guildId;
	}

	public void setGuildId(Integer guildId) {
		changed("guild_id", guildId, this.guildId);
		this.guildId = guildId;
	}

	@Column(name = "guild_invitions")
	public String getGuildInvitions() {
		return guildInvitions;
	}

	public void setGuildInvitions(String guildInvitions) {
		changed("guild_invitions", guildInvitions, this.guildInvitions);
		this.guildInvitions = guildInvitions;
	}
	
	
	@Column(name = "guild_exit_time")
	public Long getGuildExitTime() {
		return guildExitTime;
	}

	public void setGuildExitTime(Long guildExitTime) {
		changed("guild_exit_time", guildExitTime, this.guildExitTime);
		this.guildExitTime = guildExitTime;
	}

	@Column(name = "wing_equip_status")
	public Integer getWingEquipStatus() {
		return wingEquipStatus;
	}

	public void setWingEquipStatus(Integer wingEquipStatus) {
		changed("wing_equip_status", wingEquipStatus, this.wingEquipStatus);
		this.wingEquipStatus = wingEquipStatus;
	}

	@Column(name = "wing_star")
	public Integer getWingStar() {
		return wingStar;
	}

	public void setWingStar(Integer wingStar) {
		changed("wing_star", wingStar, this.wingStar);
		this.wingStar = wingStar;
	}

	@Column(name = "wing_star_exp")
	public Integer getWingStarExp() {
		return wingStarExp;
	}

	public void setWingStarExp(Integer wingStarExp) {
		changed("wing_star_exp", wingStarExp, this.wingStarExp);
		this.wingStarExp = wingStarExp;
	}

	@Column(name = "wing_step_poss")
	public Integer getWingStepPoss() {
		return wingStepPoss;
	}

	public void setWingStepPoss(Integer wingStepPoss) {
		changed("wing_step_poss", wingStepPoss, this.wingStepPoss);
		this.wingStepPoss = wingStepPoss;
	}

	@Column(name = "wing_was_hidden")
	public Integer getWingWasHidden() {
		return wingWasHidden;
	}

	public void setWingWasHidden(Integer wingWasHidden) {
		changed("wing_was_hidden", wingWasHidden, this.wingWasHidden);
		this.wingWasHidden = wingWasHidden;
	}

	@Column(name = "battle_power")
	public Integer getBattlePower() {
		return battlePower;
	}

	public void setBattlePower(Integer battlePower) {
		changed("battle_power", battlePower, this.battlePower);
		this.battlePower = battlePower;
	}

	@Column(name = "rpets")
	public String getRpets() {
		return rpets;
	}

	public void setRpets(String rpets) {
		changed("rpets", rpets, this.rpets);
		this.rpets = rpets;
	}

	@Column(name = "rpet_fighter_id")
	public Integer getRpetFighterId() {
		return rpetFighterId;
	}

	public void setRpetFighterId(Integer rpetFighterId) {
		changed("rpet_fighter_id", rpetFighterId, this.rpetFighterId);
		this.rpetFighterId = rpetFighterId;
	}

	@Column(name = "pet_roll_diamond_today_times")
	public Integer getPetRollDiamondTodayTimes() {
		return petRollDiamondTodayTimes;
	}

	public void setPetRollDiamondTodayTimes(Integer petRollDiamondTodayTimes) {
		changed("pet_roll_diamond_today_times", petRollDiamondTodayTimes,
				this.petRollDiamondTodayTimes);
		this.petRollDiamondTodayTimes = petRollDiamondTodayTimes;
	}

	@Column(name = "pet_roll_diamond_total_times")
	public Integer getPetRollDiamondTotalTimes() {
		return petRollDiamondTotalTimes;
	}

	public void setPetRollDiamondTotalTimes(Integer petRollDiamondTotalTimes) {
		changed("pet_roll_diamond_total_times", petRollDiamondTotalTimes,
				this.petRollDiamondTotalTimes);
		this.petRollDiamondTotalTimes = petRollDiamondTotalTimes;
	}

	@Column(name = "pet_roll_diamond_free_next_time")
	public Long getPetRollDiamondFreeNextTime() {
		return petRollDiamondFreeNextTime;
	}

	public void setPetRollDiamondFreeNextTime(Long petRollDiamondFreeNextTime) {
		changed("pet_roll_diamond_free_next_time", petRollDiamondFreeNextTime,
				this.petRollDiamondFreeNextTime);
		this.petRollDiamondFreeNextTime = petRollDiamondFreeNextTime;
	}

	@Column(name = "pet_roll_gold_today_times")
	public Integer getPetRollGoldTodayTimes() {
		return petRollGoldTodayTimes;
	}

	public void setPetRollGoldTodayTimes(Integer petRollGoldTodayTimes) {
		changed("pet_roll_gold_today_times", petRollGoldTodayTimes,
				this.petRollGoldTodayTimes);
		this.petRollGoldTodayTimes = petRollGoldTodayTimes;
	}

	@Column(name = "pet_roll_gold_total_times")
	public Integer getPetRollGoldTotalTimes() {
		return petRollGoldTotalTimes;
	}

	public void setPetRollGoldTotalTimes(Integer petRollGoldTotalTimes) {
		changed("pet_roll_gold_total_times", petRollGoldTotalTimes,
				this.petRollGoldTotalTimes);
		this.petRollGoldTotalTimes = petRollGoldTotalTimes;
	}

	@Column(name = "pet_roll_gold_free_next_time")
	public Long getPetRollGoldFreeNextTime() {
		return petRollGoldFreeNextTime;
	}

	public void setPetRollGoldFreeNextTime(Long petRollGoldFreeNextTime) {
		changed("pet_roll_gold_free_next_time", petRollGoldFreeNextTime,
				this.petRollGoldFreeNextTime);
		this.petRollGoldFreeNextTime = petRollGoldFreeNextTime;
	}

	@Column(name = "pet_constell")
	public String getPetConstell() {
		return petConstell;
	}

	public void setPetConstell(String petConstell) {
		changed("pet_constell", petConstell, this.petConstell);
		this.petConstell = petConstell;
	}

	@Column(name = "pet_talent")
	public String getPetTalent() {
		return petTalent;
	}

	public void setPetTalent(String petTalent) {
		changed("pet_talent", petTalent, this.petTalent);
		this.petTalent = petTalent;
	}

	@Column(name = "slot_souls")
	public String getSlotSouls() {
		return slotSouls;
	}

	public void setSlotSouls(String slotSouls) {
		changed("slot_souls", slotSouls, this.slotSouls);
		this.slotSouls = slotSouls;
	}

	@Column(name = "activity_info")
	public String getActivityInfo() {
		return activityInfo;
	}

	public void setActivityInfo(String activityInfo) {
		changed("activity_info", activityInfo, this.activityInfo);
		this.activityInfo = activityInfo;
	}

	@Column(name = "recharge_info")
	public String getRechargeInfo() {
		return rechargeInfo;
	}

	public void setRechargeInfo(String rechargeInfo) {
		changed("recharge_info", rechargeInfo, this.rechargeInfo);
		this.rechargeInfo = rechargeInfo;
	}

	@Column(name = "advance_suit_plus")
	public String getAdvanceSuitPlus() {
		return advanceSuitPlus;
	}

	public void setAdvanceSuitPlus(String advanceSuitPlus) {
		changed("advance_suit_plus", advanceSuitPlus, this.advanceSuitPlus);
		this.advanceSuitPlus = advanceSuitPlus;
	}

	@Column(name = "cumulative_login_award_record")
	public String getCumulativeLoginAwardRecord() {
		return cumulativeLoginAwardRecord;
	}

	public void setCumulativeLoginAwardRecord(String cumulativeLoginAwardRecord) {
		changed("cumulative_login_award_record", cumulativeLoginAwardRecord,
				this.cumulativeLoginAwardRecord);
		this.cumulativeLoginAwardRecord = cumulativeLoginAwardRecord;
	}

	@Column(name = "to_take_cumulative_login_award_record")
	public String getToTakeCumulativeLoginAwardRecord() {
		return toTakeCumulativeLoginAwardRecord;
	}

	public void setToTakeCumulativeLoginAwardRecord(
			String toTakeCumulativeLoginAwardRecord) {
		changed("to_take_cumulative_login_award_record",
				toTakeCumulativeLoginAwardRecord,
				this.toTakeCumulativeLoginAwardRecord);
		this.toTakeCumulativeLoginAwardRecord = toTakeCumulativeLoginAwardRecord;
	}

	@Column(name = "level_award_record")
	public String getLevelAwardRecord() {
		return levelAwardRecord;
	}

	public void setLevelAwardRecord(String levelAwardRecord) {
		changed("level_award_record", levelAwardRecord, this.levelAwardRecord);
		this.levelAwardRecord = levelAwardRecord;
	}

	@Column(name = "online_time_awrod_record")
	public String getOnlineTimeAwrodRecord() {
		return onlineTimeAwrodRecord;
	}

	public void setOnlineTimeAwrodRecord(String onlineTimeAwrodRecord) {
		changed("online_time_awrod_record", onlineTimeAwrodRecord,
				this.onlineTimeAwrodRecord);
		this.onlineTimeAwrodRecord = onlineTimeAwrodRecord;
	}

	@Column(name = "sign_in_award_record")
	public String getSignInAwardRecord() {
		return signInAwardRecord;
	}

	public void setSignInAwardRecord(String signInAwardRecord) {
		changed("sign_in_award_record", signInAwardRecord,
				this.signInAwardRecord);
		this.signInAwardRecord = signInAwardRecord;
	}

	@Column(name = "daily_lively_award_record")
	public String getDailyLivelyAwardRecord() {
		return dailyLivelyAwardRecord;
	}

	public void setDailyLivelyAwardRecord(String dailyLivelyAwardRecord) {
		changed("daily_lively_award_record", dailyLivelyAwardRecord,
				this.dailyLivelyAwardRecord);
		this.dailyLivelyAwardRecord = dailyLivelyAwardRecord;
	}

	@Column(name = "vip_lv")
	public Integer getVipLv() {
		return vipLv;
	}

	public void setVipLv(Integer vipLv) {
		changed("vip_lv", vipLv, this.vipLv);
		this.vipLv = vipLv;
	}

	@Column(name = "every_day_buy_product_count")
	public String getEveryDayBuyProductCount() {
		return everyDayBuyProductCount;
	}

	public void setEveryDayBuyProductCount(String everyDayBuyProductCount) {
		changed("every_day_buy_product_count", everyDayBuyProductCount,
				this.everyDayBuyProductCount);
		this.everyDayBuyProductCount = everyDayBuyProductCount;
	}

	@Column(name = "only_buy_product_count")
	public String getOnlyBuyProductCount() {
		return onlyBuyProductCount;
	}

	public void setOnlyBuyProductCount(String onlyBuyProductCount) {
		changed("only_buy_product_count", onlyBuyProductCount,
				this.onlyBuyProductCount);
		this.onlyBuyProductCount = onlyBuyProductCount;
	}

	@Column(name = "every_day_buy_product_reset_time")
	public Long getEveryDayBuyProductResetTime() {
		return everyDayBuyProductResetTime;
	}

	public void setEveryDayBuyProductResetTime(Long everyDayBuyProductResetTime) {
		changed("every_day_buy_product_reset_time",
				everyDayBuyProductResetTime, this.everyDayBuyProductResetTime);
		this.everyDayBuyProductResetTime = everyDayBuyProductResetTime;
	}

	@Column(name = "mail_unread")
	public Integer getMailUnread() {
		return mailUnread;
	}

	public void setMailUnread(Integer mailUnread) {
		changed("mail_unread", mailUnread, this.mailUnread);
		this.mailUnread = mailUnread;
	}

	@Column(name = "pet_soul")
	public Integer getPetSoul() {
		return petSoul;
	}

	public void setPetSoul(Integer petSoul) {
		changed("pet_soul", petSoul, this.petSoul);
		this.petSoul = petSoul;
	}

	@Column(name = "daily_guild_contribute_gold_count")
	public Integer getDailyGuildContributeGoldCount() {
		return dailyGuildContributeGoldCount;
	}

	public void setDailyGuildContributeGoldCount(
			Integer dailyGuildContributeGoldCount) {
		changed("daily_guild_contribute_gold_count",
				dailyGuildContributeGoldCount,
				this.dailyGuildContributeGoldCount);
		this.dailyGuildContributeGoldCount = dailyGuildContributeGoldCount;
	}

	@Column(name = "daily_guild_contribute_item_count")
	public Integer getDailyGuildContributeItemCount() {
		return dailyGuildContributeItemCount;
	}

	public void setDailyGuildContributeItemCount(
			Integer dailyGuildContributeItemCount) {
		changed("daily_guild_contribute_item_count",
				dailyGuildContributeItemCount,
				this.dailyGuildContributeItemCount);
		this.dailyGuildContributeItemCount = dailyGuildContributeItemCount;
	}

	@Column(name = "daily_guild_contribute_reset_time")
	public Long getDailyGuildContributeResetTime() {
		return dailyGuildContributeResetTime;
	}

	public void setDailyGuildContributeResetTime(
			Long dailyGuildContributeResetTime) {
		changed("daily_guild_contribute_reset_time",
				dailyGuildContributeResetTime,
				this.dailyGuildContributeResetTime);
		this.dailyGuildContributeResetTime = dailyGuildContributeResetTime;
	}

	@Column(name = "open_system_list")
	public String getOpenSystemList() {
		return openSystemList;
	}

	public void setOpenSystemList(String openSystemList) {
		changed("open_system_list", openSystemList, this.openSystemList);
		this.openSystemList = openSystemList;
	}

	@Column(name = "role_infor_id")
	public Integer getRoleInforId() {
		return roleInforId;
	}

	public void setRoleInforId(Integer roleInforId) {
		changed("role_infor_id", roleInforId, this.roleInforId);
		this.roleInforId = roleInforId;
	}
	
	
	
	@Column(name = "device_id")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		changed("device_id", deviceId, this.deviceId);
		this.deviceId = deviceId;
	}

	@Column(name = "current_finish_taskId")
	public Integer getCurrentFinishTaskId() {
		return currentFinishTaskId;
	}

	public void setCurrentFinishTaskId(Integer currentFinishTaskId) {
		changed("current_finish_taskId", currentFinishTaskId,
				this.currentFinishTaskId);
		this.currentFinishTaskId = currentFinishTaskId;
	}

	@Column(name = "role_options")
	public String getRoleOptions() {
		return roleOptions;
	}

	public void setRoleOptions(String roleOptions) {
		changed("role_options", roleOptions, this.roleOptions);
		this.roleOptions = roleOptions;
	}

	@Column(name = "online_time")
	public String getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(String onlineTime) {
		changed("online_time", onlineTime, this.onlineTime);
		this.onlineTime = onlineTime;
	}

	@Column(name = "bat_hit_rate_rate")
	public Integer getBatHitRateRate() {
		return batHitRateRate;
	}

	public void setBatHitRateRate(Integer batHitRateRate) {
		changed("bat_hit_rate_rate", batHitRateRate, this.batHitRateRate);
		this.batHitRateRate = batHitRateRate;
	}

	@Column(name = "bat_dodge_rate")
	public Integer getBatDodgeRate() {
		return batDodgeRate;
	}

	public void setBatDodgeRate(Integer batDodgeRate) {
		changed("bat_dodge_rate", batDodgeRate, this.batHitRateRate);
		this.batDodgeRate = batDodgeRate;
	}

	@Column(name = "bat_add_critical_rate")
	public Integer getBatAddCriticalRate() {
		return batAddCriticalRate;
	}

	public void setBatAddCriticalRate(Integer batAddCriticalRate) {
		changed("bat_add_critical_rate", batAddCriticalRate,
				this.batAddCriticalRate);
		this.batAddCriticalRate = batAddCriticalRate;
	}

	@Column(name = "bat_derate_critical_rate")
	public Integer getBatDerateCriticalRate() {
		return batDerateCriticalRate;
	}

	public void setBatDerateCriticalRate(Integer batDerateCriticalRate) {
		changed("bat_derate_critical_rate", batDerateCriticalRate,
				this.batDerateCriticalRate);
		this.batDerateCriticalRate = batDerateCriticalRate;
	}

	@Column(name = "bat_rebound_damage")
	public Integer getBatReboundDamage() {
		return batReboundDamage;
	}

	public void setBatReboundDamage(Integer batReboundDamage) {
		this.batReboundDamage = batReboundDamage;
	}

	@Column(name = "ring_task_current_index")
	public Integer getRingTaskCurrentIndex() {
		return ringTaskCurrentIndex;
	}

	public void setRingTaskCurrentIndex(Integer ringTaskCurrentIndex) {
		changed("ring_task_current_index", ringTaskCurrentIndex,
				this.ringTaskCurrentIndex);
		this.ringTaskCurrentIndex = ringTaskCurrentIndex;
	}

	@Column(name = "ring_task_current_quality")
	public Integer getRingTaskCurrentQuality() {
		return ringTaskCurrentQuality;
	}

	public void setRingTaskCurrentQuality(Integer ringTaskCurrentQuality) {
		changed("ring_task_current_quality", ringTaskCurrentQuality,
				this.ringTaskCurrentQuality);
		this.ringTaskCurrentQuality = ringTaskCurrentQuality;
	}

	@Column(name = "achieve_point")
	public Integer getAchievePoint() {
		return achievePoint;
	}

	public void setAchievePoint(Integer achievePoint) {
		changed("achieve_point", achievePoint, this.achievePoint);
		this.achievePoint = achievePoint;
	}

	@Column(name = "title_lv")
	public Integer getTitleLv() {
		return titleLv;
	}

	public void setTitleLv(Integer titleLv) {
		changed("title_lv", titleLv, this.titleLv);
		this.titleLv = titleLv;
	}
	
	@Column(name = "current_special_title_lv")
	public Integer getCurrentSpecialTitleLv() {
		return currentSpecialTitleLv;
	}

	public void setCurrentSpecialTitleLv(Integer currentSpecialTitleLv) {
		changed("current_special_title_lv", currentSpecialTitleLv, this.currentSpecialTitleLv);
		this.currentSpecialTitleLv = currentSpecialTitleLv;
	}

	@Column(name = "special_title_lv")
	public String getSpecialTitleLv() {
		return specialTitleLv;
	}

	public void setSpecialTitleLv(String specialTitleLv) {
		changed("special_title_lv", specialTitleLv, this.specialTitleLv);
		this.specialTitleLv = specialTitleLv;
	}

	@Column(name = "award_retrieve")
	public String getAwardRetrieve() {
		return awardRetrieve;
	}

	public void setAwardRetrieve(String awardRetrieve) {
		changed("award_retrieve", awardRetrieve, this.awardRetrieve);
		this.awardRetrieve = awardRetrieve;
	}

	@Column(name = "role_live_activitys")
	public String getRoleLiveActivitys() {
		return roleLiveActivitys;
	}

	public void setRoleLiveActivitys(String roleLiveActivitys) {
		changed("role_live_activitys", roleLiveActivitys,
				this.roleLiveActivitys);
		this.roleLiveActivitys = roleLiveActivitys;
	}

	@Column(name = "sustain_kill")
	public String getSustainKill() {
		return sustainKill;
	}

	@Column(name = "each_first_recharge_status")
	public String getEachFirstRechargeStatus() {
		return eachFirstRechargeStatus;
	}

	public void setEachFirstRechargeStatus(String eachFirstRechargeStatus) {
		changed("each_first_recharge_status", eachFirstRechargeStatus,
				this.eachFirstRechargeStatus);
		this.eachFirstRechargeStatus = eachFirstRechargeStatus;
	}

	public void setSustainKill(String sustainKill) {
		changed("sustain_kill", sustainKill, this.sustainKill);
		this.sustainKill = sustainKill;
	}

	@Column(name = "fashion")
	public String getFashion() {
		return fashion;
	}

	public void setFashion(String fashion) {
		changed("fashion", fashion, this.fashion);
		this.fashion = fashion;
	}

	@Column(name = "newbie_step_group")
	public Integer getNewbieStepGroup() {
		return newbieStepGroup;
	}

	public void setNewbieStepGroup(Integer newbieStepGroup) {
		changed("newbie_step_group", newbieStepGroup, this.newbieStepGroup);
		this.newbieStepGroup = newbieStepGroup;
	}

	@Column(name = "siege_last_award_time")
	public Long getSiegeLastAwardTime() {
		return siegeLastAwardTime;
	}
	
	
	public void setSiegeLastAwardTime(Long siegeLastAwardTime) {
		changed("siege_last_award_time", siegeLastAwardTime,
				this.siegeLastAwardTime);
		this.siegeLastAwardTime = siegeLastAwardTime;
	}
	
	
	
	@Column(name = "last_login_ip")
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		changed("last_login_ip", lastLoginIp,this.lastLoginIp);
		this.lastLoginIp = lastLoginIp;
	}

	@Column(name = "domain_last_award_time")
	public Long getDomainLastAwardTime() {
		return domainLastAwardTime;
	}

	public void setDomainLastAwardTime(Long domainLastAwardTime) {
		changed("domain_last_award_time", domainLastAwardTime,
				this.domainLastAwardTime);
		this.domainLastAwardTime = domainLastAwardTime;
	}

	@Column(name = "yun_dart_task_info")
	public String getYunDartTaskInfo() {
		return yunDartTaskInfo;
	}

	public void setYunDartTaskInfo(String yunDartTaskInfo) {
		changed("yun_dart_task_info", yunDartTaskInfo, this.yunDartTaskInfo);
		this.yunDartTaskInfo = yunDartTaskInfo;
	}

	@Column(name = "fight_activity_max_score_records")
	public String getFightActivityMaxScoreRecords() {
		return fightActivityMaxScoreRecords;
	}

	public void setFightActivityMaxScoreRecords(
			String fightActivityMaxScoreRecords) {
		changed("fight_activity_max_score_records",
				fightActivityMaxScoreRecords, this.fightActivityMaxScoreRecords);
		this.fightActivityMaxScoreRecords = fightActivityMaxScoreRecords;
	}

	@Column(name = "prestige")
	public Integer getPrestige() {
		return prestige;
	}

	public void setPrestige(Integer prestige) {
		changed("prestige", prestige, this.prestige);
		this.prestige = prestige;
	}

	@Column(name = "prestige_total")
	public Integer getPrestigeTotal() {
		return prestigeTotal;
	}

	public void setPrestigeTotal(Integer prestigeTotal) {
		changed("prestige_total", prestigeTotal, this.prestigeTotal);
		this.prestigeTotal = prestigeTotal;
	}

	@Column(name = "current_military_rank_id")
	public Integer getCurrentMilitaryRankId() {
		return currentMilitaryRankId;
	}

	public void setCurrentMilitaryRankId(Integer currentMilitaryRankId) {
		changed("current_military_rank_id", currentMilitaryRankId, this.currentMilitaryRankId);
		this.currentMilitaryRankId = currentMilitaryRankId;
	}
	
	@Column(name = "military_rank_record")
	public String getMilitaryRankRecord() {
		return militaryRankRecord;
	}

	public void setMilitaryRankRecord(String militaryRankRecord) {
		changed("military_rank_record", militaryRankRecord, this.militaryRankRecord);
		this.militaryRankRecord = militaryRankRecord;
	}

	
	@Column(name = "guild_priest_state")
	public Integer getGuildPriestState() {
		return guildPriestState;
	}

	public void setGuildPriestState(Integer guildPriestState) {
		changed("guild_priest_state", guildPriestState, this.guildPriestState);
		this.guildPriestState = guildPriestState;
	}

	@Column(name = "buy_play_times")
	public String getBuyPlayTimes() {
		return buyPlayTimes;
	}

	public void setBuyPlayTimes(String buyPlayTimes) {
		changed("buy_play_times", buyPlayTimes, this.buyPlayTimes);
		this.buyPlayTimes = buyPlayTimes;
	}

	@Column(name = "guild_boss_award")
	public String getGuildBossAward() {
		return guildBossAward;
	}

	public void setGuildBossAward(String guildBossAward) {
		changed("guild_boss_award", guildBossAward, this.guildBossAward);
		this.guildBossAward = guildBossAward;
	}
	
	@Column(name = "guild_boss_award_flush_time")
	public Long getGuildBossAwardFlushTime() {
		return guildBossAwardFlushTime;
	}

	public void setGuildBossAwardFlushTime(Long guildBossAwardFlushTime) {
		changed("guild_boss_award_flush_time", guildBossAwardFlushTime, this.guildBossAwardFlushTime);
		this.guildBossAwardFlushTime = guildBossAwardFlushTime;
	}

	
	

	@Column(name = "product_today_buyed")
	public String getProductTodayBuyed() {
		return productTodayBuyed;
	}

	public void setProductTodayBuyed(String productTodayBuyed) {
		changed("product_today_buyed", productTodayBuyed, this.productTodayBuyed);
		this.productTodayBuyed = productTodayBuyed;
	}

	@Column(name = "role_treasures")
	public String getRoleTreasures() {
		return roleTreasures;
	}

	public void setRoleTreasures(String roleTreasures) {
		changed("role_treasures", roleTreasures, this.roleTreasures);
		this.roleTreasures = roleTreasures;
	}

	@Column(name = "room_loading")
	public Integer getRoomLoading() {
		return roomLoading;
	}

	public void setRoomLoading(Integer roomLoading) {
		changed("room_loading", roomLoading, this.roomLoading);
		this.roomLoading = roomLoading;
	}
	
	@Column(name = "channel_key")
	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		changed("channel_key", channelKey, this.channelKey);
		this.channelKey = channelKey;
	}

	@Column(name = "military_forces")
	public Integer getMilitaryForces() {
		return militaryForces;
	}

	public void setMilitaryForces(Integer militaryForces) {
		changed("military_forces", militaryForces, this.militaryForces);
		this.militaryForces = militaryForces;
	}

	
	@Column(name = "user_created_time")
	public Long getUserCreatedTime() {
		return userCreatedTime;
	}

	public void setUserCreatedTime(Long userCreatedTime) {
		changed("user_created_time", userCreatedTime, this.userCreatedTime);
		this.userCreatedTime = userCreatedTime;
	}

	@Column(name = "total_online")
	public Integer getTotalOnline() {
		return totalOnline;
	}

	public void setTotalOnline(Integer totalOnline) {
		changed("total_online", totalOnline, this.totalOnline);
		this.totalOnline = totalOnline;
	}
	
	
	@Column(name = "was_hidden_fashion")
	public String getWasHiddenFashion() {
		return wasHiddenFashion;
	}

	public void setWasHiddenFashion(String wasHiddenFashion) {
		changed("was_hidden_fashion", wasHiddenFashion, this.wasHiddenFashion);
		this.wasHiddenFashion = wasHiddenFashion;
	}

	
	@Column(name = "first_recharge_state")
	public Integer getFirstRechargeState() {
		return firstRechargeState;
	}

	public void setFirstRechargeState(Integer firstRechargeState) {
		changed("first_recharge_state", firstRechargeState, this.firstRechargeState);
		this.firstRechargeState = firstRechargeState;
	}



	@Column(name = "user_iuid")
	public String getUserIuid() {
		return userIuid;
	}

	public void setUserIuid(String userIuid) {
		changed("user_iuid", userIuid, this.userIuid);
		this.userIuid = userIuid;
	}

	@Column(name = "arena_rank")
	public Integer getArenaRank() {
		return arenaRank;
	}

	public void setArenaRank(Integer arenaRank) {
		changed("arena_rank", arenaRank, this.arenaRank);
		this.arenaRank = arenaRank;
	}

	@Column(name = "daily_lively_task_finish_score")
	public Integer getDailyLivelyTaskFinishScore() {
		return dailyLivelyTaskFinishScore;
	}

	public void setDailyLivelyTaskFinishScore(Integer dailyLivelyTaskFinishScore) {
		changed("daily_lively_task_finish_score", dailyLivelyTaskFinishScore, this.dailyLivelyTaskFinishScore);
		this.dailyLivelyTaskFinishScore = dailyLivelyTaskFinishScore;
	}

	@Column(name = "guild_name")
	public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		changed("guild_name", guildName, this.guildName);
		this.guildName = guildName;
	}

	@Column(name = "main_task_id")
	public Integer getMainTaskId() {
		return mainTaskId;
	}

	public void setMainTaskId(Integer mainTaskId) {
		changed("main_task_id", mainTaskId, this.mainTaskId);
		this.mainTaskId = mainTaskId;
	}

	@Column(name = "main_task_name")
	public String getMainTaskName() {
		return mainTaskName;
	}

	public void setMainTaskName(String mainTaskName) {
		changed("main_task_name", mainTaskName, this.mainTaskName);
		this.mainTaskName = mainTaskName;
	}

	@Column(name = "first_charge_diamond")
	public Integer getFirstChargeDiamond() {
		return firstChargeDiamond;
	}

	public void setFirstChargeDiamond(Integer firstChargeDiamond) {
		changed("first_charge_diamond", firstChargeDiamond, this.firstChargeDiamond);
		this.firstChargeDiamond = firstChargeDiamond;
	}

	@Column(name = "first_charge_role_lv")
	public Integer getFirstChargeRoleLv() {
		return firstChargeRoleLv;
	}

	public void setFirstChargeRoleLv(Integer firstChargeRoleLv) {
		changed("first_charge_role_lv", firstChargeRoleLv, this.firstChargeRoleLv);
		this.firstChargeRoleLv = firstChargeRoleLv;
	}


	
	@Column(name = "first_consume_desc")
	public String getFirstConsumeDesc() {
		return firstConsumeDesc;
	}

	public void setFirstConsumeDesc(String firstConsumeDesc) {
		changed("first_consume_desc", firstConsumeDesc, this.firstConsumeDesc);
		this.firstConsumeDesc = firstConsumeDesc;
	}
	

	@Column(name = "first_consume_role_lv")
	public Integer getFirstConsumeRoleLv() {
		return firstConsumeRoleLv;
	}

	public void setFirstConsumeRoleLv(Integer firstConsumeRoleLv) {
		changed("first_consume_role_lv", firstConsumeRoleLv, this.firstConsumeRoleLv);
		this.firstConsumeRoleLv = firstConsumeRoleLv;
	}
	
	
	@Column(name = "total_diamond_charged")
	public Integer getTotalDiamondCharged() {
		return totalDiamondCharged;
	}

	public void setTotalDiamondCharged(Integer totalDiamondCharged) {
		changed("total_diamond_charged", totalDiamondCharged, this.totalDiamondCharged);
		this.totalDiamondCharged = totalDiamondCharged;
	}


	@Column(name = "pet_prower")
	public Integer getPetPrower() {
		return petPrower;
	}

	public void setPetPrower(Integer petPrower) {
		changed("pet_prower", petPrower, this.petPrower);
		this.petPrower = petPrower;
	}

	@Column(name = "achievement_sum")
	public Integer getAchievementSum() {
		return achievementSum;
	}

	public void setAchievementSum(Integer achievementSum) {
		changed("achievement_sum", achievementSum, this.achievementSum);
		this.achievementSum = achievementSum;
	}

	
	@Column(name = "resource_scene_time")
	public Integer getResourceSceneTime() {
		return resourceSceneTime;
	}

	public void setResourceSceneTime(Integer resourceSceneTime) {
		changed("resource_scene_time", resourceSceneTime, this.resourceSceneTime);
		this.resourceSceneTime = resourceSceneTime;
	}
	

	@Column(name = "start_resource_scene_time")
	public Long getStartResourceSceneTime() {
		return startResourceSceneTime;
	}

	public void setStartResourceSceneTime(Long startResourceSceneTime) {
		changed("start_resource_scene_time", startResourceSceneTime, this.startResourceSceneTime);
		this.startResourceSceneTime = startResourceSceneTime;
	}

	@Column(name = "grow_funds_award1")
	public String getGrowFundsAward1() {
		return growFundsAward1;
	}

	public void setGrowFundsAward1(String growFundsAward1) {
		changed("grow_funds_award1", growFundsAward1, this.growFundsAward1);
		this.growFundsAward1 = growFundsAward1;
	}

	@Column(name = "grow_funds_award2")
	public String getGrowFundsAward2() {
		return growFundsAward2;
	}

	public void setGrowFundsAward2(String growFundsAward2) {
		changed("grow_funds_award2", growFundsAward2, this.growFundsAward2);
		this.growFundsAward2 = growFundsAward2;
	}

	@Column(name = "was_grow_funds1")
	public Integer getWasGrowFunds1() {
		return wasGrowFunds1;
	}

	public void setWasGrowFunds1(Integer wasGrowFunds1) {
		changed("was_grow_funds1", wasGrowFunds1, this.wasGrowFunds1);
		this.wasGrowFunds1 = wasGrowFunds1;
	}

	@Column(name = "was_grow_funds2")
	public Integer getWasGrowFunds2() {
		return wasGrowFunds2;
	}

	public void setWasGrowFunds2(Integer wasGrowFunds2) {
		changed("was_grow_funds2", wasGrowFunds2, this.wasGrowFunds2);
		this.wasGrowFunds2 = wasGrowFunds2;
	}
	
	@Column(name = "grow_fund_str")
	public String getGrowFundStr() {
		return growFundStr;
	}

	public void setGrowFundStr(String growFundStr) {
		changed("grow_fund_str", growFundStr, this.growFundStr);
		this.growFundStr = growFundStr;
	}
	
	@Column(name = "invitation_code")
	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		changed("invitation_code", invitationCode, this.invitationCode);
		this.invitationCode = invitationCode;
	}

	@Column(name = "input_invitation_code")
	public String getInputInvitationCode() {
		return inputInvitationCode;
	}

	public void setInputInvitationCode(String inputInvitationCode) {
		changed("input_invitation_code", inputInvitationCode, this.inputInvitationCode);
		this.inputInvitationCode = inputInvitationCode;
	}

	@Column(name = "invitation_task")
	public String getInvitationTask() {
		return invitationTask;
	}

	public void setInvitationTask(String invitationTask) {
		changed("invitation_task", invitationTask, this.invitationTask);
		this.invitationTask = invitationTask;
	}

	@Column(name = "invitation_friend")
	public String getInvitationFriend() {
		return invitationFriend;
	}

	public void setInvitationFriend(String invitationFriend) {
		changed("invitation_friend", invitationFriend, this.invitationFriend);
		this.invitationFriend = invitationFriend;
	}

	@Column(name = "activity_login_state")
	public Integer getActivityLoginState() {
		return activityLoginState;
	}

	public void setActivityLoginState(Integer activityLoginState) {
		changed("activity_login_state", activityLoginState, this.activityLoginState);
		this.activityLoginState = activityLoginState;
	}

	@Column(name = "was_facebook_bind")
	public Integer getWasFacebookBind() {
		return wasFacebookBind;
	}

	public void setWasFacebookBind(Integer wasFacebookBind) {
		changed("was_facebook_bind", wasFacebookBind, this.wasFacebookBind);
		this.wasFacebookBind = wasFacebookBind;
	}

	@Column(name = "was_new_enemy")
	public Integer getWasNewEnemy() {
		return wasNewEnemy;
	}

	public void setWasNewEnemy(Integer wasNewEnemy) {
		changed("was_new_enemy", wasNewEnemy, this.wasNewEnemy);
		this.wasNewEnemy = wasNewEnemy;
	}

	

	@Column(name = "pack_unlock_times")
	public Integer getPackUnlockTimes() {
		return packUnlockTimes;
	}

	public void setPackUnlockTimes(Integer packUnlockTimes) {
		changed("pack_unlock_times", packUnlockTimes, this.packUnlockTimes);
		this.packUnlockTimes = packUnlockTimes;
	}


	

	@Column(name = "pick_crisital_today_times")
	public Integer getPickCrisitalTodayTimes() {
		return pickCrisitalTodayTimes;
	}

	public void setPickCrisitalTodayTimes(Integer pickCrisitalTodayTimes) {
		changed("pick_crisital_today_times", pickCrisitalTodayTimes, this.pickCrisitalTodayTimes);
		this.pickCrisitalTodayTimes = pickCrisitalTodayTimes;
	}

	@Column(name="pet_skill_max_size")
	public Integer getPetMaxSkillSize() {
		return petMaxSkillSize;
	}

	public void setPetMaxSkillSize(Integer petMaxSkillSize) {
		changed("pet_skill_max_size", petMaxSkillSize, this.petMaxSkillSize);
		this.petMaxSkillSize = petMaxSkillSize;
	}
	
	@Column(name="list_consum_cost")
	public String getListConsumCost() {
		return listConsumCost;
	}

	public void setListConsumCost(String listConsumCost) {
		changed("list_consum_cost", listConsumCost, this.listConsumCost);
		this.listConsumCost = listConsumCost;
	}

	@Column(name="soul_atb")
	public String getSoulAtb() {
		return soulAtb;
	}

	public void setSoulAtb(String soulAtb) {
		changed("soul_atb", soulAtb, this.soulAtb);
		this.soulAtb = soulAtb;
	}


	@Column(name="soul_type")
	public Integer getSoulType() {
		return soulType;
	}

	public void setSoulType(Integer soulType) {
		changed("soul_type", soulType, this.soulType);
		this.soulType = soulType;
	}

	@Column(name="soul")
	public Integer getSoul() {
		return soul;
	}

	public void setSoul(Integer soul) {
		changed("soul", soul, this.soul);
		this.soul = soul;
	}

	@Column(name="soul_exchange_times")
	public Integer getSoulExchangeTimes() {
		return soulExchangeTimes;
	}

	public void setSoulExchangeTimes(Integer soulExchangeTimes) {
		changed("soul_exchange_times", soulExchangeTimes, this.soulExchangeTimes);
		this.soulExchangeTimes = soulExchangeTimes;
	}



	/**
	 * 邀请任务列表
	 */
	public List<IdNumberVo2> listInvitationTask = new CopyOnWriteArrayList<IdNumberVo2>();

	/**
	 * 输入过自己邀请码的人
	 */
	public List<InvitationRoleVo> listInvitationFriend=new CopyOnWriteArrayList<InvitationRoleVo>();
	
	/**
	 * 钻石摇一摇 1:id 2：是否可以领取 3：是否已经领取
	 */
	public List<IdNumberVo2> listDiamondBasins = new CopyOnWriteArrayList<IdNumberVo2>();
	
	/**
	 * 成长基金奖励列表1
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listgrowFundsAward1 = new CopyOnWriteArrayList<IdNumberVo2>();

	/**
	 * 成长基金奖励列表2
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listgrowFundsAward2 = new CopyOnWriteArrayList<IdNumberVo2>();
	
	/**
	 * 运营活动记录
	 */
	public CopyOnWriteArrayList<RoleLiveActivityVo> listRoleLiveActivitys = new CopyOnWriteArrayList<RoleLiveActivityVo>();
	/**
	 * 宠物列表
	 */
	public CopyOnWriteArrayList<RpetPo> listRpets = new CopyOnWriteArrayList<RpetPo>();

	public CopyOnWriteArrayList<GuildInvitionVo> listGuildInvitions = new CopyOnWriteArrayList<GuildInvitionVo>();

	/**
	 * 好友列表
	 */
	public CopyOnWriteArrayList<RoleInforPo> listFriends = new CopyOnWriteArrayList<RoleInforPo>();

	/**
	 * 黑名单列表
	 */
	public CopyOnWriteArrayList<RoleInforPo> listBlocks = new CopyOnWriteArrayList<RoleInforPo>();

	/**
	 * 仇人列表
	 */
	public CopyOnWriteArrayList<RoleInforPo> listEnemys = new CopyOnWriteArrayList<RoleInforPo>();

	/**
	 * 1:bufferId
	 * 2:结束时间
	 * 3:生命周期
	 * 4:持续时间
	 */
	public List<List<String>> listBufferStatus = new ArrayList<List<String>>();

	/**
	 * 附魂
	 */
	public List<SlotSoulVo> listSlotSouls = new ArrayList<SlotSoulVo>();

	/**
	 * 用户活动信息
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<ActivityInfoVo> listActivityInfo = new CopyOnWriteArrayList<ActivityInfoVo>();

	/**
	 * 充值相关信息
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<RechargeInfoVo> listRechargeInfo = new CopyOnWriteArrayList<RechargeInfoVo>();

	/**
	 * 运镖任务信息
	 */
	@JSONField(serialize = false)
	private String yunDartTaskInfo;
	/**
	 * 运镖任务信息
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<YunDartTaskInfoVo> listYunDartTaskInfoVo = new CopyOnWriteArrayList<YunDartTaskInfoVo>();

	/**
	 * 进阶套装加成
	 */
	@JSONField(serialize = false)
	private String advanceSuitPlus;

	/**
	 * 进阶套装加成信息
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<AdvanceSuitPlusVo> listAdvanceSuitPlus = new CopyOnWriteArrayList<AdvanceSuitPlusVo>();

	/**
	 * 记录装备强化的等级 key=装备部位id； value=强化等级
	 */
	@JSONField(serialize = false)
	public Map<Integer, Integer> powerSuitPlusMap = new HashMap<Integer, Integer>();
	/**
	 * 记录部位升星 key=部位id； value=强化星数
	 */
	@JSONField(serialize = false)
	public Map<Integer, Integer> starSuitPlusMap = new HashMap<Integer, Integer>();
	
	/**
	 * 记录部位洗练 key=部位id_洗练槽位； value = 洗练星数
	 */
	@JSONField(serialize = false)
	public Map<String, Integer> washSuitPlusMap = new HashMap<String, Integer>();
	
	
	/**
	 * 宠物天赋
	 */
	public List<Integer> listPetTalent = new ArrayList<Integer>();

	/**
	 * 宠物星座(key:星座ID,value:星座属性)
	 */
	@JSONField(serialize = false)
	public Map<Integer, List<PetConstellVo>> petConstellMap = new ConcurrentHashMap<Integer, List<PetConstellVo>>();
	public List<List<PetConstellVo>> petConstellsList = new ArrayList<List<PetConstellVo>>();

	/**
	 * 宠物星座强化次数(key:星座ID,value:当前强化次数)
	 */
	@JSONField(serialize = false)
	public Map<Integer, Integer> petConstellStrengthenMap = new ConcurrentHashMap<Integer, Integer>();
	public List<IdNumberVo> petConstellStrengthensList = new ArrayList<IdNumberVo>();

	// /**
	// * 宠物统一加成buff
	// */
	// @JSONField(serialize=false)
	// private List<BaseBuffVo> petBuffs = new ArrayList<BaseBuffVo>();

	/** 角色背包 */
	public ConcurrentHashMap<String, RolePackItemVo> mainPackItemVosMap = new ConcurrentHashMap<String, RolePackItemVo>();

	/** 仓库 */
	public ConcurrentHashMap<String, RolePackItemVo> warehousePackItemVosMap = new ConcurrentHashMap<String, RolePackItemVo>();

	
	public List<Integer> listSkillFormation = new ArrayList<Integer>();

	/**
	 * 今日次数记录
	 * 1：copySceneConfPoId
	 * 2：是否组队
	 * 3：完成次数
	 * 4：领奖次数
	 */
	public List<IdNumberVo3> listCopySceneTodayVisitTimes = new ArrayList<IdNumberVo3>();

	public CopyOnWriteArrayList<IdNumberVo> listGuildTodayExchangeItems = new CopyOnWriteArrayList<IdNumberVo>();

	public List<IdNumberVo> listFightActivityMaxScoreRecords = new ArrayList<IdNumberVo>();
	/**
	 * 玩家生效中的buff效果
	 */
	@JSONField(serialize = false)
	public CopyOnWriteArrayList<BufferStatusVo> bufferStatusVos = new CopyOnWriteArrayList<BufferStatusVo>();
	
	public List<IdNumberVo> listConsumCostVos = new ArrayList<IdNumberVo>();
	@JSONField(serialize = false)
	public Map<Integer,List<IdNumberVo2>> soulAtbMap = new HashMap<Integer, List<IdNumberVo2>>();

	/**
	 * 今日累计采集水晶数量
	 */
	private Integer pickCrisitalTodayTimes= 0;
	
	private void loadSoulAtb(){
		if(openSystemArrayList.contains(TaskType.OPEN_SYSTEM_SOUL)){
			if(getSoulAtb()==null){
				setSoulType(IntUtil.getRandomInt(1, 5));
			}
			if(getSoul()==null){
				setSoul(0);
			}
			SoulService soulService = SoulService.instance();
			soulAtbMap = soulService.buildSoulAtb(getSoulAtb());
		}
	}
	
	private void saveSoulAtb(){
		if(openSystemArrayList.contains(TaskType.OPEN_SYSTEM_SOUL)){
			SoulService soulService = SoulService.instance();
			setSoulAtb(soulService.createSoulAtb(soulAtbMap));
		}
	}
	
	private void updateRolePackVos() {
		String[] objs = new String[mainPackItemVosMap.size()];
		int i = 0;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			Object[] props = rolePackItemVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(props, "|");
			objs[i] = targetStr;
			i++;
		}
		setMainPackItemVos(StringUtil.implode(objs, ","));
	}
	
	private void updateRoleWarehousePackItemVos(){
		String[] objs = new String[warehousePackItemVosMap.size()];
		int i = 0;
		for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
			Object[] props = rolePackItemVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(props, "|");
			objs[i] = targetStr;
			i++;
		}
		setWarehousePackItemVos(StringUtil.implode(objs, ","));
	}

	private void loadRolePackVos() {
		if (mainPackItemVos == null) {
			return;
		}
		String[] items = StringUtil.split(mainPackItemVos, ",");
		if(items!=null && items.length >0){
			mainPackItemVosMap= new ConcurrentHashMap<String, RolePackItemVo>();
		}
		for (String itemStr : items) {
			RolePackItemVo rolePackItemVo = new RolePackItemVo();
			if (itemStr != null && itemStr.length() > 0) {
				rolePackItemVo.loadProperty(itemStr, "|");
				this.mainPackItemVosMap.put(rolePackItemVo.getIndex()
						.toString(), rolePackItemVo);
			}
		}
	}
	
	
	private void loadRoleWarehousePackItemVos(){
		if (warehousePackItemVos == null) {
			return;
		}
		String[] items = StringUtil.split(warehousePackItemVos, ",");
		if(items!=null && items.length>1){
			warehousePackItemVosMap=new ConcurrentHashMap<String, RolePackItemVo>();
		}
		for (String itemStr : items) {
			RolePackItemVo rolePackItemVo = new RolePackItemVo();
			if (itemStr != null && itemStr.length() > 0) {
				rolePackItemVo.loadProperty(itemStr, "|");
				this.warehousePackItemVosMap.put(rolePackItemVo.getIndex().toString(), rolePackItemVo);
			}
		}
	}


	/**
	 * 获取副本每日可完成次数
	 * 
	 * @param copySceneId
	 * @return
	 */
	private int getCopySceneConfNum(int copySceneId) {
		int num = 0;
		List<CopySceneConfPo> cscPoList = GlobalCache.copySceneConfMap
				.get(copySceneId);
		if (null != cscPoList) {
			for (CopySceneConfPo cscPo : cscPoList)
				num += cscPo.getAvaTimes();
		}
		return num;
	}
	
	private void initPetMaxSkillSize(){
		int maxSize = 0;
		for(RpetPo rpetPo:listRpets){
			maxSize = Math.max(maxSize, rpetPo.skillIds.size());
		}
		setPetMaxSkillSize(maxSize);
	}

	
	public void loadData(BasePo basePo) {
//		System.out.println(this.hashCode()+" name="+name +"; loadData() loaded="+loaded+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		if (loaded == false) {
			unChanged();
			loadSlotSouls();
			loadActivityInfoVo();
			loadRechargeInfoVo();
			loadYunDartTaskInfoVo();
			loadAwardRetrieveVo();
			loadAdvanceSuitPlus();
			loadRolePackVos();
			loadRoleWarehousePackItemVos();
			loadEquipFromIds();
			loadGuildInvitions();
			loadPet();
			loadSustainKillVo();
			loadHiddenFashion();
			loadFashion();
			loadRoleLiveActivityVo();
			loadgrowFundVo();
			loadInvitationRoleVo();
			loadSpecialTitle();
			List<Integer> listFriends = DBFieldUtil
					.getIntegerListByCommer(friends);
			List<Integer> listBlocks = DBFieldUtil
					.getIntegerListByCommer(blocks);
			List<Integer> listEnemys = DBFieldUtil
					.getIntegerListByCommer(enemys);

			for (Integer inforId : listFriends) {
				RoleInforPo roleInforPo = RoleInforPo.findEntity(inforId);
				if (roleInforPo != null) {
					this.listFriends.add(roleInforPo);
				}
			}
			for (Integer inforId : listBlocks) {
				RoleInforPo roleInforPo = RoleInforPo.findEntity(inforId);
				if (roleInforPo != null) {
					this.listBlocks.add(roleInforPo);
				}
			}
			for (Integer inforId : listEnemys) {
				RoleInforPo roleInforPo = RoleInforPo.findEntity(inforId);
				if (roleInforPo != null) {
					this.listEnemys.add(roleInforPo);
				}
			}

			listFriends.clear();
			listBlocks.clear();
			listEnemys.clear();

			listFightActivityMaxScoreRecords = IdNumberVo
					.createList(fightActivityMaxScoreRecords);
			listSkillVos = IdNumberVo.createList(skillVos);
			
			listRoleTreasures = IdNumberVo.createList(roleTreasures);
			listProductTodayBuyed = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(productTodayBuyed));
			
			// listSkillVos=IdNumberVo.createList(skillVos);
			listSkillFormation = DBFieldUtil
					.getIntegerListByCommer(skillFormation);
			listBufferStatus = ExpressUtil.buildBattleExpressListStr(bufferStatus);
			listRoleTasks = new CopyOnWriteArrayList<IdNumberVo2>(
					IdNumberVo2.createList(roleTasks));
			listRoleAchievesTasks = new CopyOnWriteArrayList<IdNumberVo2>(
					IdNumberVo2.createList(roleAchieves));
			listCopySceneTodayVisitTimes = IdNumberVo3
					.createList(copySceneTodayVisitTimes);
			listEachFirstRechargeStatus = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(eachFirstRechargeStatus));
			listGuildBossAward = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(guildBossAward));
			listMilitaryRankRecord = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(militaryRankRecord));
			listBuyPlayTimes = new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(buyPlayTimes));
			listGuildTodayExchangeItems = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(guildTodayExchangeItems));
			listgrowFundsAward1=new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(growFundsAward1));
			listgrowFundsAward2=new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(growFundsAward2));
			listInvitationTask=new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(invitationTask));
			skills.clear();
			skills.add(110001);
			skills.add(110002);
			skills.add(110003);
			skills.add(110004);
			skills.add(110005);
			listRpets=new CopyOnWriteArrayList<RpetPo>();
			List<Integer> vals = DBFieldUtil.getIntegerListByCommer(rpets);
			for (Integer rPetId : vals) {
				RpetPo rpetPo = RpetPo.findEntity(rPetId);
				if(rpetPo != null){
					listRpets.add(RpetPo.findEntity(rPetId));					
				}
			}
			listEveryDayBuyProductCount = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(everyDayBuyProductCount));
			listOnlyBuyProductCount = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(onlyBuyProductCount));
			openSystemArrayList = new CopyOnWriteArrayList<Integer>(
					DBFieldUtil.getIntegerListBySplitter(openSystemList, ","));
			listRoleOptions = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(roleOptions));
			listOnlineTime = new CopyOnWriteArrayList<IdLongVo2>(
					IdLongVo2.createList(onlineTime));
			listCumulativeLoginAwardRecord = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(cumulativeLoginAwardRecord));
			listToTakeCumulativeLoginAwardRecord = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(toTakeCumulativeLoginAwardRecord));
			listLevelAwardRecord = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(levelAwardRecord));
			listOnlineTimeAwrodRecord = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(onlineTimeAwrodRecord));
			listSignInAwardRecord = new CopyOnWriteArrayList<IdNumberVo2>(
					IdNumberVo2.createList(signInAwardRecord));
			listDailyLivelyAwardRecord = new CopyOnWriteArrayList<IdNumberVo>(
					IdNumberVo.createList(dailyLivelyAwardRecord));
			listDotaFirstAward = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(dotaFirstAward));
			initPetMaxSkillSize();
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
			String language = String.valueOf(gp.valueObj);
			if("ko".equals(language)){
				if(worshipDiamondFirst==null){
					setWorshipDiamondFirst(0);
				}
			}else{
				setWorshipDiamondFirst(1);
			}
			loadConsumCost();
			loadSoulAtb();
			loaded = true;
		}
	}
	
	private void loadConsumCost(){
		listConsumCostVos = IdNumberVo.createList(getListConsumCost());
		if(listConsumCostVos.isEmpty()){
			GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<ConsumPo> consumPoList = gameDataTemplate.getDataList(ConsumPo.class);
			for(ConsumPo consumPo:consumPoList){
				IdNumberVo idNumberVo = new IdNumberVo(consumPo.getId(), 0);
				listConsumCostVos.add(idNumberVo);
			}
		}
	}
	
	private void saveConsumCost(){
		if(!listConsumCostVos.isEmpty()){
			setListConsumCost(IdNumberVo.createStr(listConsumCostVos));
		}
	}
	
	private void resetConsumCost(){
		listConsumCostVos = new ArrayList<IdNumberVo>();
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<ConsumPo> consumPoList = gameDataTemplate.getDataList(ConsumPo.class);
		for(ConsumPo consumPo:consumPoList){
			IdNumberVo idNumberVo = new IdNumberVo(consumPo.getId(), 0);
			listConsumCostVos.add(idNumberVo);
		}	
		sendUpdateRoleConsumCostVos();
	}
	
	public void adjustConsumCostById(Integer consumId, int num){
		for(IdNumberVo idNumberVo:listConsumCostVos){
			if(idNumberVo.getId().intValue()==consumId.intValue()){
				idNumberVo.addNum(num);
				break;
			}
		}
		sendUpdateRoleConsumCostVos();
	}
	
	private void loadSlotSouls() {
		if (StringUtil.isEmpty(this.slotSouls)) {
			return;
		}
		String[] items = StringUtil.split(this.slotSouls, ",");
		if(items != null && items.length >0){
			listSlotSouls = new CopyOnWriteArrayList<SlotSoulVo>();			
		}
		for (String itemStr : items) {
			SlotSoulVo slotSoulVo = new SlotSoulVo();
			slotSoulVo.loadProperty(itemStr, "|");
			listSlotSouls.add(slotSoulVo);
		}
	}

	private void loadActivityInfoVo() {
		if (StringUtil.isEmpty(this.activityInfo)) {
			return;
		}
		String[] items = StringUtil.split(this.activityInfo, ",");
		if(items != null && items.length>0){
			listActivityInfo = new CopyOnWriteArrayList<ActivityInfoVo>();			
		}
		for (String itemStr : items) {
			ActivityInfoVo activityInfoVo = new ActivityInfoVo();
			activityInfoVo.loadProperty(itemStr, "|");
			listActivityInfo.add(activityInfoVo);
		}
	}
	

	
	
	/** 加载运营活动记录 */
	private void loadRoleLiveActivityVo(){
		if(this.roleLiveActivitys == null){
			return;
		}
		String[] items = StringUtil.split(this.roleLiveActivitys, "@");
		if(items!=null && items.length >0){
			listRoleLiveActivitys=new CopyOnWriteArrayList<RoleLiveActivityVo>();
		}
		for (String itemStr : items) {
			RoleLiveActivityVo roleLiveActivityVo = new RoleLiveActivityVo();
			roleLiveActivityVo.loadProperty(itemStr, "#");
			listRoleLiveActivitys.add(roleLiveActivityVo);
		}
	}
	
	
	private void loadgrowFundVo(){
		if(StringUtil.isEmpty(this.growFundStr)){
			return;
		}
		GrowFundVo gfv = new GrowFundVo();
		gfv.loadProperty(growFundStr, "|");
		this.growFundVo = gfv;
	}
	
	private void loadInvitationRoleVo(){
		if(StringUtil.isEmpty(this.invitationFriend)){
			return;
		}
		String[] items = StringUtil.split(this.invitationFriend, ",");
		if(items != null && items.length>0){
			listInvitationFriend = new CopyOnWriteArrayList<InvitationRoleVo>();			
		}
		for (String itemStr : items) {
			InvitationRoleVo invitationRoleVo = new InvitationRoleVo();
			invitationRoleVo.loadProperty(itemStr, "|");
			listInvitationFriend.add(invitationRoleVo);
		}
	}
	

	private void loadAwardRetrieveVo(){
		if (this.awardRetrieve == null) {
			return;
		}
		String[] items = StringUtil.split(this.awardRetrieve, ",");
		if(items!=null && items.length>0){
			listAwardRetrieve=new CopyOnWriteArrayList<AwardRetrieveVo>();
		}
		for (String itemStr : items) {
			AwardRetrieveVo awardRetrieveVo = new AwardRetrieveVo();
			awardRetrieveVo.loadProperty(itemStr, "|");
			listAwardRetrieve.add(awardRetrieveVo);
		}
	}
	
	private void loadRechargeInfoVo() {
		if (this.rechargeInfo == null) {
			checkInitializeRechargeInfo();
			return;
		}
		String[] items = StringUtil.split(this.rechargeInfo, ",");
		if(items != null && items.length>0){
			listRechargeInfo = new CopyOnWriteArrayList<RechargeInfoVo>();
		}
		for (String itemStr : items) {
			RechargeInfoVo rechargeInfoVo = new RechargeInfoVo();
			rechargeInfoVo.loadProperty(itemStr, "|");
			listRechargeInfo.add(rechargeInfoVo);
		}
	}

	private void loadYunDartTaskInfoVo() {
		if (this.yunDartTaskInfo == null) {
			checkInitializeYunDartTaskInfo();
			return;
		}
		String[] items = StringUtil.split(this.yunDartTaskInfo, ",");
		if(items!= null && items.length > 0){
			listYunDartTaskInfoVo = new CopyOnWriteArrayList<YunDartTaskInfoVo>();
		}
		for (String itemStr : items) {
			YunDartTaskInfoVo yunDartTaskInfoVo = new YunDartTaskInfoVo();
			yunDartTaskInfoVo.loadProperty(itemStr, "|");
			listYunDartTaskInfoVo.add(yunDartTaskInfoVo);
		}
	}

	private void loadAdvanceSuitPlus() {
		if (this.advanceSuitPlus == null) {
			return;
		}
		String[] items = StringUtil.split(this.advanceSuitPlus, ",");
		if(items!=null && items.length>0){
			listAdvanceSuitPlus=new CopyOnWriteArrayList<AdvanceSuitPlusVo>();
		}
		for (String itemStr : items) {
			AdvanceSuitPlusVo advanceSuitPlusVo = new AdvanceSuitPlusVo();
			advanceSuitPlusVo.loadProperty(itemStr, "|");
			listAdvanceSuitPlus.add(advanceSuitPlusVo);
		}
	}

	private void updateGuildInvitions() {
		if (this.listGuildInvitions == null) {
			setGuildInvitions(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listGuildInvitions.size(); i++) {
			GuildInvitionVo guildInvitionVo = listGuildInvitions.get(i);
			Object[] objs = guildInvitionVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setGuildInvitions(StringUtil.implode(list, ","));
	}

	private void loadGuildInvitions() {
		if (this.guildInvitions == null) {
			return;
		}
		String[] items = StringUtil.split(this.guildInvitions, ",");
		if(items!=null && items.length>0){
			listGuildInvitions=new CopyOnWriteArrayList<GuildInvitionVo>();
		}
		for (String itemStr : items) {
			GuildInvitionVo guildInvitionVo = new GuildInvitionVo();
			guildInvitionVo.loadProperty(itemStr, "|");
			listGuildInvitions.add(guildInvitionVo);
		}
	}
	
	private void loadSpecialTitle(){
		if (this.specialTitleLv == null) {
			return;
		}
		String[] items = StringUtil.split(this.specialTitleLv, ",");
		if(items!=null && items.length>0){
			listSpecialTitle=new CopyOnWriteArrayList<SpecialTitleVo>();
		}
		for (String itemStr : items) {
			SpecialTitleVo specialTitleVo = new SpecialTitleVo();
			specialTitleVo.loadProperty(itemStr, "|");
			listSpecialTitle.add(specialTitleVo);
		}
	}

	private void loadPet() {
		// 加载星座数据
		if (StringUtil.isEmpty(this.petConstell)) {
			// 首次创建开启宠物星座（开启星座开启所需宠物等阶为0的）
			Collection<PetConstell> petConstells = GlobalCache.petConstell
					.values();
			PetConstell petConstell = null;
			for (PetConstell pc : petConstells) {
				if (pc.openNode == 0) {
					petConstell = pc;
					break;
				}
			}
			if (null != petConstell)
				saveNewPetConstell(petConstell, false);
		} else {
			// 加载已开启的宠物星座
			String[] strs = StringUtil.split(this.petConstell, ",");
			for (String str : strs) {
				String[] vals = str.split("\\|");
				Integer attachId = DBFieldUtil.fetchImpodInt(vals[0]);
				petConstellStrengthenMap.put(attachId,
						Integer.parseInt(vals[1]));
				String[] atts = StringUtil.split(vals[2], "@");
				List<PetConstellVo> pcVoList = new ArrayList<PetConstellVo>();
				for (String att : atts) {
					PetConstellVo petConstellVo = new PetConstellVo();
					petConstellVo.loadProperty(att, ":");
					pcVoList.add(petConstellVo);
				}
				petConstellMap.put(attachId, pcVoList);
			}
			transformPet();
		}

		// 加载天赋数据
		if (!StringUtil.isEmpty(this.petTalent)) {
			String[] talents = this.petTalent.split("\\|");
			for (String talent : talents) {
				listPetTalent.add(Integer.parseInt(talent));
			}
		}
	}

	/**
	 * 转换给客户端用，哎。。
	 */
	private void transformPet() {
		petConstellsList.clear();
		petConstellStrengthensList.clear();
		for (int i = 0, max = petConstellMap.size(); i < max; i++) {
			int index = i + 1;
			petConstellsList.add(petConstellMap.get(index));
			petConstellStrengthensList.add(new IdNumberVo(index,
					petConstellStrengthenMap.get(index)));
		}
	}

	private void loadSustainKillVo() {
		if (StringUtil.isEmpty(sustainKill))
			setSustainKill("0|");
		sustainKillVo.loadProperty(sustainKill, "|");
	}

	/**
	 * 增加连斩
	 * 
	 * @param roleId
	 */
	public void addSustainKill(Integer roleId) {
		int oldKillNum = sustainKillVo.killNum;
		int num = 0;
		for (Integer rId : sustainKillVo.roleIds) {
			if (rId.equals(roleId)) {
				num++;
				if (num >= 3)
					return;
			}
		}
		sustainKillVo.add(roleId);
//		System.out.println("sustainKillVo = "+sustainKillVo.toString());
//		System.out.println("oldKillNum = "+ oldKillNum+"; sustainKillVo.killNum = "+sustainKillVo.killNum);
		updateSustainKillBuff(oldKillNum, sustainKillVo.killNum);
		setSustainKill(sustainKillVo.toString());
		sendSustainKill();
	}

	/**
	 * 清空连斩
	 */
	public void clearSustainKill() {
		sustainKillVo.clear();
		clearSkillBuff();
		setSustainKill(sustainKillVo.toString());
		sendSustainKill();
	}
	
	public void clearSkillBuff(){
		if(fighter != null){
			List<BufferStatusVo> removeList = fighter.findBuffSkill();
//			System.out.println("removeList = " +removeList.size());
			for(BufferStatusVo  bsv : removeList){
				fighter.removeBuffer(bsv.buffPo.getId());				
			}			
		}
	}

	/**
	 * 更新连斩BUFF
	 */
	private void updateSustainKillBuff(int oldKillNum, int nowKillNum) {
		
		boolean reload = false;// 是否要更新连斩buff
		
//		Set<Entry<Integer, BuffPo>> sets = GlobalCache.sustainKillMap.entrySet();
//		for (Entry<Integer, BuffPo> set : sets) {
//			if (set.getKey() <= oldKillNum) {
//				reload = true;
//				break;
//			}
//		}
		
		
		if (nowKillNum > oldKillNum) {
			BuffPo buffPo = GlobalCache.sustainKillMap.get(nowKillNum);
			if (buffPo != null)
				reload = true;
		} else {
			Set<Entry<Integer, BuffPo>> sets = GlobalCache.sustainKillMap.entrySet();
			for (Entry<Integer, BuffPo> set : sets) {
				if (set.getKey() <= oldKillNum) {
					reload = true;
					break;
				}
			}
		}
		if (reload) {
			BuffPo buffPo = sustainKillBuff();
			if (fighter!=null && buffPo != null && buffPo.bufferEffetVos.get(0).buffType == BuffType.BUFF_EFFECT_9){
				clearSkillBuff();
				BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, fighter, new ArrayList<Fighter>());
				fighter.makeAddBuff(bufferStatusVo, fighter, true);				
			}
		}
	}

	/**
	 * 添加buff效果到效果列表
	 * 
	 * @param bufferStatusVo
	 */
	public void makeAddBuff(BufferStatusVo bufferStatusVo) {
		BufferStatusVo oldbsVo = findBufferStatus(bufferStatusVo);
		if (oldbsVo == null)
			bufferStatusVos.add(bufferStatusVo);
		else {
			// 如果已有这个BUFF，且不是永久有效的，则更新结束时间为新的BUFF的结束时间
			if (oldbsVo.buffPo.getDurationValexp() != CopySceneType.EIKY_TIME)
				oldbsVo.endTime = System.currentTimeMillis()
						+ bufferStatusVo.buffPo.getDurationValexp();
		}
	}

	public void delBuff(BuffModelType buffModelType, int buffId) {
		Iterator<BufferStatusVo> iter = bufferStatusVos.iterator();
		while (iter.hasNext()) {
			BufferStatusVo bsVo = iter.next();
			if (bsVo.buffPo.getId() == buffId) {
				bufferStatusVos.remove(bsVo);
				return;
			}
		}
	}



	private BufferStatusVo findBufferStatus(BufferStatusVo bufferStatusVo) {
		int buffId = bufferStatusVo.buffPo.getId();
		Iterator<BufferStatusVo> iter = bufferStatusVos.iterator();
		while (iter.hasNext()) {
			BufferStatusVo bsVo = iter.next();
			if (bsVo.buffPo.getId() == buffId) {
				return bsVo;
			}
		}
		return null;
	}

	/**
	 * 加载玩家时装信息
	 */
	private void loadFashion() {
		if (StringUtil.isNotEmpty(this.fashion)) {
			String[] items = StringUtil.split(this.fashion, ",");
			if(items != null && items.length>0){
				roleFashions = new CopyOnWriteArrayList<RoleFashionVo>();
			}
			for (String itemStr : items) {
				RoleFashionVo roleFashionVo = new RoleFashionVo();
				roleFashionVo.loadProperty(itemStr, "|");
				roleFashions.add(roleFashionVo);
			}
		}
	}
	
	private void loadHiddenFashion(){
		if(wasHiddenFashion!=null){
			hiddenFashions = IdNumberVo.createList(wasHiddenFashion);
		}else{
			hiddenFashions.add(new IdNumberVo(1, 0));
			hiddenFashions.add(new IdNumberVo(2, 0));
			setWasHiddenFashion(IdNumberVo.createStr(hiddenFashions));
		}
	}

	
	private void saveHiddenFashion(){
		wasHiddenFashion = IdNumberVo.createStr(hiddenFashions);
	}

	/**
	 * 获得新的特殊称号
	 * 
	 * @param titlePo
	 */
	public void addSpecialTitle(TitlePo titlePo) {
		if (titlePo.getType().intValue() == 2) {
			SpecialTitleVo specialTitleVo = new SpecialTitleVo();
			specialTitleVo.id=titlePo.getId();
			specialTitleVo.lv=titlePo.getLv();
			specialTitleVo.type=titlePo.getType();
			if(titlePo.getDurationVale().intValue() == -1){
				specialTitleVo.endTime=-1l;
			}else{
				specialTitleVo.endTime=System.currentTimeMillis()+ titlePo.getDurationVale() * 1000l;				
			}
			listSpecialTitle.add(specialTitleVo);	
			this.updateTitle(true);
		}
	}

	/**
	 * 更新称号
	 * 
	 * @param isSend
	 *            是否通知客户端
	 */
	public void updateTitle(boolean isSend) {
		checkRemoveSpecialTitleVo(null);
		int oldTitileLv = nowTitleLv;
		int temp = 0;
		for(SpecialTitleVo specialTitleVo : listSpecialTitle){
			if(specialTitleVo.lv > temp){
				temp = specialTitleVo.lv;
			}
		}
		setCurrentSpecialTitleLv(temp);
		if(currentSpecialTitleLv > 0){
			nowTitleLv= currentSpecialTitleLv;
		}else{
			nowTitleLv=titleLv;
		}
//		System.out.println("oldTitileLv= "+oldTitileLv+"; nowTitleLv="+nowTitleLv);
		if (isSend) {
			this.sendUpdateAchieveAndTitle();
			if (fighter != null)
				fighter.updateTitle();
		}
	}
	
	/**
	 * 检查删除特殊称号
	 * @param id
	 */
	public void checkRemoveSpecialTitleVo(Integer id){
		List<SpecialTitleVo> removeList = new ArrayList<SpecialTitleVo>();
		if(id == null){
			for(SpecialTitleVo specialTitleVo : listSpecialTitle){
				if(specialTitleVo.endTime!=-1 && System.currentTimeMillis() > specialTitleVo.endTime  ){
					removeList.add(specialTitleVo);
				}
			}
			for(SpecialTitleVo specialTitleVo : removeList){
				listSpecialTitle.remove(specialTitleVo);
			}
		}
		else{
			for(SpecialTitleVo specialTitleVo : listSpecialTitle){
				if(specialTitleVo.lv == id.intValue()){
					removeList.add(specialTitleVo);
				}
			}
			for(SpecialTitleVo specialTitleVo : removeList){
				listSpecialTitle.remove(specialTitleVo);
			}
		}
	}
	
	



	/**
	 * 添加新的时装
	 * 
	 * @param id
	 */
	public void addFashion(int id, int time) {
		FashionPo fashionPo = FashionPo.findEntity(id);
		boolean bool = true;
		for(RoleFashionVo roleFashionVo : roleFashions){
			if(roleFashionVo.id == id){
				roleFashionVo.endTime+= 1000L*time;
				bool = false;
				break;
			}
		}
		
		if(bool){
			RoleFashionVo roleFashionVo = new RoleFashionVo();
			roleFashionVo.id = id;
			roleFashionVo.endTime = System.currentTimeMillis() +1000L*time;
			roleFashionVo.isUse = 0;
			roleFashionVo.fashionPo = fashionPo;
			roleFashionVo.type = fashionPo.getType();
			roleFashions.add(roleFashionVo);
		}
		sendAddFashion();
	}
	
	/**
	 * 删除时装
	 * @param id
	 */
	public void removeFashion(){
		boolean bool = false;
		for(Iterator<RoleFashionVo> iterator=roleFashions.iterator();iterator.hasNext();){
			RoleFashionVo roleFashionVo = iterator.next();
			if(System.currentTimeMillis() > roleFashionVo.endTime){
				roleFashions.remove(roleFashionVo);
				bool = true;
			}
		}
		roleFashion = new CopyOnWriteArrayList<RoleFashionVo>();
		for(RoleFashionVo roleFashionVo : roleFashions){
			roleFashion.add(roleFashionVo);
		}
		
		if(bool){
			sendAddFashion();	
			if(fighter != null && fighter.mapRoom != null && fighter.cell != null){
				CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(), fashion, getCareer(), getWingWasHidden(),getWingStar(),hiddenFashions);
				fighter.makeAvatars(commonAvatarVo,true);
			}
		}
	}
	
	/**
	 * 登录初始化正在穿的时装
	 */
	public void checkLoginInitFashion(){
		roleFashion = new CopyOnWriteArrayList<RoleFashionVo>();
		if (StringUtil.isNotEmpty(fashion)) {
//			PrintUtil.print("fashion: "+fashion);
			String[] items = StringUtil.split(this.fashion, ",");
			for (String itemStr : items) {
				RoleFashionVo roleFashionVo = new RoleFashionVo();
				roleFashionVo.loadProperty(itemStr, "|");
				if(roleFashionVo.isUse==1){
					roleFashion.add(roleFashionVo);
				}
			}
		}
	}

	@Override
	public void saveData() {
		updateRolePackVos();
		updateRoleWarehousePackItemVos();
		updateGuildInvitions();
		updateYunDartTaskInfo();
		updateSlotSouls();
		updateActivityInfo();
		updateRechargeInfo();
		updateAdvanceSuitPlus();
		updateAwardRetrieveVo();
		updateRoleLiveActivityVo();
		updateGrowFundVo();
		updateInvitationRoleVo();
		updateSpecialTitle();
		updateRoleFashion();
		saveHiddenFashion();
		List<Integer> listFriends = new ArrayList<Integer>();
		List<Integer> listBlocks = new ArrayList<Integer>();
		List<Integer> listEnemys = new ArrayList<Integer>();
		for (RoleInforPo roleInforPo : this.listEnemys) {
			listEnemys.add(roleInforPo.getId());
		}
		for (RoleInforPo roleInforPo : this.listFriends) {
			listFriends.add(roleInforPo.getId());
		}
		for (RoleInforPo roleInforPo : this.listBlocks) {
			listBlocks.add(roleInforPo.getId());
		}

		setFriends(DBFieldUtil.implod(listFriends, ","));
		setBlocks(DBFieldUtil.implod(listBlocks, ","));
		setEnemys(DBFieldUtil.implod(listEnemys, ","));

		setSkillFormation(DBFieldUtil.implod(listSkillFormation, ","));
		setSkillVos(IdNumberVo.createStr(listSkillVos, "|", ","));
		setRoleTasks(IdNumberVo2.createStr(listRoleTasks, "|", ","));

		setRoleAchieves(IdNumberVo2.createStr(listRoleAchievesTasks, "|", ","));
		setRoleTreasures(IdNumberVo.createStr(listRoleTreasures, "|", ","));
		setProductTodayBuyed(IdNumberVo.createStr(listProductTodayBuyed, "|", ","));
		setCopySceneTodayVisitTimes(IdNumberVo3.createStr(listCopySceneTodayVisitTimes, "|", ","));
		setBufferStatus(ExpressUtil.buildBattleExpressStrStr(listBufferStatus));
		setGrowFundsAward1(IdNumberVo2.createStr(listgrowFundsAward1, "|", ","));
		setGrowFundsAward2(IdNumberVo2.createStr(listgrowFundsAward2, "|", ","));
		setInvitationTask(IdNumberVo2.createStr(listInvitationTask, "|", ","));
		List<Integer> vals = new ArrayList<Integer>();
		if(listRpets == null ){
			listRpets = new CopyOnWriteArrayList<RpetPo>();
		}
		for (RpetPo rpetPo : listRpets) {
			if(rpetPo != null){
				RpetPo rp = RpetPo.findEntity(rpetPo.getId());
				if(rp != null){
					vals.add(rpetPo.getId());									
				}
			}
		}
		setRpets(DBFieldUtil.implod(vals, ","));
		setEveryDayBuyProductCount(IdNumberVo.createStr(
				listEveryDayBuyProductCount, "|", ","));
		setOnlyBuyProductCount(IdNumberVo.createStr(listOnlyBuyProductCount,
				"|", ","));
		setOpenSystemList(DBFieldUtil
				.implodInterListWithCommer(openSystemArrayList));
		setRoleOptions(IdNumberVo.createStr(listRoleOptions, "|", ","));
		setOnlineTime(IdLongVo2.createStr(listOnlineTime, "|", ","));
		setCumulativeLoginAwardRecord(IdNumberVo.createStr(
				listCumulativeLoginAwardRecord, "|", ","));
		setToTakeCumulativeLoginAwardRecord(IdNumberVo.createStr(
				listToTakeCumulativeLoginAwardRecord, "|", ","));
		setLevelAwardRecord(IdNumberVo
				.createStr(listLevelAwardRecord, "|", ","));
		setOnlineTimeAwrodRecord(IdNumberVo.createStr(
				listOnlineTimeAwrodRecord, "|", ","));
		setSignInAwardRecord(IdNumberVo2.createStr(listSignInAwardRecord, "|",
				","));
		setDailyLivelyAwardRecord(IdNumberVo.createStr(
				listDailyLivelyAwardRecord, "|", ","));
		setFightActivityMaxScoreRecords(IdNumberVo.createStr(
				listFightActivityMaxScoreRecords, "|", ","));
		setGuildBossAward(IdNumberVo.createStr(listGuildBossAward, "|", ","));
		setMilitaryRankRecord(IdNumberVo.createStr(listMilitaryRankRecord, "|", ","));
		setBuyPlayTimes(IdNumberVo2.createStr(listBuyPlayTimes, "|", ","));
		setGuildTodayExchangeItems(IdNumberVo.createStr(listGuildTodayExchangeItems, "|", ","));
		setDotaFirstAward(IdNumberVo.createStr(listDotaFirstAward, "|", ","));
		setEachFirstRechargeStatus(IdNumberVo.createStr(listEachFirstRechargeStatus, "|", ","));
		saveConsumCost();
		saveSoulAtb();
	}

	private void updateSlotSouls() {
		if (this.listSlotSouls == null) {
			setSlotSouls(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listSlotSouls.size(); i++) {
			SlotSoulVo slotSoulVo = listSlotSouls.get(i);
			Object[] objs = slotSoulVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setSlotSouls(StringUtil.implode(list, ","));
	}

	private void updateActivityInfo() {
		if (this.listActivityInfo == null) {
			setActivityInfo(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listActivityInfo.size(); i++) {
			ActivityInfoVo aiv = listActivityInfo.get(i);
			Object[] objs = aiv.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setActivityInfo(StringUtil.implode(list, ","));
	}
	
	public void updateRoleFashion(){
		if(this.roleFashions == null){
			setFashion(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < this.roleFashions.size(); i++){
			RoleFashionVo roleFashionVo = roleFashions.get(i);
			Object[] objs = roleFashionVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr); 
		}
		setFashion(StringUtil.implode(list, ","));
	} 
	
	private void updateRoleLiveActivityVo(){
		if(this.listRoleLiveActivitys == null){
			setRoleLiveActivitys(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listRoleLiveActivitys.size(); i++) {
			RoleLiveActivityVo roleLiveActivityVo = listRoleLiveActivitys.get(i);
			Object[] objs = roleLiveActivityVo.fetchProperyItems();
			String str = IdNumberVo3.createStr((List<IdNumberVo3>)objs[0], "|", ",");
			objs[0]=str;
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "#");
			list.add(targetStr);
		}
		setRoleLiveActivitys(StringUtil.implode(list, "@"));
	}
	
	private void updateGrowFundVo(){
		if(this.growFundVo == null){
			setGrowFundStr(null);
			return;
		}
		Object[] objs = this.growFundVo.fetchProperyItems();
		String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
		setGrowFundStr(targetStr);
	}
	
	private void updateInvitationRoleVo(){
		if(this.listInvitationFriend== null){
			setInvitationFriend(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listInvitationFriend.size(); i++) {
			InvitationRoleVo invitationRoleVo = listInvitationFriend.get(i);
			Object[] objs = invitationRoleVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setInvitationFriend(StringUtil.implode(list, ","));
	}
	
	private void updateSpecialTitle(){
		if(this.listSpecialTitle==null){
			setSpecialTitleLv(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<this.listSpecialTitle.size(); i++){
			SpecialTitleVo specialTitleVo = listSpecialTitle.get(i);
			Object[] objs = specialTitleVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setSpecialTitleLv(StringUtil.implode(list, ","));
	}
	
	private void updateAwardRetrieveVo(){
		if(this.listAwardRetrieve == null){
			setAwardRetrieve(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listAwardRetrieve.size(); i++) {
			AwardRetrieveVo arv = listAwardRetrieve.get(i);
			Object[] objs = arv.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setAwardRetrieve(StringUtil.implode(list, ","));
	}

	private void updateRechargeInfo() {
		if (this.listRechargeInfo == null) {
			setRechargeInfo(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listRechargeInfo.size(); i++) {
			RechargeInfoVo riv = listRechargeInfo.get(i);
			Object[] objs = riv.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setRechargeInfo(StringUtil.implode(list, ","));
	}

	private void updateYunDartTaskInfo() {
		if (this.listYunDartTaskInfoVo == null) {
			setYunDartTaskInfo(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listYunDartTaskInfoVo.size(); i++) {
			YunDartTaskInfoVo ydti = listYunDartTaskInfoVo.get(i);
			Object[] objs = ydti.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setYunDartTaskInfo(StringUtil.implode(list, ","));
	}

	private void updateAdvanceSuitPlus() {
		if (this.listAdvanceSuitPlus== null) {
			setAdvanceSuitPlus(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < this.listAdvanceSuitPlus.size(); i++) {
			AdvanceSuitPlusVo advanceSuitPlusVo = listAdvanceSuitPlus.get(i);
			Object[] objs = advanceSuitPlusVo.fetchProperyItems();
			String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
			list.add(targetStr);
		}
		setAdvanceSuitPlus(StringUtil.implode(list, ","));
	}



	/**
	 * 
	 * 方法功能:激活角色等级 更新时间:2014-6-27, 作者:johnny
	 * 
	 * @param id2
	 */
	public void activeRoleTask(Integer id2) {
		TaskPo taskPo = TaskPo.findEntity(id2);
		if (taskPo == null) {
			return;
		}
		if(taskPo.getTaskType()==TaskType.TASK_TYPE_MAIN){
			setMainTaskId(id2);
			setMainTaskName(taskPo.getName());
		}
		listRoleTasks.add(new IdNumberVo2(id2, TaskType.TASK_STATUS_ACTIVED,
				TaskType.TASK_NEW_TASK_NEW));
		

	}
	/** 接取任务 */
	public void acceptedRoleTask(Integer activeTaskId){
		IdNumberVo2 idNumberVo =  fetchRoleTask(activeTaskId);
		idNumberVo.setInt2(TaskType.TASK_STATUS_ACCEPTED);
		TaskPo taskPo = TaskPo.findEntity(activeTaskId);
		if(taskPo == null){
			return;
		}
		if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_716){
			Integer num = fetchListRpetsSizeByQuality(taskPo.conditionVals.get(1));
			taskConditionProgressReplace(num, TaskType.TASK_TYPE_CONDITION_716,taskPo.conditionVals.get(1), null);
		}else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_717){
			taskConditionProgressReplace(fetchListRpetsMaxLv(), TaskType.TASK_TYPE_CONDITION_717,null, null);
		}else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_715){
			taskConditionProgressReplace((getWingStar()/11 + 1), TaskType.TASK_TYPE_CONDITION_715,null, null);
		}else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_712){
			for (SlotSoulVo slotSoulVo : listSlotSouls) {
				taskConditionProgressReplace(slotSoulVo.powerLv ,TaskType.TASK_TYPE_CONDITION_712,null,null);				
			}
		}else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_724){
			checkpowerEquip(listRoleTasks);
			checkpowerEquip(listRoleAchievesTasks);
		}
		else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_712){
			checkTaskType712(listRoleTasks);
			checkTaskType712(listRoleAchievesTasks);
		}
		else if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_713){
			checkTaskType713(listRoleTasks);
			checkTaskType713(listRoleAchievesTasks);
		}
	}

	/**
	 * 
	 * 方法功能:等级到了激活对应任务 更新时间:2014-6-25, 作者:johnny
	 * 
	 * @param lv
	 */
	public void activeLevelReachTask(Integer lv) {
		List<TaskPo> levelTasks = new ArrayList<TaskPo>();
		int[] types = new int[] { TaskType.TASK_TYPE_MAIN,
				TaskType.TASK_TYPE_SUB, TaskType.TASK_TYPE_YUN_DART };
		for (int i : types) {
			if (!GlobalCache.taskListByTypes.containsKey(i)) {
				continue;
			}
			for (TaskPo taskPo : GlobalCache.taskListByTypes.get(i)) {
				if (taskPo.getActiveLv() != null
						&& taskPo.listActiveLv.get(0) == lv.intValue()) {
					levelTasks.add(taskPo);
				}
			}
		}
		for (TaskPo taskPo : levelTasks) {
			activeRoleTask(taskPo.getId());
		}
	}

	/**
	 * 方法功能:初始化活跃任务 更新时间:2015-7-10, 作者:peter
	 */
	public void checkInitializeLivelyTask() {
		List<TaskPo> livelyTasks = new ArrayList<TaskPo>();
		for (TaskPo taskPo : GlobalCache.taskListByTypes
				.get(TaskType.TASK_TYPE_LIVELY)) {
			boolean flag = true;
			ALable: for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
				if (taskPo.getId().intValue() == idNumberVo2.getInt1()
						.intValue()) {
					idNumberVo2.setInt2(TaskType.TASK_STATUS_ACCEPTED);
					flag = false;
					break ALable;
				}
			}
			if (flag) {
				livelyTasks.add(taskPo);
			}
		}
		for (TaskPo taskPo : livelyTasks) {
			listRoleTasks.add(new IdNumberVo2(taskPo.getId(),
					TaskType.TASK_STATUS_ACCEPTED, TaskType.TASK_NEW_TASK_NEW));
		}
	}

	/**
	 * 方法功能:初始化成就任务 更新时间:2015-7-10, 作者:peter
	 */
	public void checekInitializeAchieveTase() {
		if (listRoleAchievesTasks == null || listRoleAchievesTasks.size() == 0) {
			listRoleAchievesTasks = new CopyOnWriteArrayList<IdNumberVo2>();
			for (TaskPo taskPo : GlobalCache.taskListByTypes
					.get(TaskType.TASK_TYPE_ACHIEVE)) {
				listRoleAchievesTasks.add(new IdNumberVo2(taskPo.getId(),
						TaskType.TASK_STATUS_ACCEPTED,
						TaskType.TASK_NEW_TASK_NEW));
			}
		}
	}
	
	/**
	 * 方法功能:初始化军衔 更新时间:2015-7-10, 作者:peter
	 */
	public void checkInitializeMilitaryRankRecord(){
		if(listMilitaryRankRecord == null || listMilitaryRankRecord.size() == 0){
			listMilitaryRankRecord = new CopyOnWriteArrayList<IdNumberVo>();
			GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<MilitaryRankPo> MilitaryRankPoList =gameDataTemplate.getDataList(MilitaryRankPo.class);
			for(int i =0; i < MilitaryRankPoList.size(); i++){
				MilitaryRankPo mrp = MilitaryRankPoList.get(i);
				listMilitaryRankRecord.add(new IdNumberVo(mrp.getId(), 0));
			}
		}
	}
	/**
	 * 方法功能:初始化购买次数 更新时间:2015-7-10, 作者:peter
	 */
	public void checkInitializeBuyPlayTimes(){
		if(listBuyPlayTimes == null || listBuyPlayTimes.size() == 0 || XmlCache.xmlFiles.constantFile.playTimes.playItem.size() != listBuyPlayTimes.size()){
			initializeDailyBuyPlayTimes();
		}
	}
	/**
	 * 方法功能:初始化每天购买的次数 更新时间:2015-7-10, 作者:peter
	 */
	public void initializeDailyBuyPlayTimes(){
		List<PlayItem> playItemList = XmlCache.xmlFiles.constantFile.playTimes.playItem;
		listBuyPlayTimes = new CopyOnWriteArrayList<IdNumberVo2>();
		for(PlayItem playItem : playItemList){
			listBuyPlayTimes.add(new IdNumberVo2(playItem.timesType, playItem.buyTimes, 0));
		}	
	}

	public void calculateBat(Integer state) {

		if(wasRobot() && taskRobot==false){
			return;
		}
		List<List<List<Integer>>> finalTotalBatExpress = new ArrayList<List<List<Integer>>>();

		// 等级加成
		LvConfigPo lvConfigPo = LvConfigPo.findEntity(getLv());
		finalTotalBatExpress.add(lvConfigPo.listBatAttrExp);
		
		// 军衔加成
		MilitaryRankPo militaryRankPo = MilitaryRankPo.findEntity(currentMilitaryRankId);
		if(militaryRankPo != null){
			finalTotalBatExpress.add(militaryRankPo.listBatAttrExp);
		}
		
		// 增加装备属性
		powerSuitPlusMap.clear();
		starSuitPlusMap.clear();
		washSuitPlusMap.clear();
		for (Integer slotId : RoleType.EQUIP_SLOTS) {
			List<List<Integer>> list = fetchCalculateBySlot(slotId);
			if (list != null) {
				finalTotalBatExpress.add(list);
			}
		}

		if (!wasRobot()) {
			// 增加套装属性
			// System.out.println("强化升星："+starSuitPlusMap);
			PowerItem pi = fetchPowerSuitPlusInfo(powerSuitPlusMap, state);
			StarItem si = fetchStarSuitPlusInfo(starSuitPlusMap, state);
			WashItem washItem = fetchWshSuitPlus(washSuitPlusMap, state);
			
			if (pi != null) {
				List<List<Integer>> listPowerSuitPlusExpress = ExpressUtil.buildBattleExpressList(pi.atb);
				// System.out.println("listPowerSuitPlusExpress =="+listPowerSuitPlusExpress);
				finalTotalBatExpress.add(listPowerSuitPlusExpress);
			}

			if (si != null) {
				List<List<Integer>> listStarSuitPlusExpress = ExpressUtil.buildBattleExpressList(si.atb);
				// System.out.println("listStarSuitPlusExpress =="+listStarSuitPlusExpress);
				finalTotalBatExpress.add(listStarSuitPlusExpress);
			}
			if(washItem != null){
				List<List<Integer>> listStarSuitPlusExpress = ExpressUtil.buildBattleExpressList(washItem.atb);
				// System.out.println("listStarSuitPlusExpress =="+listStarSuitPlusExpress);
				finalTotalBatExpress.add(listStarSuitPlusExpress);
			}
		}

		// 翅膀
		if (wingEquipStatus != null && !wasRobot()) {
			if (wingEquipStatus.intValue() != 0) {
				String batExp = XmlCache.xmlFiles.constantFile.wing.stars.star
						.get(wingStar).batExp;
				// System.out.println(name +"StarBatExp == " +batExp);
				List<List<Integer>> listStarBatExp = ExpressUtil
						.buildBattleExpressList(batExp);
				finalTotalBatExpress.add(listStarBatExp);
			}
		}

		// 宠物
		// 天赋
		List<List<Integer>> petTalentAttack = this.loadPetTalentAttackRole();
		if (null != petTalentAttack)
			finalTotalBatExpress.add(petTalentAttack);
		// 连斩
		List<List<Integer>> sustainKillAtt = loadSustainKillAtt();
		if (null != sustainKillAtt)
			finalTotalBatExpress.add(sustainKillAtt);
		// 时装
		if (!roleFashion.isEmpty()) {
			for(RoleFashionVo roleFashionVo:roleFashion){
				FashionPo fPo = roleFashionVo.fashionPo;
				if (fPo != null) {
					List<List<Integer>> fashionBatExp = fPo.listBatExp;
					if(fashionBatExp!=null&&!fashionBatExp.isEmpty()){
						finalTotalBatExpress.add(fashionBatExp);
					}
				}
			}
		}

		// 普通称号
		TitlePo titlePo = GlobalCache.titlePoMap.get(getTitleLv());
		if (titlePo != null){
			finalTotalBatExpress.add(titlePo.listBatAttrExp);			
		}
		// 特殊称号
		TitlePo specialTitlePo = GlobalCache.titlePoMap.get(getCurrentSpecialTitleLv());
		if (specialTitlePo != null){
			finalTotalBatExpress.add(specialTitlePo.listBatAttrExp);			
		}

		// buff
		for (List<String> vals : listBufferStatus) {
			BuffPo buffPo = BuffPo.findEntity(Integer.valueOf(vals.get(0)));
			for (BufferEffetVo buffVo : buffPo.bufferEffetVos) {
				if (buffVo.buffType == BuffType.BUFF_EFFECT_9) {
					String batExp = buffVo.param1 + "|" + buffVo.param2;
					List<List<Integer>> listBuffBatExp = ExpressUtil.buildBattleExpressList(batExp);
					finalTotalBatExpress.add(listBuffBatExp);
				}
			}
		}
		//灵魂
		if(!soulAtbMap.isEmpty()){
			SoulService soulService = SoulService.instance();
			finalTotalBatExpress.add(soulService.caculateBatExp(soulAtbMap));
		}
//		System.out.println("RolePo.calculateBat() listBufferStatus="+listBufferStatus);

		int batMeleeAttack = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttack, finalTotalBatExpress);
		int batMagicAttack = ExpressUtil.findExpressEffectType(
				RoleType.batMagicAttack, finalTotalBatExpress);
		int batMeleeDefence = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefence, finalTotalBatExpress);
		int batMagicDefence = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefence, finalTotalBatExpress);
		int batMeleeAttackMin = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttackMin, finalTotalBatExpress);
		int batMagicAttackMin = ExpressUtil.findExpressEffectType(RoleType.batMagicAttackMin, finalTotalBatExpress);
		int batMeleeDefenceMin = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefenceMin, finalTotalBatExpress);
		int batMagicDefenceMin = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefenceMin, finalTotalBatExpress);
		int batMeleeAttackMax = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttackMax, finalTotalBatExpress);
		int batMagicAttackMax = ExpressUtil.findExpressEffectType(
				RoleType.batMagicAttackMax, finalTotalBatExpress);
		int batMeleeDefenceMax = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefenceMax, finalTotalBatExpress);
		int batMagicDefenceMax = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefenceMax, finalTotalBatExpress);
		int batMaxHp = ExpressUtil.findExpressEffectType(RoleType.batMaxHp,
				finalTotalBatExpress);
		int batMaxMp = ExpressUtil.findExpressEffectType(RoleType.batMaxMp,
				finalTotalBatExpress);
		int batCritical = ExpressUtil.findExpressEffectType(
				RoleType.batCritical, finalTotalBatExpress);
		int batHpReg = ExpressUtil.findExpressEffectType(RoleType.batHpReg,
				finalTotalBatExpress);
		int batMpReg = ExpressUtil.findExpressEffectType(RoleType.batMpReg,
				finalTotalBatExpress);
		int batMovement = ExpressUtil.findExpressEffectType(
				RoleType.batMovement, finalTotalBatExpress);
		int batDodge = ExpressUtil.findExpressEffectType(RoleType.batDodge,
				finalTotalBatExpress);
		int batLuckyAttack = ExpressUtil.findExpressEffectType(
				RoleType.batLuckyAttack, finalTotalBatExpress);
		int batHitRate = ExpressUtil.findExpressEffectType(RoleType.batHitRate,
				finalTotalBatExpress);
		int batLuckyDefence = ExpressUtil.findExpressEffectType(
				RoleType.batLuckyDefence, finalTotalBatExpress);
		int batTough = ExpressUtil.findExpressEffectType(RoleType.batTough,
				finalTotalBatExpress);
		int batDamageRate = ExpressUtil.findExpressEffectType(
				RoleType.batDamageRate, finalTotalBatExpress);
		int batDamageResistRate = ExpressUtil.findExpressEffectType(
				RoleType.batDamageResistRate, finalTotalBatExpress);
		int batCriticalDamageRate = ExpressUtil.findExpressEffectType(
				RoleType.batCriticalDamageRate, finalTotalBatExpress);
		int batAttackRate = ExpressUtil.findExpressEffectType(
				RoleType.batAttackRate, finalTotalBatExpress);
		int batDefenceRate = ExpressUtil.findExpressEffectType(
				RoleType.batDefenceRate, finalTotalBatExpress);
		int batHitRateRate = ExpressUtil.findExpressEffectType(
				RoleType.batHitRateRate, finalTotalBatExpress);
		int batDodgeRate = ExpressUtil.findExpressEffectType(
				RoleType.batDodgeRate, finalTotalBatExpress);
		int batAddCriticalRate = ExpressUtil.findExpressEffectType(
				RoleType.batAddCriticalRate, finalTotalBatExpress);
		int batDerateCriticalRate = ExpressUtil.findExpressEffectType(
				RoleType.batDerateCriticalRate, finalTotalBatExpress);
		int batReboundDamage = ExpressUtil.findExpressEffectType(
				RoleType.batReboundDamage, finalTotalBatExpress);
		// 百分比属性修正
		batMeleeAttack = batMeleeAttack + batMeleeAttack * batAttackRate / 1000;
		batMagicAttack = batMagicAttack + batMagicAttack * batAttackRate / 1000;
		batMeleeDefence = batMeleeDefence + batMeleeDefence * batDefenceRate
				/ 1000;
		batMagicDefence = batMagicDefence + batMagicDefence * batDefenceRate
				/ 1000;
		batMeleeAttackMin = batMeleeAttackMin + batMeleeAttackMin
				* batAttackRate / 1000;
		batMagicAttackMin = batMagicAttackMin + batMagicAttackMin* batAttackRate / 1000;
		batMeleeDefenceMin = batMeleeDefenceMin + batMeleeDefenceMin
				* batDefenceRate / 1000;
		batMagicDefenceMin = batMagicDefenceMin + batMagicDefenceMin
				* batDefenceRate / 1000;
		batMeleeAttackMax = batMeleeAttackMax + batMeleeAttackMax
				* batAttackRate / 1000;
		batMagicAttackMax = batMagicAttackMax + batMagicAttackMax
				* batAttackRate / 1000;
		batMeleeDefenceMax = batMeleeDefenceMax + batMeleeDefenceMax
				* batDefenceRate / 1000;
		batMagicDefenceMax = batMagicDefenceMax + batMagicDefenceMax
				* batDefenceRate / 1000;

		// 最后赋值
		setBatMeleeAttack(batMeleeAttack);
		setBatMagicAttack(batMagicAttack);
		setBatMeleeAttackMin(batMeleeAttackMin);
		setBatMagicAttackMin(batMagicAttackMin);
		setBatMeleeDefenceMin(batMeleeDefenceMin);
		setBatMagicDefenceMin(batMagicDefenceMin);
		setBatMeleeAttackMax(batMeleeAttackMax);
		setBatMagicAttackMax(batMagicAttackMax);
		setBatMeleeDefenceMax(batMeleeDefenceMax);
		setBatMagicDefenceMax(batMagicDefenceMax);
		setBatMaxHp(batMaxHp);
		setBatMaxMp(batMaxMp);
		setBatCritical(batCritical);
		setBatHpReg(batHpReg);
		setBatMpReg(batMpReg);
		setBatMovement(batMovement);
		setBatDodge(batDodge);
		setBatLuckyAttack(batLuckyAttack);
		setBatHitRate(batHitRate);
		setBatMeleeDefence(batMeleeDefence);
		setBatMagicDefence(batMagicDefence);
		setBatLuckyDefence(batLuckyDefence);
		setBatTough(batTough);
		setBatDamageRate(batDamageRate);
		setBatDamageResistRate(batDamageResistRate);
		setBatCriticalDamageRate(batCriticalDamageRate);
		setBatAttackRate(batAttackRate);
		setBatDefenceRate(batDefenceRate);
		setBatHitRateRate(batHitRateRate);
		setBatDodgeRate(batDodgeRate);
		setBatAddCriticalRate(batAddCriticalRate);
		setBatDerateCriticalRate(batDerateCriticalRate);
		setBatReboundDamage(batReboundDamage);

		if (!wasRobot()) {
			calBattlePower();
		}
		setBatHp(Math.min(batMaxHp, batHp));
		setBatMp(Math.min(batMaxHp, batMp));	
		updateBatToFighter();
		sendUpdateRoleBatProps(true);
	}

	public void calculateRobotBat(List<List<Integer>> listBatExp) {
		List<List<List<Integer>>> finalTotalBatExpress = new ArrayList<List<List<Integer>>>();
		finalTotalBatExpress.add(listBatExp);

		int batMeleeAttack = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttack, finalTotalBatExpress);
		int batMagicAttack = ExpressUtil.findExpressEffectType(
				RoleType.batMagicAttack, finalTotalBatExpress);
		int batMeleeDefence = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefence, finalTotalBatExpress);
		int batMagicDefence = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefence, finalTotalBatExpress);
		int batMeleeAttackMin = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttackMin, finalTotalBatExpress);
		int batMagicAttackMin = ExpressUtil.findExpressEffectType(
				RoleType.batMagicAttackMin, finalTotalBatExpress);
		int batMeleeDefenceMin = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefenceMin, finalTotalBatExpress);
		int batMagicDefenceMin = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefenceMin, finalTotalBatExpress);
		int batMeleeAttackMax = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeAttackMax, finalTotalBatExpress);
		int batMagicAttackMax = ExpressUtil.findExpressEffectType(
				RoleType.batMagicAttackMax, finalTotalBatExpress);
		int batMeleeDefenceMax = ExpressUtil.findExpressEffectType(
				RoleType.batMeleeDefenceMax, finalTotalBatExpress);
		int batMagicDefenceMax = ExpressUtil.findExpressEffectType(
				RoleType.batMagicDefenceMax, finalTotalBatExpress);
		int batMaxHp = ExpressUtil.findExpressEffectType(RoleType.batMaxHp,
				finalTotalBatExpress);
		int batMaxMp = ExpressUtil.findExpressEffectType(RoleType.batMaxMp,
				finalTotalBatExpress);
		int batCritical = ExpressUtil.findExpressEffectType(
				RoleType.batCritical, finalTotalBatExpress);
		int batHpReg = ExpressUtil.findExpressEffectType(RoleType.batHpReg,
				finalTotalBatExpress);
		int batMpReg = ExpressUtil.findExpressEffectType(RoleType.batMpReg,
				finalTotalBatExpress);
		int batMovement = ExpressUtil.findExpressEffectType(
				RoleType.batMovement, finalTotalBatExpress);
		int batDodge = ExpressUtil.findExpressEffectType(RoleType.batDodge,
				finalTotalBatExpress);
		int batLuckyAttack = ExpressUtil.findExpressEffectType(
				RoleType.batLuckyAttack, finalTotalBatExpress);
		int batHitRate = ExpressUtil.findExpressEffectType(RoleType.batHitRate,
				finalTotalBatExpress);
		int batLuckyDefence = ExpressUtil.findExpressEffectType(
				RoleType.batLuckyDefence, finalTotalBatExpress);
		int batTough = ExpressUtil.findExpressEffectType(RoleType.batTough,
				finalTotalBatExpress);
		int batDamageRate = ExpressUtil.findExpressEffectType(
				RoleType.batDamageRate, finalTotalBatExpress);
		int batDamageResistRate = ExpressUtil.findExpressEffectType(
				RoleType.batDamageResistRate, finalTotalBatExpress);
		int batCriticalDamageRate = ExpressUtil.findExpressEffectType(
				RoleType.batCriticalDamageRate, finalTotalBatExpress);
		int batAttackRate = ExpressUtil.findExpressEffectType(
				RoleType.batAttackRate, finalTotalBatExpress);
		int batDefenceRate = ExpressUtil.findExpressEffectType(
				RoleType.batDefenceRate, finalTotalBatExpress);
		int batHitRateRate = ExpressUtil.findExpressEffectType(
				RoleType.batHitRateRate, finalTotalBatExpress);
		int batDodgeRate = ExpressUtil.findExpressEffectType(
				RoleType.batDodgeRate, finalTotalBatExpress);
		int batAddCriticalRate = ExpressUtil.findExpressEffectType(
				RoleType.batAddCriticalRate, finalTotalBatExpress);
		int batDerateCriticalRate = ExpressUtil.findExpressEffectType(
				RoleType.batDerateCriticalRate, finalTotalBatExpress);
		int batReboundDamage = ExpressUtil.findExpressEffectType(
				RoleType.batReboundDamage, finalTotalBatExpress);

		// 百分比属性修正
		batMeleeAttack = batMeleeAttack + batMeleeAttack * batAttackRate / 1000;
		batMagicAttack = batMagicAttack + batMagicAttack * batAttackRate / 1000;
		batMeleeDefence = batMeleeDefence + batMeleeDefence * batDefenceRate
				/ 1000;
		batMagicDefence = batMagicDefence + batMagicDefence * batDefenceRate
				/ 1000;
		batMeleeAttackMin = batMeleeAttackMin + batMeleeAttackMin
				* batAttackRate / 1000;
		batMagicAttackMin = batMagicAttackMin + batMagicAttackMin
				* batAttackRate / 1000;
		batMeleeDefenceMin = batMeleeDefenceMin + batMeleeDefenceMin
				* batDefenceRate / 1000;
		batMagicDefenceMin = batMagicDefenceMin + batMagicDefenceMin
				* batDefenceRate / 1000;
		batMeleeAttackMax = batMeleeAttackMax + batMeleeAttackMax
				* batAttackRate / 1000;
		batMagicAttackMax = batMagicAttackMax + batMagicAttackMax
				* batAttackRate / 1000;
		batMeleeDefenceMax = batMeleeDefenceMax + batMeleeDefenceMax
				* batDefenceRate / 1000;
		batMagicDefenceMax = batMagicDefenceMax + batMagicDefenceMax
				* batDefenceRate / 1000;

		// 最后赋值
		setBatMeleeAttack(batMeleeAttack);
		setBatMagicAttack(batMagicAttack);
		setBatMeleeAttackMin(batMeleeAttackMin);
		setBatMagicAttackMin(batMagicAttackMin);
		setBatMeleeDefenceMin(batMeleeDefenceMin);
		setBatMagicDefenceMin(batMagicDefenceMin);
		setBatMeleeAttackMax(batMeleeAttackMax);
		setBatMagicAttackMax(batMagicAttackMax);
		setBatMeleeDefenceMax(batMeleeDefenceMax);
		setBatMagicDefenceMax(batMagicDefenceMax);
		setBatMaxHp(batMaxHp);
		setBatMaxMp(batMaxMp);
		setBatCritical(batCritical);
		setBatHpReg(batHpReg);
		setBatMpReg(batMpReg);
		setBatMovement(batMovement);
		setBatDodge(batDodge);
		setBatLuckyAttack(batLuckyAttack);
		setBatHitRate(batHitRate);
		setBatMeleeDefence(batMeleeDefence);
		setBatMagicDefence(batMagicDefence);
		setBatLuckyDefence(batLuckyDefence);
		setBatTough(batTough);
		setBatDamageRate(batDamageRate);
		setBatDamageResistRate(batDamageResistRate);
		setBatCriticalDamageRate(batCriticalDamageRate);
		setBatAttackRate(batAttackRate);
		setBatDefenceRate(batDefenceRate);
		setBatHitRateRate(batHitRateRate);
		setBatDodgeRate(batDodgeRate);
		setBatAddCriticalRate(batAddCriticalRate);
		setBatDerateCriticalRate(batDerateCriticalRate);
		setBatReboundDamage(batReboundDamage);
		setBatHp(Math.min(batMaxHp, batHp));
		setBatMp(Math.min(batMaxHp, batMp));
	}

	private List<List<Integer>> loadSustainKillAtt() {
		BuffPo buffPo = this.sustainKillBuff();
		if (buffPo != null) {
			BufferEffetVo beVo = buffPo.bufferEffetVos.get(0);
			if (buffPo != null
					&& buffPo.getDurationValexp() == CopySceneType.EIKY_TIME
					&& beVo.buffType == BuffType.BUFF_EFFECT_9) {
				List<Integer> attBuff = new ArrayList<Integer>();
				attBuff.add(beVo.buffType);
				attBuff.add(beVo.param1);
				List<List<Integer>> sustainKillAtt = new ArrayList<List<Integer>>();
				sustainKillAtt.add(attBuff);
				return sustainKillAtt;
			}
		}
		return null;
	}

	/**
	 * 获取当前连斩BUFF
	 * 
	 * @return
	 */
	public BuffPo sustainKillBuff() {
		int numKey = 0;
		Set<Entry<Integer, BuffPo>> sets = GlobalCache.sustainKillMap
				.entrySet();
		for (Entry<Integer, BuffPo> set : sets) {
			Integer key = set.getKey();
			if (key > numKey && key <= sustainKillVo.killNum)
				numKey = key;
		}
		if (numKey > 0)
			return GlobalCache.sustainKillMap.get(numKey);
		return null;
	}

	/**
	 * 获取宠物天赋加成(角色)
	 * 
	 * @param effectType
	 * @return
	 */
	@JSONField(serialize = false)
	public List<List<Integer>> loadPetTalentAttackRole() {
		if (null != listPetTalent && listPetTalent.size() > 0) {
			List<List<Integer>> petTalentAttack = new ArrayList<List<Integer>>();
			for (Integer petTalentId : listPetTalent) {
				PetTalentPo petTalent = GlobalCache.petTalentIdMap
						.get(petTalentId);
				if (petTalent != null && petTalent.playerAttrList.size() > 0)
					petTalentAttack.add(petTalent.playerAttrList);
			}
			return petTalentAttack;
		}
		return null;
	}

	/**
	 * 获取宠物天赋加成（宠物）
	 * 
	 * @param effectType
	 * @return
	 */
	@JSONField(serialize = false)
	public List<List<Integer>> loadPetTalentAttackPet() {
		if (null != listPetTalent && listPetTalent.size() > 0) {
			List<List<Integer>> petTalentAttack = new ArrayList<List<Integer>>();
			for (Integer petTalentId : listPetTalent) {
				PetTalentPo petTalent = GlobalCache.petTalentIdMap
						.get(petTalentId);
				if (petTalent != null && petTalent.petAttrList.size() > 0)
					petTalentAttack.add(petTalent.petAttrList);
			}
			return petTalentAttack;
		}
		return null;
	}

	/**
	 * 方法功能：根据部位获得战力加成(强化、升星、洗练、宝石) 更新时间:2015-7-13, 作者:peter
	 * 
	 * @return
	 */
	private List<List<Integer>> fetchCalculateBySlot(Integer slotId) {

		EqpPo eqpPo = EqpPo.findEntity(fetchEquipIdBySlot(slotId));
		if (eqpPo == null) {
			return null;
		}
		List<List<Integer>> returnList = new ArrayList<List<Integer>>();
		ItemPo itemPo = eqpPo.itemPo();
		// System.out.println("item===" + itemPo.listEqpBatAttrExp);
		List<List<Integer>> listEqpBatAttrExp = itemPo.listEqpBatAttrExp;

		// System.out.println(itemPo.getId()+"装备表达式：" +listEqpBatAttrExp);
		int intensifyPercent = 100;
		int starPercent = 100;
		if (eqpPo.getPowerLv() != null && eqpPo.getPowerLv().intValue() != 0) {
			intensifyPercent += XmlCache.xmlFiles.constantFile.equipPower.power.get(eqpPo.getPowerLv().intValue() - 1).value;
			powerSuitPlusMap.put(slotId, eqpPo.getPowerLv());
		}
		SlotSoulVo slotSoulVo = findSlotSoul(slotId);
		if (slotSoulVo.powerLv != null && slotSoulVo.powerLv.intValue() != 0) {
			starPercent += XmlCache.xmlFiles.constantFile.soulSlot.soulPower.starUpgrade.get(slotSoulVo.powerLv.intValue() - 1).attrRate;
			starSuitPlusMap.put(slotId, slotSoulVo.powerLv);
		}
		
		
		double sumPercent = (1d * intensifyPercent * starPercent) / 10000;
		// System.out.println("部位："+slotId+"; 加成百分比："+sumPercent);
		// System.out.println("加成之前：" +listEqpBatAttrExp);
		for (int i = 0; i < listEqpBatAttrExp.size(); i++) {
			List<Integer> list = listEqpBatAttrExp.get(i);
			int finalyType = list.get(0).intValue();
			int finalyValue = DoubleUtil.toUpInt(1d * list.get(1).intValue()* sumPercent);
			List<Integer> list2 = new ArrayList<Integer>();
			list2.add(finalyType);
			list2.add(finalyValue);
			returnList.add(list2);
		}
		// System.out.println("加成之后：" +listEqpBatAttrExp);
		// System.out.println("=="+returnList);
		if (openSystemArrayList.contains(TaskType.OPEN_SYSTEM_EXTRACT)) {
			if (slotSoulVo.extract1BatType != null && slotSoulVo.extract1BatType.intValue() != 0) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(slotSoulVo.extract1BatType);
				list.add(slotSoulVo.extract1BatVal);
				washSuitPlusMap.put(slotId+"_1", slotSoulVo.extract1Star);
				returnList.add(list);
			}
			if (slotSoulVo.extract2BatType != null && slotSoulVo.extract2BatType.intValue() != 0) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(slotSoulVo.extract2BatType);
				list.add(slotSoulVo.extract2BatVal);
				washSuitPlusMap.put(slotId+"_2", slotSoulVo.extract2Star);
				returnList.add(list);
			}
			if (slotSoulVo.extract3BatType != null && slotSoulVo.extract3BatType.intValue() != 0) {
				List<Integer> list = new ArrayList<Integer>();
				list.add(slotSoulVo.extract3BatType);
				list.add(slotSoulVo.extract3BatVal);
				washSuitPlusMap.put(slotId+"_3", slotSoulVo.extract3Star);
				returnList.add(list);
			}
		}

		if (slotSoulVo.gem1Id != null && slotSoulVo.gem1Id.intValue() != 0) {
			ItemPo gemItemPo = ItemPo.findEntity(slotSoulVo.gem1Id);
			List<List<Integer>> listGemBatAttrExp = gemItemPo.listEqpBatAttrExp;
			// System.out.println("宝石1 = " +listGemBatAttrExp );
			returnList.addAll(listGemBatAttrExp);
		}

		if (slotSoulVo.gem2Id != null && slotSoulVo.gem2Id.intValue() != 0) {
			ItemPo gemItemPo = ItemPo.findEntity(slotSoulVo.gem2Id);
			List<List<Integer>> listGemBatAttrExp = gemItemPo.listEqpBatAttrExp;
			// System.out.println("宝石2 = " +listGemBatAttrExp );
			returnList.addAll(listGemBatAttrExp);
		}

		if (slotSoulVo.gem3Id != null && slotSoulVo.gem3Id.intValue() != 0) {
			ItemPo gemItemPo = ItemPo.findEntity(slotSoulVo.gem3Id);
			List<List<Integer>> listGemBatAttrExp = gemItemPo.listEqpBatAttrExp;
			// System.out.println("宝石3 = " +listGemBatAttrExp );
			returnList.addAll(listGemBatAttrExp);
		}

		if (slotSoulVo.gem4Id != null && slotSoulVo.gem4Id.intValue() != 0) {
			ItemPo gemItemPo = ItemPo.findEntity(slotSoulVo.gem4Id);
			List<List<Integer>> listGemBatAttrExp = gemItemPo.listEqpBatAttrExp;
			// System.out.println("宝石4 = " +listGemBatAttrExp );
			returnList.addAll(listGemBatAttrExp);
		}
		// 装备随机附加属性
		returnList.addAll(eqpPo.attachList);
		return returnList;
	}

	private void updateBatToFighter() {
		if (fighter != null) {
			Fighter mover = fighter;
			if (mover != null) {
				int originalHpMax = mover.getBatHp();
				int originalMpMax = mover.batMp;
				int originalLv = mover.lv;
				mover.loadBatFromRolePo();
				int newHpMax = mover.getBatHp();
				int newMpMax = mover.batMp;
				int newLv = mover.lv;
				if (originalHpMax != newHpMax || originalMpMax != newMpMax
						|| originalLv != newLv) {
					mover.sendAttrChangeInfor();
				}
			}
		}
	}

	public void updateBatToFighterPet() {
		if (fighterPet != null) {
			Fighter mover = fighterPet;
			RpetPo rpetPo = RpetPo.findEntity(getRpetFighterId());
			if (rpetPo != null) {
				mover.loadBatFromRpetPo(this, rpetPo);
				mover.sendAttrChangeInfor();
			}
		}
	}

	/**
	 * 获得总战力
	 * 
	 * @return
	 */
	public Integer calBattlePower() {
		Integer[] batMeleeArray = (Integer[]) fetchBatMeleeArray();
		double[] batMeleeValue = null;
		if (getCareer().intValue() == 1 || getCareer().intValue() == 2) {
			batMeleeValue = RoleType.batMeleeWarriorOrArcher;
		} else {
			batMeleeValue = RoleType.batMeleeMaster;
		}
		double battlePower = 0;
		if (batMeleeValue == null) {
			return 0;
		}
		for (int i = 0; i < batMeleeArray.length; i++) {
			if (batMeleeValue[i] != 0) {
				battlePower += (batMeleeArray[i] * batMeleeValue[i]);
			}
		}
		int power = (int) battlePower;
		// 战力有变化就推送
		if (getBattlePower().intValue() != power) {
			// System.out.println("变化前"+getBattlePower().intValue()
			// +"==变化后"+power);
			setBattlePower(power);
			liveActivityRankPrower();
			sendUpdateCalculate();
			sendUpdateRoleBatProps(true);
		}
		return power;
	}

	public Object[] fetchBatMeleeArray() {
		Integer[] batMeleeArray = new Integer[] { getBatMeleeAttack(),
				getBatMagicAttack(),getBatMeleeDefence(),
				getBatMagicDefence(), getBatMeleeAttackMin(),
				getBatMeleeAttackMax(), getBatMeleeDefenceMin(),
				getBatMeleeDefenceMax(), getBatMagicAttackMin(),
				getBatMagicAttackMax(), getBatMagicDefenceMin(),
				getBatMagicDefenceMax(), getBatMaxHp(), getBatMaxMp(),
				getBatHpReg(), getBatMpReg(), getBatMovement(),
				getBatCriticalDamageRate(), getBatLuckyAttack(),
				getBatLuckyDefence(), getBatHitRate(), getBatDodge(),
				getBatCritical(), getBatTough(), getBatDamageRate(),
				getBatDamageResistRate(), getBatAttackRate(),
				getBatDefenceRate() };
		return batMeleeArray;
	}

	public Boolean pickTreasure(Treasure treasure) {
		ChatService chatService = ChatService.instance();
		if (treasure == null || treasure.dropPo == null) {
			return false;
		}

		MapRoom mapRoom = MapRoom.findStage(this.getRoomId());
		if (mapRoom == null) {
			mapRoom = MapRoom.findStage(this.getStaticRoomId());
		}
		String remark = GlobalCache.fetchLanguageMap("key2410");
		if (mapRoom != null) {
			ScenePo scenePo = ScenePo.findEntity(mapRoom.sceneId);
			remark = scenePo.getName();
		}
		int dropItemId=treasure.dropPo.getItemId();
		if(treasure.dropPo.getType()==1){
			EqpPo eqpPo=EqpPo.findEntity(treasure.dropPo.getItemId());
			dropItemId=eqpPo.getItemId();
		}
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, dropItemId,treasure.dropPo.getNum(), GlobalCache.fetchLanguageMap("key2411")+remark, "");
		if(teamMemberVo != null && teamMemberVo.teamVo !=null &&  treasure.dropPo!= null &&  ItemPo.findEntity(treasure.dropPo.getItemId()) != null){
			ItemPo itemPo = ItemPo.findEntity(treasure.dropPo.getItemId());
			if(itemPo.getId().intValue() != 300004002 && itemPo.getId().intValue() != 300004003 &&itemPo.getQuality()!=null && itemPo.getQuality().intValue()!=1){
				String str = GlobalCache.fetchLanguageMap("key2592");
				int roll = IntUtil.getRandomInt(50, 100);
				String sb = MessageFormat.format(str, name , roll,ColourType.fetchColourByQuality(itemPo.getQuality()),itemPo.getName());
				chatService.sendTeam(sb, teamMemberVo.teamVo.id);
			}
		}
		int itemId = treasure.dropPo.getItemId();
		if(treasure.dropPo.getType()==ItemType.ITEM_BIG_TYPE_EQUIP){
			EqpPo eqpPo = EqpPo.findEntity(treasure.dropPo.getItemId());
			itemId = eqpPo.getItemId();
		}
		sendChatByItem(0, itemId, ChatType.CHAT_DROP_TYPE_KILL_MONSTER);
		return awardDrop(treasure.dropPo, treasure.dropPo.getBind());

		// int exp=treasure.treaureShowType*100;
		// adjustExp(exp);
		// sendUpdateExpAndLv();
		// chatService.sendSystemWorldChat(name+"吃了"+treasure.treaureShowType+"级经验果实获"+exp+"点经验！");
		// }
		// else if(treasure.treaureShowType<=10){
		// int val=fighter.makeHpChange(fighter,
		// fighter.batMaxHp*(treasure.treaureShowType-5)/5);
		// BattleMsgUtil.abroadHpChange(fighter, val,1,1);
		// }
		// else{
		// int val=fighter.makeMpChange(fighter,
		// fighter.batMaxMp*(treasure.treaureShowType-10)/5);
		// BattleMsgUtil.abroadMpChange(fighter, val);
		// }

		// if(treasure.treaureType==Treasure.TREASURE_TYPE_1 ||
		// treasure.treaureType==Treasure.TREASURE_TYPE_2){
		// adjustCoin(treasure.treasureCoin);
		// sendUpdateCoin();
		// sendTips("获得金币"+treasure.treasureCoin);
		// }
		// else{
		// addItemToPack(treasure.treasureCoin, 1);
		// ItemPo itemPo= findItemPo(treasure.treasureCoin);
		// sendTips("开宝箱你获得了"+itemPo.getName());
		// sendUpdatePack();
		// }

	}

	public Boolean awardDrop(DropPo dropPo, int bind) {
		if (dropPo != null) {
			if (dropPo.getType() == ItemType.ITEM_BIG_TYPE_ITEM) {
				Integer goldPercent = GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_GOLD);
				int itemNum = dropPo.getNum();
				int addNum = 0;
				if(dropPo.getItemId().intValue() ==300004002 || dropPo.getItemId().intValue()==300004003 ){
					if(goldPercent.intValue() !=0){
						addNum =(dropPo.getNum()*goldPercent/100);
						addNum=Math.min(1000, addNum);						
					}
					itemNum+=addNum;
				}
				return addItem(dropPo.getItemId(), itemNum, bind);
			}
			if (dropPo.getType() == ItemType.ITEM_BIG_TYPE_EQUIP) {
				addEquip(dropPo.getItemId(), bind);
				return true;
			}
		}
		return true;
	}

	private void sendTips(String msg) {
		singleRole("ServerRemoting.systemMsg", new Object[] { msg, 2 },true);
	}

	private ItemPo findItemPo(Integer itemId) {
		return ItemPo.findEntity(itemId);
	}

	/**
	 * 充钻石
	 * 
	 * @param count
	 */
	public void chargeDiamond(int count) {
		liveActivityCharge(count);
		liveActivityRankCharge(count);
	}

	public void liveActivityLogin() {
		int val = 0;
		if(activityLoginState.intValue()==0){
			for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
				if (liveActivityPo.getType() == LiveActivityType.LiveActivity_LOGIN) {
					val = addProgressToRoleLiveActivity(liveActivityPo, 1);
					setActivityLoginState(1);
				} 
			}			
		}
	}
	
	public void liveActivityLvReach() {
		int val = 0;
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if (liveActivityPo.getType() == LiveActivityType.LiveActivity_LV_REACH) {
				val = updateProgressToRoleLiveActivity(liveActivityPo, lv);
			} 
		}
	}
	
	/**
	 * 检查充值对运营活动的影响
	 * 1.每日充值 2. 累计充值
	 * @param count
	 */
	private void liveActivityCharge(int count) {
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if (liveActivityPo.getType() == LiveActivityType.LiveActivityTOTAL_CHARGE
					|| liveActivityPo.getType() == LiveActivityType.LiveActivityDAILY_CHARGE) {
				int val = 0;
				val = addProgressToRoleLiveActivity(liveActivityPo, count);
			}
		}
		sendUpdateRoleActivitysList();
	}
	
	/**
	 * 检查充值对运营活动的影响
	 *  5.充值排行
	 * @param count
	 */
	private void liveActivityRankCharge(int count){
		
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if (liveActivityPo.getType() == LiveActivityType.LiveActivity_CHARGE_RANK) {
				int var = 0;
				var =adjustAddValueLiveActivity(liveActivityPo, count);
				int rank = liveActivityPo.addRoleRankVal(var, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}

	/**
	 * 检查充值对运营活动的影响
	 * 3.累计消费 
	 * @param count
	 */
	private void liveActivityConsume(int count) {
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if (liveActivityPo.getType() == LiveActivityType.LiveActivityTOTAL_COST) {
				int val = 0;
				val = addProgressToRoleLiveActivity(liveActivityPo, count);
			}
		}
		sendUpdateRoleActivitysList();
	}
	
	/**
	 * 检查充值对运营活动的影响
	 * 6消费排行
	 * @param count
	 */
	private void liveActivityRankConsume(int count){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if (liveActivityPo.getType() == LiveActivityType.LiveActivity_COST_RANK) {
				int val = 0;
				val = adjustAddValueLiveActivity(liveActivityPo, count);
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	
	/**
	 * 检查充值对运营活动的影响
	 * 8. 战力排行
	 */
	private void liveActivityRankPrower(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_PROWER){
				int val = 0;
				val=this.getBattlePower();
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	/**
	 * 检查充值对运营活动的影响
	 *  9. 等级排行 
	 */
	private void liveActivityRankLv(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_ROLE_LV){
				int val = 0;
				val=this.lv;
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	
	/**
	 * 检查充值对运营活动的影响
	 *  11. 竞技场排行
	 */
	private void liveActivityRankArena(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_ARENA_RANK){
				int val = 0;
				RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(getId());
				if(me==null){
					continue;
				}
				val=me.getArenaRank();
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	
	/**
	 * 检查充值对运营活动的影响
	 *  12. 成就排行
	 */
	private void liveActivityRankAchieve(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_ACHIEVE){
				int val = 0;
				val=achievementSum;
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	/**
	 * 检查充值对运营活动的影响
	 *  13. 声望排行
	 */
	private void liveActivityRankPrestige(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_PRESTIGE){
				int val = 0;
				val=prestigeTotal;
				int rank = liveActivityPo.addRoleRankVal(val, this);
				updateProgressToRoleLiveActivity(liveActivityPo, rank);
			}
		}
	}
	
	/**
	 * 检查充值对运营活动的影响
	 *  14. 声望排行
	 */
	public void liveActivityRankPet(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_PET_RANK){
				int val = 0;
//				RpetPo rpetPo = RpetPo.findEntity(getRpetFighterId());
				if(fighterPet!= null){
					val=fighterPet.calBattlePower();
					int rank = liveActivityPo.addRoleRankVal(val, this);
					updateProgressToRoleLiveActivity(liveActivityPo, rank);
				}
			}
		}
		if(fighterPet!= null){
			setPetPrower(fighterPet.calBattlePower());			
		}
	}
	


	/**
	 * 更新运营活动进度
	 * 
	 * @param liveActivityPo
	 * @param rank
	 */
	private int updateProgressToRoleLiveActivity(LiveActivityPo liveActivityPo,int rank) {
		int val = 0;
		boolean flag = false;
		for (RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys) {
			if (roleLiveActivityVo.liveActivityId == liveActivityPo.getId()) {
				val = roleLiveActivityVo.updateProgress(rank);
				flag =true;
				break;
			}
		}
		if(!flag){
			RoleLiveActivityVo roleLiveActivityVo = new RoleLiveActivityVo();
			roleLiveActivityVo.updateProgress(rank);
			roleLiveActivityVo.liveActivityId = liveActivityPo.getId();
			listRoleLiveActivitys.add(roleLiveActivityVo);			
		}
		sendUpdateRoleActivitysList();
		return val;
	}

	/**
	 * 增加运营活动完成进度
	 * 
	 * @param liveActivityPo
	 * @param count
	 */
	private int addProgressToRoleLiveActivity(LiveActivityPo liveActivityPo,int count) {
		for (RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys) {
			if (roleLiveActivityVo.liveActivityId == liveActivityPo.getId()) {
				return roleLiveActivityVo.addProgress(count);
			}
		}
		return 0;
	}

	/**
	 * 增加运营活动排行(累计充值、累计消费)的值
	 * @param liveActivityPo
	 * @param count
	 * @return
	 */
	private int adjustAddValueLiveActivity(LiveActivityPo liveActivityPo,int count){
		for(RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys){
			if (roleLiveActivityVo.liveActivityId == liveActivityPo.getId()) {
				return roleLiveActivityVo.adjustAddValue(count);
			}
		}
		return 0;
	}
	
	
	/**
	 * 根据类型调整金币、绑金、钻石、绑钻、技能点、公会荣誉、魔魂
	 * 
	 * @param adjustGold
	 * @param type
	 */
	public void adjustNumberByType(Integer adjustNumber, int type) {
		// System.out.println("adjustNumber=="+adjustNumber+"; type=="+type);
		if (type == RoleType.RESOURCE_GOLD) {
			adjustGold(adjustNumber);
		} else if (type == RoleType.RESOURCE_BIND_GOLD) {
			adjustBindGold(adjustNumber);
		} else if (type == RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD) {
			int needGold = adjustBindGold(adjustNumber);
			if (needGold < 0) {
				adjustGold(needGold);
			}
		} else if (type == RoleType.RESOURCE_DIAMOND) {
			adjustDiamond(adjustNumber);
		} else if (type == RoleType.RESOURCE_BIND_DIAMOND) {
			adjustBindDiamond(adjustNumber);
		} else if (type == RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND) {
			int needDiamond = adjustBindDiamond(adjustNumber);
			if (needDiamond < 0) {
				adjustDiamond(needDiamond);
			}
		} else if (type == RoleType.RESOURCE_SKILL_POINT) {
			adjustSkillPoint(adjustNumber);
		} else if (type == RoleType.RESOURCE_GUILD_HONOR) {
			adjustGuildHonor(adjustNumber);
		} else if (type == RoleType.RESOURCE_PETSOUL) {
			adjustPetSoul(adjustNumber);
		}else if (type == RoleType.RESOURCE_WAREHOUSE_GOLD) {
			adjustWareHouseGold(adjustNumber);
		}else if (type == RoleType.RESOURCE_WAREHOUSE_BIND_GOLD) {
			adjustWarehouseBindGold(adjustNumber);
		}else if(type == RoleType.RESOURCE_PRESTIGE){
			adjustPrestige(adjustNumber);
		}else if(type == RoleType.RESOURCE_GAMSTONE){
			adjustGamstoneFragment(adjustNumber);
		}else if(type == RoleType.RESOURCE_SOUL){
			adjustSoul(adjustNumber);
		}
		else {
			ExceptionUtil.throwNotFinishedException("");
		}
	}

	/**
	 * 直接使用 类型： 优先绑金在消耗金币
	 * 
	 * @param adjustGold
	 */
	public void adjustPriorityBindgoldThenGold(Integer adjustGold) {
		adjustNumberByType(adjustGold,
				RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD);
	}

	/**
	 * 直接使用 类型： 优先绑钻在消耗钻石
	 * 
	 * @param adjustDiamond
	 */
	public void adjustPriorityBindDiamondThenDiamond(Integer adjustDiamond) {
		adjustNumberByType(adjustDiamond,
				RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND);
	}

	/**
	 * 调整公会荣誉
	 * 
	 * @param adjustNumber
	 */
	public void adjustPublicGuildHonor(Integer adjustNumber) {
		adjustNumberByType(adjustNumber, RoleType.RESOURCE_GUILD_HONOR);
	}

	/**
	 * 调整每日公会捐献金币次数
	 * 
	 * @param adjustNumber
	 */
	public void adjustDailyGuildContributeGoldCount(Integer adjustNumber) {
		setDailyGuildContributeGoldCount(dailyGuildContributeGoldCount
				.intValue() + adjustNumber.intValue());
	}

	/**
	 * 调整每日公会捐献道具次数
	 * 
	 * @param adjustNumber
	 */
	public void adjustDailyGuildContributeItemCount(Integer adjustNumber) {
		setDailyGuildContributeItemCount(dailyGuildContributeItemCount
				.intValue() + adjustNumber.intValue());
	}

	/**
	 * 调整每日角色获得个人公会战功
	 * 
	 * @param adjustNumber
	 */
	public void adjustGuildTodayContributed(Integer adjustNumber) {
		setGuildTodayContributed(guildTodayContributed.intValue()
				+ adjustNumber.intValue());
	}

	/**
	 * 调整金币
	 * 
	 * @param adjustGold
	 */
	private void adjustGold(Integer adjustGold) {
		if (adjustGold == null) {
			return;
		}
		int remainGold = gold.intValue() + adjustGold.intValue();
		if (remainGold > 0) {
			setGold(remainGold);
		} else {
			setGold(0);
		}
	}
	
	/**
	 * 调整仓库金币
	 * @param adjustGold
	 */
	private void adjustWareHouseGold(Integer adjustGold) {
		if (adjustGold == null) {
			return;
		}
		int remainGold = warehouseGold.intValue() + adjustGold.intValue();
		if (remainGold > 0) {
			setWarehouseGold(remainGold);
		} else {
			setWarehouseGold(0);
		}
	}

	/**
	 * 调整绑金
	 * 
	 * @param adjustBindGold
	 * @return
	 */
	private int adjustBindGold(Integer adjustBindGold) {
		if (adjustBindGold == null) {
			return 0;
		}
		int remainGold = bindGold.intValue() + adjustBindGold.intValue();
		if (remainGold > 0) {
			setBindGold(remainGold);
		} else {
			setBindGold(0);
		}
		return remainGold;
	}
	
	/**
	 * 调整仓库绑金
	 * @param adjustBindGold
	 * @return
	 */
	private int adjustWarehouseBindGold(Integer adjustBindGold) {
		if (adjustBindGold == null) {
			return 0;
		}
		int remainGold = warehouseBindGold.intValue() + adjustBindGold.intValue();
		if (remainGold > 0) {
			setWarehouseBindGold(remainGold);
		} else {
			setWarehouseBindGold(0);
		}
		return remainGold;
	}

	/**
	 * 调整钻石
	 * 
	 * @param addDiamond
	 */
	private void adjustDiamond(Integer addDiamond) {
		if (addDiamond == null) {
			return;
		}
		if (addDiamond < 0) {
			liveActivityConsume(Math.abs(addDiamond));
			liveActivityRankConsume(Math.abs(addDiamond));
		}

		UserPo userPo = UserPo.findEntity(getUserId());
		int remainDiamond = userPo.getDiamond().intValue() + addDiamond.intValue();
		if (remainDiamond > 0) {
			userPo.setDiamond(remainDiamond);
		} else {
			userPo.setDiamond(0);
		}
	}
	
	/**
	 * 消耗钻石
	 * @param consumeDiamond
	 */
	public void consumeDiamond(int consumeDiamond){
		if(consumeDiamond > 0){
			return;
		}
		UserPo userPo = UserPo.findEntity(getUserId());
		int remainDiamond = userPo.getDiamond().intValue() + consumeDiamond;
		if (remainDiamond > 0) {
			userPo.setDiamond(remainDiamond);
		} else {
			userPo.setDiamond(0);
		}
	}
	

	/**
	 * 调整绑钻
	 * 
	 * @param addBindDiamond
	 */
	private int adjustBindDiamond(Integer addBindDiamond) {
		if (addBindDiamond == null) {
			return 0;
		}
		// System.out.println("加的绑钻："+addBindDiamond.intValue());
		int remainDiamond = bindDiamond.intValue() + addBindDiamond.intValue();
		// System.out.println("总绑钻："+remainDiamond);
		if (remainDiamond > 0) {
			setBindDiamond(remainDiamond);
		} else {
			setBindDiamond(0);
		}
		return remainDiamond;
	}

	/**
	 * 调整魔魂
	 * 
	 * @param addPetSoul
	 */
	public void adjustPetSoul(Integer addPetSoul) {
		if (addPetSoul == null) {
			return;
		}
		int remain = petSoul + addPetSoul;
		if (remain > 0) {
			setPetSoul(remain);
		} else {
			setPetSoul(0);
		}
	}

	/**
	 * 调整公会荣誉
	 * 
	 * @param i
	 */
	private void adjustGuildHonor(int i) {
		setGuildHonor(guildHonor + i);
	}

	/**
	 * 调整成就点
	 * 
	 * @param adjustGold
	 */
	private void adjustAchievePoint(Integer adjustAchievePoint) {
		if (adjustAchievePoint == null) {
			return;
		}
		if(adjustAchievePoint.intValue()>0){
			setAchievementSum(getAchievementSum() + adjustAchievePoint);
		}
		int remainAchievePoint = achievePoint.intValue()+ adjustAchievePoint.intValue();
		if (remainAchievePoint > 0) {
			setAchievePoint(remainAchievePoint);
		} else {
			setAchievePoint(0);
		}
		liveActivityRankAchieve();
	}
	
	public void adjustAchievePointPublic(Integer adjustAchievePoint){
		adjustAchievePoint(adjustAchievePoint);
	}

	/**
	 * 调整dota挑战次数
	 * 
	 * @param addNum
	 */
	public void adjustTowerChallengeTimes(int addNum) {
		setTowerTodayChallengeTimes(towerTodayChallengeTimes + addNum);
	}

	/**
	 * 调整dota扫荡次数
	 * 
	 * @param addNum
	 */
	public void adjustTowerWipeOutTimes(int addNum) {
		setTowerTodayWipeOutTimes(towerTodayWipeOutTimes + addNum);
	}

	/**
	 * 调整dota等级
	 * 
	 * @param addNum
	 */
	public void adjustTowerCurrentLv(int addNum) {
		setTowerCurrentLv(towerCurrentLv + addNum);
		if(towerCurrentLv >= 100){
			setTowerCurrentLv(100);
		}
	}

	/**
	 * 今天原地复活次数
	 * 
	 * @param addNum
	 */
	public void adjustResurnowTodayTimes(int addNum) {
		setResurnowTodayTimes(resurnowTodayTimes + addNum);
	}

	/**
	 * 连续复活次数
	 * 
	 * @param addNum
	 */
	public void adjustResurnowContinueTimes(int addNum) {
		setResurnowContinueTimes(resurnowContinueTimes + addNum);
	}
	
	public void adjustResourceSceneTime(int addNum){
		setResourceSceneTime(resourceSceneTime +addNum);
	}
	

	public void adjustExp(Integer adjustExp) {
		// 升级配置表取最大等级
		int maxLv = GlobalCache.maxRoleLv;
		CheckUtil.checkNotBlewZero(adjustExp);
		if (lv.intValue() >= maxLv) {
			return;
		}
		setExp(exp + adjustExp);
		long nextLevelExp = RoleType.ROLE_EXPS[lv - 1];
		int currentLevel = lv;
		CheckcCircleBean checkcCircleBean = new CheckcCircleBean();
		while (exp.intValue() >= nextLevelExp) {
			checkcCircleBean.count();
			levelUp();
			if (lv.intValue() >= maxLv) {
				return;
			}
			nextLevelExp = RoleType.ROLE_EXPS[lv - 1];
		}
		int finalLevel = lv;
		if (finalLevel > currentLevel) {
			checkLevelAwardRecordState();
		}

		if (finalLevel != currentLevel) {
			calculateBat(1);
			if (fighter != null) {
				fighter.makeHpChange(fighter, fighter.batMaxHp);
				fighter.makeMpChange(fighter, fighter.batMaxMp);
				fighter.sendAttrChangeInfor();
			}
		}
	}

	/**
	 * 
	 * 方法功能:升级 更新时间:2011-9-27, 作者:johnny
	 */
	private void levelUp() {
		setExp(exp - RoleType.ROLE_EXPS[lv - 1]);
		LogUtil.writeLog(this, 201, lv, 0, 0, GlobalCache.fetchLanguageMap("key2401"), "");
		setLv(lv + 1);
		activeLevelReachTask(lv);
		// System.out.println("lv == " +lv);
		if (teamMemberVo != null && teamMemberVo.teamVo != null) {
			teamMemberVo.updateTeamMember(this);
			teamMemberVo.teamVo.sendAllMemberUpdateTeamInfor();
		}
		taskConditionProgressReplace(1, TaskType.TASK_TYPE_CONDITION_711,getLv(), null);
		openAwardRetrieve();
		liveActivityRankLv();
		liveActivityLvReach();
		checkOpenSystemArrayListByLv(47, 38);
	}

	/** 调整离线奖励时间 */
	private void adjustOffLineRewardMinutes(Integer minutes) {
		if (minutes == null) {
			return;
		}
		if(offLineRewardMinutes == null){
			setOffLineRewardMinutes(0);
		}
		int remainMinutes = offLineRewardMinutes.intValue() + minutes.intValue();
		if (remainMinutes > 0) {
			if(remainMinutes > 10080){
				remainMinutes = 10080;
			}
			setOffLineRewardMinutes(remainMinutes);
		} else {
			setOffLineRewardMinutes(0);
		}
//		System.out.println("getOffLineRewardMinutes="+getOffLineRewardMinutes());
	}
	
	

	public void addItemList(List<List<Integer>> items, int bind, String logString) {
		for (List<Integer> item : items) {
			ItemPo itemPo = ItemPo.findEntity(item.get(0));
			// if(itemPo==null){
			// ExceptionUtil.throwConfirmParamException("道具编号为："+item.get(1)+" 的道具未配");
			// }
			addItem(item.get(0), item.get(1), bind);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, item.get(0), item.get(1),logString, "");
		}
	}

	public void addItem(List<IdNumberVo> items, int bind, String logString) {
		for (IdNumberVo idNumberVo : items) {
			addItem(idNumberVo.getId(), idNumberVo.getNum(), bind);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, idNumberVo.getId(), idNumberVo.getNum(),logString, "");
		}
	}

	public int fetchFreePackCellLength() {
		PlayItem playTime = fetchPlayItemByType(PlayTimesType.PLAYTIMES_TYPE_420);
		int unlock = 5 * getPackUnlockTimes();
		return playTime.initialTimes +unlock - mainPackItemVosMap.size();
	}
	

	public Integer fetchFreeIndex(Integer timesType, ConcurrentHashMap<String, RolePackItemVo> vosMap) {
		Map<String, RolePackItemVo> themap = null;
		PlayItem playItem = fetchPlayItemByType(timesType);
		int maxIndex = playItem.initialTimes;
		if(timesType==PlayTimesType.PLAYTIMES_TYPE_420){
			maxIndex= playItem.initialTimes+5*packUnlockTimes;
		}

		themap = vosMap;

		int currentIndex = 0;
		for (int i = currentIndex; i < maxIndex; i++) {
			if (!themap.containsKey(String.valueOf(i))) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 
	 * 方法功能:程序内部调用 更新时间:2011-9-19, 作者:johnny
	 * 
	 * @param rolePackItemVo
	 */
	public void singleRolePackItemVoToPack(RolePackItemVo rolePackItemVo, Integer timesType,ConcurrentHashMap<String, RolePackItemVo> vosMap) {

		Integer index = fetchFreeIndex(timesType, vosMap);
		if (index < 0) {
			sendSystemLoseItemMail(rolePackItemVo.getItemId(), rolePackItemVo.getNum(), rolePackItemVo.getBindStatus());
//			if (rolePackItemVo.wasEquip()) {
//				sendToTempPack();
//				// addEqpToTempPack(rolePackItemVo, rolePackItemVo.getNum());
//			} else {
//				// addToTempPack(itemPo, rolePackItemVo.getNum());
//				sendToTempPack();
//			}
		} else {
			rolePackItemVo.setIndex(index);
			vosMap.put(index.toString(), rolePackItemVo);
		}

	}

	/**
	 * 
	 * 方法功能:内部调用,插到格子 更新时间:2011-9-19, 作者:johnny
	 * 
	 * @param itemPo
	 * @param remainNum
	 */
	private void insertToPack(ItemPo itemPo, int remainNum, int bind) {
//		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
		if (!itemPo.wasEquip()) {
			CheckcCircleBean checkcCircleBean = new CheckcCircleBean();
			while (remainNum > 0) {
				checkcCircleBean.count();
				int targetNum = Math.min(remainNum, itemPo.getFoldMax());
				RolePackItemVo rolePackItemVo = new RolePackItemVo();
				rolePackItemVo.setItemId(itemPo.getId());
				rolePackItemVo.setEquipId(0);
				rolePackItemVo.loadEqp();
				rolePackItemVo.setNum(targetNum);
				rolePackItemVo.setBindStatus(bind);
				singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_420, mainPackItemVosMap);
				remainNum = remainNum - targetNum;
			}
		}
		// 装备
		else {
			for (int i = 0; i < remainNum; i++) {
				EqpPo eqpPo = new EqpPo();
				eqpPo.setBindStatus(bind);
				List<Integer> combatUnitIds = new ArrayList<Integer>();
				List<Integer> randoms = new ArrayList<Integer>();
				for (List<Integer> vals : itemPo.listEqpRandomCombatUnitIds) {
					combatUnitIds.add(vals.get(1));
					randoms.add(vals.get(0));
				}

				// eqpPo.setRandomCombatUnitId(RandomUtil.randomIntegerByPecentages(combatUnitIds,
				// combatUnitIds, null));
				eqpPo.setItemId(itemPo.getId());
				eqpPo.setAttach(GlobalCache.getRandomAttach(itemPo.getQuality(), itemPo.getRequireLv()));
				BaseDAO.instance().insert(eqpPo);
				RolePackItemVo rolePackItemVo = new RolePackItemVo();
				rolePackItemVo.setEquipId(eqpPo.getId());
				rolePackItemVo.loadEqp();
				rolePackItemVo.setNum(1);
				rolePackItemVo.setItemId(itemPo.getId());
				rolePackItemVo.setBindStatus(bind);
				singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_420, mainPackItemVosMap);
			}
		}
	}

	// 移除背包道具
	public int removePackItem(Integer itemId, Integer toRemoveNumber,String logString) {
		int wasbind=1;
		CheckService checkService = CheckService.instance();
		checkService.checkExistItemPo(itemId);
		ItemPo itemPo = ItemPo.findEntity(itemId);
		Map<String, RolePackItemVo> packs = mainPackItemVosMap;
		List<String> toRemoveKeys = new ArrayList<String>();
		for (RolePackItemVo rolePackItemVo : packs.values()) {
			if (!rolePackItemVo.wasEquip()
					&& rolePackItemVo.getItemId().intValue() == itemId
							.intValue()) {
				int thisTimeRemoveNumber = Math.min(toRemoveNumber,
						rolePackItemVo.getNum());
				if (rolePackItemVo.getNum() > thisTimeRemoveNumber) {
					rolePackItemVo.setNum(rolePackItemVo.getNum()- thisTimeRemoveNumber);
					wasbind=rolePackItemVo.bindStatus;
				} else if ((rolePackItemVo.getNum() == thisTimeRemoveNumber)) {
					wasbind=rolePackItemVo.bindStatus;
					toRemoveKeys.add(rolePackItemVo.getIndex().toString());
				} else {
					wasbind=rolePackItemVo.bindStatus;
					toRemoveKeys.add(rolePackItemVo.getIndex().toString());
				}
				toRemoveNumber = toRemoveNumber - thisTimeRemoveNumber;
				if (toRemoveNumber == 0) {
					for (String key : toRemoveKeys) {
						packs.remove(key);
					}
					return wasbind;
				}
			}
		}
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, itemId, -toRemoveNumber, logString, "");
		for (String key : toRemoveKeys) {
			packs.remove(key);
		}
		// ExceptionUtil.throwConfirmParamException("道具不存在"+itemId);
		return wasbind;
	}

	public RolePackItemVo fetchRolePackItemByIndex(Integer index) {
		return mainPackItemVosMap.get(index.toString());
	}
	
	public RolePackItemVo fetchRoleWarehousePackItemByIndex(Integer index){
		return warehousePackItemVosMap.get(index.toString());
	}

	public RolePackItemVo removeItemFromMainPack(Integer index, Integer count,String logString, boolean delEquip) {
//		PrintUtil.print("背包移除道具："+index+" ： 数量："+count+ " : logString: "+logString+" 是否删除装备数据："+delEquip);
		RolePackItemVo rolePackItemVo = fetchRolePackItemByIndex(index);
		if(rolePackItemVo==null){
			return null;
		}
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, rolePackItemVo.getItemId(), -count, logString, "");
		if (rolePackItemVo.wasEquip()) {
			mainPackItemVosMap.remove(index.toString());
			if(delEquip){
				int eqpId = rolePackItemVo.getEquipId();
				EqpPo eqpPo = EqpPo.findEntity(eqpId);
				if(eqpPo!=null){
					BaseDAO.instance().remove(eqpPo);
				}
			}
			return null;
		}
		else {
			if (rolePackItemVo.getNum().intValue() > count.intValue()) {
				rolePackItemVo.setNum(rolePackItemVo.getNum().intValue() - count.intValue());
				return rolePackItemVo;
			}
			else {
				mainPackItemVosMap.remove(index.toString());
				return null;
			}
		}
	}
	

	/**
	 * 根据id删除背包物品
	 * @param index
	 */
	public void removeItemFromMainPack(Integer index,String logString, boolean delEquip) {
		removeItemFromMainPack(index, 1,logString, delEquip);
	}
	
	/**
	 * 根据Id删除仓库物品
	 * @param index
	 */
	public void removeWarehouseItemFromWarehouse(Integer index, Integer count){
		RolePackItemVo rolePackItemVo = fetchRoleWarehousePackItemByIndex(index);
		if (rolePackItemVo.wasEquip()) {
			warehousePackItemVosMap.remove(index.toString());
		} else {
			if (rolePackItemVo.getNum().intValue() > count.intValue()) {
				rolePackItemVo.setNum(rolePackItemVo.getNum().intValue() - count.intValue());
			} else {
				warehousePackItemVosMap.remove(index.toString());
			}
		}
	}
	

	public Boolean addItem(Integer itemId, int num, int bind) {
		// System.out.println("addItem() itemId="+itemId +";  num="+num
		// +";  bind="+bind);
		ItemPo itemPo = ItemPo.findEntity(itemId);
		if (career == RoleType.CAREER_WARRIOR) {
			if (GlobalCache.carrrItemWarrior.containsKey(itemId)) {
				itemPo = ItemPo.findEntity(GlobalCache.carrrItemWarrior.get(itemId));
			}
		} else if (career == RoleType.CAREER_RANGER) {
			if (GlobalCache.carrrItemRanger.containsKey(itemId)) {
				itemPo = ItemPo.findEntity(GlobalCache.carrrItemRanger
						.get(itemId));
			}
		} else if (career == RoleType.CAREER_MAGE) {
			if (GlobalCache.carrrItemMage.containsKey(itemId)) {
				itemPo = ItemPo.findEntity(GlobalCache.carrrItemMage
						.get(itemId));
			}
		}
		return addItem(ItemPo.findEntity(itemPo.getId()), num, bind);
	}

	/**
	 * 
	 * 方法功能:增加道具 更新时间:2011-9-19, 作者:johnny
	 * 
	 * @param itemPo
	 * @param num
	 */
	public Boolean addItem(ItemPo itemPo, int num, int bind) {
//		 System.out.println("addItem() itemPo.id="+itemPo.getId() +";  num="+num +";  bind="+bind);
		Boolean fresh = true;
//		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		// chatService.sendSystemWorldChat(name+"获得了装备【"+itemPo.getName()+"】");
		// Map<Integer,RolePackItemVo> theMap = null;

		if (itemPo.getAutoUse() == 1) {
			useItemEffects(itemPo.listItemUseExp, num, bind, true);
			return false;
		}

		if (itemPo.wasEquip()) {
			// 增加装备
			if (fetchFreePackCellLength() == 0) {
				sendSystemLoseItemMail(itemPo.getId(), num, bind);
				return true;
			}
			insertToPack(itemPo, num, bind);
		} else {
			// 增加道具
			int remainNum = num;
			for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
				if (rolePackItemVo.getItemId().intValue() == itemPo.getId()
						.intValue() && rolePackItemVo.getBindStatus() == bind) {
					int space = itemPo.getFoldMax() - rolePackItemVo.getNum();
					int addCount = Math.min(space, remainNum);
					int cellTargetCount = rolePackItemVo.getNum() + addCount;
					if (addCount <= 0) {
						continue;
					}
					remainNum = remainNum - addCount;
					rolePackItemVo.setNum(cellTargetCount);
					if (remainNum == 0) {
						break;
					}
				}
			}
			if (fetchFreePackCellLength() <= 0) {
				sendSystemLoseItemMail(itemPo.getId(), remainNum, bind);
				return true;
			} else {
				insertToPack(itemPo, remainNum, bind);
			}
		}
		taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_703,itemPo.getId(), null);
		return true;
	}
	
	public void sendSystemLoseItemMail(int itemId, int num, int bind){
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		StringBuilder sb = new StringBuilder();
			 sb.append(1);
			 sb.append("|");
			 sb.append(itemId);
			 sb.append("|");
			 sb.append(num);
			 sb.append("|");
			 sb.append(bind);
		mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key2686"), getId(), null, GlobalCache.fetchLanguageMap("key2686"), sb.toString(), MailType.MAIL_TYPE_SYSTEM);
	}
	

	public void doSetEquipIdBySlotType(Integer equipSlot, Integer equipId) {
		if (equipSlot == ItemType.ITEM_CATEGORY_WEAPON) {
			setEquipWeaponId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_ARMOR) {
			setEquipArmorId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_RING) {
			setEquipRingId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_BRACER) {
			setEquipBracerId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_NECKLACE) {
			setEquipNecklaceId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_HELMET) {
			setEquipHelmetId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_SHOE) {
			setEquipShoeId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_PANTS) {
			setEquipPantsId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_BRACELET) {
			setEquipBraceletId(equipId);
		}
		else if (equipSlot == ItemType.ITEM_CATEGORY_BELT) {
			setEquipBeltId(equipId);
		}
		loadEquipFromIds();
		// calculateBat();
	}

	private void loadEquipFromIds() {
		equipWeapon = EqpPo.findEntity(equipWeaponId);
		equipArmor = EqpPo.findEntity(equipArmorId);
		equipRing = EqpPo.findEntity(equipRingId);
		equipBracer = EqpPo.findEntity(equipBracerId);
		equipNecklace = EqpPo.findEntity(equipNecklaceId);
		equipHelmet = EqpPo.findEntity(equipHelmetId);
		equipShoe = EqpPo.findEntity(equipShoeId);
		equipBracelet = EqpPo.findEntity(equipBraceletId);
		equipBelt = EqpPo.findEntity(equipBeltId);
		equipPants = EqpPo.findEntity(equipPantsId);
	}

	/**
	 * 方法功能：根据集合获取装备强化套装加成 更新时间:2015-7-13, 作者:peter
	 */
	public PowerItem fetchPowerSuitPlusInfo(Map<Integer, Integer> map,
			Integer state) {
		if (map.size() == 0) {
			return null;
		}
		List<PowerItem> powerItemList = XmlCache.xmlFiles.constantFile.powerSuitPlus.powerItem;
		PowerItem pi = null;
		for (PowerItem powerItem : powerItemList) {
			int index = 0;
			if (powerItem.id <= (listAdvanceSuitPlus.get(0).powerSuitPlusArriveMaxLevel + 1)) {
				for (Integer value : map.values()) {
					if (value.intValue() >= powerItem.lv) {
						index++;
					}
				}
				if (index >= powerItem.num) {
					pi = powerItem;
				}
			}
		}
		// System.out.println("PowerItem: id="+pi.id
		// +"; lv="+pi.lv+"; num="+pi.num+"; atb="+pi.atb);
		if (pi != null) {
			if (pi.id == (listAdvanceSuitPlus.get(0).powerSuitPlusArriveMaxLevel + 1)) {
				listAdvanceSuitPlus.get(0).adjustPowerSuitPlusArriveMaxLevel(1);
			}
			listAdvanceSuitPlus.get(0).currentPowerSuitPlusLevel = pi.id;
			if (state != null && state.intValue() == 1) {
				sendUpdateAdvanceSuitPlus();
			}
			// System.out.println( "强化装备：id =" +pi.id +";num=" +
			// pi.num+"; lv="+pi.lv+
			// "; atb="+pi.atb+"; 当前最大等级："+listAdvanceSuitPlus.get(0).powerSuitPlusArriveMaxLevel);
		}
		return pi;
	}

	/**
	 * 方法功能：根据集合获取部位升星套装加成 更新时间:2015-7-13, 作者:peter
	 */
	public StarItem fetchStarSuitPlusInfo(Map<Integer, Integer> map,
			Integer state) {
		if (map.size() == 0) {
			return null;
		}
		// System.out.println("map ="+map);
		List<StarItem> starItemList = XmlCache.xmlFiles.constantFile.starSuitPlus.starItem;
		StarItem si = null;
		// 升星强化套装配置
		// System.out.println("listAdvanceSuitPlus = "+listAdvanceSuitPlus);
		for (StarItem starItem : starItemList) {
			int index = 0;
			// 达到过得最大等级
			if (starItem.id <= (listAdvanceSuitPlus.get(0).starSuitPlusArriveMaxLevel + 1)) {
				for (Integer value : map.values()) {
					// key=部位id； value=强化星数
					if (value.intValue() >= starItem.lv) {
						index++;
					}
				}
				if (index >= starItem.num) {
					si = starItem;
				}
			}
		}
		// System.out.println("PowerItem: id="+si.id
		// +"; lv="+si.lv+"; num="+si.num+"; atb="+si.atb);
		if (si != null) {
			if (si.id == (listAdvanceSuitPlus.get(0).starSuitPlusArriveMaxLevel + 1)) {
				listAdvanceSuitPlus.get(0).adjustStarSuitPlusArriveMaxLevel(1);
			}
			listAdvanceSuitPlus.get(0).currentStarSuitPlusArriveLevle = si.id;
			if (state != null && state.intValue() == 1) {
				sendUpdateAdvanceSuitPlus();
			}
			// System.out.println( "升星强化：id =" +si.id +";num=" +
			// si.num+"; lv="+si.lv+
			// "; atb="+si.atb+"; 当前最大等级："+listAdvanceSuitPlus.get(0).starSuitPlusArriveMaxLevel);
		}
		return si;
	}
	
	public WashItem fetchWshSuitPlus(Map<String, Integer> map, Integer state){
		if (map.size() == 0) {
			return null;
		}
//		System.out.println("map = " +map);
		List<WashItem> washItemList = XmlCache.xmlFiles.constantFile.washSuitPlus.washItem;
		WashItem wi = null;
		for (WashItem washItem : washItemList) {
			int index = 0;
			// 达到过得最大等级
			if (washItem.id <= (listAdvanceSuitPlus.get(0).washSuitPlusMaxLevel + 1)) {
				for (Integer value : map.values()) {
					// key=部位id； value=强化星数
					if (value.intValue() >= washItem.lv) {
						index++;
					}
				}
				if (index >= washItem.num) {
					wi = washItem;
				}
			}
		}
		
		if (wi != null) {
			if (wi.id == (listAdvanceSuitPlus.get(0).washSuitPlusMaxLevel + 1)) {
				listAdvanceSuitPlus.get(0).adjustWashSuitPlusMaxLevel(1);
			}
			listAdvanceSuitPlus.get(0).washSuitPlusCurrentLevel = wi.id;
			if (state != null && state.intValue() == 1) {
				sendUpdateAdvanceSuitPlus();
			}
//			 System.out.println( "升星强化：id =" +wi.id +";num=" + wi.num+"; lv="+wi.lv+ "; atb="+wi.atb+"; 当前最大等级："+listAdvanceSuitPlus.get(0).washSuitPlusMaxLevel+"; 当前等级："+listAdvanceSuitPlus.get(0).washSuitPlusCurrentLevel);
		}
		
		return wi;
	}
	

	public Integer fetchEquipIdBySlot(Integer equipSlot) {
		if (equipSlot == ItemType.ITEM_CATEGORY_WEAPON) {
			return getEquipWeaponId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_ARMOR) {
			return getEquipArmorId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_RING) {
			return getEquipRingId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_BRACER) {
			return getEquipBracerId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_NECKLACE) {
			return getEquipNecklaceId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_HELMET) {
			return getEquipHelmetId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_SHOE) {
			return getEquipShoeId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_BRACELET) {
			return getEquipBraceletId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_PANTS) {
			return getEquipPantsId();
		} else if (equipSlot == ItemType.ITEM_CATEGORY_BELT) {
			return getEquipBeltId();
		}
		return null;
	}

	public void addEquip(Integer equipId, Integer bind) {
		RolePackItemVo rolePackItemVo = new RolePackItemVo();
		rolePackItemVo.setBindStatus(bind);
		rolePackItemVo.setEquipId(equipId);
		rolePackItemVo.setNum(1);
		rolePackItemVo.setItemId(EqpPo.findEntity(equipId).getItemId());
		rolePackItemVo.loadEqp();
		singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_420, mainPackItemVosMap);
	}

	private void sendToTempPack() {
		ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key35"));
	}

	/**
	 * 
	 * 方法功能:整理背包 更新时间:2011-11-29, 作者:johnny
	 * 
	 * @param advantageItemId
	 * @param type
	 */
	public void makeSetPack(int[] advantageItemId, ConcurrentHashMap<String, RolePackItemVo> vosMap, int type) {
		List<RolePackItemVo> resultRolePackItemVos = new ArrayList<RolePackItemVo>();
		for (RolePackItemVo rolePackItemVo : vosMap.values()) {
			resultRolePackItemVos.add(rolePackItemVo);
		}
		vosMap.clear();
		Collections.sort(resultRolePackItemVos, new RolePackItemVoCompartar(advantageItemId));
		for (RolePackItemVo rolePackItemVo : resultRolePackItemVos) {
			if (rolePackItemVo.wasEquip()) {
				singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_420, vosMap);
			} else {
				// 增加道具

				// if(fetchPackMapByType(packType)!=null){
				ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
				int remainNum = rolePackItemVo.getNum();
				for (RolePackItemVo tmpVo : vosMap.values()) {
					if (tmpVo != null) {
						if (tmpVo.getItemId().intValue() == itemPo.getId().intValue()
								&& tmpVo.bindStatus == rolePackItemVo.bindStatus.intValue()) {
							int space = itemPo.getFoldMax() - tmpVo.getNum();
							int addCount = Math.min(space, remainNum);
							int cellTargetCount = tmpVo.getNum() + addCount;
							if (addCount <= 0) {
								continue;
							}
							remainNum = remainNum - addCount;
							tmpVo.setNum(cellTargetCount);
							if (remainNum == 0) {
								break;
							}
						}
					}
				}
				if (remainNum > 0) {
					Integer index = -1;
					if(type == 1){
						index = fetchFreeIndex(PlayTimesType.PLAYTIMES_TYPE_420, vosMap);
					}else if (type == 5){
						index = fetchFreeIndex(PlayTimesType.PLAYTIMES_TYPE_440, vosMap);
					}
					RolePackItemVo rpVo = new RolePackItemVo();
					rpVo.bindStatus = rolePackItemVo.bindStatus;
					rpVo.setItemId(rolePackItemVo.getItemId());
					rpVo.setIndex(index);
					rpVo.setNum(remainNum);
					vosMap.put(index.toString(), rpVo);
				}

			}
		}
	}


	public void unEquipBySlot(Integer equipSlot, boolean addEquip) {
		Integer equipId = fetchEquipIdBySlot(equipSlot);
		if (equipId != null) {
			if (addEquip) {
				EqpPo eqpPo = EqpPo.findEntity(equipId);
				addEquip(equipId, eqpPo.getBindStatus());
			}
			doSetEquipIdBySlotType(equipSlot, null);
		}
	}

	public void equipBySlot(RolePackItemVo rolePackItemVo, Integer equipSlot) {
		removeItemFromMainPack(rolePackItemVo.index,GlobalCache.fetchLanguageMap("key2661"),false);
		doSetEquipIdBySlotType(equipSlot, rolePackItemVo.equipId);
	}
	
	/** 推送客户端Avatars */
	public void sendAvatars(){
		CommonAvatarVo commonAvatarVo = CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(),fashion,getCareer(), getWingWasHidden(), getWingStar(), hiddenFashions);
		if(fighter != null){
			fighter.makeAvatars(commonAvatarVo, true);						
		}
	}
	
	
	/** 获取自己的拍卖行列表 */
	public List<AuctionItemPo> fetchAuctionMySellList() {
		List<AuctionItemPo> auctionItemPoList = BaseDAO.instance().dBfind("from AuctionItemPo where sellerRoleId =" + getId());
		return fetchAuctionFilterExpirationTime(auctionItemPoList);
	}

	/** 过滤拍卖时间到了的拍卖行列表 */
	public List<AuctionItemPo> fetchAuctionFilterExpirationTime(List<AuctionItemPo> auctionItemPoList){
		List<AuctionItemPo> list = new ArrayList<AuctionItemPo>();
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		for(AuctionItemPo auctionItemPo : auctionItemPoList){
			if(auctionItemPo.getSellExpirationTime() != null && System.currentTimeMillis() > auctionItemPo.getSellExpirationTime().longValue()){
				sendAuctionMail(auctionItemPo, 2);
			}else{
				list.add(auctionItemPo);
			}
		}
		return list;
	}
	
	/** 发送拍卖邮件 */
	public void sendAuctionMail(AuctionItemPo auctionItemPo,Integer auctionType){
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		StringBuilder sb = new StringBuilder();
		if (auctionItemPo.itemPo().wasEquip()) {
			 sb.append(2);
			 sb.append("|");
			 sb.append(auctionItemPo.getEquipId());
			 sb.append("|");
			 sb.append(1);
			 sb.append("|");
			 sb.append(0);
		} else{
			 sb.append(1);
			 sb.append("|");
			 sb.append(auctionItemPo.getItemId());
			 sb.append("|");
			 sb.append(auctionItemPo.getNum());
			 sb.append("|");
			 sb.append(0);
		}
		if(auctionType.intValue() == 1){
			mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key239"), getId(), null, GlobalCache.fetchLanguageMap("key2283"), sb.toString(), MailType.MAIL_TYPE_SYSTEM);	
		}else if(auctionType.intValue() == 2){
			mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key2306"), auctionItemPo.getSellerRoleId(), null, GlobalCache.fetchLanguageMap("key2284"), sb.toString(), MailType.MAIL_TYPE_SYSTEM);			
		}
		BaseDAO.instance().remove(AuctionItemPo.findEntity(auctionItemPo.getId()));
	}
	
	public ChannelHandlerContext fetchSession() {
		RoleTemplate roleTemplate = RoleTemplate.instance();
		return roleTemplate.getSessionById(getId());
	}

	public void addAuctionItem(AuctionItemPo auctionItemPo) {
		if (auctionItemPo.itemPo().wasEquip()) {
			addEquip(auctionItemPo.getEquipId(), 0);
			EqpPo eqpPo=EqpPo.findEntity(auctionItemPo.getEquipId());
			LogUtil.writeLog(this, 209, eqpPo.itemPo().getId(), 0, eqpPo.getPowerLv(), GlobalCache.fetchLanguageMap("key2358"), "");
		} else {
			addItem(auctionItemPo.getItemId(), auctionItemPo.getNum(), 0);
			LogUtil.writeLog(this, 209, auctionItemPo.getItemId(), 0, 0, GlobalCache.fetchLanguageMap("key2359"), "");
		}
	}

	public static RolePo findEntity(Integer id) {
		RolePo rolePo = findRealEntity(RolePo.class, id);
		if (rolePo == null) {
			if (id == null) {
				return null;
			}
			if (GlobalCache.robotTaskRoles.containsKey(id)) {
				return GlobalCache.robotTaskRoles.get(id);
			} else if (GlobalCache.robotArenaRoles.containsKey(id)) {
				return GlobalCache.robotArenaRoles.get(id);
			} else {
				return null;
			}
		}
		return rolePo;
	}

	public IdNumberVo2 fetchRoleTask(Integer taskId) {
		return IdNumberVo2.findIdNumber(taskId, listRoleTasks);
	}

	public List<Integer> listenerMapIds = new ArrayList<Integer>();

	@JSONField(serialize = false)
	public Fighter fighterPet;

	@JSONField(serialize = false)
	public Fighter yunDartCar;

	public BattleResultVo battleResultVo;

	/**
	 * 副本奖励
	 * 
	 * @param copySceneConfPo
	 */
	public void awardCopySceneConfPo(CopySceneConfPo copySceneConfPo,
			Integer result, boolean award) {
		int copySceneId = copySceneConfPo.getId();
//		System.out.println("awardCopySceneConfPo():"+getName()+" || "+copySceneId+"; result="+result);
		BattleResultVo battleResultVo = new BattleResultVo();
		battleResultVo.copySceneConfId = copySceneId;
		int addGold = 0;
		int addExp = 0;
		int addSkillPoint = 0;
		int currentRank = 0;
		if (result.intValue() == 1) {
			switch (copySceneConfPo.getType()) {
			case CopySceneType.COPY_SCENE_TYPE_SINGLE_EXP:
				// 经验副本
				// if(copySceneId == CopySceneType.COPYSCENE_EXP)
				// {
				addExp = 100000;
				if (getLv().intValue() > 30) {
					addExp += (getLv().intValue() - 30) * 6800;
				}
				battleResultVo.exp = addExp;
				// }
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				awardBattleResult(battleResultVo);
				sendCopySceneFinish(battleResultVo);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, addExp, GlobalCache.fetchLanguageMap("key2373"), "");
				break;
			case CopySceneType.COPY_SCENE_TYPE_SINGLE_GOLD:
				// 获得金币 伤害值^0.5*80
				int totalDamage =fighter.sumDamage;
				if(fighterPet != null){
					totalDamage+=fighterPet.sumDamage;
				}
				battleResultVo.gold=(int) (Math.pow(totalDamage, 0.5) * 80);
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				awardBattleResult(battleResultVo);
				sendCopySceneFinish(battleResultVo);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, battleResultVo.gold, GlobalCache.fetchLanguageMap("key2374"), "");
				break;
			case CopySceneType.COPY_SCENE_TYPE_SINGLE_MATERIAL:
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				awardBattleResult(battleResultVo);
				sendCopySceneFinish(battleResultVo);
				break;
			case CopySceneType.COPY_SCENE_TYPE_ARENA:
				// 竞技场

				RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(id);
				RankArenaPo target = GlobalCache.rankArenaRoleIdMaps.get(arenaTargetRoleId);
				RolePo targetRole = RolePo.findEntity(arenaTargetRoleId);
				// 排名数字大，说明排名靠后
				if (me.getArenaRank() > target.getArenaRank()) {
					// 当前对象排名
					int tempRank = me.getArenaRank();
					// 目标对象排名
					int targetRank = target.getArenaRank();
					
					me.setArenaRank(targetRank);
					target.setArenaRank(tempRank);
					me.loadByRolePo(this);
					target.loadByRolePo(targetRole);
					GlobalCache.rankArenaMaps.put(me.getArenaRank(), me);
					GlobalCache.rankArenaMaps.put(target.getArenaRank(), target);
					GlobalCache.rankArenaRoleIdMaps.put(me.getRoleId(), me);
					GlobalCache.rankArenaRoleIdMaps.put(target.getRoleId(),target);
					

					// 更新缓存信息
					battleResultVo.arenaRank = currentRank;
				} else {
					me.loadByRolePo(this);
					target.loadByRolePo(targetRole);
				}
				if(me!=null){
					setArenaRank(me.getArenaRank());
				}
				if(target!=null){
					targetRole.setArenaRank(target.getArenaRank());
				}
				this.liveActivityRankArena();
				if(!targetRole.wasRobot()){
					targetRole.liveActivityRankArena();					
				}
				
				if (fighter.cell != null) {
					List<Cell> cells = fighter.mapRoom.cellData
							.getNearByCells(fighter.cell,com.games.mmo.mapserver.bean.Entity.STAND_BORDER);
					for (Cell cell : cells) {
						List<Fighter> players = cell.getAllPlayers();
						for (Fighter fig : players) {
							if (fig.robot
									&& fig.itemId.intValue() == arenaTargetRoleId
											.intValue()) {
								fighter.mapRoom.cellData
										.removeLiving(fig, true);
							}
						}
					}
				}
				

				if(me.getWasRobot() == 1 || me.getRoleId().intValue() == id.intValue()){
					me.setWasFirstArena(1);					
				}
				if(target.getWasRobot().intValue() == 1 || target.getRoleId() == id.intValue()){
					target.setWasFirstArena(1);
				}
				
				addSkillPoint = 600;
				adjustNumberByType(addSkillPoint, RoleType.RESOURCE_SKILL_POINT);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_SKILLPOINT, 0, addSkillPoint, GlobalCache.fetchLanguageMap("key2375"), "");
				currentRank = me.getArenaRank();
				battleResultVo.arenaRank = currentRank;
				battleResultVo.skillPoint = addSkillPoint;
				
				sendCopySceneFinish(battleResultVo);
				sendUpdateSkillPoint();
				break;
			case CopySceneType.COPY_SCENE_TYPE_TEAM_DEFEND:
				if(award){
					battleResultVo.loadCardAward(copySceneConfPo);
					battleResultVo.loadItemList(copySceneConfPo);
					awardBattleResult(battleResultVo);
				}
				sendCopySceneFinish(battleResultVo);
				break;
			case CopySceneType.COPY_SCENE_TYPE_ROLE_LEVEL:
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				awardBattleResult(battleResultVo);
				sendCopySceneFinish(battleResultVo);
			default:
				break;
			}
		} else {
			if (copySceneId == CopySceneType.COPYSCENE_ARENA) {
				RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(id);
				currentRank = me.getArenaRank();
				addSkillPoint = 400;
				adjustNumberByType(addSkillPoint, RoleType.RESOURCE_SKILL_POINT);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_SKILLPOINT, 0, addSkillPoint, GlobalCache.fetchLanguageMap("key2376"), "");
				battleResultVo.arenaRank = currentRank;
				sendCopySceneFinish(battleResultVo);
				sendUpdateSkillPoint();
			}
		}
		addCopySceneTodayRecord(copySceneConfPo.getId(),copySceneConfPo.getTeamMode(),0,1);
		// System.out.println("copySceneId == " +copySceneId);

		// awardDropExp(copySceneConfPo.listItemDrop);
		// awardByExp(copySceneConfPo.listAwardExp);

	}

	/**
	 * 刀塔扫荡奖励
	 * 
	 * @param listItemDrop
	 * @return
	 */
	public List<IdNumberVo> awardTowerDropExp(List<IdNumberVo2> listItemDrop) {
		List<DropPo> totalDrops = DropPo.makeDropListByExp(listItemDrop);
		List<IdNumberVo> list = new ArrayList<IdNumberVo>();
		for (DropPo dropPo : totalDrops) {
			awardDrop(dropPo, dropPo.getBind());
			list.add(new IdNumberVo(dropPo.getItemId(), dropPo.getNum()));
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, dropPo.getItemId(), dropPo.getNum(),GlobalCache.fetchLanguageMap("key2381"), "");
		}
		return list;
	}

	public void addCopySceneTodayRecord(Integer copySceneConfigId,Integer teamOrNot, int num, int rewardNum) {
		boolean bool =false;
		for (IdNumberVo3 idNumberVo3 : listCopySceneTodayVisitTimes) {
			if (idNumberVo3.getInt1() == copySceneConfigId.intValue()
					&& idNumberVo3.getInt2().intValue() == teamOrNot.intValue()) {
				idNumberVo3.setInt3(idNumberVo3.getInt3() + num);
				idNumberVo3.setInt4(idNumberVo3.getInt4() + rewardNum);
				bool =true;
				break;
			}
		}
		if(!bool){
			listCopySceneTodayVisitTimes.add(new IdNumberVo3(copySceneConfigId,teamOrNot, num, 0));			
		}
		
		if(num != 0){
			CopySceneConfPo copySceneConfPo = CopySceneConfPo.findEntity(copySceneConfigId);
			if(copySceneConfPo.getSceneId().intValue() == PKGreatRoom.SCENE_ID){
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_3, 1);
			}else if(copySceneConfPo.getSceneId().intValue() == DemonizationCrisisRoom.SCENE_ID){
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_4, 1);
			}else if(copySceneConfPo.getSceneId().intValue() == ZaphieHaramRoom.SCENE_ID){
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_5, 1);
			}else if(copySceneConfPo.getSceneId().intValue() == BloodSeekerBastionRoom.SCENE_ID){
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_6, 1);
			}else if(copySceneConfPo.getSceneId().intValue() == TeamTowerRoom.SCENE_ID){
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_7, 1);
			}else if(copySceneConfPo.getSceneId().intValue() == 20100002){ // 竞技场
				updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_1, 1);
			}else{
				if(copySceneConfPo.getSceneId().intValue() == SingleExpRoom.SCENE_ID){
					updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_520, 1);				
				}
			}	
		}
		
	}

	public void checkHasSkillPoint(int sp) {
		if (getSkillPoint().intValue() < sp) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key36"));
		}
	}

	public void checkHasGold(int goldRequire) {
		if (getGold().intValue() < goldRequire) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key37"));
		}
	}

	public void checkHasBindGold(int bindGoldRequire) {
		if (getBindGold().intValue() < bindGoldRequire) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key38"));
		}
	}
	public void checkHasWarehouseGold(int goldRequire) {
		if (getWarehouseGold().intValue() < goldRequire) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key39"));
		}
	}
	public void checkHasWarehouseBindGold(int bindGoldRequire) {
		if (getWarehouseBindGold().intValue() < bindGoldRequire) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key40"));
		}
	}
	
	public void checkHasPrestige(int num){
		if(getPrestige().intValue() < num){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2259"));
		}
	}
	
	public void checkHasGamstone(int num){
		if(getGamstoneFragment().intValue()<num){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2708"));
		}
	}
	
	public void checkHasSoul(int num){
		if(getSoul().intValue()<num){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2711"));
		}
	}
	

	private void checkHasGuildHonor(Integer num) {
		if (getGuildHonor().intValue() < num.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key41"));
		}
	}

	private void checkHasDiamond(Integer require) {
		UserPo userPo = UserPo.findEntity(getUserId());
		if (userPo.getDiamond().intValue() < require.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key42"));
		}
	}

	private void checkHasBindDiamond(Integer bindDiamondRequire) {
		if (getBindDiamond().intValue() < bindDiamondRequire.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key43"));
		}
	}

	private void checkHasPriorityBindGoldThenGold(
			Integer bindGoldThenGoldRequire) {
		int sum = getGold().intValue() + getBindGold().intValue();
		if (sum < bindGoldThenGoldRequire.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key44"));
		}
	}

	private void checkHasPriorityBindDiamondThenDiamond(
			Integer bindDiamondThenDiamondRequire) {
		UserPo userPo = UserPo.findEntity(getUserId());
		int sum = userPo.getDiamond().intValue() + getBindDiamond().intValue();
		if (sum < bindDiamondThenDiamondRequire.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key45"));
		}
	}

	public void checkHasPetSoul(Integer petSoulRequire) {
		if (getPetSoul().intValue() < petSoulRequire.intValue()) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key46"));
		}
	}

	public void adjustSkillPoint(Integer spChange) {
		if(spChange== null){
			return;
		}
		int remainskillPoint = skillPoint.intValue()+ spChange.intValue();
		if (remainskillPoint > 0) {
			setSkillPoint(remainskillPoint);
		} else {
			setSkillPoint(0);
		}
	}

	public void adjustPrestige(Integer i) {
		if(i == null){
			return;
		}
		int remainPrestige = prestige.intValue()+ i.intValue();
		if(i > 0){
			setPrestigeTotal(prestigeTotal + i.intValue());
		}
		if (remainPrestige > 0) {
			setPrestige(remainPrestige);
		} else {
			setPrestige(0);
		}
		liveActivityRankPrestige();
		sendUpdateExpAndLv(true);
	}
	
	public void checkPrestigeTotal(){
		if(prestigeTotal == null || prestigeTotal.intValue() == 0){
			int currentPrestige = 0;
			for(IdNumberVo inv : listMilitaryRankRecord){
				if(inv.getNum() == 1){
					MilitaryRankPo mrp = MilitaryRankPo.findEntity(inv.getId());
					if(mrp != null){
						currentPrestige+=mrp.getPrestige();
					}
				}
			}
			currentPrestige+=prestige;
			setPrestigeTotal(currentPrestige);
		}
	}
	

	public int fetchSkillLv(Integer skillId) {
		for (IdNumberVo idNumberVo : listSkillVos) {
			if (idNumberVo.getId().intValue() == skillId) {
				return idNumberVo.getNum();
			}
		}
		ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"skillId:" + skillId);
		return 0;
	}

	public void sendUpdateRoleTasks(boolean flush) {
		// System.out.println("推送："+listRoleTasks);
		singleRole("PushRemoting.sendUpdateRoleTasks",
				new Object[] { listRoleTasks },flush);
	}

	public SlotSoulVo findSlotSoul(Integer slot) {
		for (SlotSoulVo slotSoulVo : listSlotSouls) {
			if (slotSoulVo.slotNum == slot.intValue()) {
				return slotSoulVo;
			}
		}
		return null;
	}

	public RolePackItemVo findAvaEquipBySlot(int slot) {
		RolePackItemVo rpiv = null;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			if (rolePackItemVo.wasEquip()) {
				ItemPo itemPo = rolePackItemVo.eqpPo.itemPo();
				if (itemPo.getCategory() == slot) {
					if (itemPo.getMatchClass().intValue() == getCareer()
							.intValue()) {
						rpiv = compareEquip(rpiv, rolePackItemVo);
						// System.out.println(rpiv);
					}
				}
			}
		}
		return rpiv;
	}

	public boolean compareRoleEquipAndItemEquip(ItemPo itemPo,
			RolePackItemVo vo1) {
		if (vo1 == null) {
			return false;
		}
		ItemPo itemPo2 = ItemPo.findEntity(vo1.getItemId());
		boolean bool = compareEquipItemPo(itemPo, itemPo2);
		return bool;
	}

	public void checkAndFreshRoleLiveActivitys() {
		for (RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys) {
			Boolean matched=false;
			for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
				if(roleLiveActivityVo.liveActivityId==liveActivityPo.getId()){
					matched=true;
				}
			}
			if(matched==false){
				listRoleLiveActivitys.remove(roleLiveActivityVo);
				break;
			}
		}
		
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			boolean hasActivity = false;
			for (RoleLiveActivityVo roleActivityVo : listRoleLiveActivitys) {
				if (roleActivityVo.liveActivityId == liveActivityPo.getId().intValue()) {
					hasActivity = true;
				}
			}
			if (!hasActivity) {
				RoleLiveActivityVo roleLiveActivityVo = new RoleLiveActivityVo();
				roleLiveActivityVo.liveActivityId = liveActivityPo.getId();
				int index = 0;
				for (IdNumberVo idNumberVo : liveActivityPo.listConditions) {
//					System.out.println(liveActivityPo.getId()+" | "+index+"| idNumberVo="+idNumberVo);
					int finishTimes = liveActivityPo.listLoopFinishTimes.get(index);
					IdNumberVo3 idNumberVo3 = new IdNumberVo3(index++,0,finishTimes,0);											// 完成次数
					roleLiveActivityVo.objs.add(idNumberVo3);
				}
				index = 0;
				for (List<IdNumberVo> list : liveActivityPo.listExchangeItems) {
//					System.out.println(liveActivityPo.getId()+" | "+index+"| idNumberVo="+list);
					int finishTimes = liveActivityPo.listLoopFinishTimes.get(index);
					IdNumberVo3 idNumberVo3 = new IdNumberVo3(index++,0,finishTimes,0);
					roleLiveActivityVo.objs.add(idNumberVo3);
				}
//				System.out.println(" ");
				listRoleLiveActivitys.add(roleLiveActivityVo);
			}
		}
//		System.out.println("listRoleLiveActivitys="+listRoleLiveActivitys);
	}

	/**
	 * 比较装备
	 * 
	 * @param vo1
	 * @param vo2
	 * @return
	 */
	public RolePackItemVo compareEquip(RolePackItemVo vo1, RolePackItemVo vo2) {
		if (vo1 == null && vo2 == null) {
			return null;
		}
		if (vo1 == null) {
			return vo2;
		}
		if (vo2 == null) {
			return vo1;
		}
		if(vo1.getItemId().intValue()==vo2.getItemId().intValue()){
			if(vo1.eqpPo.getPowerLv()>=vo2.eqpPo.getPowerLv()){
				return vo1;
			}else{
				return vo2;
			}
		}
		ItemPo itemPo1 = ItemPo.findEntity(vo1.getItemId());
		ItemPo itemPo2 = ItemPo.findEntity(vo2.getItemId());
		boolean bool = compareEquipItemPo(itemPo1, itemPo2);
		if (bool) {
			return vo2;
		} else {
			return vo1;
		}
	}

	public boolean compareEquipItemPo(ItemPo itemPo1, ItemPo itemPo2) {
		if(itemPo1.equipPower.intValue() < itemPo2.equipPower.intValue()){
			return true;
		}else{
			return false;
		}
	}

	public void checkHasRolePackIndex(Integer index) {
		RolePackItemVo rolePackItemVo = fetchRolePackItemByIndex(index);
		if (rolePackItemVo == null) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"index111:" + index);
		}
	}
	
	public void checkHasRoleWarehousePackIndex(Integer index) {
		RolePackItemVo rolePackItemVo = fetchRoleWarehousePackItemByIndex(index);
		if (rolePackItemVo == null) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"index222:" + index);
		}
	}
	

	public List<IdNumberVo> useItemEffects(List<List<Integer>> listItemUseExp,int num,Integer bind, boolean send) {
		List<IdNumberVo> addItems = new ArrayList<IdNumberVo>();
		for (List<Integer> list : listItemUseExp) {
			int effectType = list.get(0);

			if (effectType == ItemType.ITEM_USE_EFFECT_DIAMOND_ADD) {
				adjustDiamond(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2467"), "");
			}
			if (effectType == ItemType.ITEM_USE_EFFECT_BINDED_DIAMOND_ADD) {
				adjustBindDiamond(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMOND, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2468"), "");
			}
			if (effectType == ItemType.ITEM_USE_EFFECT_PRESTIGE) {
				adjustPrestige(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PRISTIGE, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2469"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_GOLD_ADD) {
				adjustGold(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_GOLD, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2470"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_BINDED_GOLD_ADD) {
				adjustBindGold(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2471"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_SKILL_POINT_ADD) {
				adjustSkillPoint(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_SKILLPOINT, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2472"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_ACHIVE_POINT_ADD) {
				adjustAchievePoint(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ACHIVEPOINT, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2474"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_BAT_SKILL) {
				if (fighter != null) {
					fighter.makeSkillEffect(list.get(1), fighter.mapUniqId,fighter.mapUniqId.toString(), 0,null,null);
				}
			} else if (effectType == ItemType.ITEM_USE_EFFECT_EXP) {
				adjustExp(list.get(1) * num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0,list.get(1) * num, GlobalCache.fetchLanguageMap("key2475"), "");
			} else if (effectType == ItemType.ITEM_USE_EFFECT_ITEM_DROP) {
				checkItemPackFull(1);
				ChatService chatService = ChatService.instance();
				ItemPo itemPo = ItemPo.findEntity(list.get(1));
				String[] jobItems=StringUtil.split(itemPo.getDropItems(), "$");
				String[] strs = StringUtil.split(jobItems[0], ";");
				if(jobItems.length==3){
					strs = StringUtil.split(jobItems[this.career-1], ";");
				}
				for (int j = 0; j < strs.length; j++) {
					List<List<Integer>> dropList = StringUtil.buildBattleExpressList(strs[j]);
					for (int i = 0; i < num; i++) {
						IdNumberVo inv = RandomUtil.calcWeightOverCardAward(dropList, null);
						sendChatByItem(list.get(1), inv.getId(), ChatType.CHAT_DROP_TYPE_OPEN_BOX);
						if(itemPo.getId().intValue()==300004065&&IntUtil.checkInInts(inv.getId(), new int[]{300004051,300010036,300099007})){
							String str = GlobalCache.fetchLanguageMap("key2702");
							String sb = MessageFormat.format(str, name , itemPo.getName(),ItemPo.findEntity(inv.getId()).getName());
							chatService.sendHorse(sb);
						}
						if(itemPo.getId().intValue()==300004087&&IntUtil.checkInInts(inv.getId(), new int[]{300004052,300010057,300004051})){
							String str = GlobalCache.fetchLanguageMap("key2702");
							String sb = MessageFormat.format(str, name , itemPo.getName(),ItemPo.findEntity(inv.getId()).getName());
							chatService.sendHorse(sb);
						}
						addItem(inv.getId(), inv.getNum(), bind);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv.getId(),inv.getNum(), GlobalCache.fetchLanguageMap("key2475"), "");
						addItems.add(inv);
					}
				}
				
			}else if(effectType == ItemType.ITEM_USE_EFFECT_110){
				BuffPo buffPo = BuffPo.findEntity(list.get(1));
				BufferStatusVo bsVo = fighter.findBufferStatus(buffPo.getId());
				if(bsVo != null){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2621"));
				}
				List<Fighter> receiveFighters = new ArrayList<Fighter>();
				receiveFighters.add(fighter);
				BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, fighter, receiveFighters);
				fighter.makeAddBuff(bufferStatusVo,fighter, true);
			}else if(effectType == ItemType.ITEM_USE_EFFECT_111){
				for(int i=0; i<num; i++){
					addPet(list.get(1));					
				}
				sendUpdatePetList();
			}else if(effectType == ItemType.ITEM_USE_EFFECT_112){
				if(listRechargeInfo.get(0).wasMonthCard == 1){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key71"));
				}
				listRechargeInfo.get(0).wasMonthCard = 1;
				listRechargeInfo.get(0).monthCardRechargeBeginTime = System.currentTimeMillis();
				listRechargeInfo.get(0).remainMonthCardDay = 30;
				listRechargeInfo.get(0).todayWasTakeMonthCard = 0;	
				sendUpdateRechargeInfo();
			}else if(effectType == ItemType.ITEM_USE_EFFECT_113){
//				System.out.println("       ");
//				System.out.println(" useItemEffects()");
//				System.out.println("pkValue = "+getPkValue());
//				System.out.println(DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) +" | "+DateUtil.getFormatDateBytimestamp(pkLastRecoverTime));
				if(getPkRedBeginTime()==null||getPkRedBeginTime()==0||getPkValue().intValue()< 10 || System.currentTimeMillis() > pkLastRecoverTime){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2507"));
				}
				List<OnlineTime> onlineTimes = XmlCache.xmlFiles.constantFile.accumulativeTime.onlineTime;
				updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,
						0l,list.get(1)*num*onlineTimes.get((int) (RoleType.ONLINE_TIME_TYPE_RED -1)).value,
						1);
				long addPkTime = onlineTimes.get((int) (RoleType.ONLINE_TIME_TYPE_RED -1)).value;
				long pkRedTime =getPkLastRecoverTime().longValue() +addPkTime*(-list.get(1));
//				System.out.println(" pkRedTime = " +DateUtil.getFormatDateBytimestamp(pkRedTime));
				setPkLastRecoverTime(pkRedTime);	
//				System.out.println("RolePo.useItemEffects() listOnlineTime = " +listOnlineTime);
				if(fighter != null){
					fighter.checkResetPkStatus();
				}
				LogUtil.writeLog(this, 1, 13, 0, -list.get(1), "吃药pk值改变", "");
			}else if(effectType == ItemType.ITEM_USE_EFFECT_114){
				UserPo userPo = UserPo.findEntity(getUserId());
				userPo.adjustCumulativeRechargeNum(list.get(1)*num);
				listRechargeInfo.get(0).adjustCumulativeRechargeNum(list.get(1)*num);
				checkVipLv();
				sendUpdateRechargeInfo();
			}else if(effectType == ItemType.ITEM_USE_EFFECT_115){
				
				FashionPo fashionPo = FashionPo.findEntity(list.get(1));
				if(fashionPo == null){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"FashionPo:"+id);					
				}
				IdNumberVo2 idNumberVo2 = IdNumberVo2.create(fashionPo.getPrice());
				if(fashionPo.getIsBuy() == 0)
				{
					addFashion(fashionPo.getId(), idNumberVo2.getInt1());
				}
			}else if(effectType == ItemType.ITEM_USE_EFFECT_116){
				RechargePo rechargePo = RechargePo.findEntity(list.get(1));
				for(int i=0; i<num; i++){
					rechargeSendByRechargeId(rechargePo.getId(), Double.valueOf(rechargePo.getRechargeRmb().toString()), rechargePo.getRechargeNum(), 0L);					
				}
			}else if(effectType == ItemType.ITEM_USE_EFFECT_117){
				for(IdNumberVo2 idNumberVo2:listRoleTasks){
					TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
					if(taskPo==null){
						continue;
					}
					int type = taskPo.getTaskType();
					if(type==TaskType.TASK_TYPE_OFFER_REWARD){
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2706"));
					}
				}
				activeRoleTask(list.get(1));
				acceptedRoleTask(list.get(1));
				freshTaskNewStatus(list.get(1));
				sendUpdateRoleTasks(false);
			}else if(effectType == ItemType.ITEM_USE_EFFECT_118){
				adjustGuildHonor(list.get(1)*num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, list.get(1) * num, GlobalCache.fetchLanguageMap("key2474"), "");
			}else if(effectType == ItemType.ITEM_USE_EFFECT_119){
				adjustGamstoneFragment(list.get(1)*num);
			}else if (effectType == ItemType.ITEM_USE_EFFECT_120) {
				adjustSoul(list.get(1)*num);
			}else if(effectType == ItemType.ITEM_USE_EFFECT_121){
				if(openSystemArrayList.contains(TaskType.OPEN_SYSTEM_SOUL)){
					SoulService soulService = SoulService.instance();
					soulService.addSoulAtbExp(soulAtbMap, SoulType.Type.getType(list.get(1)), list.get(2));
				}
			}
		}
		if(send){
			sendUpdateAchieveAndTitle();
			sendUpdateTreasure(false);
		}
		return addItems;
	}
	
	public void sendChatByItem(Integer fromId, Integer dropId, Integer dropType){
		ItemPo dropPo = ItemPo.findEntity(dropId);
		if(dropPo.listDropAnnounce!=null&&!dropPo.listDropAnnounce.isEmpty()){
			ChatService chatService = ChatService.instance();
			for(IdNumberVo idNumberVo:dropPo.listDropAnnounce){
				if(idNumberVo.getId()==dropType.intValue()){
					String sb = "";
					switch(dropType.intValue()){
					case 1:
						String str1 = GlobalCache.fetchLanguageMap("key2707");
						sb = MessageFormat.format(str1, name , dropPo.getName());
						break;
					case 2:
						ItemPo fromPo = ItemPo.findEntity(fromId);
						String str = GlobalCache.fetchLanguageMap("key2702");
						sb = MessageFormat.format(str, name , fromPo.getName(),dropPo.getName());
						break;
						default:
							break;
					}
					switch(idNumberVo.getNum()){
					case ChatType.CHAT_CHANNEL_WORLD:
						chatService.sendWorldFromSystemChat(sb);
						break;
					case ChatType.CHAT_CHANNEL_GUILD:
						chatService.sendGuild(sb, guildId);
						break;
						default:
							break;
					}
				}
			}
		}
	}

	public void checkHasAndConsumeResource(Integer resourceType, Integer num) {
		checkHasResource(resourceType, num);
		consumeResource(resourceType, num);
	}

	public void checkHasAndConsumeGuildHonor(int i) {
		checkHasAndConsumeResource(RoleType.RESOURCE_GUILD_HONOR, i);
	}

	private void consumeResource(Integer resourceType, Integer num) {
		// System.out.println("resourceType =" +resourceType +"; num = "+num );
		adjustNumberByType(-num, resourceType);
	}

	public void publicCheckHasResource(Integer resourceType, Integer num) {
		checkHasResource(resourceType, num);
	}
	
	/**
	 * 检查资源类型是否足够
	 * 
	 * @param resourceType
	 * @param num
	 */
	private void checkHasResource(Integer resourceType, Integer num) {
		if (resourceType == RoleType.RESOURCE_GOLD) {
			checkHasGold(num);
		} else if (resourceType == RoleType.RESOURCE_DIAMOND) {
			checkHasDiamond(num);
		} else if (resourceType == RoleType.RESOURCE_SKILL_POINT) {
			checkHasSkillPoint(num);
		} else if (resourceType == RoleType.RESOURCE_GUILD_HONOR) {
			checkHasGuildHonor(num);
		} else if (resourceType == RoleType.RESOURCE_BIND_GOLD) {
			checkHasBindGold(num);
		} else if (resourceType == RoleType.RESOURCE_BIND_DIAMOND) {
			checkHasBindDiamond(num);
		} else if (resourceType == RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD) {
			checkHasPriorityBindGoldThenGold(num);
		} else if (resourceType == RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND) {
			checkHasPriorityBindDiamondThenDiamond(num);
		} else if (resourceType == RoleType.RESOURCE_PETSOUL) {
			checkHasPetSoul(num);
		}else if (resourceType == RoleType.RESOURCE_WAREHOUSE_GOLD) {
			checkHasWarehouseGold(num);
		}else if (resourceType == RoleType.RESOURCE_WAREHOUSE_BIND_GOLD) {
			checkHasWarehouseBindGold(num);
		}else if (resourceType ==RoleType.RESOURCE_PRESTIGE){ 
			checkHasPrestige(num);
		}else if(resourceType == RoleType.RESOURCE_GAMSTONE){
			checkHasGamstone(num);
		}else if (resourceType == RoleType.RESOURCE_SOUL) {
			checkHasSoul(num);
		}
		else {
			ExceptionUtil.throwNotFinishedException("");
		}

	}

	public void saveFighterProToRole() {
		if (fighter != null) {
			listBufferStatus.clear();
			for (BufferStatusVo bufferStatusVo : fighter.bufferStatusVos) {
				List<String> val = new ArrayList<String>();
				val.add(String.valueOf(bufferStatusVo.buffPo.getId()));
				val.add(String.valueOf(bufferStatusVo.endTime));
				val.add(String.valueOf(bufferStatusVo.life));
				val.add(String.valueOf(bufferStatusVo.lifeTime));
				listBufferStatus.add(val);
			}
		}
	}
	
	/**
	 * 离线检查buffer状态
	 */
	public void offlineCheckBufferStatus(){
		List<List<String>> removeList = new ArrayList<List<String>>();
//		System.out.println(" offlineCheckBufferStatus() "+ listBufferStatus);
		for(List<String> list : listBufferStatus){
			Integer buffId = Integer.valueOf(list.get(0));
			Long endTime = Long.valueOf(list.get(1));
			Integer life = Integer.valueOf(list.get(2));
			Long lifeTime = Long.valueOf(list.get(3));
			BuffPo buffPo = BuffPo.findEntity(buffId);
			if(buffPo.getBuffType().intValue() == 1){
				removeList.add(list);
			}else{
				if(endTime.intValue() == CopySceneType.EIKY_TIME){
					continue;
				}
				
				if((System.currentTimeMillis()>endTime.longValue()) || (life.intValue() <= 0&&life.intValue()!=-1)){
					removeList.add(list);
				}else{
					long residueTime = endTime.longValue()-System.currentTimeMillis();
					if(residueTime <=0){
						removeList.add(list);
					}else{
						list.set(3, String.valueOf(residueTime));						
					}
				}
			}
		}
		
		for(List<String> list : removeList){
			listBufferStatus.remove(list);
		}
	}

	public void checkHasAndConsumeSkillPoint(int val) {
		checkHasAndConsumeResource(RoleType.RESOURCE_SKILL_POINT, val);
	}

	public void checkHasAndConsumeGold(int val) {
		checkHasAndConsumeResource(RoleType.RESOURCE_GOLD, val);
	}

	public void checkHasAndConsumeDiamond(int val) {
		checkHasAndConsumeResource(RoleType.RESOURCE_DIAMOND, val);
	}

	public void checkHasAndConsumeBindGoldThenGold(int val) {
		checkHasAndConsumeResource(
				RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD, val);
	}

	public void checkHasAndConsumeBindDiamondThenDiamond(int val) {
		checkHasAndConsumeResource(
				RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, val);
	}
	
	/**
	 * 
	 * 方法功能:检查任务条件完成-增加 更新时间:2014-6-25, 作者:johnny
	 * 
	 * @param num
	 *            增加的数量
	 * @param conditonType
	 *            任务类型
	 * @param par2
	 *            条件1
	 * @param par3
	 *            条件2
	 */
	public void taskConditionProgressAdd(int num, int conditonType,Integer par2, Integer par3) {
		taskConditionProgress(true, conditonType, num, par2, par3,listRoleTasks);
		taskConditionProgressAchieveAdd(num, conditonType, par2, par3);
	}

	/**
	 * 
	 * 方法功能:检查任务条件完成-更新 更新时间:2014-6-25, 作者:johnny
	 * 
	 * @param num
	 * @param conditonType
	 * @param par2
	 * @param par3
	 */
	public void taskConditionProgressReplace(int num, int conditonType,
			Integer par2, Integer par3) {
		taskConditionProgress(false, conditonType, num, par2, par3,
				listRoleTasks);
		taskConditionProgressAchieveReplace(num, conditonType, par2, par3);
	}

	/**
	 * 方法功能:检查任务条件完成-增加 更新时间:2015-8-1, 作者:peter
	 * 
	 * @param num
	 *            增加的数量
	 * @param conditonType
	 *            任务类型
	 * @param par2
	 *            条件1
	 * @param par3
	 *            条件2
	 */
	public void taskConditionProgressAchieveAdd(int num, int conditonType,
			Integer par2, Integer par3) {
		taskConditionProgress(true, conditonType, num, par2, par3,
				listRoleAchievesTasks);
		// for(IdNumberVo2 inv2 : listRoleAchievesTasks){
		// if(inv2.getInt1().intValue() == 650100024){
		// System.out.println("====" +inv2);
		// }
		// }
	}

	/**
	 * 方法功能:检查任务条件完成-更新 更新时间:2015-8-1, 作者:peter
	 * 
	 * @param num
	 *            需要更新的数量
	 * @param conditonType
	 *            任务类型
	 * @param par2
	 *            条件1
	 * @param par3
	 *            条件2
	 */
	public void taskConditionProgressAchieveReplace(int num, int conditonType,
			Integer par2, Integer par3) {
		taskConditionProgress(false, conditonType, num, par2, par3,
				listRoleAchievesTasks);
	}

	/**
	 * 
	 * 方法功能:检查任务条件完成-更新 更新时间:2014-6-25, 作者:johnny
	 * 
	 * @param addNum
	 * @param conditionType
	 * @param num
	 * @param par2
	 * @param par3
	 */
	private void taskConditionProgress(boolean addNum, int conditionType,int num, Integer par2, Integer par3,
			CopyOnWriteArrayList<IdNumberVo2> taskList) {
		// System.out.println("taskConditionProgress addNum="+addNum +
		// ";  conditionType="+conditionType+";  num="+num+";  par2="+par2+";  par3="+par3);
		List<IdNumberVo2> meetRequireTasks = new ArrayList<IdNumberVo2>();
		for (IdNumberVo2 roleTask : taskList) {
			TaskPo taskPo = TaskPo.findEntity(roleTask.getInt1());
			if (taskPo == null) {
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"taskPo:"+ roleTask.getInt1());
			}
			
			int conditionCount = taskPo.conditionVals.size();
			if (conditionType == TaskType.TASK_TYPE_CONDITION_701) {
				conditionCount = 2;
			} else if (conditionType == TaskType.TASK_TYPE_CONDITION_703) {
				conditionCount = 3;
			}


			boolean par2NotEqual = false;
			boolean par3NotEqual = false;

			if (taskPo.conditionVals.get(0).intValue() != conditionType
					|| roleTask.getInt2().intValue() <= TaskType.TASK_STATUS_ACTIVED) {
				continue;
			}
			if (conditionCount >= 3) {
				if (par2 == null) {
					// par2NotEqual=true;
				} else {
					if (conditionType == TaskType.TASK_TYPE_CONDITION_702
							|| conditionType == TaskType.TASK_TYPE_CONDITION_738
							|| conditionType == TaskType.TASK_TYPE_CONDITION_703
							|| conditionType == TaskType.TASK_TYPE_CONDITION_708
							|| conditionType == TaskType.TASK_TYPE_CONDITION_729) {
						if (taskPo.conditionVals.get(2) != par2.intValue()) {
							par2NotEqual = true;
						}
					} else if(conditionType == TaskType.TASK_TYPE_CONDITION_741){
						if(roleTask.getInt3() >= taskPo.conditionVals.get(1)||taskPo.conditionVals.get(2)>par2.intValue()){
							par2NotEqual = true;
						}
					} else {
						if (taskPo.conditionVals.get(2) > par2.intValue()) {
							par2NotEqual = true;
						}
					}
					// 处理条件为0，表示任意条件
					if (taskPo.conditionVals.get(2).intValue() == 0) {
						par2NotEqual = false;
					}
					//韩国以外，8级前打任意怪都满足打怪计数
					GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
					if(gp != null&&!"ko".equals(String.valueOf(gp.valueObj))){
						if(getLv()<=8&&IntUtil.checkInInts(conditionType, new int[]{TaskType.TASK_TYPE_CONDITION_702,TaskType.TASK_TYPE_CONDITION_738})){
							par2NotEqual = false;
						}
					}
				}

			}
			if (conditionCount >= 5) {
				if (par3 == null) {
					par3NotEqual = true;
				} else {
					if (taskPo.conditionVals.get(3) != par3.intValue()) {
						par3NotEqual = true;
					}
				}

			}
			if (!(par2NotEqual || par3NotEqual)) {
				meetRequireTasks.add(roleTask);
			}

		}
		
		
		for (IdNumberVo2 idNumberVo : meetRequireTasks) {
			TaskPo taskPo = TaskPo.findEntity(idNumberVo.getInt1());
			int currentCount = idNumberVo.getInt2();
			int currentFinishTask = idNumberVo.getInt2();
			int taskRequireCount = taskPo.conditionVals.get(1);
			if (addNum) {
				idNumberVo.setInt2(idNumberVo.getInt2() + num);
			} else {
				if(idNumberVo.getInt2().intValue() < num){
					idNumberVo.setInt2(num);					
				}
			}
			idNumberVo
					.setInt2(Math.min(idNumberVo.getInt2(), taskRequireCount));
			
			if (taskPo.getTaskType().intValue() == TaskType.TASK_TYPE_ACHIEVE
					&& idNumberVo.getInt2().intValue() >= taskRequireCount
					&& idNumberVo.getInt3().intValue() == 1) {
				idNumberVo.setInt3(2);
				// System.out.println("id="+taskPo.getId()+"；成就奖励：" +
				// taskPo.listTaskAwardId);
				addItemList(taskPo.listTaskAwardId, 1,GlobalCache.fetchLanguageMap("key2456"));
				sendUpdateTreasure(true);
			}
			if(conditionType != TaskType.TASK_TYPE_CONDITION_731){
				if (currentCount != idNumberVo.getInt2()) {
					if (taskPo.getTaskType() == TaskType.TASK_TYPE_ACHIEVE
							|| taskPo.getTaskType() == TaskType.TASK_TYPE_LIVELY) {
						if (idNumberVo.getInt2() >= taskRequireCount) {
							sendUpdateTaskProgress(idNumberVo.getInt1(),
									idNumberVo.getInt2());
						}
					} else {
						sendUpdateTaskProgress(idNumberVo.getInt1(),
								idNumberVo.getInt2());
					}
				}	
			}
		}
	}

	public RoleViewInforVo toRoleViewInforVo() {
		RoleViewInforVo roleViewInforVo = new RoleViewInforVo();
		roleViewInforVo.equipWeapon = equipWeapon;
		roleViewInforVo.equipRing = equipRing;
		roleViewInforVo.equipArmor = equipArmor;
		roleViewInforVo.equipNecklace = equipNecklace;
		roleViewInforVo.equipBracer = equipBracer;
		roleViewInforVo.equipHelmet = equipHelmet;
		roleViewInforVo.equipShoe = equipShoe;
		roleViewInforVo.equipBelt = equipBelt;
		roleViewInforVo.equipBracelet = equipBracelet;
		roleViewInforVo.equipPants = equipPants;
		roleViewInforVo.roleId = id;
		roleViewInforVo.roleName = name;
		roleViewInforVo.roleLv = lv;
		if (vipLv == null) {
			roleViewInforVo.roleVip = 0;
		} else {
			roleViewInforVo.roleVip = vipLv;
		}
		roleViewInforVo.roleBattlePower = battlePower;
		roleViewInforVo.roleCareer = career;
		GuildPo guildPo = GuildPo.findEntity(getGuildId());
		roleViewInforVo.roleGuildName = guildPo != null ? guildPo.getName(): "";
		roleViewInforVo.listSlotSouls = listSlotSouls;
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(),fashion,getCareer(), getWingWasHidden(),getWingStar(),hiddenFashions);
		roleViewInforVo.makeAvatars(commonAvatarVo);
		roleViewInforVo.powerSuitPlusArriveMaxLevel = powerSuitPlusArriveMaxLevel;
		roleViewInforVo.currentPowerSuitPlusLevel = currentPowerSuitPlusLevel;
		roleViewInforVo.starSuitPlusArriveMaxLevel = starSuitPlusArriveMaxLevel;
		roleViewInforVo.currentStarSuitPlusArriveLevle = currentStarSuitPlusArriveLevle;
		if (rpetFighterId != null && rpetFighterId != 0){
			roleViewInforVo.rpetPo = RpetPo.findEntity(rpetFighterId);			
		}
		roleViewInforVo.petConstellsList=petConstellsList;

		roleViewInforVo.washSuitPlusMaxLevel = washSuitPlusMaxLevel;

		roleViewInforVo.washSuitPlusCurrentLevel = washSuitPlusCurrentLevel;
		return roleViewInforVo;
	}

	/**
	 * 检查是否有道具
	 * 
	 * @param itemId
	 * @param requireCount
	 */
	public void checkHasItem(int itemId, int requireCount) {
		int currentCount = fetchItemCount(itemId);
		if (requireCount > currentCount) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key47"));
		}
	}
	
	/**
	 * 检查背包和仓库中道具数量是否足够
	 * @param itemId
	 * @param requireCount
	 * @param bindState
	 * @return
	 */
	public boolean checkHasItembyBindOrNot(int itemId, int requireCount, int bindState){
		int currentCount = fetchItemCountbyBindOrNot(itemId, bindState);
		if (requireCount > currentCount) {
			return false;
		}
		return true;
	}

	/**
	 * 检查是否有道具，如果没有道具，查看钻石是否足够,并扣除（翅膀）
	 * 
	 * @param itemId
	 * @param requireCount
	 */
	public void checkHasItemORDiamond(int itemId, int requireCount,String logString) {
		int currentCount = fetchItemCount(itemId);
		// System.out.println("itemId == " +itemId +"; requireCount= "
		// +requireCount);
		if (requireCount > currentCount) {
			ProductPo productPo = ProductPo.fetchProductPoByItemId(itemId, 2);
			if (productPo != null) {
				// System.out.println("type=="
				// +productPo.getMoneyType().intValue()+"; 扣除："+productPo.getDiscountsPrice().intValue()*requireCount);
				checkHasAndConsumeResource(productPo.getMoneyType().intValue(),
						productPo.getDiscountsPrice().intValue() * requireCount);
			}
			if(itemId==300099005){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -productPo.getDiscountsPrice().intValue() * requireCount, GlobalCache.fetchLanguageMap("key2390"),"");
			}else if(itemId==300099006){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -productPo.getDiscountsPrice().intValue() * requireCount, GlobalCache.fetchLanguageMap("key2391"),"");
			}
		} else {
			consumeItem(itemId, requireCount, null,GlobalCache.fetchLanguageMap("key2392"));
		}
	}

	/**
	 * 检查是否有道具，如果没有道具，查看钻石是否足够,并扣除（通用）
	 * 
	 * @param consumId
	 */
	public void checkHasItemElseDiamond(Integer consumId, int timesType) {
		ConsumPo consumPo = ConsumPo.findEntity(consumId);
		int count = 0;
		if (consumPo.getItem() != null && consumPo.getItem() != 0) {
			count = fetchItemCount(consumPo.getItem());
		}
		if (consumPo.getItem() != null && consumPo.getItem() != 0 && count > 0) {
			removePackItem(consumPo.getItem(), 1,GlobalCache.fetchLanguageMap("key2387")+consumPo.descripe);
			sendUpdateMainPack(true);
		} else {
			UserPo userPo = UserPo.findEntity(getUserId());
			int consumNum = fetchConsum(consumPo, timesType);
			if(consumPo.getConsumType()==2&&userPo.getDiamond().intValue()>=consumNum){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2388")+consumPo.descripe,"");
			}else if(consumPo.getConsumType()==8){
				checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, consumNum);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2389")+consumPo.descripe,"");
			}
			checkHasAndConsumeResource(consumPo.getConsumType(),
					consumNum);
			sendUpdateTreasure(true);
		}
		adjustConsumCostById(consumId, 1);
	}

	public int fetchItemCount(int itemId) {
		int totalCount = 0;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			if (rolePackItemVo.getItemId() == itemId) {
				totalCount += rolePackItemVo.getNum();
			}
		}
		return totalCount;
	}

	/**
	 * 获取背包和仓库中道具总数量
	 * @param itemId
	 * @param bindState
	 * @return
	 */
	public int fetchItemCountbyBindOrNot(int itemId, int bindState){
		int totalCount = 0;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			if (rolePackItemVo.getItemId().intValue() == itemId && bindState == rolePackItemVo.getBindStatus().intValue()) {
				totalCount += rolePackItemVo.getNum();
			}
		}
		for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
			if (rolePackItemVo.getItemId().intValue() == itemId && bindState == rolePackItemVo.getBindStatus().intValue()) {
				totalCount += rolePackItemVo.getNum();
			}
		}
		return totalCount;
	}
	
	
	
	
	/**
	 * 消耗道具
	 * 
	 * @param itemId
	 * @param requireCount
	 */
	public List<IdNumberVo> consumeItem(int itemId, int requireCount, Integer needNum,String logString) {
		List<IdNumberVo> list = new ArrayList<IdNumberVo>();
		if (requireCount < 0) {
			ExceptionUtil.throwConfirmParamException("below zero");
		}
		Integer count = fetchMainPackItemRemoveKey(itemId, requireCount, 1,logString);
		int num = requireCount-count;
		if(count > 0){
			fetchMainPackItemRemoveKey(itemId, count, 0,logString);
		}
		if(needNum != null){
			for(int i=1; i<= requireCount/needNum; i++){
				if((Math.ceil(1d*num/needNum))>=i){
					list.add(new IdNumberVo(i, 1));
				}else{ 
					list.add(new IdNumberVo(i, 0));
				}
			}			
		}
		return list;
	}
	
	/**
	 * 消耗道具活动需要删除的key
	 * @param itemId
	 * @param requireCount
	 * @param wasBind
	 * @return
	 */
	public Integer fetchMainPackItemRemoveKey(int itemId, int requireCount,Integer wasBind,String logString){
		List<String> toRemoveItems = new ArrayList<String>();;
		Iterator iter = mainPackItemVosMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			RolePackItemVo rolePackItemVo = (RolePackItemVo) entry.getValue();
			if (rolePackItemVo.getItemId().intValue() == itemId && rolePackItemVo.getBindStatus().intValue()==wasBind.intValue()) {
				int reduceCount = Math.min(requireCount, rolePackItemVo.num);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, itemId, -requireCount, logString, "");
				requireCount -= rolePackItemVo.getNum();
				rolePackItemVo.setNum(rolePackItemVo.getNum() - reduceCount);
				if (rolePackItemVo.getNum() <= 0) {
					toRemoveItems.add(key);
				}
				if (requireCount <= 0) {
					break;
				}
			}
		}
		for (String index : toRemoveItems) {
			mainPackItemVosMap.remove(index);
		}
		return requireCount;
	}
	

	/**
	 * 检查需要道具的数量，并且使用掉
	 * 
	 * @param itemId
	 * @param count
	 */
	public void checkHasAndConsumeItem(int itemId, int count,String logString) {
		checkHasItem(itemId, count);
		consumeItem(itemId, count,null,logString);
	}

	/**
	 * 检查需要道具的数量，并且使用掉,如果道具不足使用钻石
	 * 
	 * @param itemId
	 * @param count
	 */
	public void checkHasAndConsumeItemORDiamond(int itemId, int count,String logString) {
		checkHasItemORDiamond(itemId, count,logString);
	}

	public void adjustWingStarExp(int adjustExp) {
		CheckUtil.checkNotBlewZero(adjustExp);
		if (wingStar >= 110) {
			return;
		}
		setWingStarExp(wingStarExp + adjustExp);
		long nextLevelExp = XmlCache.xmlFiles.constantFile.wing.stars.star
				.get(wingStar).starExp;
		int currentLevel = wingStar;
		CheckcCircleBean checkcCircleBean = new CheckcCircleBean();
		while (wingStarExp >= nextLevelExp) {
			checkcCircleBean.count();

			// System.out.println("aa");
			// System.out.println(XmlCache.xmlFiles.constantFile.wing.stars.star.get(wingStar-1).starExp);
			// System.out.println(XmlCache.xmlFiles.constantFile.wing.stars.star.get(wingStar).starExp);
			// System.out.println(XmlCache.xmlFiles.constantFile.wing.stars.star.get(wingStar+1).starExp);
			if (XmlCache.xmlFiles.constantFile.wing.stars.star.get(wingStar).starExp == 0) {
				setWingStarExp(0);
				setWingStepPoss(10);
				return;
			}
			setWingStarExp(wingStarExp
					- XmlCache.xmlFiles.constantFile.wing.stars.star
							.get(wingStar).starExp);
			LogUtil.writeLog(this, 202, this.getWingStar()%11, this.fetchItemCount(ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID), 0, GlobalCache.fetchLanguageMap("key2360")+(this.getWingStar()/11+1), "");
			setWingStar(wingStar + 1);
			taskConditionProgressReplace(getWingStar(),TaskType.TASK_TYPE_CONDITION_733,null,null);
			nextLevelExp = XmlCache.xmlFiles.constantFile.wing.stars.star
					.get(wingStar).starExp;
		}

	}

	/**
	 * 
	 * 方法功能:随英雄 更新时间:2014-6-27, 作者:johnny
	 * 
	 * @param rollType
	 * @param diamondPurpleTimes
	 * @return
	 */
	public Integer rollPet(Integer rollType) {

		boolean specialCard = false;
		// if(diamondPurpleTimes==0){
		specialCard = false;
		// }
		List<Integer> toRandomHeroList = GlobalCache.petRollIdGroup;
		List<Integer> toRandomPertangeList;
		String typeString="";
		if (rollType == PetType.PET_ROLL_DIAMOND) {
			toRandomPertangeList = GlobalCache.petRollDiamondPossGroup1;
			// if(getHeroTotalDiamondRoll()==0){
			// toRandomPertangeList=GlobalCache.heroRollDiamonPossGroup2;
			// }
			// else{
			// toRandomPertangeList=GlobalCache.heroRollDiamonPossGroup1;
			// }
			typeString="钻石抽取";
		} else {
			toRandomPertangeList = GlobalCache.petRollGoldPossGroup1;
			// if(getHeroTotalGiftRoll()==0){
			// toRandomPertangeList=GlobalCache.heroRollGiftPossGroup2;
			// }
			// else{
			// toRandomPertangeList=GlobalCache.heroRollGiftPossGroup1;
			// }
			typeString="金币抽取";
		}

		// if(getHeroTotalDiamondRoll()!=0 &&
		// rollType==HeroType.HERO_ROLL_DIAMOND_CARD && specialCard){
		// toRandomPertangeList=GlobalCache.heroRollSuperGroup;
		// }

		int rolledCard = RandomUtil.randomIntegerByPecentages(toRandomHeroList,
				toRandomPertangeList, null);
		addPet(rolledCard);
		taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_722, null,
				null);
		PetPo petPo = PetPo.findEntity(rolledCard);
		if (petPo.getQuality().intValue() == 5) {
			ChatService chatService = (ChatService) BeanUtil
					.getBean("chatService");
			StringBuffer sb = new StringBuffer();
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
			sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName())
					.append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key49"));
			sb.append(ColourType.fetchColourByQuality(petPo.getQuality()))
					.append(petPo.getName());
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
			chatService.sendHorse(sb.toString());
			chatService.sendSystemWorldChat(sb.toString());
		}
		LogUtil.writeLog(this, 1, 12,rolledCard , 1, typeString+GlobalCache.fetchLanguageMap("key2420"), "");
		return rolledCard;

	}

	public void addPetPublic(int petId) {
		addPet(petId);
	}

	private void addPet(int petId) {
		PetPo petPo = PetPo.findEntity(petId);
		RpetPo rpetPo = new RpetPo();
		rpetPo.setExp(0);
		rpetPo.setLv(1);
		rpetPo.setName(petPo.getName());
		rpetPo.setPetId(petId);
		rpetPo.setStep(0);
		BaseDAO.instance().insert(rpetPo);
		listRpets.add(rpetPo);
		taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_716, petPo.getQuality(), null);
	}
	
	/** 根据宠物品质获取宠物数量*/
	public Integer fetchListRpetsSizeByQuality(Integer quality){
		Integer size = 0;
		for(RpetPo rpetPo : listRpets){
			PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
			if(petPo != null){
				if(petPo.getQuality().intValue() >= quality.intValue()){
					size++;
				}				
			}
		}
		return size;
	}
	/** 获取宠物最大等级*/
	public Integer fetchListRpetsMaxLv(){
		Integer maxLv = 0;
		for(RpetPo rpetPo : listRpets){
			if(rpetPo.getLv().intValue() > maxLv.intValue()){
				maxLv=rpetPo.getLv();
			}
		}
		return maxLv;
	}
	

	// public void removeFigherPetFromMap() {
	// if(fighterPet!=null){
	// fighter.mapRoom.doRemoveMonsterFromStage(fighterPet);
	// }
	// }

	/**
	 * 根据技能Id找到技能升级表
	 * 
	 * @param skillId
	 * @return
	 */
	public UpgradeSkillPo fetchUpgradeSkillPoBySikllId(Integer skillId) {
		List<UpgradeSkillPo> upgradeSkillPos = GameDataTemplate
				.getDataList(UpgradeSkillPo.class);
		UpgradeSkillPo upgradeSkillPo = null;
		for (UpgradeSkillPo usp : upgradeSkillPos) {
			if (usp.getSkillId().intValue() == skillId.intValue()) {
				upgradeSkillPo = usp;
			}
		}
		if (upgradeSkillPo == null) {
			ExceptionUtil
					.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"upgradeSkillPo:" + skillId);
		}
		return upgradeSkillPo;
	}

	/**
	 * 检查宠物背包是否满了
	 */
	public void checkPetPackFull() {
		if (listRpets.size() >= 50) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key51"));
		}
	}

	/**
	 * 检查角色背包是否满了
	 */
	public void checkItemPackFull(Integer num) {
		int freeCellSize = fetchFreePackCellLength();
		int sum = num;
		if (sum > freeCellSize) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key52"));
			return;
		}
	}
	
	/**
	 * 检查仓库是否满了
	 * @param num
	 */
	public void checkWarehousePackItemFull(Integer num){
		PlayItem playTime = fetchPlayItemByType(PlayTimesType.PLAYTIMES_TYPE_440);
		int sum = num +warehousePackItemVosMap.size();
		if ( sum> playTime.initialTimes) {
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key53"));
		}
	}

	/**
	 * 改变坐标
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void ChangeCoordinate(int x, int y, int z) {

		// System.out.println("1change:"+x+"||"+y+"||"+z);
		// System.out.println("2change:"+intX+"||"+intY+"||"+intZ);
		setX(x);
		setY(y);
		setZ(z);
		// System.out.println("3change:"+getX()+"||"+getY()+"||"+getZ());
	}

	/**
	 * 根据类型获得设置状态
	 * 
	 * @param optionsType
	 * @return
	 */
	public Integer fetchOptionsStatusByType(Integer optionsType) {
		int optionsStatus = 0;
		for (IdNumberVo inv : listRoleOptions) {
			if (inv.getId().intValue() == optionsType.intValue()) {
				optionsStatus = inv.getNum();
				// System.out.println("optionsStatus ="+optionsStatus);
				break;
			}
		}
		return optionsStatus;
	}

	/**
	 * 根据类型Id获取时间
	 * 
	 * @param typeId
	 * @return
	 */
	public Long fetchOnlineTimeByTypeId(Long typeId) {
		long dynamicTime = 0l;
		for (IdLongVo2 ilv : listOnlineTime) {
			if (ilv.getLong1().longValue() == typeId.longValue()) {
				dynamicTime = ilv.getLong3();
				break;
			}
		}
		return dynamicTime;
	}

	/**
	 * 修改在线动态时间
	 * 
	 * @param typeId
	 * @param type
	 *            1.增加； 0设置
	 */
	public void updateDynamicOnlineTimeByTypeId(Long typeId, Long baseTime,
			Long dynamicTime, Integer type) {
		for (IdLongVo2 ilv : listOnlineTime) {
			if (ilv.getLong1().longValue() == typeId.longValue()) {
				if (type.intValue() == 1) {
					ilv.addNum(baseTime, dynamicTime);
				} else if (type.intValue() == 0) {
					ilv.setLong2(baseTime);
					ilv.setLong3(dynamicTime);
				}
				break;
			}
		}
	}

	/**
	 * 获取当前在线时间
	 * 
	 * @return
	 */
	public Long fetchCurrentOnlineTime() {
		long currentOnlineTime = 0l;
		if (getLastLoginTime() != null && getLastLoginTime().longValue() != 0) {
			currentOnlineTime = System.currentTimeMillis()
					- getLastLoginTime().longValue();
		}
		return currentOnlineTime;
	}

	/**
	 * 获取红名玩家当前在线时间
	 * 
	 * @return
	 */
	public Long fetchRedCurrentOnlineTime() {
		long currentOnlineTime = 0l;
		if (getLastLoginTime() != null&&getPkRedBeginTime()!=null) {
			if (getPkRedBeginTime().longValue() > getLastLoginTime().longValue()) {
//				System.out.println(name + " 11 " + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) +" | "+DateUtil.getFormatDateBytimestamp(getPkRedBeginTime()));
				currentOnlineTime = System.currentTimeMillis() - getPkRedBeginTime().longValue();
			} else {
//				System.out.println(name + " 22 " + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) +" | "+DateUtil.getFormatDateBytimestamp(getLastLoginTime()));
				currentOnlineTime = System.currentTimeMillis() - getLastLoginTime().longValue();
			}
		}
		return currentOnlineTime;
	}

	public List<IdNumberVo> checkHasAndConsumeItems(List<List<Integer>> listConsumItems, Integer num,String logSting) {
		List<IdNumberVo> listIdNumberVo = null;
		for (List<Integer> list : listConsumItems) {
			checkHasItem(list.get(0), list.get(1)*num);
		}
		for (List<Integer> list : listConsumItems) {
			listIdNumberVo=consumeItem(list.get(0), list.get(1)*num, list.get(1),logSting);
		}
		return listIdNumberVo;
	}

	/**
	 * 初始化创建角色的属性
	 */
	public void initCreateRoleAttribute() {
		checkInitializeActivityInfo();
		checkInitializeAdvanceSuitPlusInfo();
		checkInitializeRechargeInfo();
		checkInitializeYunDartTaskInfo();
		checkInitializeAwardRetrieveVo();
		checkFreshEveryDayBuyProductCount();
		checkFreshDailyPetRollFreeTimes();
		checkFreshDailyGuildContributeTimes();
		checkFreshListCopySceneTodayVisitTimes();
		checkFreshResurnowTimes();
		checkGuildTodayExchangeItems();
		checkOptionsType();
		checkOnlineTiem();
		checkFreshRingTask();
		checkCumulativeLoginDays(null);
		checkInitializeLevelAwardRecord();
		checkInitializeOnlineTimeAwrod();
		checkInitializeSignInAwardRecord();
		checkInitializeDailyLivelyAwardRecord();
		checkInitializeLivelyTask();
		checekInitializeAchieveTase();
		checkInitializeEachFirstRechargeStatus();
		checkInitializeGulidBossAward();
		checkInitializeMilitaryRankRecord();
		checkInitializeProductBuy();
		checkInitInvitationTask();
		initListDiamondBasins();
		taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_731, null,null);
	}

	private void checkInitializeProductBuy() {
		listProductTodayBuyed.clear();
		
	}

	private void checkFreshRingTask() {
		boolean flag = false;
		for (int i = 0; i < openSystemArrayList.size(); i++) {
			if (openSystemArrayList.get(i).intValue() == 21) {
				flag = true;
				break;
			}
		}

		if (flag) {
			for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
				TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
				if (taskPo.getTaskType() == TaskType.TASK_TYPE_RING) {
					listRoleTasks.remove(idNumberVo2);
					break;
				}
			}
			setRingTaskCurrentIndex(1);
			setRingTaskCurrentQuality(IntUtil.getRandomInt(1, 5));
			activeOneRandomRingTask();
		}
	}

	public void activeOneRandomRingTask() {
		List<TaskPo> allTasks = new ArrayList<TaskPo>();
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		boolean lockTask = true;
		if(gp!=null&&"ko".equals(String.valueOf(gp.valueObj))){
			lockTask = false;
		}else if(!DateUtil.isSameDay(getCreateTime(), DateUtil.getCurrentTime(), 0)){
			lockTask = false;
		}
		if(lockTask){
			int[] taskIds = new int[]{630100008,630100007,630100006,630100005,
					630100004,630100003,630100005,630100006,630100007,630100008};
			int taskId = taskIds[getRingTaskCurrentIndex()-1];
			setRingTaskCurrentQuality(IntUtil.getRandomInt(1, 5));
			listRoleTasks.add(new IdNumberVo2(taskId, 0, 0));
		}else {
			List<TaskPo> tasks = GlobalCache.taskListByTypes
					.get(TaskType.TASK_TYPE_RING);
			for (TaskPo taskPo : tasks) {
				if (getLv() >= taskPo.listActiveLv.get(0)
						&& getLv() <= taskPo.listActiveLv.get(1)) {
					allTasks.add(taskPo);
				}
			}
			TaskPo taskPo = (TaskPo) RandomUtil.randomObject(allTasks, null);
			setRingTaskCurrentQuality(IntUtil.getRandomInt(1, 5));
			listRoleTasks.add(new IdNumberVo2(taskPo.getId(), 0, 0));
		}
	}

	/**
	 * 检查登陆重置
	 */
	public void checkLoginReset() {
//		System.out.println("checkLoginReset() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		checkInitializeActivityInfo();
		checkInitializeAdvanceSuitPlusInfo();
		checkSocialItemId();
		checkInitializeAwardRetrieveVo();
		checkInitializeDotaFristAward();
		checkInvitationTask(false);
		checkOpenSystemArrayListByLv(47, 38);
		checkPrestigeTotal();
		checkInitializeBuyPlayTimes();
		if (CheckService.checkRequireDailyFresh(getEveryDayBuyProductResetTime())) {
//			System.out.println(name + " 初始化");
			setActivityLoginState(0);
			setGuildPriestState(0);
			listOnlineTimeAwrodRecord.clear();
			listDailyLivelyAwardRecord.clear();
			listActivityInfo.get(0).theSameDayOnlineTime = 0l;
			listActivityInfo.get(0).sameDayLuckyWheelNumberOfFree = 0;
			listActivityInfo.get(0).takeLuckyWheelFreeNextTime = System.currentTimeMillis();
			listActivityInfo.get(0).signInAwardSameDayIsTake = 0;
			listActivityInfo.get(0).dailyLivelyTaskFinishScore = 0;
			listActivityInfo.get(0).dailyWorshipGoldStatus = 0;
			listActivityInfo.get(0).dailyWorshipDiamondStatus = 0;
			setDomainLastAwardTime(0l);
			setSiegeLastAwardTime(0l);
			listRechargeInfo.get(0).wasTakeDailyVipAward = 0;
			listRechargeInfo.get(0).todayWasTakeMonthCard = 0;
			listYunDartTaskInfoVo.get(0).dailyCurrentFinishYunDartCount = 0;
			listYunDartTaskInfoVo.get(0).dailyCurrentFreeFlushYunDartCarCount = 0;
			initializeDailyBuyPlayTimes();
			checkResetGuildBossFlushTime();
			checkLoginAwardRetrieveVo();
			initCreateRoleAttribute();
			checkDailyTask();
			checkDailyLiveActivity();
			checkMailTimeRemove();
			setResourceSceneTime(1000*60*30);
			setStartResourceSceneTime(System.currentTimeMillis());
			checkGrowFunds2TakeDay();
			setPickCrisitalTodayTimes(0);
			resetConsumCost();
			setSoulExchangeTimes(0);
		}
		liveActivityLogin();			
		checkClientGuildExitTime();
		checkInitTodayExchangeItems();
		fetchSameOnlineTime();
		fetchSameDayLuckyWheelNumberOfFree();
		fetchTakeLuckyWheelFreeNextTime();
		fetchSignInAwardSameDayIsTake();
		fetchDailyLivelyTaskFinishScore(0);
		checkLoginSignInAwardRecord();
		fetchPowerSuitPlusArriveMaxLevel();
		fetchCurrentPowerSuitPlusLevel();
		fetchStarSuitPlusArriveMaxLevel();
		fetchCurrentStarSuitPlusArriveLevle();
		fetchWashSuitPlusMaxLevel();
		fetchWashSuitPlusCurrentLevel();
		fetchWasMonthCard();
		fetchRemainMonthCardDay();
		fetchTodayWasTakeMonthCard();
		fetchCumulativeRechargeNum();
		fetchWasFirstRecharge();
		fetchWasTakeFirstRechargeAwards();
		fetchWasTakeDailyVipAward();
		fetchDailyCurrentFinishYunDartCount();
		fetchDailyCurrentFreeFlushYunDartCarCount();
		fetchCurrentYunDartCarQuality();
		fetchCurrentYunDartCarId();
		checkYunCartTaskTime(null);
		fetchDailyWorshipGoldStatus();
		fetchDailyWorshipDiamondStatus();
		checkOffLineReward();
		long current = System.currentTimeMillis();
		// 最后登录时间
		setLastLoginTime(current);
		// System.out.println(name+"；登录计算的累计登录时间：" +
		// listActivityInfo.get(0).theSameDayOnlineTime/(1000*60));
		checkYunDartFighter();
		initDiamondBasinsValidTime();
		initListDiamondBasins();
		setEveryDayBuyProductResetTime(current);
		checkRemovePackActivatyItem();
		checkpkRedTime();
		removeFashion();
		checkLoginInitFashion();
	}
	
	private void checkDailyLiveActivity() {
		for (RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys) {
			LiveActivityPo liveActivityPo= LiveActivityPo.findEntity(roleLiveActivityVo.liveActivityId);
			if(liveActivityPo!=null){
				//TODO 【业务标记】3是周处理
				if(liveActivityPo.getLoopWay()!=null && liveActivityPo.getLoopWay()!=1){
//					 * 条目列表 索引 进度 状态 已完成次数
					for (IdNumberVo3 idNumberVo3 : roleLiveActivityVo.objs) {
						idNumberVo3.setInt2(0);
						idNumberVo3.setInt4(0);
					}
				}
			}
		}
	}

	/**
	 * 检查每日任务
	 */
	public void checkDailyTask(){
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		String language = String.valueOf(gp.valueObj);
		// 只有越南服增加facebook任务
		if("vi".equals(language)){
			boolean flag = false;
			int taskId=620100020;
			TaskPo taskPo = TaskPo.findEntity(taskId);
			if(taskPo == null){
				return;
			}
			for(IdNumberVo2 idNumberVo2 : listRoleTasks){
				if(idNumberVo2.getInt1().intValue() == taskId){
					flag = true;
					break;
				}
			}
			if(!flag){
				activeRoleTask(taskId);
				acceptedRoleTask(taskId);
			}
		}
	}
	
	
	/** 检查玩家是否有镖车 */
	public void checkYunDartFighter(){
		Fighter yunDartFighter = MapWorld.yunDartFighterMap.get(id);
		if(yunDartFighter != null){
			yunDartCar = yunDartFighter;
			yunDartFighter.changeMasterPlayer(this);
		}
	}

	/** 检查离线奖励 */
	public void checkOffLineReward(){
		listOffLineReward.clear();
		long current = System.currentTimeMillis();
		int minutes =  0;
//		System.out.println(name+ " current="+current +"; "+DateUtil.getFormatDateBytimestamp(current));
//		System.out.println(name+ " lastLog="+lastLogoffTime+"; "+DateUtil.getFormatDateBytimestamp(lastLogoffTime));
		minutes = (int) ((current - lastLogoffTime.longValue())/(1000L*60));
//		System.out.println(name +"; minutes="+minutes);
		adjustOffLineRewardMinutes(minutes);
		if(offLineRewardMinutes > 0){
			OffLineReward offLineReward = fetchOffLineRewardByLv(lv);
			if(offLineReward != null){
				int bindGold = offLineReward.bindGold*offLineRewardMinutes;
				int exp = offLineReward.exp*offLineRewardMinutes;
				listOffLineReward.add(new IdNumberVo(1, bindGold));
				listOffLineReward.add(new IdNumberVo(2, exp));
				listOffLineReward.add(new IdNumberVo(3, offLineRewardMinutes));				
			}
		}
//		System.out.println("listOffLineReward = "+listOffLineReward);
	}
	
	/** 根据等级获取离线奖励对象 */
	public OffLineReward fetchOffLineRewardByLv(Integer roleLv){
		List<OffLineReward> rewardList = XmlCache.xmlFiles.constantFile.offLineRewards.offLineReward;
		OffLineReward offLineReward = null;
		for(OffLineReward olr : rewardList){
			if(roleLv >= olr.minLv  && roleLv <= olr.maxLv){
				offLineReward = olr;
				break;
			}
		}
		return offLineReward;
	}
	
	/**
	 * 检查刷新每日购买商品的数量
	 */
	public void checkFreshEveryDayBuyProductCount() {
		listEveryDayBuyProductCount.clear();
		setEveryDayBuyProductCount(null);
	}

	/**
	 * 重置每日宠物免费抽取次数
	 */
	public void checkFreshDailyPetRollFreeTimes() {
		setPetRollGoldTodayTimes(0);
	}

	/**
	 * 重置每日公会捐赠次数
	 */
	public void checkFreshDailyGuildContributeTimes() {
		setDailyGuildContributeGoldCount(0);
		setDailyGuildContributeItemCount(0);
		setGuildTodayContributed(0);
	}

	/**
	 * 重置副本次数
	 */
	public void checkFreshListCopySceneTodayVisitTimes() {
		listCopySceneTodayVisitTimes.clear();
		// 爬塔今日已挑战次数
		setTowerTodayChallengeTimes(0);
		// 爬塔今日扫荡次数
		setTowerTodayWipeOutTimes(0);
		// 竞技场今天已参加次数
		setArenaTodayPlayedTimes(0);
	}

	/**
	 * 重置公会功勋商店兑换次数
	 */
	public void checkGuildTodayExchangeItems() {
		listGuildTodayExchangeItems = new CopyOnWriteArrayList<IdNumberVo>();
		List<List<Integer>> itemList = ExpressUtil.buildBattleExpressList(XmlCache.xmlFiles.constantFile.guild.exchange.items);
		for (List<Integer> items : itemList) {
			listGuildTodayExchangeItems.add(new IdNumberVo(items.get(0), 0));
		}
	}
	/**
	 * 重置公会功勋商店兑换次数
	 */
	public void checkInitTodayExchangeItems(){
		List<List<Integer>> itemList = ExpressUtil.buildBattleExpressList(XmlCache.xmlFiles.constantFile.guild.exchange.items);
		if(listGuildTodayExchangeItems ==null || listGuildTodayExchangeItems.size()==0 || listGuildTodayExchangeItems.size() != itemList.size()){
			listGuildTodayExchangeItems = new CopyOnWriteArrayList<IdNumberVo>();
			for (List<Integer> items : itemList) {
				listGuildTodayExchangeItems.add(new IdNumberVo(items.get(0), 0));
			}
		}
	}
	
	

	/**
	 * 刷新复活次数
	 */
	public void checkFreshResurnowTimes() {
		setResurnowTodayTimes(0);
		setResurnowContinueTimes(0);
	}

	/**
	 * 初始化设置角色设置类型
	 */
	public void checkOptionsType() {
		if (listRoleOptions == null || listRoleOptions.size() == 0) {
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
			
			listRoleOptions = new CopyOnWriteArrayList<IdNumberVo>();
			for (int i = 0; i < RoleType.options.length; i++) {
				if(RoleType.options[i].intValue() == RoleType.OPTIONS_TYPE_AUTO_JOIN_GUILD && gp!=null && "vi".equals(String.valueOf(gp.valueObj))){
					listRoleOptions.add(new IdNumberVo(RoleType.options[i], 0));	
				}else{
					listRoleOptions.add(new IdNumberVo(RoleType.options[i], 1));					
				}
			}
		}
	}

	/**
	 * 方法功能： 初始化活动属性信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkInitializeActivityInfo() {
		if (listActivityInfo == null || listActivityInfo.size() == 0) {
			ActivityInfoVo aiv = new ActivityInfoVo();
			listActivityInfo.add(aiv);
		}
	}
	
	/**
	 * 方法功能： 初始化奖励找回信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkInitializeAwardRetrieveVo(){
		List<AwardRetrieve> awardRetrieves = XmlCache.xmlFiles.constantFile.awardRetrieves.awardRetrieve;
		Long todayTime = DateUtil.getInitialDate(System.currentTimeMillis());
		if(listAwardRetrieve == null || listAwardRetrieve.size() == 0 || listAwardRetrieve.size() !=awardRetrieves.size()){
			listAwardRetrieve = new CopyOnWriteArrayList<AwardRetrieveVo>();
			for(int i =0; i < awardRetrieves.size(); i++ ){
				AwardRetrieveVo awardRetrieveVo = new AwardRetrieveVo();
				AwardRetrieve ar = awardRetrieves.get(i);
				awardRetrieveVo.id= ar.id;
				awardRetrieveVo.timesType=ar.timesType;
				awardRetrieveVo.openLv=ar.openLv;
				awardRetrieveVo.todayTime=String.valueOf(todayTime);
				int num = fetchPlayItemByType(ar.timesType).initialTimes;
				awardRetrieveVo.baseCount = num;
				awardRetrieveVo.retrieveCount = num;
				if(lv.intValue() >= ar.openLv.intValue()){
					awardRetrieveVo.wasOpen = 1;
				}
				listAwardRetrieve.add(awardRetrieveVo);
			}
		}
//		System.out.println("listAwardRetrieve="+listAwardRetrieve);
	}
	
	/**
	 * 方法功能： 登录初始化奖励找回信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkLoginAwardRetrieveVo(){
		Long todayTime = DateUtil.getInitialDate(System.currentTimeMillis());
		for(AwardRetrieveVo arv : listAwardRetrieve){
			long time = todayTime.longValue() - Long.valueOf(arv.todayTime).longValue();
			if(arv.wasOpen == 1){
				GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyAwardRetrieve);
				RetrieveSystemVo retrieveSystemVo = null;
				if(arv.timesType == PlayTimesType.PLAYTIMES_TYPE_3){
					retrieveSystemVo = globalPo.fetchRetrieveSystemVo(CopySceneType.COPY_SCENE_CONF_KINGOFPK);
				}
				else if(arv.timesType == PlayTimesType.PLAYTIMES_TYPE_4){
					retrieveSystemVo = globalPo.fetchRetrieveSystemVo(CopySceneType.COPY_SCENE_CONF_MONSTERCRISIS);
				}
				if(time > (1000*60*60*24+5000)){
					arv.yesterdayTime = "0";
					arv.yesterdayFinishCount=0;
					arv.todayTime=String.valueOf(todayTime);
					arv.todayFinishCount=0;
					arv.retrieveCount=0;
				}else{
					arv.yesterdayTime = arv.todayTime;
					arv.yesterdayFinishCount=arv.todayFinishCount;
					arv.todayTime=String.valueOf(todayTime);
					arv.todayFinishCount=0;
					arv.retrieveCount=0;
				}
				if(retrieveSystemVo != null && retrieveSystemVo.yesterdayState.intValue()==0){
					arv.retrieveCount = arv.baseCount;
				}
			}
		}
		// baseCount - (yesterdayFinishCount  + retrieveCount)
//		System.out.println(" checkLoginAwardRetrieveVo() "+name+" || "+listAwardRetrieve);
	}
	
	/**
	 * 方法功能：开启奖励找回 修改时间：2015-7-13； 作者：peter
	 */
	public void openAwardRetrieve(){
		for(AwardRetrieveVo arv : listAwardRetrieve){
			if(lv.intValue() >= arv.openLv){
				arv.wasOpen = 1;
			}
		}
		sendUpdateAwardRetrieve();
	}
	
	/**
	 * 方法功能：根据Id查找AwardRetrieveVo 修改时间：2015-7-13； 作者：peter
	 */
	public AwardRetrieveVo fetchAwardRetrieveVoById(Integer timesType,Integer retrieveId){
		AwardRetrieveVo awardRetrieveVo = null;
		if(retrieveId != null){
			for(AwardRetrieveVo arv : listAwardRetrieve){
				if(retrieveId.intValue() == arv.id.intValue()){
					awardRetrieveVo = arv;
					break;
				}
			}			
		}
		if(timesType != null){
			for(AwardRetrieveVo arv : listAwardRetrieve){
				if(timesType.intValue() == arv.timesType.intValue()){
					awardRetrieveVo = arv;
					break;
				}
			}			
		}
		return awardRetrieveVo;
	}
	
	/** 根据类型修改奖励找回的完成次数*/
	public void updateAwardRetrieveCount(Integer param, Integer count){
		AwardRetrieveVo awardRetrieveVo = fetchAwardRetrieveVoById(param, null);
//		System.out.println("id="+awardRetrieveVo.id +"; timesType="+awardRetrieveVo.timesType);
		if(awardRetrieveVo != null){
			awardRetrieveVo.adjustTodayFinishCountCount(count);
			sendUpdateAwardRetrieve();			
		}
//		System.out.println("updateAwardRetrieveCount() name="+name+"; "+ listAwardRetrieve);
	}
	

	/**
	 * 方法功能： 初始化充值相关信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkInitializeRechargeInfo() {
		if (listRechargeInfo == null || listRechargeInfo.size() == 0) {
			RechargeInfoVo riv = new RechargeInfoVo();
			listRechargeInfo.add(riv);
		}
	}

	/**
	 * 方法功能： 初始化运镖信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkInitializeYunDartTaskInfo() {
		if (listYunDartTaskInfoVo == null || listYunDartTaskInfoVo.size() == 0) {
			YunDartTaskInfoVo yunDartTaskInfoVo = new YunDartTaskInfoVo();
			listYunDartTaskInfoVo.add(yunDartTaskInfoVo);
		}
	}

	/**
	 * 方法功能： 初始化套装属性信息 修改时间：2015-7-13； 作者：peter
	 */
	public void checkInitializeAdvanceSuitPlusInfo() {
		if (listAdvanceSuitPlus == null || listAdvanceSuitPlus.size() == 0) {
			AdvanceSuitPlusVo asp = new AdvanceSuitPlusVo();
			listAdvanceSuitPlus.add(asp);
		}
	}

	/**
	 * 初始或化在线时间列表
	 */
	public void checkOnlineTiem() {
		List<OnlineTime> onlineTimes = XmlCache.xmlFiles.constantFile.accumulativeTime.onlineTime;
		if (listOnlineTime == null || listOnlineTime.size() == 0) {
			listOnlineTime = new CopyOnWriteArrayList<IdLongVo2>();
			for (OnlineTime ot : onlineTimes) {
				listOnlineTime.add(new IdLongVo2(ot.id, 0l, 0l));
			}
		}
		if(listOnlineTime.size()<onlineTimes.size()){
			outer:for(OnlineTime ot:onlineTimes){
				for(IdLongVo2 idLongVo2:listOnlineTime){
					if(idLongVo2.getLong1().longValue()==ot.id){
						continue outer;
					}
				}
				listOnlineTime.add(new IdLongVo2(ot.id, 0l, 0l));
			}
		}
	}

	/**
	 * 初始化累计登录奖励
	 */
	public void checkInitializeCumulativeLoginAwardRecord() {
		List<Day> dayList = XmlCache.xmlFiles.constantFile.totalLogin.day;
		if (listCumulativeLoginAwardRecord == null
				|| listCumulativeLoginAwardRecord.size() == 0) {
			for (Day day : dayList) {
				listCumulativeLoginAwardRecord.add(new IdNumberVo(day.id, 0));
			}
		}
	}

	/**
	 * 检查累计登录天数
	 */
	public void checkCumulativeLoginDays(Integer days) {
		if (listActivityInfo.get(0).takeCumulativeLoginTime == null) {
			listActivityInfo.get(0).takeCumulativeLoginTime = 0l;
		}
		long createTime = DateUtil.getInitialDate(listActivityInfo.get(0).takeCumulativeLoginTime);
		// 间隔时间
		long intervalTime = System.currentTimeMillis() - createTime;
		int day = (int) (intervalTime / (1000 * 60 * 60 * 24));
		listCumulativeLoginAwardRecord.clear();
		checkInitializeCumulativeLoginAwardRecord();
		if (day == 1) {
			listActivityInfo.get(0).adjustCumulativeLoginDays(1);
			for (int i = 0; i < listCumulativeLoginAwardRecord.size(); i++) {
				if (listCumulativeLoginAwardRecord.get(i).getNum() == 0
						&& (listCumulativeLoginAwardRecord.get(i).getId()
								.intValue() <= listActivityInfo.get(0).cumulativeLoginDays)) {
					listCumulativeLoginAwardRecord.get(i).setNum(1);
				}
			}
		} else if (day != 0) {
			listActivityInfo.get(0).cumulativeLoginDays = 1;
			listCumulativeLoginAwardRecord.get(0).setNum(1);
		}

		// 作弊命令用
		if (days != null && days != 0) {
			listCumulativeLoginAwardRecord.clear();
			checkInitializeCumulativeLoginAwardRecord();
			listActivityInfo.get(0).cumulativeLoginDays = days;
			for (int i = 0; i < listCumulativeLoginAwardRecord.size(); i++) {
				if (listCumulativeLoginAwardRecord.get(i).getNum() == 0
						&& (listCumulativeLoginAwardRecord.get(i).getId()
								.intValue() <= days)) {
					listCumulativeLoginAwardRecord.get(i).setNum(1);
				}
			}
		}
	}

	/**
	 * 初始化等级奖励
	 */
	public void checkInitializeLevelAwardRecord() {
		if (listLevelAwardRecord == null || listLevelAwardRecord.size() == 0) {
			listLevelAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();
			for (LvConfigPo lvConfigPo : GlobalCache.listLevelAward) {
				listLevelAwardRecord.add(new IdNumberVo(lvConfigPo
						.getPlayerLevel(), 0));
			}
		}
	}

	/**
	 * 检查等级奖励领取状态
	 */
	public void checkLevelAwardRecordState() {
		for (int i = 0; i < listLevelAwardRecord.size(); i++) {
			if (listLevelAwardRecord.get(i).getId().intValue() <= lv.intValue()
					&& listLevelAwardRecord.get(i).getNum().intValue() == 0) {
				listLevelAwardRecord.get(i).setNum(1);
			}
		}
		sendLevelAward();
		// System.out.println(name +
		// "; listLevelAwardRecord == "+listLevelAwardRecord);
	}

	/**
	 * 初始化在线时间奖励
	 */
	public void checkInitializeOnlineTimeAwrod() {
		if (listOnlineTimeAwrodRecord == null
				|| listOnlineTimeAwrodRecord.size() == 0) {
			listOnlineTimeAwrodRecord = new CopyOnWriteArrayList<IdNumberVo>();
			List<OnlineTimes> onlineTimesList = XmlCache.xmlFiles.constantFile.onlineTimeAwrod.onlineTimes;
			for (OnlineTimes ot : onlineTimesList) {
				listOnlineTimeAwrodRecord.add(new IdNumberVo(ot.id, 0));
			}
		}
		// System.out.println("listOnlineTimeAwrodRecord == "+listOnlineTimeAwrodRecord);
	}

	/**
	 * 检查在线奖励状态
	 */
	public void checkOnlineTimeAwrodState(Integer state) {
		int currentOnlineTime = fetchSameOnlineTime();
		List<OnlineTimes> onlineTimesList = XmlCache.xmlFiles.constantFile.onlineTimeAwrod.onlineTimes;
		for (OnlineTimes ot : onlineTimesList) {
			if (currentOnlineTime > ot.onlineTime.intValue()) {
				IdNumberVo idNumberVo = IdNumberVo.findIdNumber(ot.id,
						listOnlineTimeAwrodRecord);
				if (idNumberVo.getNum().intValue() == 0) {
					idNumberVo.setNum(1);
				}
			}else{
				IdNumberVo idNumberVo = IdNumberVo.findIdNumber(ot.id,listOnlineTimeAwrodRecord);
				if(idNumberVo.getNum().intValue() == 1){
					idNumberVo.setNum(0);					
				}
			}
		}
		if (state.intValue() == 1) {
			sendOnlineTimeAward(fetchSameOnlineTime());
		}
		// System.out.println(name +" listOnlineTimeAwrodRecord == "
		// +listOnlineTimeAwrodRecord);
	}

	/**
	 * 检查各种使用次数(副本之类的)
	 * @param currentTimes 当前用掉的次数
	 * @param sum 可以用的总次数
	 */
	public void checkPlayTimes(Integer currentTimes, Integer timesType, String languageKey){
		// 基础次数
		PlayItem playItem = fetchPlayItemByType(timesType);
		// vip 增加次数
		VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
		int flag = vipPo.fetchTypeNumByType(timesType);
		int sum = flag +  playItem.initialTimes;
		// 购买的次数
		IdNumberVo2 idNumberVo2 = fetchBuyPlayItemsByType(timesType);
//		System.out.println("sum="+sum+"; 购买次数："+idNumberVo2.getInt3()+"; vip="+flag+"; init="+playItem.initialTimes+"; currentTimes="+currentTimes);
		if(currentTimes.intValue() >=  (sum+idNumberVo2.getInt3().intValue())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap(languageKey));
		}
	}
	
	public boolean checkPlayTimes(Integer currentTimes, Integer timesType){
		// 基础次数
		PlayItem playItem = fetchPlayItemByType(timesType);
		// vip 增加次数
		VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
		int flag = vipPo.fetchTypeNumByType(timesType);
		int sum = flag +  playItem.initialTimes;
		// 购买的次数
		IdNumberVo2 idNumberVo2 = fetchBuyPlayItemsByType(timesType);
//		System.out.println("sum="+sum+"; 购买次数："+idNumberVo2.getInt3()+"; vip="+flag+"; init="+playItem.initialTimes+"; currentTimes="+currentTimes);
		if(currentTimes.intValue() >  (sum+idNumberVo2.getInt3().intValue())){
			return false;
		}
		return true;
	}
	
	/**
	 * 检查副本次数
	 * @param currentTimes
	 * @param sceneId
	 */
	public void checkSceneTimesBySceneId(Integer currentTimes, Integer sceneId, Integer otherSum){
		if(sceneId.intValue() == PKGreatRoom.SCENE_ID){
			checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_3, "key214");
		}else if(sceneId.intValue() == DemonizationCrisisRoom.SCENE_ID){
			checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_4, "key214");
		}else if(sceneId.intValue() == ZaphieHaramRoom.SCENE_ID){
			checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_5, "key214");
		}else if(sceneId.intValue() == BloodSeekerBastionRoom.SCENE_ID){
			checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_6, "key214");
		}else if(sceneId.intValue() == TeamTowerRoom.SCENE_ID){
			checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_7, "key214");
		}else{
			if(currentTimes.intValue() >= otherSum.intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key214"));
			}
		}
	}
	
	public boolean checkSceneTimes(Integer currentTimes, Integer sceneId, Integer otherSum){
		boolean retB = true;
		if(sceneId.intValue() == PKGreatRoom.SCENE_ID){
			retB = checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_3);
		}else if(sceneId.intValue() == DemonizationCrisisRoom.SCENE_ID){
			retB = checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_4);
		}else if(sceneId.intValue() == ZaphieHaramRoom.SCENE_ID){
			retB = checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_5);
		}else if(sceneId.intValue() == BloodSeekerBastionRoom.SCENE_ID){
			retB = checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_6);
		}else if(sceneId.intValue() == TeamTowerRoom.SCENE_ID){
			retB = checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_7);
		}else{
			if(currentTimes.intValue() > otherSum.intValue()){
				retB = false;
			}
		}
		return retB;
	}
	
	
	
	/**
	 * 获取当前同一天的在线时间
	 */
	public int fetchSameOnlineTime() {
		currentSameDayOnlineTime = (int) ((listActivityInfo.get(0).theSameDayOnlineTime + fetchCurrentOnlineTime()) / 1000);
		// System.out.println("currentSameDayOnlineTime="
		// +currentSameDayOnlineTime);
		return currentSameDayOnlineTime;
	}
	

	
	/**
	 * 获取每天金币膜拜状态
	 */
	public int fetchDailyWorshipGoldStatus(){
		dailyWorshipGoldStatus = listActivityInfo.get(0).dailyWorshipGoldStatus;
		return dailyWorshipGoldStatus;
	}
	
	/**
	 * 获取每天钻石膜拜状态
	 */
	public int fetchDailyWorshipDiamondStatus(){
		dailyWorshipDiamondStatus = listActivityInfo.get(0).dailyWorshipDiamondStatus;
		return dailyWorshipDiamondStatus;
	}

	/**
	 * 获取同一天幸运转盘免费的次数
	 */
	public int fetchSameDayLuckyWheelNumberOfFree() {
		currentSameDayLuckyWheelNumberOfFree = listActivityInfo.get(0).sameDayLuckyWheelNumberOfFree;
		return currentSameDayLuckyWheelNumberOfFree;
	}

	public int fetchTakeLuckyWheelFreeNextTime() {
		currentTakeLuckyWheelFreeNextTime = (int) (listActivityInfo.get(0).takeLuckyWheelFreeNextTime / 1000);
		return currentTakeLuckyWheelFreeNextTime;
	}

	public int fetchSignInAwardSameDayIsTake() {
		signInAwardSameDayIsTake = listActivityInfo.get(0).signInAwardSameDayIsTake;
		return signInAwardSameDayIsTake;
	}

	/**
	 * 强化套装加成到达最大等级
	 */
	public int fetchPowerSuitPlusArriveMaxLevel() {
		powerSuitPlusArriveMaxLevel = listAdvanceSuitPlus.get(0).powerSuitPlusArriveMaxLevel;
		return powerSuitPlusArriveMaxLevel;
	}

	/**
	 * 当前强化套装加成等级
	 */
	public int fetchCurrentPowerSuitPlusLevel() {
		currentPowerSuitPlusLevel = listAdvanceSuitPlus.get(0).currentPowerSuitPlusLevel;
		return currentPowerSuitPlusLevel;
	}

	/**
	 * 升星套装加成到达最大等级
	 */
	public int fetchStarSuitPlusArriveMaxLevel() {
		starSuitPlusArriveMaxLevel = listAdvanceSuitPlus.get(0).starSuitPlusArriveMaxLevel;
		return starSuitPlusArriveMaxLevel;
	}

	/**
	 * 当前升星套装加成等级
	 */
	public int fetchCurrentStarSuitPlusArriveLevle() {
		currentStarSuitPlusArriveLevle = listAdvanceSuitPlus.get(0).currentStarSuitPlusArriveLevle;
		return currentStarSuitPlusArriveLevle;
	}
	
	
	/**
	 * 洗练套装加成达到最大等级
	 */
	public int fetchWashSuitPlusMaxLevel(){
		washSuitPlusMaxLevel = listAdvanceSuitPlus.get(0).washSuitPlusMaxLevel;
		return washSuitPlusMaxLevel;
	}
	
	/**
	 * 当前洗练套装加成等级
	 */
	public int fetchWashSuitPlusCurrentLevel(){
		washSuitPlusCurrentLevel = listAdvanceSuitPlus.get(0).washSuitPlusCurrentLevel;
		return washSuitPlusCurrentLevel;
	}

	/**
	 * 是否是月卡
	 */
	public int fetchWasMonthCard() {
		wasMonthCard = listRechargeInfo.get(0).wasMonthCard;
		return wasMonthCard;
	}

	/**
	 * 剩余月卡天数
	 */
	public int fetchRemainMonthCardDay() {
		remainMonthCardDay = listRechargeInfo.get(0).remainMonthCardDay;
		return remainMonthCardDay;
	}

	/**
	 * 今天是否领取了月卡
	 */
	public int fetchTodayWasTakeMonthCard() {
		todayWasTakeMonthCard = listRechargeInfo.get(0).todayWasTakeMonthCard;
		return todayWasTakeMonthCard;
	}

	/**
	 * 累计充值数量
	 */
	public int fetchCumulativeRechargeNum() {
		UserPo userPo = UserPo.findEntity(getUserId());
//		cumulativeRechargeNum = listRechargeInfo.get(0).cumulativeRechargeNum;
		cumulativeRechargeNum = userPo.getCumulativeRechargeNum();
		return cumulativeRechargeNum;
	}

	/**
	 * 是否首次充值
	 */
	public int fetchWasFirstRecharge() {
		wasFirstRecharge = listRechargeInfo.get(0).wasFirstRecharge;
		return wasFirstRecharge;
	}

	/**
	 * 是否领取首次充值奖励
	 */
	public int fetchWasTakeFirstRechargeAwards() {
		wasTakeFirstRechargeAwards = listRechargeInfo.get(0).wasTakeFirstRechargeAwards;
		return wasTakeFirstRechargeAwards;
	}

	/**
	 * 是否领取每日vip奖励
	 */
	public int fetchWasTakeDailyVipAward() {
		wasTakeDailyVipAward = listRechargeInfo.get(0).wasTakeDailyVipAward;
		return wasTakeDailyVipAward;
	}

	/**
	 * 每天当前完成次数
	 */
	public int fetchDailyCurrentFinishYunDartCount() {
		dailyCurrentFinishYunDartCount = listYunDartTaskInfoVo.get(0).dailyCurrentFinishYunDartCount;
		return dailyCurrentFinishYunDartCount;
	}

	/**
	 * 每天当前免费刷新了的镖车次数
	 */
	public int fetchDailyCurrentFreeFlushYunDartCarCount() {
		dailyCurrentFreeFlushYunDartCarCount = listYunDartTaskInfoVo.get(0).dailyCurrentFreeFlushYunDartCarCount;
		return dailyCurrentFreeFlushYunDartCarCount;
	}

	/**
	 * 当前运镖车的品质 -1：没有运镖车 0：破损的物资车 1：白色物资车 2：绿色物资车 3：蓝色物资车 4：紫色物资车 5：橙色物资车
	 */
	public int fetchCurrentYunDartCarQuality() {
		currentYunDartCarQuality = listYunDartTaskInfoVo.get(0).currentYunDartCarQuality;
		return currentYunDartCarQuality;
	}

	/**
	 * 当前运镖车Id -1：没有运镖车
	 */
	public int fetchCurrentYunDartCarId() {
		currentYunDartCarId = listYunDartTaskInfoVo.get(0).currentYunDartCarId;
		return currentYunDartCarId;
	}

	/**
	 * 根据Id获取在线奖励对象
	 * 
	 * @param onlineId
	 * @return
	 */
	public OnlineTimes fetchOnlineTimesById(Integer onlineId) {
		List<OnlineTimes> onlineTimesList = XmlCache.xmlFiles.constantFile.onlineTimeAwrod.onlineTimes;
		OnlineTimes onlineTimes = null;
		for (OnlineTimes ot : onlineTimesList) {
			if (ot.id.intValue() == onlineId.intValue()) {
				onlineTimes = ot;
				break;
			}
		}

		return onlineTimes;
	}

	/**
	 * 初始化签到列表
	 */
	public void checkInitializeSignInAwardRecord() {
		if (listSignInAwardRecord == null || listSignInAwardRecord.size() == 0) {
			listSignInAwardRecord = new CopyOnWriteArrayList<IdNumberVo2>();
			int month = DateUtil.getTodayMonth(System.currentTimeMillis());
			List<MonthItem> monthItemList = XmlCache.xmlFiles.constantFile.monthAward.monthItem;
			List<List<Integer>> dropList = StringUtil
					.buildBattleExpressList(monthItemList.get(month-1).award);
			for (int i = 0; i < dropList.size(); i++) {
				listSignInAwardRecord.add(new IdNumberVo2(month, i, 0));
			}
		}
	}

	/**
	 * 检查每天登录签到列表是否更新
	 */
	public void checkLoginSignInAwardRecord() {
		int month = DateUtil.getTodayMonth(System.currentTimeMillis());
		// System.out.println("month == " +month);
		if (month != listSignInAwardRecord.get(0).getInt1().intValue()) {
			listSignInAwardRecord.clear();
			checkInitializeSignInAwardRecord();
			listActivityInfo.get(0).signInAwardCount = 0;
		}
	}
	
	/** 初始化刀塔首次通关奖励 */
	public void checkInitializeDotaFristAward(){
		if(listDotaFirstAward == null || listDotaFirstAward.size() == 0){
			listDotaFirstAward = new CopyOnWriteArrayList<IdNumberVo>();
			List<Layer> layerList = XmlCache.xmlFiles.constantFile.dota.layer;
			for(Layer layer : layerList){
				String firstAwardExp = DBFieldUtil.fetchImpodString(layer.firstAwardExp);
				if(firstAwardExp != null){
					listDotaFirstAward.add(new IdNumberVo(layer.lv, 0));
				}
			}			
		}
		
	}
	
	/**
	 * 获取补签日期
	 * @return
	 */
	public IdNumberVo2 fetchSignedSupplementDate(){
		IdNumberVo2 inv2 = null;
		for(IdNumberVo2 idNumberVo2 : listSignInAwardRecord){
//			System.out.println("idNumberVo2 = "+ idNumberVo2);
			if(idNumberVo2.getInt3().intValue() == 0){
				inv2 = idNumberVo2;
				break;
			}
		}
		return inv2;
	}

	/**
	 * 初始化活跃度列表
	 */
	public void checkInitializeDailyLivelyAwardRecord() {
		if (listDailyLivelyAwardRecord == null
				|| listDailyLivelyAwardRecord.size() == 0) {
			listDailyLivelyAwardRecord = new CopyOnWriteArrayList<IdNumberVo>();
			List<LivelyItem> livelyItemList = XmlCache.xmlFiles.constantFile.livelyAward.livelyItem;
			for (int i = 0; i < livelyItemList.size(); i++) {
				listDailyLivelyAwardRecord.add(new IdNumberVo(livelyItemList
						.get(i).id, 0));
			}
		}
	}

	/**
	 * 初始化公会boss奖励
	 */
	public void checkInitializeGulidBossAward() {
		if (listGuildBossAward == null || listGuildBossAward.size() == 0) {
			listGuildBossAward = new CopyOnWriteArrayList<IdNumberVo>();
			List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
			for (int i = 0; i < listBoss.size(); i++) {
				listGuildBossAward.add(new IdNumberVo(
						listBoss.get(i).copysceneconfid, 0));
			}
		}
		// System.out.println(listGuildBossAward);
	}

	/**
	 * 初始化每一项第一次充值状态
	 */
	public void checkInitializeEachFirstRechargeStatus() {
		if (listEachFirstRechargeStatus == null|| listEachFirstRechargeStatus.size() == 0) {
			listEachFirstRechargeStatus = new CopyOnWriteArrayList<IdNumberVo>();
			GameDataTemplate gameDataTemplate = (GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<RechargePo> list = gameDataTemplate.getDataList(RechargePo.class);
			List<Integer> listGroup = new ArrayList<Integer>();
			for (RechargePo rechargePo : list) {
				if(!listGroup.contains(rechargePo.getGroupId())){
					listGroup.add(rechargePo.getGroupId());
				}
			}
			for(Integer i : listGroup){
				listEachFirstRechargeStatus.add(new IdNumberVo(i, 0));				
			}
		}
	}

	/**
	 * 方法功能:检查每日活跃度任务完成积分情况 更新时间:2015-7-10, 作者:peter
	 * 
	 */
	public int fetchDailyLivelyTaskFinishScore(Integer state) {
		dailyLivelyTaskFinishScore = 0;
		for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
			TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
//			 if(taskPo==null){
//				 ExceptionUtil.throwConfirmParamException("任务不存在:"+idNumberVo2.getInt1());
//			 }
			if (idNumberVo2.getInt2().intValue() >= taskPo.conditionVals.get(1)
					&& taskPo.getTaskType().intValue() == TaskType.TASK_TYPE_LIVELY) {
				setDailyLivelyTaskFinishScore(dailyLivelyTaskFinishScore+taskPo.getDailyScore());
			}
		}
		List<LivelyItem> livelyItemList = XmlCache.xmlFiles.constantFile.livelyAward.livelyItem;
		for (int i = 0; i < livelyItemList.size(); i++) {
			if (dailyLivelyTaskFinishScore.intValue() >= livelyItemList.get(i).needScore
					.intValue()
					&& listDailyLivelyAwardRecord.get(i).getNum().intValue() == 0) {
				listDailyLivelyAwardRecord.get(i).setNum(1);
			}
		}
		if (state != null && state.intValue() == 1) {
			sendUpdateDailyLivelyAward();
		}
		listActivityInfo.get(0).dailyLivelyTaskFinishScore = dailyLivelyTaskFinishScore;
		return dailyLivelyTaskFinishScore;
	}

	/**
	 * 充值通用接口
	 * @param rechargeId 品项Id
	 * @param rechargeMoney 充值金额 （可以和数据库里不一样）
	 * @param rechargeDiamond 奖励钻石 （可以和数据库里不一样）
	 * @param percent 加成百分比
	 * @return 返回是否首次充值
	 */
	public Integer rechargeSendByRechargeId(Integer rechargeId, Double rechargeMoney,Integer rechargeDiamond,double percent) {
		PrintUtil.print(id+" | "+name +" | "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+ " rechargeSendByRechargeId() rechargeId="+rechargeId+"; rechargeMoney="+rechargeMoney+"; rechargeDiamond="+rechargeDiamond+";percent="+percent);
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
//		RechargePo rechargePo = RechargePo.findEntity(rechargeId);
		int lv = getVipLv();
		// 基础钻石
		int baseDiamond = 0;
		// 需要奖励的总钻石
		int totalDiamond = 0;
		// rechargeId,rechargeMoney,rechargeDiamond 这三个值和数据库里相等才按品项充值
//		System.out.println("rechargePo.getRechargeRmb() ="+rechargePo.getRechargeRmb()+"; rechargeMoney="+rechargeMoney);
//		System.out.println("rechargePo.getRechargeNum() ="+rechargePo.getRechargeNum()+"; rechargeDiamond="+rechargeDiamond);
//		if(rechargePo == null){
//		}
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		String language = String.valueOf(gp.valueObj);
		RechargePo rechargePo = null;
		if("vi".equals(language)){
			rechargePo=GlobalCache.fecthRechargePo(rechargeDiamond);						
		}else if("ko".equals(language)){
			rechargePo=RechargePo.findEntity(rechargeId);
		}else{
			rechargePo=RechargePo.findEntity(rechargeId);
		}
		int firstCharge=1;
		if (rechargePo!= null) {
			baseDiamond = rechargePo.getRechargeNum();
			boolean wasFirst = false;
			for (IdNumberVo inv : listEachFirstRechargeStatus) {
				if (rechargePo.getGroupId().intValue() == inv.getId().intValue()&& inv.getNum().intValue() == 0) {
					int doubleDiamond = rechargePo.getRechargeNum() * 1;
					adjustNumberByType(doubleDiamond,RoleType.RESOURCE_BIND_DIAMOND);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMOND, 0, doubleDiamond, GlobalCache.fetchLanguageMap("key2624"), "");
					inv.setNum(1);
					wasFirst = true;
					firstCharge=0;
					break;
				}
			}
			if(wasFirst){
				totalDiamond += rechargePo.getRechargeNum()+ (rechargePo.getRechargeNum() * percent);		
			}else{
				totalDiamond += (rechargePo.getRechargeNum()+rechargePo.getAttachNum())+ (rechargePo.getRechargeNum() * percent);				
			}
		}else{
			baseDiamond = rechargeDiamond;
			totalDiamond = rechargeDiamond;
		}
		listRechargeInfo.get(0).adjustCumulativeRechargeNum(baseDiamond);
		UserPo userPo = UserPo.findEntity(getUserId());
		userPo.adjustCumulativeRechargeNum(baseDiamond);
		userPo.checkDiamondBasinsState();
		setTotalDiamondCharged(totalDiamondCharged+baseDiamond);
		adjustNumberByType(totalDiamond,RoleType.RESOURCE_DIAMOND);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, totalDiamond, GlobalCache.fetchLanguageMap("key2623"), "");
		liveActivityCharge(baseDiamond);
		liveActivityRankCharge(baseDiamond);
		
		checkVipLv();
		//设备首冲  count=0，不是首充>=1;
		int count=1;
		int wasFirstRecharge = 0;
		if (listRechargeInfo.get(0).wasFirstRecharge == 0) {
			setFirstChargeDiamond(rechargeDiamond);
			setFirstChargeRoleLv(getLv());
			
			listRechargeInfo.get(0).wasFirstRecharge = 1;
			wasFirstRecharge = 1;
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_role where device_id='").append(deviceId).append("'").append(" and first_recharge_state=1");
//			System.out.println("sb="+sb);
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			setFirstRechargeState(1);
		}
		if (getVipLv().intValue() > lv) {
			StringBuffer sb = new StringBuffer();
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key55")).append(getName())
					.append(GlobalCache.fetchLanguageMap("key56")).append(getVipLv()).append(GlobalCache.fetchLanguageMap("key57"));
			ChatService chatService = (ChatService) BeanUtil
					.getBean("chatService");
			chatService.sendHorse(sb.toString());
			chatService.sendSystemWorldChat(sb.toString());
		}
		sendUpdateRechargeInfo();
		sendUpdateTreasure(true);
		sendUpdateClientRechargeDiamond(rechargeMoney, baseDiamond);
		sendUpdateListEachFirstRechargeStatus();
		//以分为单位的钱
		int money = (int) (rechargeMoney*100);
		LogUtil.writeLog(this, 307, firstCharge, count, money,  name+GlobalCache.fetchLanguageMap("key2419"),deviceId);
		return  wasFirstRecharge;
	}

	/** 检查vip等级 */
	public void checkVipLv(){
		List<VipPo> listVipPo = GlobalCache.listVipPo;
		UserPo userPo = UserPo.findEntity(getUserId());
		int vipLv = userPo.getVipLv();
		for (int i = 0; i < listVipPo.size(); i++) {
			VipPo vipPo = listVipPo.get(i);
			if (userPo.getCumulativeRechargeNum().intValue() >= vipPo.getRmbNeed().intValue() * 10) {
				vipLv = Math.max(vipLv, vipPo.getVipLv());
			}
		}
		setVipLv(vipLv);
		userPo.setVipLv(vipLv);
	}
	
	/**
	 * 检查社交队伍Id
	 */
	public void checkSocialItemId() {
		if (socialTeamId != null && socialTeamId != 0) {
			TeamVo teamVo = TeamVo.findTeam(socialTeamId, 0);
			if (teamVo != null) {
				teamMemberVo = teamVo.findTeamMember(id);
			}
		}
	}

	public void freshTaskNewStatus(Integer activeTaskId) {
		IdNumberVo2 idNumberVo =  fetchRoleTask(activeTaskId);
		int newed = 1;
		TaskPo taskPo = TaskPo.findEntity(idNumberVo.getInt1());
		if (taskPo.conditionVals.get(0).intValue() != TaskType.TASK_TYPE_CONDITION_730) {
			idNumberVo.setInt3(TaskType.TASK_NEW_TASK_NEW);
		}
		for (int i = listRoleTasks.size() - 1; i >= 0; i--) {
			IdNumberVo2 idNumberVo2 = listRoleTasks.get(i);
			if (idNumberVo2.getInt1().intValue() == idNumberVo.getInt1()) {
				continue;
			}
			if (newed >= 2) {
				idNumberVo2.setInt3(TaskType.TASK_NEW_TASK_NONE);
				continue;
			}
			if (idNumberVo2.getInt3() == TaskType.TASK_NEW_TASK_NEW) {
				newed++;
			}
		}
	}

	/**
	 * 
	 * 方法功能:同步角色信息 更新时间:2014-6-27, 作者:johnny
	 */
	public void syncToInfor() {
		try {
			RoleInforPo roleInforPo = RoleInforPo.findEntity(roleInforId);
			if (roleInforPo != null) {
				roleInforPo.setRoleName(getName());
				roleInforPo.setRoleLv(getLv());
				roleInforPo.setBattlePower(getBattlePower());
				roleInforPo.setLastLoginTime(getLastLoginTime());
				fetchRoleOnlineStatus();
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

	/**
	 * 获取角色在线状态
	 */
	public boolean fetchRoleOnlineStatus() {
		RoleInforPo roleInforPo = RoleInforPo.findEntity(roleInforId);
		if (RoleTemplate.roleIdIuidMapping.containsKey(id)) {
			roleInforPo.setOnlineStatus(1);
			return true;
		} else {
			roleInforPo.setOnlineStatus(0);
			return false;
		}
	}

	/**
	 * 是否是机器人
	 * 
	 * @return
	 */
	public boolean wasRobot() {
		return robot;
	}

	public void adjustPkValue(int i) {
//		 System.out.println(name + "; PkValue="+getPkValue()+"; adjustPkValue i = " +i);
		int num = getPkValue() + i;
		if(num < 0){
			num = 0;
		}
		setPkValue(num);
		if (fighter.rolePo != null) {
			fighter.swithPkStatus(Fighter.PK_STATUS_RED,i);
		}
	}

	public void sendUpdateRoleBatProps(boolean flush) {
		singleRole("PushRemoting.updateRoleBatPros", new Object[] {
				batMeleeAttackMin, batMagicAttackMin, batMeleeDefenceMin,
				batMagicDefenceMin, batMeleeAttackMax, batMagicAttackMax,
				batMeleeDefenceMax, batMagicDefenceMax, batHp, batMp,
				batCritical, batHpReg, batMpReg, batMovement, batDodge,
				batMaxHp, batMaxMp, batLuckyAttack, batHitRate,
				batMeleeDefence, batMagicDefence, batLuckyDefence, batTough,
				batDamageRate, batDamageResistRate, batCriticalDamageRate,
				batAttackRate, batDefenceRate, batMeleeAttack, batMagicAttack },flush);
	}

	/**
	 * 添加一个新的宠物星座
	 * 
	 * @param constell
	 */
	public void saveNewPetConstell(PetConstell constell, boolean isSend) {
		List<PetConstellVo> pcVoList = petConstellMap.get(constell.constellId);
		if (null == pcVoList) {
			pcVoList = new ArrayList<PetConstellVo>();
			petConstellMap.put(constell.constellId, pcVoList);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(constell.constellId + "|0|");
		petConstellStrengthenMap.put(constell.constellId, 0);
		ConstellAttachVo caVo = GlobalCache.petConstellAttachVo
				.get(constell.constellId);
		int attId = 0;
		String attachValue = "";
		boolean bl = true;
		for (int i = 0; i < 3; i++) {
			Integer attachId = caVo.getRandomAttach();
			PetAttach petAttach = GlobalCache.petAttach.get(1).get(attachId);
			String attach = attachId + ":" + petAttach.attachLevel;
			PetConstellVo petConstellVo = new PetConstellVo();
			petConstellVo.loadProperty(attach, ":");
			pcVoList.add(petConstellVo);

			sb.append(attach + "@");
			attachValue = petAttach.attachValue;
			if (attId == 0)
				attId = attachId;
			else if (attId != attachId)
				bl = false;
		}

		String str = "";
		// 三属性类型相同，出发第四属性
		if (bl) {
			PetConstellVo petConstellVo = new PetConstellVo();
			String attach = attId + ":" + 1;
			petConstellVo.loadProperty(attach, ":");
			pcVoList.add(petConstellVo);
			sb.append(attach);
			str = sb.toString();
		} else {
			str = sb.toString();
			str = str.substring(0, str.length() - 1);
		}
		// 更新数据
		if (StringUtil.isEmpty(this.petConstell))
			this.setPetConstell(str);
		else
			this.setPetConstell(this.getPetConstell() + "," + str);
		transformPet();
		if (isSend)
			sendUpdatePetConstell(constell.constellId);
	}

	/**
	 * 更新星座属性
	 * 
	 * @param constellId
	 *            星座ID
	 * @param strengthen
	 *            强化次数
	 * @param pcVoList
	 *            星座属性
	 */
	public void updatePetConstell(int constellId, int strengthen,
			List<PetConstellVo> pcVoList) {
		petConstellMap.put(constellId, pcVoList);
		StringBuilder sb = new StringBuilder();
		for (PetConstellVo pcVo : pcVoList) {
			sb.append(pcVo.attachType + ":" + pcVo.attachLevel + "@");
		}
		String str = sb.toString();
		str = str.substring(0, str.length() - 1);
		String[] pcs = petConstell.split(",");
		sb = new StringBuilder();
		for (String pc : pcs) {
			String[] vals = pc.split("\\|");
			if (Integer.parseInt(vals[0]) == constellId)
				sb.append(vals[0] + "|" + strengthen + "|" + str + ",");
			else
				sb.append(pc + ",");
		}
		str = sb.toString();
		str = str.substring(0, str.length() - 1);
		this.setPetConstell(str);
		petConstellStrengthenMap.put(constellId, strengthen);
		transformPet();
		sendUpdatePetConstell(constellId);
	}

	/**
	 * 添加一个新的宠物天赋
	 * 
	 * @param constell
	 */
	public void saveNewPetTalent(Integer talentId) {
		listPetTalent.add(talentId);
		if (StringUtil.isEmpty(this.getPetTalent()))
			this.setPetTalent("" + talentId);
		else
			this.setPetTalent(this.getPetTalent() + "|" + talentId);
		sendUpdatePetTalent();
	}

	/**
	 * 更新天赋
	 */
	public void updatePetTalent() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, max = listPetTalent.size(); i < max; i++) {
			if (i != 0)
				sb.append("|");
			sb.append(listPetTalent.get(i));
		}
		this.setPetTalent(sb.toString());
		sendUpdatePetTalent();
	}

	/**
	 * 获取运镖车
	 * 
	 * @return
	 */
	public Cart fetchYunCart() {
		List<Cart> cartList = XmlCache.xmlFiles.constantFile.trade.cart;
		List<List<Integer>> baseList = new ArrayList<List<Integer>>();
		for (Cart cart : cartList) {
			List<Integer> list = new ArrayList<Integer>();
			list.add(cart.quality);
			list.add(1);
			list.add(cart.freshWeight);
			baseList.add(list);
		}
		IdNumberVo qualityId = RandomUtil.calcWeightOverCardAward(baseList,null);
		Cart cart = cartList.get(qualityId.getId().intValue());
		listYunDartTaskInfoVo.get(0).currentYunDartCarQuality = cart.quality;
		LogUtil.writeLog(this, 351, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		return cart;
	}

	/**
	 * 检查运镖车任务时间
	 * 
	 * @param taskId
	 */
	public void checkYunCartTaskTime(Integer taskId) {
		CopySceneActivityPo csap = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_YUN_DART);
		if (csap == null) {
			return;
		}
		int currentTime = (int) (System.currentTimeMillis() / 1000);
//		if(taskId != null){
//			IdNumberVo2 idNumberVo =  fetchRoleTask(taskId);
//			System.out.println(getName() + "; checkYunCartTaskTime()镖车结束时间："+DateUtil.getFormatDateBytimestamp(1L*1000*idNumberVo.getInt3()));			
//		}
		if (currentTime >= listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime.intValue()) {	
			initYunDartInfo(taskId);
		}
	}

	/**
	 * 初始化运镖信息
	 * 
	 * @param taskId
	 */
	public void initYunDartInfo(Integer taskId) {
//		System.out.println("initYunDartInfo(): "+name+" "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		if (taskId == null) {
			for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
				TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
				if (taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730) {
					taskId = idNumberVo2.getInt1();
					break;
				}
			}
		}
		if (fighter != null && yunDartCar != null) {
			BattleMsgUtil.abroadyunDartCarRemove(yunDartCar, fighter);
			fighter.mapRoom.cellData.removeLiving(yunDartCar, true);
		}
		yunDartCar = null;
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if (taskPo == null) {
			return;
		}
		IdNumberVo2 idNumberVo = fetchRoleTask(taskId);
		idNumberVo.setInt2(TaskType.TASK_STATUS_ACTIVED);
		if (taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730) {
			MapRoom mapRoom = MapWorld.findStage(getRoomId());
			Fighter mover = mapRoom.findMoverId(listYunDartTaskInfoVo.get(0).currentYunDartCarId);
			if (mover != null) {
				mapRoom.cellData.removeLiving(mover, true);
			}
			idNumberVo.setInt3(0);
			listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime = 0;
			listYunDartTaskInfoVo.get(0).currentYunDartCarId = -1;
			listYunDartTaskInfoVo.get(0).currentYunDartCarQuality = -1;
			listYunDartTaskInfoVo.get(0).yunDartState=0;
			fetchCurrentYunDartCarQuality();
			fetchCurrentYunDartCarId();
			fetchDailyCurrentFinishYunDartCount();
		}
	}

	/**
	 * 获得公会boss对象
	 * 
	 * @param copysceneconfId
	 * @return
	 */
	public Boss fetchGuildBoss(Integer copysceneconfId) {
		List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
		for (Boss boss : listBoss) {
			if (copysceneconfId.intValue() == boss.copysceneconfid.intValue()) {
				return boss;
			}
		}
		return null;
	}

	/**
	 * 根据类型获取游戏基础次数
	 * 
	 * @return
	 */
	public PlayItem fetchPlayItemByType(Integer timesType) {
		List<PlayItem> playItemList = XmlCache.xmlFiles.constantFile.playTimes.playItem;
		for (PlayItem playItem : playItemList) {
			if (playItem.timesType == timesType.intValue()) {
				return playItem;
			}
		}
		return null;
	}
	/**
	 * 根据类型获取游戏各种购买次数
	 * @return
	 */
	public IdNumberVo2 fetchBuyPlayItemsByType(Integer timesType){
		IdNumberVo2 idNumberVo2 = null;
		for(IdNumberVo2 idv2 : listBuyPlayTimes){
			if(idv2.getInt1().intValue() == timesType.intValue()){
				idNumberVo2 = idv2;
				break;
			}
		}
		return idNumberVo2;
	}
	

	/**
	 * 检查副本所需要达到的条件
	 * 
	 * @param copySceneConfigId
	 */
	public void checkCopySceneConfig(Integer copySceneConfigId) {
		CopySceneConfPo copySceneConfPo = CopySceneConfPo
				.findEntity(copySceneConfigId);
		if (getLv().intValue() < copySceneConfPo.getRequireLv().intValue()) {
			CopyScenePo copyScenePo = CopyScenePo.findEntity(copySceneConfPo
					.getCopySceneId());
			ExceptionUtil.throwConfirmParamException(copyScenePo.getName()
					+ GlobalCache.fetchLanguageMap("key58") + copySceneConfPo.getRequireLv().intValue()
					+ GlobalCache.fetchLanguageMap("key59"));
		}
	}

	/**
	 * 根据技能IdNumberVo列表获取技能id列表
	 * 
	 * @param listIdNumberVos
	 * @return
	 */
	public List<Integer> fetchSkillStr(List<IdNumberVo> listIdNumberVos) {
		List<Integer> list = new ArrayList<Integer>();
		for (IdNumberVo idNumberVo : listIdNumberVos) {
			list.add(idNumberVo.getId());
		}
		return list;
	}

	/**
	 * 
	 * 方法功能:推送未读邮件数量 更新时间:2014-6-27, 作者:johnny
	 */
	public void sendUpdateClientMailUnRead() {
		singleRole("PushRemoting.sendUpdateClientMailUnRead",
				new Object[] { getMailUnread() },true);
	}

	/**
	 * 给玩家发送消息
	 * 
	 * @param msg
	 */
	public void sendMsg(String msg) {
		ChatService chatService = ChatService.instance();
		chatService.sendSystemMsg(msg, id);
	}
	
	public void sendGoodFriend(String msg){
		ChatService chatService = ChatService.instance();
		chatService.sendGoodFriendMsg(msg, this);
	}

	public void sendBeAttacked(Fighter figher) {
		// 1 XXX正在攻击你，切换善恶模式可进行反击 XXX玩家名字
		singleRole("PushRemoting.sendBeAttacked", new Object[] { figher.name },true);
	}

	public void sendTeamInvitition(RolePo rolePo) {
		singleRole("PushRemoting.sendTeamInvitition", new Object[] { rolePo.id,
				rolePo.name, rolePo.teamMemberVo.teamVo.id },true);
	}

	public void sendRequireEnterRoom(int mapRoomId, int sceneId,
			int copySceneConfPoId) {
		singleRole("PushRemoting.sendRequireEnterRoom", new Object[] {
				mapRoomId, sceneId, copySceneConfPoId },true);
	}

	public void sendAskTeamEnterRoom(int mapRoomId, int sceneId,
			Integer teamOrNot, Integer copySceneConfigId, Integer diffucult) {
		singleRole("PushRemoting.sendAskTeamEnterRoom", new Object[] {
				mapRoomId, sceneId, teamOrNot, copySceneConfigId, diffucult },true);
	}

	public void sendUpdateGuildInfor() {
		singleRole("PushRemoting.sendUpdateGuildInfor", new Object[] {
				guildAutoAccept, guildHonor, guildId, guildTodayContributed,
				listGuildTodayExchangeItems },true);
	}

	public void sendUpdateOpenSystem() {
		singleRole("PushRemoting.sendUpdateOpenSystem",
				new Object[] { openSystemArrayList },true);
	}

	public void sendUpdateCopySceneTodayVisitTimes() {
		singleRole("PushRemoting.sendUpdateOpenSystem",
				new Object[] { listCopySceneTodayVisitTimes },true);
	}

	public void sendUpdateSkillPoint() {
		singleRole("PushRemoting.sendUpdateSkillPoint",
				new Object[] { getSkillPoint() },true);
	}

	public void sendUpdateCurrentTime() {
		singleRole("PushRemoting.sendUpdateCurrentTime",
				new Object[] { System.currentTimeMillis() },true);
	}

	public void sendUpdateEquip(EqpPo eqpPo) {
		singleRole("PushRemoting.sendUpdateEquip", new Object[] { eqpPo },true);
	}

	public void sendUpdateResurInfor() {
		singleRole("PushRemoting.sendUpdateResurInfor", new Object[] {
				resurnowTodayTimes, resurnowContinueTimes },true);
	}

	public void sendUpdateRelations() {
		singleRole("PushRemoting.sendUpdateRelations", new Object[] {
				listFriends, listEnemys, listBlocks },true);
	}

	public void sendUpdateWingInfor() {
		// System.out.println("wingStar:"+wingStar);
		singleRole("PushRemoting.sendUpdateWingInfor", new Object[] { wingStar,
				wingStarExp, wingStepPoss, wingEquipStatus, wingWasHidden },true);
	}

	public void sendUpdateGuildInvititions() {
		singleRole("PushRemoting.sendUpdateGuildInvititions",
				new Object[] { listGuildInvitions },true);
	}

	public void sendUpdatePetList() {
		singleRole("PushRemoting.sendUpdatePetList", new Object[] { listRpets,
				petRollGoldTodayTimes },true);
	}

	public void sendUpdatePetInfor() {
		singleRole("PushRemoting.sendUpdatePetInfor", new Object[] { petSoul,
				rpetFighterId },true);
	}

	public void sendUpdateRpet(RpetPo rpetPo) {
		singleRole("PushRemoting.sendUpdateRpet", new Object[] { rpetPo,
				petSoul },true);
	}

	public void sendUpdateSoulSlot(Integer slot) {
		SlotSoulVo slotSoulVo = findSlotSoul(slot);
//		System.out.println("slotSoulVo = " +slotSoulVo);
		singleRole("PushRemoting.sendUpdateSoulSlot",
				new Object[] { slotSoulVo},true);
	}

	public void sendUpdateCalculate() {
		singleRole("PushRemoting.sendUpdateCalculate",
				new Object[] { battlePower },true);
	}

	public void sendCopySceneFinish(BattleResultVo battleResultVo) {
		singleRole("PushRemoting.sendCopySceneFinish",
				new Object[] { battleResultVo },true);

	}

	public void sendRequestTransferToPlayer(Integer targetPlayerId) {
		singleRole("PushRemoting.sendRequestTransferToPlayer",
				new Object[] { targetPlayerId },true);
	}

	public void sendUpdateTaskProgress(Integer id, Integer num) {
		singleRole("PushRemoting.sendUpdateTaskProgress", new Object[] { id,
				num },true);
	}

	public void sendApplyToJoinTeam(Integer targetCaptainId, String name) {
		singleRole("PushRemoting.sendApplyToJoinTeam", new Object[] {
				targetCaptainId, name },true);
	}

	public void sendUpdateTeamInfor(TeamVo teamVo) {
		singleRole("PushRemoting.sendUpdateTeamInfor", new Object[] { teamVo },true);
	}

	public void sendUpdateDungeonTeamInfor(TeamVo teamVo) {
		singleRole("PushRemoting.sendUpdateDungeonTeamInfor",
				new Object[] { teamVo },true);
	}

	public void sendUpdateShowMsg(String msg) {
		singleRole("PushRemoting.sendUpdateShowMsg", new Object[] { msg },true);
	}

	public void sendUpdateExpAndLv(boolean flush) {
		singleRole("PushRemoting.updateRoleExpAndLv", new Object[] { getExp(),
getLv(), getPrestige() },flush);
	}

	public void sendUpdateRoleEquipSlot(Integer equipSlot) {
		Integer equipId = fetchEquipIdBySlot(equipSlot);
		// System.out.println(EqpPo.findEntity(equipId));
		singleRole("PushRemoting.updateRoleEquipSlot", new Object[] {
				equipSlot, EqpPo.findEntity(equipId) },true);
	}

	public void sendUpdateTreasure(boolean flush) {
		// System.out.println("sendUpdateTreasure");
		UserPo userPo = UserPo.findEntity(getUserId());
		singleRole("PushRemoting.updateRoleTreasure", new Object[] { getGold(),
				getBindGold(), userPo.getDiamond(), getBindDiamond(),getGamstoneFragment(),getGuildHonor(),getSoul() },flush);
	}

	public void sendUpdateMainPack(boolean flush) {
		singleRole("PushRemoting.updateRolePack",new Object[] { mainPackItemVosMap },flush);
	}
	
	public void sendUpdateSingleMainPack(RolePackItemVo rolePackItemVo,boolean flush) {
		singleRole("PushRemoting.updateSingleRolePack",new Object[] { rolePackItemVo },flush);
	}

	public void sendUpdateBuyProductList() {
		singleRole("PushRemoting.sendUpdateBuyProductList", new Object[] {
				listEveryDayBuyProductCount, listOnlyBuyProductCount },true);
	}

	public void sendUpdateRole() {
		singleRole("PushRemoting.sendUpdateRole", new Object[] { this },true);
	}

	public void sendUpdateRoleOptions() {
		singleRole("PushRemoting.sendUpdateRoleOptions",
				new Object[] { listRoleOptions },true);
	}

	public void sendUpdatePetConstell(Integer constellId) {
		singleRole("PushRemoting.sendUpdatePetConstell",
				new Object[] { petConstellsList },true);
	}

	public void sendUpdatePetTalent() {
		singleRole("PushRemoting.sendUpdatePetTalent",
				new Object[] { listPetTalent },true);
	}

	public void sendReceieveSummonInfor(Float x, Float y, Float z,
			Integer staticRoomId2) {
		singleRole("PushRemoting.sendReceieveSummonInfor", new Object[] { x, y,
				z, staticRoomId2 },true);
	}

	public void sendCumulativeLoginAward() {
		singleRole("PushRemoting.sendCumulativeLoginAward",
				new Object[] { listCumulativeLoginAwardRecord },true);
	}

	public void sendLevelAward() {
		singleRole("PushRemoting.sendLevelAward",
				new Object[] { listLevelAwardRecord },true);
	}

	public void sendOnlineTimeAward(long time) {
		singleRole("PushRemoting.sendOnlineTimeAward", new Object[] { time,
				listOnlineTimeAwrodRecord },true);
	}

	public void sendUpdateAchieveAndTitle() {
		singleRole("PushRemoting.sendUpdateAchieveAndTitle", new Object[] {
				achievePoint, nowTitleLv},true);
	}

	public void sendUpdateDailyLivelyAward() {
		singleRole("PushRemoting.sendUpdateDailyLivelyAward", new Object[] {
				dailyLivelyTaskFinishScore, listDailyLivelyAwardRecord },true);
	}

	public void sendUpdateAwardRetrieve() {
		singleRole("PushRemoting.sendUpdateAwardRetrieve",new Object[] { listAwardRetrieve },true);
	}

	public void sendSustainKill() {
		singleRole("PushRemoting.sendSustainKill",
				new Object[] { sustainKillVo.killNum },true);
	}

	public void sendAddFashion() {
		singleRole("PushRemoting.sendAddFashion", new Object[] { roleFashions },true);
	}

	public void sendUpdateActivitysList() {
		singleRole("PushRemoting.sendUpdateActivitysList",
				new Object[] { GlobalCache.fetchActiveLiveActivitys() },true);
	}
	
	public void sendUpdateLogVo(LogVo logVo){
		singleRole("PushRemoting.sendUpdateLogVo",
				new Object[] { logVo},false);
	}

	public void sendUpdatePVPPVEActivity() {
		singleRole("PushRemoting.sendUpdatePVPPVEActivity",
				new Object[] { pVPPVEActivityStatusVo },true);
	}

	public void sendUpdateAddGuildTips() {
		singleRole("PushRemoting.sendUpdateAddGuildTips",
				new Object[] { guildId },true);
	}
	
	public void sendUpdateListEachFirstRechargeStatus(){
		singleRole("PushRemoting.sendUpdateListEachFirstRechargeStatus",
				new Object[] { listEachFirstRechargeStatus },true);
	}
	
	public void sendUpdateRoleCimelias(){
		singleRole("PushRemoting.sendUpdateRoleCimeliasStep",
				new Object[] { cimeliasId },true);
	}
	
	public void sendUpdateRoleConsumCostVos(){
//		PrintUtil.print("PushRemoting.sendUpdateRoleConsumCostVos: "+listConsumCostVos.size());
		singleRole("PushRemoting.sendUpdateRoleConsumCostVos", new Object[]{listConsumCostVos}, true);
	}

	public void sendUpdateAdvanceSuitPlus() {
		fetchPowerSuitPlusArriveMaxLevel();
		fetchCurrentPowerSuitPlusLevel();
		fetchStarSuitPlusArriveMaxLevel();
		fetchCurrentStarSuitPlusArriveLevle();
		fetchWashSuitPlusMaxLevel();
		fetchWashSuitPlusCurrentLevel();
		singleRole("PushRemoting.sendUpdateAdvanceSuitPlus", new Object[] {
				powerSuitPlusArriveMaxLevel, currentPowerSuitPlusLevel,
				starSuitPlusArriveMaxLevel, currentStarSuitPlusArriveLevle,
				washSuitPlusMaxLevel, washSuitPlusCurrentLevel},true);
	}

	public void sendUpdateRechargeInfo() {
		fetchWasMonthCard();
		fetchRemainMonthCardDay();
		fetchTodayWasTakeMonthCard();
		fetchCumulativeRechargeNum();
		fetchWasFirstRecharge();
		fetchWasTakeFirstRechargeAwards();
		fetchWasTakeDailyVipAward();
		singleRole("PushRemoting.sendUpdateRechargeInfo", new Object[] {
				wasMonthCard, remainMonthCardDay, todayWasTakeMonthCard,
				cumulativeRechargeNum, wasFirstRecharge,
				wasTakeFirstRechargeAwards, wasTakeDailyVipAward, vipLv },true);
	}

	public void sendNearByFriendAddRequest(Integer roleId, String roleName) {
		singleRole("PushRemoting.sendNearByFriendAddRequest", new Object[] {
				roleId, roleName },true);
	}

	public void sendUpdateYunDartTaskInfo() {
		fetchCurrentYunDartCarId();
		fetchCurrentYunDartCarQuality();
		fetchDailyCurrentFinishYunDartCount();
		fetchDailyCurrentFreeFlushYunDartCarCount();
//		 System.out.println("dailyCurrentFinishYunDartCount="+dailyCurrentFinishYunDartCount);
//		 System.out.println("dailyCurrentFreeFlushYunDartCarCount="+dailyCurrentFreeFlushYunDartCarCount);
//		 System.out.println("currentYunDartCarId="+currentYunDartCarId);
//		 System.out.println("currentYunDartCarQuality="+currentYunDartCarQuality);
		singleRole("PushRemoting.sendUpdateYunDartTaskInfo",
				new Object[] { dailyCurrentFinishYunDartCount,
						dailyCurrentFreeFlushYunDartCarCount,
						currentYunDartCarQuality },true);
	}

	public void sendUpdateTeamDungeon(List<TeamAbroadRoomInforVo> list) {
		singleRole("PushRemoting.sendUpdateTeamDungeon", new Object[] { list },true);
	}
	
	public void sendUpdateGrowFund1Info(){
		singleRole("PushRemoting.sendUpdateGrowFund1Info", new Object[] { wasGrowFunds1,listgrowFundsAward1 },true);
	}
	
	public void sendUpdateGrowFund2Info(){
		checkGrowFunds2TakeDay();
//		System.out.println("growFunds2TakeDay="+growFunds2TakeDay);
		singleRole("PushRemoting.sendUpdateGrowFund2Info", new Object[] { wasGrowFunds2, growFunds2TakeDay,listgrowFundsAward2 },true);
	}
	
	public void sendUpdateListInvitationTask(){
		singleRole("PushRemoting.sendUpdateListInvitationTask", new Object[] {listInvitationTask},true);
	}

	public void sendUpdateCopySceneAativityWasOpenInfo() {
		Integer currentTime = (int) (System.currentTimeMillis() / 1000);
		singleRole(
				"PushRemoting.sendUpdateCopySceneAativityWasOpenInfo",
				new Object[] { currentTime,
						GlobalCache.fetchCopySceneActivityVoList() },true);
	}

	private void sendUpdateTeamMemberHpChange(Integer targetId, Integer batHp2,
			Integer batMaxHp2, Integer targetLv) {
		singleRole("PushRemoting.sendUpdateTeamMemberHpChange", new Object[] {
				targetId, batHp2, batMaxHp2, targetLv },true);
	}

	// public void sendUpdateTeamMemberOnlineChange(Integer targetId, int
	// online) {
	// singleRole("PushRemoting.sendUpdateTeamMemberHpChange",new
	// Object[]{targetId,online});
	// }

	public void sendUpdateRoleActivitysList() {
		singleRole("PushRemoting.sendUpdateRoleActivitysList",
				new Object[] { listRoleLiveActivitys },true);
	}

	public void sendUpdateGuildPoMembersInfo(List<GuildMemberVo> list) {
		singleRole("PushRemoting.sendUpdateGuildPoMembersInfo",
				new Object[] { list },true);
	}

	public void sendUpdatePKInfor() {
		singleRole("PushRemoting.sendUpdatePKInfor", new Object[] {
				getPkValue(), getPkStatus() },true);
	}

	public void sendUpdateGuildBossInfo(
			CopyOnWriteArrayList<IdNumberVo2> listBossInfo) {
		singleRole("PushRemoting.sendUpdateGuildBossInfo",
				new Object[] { listBossInfo },true);
	}

	public void sendUpdateClientRechargeDiamond(Double rechargeMoney, Integer baseDiamond) {
		singleRole("PushRemoting.sendUpdateClientRechargeDiamond",
				new Object[] {rechargeMoney, baseDiamond},true);
	}

	public void singleRole(String order, Object[] objs,Boolean flush) {
		BasePushTemplate pushTemplate = BasePushTemplate.instance();
		pushTemplate.sendOrder(order, getId(), objs, null,flush);
	}
	
	public void sendUpdateGuildPriestStart(GuildPo guildPo){
		singleRole("PushRemoting.sendUpdateGuildPriestStart",new Object[] { guildPo.getPriestFreshStartTime(), guildPo.getPriestFreshState(), guildPo.getPriestFreshQuality() },true);
	}
	
	public void sendUpdateWasNewEnemy(){
		singleRole("PushRemoting.sendUpdateWasNewEnemy",new Object[] { wasNewEnemy },true);
	}
	
	public void sendUpdateListBufferStatus(){
		singleRole("PushRemoting.sendUpdateListBufferStatus",new Object[] { listBufferStatus },true);
	}
	
	public void sendUpdateNewbieStepGroup(){
		singleRole("PushRemoting.sendUpdateNewbieStepGroup", new Object[]{newbieStepGroup},true);
	}
	
	public void sendResourceScene(){
//		System.out.println("sendResourceScene() startResourceSceneTime="+DateUtil.getFormatDateBytimestamp(startResourceSceneTime)+"; resourceSceneTime="+resourceSceneTime);
		singleRole("PushRemoting.sendResourceScene", new Object[]{startResourceSceneTime,resourceSceneTime},true);
	}
	
	public void sendUpdateClientGuildExitTime(){
		checkClientGuildExitTime();
		singleRole("PushRemoting.sendUpdateClientGuildExitTime", new Object[]{clientGuildExitTime},true);		
	}
	
	public void sendUpdatelistDiamondBasins(){
		singleRole("PushRemoting.sendUpdatelistDiamondBasins", new Object[]{listDiamondBasins},true);
	}
	

	public void tryUpdateTeamMembersHpChange() {
		if (teamMemberVo != null && teamMemberVo.teamVo != null) {
			for (TeamMemberVo teamMemberVo2 : teamMemberVo.teamVo.teamMemberVos) {
				RolePo rolePo = RolePo.findEntity(teamMemberVo2.roleId);
				if (fighter != null) {
					rolePo.sendUpdateTeamMemberHpChange(id, fighter.getBatHp(), fighter.batMaxHp, lv);
				}
			}
		}
	}

	// public void tryUpdateTeamMembersOnlineChange(Integer online) {
	// if(teamMemberVo!=null && teamMemberVo.teamVo!= null){
	// for (TeamMemberVo teamMemberVo2 : teamMemberVo.teamVo.teamMemberVos) {
	// RolePo rolePo = RolePo.findEntity(teamMemberVo2.roleId);
	// if(fighter!=null){
	// rolePo.sendUpdateTeamMemberOnlineChange(id,online);
	// }
	// }
	// }
	// }

	public boolean wasInKillingTowerActivity(Integer roomId) {
		if (MapWorld.findStage(roomId) == null
				|| MapWorld.findStage(roomId).copySceneConfPo == null) {
			return false;
		}
		if (IntUtil.inRange(MapWorld.findStage(roomId).copySceneConfPo.getId(),
				CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START,
				CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_END)) {
			return true;
		}
		return false;
	}

	public void awardBattleResult(BattleResultVo battleResultVo) {
		adjustBindGold(battleResultVo.gold);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, battleResultVo.gold, GlobalCache.fetchLanguageMap("key2367"), "");
		adjustExp(battleResultVo.exp);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, battleResultVo.exp, GlobalCache.fetchLanguageMap("key2368"), "");
		adjustPrestige(battleResultVo.prestige);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PRISTIGE, 0, battleResultVo.prestige, GlobalCache.fetchLanguageMap("key2369"), "");
		adjustAchievePoint(battleResultVo.achievePoint);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ACHIVEPOINT, 0, battleResultVo.achievePoint, GlobalCache.fetchLanguageMap("key2370"), "");
		adjustGuildHonor(battleResultVo.guildHonor);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, battleResultVo.guildHonor, GlobalCache.fetchLanguageMap("key2370"), "");
		addItemList(battleResultVo.itemList, 1, GlobalCache.fetchLanguageMap("key2371"));
		for (List<Integer> list : battleResultVo.rollAwardList) {
			if (list.get(1) == 1) {
				addItem(list.get(2), list.get(3), 1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, list.get(2), list.get(3), GlobalCache.fetchLanguageMap("key2372"), "");
			}
		}
	}

	public void checkRecordLiveAcitivity(Integer score, int type) {
		for (IdNumberVo idNumberVo : listFightActivityMaxScoreRecords) {
			if (idNumberVo.getId().intValue() == type) {
				idNumberVo.setNum(Math.max(idNumberVo.getNum(), score));
				return;
			}
		}
		listFightActivityMaxScoreRecords.add(new IdNumberVo(type, score));
	}

	public boolean hasItemList(List<IdNumberVo> requireList) {
		for (IdNumberVo idNumberVo : requireList) {
			if (fetchItemCount(idNumberVo.getId()) < idNumberVo.getNum()) {
				return false;
			}
		}
		return true;
	}

	public int fetchSoulStarLv(Integer slot) {
		SlotSoulVo slotSoulVo = findSlotSoul(slot);
		if (slotSoulVo == null) {
			return 0;
		}
		return slotSoulVo.powerLv;
	}

	public void sendUpdateHorse(String msg) {
		singleRole("PushRemoting.sendUpdateHorse", new Object[] { msg },true);
	}

	public void leaveMyTeam() {
		if (teamMemberVo == null) {
			return;
		}
		int teamId = teamMemberVo.teamVo.id;
		teamMemberVo.leaveTeam(0);
		if (TeamVo.findTeam(teamId, 0) != null) {
			TeamVo.findTeam(teamId, 0).sendAllMemberUpdateTeamInfor();
		}
	}

	/**
	 * 克隆属性
	 * 
	 * @param targetRolePo
	 */
	public void cloneAttribute(RolePo targetRolePo) {
		this.name = targetRolePo.getName();
		this.lv = targetRolePo.getLv();
		this.career = targetRolePo.getCareer();
		this.skills = targetRolePo.skills;
		this.batMeleeAttackMin = targetRolePo.getBatMeleeAttackMin();
		this.batMagicAttackMin = targetRolePo.getBatMagicAttackMin();
		this.batMeleeDefenceMin = targetRolePo.getBatMeleeDefenceMin();
		this.batMagicDefenceMin = targetRolePo.getBatMagicDefenceMin();
		this.batMeleeAttackMax = targetRolePo.getBatMeleeAttackMax();
		this.batMagicAttackMax = targetRolePo.getBatMagicAttackMax();
		this.batMeleeDefenceMax = targetRolePo.getBatMeleeDefenceMax();
		this.batMagicDefenceMax = targetRolePo.getBatMagicDefenceMax();
		this.batHp = targetRolePo.getBatMaxHp();
		this.batMp = targetRolePo.getBatMaxMp();
		this.batCritical = targetRolePo.getBatCritical();
		this.batHpReg = targetRolePo.getBatHpReg();
		this.batMpReg = targetRolePo.getBatMpReg();
		this.batMovement = targetRolePo.getBatMovement();
		this.batDodge = targetRolePo.getBatDodge();
		this.batHitRateRate = targetRolePo.getBatHitRateRate();
		this.batDodgeRate = targetRolePo.getBatDodgeRate();
		this.batAddCriticalRate = targetRolePo.getBatAddCriticalRate();
		this.batDerateCriticalRate = targetRolePo.getBatDerateCriticalRate();
		this.batReboundDamage = targetRolePo.getBatReboundDamage();
		this.equipWeapon = targetRolePo.equipWeapon;
		this.equipRing = targetRolePo.equipRing;
		this.equipArmor = targetRolePo.equipArmor;
		this.equipNecklace = targetRolePo.equipNecklace;
		this.equipBracer = targetRolePo.equipBracer;
		this.equipHelmet = targetRolePo.equipHelmet;
		this.equipShoe = targetRolePo.equipShoe;
		this.equipBelt = targetRolePo.equipBelt;
		this.equipBracelet = targetRolePo.equipBracelet;
		this.equipPants = targetRolePo.equipPants;
		this.equipWeaponId = targetRolePo.getEquipWeaponId();
		this.equipRingId = targetRolePo.getEquipRingId();
		this.equipArmorId = targetRolePo.getEquipArmorId();
		this.equipNecklaceId = targetRolePo.getEquipNecklaceId();
		this.equipBracerId = targetRolePo.getEquipBracerId();
		this.equipHelmetId = targetRolePo.getEquipHelmetId();
		this.equipShoeId = targetRolePo.getEquipShoeId();
		this.equipBeltId = targetRolePo.getEquipBeltId();
		this.equipBraceletId = targetRolePo.getEquipBraceletId();
		this.equipPantsId = targetRolePo.getEquipPantsId();
		this.bufferStatus = targetRolePo.getBufferStatus();
		this.wingWasHidden = targetRolePo.getWingWasHidden();
		this.wingEquipStatus = targetRolePo.getWingEquipStatus();
		this.wingStar = targetRolePo.getWingStar();
		this.battlePower = targetRolePo.getBattlePower();
		this.batMaxHp = targetRolePo.getBatMaxHp();
		this.batMaxMp = targetRolePo.getBatMaxMp();
		this.batLuckyAttack = targetRolePo.getBatLuckyAttack();
		this.batHitRate = targetRolePo.getBatHitRate();
		this.batMeleeDefence = targetRolePo.getBatMeleeDefence();
		this.batMagicDefence = targetRolePo.getBatMagicDefence();
		this.batLuckyDefence = targetRolePo.getBatLuckyDefence();
		this.batTough = targetRolePo.getBatTough();
		this.batDamageRate = targetRolePo.getBatDamageRate();
		this.batDamageResistRate = targetRolePo.getBatDamageResistRate();
		this.batCriticalDamageRate = targetRolePo.getBatCriticalDamageRate();
		this.batAttackRate = targetRolePo.getBatAttackRate();
		this.batDefenceRate = targetRolePo.getBatDefenceRate();
		this.batMeleeAttack = targetRolePo.getBatMeleeAttack();
		this.batMagicAttack = targetRolePo.getBatMagicAttack();
		this.guildId = targetRolePo.getGuildId();
		this.rpetFighterId = targetRolePo.getRpetFighterId();
		this.petConstell = targetRolePo.getPetConstell();
		this.petTalent = targetRolePo.getPetTalent();
		this.slotSouls = targetRolePo.getSlotSouls();
		this.vipLv = targetRolePo.getVipLv();
		this.titleLv = targetRolePo.getTitleLv();
		this.currentSpecialTitleLv = targetRolePo.currentSpecialTitleLv;
		this.nowTitleLv=targetRolePo.nowTitleLv;
		this.listSkillVos = targetRolePo.listSkillVos;
		this.roleFashion = targetRolePo.roleFashion;
		this.listSlotSouls = targetRolePo.listSlotSouls;
		this.listBufferStatus = targetRolePo.listBufferStatus;
		this.arenaWasFighting = true;
		this.currentMilitaryRankId = targetRolePo.currentMilitaryRankId;
		this.powerSuitPlusMap = targetRolePo.powerSuitPlusMap;
		this.starSuitPlusMap = targetRolePo.starSuitPlusMap;
		this.wingEquipStatus = targetRolePo.wingEquipStatus;
		this.wingStar = targetRolePo.wingStar;
		this.listAdvanceSuitPlus = targetRolePo.listAdvanceSuitPlus;
		GuildPo guildPo = GuildPo.findEntity(targetRolePo.getGuildId());
		if(guildPo != null){
			GuildMemberVo guildMemberVo = guildPo.fetchGuildMemberVoInfo(targetRolePo.getId());
			if(guildMemberVo != null){
				this.guildPosition = guildMemberVo.guildPosition;				
			}
		}
		
	}

	public void consumeItemList(List<IdNumberVo> requireList,String logString) {
		for (IdNumberVo idNumberVo : requireList) {
			consumeItem(idNumberVo.getId(), idNumberVo.getNum(),null,logString);
		}
	}

	public int fetchCurrentZoneOtherTeamMembers() {
		// System.out.println("fighter:"+fighter.name);
		int count = 0;
		if (teamMemberVo != null && teamMemberVo.teamVo != null
				&& teamMemberVo.teamVo.teamMemberVos != null) {
			for (TeamMemberVo singleTeamMemberVo : teamMemberVo.teamVo.teamMemberVos) {
				RolePo tartgetRolePo = RolePo
						.findEntity(singleTeamMemberVo.roleId);
				if (tartgetRolePo != null) {
					if (tartgetRolePo == this) {
						continue;
					}
					Fighter member = tartgetRolePo.fighter;
					if (member != null) {
						if (member.isIn9Cell(fighter)) {
							count += member.rolePo.getLv();
						}
					}

				}
			}
		}

		return count;
	}

	public List<RolePo> fetchCurrentZoneOtherTeamMemberList() {
		List<RolePo> list = new ArrayList<RolePo>();
		if (teamMemberVo != null && teamMemberVo.teamVo != null
				&& teamMemberVo.teamVo.teamMemberVos != null) {
			for (TeamMemberVo singleTeamMemberVo : teamMemberVo.teamVo.teamMemberVos) {
				RolePo tartgetRolePo = RolePo
						.findEntity(singleTeamMemberVo.roleId);
				if (tartgetRolePo != null) {
					if (tartgetRolePo == this) {
						continue;
					}
					Fighter member = tartgetRolePo.fighter;
					if (member != null) {
						if (member.isIn9Cell(fighter)) {
							list.add(member.rolePo);
						}
					}

				}
			}
		}
		return list;
	}

	public void sendUpdateListGuildBossAward() {
		singleRole("PushRemoting.sendUpdateListGuildBossAward",
				new Object[] { listGuildBossAward },true);
	}

	public boolean isPlayingActivityType(int type) {
		MapRoom mapRoom = MapRoom.findStage(roomId);
		if (mapRoom != null && mapRoom.copySceneConfPo != null
				&& mapRoom.copySceneConfPo.getType() == type) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查重置公会boss刷新时间
	 */
	public void checkResetGuildBossFlushTime(){
		List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
		if(System.currentTimeMillis() > getGuildBossAwardFlushTime().longValue()){
			listGuildBossAward = new CopyOnWriteArrayList<IdNumberVo>();
			for(int i=0; i < listBoss.size(); i++){
				listGuildBossAward.add(new IdNumberVo(listBoss.get(i).copysceneconfid, 0));
			}
			setGuildBossAwardFlushTime(DateUtil.fetchTimesWeekSat());
		}
		if(getGuildId() != null && getGuildId().intValue() != 0){
			GuildPo guildPo = GuildPo.findEntity(getGuildId());
			if(guildPo != null){
				if(System.currentTimeMillis() > guildPo.getGuildBossFlushTime().longValue()){
					guildPo.listBossInfo.clear();
					for(Boss boss : listBoss){
						if(boss.lv == 1){
							guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 1, 0));				
						}else{
							guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 0, 0));	
						}
					}
					guildPo.setGuildBossFlushTime(DateUtil.fetchTimesWeekSat());
				}
			}
		}
	}
	
	
	


	/**
	 * 
	 * @param roomId2
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param type 1.入口复活；2:原地复活； 3：回村复活；4杀人爬塔入口，5杀人爬塔原地，6杀人爬塔掉层复活
	 * @param teleportType
	 * @return
	 */
	public boolean enterRoom(Integer roomId2, Integer x2, Integer y2,Integer z2,Integer type, Integer teleportType) {
//		System.out.println(name +" enterRoom() roomId2 = " + roomId2);
//		System.out.println(name+":enter room"+ System.currentTimeMillis());
//		PrintUtil.print(getName()+" roomId2:"+ roomId2+ " x2:" +x2+" y2:"+y2+" z2:"+ z2+" type:"+ type+" teleportType:" +teleportType);
		MapRoom targetMapRoom = MapWorld.findStage(roomId2);
		if(targetMapRoom==null){
			ExceptionUtil.throwConfirmParamException("Room Not Exist:"+roomId2);
		}
//		System.out.println(new Date().toLocaleString()+" "+name+" enterRoom:"+targetMapRoom.mapRoomId);
		ChatService chatService =ChatService.instance();

		if(!targetMapRoom.cellData.isValidCell(targetMapRoom.cellData.pixelXToCellX(x2), targetMapRoom.cellData.pixelXToCellX(z2)))
		{
			ScenePo sc =ScenePo.findEntity(targetMapRoom.sceneId);
			x2=sc.getX();
			y2=sc.getY();
			z2=sc.getZ();
		}
		
//		System.out.println(" getRoomLoading() = " +getRoomLoading() +"; type = " +type);
		if(getRoomLoading()!=1 && type!=1 && type!=2 && type!=6){
			ExceptionUtil.throwConfirmParamException("You must be loading layer:"+id);
		}
		if(teleportType != null && teleportType.intValue() == 1){
			publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,1000);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -1000, GlobalCache.fetchLanguageMap("key2395"), "");
			this.checkHasAndConsumeBindGoldThenGold(1000);
		}
		if(!targetMapRoom.cellData.isValidCell(targetMapRoom.cellData.pixelXToCellX(x2), targetMapRoom.cellData.pixelXToCellX(z2)))
		{
			ExceptionUtil.throwConfirmParamException("beyond the scope of map1 ,x:"+x2+",z:"+z2);
			return false;
		}
		//原地复活
		if(type==2){
			this.setBatHp(this.getBatMaxHp());
			this.setBatMp(this.getBatMaxMp());
			PlayItem playItem = this.fetchPlayItemByType(PlayTimesType.PLAYTIMES_TYPE_11);
			VipPo vipPo = GlobalCache.mapVipPo.get(this.getVipLv());
			int flag = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_11);
			int sum = flag + playItem.initialTimes;
			if(this.getResurnowTodayTimes()>=sum){
				int i = this.getResurnowContinueTimes() +1;
				for(ReviveCost reviveCost :XmlCache.xmlFiles.constantFile.reviveCosts.reviveCost){
					if(i<=reviveCost.extraMaxTimes&&i>=reviveCost.extraMinTimes){
						this.checkHasAndConsumeBindGoldThenGold(reviveCost.buyPrice);
						this.checkHasAndConsumeBindDiamondThenDiamond(reviveCost.buyDiamond);
						this.sendUpdateTreasure(false);
						break;
					}
				}
			}
			if(this.getResurnowTodayTimes()>=sum){
				this.adjustResurnowContinueTimes(1);
			}
			this.adjustResurnowTodayTimes(1);
		}
		//回村复活
		else if(type==3 || type == 6){
			this.setBatHp(this.getBatMaxHp());
			this.setBatMp(this.getBatMaxMp());
			this.setResurnowContinueTimes(0);
		}
		// 入口复活
		else if(type == 1){
			if(this.getBatHp() <= 0){
				this.setBatHp(this.getBatMaxHp());
				this.setBatMp(this.getBatMaxMp());
			}
		}

		setRoomLoading(0);
		setRoomId(roomId2);
		
		ScenePo scenePo = ScenePo.findEntity(targetMapRoom.sceneId);
		//进入资源挂机地图
		if(scenePo != null && scenePo.getSceneAttribute().intValue() == 2){
			if(resourceSceneTime.intValue() <= 0 ){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2320"));
			}
			setStartResourceSceneTime(System.currentTimeMillis());
			sendResourceScene();
		}
		
		if(scenePo != null && scenePo.getId().intValue() == SceneType.SCENE_TIMER_BOSS){
			GuildPo guildPo = GuildPo.findEntity(getGuildId());
			if(guildPo == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2687"));
			}
		}
		
		if(this.wasInKillingTowerActivity(this.getRoomId())){
			int targetFloor=1+MapWorld.findStage(roomId).copySceneConfPo.getId()-CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START;
			if(targetFloor!=this.pVPPVEActivityStatusVo.currentFloor){
				this.pVPPVEActivityStatusVo.currentFloor=1+MapWorld.findStage(roomId).copySceneConfPo.getId()-CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START;
				this.pVPPVEActivityStatusVo.currentFloorKillCount=0;
				this.sendUpdatePVPPVEActivity();
			}
		}

		
		if(this.fighter!=null && this.fighter.mapRoom != null){
			//退出老地图cell
			this.fighter.mapRoom.onRemoveMover(this.fighter);
			this.fighter.mapRoom.cellData.removeLiving(this.fighter, true);
			if(this.fighterPet!=null){
				this.fighter.mapRoom.onRemoveMover(this.fighterPet);
				this.fighter.mapRoom.cellData.removeLiving(this.fighterPet, true);
			}
		}

		chatService.joinRoomChannel(this.getRoomId(), this.getIuid(), this.fetchSession(), ChatTempate.chatMMOStaticAndDynalicRooms);

		
		

		this.sendUpdateResurInfor();
		Fighter mover=Fighter.create(this,null,false);
		if(targetMapRoom.isDynamic){
			if(targetMapRoom.copySceneConfPo.getId() == CopySceneType.COPYSCENE_ARENA){
				mover.changeBatHp(this.getBatMaxHp());
				mover.changeBatMp(this.getBatMaxMp());
			}
		}
		else{
//			this.setStaticRoomId(targetMapRoom.mapRoomId);
		}

		if(mover.getBatHp().intValue() <= 0){
			mover.changeBatHp(this.getBatMaxHp());
			mover.changeBatMp(this.getBatMaxMp());
		}

		float baseNumber = (float)com.games.mmo.mapserver.bean.Entity.BASE_NUMBER;

		mover.changeX(x2/baseNumber);
		mover.changeY(y2/baseNumber);
		mover.changeZ(z2/baseNumber);
		
		if(type != 2){
			if(targetMapRoom.copySceneConfPo!=null && targetMapRoom.copySceneConfPo.getId().intValue() == CopySceneType.COPY_SCENE_CONF_FREE_WAR){
				if(mover.militaryForces == CopySceneType.MILITARY_FORCES_1){
					mover.changeX(90.41f);
					mover.changeY(18.35f);
					mover.changeZ(-2.9f);
				}else if(mover.militaryForces == CopySceneType.MILITARY_FORCES_2){
					mover.changeX(51.94f);
					mover.changeY(18.35f);
					mover.changeZ(153.83f);
				}
			}	
		}
		
		mover.aimX=mover.x;
		mover.aimY=mover.y;
		mover.aimZ=mover.z;
		
		setX(FloatUtil.toUpInt(mover.x*baseNumber));
		setY(FloatUtil.toUpInt(mover.y*baseNumber));
		setZ(FloatUtil.toUpInt(mover.z*baseNumber));
		//进入新地图cell
		switchFighter(mover);
		targetMapRoom.doAddMoverToStage(mover,this,false);
		byte[] results=mover.buildMoverAppearBytes();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		BattleMsgUtil.abroadBattleMsgWithFilter(targetMapRoom,results,this,mover,true,BattleMsgUtil.DEFAULT_MAX_limit,BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING);
		this.tryUpdateTeamMembersHpChange();
		copySceneStartState=true;
		this.sendUpdateTreasure(false);
		return true;
	}

	
	public void switchFighter(Fighter mover) {
		if(this.fighter!=null && this.fighter.mapRoom!=null && this.fighter.mapRoom.cellData!=null){
//			System.out.println("移除老Fighter:"+this.fighter.name+" "+this.fighter.mapUniqId);
			this.fighter.cellDateRemoveLiving(this.fighter.mapRoom.cellData, true);
		}
		if(this.fighter!=null && this.fighter.mapRoom!=null){
			this.fighter.mapRoom.cellData.removeLiving(this.fighterPet, true);
		}
		this.fighter=mover;

	}

	public void leaveRoom(Boolean mustOff) {
//		System.out.println(name+" leaveRoom:");
//		System.out.println(name+":leave room"+ System.currentTimeMillis());
//		setLastLogoffTime(System.currentTimeMillis());
		checkLeaveRoomResourceScene();
		if(mustOff==false && getRoomLoading()!=0){
//			System.out.println("Warning:You must not be loading layer to leave,return...");
			return;
			//ExceptionUtil.throwConfirmParamException("You must not be loading layer to leave");
		}
		setRoomLoading(1);
		ChatService chatService = ChatService.instance();
		if(this.fighter!=null ){
			// 冰冻buffer
			this.fighter.removeBuffer(3);
			this.fighter.removeBuffer(15);
			// 移除变身buffer
			this.fighter.removeBuffer(40);
			// 移除副本鼓舞buffer
			this.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
			// 移除假死buffer
			this.fighter.removeBuffer(101);
			this.sendUpdateListBufferStatus();
		}
//		MapRoom currentMapRoom = MapWorld.findStage(this.getRoomId());
//		ScenePo currentScene = ScenePo.findEntity(currentMapRoom.sceneId);
//		if(currentScene.getSceneAttribute()==2){
//			System.out.println("switchTogetSceneAttribute:"+currentScene);
//			ScenePo scenePo = ScenePo.findEntity(20101001);
//			setRoomId(scenePo.getId());
//			setX(scenePo.getX());
//			setY(scenePo.getY());
//			setZ(scenePo.getZ());
//		}
//		MapRoom targetMapRoom = MapWorld.findStage(roomId);
		MapRoom currentMapRoom = MapWorld.findStage(this.getRoomId());
//		
//		int originalRoomId = this.getRoomId();
//		if(roomId!=originalRoomId){	
//			if(targetMapRoom != null)
//			{	
//				boolean killingChange=false;
//				if(this.wasInKillingTowerActivity(targetMapRoom.mapRoomId)){
//					if(this.wasInKillingTowerActivity(currentMapRoom.mapRoomId)){
//						killingChange=true;
//					}
//				}
//				if(!killingChange){
//					if(currentMapRoom != null){
//						currentMapRoom.logoff(this);						
//					}
//				}
//			}
//		}
		
		chatService.leaveRoomChannel(this.getRoomId(), this.getIuid(), this.fetchSession(), ChatTempate.chatMMOStaticAndDynalicRooms);
		if(this.fighter!=null && this.fighter.mapRoom !=null){
			this.setBatHp(this.fighter.getBatHp());
			this.setBatMp(this.fighter.batMp);
			this.fighter.mapRoom.onRemoveMover(this.fighter);
			if(this.fighterPet!=null){
				this.fighter.mapRoom.onRemoveMover(this.fighterPet);
			}
			if(fighter!=null && fighter.mapRoom!=null){
				fighter.mapRoom.logoff(this);
			}
			this.switchFighter(null);
			
//			this.fighter.mapRoom.cellData.removeLiving(this.fighter, true);
//			this.fighter.mapRoom.cellData.removeLiving(this.fighterPet, true);
			

			if(fighter != null){
				fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);				
			}
			fighter = null;
		}
		if(currentMapRoom!=null && currentMapRoom.copySceneConfPo!=null){
			LogUtil.writeLog(this, 239, currentMapRoom.copySceneConfPo.getId(), 0, 0, GlobalCache.fetchLanguageMap("key2631"), "");
		}


	}
	
	/**
	 * 离开地图
	 * @param roomId
	 */
	public void checkLeaveRoomResourceScene(){
		MapRoom currentMapRoom = MapWorld.findStage(roomId);
		if(currentMapRoom != null){
			ScenePo scenePo = ScenePo.findEntity(currentMapRoom.sceneId);
			if(scenePo != null){
				//离开资源挂机地图
				if(scenePo.getSceneAttribute().intValue() == 2){
//				System.out.println("离开挂机地图111111111111111111111111");
					fetchResourceSceneRemainingTime();				
				}
				// 离开组队副本
				else if(scenePo.getId().intValue() == TeamMonstertInvadeRoom.SCENE_ID ||
						scenePo.getId().intValue() == TeamTowerRoom.SCENE_ID){
					changeTeamCaptain();
				}
				
			}
		}
	}
	
	/**
	 * 获取资源挂机场景的剩余时间
	 * @return
	 */
	public Integer fetchResourceSceneRemainingTime(){
//		System.out.println("fetchResourceSceneRemainingTime() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+ " | "+DateUtil.getFormatDateBytimestamp(getStartResourceSceneTime()));
		int time = getResourceSceneTime().intValue() - ((int)(System.currentTimeMillis() - getStartResourceSceneTime().longValue()));
		
		IdNumberVo2 idNumberVo2 = fetchBuyPlayItemsByType(PlayTimesType.PLAYTIMES_TYPE_540);
		if(idNumberVo2 != null){
			//已进时间=（买的次数+1）*30分钟-剩余时间
			long num=30L*60*1000*(idNumberVo2.getInt3().intValue()+1) -resourceSceneTime.intValue();
			 if(num>= 30L*60*1000){
//				 taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_734, null, null);
				 int count=(int) (num/(30L*60*1000));
//				 System.out.println("危机边缘任务执行 "+name+"; count="+count);
				 taskConditionProgressReplace(count, TaskType.TASK_TYPE_CONDITION_734, null, null);
//				 System.out.println(name+" listRoleAchievesTasks="+listRoleAchievesTasks);
			 }
		}
		if(time >0){
			setResourceSceneTime(time);
		}else{
			setResourceSceneTime(0);
		}
//		System.out.println("resourceSceneTime="+getResourceSceneTime()+" || "+getResourceSceneTime()/1000);
		setStartResourceSceneTime(System.currentTimeMillis());
		sendResourceScene();			
		return getResourceSceneTime();
	}
	

	public void activeTreasure(Integer treasureStart) {
		listRoleTreasures.add(new IdNumberVo(treasureStart,TaskType.TREASURE_STATUS_NOT_FINISH));
		sendUpdateTreasureList();
		if(listRoleTreasures.size()<=1){
			sendUpdateTreasureOpen(treasureStart);
		}
	}

	private void sendUpdateTreasureList() {
		singleRole("PushRemoting.sendUpdateTreasureList", new Object[]{listRoleTreasures},true);
	}
	
	private void sendUpdateTreasureOpen(Integer treasureId) {
		singleRole("PushRemoting.sendUpdateTreasureOpen", new Object[]{treasureId},true);
	}
	


	public void takeTreasureAward(Integer treasureId) {
		for (IdNumberVo idNumberVo : listRoleTreasures) {
			if(idNumberVo.getId()==treasureId.intValue()){
				listRoleTreasures.remove(idNumberVo);
				break;
			}
		}
		sendUpdateTreasureList();
		if(listRoleTreasures.size()>=1){
			sendUpdateTreasureOpen(treasureId);
		}
	}

	public Layer fetchDotaLayerInfo(Integer layerId){
		Layer currentLayer = null;
		List<Layer> layerList = XmlCache.xmlFiles.constantFile.dota.layer;
		for(Layer layer : layerList){
			if(layer.lv == layerId.intValue()){
				currentLayer = layer;
				break;
			}
		}
		return currentLayer;
	}

	public void finishTreasure(Integer treasureEnd) {
	out:for (IdNumberVo idNumberVo : listRoleTreasures) {
			if(idNumberVo.getId()==treasureEnd.intValue()){
				//TODO 【性能优化】CACHE TREASURE LIST
				for (com.games.mmo.vo.xml.ConstantFile.Treasures.Treasure treasure : XmlCache.xmlFiles.constantFile.treasures.treasure) {
					if(treasure.id==treasureEnd && treasure.type==2){
						listRoleTreasures.remove(idNumberVo);
						break out;
					}
				}
				idNumberVo.setNum(TaskType.TREASURE_STATUS_NOT_AWARD);
				break;
			}
		}
		sendUpdateTreasureList();
	}

	public Object[] submitTask(Integer taskId) {
		
		Integer newTaskId=0; 

			for(int i=0;i<listRoleTasks.size();i++){
				if(listRoleTasks.get(i).getInt1()==taskId.intValue()){
					TaskPo taskPo = TaskPo.findEntity(listRoleTasks.get(i).getInt1());
					if(taskPo.getTreasureStart()!=0){
						activeTreasure(taskPo.getTreasureStart());
					}
					if(taskPo.getTreasureEnd()!=0){
						finishTreasure(taskPo.getTreasureEnd());
					}
					// 解锁系统功能
					if(taskPo.openSystemVals != null && taskPo.openSystemVals.size() != 0){
						int systemType =Integer.parseInt(taskPo.openSystemVals.get(0));
						boolean falg = true;
						// 如果系统已经开启就跳过
						lableA:	for(Integer n : openSystemArrayList){
							if(n.intValue() == systemType){
								falg = false;
								break lableA;
							}
						}
						// 开启系统
						if(falg){
							// 开启翅膀需要初始化
							if(systemType == TaskType.OPEN_SYSTEM_WING){
								setWingEquipStatus(1);
								setWingWasHidden(1);
							}else if(systemType == TaskType.OPEN_SYSTEM_CIMELIAS){
								setCimeliasId(1);
								sendUpdateRoleCimelias();
							}else if (systemType == TaskType.OPEN_SYSTEM_SOUL) {
								SoulService soulService = SoulService.instance();
								soulAtbMap = soulService.buildSoulAtb(null);
								if(getSoulAtb()==null){
									setSoulType(IntUtil.getRandomInt(1, 5));
								}
								if(getSoul()==null){
									setSoul(0);
								}
								calculateBat(1);
							}
							openSystemArrayList.add(systemType);
							if(fighter != null){
								CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(), fashion, getCareer(), getWingWasHidden(),getWingStar(),hiddenFashions);
								fighter.makeAvatars(commonAvatarVo,true);						
							}
							sendUpdateOpenSystem();						
						}
					}
					
					
					for (Integer activeTaskId : taskPo.listActiveTasks) {
						activeRoleTask(activeTaskId);
						newTaskId=activeTaskId;
						acceptedRoleTask(activeTaskId);
						freshTaskNewStatus(activeTaskId);
					}
					if(taskPo.getLeastNewbieStep()!=null && taskPo.getLeastNewbieStep().intValue() != 0){
						setNewbieStepGroup(taskPo.getLeastNewbieStep());
						sendUpdateNewbieStepGroup();
					}
					
					taskConditionProgressReplace(1,TaskType.TASK_TYPE_CONDITION_711,getLv(),null);
					taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_729,taskId,null);
					

					
					if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730){
						int time = (int) (System.currentTimeMillis()/1000L);
						if(time > listRoleTasks.get(i).getInt3()){
							break;
						}
						listRoleTasks.get(i).setInt3(0);
						// 运镖任务
						if(listYunDartTaskInfoVo.get(0).currentYunDartCarQuality!=-1){
							List<Cart> cartList = XmlCache.xmlFiles.constantFile.trade.cart;
							Cart cart = cartList.get(listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
							int param = getLv() -15;
							if(param < 0){
								param =1;
							}
							int baseExp = 12000*param;
							int basePrestige = 2000;
							int totalExp = baseExp*cart.expPar/100;
							int totalPrestige = basePrestige*cart.prestigePar/100;
							adjustExp(totalExp);
							LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, totalExp, GlobalCache.languageMap.get("key2339"), "");
							adjustPrestige(totalPrestige);
							
						}
						
						initYunDartInfo(taskPo.getId());
						sendUpdateYunDartTaskInfo();
					}else{
						for (IdNumberVo idNumberVo : taskPo.listTaskDelivery) {
							// 血瓶
							if(610100014 == taskPo.getId().intValue()){
								this.removePackItem(idNumberVo.getId(), idNumberVo.getNum(),GlobalCache.fetchLanguageMap("key2662"));
							}else{
								this.removePackItem(idNumberVo.getId(), 999,GlobalCache.fetchLanguageMap("key2662"));						
							}
						}
						addItemList(taskPo.listTaskAwardId,1, GlobalCache.languageMap.get("key2443"));
						this.listRoleTasks.remove(i);										
					}
					break;
				}
			}
			setCurrentFinishTaskId(taskId);
		return new Object[]{newTaskId};
	}

	public String fetchRoleInfor() {
		UserPo userPo = UserPo.findEntity(getUserId());
		String str="";
		str+="lastLogInIp:"+getLastLoginIp()+"<br>";
		str+="Account："+getUserIuid()+"<br>";
		str+="ID："+getId()+"<br>";
		str+="Channel："+getChannelKey()+"<br>";	
		str+="lastLogInTime:"+DateUtil.getText(getLastLoginTime())+"<br>";	
		str+="lastLogOutTime:"+DateUtil.getText(getLastLogoffTime())+"<br>";
		String car[]={GlobalCache.fetchLanguageMap("key2529"),GlobalCache.fetchLanguageMap("key2530"),GlobalCache.fetchLanguageMap("key2531")};
		String career=car[getCareer()-1];
		String sceneName="unKown";
		if(MapRoom.findStage(getRoomId())!=null){
			sceneName=ScenePo.findEntity(MapRoom.findStage(getRoomId()).sceneId).getName();
		}
		
		str+="<table border='1'><tr><th>---roleName---</th><th>----Carrer----</th><th>----level----</th><th>----exp----</th><th>----map----</th><th>--creatRoleTime--</th><th>--deleteState--</th></tr>";
		String timestring=DateUtil.getFormatDateBytimestamp(getCreateTime()); 
		String isDelet="wasDelete";
		if(abandomState==null){
			isDelet="notDelete";
		}
		str+="<tr><td>"+getName()+"</td><td>"+career+"</td><td>"+getLv()+"</td><td>"+getExp()+"</td><td>"+sceneName+"</td><td>"+timestring+"</td><td>"+isDelet+"</td></tr></table>";
		
		str+="<hr>";
		str+="<table border='1'><tr><th>----"+GlobalCache.fetchLanguageMap("key2511")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2510")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2509")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2508")+"----</th><th>---"+GlobalCache.fetchLanguageMap("key2513")+"---</th><th>---"+GlobalCache.fetchLanguageMap("key2516")+"---</th><th>---"+GlobalCache.fetchLanguageMap("key2532")+"---</th><th>---"+GlobalCache.fetchLanguageMap("key2533")+"---</th></tr>";
		str+="<tr><td>"+userPo.getDiamond()+"</td><td>"+getBindDiamond()+"</td><td>"+getGold()+"</td><td>"+getBindGold()+"</td><td>"+getSkillPoint()+"</td><td>"+getAchievePoint()+"</td><td>"+getPrestige()+"</td><td>"+getPetSoul()+"</td></tr></table>";
		String mainTask="";
		for(int i=0;i<listRoleTasks.size();i++){
			TaskPo task=TaskPo.findEntity(listRoleTasks.get(i).getInt1());
			if(task.getTaskType()==1){
				mainTask+=task.getName();
				break;
			}
		}
		
		String [] pkStatus={"white","grey","red"};
		
		str+="<hr>";
		str+="<table border='1'><tr><th>--"+"ChargeNum"+"--</th><th>---"+"VIPlv"+"---</th><th>---"+"PKstate"+"---</th><th>---"+"mainTask"+"---</th></tr>";
		str+="<tr><td>"+fetchCumulativeRechargeNum()+"</td><td>"+getVipLv()+"</td><td>"+pkStatus[getPkStatus()]+"</td><td>"+mainTask+"</td></tr></table>";
		
		str+="<hr>";
		str+=GlobalCache.fetchLanguageMap("key2534")+getBattlePower();
		
		String wuGong=(getBatMeleeAttack()+getBatMeleeAttackMin())+"-"+(getBatMeleeAttack()+getBatMeleeAttackMax());
		String faGong=(getBatMagicAttack()+getBatMagicAttackMin())+"-"+(getBatMagicAttack()+getBatMagicAttackMax());
		String wuFang=(getBatMeleeDefence()+getBatMeleeDefenceMin())+"-"+(getBatMeleeDefence()+getBatMeleeDefenceMax());
		String faFang=(getBatMagicDefence()+getBatMagicDefenceMin())+"-"+(getBatMagicDefence()+getBatMagicDefenceMax());
		
		
		str+="<table border='1'><tr><th>----"+GlobalCache.fetchLanguageMap("key2535")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2536")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2537")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2538")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2539")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2540")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2541")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2542")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2543")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2544")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2545")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2546")+"----</th></tr>";
		str+="<tr><td>"+wuGong+"</td><td>"+faGong+"</td><td>"+wuFang+"</td><td>"+faFang+"</td><td>"+getBatHp()+"</td><td>"+getBatMaxMp()+"</td><td>"+getBatHitRate()+"</td><td>"+getBatDodge()+"</td><td>"+getBatCritical()+"</td><td>"+getBatTough()+"</td><td>"+getBatHpReg()+"</td><td>"+getBatMpReg()+"</td></tr>";
		str+="<tr><th>--"+GlobalCache.fetchLanguageMap("key2547")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2548")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2549")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2550")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2551")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2552")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2553")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2554")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2555")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2556")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2557")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2558")+"--</th></tr>";
		str+="<tr><td>"+getBatLuckyAttack()+"</td><td>"+getBatLuckyDefence()+"</td><td>"+(getBatCriticalDamageRate()/10d)+"%</td><td>"+(batDamageRate/10d)+"%</td><td>"+(getBatDamageResistRate()/10d)+"%</td><td>"+(getBatAttackRate()/10d)+"%</td><td>"+(getBatDefenceRate()/10d)+"%</td><td>"+(getBatHitRateRate()/10d)+"%</td><td>"+(getBatDodgeRate()/10d)+"%</td><td>"+(getBatAddCriticalRate()/10d)+"%</td><td>"+(getBatDefenceRate()/10d)+"%</td><td>"+(getBatReboundDamage()/10d)+"%</td></tr></table>";
		
		List<EqpPo> eqps=new ArrayList<EqpPo>();
		
		List<List<String>> functions=new ArrayList<List<String>>();
		
		List<String> eqpNames=new ArrayList<String>();
		List<String> eqpLv=new ArrayList<String>();
		List<String> eqpQuality=new ArrayList<String>();
		List<String> powLv=new ArrayList<String>();
		List<String> randomAtb1=new ArrayList<String>();
		List<String> randomAtb2=new ArrayList<String>();
		List<String> randomAtb3=new ArrayList<String>();
		List<String> starLv=new ArrayList<String>();
		List<String> wash1=new ArrayList<String>();
		List<String> wash2=new ArrayList<String>();
		List<String> wash3=new ArrayList<String>();
		List<String> gem1=new ArrayList<String>();
		List<String> gem2=new ArrayList<String>();
		List<String> gem3=new ArrayList<String>();
		List<String> gem4=new ArrayList<String>();
		
		functions.add(eqpNames);
		functions.add(eqpLv);
		functions.add(eqpQuality);
		functions.add(powLv);
		functions.add(randomAtb1);
		functions.add(randomAtb2);
		functions.add(randomAtb3);
		functions.add(starLv);
		functions.add(wash1);
		functions.add(wash2);
		functions.add(wash3);
		functions.add(gem1);
		functions.add(gem2);
		functions.add(gem3);
		functions.add(gem4);
		
		
		EqpPo eqp1=equipWeapon;
		EqpPo eqp2=equipNecklace;
		EqpPo eqp3=equipRing;
		EqpPo eqp4=equipBracelet;
		EqpPo eqp5=equipArmor;
		EqpPo eqp6=equipPants;
		EqpPo eqp7=equipShoe;
		EqpPo eqp8=equipBracer;
		EqpPo eqp9=equipHelmet;
		EqpPo eqp10=equipBelt;
		eqps.add(eqp1);
		eqps.add(eqp2);
		eqps.add(eqp3);
		eqps.add(eqp4);
		eqps.add(eqp5);
		eqps.add(eqp6);
		eqps.add(eqp7);
		eqps.add(eqp8);
		eqps.add(eqp9);
		eqps.add(eqp10);
		for(int i=0;i<10;i++){
			if(eqps.get(i)!=null){
				ItemPo itemPo=eqps.get(i).itemPo();
				eqpNames.add(itemPo.getName()+"("+eqps.get(i).getId()+")");
				eqpLv.add(""+itemPo.getItemLv());
				eqpQuality.add(""+itemPo.getQuality());
				powLv.add(""+eqps.get(i).getPowerLv());
				if(eqps.get(i).attachList.size()>=1){
					randomAtb1.add(GameUtil.getAtbDescripeByAtbType(eqps.get(i).attachList.get(0).get(0), eqps.get(i).attachList.get(0).get(1)));
				}else{
					randomAtb1.add("");
				}
				if(eqps.get(i).attachList.size()>=2){
					randomAtb2.add(GameUtil.getAtbDescripeByAtbType(eqps.get(i).attachList.get(1).get(0), eqps.get(i).attachList.get(1).get(1)));
				}else{
					randomAtb2.add("");
				}
				if(eqps.get(i).attachList.size()>=3){
					randomAtb3.add(GameUtil.getAtbDescripeByAtbType(eqps.get(i).attachList.get(2).get(0), eqps.get(i).attachList.get(2).get(1)));
				}else{
					randomAtb3.add("");
				}
			}else{
				eqpNames.add("");
				eqpLv.add("");
				eqpQuality.add("");
				powLv.add("");
				randomAtb1.add("");
				randomAtb2.add("");
				randomAtb3.add("");
			}
			SlotSoulVo slotSoulVo =findSlotSoul(i+1);
			if(slotSoulVo.powerLv>0){
				starLv.add(""+slotSoulVo.powerLv);
			}else{
				starLv.add("");
			}
			if(slotSoulVo.extract1Star>0){
				wash1.add(""+slotSoulVo.extract1Star+"★");
			}else{
				wash1.add("");
			}
			if(slotSoulVo.extract2Star>0){
				wash2.add(""+slotSoulVo.extract2Star+"★");
			}else{
				wash2.add("");
			}
			if(slotSoulVo.extract3Star>0){
				wash3.add(""+slotSoulVo.extract3Star+"★");
			}else{
				wash3.add("");
			}
			if(slotSoulVo.gem1Id!=0){
				ItemPo gemItem1=ItemPo.findEntity(slotSoulVo.gem1Id);
				gem1.add(gemItem1.getName());
			}else{
				gem1.add("");
			}
			if(slotSoulVo.gem2Id!=0){
				ItemPo gemItem2=ItemPo.findEntity(slotSoulVo.gem2Id);
				gem2.add(gemItem2.getName());
			}else{
				gem2.add("");
			}
			if(slotSoulVo.gem3Id!=0){
				ItemPo gemItem3=ItemPo.findEntity(slotSoulVo.gem3Id);
				gem3.add(gemItem3.getName());
			}else{
				gem3.add("");
			}
			if(slotSoulVo.gem4Id!=0){
				ItemPo gemItem4=ItemPo.findEntity(slotSoulVo.gem4Id);
				gem4.add(gemItem4.getName());
			}else{
				gem4.add("");
			}
		}
		
		
		String [] functionNames={GlobalCache.fetchLanguageMap("key2559"),GlobalCache.fetchLanguageMap("key2560"),GlobalCache.fetchLanguageMap("key2561"),GlobalCache.fetchLanguageMap("key2562"),GlobalCache.fetchLanguageMap("key2563"),GlobalCache.fetchLanguageMap("key2564"),GlobalCache.fetchLanguageMap("key2565"),GlobalCache.fetchLanguageMap("key2566"),GlobalCache.fetchLanguageMap("key2567"),GlobalCache.fetchLanguageMap("key2568"),GlobalCache.fetchLanguageMap("key2569"),GlobalCache.fetchLanguageMap("key2570"),GlobalCache.fetchLanguageMap("key2571"),GlobalCache.fetchLanguageMap("key2572"),GlobalCache.fetchLanguageMap("key2573")};
		
		str+="<hr>Equip：<br>";
		str+="<table border='1'><tr><th>----tabel----</th><th>----"+GlobalCache.fetchLanguageMap("key2574")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2575")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2576")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2577")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2578")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2579")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2580")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2581")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2582")+"----</th><th>----"+GlobalCache.fetchLanguageMap("key2583")+"----</th></tr>";
		
		for(int i=0;i<functionNames.length;i++){
			str+="<tr><th>"+functionNames[i]+"</th>";
			for(int j=0;j<10;j++){
				str+="<td>";
				str+=functions.get(i).get(j);
				str+="</td>";
			}
			str+="</tr>";
		}
		str+="</table><br>";
		
		str+="<table border='1'><tr><th>--"+GlobalCache.fetchLanguageMap("key2584")+"--</th><th>--"+GlobalCache.fetchLanguageMap("key2585")+"--</th><th>----"+GlobalCache.fetchLanguageMap("key2586")+"----</th></tr>";
		String winstr=""+(getWingStar()/11+1)+"step"+getWingStar()%11+"star";
		str+="<tr><td>"+winstr+"</td><td>"+getTitleLv()+"</td>";
		String shizhuang="";
		if(roleFashion!=null){
			for(RoleFashionVo roleFashionVo:roleFashion){
				shizhuang+="<td>"+roleFashionVo.fashionPo.getName()+"</td>";
			}
		}
		str+=shizhuang+"</tr></table>";
		str+="<hr>PET：<br>";
		str+="<table border='1'><tr><th>----tabel----</th>";
		for(int i=0;i<listRpets.size();i++){
			str+="<th>--";
			str+=listRpets.get(i).petPo().getName()+"("+listRpets.get(i).getId()+")";
			str+="--</th>";
		}
		str+="<tr><td>id</td>";
		for(int i=0;i<listRpets.size();i++){
			str+="<td>";
			str+=listRpets.get(i).getId();
			str+="</td>";
		}
		str+="<tr><td>quality</td>";
		for(int i=0;i<listRpets.size();i++){
			str+="<td>";
			str+=listRpets.get(i).petPo().getQuality();
			str+="</td>";
		}
		str+="<tr><td>level</td>";
		for(int i=0;i<listRpets.size();i++){
			str+="<td>";
			PetUpgradePo petUpgradePo=PetUpgradePo.findEntity(listRpets.get(i).getLv());
			str+=petUpgradePo.getStep()+"step"+petUpgradePo.getLv()+"level";
			str+="</td>";
		}
		for(int j=0;j<10;j++){
			str+="<tr><td>skll"+(j+1)+"</td>";
			for(int i=0;i<listRpets.size();i++){
				str+="<td>";
				if(listRpets.get(i).skillIds.size()>j){
					str+=listRpets.get(i).skillIds.get(j);
				}
				str+="</td>";
			}
		}
		str+="</table><br>";
		
		/*************************好友列表 start*************************/
		str+="<hr>Friends：<br>";
		str+="<table border='1'><tr><th>----roleName----</th></tr>";
		for (RoleInforPo roleInforPo : listFriends) {
			if (roleInforPo != null) {
				str+="<tr><th>"+roleInforPo.getRoleName()+"</th></tr>";
			}
		}
		str+="</table><br>";
		/*************************好友列表 end*************************/
		
		/*************************黑名单列表 start*************************/
		str+="<hr>Blocks：<br>";
		str+="<table border='1'><tr><th>----roleName----</th></tr>";
		for (RoleInforPo roleInforPo : listBlocks) {
			if (roleInforPo != null) {
				str+="<tr><th>"+roleInforPo.getRoleName()+"</th></tr>";
			}
		}
		str+="</table><br>";
		/*************************黑名单列表 end*************************/
		
		/*************************仇人列表 start*************************/
		str+="<hr>Enemys：<br>";
		str+="<table border='1'><tr><th>----roleName----</th></tr>";
		for (RoleInforPo roleInforPo : listEnemys) {
			if (roleInforPo != null) {
				str+="<tr><th>"+roleInforPo.getRoleName()+"</th></tr>";
			}
		}
		str+="</table><br>";
		/*************************仇人列表 end*************************/
		
		str+="<hr>"+GlobalCache.fetchLanguageMap("key2587")+"：<br>";
		str+="<table border='1'><tr><th>----tabel----</th><th>----atb1----</th><th>----atb2----</th><th>----atb3----</th><th>----atb4----</th>";
		for(int j=0;j<6;j++){
			str+="<tr><td>star"+(j+1)+"</td>";
			for(int i=0;i<4;i++){
				str+="<td>";
				if(petConstellMap.get(j+1)!=null){
					if(petConstellMap.get(j+1).size()>i){
						str+=petConstellMap.get(j+1).get(i).name+petConstellMap.get(j+1).get(i).attachLevel+"level";
					}
				}
				str+="</td>";
			}
		}
		str+="</table><br>";
		
		
		str+="<hr>bag：<br><table border='1'><tr>";
		str+="<table border='1'><tr><th>------1------</th><th>------2------</th><th>------3------</th><th>------4------</th><th>------5------</th><th>------6------</th><th>------7------</th><th>------8------</th><th>------9------</th><th>------10------</th></tr>";
		int i=0;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			str+="<td>";
			str+=rolePackItemVo.itemPo().getName()+"*"+rolePackItemVo.getNum();
			str+="</td>";
			if(i%10==9){
				str+="<tr>";
			}
			i=i+1;
		}
		str+="</table><br>";
		
		
		str+="<hr>warehouse：<br><table border='1'><tr>";
		str+="<table border='1'><tr><th>------1------</th><th>------2------</th><th>------3------</th><th>------4------</th><th>------5------</th><th>------6------</th><th>------7------</th><th>------8------</th><th>------9------</th><th>------10------</th></tr>";
		int j=0;
		for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
			str+="<td>";
			str+=rolePackItemVo.itemPo().getName()+"*"+rolePackItemVo.getNum();
			str+="</td>";
			if(j%10==9){
				str+="<tr>";
			}
			j=j+1;
		}
		str+="</table><br>";
		//背包装备
		str += "<hr>all equipment details：<br>";
		str += "<table border='1'><tr><th>--id--</th><th>--" + GlobalCache.fetchLanguageMap("key2559") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2560") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2561") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2562") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2563") + "--</th><th>--"+ GlobalCache.fetchLanguageMap("key2564") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2565") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2638") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2639") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2640") + "--</th><th>--" + GlobalCache.fetchLanguageMap("key2641") + "--</th></tr>";
		Map<String, RolePackItemVo> allItemVosMap = new HashMap<String, RolePackItemVo>();
		List<RolePackItemVo> list = new ArrayList<RolePackItemVo>();
		list.addAll(mainPackItemVosMap.values());
		list.addAll(warehousePackItemVosMap.values());
		Collections.sort(list, new Comparator<RolePackItemVo>() {
			@Override
			public int compare(RolePackItemVo o1, RolePackItemVo o2) {
				return o1.itemPo().getName().compareTo(o2.itemPo().getName());
			}
		});
		for (RolePackItemVo rolePackItemVo : list) {
			if (rolePackItemVo.wasEquip()) {
				EqpPo eqpPo = rolePackItemVo.eqpPo;
				ItemPo itemPo = rolePackItemVo.itemPo();
				str += "<tr><td>" + eqpPo.getId() + "</td>";//id
				str += "<td>" + itemPo.getName() + "</td>";//装备名
				str += "<td>" + itemPo.getRequireLv() + "</td>";//装备等级
				str += "<td>" + itemPo.getQuality() + "</td>";//品质
				str += "<td>" + eqpPo.getPowerLv() + "</td>";//强化等级
				//随机属性1
				int attachSize = eqpPo.attachList.size();
				str += "<td>";
				if (attachSize >= 1)
					str += GameUtil.getAtbDescripeByAtbType(eqpPo.attachList.get(0).get(0), eqpPo.attachList.get(0).get(1));
				else
					str += randomAtb1.add("");
				str += "</td>";
				//随机属性2
				str += "<td>";
				if (attachSize >= 2)
					str +=  GameUtil.getAtbDescripeByAtbType( eqpPo.attachList.get(1).get(0), eqpPo.attachList.get(1).get(1));
				else
					str += "";
				str += "</td>";
				//随机属性3
				str += "<td>";
				if (attachSize >= 3)
					str +=  GameUtil.getAtbDescripeByAtbType(eqpPo.attachList.get(2).get(0), eqpPo.attachList.get(2).get(1));
				else
					str += "";
				str += "</td>";
				//装备部位
				str += "<td>" + GlobalCache.fetchLanguageMap("key" + (itemPo.getCategory() + 2573)) + "</td>";
				//职业
				str += "<td>" + GlobalCache.fetchLanguageMap("key" + (itemPo.getMatchClass() + 2528)) + "</td>";
				//战斗力
				str += "<td>" + itemPo.equipPower + "</td>";
				//基础属性
				String att = "";
				if (itemPo.getEqpBatAttrExp().length() != 0) {
					Map<Integer, Integer> map = new HashMap<Integer, Integer>();
					for (String one : itemPo.getEqpBatAttrExp().split(",")) {
						map.put(Integer.valueOf(one.split("\\|")[0]), Integer.valueOf(one.split("\\|")[1]));
					}
					att = toUnderstandString(map);
				}
				str += "<td>" + att + "</td>";
			}
		}
		return str;
	}
	
	/**
	 * 将属性map翻译成策划看得懂的String格式
	 * @param map
	 * @return
	 */
	private String toUnderstandString(Map<Integer, Integer> map) {
		String reslut = "";
		Map<Integer, Point> map2 = new HashMap<Integer, Point>();
		map2.put(RoleType.batMeleeAttack, new Point(RoleType.batMeleeAttackMin, RoleType.batMeleeAttackMax));
		map2.put(RoleType.batMagicAttack, new Point(RoleType.batMagicAttackMin, RoleType.batMagicAttackMax));
		map2.put(RoleType.batMeleeDefence, new Point(RoleType.batMeleeDefenceMin, RoleType.batMeleeDefenceMax));
		map2.put(RoleType.batMagicDefence, new Point(RoleType.batMagicDefenceMin, RoleType.batMagicDefenceMax));
		for (Integer key : map2.keySet()) {
			boolean contain1 = map.containsKey(key);
			int min = map2.get(key).x;
			boolean contain2 = map.containsKey(min);
			int max = map2.get(key).y;
			if (contain1 && contain2) {
				int m = map.get(min);
				reslut += GlobalCache.fetchLanguageMap("key" + (key + 2534)) + ":" + (m + map.get(min))
						+ "-" + (m + map.get(max)) + ";";
				map.remove(key);
				map.remove(min);
				map.remove(max);
			} else if (contain1) {
				reslut += GlobalCache.fetchLanguageMap("key" + (key + 2534)) + ":" + map.get(key) + ";";
				map.remove(key);
			} else if (contain2) {
				reslut += GlobalCache.fetchLanguageMap("key" + (key + 2534)) + ":" + map.get(min)
						+ "-" + map.get(max) + ";";
				map.remove(min);
				map.remove(max);
			}
		}
		for (Integer key : map.keySet()) {
			int value = map.get(key);
			reslut += GlobalCache.fetchLanguageMap("key" + (key + 2534)) + ":" + value + ";";
		}
		return reslut;
	}

	/**  累计登录奖励 */
	public void takeCumulativeLoginAward(){
		int num =0;
		for(IdNumberVo idNumberVo : listCumulativeLoginAwardRecord){
			if(idNumberVo.getNum().intValue() == 1){
				num++;
			}
		}
		checkItemPackFull(num);
		List<Day> dayList = XmlCache.xmlFiles.constantFile.totalLogin.day;
		CopyOnWriteArrayList<IdNumberVo> clientShowList = new CopyOnWriteArrayList<IdNumberVo>();
		int index = 0;
		for(IdNumberVo idNumberVo : listCumulativeLoginAwardRecord){
			if (idNumberVo.getNum().intValue() == 1) {
				Day day = dayList.get(idNumberVo.getId()-1);
				List<List<Integer>> dropList =  StringUtil.buildBattleExpressList(day.drop);
				IdNumberVo inv = RandomUtil.calcWeightOverCardAward(dropList, null);
				addItem(inv.getId(), inv.getNum(),1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv.getId(), inv.getNum(), GlobalCache.fetchLanguageMap("key2446"), "");
				clientShowList.add(inv);
				idNumberVo.setNum(2);
				index++;
			}
		}
		if(index > 0){
			listToTakeCumulativeLoginAwardRecord.clear();
			listToTakeCumulativeLoginAwardRecord =clientShowList;
			listActivityInfo.get(0).takeCumulativeLoginTime = System.currentTimeMillis();			
		}
	}
	
	/** 方法功能:获取角色等级奖励*/
	public void takeLevelAward(Integer lv){
		LvConfigPo lcp = GlobalCache.mapLevelAward.get(lv);
		if(lcp == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"LvConfigPo：" + lv);	
		}
		checkItemPackFull(lcp.listLevelAwrod.size());
		for(int i = 0; i < listLevelAwardRecord.size(); i++){
			if(listLevelAwardRecord.get(i).getId().intValue() == lv && listLevelAwardRecord.get(i).getNum().intValue() == 2){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key60")+ lv);	
			}
		}
		for(IdNumberVo idNumberVo : lcp.listLevelAwrod){
			addItem(idNumberVo.getId(), idNumberVo.getNum(),1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, idNumberVo.getId(), idNumberVo.getNum(), GlobalCache.fetchLanguageMap("key2448"), "");
		}
		for(int i = 0; i < listLevelAwardRecord.size(); i++){
			if(listLevelAwardRecord.get(i).getId().intValue() == lv.intValue()){
				listLevelAwardRecord.get(i).setNum(2);
				break;
			}
		}
	}
	
	/** 领取在线奖励 */
	public void takeOnlineTimeAward(Integer onlineId){
		checkOnlineTimeAwrodState(0);
		OnlineTimes onlineTimes = fetchOnlineTimesById(onlineId);
		if(onlineTimes == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"OnlineTimes：" + onlineId);	
		}
		int currentOnlineTime = (int) ((listActivityInfo.get(0).theSameDayOnlineTime + fetchCurrentOnlineTime())/1000);
		
		if(currentOnlineTime < onlineTimes.onlineTime.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key61") + onlineId);	
		}
		for(int i = 0; i < listOnlineTimeAwrodRecord.size(); i++){
			if(listOnlineTimeAwrodRecord.get(i).getId().intValue() == onlineId && listOnlineTimeAwrodRecord.get(i).getNum().intValue() == 2){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key62") + onlineId);	
			}
		}		
		for(int i = 0; i < listOnlineTimeAwrodRecord.size(); i++){
			if(listOnlineTimeAwrodRecord.get(i).getId().intValue() == onlineId.intValue()){
				List<IdNumberVo> onlineAward = IdNumberVo.createList(onlineTimes.award);	
				checkItemPackFull(onlineAward.size());
				for(IdNumberVo idNumberVo : onlineAward){
					addItem(idNumberVo.getId(), idNumberVo.getNum(),1);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, idNumberVo.getId(), idNumberVo.getNum(), GlobalCache.fetchLanguageMap("key2453"), "");
				}
				listOnlineTimeAwrodRecord.get(i).setNum(2);
				listActivityInfo.get(0).takeOnlineAwardTime = System.currentTimeMillis();
				break;
			}
		}
		LogUtil.writeLog(this, 312,this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	/** 领取幸运转盘奖励 */
	public List<IdNumberVo> takeLuckyWheelAward(Integer type){
		checkItemPackFull(1);
		List<Item> luckyWhell = XmlCache.xmlFiles.constantFile.luckyWheel.item;
		List<List<Integer>> dropList =  StringUtil.buildBattleExpressList(luckyWhell.get(0).award);
		List<List<Integer>> dropList2 =  StringUtil.buildBattleExpressList(luckyWhell.get(0).award2);
		ConsumPo consumOnlyWheel = ConsumPo.findEntity(10);
		ConsumPo consumTenWhell = ConsumPo.findEntity(11);
		List<IdNumberVo> clientShowList = new ArrayList<IdNumberVo>();
		if(type == 1)
		{
			if(fetchSameDayLuckyWheelNumberOfFree() > consumOnlyWheel.getDayFreeTime().intValue() ){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key63") + fetchSameDayLuckyWheelNumberOfFree()+"/"+consumOnlyWheel.getDayFreeTime());
			}
			if(System.currentTimeMillis() < listActivityInfo.get(0).takeLuckyWheelFreeNextTime.longValue() ){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key61") + listActivityInfo.get(0).takeLuckyWheelFreeNextTime.longValue());
			}
			clientShowList = RandomUtil.fetchOverCardAwardByCount(dropList, null, 1);
			listActivityInfo.get(0).takeLuckyWheelFreeNextTime = System.currentTimeMillis() + 599000;
			listActivityInfo.get(0).adjustSameDayLuckyWheelNumberOfFree(1);
			LogUtil.writeLog(this, 313, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if (type == 2)
		{
			int consumNum = fetchConsum(consumOnlyWheel, 0);
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,consumNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2449"), "");
			checkHasAndConsumeBindDiamondThenDiamond(consumNum);
			clientShowList = RandomUtil.fetchOverCardAwardByCount(dropList2, null, 1);
			adjustConsumCostById(10, 1);
			LogUtil.writeLog(this, 314, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if(type == 3){
			int consumNum = fetchConsum(consumTenWhell, 0);
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,consumNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2450"), "");
			checkHasAndConsumeBindDiamondThenDiamond(consumNum);
			for(int i=0;i<10;i++){
				IdNumberVo idNumberVo = RandomUtil.calcWeightOverCardAward(dropList2, null);
				clientShowList.add(idNumberVo);
			}
			adjustConsumCostById(11, 1);
			LogUtil.writeLog(this, 315, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		
		for(IdNumberVo idNumberVo : clientShowList){
			addItem(idNumberVo.getId(), idNumberVo.getNum(),1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, idNumberVo.getId(), idNumberVo.getNum(), GlobalCache.fetchLanguageMap("key2451"), "");
		}
		return clientShowList;
	}
	/**
	 * 方法功能:领取月签到奖励
	 * 更新时间:2014-12-15, 作者:peter
	 * @param 是否补签 0：正常； 1：补签 
	 * @return
	 */
	public void takeSignInReward(Integer wasSignedSupplement){
		int month = DateUtil.getTodayMonth(System.currentTimeMillis());
		int day = DateUtil.getCurDay();
		IdNumberVo2 idNumberVo2 = fetchSignedSupplementDate();
		
		if(idNumberVo2 == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key109"));
		}
		
		if(wasSignedSupplement.intValue() == 1 && listActivityInfo.get(0).signInAwardSameDayIsTake.intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2270"));
		}
		// 签到下标
		int index = -1;
		if(wasSignedSupplement.intValue() == 0){
			index = listActivityInfo.get(0).signInAwardCount;
			if(listActivityInfo.get(0).signInAwardSameDayIsTake == 1){
				index = index - 1;
			}
		}else if(wasSignedSupplement.intValue() == 1){
			index = idNumberVo2.getInt2();
//			ConsumPo consumPo = ConsumPo.findEntity(16);
//			checkHasAndConsumeBindDiamondThenDiamond(consumPo.getConsumNum());
			checkHasItemElseDiamond(16, 0);
		}
		if(index == -1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key109"));
		}
		
		List<MonthItem> monthItemList = XmlCache.xmlFiles.constantFile.monthAward.monthItem;
		List<List<Integer>> dropList =  StringUtil.buildBattleExpressList(monthItemList.get(month-1).award);
		if( index >= dropList.size()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key109"));
		}
		List<Integer> awardList = dropList.get(index);
		
		if(awardList.get(2).intValue() == 0){
			if(listSignInAwardRecord.get(index).getInt3() == 1){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key64"));
			}
		}
		if(listSignInAwardRecord.get(index).getInt3() >= 2){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key64"));
		}
		if(listActivityInfo.get(0).signInAwardCount.intValue() >= day){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2269"));
		}
		if(listSignInAwardRecord.get(index).getInt3() == 1 && awardList.get(2).intValue() != 0){
			if(getVipLv() == 0 || getVipLv().intValue() < awardList.get(2).intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key65"));
			}
		}
		
		
		if(listSignInAwardRecord.get(index).getInt3() == 0){
			addItem(awardList.get(0), awardList.get(1),1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, awardList.get(0), awardList.get(1), GlobalCache.fetchLanguageMap("key2454"), "");
			listSignInAwardRecord.get(index).setInt3(1);
			listActivityInfo.get(0).adjustSignInAwardCount(1);
			listActivityInfo.get(0).signInAwardSameDayIsTake = 1;
		}

		if(getVipLv().intValue() >= awardList.get(2).intValue() && awardList.get(2).intValue() != 0){
			if(listSignInAwardRecord.get(index).getInt3().intValue() == 1){
				addItem(awardList.get(0), awardList.get(1),1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, awardList.get(0), awardList.get(1), GlobalCache.fetchLanguageMap("key2455"), "");
				listSignInAwardRecord.get(index).setInt3(2);
			}
		}
		LogUtil.writeLog(this, 311, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	/** 领取活跃度任务完成积分奖励 */
	public void takeDailyLivelyTaskFinishScoreReward(Integer livelyId){
		List<LivelyItem> livelyItemList = XmlCache.xmlFiles.constantFile.livelyAward.livelyItem;
		LivelyItem livelyItem = null;
		for(LivelyItem li : livelyItemList){
			if(li.id.intValue() == livelyId){
				livelyItem = li;
			}
		}
		if(livelyItem == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"LivelyItem："+livelyId);
		}
		List<IdNumberVo> awardList = IdNumberVo.createList(livelyItem.award);
		for(IdNumberVo idNumberVo : listDailyLivelyAwardRecord){
			if(idNumberVo.getId().intValue() == livelyId){
				if(idNumberVo.getNum().intValue() == 2){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key66")+"livelyId："+livelyId);
				}
				if(idNumberVo.getNum().intValue() == 1){
					addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2601"));
					idNumberVo.setNum(2);
				}
				break;
			}
		}
		LogUtil.writeLog(this, 352, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	/** 领取首冲奖励*/
	public void takeFristRechargeAwards(){
		if(listRechargeInfo.get(0).wasFirstRecharge != 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key67"));
		}
		if(listRechargeInfo.get(0).wasTakeFirstRechargeAwards !=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key64"));
		}
		String award = XmlCache.xmlFiles.constantFile.firstRecharge.awards;
		List<IdNumberVo> awardList = IdNumberVo.createList(award);
		addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2602"));
		listRechargeInfo.get(0).wasTakeFirstRechargeAwards = 1;
		StringBuffer sb = new StringBuffer();
		sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key55")).append(getName()).append(GlobalCache.fetchLanguageMap("key68"));
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		chatService.sendHorse(sb.toString());
		chatService.sendSystemWorldChat(sb.toString());
	}
	
	/**  领取月卡*/
	public void takeMonthCard(){
		if(listRechargeInfo.get(0).wasMonthCard != 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key69"));
		}
		if(listRechargeInfo.get(0).todayWasTakeMonthCard != 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key70"));
		}
		String award = XmlCache.xmlFiles.constantFile.monthCard.awards;
		List<IdNumberVo> awardList = IdNumberVo.createList(award);
		addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2452"));
		listRechargeInfo.get(0).todayWasTakeMonthCard = 1;
		listRechargeInfo.get(0).todayTakeMonthCardTime = System.currentTimeMillis();
		listRechargeInfo.get(0).adjustRemainMonthCardDay(-1);
		LogUtil.writeLog(this, 317, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	/** 领取每日vip奖励*/
	public void takeDailyVipAward(){
		checkItemPackFull(1);
		if(listRechargeInfo.get(0).wasTakeDailyVipAward != 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key70"));
		}
		
		VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
		Integer awardId = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_14);
		addItem(awardId, 1, 1);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, awardId, 1, GlobalCache.fetchLanguageMap("key2447"), "");
		listRechargeInfo.get(0).wasTakeDailyVipAward = 1;
		LogUtil.writeLog(this, 316, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	/** 领取运营活动奖励*/
	public void awardLiveActivity(Integer activityId,Integer index){
		boolean matched=false;
		for (RoleLiveActivityVo roleLiveActivityVo : listRoleLiveActivitys) {
			if(roleLiveActivityVo.liveActivityId==activityId.intValue()){
//				System.out.println("11roleLiveActivityVo="+roleLiveActivityVo);
				if(index > roleLiveActivityVo.objs.size()){
					continue;
				}
				IdNumberVo3 idNumberVo3 = roleLiveActivityVo.objs.get(index-1);
				LiveActivityPo liveActivityPo = LiveActivityPo.findEntity(roleLiveActivityVo.liveActivityId);
				if(liveActivityPo==null){
					continue;
				}
				boolean mode2=false;
				if(liveActivityPo.listConditionModes.size()>0){
					//模式为2
					if(liveActivityPo.listConditionModes.get(index-1)==2){
						int finishedTimes = idNumberVo3.getInt4();
						int currentProgress = idNumberVo3.getInt2();
						int targetVal = liveActivityPo.listConditions.get(index-1).getNum();
						if(finishedTimes>=(currentProgress/targetVal)){
							ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key2297"));
						}
						mode2=true;
					}
				}

				if(!mode2 && idNumberVo3.getInt3().intValue()!=-1 && idNumberVo3.getInt4().intValue() >= idNumberVo3.getInt3().intValue()){
					ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key2297"));
				}
				
				if(liveActivityPo.getType()==LiveActivityType.LiveActivity_EXCHANGE){
					List<IdNumberVo>  requireList = liveActivityPo.listExchangeItems.get(index-1);
					if(!hasItemList(requireList)){
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key29"));
					}else{
					    consumeItemList(requireList,GlobalCache.fetchLanguageMap("key2656"));
					}
				}
				else{
					//需要达到的目标
					int targetVal = liveActivityPo.listConditions.get(index-1).getNum()*(1+idNumberVo3.getInt4());
					if(idNumberVo3.getInt2().intValue() < targetVal){
						ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key72"));
					}
				}
				
//				索引 进度 状态 已完成次数
				idNumberVo3.addNum4(1);
				matched=true;
//				System.out.println("22roleLiveActivityVo="+roleLiveActivityVo);
			}
		}
		if(!matched){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key75"));
		}
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getId()==activityId.intValue()){
				addItem(liveActivityPo.listAwardItems.get(index-1),GlobalCache.fetchLanguageMap("key2611")+"id:"+activityId+"index:"+index);
			}
		}
	}
	
	private void addItem(List<IdNumberVo2> list,String logString) {
		for (IdNumberVo2 idNumberVo2 : list) {
			addItem(idNumberVo2.getInt1(), idNumberVo2.getInt2(), idNumberVo2.getInt3());
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, idNumberVo2.getInt1(), idNumberVo2.getInt2(),logString, "");
		}
		
	}

	/**
	 * 膜拜
	 * @param worshipType 膜拜类型 1：免费； 2：钻石
	 * @return
	 */
	public void worship(Integer worshipType){
		if(worshipType== null || (worshipType.intValue()!= 1 && worshipType.intValue() !=2)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key73")+worshipType);
		}
		if(worshipType.intValue() == 1 && listActivityInfo.get(0).dailyWorshipGoldStatus.intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key74"));
		}
		
		if(worshipType.intValue() ==2 && listActivityInfo.get(0).dailyWorshipDiamondStatus.intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key74"));
		}
		
		List<WorshipItem> worshipItemList  = XmlCache.xmlFiles.constantFile.worship.worshipItem;
		WorshipItem worshipItem =worshipItemList.get(worshipType-1);
		List<IdNumberVo> awardList = IdNumberVo.createList(worshipItem.awards);
		if(worshipType.intValue() == 1){
			listActivityInfo.get(0).dailyWorshipGoldStatus =1;		
			fetchDailyWorshipGoldStatus();
			LogUtil.writeLog(this, 353, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}else if(worshipType.intValue() == 2){
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
			String language = String.valueOf(gp.valueObj);
			if("ko".equals(language)){
				if(getWorshipDiamondFirst()!=0){
					checkHasAndConsumeDiamond(worshipItem.diamond);
				}else{
					setWorshipDiamondFirst(1);
				}
			}else{
				checkHasAndConsumeDiamond(worshipItem.diamond);
			}
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -worshipItem.diamond, GlobalCache.fetchLanguageMap("key2476"), "");
			listActivityInfo.get(0).dailyWorshipDiamondStatus =1;	
			fetchDailyWorshipDiamondStatus();
			LogUtil.writeLog(this, 354, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2603"));
		taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_732,null,null);
	}
	/**
	 * 获取离线奖励
	 * @param rewardType 奖励类型 1：普通；2:2倍领取； 3:3倍领取（只针对exp）
	 * @return
	 */
	public void fetchOffLineReward(Integer rewardType){
		IdNumberVo bindGoldVo = IdNumberVo.findIdNumber(1, listOffLineReward);
		IdNumberVo expVo = IdNumberVo.findIdNumber(2, listOffLineReward);
		if(bindGoldVo != null && expVo != null){
			int num =1;
			int diamond = 0;
			if(rewardType.intValue() == 2 ){
				diamond =DoubleUtil.toUpInt(1d*expVo.getNum()/25000*5);
				num=2;
				LogUtil.writeLog(this, 355, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(rewardType.intValue() == 3){
				diamond =DoubleUtil.toUpInt(1d*expVo.getNum()/25000*10);
				num =3;
				LogUtil.writeLog(this, 356, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}
			if(rewardType.intValue() == 1 ){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, expVo.getNum()*num, GlobalCache.fetchLanguageMap("key2396"), "");
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, bindGoldVo.getNum(), GlobalCache.fetchLanguageMap("key2396"), "");
			}
			if(rewardType.intValue() == 2 ){
				checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, diamond);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -diamond, GlobalCache.fetchLanguageMap("key2397"), "");
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, expVo.getNum()*num, GlobalCache.fetchLanguageMap("key2398"), "");
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, bindGoldVo.getNum(), GlobalCache.fetchLanguageMap("key2396"), "");
			}else if(rewardType.intValue() == 3){
				checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, diamond);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -diamond, GlobalCache.fetchLanguageMap("key2399"), "");
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, expVo.getNum()*num, GlobalCache.fetchLanguageMap("key2400"), "");
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, bindGoldVo.getNum(), GlobalCache.fetchLanguageMap("key2396"), "");
			}
			checkHasAndConsumeResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, diamond);
			adjustExp(expVo.getNum()*num);
			adjustNumberByType(bindGoldVo.getNum() ,RoleType.RESOURCE_BIND_GOLD);
		}
		setOffLineRewardMinutes(0);
		listOffLineReward.clear();
	}

	public void sendSessionLost() {
		singleRole("PushRemoting.sendSessionLost", new Object[]{},true);
	}
	
	/** 出售背包物品*/
	public IdNumberVo sellPackItem(Integer index, Integer num){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		checkHasRolePackIndex(index);
		RolePackItemVo rolePackItemVo = fetchRolePackItemByIndex(index);
		int bindStatus = rolePackItemVo.bindStatus;
		int itemId = rolePackItemVo.getItemId();
		checkService.checkExistItemPo(rolePackItemVo.getItemId());
		ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
		if(rolePackItemVo.getNum().intValue() < num.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key122"));
		}
 		int addPrice = itemPo.getSellPrice().intValue()*num.intValue();
 		//回收类型1=金币\n2=灵魂宝石\n3=祝福宝石\n4=玛雅宝石\n5=星尘
 		IdNumberVo idNumberVo = new IdNumberVo();
 		switch (itemPo.getRecoveryType()) {
		case 1:
			adjustNumberByType(addPrice, RoleType.RESOURCE_BIND_GOLD);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, addPrice, GlobalCache.fetchLanguageMap("key2421"), "");
			idNumberVo = null;
			break;
		case 2:
			addItem(300001001, addPrice, bindStatus);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001001, addPrice, GlobalCache.fetchLanguageMap("key2421"), "");
			idNumberVo.setId(300001001);
			idNumberVo.setNum(addPrice);
			break;
		case 3:
			addItem(300001002, addPrice, bindStatus);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001002, addPrice, GlobalCache.fetchLanguageMap("key2422"), "");
			idNumberVo.setId(300001002);
			idNumberVo.setNum(addPrice);
			break;
		case 4:
			addItem(300001003, addPrice, bindStatus);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001003, addPrice, GlobalCache.fetchLanguageMap("key2423"), "");
			idNumberVo.setId(300001003);
			idNumberVo.setNum(addPrice);
			break;
		case 5:
			if(addPrice>0){
				adjustNumberByType(addPrice, RoleType.RESOURCE_GAMSTONE);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 0, addPrice, GlobalCache.fetchLanguageMap("key2709"), "");
				idNumberVo = null;
			}
			break;
		}
		removeItemFromMainPack(index,num,GlobalCache.fetchLanguageMap("key2658"), true);
		if(itemPo.getCategory().intValue() <= 10){
			taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_735,null,null);			
		}
		return idNumberVo;
	}
	
	public List<Integer>  sellPackItemOneKey(String indexStr){
		List<IdNumberVo> addList = new ArrayList<IdNumberVo>();
		addList.add(new IdNumberVo(1, 0));
		addList.add(new IdNumberVo(0, 0));
		addList.add(new IdNumberVo(1, 0));
		addList.add(new IdNumberVo(0, 0));
		addList.add(new IdNumberVo(1, 0));
		addList.add(new IdNumberVo(0, 0));
		addList.add(new IdNumberVo(1, 0));
		List<Integer> itemIds = new ArrayList<Integer>();
		List<Integer> indexes = StringUtil.getListByStr(indexStr);
		for (int i=0;i<indexes.size();i++) {
			RolePackItemVo rolePackItemVo =fetchRolePackItemByIndex(indexes.get(i));
			if(rolePackItemVo == null ){
//				System.out.println("indexes.get("+i+") = "+indexes.get(i));
				continue;
			}
			ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
			if(itemPo.getRecoveryType().intValue() == 1){
				addList.get(0).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 2 && rolePackItemVo.bindStatus == 0){
				addList.get(1).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 2 && rolePackItemVo.bindStatus == 1){
				addList.get(2).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 3 && rolePackItemVo.bindStatus == 0){
				addList.get(3).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 3 && rolePackItemVo.bindStatus == 1){
				addList.get(4).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 4 && rolePackItemVo.bindStatus == 0){
				addList.get(5).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}else if(itemPo.getRecoveryType().intValue() == 4 && rolePackItemVo.bindStatus == 1){
				addList.get(6).addNum(itemPo.getSellPrice().intValue()*rolePackItemVo.getNum().intValue());
			}
			itemIds.add(rolePackItemVo.getItemId());
			if(itemPo.getCategory().intValue() <= 10){
				taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_735,null,null);			
			}
		}
		for (Integer removeIndex : indexes) {
			removeItemFromMainPack(removeIndex,GlobalCache.fetchLanguageMap("key2424"),true);
		}
		//回收类型1=金币\n2=灵魂宝石\n3=祝福宝石\n4=玛雅宝石
		if(addList.get(0).getNum() > 0){
			adjustPriorityBindgoldThenGold(addList.get(0).getNum());
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, addList.get(0).getNum(), GlobalCache.fetchLanguageMap("key2424"), "");
		}
		if(addList.get(1).getNum() > 0){
			addItem(300001001, addList.get(1).getNum(), 0);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001001, addList.get(1).getNum(), GlobalCache.fetchLanguageMap("key2425"), "");
		}
		if(addList.get(2).getNum() > 0){
			addItem(300001001, addList.get(2).getNum(), 1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001001, addList.get(2).getNum(), GlobalCache.fetchLanguageMap("key2426"), "");
		}
		if(addList.get(3).getNum() > 0){
			addItem(300001002, addList.get(3).getNum(), 0);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001002,addList.get(3).getNum(), GlobalCache.fetchLanguageMap("key2427"), "");
		}
		if(addList.get(4).getNum() > 0){
			addItem(300001002, addList.get(4).getNum(), 1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001002,addList.get(4).getNum(), GlobalCache.fetchLanguageMap("key2428"), "");
		}
		if(addList.get(5).getNum() > 0){
			addItem(300001003, addList.get(5).getNum(), 0);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001003, addList.get(5).getNum(), GlobalCache.fetchLanguageMap("key2429"), "");
		}
		if(addList.get(6).getNum() > 0){
			addItem(300001003, addList.get(6).getNum(), 0);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300001003, addList.get(6).getNum(), GlobalCache.fetchLanguageMap("key2430"), "");
		}
		return itemIds;
	}
	
	public Integer powerEquip(Integer equipId,Integer itemId){
		ChatService chatService =(ChatService) BeanUtil.getBean("chatService");
		EqpPo eqpPo = EqpPo.findEntity(equipId);
		if(eqpPo.getPowerLv().intValue() >= 20){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2296"));
		}
		int result = 0;
		int poss=0;
		EquipPower equipPower= XmlCache.xmlFiles.constantFile.equipPower;
		Power power = equipPower.power.get(eqpPo.getPowerLv());
		checkHasAndConsumeItem(itemId, 1,GlobalCache.fetchLanguageMap("key2655"));
		ItemPo itemPo  = ItemPo.findEntity(itemId);

		if(itemPo.getCategory()==ItemType.ITEM_CATEGORY_POWER_STONE_1){
			poss=power.lowerRate;
		}
		else if(itemPo.getCategory()==ItemType.ITEM_CATEGORY_POWER_STONE_2){
			poss=power.middleRate;
		}
		else if(itemPo.getCategory()==ItemType.ITEM_CATEGORY_POWER_STONE_3){
			poss=power.highRate;
		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key123"));
		}
		taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_704,null,null);
		VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
		int flag = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_12);
		poss+=flag;
		
		if(RandomUtil.random(poss)){
			result=1;
		}
		if(result==1){
			eqpPo.setPowerLv(eqpPo.getPowerLv()+1);
//			System.out.println("11eqpPoId="+eqpPo.getId() +"; powerLv="+eqpPo.getPowerLv());
			checkpowerEquip(listRoleTasks);
			checkpowerEquip(listRoleAchievesTasks);
			if(eqpPo.getPowerLv().intValue() == 9 || eqpPo.getPowerLv().intValue() ==12 || eqpPo.getPowerLv().intValue() ==15){
				ItemPo itemInfo = ItemPo.findEntity(eqpPo.getItemId());
//				StringBuffer sb = new StringBuffer();
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//				sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key124"));
//				sb.append(ColourType.fetchColourByQuality(itemInfo.getQuality())).append(itemInfo.getName());
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key125"));
//				sb.append(ColourType.COLOUR_GOLDEN).append(eqpPo.getPowerLv());
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
				String str =MessageFormat. format(GlobalCache.fetchLanguageMap("key2614"), getName() , ColourType.fetchColourByQuality(itemInfo.getQuality()),itemInfo.getName(),eqpPo.getPowerLv());
				chatService.sendHorse(str.toString());
				chatService.sendSystemWorldChat(str.toString());
				CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(), fashion, getCareer(), getWingWasHidden(),getWingStar(),hiddenFashions);
				if(fighter != null){
					fighter.makeAvatars(commonAvatarVo,true);					
				}
			}
		}
		else{
			int punishInt = IntUtil.getRandomInt(1, 100);
			int minLv = 0;
			if(eqpPo.getPowerLv()>=3){
				minLv=3;
			}
			if(eqpPo.getPowerLv()>=6){
				minLv=6;
			}
			if(eqpPo.getPowerLv()>=9){
				minLv=9;
			}
			if(eqpPo.getPowerLv()>=12){
				minLv=12;
			}
			if(eqpPo.getPowerLv()>=15){
				minLv=15;
			}
			if(punishInt<=35){
				result=2;
			}
			else if(punishInt<=75){
				result=eqpPo.getPowerLv()-Math.max(minLv,eqpPo.getPowerLv()-1)+2;
				eqpPo.setPowerLv(Math.max(minLv,eqpPo.getPowerLv()-1));
			}
			else{
				result=eqpPo.getPowerLv()-Math.max(minLv,eqpPo.getPowerLv()-2)+2;
				eqpPo.setPowerLv(Math.max(minLv,eqpPo.getPowerLv()-2));
			}
		}
	
		ItemPo eqpItem=ItemPo.findEntity(eqpPo.getItemId());
		if(result==1){
			LogUtil.writeLog(this, 211, eqpPo.getPowerLv(), fetchItemCount(itemId), itemPo.getCategory()-20, GlobalCache.fetchLanguageMap("key2412")+eqpItem.getName(), "");
		}else if(result==3||result==4){
			LogUtil.writeLog(this, 212, eqpPo.getPowerLv()+result-2, fetchItemCount(itemId), itemPo.getCategory()-20, GlobalCache.fetchLanguageMap("key2413")+(result-2)+eqpItem.getName(), "");
		}else if(result==2){
			LogUtil.writeLog(this, 234, eqpPo.getPowerLv(), fetchItemCount(itemId), itemPo.getCategory()-20, GlobalCache.fetchLanguageMap("key2414")+eqpItem.getName(), "");
		}
		LogUtil.writeLog(this, 319, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		return result;
	}
	
	public void auctionSell(Integer roleMainPackIndex,Integer count,Integer totalPrice, Integer sellType){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
		int sum = fetchAuctionMySellList().size();
		if(sum >= 20){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key126"));
		}
		if(totalPrice.intValue() < 100){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key127"));
		}
		if(sellType.intValue() == 1){
			checkHasResource(RoleType.RESOURCE_DIAMOND, count);
			if(count <= 0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key26"));
			}
			consumeDiamond(-count);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -count, GlobalCache.languageMap.get("key2664"), "");
			// 出售钻石
			ItemPo itemPo = ItemPo.findEntity(300004054);
			AuctionItemPo auctionItemPo = new AuctionItemPo();
			auctionItemPo.setEquipId(0);
			auctionItemPo.setItemId(itemPo.getId());
			auctionItemPo.setNum(count);
			auctionItemPo.setSellerRoleId(getId());
			auctionItemPo.setSellerRoleName(getName());
			auctionItemPo.setSellTime(System.currentTimeMillis());
			auctionItemPo.setTotalPrice(totalPrice);
			auctionItemPo.setSellItemName(itemPo.getName());
			auctionItemPo.setSellItemCategory(itemPo.getCategory());
			auctionItemPo.setSellItemType(itemPo.getType());
			auctionItemPo.setRequireLv(itemPo.getRequireLv());
			auctionItemPo.setSellEquipPowerLv(0);
			auctionItemPo.setSellExpirationTime(System.currentTimeMillis() + (2*24*60*60*1000));
			auctionItemPo.setMatchClass(itemPo.getMatchClass());
			baseDAO.insert(auctionItemPo);
			LogUtil.writeLog(this, 208,itemPo.getId(), totalPrice, 0, GlobalCache.languageMap.get("key2366"), "");
		}else{
			checkHasRolePackIndex(roleMainPackIndex);
			RolePackItemVo rolePackItemVo = fetchRolePackItemByIndex(roleMainPackIndex);
			
			if(count.intValue() > rolePackItemVo.num.intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key128")+rolePackItemVo.num);
			}
			
			if(rolePackItemVo.getBindStatus()==1){
				ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key129"));
			}
			int myActioncount = baseDAO.jdbcTemplate.queryForInt("select count(id) from "+BaseStormSystemType.USER_DB_NAME+".u_po_auction_item where seller_role_id="+getId());
			
			if(myActioncount>=20){
				ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key130"));
			}
			checkService.checkExistItemPo(rolePackItemVo.getItemId());
			ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
			EqpPo ep = EqpPo.findEntity(rolePackItemVo.getEquipId());
			int powerLv = 0;
			if(rolePackItemVo.getEquipId().intValue()!=0 &&  ep != null){
				powerLv = ep.getPowerLv();
			}
			AuctionItemPo auctionItemPo = new AuctionItemPo();
			auctionItemPo.setEquipId(rolePackItemVo.getEquipId());
			auctionItemPo.setItemId(rolePackItemVo.getItemId());
			auctionItemPo.setNum(count);
			auctionItemPo.setSellerRoleId(getId());
			auctionItemPo.setSellerRoleName(getName());
			auctionItemPo.setSellTime(System.currentTimeMillis());
			auctionItemPo.setTotalPrice(totalPrice);
			auctionItemPo.setSellItemName(itemPo.getName());
			auctionItemPo.setSellItemCategory(itemPo.getCategory());
			auctionItemPo.setSellItemType(itemPo.getType());
			auctionItemPo.setRequireLv(itemPo.getRequireLv());
			auctionItemPo.setSellEquipPowerLv(powerLv);
			auctionItemPo.setSellExpirationTime(System.currentTimeMillis() + (2*24*60*60*1000));
			auctionItemPo.setMatchClass(itemPo.getMatchClass());
			baseDAO.insert(auctionItemPo);
			removeItemFromMainPack(roleMainPackIndex,count,GlobalCache.fetchLanguageMap("key2659"),false);
			LogUtil.writeLog(this, 208, rolePackItemVo.getItemId(), totalPrice, powerLv, GlobalCache.fetchLanguageMap("key2366"), ""+rolePackItemVo.getNum());
		}
		LogUtil.writeLog(this, 345, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	/** 拍卖行-购买卖品 */
	public void auctionBuyAuction(Integer auctionId){
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		CheckService checkService = CheckService.instance();
		checkService.checkExisAuctionItemPo(auctionId);
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		if(getGold().intValue() < auctionItemPo.getTotalPrice().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key37"));
		}
		
		RolePo sellerRole =RolePo.findEntity(auctionItemPo.getSellerRoleId());
		if(sellerRole!=null){
			int sellGold = (int) (auctionItemPo.getTotalPrice().doubleValue()*0.95);
			int fei=auctionItemPo.getTotalPrice()-sellGold;
			List<IdNumberVo3> list =new ArrayList<IdNumberVo3>();
			if(sellGold>0){
				list.add(new IdNumberVo3(1,ItemType.ITEM_GOLD,sellGold,0));
			}
			mailService.sendAwardSystemMail(sellerRole.getId(), auctionItemPo.getSellItemName()+GlobalCache.fetchLanguageMap("key132"),GlobalCache.fetchLanguageMap("key138")+auctionItemPo.getSellItemName()+GlobalCache.fetchLanguageMap("key133")+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+GlobalCache.fetchLanguageMap("key134")+getName()+GlobalCache.fetchLanguageMap("key135")+(sellGold+fei)+GlobalCache.fetchLanguageMap("key136")+fei+GlobalCache.fetchLanguageMap("key137")+sellGold,list);
			sellerRole.sendUpdateTreasure(true);
		}
		sendAuctionMail(auctionItemPo, 1);
		int powerLv=0;
		if(auctionItemPo.eqpPo!=null){
			powerLv=auctionItemPo.eqpPo.getPowerLv();
		}
		LogUtil.writeLog(this, 210, auctionItemPo.getItemId(), auctionItemPo.getTotalPrice(), powerLv, GlobalCache.fetchLanguageMap("key2365")+"*"+auctionItemPo.getNum(), "");
		adjustNumberByType(-auctionItemPo.getTotalPrice(), RoleType.RESOURCE_GOLD);
		LogUtil.writeLog(this, 344, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	public List<IdNumberVo> itemUse(Integer mainPackIndex,Integer num, boolean send){
		CheckService checkService = CheckService.instance();
		checkHasRolePackIndex(mainPackIndex);
		RolePackItemVo rolePackItemVo =fetchRolePackItemByIndex(mainPackIndex);
		checkService.checkExistItemPo(rolePackItemVo.getItemId());
		if(rolePackItemVo.num < 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key26"));
		}
		ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
		if(getLv() <  itemPo.getRequireLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key84"));
		}
		List<IdNumberVo> list= new ArrayList<IdNumberVo>();
		if(rolePackItemVo.itemPo().couldUse()){
			list=useItemEffects(rolePackItemVo.itemPo().listItemUseExp,num,rolePackItemVo.getBindStatus(), send);
		}
		return list;
	}
	/**  NPC 售卖商品*/
	public void npcSellItem(Integer monsterId,Integer itemId,Integer num){
		checkItemPackFull(1);
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		checkService.checkExisMonsterPo(monsterId);
		MonsterPo npcPo = MonsterPo.findEntity(monsterId);
		List<Integer> npcSellItem =npcPo.findNpcSellItem(itemId);
		if(npcSellItem != null){
			checkHasAndConsumeResource(npcSellItem.get(1),npcSellItem.get(2)*num);
			addItem(npcSellItem.get(0), num,1);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, npcSellItem.get(0), num, GlobalCache.fetchLanguageMap("key2404"), "");			
		}
	}
	/** 商店售卖*/
	public void  productSellItem(Integer productId, Integer num){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		ItemService itemService = (ItemService) BeanUtil.getBean("itemService");
		checkItemPackFull(1);
		checkService.checkExistProductPo(productId);
		ProductPo productPo = ProductPo.findEntity(productId);
		checkService.checkExistItemPo(productPo.getItemId());
		ItemPo itemPo=ItemPo.findEntity(productPo.getItemId());
		if(itemPo.getId()==300010032 && listRechargeInfo.get(0).wasMonthCard == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2298"));
		}
		
		if(getVipLv().intValue() < productPo.getBuyViplv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key65"));
		}

		long currentTime = System.currentTimeMillis();
		if(getLv()<productPo.getMinLv()){
			// 需要等级
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2286")+productPo.getMinLv());
		}
//		System.out.println("当前时间："+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		if(productPo.getItemSell() != null){
			long openTime = Long.parseLong(productPo.getItemSell() + "000");
//			System.out.println("开始时间："+ DateUtil.getFormatDateBytimestamp(openTime));
			if(currentTime < openTime){	
				//商品未上架
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key147"));
			}			
		}
		if(productPo.getTimeOff() != null ){
			long closeTime = Long.parseLong(productPo.getTimeOff() + "000");
			if(currentTime > closeTime){
//				System.out.println("结束时间："+ DateUtil.getFormatDateBytimestamp(currentTime));
				//商品已下架
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key148"));
			}			
		}

		
		// 每日允许购买数量
		Integer dailyPermissionPurchase = 0;
		if(productPo.listDayCount != null){
			if(getVipLv().intValue() <= productPo.listDayCount.size()){
				dailyPermissionPurchase = productPo.listDayCount.get(getVipLv());				
			}
		}
		// 每天已经购买的次数
		Integer dailyCount = itemService.fetchListElementCount(productId, listEveryDayBuyProductCount);
		// 验证每日允许购买的数量
		if(dailyPermissionPurchase.intValue() != -1){
			if((dailyCount.intValue()+num.intValue()) > dailyPermissionPurchase.intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key149")+dailyPermissionPurchase+GlobalCache.fetchLanguageMap("key150"));
			}			
		}
		
		// 只能购买数量
		Integer onlyCount = itemService.fetchListElementCount(productId, listOnlyBuyProductCount);
		if(productPo.getBuyCount().intValue() != -1){
			if((onlyCount.intValue() + num.intValue()) > productPo.getBuyCount()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key151")+productPo.getBuyCount()+GlobalCache.fetchLanguageMap("key150"));
			}
		}
		
		if(productPo.getTotalCount() == null){
			// 商品限购总量错误：
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2289")+productPo.getTotalCount());
		}
		if(productPo.getTotalCount().intValue() != -1){
			if((productPo.getTotalCountBuyed().intValue()+num.intValue()) >= productPo.getTotalCount().intValue()){
				// 限购商品已卖完：
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2290")+productPo.getTotalCountBuyed());
			}			
		}

		
		UserPo userPo = UserPo.findEntity(getUserId());
		if(productPo.getMoneyType()==1&&getGold()>=productPo.getDiscountsPrice()*num){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_GOLD, 0, -productPo.getDiscountsPrice()*num, GlobalCache.fetchLanguageMap("key2415")+itemPo.getName(), num+"");
			LogUtil.writeLog(this, 346, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}else if(productPo.getMoneyType()==2&&userPo.getDiamond()>=productPo.getDiscountsPrice()*num){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -productPo.getDiscountsPrice()*num, GlobalCache.fetchLanguageMap("key2416")+itemPo.getName(),num+"");
			LogUtil.writeLog(this, 347, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}else if(productPo.getMoneyType()==6&&(userPo.getDiamond()+getBindDiamond())>=productPo.getDiscountsPrice()*num){
			checkHasResource(productPo.getMoneyType(),productPo.getDiscountsPrice()*num);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -productPo.getDiscountsPrice()*num, GlobalCache.fetchLanguageMap("key2417")+itemPo.getName(), num+"");
			LogUtil.writeLog(this, 348, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		checkHasAndConsumeResource(productPo.getMoneyType(),productPo.getDiscountsPrice()*num);
		int bind = 1;
		if((productPo.getMoneyType()==1||productPo.getMoneyType()==2)&&productPo.getShopTab()!=4){
			bind = 0;
		}
		addItem(productPo.getItemId(), num,bind);	
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, productPo.getItemId(), num, GlobalCache.fetchLanguageMap("key2418")+itemPo.getName(), num+"");

		if(dailyPermissionPurchase != -1){
			itemService.addListElement(productId, num, listEveryDayBuyProductCount);				
		}
		if(productPo.getBuyCount().intValue() != -1){
			itemService.addListElement(productId, num, listOnlyBuyProductCount);
		}
		boolean matched=false;
		for (IdNumberVo idNumberVo : listProductTodayBuyed) {
			if(idNumberVo.getId().intValue()==productId){
				idNumberVo.setNum(idNumberVo.getNum()+num);
				matched=true;
				break;
			}
		}
		if(!matched){
			listProductTodayBuyed.add(new IdNumberVo(productId,num));
			if(productPo.getTotalCountBuyed()!=null){
				productPo.setTotalCountBuyed(productPo.getTotalCountBuyed()+num);
			}
		}
		taskConditionProgressAdd(num, TaskType.TASK_TYPE_CONDITION_742, productId, null);
	}
	
	/** 合成道具*/
	public Integer mergeItem(Integer mergeId,Integer num){
		checkItemPackFull(1);
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		MergePo mergePo = MergePo.findEntity(mergeId);
		publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,mergePo.getConsumeGold()*num.intValue());
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -mergePo.getConsumeGold()*num.intValue(), GlobalCache.fetchLanguageMap("key2402"), "");
		checkHasAndConsumeBindGoldThenGold(mergePo.getConsumeGold()*num.intValue());
		List<IdNumberVo> list = checkHasAndConsumeItems(mergePo.listConsumItems, num,GlobalCache.fetchLanguageMap("key2402"));
		Integer successBind = 0;
		Integer successUnBind=0;
		for(IdNumberVo idNumberVo : list){
			boolean flag=RandomUtil.random1W(mergePo.getSuccessRate());
			if(flag){
				if(idNumberVo.getNum().intValue()==1){
					successBind++;
				}else if(idNumberVo.getNum().intValue()==0){
					successUnBind++;
				}
			}
		}

		if(successBind.intValue() > 0 || successUnBind>0){
			if(successBind > 0){
				addItem(mergePo.getTargetItemId(), successBind,1);				
			}
			if(successUnBind>0){
				addItem(mergePo.getTargetItemId(), successUnBind,0);	
			}
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, mergePo.getTargetItemId(), (successBind+successUnBind), GlobalCache.fetchLanguageMap("key2403"), "");
			ItemPo itemPo = ItemPo.findEntity(mergePo.getTargetItemId());
			taskConditionProgressAchieveAdd(1, TaskType.TASK_TYPE_CONDITION_727, itemPo.getItemLv(), null);
			if(itemPo.getQuality().intValue() >= 5){
				StringBuffer sb = new StringBuffer();
				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
				sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key152"));
				sb.append(ColourType.fetchColourByQuality(itemPo.getQuality())).append(itemPo.getName());
				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
				chatService.sendHorse(sb.toString());
			}
		}
		return (successBind+successUnBind);
	}
	/**
	 * 从背包取出=>放入仓库
	 * @return
	 */
	public void putInByWareHouse(String indexStr){
		List<Integer> indexes = StringUtil.getListByStr(indexStr);
		checkWarehousePackItemFull(indexes.size());
		for(Integer i : indexes){
			checkHasRolePackIndex(i);
		}
		for(Integer i : indexes){
			RolePackItemVo rolePackItemVo = fetchRolePackItemByIndex(i);
			removeItemFromMainPack(rolePackItemVo.index, rolePackItemVo.num,GlobalCache.fetchLanguageMap("key2660"),false);
			singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_440, warehousePackItemVosMap);
		}
	}
	/**
	 * 从仓库取出=>放入背包
	 * @return
	 */
	public void takeOutByWarehouse(String indexStr){
		List<Integer> indexes = StringUtil.getListByStr(indexStr);
		checkItemPackFull(indexes.size());
		
		for(Integer i : indexes){
			checkHasRoleWarehousePackIndex(i);	
		}
		
		for(Integer i : indexes){
			RolePackItemVo rolePackItemVo = fetchRoleWarehousePackItemByIndex(i);
			removeWarehouseItemFromWarehouse(rolePackItemVo.index, rolePackItemVo.num);
			singleRolePackItemVoToPack(rolePackItemVo, PlayTimesType.PLAYTIMES_TYPE_440, mainPackItemVosMap);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, rolePackItemVo.getItemId(), rolePackItemVo.getNum(), GlobalCache.fetchLanguageMap("key2695"), "");
		}
	}
	/**
	 * 放入金币或绑金到仓库
	 * @param goldType 1:金币； 5绑金
	 * @param num
	 * @return
	 */
	public void putInGoldOrBindGoldByWareHouse(Integer goldType, Integer num){
		if(goldType.intValue() == RoleType.RESOURCE_GOLD){
			checkHasAndConsumeGold(num);
			adjustNumberByType(num, RoleType.RESOURCE_WAREHOUSE_GOLD);
		}else if (goldType.intValue() == RoleType.RESOURCE_BIND_GOLD){
			checkHasAndConsumeResource(RoleType.RESOURCE_BIND_GOLD, num);
			adjustNumberByType(num, RoleType.RESOURCE_WAREHOUSE_BIND_GOLD);
		}
	}
	/**
	 * 从仓库获取金币或者绑金
	 * @param goldType 10:仓库金币； 11：仓库绑金
	 * @param num
	 * @return
	 */
	public void takeOutGoldOrBindGoldByWarehouse(Integer goldType, Integer num){
		if(goldType.intValue() == RoleType.RESOURCE_WAREHOUSE_GOLD){
			checkHasAndConsumeResource(RoleType.RESOURCE_WAREHOUSE_GOLD, num);
			adjustNumberByType(num, RoleType.RESOURCE_GOLD);
		}else if (goldType.intValue() == RoleType.RESOURCE_WAREHOUSE_BIND_GOLD){
			checkHasAndConsumeResource(RoleType.RESOURCE_WAREHOUSE_BIND_GOLD, num);
			adjustNumberByType(num, RoleType.RESOURCE_BIND_GOLD);
		}
	}
	
	/**
	 * 宠物抽取
	 * @param way 1=金币抽取 2=钻石抽取 3=金币十连抽 4=钻石十连抽 
	 * @return
	 */
	public void petRoll(Integer rollType){
		checkPetPackFull();
		
		if(rollType==PetType.PET_ROLL_DIAMOND){
			ConsumPo consumDiamond = ConsumPo.findEntity(1);
			int consumNum = fetchConsum(consumDiamond, 0);
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, consumNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2406"), "");
			checkHasAndConsumeBindDiamondThenDiamond(consumNum);
			setPetRollDiamondTodayTimes(getPetRollDiamondTodayTimes()+1);
			setPetRollDiamondTotalTimes(getPetRollDiamondTotalTimes()+1);
			rollPet(rollType);
			adjustConsumCostById(1, 1);
			LogUtil.writeLog(this, 329, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if(rollType.intValue()==PetType.PET_ROLL_GOLD)
		{
			ConsumPo consumGold = ConsumPo.findEntity(3);
			int consumNum = fetchConsum(consumGold, 0);
			 if(getPetRollGoldTodayTimes().intValue() >= 10)
			{
				 publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,consumNum);
				 LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -consumNum, GlobalCache.fetchLanguageMap("key2407"), "");
				 checkHasAndConsumeBindGoldThenGold(consumNum);
				 LogUtil.writeLog(this, 327, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else{
				LogUtil.writeLog(this, 326, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}
			setPetRollGoldTodayTimes(getPetRollGoldTodayTimes()+1);
			setPetRollGoldTotalTimes(getPetRollGoldTotalTimes()+1);
			rollPet(rollType);
			adjustConsumCostById(3, 1);
		}
		else if(rollType.intValue() == PetType.PET_TEN_ROLL_GOLD)
		{
			ConsumPo consumTenGold = ConsumPo.findEntity(4);
			int consumNum = fetchConsum(consumTenGold, 0);
			publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,consumNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -consumNum, GlobalCache.fetchLanguageMap("key2408"), "");
			checkHasAndConsumeBindGoldThenGold(consumNum);
			setPetRollGoldTotalTimes(getPetRollGoldTotalTimes()+10);
			for(int i=0; i < 10; i++){
				rollPet(PetType.PET_ROLL_GOLD);
			}
			adjustConsumCostById(4, 1);
			LogUtil.writeLog(this, 328, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if(rollType.intValue() == PetType.PET_TEN_ROLL_DIAMOND)
		{
			ConsumPo consumTenDiamond = ConsumPo.findEntity(2);
			int consumNum = fetchConsum(consumTenDiamond, 0);
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, consumNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -consumNum, GlobalCache.fetchLanguageMap("key2409"), "");
			checkHasAndConsumeBindDiamondThenDiamond(consumNum);
			setPetRollDiamondTodayTimes(getPetRollDiamondTodayTimes()+10);
			setPetRollDiamondTotalTimes(getPetRollDiamondTotalTimes()+10);
			for(int i=0; i < 10; i++){
				rollPet(PetType.PET_ROLL_DIAMOND);
			}
			adjustConsumCostById(2, 1);
			LogUtil.writeLog(this, 330, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if(rollType.intValue() == PetType.PET_TEN_ROLL_FREE){
			if(getPetRollGoldTodayTimes().intValue() < 10){
				int count = 10 - getPetRollGoldTodayTimes();
				setPetRollGoldTodayTimes(getPetRollGoldTodayTimes()+count);
				setPetRollGoldTotalTimes(getPetRollGoldTotalTimes()+count);
				for(int i=0; i < count; i++){
					rollPet(PetType.PET_ROLL_GOLD);
				}

			}
		}
	}
	
	public void disolve(String rpetIds){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		List<Integer> listRpetIds = StringUtil.getListByStr(rpetIds, ",");
		for(Integer rpetId : listRpetIds){
			checkService.checkExistRpetPo(rpetId);
			checkService.checkOwnRpetPo(rpetId, getId());
			RpetPo rpetPo = RpetPo.findEntity(rpetId);
			checkService.checkExisPetUpgradePo(rpetPo.getLv());
			checkService.checkExistPetPo(rpetPo.getPetId());
		}
		for (Integer rpetId : listRpetIds) {
			int basePetSoul = 0;
			int addPetSoul = 0;
			RpetPo rpetPo = RpetPo.findEntity(rpetId);
			PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
			basePetSoul = petPo.getBaseExp();
			for(int i = 1; i < rpetPo.getLv(); i++){
				PetUpgradePo petUpGradePo = PetUpgradePo.findEntity(i);
				addPetSoul += petUpGradePo.getCostPetSoul();
			}
			int sumPetSoul = basePetSoul+addPetSoul;
			listRpets.remove(rpetPo);
			BaseDAO.instance().remove(rpetPo);
			adjustPetSoul(sumPetSoul);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PETSOUL, 0, sumPetSoul, GlobalCache.fetchLanguageMap("key2393")+rpetPo.getName(), "");
			LogUtil.writeLog(this, 1, 12,rpetPo.getPetId() , -1, GlobalCache.fetchLanguageMap("key2393"), "");
			int num=0;
			if(petPo.getQuality().intValue() == 5){
				num=num+1;
			}
			if(rpetPo.getStep()>=1){
				num=num+1;
			}
			if(num>=1){
				addItem(300099007, num,1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, 300099007, num, GlobalCache.fetchLanguageMap("key2394")+rpetPo.getName(), "");
			}
		}
	}
	/**
	 * 宠物升级、升阶
	 * @param rpetId
	 * @return
	 */
	public Boolean ugrade(Integer rpetId){
		Boolean flag =false;
		CheckService checkService =(CheckService) BeanUtil.getBean("checkService");
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		checkService.checkExistRpetPo(rpetId);
		checkService.checkOwnRpetPo(rpetId, getId());
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		checkService.checkExisPetUpgradePo(rpetPo.getLv());
		PetUpgradePo petUpGradePo = PetUpgradePo.findEntity(rpetPo.getLv());
		PetUpgradePo nextPetUpGradePo = PetUpgradePo.findEntity(rpetPo.getLv()+1);
		if(nextPetUpGradePo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key203"));
		}
		if(nextPetUpGradePo.getLv().intValue() > getLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key84"));
		}
		// 升级
		if(petUpGradePo.getCostPetSoul()!=null && petUpGradePo.getCostPetSoul() != 0){
			int upGradeNeed = petUpGradePo.getCostPetSoul().intValue();
			if(getPetSoul()>=upGradeNeed){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PETSOUL, 0, -upGradeNeed, GlobalCache.fetchLanguageMap("key2461")+rpetPo.getName(), "");
			}
			checkHasAndConsumeResource(RoleType.RESOURCE_PETSOUL, upGradeNeed);
			rpetPo.adjustPetLv(1);
		}
		// 升阶
		if(petUpGradePo.getCostItems() != null && !"".equals(petUpGradePo.getCostItems())){
			for(IdNumberVo iv : petUpGradePo.listCostItems){
				checkHasAndConsumeItem(iv.getId().intValue(), iv.getNum().intValue(),GlobalCache.fetchLanguageMap("key2652"));
			}
			rpetPo.adjustPetLv(1);
			rpetPo.adjustPetStep(1);
			PetUpgradePo pup = PetUpgradePo.findEntity(rpetPo.getLv());
			PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
			StringBuffer sb = new StringBuffer();
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key20"));
			sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key165"));
			sb.append(ColourType.fetchColourByQuality(petPo.getQuality())).append(petPo.getName());
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key166"));
			sb.append(ColourType.COLOUR_GOLDEN).append(pup.getStep());
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key167"));
			chatService.sendHorse(sb.toString());
			flag=true;
		}
		taskConditionProgressReplace(rpetPo.getLv(),TaskType.TASK_TYPE_CONDITION_717,null,null);
		return flag;
	}
	
	/**
	 * 宠物一键升级
	 * @param rpetId
	 * @return
	 */
	public void ugradeOneKey(Integer rpetId){
		CheckService checkService =(CheckService) BeanUtil.getBean("checkService");
		checkService.checkExistRpetPo(rpetId);
		checkService.checkOwnRpetPo(rpetId, getId());
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		checkService.checkExisPetUpgradePo(rpetPo.getLv());
		PetUpgradePo petUpGradePo = PetUpgradePo.findEntity(rpetPo.getLv());
		if(petUpGradePo.getLv().intValue() >= getLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key84"));
		}
		// 升级
		if(petUpGradePo.getCostPetSoul()!=null && petUpGradePo.getCostPetSoul() != 0){
			int upGradeNeed = petUpGradePo.getCostPetSoul().intValue();
			checkHasPetSoul(upGradeNeed);
		}	
		
		int baseLv = rpetPo.getLv().intValue();
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<PetUpgradePo> list =gameDataTemplate.getDataList(PetUpgradePo.class);
		for(int i = baseLv; i < list.size(); i++ ){
			checkService.checkExisPetUpgradePo(i);
			PetUpgradePo petugp = PetUpgradePo.findEntity(i);
			int upGradeNeed = petugp.getCostPetSoul().intValue();
			if(petugp.getCostPetSoul()!=null && petugp.getCostPetSoul() != 0){
				if(petugp.getLv().intValue() >= getLv().intValue()){
					break;
				}
				if(upGradeNeed > getPetSoul().intValue()){
					break;
				}
				if(getPetSoul()>=upGradeNeed){
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PETSOUL, 0, -upGradeNeed, GlobalCache.fetchLanguageMap("key2462"), "");
				}
				checkHasAndConsumeResource(RoleType.RESOURCE_PETSOUL, upGradeNeed);
				rpetPo.adjustPetLv(1);
			}else{
				break;
			}
		}
		taskConditionProgressReplace(rpetPo.getLv(),TaskType.TASK_TYPE_CONDITION_717,null,null);
	}
	
	public List<Integer> petConstellStrengthen(Integer type, Integer constellId){
		List<Integer> resultList = new ArrayList<Integer>();
		resultList.add(0);
		resultList.add(0);
		PetService petService = (PetService) BeanUtil.getBean("petService");
		PetConstell petConstell = GlobalCache.petConstell.get(constellId);
		if(petConstell == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key168"));
		List<PetConstellVo> pcList = petConstellMap.get(constellId);
		if(pcList == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key169"));
		Integer strengthen = petConstellStrengthenMap.get(constellId);
		if(type == PetService.CONSTELL_STRENGTHEN_TYPE)
		{
			int itemId = XmlCache.xmlFiles.constantFile.pet.petConstells.itemId;//普通强化所需物品ID
			int itemCount = XmlCache.xmlFiles.constantFile.pet.petConstells.itemCount;//普通强化所需物品数量
			checkHasAndConsumeItem(itemId,itemCount,GlobalCache.fetchLanguageMap("key2405"));
		}
		else
		{
			int diamond = XmlCache.xmlFiles.constantFile.pet.petConstells.diamond;//高级强化所需钻石
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, diamond);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -diamond, GlobalCache.fetchLanguageMap("key2405"), "");
			checkHasAndConsumeBindDiamondThenDiamond(diamond);
			
		}
		
		int starNum = 0;//强化之前当前星座拥有星数量
		int starLastNum = 0;//强化之后当前星座拥有星数量
		//进行强化
		int index = 3;//第四属性索引
		boolean bl = true;//三属性类型是否相同
		int attachType = 0;
		int attachLevel = Integer.MAX_VALUE;
		int attType = -1;//-1为没强化到最大值，其它为强化最大值后属性类型
		if(strengthen>=PetService.Constell_STRENGTHEN_MAX)
		{
			ConstellAttachVo caVo = GlobalCache.petConstellAttachVo.get(constellId);
			attType = caVo.getRandomAttach();
		}
		for(int i=0;i<3;i++)
		{
			PetConstellVo pc = pcList.get(i);
			if(pc.attachLevel == PetService.CONSTELL_STRENGTHEN_MAX_LEVEL)
				starNum++;
			petService.constellStrengthen(pc, petConstell, type, attType);
			if(pc.attachLevel == PetService.CONSTELL_STRENGTHEN_MAX_LEVEL)
				starLastNum++;
			if(attachType == 0)
				attachType = pc.attachType;
			else if(attachType != pc.attachType)
				bl = false;
			if(pc.attachLevel < attachLevel)
				attachLevel = pc.attachLevel;
		}
		if(bl)
		{
			if(pcList.size() <= index)
				pcList.add(new PetConstellVo());
			PetConstellVo pc = pcList.get(index);
			pc.attachType = attachType;
			pc.attachLevel = attachLevel;
			if(pc.attachLevel == PetService.CONSTELL_STRENGTHEN_MAX_LEVEL)
				starLastNum++;
			PetAttach petAttach = GlobalCache.petAttach.get(pc.attachLevel).get(pc.attachType);
			pc.icon = petAttach.icon;
			pc.name = petAttach.name;
			pc.updateAttachs(GlobalCache.petAttach.get(pc.attachLevel).get(pc.attachType));
		}
		else
		{
			if(pcList.size() > index)
				pcList.remove(index);
		}
		strengthen = strengthen>=PetService.Constell_STRENGTHEN_MAX?0:(type == PetService.CONSTELL_STRENGTHEN_TYPE?++strengthen:strengthen+2);
		updatePetConstell(constellId, strengthen, pcList);
		if(starLastNum > starNum)
		{
			int sumStar = 0;//总星数
			Collection<List<PetConstellVo>> pcVos = petConstellMap.values();
			for(List<PetConstellVo> pcVoList:pcVos)
			{
				for(PetConstellVo pcVo:pcVoList)
				{
					if(pcVo.attachLevel == PetService.CONSTELL_STRENGTHEN_MAX_LEVEL)
						sumStar++;
				}
			}
			resultList.set(0,1);
			resultList.set(1,sumStar);
		}
		return resultList;
	}
	/**
	 * 天赋升级
	 */
	public void petTalentUpLevel(Integer alentId){
		PetTalentPo petTalentPo = GlobalCache.petTalentIdMap.get(alentId);
		if(petTalentPo == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key170"));
		PetTalentPo newPetTalentPo = GlobalCache.petTalentMap.get(petTalentPo.talentType).get(petTalentPo.talentLevel+1);
		if(newPetTalentPo == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key171"));
		int index = -1;
		for(int i=0,max=listPetTalent.size();i<max;i++)
		{
			if(listPetTalent.get(i).intValue() == alentId.intValue())
			{
				index = i;
				break;
			}
		}
		if(index == -1)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key172"));
		String[] expend = petTalentPo.upgradeExpend.split("\\|");
		checkHasAndConsumeItem(Integer.parseInt(expend[0]),Integer.parseInt(expend[1]),GlobalCache.fetchLanguageMap("key2653"));
		//天赋进行升级
		listPetTalent.remove(index);
		listPetTalent.add(index, newPetTalentPo.getId());
		updatePetTalent();
		updateBatToFighterPet();
	}
	
	/**
	 * 技能学习
	 */
	public void petSkillLearn(Integer rpetId, Integer skillId){
//		System.out.println("rolePo() rpetId = " +rpetId +"; skillId = "+ skillId);
		PetService petService=(PetService) BeanUtil.getBean("petService");
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		if(rpetPo == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key173"));
		PetSkillPo petSkillPo = PetSkillPo.findEntity(skillId);
		if(petSkillPo == null)
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key174"));
		List<Integer> skillIds = rpetPo.skillIds;
		if(null != skillIds)
		{
			for(Integer skill:skillIds)
			{
				if(skill.intValue() == skillId.intValue())
				{
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key175"));
				}
			}
		}
		if(!StringUtil.isEmpty(petSkillPo.getLearnExpend()))
		{
			String[] expend = petSkillPo.getLearnExpend().split("\\|");
			checkHasAndConsumeItem(Integer.parseInt(expend[0]),Integer.parseInt(expend[1]),GlobalCache.fetchLanguageMap("key2654"));
		}
		//进行技能升级
		petService.petSkillLearn(this, rpetPo, petSkillPo);
		updateBatToFighterPet();
		calculateBat(1);
	}
	
	public void upgradeWing(Integer consumeType, Integer wasAuto){
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		//本次执行升阶时
//		PrintUtil.print("consumeType=== "+consumeType+" wasAuto=== "+wasAuto);
		if(getWingStar()%11==10){
			int count = XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).stepUpgradeCostItemCount;
			boolean bool = false;
			if(consumeType.intValue() == 0){
				if(wasAuto.intValue() == 0){
					checkHasAndConsumeItem(ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));
					bool = wasWingUpdateStarSuccess();
				}else{
					int num=1;
					int total = fetchItemCount(ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID);
					while(true){
						checkHasAndConsumeItem(ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));
						bool = wasWingUpdateStarSuccess();
//						System.out.println(num+" "+bool + "; "+(count*num+count)+"; total="+total);
						if((count*num+count) > total || bool){
							break;
						}
						num++;
					}		
				}			
			}
			else{
				checkHasAndConsumeItemORDiamond(ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));
				bool = wasWingUpdateStarSuccess();
			}
			int add = XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).stepFailExp;

//			PrintUtil.print("bool== "+bool);
			if(bool){
				setWingStarExp(0);
				setWingStar(getWingStar()+1);
				CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(getEquipWeaponId(), getEquipArmorId(), fashion, getCareer(), getWingWasHidden(),getWingStar(),hiddenFashions);
				fighter.makeAvatars(commonAvatarVo,true);		
				taskConditionProgressReplace((getWingStar()/11 + 1),TaskType.TASK_TYPE_CONDITION_715,null,null);
//				StringBuffer sb = new StringBuffer();
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//				sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key176"));
//				sb.append(ColourType.COLOUR_GOLDEN).append((getWingStar()/11)+1);
//				sb.append(ColourType.COLOUR_GREEN).append(GlobalCache.fetchLanguageMap("key177"));
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
				String str = MessageFormat.format(GlobalCache.fetchLanguageMap("key2591"),getName(),(getWingStar()/11)+1);
				chatService.sendHorse(str);
				LogUtil.writeLog(this, 203, getWingStar()/11, fetchItemCount(ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID), 0, GlobalCache.fetchLanguageMap("key2466"), "");
			}
			else{
				setWingStarExp(0);
			}
		}
		//本次执行升星时
		else{
			int count = XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).starUpgradeCostItemCount;
			if(consumeType.intValue() == 0){
				if(wasAuto.intValue() == 0){
					checkHasAndConsumeItem(ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));
					adjustWingStarExp(100*count);
				}else{
					int num = 1;
					int total = fetchItemCount(ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID);
					while(true){
						checkHasAndConsumeItem(ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));				
						adjustWingStarExp(100*count);
						if((count*num+count) > total || getWingStar()%11==10){
							break;
						}
						num++;
					}
				}
			}
			else{
				checkHasAndConsumeItemORDiamond(ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID,count,GlobalCache.fetchLanguageMap("key2392"));				
				adjustWingStarExp(100*count);
			}
		}
		taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_728, null, null);
		calculateBat(1);
	}
	
	/**
	 * 翅膀升阶是否成功
	 * @return
	 */
	public boolean wasWingUpdateStarSuccess(){
		int add = XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).stepFailExp;
		double min = (double)XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).stepMinExp;
		double max = (double)XmlCache.xmlFiles.constantFile.wing.stars.star.get(getWingStar()).stepMaxExp;
		
		double stepPoss=(double)getWingStepPoss();
		double randomPar=0d;
		if(stepPoss<=min){
			randomPar=0d;
		}else if(min<stepPoss&&stepPoss<=max/4+min*3/4){
			randomPar=(stepPoss-min)/(max-min)/8;									
		}else if(max/4+min*3/4<stepPoss&&stepPoss<=max/2+min/2){
			randomPar=(stepPoss-max/4-min*3/4)/(max-min)/2+1d/32;		
		}else if(stepPoss>max/2+min/2){
			randomPar=(stepPoss-max/2-min/2)/(max-min)*7/4+1d/8;
		}
		boolean bool = RandomUtil.random1W((int)(randomPar*10000));
		setWingStepPoss(getWingStepPoss()+add);
		return bool;
	}
	
	/**
	 * 升级称号等级
	 * @return 
	 */
	public void upgradeTitle(){
		int titleLv = getTitleLv()+1;
//		System.out.println("titleLv="+titleLv);
		TitlePo titlePo = GlobalCache.titlePoMap.get(titleLv);
		if(null != titlePo)
		{
			if((getAchievePoint().intValue()-titlePo.getExp().intValue())<0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2293"));
			}
			setTitleLv(titlePo.getLv());
			adjustAchievePoint(-titlePo.getExp());
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ACHIVEPOINT, 0, -titlePo.getExp(),GlobalCache.fetchLanguageMap("key2625") , "");
			updateTitle(true);
			calculateBat(1);
		}
	}
	/**
	 * 奖励找回
	 * @param retrieveId 奖励找回ID
	 * @param type 奖励找回类型（1金币找回2钻石找回）
	 * @return
	 */
	public void awardRetrieve(Integer retrieveId, Integer type){
		AwardRetrieveVo awardRetrieveVo = fetchAwardRetrieveVoById(null,retrieveId);
		int count =  awardRetrieveVo.baseCount -  (awardRetrieveVo.yesterdayFinishCount+awardRetrieveVo.retrieveCount);
		if(count <= 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2265"));
		}
		if(awardRetrieveVo.wasOpen==0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2316"));
		}
		
		AwardRetrieve ar = GlobalCache.awardRetrieveMap.get(retrieveId);
		
		if(type.intValue() ==1){
			publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,ar.gold*count);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -ar.gold*count, GlobalCache.fetchLanguageMap("key2377"), "");
			checkHasAndConsumeBindGoldThenGold(ar.gold*count);
			List<IdNumberVo2> list = IdNumberVo2.createList(ar.goldAward);
			for(IdNumberVo2 inv2 : list){
				int sum = (inv2.getInt2()*getLv()+inv2.getInt3())*count;
				addItem(inv2.getInt1(), sum, 1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv2.getInt1(), sum, GlobalCache.fetchLanguageMap("key2378"), "");
			}	
		}else if(type.intValue() == 2){
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, ar.diamond*count);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -ar.diamond*count, GlobalCache.fetchLanguageMap("key2379"), "");
			checkHasAndConsumeBindDiamondThenDiamond(ar.diamond*count);
			List<IdNumberVo2> list = IdNumberVo2.createList(ar.diamondAward);
			for(IdNumberVo2 inv2 : list){
				int sum = (inv2.getInt2()*getLv()+inv2.getInt3())*count;
				addItem(inv2.getInt1(), sum, 1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv2.getInt1(), count, GlobalCache.fetchLanguageMap("key2380"), "");
			}
		}
		awardRetrieveVo.adjustRetrieveCount(count);
	}
	/**
	 * 奖励找回（全部）
	 * @param retrieveId 奖励找回ID
	 * @param type 奖励找回类型（1金币找回2钻石找回）
	 * @return
	 */
	public  void allRetrieve(Integer type){
		for(AwardRetrieveVo awardRetrieveVo : listAwardRetrieve){
			if(awardRetrieveVo.wasOpen == 0){
				continue;
			}
			int count =  awardRetrieveVo.baseCount -  (awardRetrieveVo.yesterdayFinishCount+awardRetrieveVo.retrieveCount);
			if(count > 0){
				AwardRetrieve ar = GlobalCache.awardRetrieveMap.get(awardRetrieveVo.id);
				if(type.intValue() ==1){
					publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,ar.gold*count);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -ar.gold*count, GlobalCache.fetchLanguageMap("key2361"), "");
					checkHasAndConsumeBindGoldThenGold(ar.gold*count);
					List<IdNumberVo2> list = IdNumberVo2.createList(ar.goldAward);
					for(IdNumberVo2 inv2 : list){
						int sum = (inv2.getInt2()*getLv()+inv2.getInt3())*count;
						addItem(inv2.getInt1(), sum, 1);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv2.getInt1(), sum, GlobalCache.fetchLanguageMap("key2362"), "");
					}	
				}else if(type.intValue() == 2){
					checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, ar.diamond*count);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -ar.diamond*count, GlobalCache.fetchLanguageMap("key2363"), "");
					checkHasAndConsumeBindDiamondThenDiamond(ar.diamond*count);
					List<IdNumberVo2> list = IdNumberVo2.createList(ar.diamondAward);
					for(IdNumberVo2 inv2 : list){
						int sum = (inv2.getInt2()*getLv()+inv2.getInt3())*count;
						addItem(inv2.getInt1(), sum, 1);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, inv2.getInt1(), sum, GlobalCache.fetchLanguageMap("key2364"), "");
					}
				}
				awardRetrieveVo.adjustRetrieveCount(count);
			}
		}
	}
	
	/**
	 * 时装够买
	 * @param id 时装ID
	 * @return
	 */
	public void buyFashion(Integer id, Integer time){
//		System.out.println("id = " +id +"; time = " +time);
		CheckService checkService = CheckService.instance();
		checkService.checkExisFashionPo(id);
		FashionPo fashionPo = FashionPo.findEntity(id);
		if(fashionPo.getIsBuy()==0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key316"));
		}
		IdNumberVo2 idNumberVo2 = IdNumberVo2.findIdNumber(time, fashionPo.listPrice);
		checkHasResource(idNumberVo2.getInt2(), idNumberVo2.getInt3());
		if(idNumberVo2.getInt2()==8){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -idNumberVo2.getInt3(), GlobalCache.fetchLanguageMap("key2382")+fashionPo.getName()+GlobalCache.fetchLanguageMap("key2383")+idNumberVo2.getInt1(), "");
		}else if(idNumberVo2.getInt2()==2){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -idNumberVo2.getInt3(), GlobalCache.fetchLanguageMap("key2384")+fashionPo.getName()+GlobalCache.fetchLanguageMap("key2385")+idNumberVo2.getInt1(), "");
		}
		checkHasAndConsumeResource(idNumberVo2.getInt2(), idNumberVo2.getInt3());
		addFashion(id, time);
	}
	
	/**
	 * 升级军衔等级
	 * @param militaryRankId 需要升级的军衔Id
	 * @return
	 */
	public void upgradeMilitaryRank(){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		int militaryRankId = getCurrentMilitaryRankId() + 1;
		if( militaryRankId> listMilitaryRankRecord.size()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2260")+militaryRankId);
		}
		checkService.checkExisMilitaryRankPo(militaryRankId);
		MilitaryRankPo militaryRankPo = MilitaryRankPo.findEntity(militaryRankId);
		checkHasAndConsumeResource(RoleType.RESOURCE_PRESTIGE, militaryRankPo.getPrestige());
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_PRISTIGE, 0, -militaryRankPo.getPrestige(),GlobalCache.fetchLanguageMap("key2626") , "");
		setCurrentMilitaryRankId(militaryRankId);
		if(fighter!=null){
			fighter.changeCurrentMilitaryRankId(militaryRankId);
		}
		for(IdNumberVo inv : listMilitaryRankRecord){
			if(inv.getId().intValue() == militaryRankId){
				inv.setNum(1);
				break;
			}
		}
		if(fighter != null){
			if(fighter.cell != null){
				BattleMsgUtil.abroadMilitaryRankId(fighter);				
			}
		}
		calculateBat(1);
	}
	
	public  void buyPlayTimesByType(Integer timesType){
		IdNumberVo2 idNumberVo2 = fetchBuyPlayItemsByType(timesType);
		PlayItem playTime = fetchPlayItemByType(timesType);
		if(idNumberVo2 == null || playTime==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2262")+timesType);
		}
		if(idNumberVo2.getInt3().intValue() >= idNumberVo2.getInt2().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2263"));
		}
//		checkHasAndConsumeBindDiamondThenDiamond(playTime.buyPrice);
		
//		ConsumPo consumPo = ConsumPo.findEntity(playTime.buyConsumId);
//		checkHasAndConsumeResource(consumPo.getConsumType(),consumPo.getConsumNum());

		
		
		if(timesType.intValue()==PlayTimesType.PLAYTIMES_TYPE_540){
			checkHasItemElseDiamond(playTime.buyConsumId, timesType.intValue());
			adjustResourceSceneTime(30*60*1000);
			sendResourceScene();
		}
		else if(timesType.intValue()==PlayTimesType.PLAYTIMES_TYPE_420){
			for (Consum consum : XmlCache.xmlFiles.constantFile.openPackage.consum) {
				int minLv = consum.min;
				int maxLv = consum.max;
				int count = consum.num;
				int nextLv = getPackUnlockTimes()+1;
				if(nextLv>=minLv && nextLv<=maxLv){
					ConsumPo consumePo=ConsumPo.findEntity(playTime.buyConsumId);
					if(consumePo.getConsumType()==8){
						checkHasResource(consumePo.getConsumType(),count);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -count, GlobalCache.fetchLanguageMap("key2388")+consumePo.descripe, "");
					}else if(consumePo.getConsumType()==2){
						checkHasResource(consumePo.getConsumType(),count);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -count, GlobalCache.fetchLanguageMap("key2388")+consumePo.descripe, "");
					}
					checkHasAndConsumeResource(consumePo.getConsumType(),count);
					adjustConsumCostById(playTime.buyConsumId, 1);
					break;
				}
			}
			setPackUnlockTimes(getPackUnlockTimes()+1);
			sendUpdateUnPackTimes();
		}
		else{
			checkHasItemElseDiamond(playTime.buyConsumId, 0);
		}
		idNumberVo2.setInt3(idNumberVo2.getInt3()+ 1);
	}

	private void sendUpdateUnPackTimes() {
		singleRole("PushRemoting.sendUpdateUnPackTimes",new Object[] {packUnlockTimes },true);
	}

	/**
	 * 领取宝物
	 * @param treasureId
	 * @return
	 */
	public void takeTreasureAwardRemoting(Integer treasureId){
		for (com.games.mmo.vo.xml.ConstantFile.Treasures.Treasure treasure : XmlCache.xmlFiles.constantFile.treasures.treasure) {
			if(treasure.id==treasureId){
				List<Integer> ints = DBFieldUtil.getIntegerListBySplitter(treasure.awardItem, "|");
				addItem(ints.get(getCareer()-1), 1,1);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, ints.get(getCareer()-1), 1, GlobalCache.fetchLanguageMap("key2600"), "");
				sendUpdateMainPack(true);
				takeTreasureAward(treasureId);
			}
		}
	}
	/**
	 * 沙巴克领奖
	 * @param slot 1~3
	 * @return 
	 */
	public void guildAwardSiege(){
		FlagPo flagPo = FlagPo.findFlagBySceneId(MapType.SIEGE_SCENE_MAP);
		if(flagPo.getGuildId()==null || !(flagPo.getGuildId().intValue()==getGuildId())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key118"));
		}
		List<IdNumberVo> awards= IdNumberVo.createList(XmlCache.xmlFiles.constantFile.guild.guildwar.territory.get(5).award);
		
		checkItemPackFull(awards.size());
		
		addItem(awards, 1,GlobalCache.fetchLanguageMap("key2604"));
	}
	/**
	 * 领地领奖
	 * @param slot
	 * @return
	 */
	public List<IdNumberVo> guildAwardOwner(){
		if(DateUtil.sameDay(System.currentTimeMillis(), getDomainLastAwardTime())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key70"));
		}
		boolean avaAward=false;
		List<IdNumberVo> list =new ArrayList<IdNumberVo>();
		for (Territory territory : XmlCache.xmlFiles.constantFile.guild.guildwar.territory) {
			if(territory.sceneid!=MapType.SIEGE_SCENE_MAP){
				FlagPo flagPo = FlagPo.findFlagBySceneId(territory.sceneid);
				if(flagPo.getGuildId()!=null && flagPo.getGuildId().intValue()==getGuildId()){
					list.addAll(IdNumberVo.createList(territory.award));
					avaAward=true;
				}
			}
		}
		if(!avaAward){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key118"));
		}
		
		checkItemPackFull(list.size());
		
		addItem(list, 1,GlobalCache.fetchLanguageMap("key2605"));
		return list;
	}
	/**
	 * 方法功能:拍卖行-取消拍卖
	 * 更新时间:2014-11-14, 作者:johnny
	 * @param keyword
	 * @param category
	 * @return
	 */
	public void auctionCancelAuction(Integer auctionId){
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		if(auctionItemPo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key131"));
		}
		addAuctionItem(auctionItemPo);
		BaseDAO.instance().remove(AuctionItemPo.findEntity(auctionId));
	}
	
	/**
	 * 
	 * 方法功能:升级技能
	 * 更新时间:2014-12-5, 作者:johnny
	 * @param skillId
	 * @return
	 */
	public void upgradeSkill(Integer skillId){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		SkillPo skillPo = SkillPo.findEntity(skillId);
		int roleSkillLv = fetchSkillLv(skillId);
		if(roleSkillLv >= skillPo.getMaxLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key180"));
		}
		
		if(roleSkillLv >= getLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key181"));
		}
		checkService.checkExisLvConfigPo(roleSkillLv);
		LvConfigPo lvConfigPo = LvConfigPo.findEntity(roleSkillLv);
		UpgradeSkillPo upgradeSkillPo = fetchUpgradeSkillPoBySikllId(skillId);
		int costSP =DoubleUtil.toUpInt(1d*lvConfigPo.getSkillBaseSp().intValue()*upgradeSkillPo.getCostSpVar().intValue()/100);
		int costGold = DoubleUtil.toUpInt(1d*lvConfigPo.getSkillBaseGold().intValue()*upgradeSkillPo.getCostGoldVar().intValue()/100);
//		System.out.println("costSP == " +costSP);
//		System.out.println("costGold == " +costGold);
		if(getSkillPoint()>=costSP){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_SKILLPOINT, 0, -costSP, GlobalCache.fetchLanguageMap("key2463")+skillPo.getName(),"");
			LogUtil.writeLog(this, 204,roleSkillLv , getSkillPoint()-costSP, 0, GlobalCache.fetchLanguageMap("key2464")+skillPo.getName(),"");
			LogUtil.writeLog(this, 324, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		checkHasAndConsumeSkillPoint(costSP);
		publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,costGold);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -costGold, GlobalCache.fetchLanguageMap("key2465")+skillPo.getName(), "");
		checkHasAndConsumeBindGoldThenGold(costGold);
		taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_705,null,null);
		for (IdNumberVo idNumberVo : listSkillVos) {
			if(idNumberVo.getId()==skillId.intValue()){
				idNumberVo.addNum(1);
			}
		}
	}
	
	public List<Object> soulPower(Integer slot){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		SlotSoulVo slotSoulVo = findSlotSoul(slot);
		if(slotSoulVo.powerLv>=15){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key203"));
		}
		
		StarUpgrade starUpgrade = XmlCache.xmlFiles.constantFile.soulSlot.soulPower.starUpgrade.get(slotSoulVo.powerLv);
		//写死扣200金币
		int sum = getGold().intValue() + getBindGold().intValue();
		if(sum<1000){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key37"));
		}else{
			checkHasAndConsumeItem(starUpgrade.costItemId, starUpgrade.costItemCount,GlobalCache.fetchLanguageMap("key2441"));
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, starUpgrade.costItemId, -starUpgrade.costItemCount, GlobalCache.fetchLanguageMap("key2441"), "");
			publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,1000);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -1000, GlobalCache.fetchLanguageMap("key2441"), "");
			checkHasAndConsumeBindGoldThenGold(1000);
		}
		LogUtil.writeLog(this, 205,slotSoulVo.powerLv , fetchItemCount(starUpgrade.costItemId), 0, GlobalCache.fetchLanguageMap("key2442")+slot, "");
		boolean successFlag = RandomUtil.random1W(starUpgrade.successRate);
		boolean upRateFlag = false;
		int var = 0;
		if(successFlag)
		{
			slotSoulVo.powerLv++;
			slotSoulVo.powerExp = 0;
			var = 1;
			if(slotSoulVo.powerLv.intValue()>= 5){
//				StringBuffer sb = new StringBuffer();
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//				sb.append(ColourType.COLOUR_YELLOW).append("【").append(getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key124"));
//				sb.append(ColourType.COLOUR_GREEN).append(ItemType.fecthNameBySlot(slot));
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key204"));
//				sb.append(ColourType.COLOUR_GOLDEN).append(slotSoulVo.powerLv);
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key205")+GlobalCache.fetchLanguageMap("key50"));
				String str = MessageFormat. format(GlobalCache.fetchLanguageMap("key2615"), getName() , ItemType.fecthNameBySlot(slot),slotSoulVo.powerLv);
				chatService.sendHorse(str);				
			}
		}
		else
		{
			upRateFlag = RandomUtil.random1W(starUpgrade.upRate);
			if(upRateFlag)
			{
				List<List<Integer>> list  = ExpressUtil.buildBattleExpressList(starUpgrade.upVal);
				int start = list.get(0).get(0).intValue();
				int end = list.get(0).get(1).intValue();
				int addExp = IntUtil.getRandomInt(start, end);
				slotSoulVo.powerExp+=addExp;
				if(slotSoulVo.powerExp.intValue() >= 100){
					slotSoulVo.powerLv++;
					slotSoulVo.powerExp = 0;
					var = 1;
				}else{
					var = 0;					
				}
			}
			else
			{
				List<List<Integer>> list  = ExpressUtil.buildBattleExpressList(starUpgrade.downVal);
				int start = list.get(0).get(0).intValue();
				int end = list.get(0).get(1).intValue();
				int downExp =  IntUtil.getRandomInt(start, end);
				slotSoulVo.powerExp -= downExp;
				if(slotSoulVo.powerExp.intValue() <= 0){
					slotSoulVo.powerExp=0;
				}
				var = 0;
			}
		}
		taskConditionProgressAdd(1 ,TaskType.TASK_TYPE_CONDITION_720,null,null);
		checkTaskType712(listRoleTasks);
		checkTaskType712(listRoleAchievesTasks);
		calculateBat(1);
		
		List<Object> list = new ArrayList<Object>();
		list.add(var);
		list.add(upRateFlag);
		LogUtil.writeLog(this, 320, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		return list;
	}
	/**
	 * 激活槽位
	 * @param equipSlot
	 * @param slotIndex
	 * @return
	 */
	public void soulExtractActive(Integer equipSlot,Integer slotIndex){
		SlotSoulVo slotSoulVo = findSlotSoul(equipSlot);
		ConsumPo cp1 = ConsumPo.findEntity(5);
		ConsumPo cp2 = ConsumPo.findEntity(6);
		String initAtbs =  XmlCache.xmlFiles.constantFile.soulSlot.extract.initAtb;
		List<List<Integer>> initAtbList = ExpressUtil.buildBattleExpressList(initAtbs);
		List<Integer> initAtb = initAtbList.get(equipSlot.intValue() - 1);
		List<ExtractUpgrade> extractUpgradeList = XmlCache.xmlFiles.constantFile.soulSlot.extract.extractUpgrade;
		ExtractUpgrade extractUpgrade = null;
		for(ExtractUpgrade eu : extractUpgradeList){
			if(slotSoulVo.slotQuality.intValue() == eu.quality.intValue() && slotSoulVo.slotNum.intValue() == eu.slot.intValue()){
				extractUpgrade = eu;
				break;
			}
		}
		
		if(slotIndex.intValue() == 2)
		{
			checkHasItemElseDiamond(cp1.getId(), 0);
			slotSoulVo.extract2BatType=initAtb.get(0);
			if(getCareer().intValue() == RoleType.CAREER_MAGE){
				if(initAtb.get(0).intValue() == 1){
					slotSoulVo.extract2BatType=2;
				}
			}
			slotSoulVo.makeSlotExtract(1, 2, extractUpgrade, 0, this);
		}
		else if(slotIndex.intValue() == 3)
		{
			checkHasItemElseDiamond(cp2.getId(), 0);
			slotSoulVo.extract3BatType=initAtb.get(0);
			if(getCareer().intValue() == RoleType.CAREER_MAGE){
				if(initAtb.get(0).intValue() == 1){
					slotSoulVo.extract3BatType=2;
				}
			}
			slotSoulVo.makeSlotExtract(1, 3, extractUpgrade, 0, this);
		}	
		calculateBat(1);
		slotSoulVo.makeSoulSlotVal(slotIndex,SlotSoulType.EXTRACT_STATUS_UNLOCK);
		slotSoulVo.extractAssignment();
	}
	/**
	 * 洗练 
	 * @param equipSlot 部位
	 * @param normalORAdvance 1:普通  2:高级
	 * @return
	 */
	public void soulExtract(Integer equipSlot, Integer normalORAdvance){
		SlotSoulVo slotSoulVo = findSlotSoul(equipSlot);
		List<ExtractUpgrade> extractUpgradeList = XmlCache.xmlFiles.constantFile.soulSlot.extract.extractUpgrade;
		ExtractUpgrade extractUpgrade = null;
		for(ExtractUpgrade eu : extractUpgradeList){
			if(slotSoulVo.slotQuality.intValue() == eu.quality.intValue() && slotSoulVo.slotNum.intValue() == eu.slot.intValue()){
				extractUpgrade = eu;
				break;
			}
		}
		
		int num =  slotSoulVo.fetchNumByExtractStatus();
		if(normalORAdvance.intValue() == 1)
		{
//			System.out.println("消耗道具数量："+num);
			checkHasAndConsumeItem(extractUpgrade.costNormalItemId, num,GlobalCache.fetchLanguageMap("key2431"));
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, extractUpgrade.costNormalItemId, -num, GlobalCache.fetchLanguageMap("key2431"), "");
			slotSoulVo.extractNormalTimes++;
			LogUtil.writeLog(this, 321, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		else if(normalORAdvance.intValue() == 2)
		{
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,extractUpgrade.costAdvanceItemId.intValue()*num);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -extractUpgrade.costAdvanceItemId.intValue()*num, GlobalCache.fetchLanguageMap("key2432"), "");
			checkHasAndConsumeBindDiamondThenDiamond(extractUpgrade.costAdvanceItemId.intValue()*num);
			slotSoulVo.extractAdvanceTimes++;
			LogUtil.writeLog(this, 322, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
		}
		
		int must = 1;
		if(slotSoulVo.extract1Status==SlotSoulType.EXTRACT_STATUS_UNLOCK){
			slotSoulVo.makeSlotExtract(normalORAdvance, 1, extractUpgrade,must,this);
			must = 0;
		}
		if(slotSoulVo.extract2Status==SlotSoulType.EXTRACT_STATUS_UNLOCK){
			slotSoulVo.makeSlotExtract(normalORAdvance, 2, extractUpgrade,must,this);
			must = 0;
		}
		if(slotSoulVo.extract3Status==SlotSoulType.EXTRACT_STATUS_UNLOCK){
			slotSoulVo.makeSlotExtract(normalORAdvance, 3, extractUpgrade,must,this);
			must =0;
		}
		
		if(slotSoulVo.extractNormalTimes.intValue() >= extractUpgrade.extractNormalNums){
			slotSoulVo.extractNormalTimes=0;
		}
		if(slotSoulVo.extractAdvanceTimes.intValue() >= extractUpgrade.extractAdvanceNums){
			slotSoulVo.extractAdvanceTimes=0;
		}
		slotSoulVo.upGreadSlotQuality(this);
		slotSoulVo.extractAssignment();
		
		if(normalORAdvance.intValue() == 1)
		{
			String washString1=slotSoulVo.extract1BatType+"|"+slotSoulVo.extract1BatVal+"|"+slotSoulVo.extract1Star;
			String washString2=slotSoulVo.extract2BatType+"|"+slotSoulVo.extract2BatVal+"|"+slotSoulVo.extract2Star;
			String washString3=slotSoulVo.extract3BatType+"|"+slotSoulVo.extract3BatVal+"|"+slotSoulVo.extract3Star;
			String washString=washString1+","+washString2+","+washString3;
			LogUtil.writeLog(this, 224, slotSoulVo.slotQuality, slotSoulVo.slotNum, 0, GlobalCache.fetchLanguageMap("key2431"), washString);
		}
		else if(normalORAdvance.intValue() == 2)
		{
			String washString1=slotSoulVo.extract1BatType+"|"+slotSoulVo.extract1BatVal+"|"+slotSoulVo.extract1Star;
			String washString2=slotSoulVo.extract2BatType+"|"+slotSoulVo.extract2BatVal+"|"+slotSoulVo.extract2Star;
			String washString3=slotSoulVo.extract3BatType+"|"+slotSoulVo.extract3BatVal+"|"+slotSoulVo.extract3Star;
			String washString=washString1+","+washString2+","+washString3;
			LogUtil.writeLog(this, 225, slotSoulVo.slotQuality, slotSoulVo.slotNum, 0, GlobalCache.fetchLanguageMap("key2433"), washString);
		}
		
		
		taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_721,null,null);
		checkTaskType713(listRoleTasks);
		checkTaskType713(listRoleAchievesTasks);
//		System.out.println(slotSoulVo.extract1BatType+"|"+slotSoulVo.extract1BatVal+"|"+slotSoulVo.extract1Star+"|"+slotSoulVo.extract1Status);
//		System.out.println(slotSoulVo.extract2BatType+"|"+slotSoulVo.extract2BatVal+"|"+slotSoulVo.extract2Star+"|"+slotSoulVo.extract2Status);
//		System.out.println(slotSoulVo.extract3BatType+"|"+slotSoulVo.extract3BatVal+"|"+slotSoulVo.extract3Star+"|"+slotSoulVo.extract3Status);
//		System.out.println("部位："+slotSoulVo.slotNum+"; 品质："+slotSoulVo.slotQuality);
		calculateBat(1);
	}
	/**
	 * 镶嵌宝石
	 * @param equipSlot
	 * @param gemIndex
	 * @param itemId
	 * @return
	 */
	public void soulGemInsert(Integer equipSlot,Integer gemIndex,Integer itemId){
//		System.out.println(" RolePo.soulGemInsert() equipSlot="+equipSlot+"; gemIndex="+gemIndex+"; itemId="+itemId);
		ItemPo itemPo = ItemPo.findEntity(itemId);
		checkHasItem(itemId, 1);
		SlotSoulVo slotSoulVo = findSlotSoul(equipSlot);
		slotSoulVo.checkGemCategory(itemId);
		slotSoulVo.checkGemSlot(gemIndex);
		int wasbind = removePackItem(itemId, 1,GlobalCache.fetchLanguageMap("key2663"));
//		System.out.println("soulGemInsert() wasbind = " +wasbind);
//		System.out.println(name +" wasbind="+wasbind);
		slotSoulVo.makeSoulSlotGemVal(equipSlot,gemIndex,itemId,wasbind);
		taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_714,itemPo.getItemLv(),null);
		LogUtil.writeLog(this, 206, itemId, 0, 0, GlobalCache.fetchLanguageMap("key2438")+equipSlot, "");
		LogUtil.writeLog(this, 323, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
	}
	
	/**
	 * 卸载宝石
	 * @param equipSlot
	 * @param gemIndex
	 * @param itemId
	 * @return
	 */
	public  void soulGemRemove(Integer equipSlot,Integer gemIndex){
//		System.out.println("soulGemRemove() equipSlot="+equipSlot+"; gemIndex="+gemIndex);
		SlotSoulVo slotSoulVo = findSlotSoul(equipSlot);
		slotSoulVo.checkHasGemIndex(gemIndex);
		int itemId = slotSoulVo.fetchItemIdByGemIndex(gemIndex);
		int wasbind =slotSoulVo.fetchItemIdwasbindByGemIndex(gemIndex);
//		System.out.println("soulGemRemove() wasbind = " +wasbind);
//		System.out.println(name +" wasbind="+wasbind);
		addItem(itemId, 1,wasbind);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, itemId, 1, GlobalCache.fetchLanguageMap("key2439"), "");
		LogUtil.writeLog(this, 207, itemId, 0, 0, GlobalCache.fetchLanguageMap("key2440")+equipSlot, "");
		slotSoulVo.makeSoulSlotGemVal(equipSlot,gemIndex,0,0);
	}
	/**
	 * 刷新镖车
	 * @param type 1:普通； 2：一键橙车
	 * @return
	 */
	public void flushYunDartCar(Integer type){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		MapRoom mapRoom = MapWorld.findStage(getRoomId());
		if(mapRoom == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"mapRoomId:  "+getRoomId());
		}
		Fighter mover=mapRoom.findMoverId(listYunDartTaskInfoVo.get(0).currentYunDartCarId);
		if(mover == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key211")+"：currentYunDartCarId:  "+listYunDartTaskInfoVo.get(0).currentYunDartCarId);
		}
		if(fighter != null && yunDartCar != null){
			fighter.mapRoom.cellData.removeLiving(yunDartCar, true);			
		}
		listYunDartTaskInfoVo.get(0).currentYunDartCarId = -1;
		if(type == 1)
		{
			PlayItem playItem = fetchPlayItemByType(PlayTimesType.PLAYTIMES_TYPE_10);
			VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
			int flag = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_10);
			int sum = flag +  playItem.initialTimes;
			if(listYunDartTaskInfoVo.get(0).dailyCurrentFreeFlushYunDartCarCount >= sum){
				checkHasItemElseDiamond(13, 0);
			}
			Cart cart = fetchYunCart();
			checkService.checkExisMonsterPo(cart.monsterId);
			mapRoom.spawnYunDartFighter(cart.monsterId, this);
			listYunDartTaskInfoVo.get(0).adjustDailyCurrentFreeFlushYunDartCarCount(1);
		}
		else if(type == 2)
		{
			VipPo vipPo = GlobalCache.mapVipPo.get(getVipLv());
			int flag = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_15);
			if(flag == 0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key65"));
			}
			checkHasItemElseDiamond(14, 0);
			List<Cart> cartList = XmlCache.xmlFiles.constantFile.trade.cart;
			Cart cart = cartList.get(cartList.size()-1);
			listYunDartTaskInfoVo.get(0).currentYunDartCarQuality=cart.quality;
			mapRoom.spawnYunDartFighter(cart.monsterId, this);
		}
	}
	
	/**
	 * 提交环任务
	 * @param awardType 普通=1
	 * @return
	 */
	public void taskRingSubmit(Integer awardType){
		Integer currentTaskId=0;
		if(getRingTaskCurrentIndex()<=10){	
			for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
				TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
				if(taskPo.getTaskType()==TaskType.TASK_TYPE_RING){
					//星级奖励系数
					int [] qualityPars={50,60,75,90,100};
					//是否双倍领取
					int awardTypePar=1;
					if(awardType==2){
						awardTypePar=2;
						int num=XmlCache.xmlFiles.constantFile.ringTask.doubleExpCost;
						checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,num);
						LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -num, GlobalCache.fetchLanguageMap("key2505"), "");
						checkHasAndConsumeResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, num);
					}
					setRingTaskCurrentIndex(getRingTaskCurrentIndex()+1);
					updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_510, 1);
					currentTaskId=taskPo.getId();
					listRoleTasks.remove(idNumberVo2);
					int lv=getLv();
					int expNum=Math.max(2000 *(lv-15),20000)*qualityPars[getRingTaskCurrentQuality()-1]/100*awardTypePar;
					int bindGoldNum=Math.max(100*lv,4000)*qualityPars[getRingTaskCurrentQuality()-1]/100*awardTypePar;
					adjustExp(expNum);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, expNum, GlobalCache.fetchLanguageMap("key2460"), "");
					adjustNumberByType(bindGoldNum, RoleType.RESOURCE_BIND_GOLD);
					LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, bindGoldNum, GlobalCache.fetchLanguageMap("key2460"), "");
					for (IdNumberVo idNumberVo : taskPo.listTaskDelivery) {
						removePackItem(idNumberVo.getId(), 999,GlobalCache.fetchLanguageMap("key2662"));
					}
					//10环任务都做完的奖励
					if(getRingTaskCurrentIndex()==11){
						List<IdNumberVo> awardList = IdNumberVo.createList(XmlCache.xmlFiles.constantFile.ringTask.ring10Award);
						addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2606"));
					}
					break;
				}
			}
			if(getRingTaskCurrentIndex()<=10){
				activeOneRandomRingTask();
			}
			setCurrentFinishTaskId(currentTaskId);
			taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_723, null, null);
		}
	}
	/**
	 * 一键满星
	 * @return
	 */
	public void taskRingQualityUp(){
		int goldNum=XmlCache.xmlFiles.constantFile.ringTask.onkeyStarCost;
		publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,goldNum);
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -goldNum, GlobalCache.fetchLanguageMap("key2459"), "");
		checkHasAndConsumeResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD, goldNum);
		setRingTaskCurrentQuality(5);
	}
	
	/**
	 * 一键全部完成
	 * @return
	 */
	public void taskRingAllFinish(Integer taskId){
		if(getRingTaskCurrentIndex()<=10){
			int num=10-getRingTaskCurrentIndex()+1;
			updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_510, num);
			int dimondNum=XmlCache.xmlFiles.constantFile.ringTask.onkeyFinishOneRingCost*num;
			checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,dimondNum);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -dimondNum, GlobalCache.fetchLanguageMap("key2457"), "");
			checkHasAndConsumeResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND, dimondNum);
			
			//加绑金和经验
			int lv=getLv();
			int expNum=Math.max(2000 *(lv-15),20000)*2;
			int bindGoldNum=Math.max(100*lv,4000)*2;
			adjustExp(expNum*num);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_EXP, 0, expNum*num, GlobalCache.fetchLanguageMap("key2458"), "");
			adjustNumberByType(bindGoldNum*num, RoleType.RESOURCE_BIND_GOLD);
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDGOLD, 0, bindGoldNum*num, GlobalCache.fetchLanguageMap("key2458"), "");
			setCurrentFinishTaskId(taskId);
			//加道具
			List<IdNumberVo> awardList = IdNumberVo.createList(XmlCache.xmlFiles.constantFile.ringTask.ring10Award);
			addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2607"));
			setRingTaskCurrentIndex(11);
			for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
				TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
				if(taskPo.getTaskType()==TaskType.TASK_TYPE_RING){
					listRoleTasks.remove(idNumberVo2);
					break;
				}
			}
			taskConditionProgressAdd(num, TaskType.TASK_TYPE_CONDITION_723, null, null);
		}
	}
	/**
	 * 成就领奖
	 * @return
	 */
	public void achieveFinish(Integer taskId){
		for (IdNumberVo2 idNumberVo2 : listRoleTasks) {
			TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
			if(idNumberVo2.getInt1()==taskId.intValue()){
				idNumberVo2.setInt2(TaskType.TASK_STATUS_ACHIEVED);
				addItemList(taskPo.listTaskAwardId,1, GlobalCache.fetchLanguageMap("key2357"));
				break;
			}
		}
	}
	/**
	 * 副本领取奖励
	 * @return
	 */
	public void takeBattleResultAward(Integer option){
		if(battleResultVo!=null){
			boolean flag = false;
			if(option==2){
				checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,30);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -30, GlobalCache.fetchLanguageMap("key2444"), "");
				checkHasAndConsumeBindDiamondThenDiamond(30);
				flag=true;
			}
			else if(option==3){
				checkHasResource(RoleType.RESOURCE_PRIORITY_BINDDIAMOND_THEN_DIAMOND,50);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -50, GlobalCache.fetchLanguageMap("key2445"), "");
				checkHasAndConsumeBindDiamondThenDiamond(50);
				flag=true;
			}
			if(flag){
				battleResultVo.exp=battleResultVo.exp*option;
			}
			awardBattleResult(battleResultVo);
		}
	}
	
	/**
	 * 检查退出公会时间
	 * @return
	 */
	public void checkGuildExitTime(){
		long endExitTime = guildExitTime.longValue() + (1000*60*60*24);
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		String language = String.valueOf(gp.valueObj);
		// 只有越南做限制
		if(endExitTime > System.currentTimeMillis() && "vi".equals(language)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2307"));
		}
	}
	
	/**
	 * 检查邮件时间删除邮件(15天)
	 */
	public void checkMailTimeRemove(){
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		List<SimpleMailHeadVo> list = mailService.mailLoadAll(this);
		List<Integer> removeList = new ArrayList<Integer>();
		for(SimpleMailHeadVo simpleMailHeadVo : list){
			long endTime=simpleMailHeadVo.getMailTime().longValue()+1000*60*60*24*15;
			if(System.currentTimeMillis() > endTime){
				removeList.add(simpleMailHeadVo.getMailId());
			}
		}
		for (int i = 0; i < removeList.size(); i++) {
			MailPo mailPo = mailService.getMail(removeList.get(i));
			mailService.removeMail(mailPo,false);
		}
		mailService.syncRoleRead(this);
//		sendUpdateClientMailUnRead();
	}
	
	/**
	 * 检查邮件结束时间删除邮件(运行管理后台)
	 */
	public void checkMailEndTimeRemove(){
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		List<SimpleMailHeadVo> list = mailService.mailLoadAll(this);
		List<Integer> removeList = new ArrayList<Integer>();
		for(SimpleMailHeadVo simpleMailHeadVo : list){
			MailPo mailPo = mailService.getMail(simpleMailHeadVo.getMailId());
			if(mailPo != null && mailPo.getEndTime() != null&& mailPo.getEndTime().longValue() !=0l && System.currentTimeMillis() > mailPo.getEndTime().longValue()){
				removeList.add(simpleMailHeadVo.getMailId());
			}
		}
		for (int i = 0; i < removeList.size(); i++) {
			MailPo mailPo = mailService.getMail(removeList.get(i));
			mailService.removeMail(mailPo,false);
		}
		mailService.syncRoleRead(this);
	}
	
	

	/**
	 * 修改玩家名称
	 * @param name
	 * @return
	 */
	public void changeRoleName(String name){
		checkHasItemElseDiamond(17, 0);
		setName(name);
		if(this.fighter != null){
			BattleMsgUtil.abroadRoleChangeName(this.fighter);			
		}
		checkChangeRoleName(name);
	}
	/**
	 * 后台修改玩家名称
	 * @param name
	 * @return
	 */
	public void backChangeRoleName(String name){

		setName(name);
		if(this.fighter != null){
			BattleMsgUtil.abroadRoleChangeName(this.fighter);			
		}
		checkChangeRoleName(name);
	}
	
	public void checkChangeRoleName(String name){
		//公会相关改名
		GuildPo guildPo = GuildPo.findEntity(getGuildId());
		if(guildPo != null){
			for(GuildMemberVo guildMemberVo : guildPo.listMembers){
				if(guildMemberVo.roleId.intValue() == id.intValue()){
					guildMemberVo.roleName=name;
					if(guildMemberVo.guildPosition.intValue()==1){
						guildPo.setLeaderRoleName(name);
						GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
						SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
						if(siegeBidVo.ownerGuidId.intValue()==guildPo.getId()){
							siegeBidVo.ownerGuildLeaderName=name;
						}
					}
					break;
				}
			}
		}
		
		
		// 竞技场改名
		RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(getId());
		if(me != null){
			me.setRoleName(name);
		}
		
		//roleInfor改名
		RoleInforPo roleInforPo = RoleInforPo.findEntity(roleInforId);
		if(roleInforPo != null){
			roleInforPo.setRoleName(name);			
		}
		
		//排行榜
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(IntUtil.checkInInts(liveActivityPo.getType(), LiveActivityType.LiveActivity_TYPE)){
				for(RankVo rankVo : liveActivityPo.listRankItems){
					if(rankVo.roleId.intValue()==id.intValue()){
						rankVo.roleName=name;
						break;
					}
				}
			}
		}
		
		//队伍改名
		if(teamMemberVo != null && teamMemberVo.teamVo != null){
			TeamMemberVo tmv = teamMemberVo.teamVo.fetchTeamMemberVo(id);
			if(tmv != null){
				tmv.roleName=name;
				teamMemberVo.roleName=name;
				teamMemberVo.teamVo.sendAllMemberUpdateTeamInfor();				
			}
		}
		
		//拍卖行改名
		List<AuctionItemPo> auctionItemPoList=fetchAuctionMySellList();
		for(AuctionItemPo auctionItemPo : auctionItemPoList){
			AuctionItemPo aip = AuctionItemPo.findEntity(auctionItemPo.getId());
			aip.setSellerRoleName(name);
		}
		
		if(pVPPVEActivityStatusVo != null){
			pVPPVEActivityStatusVo.roleName=name;
		}
		
	}
	
	
	
	/**
	 * 公会退出时间(客户端用)
	 */
	public void checkClientGuildExitTime(){
		clientGuildExitTime = String.valueOf(guildExitTime);
	}
	
	/**
	 * 初始化成长基金
	 * @param typeId
	 */
	public void checkInitGrowFundAward(Integer typeId){
		GrowFund growFund = GlobalCache.fetchGrowFund(typeId);
		if(typeId.intValue() == 1){
			this.listgrowFundsAward1.clear();
			List<IdNumberVo> list = IdNumberVo.createList(growFund.award);
			for(int i=0; i < list.size(); i++){
				IdNumberVo idNumberVo = list.get(i);
				listgrowFundsAward1.add(new IdNumberVo2(idNumberVo.getId(), idNumberVo.getNum(), 0));
			}
			growFundVo.buyGrowFund1Time=System.currentTimeMillis();
			sendUpdateGrowFund1Info();
		}else if(typeId.intValue() == 2){
			this.listgrowFundsAward2.clear();
			List<IdNumberVo> list = IdNumberVo.createList(growFund.award);
			for(int i=0; i < list.size(); i++){
				IdNumberVo idNumberVo = list.get(i);
				listgrowFundsAward2.add(new IdNumberVo2(idNumberVo.getId(), idNumberVo.getNum(), 0));
			}
			growFundVo.buyGrowFund2Time=System.currentTimeMillis();
			sendUpdateGrowFund2Info();
		}
	}
	
	/**
	 * 初始化钻石幸运抽取
	 */
	public void initListDiamondBasins(){
		UserPo userPo = UserPo.findEntity(getUserId());
		listDiamondBasins=userPo.listDiamondBasins;
	}
	
	public void initDiamondBasinsValidTime(){
		int validTime = XmlCache.xmlFiles.constantFile.diamondBasins.validTime;
		diamondBasinsValidTime=String.valueOf(getDiamondBasinsTime()*1000L+60*60*24*validTime*1000L);
//		diamondBasinsValidTime=String.valueOf(getCreateTime().longValue()+60*60*24*validTime*1000L);
	}
	
	

	
	
	/**
	 * 购买成长基金
	 * @param typeId 1.通用; 2.韩国
	 * @return
	 */
	public void buyGrowFund(Integer typeId){
		if(typeId.intValue() == 1){
//			ConsumPo consumPo = ConsumPo.findEntity(18);
//			checkHasAndConsumeResource(consumPo.getConsumType(),consumPo.getConsumNum());
			checkHasItemElseDiamond(18, 0);
			setWasGrowFunds1(1);
		}else if(typeId.intValue() == 2){
//			ConsumPo consumPo = ConsumPo.findEntity(19);
//			checkHasAndConsumeResource(consumPo.getConsumType(),consumPo.getConsumNum());
			checkHasItemElseDiamond(19, 0);
			setWasGrowFunds2(1);
		}
		GrowFund growFund = GlobalCache.fetchGrowFund(typeId);
		adjustNumberByType(growFund.onceAward, growFund.moneyType);
		if(growFund.moneyType==6){
			LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMOND, 0, growFund.onceAward, GlobalCache.fetchLanguageMap("key2598"), "");
		}
		checkInitGrowFundAward(typeId);
		
	}
	
	/**
	 * 获取成长基金1
	 * @return
	 */
	public void takeGrowFund1(Integer id){
		if(getWasGrowFunds1().intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2321"));
		}	
		GrowFund growFund = GlobalCache.fetchGrowFund(1);
		IdNumberVo2 idNumberVo2 = IdNumberVo2.findIdNumber(id,listgrowFundsAward1);
		if(idNumberVo2 == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2322"));
		}
		if(idNumberVo2.getInt3().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2323"));
		}
		if(idNumberVo2.getInt1().intValue() > getLv()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2324"));
		}
		
		if(idNumberVo2.getInt3().intValue() == 0){
			adjustNumberByType(idNumberVo2.getInt2(), growFund.moneyType);
			if(growFund.moneyType==6){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMOND, 0, idNumberVo2.getInt2(), GlobalCache.fetchLanguageMap("key2598"), "");
			}
			idNumberVo2.setInt3(1);
			growFundVo.takeGrowFund1Time=System.currentTimeMillis();
			growFundVo.takeGrowFund1Index=id;
		}
	}
	/**
	 * 领取成长基金2
	 * @return
	 */
	public void takeGrowFund2(){
		if(getWasGrowFunds2().intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2321"));
		}	
		GrowFund growFund = GlobalCache.fetchGrowFund(2);
//		long buyTime = DateUtil.getInitialDate(growFundVo.buyGrowFund2Time);
//		long currentTime = DateUtil.getInitialDate(System.currentTimeMillis());
//		int id=1;
//		if(currentTime>buyTime){
//			id=(int) ((currentTime-buyTime)/1000*60*60*24)+1;
//		}
		checkGrowFunds2TakeDay();
//		System.out.println("takeGrowFund2() id="+growFunds2TakeDay);
		IdNumberVo2 idNumberVo2 = IdNumberVo2.findIdNumber(growFunds2TakeDay,listgrowFundsAward2);
		if(idNumberVo2 == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2322"));
		}
		if(idNumberVo2.getInt3().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2323"));
		}
		if(idNumberVo2.getInt3().intValue() == 0){
			adjustNumberByType(idNumberVo2.getInt2(), growFund.moneyType);
			if(growFund.moneyType==6){
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_BINDDIMOND, 0, idNumberVo2.getInt2(), GlobalCache.fetchLanguageMap("key2598"), "");
			}
			idNumberVo2.setInt3(1);
			growFundVo.takeGrowFund2Time=System.currentTimeMillis();
			growFundVo.takeGrowFund2Index=idNumberVo2.getInt1();
		}
	}
	
	/**
	 * 初始化邀请任务列表
	 */
	public void checkInitInvitationTask(){
		if(this.listInvitationTask== null || this.listInvitationTask.size() == 0){
			listInvitationTask = new CopyOnWriteArrayList<IdNumberVo2>();
			GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<InvitationPo> invitationPoList =gameDataTemplate.getDataList(InvitationPo.class);
			for(InvitationPo invitationPo : invitationPoList){
				listInvitationTask.add(new IdNumberVo2(invitationPo.getId(), 0, 0));
			}
		}
	}
	
	/**
	 * 验证成长基金2领取天数
	 */
	public void checkGrowFunds2TakeDay(){
		if(wasGrowFunds2.intValue() == 1){
			long buyTime = DateUtil.getInitialDate(growFundVo.buyGrowFund2Time);
			long currentTime = DateUtil.getInitialDate(System.currentTimeMillis());
			int id=1;
			if(currentTime>buyTime){
				id=(int) ((currentTime-buyTime)/(1000L*60*60*24))+1;
			}
			growFunds2TakeDay = id;
		}
	}
	
	
	public void checkInvitationTask(boolean bool){
		updateListInvitationFriend();
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		for(IdNumberVo2 idNumberVo2 : listInvitationTask){
			InvitationPo invitationPo = InvitationPo.findEntity(idNumberVo2.getInt1());
			if(invitationPo.getInvitationCondition().intValue() == InvitationType.INVITATION_TYPE_PLAYER_NUM){
				int num=listInvitationFriend.size();
				num=Math.min(invitationPo.getConditionNumber(), num);
				idNumberVo2.setInt2(num);
				
			}
			if(invitationPo.getInvitationCondition().intValue()== InvitationType.INVITATION_TYPE_PLAYER_VIPLV){
				int temp = 0;
				for(InvitationRoleVo invitationRoleVo : listInvitationFriend){
					if(temp < invitationRoleVo.vipLv){
						temp = invitationRoleVo.vipLv;
					}
				}
				temp=Math.min(invitationPo.getConditionNumber(), temp);
				idNumberVo2.setInt2(temp);
			}
			if(invitationPo.getInvitationCondition().intValue() == InvitationType.INVITATION_TYPE_PLAYER_LV){
				int temp = 0;
				for(InvitationRoleVo invitationRoleVo : listInvitationFriend){
					if(temp < invitationRoleVo.lv){
						temp = invitationRoleVo.lv;
					}
				}
				temp=Math.min(invitationPo.getConditionNumber(), temp);
				idNumberVo2.setInt2(temp);
			}
			
			if(idNumberVo2.getInt2().intValue() == invitationPo.getConditionNumber().intValue() && idNumberVo2.getInt3().intValue() == 0){
				List<IdNumberVo3> list = new ArrayList<IdNumberVo3>();
				for(IdNumberVo2 inv2 : invitationPo.listInvitationReward){
					list.add(new IdNumberVo3(1, inv2.getInt1(), inv2.getInt2(), inv2.getInt3()));
				}
				mailService.sendAwardSystemMail(id, GlobalCache.fetchLanguageMap("key2337"),invitationPo.getInvitationInfo(),list);
				idNumberVo2.setInt3(1);
			}
		}
		
		if(bool){
			sendUpdateListInvitationTask();			
		}
	}
	
	/**
	 * 更新邀请好友列表
	 */
	public void updateListInvitationFriend(){
		for(InvitationRoleVo invitationRoleVo : listInvitationFriend){
			RolePo targeRole = RolePo.findEntity(invitationRoleVo.id);
			if(targeRole != null){
				invitationRoleVo.initProperty(targeRole);				
			}
		}
	}
	
	public void useInvitationCode(String currentInvitationCode){
		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
		if(!StringUtil.isEmpty(getInputInvitationCode())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2325"));
		}
		
		Integer targetId = roleService.findRoleIdByInvitationCode(currentInvitationCode);
		if(targetId == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2326"));
		}
		if(id.intValue() == targetId.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2338"));
		}
		RolePo targeRole = RolePo.findEntity(targetId);
		if(targeRole.listInvitationFriend.size() >= 100){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2327"));
		}
		InvitationRoleVo invitatiionRoleVo = new InvitationRoleVo();
		invitatiionRoleVo.initProperty(this);
		targeRole.listInvitationFriend.add(invitatiionRoleVo);
		targeRole.checkInvitationTask(true);
		setInputInvitationCode(currentInvitationCode);
		String str =  MessageFormat. format(GlobalCache.fetchLanguageMap("key2597"), targeRole.getName());
		sendMsg(str);
	}
	
	/**
	 * 添加仇人
	 * @param roleId
	 */
	public Boolean addEnemy(Integer roleId){	
		RolePo enemyRole = RolePo.findEntity(roleId);
		if(enemyRole!= null){
			enemyRole.syncToInfor();
			RoleInforPo roleInfoPo = RoleInforPo.findEntity(enemyRole.getRoleInforId());
			if(roleInfoPo != null){
				for (RoleInforPo roleInforPo : listEnemys) {
					if(roleInforPo.getRoleId().intValue()==roleId){
						return false;
					}
				}
				listEnemys.add(roleInfoPo);
			}
		}
		
		for (RoleInforPo friendInforPo : listFriends) {
			if(friendInforPo.getRoleId()==roleId.intValue()){
				listFriends.remove(friendInforPo);
				break;
			}
		}
		sendUpdateRelations();
		return true;
	}
	
	/**
	 * 韩国临时加的活动
	 */
	public void koActivity(){
		String sql1 = "SELECT TABLE_NAME FROM information_schema.`COLUMNS` WHERE TABLE_NAME = 'u_po_ko'  and TABLE_SCHEMA='" + BaseStormSystemType.USER_DB_NAME + "'";
		SqlRowSet rs1 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql1);
		if (rs1.next()) {
	    	Integer result =BaseDAO.instance().queryIntForSql("select state from "+BaseStormSystemType.USER_DB_NAME+".u_po_ko where iuid='"+userIuid+"'",null);
	    	if(result!=null && result==0){
				MailService mailService = (MailService) BeanUtil.getBean("mailService");
				String title="CBT참여보상";
				String conent="CBT참여보상 지급해 드립니다.즐거운 시간 되세요!";
				StringBuilder sb=new StringBuilder();
				 sb.append(1);
				 sb.append("|");
				 sb.append(300004017);
				 sb.append("|");
				 sb.append(500);
				 sb.append("|");
				 sb.append(0);
				mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key239"), getId(), title, conent,sb.toString(), MailType.MAIL_TYPE_SYSTEM);
				BaseDAO.instance().execute("update "+BaseStormSystemType.USER_DB_NAME+".u_po_ko set state = 1 where iuid='"+userIuid+"'");
	    	}
		}

	}
	
	/**
	 * 检查活动排行
	 */
	public void checkliveActivityRank(){
		liveActivityRankPrower();
		liveActivityRankLv();
		liveActivityRankArena();
		liveActivityRankAchieve();
		liveActivityRankPrestige();
		liveActivityRankPet();
	}
	
	
	public int fetchDiamondBasins(){
//		System.out.println("listDiamondBasins="+listDiamondBasins);
		UserPo userPo = UserPo.findEntity(getUserId());
		int rewardDiamond=0;
		boolean bool = true;
//		System.out.println("11userPo.listDiamondBasins="+userPo.listDiamondBasins);
		for(IdNumberVo2 idNumberVo2 : userPo.listDiamondBasins){
			if(idNumberVo2.getInt2().intValue()==1 && idNumberVo2.getInt3().intValue()==0){
				DiamondBasin diamondBasin = GlobalCache.fetchDiamondBasinBytimes(idNumberVo2.getInt1());
				checkHasAndConsumeDiamond(diamondBasin.diamond);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, -diamondBasin.diamond, GlobalCache.fetchLanguageMap("key2633"), "");
				List<List<Integer>> list =  StringUtil.buildBattleExpressList(diamondBasin.value);
				IdNumberVo idNumberVo = RandomUtil.calcWeightOverCardAward(list, null);
				int randomInt = RandomUtil.randomInteger(2);
				
				if(randomInt==0){
					rewardDiamond=idNumberVo.getId();
				}else{
					rewardDiamond=idNumberVo.getNum();
				}
				idNumberVo2.setInt3(1);
				adjustNumberByType(rewardDiamond, RoleType.RESOURCE_DIAMOND);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_DIMOND, 0, rewardDiamond, GlobalCache.fetchLanguageMap("key2632"), "");
				bool=false;
				break;
			}
		}
//		System.out.println("22userPo.listDiamondBasins="+userPo.listDiamondBasins);
		if(bool){
			//再充值（needVipExp-当前VIP经验）钻石可使用
			int value = 0;
			for(IdNumberVo2 idNumberVo2 : userPo.listDiamondBasins){
				if(idNumberVo2.getInt2().intValue()==0 && idNumberVo2.getInt3().intValue()==0){
					DiamondBasin diamondBasin = GlobalCache.fetchDiamondBasinBytimes(idNumberVo2.getInt1());
					checkHasResource(RoleType.RESOURCE_DIAMOND, diamondBasin.diamond);
//					System.out.println("diamondBasin.needVipExp="+diamondBasin.needVipExp+"; userPo.getCumulativeRechargeNum()="+userPo.getCumulativeRechargeNum());
					value = diamondBasin.needVipExp-userPo.getCumulativeRechargeNum().intValue(); 
//					System.out.println("value="+value);
					break;
				}
			}
			//在充值{0}钻石可使用!
			if(value > 0){
				String str = MessageFormat. format(GlobalCache.fetchLanguageMap("key2634"),value);
				ExceptionUtil.throwConfirmParamException(str);				
			}else{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2643"));	
			}
		}
		return rewardDiamond;
	}
	
	
	public int fetchUserCumulativeRechargeNum(){
		int value = 0;
		UserPo userPo=UserPo.findEntity(this.getUserId());
		if(userPo !=null){
			value=userPo.getCumulativeRechargeNum();			
		}
		return value;
	}


	public int copySceneStart(Integer copySceneConfigId){
		CheckService checkService = CheckService.instance();
		int mapRoomId = 0;
		if(copySceneStartState){
			

			checkService.checkExisCopySceneConfPo(copySceneConfigId);	
			CopySceneConfPo realCopySceneConfPo= CopySceneConfPo.findEntity(copySceneConfigId);
			checkService.checkExisCopyScenePo(realCopySceneConfPo.getCopySceneId());
			CopyScenePo copyScenePo = CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
			checkCopySceneConfig(copySceneConfigId);
			
			MapRoom mapRoom = MapWorld.createDynalicMapRoom(realCopySceneConfPo,ScenePo.findEntity(realCopySceneConfPo.getSceneId()));
			if(realCopySceneConfPo.getTeamMode().intValue()==1){
				if(teamMemberVo==null){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key186"));
				}
				TeamVo teamVo = TeamVo.findTeam(teamMemberVo.teamVo.id, copySceneConfigId);
//				for (TeamMemberVo teamMemberVo : teamVo.teamMemberVos) {
//					if(this.teamMemberVo==teamMemberVo){
//						continue;
//					}
//					RolePo targetRole =RolePo.findEntity(teamMemberVo.roleId);
//					for (IdNumberVo3 idNumberVo3 : targetRole.listCopySceneTodayVisitTimes) {
//						if(idNumberVo3.getInt1().intValue()==copySceneConfigId.intValue() && idNumberVo3.getInt2().intValue()==realCopySceneConfPo.getTeamMode().intValue()){
//							targetRole.checkSceneTimesBySceneId(idNumberVo3.getInt3(), realCopySceneConfPo.getSceneId(), realCopySceneConfPo.getAvaTimes());
//						}
//					}
//				}
				teamVo.startTeamRoomGather(mapRoom);
				teamVo.teamStatus = 1;
				for (TeamMemberVo teamMemberVo : teamVo.teamMemberVos) {
					RolePo targetRole =RolePo.findEntity(teamMemberVo.roleId);
//				targetRole.sendAskTeamEnterRoom(mapRoom.mapRoomId, mapRoom.sceneId,realCopySceneConfPo.getTeamMode(),copySceneConfigId,realCopySceneConfPo.getDifficulty());
					targetRole.teamMemberVo.readyTeamRoomGather();
					if(!IntUtil.checkInInts(mapRoom.copySceneConfPo.getId(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
						targetRole.addCopySceneTodayRecord(mapRoom.copySceneConfPo.getId(),realCopySceneConfPo.getTeamMode(),1,0);
						if(copyScenePo.getCopyType().intValue() == 1 && targetRole.getId() != getId()){
							targetRole.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_708,mapRoom.copySceneConfPo.getCopySceneId(),null);			
						}
					}
				}
				
//			rolePo.teamMemberVo.readyTeamRoomGather();
			}
			else{
				for (IdNumberVo3 idNumberVo3 : listCopySceneTodayVisitTimes) {
					if(idNumberVo3.getInt1().intValue()==copySceneConfigId.intValue() && idNumberVo3.getInt2().intValue()==realCopySceneConfPo.getTeamMode().intValue()){
						checkSceneTimesBySceneId(idNumberVo3.getInt3(), realCopySceneConfPo.getSceneId(), realCopySceneConfPo.getAvaTimes());
					}
				}
				if(!IntUtil.checkInInts(mapRoom.copySceneConfPo.getId(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
					addCopySceneTodayRecord(mapRoom.copySceneConfPo.getId(),realCopySceneConfPo.getTeamMode(),1,0);
				}
			}
			if(copyScenePo.getCopyType().intValue() == 1){
//			System.out.println("mapRoom.copySceneConfPo.getCopySceneId()="+mapRoom.copySceneConfPo.getCopySceneId());
				taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_708,mapRoom.copySceneConfPo.getCopySceneId(),null);			
			}
//			sendUpdateCurrentTime();

			LogUtil.writeLog(this, 236, copySceneConfigId, 0, 0, GlobalCache.fetchLanguageMap("key2628"), "");
			if(copySceneConfigId.intValue()==20206008){
				LogUtil.writeLog(this, 332, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(copySceneConfigId.intValue()==20206009){
				LogUtil.writeLog(this, 333, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(copySceneConfigId.intValue()==20206010){
				LogUtil.writeLog(this, 334, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(copySceneConfigId.intValue()==20206011){
				LogUtil.writeLog(this, 335, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(copySceneConfigId.intValue()==20201002){
				LogUtil.writeLog(this, 337, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}else if(copySceneConfigId.intValue()==20201001){
				LogUtil.writeLog(this, 338, this.getLv(), this.getCareer(),this.getVipLv(), "", "");
			}
			mapRoomId=mapRoom.mapRoomId;
			copySceneStartState=false;
		}
		
		return mapRoomId;
	}
	
	
	/**
	 * 检查装备强化等级
	 * @param powerLv
	 */
	public void checkpowerEquip(CopyOnWriteArrayList<IdNumberVo2> listTasks){
		Integer[] equips = new Integer[]{equipWeaponId,equipArmorId,equipRingId,equipBracerId,equipNecklaceId,equipHelmetId,equipShoeId,equipBraceletId,equipBeltId,equipPantsId};
		for(IdNumberVo2 idNumberVo2 : listTasks){
			TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
			if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_724){
				int index=0;
				int powerLv=taskPo.conditionVals.get(2);
				for(int i=0; i<equips.length; i++){
					if(equips[i]!=null){
						EqpPo eqpPo = EqpPo.findEntity(equips[i]);
//						System.out.println("22eqpPoId="+eqpPo.getId() +"; powerLv="+eqpPo.getPowerLv());
						if(eqpPo!= null && eqpPo.getPowerLv().intValue() >= powerLv){
							index++;
						}
					}
				}

				Iterator iter = mainPackItemVosMap.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					RolePackItemVo rolePackItemVo = (RolePackItemVo) entry.getValue();
					if(rolePackItemVo.wasEquip()){
						EqpPo eqpPo = EqpPo.findEntity(rolePackItemVo.equipId);
						if(eqpPo!= null && eqpPo.getPowerLv().intValue() >= powerLv){
							index++;
						}
					}
				}
//				System.out.println("index ="+index);
				taskConditionProgressReplace(index, TaskType.TASK_TYPE_CONDITION_724, powerLv, null);				
			}
		}
	}
	
	/**
	 * 检查部位升星条件
	 * @param listTasks
	 */
	public void checkTaskType712(CopyOnWriteArrayList<IdNumberVo2> listTasks){
		for(IdNumberVo2 idNumberVo2 : listTasks){
			TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
			if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_712){
				int index=0;
				int powerLv=taskPo.conditionVals.get(2);
				for (SlotSoulVo slotSoulVo : listSlotSouls) {
					if(slotSoulVo!= null && slotSoulVo.powerLv.intValue() >= powerLv){
						index++;
					}
				}
				taskConditionProgressReplace(index, TaskType.TASK_TYPE_CONDITION_712, powerLv, null);
			}
		}
	}
	
	/**
	 * 检查部位洗练条件
	 * @param listTasks
	 */
	public void checkTaskType713(CopyOnWriteArrayList<IdNumberVo2> listTasks){
		for(IdNumberVo2 idNumberVo2 : listTasks){
			TaskPo taskPo = TaskPo.findEntity(idNumberVo2.getInt1());
			if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_713){
				int index=0;
				int powerLv=taskPo.conditionVals.get(2);
				for (SlotSoulVo slotSoulVo : listSlotSouls) {
					if(slotSoulVo!= null && slotSoulVo.extract1Star >= powerLv){
						index++;
					}
					if(slotSoulVo!= null && slotSoulVo.extract2Star >= powerLv){
						index++;
					}
					if(slotSoulVo!= null && slotSoulVo.extract3Star >= powerLv){
						index++;
					}
				}
				taskConditionProgressReplace(index, TaskType.TASK_TYPE_CONDITION_713, powerLv, null);
			}
		}
	}
	
	
	/**
	 * 活动结束删除道具
	 */
	public void checkRemovePackActivatyItem(){
		List<LiveActivityPo> liveActivityPos = GlobalCache.fetchLiveActivityPosAll();
		for(LiveActivityPo liveActivityPo:liveActivityPos){
			if(liveActivityPo.getType()==LiveActivityType.LiveActivity_EXCHANGE&&!liveActivityPo.wasLiveActivityOpen()){
				List<IdNumberVo> listRateItems = liveActivityPo.listRateItems;
				for(IdNumberVo idNumberVo:listRateItems){
					int dropId = idNumberVo.getId();
					if(dropId<8000||dropId>9000){
						continue;
					}
					List<DropPo> dropPos = GlobalCache.idTcGroupsMap.get(dropId);
					for(DropPo dropPo:dropPos){
						int itemId = dropPo.getItemId();
						removePackActivatyItem(itemId, mainPackItemVosMap);
						removePackActivatyItem(itemId, warehousePackItemVosMap);
					}
				}
			}
		}
	}
	
	public void removePackActivatyItem(int itemId,ConcurrentHashMap<String, RolePackItemVo> packItemVosMap){
		List<String> toRemoveKeys = new ArrayList<String>();
		for (RolePackItemVo rolePackItemVo : packItemVosMap.values()) {
			if (!rolePackItemVo.wasEquip()&& rolePackItemVo.getItemId().intValue() == itemId) {
				toRemoveKeys.add(rolePackItemVo.getIndex().toString());
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, itemId, -rolePackItemVo.num, GlobalCache.fetchLanguageMap("key2651"), "");
			}
		}
		for (String key : toRemoveKeys) {
			packItemVosMap.remove(key);
		}
	}
	
	
	/**
	 * 根据等级开启系统
	 * @param systemId
	 * @param needLv
	 */
	public void checkOpenSystemArrayListByLv(int systemId,int needLv){
		boolean bool =true;
		if(this.lv >= needLv){
			for(Integer i : openSystemArrayList){
				if(i == systemId){
					bool = false;
					break;
				}
			}
			if(bool){
				openSystemArrayList.add(systemId);
			}			
		}
	}
	
	/**
	 * 检查各种奖励领取
	 * @param copySceneConfPoId
	 */
	public void checkCopySceneConfPoReward(Integer copySceneConfPoId){
		CheckService checkService = CheckService.instance();
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo copySceneConfPo = CopySceneConfPo.findEntity(copySceneConfPoId);
		
		for (IdNumberVo3 idNumberVo3 : listCopySceneTodayVisitTimes) {
			if(idNumberVo3.getInt1().intValue()==copySceneConfPoId.intValue() && idNumberVo3.getInt2().intValue()==copySceneConfPo.getTeamMode().intValue()){
				if(IntUtil.checkInInts(copySceneConfPo.getId().intValue(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
					if(idNumberVo3.getInt4().intValue() > idNumberVo3.getInt3().intValue()){
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key120"));
					}	
				}else{
					if(idNumberVo3.getInt4().intValue() >= idNumberVo3.getInt3().intValue()){
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key120"));
					}					
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param itemId 道具ID
	 * @param toRemoveNumber 移除数量
	 * @param wasBind 是否绑定
	 * @return
	 */
	public int removePackItem(Integer itemId, Integer toRemoveNumber, Integer wasBind,String logString) {
		List<String> toRemoveKeys = new ArrayList<String>();
		int totalCount = 0;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			if(toRemoveNumber <= 0) break;
			if (!rolePackItemVo.wasEquip() && rolePackItemVo.getItemId().intValue() == itemId.intValue()
					&& rolePackItemVo.getBindStatus().intValue()==wasBind) {
				int thisTimeRemoveNumber = Math.min(toRemoveNumber, rolePackItemVo.getNum());
				totalCount +=thisTimeRemoveNumber;
				if (rolePackItemVo.getNum() > thisTimeRemoveNumber) {
					rolePackItemVo.setNum(rolePackItemVo.getNum() - thisTimeRemoveNumber);
				} else {
					toRemoveKeys.add(rolePackItemVo.getIndex().toString());
				}
				toRemoveNumber = toRemoveNumber - thisTimeRemoveNumber;
			}
		}
		if(toRemoveKeys.size()>0){
			for (String key : toRemoveKeys) {
				mainPackItemVosMap.remove(key);
			}
		}
		LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM, itemId, -toRemoveNumber, logString, "");
		return totalCount;
	}
	
	/**
	 *  移除仓库道具
	 * @param itemId 道具ID
	 * @param toRemoveNumber 移除数量
	 * @param wasBind 是否绑定
	 * @return
	 */
	public int removeWarehouseItem(Integer itemId, Integer toRemoveNumber, Integer wasBind) {
		List<String> toRemoveKeys = new ArrayList<String>();
		int totalCount = 0;
		for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
			if(toRemoveNumber <= 0) break;
			if (!rolePackItemVo.wasEquip() && rolePackItemVo.getItemId().intValue() == itemId.intValue()
					&& rolePackItemVo.getBindStatus().intValue()==wasBind) {
				int thisTimeRemoveNumber = Math.min(toRemoveNumber, rolePackItemVo.getNum());
				totalCount +=thisTimeRemoveNumber;
				if (rolePackItemVo.getNum() > thisTimeRemoveNumber) {
					rolePackItemVo.setNum(rolePackItemVo.getNum() - thisTimeRemoveNumber);
				} else {
					toRemoveKeys.add(rolePackItemVo.getIndex().toString());
				}
				toRemoveNumber = toRemoveNumber - thisTimeRemoveNumber;
			}
		}
		if(toRemoveKeys.size()>0){
			for (String key : toRemoveKeys) {
				warehousePackItemVosMap.remove(key);
			}
		}
		
		return totalCount;
	}
	
	/**
	 * 根据装备ID移除背包装备
	 * @param equipId
	 */
	public void removePackEquip(Integer equipId,String logString) {
		List<String> toRemoveKeys = new ArrayList<String>();
		for (String  key : mainPackItemVosMap.keySet()) {
			RolePackItemVo rolePackItemVo = mainPackItemVosMap.get(key);
			if (rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId.intValue()){
				toRemoveKeys.add(key);
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM,rolePackItemVo.getItemId() , -1, logString, "");
			}
		}
		if(toRemoveKeys.size()>0){
			for (String toRemoveKey : toRemoveKeys) {
				mainPackItemVosMap.remove(toRemoveKey);
			}
		}
	}
	/**
	 * 根据装备ID移除仓库装备
	 * @param equipId
	 */
	public void removeWarehouseEquip(Integer equipId) {
		List<String> toRemoveKeys = new ArrayList<String>();
		for (String  key : warehousePackItemVosMap.keySet()) {
			RolePackItemVo rolePackItemVo = warehousePackItemVosMap.get(key);
			if (rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId.intValue()){
				toRemoveKeys.add(key);
			}
		}
		if(toRemoveKeys.size()>0){
			for (String toRemoveKey : toRemoveKeys) {
				warehousePackItemVosMap.remove(toRemoveKey);
			}
		}
	}
	
	
	
	/**
	 * 检查背包是否有指定的装备
	 * @param itemId
	 * @param requireCount
	 * @param bindState
	 * @return
	 */
	public boolean checkPackHasEquip(int equipId){
		boolean hasEquip = false;
		for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
			if(rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId){
				hasEquip = true;
				break;
			}
		}
		return hasEquip;
	}
	
	/**
	 * 检查仓库是否有指定的装备
	 * @param itemId
	 * @param requireCount
	 * @param bindState
	 * @return
	 */
	public boolean checkWarehouseHasEquip(int equipId){
		boolean hasEquip = false;
		for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
			if(rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId){
				hasEquip = true;
				break;
			}
		}
		return hasEquip;
	}
	
	public void checkpkRedTime(){
		if(getPkGrewRecoverTime()!=null&&getPkGrewRecoverTime()>0){
			IdLongVo2 idLongVo2 = IdLongVo2.findIdLong(RoleType.ONLINE_TIME_TYPE_GREY, listOnlineTime);
			// 灰名当前在线时间
			long currentOnlineTime =fetchRedCurrentOnlineTime().longValue() + idLongVo2.getLong3().longValue();
			// 剩余时间
			long surplusTime =  idLongVo2.getLong2().longValue() - currentOnlineTime;
			long pkRedTime = System.currentTimeMillis() + surplusTime;
			setPkGrewRecoverTime(pkRedTime);
		}
		if(getPkRedBeginTime()!=null&&getPkRedBeginTime()>0){
			IdLongVo2 idLongVo2 = IdLongVo2.findIdLong(RoleType.ONLINE_TIME_TYPE_RED, listOnlineTime);
			// 红名当前在线时间
			long currentOnlineTime =fetchRedCurrentOnlineTime().longValue() + idLongVo2.getLong3().longValue();
			// 剩余时间
			long surplusTime =  idLongVo2.getLong2().longValue() - currentOnlineTime;
			long pkRedTime = System.currentTimeMillis() + surplusTime;
			setPkLastRecoverTime(pkRedTime);
		}else if(getPkLastRecoverTime()!=null&&getPkLastRecoverTime()>0){
			setPkLastRecoverTime(0l);
			setPkStatus(Fighter.PK_STATUS_PEACE);
			if(fighter!=null){
				fighter.changePkStatus(Fighter.PK_STATUS_PEACE);
			}
			setPkValue(0);
			updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,0l,0l,0);
		}
	}

	public void sendUpdatePickCrisitalTodayTimes() {
		singleRole("PushRemoting.sendUpdatePickCrisitalTodayTimes", new Object[]{pickCrisitalTodayTimes},true);
		
	}
	
	/**
	 * 移交队长
	 */
	public void changeTeamCaptain(){
		if( teamMemberVo != null && teamMemberVo.teamVo!=null && teamMemberVo.isCaptain == 1){
			
			int targetId = 0; 
			for(TeamMemberVo tmv : teamMemberVo.teamVo.teamMemberVos){
				if(getId().intValue() != tmv.roleId.intValue()){
					targetId = tmv.roleId;						
				}
				if(targetId != 0){
					break;
				}
			}
			TeamVo teamVo = null;
			if(targetId != 0){
				TeamMemberVo tmv = teamMemberVo.teamVo.findTeamMember(targetId);
				if(tmv != null){
					teamVo = TeamVo.findTeam(tmv.teamVo.id, tmv.teamVo.currentCopySceneConfPoId);
					if(teamVo != null){
						teamVo.makeCaptain(tmv);					
						teamVo.sendAllDungeonTeamMemberUpdateTeamInfor();	
						TeamMemberVo currentMemberVo = teamVo.findTeamMember(getId());
						if(currentMemberVo != null){
							currentMemberVo.leaveTeam(teamVo.currentCopySceneConfPoId);	
						}
					}
				}			
			}else{
				teamMemberVo.teamVo.dismiss();
			}
		}
	}

	public void tryFixMe() {
		//小稽
		if(lv>=41){
			boolean matched=false;
			for (Integer intVal : openSystemArrayList) {
				if(intVal==9){
					matched=true;
				}
			}
			if(!matched){
				openSystemArrayList.add(9);
				System.out.println("fixed me");
			}
		}
	}
	
	public void adjustGamstoneFragment(Integer adjustFragment) {
		if (adjustFragment == null) {
			return;
		}
		int remainFragment = getGamstoneFragment().intValue() + adjustFragment.intValue();
		setGamstoneFragment(Math.max(0, remainFragment));
	}
	
	public void adjustSoul(Integer adjustSoul){
		if(adjustSoul==null){
			return;
		}
		if(getSoul()==null){
			setSoul(0);
		}
		setSoul(Math.max(0, getSoul().intValue()+adjustSoul.intValue()));
	}
	
	/**
	 * 修改背包装备
	 * @param equipId
	 */
	public void modifyPackEquip(Integer equipId, Integer powerLv, String attach) {
		for (String  key : mainPackItemVosMap.keySet()) {
			RolePackItemVo rolePackItemVo = mainPackItemVosMap.get(key);
			if (rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId.intValue()){
				if(powerLv >= 0) rolePackItemVo.eqpPo.setPowerLv(powerLv);
				if(StringUtil.isNotEmpty(attach)){
					if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
						rolePackItemVo.eqpPo.setAttach(StringUtil.EMPTY);
					}else{
						rolePackItemVo.eqpPo.setAttach(attach);
					}
					rolePackItemVo.eqpPo.updateAttach();
				}
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM,rolePackItemVo.getItemId() , -1, "修改背包装备:强化等级="+rolePackItemVo.eqpPo.getPowerLv()+", 随机附加属性="+attach, "");
				break;
			}
		}
	}
	
	/**
	 * 修改仓库装备
	 * @param equipId
	 */
	public void modifyWarehouseEquip(Integer equipId, Integer powerLv, String attach) {
		for (String  key : warehousePackItemVosMap.keySet()) {
			RolePackItemVo rolePackItemVo = warehousePackItemVosMap.get(key);
			if (rolePackItemVo.wasEquip() && rolePackItemVo.eqpPo.getId().intValue()==equipId.intValue()){
				if(powerLv >= 0) rolePackItemVo.eqpPo.setPowerLv(powerLv);
				if(StringUtil.isNotEmpty(attach)){
					if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
						rolePackItemVo.eqpPo.setAttach(StringUtil.EMPTY);
					}else{
						rolePackItemVo.eqpPo.setAttach(attach);
					}
					rolePackItemVo.eqpPo.updateAttach();
				}
				LogUtil.writeLog(this, 1, ItemType.LOG_TYPE_ITEM,rolePackItemVo.getItemId() , -1, "修改仓库装备:强化等级="+rolePackItemVo.eqpPo.getPowerLv()+", 随机附加属性="+attach, "");
				break;
			}
		}
	}
	
	/**
	 * 获取消费点消费消耗
	 * @param consumPo
	 * @param timesType
	 * @return
	 */
	public int fetchConsum(ConsumPo consumPo, int timesType){
		if(consumPo.getConsumNum()==null){
			return 0;
		}
		List<Integer> consum = DBFieldUtil.getIntegerListBySplitter(consumPo.getConsumNum(), "|");
		if(timesType==0){
			return consum.get(0).intValue();
		}else{
			switch(timesType){
			case PlayTimesType.PLAYTIMES_TYPE_540:
				IdNumberVo2 idNumberVo2 =fetchBuyPlayItemsByType(timesType);
				if(idNumberVo2==null){
					return consum.get(0).intValue();
				}else{
					return consum.get(Math.min(consum.size()-1, idNumberVo2.getInt3().intValue()));
				}
			default:
					return consum.get(0);
			}
		}
	}
	
	public void checkOpenSystem(int systemId){
		if(openSystemArrayList.isEmpty()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2717"));
		}
		boolean has = false;
		for(Integer id:openSystemArrayList){
			if(id.intValue()==systemId){
				has = true;
				break;
			}
		}
		if(!has){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2717"));
		}
	}
}
