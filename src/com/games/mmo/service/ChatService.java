package com.games.mmo.service;

import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dyuproject.protostuff.parser.ProtoParser.parse_return;
import com.games.backend.vo.BlockVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RoleInforPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.OpenfunctionPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.task.activity.BloodMagicFortressBeginJob;
import com.games.mmo.task.activity.EvilSoulForbiddenSpaceBeginJob;
import com.games.mmo.task.activity.FreeWarBeginJob;
import com.games.mmo.task.activity.GuildBidSiegeBeginJob;
import com.games.mmo.task.activity.GuildBidSiegeEndJob;
import com.games.mmo.task.activity.KillingTowerBeginJob;
import com.games.mmo.task.activity.KingOfPkBeginJob;
import com.games.mmo.task.activity.MonsterCrisisBeginJob;
import com.games.mmo.task.activity.YunDartTaskBeginJob;
import com.games.mmo.task.activity.YunDartTaskEndJob;
import com.games.mmo.type.ChatType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.MailType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.SlotSoulType;
import com.games.mmo.type.SystemType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.SlotSoulVo;
import com.storm.lib.component.chat.BaseChatService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.BaseNettySocketServer;
import com.storm.lib.component.socket.netty.jin.JinByteEncoder;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;
@Controller
public class ChatService extends BaseChatService{
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private CheckService checkService;
	
