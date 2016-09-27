package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.LiveActivityVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.RoleService;
import com.games.mmo.service.ServerService;
import com.games.mmo.service.UserService;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.RankVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

@Controller
public class ActivityRemoting  extends BaseRemoting {
	@Autowired
	ServerService serverService;
	@Autowired
	CheckService checkService;
	@Autowired
	RoleService roleService;
	@Autowired
	private UserService userService;
	/**
	 * 累计登录奖励
	 * @return
	 */
	public Object takeCumulativeLoginAward(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeCumulativeLoginAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeCumulativeLoginAward();
		rolePo.sendCumulativeLoginAward();
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.listToTakeCumulativeLoginAwardRecord);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 方法功能:获取角色等级奖励
	 * 更新时间:2014-12-15, 作者:peter
	 * @return
	 */
	public Object takeLevelAward(Integer lv){
		CheckUtil.checkIsNull(lv);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeLevelAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeLevelAward(lv);
		rolePo.sendLevelAward();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.SUCCESS;
	}
	
	/**
	 * 打开在线奖励面板
	 * @return
	 */
	public Object openOnlineAwardPanel(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println(rolePo.getName()+ " openOnlineAwardPanel()");
		String key = rolePo.getId() +"_ActivityRemoting.openOnlineAwardPanel";
		GlobalCache.checkMapResponse(key, 1000L, 5,true);
		rolePo.checkOnlineTimeAwrodState(1);
		return SessionType.SUCCESS;
	}
	
