package com.games.mmo.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.netty.channel.ChannelHandlerContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.dao.RoleDAO;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.vo.role.RoleBriefVo;
import com.storm.lib.base.BaseService;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.StringUtil;

@Controller
public class UserService  extends BaseService {
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleTemplate roleTemplate;
	@Autowired	
	private RoleService roleService;
	
	/**
	 * 检查用户是否存在
	 * @param iuid
	 * @param password
	 * @return
	 */
	public boolean existUserIuidAndPass(String iuid,String password) {
		return roleDAO.existUseriuidAndpass(iuid, password);
	}
	
	/**
	 *  根据iuid查看账户名是否重复
	 * @param iuid
	 * @return
	 */
	public boolean existUserIuid(String iuid) {
		return roleDAO.existUseriuid(iuid);
	}
	
	/**
	 * 根据iuid查找User对象
	 * @param iuid
	 * @return
	 */
	public UserPo findUserByIuid(String iuid,Integer serverId) {
		UserPo userPo= (UserPo) roleDAO.findBasePoByHql("from UserPo where  iuid='"+iuid+"' and serverId="+serverId);
		return userPo;
	}
	
	public UserPo findUserByToken(String token, int serverId){
		UserPo userPo= (UserPo) roleDAO.findBasePoByHql("from UserPo where  token='"+token+"' and serverId="+serverId);
		return userPo;
	}

	
	/**
	 * 创建正式用户
	 * @param iuid
	 * @param password
	 * @return
	 */
	public UserPo creteUser(String iuid,String password,String channleKey,String deviceId,Integer serverId, String idfa, String packageName) {
		UserPo userPo = new UserPo();
		userPo.setIuid(iuid);
		userPo.setPssd(password);
		userPo.setDiamond(0);
		userPo.setVipLv(0);
		userPo.setChannelKey(channleKey);
		userPo.setCreateTime(System.currentTimeMillis());
		userPo.setDeviceId(deviceId);
		userPo.setCumulativeRechargeNum(0);
		userPo.setServerId(serverId);
		userPo.setKeepInformation("");
		userPo.setIdfa(idfa);
		userPo.setPackageName(packageName);
		userPo.setToken("");
		roleDAO.insert(userPo);
		return userPo;
	}
	
	/**
	 * 创建游客用户
	 * @param iuid
	 * @param password
	 * @return
	 */
	public UserPo creteTokenUser(String token,String channleKey,String deviceId,Integer serverId, String idfa, String packageName) {
		UserPo userPo = new UserPo();
		userPo.setPssd(token);
		userPo.setDiamond(0);
		userPo.setVipLv(0);
		userPo.setChannelKey(channleKey);
		userPo.setCreateTime(System.currentTimeMillis());
		userPo.setDeviceId(deviceId);
		userPo.setCumulativeRechargeNum(0);
		userPo.setServerId(serverId);
		userPo.setKeepInformation("");
		userPo.setIdfa(idfa);
		userPo.setPackageName(packageName);
		userPo.setToken(token);
		roleDAO.insert(userPo);
		return userPo;
	}
	
	
	public UserPo userLogin(UserPo userPo,ChannelHandlerContext session) {
//		IoSession oldSession = roleTemplate.getSessionByUserId(userPo.getId());
//		if(oldSession!=null && oldSession.isConnected()){
////			notifyService.sendOnlineAutoMissConfirmMessage(userRoleVo.getRoleId(), "您在其他地方登陆了");
//			try {
//				Thread.sleep(200);
//			} catch (InterruptedException e) {
//				ExceptionUtil.processException(e);
//			}
////			System.out.println("login logoff");
////			logoff(oldSession,true);
//			//Sout.println("被踢下");
//			oldSession.close();
//		}
		// 最后登录时间
		long newLastLoginIime=System.currentTimeMillis();
		long deletaTime=DateUtil.getInitialDate(newLastLoginIime)-DateUtil.getInitialDate(userPo.getCreateTime());
		if(userPo.getKeepInformation()==null){
			userPo.setKeepInformation("");
		}
		int addNum=(int)(deletaTime/(3600*1000*24l))-userPo.getKeepInformation().length();
		if(addNum>0){
			for(int i=0;i<addNum;i++){
				if(i!=addNum-1){
					userPo.setKeepInformation(userPo.getKeepInformation()+"0");
				}else{
					userPo.setKeepInformation(userPo.getKeepInformation()+"1");
				}
			}
		}
		userPo.setLastLoginTime(newLastLoginIime);
		
		userPo.initCreateUserAttribute();
//		IoSession ioSession = SessionUtil.getCurrentSession();
//		roleTemplate.insertUserIdIuidMapping(userPo.getId(),userPo.getIuid());
//		roleTemplate.insertIuidSessionMapping(userPo.getIuid(), ioSession);
//		ioSession.setAttribute("userId", userPo.getId());

		roleTemplate.insertUserIuidSessionMapping(userPo.fetchKey(), session);
		for (RoleBriefVo roleBriefVo : userPo.listRoleBriefVo) {
			roleService.updateLoginRoleList(RolePo.findEntity(roleBriefVo.getRoleId()));
		}
		return userPo;
	}
}
