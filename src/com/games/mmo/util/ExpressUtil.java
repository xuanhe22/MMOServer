package com.games.mmo.util;

import java.util.ArrayList;
import java.util.List;

import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class ExpressUtil {
	public static List<List<Integer>> buildBattleExpressList(String effectExpress) {
		List<List<Integer>> theList =new ArrayList<List<Integer>>();
		theList=new ArrayList<List<Integer>>();
		if(effectExpress==null){
			return theList;
		}
		String[] vals = StringUtil.split(effectExpress, ",");
		for (int i = 0; i < vals.length; i++) {
			List<Integer> list = DBFieldUtil.getIntegerListBySplitter(vals[i],"|");
			theList.add(list);
		}
		return theList;
	}
	
	public static List<List<Long>> buildBattleExpressListLong(String effectExpress) {
		List<List<Long>> theList =new ArrayList<List<Long>>();
		theList=new ArrayList<List<Long>>();
		if(effectExpress==null){
			return theList;
		}
		String[] vals = StringUtil.split(effectExpress, ",");
		for (int i = 0; i < vals.length; i++) {
			List<Long> list = DBFieldUtil.getLongListBySplitter(vals[i],"|");
			theList.add(list);
		}
		return theList;
	}
	
	public static String buildBattleExpressStr(List<List<Integer>> list) {
		String val=null;
		String[] vals = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			vals[i] = StringUtil.getStrByList(list.get(i), "|");
		}
		val = StringUtil.implode(vals, ",");
		return val;
	}
	
	public static String buildBattleExpressStrLong(List<List<Long>> list) {
		String val=null;
		String[] vals = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			vals[i] = StringUtil.getStrByListLong(list.get(i), "|");
		}
		val = StringUtil.implode(vals, ",");
		return val;
	}
	
	public static List<List<String>> buildBattleExpressListStr(String effectExpress) {
		List<List<String>> theList =new ArrayList<List<String>>();
		theList=new ArrayList<List<String>>();
		if(effectExpress==null){
			return theList;
		}
		String[] vals = StringUtil.split(effectExpress, ",");
		for (int i = 0; i < vals.length; i++) {
			List<String> list = DBFieldUtil.getStringListBySplitter(vals[i],"|");
			theList.add(list);
		}
		return theList;
	}
	
	public static String buildBattleExpressStrStr(List<List<String>> list) {
		String val=null;
		String[] vals = new String[list.size()];
		for (int i=0;i<list.size();i++) {
			vals[i] = StringUtil.getStrByListWithStr(list.get(i), "|");
		}
		val = StringUtil.implode(vals, ",");
		return val;
	}

	public static int findExpressEffectType(int type,List<List<List<Integer>>> effects) {
		int total=0;
		for (List<List<Integer>> list : effects) {
			for (List<Integer> list2 : list) {
				if(list2.get(0).intValue()==type){
					total+=list2.get(1);
				}
			}
		}
		return total;
	}
	
	public static int findExpressEffectType2(int type,List<List<Integer>> effects) {
		int total=0;

		for (List<Integer> list2 : effects) {
			if(list2.get(0).intValue()==type){
				total+=list2.get(1);
			}
		}
		
		return total;
	}
	
	
}
