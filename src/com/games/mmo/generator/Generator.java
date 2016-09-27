package com.games.mmo.generator;

import org.springframework.stereotype.Controller;

import com.games.mmo.type.SystemType;
import com.storm.lib.component.entity.BaseGenerator;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
@Controller
public class Generator {

	/**
	 * 方法功能:
	 * 更新时间:2011-12-12, 作者:john
	 * @param args
	 */
	public static void main(String[] args) {
		SystemType.initStormLib(args, "com"+BaseStormSystemType.fs+"games"+BaseStormSystemType.fs+"snake"+BaseStormSystemType.fs, true, false, null, true, SystemType.class);
		BaseGenerator baseGenerator = (BaseGenerator) BeanUtil.getBean("baseGenerator");
//		baseGenerator.generatorDB(new String[]{"Tag-structure-套装(Game)"}, true,FileUtil.getGameExcelFile());
//		baseGenerator.generatorPo(sheetsNames);
//		baseGenerator.generatorPoAndDB(new String[]{"Tag-structure-商城(静态)"}, true,FileUtil.getExcelByFile(new File("C:\\Users\\Administrator\\Desktop\\其他\\P4\\Design\\配置表\\商店配置表.xls")));
//		baseGenerator.generatorAsPo(new String[]{"Tag-structure-道具(Game)"});
	}

}
