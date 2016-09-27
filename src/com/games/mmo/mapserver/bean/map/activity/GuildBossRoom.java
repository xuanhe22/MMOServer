package com.games.mmo.mapserver.bean.map.activity;

import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.storm.lib.vo.IdNumberVo2;

public class GuildBossRoom extends DynamicMapRoom {
	
	/** 公会副本地图Id*/
	public static final int SCENE_ID = 920100020;
	

	public GuildBossRoom(ScenePo scenePo) {
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
		freshMonster(0);
		checkMonsterAllDieGameOver();
		checkTimeExpiredGameOver();
	}	
	
	/**
	 * 怪物死亡
	 */
	@Override
	public void doMonsterDie(Fighter killer, Fighter deadFighter) {
		super.doMonsterDie(killer, deadFighter);
		if(killer.rolePo != null){
			GuildPo guildPo = GuildPo.findEntity(killer.rolePo.getGuildId());
			if(guildPo!= null){
				MonsterPo monsterPo = MonsterPo.findEntity(deadFighter.itemId);
				if(monsterPo != null){
					List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
					Boss currentBoss = null;
					for(Boss boss : listBoss){
						if(boss.bossid.intValue() == monsterPo.getId().intValue()){
							currentBoss=boss;
							break;
						}
					}
					if(currentBoss != null){
						for(IdNumberVo2 idNumberVo2 : guildPo.listBossInfo){
							if(idNumberVo2.getInt1().intValue() == currentBoss.copysceneconfid){
								idNumberVo2.setInt3(1);
								break;
							}
						}
//					System.out.println("11"+guildPo.getName() + " | "+guildPo.listBossInfo);
						if(currentBoss.lv < listBoss.size()){
							guildPo.listBossInfo.get(currentBoss.lv).setInt2(1);
							guildPo.sendUpdateGuildBossInfo();
						}
//					System.out.println("22"+guildPo.getName() + " | "+guildPo.listBossInfo);
					}
				}
			}
		}
	}
	
	
	/**
	 * 实体添加到舞台
	 */
	@Override
	public void onAddMover(Fighter fighter){
		super.onAddMover(fighter);
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
			battleResultVo.copySceneConfId = copySceneConfPo.getId();
			player.battleResultVo=battleResultVo;
			player.fighter.removeBuffer(CopySceneType.COPY_INSPIRE_BUFFID);
			player.sendCopySceneFinish(battleResultVo);
		}

	}
	
	

	
}
