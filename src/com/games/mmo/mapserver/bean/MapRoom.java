package com.games.mmo.mapserver.bean;

import io.netty.channel.ChannelHandlerContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.map.activity.KilllingTowerRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.mapserver.cell.CellData;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.mapserver.vo.MonsterFreshInforVo;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.MailService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.MailType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.PVPPVEActivityStatusVo;
import com.games.mmo.vo.TimerBossVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.role.RolePackItemVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.PoseidonAward;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ArrayUtil;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;


public class MapRoom {
	public long createdTime=0l;
    public int mapRoomId;
    public int sceneId;
    public int guildId;
	public Integer entityIndex=1;
	public boolean isDynamic=false;
	public List<MonsterFreshInforVo> monsterFreshInforVos=new CopyOnWriteArrayList<MonsterFreshInforVo>();
	public List<Treasure> treasures = new CopyOnWriteArrayList<Treasure>();
	public List<MonsterFreshInforVo> robotFreshInforVos = new CopyOnWriteArrayList<MonsterFreshInforVo>();
	
	public CopySceneConfPo copySceneConfPo;
	public CellData cellData;
	// 是否播放boss刷新公告
	public boolean wasBossFlushNotice = true;
	
	public ConcurrentHashMap<Integer, Fighter> fighterIdMaps =new ConcurrentHashMap<Integer, Fighter>();
	
	public MapRoom(ScenePo scenePo)
	{
//		PrintUtil.print("MapRoom(scenePo)");
		cellData = new CellData(scenePo.getMapPixelWidth(), scenePo.getMapPixelHeight(), scenePo.getCellPixelX(), scenePo.getCellPixelY(), this);
//		cellData = new CellData(Math.abs(scenePo.getX())+1000000, Math.abs(scenePo.getZ())+1000000, 120000, 120000, this);
	}

    public void buildMonsterFreshInforVos(){
    	List<MonsterFreshPo> monsterFreshPos = GameDataTemplate.gameDataListCache.get("MonsterFreshPo");
    	for (MonsterFreshPo monsterFreshPo : monsterFreshPos) {
			if(monsterFreshPo.getSceneId().intValue()==sceneId){
//				RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
				MonsterFreshInforVo monsterFreshInforVo = new MonsterFreshInforVo();
				monsterFreshInforVo.disapperTime=0l;
				if(monsterFreshPo.getMonsterId()==450000016){
					monsterFreshInforVo.disapperTime=System.currentTimeMillis();
				}
				monsterFreshInforVo.monsterFreshPo=monsterFreshPo;
				monsterFreshInforVo.monsterFreshId=monsterFreshPo.getId();
				monsterFreshInforVo.monsterPo=MonsterPo.findEntity(monsterFreshPo.getMonsterId());
				monsterFreshInforVos.add(monsterFreshInforVo);
				MonsterPo monsterPo = MonsterPo.findEntity(monsterFreshInforVo.monsterFreshPo.getMonsterId());
//				PrintUtil.print("monsterFreshInforVo:"+monsterFreshInforVo.monsterFreshPo.getMonsterId());
				GlobalPo globalPo = null;
				if(monsterPo.getBossUsage()==1){
					globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyStaticMapBossInfo);				
				}else if(monsterPo.getBossUsage()==3){
					globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyBossSecretRoom);
				}
				if(globalPo!=null){
					ConcurrentHashMap<Integer,MonsterFreshInforVo> staticMapBossMap = (ConcurrentHashMap<Integer, MonsterFreshInforVo>) globalPo.valueObj;
					boolean bool = false;
				Lable:for(MonsterFreshInforVo mon : staticMapBossMap.values()){
						if(mon.monsterFreshId == monsterFreshInforVo.monsterFreshId){
							monsterFreshInforVo.disapperTime= mon.disapperTime;
							bool=true;
							break Lable;
						}
					}
					if(!bool){
						globalPo.addStaticMapBossInfo(monsterFreshInforVo);					
					}
				}
				if(monsterFreshInforVo.disapperTime==0L && monsterFreshInforVo.monsterPo.getBossUsage().intValue()!=2){
					spawnMonster(monsterFreshInforVo);					
				}else if(monsterFreshInforVo.monsterPo.getBossUsage().intValue()==2){
					globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
					TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
					if(timerBossVo.bossState == 1){
//						PrintUtil.print("11服务器启动地图加载刷新TimerBoss");
						spawnMonster(monsterFreshInforVo);	
					}else if(timerBossVo.bossState == 0){
						long dieTime = timerBossVo.killTime;
						long noonTime = DateUtil.getInitialDate(System.currentTimeMillis()) + 1000l*60*60*12;
						if(System.currentTimeMillis() > noonTime && noonTime > dieTime){
//							PrintUtil.print("22服务器启动地图加载刷新TimerBoss");
							spawnMonster(monsterFreshInforVo);
						}
					}
				}

			}
		}
    }
    
    
    /**
     * 中午定时boss刷新
     */
    public void noonTimerBossMonsterFresh(){
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
		TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
		if(timerBossVo.bossState == 0){
			for (MonsterFreshInforVo monsterFreshInforVo : monsterFreshInforVos) {
				MonsterFreshPo monsterFreshPo =monsterFreshInforVo.monsterFreshPo;
				if(monsterFreshPo==null){
					PrintUtil.print("monsterFreshPo不存在："+monsterFreshInforVo.monsterFreshId);
					continue;
				}
				spawnMonster(monsterFreshInforVo);
				PrintUtil.print("中午12点刷新 TimerBoss");
			}
		}
    }

    
    
    /**
     * 创建机器人
     */
	public void buildRobotFighterInfoVos(Integer roomId){
		if(20101099 == sceneId)
		{
			for(RolePo robot :  GlobalCache.robotTaskRoles.values())
			{
				robot.setRoomId(roomId);
				robot.listBufferStatus.clear();
//				MonsterFreshInforVo robotFreshInforVo = new MonsterFreshInforVo();
				Fighter mover=Fighter.create(robot,null,true);
				mover.robotType = Entity.ROBOT_TYPE_TASK;
//				robotFreshInforVo.disapperTime=0l;
//				robotFreshInforVo.robotFighter = mover;
//				robotFreshInforVos.add(robotFreshInforVo);
//				spawnRobotFighter(robotFreshInforVo);
				mover.changeBatHp(robot.getBatMaxHp());
				mover.changeBatMp(robot.getBatMaxMp());
//				mover.batHp = robot.getBatMaxHp();
//				mover.batMp = robot.getBatMaxMp();
				robot.fighter = mover;
				doAddMoverToStage(mover,null,true);
				
				// 加载宠物
//				Fighter petMover=Fighter.create(robot, RpetPo.findEntity(robot.getRpetFighterId()), robot.fighter.mapRoom);
////				Fighter mover=Fighter.create(monsterPo, rolePo.fighter.mapRoom, null);
////				mover.name=mover.name;
//				
//				petMover.x=((double)robot.getX())/Entity.BASE_NUMBER;
//				petMover.y=((double)robot.getY())/Entity.BASE_NUMBER;
//				petMover.z=((double)robot.getZ())/Entity.BASE_NUMBER;
//				petMover.aimX = petMover.x;
//				petMover.aimY = petMover.y;
//				petMover.aimZ = petMover.z;
//				petMover.type=Entity.MOVER_TYPE_PET;
//				robot.fighterPet=petMover;
//				petMover.master=robot;
//				robot.fighter.mapRoom.doAddMoverToStage(petMover,null, true);

			}
		}
    }

    /**
     * 刷新怪物
     * @param monsterFreshInforVo
     */
	public void spawnMonster(MonsterFreshInforVo monsterFreshInforVo) {
		MonsterFreshPo monsterFreshPo=MonsterFreshPo.findEntity(monsterFreshInforVo.monsterFreshId);
		MonsterPo monsterPo=MonsterPo.findEntity(monsterFreshPo.getMonsterId());
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_COLLECT_CRYSTAL);
//		PrintUtil.print(" copySceneActivityPo = " +copySceneActivityPo.getActivityWasOpen().intValue());
		if(copySceneActivityPo==null || copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			if(monsterPo.getId()==450000016){
				return;
			}
		}
		monsterFreshInforVo.disapperTime=0l;
