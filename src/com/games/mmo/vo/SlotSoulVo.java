package com.games.mmo.vo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.vo.xml.ConstantFile.Global;
import com.games.mmo.vo.xml.ConstantFile.SoulSlot.Extract.ExtractUpgrade;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;

/**
 * 附魂
 * @author Administrator
 *
 */
public class SlotSoulVo extends BasePropertyVo{
	/**
	 * 槽位编号
	 */
	public Integer slotNum=0;
	
	/**
	 * 槽位品质
	 */
	public Integer slotQuality = SlotSoulType.EXTRACT_QUALITY_PURPLE;
	
	/**
	 * 洗练槽位1品质
	 */
	public Integer extract1Quality = 0;
	
	/**
	 * 洗练槽位1星级
	 */
	public Integer extract1Star = 0;
	/**
	 * 洗练槽位1状态
	 */
	public Integer extract1Status=SlotSoulType.EXTRACT_STATUS_UNLOCK;
	/**
	 * 洗练槽位1属性类型
	 */
	public Integer extract1BatType=0;
	/**
	 * 洗练槽位1属性值
	 */
	public Integer extract1BatVal=0;
	
	
	/**
	 * 洗练槽位2品质
	 */
	public Integer extract2Quality = 0;
	
	/**
	 * 洗练槽位2星级
	 */
	public Integer extract2Star = 0;
	/**
	 * 洗练槽位2状态
	 */
	public Integer extract2Status=SlotSoulType.EXTRACT_STATUS_NONE;
	/**
	 * 洗练槽位2属性类型
	 */
	public Integer extract2BatType=0;
	/**
	 * 洗练槽位2属性值
	 */
	public Integer extract2BatVal=0;

	/**
	 * 洗练槽位3品质
	 */
	public Integer extract3Quality = 0;
	
	/**
	 * 洗练槽位3星级
	 */
	public Integer extract3Star = 0;
	/**
	 * 洗练槽位3状态
	 */
	public Integer extract3Status=SlotSoulType.EXTRACT_STATUS_NONE;
	/**
	 * 洗练槽位3属性类型
	 */
	public Integer extract3BatType=0;
	/**
	 * 洗练槽位3属性值
	 */
	public Integer extract3BatVal=0;
	/**
	 * 洗练普通洗练累计次数
	 */
	public Integer extractNormalTimes=0;
	/**
	 * 洗练高级洗练累计次数
	 */
	public Integer extractAdvanceTimes=0;
	/**
	 * 宝石槽位1宝石ID
	 */
	public Integer gem1Id=0;
	public int wasbind1=0;
	/**
	 * 宝石槽位2宝石ID
	 */
	public Integer gem2Id=0;
	public int wasbind2=0;
	/**
	 * 宝石槽位3宝石ID
	 */
	public Integer gem3Id=0;
	public int wasbind3=0;
	/**
	 * 宝石槽位4宝石ID
	 */
	public Integer gem4Id=0;
	public int wasbind4=0;
	
