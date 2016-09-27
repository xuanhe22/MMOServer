package com.games.mmo.remoting;

import io.netty.channel.ChannelHandlerContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.Treasure;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.cell.CellData;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.RoleService;
import com.games.mmo.type.AttackModeType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.RankVo;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.FloatUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
@Controller
public class MapRemoting extends BaseRemoting{

	@Autowired
	private ChatService chatService;
	@Autowired
	private RoleService roleService;
	
	/**
	 *  请求追踪
	 * @param roleId
	 * @return
	 */
	public Object requestTrackPlayer(Integer roleId,Integer costDiamond){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		RolePo targetRolePo = roleService.getRolePo(roleId);
		if(rolePo.getId().intValue() == targetRolePo.getId().intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2305"));
		}
		if(targetRolePo.fetchRoleOnlineStatus()){
			MapRoom targetMapRoom = MapWorld.findStage(targetRolePo.getRoomId());
			if(targetMapRoom != null){
				if(IntUtil.checkInInts(targetMapRoom.sceneId, CopySceneType.COPY_SCENE_CONF_ALL_DYNAMIC) || targetRolePo.getRoomLoading().intValue()==1) {
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2506"));
				}
			}else{
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2506"));
			}


			if(costDiamond==1){
				rolePo.publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,20000);
				LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -20000, GlobalCache.fetchLanguageMap("key2488"), "");
				rolePo.checkHasAndConsumeBindGoldThenGold(20000);
			}
			
