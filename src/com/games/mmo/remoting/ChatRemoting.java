package com.games.mmo.remoting;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.TestVoice;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.RoleService;
import com.games.mmo.type.ChatType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.SessionUtil;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;
@Controller
public class ChatRemoting extends BaseRemoting{

	@Autowired
	private ChatService chatService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private RoleService roleService;
	/**
	 * 
	 * 方法功能:发送消息
	 * 更新时间:2014-11-18, 作者:johnny
	 * @param message	消息
	 * @param channel	频道
	 * @param par1	参数
	 * @return
	 */
	public Object sendChat(String message,Integer channel,Integer par1,String roleName){
//		CheckUtil.checkContianFiltedWord(message, false,ChatType.CHAT_FILTERS);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);

		RolePo targetRolePo =null;
		if(roleName.equals("0")){
			targetRolePo =RolePo.findEntity(par1);
		}
		else{
			targetRolePo=roleService.findRoleByName(roleName);
			if(targetRolePo==null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2696"));
			}
			else{
				par1=targetRolePo.getId();
			}
		}

//		System.out.println("sendChat():"+rolePo.getName()+"; message="+message+"; channel="+channel+"; par1="+par1);

		if(par1 == null || par1 == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key77"));
		}
		if(channel.intValue() == ChatType.CHAT_CHANNEL_PRIVATE){
			checkService.checkExisRolePo(par1);
			if(targetRolePo == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key78"));
			}
			if(!targetRolePo.fetchRoleOnlineStatus() || targetRolePo.fighter==null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
			}
			if(targetRolePo.fetchOptionsStatusByType(RoleType.OPTIONS_TYPE_REFUSAL_PRIVATE_CHAT).intValue() == 0){
				ExceptionUtil.throwConfirmParamException(targetRolePo.getName()+GlobalCache.fetchLanguageMap("key80"));
			}
		}
		chatService.checkBlockChat(rolePo);
		if(!BaseStormSystemType.ALLOW_CHEAT){
			CheckUtil.checkValidString(message, 1000);			
			if(!message.startsWith("[url")){
				CheckUtil.checkContianFiltedWord(message, false,ChatType.CHAT_FILTERS);
			}
		}
		
		chatService.playerSendChat(rolePo, message, channel,par1,0);
		return 1;
	}

	public Object sendVoiceChat(String data,Integer channel,Integer par1){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println("sendVoiceChat()");
		chatService.checkBlockChat(rolePo);
		String msg=null;
		byte[] bytes =null;

		int id = GlobalCache.VOICE_CHAT_INDEX++;
		
		bytes = DatatypeConverter.parseBase64Binary(data);
		try {
			msg=TestVoice.method1(bytes.length,data);
		} catch (Exception e) {
//			ExceptionUtil.processException(e);
		}
		if(msg==null){
			msg=GlobalCache.fetchLanguageMap("key81");
		}
		GlobalCache.VOICE_MSGS.put(id, data);
		if(GlobalCache.VOICE_MSGS.size()>=1000){
			GlobalCache.VOICE_MSGS.remove(GlobalCache.VOICE_CHAT_REMAIN_INDEX);
			GlobalCache.VOICE_CHAT_REMAIN_INDEX++;
		}
		chatService.playerSendChat(rolePo, msg, channel,par1,id);
		return 1;
	}
	
	public Object readVoiceChat(Integer voiceMsgId){
		if(GlobalCache.VOICE_MSGS.containsKey(voiceMsgId)){
			String val=GlobalCache.VOICE_MSGS.get(voiceMsgId);
			SessionUtil.addDataArray(val);
		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2266"));
		}
		return SessionType.MULTYPE_RETURN;
	}

}
