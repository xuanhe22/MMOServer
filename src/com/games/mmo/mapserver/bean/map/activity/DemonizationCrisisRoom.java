package com.games.mmo.mapserver.bean.map.activity;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;

/**
 * 魔化危机
 * @author saly.bao
 *
 */
public class DemonizationCrisisRoom extends DynamicMapRoom {

	public int runTimeIndex=0;
	public static final int SCENE_ID = 20100010;
	
	public DemonizationCrisisRoom(ScenePo scenePo) {
		super(scenePo);
		startThread();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key8"));
	}
	

	public void play(){
		super.play();
		
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_MONSTERCRISIS);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			freshMonster(0);
			updateScoreChange();			
		}
		checkTimeExpiredGameOver();
	}


	private void updateScoreChange() {
		runTimeIndex++;
		boolean changed=false;
		if(runTimeIndex%2==0)
		{
			List<Fighter> fighterList = cellData.getAllCellPlayers();
			//每秒执行一次
			for(Fighter fighter:fighterList)
			{
				if(fighter.buffAvatar != 0){
					fighter.changeScore(3);
					changed=true;
				}
			}
			
			if(runTimeIndex%20==0)
			{
				//每10秒执行一次
				for(Fighter fighter:fighterList){
					fighter.changeScore(1);
					changed=true;
				}
			}
			if(changed){
				sendRoomAboradScoreRankUpdate();
			}	
		}
	}

	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		boolean beKillerPlayerIsSnake=false;
		if(deadFighter.type==Entity.MOVER_TYPE_PLAYER && deadFighter.buffAvatar != 0){
			beKillerPlayerIsSnake=true;
		}
		if(deadFighter.type==Entity.MOVER_TYPE_PLAYER){
			deadFighter.removeBuffer(40);
		}
		
		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.type == Entity.MOVER_TYPE_MONSTER)
		{
			Fighter fighter = null;
			if(killer.type == Entity.MOVER_TYPE_PLAYER){
				fighter = killer;
				if(beKillerPlayerIsSnake){
					fighter.changeScore(2);
				}
				else{
					fighter.changeScore(1);
				}
			}
			else if (killer.type == Entity.MOVER_TYPE_PET){
				fighter = killer.rolePo.fighter;
			}
			if(null != fighter)
			{
				MonsterPo monsterPo=MonsterPo.findEntity(deadFighter.itemId);
				if(monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE3){
					fighter.changeScore(2);
				}
			}
		}
		sendRoomAboradScoreRankUpdate();
	}

	public boolean playerJoinRoom(RolePo rolePo){
		boolean firstJoin=super.playerJoinRoom(rolePo);
		if(firstJoin){
			rolePo.pVPPVEActivityStatusVo.init();
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
//			System.out.println("玩家"+fighter.name+"进入FightMapRoom");

//			fighter.rolePo.pVPPVEActivityStatusVo.init();
			fighter.rolePo.pVPPVEActivityStatusVo.roleName=fighter.name;
//			if(currentFreshIndex<copySceneConfPo.listRefreshTime.size()){
//				fighter.rolePo.pVPPVEActivityStatusVo.currentWave=copySceneConfPo.listRefreshTime.get(currentFreshIndex).getId();
//			}
			fighter.rolePo.pVPPVEActivityStatusVo.totalWave=copySceneConfPo.listRefreshTime.size();
			fighter.rolePo.pVPPVEActivityStatusVo.roomCreatedTime=new Long(createdTime/1000).intValue();
			fighter.rolePo.sendUpdatePVPPVEActivity();
			sendRoomAboradScoreRankUpdate();
		}

		
	}
	
	/**
	 * 舞台实体移除
	 */
	@Override
	public void onRemoveMover(Fighter fighter){
		super.onRemoveMover(fighter);
		if(fighter.type==Fighter.MOVER_TYPE_PLAYER){
			sendRoomAboradScoreRankUpdate();
			fighter.rolePo.calculateBat(1);
		}
		if(fighter.type==Fighter.MOVER_TYPE_MONSTER){
			int currentMonsterCount = cellData.getAllCellMonsterss().size();
			for (Fighter player : cellData.getAllCellPlayers()) {
				player.rolePo.pVPPVEActivityStatusVo.remainMonsterCount=currentMonsterCount;
				player.rolePo.sendUpdatePVPPVEActivity();
			}
		}
	}
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
//		System.out.println("---------------FightMapRoom房间结束--------------------");
//		ChatService.sendStaticHorse("------FightMapRoom房间结束，开始推送消息sendCopySceneFinish----");
//		System.out.println("---------------FightMapRoom房间结束--------------------");
//		ChatService.sendStaticHorse("------FightMapRoom房间结束，开始推送消息sendCopySceneFinish----");
		
		for (RolePo player : joinPlayers) {
			BattleResultVo battleResultVo = new BattleResultVo();
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			int addGold = 0;
			int addExp = 0;
			
//			成就=积分*20
//			经验=积分*35*max((等级-15）,10)

			int addAchievePoint = player.pVPPVEActivityStatusVo.score*10;
			int addGuildHonor = player.pVPPVEActivityStatusVo.score*20;
			addExp=player.pVPPVEActivityStatusVo.score*35*Math.max(player.getLv()-15, 10);
			addGold=0;
			
			battleResultVo.gold = addGold;
			battleResultVo.exp = addExp;
			battleResultVo.achievePoint = addAchievePoint;
			battleResultVo.guildHonor = addGuildHonor;
			
			battleResultVo.loadCardAward(copySceneConfPo);
			battleResultVo.loadItemList(copySceneConfPo);
			List<Integer>  val = new ArrayList<Integer>();
			val.add(300010083);
			val.add(1);
			battleResultVo.itemList.add(val);
			player.battleResultVo=battleResultVo;
			
			player.checkRecordLiveAcitivity(player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_DemonizationCrisisRoom);
			PVEPVPRecordVo.instance.checkHighLiveActivity(player.getName(),player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_DemonizationCrisisRoom,0,0);

//			player.rolePo.sendUpdateMainPack();
//			player.rolePo.sendUpdateTreasure();
//			player.rolePo.sendUpdateExpAndLv();
			player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
			player.sendCopySceneFinish(battleResultVo);

			player.calculateBat(1);
//			player.removeBuffer(CopySceneType.COPY_INSPIRE_DIAMOND);
		}

	}
	

}

