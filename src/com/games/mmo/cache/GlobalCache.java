package com.games.mmo.cache;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.backend.vo.BlockVo;
import com.games.backend.vo.ForbidVo;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.vo.BufferEffetVo;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.AbroadPo;
import com.games.mmo.po.BlockPo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.ForbidPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.ProductPo;
import com.games.mmo.po.RankArenaPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.DicClientPo;
import com.games.mmo.po.game.DicServerPo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.po.game.FashionPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.LvConfigPo;
import com.games.mmo.po.game.NoticePo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetRollPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.po.game.PetTalentPo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.RobotPo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.po.game.TitlePo;
import com.games.mmo.po.game.UpgradeSkillPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.task.AbroadJob;
import com.games.mmo.task.NoticeJob;
import com.games.mmo.thread.SimpleAbroadThread;
import com.games.mmo.type.EffectType;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.RankType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SceneType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LangUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.ConstellAttachVo;
import com.games.mmo.vo.CopySceneActivityVo;
import com.games.mmo.vo.EquipAttachVo;
import com.games.mmo.vo.LoginQueueVo;
import com.games.mmo.vo.RankVo;
import com.games.mmo.vo.SessionSaveVo;
import com.games.mmo.vo.SlotSoulVo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.games.mmo.vo.xml.ConstantFile.AwardRetrieves.AwardRetrieve;
import com.games.mmo.vo.xml.ConstantFile.DiamondBasins.DiamondBasin;
import com.games.mmo.vo.xml.ConstantFile.EquipRandomAtts.EquipRandomAtt;
import com.games.mmo.vo.xml.ConstantFile.Global.CareerItems.ReplaceItem;
import com.games.mmo.vo.xml.ConstantFile.GrowFunds.GrowFund;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetAttachs.PetAttach;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetConstells.PetConstell;
import com.games.mmo.vo.xml.ConstantFile.SustainKills.SustainKill;
import com.games.mmo.vo.xml.LanguageFile.Languages.Words;
import com.games.mmo.vo.xml.PlayerFile.UserInfos.UserInfo;
import com.storm.lib.bean.CheckcCircleBean;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.quartz.QuartzSchedulerTemplate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdLongVo;
import com.storm.lib.vo.IdNumberVo;



public class GlobalCache {

	/**
	 * 服务器排队人数信息
	 */
	public static List<LoginQueueVo> serverQueueNum = new CopyOnWriteArrayList<LoginQueueVo>();
	
	public static ConcurrentHashMap<String, List<String[]>> langs = new ConcurrentHashMap<String, List<String[]>>();
	public static int OPEN_TRADE=1;
	public static boolean inited = false;
	
	public static int SERVER_CURRENT_START_KEY = 0;

	public static int VOICE_CHAT_REMAIN_INDEX=1;
	
	public static int VOICE_CHAT_INDEX=1;
	
	public static int arenaRobotRolePoid = 9000000;
	
	public static ConcurrentHashMap<Integer, String> VOICE_MSGS=new ConcurrentHashMap<Integer,String>();
	/**
	 * 掉落组的MAP，key=掉落组的编号(不是id),和掉落列表
	 */
	public static ConcurrentHashMap<Integer, List<DropPo>> idTcGroupsMap=new ConcurrentHashMap<Integer, List<DropPo>>();
	
	/**
	 * 竞技场排行MAP，key=排名 value=竞技场数据
	 */
	public static ConcurrentHashMap<Integer, RankArenaPo> rankArenaMaps = new ConcurrentHashMap<Integer, RankArenaPo>();
	
	/**
	 * 竞技场角色MAP，key=角色编号 value=竞技场数据
	 */
	public static ConcurrentHashMap<Integer, RankArenaPo> rankArenaRoleIdMaps = new ConcurrentHashMap<Integer, RankArenaPo>();
	
	/**
	 * 队伍MAP,key=队伍编号 value=TeamVo
	 */
	public static ConcurrentHashMap<Integer, TeamVo> teamIdMaps = new ConcurrentHashMap<Integer, TeamVo>();
	
	/**
	 * 副本队伍，key=copySceneConfPoId value=<key = 队伍编号，value=TeamVo> 
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, TeamVo>> teamDungeonIdMaps = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,TeamVo>>();
	
	/**
	 * 副本队员, key=copySceneConfPoId value=<key = roleId，value=TeamMemberVo> 
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,TeamMemberVo>> teamDungeonMemberVos = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,TeamMemberVo>>();
	
	/**
	 * 用于推送数据， 记录需要推送的角色Id
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> teamDungeonRoleids = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>>();
	
	/**
	 * 职业的升级技能编号
	 */
	public static ConcurrentHashMap<Integer, List<UpgradeSkillPo>> careerUpgradeSkillPos =new ConcurrentHashMap<Integer, List<UpgradeSkillPo>>();
	
	
	/**
	 * 任务MAP,根据类型来查询列表
	 */
	public static ConcurrentHashMap<Integer, List<TaskPo>> taskListByTypes = new ConcurrentHashMap<Integer, List<TaskPo>>();
	
	/**
	 * 完成任务时触发新的任务的MAP key=完成的任务编号 value=新激活的任务编号
	 */
	public static ConcurrentHashMap<Integer,Integer> finishTaskActiveNewTaskMap = new ConcurrentHashMap<Integer, Integer>();

	public static List<Integer> petRollIdGroup=new ArrayList<Integer>();

	public static List<Integer> petRollDiamondPossGroup1=new ArrayList<Integer>();

	public static List<Integer> petRollGoldPossGroup1=new ArrayList<Integer>();
	
	public static ConcurrentHashMap<Integer,Integer> carrrItemWarrior = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentHashMap<Integer,Integer> carrrItemRanger = new ConcurrentHashMap<Integer, Integer>();
	public static ConcurrentHashMap<Integer,Integer> carrrItemMage = new ConcurrentHashMap<Integer, Integer>();

	public static ConcurrentHashMap<Integer, ForbidVo> forbidMap = new ConcurrentHashMap<Integer, ForbidVo>();
	public static ConcurrentHashMap<Integer, BlockVo> blockMap = new ConcurrentHashMap<Integer, BlockVo>();
	/**
	 * 排行榜的hashMap,key=排行榜类型  value=排行榜列表
	 */
	public static ConcurrentHashMap<String, List<RankVo>> rankMap = new ConcurrentHashMap<String, List<RankVo>>();
	
	/**
	 * dota排行
	 */
	public static CopyOnWriteArrayList<RankVo> listDotaRank = new CopyOnWriteArrayList<RankVo>();
	
	/**
	 * 机器人需要的装备
	 */
	public static ConcurrentHashMap<Integer, EqpPo> robotEquip =new ConcurrentHashMap<Integer, EqpPo>();
	
	/**
	 * 机器人需要的宠物
	 */
	 public static ConcurrentHashMap<Integer, RpetPo> robotPet = new ConcurrentHashMap<Integer, RpetPo>();
	
	/**
	  * 任务要用的机器人角色
	  * @author peter
	  */
	 public static ConcurrentHashMap<Integer, RolePo> robotTaskRoles =new ConcurrentHashMap<Integer, RolePo>();
	 
	/**
	  * 竞技场要用的机器人角色
	  * @author peter
	  */
	 public static ConcurrentHashMap<Integer, RolePo> robotArenaRoles =new ConcurrentHashMap<Integer, RolePo>();
	 
	public static String chatString="aaaa";
	
	/**
	* 装备品质和装备等级随机对应的随机属性配置(key:品质，value（key:装备等级：value:配置）)
	*/
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, EquipRandomAtt>> equipRandomAtts = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,EquipRandomAtt>>();

