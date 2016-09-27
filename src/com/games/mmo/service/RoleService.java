package com.games.mmo.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeMap;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.ForbidVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.event.listener.LoginListener;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.ForbidPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.po.game.UpgradeSkillPo;
import com.games.mmo.type.AttackModeType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.type.TaskType;
import com.games.mmo.type.UserEventType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.SessionSaveVo;
import com.games.mmo.vo.SlotSoulVo;
import com.games.mmo.vo.role.RoleBriefVo;
import com.games.mmo.vo.xml.PlayerFile.UserInfos.UserInfo;
import com.storm.lib.base.BaseService;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.component.socket.netty.NettyType;
import com.storm.lib.component.text.TextUtil;
import com.storm.lib.event.EventArg;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

@Controller
public class RoleService  extends BaseService{
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleTemplate roleTemplate;
	@Autowired
	private ChatService chatService;
	@Autowired
	private MailService mailService;
	/**
	 * 事件初始化
	 */
	@Override
	protected void initListener() {
		addListener(UserEventType.LOGIN.getValue(), (LoginListener)BeanUtil.getBean("loginListener"));
	}

	public RolePo creteRole(Integer userId,String name,Integer modelId) {
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		String language = String.valueOf(gp.valueObj);
		 UserPo userPo = UserPo.findEntity(userId);
		 RolePo rolePo = new RolePo();
		 rolePo.setName(name);
		 rolePo.setPssd(null);
		 if("vi".equals(language)){
			 rolePo.setLv(2);			 
			 rolePo.setNewbieStepGroup(3);
		 }else{
			 rolePo.setLv(1);
			 rolePo.setNewbieStepGroup(1);
		 }
		 rolePo.setExp(0);
		 rolePo.setCreateTime(System.currentTimeMillis());
		 rolePo.setDiamondBasinsTime((int) (System.currentTimeMillis()/1000));
		 rolePo.setLastLoginTime(System.currentTimeMillis());
		 rolePo.setLastLogoffTime(System.currentTimeMillis());
		 rolePo.setX(0);
		 rolePo.setY(0);
		 rolePo.setZ(0);
		 rolePo.setCareer(modelId);
		 rolePo.setRoomId(20101001);
		 rolePo.setDiamond(userPo.getDiamond());
		 rolePo.setSkillPoint(0);
		 rolePo.setIuid(userPo.fetchKey()+"_"+System.currentTimeMillis());
		 rolePo.setUserCreatedTime(userPo.getCreateTime());
		 rolePo.setUserId(userId);
		 rolePo.setUserIuid(userPo.getIuid());
		 rolePo.setVipLv(userPo.getVipLv());
		 rolePo.checkInitializeActivityInfo();
		 rolePo.checkInitializeAdvanceSuitPlusInfo();
		 rolePo.setGuildBossAwardFlushTime(DateUtil.fetchTimesWeekSat());
		 rolePo.setChannelKey(userPo.getChannelKey());
		 rolePo.setResourceSceneTime(1000*60*30);
		 rolePo.setStartResourceSceneTime(System.currentTimeMillis());
		 roleDAO.insert(rolePo);
		 rolePo.activeLevelReachTask(1);
		 
		 RoleBriefVo roleBriefVo = new RoleBriefVo();
		 roleBriefVo.syncFromRolePo(rolePo);
		 roleBriefVo.setRemoveTime("0");
		 userPo.listRoleBriefVo.add(roleBriefVo);
		 rolePo.setDeviceId(userPo.getDeviceId());
		
		 String uuid = StringUtil.fetchUUID(Long.valueOf(rolePo.getId()), 9);
		 rolePo.setInvitationCode(uuid);
		 
		 for (IdNumberVo2 idNumberVo : rolePo.listRoleTasks) {
			 TaskPo taskPo = TaskPo.findEntity(idNumberVo.getInt1());
			 if(taskPo.getTaskType().intValue() != TaskType.TASK_TYPE_YUN_DART){
				 idNumberVo.setInt2(TaskType.TASK_STATUS_ACCEPTED);				 
			 }
		 }
		 List<UpgradeSkillPo> upgradeSkillPos = GlobalCache.careerUpgradeSkillPos.get(rolePo.getCareer());
		 if(upgradeSkillPos!=null){
			 for(int i=0;i<upgradeSkillPos.size();i++){
				 rolePo.listSkillVos.add(new IdNumberVo(upgradeSkillPos.get(i).getSkillId(),1));
			 } 
		 }
//		 MailService mailService = (MailService) BeanUtil.getBean("mailService");
//		 StringBuilder sb = new StringBuilder();
//		 sb.append(1);
//		 sb.append("|");
//		 sb.append(310100001);
//		 sb.append("|");
//		 sb.append(1);
//		 mailService.sendSystemMail("系统", rolePo.getId(), null, "加入官方QQ群领取神秘礼包，内含紫色武将，紫色武器，海量钻石等等，官方QQ群号：372100573", sb.toString(), MailType.MAIL_TYPE_SYSTEM);

//		 List<List<Integer>> itemList =ExpressUtil.buildBattleExpressList(XmlCache.xmlFiles.constantFile.guild.exchange.items);
//		 for (List<Integer> items: itemList) {
//			 List<Integer> vals = new ArrayList<Integer>();
//			 vals.add(items.get(0));
//			 vals.add(0);
//			 rolePo.listGuildTodayExchangeItems.add(vals);
////			 System.out.println(rolePo.listGuildTodayExchangeItems);
//		 }
		 
			String initAtbs =  XmlCache.xmlFiles.constantFile.soulSlot.extract.initAtb;
			List<List<Integer>> initAtbList = ExpressUtil.buildBattleExpressList(initAtbs);
			
		 
		 // 附魂
		 for (Integer slot : RoleType.EQUIP_SLOTS) {
			 List<Integer> initAtb = initAtbList.get(slot.intValue() - 1);
			SlotSoulVo slotSoulVo = new SlotSoulVo();

			slotSoulVo.slotNum=slot;
			slotSoulVo.extract1BatType=initAtb.get(0);
			if(rolePo.getCareer().intValue() == RoleType.CAREER_MAGE){
				if(initAtb.get(0).intValue() == 1){
					slotSoulVo.extract1BatType=2;
				}
			}
			slotSoulVo.extract1BatVal=initAtb.get(1);
			slotSoulVo.extract1Quality=1;
			slotSoulVo.extract1Star=1;
			slotSoulVo.extract1Status= SlotSoulType.EXTRACT_STATUS_UNLOCK;
			rolePo.listSlotSouls.add(slotSoulVo);
		}
		 rolePo.initCreateRoleAttribute();
		 rolePo.calculateBat(0);
//		 System.out.println("roleId="+rolePo.getId()+"; name="+rolePo.getName()+"; lv="+rolePo.getLv()+"; career="+rolePo.getCareer()+"; BattlePower="+rolePo.getBattlePower());
		 RoleInforPo roleInforPo = new RoleInforPo();
		 roleInforPo.setRoleName(rolePo.getName());
		 roleInforPo.setRoleId(rolePo.getId());
		 roleInforPo.setRoleLv(rolePo.getLv());
		 roleInforPo.setCareer(rolePo.getCareer());
		 roleInforPo.setBattlePower(rolePo.getBattlePower());
		 roleInforPo.setLastLoginTime(rolePo.getLastLoginTime());
		 roleInforPo.setOnlineStatus(1);
		 roleDAO.insert(roleInforPo); 
		 rolePo.setRoleInforId(roleInforPo.getId());
		 
//		 for (TaskPo taskPo : GlobalCache.taskListByTypes.get(TaskType.TASK_TYPE_ACHIEVE)) {
//			rolePo.listRoleAchieves.add(new IdNumberVo2(taskPo.getId(),0,0));
//		 }
		 
		 // TODO 韩国CBT奖励 2016-06-30 删除
		 rolePo.koActivity();
		 
		 
		 GlobalPo globalPo=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions));
		 List<IdNumberVo> list=(List<IdNumberVo>) globalPo.valueObj;
		 IdNumberVo idNumberVo = IdNumberVo.findIdNumber(GlobalPo.OptionTypeOldPlayerReward, list);
