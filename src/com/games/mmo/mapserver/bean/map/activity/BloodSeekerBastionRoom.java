package com.games.mmo.mapserver.bean.map.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
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
import com.storm.lib.util.ExceptionUtil;

/**
 * 血魔堡垒
 * @author saly.bao
 *
 */
public class BloodSeekerBastionRoom extends DynamicMapRoom {

	public static final int SCENE_ID = 20100012;
	
	public BloodSeekerBastionRoom(ScenePo scenePo) {
		super(scenePo);
		startThread();
	}
	
	@Override
	public void onStart(){
//		System.out.println("BloodSeekerBastionRoom start"+new Date().toLocaleString());
		super.onStart();
		sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key8"));
	}
	
	
	public void play(){
		super.play();
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(CopySceneType.COPY_SCENE_CONF_BLOODMAGICFORTRESS);
//		System.out.println("BloodSeekerBastionRoom go fresh111");
		if(copySceneActivityPo.getActivityWasOpen().intValue() == 0){
//			System.out.println("BloodSeekerBastionRoom go fresh222");
			freshMonster(0);			
		}
		checkGameOver();
	}
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		for (Fighter player : cellData.getAllCellPlayers()) {
			BattleResultVo battleResultVo = new BattleResultVo();
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			int addGold = 0;
			int addExp = 0;
			addExp=(player.rolePo.pVPPVEActivityStatusVo.score*3+3200)*Math.max(player.rolePo.getLv()-15, 10);
			addGold=(player.rolePo.pVPPVEActivityStatusVo.score*20+24000);
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
			player.rolePo.battleResultVo=battleResultVo;
			
			player.rolePo.checkRecordLiveAcitivity(player.rolePo.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_BloodSeekerBastionRoom);
			PVEPVPRecordVo.instance.checkHighLiveActivity(player.rolePo.getName(),player.rolePo.pVPPVEActivityStatusVo.score,CopySceneType.COPY_SCENE_TYPE_BloodSeekerBastionRoom,0,0);

			player.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
			player.rolePo.sendCopySceneFinish(battleResultVo);
			

			player.rolePo.calculateBat(1);
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
			Fighter fighter = null;
			if(killer.type == Entity.MOVER_TYPE_PLAYER){
				fighter = killer;
			}
			else if (killer.type == Entity.MOVER_TYPE_PET){
				fighter = killer.rolePo.fighter;
			}
			for (RolePo player : joinPlayers) {
				player.pVPPVEActivityStatusVo.remainMonsterCount=cellData.getAllCellMonsterss().size();
				player.sendUpdatePVPPVEActivity();
			}
			if(null != fighter)
			{
				MonsterPo monsterPo=MonsterPo.findEntity(deadFighter.itemId);
				if(monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE1){
					fighter.changeScore(2);
				}
				else if(monsterPo.getMonsterType() == MonsterType.MONSTER_TYPE4){
					fighter.changeScore(100);
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
			if(currentFreshIndex<copySceneConfPo.listRefreshTime.size()){

				fighter.rolePo.pVPPVEActivityStatusVo.totalWave=copySceneConfPo.listRefreshTime.size();
				fighter.rolePo.pVPPVEActivityStatusVo.roomCreatedTime=new Long(createdTime/1000).intValue();
				fighter.rolePo.sendUpdatePVPPVEActivity();				
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
		}
		if(fighter.type==Fighter.MOVER_TYPE_MONSTER){
			int currentMonsterCount = cellData.getAllCellMonsterss().size();
			for (Fighter player : cellData.getAllCellPlayers()) {
				player.rolePo.pVPPVEActivityStatusVo.remainMonsterCount=currentMonsterCount;
				player.rolePo.sendUpdatePVPPVEActivity();
			}
		}
	}
	

}