//		RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");

		if(monsterPo.getBossUsage().intValue()==1){
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get("keyStaticMapBossInfo");
			globalPo.updateStaticMapBossInfo(monsterFreshInforVo);
		}
		else if (monsterPo.getBossUsage().intValue()==3) {
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyBossSecretRoom);
			globalPo.updateStaticMapBossInfo(monsterFreshInforVo);
		}
		else if(monsterPo.getBossUsage().intValue()==2){
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
			TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
			timerBossVo.bossState=1;
			PrintUtil.print("timerBossVo = " +timerBossVo);
		}
		
		if(monsterPo.getHorseAbroad().intValue() == 1){
			ScenePo scenePo = ScenePo.findEntity(monsterFreshPo.getSceneId());
			ChatService chatService=ChatService.instance();
			StringBuffer sb = new StringBuffer();			
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key13"));
			sb.append(ColourType.COLOUR_YELLOW).append(monsterPo.getName());
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key14"));
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key15"));
			chatService.sendHorse(sb.toString());
			wasBossFlushNotice = true;
		}
		Fighter mover = Fighter.create(monsterPo, this,monsterFreshPo.getId(),monsterFreshPo);
		mover.changeX(((float)monsterFreshPo.getX())/Entity.BASE_NUMBER);
		mover.changeY(((float)monsterFreshPo.getY())/Entity.BASE_NUMBER);
		mover.changeZ(((float)monsterFreshPo.getZ())/Entity.BASE_NUMBER);
		mover.aimX = mover.x;
		mover.aimY = mover.y;
		mover.aimZ = mover.z;
		mover.pointX = monsterFreshPo.getX();
		mover.pointY = monsterFreshPo.getY();
		mover.pointZ = monsterFreshPo.getZ();
		doAddMoverToStage(mover, null, true);
		if(monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE10){
			byte[] results=mover.buildMoverAppearBytes();
			results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
			BattleMsgUtil.abroadBattleMsg(this,results,mover,true,3,BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING,true);
		}
	}
	
	/**
	 * 刷新机器人
	 * @param robot
	 */
	public void spawnRobotFighter(MonsterFreshInforVo monsterFreshInforVo){
		monsterFreshInforVo.disapperTime=0l;
		Fighter robotFighter = monsterFreshInforVo.robotFighter;
		RolePo robotRolePo = GlobalCache.robotTaskRoles.get(robotFighter.itemId);
		robotRolePo.setStaticRoomId(20101099);
		robotRolePo.listBufferStatus.clear();
		Fighter mover=Fighter.create(robotRolePo,null,true);
		mover.robotType = Entity.ROBOT_TYPE_TASK;
//		mover.batHp = robotRolePo.getBatMaxHp();
//		mover.batMp = robotRolePo.getBatMaxMp();
		mover.changeBatHp(robotRolePo.getBatMaxHp());
		mover.changeBatMp(robotRolePo.getBatMaxMp());
		robotRolePo.fighter = mover;
//		PrintUtil.print("刷新机器人："+robotRolePo.getId());
		doAddMoverToStage(mover,null,true);
	}
	
	/**
	 * 镖车刷新
	 * @param monsterId
	 * @param rolePo
	 */
	public void spawnYunDartFighter(Integer monsterId, RolePo rolePo){
		MonsterPo monsterPo = MonsterPo.findEntity(monsterId);
		Fighter mover = Fighter.create(monsterPo,this,0,null, rolePo);
		mover.changeX(Float.valueOf(rolePo.getX()/10000));
		mover.changeY(Float.valueOf(rolePo.getY()/10000));
		mover.changeZ(Float.valueOf(rolePo.getZ()/10000));
		mover.aimX = mover.x;
		mover.aimY = mover.y;
		mover.aimZ = mover.z;
		rolePo.yunDartCar=mover;
		MapWorld.removeYunDartFighter(rolePo);
		MapWorld.addYunDartFighter(mover, rolePo);
		this.doAddMoverToStage(mover, null, true);
		rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId = mover.mapUniqId;
	}
	

	

	/**
	 * 查找怪物对象
	 * @param monsterFreshId
	 * @return
	 */
	public MonsterFreshInforVo findMonsterFreshInfor(Integer monsterFreshId) {
		for (MonsterFreshInforVo monsterFreshInforVo : monsterFreshInforVos) {
			if(monsterFreshInforVo.monsterFreshId==monsterFreshId.intValue()){
				return monsterFreshInforVo;
			}
		}
		return null;
	}
	
	/**
	 *  查找机器人
	 * @param robotId
	 * @return
	 */
	public MonsterFreshInforVo findRobotFreshInfo(Integer robotId){
		for(MonsterFreshInforVo monsterFreshInforVo : robotFreshInforVos){
			if(monsterFreshInforVo.robotFighter.itemId.intValue() == robotId.intValue()){
				return monsterFreshInforVo;
			}
		}
		return null;
	}
	
	public void sendRoomAboradScoreRankUpdate(){
		List<PVPPVEActivityStatusVo> all =new ArrayList<PVPPVEActivityStatusVo>();
		for (Fighter player : cellData.getAllCellPlayers()) {
			all.add(player.rolePo.pVPPVEActivityStatusVo);
		}
		Collections.sort(all);
		int index=1;
		for (PVPPVEActivityStatusVo pVPPVEActivityStatusVo : all) {
			pVPPVEActivityStatusVo.myRank=index++;
		}
		int maxIndex=Math.min(all.size(),10);
		all=all.subList(0, maxIndex);
		sendRoomAboradMsg("PushRemoting.sendUpdatePVPPVEActivityRank",new Object[]{all});
	}
	
	
	public void sendRoomAboradMsg(String order,Object[] objs){
		List<Fighter> players = cellData.getAllCellPlayers();
		System.out.println("              ");
//		System.out.println("开始=========================================================");
		for (Fighter fighter : players) {
//			System.out.println("fighter.name = "+fighter.name +"; objs = " +objs[0]);
			fighter.rolePo.singleRole(order, objs,true);
		}
//		System.out.println("结束=========================================================");
	}


	public List<Entity> fetchAllMoves(Fighter fighter,int limit) {
		List<Entity> all = new ArrayList<Entity>();
		List<Cell> cells = cellData.getNearByCells(fighter.cell,limit);
		for(Cell cell:cells)
		{
			all.addAll(cell.getAllLivings());
		}
		all.remove(fighter);
		return all;
	}
	
	

	public void launchRegularTask(int runTimeIndex) {
		try {
				//每500秒钟处理每个对象
				for (Fighter fighter : fighterIdMaps.values()) {
					//每秒
					if(runTimeIndex%2==0){
						fighter.checkIntervalBuffer();
						fighter.checkRemoveExpireBuffer();
					}
					//每12.5秒
					if(runTimeIndex%25==0)
					{
						//玩家
						if(fighter.type == Entity.MOVER_TYPE_PLAYER){
							fighter.checkResetPkStatus();
							fighter.doMpChange(fighter.batMpReg,true);
							fighter.doHpChange(fighter.batHpReg,1,0,fighter,true);
						}
						//怪物
						else if(fighter.type == Entity.MOVER_TYPE_MONSTER)
						{
							Integer change = null;
							if(fighter.lastHitTime!=0l){
								change = fighter.batHpReg;
							}
							if(fighter.monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE2&&fighter.lastHitTime!=0l && (System.currentTimeMillis()-fighter.lastHitTime)>=60*1000){
								fighter.lastHitTime=0l;
								fighter.roleHitTotalCount.clear();
								fighter.fightSkillOnMe.clear();
								if(fighter.batMaxHp>fighter.getBatHp()){
									change = fighter.batMaxHp-fighter.getBatHp();
								}
							}
							if(change!=null&&change.intValue()>0){
//								PrintUtil.print("change ========== "+change);
								fighter.doHpChange(change.intValue(),1,0,fighter,true);
							}
//							fighter.checkResetHitStatus();
//							//飙车
//							if(fighter.wasYunDart==1){
//								fighter.doHpChange(fighter.batHpReg,1,0,fighter,true);
//							}
//							else{
//								if(fighter.monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE2){
//									PrintUtil.print("------------------------------------boss:"+fighter.monsterPo.getName());
//									fighter.doHpChange(fighter.batHpReg,1,0,fighter,true);
//								}
//							}
						}
						else if(fighter.type == Entity.MOVER_TYPE_PET){
							fighter.doHpChange(fighter.batHpReg,1,0,fighter,true);
						}
					}
				}
			//每秒钟刷怪物
			if(runTimeIndex%2==0){
				Integer bossRefreshTime =GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_BOSS_REFRESH);
				for (MonsterFreshInforVo monsterFreshInforVo : monsterFreshInforVos) {
					MonsterFreshPo monsterFreshPo =monsterFreshInforVo.monsterFreshPo;
					if(monsterFreshPo==null){
						PrintUtil.print("monsterFreshPo不存在："+monsterFreshInforVo.monsterFreshId);
						continue;
					}
					if(monsterFreshInforVo.disapperTime!=0){
						long deltaMs=System.currentTimeMillis()-monsterFreshInforVo.disapperTime;
						MonsterPo monsterPo=monsterFreshInforVo.monsterPo;
						if(monsterPo.getHorseAbroad().intValue() == 1 && wasBossFlushNotice ){
							if(bossRefreshTime != null && bossRefreshTime!=0 && monsterPo.getBossUsage().intValue() == 1){
								if(deltaMs>= (bossRefreshTime*1000*60*60-5*60*1000)){
									ChatService chatService=ChatService.instance();
									StringBuffer sb = new StringBuffer();			
									sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key13"));
									sb.append(ColourType.COLOUR_YELLOW).append(monsterPo.getName());
									sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key16"));
									sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key15"));
									wasBossFlushNotice = false;
									chatService.sendHorse(sb.toString());
								}
							}
							else{
								if(deltaMs>= (monsterFreshPo.getFreshSeconds().longValue()-5*60*1000)){
									if(monsterPo.getHorseAbroad().intValue() == 1 && wasBossFlushNotice){
										ChatService chatService=ChatService.instance();
										StringBuffer sb = new StringBuffer();			
										sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key13"));
										sb.append(ColourType.COLOUR_YELLOW).append(monsterPo.getName());
										sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key16"));
										sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key15"));
										wasBossFlushNotice = false;
										chatService.sendHorse(sb.toString());
									}
								}			
							}	
						}
						if(monsterPo.getBossUsage().intValue() == 1||monsterPo.getBossUsage().intValue() == 3){
							if(bossRefreshTime != null && bossRefreshTime>0){
								if(deltaMs>=1000L*60*60*bossRefreshTime){
									spawnMonster(monsterFreshInforVo);
								}	
							}else{
								if(deltaMs>=monsterFreshPo.getFreshSeconds().longValue()){
									spawnMonster(monsterFreshInforVo);
								}
							}
						}else if(monsterPo.getBossUsage().intValue() == 0){
							if(deltaMs>=monsterFreshPo.getFreshSeconds().longValue()){
								spawnMonster(monsterFreshInforVo);
							}
						}
					}
				}
			}
			if(runTimeIndex%2==0){
				List<Treasure> toRemove =new ArrayList<Treasure>();
				for (Treasure treasure : treasures) {
					if(treasure.dropTime!=0l && (System.currentTimeMillis()-treasure.dropTime)>=1000*60*30){
						toRemove.add(treasure);
					}
				}

				for (Treasure treasure2 : toRemove) {
					doPickTreasureFromStage(treasure2,null);
				}
			}

		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

//	public void singleBattleMsg(ChannelHandlerContext session,String msg) {
//		StringBuilder sb=new StringBuilder();
//		sb.append(ServerPack.SPECIAL_ORDER_ID_BATTLE);
//		sb.append("||");
//		sb.append(ThreadLocalUtil.fetchCurrentThreadId());
//		sb.append("#");
//		sb.append(msg);
//		if(session!=null){
//			BaseSessionUtil.send(session, sb.toString(), true);
//		}
//	}

	public static MapRoom findStage(Integer roomId) {
		return MapWorld.findStage(roomId);
	}
	
	
	/**
	 * 视野内取生物对象
	 * @param id
	 * @return
	 */
//	public Fighter findEntityById(Fighter fighter, Integer id,int limit) {
//		if(fighter.cell != null)
//		{
//			List<Cell> cells = cellData.getNearByCells(fighter.cell,limit);
//			for(Cell cell:cells)
//			{
//				List<Entity> livings = cell.getAllLivings();
//				for(Entity living:livings)
//				{
//					if(living.mapUniqId==id.intValue() && living instanceof Fighter)
//						return (Fighter)living;
//				}
//			}
//		}
//		return null;
//	}
	
//	public Fighter findPlayerById(Integer id) {
//		for (int i = 0; i < players.size(); i++) {
//			Fighter mover=players.get(i);
//			if(mover.mapUniqId==id.intValue()){
//				return mover;
//			}
//		}
//		return null;
//	}
	
//	public Fighter findMonsterById(Integer id) {
//		for (int i = 0; i < monsters.size(); i++) {
//			Fighter mover=monsters.get(i);
//			if(mover.mapUniqId==id.intValue()){
//				return mover;
//			}
//		}
//		return null;
//	}
	
	
	
//	public Fighter findPlayer(Integer roleId) {
//		for (int i = 0; i < players.size(); i++) {
//			Fighter mover=players.get(i);
//			if(mover.type==Entity.MOVER_TYPE_PLAYER){
//				if(mover.itemId==roleId.intValue()){
//					return mover;
//				}
//			}
//		}
//		return null;
//	}
	
//	public Fighter findMonster(Integer roleId) {
//		for (int i = 0; i < players.size(); i++) {
//			Fighter mover=players.get(i);
//			if(mover.type==Entity.MOVER_TYPE_PLAYER){
//				if(mover.itemId==roleId.intValue()){
//					return mover;
//				}
//			}
//		}
//		return null;
//	}

	/**
	 * 对象死亡处理
	 * @param killer 技能施放者
	 * @param deadFighter 死亡者
	 */
	public void doMonsterDie(Fighter killer,Fighter deadFighter) {
		if(killer !=null && killer.master!=null){
			killer=killer.master.fighter;
		}
		ChatService chatService=ChatService.instance();

		//怪物死亡
		if(killer!=null && deadFighter.type==Entity.MOVER_TYPE_MONSTER){
			MonsterPo monsterPo = MonsterPo.findEntity(deadFighter.itemId);
			//决定击杀授权
			List<RolePo> finalSlaughters = deadFighter.generateFinalSlaughters(killer);
			
			//准备道具掉落
			List<DropPo> totalDropList = DropPo.makeDropListByExp(monsterPo.listItemDrop);
			//准备任务道具掉落
			if(killer!= null && killer.rolePo!=null){
				List<DropPo> totalTaskDropList = DropPo.makeDropListByListExp(killer.rolePo,monsterPo.listItemDropTask);
				if(finalSlaughters!= null && finalSlaughters.size() != 0){
					for(RolePo rp : finalSlaughters){
						for(DropPo dp : totalTaskDropList){
							rp.addItem(dp.getItemId(), dp.getNum(), 1);						
						}
					}
				}
			}
			
			// 运营活动-开服有礼
			List<LiveActivityPo> liveActivityPos =GlobalCache.fetchLiveActivityPosAll();
			for(LiveActivityPo liveActivityPo:liveActivityPos){
				if(liveActivityPo.getType()==LiveActivityType.LiveActivity_EXCHANGE){
					if(liveActivityPo != null && deadFighter!=null && deadFighter.lv.intValue() > 20){
						if(liveActivityPo.wasLiveActivityOpen()){
							List<IdNumberVo> invList = liveActivityPo.listRateItems;
							List<IdNumberVo2> inv2List = new ArrayList<IdNumberVo2>();
							for(IdNumberVo idNumberVo : invList){
								inv2List.add(new IdNumberVo2(idNumberVo.getId(), idNumberVo.getNum(), 1));
							}
							List<DropPo> liveActivityList = DropPo.makeDropListByExp(inv2List);
							totalDropList.addAll(liveActivityList);
						}
					}
				}
			}
			//生成掉落字符串掉落部分
			List<byte[]> finalDrop = new ArrayList<byte[]>();
			for (int j = 0; j < totalDropList.size(); j++) {
				DropPo dropPo = totalDropList.get(j);
				List<RolePo>  finalOwnership = new ArrayList<RolePo>();
				if(finalSlaughters!= null && finalSlaughters.size() != 0){
					int n = RandomUtil.randomInteger(finalSlaughters.size());
					finalOwnership.add(finalSlaughters.get(n));
				}
				Treasure treasure = Treasure.create(finalOwnership,dropPo.getItemId(), dropPo.getNum(),dropPo,this,deadFighter.x,deadFighter.y,deadFighter.z);
				byte[] dropInfor = treasure.makeDrop();
				finalDrop.add(dropInfor);
			}
			//给授权击杀的玩家结算
			for (RolePo rolePo : finalSlaughters) {
				int currentZoneOtherTeamMemberCount = rolePo.fetchCurrentZoneOtherTeamMembers();
				//怪物经验*1.5*（玩家等级/可以分享到经验的玩家等级总和
				int exp=0;
				if(rolePo.teamMemberVo!=null && currentZoneOtherTeamMemberCount>0){
//					PrintUtil.print("currentZoneOtherTeamMemberCount:"+currentZoneOtherTeamMemberCount);
					exp=DoubleUtil.toUpInt(1.5d*rolePo.getLv()*deadFighter.monsterPo.exp/(rolePo.getLv()+currentZoneOtherTeamMemberCount));
				}
				else{
					exp=deadFighter.monsterPo.exp;
				}
				// 运营活动加成经验百分比
				Integer liveExpPercent =GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_EXP);
				// 经验丹加成经验百分比	
				exp =exp +Math.min(5000, (exp*(rolePo.expPercent.intValue()+liveExpPercent.intValue())/100));
				rolePo.adjustExp(exp);
				LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_EXP, 0, exp, GlobalCache.fetchLanguageMap("key2341"), "");
				rolePo.sendUpdateExpAndLv(false);
				//追加进度
				rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_702,monsterPo.getId(),null);
				rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_738,monsterPo.getId(),null);
				rolePo.taskConditionProgressAdd(1, TaskType.TASK_TYPE_CONDITION_741, monsterPo.getLv(), null);
			}	
			//广播怪物死亡
			BattleMsgUtil.abroadDie(deadFighter, killer, finalDrop);
			//旗帜类特殊处理
