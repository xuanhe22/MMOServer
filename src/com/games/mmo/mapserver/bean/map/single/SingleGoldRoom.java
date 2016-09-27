package com.games.mmo.mapserver.bean.map.single;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.MonsterType;
import com.games.mmo.vo.BattleResultVo;
import com.games.mmo.vo.global.PVEPVPRecordVo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;

/**
 * 金币副本
 * @author saly.bao
 *
 */
public class SingleGoldRoom extends DynamicMapRoom {
	public static final int SCENE_ID = 20100014;
	
	public SingleGoldRoom(ScenePo scenePo) {
		super(scenePo);
//		startThread();
	}
	
	@Override
	public void onStart(){
		super.onStart();

	}
	
	
	public void play(){
		super.play();
		//groupFreshMonster(0);
		//checkGameOver();
		checkTimeExpiredGameOver();
	}
	
	/**
	 * 活动完成后处理业务
	 */
	@Override
	public void onGameOver() {
		super.onGameOver();
		destroyMeRoom(false);

	}
	
	@Override
	public void logoff(RolePo rolePo) {
		onGameOver();
	}

}