			ScenePo scenePo = ScenePo.findEntity(targetMapRoom.sceneId);
			//进入资源挂机地图
			if(scenePo != null && scenePo.getSceneAttribute().intValue() == 2){
//				System.out.println("EnterRoom:"+resourceSceneTime);
				if(rolePo.getResourceSceneTime().intValue() <= 0 ){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2320"));
				}
				rolePo.setStartResourceSceneTime(System.currentTimeMillis());
				rolePo.sendResourceScene();
			}
			targetRolePo.sendRequestTransferToPlayer(rolePo.getId());
			rolePo.sendUpdateTreasure(false);
		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}

		return 1;
	}
	
	public void summonToMyPlace(Integer roleId,Float x,Float y,Float z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		RolePo targetRolePo = roleService.getRolePo(roleId);
		if(targetRolePo.fetchRoleOnlineStatus()){

		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
		targetRolePo.sendReceieveSummonInfor(x,y,z,rolePo.getStaticRoomId());
	}
	
	public void blink(Integer entityId,Float x,Float y,Float z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println(new Date().toLocaleString()+" "+rolePo.getName()+" blink:" +System.currentTimeMillis());
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover==null){
			
		}
		else{
			int newX = (int)(x*Entity.BASE_NUMBER);
			int newY = (int)(y*Entity.BASE_NUMBER);
			int newZ = (int)(z*Entity.BASE_NUMBER);
			CellData cellDate = rolePo.fighter.mapRoom.cellData;
			if(!cellDate.isValidCell(cellDate.pixelXToCellX(newX), cellDate.pixelXToCellX(newZ)))
			{
				//超出地图范围
				PrintUtil.print("beyond the scope of map2 ,x:"+newX+",y:"+newZ);
				return;
			}
			rolePo.ChangeCoordinate(newX,newY,newZ);
			rolePo.fighter.mapRoom.doMonsterBlink(mover, x, y,z,newX,newY,newZ);
		}
	}
	
	
	/**
	 * 目标点同步（消息通知客户端）
	 * @param entityId
	 * @param x 目标点
	 * @param y 目标点
	 * @param z 目标点
	 * @param entityName
	 */
	public void move(Integer entityId,Float x,Float y,Float z,String entityName){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		
		if(rolePo.fighter == null || rolePo.fighter.mapRoom == null || rolePo.fighter.mapRoom.cellData == null){
			return;
		}
		if(x==0 && y==0 &&z==0){
			return;
		}
		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_").append(rolePo.fighter.mapRoom.mapRoomId).append("_").append(entityId);
		String key=sb.toString();
		if(!BattleMsgUtil.msgCheckTime.containsKey(key)){
			BattleMsgUtil.msgCheckTime.put(key,1l);
		}
		else{
			BattleMsgUtil.msgCheckTime.put(key,BattleMsgUtil.msgCheckTime.get(key)+1);
		}
		if(BattleMsgUtil.msgCheckTime.get(key)>=100){
			if(SessionUtil.getCurrentSession()!=null){
				SessionUtil.getCurrentSession().disconnect();
			}
		}
		if(BattleMsgUtil.msgCheckTime.get(key)>=50){
			ExceptionUtil.throwConfirmParamException("Map Error,Pls Relogin!!"+key+" "+x+" "+y+" "+z);
		}
		
		CellData cellDate = rolePo.fighter.mapRoom.cellData;
		Float x1 = x*Entity.BASE_NUMBER;
		Float y1 = y*Entity.BASE_NUMBER;
		Float z1 = z*Entity.BASE_NUMBER;
		if(!cellDate.isValidCell(cellDate.pixelXToCellX(x1.intValue()), cellDate.pixelXToCellX(z1.intValue())))
		{
			//超出地图范围
			PrintUtil.print("beyond the scope of map3 ,x:"+x1.intValue()+",y:"+y1.intValue());
			return;
		}
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover!=null && rolePo!=null && rolePo.fighter!=null && rolePo.fighter.mapRoom!=null){
			mover.changeTargetX(x);
			mover.changeTargetY(y);
			mover.changeTargetZ(z);
			rolePo.fighter.mapRoom.doMonsterMove(mover, x, y,z,x1.intValue(),y1.intValue(),z1.intValue());
		}
	}
	
	public void moveStop(Integer entityId,Float x,Float y,Float z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.fighter == null || rolePo.fighter.mapRoom == null || rolePo.fighter.mapRoom.cellData == null){
			return;
		}
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover!=null){
			mover.changeTargetX(0f);
			mover.changeTargetY(0f);
			mover.changeTargetZ(0f);
			
			//TODO 这里要处理的，但是自由之战会出现坐标错误

			mover.changeX(x);
			mover.changeY(y);
			mover.changeZ(z);
			rolePo.ChangeCoordinate(FloatUtil.toInt(x*Entity.BASE_NUMBER), FloatUtil.toInt(y*Entity.BASE_NUMBER), FloatUtil.toInt(z*Entity.BASE_NUMBER));
//			PrintUtil.print(mover.name+" move stop:"+x+" "+y+" "+z);
		}
	}
	
	/**
	 * 尝试九宫格切换
	 * @param entityId
	 * @param x 当前位置
	 * @param y 当前位置
	 * @param z 当前位置
	 */
	public void updateMove(Integer entityId,Float x,Float y,Float z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.getRoomLoading()==1){
			return;
		}
		if(rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key163"));
		}
		if(rolePo.fighter == null || rolePo.fighter.mapRoom == null || rolePo.fighter.mapRoom.cellData == null){
			return;
		}
		
		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_MapRemoting.updateMove").append(entityId);
		if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 200, false)){
			return;
		}
		
		Fighter mover=rolePo.fighter.mapRoom.findMoverId( entityId);
		CellData cellDate = rolePo.fighter.mapRoom.cellData;  
		int x1 = (int)(x*Entity.BASE_NUMBER);
		int y1 = (int)(y*Entity.BASE_NUMBER);
		int z1 = (int)(z*Entity.BASE_NUMBER);
		if(!cellDate.isValidCell(cellDate.pixelXToCellX(x1), cellDate.pixelXToCellX(z1)))
		{
			//超出地图范围
			PrintUtil.print("beyond the scope of map4 ,x:"+x1+",y:"+y1);
			return;
		}


		if(mover ==null && (rolePo.fighterPet!= null && entityId.intValue()==rolePo.fighterPet.mapUniqId.intValue() )){
			mover = rolePo.fighterPet;
		}
		else if(mover==null && (rolePo.yunDartCar != null && entityId.intValue() == rolePo.yunDartCar.mapUniqId.intValue())){
			mover = rolePo.yunDartCar;
		}
		if(mover!=null){
			if(mover.type == Entity.MOVER_TYPE_MONSTER && mover.master == null)
			{
				//怪物离出生点验证，如果超出指定范围则传送回出生点
				if(GameUtil.getDiffer(mover.pointX, x1) > mover.traceRange || GameUtil.getDiffer(mover.pointZ, z1) > mover.traceRange)
				{
					x1 = mover.pointX;
					y1 = mover.pointY;
					z1 = mover.pointZ;
					x = (float)mover.pointX/Entity.BASE_NUMBER;
					y = (float)mover.pointY/Entity.BASE_NUMBER;
					z = (float)mover.pointZ/Entity.BASE_NUMBER;
				}
			}
			
			if(rolePo != null && rolePo.fighter != null && rolePo.fighter.mapUniqId.intValue() == entityId){
				rolePo.ChangeCoordinate(x1,y1,z1);								
			}

			if((mover.type.intValue() == Entity.MOVER_TYPE_PET && entityId.intValue() == rolePo.fighterPet.mapUniqId.intValue()) ||
				(mover.master != null && mover.master.getId().intValue() == rolePo.getId().intValue()))
			{
				 x1 = (int)(rolePo.fighter.x*Entity.BASE_NUMBER);
				 y1 = (int)(rolePo.fighter.y*Entity.BASE_NUMBER);
				 z1 = (int)(rolePo.fighter.z*Entity.BASE_NUMBER);
				 x = rolePo.fighter.x;
				 y = rolePo.fighter.y;
				 z = rolePo.fighter.z;
			}
			rolePo.fighter.mapRoom.doMonsterUpdateMove(mover, x, y,z,x1,y1,z1);
		}
	}
	
	/**
	 * 
	 * 方法功能:释放技能
	 * 更新时间:2014-10-10, 作者:johnny
	 * @param skillId	技能编号
	 * @param selectEntityId	选中目标的实体编号
	 * @param targetEntityIds	作用目标的实体编号,at符号分割
	 * @return
	 */
	public void skill(Integer skillId,Integer casterId,Integer selectEntityId,String targetEntityIds,Integer attractIndex,Float x,Float y,Float z){
		
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		SkillPo skillPo = SkillPo.findEntity(skillId);

		if(skillPo==null){
			return;
		}
		//施法者
		if(rolePo==null || rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			return;
		}
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(casterId);
		if(mover==null){
			return;
		}
//		PrintUtil.print("skill() mover.name = " +mover.name);
		BufferStatusVo bufferStatusVo = mover.findBufferStatus(101);
		if(bufferStatusVo != null){
			PrintUtil.print(mover.name + " 假死不做处理................1111");
			return;
		}
		
		List<Integer> ids = StringUtil.getListByStr(targetEntityIds,"@");
		List<Integer> entityIds = new ArrayList<Integer>();
		for (Integer targetEntityId : ids) {
			Fighter target = rolePo.fighter.mapRoom.findMoverId(targetEntityId);
			if(target != null){
				BufferStatusVo bsv = target.findBufferStatus(101);
				if(bsv == null){
					entityIds.add(targetEntityId);
				}else{
					PrintUtil.print(target.name + " 假死不做处理................2222");
				}
			}else{
				entityIds.add(targetEntityId);
			}
		}
		
		
		
		//TODO 【业务标记】未来优化
		if(mover.type==Entity.MOVER_TYPE_PLAYER){
			StringBuilder sb=new StringBuilder();
			sb.append(rolePo.getId()).append("_MapRemoting.skill_").append(skillId);
			if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 50, false)){
				return;
			}
		}
		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_MapRemoting.updateMove").append(casterId);
		if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 50, false)){
			return;
		}
		
		
		
		
		//怪物保命技能CD检测
		if(skillId==140027){
			if(mover.lastSkillSaveMeTime!=0 && (System.currentTimeMillis()-mover.lastSkillSaveMeTime)<=40000){
				BattleMsgUtil.abroadSkill(false,mover,skillId,selectEntityId,targetEntityIds,attractIndex,mover,0,x,y,z,entityIds);
				return;
			}
			else{
				mover.lastSkillSaveMeTime=System.currentTimeMillis();
//				BattleMsgUtil.abroadSkill(false,mover,skillId,selectEntityId,targetEntityIds,attractIndex,mover,0,x,y,z,entityIds);
			}
		}
		

		boolean allowGrey=false;
		if(skillPo.getSkillAffact()==2 && ScenePo.findEntity(rolePo.fighter.mapRoom.sceneId).getRedName()==1 && mover.rolePo!=null && mover.rolePo.fighter.mapRoom != null){
			allowGrey=true;
		}
		for (Integer targetEntityId : entityIds) {
			Fighter target = rolePo.fighter.mapRoom.findMoverId(targetEntityId);
			if(target!=null){
				target.checkAddMonsterSkillOnMe(mover);
				if(mover.type==Entity.MOVER_TYPE_MONSTER){
					if(target.rolePo!=null && !target.robot){
						mover.checkAddMonsterSkillOnMe(target);
					}
				}
				//灰名判断
				if(allowGrey){
					if(target.rolePo!=null && !target.robot){
						int attackerRolePkStatus = mover.rolePo.getPkStatus();
						int defenderRolePkStatus = target.rolePo.getPkStatus();
						if(attackerRolePkStatus==0 && defenderRolePkStatus==0){
							if(mover.type==Entity.MOVER_TYPE_PLAYER){
								mover.swithPkStatus(Fighter.PK_STATUS_GREY,1);
							}
							if(mover.type==Entity.MOVER_TYPE_PET){
								if(mover.master!=null && mover.master.fighter!=null){
									mover.master.fighter.swithPkStatus(Fighter.PK_STATUS_GREY,1);
								}
							}
						}
					}
				}
			}
			else{
//				System.out.println("发生补刀:"+targetEntityId);
				BattleMsgUtil.singleAbroadDie(rolePo.fetchSession(),targetEntityId);
			}
		}
		// 选中被攻击对象