	/**
	 * 装备品质和装备等级随机对应的随机属性配置(key:id,value(属性))
	 */
	public static ConcurrentHashMap<Integer, List<EquipAttachVo>> equipAttachVos = new ConcurrentHashMap<Integer, List<EquipAttachVo>>();
	 
	/**
	 * 宠物星座配置（key:星座ID,value:星座配置）
	 */
	public static ConcurrentHashMap<Integer, PetConstell> petConstell = new ConcurrentHashMap<Integer, PetConstell>();
	/**
	 * 宠物星座配置（key:星座ID,value:星座属性随机配置）
	 */
	public static ConcurrentHashMap<Integer, ConstellAttachVo> petConstellAttachVo = new ConcurrentHashMap<Integer, ConstellAttachVo>();
	/**
	 * 宠物星座属性配置（key:属性等级,value:（key:属性ID,value:属性值））
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, PetAttach>> petAttach = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, PetAttach>>();

	/**
	 * 宠物天赋配置（key:天赋ID,value:天赋配置）
	 */
	public static ConcurrentHashMap<Integer, PetTalentPo> petTalentIdMap = new ConcurrentHashMap<Integer, PetTalentPo>();
	/**
	 * 宠物天赋配置（key:天赋类型,value:(key:天赋等级,value:天赋配置)）
	 */
	public static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, PetTalentPo>> petTalentMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, PetTalentPo>>();
	/**
	 * 宠物技能学习配置（key:已学习技能数量,value:新技能概率总10000）
	 */
	public static ConcurrentHashMap<Integer, Integer> petSkillLearnMap = new ConcurrentHashMap<Integer, Integer>();


	 /**
	  * 等级奖励配置
	  */
	public static ConcurrentHashMap<Integer, LvConfigPo> mapLevelAward = new ConcurrentHashMap<Integer, LvConfigPo>();
	public static CopyOnWriteArrayList<LvConfigPo> listLevelAward = new CopyOnWriteArrayList<LvConfigPo>();
	public static CopyOnWriteArrayList<LvConfigPo> lvConfigPos = new CopyOnWriteArrayList<LvConfigPo>();
	/**
	 * 奖励找回
	 */
	public static ConcurrentHashMap<Integer, AwardRetrieve> awardRetrieveMap = new ConcurrentHashMap<Integer, AwardRetrieve>();
	
	/**
	 * 断线重连的状态结构
	 */
	public static ConcurrentHashMap<Integer, SessionSaveVo> sessionSaveVos = new ConcurrentHashMap<Integer, SessionSaveVo>();
	
	/**
	 * 副本配置（key:copySceneId,value:List<CopySceneConfPo>)
	 */
	public static ConcurrentHashMap<Integer, List<CopySceneConfPo>> copySceneConfMap = new ConcurrentHashMap<Integer, List<CopySceneConfPo>>();
	
	/**
	 * 所有的运营活动
	 */
	private static CopyOnWriteArrayList<LiveActivityPo> allLiveActivitys = new CopyOnWriteArrayList<LiveActivityPo>();
	
	/**
	 * 所有开启的运营活动
	 */
	public static CopyOnWriteArrayList<LiveActivityPo> allActiveLiveActivitys = new CopyOnWriteArrayList<LiveActivityPo>();
	
	/**
	 * 连斩BUFF
	 */
	public static ConcurrentHashMap<Integer, BuffPo> sustainKillMap = new ConcurrentHashMap<Integer, BuffPo>();
	
	/**
	 * 称号（key:称号等级）
	 */
	public static ConcurrentHashMap<Integer, TitlePo> titlePoMap = new ConcurrentHashMap<Integer, TitlePo>();
	
	 /**
	  * vip配置
	  */
	public static List<VipPo> listVipPo = new ArrayList<VipPo>();
	public static CopyOnWriteArrayList<RechargePo> listRechargePo = new CopyOnWriteArrayList<RechargePo>();
	public static ConcurrentHashMap<Integer, VipPo> mapVipPo = new ConcurrentHashMap<Integer, VipPo>();
	
	public static ConcurrentHashMap<String, ItemPo> itemNamesMap = new ConcurrentHashMap<String, ItemPo>();

	public static ConcurrentHashMap<Integer,FlagPo> sceneIdFlagMap = new ConcurrentHashMap<Integer, FlagPo>();
	
	/**
	 * 活动信息记录
	 */
	 public static ConcurrentHashMap<Integer, CopySceneActivityPo> mapCopySceneActivityPos = new ConcurrentHashMap<Integer, CopySceneActivityPo>();
	 
	 public static ConcurrentHashMap<String, String> languageMap = new ConcurrentHashMap<String, String>();
	 
	 public static ConcurrentHashMap<Integer, Boolean> redRoleDieMap = new ConcurrentHashMap<Integer, Boolean>();
	 
	 /**
	  * 协议响应频率
	  */
	 public static ConcurrentHashMap<String, Long> mapProtocolFrequencyResponse = new ConcurrentHashMap<String, Long>();
	 
	 /**
	  * 判断接收消息时间/次数 id时间;num次数
	  */
	 public static ConcurrentHashMap<String, IdLongVo> mapResponse = new ConcurrentHashMap<String, IdLongVo>();
	 
	 public  static List<Integer> schedAbroadIds =new ArrayList<Integer>();
	
	 public static int maxRoleLv=0;
	 
