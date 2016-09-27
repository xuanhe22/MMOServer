package com.games.mmo.remoting;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.games.backend.vo.ExceptionVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.RoleService;
import com.games.mmo.service.ServerService;
import com.games.mmo.thread.ShutDownThread;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.MergeUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.AgreementCountVo;
import com.games.mmo.vo.SessionSaveVo;
import com.games.mmo.vo.ThreadInforVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BaseGenerator;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.entity.MemoryTemplate;
import com.storm.lib.component.remoting.ProtocalObject;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.NettyCache;
import com.storm.lib.component.socket.netty.NettyStatus;
import com.storm.lib.component.socket.netty.NettyType;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.remoting.BaseGameServerRemoting;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.ProtobufUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

/**
 * 
 * 类功能:服务器请求类
 * @author johnny
 * @version 2011-8-7
 */
@Controller
public class ServerRemoting extends BaseGameServerRemoting{
	@Autowired
	private ServerService serverService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleTemplate roleTemplate;
	@Autowired
	private ChatService chatService;

	
	public String syncData(String[]tables,byte[] out,Boolean reCre){
		BaseStormSystemType.isSyncingData=true;
		byte[] out2=StringUtil.unGZip(out);

		ByteArrayInputStream is = new ByteArrayInputStream(out2);
		try {
			XSSFWorkbook workBook = new XSSFWorkbook(is);
			serverService.syncData(tables, workBook,reCre);
			BaseGenerator baseGenerator = (BaseGenerator) BeanUtil.getBean("baseGenerator");
			GlobalCache.loadAndClean();
			
		} catch (Exception e) {
			ExceptionUtil.processException(e);
			String msg=e.getLocalizedMessage();
			return msg;
		}
		finally{
			BaseStormSystemType.isSyncingData=false;
		}
		return "import success";
	}
	