//		Fighter selectEntity=rolePo.fighter.mapRoom.findMoverId(selectEntityId);
		//灰名判断
//		if(skillPo.getSkillAffact()==2 && 
//		   selectEntity!=null && 
//		   selectEntity.rolePo!=null && 
//		   selectEntity.type==Entity.MOVER_TYPE_PLAYER &&
//		   selectEntity.rolePo.getPkStatus()==Fighter.PK_STATUS_PEACE &&
//		   mover!= null &&
//		   mover.rolePo!= null &&
//		   mover.type==Entity.MOVER_TYPE_PLAYER){
//			if(rolePo.fighter.mapRoom != null && mover.rolePo.getAttackMode().intValue()==1 && ScenePo.findEntity(rolePo.fighter.mapRoom.sceneId).getRedName()==1){
//				if(mover.rolePo.getId() != selectEntity.rolePo.getId().intValue()){
//					mover.swithPkStatus(Fighter.PK_STATUS_GREY);											
//				}
//			}
//		}
		int cost = mover.makeMpChange(mover,-skillPo.getMpCostValexp());
		BattleMsgUtil.singleAbroadMpChange(mover, cost,false);
		List<Fighter> fighters=BattleMsgUtil.abroadSkill(true,mover,skillId,selectEntityId,targetEntityIds,attractIndex,mover,0,x,y,z,entityIds);
		for (Fighter fighter : fighters) {
				ChannelHandlerContext ctx=fighter.rolePo.fetchSession();
				if(ctx!=null){
					BaseSessionUtil.flushSession(ctx);
				}
		}
	}
	
	
	/**
	 * 
	 * 方法功能:进入房间
	 * 更新时间:2014-12-1, 作者:johnny 
	 * @param roomId
	 * @param type 1=普通  2=原地复活 3=出生点复活
	 * @param teleportType 传送类型 0：普通传送； 1：金币传送
	 */
	public Object enterRoom(Integer roomId,Integer x,Integer y,Integer z,Integer type,Integer teleportType){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);

		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_MapRemoting.enterleaveRoom_").append(rolePo.getId());
		if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 100, false)){
			ExceptionUtil.throwConfirmParamException("too rapid leave room");
		}
		int roomInvalid=0;
		MapRoom mapRoom = MapWorld.findStage(roomId);
		if(mapRoom==null){
			roomInvalid=1;
		}
		else{
			rolePo.setRoomId(roomId);
			if(!mapRoom.isDynamic){
				rolePo.setStaticRoomId(roomId);
			}
			rolePo.enterRoom(roomId,x,y,z,type,teleportType);
		}
		if(roomInvalid==1){
			if(rolePo.getStaticRoomId() == null){
				rolePo.setStaticRoomId(20101001);				
			}
			SessionUtil.addDataArray(rolePo.getStaticRoomId());
		}
		else{
			SessionUtil.addDataArray(rolePo.getRoomId());
		}
		SessionUtil.addDataArray(roomInvalid);
		if(roomId.intValue()==20100007){
			LogUtil.writeLog(rolePo, 336, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		}
		LogUtil.writeLog(rolePo, 235, roomId, 0, 0, GlobalCache.fetchLanguageMap("key2627"), "");
		return SessionType.MULTYPE_RETURN;
	}
	
	
	public void refreshNearByEntitys(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo==null || rolePo.fighter==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
		//断线重连目标点同步
		rolePo.fighter.aimX = rolePo.fighter.x;
		rolePo.fighter.aimY = rolePo.fighter.y;
		rolePo.fighter.aimZ = rolePo.fighter.z;

		
    	MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
    	if(mapRoom!=null){	
    		List<Entity> list = mapRoom.buildEntityListByAllNpcAndNearByCell(mapRoom.cellData,rolePo.fighter.cell,5,true);
    		mapRoom.cellData.sendNearByContextToPlayer(rolePo.fighter, list);
    	}

	}
	
	/**
	 * 离开房间
	 * @param roomId
	 * @param leaverType 0:默认； 1：下线；
	 */
	public void leaveRoom(Integer roomId, Integer leaverType){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		
		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_MapRemoting.enterleaveRoom_").append(rolePo.getId());
		if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 100, false)){
			ExceptionUtil.throwConfirmParamException("too rapid leave room");
		}
		
		rolePo.leaveRoom(false);
	}
	
	
	
	public void pickTreasure(String treasureIds){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo==null || rolePo.fighter==null || rolePo.fighter.mapRoom==null){
//			System.out.println("没有fighter");
			return;
		}
		List<Integer> treasures = DBFieldUtil.getIntegerListByCommer(treasureIds);
		rolePo.checkItemPackFull(treasures.size());
		List<Treasure> pickedTreasure =  new ArrayList<Treasure>();
		boolean finalFreshPack=false;
		for (Integer treasureId : treasures) {
			boolean freshPack=false;
			Treasure treasure = rolePo.fighter.mapRoom.findTreasureById(treasureId);
			if(treasure==null){
//				System.out.println("宝物不存在:"+treasureId);
				continue;
			}
			if((System.currentTimeMillis()-treasure.dropTime)<=15*1000){
				List<String> names = new ArrayList<String>();
				for (RolePo rolePo2 : treasure.avaPickers) {
					names.add(rolePo2.getName());
					if(rolePo2.getId()==rolePo.getId().intValue()){
						freshPack=rolePo.fighter.pickTreasure(treasure);
						pickedTreasure.add(treasure);
						continue;
					}
				}
				//ExceptionUtil.throwConfirmParamException("掉落物品属于【"+StringUtil.implode(names, ",")+"】,剩余"+(15-(System.currentTimeMillis()-treasure.dropTime)/1000)+"秒");
			}
			else{
				freshPack=rolePo.fighter.pickTreasure(treasure);
				pickedTreasure.add(treasure);
			}
			if(freshPack==true){
//				System.out.println("freshPack:"+freshPack);
				finalFreshPack=true;
			}
		}
		BattleMsgUtil.abroadRemoveTreasure(rolePo.fighter,pickedTreasure);
