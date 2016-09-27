package com.games.mmo.po;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.task.TimerBossJob;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.RoleType;
import com.games.mmo.vo.ActivityInfoVo;
import com.games.mmo.vo.PVPPVEActivityStatusVo;
import com.games.mmo.vo.RetrieveSystemVo;
import com.games.mmo.vo.TimerBossVo;
import com.games.mmo.vo.global.MonsterVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.Global;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

/**
 *
 * 类功能: 宠物
 *
 * @author Johnny
 * @version
 */
@Entity
@Table(name = "u_po_global")
public class GlobalPo extends BaseUserDBPo {

	/**
	 * 主键
	 **/

	private Integer id;

	private String keyStr;
	
	private String valueStr;

	public Object valueObj=null;
	public static String keySiegeBid="keySiegeBid";
	public static String keyPVEPVPRecordVo="keyPVEPVPRecordVo";
	public static String keyOptions="keyOptions";
	public static String keyLanguage="keyLanguage";
	public static String keyMonster="keyMonster";
	public static String keyAwardRetrieve="keyAwardRetrieve";
	public static String keyRecordTime = "keyRecordTime";
	public static String keyStaticMapBossInfo ="keyStaticMapBossInfo";
	public static String keyLoginQueueNum = "keyLoginQueueNum";
	public static String keyTimerBoss = "keyTimerBoss";
	public static String keyFirstThreeRankAndCastellan = "keyFirstThreeRankAndCastellan";
	public static String keyServerId = "keyServerId";
	public static String keyBossSecretRoom = "keyBossSecretRoom";
	