	/**
	 * 获取客户端列表
	 * @return
	 */
	public Object getDicClientList(Integer requireUpdate){
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		SessionUtil.addDataArray(GlobalCache.langClientDic.get(gp.getValueStr()));
		SessionUtil.addDataArray(gp.getValueStr()+"_"+GameDataTemplate.STATIC_DATA_VERSION_KEY);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	public Object getStaticData(Integer requireUpdateGameData){
		List<IdNumberVo> list2 = (List<IdNumberVo>) ((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions)).valueObj;
		if(requireUpdateGameData==2){
			SessionUtil.addDataArray(list2);
			return SessionType.MULTYPE_RETURN;
		}
		if(requireUpdateGameData==3){
			List list = serverService.createList();
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
			SessionUtil.addDataArray(list);
			SessionUtil.addDataArray(XmlCache.xmlFiles);
			SessionUtil.addDataArray(GlobalCache.langClientDic.get(gp.getValueStr()));
			SessionUtil.addDataArray(list2);
			return SessionType.MULTYPE_RETURN;
		}
		if(requireUpdateGameData==1){
			List list = serverService.createList();
			SessionUtil.addDataArray(list);
		}
		else{
			SessionUtil.addDataArray(null);
		}
		
//		List list = serverService.createList();
//		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
//		File file=new File("C:\\Work\\SSDUpload\\release\\data\\static_"+System.currentTimeMillis()+".data");
//		ServerPack serverPack=new ServerPack();
//		serverPack.obj=new Object[]{list,XmlCache.xmlFiles,GlobalCache.langClientDic.get(gp.getValueStr()),list2};
//		String val = JSON.toJSONString(serverPack,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
//		FileWriter fileWritter;
//		try {
//			fileWritter = new FileWriter(file,true);
//	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//	        bufferWritter.write(val);
//	        bufferWritter.close();
//		} catch (IOException e) {
//			ExceptionUtil.processException(e);
//		}
		
		SessionUtil.addDataArray(XmlCache.xmlFiles);
		SessionUtil.addDataArray(GlobalCache.OPEN_TRADE);
		SessionUtil.addDataArray(list2);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	public Object getOrderStringIdMap(){
		SessionUtil.addDataArray(ProtobufUtil.convertToProtobufMap(ProtocalObject.orderStringIdMapping));
//		System.out.println("GameDataTemplate.STATIC_DATA_VERSION_KEY == "+GameDataTemplate.STATIC_DATA_VERSION_KEY);
		SessionUtil.addDataArray(GameDataTemplate.STATIC_DATA_VERSION_KEY);
		SessionUtil.addDataArray(GlobalCache.SERVER_CURRENT_START_KEY);
		return SessionType.MULTYPE_RETURN;
	}

	public Object test(Integer par1,String par2,String par3,Double par4){
		return "johnny";
	}


	public String backShutdown(Integer clean) {
		PrintUtil.print("停服请求收到");
		if(clean==1){
			ShutDownThread thread = new ShutDownThread();
			thread.clean=true;
			thread.start();
		}
		else{
			ShutDownThread thread = new ShutDownThread();
			thread.clean=false;
			thread.start();
		}
		return GlobalCache.fetchLanguageMap("key179");
	}

	
	public Object getTime(){
//		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		if(rolePo!=null){
//			rolePo.copySceneStartState=true;
//			rolePo.sendUpdateCurrentTime();
//		}
		SessionUtil.addDataArray(System.currentTimeMillis());
		return SessionType.MULTYPE_RETURN;
	}

	
	/**
	 * 
	 * 方法功能:支持客户端重连
	 * 更新时间:2014-9-19, 作者:guole
	 * @param iuid
	 * @return 1
	 */
	public Object reconnect(String iuid){
		//连接中重连其他的踢下去
		//TODO 【请求优化】每次都要查询数据库可以优化的,而且iuid没有索引
		RolePo rolePo = roleService.findRoleByIuid(iuid);
		ChannelHandlerContext session = SessionUtil.getCurrentSession();
		if(rolePo == null){
//			System.out.println("reconnect: 没有找到用户："+iuid);
			SessionUtil.addDataArray(GlobalCache.SERVER_CURRENT_START_KEY);
			SessionUtil.addDataArray(0);
			return SessionType.MULTYPE_RETURN;
		}
		SessionSaveVo sessionSaveVo = GlobalCache.sessionSaveVos.get(rolePo.getId());
		if(!GlobalCache.sessionSaveVos.containsKey(rolePo.getId())){
//			System.out.println("reconnect没有key:"+rolePo.getId());
			SessionUtil.addDataArray(GlobalCache.SERVER_CURRENT_START_KEY);
			SessionUtil.addDataArray(0);
			return SessionType.MULTYPE_RETURN;
		}
		if(sessionSaveVo==null){
			SessionUtil.addDataArray(GlobalCache.SERVER_CURRENT_START_KEY);
			SessionUtil.addDataArray(0);
			return SessionType.MULTYPE_RETURN;
		}else{
			if(session!=sessionSaveVo.session){
			}
		}
		
		MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
		boolean invalidRoom=false;
		if(mapRoom == null){
			invalidRoom=true;
			Integer staticRoomId = rolePo.getStaticRoomId();
			if(staticRoomId == null){
				staticRoomId = 20101001; 					
			}
			rolePo.setStaticRoomId(staticRoomId);
			rolePo.setRoomId(staticRoomId);
			ScenePo scenePo = ScenePo.findEntity(MapWorld.findStage(rolePo.getRoomId()).sceneId);
			rolePo.setX(scenePo.getX());
			rolePo.setY(scenePo.getY());
			rolePo.setZ(scenePo.getZ());
		}
		else{
			if(mapRoom.isDynamic){
				DynamicMapRoom room = (DynamicMapRoom) mapRoom;
				if(room.status!=DynamicMapRoom.ACTIVITY_STATUS_OPEN && room.status!=DynamicMapRoom.ACTIVITY_STATUS_PLAY){
					invalidRoom=true;
				}
			}
		}
		
		
		roleTemplate.insertRoleIdIuidMapping(rolePo.getId(),rolePo.getIuid());
		roleTemplate.insertIuidSessionMapping(rolePo.getIuid(), session);
		((AttributeMap) session).attr(NettyType.roleId).set(rolePo.getId());
		((AttributeMap) session).attr(NettyType.rolePo).set(rolePo);
		//session.setAttribute("roleId", rolePo.getId());
		chatService.joinWorldChannel(rolePo.getIuid(), session);
		SessionUtil.addDataArray(GlobalCache.SERVER_CURRENT_START_KEY);
		if(invalidRoom){
			SessionUtil.addDataArray(2);
		}
		else{
			SessionUtil.addDataArray(1);
		}
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object fetchInfor(){
		MemoryTemplate memoryTemplate = (MemoryTemplate) BeanUtil.getBean("memoryTemplate");
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		StringBuilder sb =new StringBuilder();
		sb.append("当前时间: "+DateUtil.getCurrentText()).append("</br>");
		sb.append("当前在线人数: "+RoleTemplate.roleIdIuidMapping.size()).append("</br>");
		
		String sql = "select count(*) from u_po_role where abandom_state is null";
		Integer roleAmount = BaseDAO.instance().jdbcTemplate.queryForInt(sql);
		sb.append("角色生成数量：").append(roleAmount).append("</br>");
		
		sb.append("当前房间数: "+MapWorld.mapRooms.size()).append("</br>");
		sb.append("当前活动房间数: "+MapWorld.activityMapRooms.size()).append("</br>");
		sb.append("当前线程数: "+map.size()).append("</br>");
		sb.append("当前每秒发出消息数: "+MMOByteEncoder.last1SecondTotalMsg).append("</br>");	
		sb.append("当前空闲内存: "+Runtime.getRuntime().freeMemory()/1024/1024+"MB").append("</br>");
		sb.append("当前UserCachePo实体数量: "+memoryTemplate.getObjectInt()).append("</br>");
		sb.append("当前未写入log数: "+LogUtil.log.size()).append("</br>");
		sb.append("当前未同步实体数: "+MemoryTemplate.toSyncPos.size()).append("</br>");
		sb.append("当前错误同步实体数: "+MemoryTemplate.errorPos.size()).append("</br>");	
		sb.append("NettyCache.msgStartTime: "+NettyCache.msgStartTime.size()).append("</br>");	
		sb.append("ByteUtil.intCache.size(): "+ByteUtil.intCache.size()).append("</br>");
		sb.append("ByteUtil.stringCache.size(): "+ByteUtil.stringCache.size()).append("</br>");
		int online=1;
		long secondRecvMsgSize=Math.max(1, NettyStatus.secondRecvMsgSize);
		long secondSentMsgSize=Math.max(1, NettyStatus.secondSentMsgSize);
		online=Math.max(online, RoleTemplate.roleIdIuidMapping.size());
		sb.append("recv/send:"+secondRecvMsgSize+"/"+secondSentMsgSize+" avg process:"+NettyStatus.secondWholeProcessTime/secondRecvMsgSize+"ms flush:"+(NettyStatus.secondWriteAndFlush/secondSentMsgSize)+"ms ->"+online+"</br>");	
		sb.append("<table border='1' style='width:50%'><tr><td >房间Id</td><td >sceneId</td><td >服务器发送消息数量</td><td>服务器发送消息类型</td><td>服务器发送消息类型数量</td></tr>");
		
		
		for(AgreementCountVo agreementCountVo : Cell.countMap.values()){
			ConcurrentHashMap<Integer,IdNumberVo> agreementMap = agreementCountVo.agreementMap;
			sb.append("<tr>");
			sb.append("<td rowspan=").append(agreementMap.size()).append(">").append(agreementCountVo.mapRoomId+"#").append("</td>");
			sb.append("<td rowspan=").append(agreementMap.size()).append(">").append(agreementCountVo.sceneId).append("</td>");
			sb.append("<td rowspan=").append(agreementMap.size()).append(">").append(agreementCountVo.totalCount).append("</td>");
			int index = 0;
			for(IdNumberVo idNumberVo : agreementMap.values()){
				if(index == 0){
					sb.append("<td>").append(idNumberVo.getId()).append("</td>");
					sb.append("<td>").append(idNumberVo.getNum()).append("</td>");
					sb.append("</tr>");					
				}else{
					sb.append("<tr>");
					sb.append("<td>").append(idNumberVo.getId()).append("</td>");
					sb.append("<td>").append(idNumberVo.getNum()).append("</td>");
					sb.append("</tr>");	
				}
				index++;
			}
		}
		sb.append("</table>");
		
		sb.append("<table border='1' style='width:50%'><tr><td >线程</td><td>状态</td><td>Trace</td></tr>");
		
		List<ThreadInforVo> list=new ArrayList<ThreadInforVo>();
		for (Thread thread : map.keySet()) {
			ThreadInforVo threadInforVo=new ThreadInforVo();
			threadInforVo.name=thread.getName();
			threadInforVo.state=thread.getState();
			threadInforVo.trace=thread.getStackTrace();
			list.add(threadInforVo);
//			System.out.println(thread.getThreadGroup().);
		}
		Collections.sort(list);
		for (ThreadInforVo threadInforVo : list) {;
			sb.append("<tr>");
			sb.append("<td>").append(threadInforVo.name).append("</td>");
			sb.append("<td>").append(threadInforVo.state).append("</td>");
			if(threadInforVo.trace.length>0){
				sb.append("<td>").append(threadInforVo.trace[0]).append("</td>");
			}

			sb.append("</tr>");	
		}
		sb.append("</table>");
		return sb.toString();
//		List<Point> keepResult = serverService.systemPartResult(2,"vienan_google",1458835200000l);
//		StringBuilder sb =new StringBuilder();
//		String[] kpiDes1={"新用户(注册)","新用户（有角色）","设备DAU","设备老用户","设备新用户","设备PU","设备新PU","设备PU%","设备DARPU","设备DARPPU","充值（RMB)","消耗（钻石）","平均在线","最高在线","剩余钻石"};
//		
//		for(int i=0;i<keepResult.size();i++){
//			sb.append("-------"+":"+keepResult.get(i).x+"-"+keepResult.get(i).y+"--------");
//		}
//		System.out.println(sb.toString());
//		return sb.toString();

	}
	
	public Object net(Integer filter,Integer limit) throws Exception{
//		NetType.FILTER_LIMIT=limit;
		return "Success Net setting to NetType.FILTER_LIMIT:";
	}
	
	public Object readSetting() throws Exception{
		List<IdNumberVo> list2 = (List<IdNumberVo>) ((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions)).valueObj;
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		GlobalPo gp2 = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum);
		//服务器Id
		GlobalPo gp3 = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyServerId);
		String serverId = "0";
		if(gp3 != null && gp3.valueObj != null){
			serverId = gp3.valueObj.toString();
		}
		Object[] objs =new Object[]{list2,gp.valueObj.toString(),gp2.valueObj.toString(), serverId};
		return objs;
	}   

	/**
	 * 服务器设置（废弃）
	 */
	public String settings(Integer monthCard,Integer auction,Integer LiveActivity,Integer giftCode,String lang,Integer facebook,Integer growth,Integer growthko,Integer rank,Integer shopScore,Integer inviteFriend,Integer charge, Integer shake,Integer onlineMaxNum,Integer battleMsg,Integer oldPlayerReward, Integer luckyTurn){
		GlobalPo globalPo=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions));
//		if(globalPo==null){
//			GlobalPo.keyGlobalPoMap.put(GlobalPo.keyOptions, new GlobalPo());
//		}
		List<IdNumberVo> list=(List<IdNumberVo>) globalPo.valueObj;
    	list.clear();
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthCardOpen,monthCard));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthAuctionOpen,auction));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeLiveActivityOpen,LiveActivity));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGiftCodeOpen,giftCode));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMainFacebook,facebook));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowth,growth));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowthKOVERSION,growthko));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeRank,rank));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShopScore,shopScore));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeInviteFriend,inviteFriend));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeCharge,charge));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShake,shake));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeBattleMsg,battleMsg));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeOldPlayerReward,oldPlayerReward));
    	list.add(new IdNumberVo(GlobalPo.luckyTurn,luckyTurn));
    	GlobalCache.battleMsg=battleMsg;
		String langStr = DBFieldUtil.fetchImpodString(lang);
		if(langStr != null){
			if("zh_cn".equals(langStr) || 
				"vi".equals(langStr) ||
				"zh_tw".equals(langStr) ||
				"ko".equals(langStr)){
				XmlCache.loadAndClean(langStr);
				GameUtil.reloadFilterString();
			}
		}
		GlobalPo globalPo2=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum));
		globalPo2.valueObj=onlineMaxNum;
		return "保存成功";
	} 
	
	/**
	 * 服务器设置(2016-08-18废弃)
	 */
	public String serverSettings(Integer monthCard,Integer auction,Integer LiveActivity,Integer giftCode,String lang,Integer facebook,Integer growth,Integer growthko,Integer rank,Integer shopScore,Integer inviteFriend,Integer charge, Integer shake,Integer onlineMaxNum,Integer battleMsg,Integer oldPlayerReward, Integer luckyTurn, Integer singleStory, Integer kingActivity){
		GlobalPo globalPo=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions));
