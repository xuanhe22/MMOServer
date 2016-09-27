package com.games.mmo.mapserver.bean.map.team;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.RightMarginRecord;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RankArenaPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.type.RoleType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 塔防
 * @author saly.bao
 *
 */
public class TeamTowerRoom extends DynamicMapRoom {

	public static final int SCENE_ID = 20100008;
	
	public TeamTowerRoom(ScenePo scenePo) {
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
		freshMonster(1);
		checkGameOver();
		checkTowerGameOver();
	}		
	


	private void checkTowerGameOver() {
		int totalWaveSize = copySceneConfPo.listRefreshTime.size();	
		int monsterSize=0;
		for (Fighter fighter : cellData.getAllCellMonsterss()) {
			if(fighter.monsterPo.getMonsterType() != MonsterType.MONSTER_TYPE4){
				monsterSize++;
			}
		}
		if(monsterSize<=0 && currentFreshIndex>=totalWaveSize){
			gameOver();
		}
	}

	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {

		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.monsterPo!=null && deadFighter.monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE4){
			success=false;
			onGameOver();
		}
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
			fighter.rolePo.pVPPVEActivityStatusVo.totalWave=copySceneConfPo.listRefreshTime.size();
			fighter.rolePo.pVPPVEActivityStatusVo.roomCreatedTime=new Long(createdTime/1000).intValue();
			fighter.rolePo.sendUpdatePVPPVEActivity();
		}
	}
	
	
	/**
	 * 舞台实体移除
	 */
	@Override
	public void onRemoveMover(Fighter fighter){
		super.onRemoveMover(fighter);
	}
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		for (RolePo player : joinPlayers) {
			BattleResultVo battleResultVo = new BattleResultVo();
			battleResultVo.success=success==true?1:0;
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			int addGold = 0;
			int addExp = 0;
			boolean award = true;
			for (IdNumberVo3 idNumberVo3 : player.listCopySceneTodayVisitTimes) {
				if(idNumberVo3.getInt1().intValue()==copySceneConfPo.getId() && idNumberVo3.getInt2().intValue()==copySceneConfPo.getTeamMode().intValue()){
					award = player.checkSceneTimes(idNumberVo3.getInt3(), copySceneConfPo.getSceneId(), copySceneConfPo.getAvaTimes());
					break;
				}
			}
//			经验=max(（人物等级-15）*12000，180000）
			addExp=Math.max((player.getLv()-15)*12000, 180000);
//			addGold=player.rolePo.pVPPVEActivityStatusVo.score*100;
			if(award){
				battleResultVo.gold = addGold;
				battleResultVo.exp = addExp;
				battleResultVo.prestige = 0;
				battleResultVo.achievePoint = 800;
				battleResultVo.loadCardAward(copySceneConfPo);
				battleResultVo.loadItemList(copySceneConfPo);
			}
			player.battleResultVo=battleResultVo;
			player.awardBattleResult(battleResultVo);
			player.sendUpdateMainPack(true);
			player.sendUpdateTreasure(true);
			player.sendUpdateExpAndLv(true);
			player.sendUpdateAchieveAndTitle();
			player.leaveMyTeam();
			player.sendCopySceneFinish(battleResultVo);
			player.calculateBat(1);
		}
	}
}