	/**
	 * 领取在线奖励
	 * @param onlineId
	 * @return
	 */
	public Object takeOnlineTimeAward(Integer onlineId){
		CheckUtil.checkIsNull(onlineId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeOnlineTimeAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeOnlineTimeAward(onlineId);
		rolePo.sendOnlineTimeAward(rolePo.fetchSameOnlineTime());
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 领取幸运转盘奖励
	 * @param type 1免费 2用钻石 3抽十次
	 * @return
	 */
	public Object takeLuckyWheelAward(Integer type){
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeLuckyWheelAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		List<IdNumberVo> clientShowList =rolePo.takeLuckyWheelAward(type);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
//		rolePo.sendUpdateCurrentTime();
		SessionUtil.addDataArray(rolePo.fetchSameDayLuckyWheelNumberOfFree());
		SessionUtil.addDataArray(rolePo.fetchTakeLuckyWheelFreeNextTime());
		SessionUtil.addDataArray(clientShowList);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 方法功能:领取月签到奖励
	 * 更新时间:2014-12-15, 作者:peter
	 * @param 是否补签 0：正常； 1：补签 
	 * @return
	 */
	public Object takeSignInReward(Integer wasSignedSupplement){
		CheckUtil.checkIsNull(wasSignedSupplement);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeSignInReward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeSignInReward(wasSignedSupplement);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.fetchSignInAwardSameDayIsTake());
		SessionUtil.addDataArray(rolePo.listSignInAwardRecord);
		return SessionType.SUCCESS;
	}
	
	/**
	 * 领取活跃度任务完成积分奖励
	 * @param livelyId
	 * @return
	 */
	public Object takeDailyLivelyTaskFinishScoreReward(Integer livelyId){
		CheckUtil.checkIsNull(livelyId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeDailyLivelyTaskFinishScoreReward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeDailyLivelyTaskFinishScoreReward(livelyId);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.listDailyLivelyAwardRecord);
		return SessionType.SUCCESS;
	}
	
	/**
	 * 领取首冲奖励
	 * @return
	 */
	public Object takeFristRechargeAwards(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeFristRechargeAwards";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.takeFristRechargeAwards();
		rolePo.sendUpdateRechargeInfo();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 领取月卡
	 * @return
	 */
	public Object takeMonthCard(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeMonthCard";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);

		rolePo.takeMonthCard();
		rolePo.sendUpdateRechargeInfo();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		return SessionType.SUCCESS;
	}
	
	/**
	 * 领取每日vip奖励
	 * @return
	 */
	public Object takeDailyVipAward(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeDailyVipAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeDailyVipAward();
		rolePo.sendUpdateRechargeInfo();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.sendUpdateMainPack(false);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 根据充值Id充值
	 * @param rechargeId
	 * @return
	 */
	public Object rechargeByRechargeId(Integer rechargeId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		PrintUtil.print(rolePo.getName() + " rechargeId =" +rechargeId );
		checkService.checkExisRechargePo(rechargeId);
		
		if(BaseStormSystemType.ALLOW_CHEAT){
			RechargePo rechargePo = RechargePo.findEntity(rechargeId);
			
			if(rechargePo.getId().intValue() == 9999){
				if(rolePo.listRechargeInfo.get(0).wasMonthCard == 1){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key71"));
				}
			}
			double  percent = 0;
			Double rechargeRmb = Double.valueOf(rechargePo.getRechargeRmb());
			rolePo.rechargeSendByRechargeId(rechargeId, rechargeRmb, rechargePo.getRechargeNum(), percent);
			SessionUtil.addDataArray(rolePo.listEachFirstRechargeStatus);			
		}
		return SessionType.SUCCESS;
	}
	
	/**
	 * 充值接口
	 * @param map
	 * @return 1:充值成功；2：没有角色； 3：没有充值配置； 4:充值金额错误; 5:充值月卡错误(月卡以存在)
	 */
	public Object rechargeInterface(Integer roleId, Integer rechargeId, Double rechargeMoney, Integer rechargeDiamond){
		
		PrintUtil.print(DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) + " rechargeInterface() roleId=" +roleId+"; rechargeId="+rechargeId+"; rechargeMoney="+rechargeMoney+"; rechargeDiamond"+rechargeDiamond );
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo == null){
			return "5";
		}
		RechargePo rechargePo = RechargePo.findEntity(rechargeId);
		if(rechargePo!= null && rechargePo.getChannelType() == 3){
			if(rolePo.listRechargeInfo.get(0).wasMonthCard == 1){
				return "6";
			}
		}
		double  percent = 0;
		int wasFirstRecharge = rolePo.rechargeSendByRechargeId(rechargeId, rechargeMoney,rechargeDiamond, percent);
		StringBuilder sb = new StringBuilder();
		sb.append("0,").append(wasFirstRecharge);
		return sb.toString();
	}
	
	
	
	public Object fetchLiveActivity(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchLiveActivity";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkAndFreshRoleLiveActivitys();
		rolePo.sendUpdateActivitysList();
		return SessionType.SUCCESS;
	}
	
	public Object fetchLiveActivityNow(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchLiveActivityNow";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("fetchLiveActivityNow() 11 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		rolePo.checkliveActivityRank();
//		System.out.println("fetchLiveActivityNow() 22 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		rolePo.checkAndFreshRoleLiveActivitys();
//		System.out.println("fetchLiveActivityNow() 33 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.addDataArray(rolePo.listRoleLiveActivitys);
		SessionUtil.addDataArray(GlobalCache.allActiveLiveActivitys);
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 领取运营活动奖励
	 * @param index
	 * @return
	 */
	public Object awardLiveActivity(Integer activityId,Integer index){
		CheckUtil.checkIsNull(activityId);
		CheckUtil.checkIsNull(index);
//		System.out.println("awardLiveActivity() activityId="+activityId+"; index="+index);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.awardLiveActivity";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.awardLiveActivity(activityId,index);
		rolePo.sendUpdateRoleActivitysList();
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获得活动排行
	 * @param activityId
	 * @return
	 */
	public Object liveActivityRank(Integer activityId){
		CheckUtil.checkIsNull(activityId);
		LiveActivityPo theLiveActivityPo = null;
		for (LiveActivityPo liveActivityPo : GlobalCache.fetchActiveLiveActivitys()) {
			if(liveActivityPo.getId()==activityId.intValue()){
				theLiveActivityPo=liveActivityPo;
			}
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.liveActivityRank";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<RankVo> allranks = theLiveActivityPo.listRankItems;
		List<RankVo> players = new ArrayList<RankVo>();
		if(allranks!=null){
//			System.out.println("size ="+allranks.size());
			int maxSize = Math.min(allranks.size(), 50);
			for(int i=1;i<=maxSize;i++){
				allranks.get(i-1).type=theLiveActivityPo.getType();
//				System.out.println("player="+allranks.get(i-1));
				players.add(allranks.get(i-1));
			}
		}

		SessionUtil.addDataArray(players);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 膜拜
	 * @param worshipType 膜拜类型 1：免费； 2：钻石
	 * @return
	 */
	public Object worship(Integer worshipType){
		CheckUtil.checkIsNull(worshipType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.worship";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.worship(worshipType);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		SessionUtil.addDataArray(rolePo.dailyWorshipGoldStatus);
		SessionUtil.addDataArray(rolePo.dailyWorshipDiamondStatus);
		SessionUtil.addDataArray(rolePo.getWorshipDiamondFirst());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获取离线奖励
	 * @param rewardType 奖励类型 1：普通；2:2倍领取； 3:3倍领取（只针对exp）
	 * @return
	 */
	public Object fetchOffLineReward(Integer rewardType){
		CheckUtil.checkIsNull(rewardType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchOffLineReward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.fetchOffLineReward(rewardType);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		SessionUtil.addDataArray(rolePo.listOffLineReward);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	public List<LiveActivityVo> list(){
		return com.games.backend.vo.LiveActivityVo.createFromPos();
	}

	/**
	 * 已废弃
	 */
	public String addEdit(Integer id,String name,String description,String loopFinishTimes,Integer type,Long startTime,Long endTime,String timeTxt,Integer category,Integer status,String conditionItems,String exchangeItems,String awardItems,String rateItems,Integer rank, Integer refresh){
		PrintUtil.print("ActivityRemoting.addEdit() awardItems="+awardItems+", conditionItems"+conditionItems+", exchangeItems="+exchangeItems+", loopFinishTimes="+loopFinishTimes+", rateItems="+rateItems);
		int awardItemSize=0;
		int conditionSize=0;
		int exChangeSize=0;
		int loopFinishTimesSize=0;
		int length = 0;
		if(!StringUtil.isEmpty(awardItems)){
			awardItemSize=StringUtil.split(awardItems, ";").length;
		}
		if(!StringUtil.isEmpty(exchangeItems)){
			exChangeSize=StringUtil.split(exchangeItems, ";").length;
			length=exChangeSize;
		}
		if(!StringUtil.isEmpty(conditionItems)){
			conditionSize=IdNumberVo.createList(conditionItems).size();
			length=conditionSize;
		}
		if(StringUtil.isNotEmpty(loopFinishTimes)){
			loopFinishTimesSize=StringUtil.split(loopFinishTimes, "|").length;
		}
		if((awardItemSize!=conditionSize) && type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数");
		}
		if((exChangeSize>0 && exChangeSize!=awardItemSize) &&  type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数2");
		}
		if(loopFinishTimesSize>0 && loopFinishTimesSize!=length && type!=LiveActivityType.LiveActivityDESC){
			ExceptionUtil.throwConfirmParamException("不匹配的个数3");
		}
		
		LiveActivityPo po =null;
		if(id==0){
			po = new LiveActivityPo();
    	}
    	else{
    		po = LiveActivityPo.findEntity(id);
    	}
		po.setAwardItems(awardItems);
		po.setCategory(category);
		po.setConditionItems(conditionItems);
		po.setDescription(description);
		po.setEndTime(endTime);
		po.setExchangeItems(exchangeItems);
		po.setLoopFinishTimes(loopFinishTimes);
		po.setName(name);
		po.setStartTime(startTime);
		po.setStatus(status);
		po.setTimeTxt(timeTxt);
		po.setType(type);
		po.setRateItems(rateItems);
		po.setRank(rank);
		po.reloadByStrs();
		PrintUtil.print("refresh="+refresh);
		po.setLoopWay(refresh);
    	if(id==0){
    		po.setCreateTime(System.currentTimeMillis());
    		po = (LiveActivityPo) BaseDAO.instance().insert(po);
    	}

		BaseDAO.instance().syncToDB(po);
    	GlobalCache.syncActiveLiveActivitys();
		return po.getId() + "";
	}
	
	/**
	 * 保存活动顺序
	 * @param ids
	 * @return 1：成功
	 */
	public Integer saveRank(String ids){
		if(StringUtil.isEmpty(ids)) return 1;
		List<Integer> list = StringUtil.getListByStr(ids);
		int rank = 0;
    	for(int i=list.size()-1; i>=0; i--){
    		rank++;
    		LiveActivityPo po = LiveActivityPo.findEntity(list.get(i));
    		po.setRank(rank);
    		BaseDAO.instance().syncToDB(po);
    	}
    	GlobalCache.syncActiveLiveActivitys();
		return 1;
	}
    
	
	public LiveActivityPo show(Integer id){
		return LiveActivityPo.findEntity(id);
	}
    
	
	public String delete(Integer id) throws Exception{
		LiveActivityPo liveActivityPo=LiveActivityPo.findEntity(id);
		if(liveActivityPo!=null){
			BaseDAO.instance().remove(liveActivityPo);			
		}
    	GlobalCache.syncActiveLiveActivitys();
		return "";
	}
	
	/**
	 * 批量删除运营活动(GMT)
	 * @param ids 活动ID
	 * @return
	 */
	public Integer batchDelete(String ids){
		List<Integer> list = StringUtil.getListByStr(ids);
		boolean needSync = false;
		for(Integer id : list){
			LiveActivityPo liveActivityPo = LiveActivityPo.findEntity(id);
			if(liveActivityPo != null){
				if(!needSync) needSync = true;
				BaseDAO.instance().remove(liveActivityPo);
			}
		}
		if(needSync) GlobalCache.syncActiveLiveActivitys();
		return SessionType.SUCCESS;
	}
	
	/**
	 * 查找批次号和服务区ID
	 * @param ids
	 * @return 批次号和服务区ID
	 */
	public Object[] fetchLotNumberByIds(String ids){
		Object [] objs = new Object [2];
		List<String> list = new ArrayList<String>();//批次号
		List<Integer> list2 = new ArrayList<Integer>();//服务器
		if(StringUtil.isNotEmpty(ids)){
			List<Map<String, Object>> ll = BaseDAO.instance().jdbcTemplate.queryForList("select lot_number, servers from u_po_live_activity where id in(" + ids + ")");
			for(Map<String, Object> map : ll){
				if(map.get("lot_number") != null) list.add((String) map.get("lot_number"));
				if(map.get("servers") != null){
					List<Integer> servers = StringUtil.getListByStr((String) map.get("servers"), ",");
					for(Integer server : servers){                                                                                                                                                                                               
						if(!list2.contains(server)){
							list2.add(server);
						}
					}
				}
			}
		}
		objs[0] = list;
		objs[1] = list2;
		return objs;
	}
	
	/**
	 * 删除运营活动
	 * @param lotNumber 批次号("'aa','bb'")
	 * @return
	 */
	public Integer deleteByLotNumber(String lotNumber){
		List<LiveActivityPo> list = BaseDAO.instance().dBfind("from LiveActivityPo where lotNumber in ("+ lotNumber +")");
		boolean needSync = false;
		for(LiveActivityPo liveActivityPo : list){
			if(liveActivityPo != null){
				if(!needSync) needSync = true;
				BaseDAO.instance().remove(liveActivityPo);
			}
		}
    	GlobalCache.syncActiveLiveActivitys();
		return SessionType.SUCCESS;
	}
	
	
	/**
	 * 购买成长基金
	 * @param typeId 1.通用; 2.韩国
	 * @return
	 */
	public Object buyGrowFund(Integer typeId){
		CheckUtil.checkIsNull(typeId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.buyGrowFund";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		if(typeId.intValue() == 1){
			if(rolePo.getWasGrowFunds1().intValue() ==1){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2319"));
			}	
		}else if(typeId.intValue() == 2){
			if(rolePo.getWasGrowFunds2().intValue() ==1){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2319"));
			}
		}	
		rolePo.buyGrowFund(typeId);
		rolePo.sendUpdateTreasure(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 领取成长基金1
	 * @return
	 */
	public Object takeGrowFund1(Integer id){
		CheckUtil.checkIsNull(id);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeGrowFund1";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeGrowFund1(id);
		rolePo.sendUpdateGrowFund1Info();
		rolePo.sendUpdateTreasure(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 领取成长基金2
	 * @return
	 */
	public Object takeGrowFund2(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.takeGrowFund2";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeGrowFund2();
		rolePo.sendUpdateGrowFund2Info();
		rolePo.sendUpdateTreasure(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获取邀请好友列表
	 * @return
	 */
	public Object fetchInvitationFriend(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchInvitationFriend";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkInvitationTask(true);
		SessionUtil.addDataArray(rolePo.listInvitationFriend);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 使用邀请码
	 * @return
	 */
	public Object useInvitationCode(String invitationCode){
		CheckUtil.checkIsNull(invitationCode);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.getLv().intValue()>=40){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2596"));
		}
		String key = rolePo.getId() +"_ActivityRemoting.useInvitationCode";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.useInvitationCode(invitationCode);
		SessionUtil.addDataArray(1);
		SessionUtil.addDataArray(rolePo.getInputInvitationCode());
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object addImageActivity(Integer id, String name, String description, Long startTime, Long endTime, Integer imageChange, String address, String lotNumber, String servers) throws Exception {
		if (id == 0) {//新增活动
			LiveActivityPo activityPo = new LiveActivityPo();
			activityPo.setType(16);//运营图片活动
			activityPo.setDescription(description);
			activityPo.setName(name);
			activityPo.setStartTime(startTime);
			activityPo.setEndTime(endTime);
			activityPo = (LiveActivityPo) BaseDAO.instance().insertToDB(activityPo);
			if(address.contains("/StormBackend/activity_images/")){
				activityPo.setUrl(address);
			}else{
				activityPo.setUrl(address + "/StormBackend/activity_images/"  + activityPo.getId() + ".png");
			}
			activityPo.setCreateTime(System.currentTimeMillis());
			activityPo.setLotNumber(lotNumber);
			activityPo.setServers(servers);
			BaseDAO.instance().updateEntityToDB(activityPo);
			GlobalCache.syncActiveLiveActivitys();
			return activityPo.getId();
		} else if (imageChange == 1){//修改活动
			LiveActivityPo activityPo =LiveActivityPo.findEntity(id);
			BaseDAO.instance().remove(activityPo);
			activityPo.setType(16);
			activityPo.setName(name);
			activityPo.setDescription(description);
			activityPo.setStartTime(startTime);
			activityPo.setEndTime(endTime);
			BaseDAO.instance().insertToDB(activityPo);
			activityPo.setUrl(address + "/StormBackend/activity_images/" + activityPo.getId() + ".png");
			BaseDAO.instance().syncToDB(activityPo);
			GlobalCache.syncActiveLiveActivitys();
			return activityPo.getId();
		} else {
			LiveActivityPo activityPo = (LiveActivityPo) BaseDAO.instance().getEntityPo(id, LiveActivityPo.class);
			activityPo.setType(16);
			activityPo.setName(name);
			activityPo.setDescription(description);
			activityPo.setStartTime(startTime);
			activityPo.setEndTime(endTime);
			BaseDAO.instance().updateEntityToDB(activityPo);
			GlobalCache.syncActiveLiveActivitys();
			return id;
		}
	}
	/**
	 * 摇一摇
	 * @return
	 */
	public Object fetchDiamondBasins(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchDiamondBasins";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(System.currentTimeMillis() > Long.valueOf(rolePo.diamondBasinsValidTime)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key12"));
		}
		int rewardDiamond = rolePo.fetchDiamondBasins();
//		rolePo.sendUpdatelistDiamondBasins();
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rewardDiamond);
		SessionUtil.addDataArray(rolePo.listDiamondBasins);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 购买月卡
	 * @return
	 */
	public Object buyMonthCard(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchDiamondBasins";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		if(rolePo.listRechargeInfo.get(0).wasMonthCard == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key71"));
		}
		
		rolePo.checkHasItemElseDiamond(21, 0);
		rolePo.listRechargeInfo.get(0).wasMonthCard = 1;
		rolePo.listRechargeInfo.get(0).monthCardRechargeBeginTime = System.currentTimeMillis();
		rolePo.listRechargeInfo.get(0).remainMonthCardDay = 30;
		rolePo.listRechargeInfo.get(0).todayWasTakeMonthCard = 0;	
		rolePo.sendUpdateRechargeInfo();
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * facebook绑定
	 * @param taskId
	 * @param token
	 * @return
	 */
	public Object facebookTokenBind(String iuid){
		PrintUtil.print("       ");
		PrintUtil.print("ActivityRemoting.facebookTokenBind() ");
		PrintUtil.print("iuid = " +iuid);
		
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.facebookTokenBind";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		PrintUtil.print("iuid = "+iuid+"; rolePo.name = " +rolePo.getName());
		if(StringUtil.isEmpty(iuid)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2644"));
		}
		UserPo userPo = UserPo.findEntity(rolePo.getUserId());
		if(StringUtil.isNotEmpty(userPo.getIuid())){
			SessionUtil.addDataArray(2);
			return SessionType.MULTYPE_RETURN;
		}
		
		UserPo userPoToken = userService.findUserByIuid(iuid,userPo.getServerId());
		if(userPoToken != null){
			PrintUtil.print("facebook Token is binding error! userPo.iuid = " +userPo.getIuid()+"; rolePo.name = "+rolePo.getName() +" iuid = " +iuid);
//			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2645"));
			SessionUtil.addDataArray(0);
			return SessionType.MULTYPE_RETURN;
		}
		userPo.setIuid(iuid);
		userPo.setPssd(iuid);
		PrintUtil.print(" facebook token binding successful userPo.iuid = " +userPo.getIuid() +"; rolePo.name = " +rolePo.getName() +"; iuid = " +iuid);
		BaseDAO.instance().syncToDB(userPo);
		SessionUtil.addDataArray(1);
		SessionUtil.addDataArray(userPo.getIuid());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获取Facebook绑定奖励
	 * @return
	 */
	public Object fetchFacebookBindReward(){
		PrintUtil.print("    ");
		PrintUtil.print("fetchFacebookBindReward() "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.fetchFacebookBindReward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		UserPo userPo = UserPo.findEntity(rolePo.getUserId());
		if(StringUtil.isEmpty(userPo.getIuid())){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2699"));
		}
		
		if(rolePo.getWasFacebookBind().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2646"));
		}
		
		String award = XmlCache.xmlFiles.constantFile.fbBindAward.awards;
		List<IdNumberVo> awardList = IdNumberVo.createList(award);
		rolePo.addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2647"));
		rolePo.setWasFacebookBind(1);
		
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.getWasFacebookBind());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 修改运营活动
	 * @return
	 */
	public String addEdit(Integer id,String name,String description,String loopFinishTimes,Integer type,Long startTime,Long endTime,String timeTxt,Integer category,Integer status,String conditionItems,String exchangeItems,String awardItems,String rateItems,Integer rank, Integer refresh, String conditionModes){
		PrintUtil.print("ActivityRemoting.addEdit() awardItems="+awardItems+", conditionItems"+conditionItems+", exchangeItems="+exchangeItems+", loopFinishTimes"+loopFinishTimes+", rateItems="+rateItems+", conditionModes="+conditionModes);
		int awardItemSize=0;
		int conditionSize=0;
		int exChangeSize=0;
		int loopFinishTimesSize=0;
		int length = 0;
		if(!StringUtil.isEmpty(awardItems)){
			awardItemSize=StringUtil.split(awardItems, ";").length;
		}
		if(!StringUtil.isEmpty(exchangeItems)){
			exChangeSize=StringUtil.split(exchangeItems, ";").length;
			length=exChangeSize;
		}
		if(!StringUtil.isEmpty(conditionItems)){
			conditionSize=IdNumberVo.createList(conditionItems).size();
			length=conditionSize;
		}
		if(StringUtil.isNotEmpty(loopFinishTimes)){
			loopFinishTimesSize=StringUtil.split(loopFinishTimes, "|").length;
		}
		if((awardItemSize!=conditionSize) && type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数");
		}
		if((exChangeSize>0 && exChangeSize!=awardItemSize) &&  type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数2");
		}
		if(loopFinishTimesSize>0 && loopFinishTimesSize!=length && type!=LiveActivityType.LiveActivityDESC){
			ExceptionUtil.throwConfirmParamException("不匹配的个数3");
		}
		
		LiveActivityPo po =null;
		if(id==0){
			po = new LiveActivityPo();
    	}
    	else{
    		po = LiveActivityPo.findEntity(id);
    	}
		po.setAwardItems(awardItems);
		po.setCategory(category);
		po.setConditionItems(conditionItems);
		po.setDescription(description);
		po.setEndTime(endTime);
		po.setExchangeItems(exchangeItems);
		po.setLoopFinishTimes(loopFinishTimes);
		po.setName(name);
		po.setStartTime(startTime);
		po.setStatus(status);
		po.setTimeTxt(timeTxt);
		po.setType(type);
		po.setRateItems(rateItems);
		po.setRank(rank);
		po.setConditionModes(conditionModes);
		po.reloadByStrs();
		PrintUtil.print("refresh="+refresh);
		po.setLoopWay(refresh);
    	if(id==0){
    		po.setCreateTime(System.currentTimeMillis());
    		po = (LiveActivityPo) BaseDAO.instance().insert(po);
    	}else{
    		BaseDAO.instance().syncToDB(po);
    	}
    	GlobalCache.syncActiveLiveActivitys();
		return po.getId() + "";
	}
	
	/**
	 * 天降宝箱
	 * @return
	 */
	public Object receiveCimelias(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ActivityRemoting.receiveCimelias";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		int cimeliasId = rolePo.getCimeliasId()==null?0:rolePo.getCimeliasId().intValue();
		boolean hasActivity = false;
		for(com.games.mmo.vo.xml.ConstantFile.Cimelias.Cimelia cimelia:XmlCache.xmlFiles.constantFile.cimelias.cimelia){
			if(cimelia.id == cimeliasId){
				hasActivity = true;
				List<IdNumberVo> needItems = IdNumberVo.createList(cimelia.taskItem, "|", ",");
				for(IdNumberVo idNumberVo:needItems){
					rolePo.checkHasItem(idNumberVo.getId(), idNumberVo.getNum());
				}
				for(IdNumberVo idNumberVo:needItems){
					rolePo.removePackItem(idNumberVo.getId(), idNumberVo.getNum(),GlobalCache.fetchLanguageMap("key30"));
				}
				List<IdNumberVo> awardItems = IdNumberVo.createList(cimelia.awardItem, "|", ",");
				for(IdNumberVo idNumberVo:awardItems){
					rolePo.addItem(idNumberVo.getId(), idNumberVo.getNum(), 1);
				}
				rolePo.setCimeliasId(rolePo.getCimeliasId()+1);
				SessionUtil.addDataArray(awardItems);
				break;
			}
		}
		if(!hasActivity){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key75"));
		}
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.getCimeliasId());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 批量添加运营活动(GMT)
	 */
	public String addEditWithLotNumber(Integer id,String name,String description,String loopFinishTimes,Integer type,Long startTime,Long endTime,String timeTxt,Integer category,Integer status,String conditionItems,String exchangeItems,String awardItems,String rateItems,Integer rank, Integer refresh, String conditionModes, String lotNumber, String servers){
		PrintUtil.print("ActivityRemoting.addEditWithLotNumber() awardItems="+awardItems+", conditionItems"+conditionItems+", exchangeItems="+exchangeItems+", loopFinishTimes"+loopFinishTimes+", rateItems="+rateItems+", conditionModes="+conditionModes);
		int awardItemSize=0;
		int conditionSize=0;
		int exChangeSize=0;
		int loopFinishTimesSize=0;
		int length = 0;
		if(!StringUtil.isEmpty(awardItems)){
			awardItemSize=StringUtil.split(awardItems, ";").length;
		}
		if(!StringUtil.isEmpty(exchangeItems)){
			exChangeSize=StringUtil.split(exchangeItems, ";").length;
			length=exChangeSize;
		}
		if(!StringUtil.isEmpty(conditionItems)){
			conditionSize=IdNumberVo.createList(conditionItems).size();
			length=conditionSize;
		}
		if(StringUtil.isNotEmpty(loopFinishTimes)){
			loopFinishTimesSize=StringUtil.split(loopFinishTimes, "|").length;
		}
		if((awardItemSize!=conditionSize) && type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数");
		}
		if((exChangeSize>0 && exChangeSize!=awardItemSize) &&  type!=LiveActivityType.LiveActivity_EXCHANGE){
			ExceptionUtil.throwConfirmParamException("不匹配的个数2");
		}
		if(loopFinishTimesSize>0 && loopFinishTimesSize!=length && type!=LiveActivityType.LiveActivityDESC){
			ExceptionUtil.throwConfirmParamException("不匹配的个数3");
		}
		
		LiveActivityPo po =null;
		if(id==0){
			po = new LiveActivityPo();
    	}
    	else{
    		po = LiveActivityPo.findEntity(id);
    	}
		po.setAwardItems(awardItems);
		po.setCategory(category);
		po.setConditionItems(conditionItems);
		po.setDescription(description);
		po.setEndTime(endTime);
		po.setExchangeItems(exchangeItems);
		po.setLoopFinishTimes(loopFinishTimes);
		po.setName(name);
		po.setStartTime(startTime);
		po.setStatus(status);
		po.setTimeTxt(timeTxt);
		po.setType(type);
		po.setRateItems(rateItems);
		po.setRank(rank);
		po.reloadByStrs();
		PrintUtil.print("refresh="+refresh);
		po.setLoopWay(refresh);
    	if(id==0){
    		po.setLotNumber(lotNumber);
    		po.setServers(servers);
    		po.setCreateTime(System.currentTimeMillis());
    		po = (LiveActivityPo) BaseDAO.instance().insert(po);
    	}else{
    		BaseDAO.instance().syncToDB(po);
    	}
    	GlobalCache.syncActiveLiveActivitys();
		return po.getId() + "";
	}
}