	public static void sendStaticHorse(String msg){
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		chatService.sendHorse(msg);
		
	}
	/**
	 * 
	 * 方法功能:玩家发送聊天
	 * 更新时间:2011-9-2, 作者:johnny
	 * @param message
	 * @param channelId
	 */
	public void playerSendChat(RolePo rolePo, String message, Integer channelId,Integer par1,Integer voiceId) {
		String targetRoleName="";
		Collection<ChannelHandlerContext> sessions =new ArrayList<ChannelHandlerContext>();
		if(channelId.intValue() == ChatType.CHAT_CHANNEL_WORLD || channelId.intValue() == ChatType.CHAT_CHANNEL_SYSTEM )
		{
			sessions = chatTemplate.getChatWorldRoom().values();
		} 
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_ROOM){
			ConcurrentHashMap<Integer,ConcurrentHashMap<String,ChannelHandlerContext>> chatMMORooms = chatTemplate.getChatMMOStaticAndDynalicRooms();
			ConcurrentHashMap<String,ChannelHandlerContext> chatMMORoom = chatMMORooms.get(par1);
			if(chatMMORoom != null){
				sessions = chatMMORoom.values();				
			}
		}
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_PRIVATE)
		{
			RolePo targetRolePo = roleService.getRolePo(par1);
			targetRoleName=targetRolePo.getName();
			//			System.out.println("roleId=" +rolePo.getId()+" || targetRolePo="+targetRolePo.getId() );
			sessions.add(rolePo.fetchSession());
			sessions.add(targetRolePo.fetchSession());
		}
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_GUILD)
		{
			ConcurrentHashMap<Integer,ConcurrentHashMap<String,ChannelHandlerContext>> chatGuildRooms = chatTemplate.getChatGuildRooms();
			ConcurrentHashMap<String,ChannelHandlerContext> chatGuildRoom = chatGuildRooms.get(par1);
			if(chatGuildRoom != null){
				sessions = chatGuildRoom.values();				
			}
		}
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_TEAM)
		{
			ConcurrentHashMap<Integer,ConcurrentHashMap<String,ChannelHandlerContext>> chatTeamRooms = chatTemplate.getChatTeamRooms();
			ConcurrentHashMap<String,ChannelHandlerContext> chatTeamRoom = chatTeamRooms.get(par1);
			if(chatTeamRoom != null){
				sessions = chatTeamRoom.values();				
			}
		}
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_GOOD_FRIEND)
		{
			RoleTemplate roleTemplate=(RoleTemplate) BeanUtil.getBean("roleTemplate");
			 for(RoleInforPo rp :  rolePo.listFriends){
				 ChannelHandlerContext session =roleTemplate.getSessionById(rp.getRoleId());
				 if(session != null){
					 sessions.add(session);					 
				 }
			 }
		}
		else if(channelId.intValue() == ChatType.CHAT_CHANNEL_ONLY_SELF)
		{
			RolePo targetRolePo = roleService.getRolePo(par1);
			if(targetRolePo != null && targetRolePo.fetchSession() != null){
//				System.out.println("playerSendChat() targetRolePo = " +targetRolePo.getName() +"; message = " +message);
				sessions.add(targetRolePo.fetchSession());									
			}
		}
		
		if(BaseStormSystemType.ALLOW_CHEAT){
			doChatCheatOrder(rolePo,message);
		}
		byte[] chatBytes=null;
		if(rolePo==null){
			chatBytes=buildMessageSb(channelId, par1, message, 0, GlobalCache.fetchLanguageMap("key239"), 0, 0,voiceId,targetRoleName);
		}
		else{
			chatBytes=buildMessageSb(channelId, par1, message, rolePo.getId(), rolePo.getName(), rolePo.getLv(), 0,voiceId,targetRoleName);
		}
		if(BaseNettySocketServer.socketType==2){
			chatBytes=MMOByteEncoder.buildChatResult(chatBytes);
		}
		else if(BaseNettySocketServer.socketType==3){
//			results=JinByteEncoder.buildResultBytes(msg);
		}
		
		for (ChannelHandlerContext ioSession : sessions) {
			sendSingleChat(chatBytes, ioSession);
		}
	}
	
	/**
	 * 
	 * 方法功能:构建消息字符串
	 * 更新时间:2012-6-20, 作者:johnny
	 * @param channelId
	 * @param senderId
	 * @param senderName
	 * @param senderGendar
	 * @param message
	 * @return
	 */
	private byte[] buildMessageSb(int channelId,Integer par1,String message, int senderId,String senderName,int lv,Integer vip,Integer voiceMsgId,String receiveName) {
//		频道|辅助ID|消息体|发送者角色ID|发送者名字|发送者等级|发送者VIP
		ByteArrayOutputStream out=new ByteArrayOutputStream(128);
		try {
			out.write(ByteUtil.int2Byte(channelId));
			out.write(ByteUtil.int2Byte(par1));
			out.write(ByteUtil.StringToBytes(message));
			out.write(ByteUtil.int2Byte(senderId));
			out.write(ByteUtil.StringToBytes(senderName));
			out.write(ByteUtil.int2Byte(lv));
			out.write(ByteUtil.int2Byte(vip));
			out.write(ByteUtil.int2Byte(voiceMsgId));
			out.write(ByteUtil.StringToBytes(receiveName));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		return out.toByteArray();
	}

	private void doChatCheatOrder(RolePo rolePo, String message) {
		if (rolePo == null) {
			return;
		}
		// 切换语言
		if(message.startsWith("language ")){
			String pars = StringUtil.split(message, " ")[1];
			String str = "";
			boolean flag =false;
			if("zh_cn".equals(pars)){
				str = "中文";	
				flag=true;
			}else if("zh_tw".equals(pars)){
				str = "繁体中文";	
				flag=true;
			}else if("vi".equals(pars)){
				str = "越南文";
				flag=true;
			}else if("ko".equals(pars)){
				str = "韩文";
				flag=true;
			}else{
				str = "错误";	
			}
			if(flag){
				XmlCache.loadAndClean(pars);				
			}
			sendSystemWorldChat(rolePo.getName()+"切换"+str+"！");
		}
		
		
		if(message.startsWith("openfreewar")){
			long currentTime = System.currentTimeMillis()+60*60*1000;
			FreeWarBeginJob freeWarBeginJob = new FreeWarBeginJob();
			freeWarBeginJob.beginFreeWarDispose(CopySceneType.COPY_SCENE_CONF_FREE_WAR, currentTime);
			sendSystemWorldChat(rolePo.getName()+"没有节操的开启了自由之战！");
		}
		
		
		
		
		if(message.startsWith("resetpriest")){
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
			if(guildPo != null){
				guildPo.setPriestFreshCount(0);
				guildPo.setPriestFreshQuality(1);
				guildPo.setPriestFreshStartTime(0l);
				guildPo.setPriestFreshState(0);	
				
				for(GuildMemberVo guildMemberVo : guildPo.listMembers){
					RolePo rolePoMember = RolePo.findEntity(guildMemberVo.roleId);
					if(rolePoMember != null){
						rolePoMember.setGuildPriestState(0);						
					}
				}
				sendSystemWorldChat(rolePo.getName()+"没有节操的给自己增加了重置了公会祭祀！");
			}
		}
		
		// 增加经验
		if(message.startsWith("exp ")){
			String pars = StringUtil.split(message, " ")[1];
			if(rolePo.getLv().intValue() >98){
				pars="0";
			}
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 999999999);
			rolePo.adjustExp(targetValue);
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己增加了"+pars+"经验！");
			rolePo.calculateBat(1);
		}
		// 设置等级
		if(message.startsWith("setLv ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 99);
			rolePo.setLv(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的把自己等级设置成了"+pars+"级！");
			rolePo.calculateBat(1);
		}
		// 增加公会战功
		if(message.startsWith("guildHonor ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 999999999);
			rolePo.setGuildHonor(rolePo.getGuildHonor()+Integer.valueOf(pars));
			rolePo.sendUpdateGuildInfor();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己增加了"+pars+"公会荣誉！");
		}
		if(message.endsWith("charge")){
			String pars = StringUtil.split(message, " ")[1];
			rolePo.rechargeSendByRechargeId(7, 648.0,6480,0);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己充了"+pars+"钻石！");
		}	
		if(message.startsWith("recharge ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 7);
			RechargePo rechargePo = RechargePo.findEntity(targetValue);
			String str = rolePo.getName()+"充值钻石失败，rechargeId错误："+targetValue;
			if(rechargePo != null){
				Double rechargeRmb = Double.valueOf(rechargePo.getRechargeRmb());
				rolePo.rechargeSendByRechargeId(rechargePo.getId(), rechargeRmb,rechargePo.getRechargeNum(),0);				
				str = rolePo.getName()+"没有节操的给自己充了"+rechargePo.getRechargeNum()+"钻石！";
			}
			sendSystemWorldChat(str);
		}
		if(message.equals("showdebug")){
			SystemType.showdebug=true;
			//sendSystemWorldChat("showdebug");
		}
		if(message.equals("hidedebug")){
			SystemType.showdebug=false;
			//sendSystemWorldChat("hidedebug");
		}
		// 调整魔魂
		if(message.startsWith("petSoul ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_PETSOUL);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己增加了"+pars+"魔魂！");
		}
		// 调整技能点
		if(message.startsWith("skillPonit ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_SKILL_POINT);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了技能点");
		}
		//调整金币
		if(message.startsWith("onlyGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_GOLD);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了金币");
		}
		//调整绑金
		if(message.startsWith("bindGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_BIND_GOLD);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了绑金");
		}
		//调整钻石
		if(message.startsWith("diamond ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_DIAMOND);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了钻石");
		}
		//调整绑定钻石
		if(message.startsWith("bindDiamond ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_BIND_DIAMOND);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了绑定钻石");
		}
		// 调整公会金币
		if(message.startsWith("guildGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			GuildPo.findEntity(rolePo.getGuildId()).setGold(GuildPo.findEntity(rolePo.getGuildId()).getGold()+targetValue);
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己公会增加了"+pars+"金币");
		}
		if(message.startsWith("setguildgold ")){
			String pars = StringUtil.split(message, " ")[1];
			GuildPo.findEntity(rolePo.getGuildId()).setGold(Integer.valueOf(pars));
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己公会修改了"+pars+"金币");
		}
		if(message.startsWith("pk ")){
			String pars = StringUtil.split(message, " ")[1];
			rolePo.adjustPkValue(Integer.valueOf(pars));
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己修改了PK值为"+pars+"");
		}
		if(message.startsWith("pkinit")){
			rolePo.setPkStatus(Fighter.PK_STATUS_PEACE);
			rolePo.setPkRedBeginTime(0l);
			rolePo.setPkLastRecoverTime(0l);
			rolePo.setPkGrewRecoverTime(0l);
			rolePo.setPkValue(0);	
			rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_RED,0l,0l,0);
			rolePo.updateDynamicOnlineTimeByTypeId(RoleType.ONLINE_TIME_TYPE_GREY,0l,0l,0);
			rolePo.sendUpdatePKInfor();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己重置了PK值");
		}
		
		// 设置金币
		if(message.startsWith("setOnlyGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setGold(targetValue);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的把自己金币设置成了"+targetValue);
		}
		if(message.startsWith("achievePoint ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.adjustAchievePointPublic(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"任性的把自己ac+了"+targetValue);
		}		
		// 设置绑金
		if(message.startsWith("setBindGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setBindGold(targetValue);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的把自己绑金设置成了"+targetValue);
		}
		// 设置钻石
		if(message.startsWith("setDiamond ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			UserPo userPo = UserPo.findEntity(rolePo.getUserId());
			userPo.setDiamond(targetValue);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的把自己钻石设置成了"+targetValue);
		}
		// 设置绑定钻石
		if(message.startsWith("setBindDiamond ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setBindDiamond(targetValue);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的把自己绑定钻石设置成了"+targetValue);
		}
		// 设置公会战功
		if(message.startsWith("setGuildHonor ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setGuildHonor(targetValue);
			rolePo.sendUpdateGuildInfor();
			sendSystemWorldChat(rolePo.getName()+"任性的把自己公会战功设置成了"+targetValue);
		}
		// 设置宠物魔魂
		if(message.startsWith("setPetSoul ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setPetSoul(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"任性的把自己宠物魔魂设置成了"+targetValue);
		}
		// 设置技能点
		if(message.startsWith("setSkillPonit ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 99999999);
			rolePo.setSkillPoint(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"任性的把自己技能点设置成了"+targetValue);
		}
		
		if(message.startsWith("resetguildboss")){
			GuildService guildService = (GuildService) BeanUtil.getBean("guildService");
			guildService.checkResetGuildBossInfo();
			sendSystemWorldChat(rolePo.getName()+"重置了公会boss");
		}
		
		// 重置运镖次数
		if(message.startsWith("resetyundartcount")){
			rolePo.listYunDartTaskInfoVo.get(0).dailyCurrentFinishYunDartCount=0;
			rolePo.listYunDartTaskInfoVo.get(0).dailyCurrentFreeFlushYunDartCarCount =0;
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId = -1;
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality=-1;
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime=0;
			rolePo.fetchDailyCurrentFinishYunDartCount();
			rolePo.fetchDailyCurrentFreeFlushYunDartCarCount();
			rolePo.fetchCurrentYunDartCarId();
			rolePo.fetchCurrentYunDartCarQuality();
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"重置了运镖次数");
		}
		
		// 重置副本次数
		if(message.startsWith("resetCopySceneCount")){
			rolePo.checkFreshListCopySceneTodayVisitTimes();
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"重置了副本次数");
		}
		// 设置竞技场挑战次数
		if(message.startsWith("setArenaCount ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 10);
			rolePo.setArenaTodayPlayedTimes(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"设置竞技场挑战次数："+targetValue);
		}
		// 设置幽冥之境挑战次数
		if(message.startsWith("setExpCount ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 10);
			boolean flag = true;
			for(IdNumberVo3 idNumberVo3 : rolePo.listCopySceneTodayVisitTimes){
				if(idNumberVo3.getInt1()== CopySceneType.COPYSCENE_EXP){
					idNumberVo3.setInt3(targetValue);
					flag = false;
					break;
				}
			}
			if(flag){
				rolePo.listCopySceneTodayVisitTimes.add(new IdNumberVo3( CopySceneType.COPYSCENE_EXP,0,targetValue,0));				
			}
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"设置幽冥之境挑战次数："+targetValue);
		}
		//设置通天塔挑战次数
		if(message.startsWith("setDotaCount ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 10);
			rolePo.setTowerTodayChallengeTimes(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"设置通天塔挑战次数："+targetValue);
		}
		//设置通天塔扫荡次数
		if(message.startsWith("setDotaMopUpCount ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 0, 10);
			rolePo.setTowerTodayWipeOutTimes(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"设置通天塔扫荡次数："+targetValue);
		}
		
		// addItems XX@XX@XX 添加多个物品id号要连着
		if(message.startsWith("addItems ")){
			String pars = StringUtil.split(message, " ")[1];
			String[] vals = StringUtil.split(pars,"@");
			int begin = Integer.parseInt(vals[0]);
			int end = Integer.parseInt(vals[1]);
			int num = Integer.parseInt(vals[2]);
			for(int i = begin; i <= end; i++){
				rolePo.addItem(i,num,0);
				ItemPo itemPo = ItemPo.findEntity(i);
				sendSystemWorldChat(rolePo.getName()+"任性的给自己增加了"+num+"个"+itemPo.getName());
			}
			rolePo.sendUpdateMainPack(true);
		}
		// 添加宠物
		if(message.startsWith("pet ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 100);
			rolePo.addPetPublic(targetValue);
			rolePo.sendUpdateRole();
			PetPo pet = PetPo.findEntity(targetValue);
			sendSystemWorldChat(rolePo.getName()+"作弊添加了："+pet.getName());
		}
		if(message.startsWith("pets ")){
			String parString = StringUtil.split(message, " ")[1];
			String[] pets = StringUtil.split(parString, ",");
			for (int i = 0; i < pets.length; i++) {
				int petId = Integer.valueOf(pets[i]);
				rolePo.addPetPublic(petId);
				rolePo.sendUpdateRole();
				PetPo pet = PetPo.findEntity(petId);
				sendSystemWorldChat(rolePo.getName()+"作弊添加了："+pet.getName());
			}
		}
		// 设置公会背包物品数量
		if(message.startsWith("addGuildItem ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 9999);
			checkService.checkExisGuildPo(rolePo.getGuildId());
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
			for(IdNumberVo inv : guildPo.listItemPack){
				inv.setNum(targetValue);
			}
			sendSystemWorldChat(rolePo.getName()+"作弊添加了："+targetValue +"公会资源");
		}
		if (message.startsWith("newbie ")) {
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			rolePo.setNewbieStepGroup(targetValue);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己改了新手流程到"+targetValue);
		}		
		
		if(message.startsWith("finishtask")){
			for (IdNumberVo2 idNumberVo : rolePo.listRoleTasks) {
				idNumberVo.setInt2(99999999);
			}
			for (IdNumberVo2 idNumberVo : rolePo.listRoleAchievesTasks) {
				idNumberVo.setInt2(99999999);
			}
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操完成了所有任务");
		}
		if(message.equals("activity")){
			rolePo.sendUpdateRoleActivitysList();
		}
		if(message.equals("yundartbegin")){
			long nextTime = System.currentTimeMillis()+ 60*60*1000;
			YunDartTaskBeginJob yunDartTaskBeginJob = new YunDartTaskBeginJob();
			yunDartTaskBeginJob.beginYunDartTaskDispose( CopySceneType.COPY_SCENE_CONF_YUN_DART, nextTime);
		}
		if(message.equals("yundartend")){
			long nextTime = System.currentTimeMillis()+ 60*60*1000;
			YunDartTaskEndJob yunDartTaskEndJob = new YunDartTaskEndJob();
			yunDartTaskEndJob.endYunDartTaskEndDispose(CopySceneType.COPY_SCENE_CONF_YUN_DART, nextTime);
		}
		
		if(message.equals("opensystem")){
			GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<OpenfunctionPo> openfunctionPoList =gameDataTemplate.getDataList(OpenfunctionPo.class);
			rolePo.openSystemArrayList.clear();
			for(int i=0;i<openfunctionPoList.size();i++){
				OpenfunctionPo openfunctionPo = openfunctionPoList.get(i);
				rolePo.openSystemArrayList.add(openfunctionPo.getOpenFunction());
				if(openfunctionPo.getOpenFunction().intValue()==TaskType.OPEN_SYSTEM_SOUL){
					SoulService soulService = SoulService.instance();
					rolePo.soulAtbMap = soulService.buildSoulAtb(null);
					rolePo.calculateBat(1);
					if(rolePo.getSoulAtb()==null){
						rolePo.setSoulType(IntUtil.getRandomInt(1, 5));
					}
					if(rolePo.getSoul()==null){
						rolePo.setSoul(0);
					}
				}
			}
			rolePo.sendUpdateOpenSystem();	
//			System.out.println(rolePo.openSystemArrayList);
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己开启所有功能");
		}
		
		// 重置所有属性
		if(message.equals("initcreateroleattribute")){
			rolePo.initCreateRoleAttribute();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己重置所有属性");
		}
		
		// 重新刷boss
		if(message.equals("freshallboss")){
			
			List<ScenePo> scenePos = GameDataTemplate.getDataList(ScenePo.class);
			for (ScenePo scenePo : scenePos) {
				MapRoom mapRoom =MapWorld.findStage(scenePo.getId());
				if(mapRoom!=null){
					for (MonsterFreshInforVo monsterFreshInforVo : mapRoom.monsterFreshInforVos) {
						if(MonsterPo.findEntity(monsterFreshInforVo.monsterFreshPo.getMonsterId()).getMonsterType()==MonsterType.MONSTER_TYPE2 && monsterFreshInforVo.disapperTime!=0l){
							monsterFreshInforVo.disapperTime=System.currentTimeMillis()-1000*60*60*24*7;
						}
					}
				}
			}			
			
			sendSystemWorldChat(rolePo.getName()+"没有节操的刷出了所有BOSS");
		}
		
		// 竞技场结算
		if(message.equals("sendarenaaward")){
			MapService mapService = (MapService) BeanUtil.getBean("mapService");
			mapService.sendArenaAward();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给发竞技场奖励了");
		}

		// 开启副本
		if(message.equals("opencopysceneconf")){
			long currentTime = System.currentTimeMillis()+60*60*1000;
			BloodMagicFortressBeginJob bmfbj = new BloodMagicFortressBeginJob();
			bmfbj.beginBloodMagicFortressDispose(CopySceneType.COPY_SCENE_CONF_BLOODMAGICFORTRESS, currentTime);
			EvilSoulForbiddenSpaceBeginJob esfsbj = new EvilSoulForbiddenSpaceBeginJob();
			esfsbj.beginEvilSoulForbiddenSpaceDispose(CopySceneType.COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE, currentTime);
			KillingTowerBeginJob ktbj = new KillingTowerBeginJob();
			ktbj.beginKillingTowerDispose(CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START, currentTime);
			KingOfPkBeginJob kopbj = new KingOfPkBeginJob();
			kopbj.beginKingOfPkDispose(CopySceneType.COPY_SCENE_CONF_KINGOFPK, currentTime);
			MonsterCrisisBeginJob mcbj = new MonsterCrisisBeginJob();
			mcbj.beginMonsterCrisisDispose( CopySceneType.COPY_SCENE_CONF_MONSTERCRISIS, currentTime);
			YunDartTaskBeginJob ydtbj = new YunDartTaskBeginJob();
			ydtbj.beginYunDartTaskDispose( CopySceneType.COPY_SCENE_CONF_YUN_DART, currentTime);

		}
		
		if(message.equals("openguildbidsiege")){
			long currentTime = System.currentTimeMillis()+60*60*1000;
			GuildBidSiegeBeginJob gbsbj = new GuildBidSiegeBeginJob();
			gbsbj.beginGuildBidSiegeContextDispose(CopySceneType.COPY_SCENE_CONF_GUILD_BID, currentTime);
			sendSystemWorldChat(rolePo.getName()+"开启公会竞标");
		}
		
		if(message.equals("closeguildbidsiege")){
			long currentTime = System.currentTimeMillis()+60*60*1000;
			GuildBidSiegeEndJob gbsej = new GuildBidSiegeEndJob();
			gbsej.endGuildBidSiegeDispose(CopySceneType.COPY_SCENE_CONF_GUILD_BID, currentTime);
			sendSystemWorldChat(rolePo.getName()+"关闭公会竞标");
		}
		
		// 修改任务 任务编号@完成数量
		if(message.startsWith("updatetask ")){
			String pars = StringUtil.split(message, " ")[1];
			String[] vals = StringUtil.split(pars,"@");
			Integer id = Integer.valueOf(vals[0]);
			Integer num = Integer.valueOf(vals[1]);
			TaskPo taskPo = TaskPo.findEntity(id);
			if(taskPo == null){
				sendSystemWorldChat(id + "  任务编号错误");
			}
			for(IdNumberVo2 id2 : rolePo.listRoleTasks){
				if(id2.getInt1().intValue() == id.intValue()){
					id2.setInt2(num);
					rolePo.addItemList(taskPo.listTaskAwardId,1,GlobalCache.fetchLanguageMap("key2612"));
					rolePo.sendUpdateTaskProgress(id2.getInt1(), id2.getInt2());
					sendSystemWorldChat(rolePo.getName()+"修改 "+taskPo.getName()+" 任务成功！");
					break;
				}
			}
			for(IdNumberVo2 id2 : rolePo.listRoleAchievesTasks){
				if(id2.getInt1().intValue() == id.intValue()){
					id2.setInt2(num);
					rolePo.addItemList(taskPo.listTaskAwardId,1,GlobalCache.fetchLanguageMap("key2612"));
					rolePo.sendUpdateTaskProgress(id2.getInt1(), id2.getInt2());
					sendSystemWorldChat(rolePo.getName()+"修改 "+taskPo.getName()+" 任务成功！");
					break;
				}
			}
			sendSystemWorldChat(rolePo.getName()+"修改 "+taskPo.getName()+" 任务失败！");
		}
		
		// 添加好友
		if(message.startsWith("addfrend ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 99);
			RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
			List<Integer> list = roleDAO.fetchAllRoleIds(0,0,100,100);
			int num = 0;
			List<RoleInforPo> friendList = new ArrayList<RoleInforPo>();
LableA:for(Integer i : list){
				if(num >= targetValue){
					break;
				}
				RolePo friendRolePo = RolePo.findEntity(i);
				friendRolePo.syncToInfor();
				RoleInforPo roleInfoPo = RoleInforPo.findEntity(friendRolePo.getRoleInforId());
				if(roleInfoPo != null && roleInfoPo.getRoleId().intValue() != rolePo.getId()){
					for(RoleInforPo rp : rolePo.listFriends){
						if(rp.getRoleId().intValue() == roleInfoPo.getRoleId().intValue()){
							continue LableA;
						}
					}
					friendList.add(roleInfoPo);			
					num++;
				}
			}
			if(friendList.size() != 0){
				for(RoleInforPo rp : friendList){
					rolePo.listFriends.add(rp);
				}
			}
			sendSystemWorldChat(rolePo.getName()+"给自己加了"+friendList.size()+"个好友！");
		}
		
		// 添加公会成员
		if(message.startsWith("addguildmember ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 99);
			RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
			List<Integer> list = roleDAO.fetchAllRoleIds(0,0,100,100);
			int num = 0;
			List<Integer> guildMemberList = new ArrayList<Integer>();
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
			if(guildPo == null){
				ExceptionUtil.throwConfirmParamException("没有公会");
			}
			LableA:for(Integer i : list){
				if(num >= targetValue){
					break;
				}
				RolePo memberPolePo = RolePo.findEntity(i);
				if(rolePo == null || memberPolePo.getGuildId() != 0){
					continue LableA;
				}
				
				if( i != rolePo.getId()){
					for(GuildMemberVo gmv : guildPo.listMembers){
						if(gmv.roleId.intValue() == i.intValue()){
							continue LableA;
						}
					}
					guildMemberList.add(i);			
					num++;
				}
			}
			if(guildMemberList.size() != 0){
				for(Integer i : guildMemberList){
					guildPo.addMember(i);
				}
			}
			sendSystemWorldChat(rolePo.getName()+"给自"+guildPo.getName()+"公会加了"+guildMemberList.size()+"个会员！");
			
			
		}
		
		
		if(message.equals("igod")){
			rolePo.setLv(99);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_GOLD);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_DIAMOND);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_BIND_GOLD);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_BIND_DIAMOND);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_GUILD_HONOR);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_PETSOUL);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_SKILL_POINT);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_PETSOUL);
			
			
			
			for (Object itemPo : GameDataTemplate.gameDataListCache.get("ItemPo")) {
				ItemPo obj = (ItemPo) itemPo;
				if(obj.getMatchClass().intValue()==rolePo.getCareer().intValue() && obj.getQuality().intValue() == 5 && obj.getItemLv().intValue() == 2){
					rolePo.addItem(obj.getId(), 1,0);
				}
			}
			
			rolePo.setWingEquipStatus(1);
			rolePo.setWingWasHidden(1);
			rolePo.setWingStar(109);
			
			rolePo.listSlotSouls.clear();
			 for (Integer slot : RoleType.EQUIP_SLOTS) {
				SlotSoulVo slotSoulVo = new SlotSoulVo();
				slotSoulVo.slotNum=slot;
				slotSoulVo.slotQuality = 6;
				slotSoulVo.extract1BatType=25;
				slotSoulVo.extract1BatVal=45;
				slotSoulVo.extract1Quality=6;
				slotSoulVo.extract1Star=15;
				slotSoulVo.extract1Status= SlotSoulType.EXTRACT_STATUS_UNLOCK;
				slotSoulVo.extract2BatType=25;
				slotSoulVo.extract2BatVal=45;
				slotSoulVo.extract2Quality=6;
				slotSoulVo.extract2Star=15;
				slotSoulVo.extract2Status= SlotSoulType.EXTRACT_STATUS_UNLOCK;
				slotSoulVo.extract3BatType=25;
				slotSoulVo.extract3BatVal=45;
				slotSoulVo.extract3Quality=6;
				slotSoulVo.extract3Star=15;
				slotSoulVo.extract3Status= SlotSoulType.EXTRACT_STATUS_UNLOCK;
				if(rolePo.getCareer() == 3){
					slotSoulVo.gem1Id = 300006020;
				}else{
					slotSoulVo.gem1Id = 300006010;
				}
				slotSoulVo.gem2Id = 300006080;
				slotSoulVo.gem3Id = 300006040;
				slotSoulVo.gem4Id = 300006030;
				slotSoulVo.powerLv = 15;
				rolePo.listSlotSouls.add(slotSoulVo);
			}
			for (IdNumberVo idNumberVo : rolePo.listSkillVos) {
					idNumberVo.setNum(99);
			}
			GameDataTemplate gameDataTemplate=(GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			List<OpenfunctionPo> openfunctionPoList =gameDataTemplate.getDataList(OpenfunctionPo.class);
			rolePo.openSystemArrayList.clear();
			for(int i=0;i<openfunctionPoList.size();i++){
				OpenfunctionPo openfunctionPo = openfunctionPoList.get(i);
				rolePo.openSystemArrayList.add(openfunctionPo.getOpenFunction());
			}
			rolePo.sendUpdateOpenSystem();					

			
			rolePo.listSkillFormation.clear();
			if(rolePo.getCareer().intValue() == RoleType.CAREER_MAGE)
			{
				rolePo.listSkillFormation.add(130002);
				rolePo.listSkillFormation.add(130003);
				rolePo.listSkillFormation.add(130004);
				rolePo.listSkillFormation.add(130005);
				rolePo.listSkillFormation.add(130006);
			}
			else if(rolePo.getCareer().intValue() == RoleType.CAREER_RANGER)
			{
				rolePo.listSkillFormation.add(120002);
				rolePo.listSkillFormation.add(120003);
				rolePo.listSkillFormation.add(120004);
				rolePo.listSkillFormation.add(120005);
				rolePo.listSkillFormation.add(120006);
			}
			else if(rolePo.getCareer().intValue() == RoleType.CAREER_WARRIOR)
			{
				rolePo.listSkillFormation.add(110002);
				rolePo.listSkillFormation.add(110003);
				rolePo.listSkillFormation.add(110004);
				rolePo.listSkillFormation.add(110005);
				rolePo.listSkillFormation.add(110006);
			}
			
			// 称号
			rolePo.setTitleLv(12);
			 
			// 军衔
			for(IdNumberVo inv : rolePo.listMilitaryRankRecord){
				inv.setNum(1);
			}
			rolePo.setCurrentMilitaryRankId(12);
			
			// 增加宠物
			rolePo.addPetPublic(26);
			rolePo.addPetPublic(27);
			rolePo.addPetPublic(28);
			rolePo.addPetPublic(29);
			rolePo.addPetPublic(30);
			rolePo.addPetPublic(31);
			rolePo.addPetPublic(32);
			rolePo.addPetPublic(33);
			rolePo.addPetPublic(34);
			rolePo.addPetPublic(35);
			rolePo.sendUpdateRole();
			rolePo.addItem(300099007, 99, 0);
			rolePo.addItem(300099008, 9999, 0);
			
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己增加了最强装备");
			rolePo.calculateBat(1);
		}
		
		if(message.startsWith("petskill")){
			RpetPo rpetPo = RpetPo.findEntity(rolePo.getRpetFighterId());
			if(rpetPo != null){
				rpetPo.skillIds.clear();
				rpetPo.skillIds.add(11);
				rpetPo.skillIds.add(12);
				rpetPo.skillIds.add(13);
				rpetPo.skillIds.add(14);
				rpetPo.skillIds.add(19);
				rpetPo.skillIds.add(20);
				rpetPo.updateSkillPlace();
				sendSystemWorldChat(rolePo.getName()+"没有节操的给宠物加强了宠物");
			}else{
				sendSystemWorldChat(rolePo.getName()+": 当前没有出战的宠物");
			}
		}
		
		if(message.startsWith("igodequip")){
			rolePo.equipWeapon.setPowerLv(20);
			rolePo.equipArmor.setPowerLv(20);
			rolePo.equipRing.setPowerLv(20);
			rolePo.equipBracer.setPowerLv(20);
			rolePo.equipNecklace.setPowerLv(20);
			rolePo.equipHelmet.setPowerLv(20);
			rolePo.equipShoe.setPowerLv(20);
			rolePo.equipBracelet.setPowerLv(20);
			rolePo.equipBelt.setPowerLv(20);
			rolePo.equipPants.setPowerLv(20);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己装备强化到了最高等级");
			rolePo.calculateBat(1);
		}
		
		if(message.startsWith("businessdemand")){
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_GOLD);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_DIAMOND);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_BIND_GOLD);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_BIND_DIAMOND);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_GUILD_HONOR);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_PETSOUL);
			rolePo.adjustNumberByType(9000000, RoleType.RESOURCE_SKILL_POINT);	

			for (Object itemPo : GameDataTemplate.gameDataListCache.get("ItemPo")) {
				ItemPo obj = (ItemPo) itemPo;
				if(obj.getMatchClass().intValue()==rolePo.getCareer().intValue() && obj.getQuality().intValue() == 4 && obj.getItemLv() == 20){
					rolePo.addItem(obj.getId(), 1,1);
				}
			}
			
			for(int i = 300001001; i <= 300001010; i++){
				rolePo.addItem(i,900,0);
			}
			for(int i = 300006001; i <= 300006090; i++){
				if(i%10 == 0){
					rolePo.addItem(i,90,0);					
				}
			}
			rolePo.addItem(300005001,900,0);
			rolePo.addItem(300005002,900,0);
			rolePo.addItem(300005003,900,0);
			rolePo.addItem(300005004,900,0);
			
			rolePo.addItem(300099005,900,0);
			rolePo.addItem(300099006,900,0);
			rolePo.addItem(300099007,900,0);
			rolePo.addPetPublic(30);
			rolePo.setAchievePoint(900000);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"添加商务需求数据");
		}
		
		
		if(message.startsWith("addMember ")){
			String pars = StringUtil.split(message, " ")[1];
			int num = Integer.parseInt(pars);
			CheckUtil.checkInValueRangess(num, 1, 99);
			checkService.checkExisGuildPo(rolePo.getGuildId());
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
//			select id from mmo_db.u_po_role  where guild_id =0 and lv > 1 limit 10;
			List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where guild_id =0 and lv > 1 limit "+num);
			Iterator it = rows.iterator();
			while(it.hasNext()) {
				Map userMap = (Map) it.next();  
				int tagerRoleId = Integer.valueOf(userMap.get("id").toString());
//				System.out.println("tagerRoleId==" +tagerRoleId);
				guildPo.addMember(tagerRoleId);
			} 
			sendSystemWorldChat(rolePo.getName()+"没有节操的给自己公会增加了"+num+"会员");
		}
		
		
		
		if(message.startsWith("item ")){
			String pars = StringUtil.split(message, " ")[1];
			List<IdNumberVo> items = IdNumberVo.createList(pars,"@",",");
			outer: for (IdNumberVo idNumberVo : items) {
				ItemPo theItemPo = ItemPo.findEntity(idNumberVo.getId());
				if(theItemPo.getAutoUse()==1 && idNumberVo.getNum()>100){
					for (List<Integer> type : theItemPo.listItemUseExp) {
						if(type.get(0).intValue()==ItemType.ITEM_USE_EFFECT_ITEM_DROP){
							sendSystemWorldChat(rolePo.getName()+"这个贪婪的家伙要的太多了[1-100]");
							continue outer;
						}
					}
				}
				rolePo.addItem(idNumberVo.getId(), idNumberVo.getNum(),0);
			}

			rolePo.sendUpdateMainPack(true);
			ItemPo itemPo = ItemPo.findEntity(items.get(0).getId());
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加了"+items.get(0).getNum()+"个"+itemPo.getName());
		}
		if(message.startsWith("nameitem ")){
			String pars = StringUtil.split(message, " ")[1];
			String par1=StringUtil.split(pars, "@")[0];
			int par2= Integer.parseInt(StringUtil.split(pars, "@")[1]);
			outer:for (Object itemPo : GameDataTemplate.gameDataListCache.get("ItemPo")) {
				ItemPo obj = (ItemPo) itemPo;
				if(obj.getName().equals(par1)){
					
					if(obj.getAutoUse()==1 && par2>100){
						for (List<Integer> type : obj.listItemUseExp) {
							if(type.get(0).intValue()==ItemType.ITEM_USE_EFFECT_ITEM_DROP){
								sendSystemWorldChat(rolePo.getName()+"这个贪婪的家伙要的太多了[1-100]");
								continue outer;
							}
						}
					}
					
					rolePo.addItem(obj.getId(),par2,0);
				}
			}
			rolePo.sendUpdateMainPack(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+par1+par2+"个");
		}
		

		if(message.startsWith("allequips ")){
			String pars1 = StringUtil.split(message, " ")[1];
			String pars2 = StringUtil.split(message, " ")[2];
			for (Object itemPo : GameDataTemplate.gameDataListCache.get("ItemPo")) {
				ItemPo obj = (ItemPo) itemPo;
				if(obj.getMatchClass()==rolePo.getCareer().intValue()&&obj.getItemLv()==Integer.valueOf(pars1).intValue()&&obj.getQuality()==Integer.valueOf(pars2).intValue()){
					rolePo.addItem(obj.getId(), 1,0);
				}
			}
			rolePo.sendUpdateMainPack(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加了所有装备");
		}
		if(message.startsWith("gold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 99999999);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_GOLD);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_DIAMOND);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_BIND_GOLD);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_BIND_DIAMOND);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_GUILD_HONOR);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_PETSOUL);
			rolePo.adjustNumberByType(targetValue, RoleType.RESOURCE_SKILL_POINT);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己增加"+targetValue+"了绑金、金币、绑钻、钻石、公会荣誉、宠物魔魂、技能点");
		}
		if(message.startsWith("setGold ")){
			String pars = StringUtil.split(message, " ")[1];
			Integer targetValue = Integer.valueOf(pars);
			CheckUtil.checkInValueRangess(targetValue, 1, 99999999);
			rolePo.setGold(targetValue);
			rolePo.setBindGold(targetValue);
			rolePo.setDiamond(targetValue);
			rolePo.setBindDiamond(targetValue);
			rolePo.setGuildHonor(targetValue);
			rolePo.setPetSoul(targetValue);
			rolePo.setSkillPoint(targetValue);
			rolePo.sendUpdateTreasure(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己设置"+targetValue+"了绑金、金币、绑钻、钻石、公会荣誉、宠物魔魂、技能点");
		}
		if(message.startsWith("cleanPack")){
			rolePo.mainPackItemVosMap.clear();
			rolePo.sendUpdateMainPack(true);
			sendSystemWorldChat(rolePo.getName()+"清空背包");
		}
		if(message.startsWith("task ")){
			String pars = StringUtil.split(message, " ")[1];
			int taskId = Integer.parseInt(pars);
			IdNumberVo2 idNumberVo = rolePo.fetchRoleTask(taskId);
			if(idNumberVo == null){
				List<IdNumberVo2> list = new ArrayList<IdNumberVo2>();
				for(IdNumberVo2 inv2 : rolePo.listRoleTasks){
					TaskPo tp = TaskPo.findEntity(inv2.getInt1());
					if(tp.getTaskType().intValue() == 1){
						list.add(inv2);
					}
				}
				for(IdNumberVo2 inv2 : list){
					rolePo.listRoleTasks.remove(inv2);
				}
			}
			rolePo.activeRoleTask(taskId);
			idNumberVo = rolePo.fetchRoleTask(taskId);
			idNumberVo.setInt2(TaskType.TASK_STATUS_ACCEPTED);
			rolePo.sendUpdateRoleTasks(true);
//			System.out.println("==" + rolePo.listRoleTasks);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己加了任务");
		}
		if(message.startsWith("finishtask ")){
			String pars = StringUtil.split(message, " ")[1];
			int taskId = Integer.parseInt(pars);
			IdNumberVo2 idNumberVo = rolePo.fetchRoleTask(taskId);
			TaskPo taskPo = TaskPo.findEntity(taskId);
			if(taskPo!=null){
				if(idNumberVo == null){
					rolePo.listRoleTasks.add(new IdNumberVo2(taskId, -1, 0));
					 idNumberVo = rolePo.fetchRoleTask(taskId);
				}				
			}
			if(idNumberVo == null){
				return;
			}
			if(taskPo.conditionVals.size() ==2){
				idNumberVo.setInt2(taskPo.conditionVals.get(1));
			}
			else if(taskPo.conditionVals.size() ==3)
			{
				idNumberVo.setInt2(taskPo.conditionVals.get(2));
			}
			rolePo.sendUpdateRoleTasks(true);
			sendSystemWorldChat(rolePo.getName()+"任性的给自己完成了任务");
		}
		if(message.startsWith("tower ")){
			String pars = StringUtil.split(message, " ")[1];
			int towerCurrentLv = Integer.parseInt(pars);
			CheckUtil.checkInValueRangess(towerCurrentLv, 1, 99);
			rolePo.setTowerCurrentLv(towerCurrentLv);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+"任性的吧自己dota等级设置为了"+pars);
		}
		
		// 模拟每周任务
		if(message.startsWith("weeklyJob")){
			GuildService guildService = (GuildService) BeanUtil.getBean("guildService");
			guildService.checkGuildGoldWeeklyDeduct();
			sendSystemWorldChat(rolePo.getName()+"执行了weeklyJob");
		}
		
		if(message.startsWith("sendmail ")){
			String[] pars = StringUtil.split(message, " ");
			 MailService mailService = (MailService) BeanUtil.getBean("mailService");
			 StringBuilder sb = new StringBuilder();
			 
			 for(int i = 1; i < pars.length; i++){
				 String[] par=StringUtil.split(pars[i], "@");
				 sb.append(par[0]);
				 sb.append("|");
				 sb.append(par[1]);
				 sb.append("|");
				 sb.append(par[2]);
				 if((i+1) != pars.length){
					 sb.append(",");					 
				 }
			 }
//			 System.out.println("sb == " +sb.toString());
			 mailService.sendSystemMail("系统", rolePo.getId(), null, "作弊命令邮件", sb.toString(), MailType.MAIL_TYPE_SYSTEM);
		}
		
		//重置累计登录奖励
		if(message.startsWith("resetCumulativeLoginAward ")){
			String pars = StringUtil.split(message, " ")[1];
			int days = Integer.parseInt(pars);
			CheckUtil.checkInValueRangess(days, 1, 7);
			if(days > 7){
				days = 7;
			}
			rolePo.checkCumulativeLoginDays(days);
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+" 重置累计登录奖励");
		}
		
		// 重置等级奖励
		if(message.startsWith("resetlistLevelAward")){
			rolePo.listLevelAwardRecord.clear();
			rolePo.checkInitializeLevelAwardRecord();
			rolePo.checkLevelAwardRecordState();
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+" 重置等级奖励");
		}
		
		if(message.startsWith("diamondbasins")){
			rolePo.fetchDiamondBasins();
			rolePo.sendUpdateRole();
			sendSystemWorldChat(rolePo.getName()+" 获取摇一摇奖励");
		}
		
		if(message.startsWith("disconnect")){
			sendSystemWorldChat(rolePo.getName()+" 断线");
			SessionUtil.getCurrentSession().disconnect();
		}
		
		
		if(message.equals("test")){
			for (int i = 0; i <= 99999; i++) {
				rolePo.sendUpdateMainPack(true);
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			sendSystemWorldChat("这下牛逼了!");
		}
		
		if(message.equals("shutdownreset")){
			ServerService serverService = (ServerService) BeanUtil.getBean("serverService");
			serverService.stopService(true);
			sendSystemWorldChat("服务器开始停机清理数据,大约持续1分钟");
		}	
		if(message.equals("shutdown")){
			ServerService serverService = (ServerService) BeanUtil.getBean("serverService");
			serverService.stopService(false);
			sendSystemWorldChat("服务器开始重启");
		}
	}
	


	public String buildTag(int bigType, int realId, Integer itemId, Integer equipId) {     
		return "["+bigType+","+realId+","+itemId+","+equipId+"]";
	}
	
	/**
	 * 系统频道
	 */
	public void sendSystemWorldChat(String message) {
		playerSendChat(null,message,ChatType.CHAT_CHANNEL_SYSTEM,ChatType.CHAT_TYPE_CHAT,0);
	}
	
	public void sendWorldFromSystemChat(String message){
		playerSendChat(null,message,ChatType.CHAT_CHANNEL_WORLD,ChatType.CHAT_TYPE_CHAT,0);
	}
	
	/**
	 * 跑马灯
	 * @param content
	 */
	public void sendHorse(String content) {
		playerSendChat(null, content, ChatType.CHAT_CHANNEL_SYSTEM,ChatType.CHAT_TYPE_HORSE,0);
	}

	/**
	 * 公会聊天
	 * @param content
	 * @param guildId
	 */
	public void sendGuild(String content, Integer guildId){
		playerSendChat(null, content, ChatType.CHAT_CHANNEL_GUILD,guildId,0);
	}
	
	/**
	 * 队伍聊天
	 * @param content
	 * @param teamId
	 */
	public void sendTeam(String content, Integer teamId){
		playerSendChat(null, content, ChatType.CHAT_CHANNEL_TEAM,teamId,0);
	}
	
	/**
	 * 房间聊天
	 * @param content
	 * @param roomId
	 */
	public void sendRoom(String content, Integer roomId){
		playerSendChat(null, content, ChatType.CHAT_CHANNEL_ROOM,roomId,0);
	}
	
	/**
	 * 系统提示（个人）
	 * @param content
	 * @param roomId
	 */
	public void sendSystemMsg(String content, Integer roleId){
		playerSendChat(null, content, ChatType.CHAT_CHANNEL_ONLY_SELF,roleId,0);
	}
	
	/**
	 * 好友频道
	 * @param content
	 * @param rolePo
	 */
	public void sendGoodFriendMsg(String content, RolePo rolePo){
		playerSendChat(rolePo, content, ChatType.CHAT_CHANNEL_GOOD_FRIEND,rolePo.getId(),0);
	}
	
	/**
	 * 加入房间
	 * @param roomId
	 * @param iuid
	 * @param ioSession
	 */
	public void joinRoomChannel(Integer roomId, String iuid,ChannelHandlerContext ioSession, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ChannelHandlerContext>> chatRooms ) {
		if(roomId==null || ioSession == null || chatRooms == null){
			return;
		}
		
		ConcurrentHashMap<String,ChannelHandlerContext> theChat = chatRooms.get(roomId);
		if(theChat==null){
			theChat = new ConcurrentHashMap<String, ChannelHandlerContext>();
			chatRooms.put(roomId,theChat);
		}
		theChat.put(iuid, ioSession);
	}
	
	/**
	 * 离开房间
	 * @param roomId
	 * @param iuid
	 * @param ioSession
	 */
	public void leaveRoomChannel(Integer roomId, String iuid,ChannelHandlerContext ioSession, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ChannelHandlerContext>> chatRooms) {
		if(roomId==null || chatRooms == null){
			return;
		}

		ConcurrentHashMap<String,ChannelHandlerContext> theChat = chatRooms.get(roomId);
		if(theChat==null){
			return;
		}
		theChat.remove(iuid);
		if(theChat.size()==0){
			chatRooms.remove(roomId);
		}
	}
	
	/**
	 * 删除房间
	 * @param roomId
	 */
	public void roomDimissChannel(Integer roomId, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ChannelHandlerContext>> chatRooms){
		if(roomId==null || chatRooms == null){
			return;
		}
		if(chatRooms.containsKey(roomId)){
			chatRooms.remove(roomId);			
		}
	}

	public void checkBlockChat(RolePo rolePo) {
		for (Object obj : GlobalCache.blockMap.values()) {
			BlockVo blockVo = (BlockVo) obj;
			if(blockVo.roleId==rolePo.getId().intValue()){
				if(System.currentTimeMillis()>=blockVo.startTime && System.currentTimeMillis()<=blockVo.endTime){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key235"));
				}
			}
		}
	}
	private static ChatService instance;
	public static ChatService instance() {
		if(instance==null){
			instance=(ChatService) BeanUtil.getBean("chatService");
		}
		return instance;
	}
	
	
	
	
}
