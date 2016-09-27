package com.games.mmo.mapserver.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.text.ChangedCharSetException;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.skilleffect.BaseSkillEffect;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.mapserver.vo.BufferEffetVo;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.po.game.PetUpgradePo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.service.RoleService;
import com.games.mmo.type.BuffModelType;
import com.games.mmo.type.BuffType;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.EffectType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.LivingEventType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.UsuallyType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.PetConstellVo;
import com.games.mmo.vo.RankVo;
import com.games.mmo.vo.TimerBossVo;
import com.games.mmo.vo.global.MonsterVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.AccumulativeTime.OnlineTime;
import com.games.mmo.vo.xml.ConstantFile.PVP.DropProbability;
import com.games.mmo.vo.xml.ConstantFile.SustainKills.SustainKill;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.event.EventArg;
import com.storm.lib.type.NetType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.util.ThreadLocalUtil;
import com.storm.lib.vo.IdLongVo2;
import com.storm.lib.vo.IdNumberVo;

public class Fighter extends Entity{
	/**
	 * 白名
	 */
	public static final int PK_STATUS_PEACE = 0;
	/**
	 * 灰名
	 */
	public static final int PK_STATUS_GREY = 1;
	/**
	 * 红名
	 */
	public static final int PK_STATUS_RED = 2;
	
	/**
	 * 是否是机器人
	 */
	public boolean robot = false;
	
	/**
	 * 机器人类型
	 */
	public Integer robotType = 0;
	
	/**
	 * 机器人死亡刷新时间
	 */
	public Long robotDieFlushTime = 0l;
	
	public String name=null;

	/**
	 * 物攻：影响物理攻击时输出
	 */
	public Integer batMeleeAttackMin=0;
	/**
	 * 法攻：影响法术攻击时输出
	 */
	public Integer batMagicAttackMin=0;
	/**
	 * 物防：影响物理攻击对自身的伤害
	 */
	public Integer batMeleeDefenceMin=0;
	/**
	 * 法防：影响法术攻击队自身的伤害
	 */
	public Integer batMagicDefenceMin=0;
	
	/**
	 * 物攻：影响物理攻击时输出
	 */
	public Integer batMeleeAttackMax=0;
	/**
	 * 法攻：影响法术攻击时输出
	 */
	public Integer batMagicAttackMax=0;
	/**
	 * 物防：影响物理攻击对自身的伤害
	 */
	public Integer batMeleeDefenceMax=0;
	/**
	 * 法防：影响法术攻击队自身的伤害
	 */
	public Integer batMagicDefenceMax=0;
	/**
	 * 生命：生命值为0时角色死亡
	 */
	private Integer batHp=0;
	/**
	 * 法力：法力值影响技能释放
	 */
	public Integer batMp=0;
	/**
	 * 暴击：暴击时伤害翻倍，取值范围[0~1]
	 */
	public Integer batCritical=0;
	/**
	 * 生命恢复：每5秒生命恢复的值
	 */
	public Integer batHpReg=0;
	/**
	 * 法力恢复：每5秒法力恢复的值
	 */
	public Integer batMpReg=0;
	/**
	 * 移动速度：每秒钟移动的像素数
	 */
	public Integer batMovement=0;
	
	/**
	 * 闪避
	 */
	public Integer batDodge=0;
	
	public Integer batMaxHp=0;
	
	public Integer batMaxMp=0;
	
	public Integer batLuckyAttack=0;
	public Integer batHitRate=0;
	public Integer batMeleeDefence=0;
	public Integer batMagicDefence=0;
	public Integer batLuckyDefence=0;
	public Integer batTough=0;
	
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
	
	
	public Integer batMeleeAttack = 0;
	
	public Integer batMagicAttack = 0;
	
	/**
	 * 命中率(比率非数值,说明一下以前的命中属性所用的字段名用成了batHitRate了，所以这里命中率字段进行再加个Rate处理)
	 */
	public Integer batHitRateRate=0;
	
	/**
	 * 闪避率(比率非数值)
	 */
	public Integer batDodgeRate=0;
	
	/**
	 * 暴击增加率(比率非数值)
	 */
	public Integer batAddCriticalRate=0;
	
	/**
	 * 暴击减免率(比率非数值)
	 */
	public Integer batDerateCriticalRate=0;
	
	/**
	 * 反弹伤害百分比
	 */
	public Integer batReboundDamage=0;
	
	public RolePo rolePo;
	public MonsterPo monsterPo;
	public Integer monsterFreshId=null;
	public String modelAvatar="0";
	public String wingAvatar="0";
	public String weaponAvatar="0";	
	public Integer pkStatus=0;
	public Integer lv=1;
	public Integer tag=0;
	/**
	 * 生物生效中的buff效果
	 */
	public CopyOnWriteArrayList<BufferStatusVo> bufferStatusVos = new CopyOnWriteArrayList<BufferStatusVo>();
	public RolePo master;
	
	private Integer career=0;

	public Fighter agentPlayer=null;
	
	public Map<Integer, Date> beAttacked;//玩家被攻击记录
	
	/** 连斩数 */
	public int killNum = 0;
	/** 公会名称 */
	public String guildName = "";
	/** 公会职务 */
	public int guildPosition =0;
	/** 总伤害值 */
	public int sumDamage = 0;
	/** 当前普通称号 */
	public int currentTitleLv = 0;
	/** 当前特殊称号*/
	public int currentSpecialTitleLv=0;
	/** 客户端显示称号*/
	public int nowTitleLv=0;
//	/** 获得积分 */
//	private int score = 0;
	/** 当前击杀数 */
	public int nowKill = 0;
	/** 魔化后的模型ID */
	public int buffAvatar = 0;
	/** 变身状态（0没变身1变身中） */
	public int avatarStatus = 0;
	public int pointX=0;//出生点X
	public int pointY=0;//出生点Y
	public int pointZ=0;//出生点Z
	public int traceRange=0;//追踪距离
	/** 是否是运镖车 */
	public int wasYunDart=0;
	/** 阵营 */
	public int militaryForces=0;
	/** 总战力*/
	public int battlePower=0;
	/** 动态宠物Id*/
	public int rpetId=0;
//	/** 是否占领那鲁亚土城*/
	public int wasSiegeBid=0;
	
	public int currentMilitaryRankId = 0;
	
	// 技能列表
	public List<Integer> listSkillIds = new ArrayList<Integer>();
	
	public String skillString="0";
	/**
	 * 生物buff(生物身上可触发的buff)
	 */
	public CopyOnWriteArrayList<BuffPo> livingBuffers = new CopyOnWriteArrayList<BuffPo>();
	
	public long lastHitTime=0l;
	
	public CopyOnWriteArrayList<Fighter> fightSkillOnMe=new CopyOnWriteArrayList<Fighter>();
	
	public ConcurrentHashMap<RolePo,Integer> roleHitTotalCount =new ConcurrentHashMap<RolePo, Integer>();
	
	/**
	 * 25宫格
	 */
	public ConcurrentHashMap<Integer, Integer> farSeeingPlayeFighters = new ConcurrentHashMap<Integer, Integer>();
	/**
	 * 25宫格
	 */
	public ConcurrentHashMap<Integer, Integer> farBeSeeingPlayeFighters = new ConcurrentHashMap<Integer, Integer>();
	
	/**
	 * 9宫格
	 */
	public ConcurrentHashMap<Integer, Integer> nearSeeingPlayeFighters = new ConcurrentHashMap<Integer, Integer>();
	/**
	 * 9宫格
	 */
	public ConcurrentHashMap<Integer, Integer> nearBeSeeingPlayeFighters = new ConcurrentHashMap<Integer, Integer>();
	
	/**D
	 * 写死的，怪物保命技能检测重发机制
	 */
	public Long lastSkillSaveMeTime = 0l;



	public synchronized Integer getBatHp() {
		return batHp;
	}
	public synchronized void setBatHp(Integer batHp) {
		this.batHp = batHp;
	}
	public void checkResetHitStatus(){
		if(lastHitTime!=0l && (System.currentTimeMillis()-lastHitTime)>=60*1000){
			lastHitTime=0l;
			roleHitTotalCount.clear();
			fightSkillOnMe.clear();
			if(batMaxHp>batHp){
				doHpChange(batMaxHp-batHp,1,0,this,true);
			}
		}
	}
	public static Fighter create(MonsterPo monsterPo,MapRoom stage,Integer monsterFreshId,MonsterFreshPo monsterFreshPo) {
		Fighter mover =new Fighter();
		mover.type=Entity.MOVER_TYPE_MONSTER;
		mover.itemId=monsterPo.getId();
		mover.mapUniqId=stage.entityIndex++;
		stage.fighterIdMaps.put(mover.mapUniqId, mover);
		mover.loadBatFromMonsterPo(monsterPo);
		mover.batHp=mover.batMaxHp;
		mover.batMp=mover.batMaxMp;
		mover.lv=monsterPo.getLv();
		mover.modelAvatar=monsterPo.getModel().toString();
		mover.name=monsterPo.getName();
		mover.monsterFreshId=monsterFreshId;
		mover.traceRange=(monsterPo.getTraceRange()+10)*Entity.BASE_NUMBER;
		if(monsterFreshPo!= null){
			mover.tag=monsterFreshPo.getTag();		
//			mover.type=monsterPo.getMonsterType();
		}
		if(monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE7){
			FlagPo flagPo = FlagPo.findFlagBySceneId(stage.sceneId);
			if(flagPo.getGuildId()!=null){
				GuildPo guildPo = GuildPo.findEntity(flagPo.getGuildId());
				if(guildPo!=null){
					mover.guildName=GuildPo.findEntity(flagPo.getGuildId()).getName();
				}
				else{
					mover.guildName=GlobalCache.fetchLanguageMap("key2276");
				}
			}
		}
		if(monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE10){
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyMonster);
			List<MonsterVo> list = ((List<MonsterVo>)gp.valueObj);
			for(MonsterVo mv : list){
				if(mv.id == monsterPo.getId().intValue()){
					mover.militaryForces = mv.militaryForces;
					break;
				}
			}
		}
//		stage.cellData.addLiving(mover);
//		mover.mapRoom=stage;
		mover.change();
		return mover;
	}
	
	/**
	 * 创建镖车
	 * @param monsterPo
	 * @param stage
	 * @param monsterFreshId
	 * @param monsterFreshPo
	 * @param rolePo
	 * @return
	 */
	public static  Fighter create(MonsterPo monsterPo,MapRoom stage,Integer monsterFreshId,MonsterFreshPo monsterFreshPo,RolePo rolePo) {
		Fighter mover = create(monsterPo, stage, monsterFreshId, monsterFreshPo);
		mover.master = rolePo;
		mover.wasYunDart =1;
		return mover;
	}
	
	public void loadBatFromRpetPo(RolePo rolePo, RpetPo rpetPo) {
//		System.out.println(" loadBatFromRpetPo() rolePo.name = " + rolePo.getName() + "; rpetPo.name = " +rpetPo.getName());
		//宠物技能BUFF初始化
		List<List<List<Integer>>> batExpress = new ArrayList<List<List<Integer>>>();
		
		for(BuffPo buffPo : livingBuffers){
			removeBuffer(buffPo.getId());
		}
		
		livingBuffers.clear();
		for(Integer skillId:rpetPo.skillIds)
		{
			PetSkillPo petSkillPo = PetSkillPo.findEntity(skillId);
			if(petSkillPo != null)
			{
				for(Integer buffId:petSkillPo.buffIdList)
					addSkillBuffer(buffId);
			}
		}
		
		
		
		/*************************************宠物常规属性初始化*****************************************************/
		//宠物星座属性加成
		Collection<List<PetConstellVo>> cols = rolePo.petConstellMap.values();
		List<List<Integer>> petConstellAttack = new ArrayList<List<Integer>>();
		for(List<PetConstellVo> constells:cols)
		{
			for(PetConstellVo constell:constells)
			{
				List<IdNumberVo> attachs = constell.attachs;
				for(IdNumberVo attach:attachs)
				{
					List<Integer> constellBat = new ArrayList<Integer>();
					constellBat.add(attach.getId());
					constellBat.add(attach.getNum());
					petConstellAttack.add(constellBat);
				}
			}
		}
		if(petConstellAttack.size() > 0)
			batExpress.add(petConstellAttack);
		//宠物天赋属性加成
		List<List<Integer>> petTalentAttack = rolePo.loadPetTalentAttackPet();
		if(null != petTalentAttack && petTalentAttack.size() > 0)
			batExpress.add(petTalentAttack);
		

		
		//宠物原始属性*potential/100;
		PetUpgradePo petUpgradePo = PetUpgradePo.findEntity(rpetPo.getLv());
		PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
		int potential = petPo.getPotential();
		List<List<Integer>> batList = new ArrayList<List<Integer>>();
		batList = ExpressUtil.buildBattleExpressList(petUpgradePo.getBatExp());
		batMeleeAttackMin=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttackMin, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeAttackMin, batExpress);
		/**
		 * 法攻：影响法术攻击时输出
		 */
		batMagicAttackMin=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttackMin, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicAttackMin, batExpress);
		/**
		 * 物防：影响物理攻击对自身的伤害
		 */
		batMeleeDefenceMin=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefenceMin, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeDefenceMin, batExpress);
		/**
		 * 法防：影响法术攻击队自身的伤害
		 */
		batMagicDefenceMin=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefenceMin, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicDefenceMin, batExpress);
		
		/**
		 * 物攻：影响物理攻击时输出
		 */
		batMeleeAttackMax=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttackMax,batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeAttackMax, batExpress);
		/**
		 * 法攻：影响法术攻击时输出
		 */
		batMagicAttackMax=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttackMax, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicAttackMax, batExpress);
		/**
		 * 物防：影响物理攻击对自身的伤害
		 */
		batMeleeDefenceMax=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefenceMax, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeDefenceMax, batExpress);
		/**
		 * 法防：影响法术攻击队自身的伤害
		 */
		batMagicDefenceMax=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefenceMax,batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicDefenceMax, batExpress);

		/**
		 * 暴击：暴击时伤害翻倍，取值范围[0~1]
		 */
		batCritical=ExpressUtil.findExpressEffectType2(RoleType.batCritical, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batCritical, batExpress);
		/**
		 * 生命恢复：每5秒生命恢复的值
		 */
		batHpReg=ExpressUtil.findExpressEffectType2(RoleType.batHpReg, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batHpReg, batExpress);
		/**
		 * 法力恢复：每5秒法力恢复的值
		 */
		batMpReg=ExpressUtil.findExpressEffectType2(RoleType.batMpReg, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMpReg, batExpress);
		/**
		 * 移动速度：每秒钟移动的像素数
		 */
		batMovement=ExpressUtil.findExpressEffectType2(RoleType.batMovement, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMovement, batExpress);
		
		/**
		 * 闪避
		 */
		batDodge=ExpressUtil.findExpressEffectType2(RoleType.batDodge, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batDodge, batExpress);
		
		/**
		 * 生命：生命值为0时角色死亡
		 */
		changeBatMaxHp(ExpressUtil.findExpressEffectType2(RoleType.batMaxHp, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMaxHp, batExpress));
		/**
		 * 法力：法力值影响技能释放
		 */
		changeBatMaxMp(ExpressUtil.findExpressEffectType2(RoleType.batMaxMp, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMaxMp, batExpress));	
		batLuckyAttack=ExpressUtil.findExpressEffectType2(RoleType.batLuckyAttack, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batLuckyAttack, batExpress);
		batHitRate=ExpressUtil.findExpressEffectType2(RoleType.batHitRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batHitRate, batExpress);
		batMeleeDefence=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefence, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeDefence, batExpress);
		batMagicDefence=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefence, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicDefence, batExpress);
		batLuckyDefence=ExpressUtil.findExpressEffectType2(RoleType.batLuckyDefence, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batLuckyDefence, batExpress);
		batTough=ExpressUtil.findExpressEffectType2(RoleType.batTough, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batTough, batExpress);
		batDamageRate=ExpressUtil.findExpressEffectType2(RoleType.batDamageRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batDamageRate, batExpress);
		batDamageResistRate=ExpressUtil.findExpressEffectType2(RoleType.batDamageResistRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batDamageResistRate, batExpress);
		batCriticalDamageRate=ExpressUtil.findExpressEffectType2(RoleType.batCriticalDamageRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batCriticalDamageRate, batExpress);
		batAttackRate=ExpressUtil.findExpressEffectType2(RoleType.batAttackRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batAttackRate, batExpress);
		batDefenceRate=ExpressUtil.findExpressEffectType2(RoleType.batDefenceRate, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batDefenceRate, batExpress);
		batMeleeAttack=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttack, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMeleeAttack, batExpress);
		batMagicAttack=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttack, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batMagicAttack, batExpress);
		batReboundDamage = ExpressUtil.findExpressEffectType2(RoleType.batReboundDamage, batList)*potential/100
				+ExpressUtil.findExpressEffectType(RoleType.batReboundDamage, batExpress);
