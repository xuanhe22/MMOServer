package com.games.mmo.vo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ConstantFile{

	public Dota dota;
	public EquipPower equipPower;
	public Guild guild;
	public Wing wing;
	public SoulSlot soulSlot;
	public Global global;
	public RankAwards rankAwards;
	public PVP pvp;
	public AccumulativeTime accumulativeTime;
	public EquipRandomAtts equipRandomAtts;
	public EquipRandomAttsQualitys equipRandomAttsQualitys;
	public Pet pet;
	public TotalLogin totalLogin;
	public OnlineTimeAwrod onlineTimeAwrod;
	public LuckyWheel luckyWheel;
	public MonthAward monthAward;
	public LivelyAward livelyAward;
	public RingTask ringTask;
	public AwardRetrieves awardRetrieves;
	public PowerSuitPlus powerSuitPlus;
	public StarSuitPlus starSuitPlus;
	public WashSuitPlus washSuitPlus;
	public SustainKills sustainKills;
	public MonthCard monthCard;
	public FirstRecharge firstRecharge;
	public FbBindAward fbBindAward;
	public KillingTower killingTower;
	public Trade trade;
	public PlayTimes playTimes;
	public ReviveCosts reviveCosts;
	public Worship worship;
	public Treasures treasures;
	public Cimelias cimelias;
	public OffLineRewards offLineRewards;
	public ItemAcquisitionTips itemAcquisitionTips;
	public GrowFunds growFunds;
	public DiamondBasins diamondBasins;
	public OpenPackage openPackage;
	public static class ItemAcquisitionTips{
		public List<AcquisitionTips> acquisitionTips;
		public static class AcquisitionTips{
			@XmlAttribute
			public int systemId;
			@XmlAttribute
			public String itemName;
			@XmlAttribute
			public String provenance;
		}
	}
	public static class OpenPackage{
		public List<Consum> consum;
		public static class Consum{
			@XmlAttribute
			public int min;
			@XmlAttribute
			public int max;
			@XmlAttribute
			public int num;
		}
	}
	public static class OffLineRewards{
		public List<OffLineReward> offLineReward;
		public static class OffLineReward{
			@XmlAttribute
			public int minLv;
			@XmlAttribute
			public int maxLv;
			@XmlAttribute
			public int exp;
			@XmlAttribute
			public int bindGold;
		}
	}
	
	public static class Treasures{
		public List<Treasure> treasure;
		public static class Treasure {
			@XmlAttribute
			public int id;
			
			@XmlAttribute
			public int type;			
			
			@XmlAttribute
			public String awardItem;	
			
			@XmlAttribute
			public String treasureImage;
			@XmlAttribute
			public String treasureIcon;
			
			@XmlAttribute
			public String descrip;
			
			@XmlAttribute
			public String finishDes;
			
			@XmlAttribute
			public String title;
		}
	}
	
	public static class Cimelias{
		public List<Cimelia> cimelia;
		public static class Cimelia {
			@XmlAttribute
			public int id;		
			
			@XmlAttribute
			public String awardItem;	
			
			@XmlAttribute
			public String treasureImage;
			
			@XmlAttribute
			public String title;
			
			@XmlAttribute
			public String taskItem;
			
			
			@XmlAttribute
			public String taskSource;
		}
	}
	
	public static class KillingTower{
		public List<Toweritem> toweritem;
		public static class Toweritem {
			@XmlAttribute
			public int layer;
			
			@XmlAttribute
			public int needKillNum;			
			
			@XmlAttribute
			public String awards;	
			
			@XmlAttribute
			public int nextLayer;
			
		}
	}
	
	
	
	public static class Dota{
		public List<Layer> layer;
		public static class Layer {
			@XmlAttribute
			public int lv;
			@XmlAttribute
			public int suggestPower;
			@XmlAttribute
			public int monsterLv;
			@XmlAttribute
			public String showAwardItems;
			@XmlAttribute
			public String dropExp;
			@XmlAttribute
			public String script;
			@XmlAttribute
			public int exp;
			@XmlAttribute
			public String firstAwardExp;
		}
	}
	
	public static class Trade{
		public List<Cart> cart;
		public static class Cart {
			@XmlAttribute
			public int quality;
			@XmlAttribute
			public int monsterId;
			@XmlAttribute
			public int freshWeight;
			@XmlAttribute
			public int expPar;
			@XmlAttribute
			public int prestigePar;
		}
	}
	
	public static class EquipPower{
		public List<Power> power;
		public static class Power {
			@XmlAttribute
			public int lv;
			@XmlAttribute
			public int lowerRate;
			@XmlAttribute
			public int middleRate;
			@XmlAttribute
			public int highRate;
			@XmlAttribute
			public int value;
		}
	}
	
	public static class Guild{
		public Establish establish;
		public Contribute contribute;
		public PoseidonAward poseidonAward;
		public Exchange exchange;
		public Buildings buildings;
		public Maintiance maintiance;
		public Positions positions;
		public Guildboss guildboss;
		public Guildwar guildwar;
		public PriestFresh priestFresh;
		public static class Establish{
			@XmlAttribute
			public Integer lv;
			@XmlAttribute
			public Integer gold;
			@XmlAttribute
			public Integer diamond;
			@XmlAttribute
			public Integer item;
			@XmlAttribute
			public String num;
		}
		public static class PriestFresh{
			@XmlAttribute
			public Integer freeTimes;
			@XmlAttribute
			public Integer cost;
			public List<FreshQuality> freshQuality;
			public static class FreshQuality {
				@XmlAttribute
				public Integer id;
				@XmlAttribute
				public String name;	
				@XmlAttribute
				public Integer expPar;
				@XmlAttribute
				public Integer needGuildLv;
				@XmlAttribute
				public Integer freeWeight;
				@XmlAttribute
				public Integer weight;
			}
		}
		public static class Guildwar{
			public List<Territory> territory;
			public static class Territory {
				@XmlAttribute
				public Integer sceneid;
				@XmlAttribute
				public String award;	
				@XmlAttribute
				public String battleStartTime;	
				@XmlAttribute
				public String battleEndTime;
				@XmlAttribute
				public String competitiveStartTime;	
				@XmlAttribute
				public String competitiveEndTime;
				
				
				
			}
		}
		public static class Contribute {
			@XmlAttribute
			public String items;
		}
		public static class PoseidonAward {
			@XmlAttribute
			public String award;
		}
		public static class Exchange {
			@XmlAttribute
			public String items;
		}
		public static class Maintiance {
			public List<Cost> cost;
			public static class Cost {
				@XmlAttribute
				public Integer lv;
				@XmlAttribute
				public Integer gold;				
			}
		}
		public static class Guildboss {
			public List<Boss> boss;
			public static class Boss {
				@XmlAttribute
				public Integer lv;
				@XmlAttribute
				public Integer bossid;			
				@XmlAttribute
				public String award;		
				@XmlAttribute
				public Integer copysceneconfid;
			}
		}		
		public static class Positions{
			public List<Position> position;
			public static class Position{
				@XmlAttribute
				public Integer post;
				@XmlAttribute
				public Integer allow;
				@XmlAttribute
				public Integer dismiss;
				@XmlAttribute
				public Integer upgrade;
				@XmlAttribute
				public Integer notice;
				@XmlAttribute
				public Integer addMember;
				@XmlAttribute
				public Integer removeMember;
				@XmlAttribute
				public Integer exit;
				@XmlAttribute
				public Integer auto;
			}
		}
		public static class Buildings {
			public List<Building> building;
			public static class Building{
				@XmlAttribute
				public int id;
				@XmlAttribute
				public String name;
				@XmlAttribute
				public int skillId;
				@XmlAttribute
				public int skillCostHonor;
				public List<Upgrade> upgrade;		
				public static class Upgrade{
					@XmlAttribute
					public int lv;
					@XmlAttribute
					public int costGold;
					@XmlAttribute
					public String itemList;
					@XmlAttribute
					public int skillId;
					@XmlAttribute
					public int skillCostHonor;
				}
			}	
		}
	}
	public static class SoulSlot{
		public SoulPower soulPower;
		public Extract extract;
		public static class SoulPower{
			public List<StarUpgrade> starUpgrade;
			public static class StarUpgrade{
				@XmlAttribute
				public Integer lv;
				@XmlAttribute
				public Integer attrRate;
				@XmlAttribute
			    public Integer costItemId;
				@XmlAttribute
				public Integer costItemCount;
				@XmlAttribute
				public Integer successRate;
				@XmlAttribute
				public Integer upRate;
				@XmlAttribute
				public String upVal;
				@XmlAttribute
				public String downVal;
			}
		}
		public static class Extract{
			@XmlAttribute
			public String totalAtb;
			@XmlAttribute
			public String initAtb;
			public List<ExtractUpgrade> extractUpgrade;
			public static class ExtractUpgrade{
				@XmlAttribute
				public Integer quality;
				@XmlAttribute
				public Integer slot;
				@XmlAttribute
				public Integer costNormalItemId;
				@XmlAttribute
				public String extractNormalExp;
				@XmlAttribute
				public Integer extractNormalNums;
				@XmlAttribute
				public Integer costAdvanceItemId;
				@XmlAttribute
				public String extractAdvanceExp;
				@XmlAttribute
				public Integer extractAdvanceNums;
			}
		}
	}
	public static class Global{
		public CareerItems careerItems;
		public static class CareerItems{
			public List<ReplaceItem> replaceItem;
			public static class ReplaceItem{
				@XmlAttribute
				public Integer itemId;
				@XmlAttribute
				public String replaceItems;
			}
		}
	}
	public static class Wing{
		/**
		 * 翅膀星级消耗道具编号
		 */
		@XmlAttribute
		public int starUpgradeCostItemId;
		
		/**
		 * 翅膀阶数消耗道具编号
		 */
		@XmlAttribute
		public int stepUpgradeCostItemId;
		public WingLooks wingLooks;
		public Stars stars;
		public static class WingLooks{
			public List<WingLook> wingLook;
			public static class WingLook{
				/**
				 * 结束
				 */
				@XmlAttribute
				public int step;
				/**
				 * 图片
				 */
				@XmlAttribute
				public String icon;
				/**
				 * 翅膀名称
				 */
				@XmlAttribute
				public String name;
				/**
				 * 模型
				 */
				@XmlAttribute
				public String model;
			}
		}
		public static class Stars{
			public List<Star> star;
			public static class Star{
				/**
				 * 当前星级
				 */
				@XmlAttribute
				public int lv;
				/**
				 * 翅膀名字
				 */
				@XmlAttribute
				public String name;
				/**
				 * 星级道具消耗数量
				 */
				@XmlAttribute
				public int starUpgradeCostItemCount;
				/**
				 * 阶数道具消耗数量
				 */
				@XmlAttribute
				public int stepUpgradeCostItemCount;
				/**
				 * 星级属性附加
				 */
				@XmlAttribute
				public String batExp;
				
				/**
				 * 需要的经验
				 */
				@XmlAttribute
				public int starExp;
				
				/**
				 * 升阶最小祝福
				 */
				@XmlAttribute
				public int stepMinExp;
				
				/**
				 * 升阶最大祝福
				 */
				@XmlAttribute
				public int stepMaxExp;
				
				/**
				 * 升阶失败返回祝福
				 */
				@XmlAttribute
				public int stepFailExp;
			}
		}
	}
	
	public static class RankAwards{
		/**
		 *  金币道具
		 */
		@XmlAttribute
		public int goldItemId;
		/**
		 * 技能点道具
		 */
		@XmlAttribute
		public int skillPointItemId;
		public List<RankAward> rankAward;
		public static class RankAward{
			/**
			 * 最小排名
			 */
			@XmlAttribute
			public int minRank;
			/**
			 * 最大排名
			 */
			@XmlAttribute
			public int maxRank;
			/**
			 * 奖励金币基础数量
			 */
			@XmlAttribute
			public int gold;
			/**
			 * 奖励技能点基础数量
			 */
			@XmlAttribute
			public int skillPoint;
			/**
			 * 根据排名增加的金币
			 */
			@XmlAttribute
			public int goldAdd;
			/**
			 * 根据排名增加技能点
			 */
			@XmlAttribute
			public int skillPointAdd;
		}
	}
	
	public static class PVP{
		public List<DropProbability> dropprobability;
		public static class DropProbability{
			/**
			 * 装备品质
			 */
			@XmlAttribute
			public int quality;
			/**
			 * 红名掉落概率
			 */
			@XmlAttribute
			public int red;
			 /**
			  * 白名掉落概率
			  */
			@XmlAttribute
			public int white;
			/**
			 * 最少掉落数量
			 */
			@XmlAttribute
			public int minNum;
			/**
			 * 最多掉落数量
			 */
			@XmlAttribute
			public int maxNum;
		}
	}
	
	public static class AccumulativeTime{
		public List<OnlineTime> onlineTime;
		public static class OnlineTime{
			@XmlAttribute
			public long id;
			/**
			 * 需要的时间
			 */
			@XmlAttribute
			public long value;
			/**
			 * 是否需要在线：1需要，0不需要
			 */
			@XmlAttribute
			public int neadonline;
		}
	}
	public static class EquipRandomAttsQualitys{
		public List<EquipRandomAttsQuality> equipRandomAttsQuality;
		public static class EquipRandomAttsQuality{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public String value;
		}
	}
	public static class EquipRandomAtts{
		/**
		 * 装备品质和装备等级随机对应的随机属性配置
		 */
		public List<EquipRandomAtt> equipRandomAtt;
		public static class EquipRandomAtt{
			@XmlAttribute
			public int id;
			/**
			 * 装备等级
			 */
			@XmlAttribute
			public int level;
			/**
			 * 装备品质	
			 */
			@XmlAttribute
			public int quality;
			/**
			 * 最小属性条数
			 */
			@XmlAttribute
			public int minAttNum;
			/**
			 * 最大属性条数	
			 */
			@XmlAttribute
			public int maxAttNum;
			/**
			 * 属性库
			 */
			@XmlAttribute
			public String equipAttachs;
		}
	}
	
	public static class Pet{
		public PetConstells petConstells;
		public static class PetConstells{
			/**
			 * 星座普通强化所需道具ID
			 */
			@XmlAttribute
			public int itemId;
			/**
			 * 星座普通强化所需道具数量
			 */
			@XmlAttribute
			public int itemCount;
			/**
			 * 星座高级强化所需钻石数量
			 */
			@XmlAttribute
			public int diamond;
			public List<PetConstell> petConstell;
			public static class PetConstell{
				/**
				 * 星座id
				 */
				@XmlAttribute
				public int constellId;
				/**
				 * 开启节
				 */
				@XmlAttribute
				public int openNode;
				/**
				 * 星座属性列表(星座属性ID|概率,星座属性ID|概率...)
				 */
				@XmlAttribute
				public String constellAttachs;
				/**
				 * 1级随机等级(上升概率|下降概率|不变概率(普通),上升概率|下降概率|不变概率(高级))
				 */
				@XmlAttribute
				public String probability1;
				/**
				 * 2级随机等级(上升概率|下降概率|不变概率,上升概率|下降概率|不变概率)
				 */
				@XmlAttribute
				public String probability2;
				/**
				 * 3级随机等级(上升概率|下降概率|不变概率,上升概率|下降概率|不变概率)
				 */
				@XmlAttribute
				public String probability3;
				/**
				 * 4级随机等级(上升概率|下降概率|不变概率,上升概率|下降概率|不变概率)
				 */
				@XmlAttribute
				public String probability4;
				/**
				 * 5级随机等级(上升概率|下降概率|不变概率,上升概率|下降概率|不变概率)
				 */
				@XmlAttribute
				public String probability5;
			}
		}
		public PetAttachs petAttachs;
		public static class PetAttachs{
			public List<PetAttach> petAttach;
			public static class PetAttach{
				/**
				 * 星座属性ID
				 */
				@XmlAttribute
				public int attachId;
				/**
				 * 属性等级
				 */
				@XmlAttribute
				public int attachLevel;
				/**
				 * 图标
				 */
				@XmlAttribute
				public String icon;
				/**
				 * 名称
				 */
				@XmlAttribute
				public String name;
				/**
				 * 属性ID|属性值,属性ID|属性值
				 */
				@XmlAttribute
				public String attachValue;
			}
		}
		
		public PetSkill petSkill;
		public static class PetSkill{
			/**
			 * 宠物学习技能概率配置
			 */
			@XmlAttribute
			public String learn;
		}
		public PetTalent petTalent;
		public static class PetTalent{
			/**
			 * 宠物天赋（客户端用）
			 */
			@XmlAttribute
			public String learn;
		}
	}
	
	public static class TotalLogin{
		public List<Day> day;
		public static class Day{
			/**
			 * 第几天
			 */
			@XmlAttribute
			public Integer id;
			/**
			 * 前端显示道具Id
			 */
			@XmlAttribute
			public Integer showItemId;
			/**
			 * 权重随机掉落组
			 */
			@XmlAttribute
			public String drop;
		}
	}
	
	public static class OnlineTimeAwrod{
		public List<OnlineTimes> onlineTimes;
		public static class OnlineTimes{
			/**
			 * 在线奖励id
			 */
			@XmlAttribute
			public Integer id;
			/**
			 * 在线奖励时间
			 */
			@XmlAttribute
			public Integer onlineTime;
			/**
			 * 在线奖励
			 */
			@XmlAttribute
			public String award;
		}
	}
	
	public static class LuckyWheel{
		public List<Item> item;
		public static class Item{
			@XmlAttribute
			public Integer id;
			/**
			 * 免费时间
			 */
			@XmlAttribute
			public Integer freeTime;
			/**
			 * 奖励列表
			 */
			@XmlAttribute
			public String award;
			/**
			 * 奖励列表2
			 */
			@XmlAttribute
			public String award2;
		}
	}
	
	public static class MonthAward{
		public List<MonthItem> monthItem;
		public static class MonthItem{
			@XmlAttribute
			public Integer id;
			@XmlAttribute
			public Integer month;
			@XmlAttribute
			public String award;
		}
	}
	
	public static class AwardRetrieves
	{
		/**
		 * 奖励计算方式：   奖励数量=系数1*人物等级+系数2
		 */
		public List<AwardRetrieve> awardRetrieve;
		public static class AwardRetrieve
		{
			/**
			 * 系统id
			 */
			@XmlAttribute
			public Integer id;
			/**
			 * 次数类型
			 */
			@XmlAttribute
			public Integer timesType;
			/**
			 * 系统名称
			 */
			@XmlAttribute
			public String name;
			/**
			 * 开启等级
			 */
			@XmlAttribute
			public Integer openLv;
			/**
			 * 金币找回消耗
			 */
			@XmlAttribute
			public Integer gold;
			/**
			 * 金币找回奖励(道具id|系数1|系数2,道具id|系数1|系数2)
			 */
			@XmlAttribute
			public String goldAward;
			/**
			 * 钻石找回消耗
			 */
			@XmlAttribute
			public Integer diamond;
			/**
			 * 钻石找回奖励(道具id|系数1|系数2,道具id|系数1|系数2)
			 */
			@XmlAttribute
			public String diamondAward;
		}

	}
	
	public static class LivelyAward{
		public List<LivelyItem> livelyItem;
		public static class LivelyItem{
			@XmlAttribute
			public Integer id;
			@XmlAttribute
			public Integer needScore;
			@XmlAttribute
			public String award;
		}
	}
	
	public static class RingTask{
		@XmlAttribute
		public int onkeyStarCost;
		@XmlAttribute
		public int onkeyFinishOneRingCost;
		@XmlAttribute
		public int doubleExpCost;
		@XmlAttribute		
		public String ring10Award;
	}
	
	/**
	 * 强化套装加成
	 * @author peter
	 *
	 */
	public static class PowerSuitPlus{
		public List<PowerItem> powerItem;
		public static class PowerItem{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int num;
			@XmlAttribute
			public int lv;
			@XmlAttribute
			public String atb;
		}
	}
	
	/**
	 * 升星套装加成
	 * @author peter
	 *
	 */
	public static class StarSuitPlus{
		public List<StarItem> starItem;
		public static class StarItem{
			@XmlAttribute
			public int id;
			@XmlAttribute
			/** 达到的数量 */
			public int num;
			@XmlAttribute
			/** 需要的等级*/
			public int lv;
			@XmlAttribute
			public String atb;
		}
	}
	/**
	 * 洗练套装加成
	 * @author peter
	 *
	 */
	public static class WashSuitPlus{
		public List<WashItem> washItem;
		public static class WashItem{
			@XmlAttribute
			public int id;
			@XmlAttribute
			/** 达到的数量 */
			public int num;
			@XmlAttribute
			/** 需要的等级*/
			public int lv;
			@XmlAttribute
			public String atb;
		}
	}
	
	public static class SustainKills{
		public List<SustainKill> sustainKill;
		public static class SustainKill{
			@XmlAttribute
			public int killNum;
			@XmlAttribute
			public int buffId;
		}
	}
	
	public static class MonthCard{
		@XmlAttribute
		public String awards;
	}
	
	public static class FirstRecharge{
		@XmlAttribute
		public String awards;
	}
	
	public static class FbBindAward{
		@XmlAttribute
		public String awards;
	}
	
	public static class PlayTimes{
		public List<PlayItem> playItem;
		public static class PlayItem{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int timesType;
			@XmlAttribute
			public int initialTimes;
			@XmlAttribute
			public int  buyConsumId;
			@XmlAttribute
			public int buyTimes;
		}
	}
	
	public static class ReviveCosts{
		public List<ReviveCost> reviveCost;
		public static class ReviveCost{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int extraMinTimes;
			@XmlAttribute
			public int extraMaxTimes;
			@XmlAttribute
			public int  buyPrice;
			@XmlAttribute
			public int buyDiamond;
			
		}
	}
	
	public static class Worship{
		public List<WorshipItem> worshipItem;
		public static class WorshipItem{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int gold;
			@XmlAttribute
			public int diamond;
			@XmlAttribute
			public String awards;
		}
	}
	
	public static class GrowFunds{
		public List<GrowFund> growFund;
		public static class GrowFund{
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int moneyType;
			@XmlAttribute
			public int onceAward;
			@XmlAttribute
			public String award;
		}
	}
	
	public static class DiamondBasins{
		@XmlAttribute
		public int validTime;
		public List<DiamondBasin> diamondBasin;
		public static class DiamondBasin{
			@XmlAttribute
			public int times;
			@XmlAttribute
			public int diamond;
			@XmlAttribute
			public int needVipExp;
			@XmlAttribute
			public String value;
		}
	}
	
}


