package com.games.mmo.util;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.po.game.ItemPo;
import com.storm.lib.util.CheckUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo3;

public class BackendUtil {

	public static List<Integer> fetchRoleIds(java.lang.String mailNames,Integer minLv,Integer minVipLv,Integer maxLv,Integer maxVipLv) {
		mailNames=StringUtil.replaceChineseChararsAndTrim(mailNames);
		List<Integer> list = new ArrayList<Integer>();
		String[] vals = mailNames.split(",");
		RoleDAO roleDAO = RoleDAO.instance();
		for (String item : vals) {
			item=item.trim();
			Integer id=roleDAO.getRoleIdByName(item,minLv,minVipLv,maxLv,maxVipLv);
			if(id==null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key303")+item);
			}
			list.add(id);
		}
		return list;
	}

	public static List<IdNumberVo3> fetchAttachItemList(java.lang.String mailAttach) {
		mailAttach=StringUtil.replaceChineseChararsAndTrim(mailAttach);
		List<IdNumberVo3> items = new ArrayList<IdNumberVo3>();
		if(mailAttach.equals("")){
			return  items;
		}
		String[] vals = mailAttach.split(",");
		for (String str : vals) {
			str=str.trim();
			String[] values = str.split("\\*");
			values[0]=values[0].trim();
			values[1]=values[1].trim();
			ItemPo itemPo = ItemPo.findEntityByName(values[0]);
			int val = Integer.valueOf(values[1]);
			CheckUtil.checkInValueRangess(val, 1, 999999999);
			int bind=1;
			if(values.length>=3){
				if(values[2].equals("ub")){
					bind=0;
				}
				else{
					bind=1;
				}
			}
			items.add(new IdNumberVo3(1,itemPo.getId(),val,bind));
		}
		return items;
	}

	public static String convertToActivityAwards(String awardItems) {
		awardItems=StringUtil.replaceChineseChararsAndTrim(awardItems);
		if(StringUtil.isEmpty(awardItems)){
			return null;
		}
		String[] items = StringUtil.split(awardItems, ";");
		List<String> vals =new ArrayList<String>();
		for (String item : items) {
			String[] subItems = StringUtil.split(item, ",");
			List<String> vals2 =new ArrayList<String>();
			for (String sub : subItems) {
				sub=sub.trim();
				String[] values = sub.split("\\*");
				values[0]=values[0].trim();
				values[1]=values[1].trim();
				ItemPo itemPo = ItemPo.findEntityByName(values[0]);
				int val = Integer.valueOf(values[1]);
				CheckUtil.checkInValueRangess(val, 1, 999999999);
				int bind=1;
				if(values.length>=3){
					if(values[2].equals("ub")){
						bind=0;
					}
					else{
						bind=1;
					}
				}
				sub=itemPo.getId()+"|"+values[1]+"|"+bind;
				vals2.add(sub);
			}
			String val = StringUtil.implode(vals2, ",");
			vals.add(val);
		}
		return StringUtil.implode(vals, ";");
	}

}
