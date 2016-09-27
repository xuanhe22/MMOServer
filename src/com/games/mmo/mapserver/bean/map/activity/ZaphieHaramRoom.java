package com.games.mmo.mapserver.bean.map.activity;

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

/**
 * 恶灵禁地
 * @author saly.bao
 *
 */
public class ZaphieHaramRoom extends DynamicMapRoom {

	/** 恶灵禁地地图ID */
	public static final int SCENE_ID = 20100011;
	
	public ZaphieHaramRoom(ScenePo scenePo) {
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
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE);
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
			freshMonster(0);			
		}
		checkGameOver();
	}		

	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		super.doMonsterDie(killer, deadFighter);
		if(deadFighter.type == Entity.MOVER_TYPE_MONSTER)
		{
			Fighter fighter = null;
			if(killer.type == Entity.MOVER_TYPE_PLAYER){
				fighter = killer;
			}
			else if (killer.type == Entity.MOVER_TYPE_PET){
				fighter = killer.rolePo.fighter;
			}
			if(null != fighter)
			{
				MonsterPo monsterPo=MonsterPo.findEntity(deadFighter.itemId);
				
				if(monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE1){
					fighter.changeScore(1);
				}
				else if(monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE2){
					fighter.changeScore(100);
					gameOver();
				}
			}
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
//			fighter.rolePo.pVPPVEActivityStatusVo.init();
//			System.out.println("玩家"+fighter.name+"进入FightMapRoom");
//			if(currentFreshIndex<copySceneConfPo.listRefreshTime.size()){
//				fighter.rolePo.pVPPVEActivityStatusVo.currentWave=copySceneConfPo.listRefreshTime.get(currentFreshIndex).getId();
//			}
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
		if(fighter.type==Fighter.MOVER_TYPE_PLAYER){
			fighter.rolePo.calculateBat(1);
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
		
		for (RolePo player : joinPlayers) {
			BattleResultVo battleResultVo = new BattleResultVo();
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			int addGold = 0;
			int addExp = 0;
			addExp=(player.pVPPVEActivityStatusVo.score*3+3200)*Math.max(player.getLv()-15, 10);
			addGold=(player.pVPPVEActivityStatusVo.score*20+24000);
			
			battleResultVo.gold = addGold;
			battleResultVo.exp = addExp;
			battleResultVo.prestige = 0;
			battleResultVo.guildHonor = 1000;
			
			battleResultVo.loadCardAward(copySceneConfPo);
			battleResultVo.loadItemList(copySceneConfPo);
			List<Integer>  val = new ArrayList<Integer>();
			val.add(300010080);
			val.add(1);
			battleResultVo.itemList.add(val);
			player.battleResultVo=battleResultVo;
			
			
			player.checkRecordLiveAcitivity(player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_ZaphieHaramRoom);
			PVEPVPRecordVo.instance.checkHighLiveActivity(player.getName(),player.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_ZaphieHaramRoom,0,0);
//			player.rolePo.sendUpdateMainPack();
//			player.rolePo.sendUpdateTreasure();
//			player.rolePo.sendUpdateExpAndLv();
			player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
			player.sendCopySceneFinish(battleResultVo);

//			player.removeBuffer(CopySceneType.COPY_INSPIRE_DIAMOND);
			player.calculateBat(1);
		}

	}
	

}
