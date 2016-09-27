package com.games.mmo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.AuctionItemPo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.ForbidPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.MailPo;
import com.games.mmo.po.ProductPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.FashionPo;
import com.games.mmo.po.game.InvitationPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.LvConfigPo;
import com.games.mmo.po.game.MilitaryRankPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetUpgradePo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.storm.lib.base.BaseService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;



@Controller
public class CheckService extends BaseService {
	@Autowired
	private RoleService roleService;
	
	/**
	 * 
	 * 方法功能:检查是否日刷新
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param lastDailyFreshTime
	 * @return
	 */
	public static boolean checkRequireDailyFresh(Long lastDailyFreshTime) {
		return DateUtil.checkRequireDailyFresh(lastDailyFreshTime, 0, 1);

	}
	
	
	/**
	 * 检查商店配置表
	 * @param productId
	 */
	public void checkExistProductPo(Integer productId){
		ProductPo productPo = ProductPo.findEntity(productId);
		if(productPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_product："+productId);
		}
	}
	/**
	 * 检查道具配置表
	 * @param itemId
	 */
	public void checkExistItemPo(Integer itemId){
		ItemPo itemPo = ItemPo.findEntity(itemId);
		if(itemPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_item："+itemId);
		}
	}
	
	/**
	 * 检查宠物升级配置表
	 * @param petUpGradeId
	 */
	public void checkExisPetUpgradePo(Integer petUpGradeId){
		PetUpgradePo petUpGradePo = PetUpgradePo.findEntity(petUpGradeId);
		if(petUpGradePo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"PetUpgradePo："+petUpGradeId);
		}
	}
	
	/**
	 * 检查宠物配置表
	 * @param petId
	 */
	public void checkExistPetPo(Integer petId){
		PetPo petPo = PetPo.findEntity(petId);
		if(petPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_pet："+petId);
		}
	}
	
	/**
	 * 检查宠物是否存在
	 * @param rPetId
	 * @param roleId
	 */
	public void checkExistRpetPo(Integer rpetId){
		RpetPo rpetPo = RpetPo.findEntity(rpetId);
		if(rpetPo == null){
//			System.out.println("rpetId="+rpetId);
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_rpet："+rpetId);
		}
	}
	
	/**
	 * 检查宠物是否属于该玩家
	 * @param rpetId
	 * @param roleId
	 */
	public void checkOwnRpetPo(Integer rpetId, Integer roleId){
		RolePo rolePo = roleService.getRolePo(roleId);
		for(RpetPo rpetPo :  rolePo.listRpets){
			if(rpetPo.getId().intValue() == rpetId.intValue()){
				return;
			}
		}
		ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"listRpets："+rpetId);
	}
	
	public void checkExisSkillPo(Integer skillId){
		SkillPo skillPo = SkillPo.findEntity(skillId);
		if(skillPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_skill："+skillId);
		}
	}
	
	public void checkExisBuffPo(Integer buffId){
		BuffPo buffPo = BuffPo.findEntity(buffId);
		if(buffPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_buff："+buffId);
		}
	}
	
	
	public void checkExisLvConfigPo(Integer lvId){
		LvConfigPo lvConfigPo = LvConfigPo.findEntity(lvId);
		if(lvConfigPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_lv_config："+lvId);
		}
	}
	
	public void checkExisGuildPo(Integer guildId){
		GuildPo guildPo = GuildPo.findEntity(guildId);
		if(guildPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key236"));
		}
	}
	public void checkExisCopySceneConfPo(Integer copySceneConfId){
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfId);
		if(cscp == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_copy_scene_conf："+copySceneConfId);
		}
	}
	
	public void checkExisCopyScenePo(Integer copySceneId){
		CopyScenePo csp = CopyScenePo.findEntity(copySceneId);
		if(csp == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_copy_scene："+copySceneId);
		}
	}
	
	public void checkExisScenePo(Integer sceneId){
		ScenePo scenePo = ScenePo.findEntity(sceneId);
		if(scenePo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_scene："+sceneId);
		}
	}
	
	public void checkExisCopySceneActivityPo(Integer copySceneActivityId){
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(copySceneActivityId);
		if(copySceneActivityPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_copy_scene_activity："+copySceneActivityId);
		}
	}
	
	
	public void checkExisRolePo(Integer roleId){
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_role："+roleId);
		}
	}
	
	public void checkExisRoleInforPo(Integer inforId){
		RoleInforPo roleInforPo = RoleInforPo.findEntity(inforId);
		if(roleInforPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_role_infor："+inforId);
		}
	}
	
	public void checkExisDungeonTeamVo(Integer itemId, Integer copySceneConfPoId){
		TeamVo teamVo = GlobalCache.teamDungeonIdMaps.get(copySceneConfPoId).get(itemId);
		if(teamVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key187"));
		}
	}
	
	public void checkExisDungeonMemberVo(Integer roleId, Integer copySceneConfPoId){
		TeamMemberVo teamMemberVo = GlobalCache.teamDungeonMemberVos.get(copySceneConfPoId).get(roleId);
		if(teamMemberVo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key219")+"roleId: "+roleId);
		}
	}
	
	public void checkExisRechargePo(Integer rechargeId){
		RechargePo rechargePo = RechargePo.findEntity(rechargeId);
		if(rechargePo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_recharge:  "+rechargeId);
		}
	}
	
	public void checkExisTaskPo(Integer taskId){
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if(taskPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_task:  "+taskId);
		}
	}
	
	public void checkExisMonsterPo(Integer monsterId){
		MonsterPo monsterPo = MonsterPo.findEntity(monsterId);
		if(monsterPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_monster:  "+monsterId);
		}
	}
	
	public void checkExisMilitaryRankPo(Integer militaryRankId){
		MilitaryRankPo militaryRankPo = MilitaryRankPo.findEntity(militaryRankId);
		if(militaryRankPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"MilitaryRankPo:  "+militaryRankId);
		}
	}
	
	public void checkExisEquip(Integer equipId){
		EqpPo eqpPo = EqpPo.findEntity(equipId);
		if(eqpPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_equip:  "+equipId);
		}
	}
	
	public void checkExisInvitationPo(Integer id){
		InvitationPo invitationPo = InvitationPo.findEntity(id);
		if(invitationPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_invitation:  "+id);
		}
	}
	
	public void checkExisAuctionItemPo(Integer auctionId){
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		if(auctionItemPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_auction_item:  "+auctionId);
		}
	}
	
	public void checkExisMailPo(Integer mailId){
		MailPo mailPo = MailPo.findEntity(mailId);
		if(mailPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_mail:  "+mailId);
		}
	}
	
	public void checkExisForbidPo(Integer id){
		ForbidPo forbidPo = ForbidPo.findEntity(id);
		if(forbidPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"u_po_forbid:  "+id);
		}
	}

	public void checkExisFashionPo(Integer id){
		FashionPo fashionPo = FashionPo.findEntity(id);
		if(fashionPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"po_fashion:  "+id);
		}
	}
	

	private static CheckService instance;
	public static CheckService instance() {
		if(instance==null){
			instance=(CheckService) BeanUtil.getBean("checkService");
		}
		return instance;
	}
	
//	public void checkExisCurrentSessionRole(RolePo rolePo){
//		if(rolePo==null){
//			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
//		}
//	}
//	59 7A 00 00 00 0F 03 32 30 30 31 7C 7C 36 36 30 31 30 30 30 30 31 7A 59 
}