//		 System.out.println("idNumberVo = "+idNumberVo);
		 UserInfo userInfo = null;
		 if(userPo.getIuid() != null){
			 userInfo = GlobalCache.fetchPlayerByIuid(userPo.getIuid());			 
		 }
		 if(userInfo!=null && userPo.getWasRewardDiamond() ==null && idNumberVo !=null && idNumberVo.getNum() == 1){
			 MailService mailService = (MailService) BeanUtil.getBean("mailService");
			 List<IdNumberVo3> award = new ArrayList<IdNumberVo3>();
			 int totalDiamond = (int) ((1+0.5)*userInfo.totalDiamond);
			 RechargePo endFlag = RechargePo.findEntity(1);
			 while(totalDiamond >= endFlag.getRechargeNum().intValue()){
				 RechargePo temp = null;
				 for(int i=0; i<GlobalCache.listRechargePo.size(); i++){
					 RechargePo rechargePo =GlobalCache.listRechargePo.get(i); 
					 if(totalDiamond >= rechargePo.getRechargeNum()){
						 temp = rechargePo;
					 }
				 }
				 if(temp!=null){
					 int code = GlobalCache.fetchRechargeItemCodeByGroupId(temp.getGroupId());
					 if(code != 0){
						 award.add(new IdNumberVo3(1, code, 1, 1));
						 if(award.size() >= 6){
							 mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2611"),GlobalCache.fetchLanguageMap("key2611"),award);
							 award.clear();
						 }
					 }
					 totalDiamond-=temp.getRechargeNum();
				 }else{
					 break;
				 }
			 
			 }
			
			 award.add(new IdNumberVo3(1, 300001003, 100, 1));
			 award.add(new IdNumberVo3(1, 310100015, 1, 1));
			 award.add(new IdNumberVo3(1, 320100015, 1, 1));
			 award.add(new IdNumberVo3(1, 330100015, 1, 1));
			 mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2611"),GlobalCache.fetchLanguageMap("key2611"),award);
			userPo.setWasRewardDiamond(1);
		 }
		 
		 LogUtil.writeLog(rolePo, 302, userId, 0, 0,  name+GlobalCache.fetchLanguageMap("key2495"),rolePo.getDeviceId());
		 
		 return rolePo;
	}
	
	public RolePo findRoleByIuid(String iuid) {
//		RolePo rolePo= (RolePo) findBasePoByHql("from RolePo where abandomState is null and iuid='"+iuid+"'");
		RolePo rolePo= (RolePo) roleDAO.findBasePoByHql("from RolePo where  iuid='"+iuid+"'");
		return rolePo;
	}
	
	public RolePo findRoleByName(String name) {
		RolePo rolePo= (RolePo) roleDAO.findBasePoByHql("from RolePo where  name='"+name+"'");
		return rolePo;
	}
	