	 public static void loadAndClean(){
		
		GlobalPo.init(1);
		syncForbids();
		syncBlockChat();
    	AbroadPo.freshAllNotice();
    	reloadServerAndClientLang();
		initializeCopySceneActivityPo();
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<LvConfigPo> LvConfigList =gameDataTemplate.getDataList(LvConfigPo.class);
		RoleType.ROLE_EXPS=new int[LvConfigList.size()];
		mapLevelAward.clear();
		listLevelAward.clear();
		lvConfigPos.clear();
		GameUtil.reloadFilterString();
		for(int i=0;i<LvConfigList.size();i++){
			LvConfigPo lvConfigPo = LvConfigList.get(i);
			boolean bl = true;
			for(int j=0;j<lvConfigPos.size();j++)
			{
				if(lvConfigPo.getPlayerLevel() < lvConfigPos.get(j).getPlayerLevel())
				{
					lvConfigPos.add(j,lvConfigPo);
					bl = false;
					break;
				}
			}
			if(bl)
				lvConfigPos.add(lvConfigPo);
			RoleType.ROLE_EXPS[i]=lvConfigPo.getExp();
			if(LvConfigList.get(i).getLevelAwrod() != null && !"".equals(lvConfigPo.getLevelAwrod())){
				mapLevelAward.put(lvConfigPo.getPlayerLevel(), lvConfigPo);
				listLevelAward.add(lvConfigPo);
			}
		}
		
		sceneIdFlagMap.clear();
		List<FlagPo> allList = new ArrayList<FlagPo>();
		List<FlagPo> flagPos = BaseDAO.instance().getDBList(FlagPo.class);
		for (FlagPo flagPo : flagPos) {
			flagPo = FlagPo.findRealEntity(FlagPo.class, flagPo.getId());
			allList.add(flagPo);
		}
		for(int i=0;i<allList.size();i++){
			FlagPo flagPo = allList.get(i);
			sceneIdFlagMap.put(flagPo.getSceneId(), flagPo);
		}
		
		titlePoMap.clear();
		List<TitlePo> titleList =gameDataTemplate.getDataList(TitlePo.class);
		for(int i=0;i<titleList.size();i++){
			TitlePo title = titleList.get(i);
			titlePoMap.put(title.getLv(), title);
		}
		
		List tcDrops =gameDataTemplate.getDataList(DropPo.class);

		idTcGroupsMap.clear();
		for (Object object : tcDrops) {
			DropPo tcDropPo = (DropPo) object;
			if(!idTcGroupsMap.containsKey(tcDropPo.getGroupsId())){
				idTcGroupsMap.put(tcDropPo.getGroupsId(), new ArrayList<DropPo>());
			}
			idTcGroupsMap.get(tcDropPo.getGroupsId()).add(tcDropPo);
		}
		
		careerUpgradeSkillPos.clear();
		List UpgradeSkillPos =gameDataTemplate.getDataList(UpgradeSkillPo.class);
		for (Object object : UpgradeSkillPos) {
			UpgradeSkillPo upgradeSkillPo = (UpgradeSkillPo) object;
			if(!careerUpgradeSkillPos.containsKey(upgradeSkillPo.getCareer())){
				careerUpgradeSkillPos.put(upgradeSkillPo.getCareer(), new ArrayList<UpgradeSkillPo>());
			}
			careerUpgradeSkillPos.get(upgradeSkillPo.getCareer()).add(upgradeSkillPo);
		}
		
		
		List<TaskPo> taskList =gameDataTemplate.getDataList(TaskPo.class);
		taskListByTypes.clear();
		for (TaskPo taskPo : taskList) {
			for (Integer activeTask : taskPo.listActiveTasks) {
				finishTaskActiveNewTaskMap.put(activeTask, taskPo.getId());
			}
			if(!taskListByTypes.containsKey(taskPo.getTaskType())){
				taskListByTypes.put(taskPo.getTaskType(), new ArrayList<TaskPo>());
			}
			taskListByTypes.get(taskPo.getTaskType()).add(taskPo);
		}
		
		reloadRankArenas();
		
		
		List<PetRollPo> petRollList =gameDataTemplate.getDataList(PetRollPo.class);
		petRollIdGroup.clear();
		petRollDiamondPossGroup1.clear();
		petRollGoldPossGroup1.clear();
		
		
		for (PetRollPo petRollPo : petRollList) {
			petRollIdGroup.add(petRollPo.getPetId());
			petRollDiamondPossGroup1.add(petRollPo.getDiamondGroup1());
			petRollGoldPossGroup1.add(petRollPo.getGoldGroup1());
		}
		
		carrrItemWarrior.clear();
		carrrItemRanger.clear();
		carrrItemMage.clear();
		
		for(int i=0;i<XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.size();i++){
			ReplaceItem replaceItem = XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.get(i);
			List<Integer> ids = StringUtil.getListByStr(replaceItem.replaceItems,"|");
			carrrItemWarrior.put(replaceItem.itemId, ids.get(0));
			carrrItemRanger.put(replaceItem.itemId, ids.get(1));
			carrrItemMage.put(replaceItem.itemId, ids.get(2));
		}
		
		createRobotEquip();
		createTaskRobot();
		createArenaRobotRank();
		
		equipRandomAtts.clear();
		List<EquipRandomAtt> list = XmlCache.xmlFiles.constantFile.equipRandomAtts.equipRandomAtt;
		for(EquipRandomAtt equipRandomAtt:list)
		{
			int quality = equipRandomAtt.quality;
			int level = equipRandomAtt.level;
			ConcurrentHashMap<Integer, EquipRandomAtt> equipQualite = equipRandomAtts.get(quality);
			if(equipQualite == null)
			{
				equipQualite = new ConcurrentHashMap<Integer, EquipRandomAtt>();
				equipRandomAtts.put(quality, equipQualite);
			}
			equipQualite.put(level, equipRandomAtt);
			List<EquipAttachVo> eaVo = new ArrayList<EquipAttachVo>();
			String[] strs = StringUtil.split(equipRandomAtt.equipAttachs, ",");
			for(String sts:strs)
			{
				EquipAttachVo ea = new EquipAttachVo();
				ea.loadProperty(sts, "|");
				eaVo.add(ea);
			}
			equipAttachVos.put(equipRandomAtt.id, eaVo);
		}
		petConstell.clear();
		petConstellAttachVo.clear();
		petAttach.clear();
		List<PetConstell> petConstells = XmlCache.xmlFiles.constantFile.pet.petConstells.petConstell;
		for(PetConstell petC:petConstells)
		{
			petConstell.put(petC.constellId, petC);
			ConstellAttachVo caVo = new ConstellAttachVo();
			caVo.loadProperty(petC.constellAttachs, ",");
			petConstellAttachVo.put(petC.constellId, caVo);
		}
		List<PetAttach> petAttachs = XmlCache.xmlFiles.constantFile.pet.petAttachs.petAttach;
		for(PetAttach petA:petAttachs)
		{
			int level = petA.attachLevel;
			ConcurrentHashMap<Integer, PetAttach> petALevel = petAttach.get(level);
			if(null == petALevel)
			{
				petALevel = new ConcurrentHashMap<Integer, PetAttach>();
				petAttach.put(level, petALevel);
			}
			petALevel.put(petA.attachId, petA);
		}
		
		petTalentIdMap.clear();
		petTalentMap.clear();
		List<PetTalentPo> petTalentPoList = gameDataTemplate.getDataList(PetTalentPo.class);
		for(PetTalentPo petTalentPo:petTalentPoList)
		{
			petTalentIdMap.put(petTalentPo.getId(), petTalentPo);
			ConcurrentHashMap<Integer, PetTalentPo> map = petTalentMap.get(petTalentPo.talentType);
			if(map == null)
			{
				map = new ConcurrentHashMap<Integer, PetTalentPo>();
				petTalentMap.put(petTalentPo.talentType, map);
			}
			map.put(petTalentPo.talentLevel, petTalentPo);
		}
		
		petSkillLearnMap.clear();
		String petSkillLearn = XmlCache.xmlFiles.constantFile.pet.petSkill.learn;
		String[] strs = StringUtil.split(petSkillLearn, ",");
		for(String str:strs)
		{
			String[] vals = StringUtil.split(str, "|");
			petSkillLearnMap.put(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
		}
		

		
		awardRetrieveMap.clear();
		List<AwardRetrieve> awardRetrieves = XmlCache.xmlFiles.constantFile.awardRetrieves.awardRetrieve;
		for(AwardRetrieve awardRetrieve:awardRetrieves)
		{
			awardRetrieveMap.put(awardRetrieve.id, awardRetrieve);
		}
		copySceneConfMap.clear();
		List<CopySceneConfPo> cscPos = gameDataTemplate.getDataList(CopySceneConfPo.class);
		for(CopySceneConfPo cscPo:cscPos)
		{
			List<CopySceneConfPo> cscPoList = copySceneConfMap.get(cscPo.getCopySceneId());
			if(null == cscPoList)
			{
				cscPoList = new ArrayList<CopySceneConfPo>();
				copySceneConfMap.put(cscPo.getCopySceneId(), cscPoList);
			}
			cscPoList.add(cscPo);
		}
		sustainKillMap.clear();
		List<SustainKill> skList = XmlCache.xmlFiles.constantFile.sustainKills.sustainKill;
		for(SustainKill sk:skList)
		{
			sustainKillMap.put(sk.killNum, BuffPo.findEntity(sk.buffId));
		}
		
		
		
		mapVipPo.clear();
		listVipPo.clear();
		List<VipPo> vipPos = gameDataTemplate.getDataList(VipPo.class);
		for(VipPo vipPo:vipPos)
		{
			boolean bl = true;
			for(int i=0;i<listVipPo.size();i++)
			{
				if(vipPo.getVipLv() < listVipPo.get(i).getVipLv())
				{
					listVipPo.add(i, vipPo);
					bl =false;
					break;
				}
			}
			if(bl)
				listVipPo.add(vipPo);
			mapVipPo.put(vipPo.getVipLv(), vipPo);
		}
		
		listRechargePo.clear();
		List<RechargePo> rechargeList =GameDataTemplate.getDataList(RechargePo.class);
		for(RechargePo rechargePo : rechargeList){
			boolean bool = true;
			for(int i=0; i < listRechargePo.size(); i++){
				if(rechargePo.getGroupId().intValue() == listRechargePo.get(i).getGroupId()){
					bool = false;
				}
			}
			if(bool){
				listRechargePo.add(rechargePo);
			}
		}
		
		
		syncActiveLiveActivitys();
//		initArenaRank();

		itemNamesMap.clear();
		List<ItemPo> items  = gameDataTemplate.getDataList(ItemPo.class);
		for (ItemPo itemPo : items) {
			int equipPower=0;
			for(List<Integer> batAttrExp : itemPo.listEqpBatAttrExp){				
					equipPower+=RoleType.batMeleeWarriorOrArcher[batAttrExp.get(0).intValue()-1]*batAttrExp.get(1);
			}
			itemPo.equipPower=equipPower;
//			System.out.println("id:"+itemPo.getId() + "; name:"+itemPo.getName() +"; eqpBatAttrExp:"+itemPo.getEqpBatAttrExp()+"; equipPower:"+itemPo.equipPower);
			itemNamesMap.put(itemPo.getName(), itemPo);
		}
		
//		GameDataTemplate gameDataTemplate = (GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
//		List<LvConfigPo> LvConfigList = gameDataTemplate.getDataList(LvConfigPo.class);
		// 升级配置表取最大等级
		maxRoleLv = LvConfigList.get(LvConfigList.size() - 1).getPlayerLevel();
		synProducts();
	}
	
	public static void syncActiveLiveActivitys() {
		allActiveLiveActivitys.clear();
		allLiveActivitys.clear();
		List<LiveActivityPo> list2 = BaseDAO.instance().getDBList(LiveActivityPo.class);
		for (LiveActivityPo liveActivityPo : list2) {
			LiveActivityPo liveActivityPo2 = LiveActivityPo.findRealEntity(LiveActivityPo.class, liveActivityPo.getId());
			allLiveActivitys.add(liveActivityPo2);
		}
		
		for (LiveActivityPo liveActivityPo : allLiveActivitys) {
			if((System.currentTimeMillis())>liveActivityPo.getEndTime() && liveActivityPo.getStatus()==0){
				liveActivityPo.processResult();
			}
		}
		
		for (LiveActivityPo liveActivityPo : allLiveActivitys) {
			if((System.currentTimeMillis())>liveActivityPo.getStartTime() && (System.currentTimeMillis())<liveActivityPo.getEndTime()){
				boolean matched=false;
				for (LiveActivityPo liveActivityPo2 : allActiveLiveActivitys) {
					if(liveActivityPo==liveActivityPo2){
						matched=true;
					}
				}
				if(!matched){
					allActiveLiveActivitys.add(liveActivityPo);
				}
			}
		}
	
	
//		List<LiveActivityPo> toRemove = new ArrayList<LiveActivityPo>();
//		for (LiveActivityPo liveActivityPo : allActiveLiveActivitys) {
//			if((System.currentTimeMillis())>liveActivityPo.getStartTime() && (System.currentTimeMillis())<liveActivityPo.getEndTime()){
//				
//			}
//			else{
//				toRemove.add(liveActivityPo);
//			}
//		}
//		allActiveLiveActivitys.removeAll(toRemove);
		List<LiveActivityPo> alls=new ArrayList<LiveActivityPo>();
		alls.addAll(allActiveLiveActivitys);
		Collections.sort(alls);	
		
		allActiveLiveActivitys.clear();
		allActiveLiveActivitys.addAll(alls);
	}
	
	public static List<LiveActivityPo> fetchLiveActivityPosAll(){
		return allLiveActivitys;
	}

	/**
	 * 
	 * 方法功能:加载竞技场排名信息
	 * 更新时间:2014-7-7, 作者:johnny
	 */
	private static void reloadRankArenas() {
		
		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
		CheckcCircleBean checkcCircleBean = new CheckcCircleBean(100);
		while(true){
			checkcCircleBean.count();
			String sql ="select role_id from "+BaseStormSystemType.USER_DB_NAME+".u_po_rank_arena group by role_id having count(role_id)>1";
			List<Integer> roleIds =baseDAO.jdbcTemplate.queryForList(sql,Integer.class);
			if(roleIds.size()<=0){
				break;
			}
			for (Integer roleId : roleIds) {
				String sql2 ="delete from "+BaseStormSystemType.USER_DB_NAME+".u_po_rank_arena where role_id ="+roleId+" order by arena_rank desc limit 1 ";
				baseDAO.execute(sql2);
			}
		}
		
		rankArenaMaps.clear();
		rankArenaRoleIdMaps.clear();
		List<RankArenaPo> rankArenaPos = baseDAO.dBfind("from RankArenaPo order by arenaRank asc");
		for (int i=1;i<=rankArenaPos.size();i++) {
			RankArenaPo rankArenaPo = RankArenaPo.findEntity(rankArenaPos.get(i-1).getId());
			if(rankArenaPo.getRoleName()==null){
				rankArenaPo.setRoleName("匿名英雄");
			}
			rankArenaPo.setArenaRank(i);
			rankArenaMaps.put(rankArenaPo.getArenaRank(), rankArenaPo);
			rankArenaRoleIdMaps.put(rankArenaPo.getRoleId(), rankArenaPo);
		}
		

	}
	
	/**
	 * 方法功能:根据角色编号获取竞技场排行
	 * 更新时间:2014-6-25, 作者:johnny
	 * @param roleId
	 * @return
	 */
	private static RankArenaPo fetchArenaRankByRoleId(Integer roleId) {
		return GlobalCache.rankArenaRoleIdMaps.get(roleId);
	}
	
	/**
	 * 
	 * 方法功能:更新竞技场角色排行
	 * 更新时间:2014-6-25, 作者:johnny
	 * @param rank
	 * @param rolePo
	 */
	public static void updateArenaRank(int rank, RolePo rolePo) {
		RankArenaPo rankArenaPo = fetchArenaRankByRoleId(rolePo.getId());
		if(rankArenaPo==null){
			rankArenaPo = new RankArenaPo();
			rankArenaPo.loadByRolePo(rolePo);
			rankArenaPo.setArenaRank(rank);
			BaseDAO.instance().insert(rankArenaPo);
		}
		else{
			rankArenaPo.loadByRolePo(rolePo);
			rankArenaPo.setArenaRank(rank);
		}

		GlobalCache.rankArenaMaps.put(rank, rankArenaPo);
		GlobalCache.rankArenaRoleIdMaps.put(rolePo.getId(), rankArenaPo);
	}

	public static void checkAddNewArena(RolePo rolePo) {
//		if(GlobalCache.rankArenaMaps.size()<=20){
			if(!GlobalCache.rankArenaRoleIdMaps.containsKey(rolePo.getId())){
				GlobalCache.updateArenaRank(GlobalCache.rankArenaMaps.size()+1,rolePo);
			}
//		}
	}
	
	
	/**
	 * 创建机器人装备
	 */
	public static void createRobotEquip(){
		robotEquip.clear();
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<ItemPo> itemList =gameDataTemplate.getDataList(ItemPo.class);
		for(int i = 0; i < itemList.size(); i++){
			ItemPo item = itemList.get(i);
			if(item.wasEquip()){
				EqpPo equipPo = new EqpPo();
				equipPo.setId(900000+i);
				equipPo.setItemId(item.getId());
				equipPo.setPowerLv(1);
				equipPo.setBindStatus(0);
				robotEquip.put(equipPo.getItemId(), equipPo);
			}
		}
	}
	
	
	/**
	 * 服务器竞技场初始500机器人
	 */
	public static void createArenaRobotRank(){
		robotArenaRoles.clear();
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<RobotPo> robotList =gameDataTemplate.getDataList(RobotPo.class);
		for(int i = 0; i < robotList.size(); i++){
			RobotPo robot = robotList.get(i);
			RolePo robotRolePo = new RolePo();
			robotRolePo.robot = true;
			robotRolePo.setId(robot.getId());
			robotRolePo.setName(robot.getName());
			robotRolePo.setCareer(robot.getCareer());
			robotRolePo.setEquipWeaponId(robot.getEquipWeaponId());
			robotRolePo.setEquipArmorId(robot.getEquipArmorId());
			robotRolePo.setLv(robot.getLv());
			robotRolePo.setBattlePower(robot.getPower());
			robotRolePo.setWingEquipStatus(1);
			robotRolePo.setWingWasHidden(1);
			robotRolePo.setWingStar(robot.getWingStar());
			robotRolePo.setRoomId(20100002);
			List<UpgradeSkillPo> upgradeSkillPos = careerUpgradeSkillPos.get(robotRolePo.getCareer());
			 if(upgradeSkillPos!=null){
				 for(int n=0;n<upgradeSkillPos.size();n++){
					 robotRolePo.listSkillVos.add(new IdNumberVo(upgradeSkillPos.get(n).getSkillId(),1));
				 }
			 }
			robotRolePo.calculateRobotBat(robot.listBatExp);
			robotArenaRoles.put(robotRolePo.getId(), robotRolePo);
			checkAddNewArena(robotRolePo);
		}
	}
	
	/** 创建竞技场需要的机器人 */
	public static RolePo createArenaRobot(){
		RolePo robot =new RolePo();
		robot.robot = true;
		robot.setId(arenaRobotRolePoid++);
		robot.setRoomId(20100002);
		return robot;
	}

	/**
	 * 根据职业获取机器人RankVo对象（用于膜拜）
	 * @param career
	 * @return
	 */
	public static RankVo fetchRankVoByRobotCareer(Integer career){
		RankVo rankVo = new RankVo();
		for(Integer roleId : GlobalCache.robotArenaRoles.keySet()){
			RolePo robotRolePo = GlobalCache.robotArenaRoles.get(roleId);
			if(robotRolePo.getCareer().intValue() == career.intValue()){
				rankVo = new RankVo();
				rankVo.id=1000000+RandomUtil.randomInteger(100000);
				rankVo.roleName = robotRolePo.getName();
				rankVo.roleId=robotRolePo.getId();
				List<IdNumberVo> hiddenFashions = new ArrayList<IdNumberVo>();
				hiddenFashions.add(new IdNumberVo(1, 1));
				hiddenFashions.add(new IdNumberVo(2, 1));
				CommonAvatarVo commonAvatarVo = CommonAvatarVo.build(
						robotRolePo.getEquipWeaponId(), robotRolePo.getEquipArmorId(),
						robotRolePo.getFashion(),
						robotRolePo.getCareer(), robotRolePo.getWingWasHidden(), robotRolePo.getWingStar(),hiddenFashions);
				rankVo.weaponAvatar = commonAvatarVo.weaponAvatar;
				rankVo.armorAvatar = commonAvatarVo.modelAvatar;
				rankVo.wingAvatar = commonAvatarVo.wingAvatar;
				break;
			}
		}
		return rankVo;
	}
	
	
	/**
	 * 创建任务机器人
	 */
	public static void createTaskRobot(){
		robotTaskRoles.clear();
		robotPet.clear();
		for (int i = 1; i <= 3; i++) {
			// 生成角色ID
			int roleId = 9000000+i;
			// 创建角色对象，添加基础属性
			RolePo robot =new RolePo();
			robot.robot = true;
			robot.taskRobot = true;
			robot.setId(roleId);
			robot.setLv(20);
			robot.setExp(0);
			robot.setPkValue(10000);
			robot.setPkStatus(Fighter.PK_STATUS_RED);
			robot.setPkLastRecoverTime(System.currentTimeMillis()+60*60*24*1000*1000);
			robot.setVipLv(0);
			int xRandom = IntUtil.getRandomInt(1, 3)*10000;
			int yRandom = IntUtil.getRandomInt(1, 3)*10000;
			if(i%4==2)
			{
				xRandom = -xRandom;
			}
			else if(i%4 == 3)
			{
				xRandom = -xRandom;
				yRandom = -yRandom;
			}else if(i%4 == 0)
			{
				yRandom = -yRandom;
			}
			
			if(i%3 == 1)
			{
				robot.setCareer(RoleType.CAREER_WARRIOR);	
				robot.setName(GameUtil.createRandomRobotName(1));
				robot.equipArmor=robotEquip.get(310500001);
				robot.equipBracer=robotEquip.get(310800001);
				robot.equipShoe=robotEquip.get(310700002);
				robot.equipBracelet=robotEquip.get(310400001);
				robot.setX(490000+xRandom);
				robot.setY(210000);
				robot.setZ(660000+yRandom);
				robot.listSkillFormation.add(110001);
				robot.listSkillFormation.add(110002);
				robot.listSkillFormation.add(110003);
				robot.listSkillFormation.add(110004);
				robot.listSkillFormation.add(110005);
				robot.listSkillFormation.add(110006);
			}
			else if(i%3 == 2)
			{
				robot.setCareer(RoleType.CAREER_RANGER);
				robot.setName(GameUtil.createRandomRobotName(0));
				robot.equipArmor=robotEquip.get(320500001);
				robot.equipBracer=robotEquip.get(320800001);
				robot.equipShoe=robotEquip.get(320700002);
				robot.equipBracelet=robotEquip.get(320400001);
				robot.setX(450000+xRandom);
				robot.setY(210000);
				robot.setZ(560000+yRandom);
				robot.listSkillFormation.add(120001);
				robot.listSkillFormation.add(120002);
				robot.listSkillFormation.add(120003);
				robot.listSkillFormation.add(120004);
				robot.listSkillFormation.add(120005);
				robot.listSkillFormation.add(120006);
			}
			else if(i%3 == 0)
			{
				robot.setCareer(RoleType.CAREER_MAGE);
				robot.setName(GameUtil.createRandomRobotName(0));
				robot.equipArmor=robotEquip.get(330500001);
				robot.equipBracer=robotEquip.get(330800001);
				robot.equipShoe=robotEquip.get(330700002);
				robot.equipBracelet=robotEquip.get(330400001);
				robot.setX(450000+xRandom);
				robot.setY(210000);
				robot.setZ(620000+yRandom);
				robot.listSkillFormation.add(130001);
				robot.listSkillFormation.add(130002);
				robot.listSkillFormation.add(130003);
				robot.listSkillFormation.add(130004);
				robot.listSkillFormation.add(130005);
				robot.listSkillFormation.add(130006);
			}
			robot.setRoomId(20101099);
			List<UpgradeSkillPo> upgradeSkillPos = careerUpgradeSkillPos.get(robot.getCareer());
			 if(upgradeSkillPos!=null){
				 for(int n=0;n<upgradeSkillPos.size();n++){
					 robot.listSkillVos.add(new IdNumberVo(upgradeSkillPos.get(n).getSkillId(),1));
				 }
			 }
			 robot.setWingEquipStatus(1);
			 robot.setWingWasHidden(1);
			 robot.setWingStar(109);
			 robot.calculateBat(0);
			 robot.listBufferStatus = new ArrayList<List<String>>();
			 // 增加机器人宠物
//			 PetPo petPo =PetPo.findEntity(30);
//			 RpetPo rpetPo = new RpetPo();
//			 rpetPo.setId(900000+i);
//			 rpetPo.setExp(0);
//			 rpetPo.setLv(1);
//			 rpetPo.setName(petPo.getName());
//			 rpetPo.setPetId(petPo.getId());
//			 rpetPo.setStep(0);
//			 robot.listRpets.add(rpetPo);
//			 robot.setRpetFighterId(rpetPo.getId());
//			 robotPet.put(rpetPo.getId(), rpetPo);
			 robotTaskRoles.put(robot.getId(), robot);
		}
	}
	
	/**
	 * 根据装备品质和装备等级获取随机属性
	 * @param quality 装备品质
	 * @param level 装备等级
	 * @return 属性ID:属性值|属性ID:属性值...
	 */
	public static String getRandomAttach(int quality, int level)
	{
		String attach = "";
		ConcurrentHashMap<Integer, EquipRandomAtt> equipRandomAttMap = equipRandomAtts.get(quality);
		if(equipRandomAttMap != null)
		{
			EquipRandomAtt equipRandomAtt = equipRandomAttMap.get(level);
			if(null != equipRandomAtt)
			{
				int atbNum=IntUtil.getRandomInt(equipRandomAtt.minAttNum, equipRandomAtt.maxAttNum);
				List<EquipAttachVo> equipAttachs = equipAttachVos.get(equipRandomAtt.id);
				if(null != equipAttachs)
				{
					int probability = 0;
					for(EquipAttachVo ea:equipAttachs)
					{
						probability+=ea.probability;
					}
					StringBuilder sb = new StringBuilder();
					for(int i=0;i<atbNum;i++)
					{
						int random = IntUtil.getRandomInt(1, probability);
						for(EquipAttachVo ea:equipAttachs)
						{
							if(random<=ea.probability)
							{
								int attValue=IntUtil.getRandomInt(ea.minAtt, ea.maxAtt);
								sb.append(ea.attachId);
								sb.append("|");
								sb.append(attValue);
								sb.append(",");
								break;
							}
							random-=ea.probability;
						}
						attach = sb.toString();
						if(attach.length() > 0)
						attach = attach.substring(0, attach.length()-1);
					}
				}
			}
		}
		return attach;
	}
	
	/**
	 * 根据buffID获取加成属性
	 * @param effectType 作用对象
	 * @param buffId
	 * @return 没有则为null
	 */
//	public static List<List<Integer>> getAttackByBuffId(EffectType effectType, int buffId)
//	{
//		List<List<Integer>> attacks = null;
//		BuffPo buff = buffMap.get(buffId);
//		if(effectType != null && buff != null && buff.effectTypes.containsKey(effectType))
//		{
//			attacks = new ArrayList<List<Integer>>();
//			if(buff.getBuffType() == BuffType.ATTACK.getType())
//			{
//				List<BufferEffetVo> bes = buff.bufferEffetVos;
//				for(BufferEffetVo be:bes)
//				{
//					List<Integer> attack = new ArrayList<Integer>();
//					attack.add(be.effectType);
//					attack.add(Integer.parseInt(be.parExps[0]));
//					attacks.add(attack);
//				}
//			}
//		}
//		return attacks;
//	}
	
	/**
	 * 初始化副本活动状态
	 */
	public static void initializeCopySceneActivityPo(){
		BaseDAO baseDAO = BaseDAO.instance();
		List<CopySceneActivityPo> copySceneActivityPos = baseDAO.dBfind("from CopySceneActivityPo ");
		String sql = "select id from "+BaseStormSystemType.USER_DB_NAME+".po_copy_scene_conf";
		List<Integer> activityIds =baseDAO.jdbcTemplate.queryForList(sql,Integer.class);
		
		if(copySceneActivityPos == null || copySceneActivityPos.size() == 0 || copySceneActivityPos.size() != activityIds.size()){
			String cleanSql="truncate table "+BaseStormSystemType.USER_DB_NAME+".u_po_copy_scene_activity";
			baseDAO.jdbcTemplate.execute(cleanSql);
			List<CopySceneConfPo> copySceneConfPos = baseDAO.dBfind("from CopySceneConfPo ");
			for(int i=0; i<copySceneConfPos.size(); i++){
				CopySceneConfPo cscp = copySceneConfPos.get(i);
				CopySceneActivityPo copySceneActivityPo = new CopySceneActivityPo();
				copySceneActivityPo.setId(cscp.getId());
				copySceneActivityPo.setActivityWasOpen(0);
				copySceneActivityPo.setActivityType(cscp.getType());
				baseDAO.insert(copySceneActivityPo);
			}
		}
		
		List<CopySceneActivityPo> csap = baseDAO.dBfind("from CopySceneActivityPo ");
		if(csap != null && csap.size() != 0){
			mapCopySceneActivityPos.clear();
			for(int i=0; i < csap.size(); i++){
				CopySceneActivityPo adp = CopySceneActivityPo.findEntity(csap.get(i).getId());
				mapCopySceneActivityPos.put(adp.getId(), adp);
			}
		}
		
		
	}
	
	public static List<CopySceneActivityVo> fetchCopySceneActivityVoList(){
//		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
		List<CopySceneActivityVo> csavList = new ArrayList<CopySceneActivityVo>();
		for(CopySceneActivityPo copySceneActivityPo : mapCopySceneActivityPos.values()){
//			System.out.println("copySceneActivityPo =" +copySceneActivityPo.getId());
			CopySceneActivityVo copySceneActivityVo = new CopySceneActivityVo();
			copySceneActivityVo.id = copySceneActivityPo.getId();
			copySceneActivityVo.activityWasOpen = copySceneActivityPo.getActivityWasOpen();
			if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
				copySceneActivityVo.beginTime = IntUtil.longConvertInt(copySceneActivityPo.getBeginTimeNext());
				copySceneActivityVo.endTime = IntUtil.longConvertInt(copySceneActivityPo.getEndTimeNext());				
			}else if(copySceneActivityPo.getActivityWasOpen().intValue() == 1 ){
				long beginTime = 0l;
			    if(copySceneActivityPo.getBeginTime() != null){
			    	beginTime = copySceneActivityPo.getBeginTime();
			    }
				if(copySceneActivityPo.getBeginTime() == null || copySceneActivityPo.getBeginTime().longValue() == 0l){
					beginTime = System.currentTimeMillis();
				}
				copySceneActivityVo.beginTime = IntUtil.longConvertInt(beginTime);
				copySceneActivityVo.endTime = IntUtil.longConvertInt(copySceneActivityPo.getEndTimeNext());	
			}
			csavList.add(copySceneActivityVo);
		}
		return csavList;
		
	}