//			PrintUtil.print("MonsterType="+monsterPo.getMonsterType());
			if(monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE7){
				GuildPo guildPo = GuildPo.findEntity(killer.rolePo.getGuildId());
				if(guildPo != null && sceneId != MapType.SIEGE_SCENE_MAP){
					FlagPo flagPo = FlagPo.findFlagBySceneId(sceneId);
					if(flagPo.getFlagStatus().intValue() == 1){
						flagPo.setGuildId(guildPo.getId());
					}
				}
				
				CopySceneActivityPo csap = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_SIEGE_WAR);
				if(guildPo != null && csap!=null && csap.getActivityWasOpen().intValue() == 1){
					GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
					SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
					if(siegeBidVo.guildId1.intValue() == guildPo.getId().intValue() ||
						siegeBidVo.guildId2.intValue() == guildPo.getId().intValue() ||
						siegeBidVo.guildId3.intValue() == guildPo.getId().intValue() ||
						siegeBidVo.lastGuildId.intValue()==guildPo.getId().intValue()){
						FlagPo flagPo = FlagPo.findFlagBySceneId(sceneId);
						if(flagPo.getFlagStatus().intValue() == 1 &&sceneId == MapType.SIEGE_SCENE_MAP){
							flagPo.setGuildId(guildPo.getId());
							siegeBidVo.ownerGuildLeaderName=guildPo.getLeaderRoleName();
							siegeBidVo.ownerGuildName=guildPo.getName();
							siegeBidVo.ownerGuidId=guildPo.getId();
							siegeBidVo.ownerGuildLeaderWeaponAvatar=killer.weaponAvatar;
							siegeBidVo.ownerGuildLeaderModelAvatar=killer.modelAvatar;
							siegeBidVo.ownerGuildLeaderWingAvatar=killer.wingAvatar;
							siegeBidVo.ownerGuildLeaderId=guildPo.getLeaderRoleId();
							siegeBidVo.save();
							StringBuffer sb2 = new StringBuffer();
							sb2.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key17")).append(guildPo.getName()).append(GlobalCache.fetchLanguageMap("key18")).append(ScenePo.findEntity(sceneId).getName()).append(GlobalCache.fetchLanguageMap("key19"));
							chatService.sendHorse(sb2.toString());
							chatService.sendSystemWorldChat(sb2.toString());
							
							if(guildPo!=null){
								try {
									for (GuildMemberVo guildMemberVo : guildPo.listMembers) {
										RolePo guildRole=RolePo.findEntity(guildMemberVo.roleId);
										if(guildRole!=null && guildRole.fighter!=null){
											guildRole.fighter.changeWasSiegeBid();
										}
									}
								} catch (Exception e) {
									ExceptionUtil.processException(e);
								}
							}
						}
					}
				}		
			}		
			
			// 定时boss
			if(monsterPo.getBossUsage().intValue()==2){
//				PrintUtil.print("*****************定时BOSS");
				RolePo killRolePo = killer.rolePo;
				RolePo maxHitter = deadFighter.fetchDeathMaxHitter();
				if(maxHitter ==null){
					maxHitter=killRolePo;	
				}
				GuildPo guildPo = GuildPo.findEntity(maxHitter.getGuildId());
				if(guildPo != null){
					GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyTimerBoss);
					TimerBossVo timerBossVo = (TimerBossVo) globalPo.valueObj;
					timerBossVo.bossState=0;
					timerBossVo.killGuildId=guildPo.getId();
					timerBossVo.adjustkillNum(1);
					timerBossVo.killGuildName=guildPo.getName();
					timerBossVo.killTime=System.currentTimeMillis();
					if(killRolePo != null){
						timerBossVo.killRoleId=killRolePo.getId();						
						timerBossVo.killName=killRolePo.getName();
					}
					List<Integer> roleIdList = new ArrayList<Integer>();
					MailService mailService = (MailService) BeanUtil.getBean("mailService");
					for(GuildMemberVo guildMemberVo : guildPo.listMembers){
						roleIdList.add(guildMemberVo.roleId);
					}
					PoseidonAward poseidonAward =XmlCache.xmlFiles.constantFile.guild.poseidonAward;
					List<IdNumberVo> awardList = IdNumberVo.createList(poseidonAward.award);
					StringBuilder sb = new StringBuilder();
					for(int i=0; i< awardList.size(); i++){
						IdNumberVo idNumberVo = awardList.get(i);
						if(i!=0){
							sb.append(",");
						}
						 sb.append(1);
						 sb.append("|");
						 sb.append(idNumberVo.getId());
						 sb.append("|");
						 sb.append(idNumberVo.getNum());
						 sb.append("|");
						 sb.append(1);
					}
//					PrintUtil.print("sendSystemMail: "+GlobalCache.fetchLanguageMap("key2682")+";奖励："+sb.toString());
					mailService.sendDirectSystemMail(roleIdList, GlobalCache.fetchLanguageMap("key2681"), GlobalCache.fetchLanguageMap("key2682"), sb.toString(), 0L, 0L, GlobalCache.fetchLanguageMap("key239"));
					String str =GlobalCache.fetchLanguageMap("key2683");
					String content = MessageFormat.format(str,guildPo.getName());
					chatService.sendHorse(content);
				
				}else{
					PrintUtil.print(" 定时boss死亡没有找到 公会 ： guildId =" +maxHitter.getGuildId()+"; roleId = "+maxHitter.getId());
				}
			}
		}
		//宠物死亡
		else if(deadFighter.type==Entity.MOVER_TYPE_PET){
			BattleMsgUtil.abroadDie(deadFighter, killer,null);
		}
		//人物死亡
		else{
			if(deadFighter.rolePo.getPkStatus()==Fighter.PK_STATUS_RED&&deadFighter.robot==false&&isDynamic==false){
				GlobalCache.redRoleDieMap.put(deadFighter.rolePo.getId(), true);
//				PrintUtil.print(deadFighter.rolePo.getName()+" dead "+deadFighter.rolePo.getId());
			}
			if(killer!=null && killer.rolePo!=null ){
				if(deadFighter.rolePo.getPkStatus()==Fighter.PK_STATUS_PEACE && deadFighter.robot==false){
					boolean shouldAdd=false;
					if(killer.type==Entity.MOVER_TYPE_PLAYER){
						if(!killer.robot){
							shouldAdd=true;	
						}
					}
					if(killer.type==Entity.MOVER_TYPE_PET){
						shouldAdd=true;
					}
					if(shouldAdd){
						if(ScenePo.findEntity(sceneId).getRedName()==1){
							CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_GUILD_WAR);
							CopySceneActivityPo copySceneActivityPo2 = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_SIEGE_WAR);
							boolean couldAdjustPk=true;
							//公会战期间
							if(copySceneActivityPo.getActivityWasOpen().intValue() == 1){
								//在公会冲突领地
								if(ArrayUtil.intInArray(sceneId, new int[]{20101004,20101005,20101008,20101009,20101010})){
									couldAdjustPk=false;
								}
							}
							//那路亚期间
							if(copySceneActivityPo2.getActivityWasOpen().intValue() == 1){
								if(sceneId==20101007){
									couldAdjustPk=false;
								}
							}
							if(couldAdjustPk){
//								PrintUtil.print("=================杀人增加PK值：1");
								killer.rolePo.adjustPkValue(1);
								LogUtil.writeLog(deadFighter.rolePo, 220, killer.rolePo.getRoleInforId(), 0, 0, GlobalCache.fetchLanguageMap("key2342"), "");
								LogUtil.writeLog(killer.rolePo, 1, 13, 0, 1, GlobalCache.fetchLanguageMap("key2343"), "");
							}

						}
					}
				}

