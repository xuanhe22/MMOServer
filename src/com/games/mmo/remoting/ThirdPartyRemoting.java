package com.games.mmo.remoting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;
import com.games.mmo.util.HttpURLUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.KoreaLoginVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.PrintUtil;

/**
 * 第三方平台 验证
 * @author Administrator
 *
 */
@Controller
public class ThirdPartyRemoting extends BaseRemoting {
	
	/**
	 * 韩国登录验证
	 * @param userId Sdk客户端返回给cp的userId
	 * @param token Sdk客户端返回给cp的token
	 * @param cpId 合作伙伴id(来源： 签约后由平台发送给合作伙伴)
	 * @param appId 游戏id(来源：同cpId)
	 * @param channelId Sdk客户端返回给cp的channelId
	 * @param channelAction Sdk客户端返回的channelAction,用于组拼检测登录的URL
	 * @param extInfo 检测登录扩展字段，用于适配渠道
	 * @return
	 * @throws Exception 
	 */
	public Object koreaLoginCheck(String userId,String token,String cpId, String appId, String channelId, String channelAction, String extInfo) throws Exception{
		PrintUtil.print("ThirdPartyRemoting.koreaLoginCheck() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+"; userId="+userId+"; token="+token+"; cpId="+cpId+"; appId="+appId+"; channelId="+channelId+"; channelAction="+channelAction+"; extInfo="+extInfo);
		// 国内测试环境
//		String url = "http://testweb.taiqigame.com/upay_sdk/channel/"+channelAction+"/checkLogin";
		
		// 国内正式环境
//		String url="http://usdk.taiqigame.com/channel/"+channelAction+"/checkLogin";
		
		String url="http://usdk.uqsoft.com/channel/" + channelAction + "/checkLogin"; 
		// 返回值 {"userId":"100015433410","respCode":"200"}
		
		KoreaLoginVo koreaLoginVo = new KoreaLoginVo(userId, token, cpId, appId, channelId, channelAction, extInfo);
		PrintUtil.print("koreaLoginVo="+koreaLoginVo.toString());
		
		String result = "";
		HttpURLUtil httpURLUtil = new HttpURLUtil();
		result =httpURLUtil.httpPost(url, koreaLoginVo.toString());
		
		PrintUtil.print("result="+result);
		
//		JSONObject returnJsonObj = new JSONObject();
//		returnJsonObj= returnJsonObj.parseObject(result);
//		String returnUserId = returnJsonObj.get("userId").toString();
//		String respCode = returnJsonObj.get("respCode").toString();
//		PrintUtil.print("returnUserId="+returnUserId+"; respCode="+respCode);
		SessionUtil.addDataArray(result);
		return SessionType.MULTYPE_RETURN;
	}
}