//	public static RolePo getCurrentSessionRole() {
//		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
//		IoSession session =(IoSession) ThreadLocalUtil.getVar(ThreadLocalType.CURRENT_SESSION);
//		Integer obj = (Integer) session.getAttribute("roleId");
//		return (RolePo) baseDAO.getEntityPo(obj, RolePo.class);
//	}
	
	
	public boolean existRoleIuid(String iuid) {
		return roleDAO.existRoleiuid(iuid);
	}
	
	public boolean existRoleIuidAndPass(String iuid,String password) {
		return roleDAO.existRoleiuidAndpass(iuid,password);
	}
	
	public boolean existRoleName(String name) {
		return roleDAO.existRoleName(name);
	}
	public RolePo login(RolePo rolePo) {
			Object session=SessionUtil.getCurrentSession();
			rolePo.leaveRoom(true);
			if(session!=null){
				ChannelHandlerContext session2 = (ChannelHandlerContext) session;
				if(session2!=null && session2.channel().isActive() && session2.channel().isOpen()){
					if(session2.channel()!=null && session2.channel().remoteAddress()!=null){
						String ipStr=session2.channel().remoteAddress().toString();
						String realIp=ipStr.split("/")[1].split(":")[0];
//						System.out.println("Ip login:"+realIp);
						rolePo.setLastLoginIp(realIp);
					}
				}
			}
			
			if(rolePo.getAbandomState() != null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.languageMap.get("key2285"));
			}
		
			if(GlobalCache.forbidMap.containsKey(rolePo.getId())){
				ForbidVo forbidVo = GlobalCache.forbidMap.get(rolePo.getId());
				if(System.currentTimeMillis()>=forbidVo.startTime&& System.currentTimeMillis()<=forbidVo.endTime){
					// 禁止登录
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2285"));
				}
			}
			//按IP或Iuid或DeviceId封人
			List<ForbidPo> forbidPos = BaseDAO.instance().dBfind("from ForbidPo where " +
					"(roleName="+"'"+rolePo.getLastLoginIp()+"'"+" or roleName ="+"'"+rolePo.getIuid().split("_")[0]+"')" +" or roleName ="+"'"+rolePo.getDeviceId()+"')");
			for(ForbidPo forbidPo :forbidPos){
				if(System.currentTimeMillis()>=forbidPo.getStartTime()&& System.currentTimeMillis()<=forbidPo.getEndTime()){
					// 禁止登录
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2285"));
					break;
				}
			}
				
			SessionSaveVo sessionSaveVo=GlobalCache.sessionSaveVos.get(rolePo.getId());