//				if(deadFighter.rolePo.getPkStatus()==Fighter.PK_STATUS_PEACE && deadFighter.robot==false){
//					if(!killer.robot){
//						if(ScenePo.findEntity(sceneId).getRedName()==1){
//							CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_GUILD_WAR);
//							CopySceneActivityPo copySceneActivityPo2 = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_SIEGE_WAR);
//							if(copySceneActivityPo.getActivityWasOpen().intValue() != 1 && copySceneActivityPo2.getActivityWasOpen().intValue() != 1){
//								killer.rolePo.adjustPkValue(1);
//								LogUtil.writeLog(deadFighter.rolePo, 220, killer.rolePo.getRoleInforId(), 0, 0, GlobalCache.fetchLanguageMap("key2342"), "");
//								LogUtil.writeLog(killer.rolePo, 1, 13, 0, 1, GlobalCache.fetchLanguageMap("key2343"), "");
//							}
//						}
//					}
//				}
				
				if(deadFighter!=null && deadFighter.robot==false && deadFighter.type==Entity.MOVER_TYPE_PLAYER &&killer.robot==false && killer.type== Entity.MOVER_TYPE_PLAYER){
					RolePo dead = RolePo.findEntity(deadFighter.itemId);
					if(dead != null){
						if(dead.addEnemy(killer.itemId)){
							dead.setWasNewEnemy(1);
							dead.sendUpdateWasNewEnemy();							
						}
						killer.addSustainKill(dead.getId());
					}
					deadFighter.clearSustainKill();
				}
				
				if(deadFighter.pkStatus == Fighter.PK_STATUS_RED){
					killer.rolePo.taskConditionProgressAdd(1,TaskType.TASK_TYPE_CONDITION_718,null,null);
				}
				
				//if(ScenePo.findEntity(sceneId).getDropAble()==1 || (deadFighter.pkStatus==Fighter.PK_STATUS_RED && deadFighter.robotType != Entity.ROBOT_TYPE_ARENA)){
				if(ScenePo.findEntity(sceneId).getDropAble()==1){
					boolean redName=false;
					if(deadFighter.pkStatus==Fighter.PK_STATUS_RED){
						redName=true;
					}
					List<DropPo> finalDrops = new ArrayList<DropPo>();
					finalDrops.addAll(dropFromBody(killer, deadFighter, redName));
					finalDrops.addAll(dropFromPackage(killer, deadFighter, redName));
					List<byte[]> finalDrop = new ArrayList<byte[]>();
					StringBuffer sb = new StringBuffer();
					sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key20"));
					sb.append(ColourType.COLOUR_GOLDEN).append("【").append(killer.name).append("】");
					sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key21"));
					sb.append(ColourType.COLOUR_GOLDEN).append("【").append(deadFighter.name).append("】");
					StringBuilder dropItemString = new StringBuilder();
					if(!finalDrop.isEmpty()){
						sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key22"));
						int index = 0;
						for(DropPo dropPo:finalDrops){
							ItemPo itemPo = null;
							if(dropPo.getType()==ItemType.ITEM_BIG_TYPE_EQUIP){
								EqpPo eqpPo = EqpPo.findEntity(dropPo.getItemId());
								itemPo = eqpPo.itemPo();
							}else{
								itemPo = ItemPo.findEntity(dropPo.getItemId());
							}
							sb.append(ColourType.fetchColourByQuality(itemPo.getQuality())).append("【").append(itemPo.getName()).append("】");
							dropItemString.append(",").append(itemPo.getName());
							if(index>0){
								dropItemString.append(",").append(itemPo.getName());
							}else{
								dropItemString.append(itemPo.getName());
							}
							index++;
						}
						MailService mailService = (MailService) BeanUtil.getBean("mailService");
						String time = DateUtil.getFormatDateBytimestamp(System.currentTimeMillis());
						String str = GlobalCache.fetchLanguageMap("key2684");
						String content = MessageFormat.format(str, time , killer.name,dropItemString.toString());
						mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key239"), deadFighter.rolePo.getId(), "PK", content, null, MailType.MAIL_TYPE_SYSTEM);
					}
					sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key23"));
					chatService.sendHorse(sb.toString());
					List<RolePo> list =new ArrayList<RolePo>();
					list.add(killer.rolePo);
					for(DropPo dropPo:finalDrops){
						ItemPo itemPo = null;
						if(dropPo.getType()==ItemType.ITEM_BIG_TYPE_EQUIP){
							EqpPo eqpPo = EqpPo.findEntity(dropPo.getItemId());
							itemPo = eqpPo.itemPo();
						}else{
							itemPo = ItemPo.findEntity(dropPo.getItemId());
						}
						Treasure treasure = Treasure.create(list,itemPo.getId(), dropPo.getNum(),dropPo,this,deadFighter.x,deadFighter.y,deadFighter.z);
						byte[] dropInfor = treasure.makeDrop();
						finalDrop.add(dropInfor);
					}
					BattleMsgUtil.abroadDie(deadFighter, killer, finalDrop);
				}
				else{
					BattleMsgUtil.abroadDie(deadFighter, killer, null);
				}
			}
			else{
				BattleMsgUtil.abroadDie(deadFighter, killer,null);
			}
			if(deadFighter!=null && !deadFighter.robot && deadFighter.rolePo!=null && deadFighter.rolePo.fighterPet !=null){
				BattleMsgUtil.abroadDie(deadFighter.rolePo.fighterPet, null,null);
				cellData.removeLiving(deadFighter.rolePo.fighterPet, false);
				deadFighter.rolePo.fighterPet=null;
			}
		}
		
		//单位移除
		cellData.removeLiving(deadFighter, false);
		
		//判断是否是镖车对象
		MonsterPo monsterPoYunCart = MonsterPo.findEntity(deadFighter.itemId);
		if(monsterPoYunCart != null){
			if(monsterPoYunCart.getMonsterType().intValue() == 8){
				RolePo targetRolePo = deadFighter.master;
				RolePo casterRolePo = killer.rolePo;
				if(targetRolePo != null){
					List<Cart> cartList = XmlCache.xmlFiles.constantFile.trade.cart;
					if(casterRolePo != null){
						Cart awardCart= null;
						if(targetRolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality!=-1){
							awardCart = cartList.get(targetRolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
						}else{
							awardCart = cartList.get(1);
						}
						int param = targetRolePo.getLv() -15;
						if(param < 0){
							param =1;
						}
						int baseExp = 12000*param;
						int basePrestige = 2000;
						int totalExp = baseExp*awardCart.expPar/1000;
						int totalPrestige = basePrestige*awardCart.prestigePar/500;
//						PrintUtil.print(casterRolePo.getName() + " totalExp="+totalExp);
//						PrintUtil.print(casterRolePo.getName() + " " + basePrestige +" * "+awardCart.prestigePar+"/500"+"="+totalPrestige);
						casterRolePo.adjustExp(totalExp);
						casterRolePo.adjustPrestige(totalPrestige);
						casterRolePo.sendUpdateExpAndLv(true);
//						PrintUtil.print("casterRolePo.name="+casterRolePo.getName() +" ==>"+" targetRolePo.name="+targetRolePo.getName());
					}
					Cart whiteCart = cartList.get(0);
					targetRolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality=whiteCart.quality;
//					PrintUtil.print("whiteCart.quality=" +whiteCart.quality+ "|| "+targetRolePo.getName()+" || currentYunDartCarQuality="+targetRolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality);
					MapRoom mapRoom = MapWorld.findStage(targetRolePo.getRoomId());
//					PrintUtil.print("mapRoomId="+mapRoom.mapRoomId+"; sceneId="+mapRoom.sceneId+"; "+ScenePo.findEntity(mapRoom.sceneId).getName());
					mapRoom.spawnYunDartFighter(whiteCart.monsterId, targetRolePo);
					targetRolePo.sendUpdateYunDartTaskInfo();
				}	
			}			
		}
		//玩家死亡后续事件
		if(deadFighter.type == Entity.MOVER_TYPE_PLAYER && !deadFighter.robot)
		{
			RoleTemplate roleTemplate=RoleTemplate.instance();
			ChannelHandlerContext session = (ChannelHandlerContext) roleTemplate.iuidSessionMapping.get(deadFighter.rolePo.getIuid());
			if(deadFighter.rolePo != null){
//				PrintUtil.print(deadFighter.rolePo.getBatHp()+"/"+deadFighter.rolePo.getBatMaxHp());
				deadFighter.rolePo.setBatHp(0);				
			}
//			if(deadFighter.rolePo != null && deadFighter.rolePo.fighterPet != null){
//				Fighter petFighter = deadFighter.rolePo.fighterPet;
//				BattleMsgUtil.abroadDie(petFighter, killer, null);				
//			}
		}
		
		if(deadFighter.type!=Entity.MOVER_TYPE_PLAYER && deadFighter.type!=Entity.MOVER_TYPE_PET){
			MonsterFreshInforVo monsterFreshInforVo = findMonsterFreshInfor(deadFighter.monsterFreshId);
			if(monsterFreshInforVo!=null){
				monsterFreshInforVo.disapperTime=System.currentTimeMillis();
				monsterFreshInforVo.lastKiller=killer.name;
				MonsterFreshPo monsterFreshPo =MonsterFreshPo.findEntity(monsterFreshInforVo.monsterFreshId);
				if(monsterFreshPo!=null){
					MonsterPo monsterPo=MonsterPo.findEntity(monsterFreshPo.getMonsterId());
					if(monsterPo!=null ){
						if(monsterPo.getBossUsage().intValue()==1){
							GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get("keyStaticMapBossInfo");
							globalPo.updateStaticMapBossInfo(monsterFreshInforVo);
						}else if(monsterPo.getBossUsage().intValue()==3){
							GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyBossSecretRoom);
							globalPo.updateStaticMapBossInfo(monsterFreshInforVo);
						}
					}
				}
				
			}
			else{
//				ExceptionUtil.throwConfirmParamException("坑爹的怪物不刷新了:"+deadFighter.monsterFreshId);
			}
		}
	}
	
	private List<DropPo> dropFromBody(Fighter killer,Fighter deadFighter, boolean redName){
		RolePo deadFighterPo =deadFighter.rolePo;
		List<EqpPo> totalDropList=new ArrayList<EqpPo>();
		List<Integer> totalSlotList=new ArrayList<Integer>();
		List<DropPo> finalDrop = new ArrayList<DropPo>();
		if(deadFighterPo.equipWeapon!=null && deadFighterPo.equipWeapon.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipWeapon);
			totalSlotList.add(ItemType.ITEM_CATEGORY_WEAPON);
		}
		if(deadFighterPo.equipNecklace!=null && deadFighterPo.equipNecklace.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipNecklace);
			totalSlotList.add(ItemType.ITEM_CATEGORY_NECKLACE);
		}
		if(deadFighterPo.equipRing!=null && deadFighterPo.equipRing.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipRing);
			totalSlotList.add(ItemType.ITEM_CATEGORY_RING);
		}
		if(deadFighterPo.equipBracelet!=null && deadFighterPo.equipBracelet.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipBracelet);
			totalSlotList.add(ItemType.ITEM_CATEGORY_BRACELET);
		}
		if(deadFighterPo.equipArmor!=null && deadFighterPo.equipArmor.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipArmor);
			totalSlotList.add(ItemType.ITEM_CATEGORY_ARMOR);
		}
		if(deadFighterPo.equipPants!=null && deadFighterPo.equipPants.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipPants);
			totalSlotList.add(ItemType.ITEM_CATEGORY_PANTS);
		}
		if(deadFighterPo.equipShoe!=null && deadFighterPo.equipShoe.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipShoe);
			totalSlotList.add(ItemType.ITEM_CATEGORY_SHOE);
		}
		if(deadFighterPo.equipBracer!=null && deadFighterPo.equipBracer.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipBracer);
			totalSlotList.add(ItemType.ITEM_CATEGORY_BRACER);
		}
		if(deadFighterPo.equipHelmet!=null && deadFighterPo.equipHelmet.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipHelmet);
			totalSlotList.add(ItemType.ITEM_CATEGORY_HELMET);
		}
		if(deadFighterPo.equipBelt!=null && deadFighterPo.equipBelt.getBindStatus()==0){
			totalDropList.add(deadFighterPo.equipBelt);
			totalSlotList.add(ItemType.ITEM_CATEGORY_BELT);
		}
		if(totalDropList.size()>0){
			if(deadFighterPo.wasRobot())
			{
				int size = totalDropList.size();
				int num = IntUtil.getRandomInt(0, (size-1));
				EqpPo dropEqp = totalDropList.get(num);
				DropPo dropPo = new DropPo();
				dropPo.setType(ItemType.ITEM_BIG_TYPE_ITEM);
				dropPo.setItemId(dropEqp.getItemId());
				dropPo.setNum(1);
				finalDrop.add(dropPo);
			}
			else
			{
				for (int j = 0; j < totalDropList.size(); j++) {
					EqpPo dropEqp = totalDropList.get(j);
					IdNumberVo poss = Fighter.getDropPoss(dropEqp.itemPo().getQuality(),redName);
					if(!RandomUtil.random1W(poss.getId())){
						continue;
					}
					Integer slotType = totalSlotList.get(j);
					DropPo dropPo = new DropPo();
					dropPo.setType(ItemType.ITEM_BIG_TYPE_EQUIP);
					dropPo.setItemId(dropEqp.getId());
					dropPo.setNum(poss.getNum());
					finalDrop.add(dropPo);
					if(!deadFighterPo.robot){
						deadFighterPo.unEquipBySlot(slotType, false);
						LogUtil.writeLog(deadFighterPo, 1, ItemType.LOG_TYPE_ITEM, dropEqp.getItemId(), -1, "PK", "powerLv"+dropEqp.getPowerLv());
						deadFighterPo.sendUpdateRoleEquipSlot(slotType);
						deadFighterPo.calculateBat(1);
					}
				}
			}
		}
		return finalDrop;
	}
	
	private List<DropPo> dropFromPackage(Fighter killer,Fighter deadFighter, boolean redName){
		IdNumberVo poss = Fighter.getDropPoss(99,redName);
		if(!RandomUtil.random1W(poss.getId())){
			return new ArrayList<DropPo>();
		}
		RolePo deadFighterPo = deadFighter.rolePo;
		List<DropPo> finalDrop = new ArrayList<DropPo>();
		ConcurrentHashMap<String, RolePackItemVo> rolePackItemVos = deadFighterPo.mainPackItemVosMap;
		List<Object> dropPackItemVos = new ArrayList<Object>();
		List<Integer> weights = new ArrayList<Integer>();
		for(RolePackItemVo rolePackItemVo:rolePackItemVos.values()){
			if(rolePackItemVo.bindStatus!=0){
				continue;
			}
			ItemPo itemPo = rolePackItemVo.itemPo();
			if(itemPo.getDropWeight()==null){
				continue;
			}
			dropPackItemVos.add(rolePackItemVo);
			weights.add(itemPo.getDropWeight());
		}
		List<Object> filterList = new ArrayList<Object>();
		if(dropPackItemVos.size()<=poss.getNum()){
			for(Object object:dropPackItemVos){
				filterList.add(object);
			}
		}else{
			for(int i=0;i<poss.getNum();i++){
				Object vo = RandomUtil.randomObjectByPecentage(dropPackItemVos, weights, filterList);
				filterList.add(vo);
			}
		}
		for(Object object:filterList){
			RolePackItemVo rolePackItemVo = (RolePackItemVo)object;
			DropPo dropPo = new DropPo();
			if(rolePackItemVo.wasEquip()){
				dropPo.setType(ItemType.ITEM_BIG_TYPE_EQUIP);
				dropPo.setItemId(rolePackItemVo.getEquipId());
			}else{
				dropPo.setType(ItemType.ITEM_BIG_TYPE_ITEM);
				dropPo.setItemId(rolePackItemVo.getItemId());
			}
//			PrintUtil.print("被杀掉落：是否装备："+rolePackItemVo.wasEquip()+" ID:"+rolePackItemVo.getItemId()+" equipId:"+rolePackItemVo.getEquipId());
			deadFighterPo.removeItemFromMainPack(rolePackItemVo.getIndex(), 1, "PK", false);
			dropPo.setNum(1);
			finalDrop.add(dropPo);
		}
		return finalDrop;
	}
	
	public Fighter findMoverId(Integer targetId) {
		return fighterIdMaps.get(targetId);
	}
	