	/**
	 * 强化星级
	 */
	public Integer powerLv=0;
	/**
	 * 强化经验
	 */
	public Integer powerExp=0;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{slotNum,slotQuality,extract1Quality,extract1Star,extract1Status,extract1BatType,extract1BatVal,extract2Quality,extract2Star,extract2Status,extract2BatType,extract2BatVal,extract3Quality,extract3Star,extract3Status,extract3BatType,extract3BatVal,extractNormalTimes,extractAdvanceTimes,gem1Id,gem2Id,gem3Id,gem4Id,powerLv,powerExp};
	}
	
	
	
	Integer[] extractQualityArray = new  Integer[]{extract1Quality,extract2Quality,extract3Quality};
	Integer[] extractBatTypeArray = new Integer[]{extract1BatType,extract2BatType,extract3BatType};
	Integer[] extractBatValArray = new Integer[]{extract1BatVal,extract2BatVal,extract3BatVal};
	Integer[] extractStatusArray = new Integer[]{extract1Status,extract2Status,extract3Status};
	Integer[] extractStarArray = new Integer[]{extract1Star,extract2Star,extract3Star};
	
	public void initializeArray(){
		extractQualityArray = new  Integer[]{extract1Quality,extract2Quality,extract3Quality};
		extractBatTypeArray = new Integer[]{extract1BatType,extract2BatType,extract3BatType};
		extractBatValArray = new Integer[]{extract1BatVal,extract2BatVal,extract3BatVal};
		extractStatusArray = new Integer[]{extract1Status,extract2Status,extract3Status};
		extractStarArray = new Integer[]{extract1Star,extract2Star,extract3Star};
	}
	
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(vals.length!=29){
			String[] currentVals = StringUtil.split(val,spliter);
			 vals = new String[29];
			 for(int i=0; i < vals.length; i++){
				 if(i < currentVals.length){
					 vals[i]= currentVals[i];					 
				 }else{
					 vals[i]="1";
				 }
				 
			 }
		}
		slotNum=DBFieldUtil.fetchImpodInt(vals[0]);
		slotQuality =DBFieldUtil.fetchImpodInt(vals[1]);
		extract1Quality=DBFieldUtil.fetchImpodInt(vals[2]);
		extract1Star=DBFieldUtil.fetchImpodInt(vals[3]);
		extract1Status=DBFieldUtil.fetchImpodInt(vals[4]);
		extract1BatType=DBFieldUtil.fetchImpodInt(vals[5]);
		extract1BatVal=DBFieldUtil.fetchImpodInt(vals[6]);
		extract2Quality=DBFieldUtil.fetchImpodInt(vals[7]);
		extract2Star=DBFieldUtil.fetchImpodInt(vals[8]);
		extract2Status=DBFieldUtil.fetchImpodInt(vals[9]);
		extract2BatType=DBFieldUtil.fetchImpodInt(vals[10]);
		extract2BatVal=DBFieldUtil.fetchImpodInt(vals[11]);
		extract3Quality=DBFieldUtil.fetchImpodInt(vals[12]);
		extract3Star=DBFieldUtil.fetchImpodInt(vals[13]);
		extract3Status=DBFieldUtil.fetchImpodInt(vals[14]);
		extract3BatType=DBFieldUtil.fetchImpodInt(vals[15]);
		extract3BatVal=DBFieldUtil.fetchImpodInt(vals[16]);
		extractNormalTimes=DBFieldUtil.fetchImpodInt(vals[17]);
		extractAdvanceTimes=DBFieldUtil.fetchImpodInt(vals[18]);
		gem1Id=DBFieldUtil.fetchImpodInt(vals[19]);
		gem2Id=DBFieldUtil.fetchImpodInt(vals[20]);
		gem3Id=DBFieldUtil.fetchImpodInt(vals[21]);
		gem4Id=DBFieldUtil.fetchImpodInt(vals[22]);
		powerLv=DBFieldUtil.fetchImpodInt(vals[23]);
		powerExp=DBFieldUtil.fetchImpodInt(vals[24]);
		wasbind1=DBFieldUtil.fetchImpodInt(vals[25]);
		wasbind2=DBFieldUtil.fetchImpodInt(vals[26]);
		wasbind3=DBFieldUtil.fetchImpodInt(vals[27]);
		wasbind4=DBFieldUtil.fetchImpodInt(vals[28]);
		initializeArray();
	}

	public void makeSoulSlotVal(int slotIndex, int status) {
		if(slotIndex==1){
			extract1Status=status;
			extractStatusArray[0]=status;
		}
		if(slotIndex==2){
			extract2Status=status;
			extractStatusArray[1]=status;
		}
		if(slotIndex==3){
			extract3Status=status;
			extractStatusArray[2]=status;
		}
	}
	
	/**
	 * 洗练
	 * @param normalORAdvance 普通或高级
	 * @param extractIndex 洗练槽位
	 */
	public void makeSlotExtract(Integer normalORAdvance, Integer extractIndex,ExtractUpgrade extractUpgrade, Integer must,RolePo rolePo){

		List<List<Integer>> listExtractExp  = new ArrayList<List<Integer>>();
		if(normalORAdvance.intValue() == 1)
		{
			listExtractExp  = ExpressUtil.buildBattleExpressList(extractUpgrade.extractNormalExp);
		}
		else if(normalORAdvance.intValue() == 2)
		{
			listExtractExp  = ExpressUtil.buildBattleExpressList(extractUpgrade.extractAdvanceExp);
		}
		Map<Integer, List<Integer>> mapExtractExp = new HashMap<Integer, List<Integer>>();
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> weiths = new ArrayList<Integer>();
		for(List<Integer> extractExp : listExtractExp){
			ids.add(extractExp.get(0));
			weiths.add(extractExp.get(4));
			mapExtractExp.put(extractExp.get(0), extractExp);
		}
	 	int extractType = RandomUtil.calcWeight(ids,weiths);
	 	List<Integer> listExtract = mapExtractExp.get(extractType);
	 	List<Integer> total = new ArrayList<Integer>();
		// 属性最高值
		String totalAtbs =  XmlCache.xmlFiles.constantFile.soulSlot.extract.totalAtb;
		List<List<Integer>> totalAtbList = ExpressUtil.buildBattleExpressList(totalAtbs);
		for(List<Integer> totalAtb : totalAtbList){
			if(totalAtb.get(0).intValue() == listExtract.get(0).intValue())
			{
				total = totalAtb;
				break;
			}
		}
	 	extractByIndex(listExtract, total, extractIndex, normalORAdvance, extractUpgrade, must,rolePo);

	}
	
	/**
	 * 根据洗练槽来赋值
	 * @param listExtract
	 * @param total
	 * @param extractIndex
	 * @param normalORAdvance
	 * @param extractUpgrade
	 */
	public void extractByIndex(List<Integer> listExtract,List<Integer> total, Integer extractIndex,Integer normalORAdvance,ExtractUpgrade extractUpgrade,Integer must,RolePo rolePo){
		int start = listExtract.get(1);
	 	int startLuck = listExtract.get(2);
	 	int end = listExtract.get(3);
		
	 	int index = extractIndex.intValue()-1;
	 	extractBatTypeArray[index] = listExtract.get(0);
	 	if(rolePo.getCareer().intValue() == RoleType.CAREER_MAGE){
			if(listExtract.get(0).intValue() == 1){
				extractBatTypeArray[index]=2;
			}
			if(listExtract.get(0).intValue() == 6){
				extractBatTypeArray[index]=10;
			}
		}
 		//普通
	 	if(normalORAdvance.intValue() == 1)
	 	{
	 		if(extractNormalTimes.intValue() == extractUpgrade.extractNormalNums.intValue() && must.intValue() ==1)
	 		{
	 			extractBatValArray[index] = getWashRandomInt(startLuck, end);
	 		}
	 		else
	 		{
	 			extractBatValArray[index] = getWashRandomInt(start, end);
	 		}
	 	}
	 	//高级
	 	else if(normalORAdvance.intValue() == 2)
	 	{
	 		if(extractAdvanceTimes.intValue() == extractUpgrade.extractAdvanceNums.intValue() && must.intValue() ==1)
	 		{
	 			extractBatValArray[index] = getWashRandomInt(startLuck, end);
	 		}
	 		else
	 		{
	 			extractBatValArray[index] =  getWashRandomInt(start, end);
	 		}
	 	}
	 	int star=1;
	 	int[] starPar1={3,6,9,12,15,18,21,24,27,30,33,36,39,42,45};
	 	int[] starPar2={100,200,300,400,500,600,800,1000,1500,2000,2500,3000,4000,5000,6000};
	 	int[] starPar;
	 	if(total.get(0)==19||total.get(0)==20||total.get(0)==25||total.get(0)==26){
	 		starPar=starPar1;
	 	}else{
	 		starPar=starPar2;
	 	}
	 	for(int i=0;i<starPar1.length;i++){
		 		if(extractBatValArray[index].intValue()<=1d*starPar[i]/starPar[14]*total.get(1).intValue()){
		 			star=i+1;
		 			break;
		 		}
	 	}
	 	extractStarArray[index]=star;
	 	if(star < 3)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_WHITE;
	 	}
	 	else if(star < 5)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_GREEN;
	 	}
	 	else if(star < 8)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_BLUE;
	 	}
	 	else if(star < 11)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_PURPLE;