//		System.out.println("finalFreshPack:"+finalFreshPack);
		if(finalFreshPack){
			rolePo.sendUpdateMainPack(false);
		}
		rolePo.sendUpdateTreasure(false);
	}
	
	



	/**
	 * 改变攻击模式
	 * @param casterId
	 * @param attackMode 
	 * @param wasStrongChange 是否强行改掉 0：默认；   1：强行切换pk模式
	 * @return
	 */
	public Object attackModeChange(Integer casterId,Integer attackMode, Integer wasStrongChange){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		// TODO 2016-06-08 取消时间验证
//		if(wasStrongChange.intValue() == 0 && (System.currentTimeMillis()-rolePo.getAttackRecoverTime().longValue())<(60*5*1000 - 5000) ){
//			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key159"));
//		}
		if(wasStrongChange!=1){
			String sb = MessageFormat.format(GlobalCache.fetchLanguageMap("key2636"), AttackModeType.parse(attackMode).getName());
			rolePo.sendMsg(sb);			
		}
		rolePo.setAttackMode(attackMode);
		rolePo.setAttackRecoverTime(System.currentTimeMillis());
//		Fighter mover=rolePo.fighter.mapRoom.findMoveryId(casterId);
//		if(mover.lv>=10){
//			chatService.sendSystemWorldChat(rolePo.getName()+"已经超神了,拜托谁去干掉他!");
//		}
		SessionUtil.addDataArray(rolePo.getAttackMode());
		SessionUtil.addDataArray(rolePo.getAttackRecoverTime());
		SessionUtil.addDataArray(wasStrongChange);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 
	 * 方法功能:结束施法姿态
	 * 更新时间:2014-11-21, 作者:johnny
	 * @param casterId
	 * @return
	 */
	public Object endCastPose(Integer casterId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo==null || rolePo.fighter==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(casterId);
		if(mover==null){
//			System.out.println(casterId+":endCastPose目标不存在");
			return 1;
		}
//		BattleMsgUtil.abroadEndCastPose(mover);
		return 1;
	}
	
	
	public Object positionStop(Integer entityId,Integer x,Integer y,Integer z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover==null){
			return 1;
//			ExceptionUtil.throwConfirmParamException("目标不存在");
		}
		
		mover.changeX(x/10000f);
		mover.changeY(y/10000f);
		mover.changeZ(z/10000f);

//		BattleMsgUtil.abroadPositionEnd(mover);
		return 1;
	}
	
	
	/**
	 * 脚本房间刷出组的怪物
	 * @param groupId 组编号
	 * @return
	 */
	public Object roomRequireFreshMonsterGroup(Integer groupId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		
		MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
		if(mapRoom==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"MapRoom:"+rolePo.getRoomId());
		}
		mapRoom.roomRequireFreshMonsterGroup(groupId);

		return 1;
	}
	
	
	/**
	 * 竞技场刷出对手
	 * @return
	 */
	public Object roomRequireArenaFreshRole(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println(rolePo.getName()+" roomRequireArenaFreshRole == " +new Date().toLocaleString());
		MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
//		System.out.println("mapRoom:+++++++"+mapRoom.mapRoomId);
		if(mapRoom!=null && mapRoom.isDynamic && mapRoom.sceneId == 20100002){
			RolePo targetRolePo = RolePo.findEntity(rolePo.arenaTargetRoleId);
//			System.out.println("roomRequireArenaFreshRole() = "+targetRolePo.getId()+"; name ="+targetRolePo.getName()+" / "+rolePo.arenaTargetRoleId);
			RolePo robotRolePo = GlobalCache.createArenaRobot();
			if(targetRolePo != null){
				robotRolePo.cloneAttribute(targetRolePo);
				Fighter mover = Fighter.create(robotRolePo, mapRoom,true);
				mover.changeBatHp(mover.batMaxHp);
				mover.changeBatMp(mover.batMaxMp);
				
				mover.changeX(48.00761f);
				mover.changeY(20.80288f);
				mover.changeZ(61.25119f);
				mover.aimX = mover.x;
				mover.aimY = mover.y;
				mover.aimZ = mover.z;
				mover.robot = true;
				mover.robotType = Entity.ROBOT_TYPE_ARENA;
				robotRolePo.fighter = mover;
				if(mover!=null){
					mover.removeBuffer(3);
					mover.removeBuffer(15);
					mover.removeBuffer(40);
					mover.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
				}
				mapRoom.doAddMoverToStage(mover, null, true);
			}
		}
		return 1;
	}
	
	/**
	 * 更新监听的map id列表
	 * @param ids
	 * @return
	 */
	public Object updateListenerMapIds(String ids){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(ids.equals("0")){
			rolePo.listenerMapIds.clear();
		}
		else{
			List<Integer> ints = StringUtil.getListByStr(ids);
			rolePo.listenerMapIds=ints;
		}
		return 1;
	}
	
	/**
	 * 代理怪物
	 * @param monsterMapId
	 * @return
	 */
	public void agentMonster(Integer monsterMapId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
		Fighter player=rolePo.fighter;
		if(rolePo==null || rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			return ;
		}
		Fighter monster=rolePo.fighter.mapRoom.findMoverId(monsterMapId);
		if(monster==null ||mapRoom==null){
			return ;
//			ExceptionUtil.throwConfirmParamException("目标不存在:"+monsterMapId);
		}

		mapRoom.doAgentMonster(player, monster);
		return ;
	}
	
	/**
	 * 让宠物出场在地图上
	 */
	public void summonFightPet(Integer x,Integer y,Integer z){
//		System.out.println("summonFightPet()");
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		System.out.println("RpetFighterId="+rolePo.getRpetFighterId());
		RpetPo rpetPo = RpetPo.findEntity(rolePo.getRpetFighterId());
		if(rolePo.fighter != null && rolePo.fighterPet != null && rolePo.fighterPet.cell != null){
			rolePo.fighter.mapRoom.cellData.removeLiving(rolePo.fighterPet, true);
		}
		if(rolePo.fighterPet!=null){
			BattleMsgUtil.abroadFighterRemove(rolePo.fighterPet);
			rolePo.fighterPet=null;
		}
		if(rpetPo==null){
			return;
//			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"RpetPo:"+rolePo.getRpetFighterId());
		}

		if(rolePo==null || rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			return;
		}
		Fighter mover=Fighter.create(rolePo, rpetPo, rolePo.fighter.mapRoom);
		mover.changeX(x/10000f);
		mover.changeY(y/10000f);
		mover.changeZ(z/10000f);

		mover.aimX = mover.x;
		mover.aimY = mover.y;
		mover.aimZ = mover.z;
		mover.type=Entity.MOVER_TYPE_PET;
		rolePo.fighterPet=mover;
		mover.changeMasterPlayer(rolePo);
		rolePo.fighter.mapRoom.doAddMoverToStage(mover,null, true);
		rolePo.liveActivityRankPet();
	}
	
	/**
	 * 让宠物从地图消失
	 */
	public void dismissFightPet(){
//		System.out.println("dismissFightPet()");
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.fighter != null && rolePo.fighterPet != null ){
			rolePo.fighter.mapRoom.cellData.removeLiving(rolePo.fighterPet, true);
			BattleMsgUtil.abroadFighterRemove(rolePo.fighterPet);
		}
		rolePo.fighterPet=null;
	}	
	
	/**
	 * 让镖车出现在地图上
	 * @param x
	 * @param y
	 * @param z
	 */
	public void summonYunCart(Integer x,Integer y,Integer z){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.fighter != null && rolePo.yunDartCar != null ){
//			System.out.println("2summonYunCart() "+ rolePo.getName());
			rolePo.fighter.mapRoom.cellData.removeLiving(rolePo.yunDartCar, true);			
		}
		
		if(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality.intValue() == -1){
//			System.out.println( "镖车品质找不到了===" +rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
			return;
		}
		Integer hp = 0;
		if(rolePo.yunDartCar != null){
			hp = rolePo.yunDartCar.getBatHp();
		}
		Cart cart = XmlCache.xmlFiles.constantFile.trade.cart.get(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
		MonsterPo monsterPo = MonsterPo.findEntity(cart.monsterId);
		Fighter mover = Fighter.create(monsterPo,rolePo.fighter.mapRoom,0,null, rolePo);
		mover.changeX(x/10000f);
		mover.changeY(y/10000f);
		mover.changeZ(z/10000f);
		
		mover.aimX = mover.x;
		mover.aimY = mover.y;
		mover.aimZ = mover.z;
		if(hp !=null && hp.intValue() !=0){
			mover.setBatHp(rolePo.yunDartCar.getBatHp());			
		}
		rolePo.yunDartCar=mover;
		MapWorld.removeYunDartFighter(rolePo);
		MapWorld.addYunDartFighter(mover, rolePo);
		rolePo.fighter.mapRoom.doAddMoverToStage(mover, null, true);	
		rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId = mover.mapUniqId;
	}
	
	/**
	 * 让运镖车消失
	 */
	public void dismissYunCart(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		if(rolePo.fighter != null && rolePo.yunDartCar != null){
//			System.out.println("2dismissYunCart() "+ rolePo.getName());
			rolePo.fighter.mapRoom.cellData.removeLiving(rolePo.yunDartCar, true);		
		}
	}
	
	public Object findRoleMapInfor(Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		RolePo target = roleService.getRolePo(roleId);
		if(target.fetchRoleOnlineStatus() && target.fighter!=null ){
			
		}
		else{
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key79"));
		}
		SessionUtil.addDataArray(target.fetchRoleOnlineStatus()?1:0);
		SessionUtil.addDataArray(target.getStaticRoomId());
		SessionUtil.addDataArray(target.fighter.mapRoom.isDynamic?1:0);
		SessionUtil.addDataArray(target.fighter.x);
		SessionUtil.addDataArray(target.fighter.y);
		SessionUtil.addDataArray(target.fighter.z);
		
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 采集资源
	 * @param entityId
	 * @return
	 */
	public Object mapItemResourcePick(Integer entityId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkItemPackFull(1);
		if(rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key163"));
		}
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key164"));
		}
		if(rolePo.fighter.mapRoom.mapRoomId==MapType.PICK_CRISTICAL_SCENE_ID){
			if(rolePo.getPickCrisitalTodayTimes()>=20){
				return 1;
			}
			rolePo.setPickCrisitalTodayTimes(rolePo.getPickCrisitalTodayTimes()+1);
			rolePo.sendUpdatePickCrisitalTodayTimes();
			rolePo.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_740, null, null);
		}
		MonsterPo monsterPo = mover.monsterPo;
		if(monsterPo.getPickDisappear()==1){
			MonsterFreshInforVo monsterFreshInforVo = rolePo.fighter.mapRoom.findMonsterFreshInfor(mover.monsterFreshId);
			if(monsterFreshInforVo!=null){
				monsterFreshInforVo.disapperTime=System.currentTimeMillis();
			}
			rolePo.fighter.mapRoom.cellData.removeLiving(mover,true);
		}
		List<IdNumberVo> list =new ArrayList<IdNumberVo>();
		List<DropPo> totalDropList = DropPo.makeDropListByExp(monsterPo.listItemDrop);
		//采集水晶的特殊处理
		boolean doubleDrop=false;
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_COLLECT_CRYSTAL);
		if(copySceneActivityPo!=null && copySceneActivityPo.getActivityWasOpen().intValue() == 1){
			if(rolePo.fighter.mapRoom.sceneId==20100028){
				doubleDrop=true;
			}
		}

		if(doubleDrop){
			totalDropList.addAll(totalDropList);
		}
		for (DropPo dropPo2 : totalDropList) {
			list.add(new IdNumberVo(dropPo2.getItemId(),dropPo2.getNum()));
			rolePo.awardDrop(dropPo2,dropPo2.getBind());
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_ITEM, dropPo2.getItemId(), dropPo2.getNum(),GlobalCache.fetchLanguageMap("key2599"), "");
		}
		
		List<DropPo> totalTaskDropList = DropPo.makeDropListByListExp(rolePo,monsterPo.listItemDropTask);
		for (DropPo dropPo2 : totalTaskDropList) {
			list.add(new IdNumberVo(dropPo2.getItemId(),dropPo2.getNum()));
			rolePo.awardDrop(dropPo2,dropPo2.getBind());
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_ITEM, dropPo2.getItemId(), dropPo2.getNum(),GlobalCache.fetchLanguageMap("key2599"), "");
		}
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	

	/**
	 * 
	 * 方法功能:触发buff
	 * @param buffId	技能编号
	 * @param entityId	触发者
	 * @param entityIds	承受者列表,at符号分割
	 * @param params buff参数
	 * @return
	 */
	public Object buff(Integer buffId, Integer entityId,String entityIds, Integer params,Integer tagerRoleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
//		PrintUtil.print("MapRemoting.buff()"+ rolePo.getName()+" ;  buffId = "+buffId +"; entityId="+entityId+"; entityIds="+entityIds+"; params="+params);
		if(rolePo.fighter.mapRoom==null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key163"));
		}
		BuffPo buffPo = BuffPo.findEntity(buffId);
		if(buffPo !=null && !StringUtil.isEmpty(entityIds))
		{
			Fighter fighter = rolePo.fighter.mapRoom.findMoverId(entityId);
			if(null != fighter)
			{
				String[] strs = StringUtil.split(entityIds, "@");
				List<Fighter> receiveFighters = new ArrayList<Fighter>();
				for(String str:strs)
				{
					Fighter receiveFighter = rolePo.fighter.mapRoom.findMoverId(Integer.parseInt(str));
					if(null != receiveFighter)
						receiveFighters.add(receiveFighter);
				}
				BufferStatusVo bufferStatusVo = new BufferStatusVo(buffPo, fighter, receiveFighters);
				for(Fighter receiveFighter:receiveFighters)
				{
					receiveFighter.makeAddBuff(bufferStatusVo,fighter, true);
				}
			}
			
			RolePo tagerRole = RolePo.findEntity(tagerRoleId);
			if(tagerRole != null && tagerRole.fighter != null){
				tagerRole.fighter.removeBuffer(40);
			}
			
		}
		
		return 1;
	}
	
	public Object fetchRandomMonsterPoint(){

		MapRoom mapRoom = MapWorld.findStage(IntUtil.getRandomInt(20101001, 20101001));
		List<Fighter> figters =mapRoom.cellData.getAllCellMonsterss();
		Object obj = RandomUtil.randomObject(figters, null);
		Fighter fighter =(Fighter) obj;

		SessionUtil.addDataArray(mapRoom.sceneId);
		SessionUtil.addDataArray(86.07296);
		SessionUtil.addDataArray(20.34316);
		SessionUtil.addDataArray(79.56821);
		SessionUtil.addDataArray(0);
		SessionUtil.addDataArray(118.5);
		SessionUtil.addDataArray(0);
		SessionUtil.addDataArray(118.5);
		return SessionType.MULTYPE_RETURN;
		
	}
	
	/**
	 * 获取boss伤害排行
	 * @param entityId
	 * @return
	 */
	public Object fetchMonsterBossHitRank(Integer entityId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		
		StringBuilder sb=new StringBuilder();
		sb.append(rolePo.getId()).append("_MapRemoting.fetchMonsterBossHitRank_");
		if(GlobalCache.checkProtocolFrequencyResponse(sb.toString(), 3000, false)){
			ExceptionUtil.throwConfirmParamException("too rapid for fetchMonsterBossHitRank");
		}
		
		if(rolePo==null || rolePo.fighter==null || rolePo.fighter.mapRoom==null){
			SessionUtil.addDataArray(0);
			SessionUtil.addDataArray(entityId);
			return 1;
		}
		
		Fighter mover=rolePo.fighter.mapRoom.findMoverId(entityId);
		if(mover == null){
			SessionUtil.addDataArray(0);
			SessionUtil.addDataArray(entityId);
			return 1;
//			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key163"));
		}
		List<RankVo> list = mover.fetchMonsterBossHitRank();
		SessionUtil.addDataArray(1);
		SessionUtil.addDataArray(entityId);
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	
	public void newbieEndHpRecover(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		if(rolePo.getLv()==1){
			rolePo.setBatHp(rolePo.getBatMaxHp());
			rolePo.setBatMp(rolePo.getBatMaxMp());
		}
	}
	
}
