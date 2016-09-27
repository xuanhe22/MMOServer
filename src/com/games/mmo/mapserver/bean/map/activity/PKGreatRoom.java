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
import com.games.mmo.po.game.TitlePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.PVPPVEActivityStatusVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;

/**
 * PK之王
 * @author saly.bao
 *
 */
public class PKGreatRoom extends DynamicMapRoom {

	/** PK之王地图ID */
	public static final int SCENE_ID = 20100009;
	public int runTimeIndex=0;
	
	public PKGreatRoom(ScenePo scenePo) {
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
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_KINGOFPK);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			updateScoreChange();			
		}
		checkGameOver();
		checkOnlyOneGameOver();
	}			

	private void updateScoreChange() {
		runTimeIndex++;
		// 线程默认5秒执行一次
		if(runTimeIndex%2==0)
		{
			List<Fighter> fighterList = cellData.getAllCellPlayers();
			//每10秒执行一次
			for(Fighter fighter:fighterList){
				fighter.changeScore(1);
			}
			sendRoomAboradScoreRankUpdate();
		}
	}


	private void checkOnlyOneGameOver() {
		if(cellData.getAllCellPlayers().size()==1){
			gameOver();
		}
	}


	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.type == Entity.MOVER_TYPE_PLAYER)
		{
			playerLeaveRoom(deadFighter.rolePo);
			RolePo player = deadFighter.rolePo;
			
			BattleResultVo battleResultVo = new BattleResultVo();
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			int addGold = 0;
			int addExp = 0;
			int addPrestige = player.pVPPVEActivityStatusVo.score*40;
			int addAchivePoint = player.pVPPVEActivityStatusVo.score*30;
			
			battleResultVo.gold = addGold;
			battleResultVo.exp = addExp;
			battleResultVo.prestige = addPrestige;
			battleResultVo.guildHonor = addAchivePoint;
			
			battleResultVo.loadCardAward(copySceneConfPo);
			battleResultVo.loadItemList(copySceneConfPo);
			player.battleResultVo=battleResultVo;
			
			player.pVPPVEActivityStatusVo.remainPlayerCount=1;
			player.sendUpdatePVPPVEActivity();
			
			player.checkRecordLiveAcitivity(player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KING_OF_PK);
			player.sendCopySceneFinish(battleResultVo);
			player.calculateBat(1);

			
			Fighter fighter = null;
			if(killer.type == Entity.MOVER_TYPE_PLAYER){
				if(killer.rolePo.pVPPVEActivityStatusVo.monsterKillCount<=80){
					killer.changeScore(5);
				}
				else{
					killer.changeScore(1);
				}
			}


			player.sendUpdatePVPPVEActivity();

			for (RolePo join : joinPlayers) {
				join.pVPPVEActivityStatusVo.remainPlayerCount=joinPlayers.size();
				join.sendUpdatePVPPVEActivity();
			}
		}
	}

	public boolean playerJoinRoom(RolePo rolePo){
		boolean firstJoin=super.playerJoinRoom(rolePo);
		if(firstJoin){
			rolePo.pVPPVEActivityStatusVo.init();
			for (RolePo join : joinPlayers) {
				join.pVPPVEActivityStatusVo.remainPlayerCount=joinPlayers.size();
				join.sendUpdatePVPPVEActivity();
			}
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
				
				fighter.rolePo.pVPPVEActivityStatusVo.totalWave=copySceneConfPo.listRefreshTime.size();
				fighter.rolePo.pVPPVEActivityStatusVo.roomCreatedTime=new Long(createdTime/1000).intValue();
				fighter.rolePo.pVPPVEActivityStatusVo.score=40;
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


		}
		if(fighter.type==Fighter.MOVER_TYPE_MONSTER){
			fighter.rolePo.calculateBat(1);
		}
	}
	
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		if(joinPlayers.size()>1){
			for(RolePo player:joinPlayers){
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				int addGold = 0;
				int addExp = 0;
				int addPrestige = player.pVPPVEActivityStatusVo.score*40;
				int addAchivePoint = player.pVPPVEActivityStatusVo.score*30;
				
				battleResultVo.gold = addGold;
				battleResultVo.exp = addExp;
				battleResultVo.prestige = addPrestige;
				battleResultVo.guildHonor = addAchivePoint;
				
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				player.battleResultVo=battleResultVo;
				
				player.pVPPVEActivityStatusVo.remainPlayerCount=1;
				player.sendUpdatePVPPVEActivity();
				
				player.checkRecordLiveAcitivity(player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KING_OF_PK);
				PVEPVPRecordVo.instance.checkHighLiveActivity(player.getName(),player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KING_OF_PK,0,0);
//				StringBuffer sb = new StringBuffer();
//				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key10")).append(player.getName()).append(GlobalCache.fetchLanguageMap("key11"));
//				ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
//				chatService.sendHorse(sb.toString());
//				chatService.sendSystemWorldChat(sb.toString());
//				PVEPVPRecordVo.instance.lastKingOfPKWinPlayerName=player.getName();

				player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
				player.sendCopySceneFinish(battleResultVo);

				player.calculateBat(1);
//				TitlePo titlePo = GlobalCache.titlePoMap.get(1000);
//				player.addSpecialTitle(titlePo);
			}
		}else{
			for (RolePo player : joinPlayers) {
//				System.out.println(" player.pVPPVEActivityStatusVo.score:"+ player.pVPPVEActivityStatusVo.score);
				BattleResultVo battleResultVo = new BattleResultVo();
				battleResultVo.copySceneConfId = copySceneConfPo.getId();
				int addGold = 0;
				int addExp = 0;
				int addPrestige = player.pVPPVEActivityStatusVo.score*40;
				int addAchivePoint = player.pVPPVEActivityStatusVo.score*30;
				
				battleResultVo.gold = addGold;
				battleResultVo.exp = addExp;
				battleResultVo.prestige = addPrestige;
				battleResultVo.guildHonor = addAchivePoint;
				
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
				player.battleResultVo=battleResultVo;
				
				player.pVPPVEActivityStatusVo.remainPlayerCount=1;
				player.sendUpdatePVPPVEActivity();
				
				player.checkRecordLiveAcitivity(player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KING_OF_PK);
				PVEPVPRecordVo.instance.checkHighLiveActivity(player.getName(),player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_KING_OF_PK,0,0);
				StringBuffer sb = new StringBuffer();
				sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key10")).append(player.getName()).append(GlobalCache.fetchLanguageMap("key11"));
				ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
				chatService.sendHorse(sb.toString());
				chatService.sendSystemWorldChat(sb.toString());
				PVEPVPRecordVo.instance.lastKingOfPKWinPlayerName=player.getName();

				player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
				player.sendCopySceneFinish(battleResultVo);

				player.calculateBat(1);
				TitlePo titlePo = GlobalCache.titlePoMap.get(1000);
				player.addSpecialTitle(titlePo);
				
			}
		}
	}


}
