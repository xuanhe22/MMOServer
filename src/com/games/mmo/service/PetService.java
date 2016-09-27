package com.games.mmo.service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.po.game.PetTalentPo;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.ConstellAttachVo;
import com.games.mmo.vo.PetConstellVo;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetAttachs.PetAttach;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetConstells.PetConstell;
import com.storm.lib.base.BaseService;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BaseLogUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;

@Controller
public class PetService extends BaseService {
	
	public static final int CONSTELL_STRENGTHEN_TYPE = 1;//星座普通强化类型
	public static final int CONSTELL_STRENGTHEN_MAX_LEVEL = 5;//星座强化属性最高等级
	public static final int Constell_STRENGTHEN_MAX = 34;//强化累计最多次数
	
	/**
	 * 宠物星座点亮
	 */
	public void updatePetConstell(RolePo rolePo, int step){
		//根据升阶数点亮的为点亮的星座
		Collection<PetConstell> pcs = GlobalCache.petConstell.values();
		for(PetConstell pc:pcs)
		{
			if(pc.openNode <= step && rolePo.petConstellMap.get(pc.constellId) == null)
			{
				rolePo.saveNewPetConstell(pc, true);
			}
		}
	}
	
	/**
	 * 星座强化
	 * @param pc
	 * @param type
	 */
	public void constellStrengthen(PetConstellVo pc, PetConstell petConstell, int type, int attType)
	{
		int attachLevel = pc.attachLevel;
		String probability = "";
		switch (attachLevel) {
		case 1: probability = petConstell.probability1; break;
		case 2: probability = petConstell.probability2; break;
		case 3: probability = petConstell.probability3; break;
		case 4: probability = petConstell.probability4; break;
		case 5: probability = petConstell.probability5; break;
		default: probability = petConstell.probability5; break;
		}
		String[] strs = StringUtil.split(probability, ",");
		String str = type == CONSTELL_STRENGTHEN_TYPE?strs[0]:strs[1];
		String[] rans = str.split("\\|");
		int nowRandom = (RandomUtil.randomInteger(10000)+1);
		int x = -1;
		for(String ran:rans)
		{
			Integer random = Integer.parseInt(ran);
			if(nowRandom<=random)
				break;
			nowRandom-=random;
			x++;
		}
		//更新等级
		//-1\0\1(下降|不变|上升),最高不超过5级
		pc.attachLevel+=x;
		if(pc.attachLevel < 1)
			pc.attachLevel = 1;
		if(pc.attachLevel > CONSTELL_STRENGTHEN_MAX_LEVEL)
			pc.attachLevel=CONSTELL_STRENGTHEN_MAX_LEVEL;
		//更新类型
		if(attType == -1)
		{
			ConstellAttachVo caVo = GlobalCache.petConstellAttachVo.get(petConstell.constellId);
			pc.attachType = caVo.getRandomAttach();
		}
		else
			pc.attachType = attType;
		//更新值
		PetAttach petAttach = GlobalCache.petAttach.get(pc.attachLevel).get(pc.attachType);
		pc.icon = petAttach.icon;
		pc.name = petAttach.name;
		pc.updateAttachs(petAttach);
	}
	
	/**
	 * 天赋获得
	 */
	public void usePetTalent(RolePo rolePo, int star){
		Collection<ConcurrentHashMap<Integer, PetTalentPo>> ptPos = GlobalCache.petTalentMap.values();
		for(ConcurrentHashMap<Integer, PetTalentPo> ptPo:ptPos)
		{
			PetTalentPo pt = ptPo.get(1);
			if(star>=pt.talentStar)
			{
				List<Integer> petTalentList = rolePo.listPetTalent;
				boolean bl = true;
				for(Integer petTalentId:petTalentList)
				{
					PetTalentPo petTalentPo = GlobalCache.petTalentIdMap.get(petTalentId);
					if(petTalentPo.talentType == pt.talentType)
					{
						bl = false;
						break;
					}
				}
				if(bl)
					rolePo.saveNewPetTalent(pt.getId());
			}
		}
	}
	
	/**
	 * 宠物技能学习
	 * @param rpetPo
	 * @param petSkillPo
	 */
	public void petSkillLearn(RolePo rolePo, RpetPo rpetPo, PetSkillPo petSkillPo)
	{
//		System.out.println("petSkillLearn() rpetPo.name = " +rpetPo.getName());
		List<Integer> skillIds = rpetPo.skillIds;
		int size = skillIds.size();
		Integer random = GlobalCache.petSkillLearnMap.get(skillIds.size());
		if(random == null){
			BaseLogUtil.exceptionLogger.error("petSkillLearn learm num error", new NullPointerException());
		}
		if(rolePo.getPetMaxSkillSize()>size){
			rpetPo.skillIds.add(petSkillPo.getId());
		}else{
			int rdm = RandomUtil.randomInteger(10000)+1;
			if(rdm <= random)
			{
				rpetPo.skillIds.add(petSkillPo.getId());
				rolePo.setPetMaxSkillSize(rolePo.getPetMaxSkillSize()+1);
			}
			else
			{
				int index = RandomUtil.randomInteger(size);
				rpetPo.skillIds.remove(index);
				rpetPo.skillIds.add(index, petSkillPo.getId());
			}
		}
		rpetPo.updateSkillPlace();
	}
	
	private int findPetSkillMaxSize(RolePo rolePo){
		List<RpetPo> rpetPos = rolePo.listRpets;
		int size = 0;
		for(RpetPo rpetPo:rpetPos){
			size = Math.max(size, rpetPo.skillIds.size());
		}
		return size;
	}
}