//			Object oldSession = roleTemplate.getSessionById(rolePo.getId());
			if(sessionSaveVo!=null){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					ExceptionUtil.processException(e);
				}
				if(((ChannelHandlerContext) sessionSaveVo.session).channel().isActive()){
					BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
					basePushTemplate.singleSession("PushRemoting.sendIAmKicked", sessionSaveVo.session, new Object[]{0}, null,true);
					BaseSessionUtil.flushSession(sessionSaveVo.session);
					logoff(sessionSaveVo.session,0); 
					((ChannelHandlerContext) sessionSaveVo.session).disconnect();
				}
				else{
					logoff(sessionSaveVo.session,0); 
				}
			}

			if(AttackModeType.MILITARY_FORCES.getType() == rolePo.getAttackMode().intValue()){
				rolePo.setAttackMode(AttackModeType.PEACE.getType());
				rolePo.setAttackRecoverTime(System.currentTimeMillis());				
			}
//			System.out.println("roomId = "+ rolePo.getRoomId());
			if(rolePo.getRoomId() == null){
				rolePo.setRoomId(20101001);
			}
			MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());

//			PrintUtil.print("rolePo.getRoomId(): "+rolePo.getRoomId());
			if(mapRoom == null || mapRoom.isDynamic){
				if(rolePo.getStaticRoomId() == null){
					rolePo.setStaticRoomId(20101001);
				}
				rolePo.setRoomId(rolePo.getStaticRoomId());
				ScenePo scenePo = ScenePo.findEntity(MapWorld.findStage(rolePo.getRoomId()).sceneId);
				rolePo.setX(scenePo.getX());
				rolePo.setY(scenePo.getY());
				rolePo.setZ(scenePo.getZ());
			}
			else{
				if(rolePo.getBatHp().intValue() <= 0){
					ScenePo scenePo = ScenePo.findEntity(mapRoom.sceneId);
					rolePo.setX(scenePo.getX());
					rolePo.setY(scenePo.getY());
					rolePo.setZ(scenePo.getZ());
					rolePo.setBatHp(rolePo.getBatMaxHp());
				}
			}
			//红名死亡到红名村
//			PrintUtil.print("GlobalCache.redRoleDieMap.size(): "+GlobalCache.redRoleDieMap.size());
			if(GlobalCache.redRoleDieMap.containsKey(rolePo.getId())){
				if(rolePo.getPkStatus().intValue()==Fighter.PK_STATUS_RED){
					mapRoom = MapWorld.findStage(20100005);
					rolePo.setRoomId(20100005);
					ScenePo sc =ScenePo.findEntity(mapRoom.sceneId);
					rolePo.setX(sc.getX());
					rolePo.setY(sc.getY());
					rolePo.setZ(sc.getZ());
				}
				GlobalCache.redRoleDieMap.remove(rolePo.getId());
//				PrintUtil.print("reset red role xyz");
			}			
