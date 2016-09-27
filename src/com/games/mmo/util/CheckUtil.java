package com.games.mmo.util;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.games.mmo.cache.GlobalCache;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

public class CheckUtil {

	private static String[] filterStr = {"`","~","!","@","#","%","^","&","*","(",")","=","+","|","[","]","{","}","'",";","/","\\",",",":","?","<",">"};
	private static String[] filterStrs = {"\r","\n","\t","\b","\"","\'","\f","\t","\b","\n\r","\r\n"};
	public static void checkInValues(Integer value,int i, int j) {
		if(value==null){
			ExceptionUtil.throwConfirmParamException("你传的坑爹的参数null");
		}
		else{
			if(value!=i && value!=j){
				ExceptionUtil.throwConfirmParamException("你传的坑爹的参数不对"+value);
			}
		}
	}
	
	public static void checkInValues(Integer value,int i, int j,int k) {
		if(value==null){
			ExceptionUtil.throwConfirmParamException("");
		}
		else{
			if(value!=i && value!=j && value!=k){
				ExceptionUtil.throwConfirmParamException("你传的坑爹的参数不对:");
			}
		}
	}	
	
	public static void checkInValues(Integer value,int i, int j,int k,int l) {
		if(value==null){
			ExceptionUtil.throwConfirmParamException("你传的坑爹的参数null");
		}
		else{
			if(value!=i && value!=j && value!=k && value!=l){
				ExceptionUtil.throwConfirmParamException("你传的坑爹的参数不对");
			}
		}
	}
	
	public static void checkInValueRangess(Integer value,int start,int end){
		if(value==null){
			ExceptionUtil.throwConfirmParamException("空啊");
		}
		else{
			if(value<start || value>end){
				ExceptionUtil.throwConfirmParamException("!!!!!!!!!你传的坑爹的参数不对"+value+"必须在"+start+"~"+end+"之间");
			}
		}
	}
	
	public static void checkInValueRangess(Double value,double start,double end){
		if(value==null){
			ExceptionUtil.throwConfirmParamException("空啊");
		}
		else{
			if(value<start || value>end){
				ExceptionUtil.throwConfirmParamException("你传的坑爹的参数不对"+value+"必须在"+start+"~"+end+"之间");
			}
		}
	}
	
	public static void checkIsNull(Object obj){
		if(obj==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2309"));
		}		
	}
	
	
	/**
	 * 
	 * 方法功能:必须是正数
	 * 更新时间:2011-11-17, 作者:johnny
	 * @param num
	 */
	public static void checkPositive(long num){
		if(num<=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2310"));
		}
	}
	
	/**
	 * 
	 * 方法功能:必须是正数
	 * 更新时间:2011-11-17, 作者:johnny
	 * @param num
	 */
	public static void checkPositive(int num){
		if(num<=0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2310"));
		}
	}
	
	/**
	 * 
	 * 方法功能:
	 * 更新时间:2011-11-17, 作者:johnny
	 * @param num
	 */
	public static void checkNotBlewZero(Integer num){
		if(num==null){
			return;
		}
		if(num<0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2311")+num);
		}
	}
	
	/**
	 * 
	 * 方法功能:
	 * 更新时间:2011-11-17, 作者:johnny
	 * @param num
	 */
	public static void checkNotBlewZero(Long num){
		if(num==null){
			return;
		}
		if(num<0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2311")+num);
		}
	}
	
	public static void checkValidString(String name,int length){
		if(StringUtil.isEmpty(name)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2312"));
		}
		if(name.length()>length){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2313")+length);
		}
	}
	
	
	public static void checkTimesWithPostive(int num,int timesValue){
		checkPositive(num);
		checkIsTimes(num,timesValue);
	}

	public static void checkIsTimes(int num, int timesValue) {
		if(num%timesValue!=0){
			ExceptionUtil.throwConfirmParamException(num+"必须是"+timesValue+"的倍数");
		}
	}
	
	/**
	 * 
	 * 方法功能:检查某事今天干过了
	 * 更新时间:2011-12-8, 作者:johnny
	 * @param timestamp
	 */
	public static void checkTodayHasDone(Timestamp timestamp){
		if(DateUtil.isSameDay(DateUtil.getCurrentTimeStamp(), timestamp)){
			ExceptionUtil.throwConfirmParamException("明天再来吧");
		}
	}
	
	public static void checkContianFiltedWord2(String str,boolean requestSystemCheck){
 		if(GameUtil.containsFilterWord(str)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
		}
		if(requestSystemCheck){
			for(int i=0;i<filterStr.length;i++){
				if(str.contains(filterStr[i])){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
				}
			}		
			for(int i=0;i<filterStrs.length;i++){
				if(str.contains(filterStrs[i])){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
				}
			}
			//首写不能为数字
		}
	}
	
	
	
	/**
	 * 
	 * 方法功能:判断是否有过滤词
	 * 更新时间:2011-12-23, 作者:johnny
	 */
	public static void checkContianFiltedWord(String str,boolean requestSystemCheck,String[] additionStrs){
 		if(GameUtil.containsFilterWord(str)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
		}
 		if(additionStrs!=null){
 			for(int i=0;i<additionStrs.length;i++){
 				if(str.contains(additionStrs[i])){
 					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
 				}
 			}	
 		}

		if(requestSystemCheck){
			for(int i=0;i<filterStr.length;i++){
				if(str.contains(filterStr[i])){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
				}
			}		
			for(int i=0;i<filterStrs.length;i++){
				if(str.contains(filterStrs[i])){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2314"));
				}
			}
			//首写不能为数字
			
			if(StringUtil.isNumeric(str.substring(0,1))){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2315")+str);
			}
		}
	}
	
	//上线后不抛出异常
	public static Integer propertiesBelowZero(Integer val){
		try {
			checkNotBlewZero(val);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
			return 0;
		}
		return val;
	}
	
	// 上线后不抛出异常
	public static Long propertiesBelowZero(Long val){
		try {
			checkNotBlewZero(val);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
			return 0l;
		}
		return val;
	}

	/**
	 * 验证用户名称
	 * @param name
	 * @return
	 */
	public static boolean checkRolePoNameWasIllegal(String name, boolean wasBlankSpace){
		//随机名连字符（“-”在32-128之间）
        //“﹡” 65121
        //“の” 12398
        //“·” 183
        String testStr = name;
        char[] charArray = testStr.toCharArray();
        boolean isIllegal = false;
        for (int i = 0; i < charArray.length; ++i)
        {
            int asciiValue = (int)charArray[i];
            if (183 == asciiValue || 
            	12398 == asciiValue || 
                65121 == asciiValue ||
                (asciiValue > (wasBlankSpace?31:32) && asciiValue < 128) ||
                (asciiValue >= 19968 && asciiValue <= 40891)) 
            {
                isIllegal = false;
            }
            else
            {
                isIllegal = true;
               break;
            }
        }
        return isIllegal;
	}

	
	public static String filterEmoji(String source) {  
        if(source != null)
        {
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()) 
            {
                source = emojiMatcher.replaceAll("");
                return source ; 
            }
        return source;
       }
       return source;  
    }
	
	public static String checkIllegelName(String name) {
		name = name.trim();
		name=filterEmoji(name);
		if(name.contains("\\")){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2312"));
		}
		if(name.contains("'")){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2312"));
		}
		if(name.contains("|")){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2313"));
		}
		if(name.contains(",")){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2313"));
		}
		return name;
	}

	public static void main(String[] args) {
		checkIllegelName("\\xF0\\x9F\\x98\\x9A");
	}
 
 
}
