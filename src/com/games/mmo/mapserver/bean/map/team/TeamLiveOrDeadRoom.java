package com.games.mmo.mapserver.bean.map.team;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 生存大挑战
 * @author 2016-06-27
 *
 */
public class TeamLiveOrDeadRoom extends DynamicMapRoom{

	public static final int SCENE_ID = 20100029;
	
	public TeamLiveOrDeadRoom(ScenePo scenePo){
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
		checkPlayerAllFeignDeathGameOver();
	}		
	
	private void checkPlayerAllFeignDeathGameOver() {
		int index = 0;
		for(Fighter fighter : cellData.getAllCellPlayers()){
			BufferStatusVo bufferStatusVo = fighter.findBufferStatus(101);
			if(bufferStatusVo != null){
				index++;
			}
		}
		
		if(cellData.getAllCellPlayers().size() == index){
			gameOver();
			if(copySceneConfPo!=null){
				PrintUtil.print("没有玩家了，游戏结束:"+copySceneConfPo.getId()+" "+mapRoomId);
			}
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
			int addAchievePoint = 0;
			boolean award = true;
			for (IdNumberVo3 idNumberVo3 : player.listCopySceneTodayVisitTimes) {
				if(idNumberVo3.getInt1().intValue()==copySceneConfPo.getId() && idNumberVo3.getInt2().intValue()==copySceneConfPo.getTeamMode().intValue()){
					award = player.checkSceneTimes(idNumberVo3.getInt3(), copySceneConfPo.getSceneId(), copySceneConfPo.getAvaTimes());
					break;
				}
			}
//			经验=max(（人物等级-15）*12000，180000）
//			addExp=Math.max((player.getLv()-15)*12000, 180000);
			addExp=currentFreshIndex*30000;
//			addGold=player.rolePo.pVPPVEActivityStatusVo.score*100;
			addAchievePoint=800+currentFreshIndex*100;
			if(award){
				battleResultVo.gold = addGold;
				battleResultVo.exp = addExp;
				battleResultVo.prestige = 0;
				battleResultVo.achievePoint = addAchievePoint;
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