//			PrintUtil.print("x: "+rolePo.getX()+" y: "+rolePo.getY()+" z: "+rolePo.getZ());
			ChannelHandlerContext ioSession = SessionUtil.getCurrentSession();
			((AttributeMap) ioSession).attr(NettyType.roleId).set(rolePo.getId()); 
			((AttributeMap) ioSession).attr(NettyType.rolePo).set(rolePo); 
//			ioSession.setAttribute("roleId", rolePo.getId());
			roleTemplate.insertRoleIdIuidMapping(rolePo.getId(),rolePo.getIuid());
			roleTemplate.insertIuidSessionMapping(rolePo.getIuid(), ioSession);
			rolePo.calculateBat(0);
			rolePo.checkAndFreshRoleLiveActivitys();
			
//			rolePo.switchState(RoleType.ROLE_STATE_FREE);
			chatService.joinWorldChannel(rolePo.getIuid(), ioSession);
			if(rolePo.getGuildId().intValue() != 0){
				chatService.joinRoomChannel(rolePo.getGuildId(), rolePo.getIuid(), ioSession, ChatTempate.chatGuildRooms);
			}
	    	//roleTemplate.insertIuidSessionMapping(rolePo.getIuid(), ioSession);
//			System.out.println(rolePo.mainPackItemVosMap.get(0));
//			System.out.println("login");
			UserPo userPo = UserPo.findEntity(rolePo.getUserId());
			rolePo.setVipLv(userPo.getVipLv());
			rolePo.setDiamond(userPo.getDiamond());
			rolePo.checkLoginReset();
//			rolePo.sendUpdateCurrentTime();
			rolePo.setRoomLoading(1);
			String content = MessageFormat.format(GlobalCache.fetchLanguageMap("key2688"), rolePo.getName());
			rolePo.sendGoodFriend(content);

			rolePo.syncToInfor();
			mailService.syncRoleRead(rolePo);
			SessionSaveVo saveVo = new SessionSaveVo();
			saveVo.session=ioSession;
			saveVo.iuid=rolePo.getIuid();
			saveVo.id=rolePo.getId();
			saveVo.lastMsgTime=System.currentTimeMillis();
			GlobalCache.sessionSaveVos.put(rolePo.getId(), saveVo);
			//登录事件抛出
			Calendar calendar = Calendar.getInstance();
			rolePo.tryFixMe();
