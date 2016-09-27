package com.games.mmo.remoting;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.AuctionItemPo;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.ItemService;
import com.games.mmo.service.MailService;
import com.games.mmo.type.ChatType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.PlayTimesType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.role.RolePackItemVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

@Controller
public class ItemRemoting extends BaseRemoting{
	@Autowired
	private BaseDAO baseDAO;
	@Autowired
	private ChatService chatService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private MailService mailService;
	/**
	 * 
	 * 方法功能:穿装备
	 * 更新时间:2014-11-4, 作者:johnny
	 * @param mainPackIndex
	 * @param equipSlot
	 * @return
	 */
	public Object equip(Integer mainPackIndex,Integer equipSlot){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		String key = rolePo.getId() +"_ItemRemoting.equip";
//		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.checkItemPackFull(1);
		rolePo.checkHasRolePackIndex(mainPackIndex);
		RolePackItemVo rolePackItemVo = rolePo.fetchRolePackItemByIndex(mainPackIndex);
		if(rolePackItemVo==null || rolePackItemVo.eqpPo==null ||!(rolePackItemVo.eqpPo.itemPo().getCategory().intValue()==equipSlot)){
			return SessionType.MULTYPE_RETURN;
		}
		checkService.checkExistItemPo(rolePackItemVo.getItemId());
		ItemPo itemPo = ItemPo.findEntity(rolePackItemVo.getItemId());
		if(itemPo.getMatchClass()!=0 && itemPo.getMatchClass()!=rolePo.getCareer().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key121"));
		}
		if(itemPo.getRequireLv().intValue() > rolePo.getLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key84"));
		}
		rolePo.unEquipBySlot(equipSlot,true);
		rolePo.equipBySlot(rolePackItemVo,equipSlot);
		if(ItemType.ITEM_CATEGORY_WEAPON==equipSlot.intValue() ||ItemType.ITEM_CATEGORY_ARMOR==equipSlot.intValue()){
			rolePo.sendAvatars();
		}
		rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_710,null,null);
		rolePo.calculateBat(1);
		rolePo.sendUpdateRoleBatProps(false);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateRoleEquipSlot(equipSlot);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:脱装备
	 * 更新时间:2014-11-4, 作者:johnny
	 * @param mainPackIndex
	 * @param equipSlot
	 * @return
	 */
	public Object unEquip(Integer equipSlot){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.unEquip";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.unEquipBySlot(equipSlot,true);
		if(ItemType.ITEM_CATEGORY_WEAPON==equipSlot.intValue() ||ItemType.ITEM_CATEGORY_ARMOR==equipSlot.intValue() 	){
			rolePo.sendAvatars();
		}
		rolePo.calculateBat(1);
		rolePo.sendUpdateRoleBatProps(false);	
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateRoleEquipSlot(equipSlot);
		return SessionType.MULTYPE_RETURN;
	}
	

	
	/**
	 * 出售背包物品
	 * @param index
	 * @return
	 */
	public Object sellPackItem(Integer index, Integer num){
		CheckUtil.checkIsNull(index);
		CheckUtil.checkIsNull(num);
		CheckUtil.checkPositive(num);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.sellPackItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		IdNumberVo idNumberVo =rolePo.sellPackItem(index, num);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(idNumberVo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键回收绿色装备
	 * @return
	 */
	public Object sellPackItemOneKey(String indexStr){
		CheckUtil.checkIsNull(indexStr);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.sellPackItemOneKey";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		List<Integer> itemIds=rolePo.sellPackItemOneKey(indexStr);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(itemIds);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键装备
	 * @return
	 */
	public Object oneKeyEquip(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.oneKeyEquip";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		int index = 0;
		for(int i=0;i<RoleType.EQUIP_SLOTS.length;i++){
			if(rolePo.fetchEquipIdBySlot(RoleType.EQUIP_SLOTS[i])==null)
			{
				RolePackItemVo rolePackItemVo = rolePo.findAvaEquipBySlot(RoleType.EQUIP_SLOTS[i]);
				if(rolePackItemVo!=null){
					ItemPo itemPo = rolePackItemVo.itemPo();
					if(itemPo.getRequireLv().intValue() > rolePo.getLv().intValue()){
						continue;
					}
					rolePo.equipBySlot(rolePackItemVo, RoleType.EQUIP_SLOTS[i]);
					rolePo.sendUpdateRoleEquipSlot(RoleType.EQUIP_SLOTS[i]);
					
					if(ItemType.ITEM_CATEGORY_WEAPON==RoleType.EQUIP_SLOTS[i] ||
						ItemType.ITEM_CATEGORY_ARMOR==RoleType.EQUIP_SLOTS[i] 	){
						index++;
					}
				}
			}
			else
			{
				Integer equipId = rolePo.fetchEquipIdBySlot(RoleType.EQUIP_SLOTS[i]);
				EqpPo eqpPo = EqpPo.findEntity(equipId);
				ItemPo itemPo = eqpPo.itemPo();
				RolePackItemVo rolePackItemVo = rolePo.findAvaEquipBySlot(RoleType.EQUIP_SLOTS[i]);
				if(rolePackItemVo != null){
					ItemPo packItemPo =   rolePackItemVo.itemPo(); 
					if(packItemPo.getRequireLv().intValue() > rolePo.getLv().intValue()){
						continue;
					}
				}
				boolean bool = rolePo.compareRoleEquipAndItemEquip(itemPo, rolePackItemVo);
				if(bool){
					if(rolePackItemVo != null){
						rolePo.unEquipBySlot(RoleType.EQUIP_SLOTS[i],true);
						rolePo.equipBySlot(rolePackItemVo,RoleType.EQUIP_SLOTS[i]);
						rolePo.sendUpdateRoleEquipSlot(RoleType.EQUIP_SLOTS[i]);
					}
				}
				if(ItemType.ITEM_CATEGORY_WEAPON==RoleType.EQUIP_SLOTS[i] ||
						ItemType.ITEM_CATEGORY_ARMOR==RoleType.EQUIP_SLOTS[i] 	){
						index++;
					}
			}
		}
		if(index > 0){
			rolePo.sendAvatars();			
		}
		rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_710,null,null);
		rolePo.sendUpdateMainPack(false);
		rolePo.calculateBat(1);
		rolePo.sendUpdateRoleBatProps(false);
//		rolePo.fighter.sendAttrChangeInfor();
		
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:整理背包
	 * 更新时间:2011-11-29, 作者:johnny
	 * @param type 1=背包 5=仓库
	 * @return [0]pcaktype [1]map
	 */
	public Object setPack(Integer type){
//		System.out.println("setPack() type = "+type);
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.setPack";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(type.intValue() == 1){
			rolePo.makeSetPack(new int[0], rolePo.mainPackItemVosMap, type);			
		}else if (type.intValue() == 5){
			rolePo.makeSetPack(new int[0], rolePo.warehousePackItemVosMap, type);
		}
		SessionUtil.addDataArray(rolePo.mainPackItemVosMap);
		SessionUtil.addDataArray(rolePo.warehousePackItemVosMap);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 强化装备
	 * @param equipId
	 * @param itemId
	 * @return
	 */
	public Object powerEquip(Integer equipId,Integer itemId){
		CheckUtil.checkIsNull(equipId);
		CheckUtil.checkIsNull(itemId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.powerEquip";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		checkService.checkExisEquip(equipId);
		EqpPo eqpPo = EqpPo.findEntity(equipId);	
		Integer result=rolePo.powerEquip( equipId, itemId);
		rolePo.calculateBat(1);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateEquip(eqpPo);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateRoleBatProps(false);
		SessionUtil.addDataArray(result);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:拍卖行-卖东西
	 * 更新时间:2014-11-14, 作者:johnny
	 * @param roleMainPackIndex
	 * @param count
	 * @param totalPrice
	 * @param sellType 寄售类型：0普通; 1钻石;
	 * @return
	 */
	public Object auctionSell(Integer roleMainPackIndex,Integer count,Integer totalPrice,Integer sellType){
		CheckUtil.checkIsNull(roleMainPackIndex);
		CheckUtil.checkIsNull(count);
		CheckUtil.checkIsNull(totalPrice);
		CheckUtil.checkIsNull(sellType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.auctionSell";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.auctionSell(roleMainPackIndex, count, totalPrice, sellType);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(rolePo.fetchAuctionMySellList());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 
	 * 方法功能:拍卖行-读取我的售卖列表
	 * 更新时间:2014-11-14, 作者:johnny
	 * @return
	 */
	public Object auctionFetchMySellList(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.auctionFetchMySellList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SessionUtil.addDataArray(rolePo.fetchAuctionMySellList());
		return SessionType.MULTYPE_RETURN;
	}
	

	/**
	 * 
	 * 方法功能:拍卖行-搜索拍卖列表
	 * 更新时间:2014-11-14, 作者:johnny
	 * @param keyword
	 * @param category
	 * @return
	 */
	public Object auctionFetchSeachList(String keyword,Integer type,Integer category){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.auctionFetchSeachList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println("ItemRemoting.auctionFetchSeachList() "+rolePo.getName()+"; keyword="+keyword+"; type="+type+"; category="+category);
		StringBuffer sb = new StringBuffer("from AuctionItemPo ");
		sb.append(" where 1=1 ");
		if(type !=null && type.intValue() !=0){
			sb.append(" and sellItemType = " ).append(type);
		}
		if(category !=null && category.intValue() != 0){
			sb.append(" and sellItemCategory=").append(category);
		}
		if(keyword != null && !"".equals(keyword)){
			sb.append(" and sellItemName like '%").append(keyword).append("%'");
		}
		List<AuctionItemPo> auctionItemPoList =BaseDAO.instance().dBfind(sb.toString());
		List<AuctionItemPo> list = rolePo.fetchAuctionFilterExpirationTime(auctionItemPoList);
		
		for(AuctionItemPo auctionItemPo : list){
			auctionItemPo.eqpPo = auctionItemPo.eqpPo();
		}
		
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:拍卖行-取消拍卖
	 * 更新时间:2014-11-14, 作者:johnny
	 * @param keyword
	 * @param category
	 * @return
	 */
	public Object auctionCancelAuction(Integer auctionId){
		CheckUtil.checkIsNull(auctionId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.auctionCancelAuction";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.auctionCancelAuction(auctionId);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.fetchAuctionMySellList());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:拍卖行-购买卖品
	 * 更新时间:2014-11-14, 作者:johnny
	 * @param auctionId
	 * @return
	 */
	public Object auctionBuyAuction(Integer auctionId){
		CheckUtil.checkIsNull(auctionId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.auctionBuyAuction";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		if(auctionItemPo==null){
			ExceptionUtil.throwConfirmParamException("sold");
		}
		rolePo.auctionBuyAuction(auctionId);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.fetchAuctionMySellList());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 拍卖行炫耀
	 * @param auctionId
	 * @return
	 */
	public Object auctionShowItem(Integer auctionId){
		CheckUtil.checkIsNull(auctionId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.auctionShowItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		if(auctionItemPo == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+auctionId);
		}
		String chatTag = chatService.buildTag(ItemType.ITEM_BIG_TYPE_AUCTION,auctionId,auctionItemPo.getItemId(),auctionItemPo.getEquipId());
		chatService.sendWorldFromSystemChat(rolePo.getName()+GlobalCache.fetchLanguageMap("key139")+chatTag+auctionItemPo.getNum()+GlobalCache.fetchLanguageMap("key140"));
		return 1;
	}
	
	/**
	 * 聊天炫耀道具
	 * @param itemId
	 * @return
	 */
	public Object chatShowItem(Integer itemId){
		CheckUtil.checkIsNull(itemId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.chatShowItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		String chatTag = chatService.buildTag(ItemType.ITEM_BIG_TYPE_ITEM,0,itemId,0);
		chatService.sendSystemWorldChat(rolePo.getName()+GlobalCache.fetchLanguageMap("key141")+chatTag+GlobalCache.fetchLanguageMap("key142"));
		return 1;
	}

	/**
	 * 聊天炫耀装备
	 * @param itemId
	 * @return
	 */
	public Object chatShowEqpInfor(Integer eqpId){ 
		CheckUtil.checkIsNull(eqpId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.chatShowEqpInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		EqpPo eqpPo = EqpPo.findEntity(eqpId);
		String chatTag = chatService.buildTag(ItemType.ITEM_BIG_TYPE_EQUIP,eqpPo.getId(),eqpPo.itemPo().getId(),eqpPo.getId());
		chatService.sendSystemWorldChat(rolePo.getName()+GlobalCache.fetchLanguageMap("key143")+chatTag+GlobalCache.fetchLanguageMap("key142"));
		return 1;
	}
	
	
	/**
	 * 显示拍卖行道具信息
	 * @param auctionId
	 * @return
	 */
	public Object showAuctionInfor(Integer auctionId){
		CheckUtil.checkIsNull(auctionId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.showAuctionInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisAuctionItemPo(auctionId);
		AuctionItemPo auctionItemPo=AuctionItemPo.findEntity(auctionId);
		EqpPo eqpPo = EqpPo.findEntity(auctionItemPo.getEquipId());
		SessionUtil.addDataArray(auctionItemPo);
		SessionUtil.addDataArray(eqpPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 显示装备信息
	 * @param auctionId
	 * @return
	 */
	public Object showEquipInfor(Integer eqpId){
		CheckUtil.checkIsNull(eqpId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.showEquipInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		EqpPo eqpPo = EqpPo.findEntity(eqpId);
		SessionUtil.addDataArray(eqpPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object itemUse(Integer mainPackIndex,Integer num){
//		System.out.println("mainPackIndex = " +mainPackIndex+"; num="+num);
		CheckUtil.checkIsNull(mainPackIndex);
		CheckUtil.checkIsNull(num);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.itemUse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkHasRolePackIndex(mainPackIndex);
		RolePackItemVo rolePackItemVo =rolePo.fetchRolePackItemByIndex(mainPackIndex);
//		System.out.println(" rolePackItemVo = " +rolePackItemVo);
		int itemId = rolePackItemVo.getItemId();
		if(itemId==300010032 && num.intValue() > 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2292"));
		}
		if(!rolePackItemVo.itemPo().couldUse()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2710"));
		}
		List<IdNumberVo> list=rolePo.itemUse( mainPackIndex, num, false);
		rolePo.calculateBat(1);
		rolePo.removeItemFromMainPack(mainPackIndex,num,GlobalCache.fetchLanguageMap("key2458"),true);
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdatePetList();
		SessionUtil.addDataArray(list);
		SessionUtil.addDataArray(itemId);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * Hp药使用
	 * @return
	 */
	public Object hpMedicineUse(Integer mainPackIndex){
		CheckUtil.checkIsNull(mainPackIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.hpMedicineUse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		//TODO 【业务标记】压测需要
		if(!BaseStormSystemType.ALLOW_CHEAT){
			if(rolePo.fighter!=null && rolePo.fighter.getBatHp().intValue() == rolePo.fighter.batMaxHp.intValue()){
				return 1;
			}
		}
		rolePo.checkHasRolePackIndex(mainPackIndex);
		RolePackItemVo rolePackItemVo =rolePo.fetchRolePackItemByIndex(mainPackIndex);
		if(!rolePackItemVo.itemPo().couldUse()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2710"));
		}
		int itemId = rolePackItemVo.getItemId();
		rolePo.itemUse( mainPackIndex, 1, false);	
		RolePackItemVo rolePackItemVo2=null;
		rolePackItemVo2=rolePo.removeItemFromMainPack(mainPackIndex,1,GlobalCache.fetchLanguageMap("key2657"),false);
		if(rolePackItemVo2!=null){
			rolePo.sendUpdateSingleMainPack(rolePackItemVo2, false);
		}
		else{
			rolePo.sendUpdateMainPack(false);
		}
		SessionUtil.addDataArray(itemId);
		return SessionType.MULTYPE_RETURN;
	}
	/**
	 * Mp药使用
	 * @return
	 */
	public Object mpMedicineUse(Integer mainPackIndex){
//		System.out.println("mpMedicineUse() mainPackIndex="+mainPackIndex);
		CheckUtil.checkIsNull(mainPackIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.mpMedicineUse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("mp="+rolePo.fighter.batMp+"; maxMp="+rolePo.fighter.batMaxMp);
		if(!BaseStormSystemType.ALLOW_CHEAT){
			if(rolePo.fighter!=null && rolePo.fighter.batMp.intValue() == rolePo.fighter.batMaxMp.intValue()){
				return 1;
//				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2317"));
			}
		}
		rolePo.checkHasRolePackIndex(mainPackIndex);

		RolePackItemVo rolePackItemVo =rolePo.fetchRolePackItemByIndex(mainPackIndex);
		if(!rolePackItemVo.itemPo().couldUse()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2710"));
		}
		int itemId = rolePackItemVo.getItemId();
		rolePo.itemUse(mainPackIndex, 1, false);
		rolePo.removeItemFromMainPack(mainPackIndex,1,GlobalCache.fetchLanguageMap("key2657"),false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(itemId);
//		System.out.println("use mp1");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 切割背包物品
	 * @param index
	 * @return
	 */
	public Object splitPackItem(Integer index, Integer num){
//		System.out.println("splitPackItem() index="+index+"; num="+num);
		CheckUtil.checkIsNull(index);
		CheckUtil.checkIsNull(num);
		CheckUtil.checkPositive(num);
		if(num < 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key146"));
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.splitPackItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkHasRolePackIndex(index);
		rolePo.checkItemPackFull(1);
		RolePackItemVo rolePackItemVo =rolePo.fetchRolePackItemByIndex(index);
//		System.out.println("rolePackItemVo.num="+rolePackItemVo.num);
		if(rolePackItemVo.num<=1 || rolePackItemVo.num.intValue() <= num.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key144"));
		}
		int remainNum = rolePackItemVo.getNum()-num;
		rolePackItemVo.setNum(remainNum);

		RolePackItemVo rolePackItemVo2 = new RolePackItemVo();
		rolePackItemVo2.setNum(num);
		rolePackItemVo2.setItemId(rolePackItemVo.getItemId());
		rolePackItemVo2.setBindStatus(rolePackItemVo.bindStatus);
		rolePo.singleRolePackItemVoToPack(rolePackItemVo2, PlayTimesType.PLAYTIMES_TYPE_420, rolePo.mainPackItemVosMap);	
		
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * NPC 售卖商品
	 * @param npcId
	 * @param itemId
	 * @param num
	 * @return
	 */
	public Object npcSellItem(Integer monsterId,Integer itemId,Integer num){
		CheckUtil.checkIsNull(monsterId);
		CheckUtil.checkIsNull(itemId);
		CheckUtil.checkIsNull(num);
		CheckUtil.checkPositive(num);
		if(num > 99){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key145"));
		}
		if(num < 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key146"));
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.npcSellItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.npcSellItem( monsterId, itemId, num);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		return 1;
	}
	
	/**
	 * 商店售卖
	 * @param productId
	 * @param num
	 * @return
	 */
	public Object productSellItem(Integer productId, Integer num){
		CheckUtil.checkIsNull(productId);
		CheckUtil.checkIsNull(num);
		CheckUtil.checkPositive(num);
		if(num > 99){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key145"));
		}
		if(num < 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key146"));
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.productSellItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.sendUpdateSkillPoint();
		rolePo.productSellItem(productId,num);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateBuyProductList();
		return 1;
	}
	
	/**
	 * 合成道具
	 * @param mergeId
	 * @param num
	 * @return
	 */
	public Object mergeItem(Integer mergeId,Integer num){
		CheckUtil.checkIsNull(mergeId);
		CheckUtil.checkIsNull(num);
		CheckUtil.checkPositive(num);
		if(num.intValue() < 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key146"));
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.mergeItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("mergeItem() name="+rolePo.getName() +"; mergeId="+mergeId+"; num="+num);
		Integer success = rolePo.mergeItem(mergeId, num);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(success);
		return SessionType.MULTYPE_RETURN;
	}
	

	/**
	 * 从背包取出=>放入仓库
	 * @return
	 */
	public Object putInByWareHouse(String indexStr){
		CheckUtil.checkIsNull(indexStr);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.putInByWareHouse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.putInByWareHouse(indexStr);
		SessionUtil.addDataArray(rolePo.mainPackItemVosMap);
		SessionUtil.addDataArray(rolePo.warehousePackItemVosMap);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 从仓库取出=>放入背包
	 * @return
	 */
	public Object takeOutByWarehouse(String indexStr){
		CheckUtil.checkIsNull(indexStr);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.takeOutByWarehouse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.takeOutByWarehouse( indexStr);
		SessionUtil.addDataArray(rolePo.mainPackItemVosMap);
		SessionUtil.addDataArray(rolePo.warehousePackItemVosMap);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 放入金币或绑金到仓库
	 * @param goldType 1:金币； 5绑金
	 * @param num
	 * @return
	 */
	public Object putInGoldOrBindGoldByWareHouse(Integer goldType, Integer num){
		CheckUtil.checkIsNull(goldType);
		CheckUtil.checkIsNull(num);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.putInGoldOrBindGoldByWareHouse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.putInGoldOrBindGoldByWareHouse( goldType,  num);
		SessionUtil.addDataArray(rolePo.getGold());
		SessionUtil.addDataArray(rolePo.getBindGold());
		SessionUtil.addDataArray(rolePo.getWarehouseGold());
		SessionUtil.addDataArray(rolePo.getWarehouseBindGold());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 从仓库获取金币或者绑金
	 * @param goldType 10:仓库金币； 11：仓库绑金
	 * @param num
	 * @return
	 */
	public Object takeOutGoldOrBindGoldByWarehouse(Integer goldType, Integer num){
		CheckUtil.checkIsNull(goldType);
		CheckUtil.checkIsNull(num);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ItemRemoting.takeOutGoldOrBindGoldByWarehouse";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeOutGoldOrBindGoldByWarehouse( goldType,  num);
		SessionUtil.addDataArray(rolePo.getGold());
		SessionUtil.addDataArray(rolePo.getBindGold());
		SessionUtil.addDataArray(rolePo.getWarehouseGold());
		SessionUtil.addDataArray(rolePo.getWarehouseBindGold());
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object getMatchedItem(String term, Integer maxLength){
		return itemService.findItemByIdOrName(term, maxLength);
	}
	
	/**
	 * 使用道具可选择获得指定道具
	 * @param mainPackIndex
	 * @param choiceId
	 * @return
	 */
	public Object useChoiceItem(Integer mainPackIndex,Integer choiceId){
		CheckUtil.checkIsNull(mainPackIndex);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		String key = rolePo.getId() +"_ItemRemoting.useChoiceItem";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkHasRolePackIndex(mainPackIndex);
		RolePackItemVo rolePackItemVo =rolePo.fetchRolePackItemByIndex(mainPackIndex);
		ItemPo itemPo = rolePackItemVo.itemPo();
		if(rolePackItemVo.itemPo().getCategory()!=ItemType.ITEM_CATEGORY_CHOICE_ITEM){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2710"));
		}
		ChatService chatService = ChatService.instance();
		String[] jobItems=StringUtil.split(itemPo.getDropItems(), "$");
		String[] strs = StringUtil.split(jobItems[0], ";");
		if(jobItems.length==3){
			strs = StringUtil.split(jobItems[rolePo.getCareer()-1], ";");
		}
		IdNumberVo idNumberVo = new IdNumberVo();
		outer:for (int j = 0; j < strs.length; j++) {
			List<List<Integer>> dropList = StringUtil.buildBattleExpressList(strs[j]);
			for(List<Integer> drops:dropList){
				if(drops.get(0).intValue()==choiceId){
					idNumberVo.setId(choiceId);
					idNumberVo.setNum(drops.get(1));
					break outer;
				}
			}
		}
		if(idNumberVo.getId().intValue()>0){
			rolePo.sendChatByItem(itemPo.getId(), choiceId, ChatType.CHAT_DROP_TYPE_OPEN_BOX);
			if(itemPo.getId().intValue()==300004065&&IntUtil.checkInInts(choiceId, new int[]{300004051,300010036,300099007})){
				String str = GlobalCache.fetchLanguageMap("key2702");
				String sb = MessageFormat.format(str, rolePo.getName() , itemPo.getName(),ItemPo.findEntity(choiceId).getName());
				chatService.sendHorse(sb);
			}
			if(itemPo.getId().intValue()==300004087&&IntUtil.checkInInts(choiceId, new int[]{300004052,300010057,300004051})){
				String str = GlobalCache.fetchLanguageMap("key2702");
				String sb = MessageFormat.format(str, rolePo.getName() , itemPo.getName(),ItemPo.findEntity(choiceId).getName());
				chatService.sendHorse(sb);
			}
			rolePo.addItem(idNumberVo.getId(), idNumberVo.getNum(), rolePackItemVo.getBindStatus());
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_ITEM, idNumberVo.getId(),idNumberVo.getNum(), GlobalCache.fetchLanguageMap("key2475"), "");
		}
		rolePo.calculateBat(1);
		rolePo.removeItemFromMainPack(mainPackIndex,1,GlobalCache.fetchLanguageMap("key2458"),true);
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdatePetList();
		return SessionType.MULTYPE_RETURN;
	}
	
}
