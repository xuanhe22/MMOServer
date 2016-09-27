package com.games.mmo.mapserver.bean.map.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.MailService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.PVPPVEActivityStatusVo;
import com.games.mmo.vo.global.MonsterVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;


/**
 * 自由之战
 * @author Administrator
 *
 */
public class FreeWarRoom extends DynamicMapRoom {

//	副本配置 type字段 14 自由之战
//    show_type 7 自由之战
	public int runTimeIndex=0;
	public static final int SCENE_ID = 20100027;
	
	/** 临时用来划分阵营*/
	public int index = 0;
	
	public CopyOnWriteArrayList<RolePo> joinPlayersForce1 = new CopyOnWriteArrayList<RolePo>();
	
	public CopyOnWriteArrayList<RolePo> joinPlayersForce2 = new CopyOnWriteArrayList<RolePo>();
	/** 阵营1积分*/
	public Integer score1 = 0;
	/** 阵营2积分*/
	public Integer score2 = 0;
	
	public FreeWarRoom(ScenePo scenePo) {
		super(scenePo);
//		System.out.println("FreeWarRoom(scenePo)");
		startThread();
	}
	
	@Override
	public void onStart(){
		super.onStart();
//		System.out.println("FreeWarRoom.onStart()");
		if(FreeWarRoom.SCENE_ID == sceneId){
//			System.out.println("开始刷怪！！！"+mapRoomId);
			buildMonsterFreshInforVos();
		}
		sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key8"));
	}
	
	
	public void play(){
		super.play();
		updateScoreChange();
		checkTimeExpiredGameOver();
	}
	
	
	private void updateScoreChange() {
		runTimeIndex++;
		boolean changed=false;
		//每5秒执行一次
		if(true)
//		if(runTimeIndex%1==0)
		{	
//			System.out.println(DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+"; score1="+score1+"; score2="+score2);
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyMonster);
			int forces1 = gp.fetchForcesMonsterNum(CopySceneType.MILITARY_FORCES_1);
			int forces2 = gp.fetchForcesMonsterNum(CopySceneType.MILITARY_FORCES_2);
			
			score1+=(500*forces1);
			score2+=(500*forces2);
			List<Fighter> fighterList = cellData.getAllCellPlayers();
			for(Fighter fighter:fighterList)
			{
				if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_1){
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore1=score1;
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore2=score2;
					changed=true;
				}else if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_2){
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore1=score1;
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore2=score2;
					changed=true;
				}
			}

			sendFreeWarScoreRankUpdate1();
			sendFreeWarScoreRankUpdate2();

		}
	}
	
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		boolean flag1 = false;
		boolean flag2 = false;
		if(score1 >= score2){
			flag1 = true;
		}else{
			flag2 = true;
		}
		if(flag1){
			for(RolePo rolePo : joinPlayersForce1){
				List<IdNumberVo3> award = new ArrayList<IdNumberVo3>();				
				int exp = 3000*Math.max(1, (rolePo.getLv().intValue()-15)); // 经验
				int achievePoint = 1500+Math.min(rolePo.pVPPVEActivityStatusVo.score*10, 1000);// 成就
				int prestige = 1500+Math.min(rolePo.pVPPVEActivityStatusVo.score*10, 2000);// 声望
				award.add(new IdNumberVo3(1,300004001,exp,1));
				award.add(new IdNumberVo3(1,300004094,achievePoint,1));
				award.add(new IdNumberVo3(1,300004046,prestige,1));
				award.add(new IdNumberVo3(1,300010082,1,0));
				mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2332"),GlobalCache.fetchLanguageMap("key2333"),award);
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				battleResultVo.success=1;
				rolePo.sendCopySceneFinish(battleResultVo);
				rolePo.setMilitaryForces(0);
			}	
			for(RolePo rolePo : joinPlayersForce2){
				List<IdNumberVo3> award = new ArrayList<IdNumberVo3>();
				int exp = 2400*Math.max(1, (rolePo.getLv().intValue()-15)); // 经验
				int achievePoint = 1000+Math.min(rolePo.pVPPVEActivityStatusVo.score*5, 500);// 成就
				int prestige = 1000+Math.min(rolePo.pVPPVEActivityStatusVo.score*5, 1000);// 声望
				award.add(new IdNumberVo3(1,300004001,exp,1));
				award.add(new IdNumberVo3(1,300004094,achievePoint,1));
				award.add(new IdNumberVo3(1,300004046,prestige,1));
				award.add(new IdNumberVo3(1,300010082,1,0));
				mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2332"), GlobalCache.fetchLanguageMap("key2334"),award);
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				battleResultVo.success=0;
				rolePo.sendCopySceneFinish(battleResultVo);
				rolePo.setMilitaryForces(0);
			}
		}
		if(flag2){
			for(RolePo rolePo : joinPlayersForce2){
				List<IdNumberVo3> award = new ArrayList<IdNumberVo3>();				
				int exp = 10000*Math.max(1, (rolePo.getLv().intValue()-15)); // 经验
				int achievePoint = 3000+Math.min(rolePo.pVPPVEActivityStatusVo.score*100, 2000);// 成就
				int prestige = 3000+Math.min(rolePo.pVPPVEActivityStatusVo.score*100, 4000);// 声望
				award.add(new IdNumberVo3(1,300004001,exp,1));
				award.add(new IdNumberVo3(1,300004094,achievePoint,1));
				award.add(new IdNumberVo3(1,300004046,prestige,1));
				award.add(new IdNumberVo3(1,300010082,1,0));
				mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2332"), GlobalCache.fetchLanguageMap("key2335"),award);
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				battleResultVo.success=1;
				rolePo.sendCopySceneFinish(battleResultVo);
				rolePo.setMilitaryForces(0);
			}
			for(RolePo rolePo : joinPlayersForce1){
				List<IdNumberVo3> award = new ArrayList<IdNumberVo3>();
				int exp = 8000*Math.max(1, (rolePo.getLv().intValue()-15)); // 经验
				int achievePoint = 2000+Math.min(rolePo.pVPPVEActivityStatusVo.score*50, 1000);// 成就
				int prestige = 2000+Math.min(rolePo.pVPPVEActivityStatusVo.score*50, 2000);// 声望
				award.add(new IdNumberVo3(1,300004001,exp,1));
				award.add(new IdNumberVo3(1,300004094,achievePoint,1));
				award.add(new IdNumberVo3(1,300004046,prestige,1));
				award.add(new IdNumberVo3(1,300010082,1,0));
				mailService.sendAwardSystemMail(rolePo.getId(), GlobalCache.fetchLanguageMap("key2332"), GlobalCache.fetchLanguageMap("key2336"),award);
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				battleResultVo.success=0;
				rolePo.sendCopySceneFinish(battleResultVo);
				rolePo.setMilitaryForces(0);
			}
		}
		

		for (Fighter player : cellData.getAllCellPlayers()) {
			BattleResultVo battleResultVo = new BattleResultVo();
			if(score1 >= score2){
				if(player.rolePo.getMilitaryForces().intValue()==CopySceneType.MILITARY_FORCES_1){
					battleResultVo.success=1;
					player.rolePo.sendCopySceneFinish(battleResultVo);									
				}else if(player.rolePo.getMilitaryForces().intValue()==CopySceneType.MILITARY_FORCES_2){
					battleResultVo.success=0;
					player.rolePo.sendCopySceneFinish(battleResultVo);	
				}
			}else{
				if(player.rolePo.getMilitaryForces().intValue()==CopySceneType.MILITARY_FORCES_1){
					battleResultVo.success=0;
					player.rolePo.sendCopySceneFinish(battleResultVo);									
				}else if(player.rolePo.getMilitaryForces().intValue()==CopySceneType.MILITARY_FORCES_2){
					battleResultVo.success=1;
					player.rolePo.sendCopySceneFinish(battleResultVo);	
				}
			}
		}
	}


	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.type == Entity.MOVER_TYPE_MONSTER)
		{
			MonsterPo monsterPo=MonsterPo.findEntity(deadFighter.itemId);
			if(monsterPo.getMonsterType().intValue() == MonsterType.MONSTER_TYPE10){
				GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyMonster);
				MonsterVo monsterVo = new MonsterVo();
				monsterVo.id=monsterPo.getId();
				monsterVo.militaryForces=killer.militaryForces;
				PrintUtil.print("monsterPo.name = " +monsterPo.getName() +"; killer.name = " +killer.name +"; killer.militaryForces = "+killer.militaryForces+" deadFighter.name +"+deadFighter.name+"; deadFighter.militaryForces = " +deadFighter.militaryForces);
				gp.updateMonsterVo(monsterVo);

				int index1 = 0;
				int index2 = 0;
				for(MonsterVo monster : ((List<MonsterVo>)gp.valueObj)){
					if(monster.militaryForces == 1){
						index1++;
					}
					else if(monster.militaryForces == 2){
						index2++;
					}
				}
				
				
				if(index1 > index2 && killer.militaryForces ==1){
					sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key2673"));
				}
				else if(index1 < index2 && killer.militaryForces == 2){
					sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key2674"));
				}
				killer.changeScore(100);
				if(killer.militaryForces == CopySceneType.MILITARY_FORCES_1){
					score1+=100;
				}else if(killer.militaryForces == CopySceneType.MILITARY_FORCES_2){
					score2+=100;
				}
			}
		}
		
		// 玩家击杀玩家获得积分
		
		if(deadFighter.type == Entity.MOVER_TYPE_PLAYER){
			Fighter fighter = null;
			if(killer.type == Entity.MOVER_TYPE_PLAYER){
				fighter = killer;
			}
			else if (killer.type == Entity.MOVER_TYPE_PET){
				fighter = killer.rolePo.fighter;
			}
			if(null != fighter)
			{
				if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_1){
					score1+=10;
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore1=score1;
				}else if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_2){
					score2+=10;
					fighter.rolePo.pVPPVEActivityStatusVo.militaryForcesScore2=score2;
				}
				fighter.rolePo.pVPPVEActivityStatusVo.monsterKillCount++;
				fighter.changeScore(10);
			}
		}
	}

	public boolean playerJoinRoom(RolePo rolePo){
		boolean firstJoin=super.playerJoinRoom(rolePo);
		if(firstJoin){
			rolePo.pVPPVEActivityStatusVo.init();
			rolePo.pVPPVEActivityStatusVo.militaryForces=rolePo.getMilitaryForces();
			rolePo.pVPPVEActivityStatusVo.roleId=rolePo.getId();
			rolePo.pVPPVEActivityStatusVo.roleName=rolePo.getName();
		}
		return firstJoin;
	}
	/**
	 * 实体添加到舞台
	 */
	@Override
	public void onAddMover(Fighter fighter){
		super.onAddMover(fighter);
		if(fighter.type==Fighter.MOVER_TYPE_PLAYER){
			if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_1){
				if(!joinPlayersForce1.contains(fighter.rolePo)){
					joinPlayersForce1.add(fighter.rolePo);
				}
			}else if(fighter.militaryForces == CopySceneType.MILITARY_FORCES_2){
				if(!joinPlayersForce2.contains(fighter.rolePo)){
					joinPlayersForce2.add(fighter.rolePo);
				}
			}

		}
	}
	
	/**
	 * 舞台实体移除
	 */
	@Override
	public void onRemoveMover(Fighter fighter){
		super.onRemoveMover(fighter);
		if(fighter.type==Fighter.MOVER_TYPE_PLAYER){
			fighter.rolePo.calculateBat(1);
			this.index--;
		}
	}
	
	
	public void sendFreeWarScoreRankUpdate1(){
		List<PVPPVEActivityStatusVo> all =new ArrayList<PVPPVEActivityStatusVo>();
		for (RolePo rolePo : joinPlayersForce1) {
			all.add(rolePo.pVPPVEActivityStatusVo);
		}
		Collections.sort(all);
		int index=1;
		for (PVPPVEActivityStatusVo pVPPVEActivityStatusVo : all) {
			pVPPVEActivityStatusVo.myRank=index++;
		}
		int maxIndex=Math.min(all.size(),10);
		all=all.subList(0, maxIndex);
		sendRoomAboradMsg("PushRemoting.sendFreeWarScoreRankUpdate1",new Object[]{all});
	}
	
	public void sendFreeWarScoreRankUpdate2(){
		List<PVPPVEActivityStatusVo> all =new ArrayList<PVPPVEActivityStatusVo>();
		for (RolePo rolePo : joinPlayersForce2) {
			all.add(rolePo.pVPPVEActivityStatusVo);
		}
		Collections.sort(all);
		int index=1;
		for (PVPPVEActivityStatusVo pVPPVEActivityStatusVo : all) {
			pVPPVEActivityStatusVo.myRank=index++;
		}
		int maxIndex=Math.min(all.size(),10);
		all=all.subList(0, maxIndex);
		sendRoomAboradMsg("PushRemoting.sendFreeWarScoreRankUpdate2",new Object[]{all});
	}
	
	@Override
	public void logoff(RolePo rolePo) {
		super.logoff(rolePo);
		removeRolePo(rolePo.getId(), joinPlayersForce1);
		removeRolePo(rolePo.getId(), joinPlayersForce2);
	}
	
	public void removeRolePo(int roleId,  CopyOnWriteArrayList<RolePo> joinPlayersForce){
		List<RolePo> removeList = new ArrayList<RolePo>();
		for(RolePo rolePo : joinPlayersForce){
			if(rolePo.getId().intValue() == roleId){
				removeList.add(rolePo);
			}
		}
		for(RolePo rp : removeList){
//			System.out.println("删除：rp.name = " +rp.getName());
			joinPlayersForce.remove(rp);
		}
	}

}