//			rolePo.tryUpdateTeamMembersOnlineChange(1);
			notifyListeners(new EventArg(rolePo, UserEventType.LOGIN.getValue(), calendar.getTime()));//发送升级通知
			LogUtil.writeLog(rolePo, 304,rolePo.getId(), 0, 0,  rolePo.getName()+GlobalCache.fetchLanguageMap("key2496"),rolePo.getDeviceId());
			return rolePo;
		}
	


	/**
	 * 
	 * 方法功能:判断角色是否存在
	 * 更新时间:2011-9-16, 作者:johnny
	 * @param targetRoleId
	 */
	public void checkAvaRole(Integer roleId){
		if(!existRole(roleId)){
			TextUtil.throwConfirmParamCode("r3");
		}
	}
	
	public Boolean existRole(Integer id) {
		return roleDAO.existRole(id);
	}

	/**
	 * 根据用户名找到id
	 * @param name
	 * @return
	 */
	public Integer getRoleIdByName(String name){
		return roleDAO.getRoleIdByName(name,0,0,100,100);
	}
	
	public Integer getRoleIdByToken(String name){
		return roleDAO.getRoleIdByToken(name);
	}
	

	/**
	 * 
	 * 方法功能:用户登出
	 * 更新时间:2011-8-7, 作者:johnny
	 * @param iuid
	 */
	public void logoff(ChannelHandlerContext ioSession,int waitMs) {
		if(ioSession==null){
			return;
		}

		Integer roleId = ((AttributeMap) ioSession).attr(NettyType.roleId).get();
		if(roleId!=null && GlobalCache.sessionSaveVos.containsKey(roleId)){
			GlobalCache.sessionSaveVos.remove(roleId);
			RolePo rolePo = RolePo.findEntity(roleId);
//			System.out.println("logoff:"+rolePo.getName());
			// 移交队长
			rolePo.changeTeamCaptain();
			rolePo.leaveRoom(true);
			rolePo.setLastLogoffTime(System.currentTimeMillis());
			UserPo userPo=UserPo.findEntity(rolePo.getUserId());
			if(userPo !=null){
				long newLastLogoffIime=System.currentTimeMillis();
				long deletaTime=DateUtil.getInitialDate(newLastLogoffIime)-DateUtil.getInitialDate(userPo.getCreateTime());
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
				userPo.setLastLogoffTime(newLastLogoffIime);
			}
			
//			if(waitMs==0){
//				rolePo.leaveRoom(true);
//			}
//			else{
//				LogoffFighterThread thread =new LogoffFighterThread(); 
//				thread.waitMs=waitMs;
//				thread.rolePo=rolePo;
//				thread.start();
//			}

			rolePo.listActivityInfo.get(0).adjustTheSameDayOnlineTime(rolePo.fetchCurrentOnlineTime());
//			System.out.println(rolePo.getName() + "; 退出计算的累计登录时间：" + rolePo.listActivityInfo.get(0).theSameDayOnlineTime/(1000*60));
			rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,
					0l,
					rolePo.fetchRedCurrentOnlineTime(),
					1);
//			System.out.println("        ");
//			System.out.println(" rolePo.fetchRedCurrentOnlineTime() = " +rolePo.fetchRedCurrentOnlineTime());
//			IdLongVo2 idLongVo2 = IdLongVo2.findIdLong(RoleType.ONLINE_TIME_TYPE_RED, rolePo.listOnlineTime);
//			System.out.println(rolePo.getName() + "logoff() idLongVo2 =" +idLongVo2);
//			
			rolePo.offlineCheckBufferStatus();
			
			if(rolePo.getTotalOnline()==null){
				rolePo.setTotalOnline(0);
			}
			Long deltaS = new Long((rolePo.getLastLogoffTime()-rolePo.getLastLoginTime())/1000);
			rolePo.setTotalOnline(rolePo.getTotalOnline()+deltaS.intValue());
			MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
			if(mapRoom == null){
				Integer staticRoomId = rolePo.getStaticRoomId();
				if(staticRoomId == null){
					staticRoomId = 20101001; 					
				}
				rolePo.setStaticRoomId(rolePo.getStaticRoomId());
				rolePo.setRoomId(rolePo.getStaticRoomId());
				if(rolePo.getRoomId()==null){
					rolePo.setRoomId(20101001);
				}
				ScenePo scenePo = ScenePo.findEntity(MapWorld.findStage(rolePo.getRoomId()).sceneId);
				rolePo.setX(scenePo.getX());
				rolePo.setY(scenePo.getY());
				rolePo.setZ(scenePo.getZ());

			}
			else{
				ScenePo scenePo = ScenePo.findEntity(MapWorld.findStage(rolePo.getRoomId()).sceneId);
				//如果在挂机地图
				if(scenePo != null && scenePo.getSceneAttribute().intValue() == 2){
					scenePo = ScenePo.findEntity(20101001);
					rolePo.setRoomId(scenePo.getId());
					rolePo.setStaticRoomId(scenePo.getId());
					rolePo.setX(scenePo.getX());
					rolePo.setY(scenePo.getY());
					rolePo.setZ(scenePo.getZ());
				}
			}
	    	roleTemplate.removeRoleIdIuidMapping(rolePo.getId());
	    	chatService.leaveWorldChannel(rolePo.getIuid());
	    	chatService.leaveRoomChannel(rolePo.getGuildId(), rolePo.getIuid(), ioSession, ChatTempate.chatGuildRooms);
//			Sout.println("完成注销用户");	
			roleTemplate.removeIuidSessionMapping(rolePo.getIuid());
			((AttributeMap) ioSession).attr(NettyType.roleId).set(null); 
//			rolePo.tryUpdateTeamMembersOnlineChange(0);
			//角色登出
			LogUtil.writeLog(rolePo, 306, rolePo.getId(), 0, 0, rolePo.getName()+GlobalCache.fetchLanguageMap("key2497"), rolePo.getDeviceId());
		}
		RolePo rolePo = null;
		if(roleId!=null){
			rolePo = RolePo.findEntity(roleId);
			updateLoginRoleList(rolePo);
//			SessionUtil.addDataArray(UserPo.findEntity(rolePo.getUserId()));
		}
		else{
//			SessionUtil.addDataArray(null);
		}