	public static List<LiveActivityPo> fetchActiveLiveActivitys() {
		return allActiveLiveActivitys;
	}
	
	public static List<LiveActivityPo> fetchLiveActivityPoByType(Integer type){
		List<LiveActivityPo> liveActivityPos = new ArrayList<LiveActivityPo>();
		for (LiveActivityPo lap : GlobalCache.fetchActiveLiveActivitys()) {
			if(lap.getType() == type.intValue()){
				liveActivityPos.add(lap);
				break;
			}
		}
		return liveActivityPos;
	}
	
	/**
	 * 双倍活动类型
	 * @param rateType
	 * @return
	 */
	public static Integer liveActivityDoubleType(Integer rateType){
		Integer percent = 0;
		List<LiveActivityPo> list = new ArrayList<LiveActivityPo>();
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivityDESC){
				list.add(liveActivityPo);
			}
		}
		for(LiveActivityPo liveActivityPo : list){
			if(liveActivityPo.getRateItems()!=null){
				IdNumberVo idNumberVo = IdNumberVo.create(liveActivityPo.getRateItems());
				if(idNumberVo.getId().intValue() == rateType.intValue()){
					percent=idNumberVo.getNum();
					break;
				}
			}
		}
		return percent;
	}
	
	
	public static PetSkillPo fetchPetSkillPoByBuffPoId(Integer buffPoId){
		PetSkillPo petSkillPo = null;
		GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		List<PetSkillPo> petSkillPoList =gameDataTemplate.getDataList(PetSkillPo.class);
		Lable:		for(PetSkillPo psp : petSkillPoList){
			for(Integer i : psp.buffIdList){
				if(i.intValue() == buffPoId.intValue()){
					petSkillPo = psp;
					break Lable;
				}
			}
		}
		return petSkillPo;
	}
	
	
	/**
	 * 加载语言Map
	 */
	public static void loadLanguageMap(String type){
		languageMap.clear();
		Map<String, String> map = fetchLanguageByType(type);
		for( String i : map.keySet()){

			if(map.get(i)!=null){
				languageMap.put("key"+i, map.get(i));
			}
			else{
				languageMap.put("key"+i, "nokey:"+i);
			}

		}
	}
	
	/**
	 * 检查协议访问频率
	 * @param key
	 * @param time
	 * @param bool 是否提示
	 */
	public static boolean checkProtocolFrequencyResponse(String key, long time, boolean bool){
		long currentTime = System.currentTimeMillis();
		Long lastTime = mapProtocolFrequencyResponse.get(key);
//		if(lastTime!=null){
//			System.out.println("key="+key);
//			System.out.println((currentTime - lastTime.longValue()));			
//		}
		if(lastTime != null && ((currentTime - lastTime.longValue()) < time)){
			if(bool){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2264") );
			}
			return true;
		}
		mapProtocolFrequencyResponse.put(key, System.currentTimeMillis());
		return false;
	}
	
	/**
	 * 收到消息验证
	 * @param key
	 * @param time
	 * @param num
	 * @param bool
	 * @return
	 */
	public static boolean checkMapResponse(String key, long time, long num, boolean bool){
		boolean currentbool =false;
		long currentTime = System.currentTimeMillis();
		IdLongVo idLongVo = mapResponse.get(key);
		if(idLongVo != null){
			long lastTime=idLongVo.getId();
			long currentNum = idLongVo.getNum();
			if(((currentTime - lastTime) < time) && currentNum>=num){
				mapResponse.put(key, new IdLongVo(System.currentTimeMillis(), 1L));	
				if(bool){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2264") );
				}
				currentbool= true;
			}else{
				if((currentTime - lastTime) > time){
					mapResponse.put(key, new IdLongVo(System.currentTimeMillis(), 1L));	
				}else{
					idLongVo.addNum(1L);
				}
			}
			
//			System.out.println("idLongVo="+idLongVo+"; time="+(currentTime - lastTime));
		}else{
			mapResponse.put(key, new IdLongVo(System.currentTimeMillis(), 1L));
		}
		return currentbool;
	}

	public static void syncForbids() {
		forbidMap.clear();
		for (Object obj : BaseDAO.instance().getDBList(ForbidPo.class)) {
			ForbidPo forbidPo = (ForbidPo) obj;
			if(forbidPo.getRoleId() != null){
				forbidMap.put(forbidPo.getRoleId(), ForbidVo.fromPo(forbidPo));
			}
		}
	}
	
	public static void syncBlockChat() {
		blockMap.clear();
		for (Object obj : BaseDAO.instance().getDBList(BlockPo.class)) {
			BlockPo blockPo = (BlockPo) obj;
			blockMap.put(blockPo.getRoleId(), BlockVo.fromPo(blockPo));
		}
	}
	
	public static synchronized int addRoleToDotaRankVo(int val, RolePo rolePo){
		int rank=0;
		boolean resort=false;
		boolean matched=false;
//		List<RankVo> list = new ArrayList<RankVo>();
		for (RankVo rankVo : listDotaRank) {
			if(rankVo.roleId==rolePo.getId().intValue()){
				rankVo.rolePower=val;
				rankVo.towerCurrentLv=rolePo.getTowerCurrentLv();
				matched=true;
				resort=true;
				break;
			}
		}
		if(matched==false && (listDotaRank.size()<100 || val>=listDotaRank.get(99).rolePower)){
			RankVo rankVo = new RankVo();
			rankVo.id=0;
			rankVo.roleId=rolePo.getId();
			rankVo.roleName=rolePo.getName();
			rankVo.type=RankType.BATTLE_RANK_TYPE_DOTA_LV;
			rankVo.roleCareer=rolePo.getCareer();
			rankVo.wingStar=rolePo.getWingStar();
			rankVo.rankGold=rolePo.getGold();
			rankVo.roleLv=rolePo.getLv();
			rankVo.rankLv=0;
			rankVo.rolePower=val;
			rankVo.weaponAvatar="0";
			rankVo.towerCurrentLv=rolePo.getTowerCurrentLv();
			CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
			rankVo.makeAvatars(commonAvatarVo);
			listDotaRank.add(rankVo);
			resort=true;
		}
		if(resort){
			List<RankVo> listTemp = new ArrayList<RankVo>(listDotaRank);
			List<RankVo> limitTemp = new ArrayList<RankVo>();
			Collections.sort(listTemp);
			for(int i=0;i<listTemp.size();i++){
				RankVo rv =  listTemp.get(i);
				rv.rankLv=i+1;
				if(rv.roleId==rolePo.getId().intValue()){
					rank=rv.rankLv;
				}
				RolePo rp = RolePo.findEntity(rv.roleId);
				if(rp != null){
					CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rp.getEquipWeaponId(), rp.getEquipArmorId(), rp.getFashion(), rp.getCareer(), rp.getWingWasHidden(),rp.getWingStar(),rp.hiddenFashions);
					rv.makeAvatars(commonAvatarVo);					
				}
				limitTemp.add(rv);
				if(i>=100){
					break;
				}
			}
			listDotaRank=new CopyOnWriteArrayList<RankVo>(limitTemp);
		}
		return rank;
	}
	
	
	public static HashMap<String, HashMap<String, String>> langServerDic=new HashMap<String, HashMap<String,String>>();
	
	public static HashMap<String, List<String[]>> langClientDic=new HashMap();
	
	public static void reloadServerAndClientLang(){
		langServerDic.clear();
//		List<String> languages=new ArrayList<String>();
		List<DicServerPo> list =GameDataTemplate.getDataList(DicServerPo.class);
		langServerDic.put("zh_cn", new HashMap<String, String>());
		langServerDic.put("zh_tw", new HashMap<String, String>());
		langServerDic.put("vi", new HashMap<String, String>());
		langServerDic.put("ko", new HashMap<String, String>());
		for (DicServerPo dicServerPo : list) {
			langServerDic.get("zh_cn").put(dicServerPo.getKey(),dicServerPo.getZhCn());
			langServerDic.get("zh_tw").put(dicServerPo.getKey(),dicServerPo.getZhTw());
			langServerDic.get("vi").put(dicServerPo.getKey(),dicServerPo.getVi());
			langServerDic.get("ko").put(dicServerPo.getKey(),dicServerPo.getKo());
		}
		
		langClientDic.clear();
		
		List<DicClientPo> list2 =GameDataTemplate.getDataList(DicClientPo.class);
		langClientDic.put("zh_cn", new ArrayList<String[]>());
		langClientDic.put("zh_tw", new ArrayList<String[]>());
		langClientDic.put("vi", new ArrayList<String[]>());
		langClientDic.put("ko", new ArrayList<String[]>());
		for (DicClientPo dicClientPo : list2) {
			langClientDic.get("zh_cn").add(new String[]{dicClientPo.getKey(),dicClientPo.getZhCn()});
			langClientDic.get("zh_tw").add(new String[]{dicClientPo.getKey(),dicClientPo.getZhTw()});
			langClientDic.get("vi").add(new String[]{dicClientPo.getKey(),dicClientPo.getVi()});
			langClientDic.get("ko").add(new String[]{dicClientPo.getKey(),dicClientPo.getKo()});			
		}

	}
	
	
	public static HashMap<String, String> fetchLanguageByType(String str){
		HashMap<String, String> map = new HashMap<String, String>();
		map = langServerDic.get(str);
		return map;
	}
	
	/**初始化竞技场活动排行 */
	public static void initArenaRank(){
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getType() == LiveActivityType.LiveActivity_ARENA_RANK){
				liveActivityPo.listRankItems.clear();
				for(int i=1; i <=100; i++){
					RankArenaPo rankArenaPo = rankArenaMaps.get(i);
					RankVo rankVo = new RankVo();
					rankVo.id=0;
					rankVo.roleId=rankArenaPo.getId();
					rankVo.roleName=rankArenaPo.getRoleName();
					rankVo.type= LiveActivityType.LiveActivity_ARENA_RANK;
					rankVo.roleCareer=rankArenaPo.getRoleCareer();
					rankVo.wingStar=0;
					rankVo.rankLv=i;
					rankVo.rolePower=rankArenaPo.getArenaRank();
					rankVo.weaponAvatar=rankArenaPo.getWeaponAvatar();
					rankVo.wingAvatar=rankArenaPo.getWingAvatar();
					rankVo.armorAvatar=rankArenaPo.getModelAvatar();
					liveActivityPo.listRankItems.add(rankVo);
				}
				break;
			}
		}
	}
	
	/**
	 * 根据充值的钻石查找RechargePo
	 * @param rechargeNum
	 * @return
	 */
	public static RechargePo fecthRechargePo(Integer rechargeNum){
		RechargePo rechargePo = null;
		List<RechargePo> list =GameDataTemplate.getDataList(RechargePo.class);
		for(RechargePo rp : list){
			if(rp.getRechargeNum().intValue() == rechargeNum.intValue()){
				rechargePo=rp;
				break;
			}
		}
		return rechargePo;
	}
	
	/**
	 * 根据ID查找成长基金
	 * @param id
	 * @return
	 */
	public static GrowFund fetchGrowFund(Integer id){
		for(GrowFund growFund :XmlCache.xmlFiles.constantFile.growFunds.growFund){
			if(id.intValue() == growFund.id){
				return growFund;
			}
		}
		return null;
	}
	
	
	public static RolePo fetchRolePoByRoleNameOrRoleId(String roleName,Integer roleId){
		RoleDAO roleDAO = RoleDAO.instance();
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo == null){
			Integer currentId = roleDAO.fetchRoleIdByRoleName(roleName);
			rolePo = RolePo.findEntity(currentId);
		}
		return rolePo;
	}
	
	public static String fetchLanguageMap(String key){
		String str = null;
		if(GlobalCache.languageMap.containsKey(key)){
			str=GlobalCache.languageMap.get(key);
		}
		if(str == null){
			str = key;
		}
		return str;
	}
	
	/**
	 * 根据次数查找钻石幸运抽取项
	 * @param times
	 * @return
	 */
	public static DiamondBasin fetchDiamondBasinBytimes(int times){
		DiamondBasin currentDiamondBasin = null;
		List<DiamondBasin> list= XmlCache.xmlFiles.constantFile.diamondBasins.diamondBasin;
		for(DiamondBasin diamondBasin : list){
			if(diamondBasin.times == times){
				currentDiamondBasin = diamondBasin;
				break;
			}
		}
		return currentDiamondBasin;
	}
	
	
	public static List<LoginQueueVo> fetchLoginQueueList(){
		return serverQueueNum;
	}
	
	/**
	 * 添加登录排队对象
	 * @param userId
	 * @param iuid
	 */
	public static int addServerQueueNum(int userId, String iuid){
		int rankNum=1;
		if(StringUtil.isNotEmpty(iuid) && userId!=0){
			int currentTime = (int) (System.currentTimeMillis()/1000);
			if(serverQueueNum.size() == 0){
				serverQueueNum.add(new LoginQueueVo(userId,iuid,1,currentTime));				
			}else{
				rankNum = serverQueueNum.get(serverQueueNum.size()-1).rankNum +1;
				serverQueueNum.add(new LoginQueueVo(userId,iuid,rankNum,currentTime));
			}
		}
		return rankNum;
	}
	
	/**
	 * 删除登录排队对象
	 * @param userId
	 */
	public static void removeServerQueueNum(int userId){
		for(LoginQueueVo loginQueueVo : serverQueueNum){
			if(loginQueueVo.userId == userId){
				serverQueueNum.remove(loginQueueVo);
				break;
			}
		}
	}
	
	/** 
	 * 登录排队List排序
	 */
	public static void sortServerQueueNum(){
		List<LoginQueueVo> listTemp = new ArrayList<LoginQueueVo>(serverQueueNum);
		List<LoginQueueVo> limitTemp = new ArrayList<LoginQueueVo>();
		Collections.sort(listTemp);
		
		for(int i=0; i<listTemp.size(); i++){
			LoginQueueVo loginQueueVo = listTemp.get(i);
			loginQueueVo.rankNum=i+1;
			limitTemp.add(loginQueueVo);
		}
		serverQueueNum=new CopyOnWriteArrayList<LoginQueueVo>(limitTemp);
		
	}
	
	/**
	 * 排队大于huor个小时的删除掉
	 */
	public static void checkRemoveQueueUser(int hour){
		List<LoginQueueVo> removeUser = new ArrayList<LoginQueueVo>();
		int currentTime = (int) (System.currentTimeMillis()/1000);
		for(LoginQueueVo loginQueueVo : serverQueueNum){
			if(currentTime > (loginQueueVo.loginTime+1*60*60*hour)){
				removeUser.add(loginQueueVo);
			}
		}
		for(LoginQueueVo vo : removeUser){
			serverQueueNum.remove(vo);
		}
		
	}

	public static void synProducts(){
		allProducts.clear();
		List list=BaseDAO.instance().getDBList(ProductPo.class);
		for (Object object : list) {
			allProducts.add((ProductPo) object);
		}
	}
	
	public static CopyOnWriteArrayList<ProductPo> allProducts=new CopyOnWriteArrayList<ProductPo>();

	public static int battleMsg=0;
	public static List<ProductPo> fetchAllPruducts() {
		return allProducts;
	}
	
	
	/**
	 * 根据Iuid查询用户信息
	 * @param iuid
	 * @return
	 */
	public static UserInfo fetchPlayerByIuid(String iuid){
		UserInfo userInfo = null;
		for(UserInfo user : XmlCache.xmlFiles.playerFile.userInfos.userInfo){
			if((user.iuid).equals(iuid.trim())){
				userInfo = user;
			}
		}
		return userInfo;
	}
	
//	300010037~300010050
	/**
	 * 根据充值groupId获取补单道具id
	 * @param groupiId
	 * @return
	 */
	public static int fetchRechargeItemCodeByGroupId(int groupiId){
		int code = 0;
		switch (groupiId) {
		case 1:
			code=300010037;
			break;
		case 2:
			code=300010038;
			break;
		case 3:
			code=300010039;
			break;
		case 4:
			code=300010040;
			break;
		case 5:
			code=300010041;
			break;
		case 6:
			code=300010042;
			break;
		case 7:
			code=300010043;
			break;
		default:
			break;
		}
		return code;
	}
	
	/**
	 * 刷新定时boss
	 */
	public static void checkTimerBossFresh(){
		MapRoom mapRoom = MapWorld.findStage(SceneType.SCENE_TIMER_BOSS);
		if(mapRoom != null){
			mapRoom.noonTimerBossMonsterFresh();				
		}			
	}
	
}
