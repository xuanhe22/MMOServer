package com.games.mmo.vo;

import javax.xml.bind.annotation.XmlAttribute;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class EquipAttachVo extends BasePropertyVo {

	/**
	 * 属性id
	 */
	@XmlAttribute
	public int attachId;
	/**
	 * 最小值
	 */
	@XmlAttribute
	public int minAtt;
	/**
	 * 最大值
	 */
	@XmlAttribute
	public int maxAtt;
	/**
	 * 权重
	 */
	@XmlAttribute
	public int probability;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		attachId=DBFieldUtil.fetchImpodInt(vals[0]);
		minAtt=DBFieldUtil.fetchImpodInt(vals[1]);
		maxAtt=DBFieldUtil.fetchImpodInt(vals[2]);
		probability=DBFieldUtil.fetchImpodInt(vals[3]);
	}

}
