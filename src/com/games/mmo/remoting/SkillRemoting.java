package com.games.mmo.remoting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.CheckService;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.StringUtil;

@Controller
public class SkillRemoting extends BaseRemoting{
	@Autowired
	private CheckService checkService;
	/**
	 * 
	 * 方法功能:保存技能阵型
	 * 更新时间:2014-12-4, 作者:johnny
	 * @param str
	 * @return
	 */
	public Object saveSkillFormation(String formationStr){
//		System.out.println("saveSkillFormation()="+formationStr);
		CheckUtil.checkIsNull(formationStr);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SkillRemoting.saveSkillFormation";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<Integer> formations = StringUtil.getListByStr(formationStr);
		rolePo.listSkillFormation=formations;
		SessionUtil.addDataArray(rolePo.listSkillFormation);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	
	/**
	 * 
	 * 方法功能:升级技能
	 * 更新时间:2014-12-5, 作者:johnny
	 * @param skillId
	 * @return
	 */
	public Object upgradeSkill(Integer skillId){
		CheckUtil.checkIsNull(skillId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"SkillRemoting.upgradeSkill";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisSkillPo(skillId);	
		rolePo.upgradeSkill(skillId);
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.listSkillVos);
		return SessionType.MULTYPE_RETURN;
	}
	
}
