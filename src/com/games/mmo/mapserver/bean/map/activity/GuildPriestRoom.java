package com.games.mmo.mapserver.bean.map.activity;

import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.vo.xml.ConstantFile.Guild.PriestFresh.FreshQuality;
import com.storm.lib.util.DateUtil;

public class GuildPriestRoom extends DynamicMapRoom {

	/**
	 * 公会祭酒
	 */
	public static final int SCENE_ID = 20107001;
	
	public GuildPriestRoom(ScenePo scenePo) {
		super(scenePo);
		startThread();
	}
	
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	
	/**
	 * 实体添加到舞台
	 */
	@Override
	public void onAddMover(Fighter fighter){
		super.onAddMover(fighter);
		RolePo rolePo = fighter.rolePo;
		if(rolePo != null){
			if(rolePo.getGuildPriestState().intValue() == 0){
				rolePo.setGuildPriestState(1);
			}
		}
	}
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		for(RolePo rolePo : joinPlayers ){
			rolePo.setGuildPriestState(2);
		}
	}
	
	public void play(){
		super.play();
		updateExpChange();
		checkTimeExpiredGameOver();
	}
	
	private void updateExpChange() {
//		System.out.println("updateExpChange() " + DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		GuildPo guildPo = GuildPo.findEntity(guildId);
		if(guildPo != null){
			FreshQuality freshQuality = guildPo.fecthFreshQualityByQuality(guildPo.getPriestFreshQuality());
			for(RolePo rolePo : joinPlayers){
				if(freshQuality != null){
					int exp = Math.max(rolePo.getLv()-15, 15)*freshQuality.expPar;
					rolePo.adjustExp(exp);	
					rolePo.sendUpdateExpAndLv(true);
				}
			}	
		}
	}
}