//		System.out.println(" 加载  batReboundDamage = " +batReboundDamage);
		
		for(BuffPo buffPo : livingBuffers){
			if(buffPo.bufferEffetVos.get(0).buffType == BuffType.BUFF_EFFECT_9){
				makeAddBuff(new BufferStatusVo(buffPo, this, new ArrayList<Fighter>()), this, true);				
			}
		}
//		System.out.println(" 22加载  batReboundDamage = " +batReboundDamage);
	}
	
	private void loadBatFromMonsterPo(MonsterPo monsterPo) {
		//怪物BUFF初始化
		List<Integer> buffIds = monsterPo.buffIds;
		for(Integer buffId:buffIds)
		{
			addSkillBuffer(buffId);
		}
		batMeleeAttackMin=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttackMin, monsterPo.listBatAttrExp);
		/**
		 * 法攻：影响法术攻击时输出
		 */
		batMagicAttackMin=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttackMin, monsterPo.listBatAttrExp);
		/**
		 * 物防：影响物理攻击对自身的伤害
		 */
		batMeleeDefenceMin=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefenceMin, monsterPo.listBatAttrExp);
		/**
		 * 法防：影响法术攻击队自身的伤害
		 */
		batMagicDefenceMin=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefenceMin, monsterPo.listBatAttrExp);
		
		/**
		 * 物攻：影响物理攻击时输出
		 */
		batMeleeAttackMax=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttackMax, monsterPo.listBatAttrExp);
		/**
		 * 法攻：影响法术攻击时输出
		 */
		batMagicAttackMax=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttackMax, monsterPo.listBatAttrExp);
		/**
		 * 物防：影响物理攻击对自身的伤害
		 */
		batMeleeDefenceMax=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefenceMax, monsterPo.listBatAttrExp);
		/**
		 * 法防：影响法术攻击队自身的伤害
		 */
		batMagicDefenceMax=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefenceMax, monsterPo.listBatAttrExp);

		/**
		 * 暴击：暴击时伤害翻倍，取值范围[0~1]
		 */
		batCritical=ExpressUtil.findExpressEffectType2(RoleType.batCritical, monsterPo.listBatAttrExp);
		/**
		 * 生命恢复：每5秒生命恢复的值
		 */
		batHpReg=ExpressUtil.findExpressEffectType2(RoleType.batHpReg, monsterPo.listBatAttrExp);
		/**
		 * 法力恢复：每5秒法力恢复的值
		 */
		batMpReg=ExpressUtil.findExpressEffectType2(RoleType.batMpReg, monsterPo.listBatAttrExp);
		/**
		 * 移动速度：每秒钟移动的像素数
		 */
		batMovement=ExpressUtil.findExpressEffectType2(RoleType.batMovement, monsterPo.listBatAttrExp);
		
		/**
		 * 闪避
		 */
		batDodge=ExpressUtil.findExpressEffectType2(RoleType.batDodge, monsterPo.listBatAttrExp);
		
		if(monsterPo.getBossUsage().intValue() == 2){
			int baseBatMaxHp = ExpressUtil.findExpressEffectType2(RoleType.batMaxHp, monsterPo.listBatAttrExp);
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
			TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
			long noon = DateUtil.getInitialDate(timerBossVo.killTime) + 1000L*60*60*12;
			int killnum = timerBossVo.killNum;
			if( timerBossVo.killTime.longValue() > noon){
				long result = (timerBossVo.killTime.longValue() - noon)/(1000l*60);
				if(result <= 5){
					killnum+=4;
				}
				else if(result <= 8) {
					killnum+=2;
				}
				else if(result <= 12) {
					killnum+=1;
				}
			}
			if(killnum > 0){
				baseBatMaxHp = (int) (baseBatMaxHp*Math.pow(1.2,killnum));				
			}
			changeBatMaxHp(baseBatMaxHp);
		}else{
			changeBatMaxHp(ExpressUtil.findExpressEffectType2(RoleType.batMaxHp, monsterPo.listBatAttrExp));			
		}
		/**
		 * 生命：生命值为0时角色死亡
		 */

		/**
		 * 法力：法力值影响技能释放
		 */
		changeBatMaxMp(ExpressUtil.findExpressEffectType2(RoleType.batMaxMp, monsterPo.listBatAttrExp));
