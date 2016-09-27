package com.games.mmo.cache;

import java.io.File;

import org.dom4j.Document;

import com.games.mmo.po.GlobalPo;
import com.games.mmo.type.ItemType;
import com.games.mmo.vo.xml.ConstantFile;
import com.games.mmo.vo.xml.LanguageFile;
import com.games.mmo.vo.xml.PlayerFile;
import com.games.mmo.vo.xml.XmlFiles;
import com.storm.lib.util.FileUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.XMLUtil;

public class XmlCache {
	public static XmlFiles xmlFiles=new XmlFiles();
	
	public static void loadAndClean(String languageType){
		String constant = "constant_"+languageType+".xml";
		String language = "language_"+languageType+".xml";
		String player = "player_iuid.xml";
		File file = FileUtil.getUnderDataFile(constant);
		Document doc = XMLUtil.getDom4JDocument(file);
		xmlFiles.constantFile=XMLUtil.createObject(doc.asXML(), ConstantFile.class);
		//TODO 【业务标记】多调一次
		GlobalCache.reloadServerAndClientLang();
		// 语言加载
		GlobalCache.loadLanguageMap(languageType);
//		File file2 = FileUtil.getUnderDataFile(language);
//		Document doc2 = XMLUtil.getDom4JDocument(file2);
//		xmlFiles.languageFile=XMLUtil.createObject(doc2.asXML(), LanguageFile.class);
		File file3 = FileUtil.getUnderDataFile(player);
		Document doc3 = XMLUtil.getDom4JDocument(file3);
		xmlFiles.playerFile=XMLUtil.createObject(doc3.asXML(), PlayerFile.class);
		PrintUtil.print("xml.playerFile finish iuid = "+xmlFiles.playerFile.userInfos.userInfo.get(0).iuid);
		
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		if(gp == null){
			GlobalPo.init(0);
			gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		}
		gp.valueObj= languageType;
		PrintUtil.print("current language :"+String.valueOf(gp.valueObj));
		PrintUtil.print("xml加载完成："+xmlFiles.constantFile);
		
		ItemType.ITEM_WING_STAR_UPGRADE_COST_ITEM_ID=xmlFiles.constantFile.wing.starUpgradeCostItemId;
		ItemType.ITEM_WING_STEP_UPGRADE_COST_ITEM_ID=xmlFiles.constantFile.wing.stepUpgradeCostItemId;
	}
	
}