//			StringBuffer sb = new StringBuffer();
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//			sb.append(ColourType.COLOUR_YELLOW).append("【").append(rolePo.getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key124"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key307"));
//			sb.append(ColourType.COLOUR_PURPLE).append(GlobalCache.fetchLanguageMap("key308"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
			String str= MessageFormat. format(GlobalCache.fetchLanguageMap("key2616"),rolePo.getName());
			ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
			chatService.sendHorse(str);
			chatService.sendSystemWorldChat(str);
	 		
	 	}
	 	else if(star < 14)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_ORANGE;
//			StringBuffer sb = new StringBuffer();
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//			sb.append(ColourType.COLOUR_YELLOW).append("【").append(rolePo.getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key124"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key307"));
//			sb.append(ColourType.COLOUR_ORANGE).append(GlobalCache.fetchLanguageMap("key309"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
			String str= MessageFormat. format(GlobalCache.fetchLanguageMap("key2617"),rolePo.getName());
			ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
			chatService.sendHorse(str.toString());
			chatService.sendSystemWorldChat(str);
	 	}
	 	else if(star < 16)
	 	{
	 		extractQualityArray[index] = SlotSoulType.EXTRACT_QUALITY_GOLD;
//			StringBuffer sb = new StringBuffer();
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48"));
//			sb.append(ColourType.COLOUR_YELLOW).append("【").append(rolePo.getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key124"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key307"));
//			sb.append(ColourType.COLOUR_GOLDEN).append(GlobalCache.fetchLanguageMap("key310"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key50"));
			String str= MessageFormat. format(GlobalCache.fetchLanguageMap("key2618"),rolePo.getName());
			ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
			chatService.sendHorse(str);
			chatService.sendSystemWorldChat(str);
	 	}
	}
	
	/**
	 * 根据洗练锁定状态，获取洗练消耗道具或钻石的数量
	 * @return
	 */
	public Integer fetchNumByExtractStatus(){
		int num = 1;
		int index = 0;
		for(int i=0; i < extractStatusArray.length; i++){
			if(extractStatusArray[i].intValue() == SlotSoulType.EXTRACT_STATUS_LOCK)
			{
				index++;
			}
		}
		if(index==1)
		{
			num=2;
		}
		else if(index == 2)
		{
			num=4;
		}
		return num;
	}

	/**
	 * 升级部位品质
	 */
	public void upGreadSlotQuality(RolePo rolePo){
		int index1 = 0;
		int index2 = 0;
		for(int i=0; i < extractQualityArray.length; i++)
		{
			if(slotQuality.intValue() == SlotSoulType.EXTRACT_QUALITY_PURPLE)
			{
				if(extractQualityArray[i].intValue() >= SlotSoulType.EXTRACT_QUALITY_PURPLE){
					index1++;
				}
			}
			if(slotQuality.intValue() == SlotSoulType.EXTRACT_QUALITY_ORANGE)
			{
				if(extractQualityArray[i].intValue() >= SlotSoulType.EXTRACT_QUALITY_ORANGE){
					index2++;
				}
			
			}
		}
		if(index1 == 3){
			slotQuality = SlotSoulType.EXTRACT_QUALITY_ORANGE;
			extractNormalTimes=0;
			extractAdvanceTimes=0;
			ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
//			StringBuffer sb = new StringBuffer();
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key20"));
//			sb.append(ColourType.COLOUR_YELLOW).append("【").append(rolePo.getName()).append("】").append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key165"));
//			sb.append(ColourType.COLOUR_GREEN).append(ItemType.fecthNameBySlot(slotNum));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key311"));
//			sb.append(ColourType.COLOUR_GOLDEN).append(GlobalCache.fetchLanguageMap("key312"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key314"));
			String str= MessageFormat. format(GlobalCache.fetchLanguageMap("key2619"),rolePo.getName(),ItemType.fecthNameBySlot(slotNum));
			
			chatService.sendHorse(str);
		}	
		if(index2 == 3){
			slotQuality = SlotSoulType.EXTRACT_QUALITY_GOLD;
			extractNormalTimes=0;
			extractAdvanceTimes=0;
			ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
//			StringBuffer sb = new StringBuffer();
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key20"));
//			sb.append(ColourType.COLOUR_BLUE).append(rolePo.getName()).append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key165"));
//			sb.append(ColourType.COLOUR_GREEN).append(ItemType.fecthNameBySlot(slotNum));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key311"));
//			sb.append(ColourType.COLOUR_GOLDEN).append(GlobalCache.fetchLanguageMap("key313"));
//			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key314"));
			String str= MessageFormat. format(GlobalCache.fetchLanguageMap("key2620"),rolePo.getName(),ItemType.fecthNameBySlot(slotNum));
			chatService.sendHorse(str);
		}	
	}
	
	public void extractAssignment(){
		if(extractQualityArray[0] != 0){
			extract1Quality=extractQualityArray[0];			
		}
		if(extractStarArray[0] != 0){
			extract1Star=extractStarArray[0];
		}
		if(extractStatusArray[0] != 0){
			extract1Status=extractStatusArray[0];			
		}
		if(extractBatTypeArray[0] != 0){
			extract1BatType=extractBatTypeArray[0];			
		}
		if(extractBatValArray[0]!= 0){
			extract1BatVal=extractBatValArray[0];			
		}
		if(extractQualityArray[1] != 0){
			extract2Quality=extractQualityArray[1];			
		}
		if(extractStarArray[1] != 0){
			extract2Star=extractStarArray[1];			
		}
		if(extractStatusArray[1] != 0){
			extract2Status=extractStatusArray[1];			
		}
		if(extractBatTypeArray[1] != 0){
			extract2BatType=extractBatTypeArray[1];			
		}
		if(extractBatValArray[1] != 0){
			extract2BatVal=extractBatValArray[1];			
		}
		if(extractQualityArray[2] != 0){
			extract3Quality=extractQualityArray[2];			
		}
		if(extractStarArray[2] != 0){
			extract3Star=extractStarArray[2];			
		}
		if(extractStatusArray[2] != 0){
			extract3Status=extractStatusArray[2];			
		}
		if(extractBatTypeArray[2] != 0){
			extract3BatType=extractBatTypeArray[2];			
		}
		if(extractBatValArray[2] != 0){
			extract3BatVal=extractBatValArray[2];			
		}
	}
	
	public void makeSoulSlotGemVal(Integer equipSlot, Integer gemIndex, int gem, int wasbind) {
//		System.out.println(" makeSoulSlotGemVal() equipSlot = " +equipSlot +"; gemIndex = " +gemIndex +"; gem = " +gem +"; wasbind = " +wasbind);
		
		if(gemIndex==1){
			gem1Id=gem;
			wasbind1=wasbind;
		}
		if(gemIndex==2){
			gem2Id=gem;
			wasbind2=wasbind;
		}
		if(gemIndex==3){
			gem3Id=gem;
			wasbind3=wasbind;
		}
		if(gemIndex==4){
			gem4Id=gem;
			wasbind4=wasbind;
		}
	}
	
	/**
	 * 检查宝石槽位
	 */
	public void checkGemSlot(int gemIndex){
		if(gemIndex==1 && gem1Id!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2693"));
		}
		if(gemIndex==2 && gem2Id!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2693"));
		}
		if(gemIndex==3 && gem3Id!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2693"));
		}
		if(gemIndex==4 && gem4Id!=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2693"));
		}
	}
	
	/**
	 * 检查宝石属性是否相同
	 * @param itemId
	 */
	public void checkGemCategory(int itemId){
		List<Integer> categoryList = new ArrayList<Integer>(); 
		ItemPo itemPo1 = ItemPo.findEntity(gem1Id);
		if(itemPo1 != null){
			categoryList.add(itemPo1.getCategory());
		}
		ItemPo itemPo2 = ItemPo.findEntity(gem2Id);
		if(itemPo2 != null){
			categoryList.add(itemPo2.getCategory());
		}
		ItemPo itemPo3 = ItemPo.findEntity(gem3Id);
		if(itemPo3 != null){
			categoryList.add(itemPo3.getCategory());
		}
		ItemPo itemPo4 = ItemPo.findEntity(gem4Id);
		if(itemPo4 != null){
			categoryList.add(itemPo4.getCategory());
		}
		ItemPo itemPo = ItemPo.findEntity(itemId);
		for(int category : categoryList){
			if(category == itemPo.getCategory()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2694"));
			}
		}
	}
	
	public void checkHasGemIndex(Integer gemIndex)
	{
		if(gemIndex==1)
		{
			if(gem1Id == 0)
			{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key315")+"1"+GlobalCache.fetchLanguageMap("key316")+gemIndex);
			}
		}
		if(gemIndex==2)
		{
			if(gem2Id==0)
			{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key315")+"2"+GlobalCache.fetchLanguageMap("key316")+gemIndex);
			}
		}
		if(gemIndex==3)
		{
			if(gem3Id==0)
			{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key315")+"3"+GlobalCache.fetchLanguageMap("key316")+gemIndex);
			}
		}
		if(gemIndex==4)
		{
			if(gem4Id==0)
			{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key315")+"4"+GlobalCache.fetchLanguageMap("key316")+gemIndex);
			}
		}
	}
	
	public Integer fetchItemIdByGemIndex(Integer gemIndex)
	{
		int itemId = 0;
		if(gemIndex==1){
			itemId = gem1Id;
		}
		if(gemIndex==2){
			itemId = gem2Id;
		}
		if(gemIndex==3){
			itemId = gem3Id;
		}
		if(gemIndex==4){
			itemId = gem4Id;
		}
		if(itemId == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key317")+gemIndex);
		}
		return itemId;
	}
	public Integer fetchItemIdwasbindByGemIndex(Integer gemIndex)
	{
		int wasbind = 0;
		if(gemIndex==1){
			wasbind = wasbind1;
		}
		if(gemIndex==2){
			wasbind = wasbind2;
		}
		if(gemIndex==3){
			wasbind = wasbind3;
		}
		if(gemIndex==4){
			wasbind = wasbind4;
		}

		return wasbind;
	}
	
	
	
	public int getWashRandomInt(int bottom,int top){
		int randNum=IntUtil.getRandomInt(1, 10000);
		int val=0;
		if(randNum<4138){
			val=IntUtil.getRandomInt(bottom, (int)(bottom+(top-bottom)*0.1));
		}else if(randNum<6572){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.1), (int)(bottom+(top-bottom)*0.2));
		}else if(randNum<8004){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.2), (int)(bottom+(top-bottom)*0.3));
		}else if(randNum<8846){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.3), (int)(bottom+(top-bottom)*0.4));
		}else if(randNum<9342){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.4), (int)(bottom+(top-bottom)*0.5));
		}else if(randNum<9633){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.5), (int)(bottom+(top-bottom)*0.6));
		}else if(randNum<9804){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.6), (int)(bottom+(top-bottom)*0.7));
		}else if(randNum<9905){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.7), (int)(bottom+(top-bottom)*0.8));
		}else if(randNum<9965){
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.8), (int)(bottom+(top-bottom)*0.9));
		}else{
			val=IntUtil.getRandomInt((int)(bottom+(top-bottom)*0.9), (int)(bottom+(top-bottom)*1));
		}
		return val;
	}

	@Override
	public String toString() {
		return "SlotSoulVo [slotNum=" + slotNum + ", slotQuality="
				+ slotQuality + ", extract1Quality=" + extract1Quality
				+ ", extract1Star=" + extract1Star + ", extract1Status="
				+ extract1Status + ", extract1BatType=" + extract1BatType
				+ ", extract1BatVal=" + extract1BatVal + ", extract2Quality="
				+ extract2Quality + ", extract2Star=" + extract2Star
				+ ", extract2Status=" + extract2Status + ", extract2BatType="
				+ extract2BatType + ", extract2BatVal=" + extract2BatVal
				+ ", extract3Quality=" + extract3Quality + ", extract3Star="
				+ extract3Star + ", extract3Status=" + extract3Status
				+ ", extract3BatType=" + extract3BatType + ", extract3BatVal="
				+ extract3BatVal + ", extractNormalTimes=" + extractNormalTimes
				+ ", extractAdvanceTimes=" + extractAdvanceTimes + ", gem1Id="
				+ gem1Id + ", gem2Id=" + gem2Id + ", gem3Id=" + gem3Id
				+ ", gem4Id=" + gem4Id + ", powerLv=" + powerLv + ", powerExp="
				+ powerExp + "]";
	}

	
	
}