//	public Fighter findMoveryId(RolePo rolePo, Integer targetId,int limit) {
//		List<Cell> cells =cellData.getNearByCells(rolePo.getX(), rolePo.getY(), rolePo.getZ(),limit);
//		for(Cell cell:cells)
//		{
//			Collection<Entity> livings = cell.getAllLivings();
//			for(Entity living:livings)
//			{
//				if(living.mapUniqId==targetId.intValue()){
//					if(living instanceof Fighter)
//						return (Fighter)living;
//					break;
//				}
//			}
//		}
//		return null;
//	}
	
	/** 在所有单元格内查找生物 */
//	public Fighter findMoveryIdByAllCell(Integer targetId){
//		for(Cell cell:cellData.allCellList)
//		{
//			Collection<Entity> livings = cell.getAllLivings();
//			for(Entity living:livings)
//			{
//				if(living.mapUniqId==targetId.intValue()){
//					if(living instanceof Fighter)
//						return (Fighter)living;
//				}
//			}
//		}
//		return null;
//	}
	
	
	public Treasure findTreasureById(Integer treasureId) {
		for (int i = 0; i < treasures.size(); i++) {
			Treasure treasure=treasures.get(i);
			if(treasure.mapUniqId==treasureId.intValue()){
				return treasure;
			}
		}
		return null;
	}


	public void doAddBuffer(Fighter fighter, Integer buffId) {
//		PrintUtil.print("MapRoom.doAddBuffer() fighter.name="+fighter.name+"; buffId="+buffId);
		BuffPo buffPo=BuffPo.findEntity(buffId);
		List<Fighter> receiveFighters = new ArrayList<Fighter>();
		receiveFighters.add(fighter);
		BufferStatusVo bufferStatusVo =new BufferStatusVo(buffPo, fighter, receiveFighters);
//		PrintUtil.print("fighter = " + fighter.name);
		fighter.makeAddBuff(bufferStatusVo, fighter ,true);
	}

	public void doPickTreasureFromStage(Treasure treasure, Fighter fighter) {
		treasures.remove(treasure);
//		Treasure.treasureTokenMaps.remove(treasure.mapUniqId);
//		BattleMsgUtil.abroadRemoveTreasure(fighter,treasure);
	}
	
	public void doMonsterBlink(Fighter mover,Float x, Float y,Float z,int newX, int newY, int newZ) {
		this.livingMoveInCell(mover, newX, newY, newZ);
		//更新最新做标
//        if(mover!=null && mover.itemId==420108015){
//        	PrintUtil.print("aaaaaaaaaaaa"+mover.x+" "+mover.y+" "+mover.z);
//        }
		mover.changeX(x);
		mover.changeY(y);
		mover.changeZ(z);
		BattleMsgUtil.abroadMonsterBlink(mover);
	}
	/**
	 * 目标点通知
	 * @param mover
	 * @param x
	 * @param y
	 * @param z
	 * @param newX
	 * @param newY
	 * @param newZ
	 */
	public void doMonsterMove(Fighter mover,Float x, Float y,Float z,int newX, int newY, int newZ) {
		//更新最新做标
//		mover.aimX=x;
//		mover.aimY=y;
//		mover.aimZ=z;
		//生物移动通知发送到Cells中所有玩家，如果生物为玩家则排除玩家自己
		BattleMsgUtil.abroadMonsterMove(mover, x, y, z);
	}
	
	/**
	 * 位置同步
	 * @param mover
	 * @param x
	 * @param y
	 * @param z
	 * @param newX
	 * @param newY
	 * @param newZ
	 */
	public void doMonsterUpdateMove(Fighter mover,float x, float y,float z,int newX, int newY, int newZ) {
		//Cell需要切换就切换
		this.livingMoveInCell(mover, newX, newY, newZ);
//		mover.changeX(x);
//		mover.changeY(y);
//		mover.changeZ(z);
	}
	
	/**
	 * 生物移动如果坐标移出当前Cell范围，则进行切换,这里z坐标不进行计算
	 */
	private void livingMoveInCell(Fighter mover, int newX, int newY, int newZ)
	{
		Cell cell = cellData.getCell(newX, newY, newZ);
//		PrintUtil.print("九宫格切换到新的:"+cell.X+","+cell.Y +" ["+ newX + " " +newZ+"]");
		if(cell != mover.cell)
			cellData.livingMoveInCell(mover, newX, newY, newZ);
	}

	
	public void doAddMoverToStage(Fighter mover,RolePo filter,boolean requireAbroad) {
		//进入新地图cell
		if(!cellData.addLiving(mover)){
			return;
		}
		mover.mapRoom=this;
		onAddMover(mover);
		
		for (BufferStatusVo bufferStatusVo : mover.bufferStatusVos) {
			BattleMsgUtil.abroadAddBuff(mover, bufferStatusVo.buffPo.getId());
		}
		if(requireAbroad){
//			byte[] results=BattleMsgUtil.buildMoverAppearMsg(mover);
//			results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);

//			BattleMsgUtil.abroadBattleMsgWithFilter(this,results,filter,mover,true,BattleMsgUtil.DEFAULT_limit,BattleMsgUtil.BATTLE_MSG_TYPE_APPEAR_LIVING);
		}
	}
	
	
	public void onAddMover(Fighter mover){
		
	}
	
	public void onRemoveMover(Fighter mover){
		
	}
	


	public void doAgentMonster(Fighter player, Fighter monster) {
		monster.changeAgentPlayer(player);
		BattleMsgUtil.abroadClientAgentChange(this, player, monster);
	}

	/**
	 * 分数变化时
	 * @param fighter
	 */
	public void playerScoreChange(Fighter fighter) {
		fighter.rolePo.sendUpdatePVPPVEActivity();
	}
	
	public void disConnectAllRoomPlayers(){
//		RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
//		for (Fighter fighter : cellData.getAllCellPlayers()) {
//			roleService.logoff(fighter.rolePo.fetchSession(), 0);
//		}
	}
	
	public void destroyMeRoom(boolean disConnect){

		if(copySceneConfPo!=null && copySceneConfPo.getSceneId()==KilllingTowerRoom.SCENE_ID){
			KilllingTowerRoom kt = (KilllingTowerRoom)this;
			List<KilllingTowerRoom> totals = new ArrayList<KilllingTowerRoom>();
			totals.addAll(kt.totalRooms);
			for (KilllingTowerRoom room : totals) {
				room.leaderFightRoom=null;
				room.totalRooms.clear();
				room.followRooms.clear();
				room.playerIds.clear();
				if(room.fightThread!=null){
					room.fightThread.fightMapRoom=null;
				}
				MapWorld.removeMapRoom(room.mapRoomId);
				if(disConnect){
					room.disConnectAllRoomPlayers();
				}
			}
		}
		else{
			if(copySceneConfPo!=null){
				PrintUtil.print("活动房间销毁"+copySceneConfPo.getId()+" "+mapRoomId);
			}
			MapWorld.removeMapRoom(mapRoomId);
			if(disConnect){
				disConnectAllRoomPlayers();
			}
		}
	}
	
	/**
	 * 结算房间
	 */
	public void onGameOver(){
		
	}

	public void roomRequireFreshMonsterGroup(Integer groupId) {
//		PrintUtil.print("roomRequireFreshMonsterGroup:"+groupId+" "+mapRoomId+" "+sceneId+" "+new Date().toLocaleString());
		if(groupId.intValue() == 9999)
		{
			buildRobotFighterInfoVos(mapRoomId);
		}
		else
		{
			List<MonsterFreshPo> monsterFreshPos = GameDataTemplate.gameDataListCache.get("MonsterFreshPo");
			for (MonsterFreshPo monsterFreshPo : monsterFreshPos) {
				if(monsterFreshPo.getSceneId().intValue()==sceneId){
					if(monsterFreshPo.getGroupId()==groupId.intValue()){
						MonsterPo monsterPo =MonsterPo.findEntity(monsterFreshPo.getMonsterId());
						if(!isDynamic){
							if(monsterFreshPo.getTag()==0){
								continue;
							}
						}
						Fighter mover = Fighter.create(monsterPo,this,0,monsterFreshPo);
						mover.changeX(monsterFreshPo.getX()/10000f);
						mover.changeY(monsterFreshPo.getY()/10000f);
						mover.changeZ(monsterFreshPo.getZ()/10000f);
						mover.aimX = mover.x;
						mover.aimY = mover.y;
						mover.aimZ = mover.z;
						mover.pointX = monsterFreshPo.getX();
						mover.pointY = monsterFreshPo.getY();
						mover.pointZ = monsterFreshPo.getZ();
						doAddMoverToStage(mover, null, true);
					}
				}
			}			
		}
	}

	public void logoff(RolePo rolePo) {
		
		
	}

	public static List<Entity>  buildEntityListByAllNpcAndNearByCell(CellData cellData,Cell cell, int limit,boolean withNpc) {
    	List<Entity> list= new ArrayList<Entity>();
    	List<Cell> newCells = cellData.getNearByCells(cell,limit);
        if (newCells!=null && newCells.size() > 0)
        {
            for (Cell cell2 : newCells)
            {
            	list.addAll(cell2.getAllLivings());
            }
        }
        if(withNpc){
            list.addAll(cellData.getAllMapNpcs());
        }
		return list;
	}
	
}
