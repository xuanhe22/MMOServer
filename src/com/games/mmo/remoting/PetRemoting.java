package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.event.listener.PetConstellStarListener;
import com.games.mmo.event.listener.PetUpGradeListener;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.PetUpgradePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.PetService;
import com.games.mmo.type.UserEventType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.SessionUtil;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.event.EventArg;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;

@Controller
public class PetRemoting extends BaseRemoting{
	@Autowired
	private CheckService checkService;
	@Autowired
	private ChatService chatService;
	@Autowired
	private PetService petService;
	
	/**
	 * 添加事件监听
	 */
	@Override
	protected void initListener() {
		//为宠物升阶监听
		addListener(UserEventType.PET_UP_GRADE.getValue(), (PetUpGradeListener)BeanUtil.getBean("petUpGradeListener"));
		addListener(UserEventType.PET_CONSTELL_STAR.getValue(), (PetConstellStarListener)BeanUtil.getBean("petConstellStarListener"));
	}

	/**
	 * 宠物抽取
	 * @param way 1=金币抽取 2=钻石抽取 3=金币十连抽 4=钻石十连抽  5抽取免费次数
	 * @return
	 */
	public Object petRoll(Integer rollType){
		CheckUtil.checkIsNull(rollType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.petRoll";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("petRoll() ="+rolePo.getName()+ "; rollType="+ rollType+"; rolePo.getPetRollGoldTodayTimes()="+rolePo.getPetRollGoldTodayTimes());
		rolePo.petRoll(rollType);
		rolePo.sendUpdatePetList();
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 宠物出战
	 * @param rpetId
	 * @return
	 */
	public Object positionFight(Integer rpetId){
		CheckUtil.checkIsNull(rpetId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.positionFight";
		GlobalCache.checkProtocolFrequencyResponse(key, 1000l,true);
		checkService.checkExistRpetPo(rpetId);
		checkService.checkOwnRpetPo(rpetId, rolePo.getId());
		
		//TODO 【业务标记】临时代码
		if(rolePo.fighter != null && rolePo.fighterPet != null && rolePo.fighterPet.cell != null){
			rolePo.fighter.mapRoom.cellData.removeLiving(rolePo.fighterPet, true);
		}
		
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		if(rpetPo != null){
			rolePo.setRpetFighterId(rpetId);	
		}else{
			rolePo.setRpetFighterId(0);	
		}
		rolePo.sendUpdatePetInfor();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 宠物休息
	 * @return
	 */
	public Object positionReset(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.positionReset";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.setRpetFighterId(0);
		rolePo.sendUpdatePetInfor();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 宠物改名
	 * @param rpetId
	 * @param name
	 * @return
	 */
	public Object changeName(Integer rpetId,String name){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.changeName";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		name=CheckUtil.checkIllegelName(name);
		CheckUtil.checkValidString(name, 12);
		CheckUtil.checkContianFiltedWord(name, true,null);

		checkService.checkExistRpetPo(rpetId);
		checkService.checkOwnRpetPo(rpetId, rolePo.getId());
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		rpetPo.setName(name);
		rolePo.sendUpdateRpet(rpetPo);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 宠物分解
	 * @param rpetIds
	 * @return
	 */
	public Object disolve(String rpetIds){
		CheckUtil.checkIsNull(rpetIds);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.disolve";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.disolve(rpetIds);
		rolePo.sendUpdatePetList();
		rolePo.sendUpdatePetInfor();
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	

	/**
	 * 宠物升级、升阶
	 * @param rpetId
	 * @return
	 */
	public Object ugrade(Integer rpetId){
		CheckUtil.checkIsNull(rpetId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.ugrade";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		Boolean bool=rolePo.ugrade(rpetId);
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		if(bool){
			PetUpgradePo pup = PetUpgradePo.findEntity(rpetPo.getLv());
			notifyListeners(new EventArg(rolePo, UserEventType.PET_UP_GRADE.getValue(), pup.getStep()));			
		}
		rolePo.sendUpdateRpet(rpetPo);
		rolePo.sendUpdateMainPack(false);
		rolePo.updateBatToFighterPet();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 宠物一键升级
	 * @param rpetId
	 * @return
	 */
	public Object ugradeOneKey(Integer rpetId){
		CheckUtil.checkIsNull(rpetId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.ugradeOneKey";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.ugradeOneKey(rpetId);
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		rolePo.sendUpdateRpet(rpetPo);
		rolePo.sendUpdateMainPack(false);
		rolePo.updateBatToFighterPet();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 星座强化
	 * @param type 强化类型(1普通2高级)
	 * @param constellId 星座ID
	 * @return
	 */
	public Object petConstellStrengthen(Integer type, Integer constellId){
		CheckUtil.checkIsNull(type);
		CheckUtil.checkIsNull(constellId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.petConstellStrengthen";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		List<Integer> resultList = rolePo.petConstellStrengthen( type,  constellId);
		if(resultList.get(0).intValue()==1){
			notifyListeners(new EventArg(rolePo, UserEventType.PET_CONSTELL_STAR.getValue(), resultList.get(1)));//发送宠物星座星数增加事件			
		}
		
		rolePo.updateBatToFighterPet();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdatePetInfor();
		rolePo.calculateBat(1);
		rolePo.sendUpdateRoleBatProps(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.petConstellStrengthensList);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 天赋升级
	 */
	public Object petTalentUpLevel(Integer alentId){
		CheckUtil.checkIsNull(alentId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.petTalentUpLevel";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.petTalentUpLevel( alentId);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdatePetInfor();
		rolePo.calculateBat(1);
		rolePo.sendUpdateRoleBatProps(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	/**
	 * 技能学习
	 */
	public Object petSkillLearn(Integer rpetId, Integer skillId){
		CheckUtil.checkIsNull(rpetId);
		CheckUtil.checkIsNull(skillId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_PetRemoting.petSkillLearn";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.petSkillLearn(rpetId,  skillId);
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdatePetInfor();
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateRoleBatProps(false);
		SessionUtil.addDataArray(rpetPo.skillIds);
		SessionUtil.addDataArray(1);
		SessionUtil.addDataArray(rolePo.getPetMaxSkillSize());
		return SessionType.MULTYPE_RETURN;
	}
}
