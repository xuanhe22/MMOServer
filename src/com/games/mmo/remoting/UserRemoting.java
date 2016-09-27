package com.games.mmo.remoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.UserPo;
import com.games.mmo.service.UserService;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;

@Controller
public class UserRemoting extends BaseRemoting {
	@Autowired
	private UserService userService;
	
	
	/**
	 * 创建和查询用户
	 * @param iuid	账号
	 * @param pass	密码
	 * @return [0]1
	 */

	public Object createAndCheckUserAccount(String iuid,String password,String channleKey,String deviceId,Integer serverId){
//		System.out.println("createAndCheckUserAccount() iuid="+iuid);
		//TODO 【请求优化】sql索引优化
//		System.out.println("serverId:"+serverId);
		if(StringUtil.isEmpty(iuid)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2308"));
		}
		
		//1=成功 2=密码错误
		int status = 1;
		UserPo userPo = userService.findUserByIuid(iuid,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteUser(iuid,password,channleKey,deviceId,serverId,null, null);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
//			System.out.println("sb="+sb);
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		userPo.createAndCheckUserAccount(channleKey);
		GlobalCache.removeServerQueueNum(userPo.getId());
		SessionUtil.addDataArray(userPo);
		//1=成功 2=密码错误
		SessionUtil.addDataArray(status);
		SessionUtil.addDataArray(System.currentTimeMillis());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 登录排队
	 * @param iuid
	 * @param password
	 * @param channleKey
	 * @param deviceId
	 * @param serverId
	 * @param wasEnter
	 * @return
	 */
	public Object createAndCheckUserAccountLoginQueue(String iuid,String password,String channleKey,String deviceId,Integer serverId, Integer wasEnter, String idfa){
		PrintUtil.print("createAndCheckUserAccountLoginQueue() iuid = " +iuid);
		if(StringUtil.isEmpty(iuid)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2308"));
		}
		
		//1=成功 2=密码错误
		int status = 1;
		UserPo userPo = userService.findUserByIuid(iuid,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteUser(iuid,password,channleKey,deviceId,serverId,idfa, null);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
//			System.out.println("sb="+sb);
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		userPo.createAndCheckUserAccount(channleKey);
		
		// 是否登录成功
		int wasLoginSuccess =1;
		// 在线总人数
		int onlineNum = RoleTemplate.roleIdIuidMapping.size();
		// 排队总人数
		int queueNum=0;
		// 当前排队名次
		int rankNum=0;
		
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum);
		int onlineMaxNum = Integer.valueOf(String.valueOf(globalPo.valueObj));
//		System.out.println("onlineNum="+onlineNum+"; onlineMaxNum="+onlineMaxNum);
		if(onlineNum >= onlineMaxNum && wasEnter.intValue()==0){
			wasLoginSuccess=0;
			GlobalCache.removeServerQueueNum(userPo.getId());
			rankNum=GlobalCache.addServerQueueNum(userPo.getId(), iuid);
			queueNum =GlobalCache.serverQueueNum.size();
		}
//		System.out.println("wasLoginSuccess="+wasLoginSuccess);
		SessionUtil.addDataArray(wasLoginSuccess);
		SessionUtil.addDataArray(queueNum);
		SessionUtil.addDataArray(rankNum);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 2016-06-23 : 增加了 packageName 字段
	 * @param iuid
	 * @param password
	 * @param channleKey
	 * @param deviceId
	 * @param serverId
	 * @param wasEnter
	 * @param idfa
	 * @param packageName
	 * @return
	 */
	public Object createAndCheckUserAccountLoginQueue2(String iuid,String password,String channleKey,String deviceId,Integer serverId, Integer wasEnter, String idfa, String packageName){
//		serverId = 4;
//		channleKey= "hd_appstore_ios";
		PrintUtil.print("      ");
		PrintUtil.print("createAndCheckUserAccountLoginQueue2");
		PrintUtil.print("iuid = " +iuid);
		PrintUtil.print("channleKey = " +channleKey);
		PrintUtil.print("serverId = " +serverId);
		if(StringUtil.isEmpty(iuid)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2308"));
		}
		
		//1=成功 2=密码错误
		int status = 1;
		UserPo userPo = userService.findUserByIuid(iuid,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteUser(iuid,password,channleKey,deviceId,serverId,idfa, packageName);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
//			System.out.println("sb="+sb);
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		userPo.createAndCheckUserAccount(channleKey);
		
		// 是否登录成功 0：失败； 1：成功 
		int wasLoginSuccess =1;
		// 在线总人数
		int onlineNum = RoleTemplate.roleIdIuidMapping.size();
		// 排队总人数
		int queueNum=0;
		// 当前排队名次
		int rankNum=0;
		
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum);
		int onlineMaxNum = Integer.valueOf(String.valueOf(globalPo.valueObj));
//		System.out.println("onlineNum="+onlineNum+"; onlineMaxNum="+onlineMaxNum);
		if(onlineNum >= onlineMaxNum && wasEnter.intValue()==0){
			wasLoginSuccess=0;
			GlobalCache.removeServerQueueNum(userPo.getId());
			rankNum=GlobalCache.addServerQueueNum(userPo.getId(), iuid);
			queueNum =GlobalCache.serverQueueNum.size();
		}
//		System.out.println("wasLoginSuccess="+wasLoginSuccess);
		SessionUtil.addDataArray(wasLoginSuccess);
		SessionUtil.addDataArray(queueNum);
		SessionUtil.addDataArray(rankNum);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 登录排队之后进入游戏
	 * @param iuid
	 * @param password
	 * @param channleKey
	 * @param deviceId
	 * @param serverId
	 * @return
	 */
	public Object createAndCheckUserAccountLoginLoginQueueAfter(String iuid,String password,String channleKey,String deviceId,Integer serverId){
//		serverId = 4;
//		channleKey = "hd_appstore_ios";
		PrintUtil.print("      ");
		PrintUtil.print("createAndCheckUserAccountLoginLoginQueueAfter");
		PrintUtil.print("iuid = " +iuid);
		PrintUtil.print("channleKey = " +channleKey);
		PrintUtil.print("serverId = " +serverId);
		
		UserPo userPo = userService.findUserByIuid(iuid,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteUser(iuid,password,channleKey,deviceId,serverId, null, null);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
//			System.out.println("sb="+sb);
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		int status = 1;
		GlobalCache.removeServerQueueNum(userPo.getId());
		SessionUtil.addDataArray(userPo);
		SessionUtil.addDataArray(status);
		SessionUtil.addDataArray(System.currentTimeMillis());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 拉取角色信息
	 * @param iuid
	 * @param password
	 * @return
	 */
	public Object fetchUserAccount(String iuid,String password,Integer serverId){
		UserPo userPo = userService.findUserByIuid(iuid,serverId);
		userPo.srotListRoleBriefVo();
		SessionUtil.addDataArray(userPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 游客登录
	 * @param token
	 * @param channleKey
	 * @param deviceId
	 * @param serverId
	 * @param wasEnter
	 * @param idfa
	 * @param packageName
	 * @return
	 */
	public Object createAndCheckUserAccountLoginQueueToken(String token,String channleKey,String deviceId,Integer serverId, Integer wasEnter, String idfa, String packageName){
		PrintUtil.print("      ");
		PrintUtil.print("createAndCheckUserAccountLoginQueueToken");
		PrintUtil.print("token = " +token);
		PrintUtil.print("channleKey = " +channleKey);
		PrintUtil.print("serverId = " +serverId);
		
		UserPo userPo = userService.findUserByToken(token,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteTokenUser(token,channleKey,deviceId,serverId,idfa, packageName);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		userPo.createAndCheckUserAccount(channleKey);
		
		// 是否登录成功
		int wasLoginSuccess =1;
		// 在线总人数
		int onlineNum = RoleTemplate.roleIdIuidMapping.size();
		// 排队总人数
		int queueNum=0;
		// 当前排队名次
		int rankNum=0;
		
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum);
		int onlineMaxNum = Integer.valueOf(String.valueOf(globalPo.valueObj));
//		System.out.println("onlineNum="+onlineNum+"; onlineMaxNum="+onlineMaxNum);
		if(onlineNum >= onlineMaxNum && wasEnter.intValue()==0){
			wasLoginSuccess=0;
			GlobalCache.removeServerQueueNum(userPo.getId());
			rankNum=GlobalCache.addServerQueueNum(userPo.getId(), token);
			queueNum =GlobalCache.serverQueueNum.size();
		}
//		System.out.println("wasLoginSuccess="+wasLoginSuccess);
		SessionUtil.addDataArray(wasLoginSuccess);
		SessionUtil.addDataArray(queueNum);
		SessionUtil.addDataArray(rankNum);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 游客登录排队之后进入游戏
	 * @param iuid
	 * @param password
	 * @param channleKey
	 * @param deviceId
	 * @param serverId
	 * @return
	 */
	public Object createAndCheckUserAccountLoginLoginQueueAfterToken(String token,String channleKey,String deviceId,Integer serverId){
		PrintUtil.print("      ");
		PrintUtil.print("createAndCheckUserAccountLoginLoginQueueAfterToken");
		PrintUtil.print("token = " +token);
		PrintUtil.print("channleKey = " +channleKey);
		PrintUtil.print("serverId = " +serverId);
		
		UserPo userPo = userService.findUserByToken(token,serverId);
		int count =1;
		if(userPo==null){
			userPo = userService.creteTokenUser(token,channleKey,deviceId,serverId, null, null);
			StringBuilder sb = new StringBuilder();
			sb.append("select count(1) from u_po_user where device_id='").append(deviceId).append("'").append(" and first_register_state>=1");
			count =BaseDAO.instance().queryIntForSql(sb.toString(), null);
			LogUtil.writeLog(null, 301, count, 0, 0,  userPo.getId()+GlobalCache.fetchLanguageMap("key2489")+channleKey,deviceId);
			userPo.setFirstRegisterState(count+1);
		}
		int status = 1;
		GlobalCache.removeServerQueueNum(userPo.getId());
		SessionUtil.addDataArray(userPo);
		SessionUtil.addDataArray(status);
		SessionUtil.addDataArray(System.currentTimeMillis());
		return SessionType.MULTYPE_RETURN;
	}
	
}
