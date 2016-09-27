package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class SustainKillVo extends BasePropertyVo {

	/**
	 * 连续击杀数
	 */
	public int killNum;
	
	/**
	 * 击杀记录（最近45人roleId）
	 */
	@JSONField(serialize=false)
	public List<Integer> roleIds;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{killNum, roleIds};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		killNum=DBFieldUtil.fetchImpodInt(vals[0]);
		roleIds = new ArrayList<Integer>();
		if(!StringUtil.isEmpty(vals[1]))
		{
			String[] strs = StringUtil.split(vals[1], ",");
			for(String str:strs)
			{
				roleIds.add(Integer.parseInt(str));
			}
		}
	}
	
	/**
	 * 增加连斩记录
	 * @param roleId
	 */
	public void add(int roleId)
	{
		killNum++;
		if(roleIds.size() >= 45)
		{
			roleIds.remove(0);
		}
		roleIds.add(roleId);
	}
	
	/**
	 * 清空连斩记录
	 */
	public void clear()
	{
		killNum = 0;
		roleIds.clear();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(killNum+"|");
		for(int i=0,max=roleIds.size();i<max;i++)
		{
			if(i!=0)
				sb.append(",");
			Integer roleId = roleIds.get(i);
			sb.append(roleId);
		}
		return sb.toString();
	}

}
