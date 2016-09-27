package com.games.mmo.vo;

import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetAttachs.PetAttach;
import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

public class PetConstellVo extends BaseVo {
	/**
	 * 属性类型
	 */
	public int attachType;
	/**
	 * 属性等级
	 */
	public int attachLevel;
	/**
	 * 属性图标
	 */
	public String icon;
	/**
	 * 属性名称
	 */
	public String name;
	/**
	 * 属性值
	 */
	public List<IdNumberVo> attachs;
	
	public void loadProperty(String val, String spliter) {
		String[] strs = StringUtil.split(val, spliter);
		this.attachType = DBFieldUtil.fetchImpodInt(strs[0]);
		this.attachLevel = DBFieldUtil.fetchImpodInt(strs[1]);
		PetAttach petAttach = GlobalCache.petAttach.get(this.attachLevel).get(this.attachType);
		this.icon = petAttach.icon;
		this.name = petAttach.name;
		updateAttachs(petAttach);
	}
	
	public void updateAttachs(PetAttach petAttach)
	{
		attachs = IdNumberVo.createList(petAttach.attachValue);
	}
}