//		if(globalPo==null){
//			GlobalPo.keyGlobalPoMap.put(GlobalPo.keyOptions, new GlobalPo());
//		}
		List<IdNumberVo> list=(List<IdNumberVo>) globalPo.valueObj;
    	list.clear();
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthCardOpen,monthCard));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthAuctionOpen,auction));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeLiveActivityOpen,LiveActivity));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGiftCodeOpen,giftCode));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMainFacebook,facebook));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowth,growth));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowthKOVERSION,growthko));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeRank,rank));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShopScore,shopScore));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeInviteFriend,inviteFriend));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeCharge,charge));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShake,shake));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeBattleMsg,battleMsg));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeOldPlayerReward,oldPlayerReward));
    	list.add(new IdNumberVo(GlobalPo.luckyTurn,luckyTurn));
    	list.add(new IdNumberVo(GlobalPo.optionTypeSingleStory, singleStory));
    	list.add(new IdNumberVo(GlobalPo.optionTypeKingActivity, kingActivity));
    	GlobalCache.battleMsg=battleMsg;
		String langStr = DBFieldUtil.fetchImpodString(lang);
		if(langStr != null){
			if("zh_cn".equals(langStr) || 
				"vi".equals(langStr) ||
				"zh_tw".equals(langStr) ||
				"ko".equals(langStr)){
				XmlCache.loadAndClean(langStr);
				GameUtil.reloadFilterString();
			}
		}
		GlobalPo globalPo2=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum));
		globalPo2.valueObj=onlineMaxNum;
		return "保存成功";
	}
	
	/**
	 * 服务器设置(GTM)
	 * 2016-08-18 增加服务器ID设置
	 */
	public String serverSettingsWithServerId(Integer monthCard,Integer auction,Integer LiveActivity,Integer giftCode,String lang,Integer facebook,Integer growth,Integer growthko,Integer rank,Integer shopScore,Integer inviteFriend,Integer charge, Integer shake,Integer onlineMaxNum,Integer battleMsg,Integer oldPlayerReward, Integer luckyTurn, Integer singleStory, Integer kingActivity, Integer wingAvatar, String serverId, Integer bossBackroom){
		GlobalPo globalPo=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions));
