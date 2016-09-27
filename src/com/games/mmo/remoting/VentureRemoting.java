package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.map.activity.FreeWarRoom;
import com.games.mmo.mapserver.bean.map.activity.GuildPriestRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RankArenaPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.type.BuffModelType;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.PlayTimesType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.TaskType;
import com.games.mmo.type.UserEventType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.MapRoomParVo;
import com.games.mmo.vo.TimerBossVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.mmo.vo.team.TeamVo;
import com.games.mmo.vo.xml.ConstantFile.Dota.Layer;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.games.mmo.vo.xml.ConstantFile.PlayTimes.PlayItem;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.event.EventArg;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

@Controller
public class VentureRemoting extends BaseRemoting{
	@Autowired
	private CheckService checkService;	
	@Autowired
	private ChatService chatService;

	
	/**
	 * 名人录
	 * @return
	 */
	public Object arenaFirstInfor(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.arenaFirstInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		GlobalCache.checkAddNewArena(rolePo);
		List<RankArenaPo> rankArenaPos=new ArrayList<RankArenaPo>();
		for(int i=1;i<=50;i++){
			if(GlobalCache.rankArenaMaps.containsKey(i)){
				rankArenaPos.add(GlobalCache.rankArenaMaps.get(i));
			}
		}
		SessionUtil.addDataArray(rankArenaPos);
		return SessionType.MULTYPE_RETURN;
	}
	/**
	 * 
	 * 方法功能:获得竞技场信息，打开面板第一个接口
	 * 更新时间:2014-11-25, 作者:johnny
	 * @return
	 */
	public Object arenaFetchInfor(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.arenaFetchInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println(rolePo.getName()+"--arenaFetchInfor == " +new Date().toLocaleString());
		GlobalCache.checkAddNewArena(rolePo);
		RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(rolePo.getId());
		List<RankArenaPo> rankArenaPos = RankArenaPo.findRanksToShow(rolePo, me.getWasFirstArena());
//		System.out.println("1=" +rankArenaPos.size() +" || " +rankArenaPos);
		SessionUtil.addDataArray(rankArenaPos);
		SessionUtil.addDataArray(me);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:刷新竞技场信息
	 * 更新时间:2014-11-25, 作者:johnny
	 * @return
	 */
	public Object arenaFresh(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.arenaFresh";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println(rolePo.getName()+"--arenaFresh == " +new Date().toLocaleString());
		GlobalCache.checkAddNewArena(rolePo);
		RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(rolePo.getId());
		List<RankArenaPo> rankArenaPos = RankArenaPo.findRanksToShow(rolePo, me.getWasFirstArena());
//		System.out.println(GlobalCache.rankArenaRoleIdMaps.size() + " || "+GlobalCache.rankArenaMaps.size());
//		System.out.println("2=" +rankArenaPos.size() +" || " +rankArenaPos);
		SessionUtil.addDataArray(rankArenaPos);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:挑战竞技场某人
	 * 更新时间:2014-5-15, 作者:johnny
	 * @param rankPos 挑战排位
	 * @return [0]列表
	 */
	public Object arenaChallenge(Integer targetRoleId){
//		System.out.println("arenaChallenge() 11"+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
//		System.out.println("目标roleId == " +targetRoleId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.arenaChallenge";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println(rolePo.getName()+"--arenaChallenge == " +new Date().toLocaleString());
//		System.out.println("自己roleId == "+rolePo.getId());
		checkService.checkExisCopySceneConfPo(CopySceneType.COPYSCENE_ARENA);
		rolePo.checkPlayTimes(rolePo.getArenaTodayPlayedTimes(), PlayTimesType.PLAYTIMES_TYPE_1, "key2649");
		
		if(!IntUtil.checkInInts(CopySceneType.COPYSCENE_ARENA, CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
			CopySceneConfPo cscp = CopySceneConfPo.findEntity(CopySceneType.COPYSCENE_ARENA);
			rolePo.addCopySceneTodayRecord(CopySceneType.COPYSCENE_ARENA,cscp.getTeamMode(),1,0);
		}
		GlobalCache.checkAddNewArena(rolePo);
		RankArenaPo target = GlobalCache.rankArenaRoleIdMaps.get(targetRoleId);
//		System.out.println("target = "+target);
//		RolePo targetRole = RolePo.findEntity(targetRoleId);
		if(target==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key226")+targetRoleId);
		}
//		System.out.println("rolePo.getArenaTodayPlayedTimes() ==" +rolePo.getArenaTodayPlayedTimes());
		rolePo.setArenaTodayPlayedTimes(rolePo.getArenaTodayPlayedTimes()+1);
		rolePo.arenaTargetRoleId=targetRoleId;
		// 获得当前竞技场对象
		RankArenaPo me = GlobalCache.rankArenaRoleIdMaps.get(rolePo.getId());
		me.loadByRolePo(rolePo);
		RolePo targetRolePo = RolePo.findEntity(targetRoleId);
		if(targetRolePo != null){
			target.loadByRolePo(targetRolePo);			
		}
		if(rolePo.fighter!=null){
			rolePo.fighter.removeBuffer(3);
			rolePo.fighter.removeBuffer(15);
		}

		
		rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_706,null,null);
		MapRoom mapRoom = MapWorld.createDynalicMapRoom(CopySceneConfPo.findEntity(CopySceneType.COPYSCENE_ARENA),ScenePo.findEntity(CopySceneConfPo.findEntity(CopySceneType.COPYSCENE_ARENA).getSceneId()));
//		System.out.println("arenaChallenge() 22"+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		List<RankArenaPo> rankArenaPos = RankArenaPo.findRanksToShow(rolePo, me.getWasFirstArena());
//		System.out.println("arenaChallenge() 33"+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
//		System.out.println("arenaChallenge==" +rankArenaPos);
		SessionUtil.addDataArray(rankArenaPos);
		SessionUtil.addDataArray(mapRoom.mapRoomId);
		SessionUtil.addDataArray(mapRoom.copySceneConfPo.getId());
		SessionUtil.addDataArray(rolePo.getArenaTodayPlayedTimes());
		LogUtil.writeLog(rolePo, 325, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 方法功能:进入活动副本
	 * 更新时间:2015-7-15, 作者:peter
	 * @param copySceneConfigId
	 * @return
	 */
	public Object activityCopySceneStart(Integer copySceneConfPoId){
//		System.out.println("activityCopySceneStart() copySceneConfPoId="+copySceneConfPoId);
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		checkService.checkExisCopyScenePo(cscp.getCopySceneId());
		CopyScenePo copyScenePo = CopyScenePo.findEntity(cscp.getCopySceneId());
		checkService.checkExisCopySceneActivityPo(copySceneConfPoId);
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.activityCopySceneStart";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key227"));
		}
		if(rolePo.getLv().intValue() < cscp.getRequireLv().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key206")+cscp.getRequireLv());
		}
		// 获得当前类型活动的房间集合
		ConcurrentHashMap<Integer, MapRoom> mapRooms = MapWorld.findActivityRoom(copySceneConfPoId);

//		System.out.println("rolePo.listCopySceneTodayVisitTimes="+rolePo.listCopySceneTodayVisitTimes);
		for (IdNumberVo3 idNumberVo3 : rolePo.listCopySceneTodayVisitTimes) {
			if(idNumberVo3.getInt1().intValue()==copySceneConfPoId.intValue() && idNumberVo3.getInt2().intValue()==cscp.getTeamMode().intValue()){
				rolePo.checkSceneTimesBySceneId(idNumberVo3.getInt3(), cscp.getSceneId(), cscp.getAvaTimes());
			}
		}
		
		MapRoom mapRoom = null;

		//限制人数上限
		if(mapRooms != null && mapRooms.size() != 0){
			for(MapRoom mr : mapRooms.values()){
				if(mr.copySceneConfPo.getId().intValue() == CopySceneType.COPY_SCENE_CONF_KINGOFPK){
					mapRoom = mr;
					break;
				}
				else
				{
					if(mr.cellData.getAllCellPlayers().size() < copyScenePo.getMaxCount() && mr.sceneId == cscp.getSceneId().intValue()){
						mapRoom = mr;
						break;
					}					
				}
			}
		}
		if(mapRoom == null){
			MapRoomParVo mapRoomParVo=new MapRoomParVo();
			mapRoomParVo.isLeaderRooom=true;
			mapRoom = MapWorld.createDynalicMapRoom(cscp,ScenePo.findEntity(cscp.getSceneId()),mapRoomParVo);
		}
		else{


		}
		if(IntUtil.checkInInts(cscp.getType(), CopySceneType.ACTIVITY_TYPE)){
			rolePo.pVPPVEActivityStatusVo.currentFloor=1;
			rolePo.pVPPVEActivityStatusVo.currentFloorKillCount=0;
			rolePo.pVPPVEActivityStatusVo.monsterKillCount=0;
			rolePo.pVPPVEActivityStatusVo.score=0;
			rolePo.sendUpdatePVPPVEActivity();
		}
		if(copyScenePo.getCopyType().intValue() == 1){
			rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_708,mapRoom.copySceneConfPo.getCopySceneId(),null);			
		}
		if(mapRoom.sceneId == FreeWarRoom.SCENE_ID){
			FreeWarRoom freeWarRoom = (FreeWarRoom) mapRoom;
			
			if(freeWarRoom.index%2 == 1){
				rolePo.setMilitaryForces(CopySceneType.MILITARY_FORCES_1);				
			}else{
				rolePo.setMilitaryForces(CopySceneType.MILITARY_FORCES_2);	
			}
			freeWarRoom.index++;
		}
		
		if(!IntUtil.checkInInts(copySceneConfPoId.intValue(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
			rolePo.addCopySceneTodayRecord(mapRoom.copySceneConfPo.getId(),cscp.getTeamMode(),1,0);
		}
		LogUtil.writeLog(rolePo, 237, copySceneConfPoId, 0, 0, GlobalCache.fetchLanguageMap("key2629"), "");
		if(copySceneConfPoId.intValue()==20206007){
			LogUtil.writeLog(rolePo, 339, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfPoId.intValue()==20206006){
			LogUtil.writeLog(rolePo, 340, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfPoId.intValue()==20206005){
			LogUtil.writeLog(rolePo, 341, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfPoId.intValue()==20206004){
			LogUtil.writeLog(rolePo, 342, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfPoId.intValue()==20206012){
			LogUtil.writeLog(rolePo, 343, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}
//		rolePo.sendUpdateCurrentTime();
		SessionUtil.addDataArray(mapRoom.mapRoomId);
		SessionUtil.addDataArray(copySceneConfPoId);
		SessionUtil.addDataArray(rolePo.listCopySceneTodayVisitTimes);
		return SessionType.MULTYPE_RETURN; 
	}
	
	
	/**
	 * 方法功能:进入公会副本
	 * 更新时间:2015-7-15, 作者:peter
	 * @param copySceneConfigId
	 * @return
	 */
	public Object guildCopySceneStart(Integer copySceneConfPoId){
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(copySceneConfPoId);
		checkService.checkExisCopyScenePo(cscp.getCopySceneId());
		CopyScenePo copyScenePo = CopyScenePo.findEntity(cscp.getCopySceneId());
		checkService.checkExisCopySceneActivityPo(copySceneConfPoId);
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();	
//		System.out.println("guildCopySceneStart() " + rolePo.getName() +"; copySceneConfPoId="+copySceneConfPoId+" " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.guildCopySceneStart";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		if(rolePo.getGuildId() == null ||  rolePo.getGuildId().intValue()==0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key91"));
		}
		MapRoom mapRoom = null;
		if(copySceneConfPoId.intValue() == CopySceneType.COPY_SCENE_CONF_GUILD_PRIEST){
			if(rolePo.getGuildPriestState().intValue() == 2){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2692"));
			}
			
			ConcurrentHashMap<Integer, MapRoom> mapRooms = MapWorld.findActivityRoom(copySceneConfPoId);
			if(mapRooms != null){
				for(MapRoom mr : mapRooms.values()){
					if(mr.copySceneConfPo.getId().intValue() == cscp.getId().intValue() && mr.guildId == rolePo.getGuildId().intValue()){
						mapRoom = mr;
						rolePo.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_739, null, null);
						break;
					}
				}			
			}
			
			if(mapRoom != null){
				GuildPriestRoom guildPriestRoom = ((GuildPriestRoom)mapRoom);
				if(guildPriestRoom.startCloseTime!=0 &&  System.currentTimeMillis() > guildPriestRoom.startCloseTime){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2691"));
				}
			}else{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2691"));
			}
			
		}else{
			checkService.checkExisGuildPo(rolePo.getGuildId());
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());	
			Boss boss = GuildPo.fetchBossInfoByCopySceneConfId(copySceneConfPoId);		
			if(boss == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key228")+"：copySceneConfPoId:"+copySceneConfPoId);
			}
			IdNumberVo2 idNumber2 = IdNumberVo2.findIdNumber(boss.copysceneconfid, guildPo.listBossInfo);
			if(idNumber2 == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key229")+"：lv:"+boss.lv);
			}
			if(idNumber2.getInt2() != 1){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key230"));
			}
			if(idNumber2.getInt3() != 0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key231"));
			}
			// 获得当前类型活动的房间集合
			ConcurrentHashMap<Integer, MapRoom> mapRooms = MapWorld.findActivityRoom(copySceneConfPoId);
			
			if(mapRooms != null){
				for(MapRoom mr : mapRooms.values()){
					if(mr.copySceneConfPo.getId().intValue() == cscp.getId().intValue() && mr.guildId == rolePo.getGuildId().intValue()){
						mapRoom = mr;
						break;
					}
				}			
			}

			if(mapRoom == null){

				mapRoom = MapWorld.createGuildPoDynalicMapRoom(cscp,ScenePo.findEntity(cscp.getSceneId()), rolePo.getGuildId());
			}
		}
		
//		System.out.println("guildCopySceneStart() " + rolePo.getName() + "; mapRoomId =" +mapRoom.mapRoomId+"; copySceneConfPoId=="+copySceneConfPoId+"; guildId="+rolePo.getGuildId());
		SessionUtil.addDataArray(mapRoom.mapRoomId);
		SessionUtil.addDataArray(copySceneConfPoId);
		return SessionType.MULTYPE_RETURN; 
	}

	/**
	 * 
	 * 方法功能:进入副本
	 * 更新时间:2014-12-2, 作者:johnny
	 * @param copySceneId
	 * @param diffucult
	 * @param teamOrNot
	 * @return
	 */
	public Object copySceneStart(Integer copySceneConfigId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
//		System.out.println("copySceneStart() copySceneConfigId:"+copySceneConfigId+"; name = "+rolePo.getName()+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.copySceneStart";
		GlobalCache.checkProtocolFrequencyResponse(key, 50l,true);
		if(rolePo.fighter!= null){
			rolePo.fighter.sumDamage =0;
			if(rolePo.fighterPet !=null){
				rolePo.fighterPet.sumDamage=0;
			}
		}
		
		int mapRoomId = rolePo.copySceneStart(copySceneConfigId);
		if(mapRoomId == 0){
			ExceptionUtil.throwConfirmParamException("too rapid");
		}
		
		SessionUtil.addDataArray(mapRoomId);
		SessionUtil.addDataArray(copySceneConfigId);
		SessionUtil.addDataArray(rolePo.listCopySceneTodayVisitTimes);
		LogUtil.writeLog(rolePo, 236, copySceneConfigId, 0, 0, GlobalCache.fetchLanguageMap("key2628"), "");
		if(copySceneConfigId.intValue()==20206008){
			LogUtil.writeLog(rolePo, 332, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfigId.intValue()==20206009){
			LogUtil.writeLog(rolePo, 333, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfigId.intValue()==20206010){
			LogUtil.writeLog(rolePo, 334, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfigId.intValue()==20206011){
			LogUtil.writeLog(rolePo, 335, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfigId.intValue()==20201002){
			LogUtil.writeLog(rolePo, 337, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}else if(copySceneConfigId.intValue()==20201001){
			LogUtil.writeLog(rolePo, 338, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 副本结算
	 * @param copySceneId
	 * @param diffucult
	 * @param teamOrNot
	 * @param 1=win 0=lose
	 * @return
	 */
	public Object copySceneEnd(Integer result){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.copySceneEnd";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println("copySceneEnd() name="+rolePo.getName()+" | "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		MapRoom mapRoom = MapRoom.findStage(rolePo.getRoomId());
		if(mapRoom==null){
			PrintUtil.print("map room 不存在:"+rolePo.getRoomId());
			return 1;
		}
		if(mapRoom.copySceneConfPo == null || result == null){
			return 1;
		}
		
		
		checkService.checkExisCopySceneConfPo(mapRoom.copySceneConfPo.getId());
		CopySceneConfPo cscp = CopySceneConfPo.findEntity(mapRoom.copySceneConfPo.getId());
		rolePo.checkCopySceneConfPoReward(mapRoom.copySceneConfPo.getId());
		if(IntUtil.checkInInts(cscp.getType(), CopySceneType.SINGLE_ROOMS_TYPE)){
			rolePo.awardCopySceneConfPo(mapRoom.copySceneConfPo, result, true);
			rolePo.sendUpdateMainPack(false);
			rolePo.sendUpdateExpAndLv(false);
			rolePo.sendUpdateTreasure(false);
		}
		else if(IntUtil.checkInInts(cscp.getType(), CopySceneType.TEAM_ROOMS_TYPE)){
			TeamVo teamVo = TeamVo.findTeam(rolePo.teamMemberVo.teamVo.id, cscp.getId());
			int copySceneConfigId = cscp.getId();
			for(int i=0;i<teamVo.teamMemberVos.size();i++){
				TeamMemberVo teamMemberVo = teamVo.teamMemberVos.get(i);
				RolePo teamRoleVo = RolePo.findEntity(teamMemberVo.roleId);
				boolean award = true;
				for (IdNumberVo3 idNumberVo3 : teamRoleVo.listCopySceneTodayVisitTimes) {
					if(idNumberVo3.getInt1().intValue()==copySceneConfigId && idNumberVo3.getInt2().intValue()==cscp.getTeamMode().intValue()){
						award = teamRoleVo.checkSceneTimes(idNumberVo3.getInt3(), cscp.getSceneId(), cscp.getAvaTimes());
						break;
					}
				}
				if(award){
					teamRoleVo.awardCopySceneConfPo(cscp, result, award);
				}
				teamRoleVo.sendUpdateMainPack(false);
//				teamRoleVo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key232"));
				teamRoleVo.sendUpdateExpAndLv(false);
				teamRoleVo.sendUpdateTreasure(false);
			}
		}
		notifyListeners(new EventArg(rolePo, UserEventType.COPY_SCENE.getValue(), mapRoom.copySceneConfPo));//发送升级通知
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 开始爬塔
	 * @return
	 */
	public Object towerChallengeStart(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.towerChallengeStart";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		
		PlayItem playItem = rolePo.fetchPlayItemByType(PlayTimesType.PLAYTIMES_TYPE_530);
		if(rolePo.getTowerTodayChallengeTimes().intValue() >= playItem.initialTimes){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2588"));
		}
		
		LogUtil.writeLog(rolePo, 238, rolePo.getTowerCurrentLv(), 0, 0, GlobalCache.fetchLanguageMap("key2630"), "");
		
		rolePo.towerState=1;
		checkService.checkExisCopySceneConfPo(CopySceneType.COPYSCENE_DOTA);
		CopySceneConfPo copySceneConfPo = CopySceneConfPo.findEntity(CopySceneType.COPYSCENE_DOTA);	
		rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_707,null,null);
		MapRoom mapRoom = MapWorld.createDynalicMapRoom(copySceneConfPo,ScenePo.findEntity(copySceneConfPo.getSceneId()));
		if(rolePo.fighter != null){
			rolePo.setBatHp(rolePo.getBatMaxHp());
			rolePo.setBatMp(rolePo.getBatMaxMp());
			
			rolePo.fighter.changeBatHp(rolePo.getBatHp());
			rolePo.fighter.changeBatMp(rolePo.getBatMp());
			rolePo.fighter.changeBatMaxHp(rolePo.getBatMaxHp());
			rolePo.fighter.changeBatMaxMp(rolePo.getBatMaxMp());
			rolePo.fighter.sendAttrChangeInfor();			
		}
		rolePo.adjustTowerChallengeTimes(1);
		SessionUtil.addDataArray(mapRoom.mapRoomId);
		SessionUtil.addDataArray(mapRoom.copySceneConfPo.getId());
		SessionUtil.addDataArray(rolePo.getTowerTodayChallengeTimes());
		LogUtil.writeLog(rolePo, 331, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 爬塔结束修改层数
	 * @return
	 */
	public Object towerEndAdjustCurrentLv(){
//		PrintUtil.print("towerEndAdjustCurrentLv() 11 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.towerEndAdjustCurrentLv";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		if(rolePo.towerState.intValue()!= 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2295"));
		}
		rolePo.adjustTowerCurrentLv(1);
		rolePo.towerState=2;
		GlobalCache.addRoleToDotaRankVo(rolePo.getTowerCurrentLv(), rolePo);
//		System.out.println(rolePo.getName() +" TowerCurrentLv:"+rolePo.getTowerCurrentLv()+ "; listDotaFirstAward="+ rolePo.listDotaFirstAward);
		Layer layer = rolePo.fetchDotaLayerInfo(rolePo.getTowerCurrentLv());
		IdNumberVo idNumberVo = IdNumberVo.findIdNumber(rolePo.getTowerCurrentLv(), rolePo.listDotaFirstAward);
		if(layer != null && idNumberVo != null){
			String firstAwardExp = DBFieldUtil.fetchImpodString(layer.firstAwardExp);
			if(firstAwardExp != null && idNumberVo.getNum().intValue() == 0){
				List<IdNumberVo> awardList = IdNumberVo.createList(layer.firstAwardExp);
//				System.out.println("awardList="+awardList);
				rolePo.addItem(awardList, 1,GlobalCache.fetchLanguageMap("key2610"));
				idNumberVo.setNum(1);
			}			
		}
		rolePo.sendUpdateMainPack(false);
//		System.out.println("towerEndAdjustCurrentLv() 22 "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		SessionUtil.addDataArray(rolePo.getTowerCurrentLv());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 爬塔当前层结束
	 * @return
	 */
	public Object towerChallengeEnd(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.towerChallengeEnd";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		PrintUtil.print(rolePo.getName() +"; towerChallengeEnd()");
		rolePo.towerState=1;
		MapRoom currentMapRoom = MapRoom.findStage(rolePo.getRoomId());
		if(currentMapRoom == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key26"));
		}
		// 重置血量
		if(rolePo.fighter != null){
			rolePo.setBatHp(rolePo.getBatMaxHp());
			rolePo.setBatMp(rolePo.getBatMaxMp());
			rolePo.fighter.changeBatHp(rolePo.getBatHp());
			rolePo.fighter.changeBatMp(rolePo.getBatMp());
			rolePo.fighter.changeBatMaxHp(rolePo.getBatMaxHp());
			rolePo.fighter.changeBatMaxMp(rolePo.getBatMaxMp());
			rolePo.fighter.sendAttrChangeInfor();			
		}
		LogUtil.writeLog(rolePo, 238, rolePo.getTowerCurrentLv(), 0, 0, GlobalCache.fetchLanguageMap("key2630"), "");
		SessionUtil.addDataArray(currentMapRoom.mapRoomId);
		SessionUtil.addDataArray(currentMapRoom.copySceneConfPo.getId());
		SessionUtil.addDataArray(rolePo.getTowerCurrentLv());
		
		//副本完成奖励事件抛出
		notifyListeners(new EventArg(rolePo, UserEventType.COPY_SCENE.getValue(), currentMapRoom.copySceneConfPo));//发送升级通知
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 扫荡
	 * @return
	 */
	public Object towerWipeout(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.towerWipeout";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		if(rolePo.getTowerCurrentLv().intValue() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key233"));
		}
		rolePo.checkPlayTimes(rolePo.getTowerTodayWipeOutTimes(), PlayTimesType.PLAYTIMES_TYPE_2, "key2650");
		rolePo.adjustTowerWipeOutTimes(1);
		List<Layer> layer = XmlCache.xmlFiles.constantFile.dota.layer;
		Map<String,List<IdNumberVo>> map = new HashMap<String, List<IdNumberVo>>();
//		PrintUtil.print("rolePo.getTowerCurrentLv() = " +rolePo.getTowerCurrentLv());
		for(int i=0; i < rolePo.getTowerCurrentLv().intValue(); i++){
			Layer lay = layer.get(i);
			List<IdNumberVo2> dropExp  = IdNumberVo2.createList(lay.dropExp);
			List<IdNumberVo> list = rolePo.awardTowerDropExp(dropExp);
			list.add(new IdNumberVo(300004001,lay.exp));
			rolePo.adjustExp(lay.exp);
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_EXP, 0, lay.exp, GlobalCache.fetchLanguageMap("key2493"), "");
			map.put(String.valueOf(i+1), list);
		}
		rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_707,null,null);
		rolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key234"));
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateExpAndLv(false);
		SessionUtil.addDataArray(rolePo.getTowerTodayWipeOutTimes());
		SessionUtil.addDataArray(map);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获取Dota排行
	 * @return
	 */
	public Object fetchDotaRank(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.fetchDotaRank";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		SessionUtil.addDataArray(GlobalCache.listDotaRank);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	
	/**
	 * 
	 * 方法功能:同意加入房间
	 * 更新时间:2014-12-4, 作者:johnny
	 * @return
	 */
	public Object teamAcceptEnterRoom(Integer agree){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.teamAcceptEnterRoom";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		if(agree==1){
			rolePo.teamMemberVo.readyTeamRoomGather();
		}
		return 1;
	}
	
	
	/**
	 * 获取世界BOSS信息
	 * @return
	 */
	public Object fetchWorldBossInfors(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.fetchWorldBossInfors";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		List<MonsterFreshInforVo> monsterFreshs = new ArrayList<MonsterFreshInforVo>();
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get("keyStaticMapBossInfo");
		ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) globalPo.valueObj;
		for(MonsterFreshInforVo monsterFresh:staticMapBossMap.values())
		{

			MonsterFreshPo monsterFreshPo =MonsterFreshPo.findEntity(monsterFresh.monsterFreshId);
			if(monsterFreshPo==null){
				continue;
			}
			// bosss刷新时间
			Integer bossRefreshTime =GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_BOSS_REFRESH);
			long deltaMs=System.currentTimeMillis()-monsterFresh.disapperTime;
			long remainingTime = 0l;
			if(bossRefreshTime != null && bossRefreshTime!=0 ){
				remainingTime = 1000L*60*60*bossRefreshTime- deltaMs;
			}
			else{
				remainingTime=monsterFreshPo.getFreshSeconds().longValue()-deltaMs;
			}	
			if(remainingTime < 0){
				remainingTime=0;
			}
			
			monsterFresh.isRefresh = monsterFresh.disapperTime>0?0:1;
			monsterFresh.remainingTime = (int) remainingTime;
			monsterFreshs.add(monsterFresh);
		}
		SessionUtil.addDataArray(monsterFreshs);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * boss密室
	 * @return
	 */
	public Object fetchBossSecertInfors(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.fetchBossSecertInfors";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		List<MonsterFreshInforVo> monsterFreshs = new ArrayList<MonsterFreshInforVo>();
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyBossSecretRoom);
		ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) globalPo.valueObj;
		for(MonsterFreshInforVo monsterFresh:staticMapBossMap.values())
		{

			MonsterFreshPo monsterFreshPo =MonsterFreshPo.findEntity(monsterFresh.monsterFreshId);
			if(monsterFreshPo==null){
				continue;
			}
			// bosss刷新时间
			Integer bossRefreshTime =GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_BOSS_REFRESH);
			long deltaMs=System.currentTimeMillis()-monsterFresh.disapperTime;
			long remainingTime = 0l;
			if(bossRefreshTime != null && bossRefreshTime!=0 ){
				remainingTime = 1000L*60*60*bossRefreshTime- deltaMs;
			}
			else{
				remainingTime=monsterFreshPo.getFreshSeconds().longValue()-deltaMs;
			}	
			if(remainingTime < 0){
				remainingTime=0;
			}
			
			monsterFresh.isRefresh = monsterFresh.disapperTime>0?0:1;
			monsterFresh.remainingTime = (int) remainingTime;
			monsterFreshs.add(monsterFresh);
		}
		SessionUtil.addDataArray(monsterFreshs);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 副本鼓舞 1=gold 2=diamond
	 * @param gold
	 * @return
	 */
	public Object copyInspire(Integer gold)
	{
		CheckUtil.checkIsNull(gold);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.copyInspire";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("11 glod = " +rolePo.getGold() +"; bindGold = " +rolePo.getBindGold());
//		System.out.println("11 Diamond = " +rolePo.getDiamond() +"; BindDiamond = " +rolePo.getBindDiamond());
		Fighter fighter = rolePo.fighter;
		if(fighter != null && fighter.mapRoom != null && fighter.cell != null)
		{
			BuffPo buffPo = BuffPo.findEntity(CopySceneType.COPY_INSPIRE_BUFFID);
			if(null != buffPo)
			{
				
				BufferStatusVo currentBufferStatusVo = fighter.findBufferStatus(buffPo.getId());
				if(currentBufferStatusVo != null){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2291"));
				}
				UserPo user=UserPo.findEntity(rolePo.getUserId());
				if(gold.intValue() == 1){
					rolePo.publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,20000);
					LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -20000, GlobalCache.fetchLanguageMap("key2491"), "");
					rolePo.checkHasAndConsumeBindGoldThenGold(20000);
				}
				else if(gold.intValue() == 2 && (rolePo.getBindDiamond()+user.getDiamond())>=10){
					LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDDIMONDTHENDIMOND, 0, -10, GlobalCache.fetchLanguageMap("key2492"), "");
					rolePo.checkHasAndConsumeBindDiamondThenDiamond(10);
				}
				//加buff
				List<Fighter> receiveFighters = new ArrayList<Fighter>();
				receiveFighters.add(fighter);
				BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, fighter, receiveFighters);
				fighter.makeAddBuff(bufferStatusVo,fighter, true);
			}
		}
		
//		System.out.println("22 glod = " +rolePo.getGold() +"; bindGold = " +rolePo.getBindGold());
//		System.out.println("22 Diamond = " +rolePo.getDiamond() +"; BindDiamond = " +rolePo.getBindDiamond());
		rolePo.calculateBat(1);
		rolePo.sendUpdateListBufferStatus();
		rolePo.sendUpdateRoleBatProps(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 获取历史记录
	 * @return
	 */
	public Object fetchHistoryRecord(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.fetchHistoryRecord";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SessionUtil.addDataArray(PVEPVPRecordVo.instance);
		SessionUtil.addDataArray(rolePo.listFightActivityMaxScoreRecords);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	
	/**
	 * 副本领取奖励
	 * @return
	 */
	public Object takeBattleResultAward(Integer option, Integer copySceneConfPoId){
//		System.out.println("takeBattleResultAward() option="+option+"; copySceneConfPoId="+copySceneConfPoId);
		CheckUtil.checkIsNull(option);
		checkService.checkExisCopySceneConfPo(copySceneConfPoId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.takeBattleResultAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeBattleResultAward( option);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateAchieveAndTitle();
		rolePo.battleResultVo=null;
		CopySceneConfPo copySceneConfPo = CopySceneConfPo.findEntity(copySceneConfPoId);
		if(IntUtil.checkInInts(copySceneConfPo.getId().intValue(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_LATER)) {
			rolePo.addCopySceneTodayRecord(copySceneConfPo.getId(),copySceneConfPo.getTeamMode(),1,1);
		}
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateAchieveAndTitle();
		SessionUtil.addDataArray(rolePo.listCopySceneTodayVisitTimes);
		return SessionType.MULTYPE_RETURN;
	}
	

	
	/**
	 * 获取危机边缘剩余时间
	 */
	public Object fetchFringeRemainingTime(){
//		System.out.println("fetchFringeRemainingTime()"+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.takeBattleResultAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.checkLeaveRoomResourceScene();
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object fetchTimerBossInfo(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_VentureRemoting.fetchTimerBossInfo";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
		TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
		String time = "0";
		if(timerBossVo.bossState == 0){
			long current = System.currentTimeMillis();
			long init = DateUtil.getInitialDate(current);
			long result = (1000L*60*60*12+init) - current;
			if(result > 0){
				time = String.valueOf(result);
			}else{
				time = String.valueOf(result + (1000L*60*60*24));
			}
		}

		SessionUtil.addDataArray(timerBossVo.bossState);
		SessionUtil.addDataArray(timerBossVo.killGuildName);
		SessionUtil.addDataArray(time);
		return SessionType.MULTYPE_RETURN;
	}

}
