package com.games.mmo.mapserver.bean.map.single;

import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ScenePo;

public class Single60LevelRoom extends DynamicMapRoom {
	public static final int SCENE_ID = 20105003;
	
	public Single60LevelRoom(ScenePo scenePo) {
		super(scenePo);
//		startThread();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key8"));
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
