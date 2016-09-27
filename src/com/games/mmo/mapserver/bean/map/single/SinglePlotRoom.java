package com.games.mmo.mapserver.bean.map.single;

import com.games.mmo.mapserver.bean.DynamicMapRoom;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ScenePo;

/**
 * 剧情副本
 * @author Administrator
 *
 */
public class SinglePlotRoom extends DynamicMapRoom {
	public static final int SCENE_ID = 20103002;
	
	public SinglePlotRoom(ScenePo scenePo) {
		super(scenePo);
//		startThread();
	}
	
	@Override
	public void onStart(){
		super.onStart();
	}
	
	
	public void play(){
		super.play();
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
