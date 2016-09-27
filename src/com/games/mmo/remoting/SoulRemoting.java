package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.SoulStepPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.SoulService;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.PlayTimesType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.type.SoulType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.SlotSoulVo;
import com.games.mmo.vo.role.RolePackItemVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

/**
 * 和部位相关的角色成长系统都放这里吧
 * @author Administrator
 *
 */
@Controller
public class SoulRemoting extends BaseRemoting{

	@Autowired
	private CheckService checkService;
	@Autowired
	private ChatService chatService;
	/**
	 * 装备升星
	 * @param slot
	 * @return
	 */
	public Object soulPower(Integer slot){
		CheckUtil.checkIsNull(slot);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulPower";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<Object> list = rolePo.soulPower( slot);
		Integer var = (Integer) list.get(0);
		Boolean upRateFlag=(Boolean) list.get(1);
		rolePo.sendUpdateSoulSlot(slot);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(var);
		SessionUtil.addDataArray(upRateFlag);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 锁定槽位
	 * @param equipSlot
	 * @param slotIndex
	 * @return
	 */
	public Object soulExtractLock(Integer equipSlot,Integer slotIndex){
		CheckUtil.checkIsNull(equipSlot);
		CheckUtil.checkIsNull(slotIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulExtractLock";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SlotSoulVo slotSoulVo = rolePo.findSlotSoul(equipSlot);
		slotSoulVo.makeSoulSlotVal(slotIndex,SlotSoulType.EXTRACT_STATUS_LOCK);
		rolePo.sendUpdateSoulSlot(equipSlot);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 解锁槽位
	 * @param equipSlot
	 * @param slotIndex
	 * @return
	 */
	public Object soulExtractUnLock(Integer equipSlot,Integer slotIndex){
		CheckUtil.checkIsNull(equipSlot);
		CheckUtil.checkIsNull(slotIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulExtractLock";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SlotSoulVo slotSoulVo = rolePo.findSlotSoul(equipSlot);
		slotSoulVo.makeSoulSlotVal(slotIndex,SlotSoulType.EXTRACT_STATUS_UNLOCK);
		rolePo.sendUpdateSoulSlot(equipSlot);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 激活槽位
	 * @param equipSlot
	 * @param slotIndex
	 * @return
	 */
	public Object soulExtractActive(Integer equipSlot,Integer slotIndex){
		CheckUtil.checkIsNull(equipSlot);
		CheckUtil.checkIsNull(slotIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulExtractActive";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.soulExtractActive( equipSlot, slotIndex);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 洗练 
	 * @param equipSlot 部位
	 * @param normalORAdvance 1:普通  2:高级
	 * @return
	 */
	public Object soulExtract(Integer equipSlot, Integer normalORAdvance){
		CheckUtil.checkIsNull(normalORAdvance);
		CheckUtil.checkIsNull(equipSlot);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulExtract";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.soulExtract( equipSlot,  normalORAdvance);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键镶嵌
	 * @param equipSlot
	 * @return
	 */
	public Object oneKeySoulGemInsert(Integer equipSlot){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(4);
		
		SlotSoulVo slotSoulVo = rolePo.findSlotSoul(equipSlot);
		if(slotSoulVo.gem1Id!=0){
			rolePo.soulGemRemove(equipSlot, 1);
		}
		if(slotSoulVo.gem2Id!=0){
			rolePo.soulGemRemove(equipSlot, 2);
		}
		if(slotSoulVo.gem3Id!=0){
			rolePo.soulGemRemove(equipSlot, 3);
		}
		if(slotSoulVo.gem4Id!=0){
			rolePo.soulGemRemove(equipSlot, 4);
		}
		PrintUtil.print("ffff slotSoulVo.gem1Id: "+slotSoulVo.gem1Id+" slotSoulVo.gem2Id: "+slotSoulVo.gem2Id+" slotSoulVo.gem3Id: "+slotSoulVo.gem3Id+" slotSoulVo.gem4Id: "+slotSoulVo.gem4Id);
		for(int i=1;i<=4;i++){
			ItemPo maxGemItemPo=null;
			int maxPower=0;
			for (RolePackItemVo rolePackItemVo : rolePo.mainPackItemVosMap.values()) {
				ItemPo itemPo=rolePackItemVo.itemPo();
				if(itemPo.wasGem()){
					if(itemPo.getCategory()==41){
						if(rolePo.getCareer()==RoleType.CAREER_WARRIOR || rolePo.getCareer()==RoleType.CAREER_RANGER){
							continue;
						}
					}
					if(itemPo.getCategory()==40){
						if(rolePo.getCareer()==RoleType.CAREER_MAGE){
							continue;
						}
					}

					if(slotSoulVo.gem1Id!=0){
						ItemPo itemPo2=ItemPo.findEntity(slotSoulVo.gem1Id);
						if(itemPo2.getCategory()==itemPo.getCategory().intValue() || itemPo2.getId().intValue() == itemPo.getId().intValue()){
							continue;
						}
					}
					if(slotSoulVo.gem2Id!=0){
						ItemPo itemPo2=ItemPo.findEntity(slotSoulVo.gem2Id);
						if(itemPo2.getCategory()==itemPo.getCategory().intValue() || itemPo2.getId().intValue() == itemPo.getId().intValue()){
							continue;
						}
					}
					if(slotSoulVo.gem3Id!=0){
						ItemPo itemPo2=ItemPo.findEntity(slotSoulVo.gem3Id);
						if(itemPo2.getCategory()==itemPo.getCategory().intValue() || itemPo2.getId().intValue() == itemPo.getId().intValue()){
							continue;
						}
					}
					if(slotSoulVo.gem4Id!=0){
						ItemPo itemPo2=ItemPo.findEntity(slotSoulVo.gem3Id);
						if(itemPo2.getCategory()==itemPo.getCategory().intValue() || itemPo2.getId().intValue() == itemPo.getId().intValue()){
							continue;
						}
					}
					
					int val = GameUtil.buildBattlePower(itemPo.listEqpBatAttrExp);
					if(val>maxPower){
						maxPower=val;
						maxGemItemPo=itemPo;
					}
				}
			}
			if(maxGemItemPo!=null){
				rolePo.soulGemInsert( equipSlot, i, maxGemItemPo.getId());
			}
		}
		rolePo.calculateBat(1);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键移除
	 * @param equipSlot
	 * @return
	 */
	public Object oneKeySoulGemRemove(Integer equipSlot){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(4);
		
		SlotSoulVo slotSoulVo = rolePo.findSlotSoul(equipSlot);
		if(slotSoulVo.gem1Id!=0){
			rolePo.soulGemRemove(equipSlot, 1);
		}
		if(slotSoulVo.gem2Id!=0){
			rolePo.soulGemRemove(equipSlot, 2);
		}
		if(slotSoulVo.gem3Id!=0){
			rolePo.soulGemRemove(equipSlot, 3);
		}
		if(slotSoulVo.gem4Id!=0){
			rolePo.soulGemRemove(equipSlot, 4);
		}
		rolePo.calculateBat(1);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 镶嵌宝石
	 * @param equipSlot
	 * @param gemIndex
	 * @param itemId
	 * @return
	 */
	public Object soulGemInsert(Integer equipSlot,Integer gemIndex,Integer itemId){
		CheckUtil.checkIsNull(gemIndex);
		CheckUtil.checkIsNull(equipSlot);
		CheckUtil.checkIsNull(itemId);
		checkService.checkExistItemPo(itemId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println(" SoulRemoting.soulGemInsert() equipSlot = " +equipSlot +"; gemIndex = " +gemIndex +"; itemId = "+ itemId);
		String key = rolePo.getId() +"_SoulRemoting.soulGemInsert";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.soulGemInsert( equipSlot, gemIndex, itemId);
		rolePo.calculateBat(1);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 卸载宝石
	 * @param equipSlot
	 * @param gemIndex
	 * @param itemId
	 * @return
	 */
	public Object soulGemRemove(Integer equipSlot,Integer gemIndex){
		CheckUtil.checkIsNull(gemIndex);
		CheckUtil.checkIsNull(equipSlot);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_SoulRemoting.soulGemRemove";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkItemPackFull(1);
		rolePo.soulGemRemove( equipSlot, gemIndex);
		rolePo.sendUpdateSoulSlot(equipSlot);
		rolePo.calculateBat(1);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 查询灵魂系统
	 * @return
	 */
	public Object fetchRoleSoul(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() + "_SoulRemoting.fetchRoleSoul";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l, true);
		rolePo.checkOpenSystem(TaskType.OPEN_SYSTEM_SOUL);
		SoulService soulService = SoulService.instance();
		int soulLv = rolePo.soulAtbMap.size() - 1;
		Map<String, List<IdNumberVo>> soulMap = soulService.fetchSoulList(rolePo.soulAtbMap);
//		Map<Integer, List<IdNumberVo>> soulMap = new HashMap<Integer, List<IdNumberVo>>();
		int soulType = rolePo.getSoulType().intValue();
		List<IdNumberVo2> currentSoulAtb = rolePo.soulAtbMap.get(soulLv);
		SessionUtil.addDataArray(currentSoulAtb);
		SessionUtil.addDataArray(soulMap);
		SessionUtil.addDataArray(soulType);
		SessionUtil.addDataArray(SoulType.fetchPossessAtbById(SoulType.Type.getType(soulType)));
		SessionUtil.addDataArray(SoulStepPo.findEntityByStep(soulLv).getMatchValue());
		SessionUtil.addDataArray(soulLv);
		SessionUtil.addDataArray(SoulType.fetchPossessAtbToId(SoulType.Type.getType(soulType)));
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 随机提升灵魂属性
	 * @param num 次数
	 * @return
	 */
	public Object randomUpSoul(Integer num){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() + "_SoulRemoting.randomUpSoul";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l, true);
		rolePo.checkOpenSystem(TaskType.OPEN_SYSTEM_SOUL);
		Map<Integer, List<IdNumberVo2>> soulMap = rolePo.soulAtbMap;
		int soulLv = soulMap.size() - 1;
		SoulStepPo soulStepPo = SoulStepPo.findEntityByStep(soulLv);
		int cost = soulStepPo.getRandomUpCost().intValue();
		int addExp = soulStepPo.getRandomUpAddExp().intValue();
		rolePo.publicCheckHasResource(RoleType.RESOURCE_SOUL, cost*num);
		SoulService soulService = SoulService.instance();
		if(soulService.fetchCanUpSoulAtb(soulMap).isEmpty()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2717"));
		}
		int totalNum = 0;
		for(int i = 0;i<num;i++){
			IdNumberVo idNumberVo = soulService.randomaSoulAtb(soulMap);
			if(idNumberVo==null){
				break;
			}
			SoulType.Type type = SoulType.Type.getType(idNumberVo.getId().intValue());
			soulService.addSoulAtbExp(soulMap, type, addExp);
			rolePo.setSoulType(soulService.changeSoulAtbType(soulMap, rolePo.getSoulType().intValue()));
			totalNum++;
		}
		rolePo.adjustNumberByType(-cost*totalNum, RoleType.RESOURCE_SOUL);
		List<IdNumberVo2> currentSoulAtb = rolePo.soulAtbMap.get(soulLv);
		rolePo.calculateBat(1);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(currentSoulAtb);
		SessionUtil.addDataArray(soulService.fetchSoulList(rolePo.soulAtbMap));
		SessionUtil.addDataArray(rolePo.getSoulType());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 灵魂转生
	 * @return
	 */
	public Object resetSoul(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() + "_SoulRemoting.resetSoul";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l, true);
		rolePo.checkOpenSystem(TaskType.OPEN_SYSTEM_SOUL);
		Map<Integer, List<IdNumberVo2>> soulMap = rolePo.soulAtbMap;
		int soulLv = soulMap.size() - 1;
		SoulStepPo soulNextStepPo = SoulStepPo.findEntityByStep(soulLv+1);
		if(soulNextStepPo==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2714"));
		}
		SoulStepPo soulStepPo = SoulStepPo.findEntityByStep(soulLv);
		int cost = soulStepPo.getStepUpCost().intValue();
		rolePo.publicCheckHasResource(RoleType.RESOURCE_SOUL, cost);
		rolePo.adjustNumberByType(-cost, RoleType.RESOURCE_SOUL);
		SoulService soulService = SoulService.instance();
		int sumAtbLv = soulService.sumSoulAtbLv(soulMap);
		if(sumAtbLv<soulStepPo.needElementLvs.get(0).intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2713"));
		}
		soulService.resetSoul(soulMap);
		soulLv = soulMap.size()-1;
		List<IdNumberVo2> currentSoulAtb = rolePo.soulAtbMap.get(soulLv);
		rolePo.calculateBat(1);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(currentSoulAtb);
		SessionUtil.addDataArray(soulService.fetchSoulList(rolePo.soulAtbMap));
		SessionUtil.addDataArray(soulLv);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 灵魂重置
	 * @return
	 */
	public Object backSoul(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() + "_SoulRemoting.backSoul";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l, true);
		rolePo.checkOpenSystem(TaskType.OPEN_SYSTEM_SOUL);
		SoulService soulService = SoulService.instance();
		int exp = soulService.fetchCurrentSoulCost(rolePo.soulAtbMap);
		exp = (int) (exp*0.8);
		rolePo.soulAtbMap = soulService.backSoul();
		int soulType = IntUtil.getRandomInt(1, 5);
		rolePo.setSoulType(soulType);
		rolePo.adjustNumberByType(exp, RoleType.RESOURCE_SOUL);
		rolePo.calculateBat(1);
		rolePo.sendUpdateTreasure(false);
		Map<Integer, List<IdNumberVo2>> soulMap = rolePo.soulAtbMap;
		int soulLv = soulMap.size() - 1;
		List<IdNumberVo2> currentSoulAtb = rolePo.soulAtbMap.get(soulLv);
		SessionUtil.addDataArray(currentSoulAtb);
		SessionUtil.addDataArray(soulService.fetchSoulList(rolePo.soulAtbMap));
		SessionUtil.addDataArray(soulType);
		SessionUtil.addDataArray(SoulType.fetchPossessAtbById(SoulType.Type.getType(soulType)));
		SessionUtil.addDataArray(SoulStepPo.findEntityByStep(soulLv).getMatchValue());
		SessionUtil.addDataArray(soulLv);
		SessionUtil.addDataArray(SoulType.fetchPossessAtbToId(SoulType.Type.getType(soulType)));
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 经验兑换灵魂值
	 * @return
	 */
	public Object exchangeSoul(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() + "_SoulRemoting.exchangeSoul";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l, true);
		rolePo.checkOpenSystem(TaskType.OPEN_SYSTEM_SOUL);
		int currentTimes = rolePo.getSoulExchangeTimes()==null?0:rolePo.getSoulExchangeTimes().intValue();
		currentTimes++;
		if(!rolePo.checkPlayTimes(currentTimes, PlayTimesType.PLAYTIMES_TYPE_550)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key208"));
		}
		Map<Integer, List<IdNumberVo2>> soulMap = rolePo.soulAtbMap;
		int soulLv = soulMap.size() - 1;
		SoulStepPo soulStepPo = SoulStepPo.findEntityByStep(soulLv);
		int changeExp = soulStepPo.getChangeExp().intValue();
		int currentExp = rolePo.getExp().intValue();
		if(currentExp<changeExp){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2715"));
		}
		int soulExp = (int)(currentExp/changeExp);
		currentExp -= soulExp*changeExp;
		rolePo.setExp(currentExp);
		rolePo.setSoulExchangeTimes(currentTimes);
		rolePo.adjustNumberByType(soulExp, RoleType.RESOURCE_SOUL);
		LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_EXP, 0, -soulExp*changeExp, GlobalCache.languageMap.get("key2716"), "");
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		return SessionType.MULTYPE_RETURN;
	}
}