	public static ConcurrentHashMap<String, Object> keyGlobalPoMap =new ConcurrentHashMap<String, Object>();


	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="key_str") 
	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		changed("key_str",keyStr,this.keyStr);
		this.keyStr = keyStr;
	}

	@Column(name="value_str") 
	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		changed("value_str",valueStr,this.valueStr);
		this.valueStr = valueStr;
	}

	/**
	 * 
	 * @param type 0:加载语言； 1：加载剩余的
	 */
	public static void init(int type){
		List<GlobalPo> globals = BaseDAO.instance().getDBList(GlobalPo.class);
		boolean keySiegeBidMatched=false;
		boolean keyPVEPVPRecordVoMatched=false;
		boolean keyOptionsMatched=false;
		boolean keyLanguageMatched=false;
		boolean keyMonsterMatched=false;
		boolean keyAwardRetrieveMatched=false;
		boolean keyRecordTimeMatched=false;
		boolean keyStaticMapBossInfoMatched=false;
		boolean keyLoginQueueNumMatched=false;
		boolean keyTimerBossMatched=false;
		boolean keyFirstThreeRankAndCastellanMatched = false;
		boolean keyBossSecretRoomed = false;
		
		if(type == 0){
			for (GlobalPo globalPo : globals) {
				globalPo=GlobalPo.findRealEntity(GlobalPo.class, globalPo.getId());
				if(globalPo.getKeyStr().equals(keyLanguage)){
					keyGlobalPoMap.put(keyLanguage, globalPo);
					keyLanguageMatched=true;
				}
			}
			
			if(!keyLanguageMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyLanguage;
				globalPo.valueObj="zh_cn";
				keyGlobalPoMap.put(keyLanguage, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
		}else if (type == 1){
			for (GlobalPo globalPo : globals) {
				globalPo=GlobalPo.findRealEntity(GlobalPo.class, globalPo.getId());
				if(globalPo.getKeyStr().equals(keySiegeBid)){
					keyGlobalPoMap.put(keySiegeBid, globalPo);
					keySiegeBidMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyPVEPVPRecordVo)){
					keyGlobalPoMap.put(keyPVEPVPRecordVo, globalPo);
					keyPVEPVPRecordVoMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyOptions)){
					keyGlobalPoMap.put(keyOptions, globalPo);
					keyOptionsMatched=true;
				}
				
				if(globalPo.getKeyStr().equals(keyMonster)){
					keyGlobalPoMap.put(keyMonster, globalPo);
					keyMonsterMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyAwardRetrieve)){
					keyGlobalPoMap.put(keyAwardRetrieve, globalPo);
					keyAwardRetrieveMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyRecordTime)){
					keyGlobalPoMap.put(keyRecordTime, globalPo);
					keyRecordTimeMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyStaticMapBossInfo)){
					keyGlobalPoMap.put(keyStaticMapBossInfo, globalPo);
					keyStaticMapBossInfoMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyLoginQueueNum)){
					keyGlobalPoMap.put(keyLoginQueueNum, globalPo);
					keyLoginQueueNumMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyTimerBoss)){
					keyGlobalPoMap.put(keyTimerBoss, globalPo);
					keyTimerBossMatched=true;
				}
				if(globalPo.getKeyStr().equals(keyFirstThreeRankAndCastellan)){
					keyGlobalPoMap.put(keyFirstThreeRankAndCastellan, globalPo);
					keyFirstThreeRankAndCastellanMatched = true;
				}
				//服务器ID
				if(globalPo.getKeyStr().equals(keyServerId)){
					keyGlobalPoMap.put(keyServerId, globalPo);
				}
				if(globalPo.getKeyStr().equals(keyBossSecretRoom)){
					keyGlobalPoMap.put(keyBossSecretRoom, globalPo);
					keyBossSecretRoomed = true;
				}
			}
			
			if(!keySiegeBidMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keySiegeBid;
				globalPo.valueObj=new SiegeBidVo();
				keyGlobalPoMap.put(keySiegeBid, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyPVEPVPRecordVoMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyPVEPVPRecordVo;
				globalPo.valueObj=new PVEPVPRecordVo();
				keyGlobalPoMap.put(keyPVEPVPRecordVo, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyOptionsMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyOptions;
				List<IdNumberVo> list=new ArrayList<IdNumberVo>();
				list.add(new IdNumberVo(OptionTypeMonthCardOpen,1));
				list.add(new IdNumberVo(OptionTypeMonthAuctionOpen,1));
				list.add(new IdNumberVo(OptionTypeLiveActivityOpen,1));
				list.add(new IdNumberVo(OptionTypeGiftCodeOpen,1));
				list.add(new IdNumberVo(OptionTypeMainFacebook,1));
				list.add(new IdNumberVo(OptionTypeGrowth,1));
				list.add(new IdNumberVo(OptionTypeGrowthKOVERSION,1));
				list.add(new IdNumberVo(OptionTypeRank,1));
				list.add(new IdNumberVo(OptionTypeShopScore,1));
				//翅膀时装
				list.add(new IdNumberVo(optionWingAvatar,1));
				globalPo.valueObj=list;
				
				keyGlobalPoMap.put(keyOptions, globalPo);
				BaseDAO.instance().insert(globalPo);
			}		
			
			if(!keyMonsterMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyMonster;
				List<MonsterVo> list = new ArrayList<MonsterVo>();	
				globalPo.valueObj=list;
				keyGlobalPoMap.put(keyMonster, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyAwardRetrieveMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyAwardRetrieve;
				List<RetrieveSystemVo> list = new ArrayList<RetrieveSystemVo>();
				// id, 开启时间， 今天状态， 昨天状态
				list.add(new RetrieveSystemVo(CopySceneType.COPY_SCENE_CONF_MONSTERCRISIS, 0l, 0, 0));
				list.add(new RetrieveSystemVo(CopySceneType.COPY_SCENE_CONF_KINGOFPK, 0l, 0, 0));
				globalPo.valueObj=list;
				keyGlobalPoMap.put(keyAwardRetrieve, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyRecordTimeMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyRecordTime;
				globalPo.valueObj=DateUtil.getInitialDate(System.currentTimeMillis());
				keyGlobalPoMap.put(keyRecordTime, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyStaticMapBossInfoMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyStaticMapBossInfo;
				ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = new ConcurrentHashMap<Integer,MonsterFreshInforVo>();
				globalPo.valueObj=staticMapBossMap;
				keyGlobalPoMap.put(keyStaticMapBossInfo, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyLoginQueueNumMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyLoginQueueNum;
				globalPo.valueObj=2000;
				keyGlobalPoMap.put(keyLoginQueueNum, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyTimerBossMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyTimerBoss;
				TimerBossVo timerBossVo = new TimerBossVo();
				globalPo.valueObj = timerBossVo;
				keyGlobalPoMap.put(keyTimerBoss, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyFirstThreeRankAndCastellanMatched){
				GlobalPo globalPo =new GlobalPo();
				globalPo.keyStr=keyFirstThreeRankAndCastellan;
				List<IdNumberVo2> list = new ArrayList<IdNumberVo2>();
				//  int1:唯一标示; int2：职业和城主 ; int3：原有roleId
				list.add(new IdNumberVo2(10000001, RoleType.CAREER_WARRIOR, 0));
				list.add(new IdNumberVo2(10000002, RoleType.CAREER_MAGE, 0));
				list.add(new IdNumberVo2(10000003, RoleType.CAREER_RANGER, 0));
				list.add(new IdNumberVo2(10000004, RoleType.CAREER_CASTELLAN,  0));
				globalPo.valueObj=list;
				keyGlobalPoMap.put(keyFirstThreeRankAndCastellan, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
			if(!keyBossSecretRoomed){
				GlobalPo globalPo = new GlobalPo();
				globalPo.keyStr=keyBossSecretRoom;
				ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = new ConcurrentHashMap<Integer,MonsterFreshInforVo>();
				globalPo.valueObj=staticMapBossMap;
				keyGlobalPoMap.put(keyBossSecretRoom, globalPo);
				BaseDAO.instance().insert(globalPo);
			}
		}
	}

	
	public void loadData(BasePo basePo) {
		if (loaded == false) {
			unChanged();
			if(keyStr.equals(keySiegeBid)){
				SiegeBidVo siegeBidVo=new SiegeBidVo();
				siegeBidVo.loadProperty(valueStr, "|");
				valueObj=siegeBidVo;
			}
			if(keyStr.equals(keyPVEPVPRecordVo)){
				PVEPVPRecordVo  pVEPVPRecordVo=new PVEPVPRecordVo();
				pVEPVPRecordVo.loadProperty(valueStr, "|");
				valueObj=pVEPVPRecordVo;
			}
			if(keyStr.equals(keyOptions)){
				valueObj=IdNumberVo.createList(valueStr);
			}			
			if(keyStr.equals(keyLanguage)){
				valueObj=valueStr;
			}	
			if(keyStr.equals(keyMonster)){
				List<MonsterVo> list = new ArrayList<MonsterVo>();
				if (valueStr != null) {
					String[] items = StringUtil.split(valueStr, ",");
					for (String itemStr : items) {
						MonsterVo monsterVo = new MonsterVo();
						monsterVo.loadProperty(itemStr, "|");
						list.add(monsterVo);
					}
				}
				valueObj=list;					
			}
			if(keyStr.equals(keyAwardRetrieve)){
				List<RetrieveSystemVo> list = new ArrayList<RetrieveSystemVo>();
				if(valueStr != null){
					String[] items = StringUtil.split(valueStr, ",");
					for(String itemStr : items){
						RetrieveSystemVo retrieveSystemVo = new RetrieveSystemVo();
						retrieveSystemVo.loadProperty(itemStr, "|");
						list.add(retrieveSystemVo);
					}
				}
				valueObj = list;
			}
			if(keyStr.equals(keyRecordTime)){
				valueObj=valueStr;
			}
			if(keyStr.equals(keyStaticMapBossInfo)){
				ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = new ConcurrentHashMap<Integer,MonsterFreshInforVo>();
				if(StringUtil.isNotEmpty(valueStr)){
					String[] items = StringUtil.split(valueStr, ",");
					for(String itemStr : items){
						MonsterFreshInforVo monsterFreshInforVo = new MonsterFreshInforVo();
						monsterFreshInforVo.loadProperty(itemStr, "|");
						staticMapBossMap.put(monsterFreshInforVo.monsterFreshId, monsterFreshInforVo);
					}
				}
				valueObj=staticMapBossMap;
			}
			
			if(keyStr.equals(keyLoginQueueNum)){
				valueObj=valueStr;
			}
			
			if(keyStr.equals(keyTimerBoss)){
				TimerBossVo timerBossVo = new TimerBossVo();
				timerBossVo.loadProperty(valueStr, "|");
				valueObj=timerBossVo;
			}
			
			if(keyStr.equals(keyFirstThreeRankAndCastellan)){
				List<IdNumberVo2> list = IdNumberVo2.createList(valueStr);
				valueObj = list;
			}
			//服务器Id
			if(keyStr.equals(keyServerId)){
				valueObj=valueStr;
			}	
			if(keyStr.equals(keyBossSecretRoom)){
				ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = new ConcurrentHashMap<Integer,MonsterFreshInforVo>();
				if(StringUtil.isNotEmpty(valueStr)){
					String[] items = StringUtil.split(valueStr, ",");
					for(String itemStr : items){
						MonsterFreshInforVo monsterFreshInforVo = new MonsterFreshInforVo();
						monsterFreshInforVo.loadProperty(itemStr, "|");
						staticMapBossMap.put(monsterFreshInforVo.monsterFreshId, monsterFreshInforVo);
					}
				}
				valueObj=staticMapBossMap;
			}
			loaded = true;
		}
	}
	
	
	public void saveData() {
		if(keyStr.equals(keySiegeBid)){
			Object[] objs = ((SiegeBidVo)valueObj).fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			setValueStr(targetStr);
		}
		if(keyStr.equals(keyPVEPVPRecordVo)){
			Object[] objs = ((PVEPVPRecordVo)valueObj).fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			setValueStr(targetStr);
		}	
		if(keyStr.equals(keyOptions)){
			setValueStr(IdNumberVo.createStr((List<IdNumberVo>) valueObj, "|", ","));
		}		
		if(keyStr.equals(keyLanguage)){
			setValueStr((String) valueObj);
		}	
		if(keyStr.equals(keyMonster)){
			List<String> list = new ArrayList<String>();
			List<MonsterVo> listMonsterVo = (List<MonsterVo>) valueObj;
			if(listMonsterVo.size() != 0){
				for(int i =0; i < listMonsterVo.size(); i++){
					MonsterVo monsterVo = listMonsterVo.get(i);
					Object[] objs = monsterVo.fetchProperyItems();
					String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
					list.add(targetStr);
				}
				setValueStr(StringUtil.implode(list, ","));				
			}else{
				setValueStr(null);
			}
		}
		if(keyStr.equals(keyAwardRetrieve)){
			List<String> list = new ArrayList<String>();
			List<RetrieveSystemVo> listRetrieveSystemVo =  (List<RetrieveSystemVo>) valueObj;
			if(listRetrieveSystemVo.size() != 0){
				for(int i=0; i < listRetrieveSystemVo.size(); i++){
					RetrieveSystemVo retrieveSystemVo = listRetrieveSystemVo.get(i);
					Object[] objs = retrieveSystemVo.fetchProperyItems();
					String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
					list.add(targetStr);
				}
				setValueStr(StringUtil.implode(list, ","));	
			}else{
				setValueStr(null);
			}
		}
		if(keyStr.equals(keyRecordTime)){
			String str = String.valueOf(valueObj);
			setValueStr(str);
		}
		
		if(keyStr.equals(keyStaticMapBossInfo)){
			List<String> list = new ArrayList<String>();
			ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) valueObj;
			if(staticMapBossMap.size() != 0){
				for(MonsterFreshInforVo monsterFreshInforVo : staticMapBossMap.values()){
					Object[] objs = monsterFreshInforVo.fetchProperyItems();
					String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
					list.add(targetStr);
				}
				setValueStr(StringUtil.implode(list,","));
			}else{
				setValueStr(null);
			}
		}
		if(keyStr.equals(keyLoginQueueNum)){
			String str = String.valueOf(valueObj);
			setValueStr(str);
		}
		if(keyStr.equals(keyTimerBoss)){
			Object[] objs = ((TimerBossVo)valueObj).fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			setValueStr(targetStr);
		}
		if(keyStr.equals(keyFirstThreeRankAndCastellan)){
			String targetStr = IdNumberVo2.createStr((List<IdNumberVo2>) valueObj);
			setValueStr(targetStr);
		}
		
		//服务器Id
		if(keyStr.equals(keyServerId)){
			setValueStr((String) valueObj);
		}	
		if(keyStr.equals(keyBossSecretRoom)){
			List<String> list = new ArrayList<String>();
			ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) valueObj;
			if(staticMapBossMap.size() != 0){
				for(MonsterFreshInforVo monsterFreshInforVo : staticMapBossMap.values()){
					Object[] objs = monsterFreshInforVo.fetchProperyItems();
					String targetStr = DBFieldUtil.createPropertyImplod(objs, "|");
					list.add(targetStr);
				}
				setValueStr(StringUtil.implode(list,","));
			}else{
				setValueStr(null);
			}
		}
	}

	/**
	 * 月卡开启，0关闭
	 */
	public static final int OptionTypeMonthCardOpen=1;
	
	/**
	 * 交易行开启 0关闭
	 */
	public static final int OptionTypeMonthAuctionOpen=2;
	
	/**
	 * 运营活动开启 0关闭
	 */
	public static final int OptionTypeLiveActivityOpen=3;

	/**
	 * 礼品码 0关闭
	 */
	public static final int OptionTypeGiftCodeOpen=4;
	
	/**
	 * Facebook 0关闭
	 */
	public static final int OptionTypeMainFacebook=5;
	
	/**
	 * 成长计划
	 */
	public static final int OptionTypeGrowth=6;
	/**
	 * 成长计划(KO)
	 */
	public static final int OptionTypeGrowthKOVERSION=7;
	/**
	 * 排行榜
	 */
	public static final int OptionTypeRank=8;
	/**
	 * 商店评分
	 */
	public static final int OptionTypeShopScore=9;

	/**
	 * 好友邀请
	 */
	public static final int OptionTypeInviteFriend=10;
	
	/**
	 * 充值
	 */
	public static final int OptionTypeCharge=11;
	
	/**
	 * 摇一摇
	 */
	public static final int OptionTypeShake=12;
	
	/**
	 * 战斗监控
	 */
	public static final int OptionTypeBattleMsg=13;
	
	/**
	 * 老玩家奖励
	 */
	public static final int OptionTypeOldPlayerReward=14;
	
	/**
	 * 幸运转盘
	 */
	public static final int luckyTurn=15;
	
	/**
	 * 单人副本
	 */
	public static final int optionTypeSingleStory = 16;
	
	/**
	 * 波塞海线王活动
	 */
	public static final int optionTypeKingActivity = 17;
	
	/**
	 * 翅膀时装 1:开启,0:关闭
	 */
	public static final int optionWingAvatar = 18;
	
	/**
	 * boss密室 1:开启,0:关闭
	 */
	public static final int optionBossBackroom = 19;
	
	/**
	 *  修改阵营战中立怪阵营
	 * @param monsterVo
	 */
	public void updateMonsterVo(MonsterVo monsterVo){
		List<MonsterVo> list = ((List<MonsterVo>)this.valueObj);
		boolean flag = false;
		for(MonsterVo mv : list){
			if(mv.id == monsterVo.id){
				mv.militaryForces = monsterVo.militaryForces;
				flag = true;
				break;
			}
		}
		if(!flag){
			list.add(monsterVo);
		}
//		System.out.println("list="+list);
	}
	
	/**
	 * 获取控制的资源怪物数量
	 * @param forcesType
	 * @return
	 */
	public Integer fetchForcesMonsterNum(Integer forcesType){
		List<MonsterVo> list = ((List<MonsterVo>)this.valueObj);
		int num = 0;
		for(MonsterVo mv : list){
			if(mv.militaryForces == forcesType){
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 获取资源找回信息
	 * @param id
	 * @return
	 */
	public RetrieveSystemVo fetchRetrieveSystemVo(Integer id){
		List<RetrieveSystemVo> list = ((List<RetrieveSystemVo>)this.valueObj);
		for(RetrieveSystemVo retrieveSystemVo : list){
			if(id.intValue()==retrieveSystemVo.id.intValue()){
				return retrieveSystemVo;
			}
		}
		return null;
	}
	
	/**
	 * 更新静态boss信息
	 * @param monsterFreshInforVo
	 */
	public boolean updateStaticMapBossInfo(MonsterFreshInforVo monsterFreshInforVo){
		boolean bool = false;
		ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) valueObj;
		MonsterFreshInforVo mon = staticMapBossMap.get(monsterFreshInforVo.monsterFreshId);
		if(mon!=null){
			mon.disapperTime=monsterFreshInforVo.disapperTime;
			mon.lastKiller=monsterFreshInforVo.lastKiller;
			bool = true;
		}
		return bool;
	}
	
	/**
	 * 添加静态boss信息
	 * @param monsterFreshInforVo
	 */
	public void addStaticMapBossInfo(MonsterFreshInforVo monsterFreshInforVo){
		ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) valueObj;
		staticMapBossMap.put(monsterFreshInforVo.monsterFreshId, monsterFreshInforVo);
	}
	
	/**
	 * 根据Id查找三个最高战力和城主
	 * @param id
	 * @return
	 */
	public IdNumberVo2 fetchFirstThreeRankAndCastellanById(int value){
		List<IdNumberVo2> list = (List<IdNumberVo2>) this.valueObj;
		IdNumberVo2 idNumberVo2 = IdNumberVo2.findIdNumber(value, list);
		return idNumberVo2;
	}
	/**
	 * 根据职业查找三个最高战力和城主
	 * @param id
	 * @return
	 */
	public IdNumberVo2 fetchFirstThreeRankAndCastellanByCareer(int value){
		IdNumberVo2 inv2 = null;
		List<IdNumberVo2> list = (List<IdNumberVo2>) this.valueObj;
		for(IdNumberVo2 idNumberVo2 : list){
			if(idNumberVo2.getInt2().intValue() == value){
				inv2 = idNumberVo2;
				break;
			}
		}
		return inv2;
	}
	
}