//		batMaxMp=ExpressUtil.findExpressEffectType2(RoleType.batMaxMp, monsterPo.listBatAttrExp);	
		batLuckyAttack=ExpressUtil.findExpressEffectType2(RoleType.batLuckyAttack, monsterPo.listBatAttrExp);
		batHitRate=ExpressUtil.findExpressEffectType2(RoleType.batHitRate, monsterPo.listBatAttrExp);
		batMeleeDefence=ExpressUtil.findExpressEffectType2(RoleType.batMeleeDefence, monsterPo.listBatAttrExp);
		batMagicDefence=ExpressUtil.findExpressEffectType2(RoleType.batMagicDefence, monsterPo.listBatAttrExp);
		batLuckyDefence=ExpressUtil.findExpressEffectType2(RoleType.batLuckyDefence, monsterPo.listBatAttrExp);
		batTough=ExpressUtil.findExpressEffectType2(RoleType.batTough, monsterPo.listBatAttrExp);
		batDamageRate=ExpressUtil.findExpressEffectType2(RoleType.batDamageRate, monsterPo.listBatAttrExp);
		batDamageResistRate=ExpressUtil.findExpressEffectType2(RoleType.batDamageResistRate, monsterPo.listBatAttrExp);
		batCriticalDamageRate=ExpressUtil.findExpressEffectType2(RoleType.batCriticalDamageRate, monsterPo.listBatAttrExp);
		batAttackRate=ExpressUtil.findExpressEffectType2(RoleType.batAttackRate, monsterPo.listBatAttrExp);
		batDefenceRate=ExpressUtil.findExpressEffectType2(RoleType.batDefenceRate, monsterPo.listBatAttrExp);
		batMeleeAttack=ExpressUtil.findExpressEffectType2(RoleType.batMeleeAttack, monsterPo.listBatAttrExp);
		batMagicAttack=ExpressUtil.findExpressEffectType2(RoleType.batMagicAttack, monsterPo.listBatAttrExp);
		this.monsterPo =monsterPo;
		notifyListeners(new EventArg(this, LivingEventType.LOAD.getValue()));//宠物加载事件
	}
	
	public Integer calBattlePower() {
		Integer[] batMeleeArray = fetchBatMeleeArray();
		double[] batMeleeValue = RoleType.batMeleeWarriorOrArcher;
		double currentBattlePower = 0;
		if (batMeleeValue == null) {
			return 0;
		}
		for (int i = 0; i < batMeleeArray.length; i++) {
			if (batMeleeValue[i] != 0) {
				currentBattlePower += (batMeleeArray[i] * batMeleeValue[i]);
			}
		}
		this.battlePower= (int) currentBattlePower;
		return this.battlePower;
	}
	
	public Integer[] fetchBatMeleeArray() {
		Integer[] batMeleeArray = new Integer[] { batMeleeAttack,
				batMeleeDefence, batMagicAttack,
				batMagicDefence, batMeleeAttackMin,
				batMeleeAttackMax, batMeleeDefenceMin,
				batMeleeDefenceMax, batMagicAttackMin,
				batMagicAttackMax, batMagicDefenceMin,
				batMagicDefenceMax, batMaxHp, batMaxMp,
				batHpReg, batMpReg, batMovement,
				batCriticalDamageRate, batLuckyAttack,
				batLuckyDefence, batHitRate, batDodge,
				batCritical, batTough, batDamageRate,
				batDamageResistRate, batAttackRate,
				batDefenceRate };
		return batMeleeArray;
	}
	

	public static Fighter create(RolePo rolePo,MapRoom mapRoom,boolean isRobot) {
		if(mapRoom==null){
			mapRoom=MapWorld.findStage(rolePo.getRoomId());
		}
		Fighter mover =new Fighter();
		mover.type= Entity.MOVER_TYPE_PLAYER;
		mover.mapUniqId=mapRoom.entityIndex++;
		mapRoom.fighterIdMaps.put(mover.mapUniqId, mover);
		mover.rolePo=rolePo;
		mover.itemId=rolePo.getId();
		if(isRobot){
			mover.listSkillIds = rolePo.fetchSkillStr(rolePo.listSkillVos);
			mover.skillString = DBFieldUtil.implod(mover.listSkillIds, "@");
			if(mover.skillString==null){
				mover.skillString="0";
			}
		}

		mover.killNum=rolePo.sustainKillVo.killNum;
		mover.loadBatFromRolePo();
		mover.batHp=rolePo.getBatHp();
		mover.batMp=rolePo.getBatMp();
		mover.batMaxHp=rolePo.getBatMaxHp();
		mover.batMaxMp=rolePo.getBatMaxMp();
		mover.career=rolePo.getCareer();
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
		mover.makeAvatars(commonAvatarVo,false);
		mover.name=rolePo.getName();
		mover.pkStatus=rolePo.getPkStatus();
		mover.robot = rolePo.wasRobot();
		mover.militaryForces=rolePo.getMilitaryForces();
		mover.x = rolePo.getX()/(float)Entity.BASE_NUMBER;
		mover.y = rolePo.getY()/(float)Entity.BASE_NUMBER;
		mover.z = rolePo.getZ()/(float)Entity.BASE_NUMBER;
		mover.aimX = mover.x;
		mover.aimY = mover.y;
		mover.aimZ = mover.z;
		List<BufferStatusVo> bufferStatusVoList = new ArrayList<BufferStatusVo>();
//		System.out.println(rolePo.getName()+" create() rolePo.listBufferStatus = "+rolePo.listBufferStatus);
		for (List<String> vals : rolePo.listBufferStatus) {
			Integer buffId = Integer.valueOf(vals.get(0));
			Long endTime = Long.valueOf(vals.get(1));
			Integer life = Integer.valueOf(vals.get(2));
			Long lifeTime = Long.valueOf(vals.get(3));
			BuffPo buffPo = BuffPo.findEntity(buffId);
			List<Fighter> fighterList = new ArrayList<Fighter>();
			BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, mover, fighterList);
			bufferStatusVo.buffPo=buffPo;
			bufferStatusVo.endTime = endTime;
			bufferStatusVo.life=life;
			bufferStatusVo.lifeTime=lifeTime;
			bufferStatusVoList.add(bufferStatusVo);
		}
		for(BufferStatusVo bsv : bufferStatusVoList){
			mover.makeAddBuff(bsv, mover , false);
		}
		mover.beAttacked = new ConcurrentHashMap<Integer, Date>();
		if(rolePo.getGuildId()!=null){
			GuildPo guildPo=GuildPo.findEntity(rolePo.getGuildId());
			if(guildPo!=null){
				mover.changeGuildName(guildPo.getName());
			}
		}

		mover.changeGuildPosition();
		mover.changeWasSiegeBid();
		mover.currentTitleLv = rolePo.getTitleLv();
		mover.currentSpecialTitleLv = rolePo.getCurrentSpecialTitleLv();
		mover.nowTitleLv=rolePo.nowTitleLv;
		mover.currentMilitaryRankId=rolePo.getCurrentMilitaryRankId();
		mover.change();
		return mover;
	}
	
	
	public static Fighter create(RolePo rolePo, RpetPo rpetPo, MapRoom stage) {
			Fighter mover =new Fighter();
			mover.itemId=rpetPo.petPo().getId();
			mover.type=Entity.MOVER_TYPE_PET;
			mover.loadBatFromRpetPo(rolePo, rpetPo);
			mover.batHp=mover.batMaxHp;
			mover.batMp=mover.batMaxMp;
			mover.lv=rpetPo.getLv()/4;
			mover.modelAvatar=rpetPo.petPo().getModel();
			mover.name=rpetPo.getName();
			mover.mapUniqId=stage.entityIndex++;
			stage.fighterIdMaps.put(mover.mapUniqId, mover);
			mover.militaryForces=rolePo.getMilitaryForces();
			mover.rolePo=rolePo;
			mover.rpetId=rpetPo.getId();
			mover.change();
			rolePo.setPetPrower(mover.calBattlePower());
			return mover;
	}
	
	public void changeX(float x){
		this.x=x;
		change();
	}
	
	public void changeY(float y){
		this.y=y;
		change();
	}
	
	public void changeZ(float z){
		this.z=z;
		change();
	}
	

	private void change() {
		moverStatusChanged=true;
		moverStatusChangedNoHead=true;
		fighterStatusChangedNoHead=true;
	}
	
	public  void makeAvatars(CommonAvatarVo commonAvatarVo,boolean requireBroad) {
		
		changeWeaponAvatar(commonAvatarVo.weaponAvatar);
		changeModelAvatar(commonAvatarVo.modelAvatar);
		changeWingAvatar(commonAvatarVo.wingAvatar);	
//		weaponAvatar=commonAvatarVo.weaponAvatar;
//		modelAvatar=commonAvatarVo.modelAvatar;
//		wingAvatar=commonAvatarVo.wingAvatar;
//		PrintUtil.print("modelAvatar: "+commonAvatarVo.modelAvatar+"; wingAvatar: "+commonAvatarVo.wingAvatar+"; weaponAvatar: "+commonAvatarVo.weaponAvatar);
		if(requireBroad){
			BattleMsgUtil.abroadAvatarInfor(this);
		}
	}

	public void loadBatFromRolePo() {
		if(rolePo!=null){
			if(rolePo.bufferStatusVos.size() > 0)
			{
				//加载连斩BUFF(不加载属性类型BUFF)
				bufferStatusVos.addAll(rolePo.bufferStatusVos);
				//加载连斩BUFF(不加载属性类型BUFF)
//				BuffPo buffPo = rolePo.sustainKillBuff();
//				if(buffPo != null && !EffectType.VALUE.equals(buffPo.bufferEffetVos.get(0).effectType))
//					addSkillBuffer(buffPo.getId(), BuffModelType.KILL_NUM);
			}
			
			changeLv(rolePo.getLv());
			batMeleeAttackMin=rolePo.getBatMeleeAttackMin();
			/**
			 * 法攻：影响法术攻击时输出
			 */
			batMagicAttackMin=rolePo.getBatMagicAttackMin();
			/**
			 * 物防：影响物理攻击对自身的伤害
			 */
			batMeleeDefenceMin=rolePo.getBatMeleeDefenceMin();
			/**
			 * 法防：影响法术攻击队自身的伤害
			 */
			batMagicDefenceMin=rolePo.getBatMagicDefenceMin();
			
			/**
			 * 物攻：影响物理攻击时输出
			 */
			batMeleeAttackMax=rolePo.getBatMeleeAttackMax();
			/**
			 * 法攻：影响法术攻击时输出
			 */
			batMagicAttackMax=rolePo.getBatMagicAttackMax();
			/**
			 * 物防：影响物理攻击对自身的伤害
			 */
			batMeleeDefenceMax=rolePo.getBatMeleeDefenceMax();
			/**
			 * 法防：影响法术攻击队自身的伤害
			 */
			batMagicDefenceMax=rolePo.getBatMagicDefenceMax();

			/**
			 * 暴击：暴击时伤害翻倍，取值范围[0~1]
			 */
			batCritical=rolePo.getBatCritical();
			/**
			 * 生命恢复：每5秒生命恢复的值
			 */
			batHpReg=rolePo.getBatHpReg();
			/**
			 * 法力恢复：每5秒法力恢复的值
			 */
			batMpReg=rolePo.getBatMpReg();
			/**
			 * 移动速度：每秒钟移动的像素数
			 */
			batMovement=rolePo.getBatMovement();
			
			/**
			 * 闪避
			 */
			batDodge=rolePo.getBatDodge();
			
			changeBatMaxHp(rolePo.getBatMaxHp());
			
			changeBatMaxMp(rolePo.getBatMaxMp());
			/**
			 * 生命：生命值为0时角色死亡
			 */
//			batMaxHp=rolePo.getBatMaxHp();
			/**
			 * 法力：法力值影响技能释放
			 */
//			batMaxMp=rolePo.getBatMaxMp();	
			
			batLuckyAttack=rolePo.getBatLuckyAttack();
			batHitRate=rolePo.getBatHitRate();
			batMeleeDefence=rolePo.getBatMeleeDefence();
			batMagicDefence=rolePo.getBatMagicDefence();
			batLuckyDefence=rolePo.getBatLuckyDefence();
			batTough=rolePo.getBatTough();
			
			
			batDamageRate=rolePo.getBatDamageRate();
			batDamageResistRate=rolePo.getBatDamageResistRate();
			batCriticalDamageRate=rolePo.getBatCriticalDamageRate();
			batAttackRate=rolePo.getBatAttackRate();
			batDefenceRate=rolePo.getBatDefenceRate();
			
			batMeleeAttack=rolePo.getBatMeleeAttack();
			batMagicAttack=rolePo.getBatMagicAttack();
			
			batReboundDamage = rolePo.getBatReboundDamage();
			
			notifyListeners(new EventArg(this, LivingEventType.LOAD.getValue()));//宠物加载事件
		}

	}


	
	public synchronized int makeHpChange(Fighter damager,int hpChange) {
//		PrintUtil.print("makeHpChange() damager.name = " +damager.name +"; hpChange="+hpChange);
		if(hpChange<0){
			//System.out.println(name+":当前 bufferSize:"+bufferStatusVos.size());
			int DAMAGE_PERCENTAGE_REDUCE_VAL = findBuffValueByEffectType(BuffType.BUFF_EFFECT_1);
			//System.out.println("减伤百分比参数:"+DAMAGE_PERCENTAGE_REDUCE_VAL);
			hpChange= DoubleUtil.toUpInt(1d*hpChange*(100-DAMAGE_PERCENTAGE_REDUCE_VAL)/100d);
			hpChange= Math.min(-1, hpChange);
			if(damager!=null && damager.rolePo!=null &&hpChange<0 && type==Fighter.MOVER_TYPE_MONSTER){
				if(!roleHitTotalCount.containsKey(damager.rolePo)){
					if(roleHitTotalCount.size()<=20){
						roleHitTotalCount.put(damager.rolePo, Math.abs(hpChange));
					}
					lastHitTime=System.currentTimeMillis();
				}
				else{
					if(roleHitTotalCount.size()<=20){
						roleHitTotalCount.put(damager.rolePo, roleHitTotalCount.get(damager.rolePo)+Math.abs(hpChange));
					}
					lastHitTime=System.currentTimeMillis();
				}
			}
		}
		//毒死
		if(damager==null && (batHp+hpChange)<=0){
			hpChange=0;
		}
		int originalHp=batHp;
		int tempBatHp=batHp;
		tempBatHp+=hpChange;
		tempBatHp=Math.max(0, tempBatHp);
		tempBatHp=Math.min(tempBatHp,batMaxHp);
		changeBatHp(tempBatHp);
		if(batHp<=0){
			boolean bool = checkTouchBuff(damager, 3, false, batHp, 1);
			if(bool){
//				PrintUtil.print("------------_++++-----------die:"+this.name);
				mapRoom.doMonsterDie(damager,this);				
			}
		}
		hpChange=batHp-originalHp;
		if(hpChange < 0 ){
			if(damager!=null){
				damager.sumDamage+=(-hpChange);
			}
		}
		return hpChange;
	}
	


	public int makeMpChange(Fighter findFighter, int hpChange) {
		int originalMp=batMp;
		int tempMp=batMp;
		tempMp+=hpChange;
		tempMp=Math.max(0, tempMp);
		tempMp=Math.min(tempMp,batMaxMp);
		changeBatMp(tempMp);
		hpChange=tempMp-originalMp;

		return hpChange;

	}
	
	/**
	 * 根据类型获取百分比加成
	 * @param buffType
	 * @return
	 */
	public int findBuffValueByEffectType(int buffType) {
		if(bufferStatusVos.size()>0){
			for (BufferStatusVo bufferStatusVo : bufferStatusVos) {
				for (BufferEffetVo bufferEffetVo : bufferStatusVo.buffPo.bufferEffetVos) {
					if(bufferEffetVo.buffType==buffType){
						return Integer.valueOf(bufferEffetVo.param1);
					}
				}
			}
		}

		return 0;
	}

	public void doMpChange(int change,boolean flush) {
		int val = makeMpChange(this, change);
		BattleMsgUtil.singleAbroadMpChange(this,val,flush);
	}
	
	public void doHpChange(Integer change,Integer times,int showNum,Fighter caster,boolean flush) {
		Integer val = makeHpChange(this, change);
		BattleMsgUtil.abroadHpChange(this,val,times,showNum,false,caster,flush);
	}
	
	private double getRange(Fighter fighter) {
		double _x = Math.abs(x - fighter.x);
		double _y = Math.abs(y - fighter.y);
		double range = Math.sqrt(_x*_x+_y*_y);
		return range;
	}
	
	public Double findNearEnemyRange() {
		Double minRange=null;
		Collection<Entity> livings = cell.getAllLivings();
		for (Entity living : livings) {
			if(living instanceof Fighter)
			{
				double range =getRange((Fighter)living);
				if(minRange==null)
					minRange=range;
				minRange=Math.min(minRange,range);
			}
		}
		return minRange;
	}

	public Fighter findNearSingleFriendByRange(Integer avaRange,Integer limit) {
		Fighter nearistFriend =null;
		Double minRange=null;
		for (Entity entity : mapRoom.fetchAllMoves(this,limit)) {
			if(entity instanceof Fighter){
				Fighter target = (Fighter) entity;
				double range =getRange(target);
				if(minRange==null){
					minRange=range;
				}
				if(range<=avaRange){
					if(range<=minRange){
						minRange=range;
						nearistFriend=target;
					}
				}
			}
		}
		return nearistFriend;
	}

	public Fighter findNearSingleEnemyByRange(Integer avaRange,Integer limit) {
		Fighter nearistEnemy =null;
		Double minRange=null;
		for (Entity entity : mapRoom.fetchAllMoves(this,limit)) {
			if(entity instanceof Fighter){
				Fighter target = (Fighter) entity;

				double range =getRange(target);
				if(minRange==null){
					minRange=range;
				}
				if(range<=avaRange){
					if(range<=minRange){
						minRange=range;
						nearistEnemy=target;
					}
				}
			}
		}
		return nearistEnemy;
	}

	public List<Fighter> findNearAllFriendsByRange(Integer avaRange,Integer limit) {
		List<Fighter> fighters  = new ArrayList<Fighter>();
		for (Entity entity : mapRoom.fetchAllMoves(this,limit)) {
			if(entity instanceof Fighter){
				Fighter target = (Fighter) entity;
				double range =getRange(target);
				if(range<=avaRange){
					fighters.add(target);
				}
			}
		}
		return fighters;
	}

	public List<Fighter> findNearAllEnemysByRange(Integer avaRange,Integer limit) {
		List<Fighter> fighters  = new ArrayList<Fighter>();
		for (Entity entity : mapRoom.fetchAllMoves(this,limit)) {
			if(entity instanceof Fighter){
				Fighter target = (Fighter) entity;
				double range =getRange(target);
				if(range<=avaRange){
					if(type.intValue()==Entity.MOVER_TYPE_MONSTER && target.type.intValue()==Entity.MOVER_TYPE_MONSTER){
						continue;
					}
					fighters.add(target);
				}
			}
		}
		return fighters;
	}

	public Boolean pickTreasure(Treasure treasure) {
		if(type.intValue()==Entity.MOVER_TYPE_PLAYER){
			RolePo rolePo = RolePo.findEntity(itemId);
			if(rolePo!=null){
				mapRoom.doPickTreasureFromStage(treasure,this);
				return rolePo.pickTreasure(treasure);
			}
		}
		return false;
	}

	/**
	 * 
	 * 方法功能:实施技能效果
	 * 更新时间:2014-10-10, 作者:johnny
	 * @param targetEntityIds
	 */
	public void makeSkillEffect(Integer skillId,Integer selectEntityId, String targetEntityIds,Integer attractIndex,List<Integer> entityIds,List<Fighter> abroadPlayers) {
		
//		PrintUtil.print("makeSkillEffect() name = "+ name +"; skillId =" + skillId + "; livingBuffers = " + livingBuffers);
		//TODO 可以Cache啊
		SkillPo skillPo = SkillPo.findEntity(skillId);
		if(skillPo.getSubSkillId()!=0){
//			System.out.println(" ===================return");
			return;
		}
		if(entityIds==null){
			entityIds = StringUtil.getListByStr(targetEntityIds,"@");
		}

		List<Fighter> receiveFighters = new ArrayList<Fighter>();
		for (Integer targetEntityId : entityIds) {
			if(targetEntityId==0){

			}
			else{
				Fighter target = mapRoom.findMoverId(targetEntityId);
				if(target==null){
					continue;
				}
				receiveFighters.add(target);
//				System.out.println("makeSkillEffect() skillId = "+skillId+" 攻this.name="+this.name +";  受target.name="+target.name);
				try {
//					Class clazz = BaseSkillEffect.class; 
					if(skillPo.getSkillType().intValue()==1){
						BaseSkillEffect.baseSkill(skillId, this, target);
					}
					else if(skillPo.getSkillType().intValue()==2){
						BaseSkillEffect.buffSkill(skillId, this, target);
					}
					else if(skillPo.getSkillType().intValue()==3){
						if(skillId==120008){
							BaseSkillEffect.SKILL120008(this, target);
						}
						else if(skillId==110006){
							BaseSkillEffect.SKILL110006(this, target);
						}
						else if(skillId==130005){
							BaseSkillEffect.SKILL130005(this, target);
						}
						else if(skillId==151001){
							BaseSkillEffect.SKILL151001(this, target);
						}
						else if(skillId==151002){
							BaseSkillEffect.SKILL151002(this, target);
						}
						else if(skillId==151003){
							BaseSkillEffect.SKILL151003(this, target);
						}
						else if(skillId==151004){
							BaseSkillEffect.SKILL151004(this, target);
						}
						else if(skillId==151005){
							BaseSkillEffect.SKILL151005(this, target);
						}
						else if(skillId==151006){
							BaseSkillEffect.SKILL151006(this, target);
						}
						else if(skillId==153001){
							BaseSkillEffect.SKILL153001(this, target);
						}
						else if(skillId==153002){
							BaseSkillEffect.SKILL153002(this, target);
						}
						else if(skillId==153003){
							BaseSkillEffect.SKILL153003(this, target);
						}
						else if(skillId==153004){
							BaseSkillEffect.SKILL153004(this, target);
						}
						else{
							ExceptionUtil.throwConfirmParamException("这个坑爹的技能不存在:"+skillId);
						}
//					     try {
//					    	java.lang.reflect.Method m1 = clazz.getDeclaredMethod("SKILL"+skillId,new Class[]{Fighter.class,Fighter.class});
//							m1.invoke(null, new Object[]{this,target});
//						} catch (Exception e1) {
//							ExceptionUtil.processException(e1);
//						}
					}
				} catch (Exception e) {
					ExceptionUtil.processException(e);
				}
			}

		}
	}
	

	/**
	 * 根据buffId查找buff
	 * @param buffId
	 * @return
	 */
	public BuffPo fetchLivingBuffers(int buffId){
		for(BuffPo buffPo : livingBuffers){
			if(buffPo.getId().intValue() == buffId){
				return buffPo;
			}
		}
		return null;
	}
	
	/**
	 * 添加新BUFF
	 * @param buffId
	 */
	private void addSkillBuffer(int buffId)
	{
		BuffPo buffPo =BuffPo.findEntity(buffId);
		livingBuffers.add(buffPo);
	}
	
	public Integer callReadLv(){
		return lv;
	}
	
	public Integer callReadMeleeMinAttack(){
		return batMeleeAttackMin;
	}
	
	public Integer callReadMeleeMaxAttack(){
		return batMeleeAttackMax;
	}
	
	public Integer callReadMagicMinAttack(){
		return batMagicAttackMin;
	}
	
	public Integer callReadMagicMaxAttack(){
		return batMagicAttackMax;
	}
	
	public Integer callMax(Integer a,Integer b){
		return Math.max(a, b);
	}
	
	public Integer callMin(Integer a,Integer b){
		return Math.min(a, b);
	}
	
	/**
	 * 
	 * 方法功能:脚本-获得物理攻击
	 * 更新时间:2014-10-29, 作者:johnny
	 * @return
	 */
	public Integer callReadMeleeAttack(){
		int minAttack = 0;
		int maxAttack = 0;
		if(this.rolePo==null){
			minAttack=(int)((this.batMeleeAttack+this.batMeleeAttackMin)*(1+this.batAttackRate/1000d));
			maxAttack=(int)((this.batMeleeAttack+this.batMeleeAttackMax)*(1+this.batAttackRate/1000d));
		}else{
			minAttack=(int)(this.batMeleeAttack+this.batMeleeAttackMin);
			maxAttack=(int)(this.batMeleeAttack+this.batMeleeAttackMax);
		}
		minAttack=(int)(minAttack+(maxAttack-minAttack)*(double)(this.batLuckyAttack)/(this.batLuckyAttack+10000));
		int finalAttack=IntUtil.getRandomInt(minAttack, maxAttack);
		return finalAttack;
	}
	
	/**
	 * 
	 * 方法功能:脚本-获得法术攻击
	 * 更新时间:2014-10-29, 作者:johnny
	 * @return
	 */
	public Integer callReadMagicAttack(){
		int minAttack = 0;
		int maxAttack = 0;
		if(this.rolePo==null){
			minAttack=(int)((this.batMagicAttack+this.batMagicAttackMin)*(1+this.batAttackRate/1000d));
			maxAttack=(int)((this.batMagicAttack+this.batMagicAttackMax)*(1+this.batAttackRate/1000d));
		}else{
			minAttack=(int)(this.batMagicAttack+this.batMagicAttackMin);
			maxAttack=(int)(this.batMagicAttack+this.batMagicAttackMax);
		}
		minAttack=(int)(minAttack+(maxAttack-minAttack)*(double)(this.batLuckyAttack)/(this.batLuckyAttack+10000));
		int finalAttack=IntUtil.getRandomInt(minAttack, maxAttack);
		return finalAttack;
	}
	
	/**
	 * 
	 * 方法功能:脚本-获得物理防御
	 * 更新时间:2014-10-29, 作者:johnny
	 * @return
	 */
	public Integer callReadMeleeDefence(){
		int minDefence = 0;
		int maxDefence = 0;
		if(this.rolePo==null){
			minDefence=(int)((this.batMeleeDefence+this.batMeleeDefenceMin)*(Math.max(1+this.batDefenceRate/1000d,0.1d)));
			maxDefence=(int)((this.batMeleeDefence+this.batMeleeDefenceMax)*(Math.max(1+this.batDefenceRate/1000d,0.1d)));
		}else{
			minDefence=(int)(this.batMeleeDefence+this.batMeleeDefenceMin);
			maxDefence=(int)(this.batMeleeDefence+this.batMeleeDefenceMax);
		}
		minDefence=(int)(minDefence+(maxDefence-minDefence)*(double)(this.batLuckyDefence)/(this.batLuckyDefence+10000));
		int finalDefence=IntUtil.getRandomInt(minDefence, maxDefence);
		return finalDefence;
	}
	
	/**
	 * 
	 * 方法功能:脚本-获得法术防御
	 * 更新时间:2014-10-29, 作者:johnny
	 * @return
	 */
	public Integer callReadMagicDefence(){
		int minDefence = 0;
		int maxDefence = 0;
		if(this.rolePo==null){
			minDefence=(int)((this.batMagicDefence+this.batMagicDefenceMin)*(Math.max(1+this.batDefenceRate/1000d,0.1d)));
			maxDefence=(int)((this.batMagicDefence+this.batMagicDefenceMax)*(Math.max(1+this.batDefenceRate/1000d,0.1d)));
		}else{
			minDefence=(int)(this.batMagicDefence+this.batMagicDefenceMin);
			maxDefence=(int)(this.batMagicDefence+this.batMagicDefenceMax);
		}
		minDefence=(int)(minDefence+(maxDefence-minDefence)*(double)(this.batLuckyDefence)/(this.batLuckyDefence+10000));
		int finalDefence=IntUtil.getRandomInt(minDefence, maxDefence);
		return finalDefence;
	}
	
	/**
	 * 
	 * 方法功能:脚本-造成血量变化
	 * 更新时间:2014-10-29, 作者:john
	 * @return
	 */
	public void callHpChange(Fighter caster,Integer change,boolean flush){
//		PrintUtil.print("callHpChange()  受this.name="+this.name +"; 攻caster.name="+caster.name);
		boolean critial=false;
		try {
			if(change<0){
				// 命中
				int hit=caster.batHitRate;
				// 闪避
				int dodge=this.batDodge;
				double hitRate=(double)hit/((caster.lv*caster.lv+10*caster.lv+300)*15);
				double dodgeRate=(double)dodge/((this.lv*this.lv+10*this.lv+300)*15);
				int calcDodge1w=(int)(Math.min(Math.max(dodgeRate+0.05-hitRate,0),0.5)*10000);
				if(RandomUtil.random1W(calcDodge1w)&&caster.lv>=10){
					// 广播闪避
					BattleMsgUtil.abroadDodge(this,caster);
					return;
				}
				else{
					// 暴击
					int crit=caster.batCritical;
					// 韧性
					int tough=this.batTough;
					double critRate=(double)crit/((caster.lv*caster.lv+10*caster.lv+300)*8)+caster.batAddCriticalRate/1000d;
					double toughRate=(double)tough/((this.lv*this.lv+10*this.lv+300)*8)+this.batDerateCriticalRate/1000d;
					int calcTough1w=(int)(Math.min(Math.max(critRate+0.05-toughRate,0),0.9)*10000);
					
					BufferStatusVo bsVo = caster.findBufferStatus(19);
//					System.out.println(" 普通伤害 change = " +change);
					if((caster!=null && RandomUtil.random1W(calcTough1w)) || bsVo!=null){
						critial=true;
						// 造成暴击
						change=(int)(change*(1.5+this.batCriticalDamageRate/1000d));
//						System.out.println(" 暴击伤害 change = " + change);
						if(bsVo != null){
							bsVo.life--;
							bsVo.life = Math.max(0, bsVo.life);
						}
						checkTouchBuff(this,2,critial, 1, 0);
					}
				}
			}
			if(change<0){
				change=Math.min(-1, change);
			}

			checkTouchBuff(caster,1,critial, 1, 0);
			
			int realChange = makeHpChange(caster,change);
			//TODO 有问题，可能是怪物先死了
			BattleMsgUtil.abroadHpChange(this, realChange,1,1,critial,caster,flush);
//			System.out.println("this.batReboundDamage = "+this.batReboundDamage +"; name = "+name+"; caster.name="+caster.name);
			if(this.batReboundDamage>0){
				if(caster != null){
					int reBoundDamage=caster.makeHpChange(this,change*this.batReboundDamage/1000);
					BattleMsgUtil.abroadHpChange(caster, reBoundDamage,1,1,false,this,flush);					
				}
			}
			
			
			if(this.type ==  Entity.MOVER_TYPE_PLAYER && 
				caster.type ==  Entity.MOVER_TYPE_PLAYER && 
				this.rolePo != null && caster.rolePo != null && 
				this.rolePo.getId().intValue() != caster.rolePo.getId().intValue() &&
				this.mapRoom!= null &&
				this.mapRoom.sceneId != 20100002){
				if(System.currentTimeMillis() > this.rolePo.lastWarningTime){
					this.rolePo.sendMsg(ColourType.COLOUR_YELLOW+caster.name+ColourType.COLOUR_WHITE+GlobalCache.fetchLanguageMap("key7"));
					this.rolePo.lastWarningTime=System.currentTimeMillis() + 1000*15;
				}
			}
			
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	/**
	 * 技能触发buff 
	 * @param caster
	 * @param touchType  触发类型，首要条件 1：攻击 2：暴击 3：死亡
	 * @param critial    暴击时触发
	 * @param hp		   判断死亡
	 * @param type 	               判断攻击，受伤的对象 0:this是触发者 1：caster是触发者
	 */
	public boolean checkTouchBuff(Fighter caster, int touchType, boolean critial, int hp, int type){
//		PrintUtil.print("checkTouchBuff() 受this.name = "+this.name+"; 攻caster.name = " + caster.name +"; touchType="+touchType +"; critial=" +critial+"; hp="+hp);
		boolean bool = true;
		Fighter newCaster = null;
		Fighter newhurt = null;
		if(type == 0){
			newCaster = caster;
			newhurt = this;
		}else if(type == 1){
			newCaster = this;
			newhurt = caster;
		}
		
		if(newCaster.type == Entity.MOVER_TYPE_PET)
		{
			if(null != newCaster.livingBuffers)
			{
				List<Fighter> receiveFighters = new ArrayList<Fighter>();
				receiveFighters.add(newhurt);

				for(BuffPo buffPo: newCaster.livingBuffers)
				{
					BufferEffetVo bufferEffetVo = buffPo.bufferEffetVos.get(0);
					
					if(bufferEffetVo.buffType == BuffType.BUFF_EFFECT_15){
						int random = RandomUtil.randomInteger(100)+1;
						
						if(random<=bufferEffetVo.param1)
						{
							
							//buff触发技能
							Integer selectEntityId = 0;
							Integer attractIndex = 0;
							List<Fighter> fighters = receiveFighters;
							StringBuilder sb = new StringBuilder();
							for(int i=0;i<fighters.size();i++)
							{
								if(i!=0){
									sb.append("@");
								}
								else
								{
									selectEntityId = fighters.get(i).mapUniqId;						
								}
								sb.append(fighters.get(i).mapUniqId);
							}
							PetSkillPo petSkillPo = GlobalCache.fetchPetSkillPoByBuffPoId(buffPo.getId());
							
							if(bufferEffetVo.param2 == touchType || 
							 (critial && bufferEffetVo.param2==touchType) ||
							 (hp<=0&& bufferEffetVo.param2==touchType)){
//								System.out.println("触发技能: "+buffPo.getId());
								BattleMsgUtil.abroadSkill(true,newCaster, bufferEffetVo.param3, selectEntityId, sb.toString(), attractIndex, newCaster,petSkillPo.getId(),x,y,z,null);					
							}
						}						
					}
				}
			}
		}else if(newCaster.type.intValue() == Entity.MOVER_TYPE_PLAYER){
			if(mapRoom != null && mapRoom.copySceneConfPo!=null){
				if(mapRoom.copySceneConfPo.getType() == CopySceneType.COPY_SCENE_TYPE_FEIGN_DEATH && batHp<=0 ){
//					PrintUtil.print(" 执行假死  newCaster.name = " +newCaster.name);
					BuffPo buffPo = BuffPo.findEntity(101);
					List<Fighter> receiveFighters = new ArrayList<Fighter>();
					receiveFighters.add(this);
					BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, this, receiveFighters);
					makeAddBuff(bufferStatusVo,this, true);
					bool = false;
				}
			}
		}
		return bool;
	}
	
	
	public void callMpChange(Fighter caster,Integer change,boolean flush){
		try {

			if(change<0){
				change=Math.min(-1, change);
			}
			int realChange = makeMpChange(caster,change);
			BattleMsgUtil.singleAbroadMpChange(this, realChange,flush);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	

	
	public void callAddBuff(Fighter caster,Integer buffId){
		if(mapRoom==null){
			//TODO safe
			return;
		}
//		System.out.println("caster.name = " +caster.name);
		mapRoom.doAddBuffer(caster, buffId);
	}
	
	
	public Integer callSkillLv(Integer skillId){
		if(rolePo!=null){
			for (int i = 0; i < rolePo.listSkillVos.size(); i++) {
				IdNumberVo idNumberVo =rolePo.listSkillVos.get(i);
				if(idNumberVo.getId()==skillId.intValue()){
//					System.out.println(name+":"+idNumberVo.getNum());
					return idNumberVo.getNum();
				}
			}
			return 0;
		}
		else{
			if(monsterPo==null){
				return 1;
			}
			for (int i = 0; i < monsterPo.skillIds.size(); i++) {
				if(skillId==monsterPo.skillIds.get(i).intValue()){
					return 1;
				}
			}
			return 0;
		}
		
	}


	public byte[] buildMoverStatus() {
		if(robot)
		{
			if(rolePo == null && robotType != Entity.ROBOT_TYPE_ARENA){
				return null;
			}
		}
		ByteArrayOutputStream out=new ByteArrayOutputStream(128);
		try {
			out.write(ByteUtil.int2Byte(mapUniqId));
			out.write(ByteUtil.int2SingleByte(type));
			out.write(ByteUtil.floatToBytes(x));
			out.write(ByteUtil.floatToBytes(y));
			out.write(ByteUtil.floatToBytes(z));
			out.write(ByteUtil.int2Byte(itemId));
			if(type==MOVER_TYPE_MONSTER){
				out.write(ByteUtil.StringToBytes(""));
			}else{
				out.write(ByteUtil.StringToBytes(name));
			}
			out.write(ByteUtil.StringToBytes(modelAvatar));
			out.write(ByteUtil.StringToBytes(weaponAvatar));
			out.write(ByteUtil.StringToBytes(wingAvatar));
			out.write(ByteUtil.int2Byte(batHp));
			out.write(ByteUtil.int2Byte(batMaxHp));
			out.write(ByteUtil.int2Byte(batMp));
			out.write(ByteUtil.int2Byte(batMaxMp));
			out.write(ByteUtil.int2SingleByte(pkStatus));
			out.write(ByteUtil.int2SingleByte(lv));
			out.write(ByteUtil.int2SingleByte(career));
			if((agentPlayer==null) || (agentPlayer.mapUniqId==null)){
				out.write(ByteUtil.int2Byte(0));
			}
			else{
				out.write(ByteUtil.int2Byte(agentPlayer.mapUniqId));
			}
			if((master==null) || (master.fighter==null) || (master.fighter.mapUniqId==null)){
				out.write(ByteUtil.int2Byte(0));
			}
			else{
				out.write(ByteUtil.int2Byte(master.fighter.mapUniqId));
			}
			out.write(ByteUtil.boolean2Byte(robot));
			out.write(ByteUtil.StringToBytes(skillString));
			out.write(ByteUtil.int2SingleByte(tag));
			out.write(ByteUtil.StringToBytes(guildName));
			out.write(ByteUtil.int2Byte(nowTitleLv));
			out.write(ByteUtil.int2SingleByte(guildPosition));
			out.write(ByteUtil.floatToBytes(targetX));
			out.write(ByteUtil.floatToBytes(targetY));
			out.write(ByteUtil.floatToBytes(targetZ));
			out.write(ByteUtil.int2SingleByte(avatarStatus));
			out.write(ByteUtil.int2ShortByte(currentMilitaryRankId));
			out.write(ByteUtil.int2SingleByte(militaryForces));
			out.write(ByteUtil.boolean2Byte(wasSiegeBid==1?true:false));		
			out.write(ByteUtil.int2SingleByte(bufferStatusVos.size()));
			for (BufferStatusVo bufferStatusVo : bufferStatusVos) {
				short s = bufferStatusVo.buffPo.getId().shortValue();
				out.write(ByteUtil.short2Byte(s));
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		return out.toByteArray();
	}

	public void changeModelAvatar(String val){
		this.modelAvatar=val;
		change();
	}
	
	public void changeWeaponAvatar(String val){
		this.weaponAvatar=val;
		change();
	}
	
	public void changeWingAvatar(String val){
		this.wingAvatar=val;
		change();
	}
	
	public void changeBatHp(Integer val){
		this.batHp=val;
		change();
	}
	
	public void changeBatMaxHp(Integer val){
		this.batMaxHp=val;
		change();
	}
	
	public void changeBatMp(Integer val){
		this.batMp=val;
		change();
	}
	
	public void changeBatMaxMp(Integer val){
		this.batMaxMp=val;
		change();
	}
	
	public void changePkStatus(Integer val){
		this.pkStatus=val;
		change();
	}
	
	public void changeLv(Integer val){
		this.lv=val;
		change();
	}
	
	public void changeAgentPlayer(Fighter fighter){
		this.agentPlayer=fighter;
		change();
	}
	
	public void changeMasterPlayer(RolePo rolePo){
		this.master=rolePo;
		change();
	}

	
	public void changeGuildName(String val){
		this.guildName=val;
		change();
	}
	
	public void changeNowTitleLv(Integer val){
		this.nowTitleLv=val;
		change();
	}
	
	public void changeGuildPosition(){
		if(rolePo!=null){
			if(rolePo.wasRobot()){
				guildPosition = rolePo.guildPosition;
			}else{
				GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
				if(guildPo != null){
					GuildMemberVo guildMemberVo = guildPo.fetchGuildMemberVoInfo(rolePo.getId());
					if(guildMemberVo != null){
						guildPosition = guildMemberVo.guildPosition;				
					}
				}
				else{
					guildPosition=0;
				}				
			}
			change();
		}
	}
	
	public void changeTargetX(float val){
		this.targetX=val;
		change();
	}
	
	public void changeTargetY(float val){
		this.targetY=val;
		change();
	}
	
	public void changeTargetZ(float val){
		this.targetZ=val;
		change();
	}
	
	public void changeAvatarStatus(Integer val){
		this.avatarStatus=val;
		change();
	}
	
	public void changeCurrentMilitaryRankId(Integer val){
		if(rolePo != null){
			currentMilitaryRankId=rolePo.getCurrentMilitaryRankId();
		}
		change();
	}
	
	public void changeMilitaryForces(Integer val){
		this.militaryForces=val;
		change();
	}
	
	public void changeWasSiegeBid(){
		if(rolePo != null){	
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
			SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
			if(siegeBidVo!=null && siegeBidVo.ownerGuidId!=0 && rolePo.getGuildId().intValue() == siegeBidVo.ownerGuidId){
				wasSiegeBid=1;
			}else{
				wasSiegeBid=0;
			}
		}
		change();
	}
	

	/**
	 * 属性修改
	 * roleType 属性类型
	 * change 变化值
	 */
	private void updateBat(int roleType, int change)
	{
//		System.out.println(name + "; roleType="+roleType + "; change="+change);
		switch (roleType) {
		case RoleType.batMeleeAttack:
			//物理攻击
			this.batMeleeAttack+=change;
			if(rolePo != null){
				rolePo.setBatMeleeAttack(this.batMeleeAttack);			
			}
			break;
		case RoleType.batMagicAttack:
			//魔法攻击
			this.batMagicAttack+=change;
			if(rolePo != null){
				rolePo.setBatMagicAttack(this.batMagicAttack);		
			}
			break;
		case RoleType.batMeleeDefence:
			//物理防御
			this.batMeleeDefence+=change;
			if(rolePo != null){
				rolePo.setBatMeleeDefence(this.batMeleeDefence);			
			}
			break;
		case RoleType.batMagicDefence:
			//魔法防御
			this.batMagicDefence+=change;
			if(rolePo != null){
				rolePo.setBatMagicDefence(this.batMagicDefence);				
			}
			break;
		case RoleType.batMeleeAttackMin:
			//物理攻击-最小值
			this.batMeleeAttackMin+=change;
			if(rolePo != null){
				rolePo.setBatMeleeAttackMin(this.batMeleeAttackMin);			
			}
			break;
		case RoleType.batMeleeAttackMax:
			//物理攻击-最大值
			this.batMeleeAttackMax+=change;
			if(rolePo != null){
				rolePo.setBatMeleeAttackMax(this.batMeleeAttackMax);			
			}
			break;
		case RoleType.batMeleeDefenceMin:
			// 物理防御-最小值
			this.batMeleeDefenceMin+=change;
			if(rolePo != null){
				rolePo.setBatMeleeDefence(this.batMeleeDefenceMin);			
			}
			break;
		case RoleType.batMeleeDefenceMax:
			// 物理防御-最大值
			this.batMeleeDefenceMax+=change;
			if(rolePo != null){
				rolePo.setBatMeleeDefenceMax(this.batMeleeDefenceMax);			
			}
			break;
		case RoleType.batMagicAttackMin:
			// 魔法攻击-最小值
//			System.out.println("     ");
//			System.out.println(name + " 1Fighter.updateBat() ; change="+change +"; this.batMagicAttackMin = " +this.batMagicAttackMin);
			this.batMagicAttackMin+=change;
			if(rolePo != null){
				rolePo.setBatMagicAttackMin(this.batMagicAttackMin);		
//				System.out.println(name + " 2Fighter.updateBat() ; change="+change +"; rolePo.batMagicAttackMin = " +rolePo.getBatMagicAttackMin());
			}
			break;
		case RoleType.batMagicAttackMax:
			// 魔法攻击-最大值
			this.batMagicAttackMax+=change;
			if(rolePo != null){
				rolePo.setBatMagicAttackMax(this.batMagicAttackMax);			
			}
			break;
		case RoleType.batMagicDefenceMin:
			// 魔法防御-最小值
			this.batMagicDefenceMin+=change;
			if(rolePo != null){
				rolePo.setBatMagicDefenceMin(this.batMagicDefenceMin);			
			}
			break;
		case RoleType.batMagicDefenceMax:
			// 魔法防御-最大值
			this.batMagicDefenceMax+=change;
			if(rolePo != null){
				rolePo.setBatMagicDefenceMax(this.batMagicDefenceMax);		
			}
			break;
		case RoleType.batMaxHp:
			// 最大生命值
			this.changeBatMaxHp(batMaxHp+change);
			if(rolePo != null){
				rolePo.setBatMaxHp(this.batMaxHp);				
			}
			break;
		case RoleType.batMaxMp:
			// 最大法力值
			this.changeBatMaxMp(this.batMaxMp+change);
//			this.batMaxMp+=change;
			if(rolePo != null){
				rolePo.setBatMaxMp(this.batMaxMp);			
			}
			break;
		case RoleType.batHpReg:
			// 生命恢复
			this.batHpReg+=change;
			if(rolePo != null){
				rolePo.setBatHpReg(this.batHpReg);	
			}
			break;
		case RoleType.batMpReg:
			// 法力恢复
			this.batMpReg+=change;
			if(rolePo != null){
				rolePo.setBatMpReg(this.batMpReg);	
			}
			break;
		case RoleType.batMovement:
			// 速度
			this.batMovement+=change;
			if(rolePo != null){
				rolePo.setBatMovement(this.batMovement);		
			}
			break;
		case RoleType.batCriticalDamageRate:
			// 暴击伤害率
			this.batCriticalDamageRate+=change;
//			System.out.println(this.name + " this.batCriticalDamageRate = " +this.batCriticalDamageRate);
			if(rolePo != null){
				rolePo.setBatCriticalDamageRate(this.batCriticalDamageRate);		
			}
			break;
		case RoleType.batLuckyAttack:
			// 攻击幸运
			this.batLuckyAttack+=change;
			if(rolePo != null){
				rolePo.setBatLuckyAttack(this.batLuckyAttack);		
			}
			break;
		case RoleType.batLuckyDefence:
			// 防御幸运
			this.batLuckyDefence+=change;
			if(rolePo != null){
				rolePo.setBatLuckyDefence(this.batLuckyDefence);
			}
			break;
		case RoleType.batHitRate:
			// 命中
			this.batHitRate+=change;
			if(rolePo != null){
				rolePo.setBatHitRate(this.batHitRate);			
			}
			break;
		case RoleType.batDodge:
			// 闪避
			this.batDodge+=change;
			if(rolePo != null){
				rolePo.setBatDodge(this.batDodge);		
			}
			break;
		case RoleType.batCritical:
			// 暴击
			this.batCritical+=change;
//			System.out.println(this.name + " batCritical = " +batCritical);
			if(rolePo != null){
				rolePo.setBatCritical(this.batCritical);		
			}
			break;
		case RoleType.batTough:
			// 抗暴击
			this.batTough+=change;
			if(rolePo != null){
				rolePo.setBatTough(this.batTough);			
			}
			break;
		case RoleType.batDamageRate:
			// 伤害率
			this.batDamageRate+=change;
			if(rolePo != null){
				rolePo.setBatDamageRate(this.batDamageRate);			
			}
			break;
		case RoleType.batDamageResistRate:
			// 抗伤害率
			this.batDamageResistRate+=change;
			if(rolePo != null){
				rolePo.setBatDamageResistRate(this.batDamageResistRate);	
			}
			break;
		case RoleType.batAttackRate:
			// 攻击百分比率
			this.batAttackRate+=change;
			if(rolePo != null){
				rolePo.setBatAttackRate(this.batAttackRate);		
			}
			break;
		case RoleType.batDefenceRate:
			// 防御百分比率
			this.batDefenceRate+=change;
			if(rolePo != null){
//				System.out.println(name +"; this.batDefenceRate = " +this.batDefenceRate);
				rolePo.setBatDefenceRate(this.batDefenceRate);		
			}
			break;
		case RoleType.batHitRateRate:
			// 命中率(比率非数值)
			this.batHitRateRate+=change;
			if(rolePo != null){
				rolePo.setBatHitRateRate(batHitRateRate);				
			}
			break;
		case RoleType.batDodgeRate:
			// 闪避率(比率非数值)
			this.batDodgeRate+=change;
			if(rolePo != null){
				rolePo.setBatDodgeRate(this.batDodgeRate);			
			}
			break;
		case RoleType.batAddCriticalRate:
			// 暴击增加率(比率非数值)
			this.batAddCriticalRate+=change;
			if(rolePo != null){
				rolePo.setBatAddCriticalRate(this.batAddCriticalRate);		
			}
			break;
		case RoleType.batDerateCriticalRate:
			// 暴击减免率(比率非数值)
			this.batDerateCriticalRate+=change;
			if(rolePo != null){
				rolePo.setBatDerateCriticalRate(this.batDerateCriticalRate);			
			}
			break;
		case RoleType.batReboundDamage:
//			System.out.println(name + " 1Fighter.updateBat() batReboundDamage="+batReboundDamage+"; change="+change +"; this.batMagicAttackMin = " +this.batMagicAttackMin);
			// 反弹伤害百分比
			this.batReboundDamage+=change;
//			System.out.println(name + " 2Fighter.updateBat() batReboundDamage="+batReboundDamage+"; change="+change +"; this.batMagicAttackMin = " +this.batMagicAttackMin);
			if(rolePo != null){
				rolePo.setBatReboundDamage(this.batReboundDamage);		
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 添加buff属性
	 * @param beVo
	 */
	private void addValueBuff(BufferEffetVo beVo)
	{
//		System.out.println(name +"; addValueBuff() beVo = " +beVo);
		
		this.updateBat(beVo.param1, beVo.param2);
		if(this.rolePo!=null){
			this.rolePo.calculateBat(1);
		}

	}
	
	/**
	 * 删除buff属性
	 * @param beVo
	 */
	private void delValueBuff(BufferEffetVo beVo)
	{
//		PrintUtil.print(name +"; delValueBuff() beVo = " +beVo);
		this.updateBat(beVo.param1, -beVo.param2);
		if(this.rolePo!=null){
			this.rolePo.calculateBat(1);
		}

	}

	
	/**
	 * 添加buff效果
	 * @param bufferStatusVo
	 */
	public void makeAddBuff(BufferStatusVo bufferStatusVo, Fighter spellCaster, boolean wasSend) {
		BufferStatusVo oldbsVo = findBufferStatus(bufferStatusVo.buffPo.getId());
//		System.out.println("makeAddBuff () "+bufferStatusVo.buffPo.getId() +" wasSend = " +wasSend);
//		System.out.println("oldbsVo="+oldbsVo);
//		System.out.println("Fighter.makeAddBuff() spellCaster.name="+spellCaster.name+";  buffPoId=" +bufferStatusVo.buffPo.getId());
//		System.out.println("buffer结束时间："+DateUtil.getFormatDateBytimestamp(bufferStatusVo.endTime));
		if(oldbsVo==null){
			bufferStatusVos.add(bufferStatusVo);
			
			//给rolePo也加buff
			if(this.rolePo != null && this.type !=Entity.MOVER_TYPE_PET){
				this.rolePo.saveFighterProToRole();
				this.rolePo.sendUpdateListBufferStatus();
			}
			int random =0;
			//属性加成特效这里做下实现
			
			for(BufferEffetVo bufferEffetVo : bufferStatusVo.buffPo.bufferEffetVos){
				if(bufferEffetVo.buffType == BuffType.BUFF_EFFECT_9){
//					System.out.println("makeAddBuff() bufferEffetVo = " +bufferEffetVo);
					this.addValueBuff(bufferEffetVo);	
				}
				else if(bufferEffetVo.buffType == BuffType.BUFF_EFFECT_16){
					random = RandomUtil.randomInteger(100)+1;
					if(random<=bufferEffetVo.param1)
					{
						//传递继承 BUFF
						if(bufferEffetVo.param3 == RoleType.NPC_MODEL_BUFF)
						{
							//TODO modelAvatar
							Integer modelAvatar = 952003;
							List<Fighter> fighters = bufferStatusVo.receiveFighters;
							for(Fighter receive:fighters)
							{
								receive.changeModelAvatar(modelAvatar.toString());
								receive.changeWingAvatar("0");
								receive.changeWeaponAvatar("0");
								receive.buffAvatar=modelAvatar;
								receive.changeAvatarStatus(1);
								BattleMsgUtil.abroadAvatarInfor(receive);
							}
						}
					}
				}
				else if(bufferEffetVo.buffType == BuffType.BUFF_EFFECT_18){
					rolePo.expPercent = bufferEffetVo.param1;
				}
				else if(bufferEffetVo.buffType == BuffType.BUFF_EFFECT_19){
//					PrintUtil.print(name + " 添加假死 buffer bufferEffetVo.buffType = " +bufferEffetVo.buffType);
					if(this.rolePo != null){
						rolePo.setBatHp(1);						
					}
					changeBatHp(1);
				}
			}
		}
		else
		{
			//如果已有这个BUFF，且不是永久有效的，则更新结束时间为新的BUFF的结束时间
			if(oldbsVo.buffPo.getDurationValexp()!=CopySceneType.EIKY_TIME){
//				System.out.println(DateUtil.getText(System.currentTimeMillis()+1000*bufferStatusVo.buffPo.getDurationValexp()));
				oldbsVo.endTime=System.currentTimeMillis()+1000*bufferStatusVo.buffPo.getDurationValexp();
				if(this.rolePo!=null  && this.type !=Entity.MOVER_TYPE_PET){
					this.rolePo.saveFighterProToRole();
//					this.rolePo.sendUpdateCurrentTime();
					this.rolePo.sendUpdateListBufferStatus();
				}
			}
		}
//		System.out.println("Fighter.makeAddBuff() bufferStatusVos = "+bufferStatusVos);
		if(wasSend){
//			PrintUtil.print(name + " 推送 bufferStatusVo.buffPo.getId() = " +bufferStatusVo.buffPo.getId());
			BattleMsgUtil.abroadAddBuff(this, bufferStatusVo.buffPo.getId());							
		}
	}
	
	

	public BufferStatusVo findBufferStatus(Integer buffId) {
		for (BufferStatusVo bufferStatusVo : bufferStatusVos) {
			if(bufferStatusVo.buffPo.getId().intValue()==buffId.intValue()){
				return bufferStatusVo;
			}
		}
		return null;
	}

	public void checkRemoveExpireBuffer() {
		if(bufferStatusVos.size()<=0){
			return;
		}
//		System.out.println("checkRemoveExpireBuffer() bufferStatusVos = " +bufferStatusVos);
		List<BufferStatusVo> toRemoveList =new ArrayList<BufferStatusVo>();
		for (BufferStatusVo bufferStatusVo : bufferStatusVos) {
			if((bufferStatusVo.endTime != CopySceneType.EIKY_TIME && System.currentTimeMillis()>bufferStatusVo.endTime.longValue() ) || 
			   (bufferStatusVo.endTime == CopySceneType.EIKY_TIME &&  bufferStatusVo.life.intValue() == 0)){
				toRemoveList.add(bufferStatusVo);
			}
			
		}
		
		for (BufferStatusVo bufferStatusVo : toRemoveList) {
			removeBuffer(bufferStatusVo.buffPo.getId());
		}
	}



	public void removeBuffer(Integer id) {
		BufferStatusVo bufferStatusVo = findBufferStatus(id);
		if(bufferStatusVo!=null &&bufferStatusVo.buffPo != null){
			BattleMsgUtil.abroadRemoveBuffer(this, bufferStatusVo.buffPo.getId());
			List<BufferEffetVo> bufferEffetVos = bufferStatusVo.buffPo.bufferEffetVos;
			for (int i = 0; i < bufferEffetVos.size(); i++) {
				BufferEffetVo beVo = bufferStatusVo.buffPo.bufferEffetVos.get(i);
				bufferStatusVos.remove(bufferStatusVo);
				if(rolePo != null){
					if(beVo.buffType == BuffType.BUFF_EFFECT_18){
						rolePo.expPercent=0;
//						System.out.println("rolePo.expPercent="+rolePo.expPercent);
					}
					rolePo.saveFighterProToRole();		
//					this.rolePo.sendUpdateCurrentTime();
					rolePo.sendUpdateListBufferStatus();
//					System.out.println(rolePo.getName()+" removeBuffer() rolePo.listBufferStatus = "+rolePo.listBufferStatus);
//					System.out.println(this.name+" remove| "+ this.rolePo.listBufferStatus+" | "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
				}
				if(beVo.buffType == BuffType.BUFF_EFFECT_9){
//					System.out.println("删除buff id = " + id);
					this.delValueBuff(beVo);
				}
			}
		}

	}
	
	
	public List<BufferStatusVo> findBuffSkill() {
		List<BufferStatusVo> findList = new ArrayList<BufferStatusVo>();
		Iterator<BufferStatusVo> iter = bufferStatusVos.iterator();
		List<SustainKill> skList = XmlCache.xmlFiles.constantFile.sustainKills.sustainKill;
		while (iter.hasNext()) {
			BufferStatusVo bsVo = iter.next();
			for(int i=0; i<skList.size(); i++){
				if (skList.get(i).buffId == bsVo.buffPo.getId()) {
					findList.add(bsVo);
				}				
			}
		}
		return findList;
	}
	

	public void sendAttrChangeInfor() {
		BattleMsgUtil.abroadMoverAttrChange(this);
	}

	/**
	 * 玩家切cell或离开cell，清除cell中的怪物玩家代理
	 */
	public void removeAllAgentMonsters() {
		List<Cell> cells = mapRoom.cellData.getNearByCells(cell,Entity.STAND_BORDER);
		if(cells == null ){
			return;
		}
		for(Cell cell:cells)
		{
			List<Entity> livings = cell.getAllLivings();
			for(Entity living:livings)
			{
				if(living instanceof Fighter && ((Fighter)living).agentPlayer==this)
					mapRoom.doAgentMonster(null, (Fighter)living);
			}
		}
	}

	public void swithPkStatus(int status, int num) {
//		PrintUtil.print(rolePo.getName()+" 要求设置成状态: "+rolePo.getPkStatus()+"; 当前状态："+rolePo.getPkStatus()+"; 当前PK值："+rolePo.getPkValue());
		List<OnlineTime> onlineTimes = XmlCache.xmlFiles.constantFile.accumulativeTime.onlineTime;
		long currentTime = DateUtil.getCurrentTime();
		boolean frush = false;
		if(status==PK_STATUS_GREY){
			if(rolePo.getPkStatus()==PK_STATUS_PEACE){
				long greyTime = onlineTimes.get(1).value;
				rolePo.setPkStatus(PK_STATUS_GREY);
				changePkStatus(PK_STATUS_GREY);
				long grewTime = currentTime+greyTime;
				rolePo.setPkGrewRecoverTime(grewTime);
				rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_GREY, greyTime, 0l, 1);
				frush = true;
			}
		}
		else if(status==PK_STATUS_RED){
			if(this.rolePo.getPkValue() >= 10){
				rolePo.setPkStatus(PK_STATUS_RED);
				changePkStatus(PK_STATUS_RED);
				frush = true;
			}
			long addPkTime = onlineTimes.get((int) (RoleType.ONLINE_TIME_TYPE_RED -1)).value*num;
			rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,
					addPkTime,
					0l,
					1);
			if(this.rolePo.getPkLastRecoverTime().longValue() == 0)
			{
				long pkRedTime = currentTime+addPkTime;
				rolePo.setPkLastRecoverTime(pkRedTime);	
				rolePo.setPkRedBeginTime(currentTime);
			}
			else
			{
				long pkRedTime =rolePo.getPkLastRecoverTime().longValue() +addPkTime;
				rolePo.setPkLastRecoverTime(pkRedTime);	
			}
			rolePo.setPkGrewRecoverTime(0l);
			rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_GREY,0l,0l,0);
		}
		else if(status==PK_STATUS_PEACE){
//			PrintUtil.print("PkGrewRecoverTime:  "+rolePo.getPkGrewRecoverTime());
			if(rolePo.getPkGrewRecoverTime()!=null&&rolePo.getPkGrewRecoverTime().longValue()>0){
				IdLongVo2 idLongVo2 = IdLongVo2.findIdLong(RoleType.ONLINE_TIME_TYPE_GREY, rolePo.listOnlineTime);
				if(idLongVo2!=null){
					long currentOnlineTime =rolePo.fetchRedCurrentOnlineTime().longValue() + idLongVo2.getLong3().longValue();
					long surplusTime =  idLongVo2.getLong2().longValue() - currentOnlineTime;
					if(surplusTime<=0){
						rolePo.setPkStatus(PK_STATUS_PEACE);
						changePkStatus(PK_STATUS_PEACE);
						rolePo.setPkGrewRecoverTime(0l);
						rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_GREY,0l,0l,0);
						frush = true;
					}
				}else{
					if(rolePo.getPkStatus()==PK_STATUS_GREY){
						rolePo.setPkStatus(PK_STATUS_PEACE);
						changePkStatus(PK_STATUS_PEACE);
						rolePo.setPkGrewRecoverTime(0l);
						rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_GREY,0l,0l,0);
						frush = true;
					}
				}
			}
//			PrintUtil.print("PkRedBeginTime:  "+rolePo.getPkRedBeginTime()+"; PkLastRecoverTime:"+rolePo.getPkLastRecoverTime());
			if(rolePo.getPkRedBeginTime()!=null&&rolePo.getPkRedBeginTime()>0){
				IdLongVo2 idLongVo2 = IdLongVo2.findIdLong(RoleType.ONLINE_TIME_TYPE_RED, rolePo.listOnlineTime);
				long currentOnlineTime =rolePo.fetchRedCurrentOnlineTime().longValue() + idLongVo2.getLong3().longValue();
				long surplusTime =  idLongVo2.getLong2().longValue() - currentOnlineTime;
				if(surplusTime>0){
					long value = onlineTimes.get((int) (RoleType.ONLINE_TIME_TYPE_RED -1)).value;
					int pkValue =  DoubleUtil.toUpInt((1d*surplusTime/value));
					if(rolePo.getPkValue().intValue() != pkValue){
						rolePo.setPkValue(pkValue);	
						LogUtil.writeLog(rolePo, 1, 13, 0, pkValue-rolePo.getPkValue().intValue(), GlobalCache.fetchLanguageMap("key2340"), "");
						frush = true;
					}
				}else{
					int value = rolePo.getPkValue()-0;
					rolePo.setPkRedBeginTime(0l);
					rolePo.setPkLastRecoverTime(0l);
					rolePo.setPkValue(0);	
					rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,0l,0l,0);
					LogUtil.writeLog(rolePo, 1, 13, 0, value, GlobalCache.fetchLanguageMap("key2340"), "");
					frush = true;
				}
				if(rolePo.getPkValue()<10){
					rolePo.setPkStatus(PK_STATUS_PEACE);
					changePkStatus(PK_STATUS_PEACE);
					frush = true;
				}
			}
		}
		if(frush){
			rolePo.sendUpdatePKInfor();
		}
//		PrintUtil.print(rolePo.getName()+ " 状态："+rolePo.getPkStatus()+"; PK值："+rolePo.getPkValue());
		BattleMsgUtil.abroadPkStautsChange(this,pkStatus);
		rolePo.sendUpdatePKInfor();
	}

	/**
	 * 检查Pk状态
	 */
	public void checkResetPkStatus() {
		if(rolePo.getPkStatus()!=PK_STATUS_PEACE||rolePo.getPkValue()>0){
			swithPkStatus(PK_STATUS_PEACE, 1);
		}
	}

	public void checkIntervalBuffer() {
		int BUFFER_EFFECT_INTERVAL_HP_CHANGE_NUM = findBuffValueByEffectType(BuffType.BUFF_EFFECT_6);
		int BUFFER_EFFECT_INTERVAL_HP_CHANGE_PERCENTAGE = findBuffValueByEffectType(BuffType.BUFF_EFFECT_5);
		int totalHpChange=BUFFER_EFFECT_INTERVAL_HP_CHANGE_NUM+batMaxHp*BUFFER_EFFECT_INTERVAL_HP_CHANGE_PERCENTAGE/100;
		if(totalHpChange < 0 && totalHpChange < -20000){
			totalHpChange=-20000;
		}
		if(totalHpChange!=0){
			int change =makeHpChange(null,totalHpChange);
			BattleMsgUtil.abroadHpChange(this, change,1,1,false,null,true);
		}
	}

	/**
	 * 玩家死亡掉落概率
	 * @param quality
	 * @param redName
	 * @return
	 */
	public static IdNumberVo getDropPoss(int quality, boolean redName) {
		List<DropProbability> dropprobability = XmlCache.xmlFiles.constantFile.pvp.dropprobability;
		IdNumberVo retVo = new IdNumberVo(0, 0);
		for(DropProbability dp : dropprobability)
		{
			if(quality == dp.quality)
			{
				retVo.setNum(IntUtil.getRandomInt(dp.minNum, dp.maxNum));
				if(redName)
				{
					retVo.setId(dp.red);
				}else{
					retVo.setId(dp.white);
				}
			}
		}
		PrintUtil.print("PK掉落红白名："+retVo.getId()+" : 数量："+retVo.getNum());
		return retVo;
	}
	
	/**
	 * 增加连斩
	 * @param roleId
	 */
	public void addSustainKill(Integer roleId)
	{
		if(rolePo == null){
			return;			
		}

		this.rolePo.addSustainKill(roleId);
		this.killNum=this.rolePo.sustainKillVo.killNum;
	}
	
	/**
	 * 清空连斩
	 */
	public void clearSustainKill()
	{
		if(rolePo == null){
			return;
		}
		int oldKillNum = killNum;
		this.killNum=0;
		if(oldKillNum > this.killNum)
		{
			this.rolePo.clearSustainKill();
//			BattleMsgUtil.abroadkillNum(this);
		}
	}
	
	/**
	 * 称号更新
	 */
	public void updateTitle()
	{
		this.currentTitleLv = rolePo.getTitleLv();
		this.currentSpecialTitleLv=rolePo.getCurrentSpecialTitleLv();
		
		changeNowTitleLv(rolePo.nowTitleLv);
		
		if(this.cell != null)
			BattleMsgUtil.abroadNowTitleLv(this);
	}
	
	/**
	 * 更新积分
	 * @param score
	 */
	public void changeScore(int score)
	{
		rolePo.pVPPVEActivityStatusVo.score+=score;
		mapRoom.playerScoreChange(this);
	}

	@Override
	public boolean realPlayer() {
		if(type == Entity.MOVER_TYPE_PLAYER && !robot){
			return true;
		}
		return false;
	}

	
	public List<RolePo> generateFinalSlaughters(Fighter killer) {
		MonsterPo monsterPo = MonsterPo.findEntity(itemId);
		List<RolePo> originalSlaughters = new ArrayList<RolePo>();
		if(monsterPo.getSlaughterMethod()==MonsterType.MONSTER_SLAUGHTER_FINAL_HIT && killer.rolePo!=null){
			originalSlaughters.add(killer.rolePo);
		}
		else if(monsterPo.getSlaughterMethod()==MonsterType.MONSTER_SLAUGHTER_MAX_HIT){
			RolePo maxHitter = fetchDeathMaxHitter();
			if(maxHitter!=null){
				originalSlaughters.add(maxHitter);
			}
			else{
				maxHitter=killer.rolePo;
				originalSlaughters.add(maxHitter);
			}
		}
		else if(monsterPo.getSlaughterMethod()==MonsterType.MONSTER_SLAUGHTER_SHARE_HIT){
			List<RolePo> shareHit = fetchShareMaxHitter();
			originalSlaughters.addAll(shareHit);
		}
		originalSlaughters=generateTeamShareSlaughters(originalSlaughters);
		return originalSlaughters;
	}

	
	private List<RolePo> generateTeamShareSlaughters(List<RolePo> originalSlaughters) {
		List<RolePo> list=new ArrayList<RolePo>();
		for (RolePo originalRolePo : originalSlaughters) {
			if(originalRolePo.teamMemberVo==null){
				if(!list.contains(originalRolePo)){
					list.add(originalRolePo);
				}
			}
			else{
				List<RolePo> otherShareCellTeamRoles = originalRolePo.fetchCurrentZoneOtherTeamMemberList();
				otherShareCellTeamRoles.add(originalRolePo);
				for (RolePo rolePo : otherShareCellTeamRoles) {
					if(!list.contains(rolePo)){
						list.add(rolePo);
					}
				}
			}
		}
		return list;
	}

	private List<RolePo> fetchShareMaxHitter() {
		List<RolePo> list=new ArrayList<RolePo>();
		Iterator iter = roleHitTotalCount.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			RolePo key = (RolePo) entry.getKey();
			if(key.fighter!=null && isIn9Cell(key.fighter)){
				list.add(key);
			}
		}
		return list;
	}
	
	

	public RolePo fetchDeathMaxHitter() {
		Iterator iter = roleHitTotalCount.entrySet().iterator();
		int maxVal=0;
		RolePo maxFighter=null;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			RolePo key = (RolePo) entry.getKey();
			Integer val = (Integer)entry.getValue();
			if(key.fighter!=null && isIn9Cell(key.fighter)){
				if(val>maxVal){
					maxFighter=key;
					maxVal=val;
				}
			}
		}
		return maxFighter;
	}
	public List<Fighter> fetchCell9AllPlayers(int limit) {
		List<Fighter> player=new ArrayList<Fighter>();
		for (Cell cell : mapRoom.cellData.getNearByCells(this.cell,Entity.STAND_BORDER)) {
			player.addAll(cell.getAllPlayers());
		}
		return player;
	}
	
	public boolean isIn9Cell(Fighter fighter) {
		if(fighter==null || mapRoom==null ||cell==null || fighter.mapRoom==null || fighter.cell==null ){
			return false;
		}
		boolean bool = false;
		// TODO 尽量不用try
		try {
			bool= mapRoom.equals(fighter.mapRoom) && Math.abs(cell.X-fighter.cell.X) <= 1 && Math.abs(cell.Y-fighter.cell.Y) <= 1?true:false;
		} catch (Exception e) {
			PrintUtil.print("fighter="+fighter);
			PrintUtil.print("mapRoom="+mapRoom);
			PrintUtil.print("fighter.mapRoom="+fighter.mapRoom);
			PrintUtil.print("cell="+cell);
			PrintUtil.print("cell.X="+cell.X);
			PrintUtil.print("cell.Y="+cell.Y);
			PrintUtil.print("fighter.cell="+fighter.cell);
			ExceptionUtil.processException(e);
			return false;
		}
		return bool;
	}
	
	/** 获取boss伤害排行 */
	public List<RankVo> fetchMonsterBossHitRank(){
		List<RankVo> list = new ArrayList<RankVo>();
		Iterator iter = roleHitTotalCount.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			RolePo key = (RolePo)entry.getKey();
			Integer val = (Integer)entry.getValue();
			RankVo rankVo = new RankVo();
			rankVo.roleId = key.getId();
			rankVo.roleName=key.getName();
			rankVo.rolePower=val;
			list.add(rankVo);
		}
		Collections.sort(list);
		for(int i=0; i < list.size(); i++){
			RankVo rv =  list.get(i);
			rv.rankLv=i+1;
		}
		return list;
	}
	
	public boolean filterTargetMonster(Entity target,int msgType) {
		Fighter targetMonster =(Fighter) target;
		boolean inList=false;
		for (Fighter hitter : targetMonster.fightSkillOnMe) {
			if(hitter==null || hitter.rolePo==null){
				continue;
			}
			if(rolePo.getId()==hitter.rolePo.getId().intValue()){
				//对目标施法过的人不过滤
				inList=true;
				continue;
			}
//			if(msgType==BattleMsgUtil.BATTLE_MSG_TYPE_FIGHTER_MOVE){
//				if(hitter.beSeeingPlayeFighters!=null){
//					for (Integer thePlayerSeeHitPlayerFighterId : hitter.beSeeingPlayeFighters.keySet()) {
//						if(thePlayerSeeHitPlayerFighterId.intValue()==mapUniqId){
//							//看见的人里面对怪物释放过法术的人不过滤怪物移动消息
//							inList=true;
//							break;
//						}
//					}
//				}
//			}
		}
		if(inList){
			return false;
		}
		else{
			return true;
		}

	}
	
	public boolean filterTargetPet(Entity target,boolean far) {
		//如果是自己不过滤
		//如果我看得见"目标"则不过滤
		Fighter pet = (Fighter) target;
		if(pet.master!=null && pet.master.fighter!=null){
			if(pet.master.fighter.mapUniqId==mapUniqId.intValue()){
				return false;
			}
			if(far){
				if(farSeeingPlayeFighters.containsKey(pet.master.fighter.mapUniqId)){
					return false;
				}
				else{
					return true;
				}
			}
			else{
				if(nearSeeingPlayeFighters.containsKey(pet.master.fighter.mapUniqId)){
					return false;
				}
				else{
					return true;
				}
			}

		}
		else{
			return false;
		}

	}
	
	public boolean filterTargetPlayer(Entity target,boolean far) {
		//如果是自己不过滤
		if(target==this){
			return false;
		}
		if(far){
			//如果我看得见"目标"则不过滤
			if(farSeeingPlayeFighters.containsKey(target.mapUniqId)){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			//如果我看得见"目标"则不过滤
			if(nearSeeingPlayeFighters.containsKey(target.mapUniqId)){
				return false;
			}
			else{
				return true;
			}
		}
	}
	
	public void checkPlayerAddSeePlayer(Entity mover,boolean far) {
		if(mover==null){
			return;
		}
		if(!(mover instanceof Fighter)){
			return;
		}
		Fighter targetFighter =(Fighter) mover;
		if(targetFighter.rolePo==null || rolePo==null){
			return;
		}
		if(type!=Entity.MOVER_TYPE_PLAYER || mover.type!=MOVER_TYPE_PLAYER){
			return;
		}
		if(mover==this){
			return;
		}
		if(robot || ((Fighter)mover).robot){
			return;
		}
		if(far){
			if(farSeeingPlayeFighters.size()<=NetType.FILTER_FAR_LIMIT){
				if(targetFighter.rolePo!=null){
					if(targetFighter.farBeSeeingPlayeFighters.size()<=(2*NetType.FILTER_FAR_LIMIT)){
						farSeeingPlayeFighters.put(targetFighter.mapUniqId, targetFighter.rolePo.getId());
						targetFighter.farBeSeeingPlayeFighters.put(mapUniqId, rolePo.getId());
					}
				}
			}
		}
		else{
			if(nearSeeingPlayeFighters.size()<=NetType.FILTER_NEAR_LIMIT){
				if(targetFighter.rolePo!=null){
					if(targetFighter.nearBeSeeingPlayeFighters.size()<=(2*NetType.FILTER_NEAR_LIMIT)){
						nearSeeingPlayeFighters.put(targetFighter.mapUniqId, targetFighter.rolePo.getId());
						targetFighter.nearBeSeeingPlayeFighters.put(mapUniqId, rolePo.getId());
						//内圈添加外圈也添加
						farSeeingPlayeFighters.put(targetFighter.mapUniqId, targetFighter.rolePo.getId());
						targetFighter.farBeSeeingPlayeFighters.put(mapUniqId, rolePo.getId());
					}
				}
			}
		}
	}
	
	public void checkPlayerRemoveSeePlayer(Entity mover,boolean far) {
		if(mover==null){
			return;
		}
		if(!(mover instanceof Fighter)){
			return;
		}
		Fighter targetFighter =(Fighter) mover;
		if(type!=Entity.MOVER_TYPE_PLAYER || mover.type!=MOVER_TYPE_PLAYER){
			return;
		}
		if(targetFighter.rolePo==null || rolePo==null){
			return;
		}
		if(robot || ((Fighter)mover).robot){
			return;
		}
		if(mover==this){
			return;
		}
		if(far){
			if(farSeeingPlayeFighters.containsKey(mover.mapUniqId)){
				farSeeingPlayeFighters.remove(mover.mapUniqId);
			}
			targetFighter.farBeSeeingPlayeFighters.remove(mapUniqId);
		}
		else{
			if(nearSeeingPlayeFighters.containsKey(mover.mapUniqId)){
				nearSeeingPlayeFighters.remove(mover.mapUniqId);
			}
			targetFighter.nearBeSeeingPlayeFighters.remove(mapUniqId);
			//内圈删除外圈也删除
			if(farSeeingPlayeFighters.containsKey(mover.mapUniqId)){
				farSeeingPlayeFighters.remove(mover.mapUniqId);
			}
			targetFighter.farBeSeeingPlayeFighters.remove(mapUniqId);
		}
	}
	
	public void checkAddMonsterSkillOnMe(Fighter fighter) {
		if(fighter==null){
			return;
		}

		if(!(type==Entity.MOVER_TYPE_MONSTER && fighter.type==Entity.MOVER_TYPE_PLAYER)){
			return;
		}
		if(fighter.fightSkillOnMe==null){
			fighter.fightSkillOnMe=new CopyOnWriteArrayList<Fighter>();
		}
		if(fightSkillOnMe.size()>100){
			return;
		}
		
		if(!fightSkillOnMe.contains(fighter)){
			fightSkillOnMe.add(fighter);
		}
	}

	
	public byte[] fetchMoverStatus() {
		if(moverStatusChanged){
			moverStatusBytes=buildMoverStatus();
			moverStatusChanged=false;
		}
		return moverStatusBytes;
		

	}
	

}