//		if(globalPo==null){
//			GlobalPo.keyGlobalPoMap.put(GlobalPo.keyOptions, new GlobalPo());
//		}
		List<IdNumberVo> list=(List<IdNumberVo>) globalPo.valueObj;
    	list.clear();
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthCardOpen,monthCard));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMonthAuctionOpen,auction));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeLiveActivityOpen,LiveActivity));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGiftCodeOpen,giftCode));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeMainFacebook,facebook));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowth,growth));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeGrowthKOVERSION,growthko));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeRank,rank));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShopScore,shopScore));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeInviteFriend,inviteFriend));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeCharge,charge));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeShake,shake));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeBattleMsg,battleMsg));
    	list.add(new IdNumberVo(GlobalPo.OptionTypeOldPlayerReward,oldPlayerReward));
    	list.add(new IdNumberVo(GlobalPo.luckyTurn,luckyTurn));
    	list.add(new IdNumberVo(GlobalPo.optionTypeSingleStory, singleStory));
    	list.add(new IdNumberVo(GlobalPo.optionTypeKingActivity, kingActivity));
    	list.add(new IdNumberVo(GlobalPo.optionWingAvatar, wingAvatar));
    	list.add(new IdNumberVo(GlobalPo.optionBossBackroom, bossBackroom));
    	GlobalCache.battleMsg=battleMsg;
		String langStr = DBFieldUtil.fetchImpodString(lang);
		if(langStr != null){
			if("zh_cn".equals(langStr) || 
				"vi".equals(langStr) ||
				"zh_tw".equals(langStr) ||
				"ko".equals(langStr)){
				XmlCache.loadAndClean(langStr);
				GameUtil.reloadFilterString();
			}
		}
		GlobalPo globalPo2=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLoginQueueNum));
		globalPo2.valueObj=onlineMaxNum;
		
		boolean needUpdate = false;
		//设置服务器ID
		if(GlobalPo.keyGlobalPoMap.containsKey(GlobalPo.keyServerId)){
			GlobalPo globalPo3=((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyServerId));
			if(!StringUtil.equal(globalPo3.getValueStr(), serverId)) needUpdate = true;
			globalPo3.setValueStr(serverId);
			globalPo3.valueObj=serverId;
		}else{
			needUpdate = true;
			GlobalPo globalPo3 =new GlobalPo();
			globalPo3.setKeyStr(GlobalPo.keyServerId);
			globalPo3.setValueStr(serverId);
			globalPo3.valueObj=serverId;
			GlobalPo.keyGlobalPoMap.put(GlobalPo.keyServerId, globalPo3);
			BaseDAO.instance().insert(globalPo3);
		}
		//设置了新的severId,需要更新日志数据库中KPI相关数据
		if(needUpdate){
			List<Integer> serverIds = BaseDAO.instance().jdbcTemplate.queryForList("select server_id from "+BaseStormSystemType.LOG_DB_NAME+".summary_remain group by server_id", Integer.class);
			PrintUtil.print("serverIds :"+serverIds);
			if(serverIds.size() == 1){
				BaseDAO.instance().execute("update "+BaseStormSystemType.LOG_DB_NAME+".summary_day set server_id = '"+serverId+"'");
				BaseDAO.instance().execute("update "+BaseStormSystemType.LOG_DB_NAME+".summary_hour set server_id = '"+serverId+"'");
				BaseDAO.instance().execute("update "+BaseStormSystemType.LOG_DB_NAME+".summary_month set server_id = '"+serverId+"'");
				BaseDAO.instance().execute("update "+BaseStormSystemType.LOG_DB_NAME+".summary_remain set server_id = '"+serverId+"'");
			}
		}
		return "保存成功";
	} 
	
	/*
	 * type 1=天kpi 2=小时kpi
	 */
    public Object kpiResult(Integer type,Long startTime){
    	
    	List<Integer> kpiResult = new ArrayList<Integer>();
		try {
			kpiResult = serverService.kpiResult(type,"inner_test",startTime);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
    	return kpiResult;
    }
    
    public Object backTime(){
    	return System.currentTimeMillis();
    }
    
    public Object exportMerge(String targetDb,String sourceDb,Integer targetServerId,String targetServerPrefix1,String targetServerPrefix2,Integer clean){
    	PrintUtil.print("targetDb="+targetDb+"; sourceDb="+sourceDb+"; targetServerId="+targetServerId+"; targetServerPrefix1="+targetServerPrefix1+"; targetServerPrefix2="+targetServerPrefix2+"; clean="+clean);
    	MergeUtil.exportMerge(targetDb,sourceDb,targetServerId,targetServerPrefix1,targetServerPrefix2,clean);
		return "导服成功";
    }

	public Object exception(Long startTime,Long endTime,Integer limit,Integer exceptionType) throws Exception{
		
		List<ExceptionVo> exceptionVos = new ArrayList<ExceptionVo>();
		String path="";
		//all
		if(exceptionType==0){
			path=BaseStormSystemType.PathOutputLogException;
		}
		//regular
		else if(exceptionType==1){
			path=BaseStormSystemType.PathOutputLogExceptionRegular;
		}
		//confirm
		else if(exceptionType==2){
			path=BaseStormSystemType.PathOutputLogExceptionConfirm;
		}
		//Sys
		else if(exceptionType==3){
			path=BaseStormSystemType.PathOutputLogExceptionSys;
		}
		File dirPath = new File(path);
		if(dirPath!=null && dirPath.exists()){
			for (File file : dirPath.listFiles()) {
				if(file.isFile()){
					String[] vals = file.getName().split("_");
					String dateStr=vals[vals.length-1].split("\\.")[0];
					long val = 0;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date=null;
					try {
						date = sdf.parse(dateStr);
					} catch (ParseException e) {
						ExceptionUtil.processException(e);
					}
					date.setHours(0);
					date.setMinutes(0);
					date.setSeconds(0);
					Long startFileTime=date.getTime();
					date.setHours(23);
					date.setMinutes(59);
					date.setSeconds(59);
					Long endFileTime=date.getTime();
					
					if(startTime>=endFileTime || endFileTime<=startFileTime){
						continue;
					}
					
					BufferedReader reader = null;  
			        try {  
//			            System.out.println("以行为单位读取文件内容，一次读一整行：");  
			            reader = new BufferedReader(new FileReader(file));  
			            String tempString = null;   
			            // 一次读入一行，直到读入null为文件结束  
			            ExceptionVo exceptionVo=null;
			            int status=0;
			            while ((tempString = reader.readLine()) != null) {  
			            	if(tempString.equals("--------------------")){
			            		status=1;
			            		exceptionVo=new ExceptionVo();
			            	}
			            	else if(tempString.equals("----------------------")){
			            		if(status==5){
			            			
			            		}
			            		else{
				            		exceptionVos.add(exceptionVo);
				            		if(exceptionVos.size()>=limit){
				            			break;
				            		}
			            		}

			            	}
			            	else if(status==1){
			            		status=2;
			            	}
			            	else if(status==2){
			            		exceptionVo.time=Long.valueOf(tempString);
			    				if(startTime>=exceptionVo.time || endFileTime<=exceptionVo.time){
			    					status=5;
			    				}
			    				else{
				            		status=3;
			    				}
			            	}
			            	else if(status==3){
			            		exceptionVo.name=tempString;
			            		status=4;
			            	}
			            	else if(status==4){
			            		exceptionVo.trace+=tempString+"</br>";
			            	}
			            }  
			            reader.close();  
			        } catch (IOException e) {  
			            ExceptionUtil.processException(e);
			        } finally {  
			            if (reader != null) {  
			                try {  
			                    reader.close();  
			                } catch (IOException e1) {  
			                	ExceptionUtil.processException(e1);
			                }  
			            }  
			        }  
				}
			}			
		}

		return exceptionVos;
	}
	

	public Object createCDNResources(){
		PrintUtil.print("createCDNResources() "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		List<IdNumberVo> list2 = (List<IdNumberVo>) ((GlobalPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions)).valueObj;
		List list = serverService.createList();
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		File file=new File("C:\\Work\\SSDUpload\\release\\data\\static_"+System.currentTimeMillis()+".data");
		ServerPack serverPack=new ServerPack();
		serverPack.obj=new Object[]{list,XmlCache.xmlFiles,GlobalCache.langClientDic.get(gp.getValueStr()),list2};
		String val = JSON.toJSONString(serverPack,SerializerFeature.WriteMapNullValue,SerializerFeature.DisableCircularReferenceDetect);
		FileWriter fileWritter;
		try {
			fileWritter = new FileWriter(file,true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(val);
	        bufferWritter.close();
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		return val;
	}
	
	/**
	 * 查询当前服务器数据
	 *   1.同时在线人,2.角色生成数量
	 *   
	 * @return
	 */
	public Object fetchServerInfor(){
		Integer[] result = new Integer[2];
		//当前在线人数
		result[0] = RoleTemplate.roleIdIuidMapping.size();
		//角色生成数量
		String sql = "select count(*) from u_po_role where abandom_state is null";
		Integer roleAmount = BaseDAO.instance().jdbcTemplate.queryForInt(sql);
		result[1] = roleAmount;
		return result;
	}
}