//		return SessionType.MULTYPE_RETURN;


	}
	
	/**
	 * 更新登陆角色列表信息
	 */
	public void updateLoginRoleList(RolePo rolePo)
	{
		UserPo userPo = UserPo.findEntity(rolePo.getUserId());
		RoleBriefVo roleBrief = userPo.findRoleBriefByRoleId(rolePo.getId());
		boolean isUpdate = false;
		if(roleBrief==null){
			return;
		}
		if(roleBrief.getRoleLv() != rolePo.getLv().intValue())
		{
			roleBrief.setRoleLv(rolePo.getLv());
			isUpdate = true;
		}
		if(!roleBrief.getRoleName().equals(rolePo.getName())){
			roleBrief.setRoleName(rolePo.getName());
			isUpdate = true;
		}
//		PrintUtil.print("aaaa:"+rolePo.getFashion());
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
		roleBrief.setRoleClothAvatar(commonAvatarVo.modelAvatar);
		roleBrief.setRoleWeaponAvatar(commonAvatarVo.weaponAvatar);
		roleBrief.setRoleWingAvatar(commonAvatarVo.wingAvatar);
		if(isUpdate){
			userPo.updateRoleBriefVo();
		}
		userPo.srotListRoleBriefVo();
	}
	


	public RolePo getRolePo(Integer roleId) {
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo==null){
			if(roleId==null){
				return null;
			}
			if(GlobalCache.robotTaskRoles.containsKey(roleId))
			{
				return GlobalCache.robotTaskRoles.get(roleId);
			}
			else if(GlobalCache.robotArenaRoles.containsKey(roleId))
			{
				return GlobalCache.robotArenaRoles.get(roleId);
			}
			else{
				return null;
			}
		}
		return rolePo;
	}

	

	public Integer findRoleIdById(Integer keyword) {
		return roleDAO.findRoleIdById(keyword);
	}

	public Integer findRoleIdByName(String keyword) {
		return roleDAO.findRoleIdByName(keyword);
	}
	
	public Integer findRoleIdByInvitationCode(String invitationCode){
		return roleDAO.findRoleIdByInvitationCode(invitationCode);
	}


	public List<Integer> fetchAllChannelRoleIds(String channels,Integer minLv,Integer minVipLv,Integer maxLv,Integer maxVipLv) {
		return roleDAO.fetchAllChannelRoleIds(channels,minLv,minVipLv,maxLv,maxVipLv);
	}
	
	public List<Integer> fetchAllRoleIds(Integer minLv,Integer minVipLv,Integer maxLv,Integer maxVipLv) {
		return roleDAO.fetchAllRoleIds(minLv,minVipLv,maxLv,maxVipLv);
	}

	public void checkKickAllForbidRoles() {
		for (ForbidVo forbidVo : GlobalCache.forbidMap.values()) {
			RolePo rolePo = RolePo.findEntity(forbidVo.roleId);
			if(rolePo.fetchRoleOnlineStatus()){
				rolePo.sendMsg(GlobalCache.fetchLanguageMap("key2285"));
				logoff(rolePo.fetchSession(), 0);
			}
		}
	}
	
	public List<RolePo> findRoleByIp(String lastLoginIp) {
		List<RolePo> list= roleDAO.dBfind("from RolePo where lastLoginIp='"+lastLoginIp+"'");
		return list;
	}
	
	public List<RolePo> findRoleByUserIuid(String userIuid) {
		List<RolePo> list= roleDAO.dBfind("from RolePo where iuid like'"+userIuid+"_%"+"'");
		return list;
	}
	public List<RolePo> findRoleByDeviceId(String deviceId) {
		List<RolePo> list= roleDAO.dBfind("from RolePo where deviceId='"+deviceId+"'");
		return list;
	}

}
