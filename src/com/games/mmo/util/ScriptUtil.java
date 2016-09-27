package com.games.mmo.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.games.mmo.mapserver.bean.Fighter;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

public class ScriptUtil {
	public static ScriptEngineManager manager = new ScriptEngineManager(); 
//	public static ScriptEngine engine;
	static{
//		engine = manager.getEngineByName("js");
//		ScriptEngineFactory sef=engine.getFactory();
//		PrintUtil.print( sef.getParameter(ScriptEngine.LANGUAGE));  
//		PrintUtil.print( sef.getParameter("THREADING"));  
	}

	
	public static Double runScript(Fighter self,Fighter target,String val){
		if(val==null){
			return null;
		}
		ScriptEngine engine = manager.getEngineByName("js");
//		PrintUtil.print("执行表达式:"+val);
		Double result=null;
		engine.put("target", target);
		engine.put("self", self);

		Object obj=null;
		try {
			obj = engine.eval(val);
		} catch (Exception e) {
			PrintUtil.print("脚本错误:"+val);
			ExceptionUtil.processException(e);
		}
		 if(obj==null){
			 return null;
		 }
		 if(obj instanceof Integer){
			 return ((Integer)obj).doubleValue();
		 }
		 else{
			 return (Double)obj;
		 }
	}
}
