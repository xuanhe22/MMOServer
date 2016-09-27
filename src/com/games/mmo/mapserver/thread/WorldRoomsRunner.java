package com.games.mmo.mapserver.thread;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.SceneType;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.util.BaseLogUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;


public class WorldRoomsRunner extends Thread{

	public final int sleep = 1000;
	
	@Override
	public void run() {
		setName("WorldRoomsRunner");
		initNormalSceneMapRooms();
		launchRegularTask();
	}

	private void launchRegularTask() {
		int runTimeIndex=0;
		while(true){
			long before = System.currentTimeMillis();
				try {
					runTimeIndex++;
					for (MapRoom mapRoom : MapWorld.mapRooms.values()) {
						try {
							if(mapRoom!=null){
								mapRoom.launchRegularTask(runTimeIndex);
							}
						} catch (Exception e) {
							ExceptionUtil.processException(e);
						}
					}
					if(runTimeIndex%1200 == 0){
						CopySceneActivityPo csap = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_YUN_DART);
						if(csap != null){
							if(csap.getActivityWasOpen().intValue() == 0 && csap.getEndTime()!=null && System.currentTimeMillis() > (csap.getEndTime().longValue()+30*60*1000)){
								MapWorld.removeAllYunDartFighter();						
							}
						}
						GlobalCache.mapProtocolFrequencyResponse.clear();
						GlobalCache.mapResponse.clear();
					}
				} catch (Exception e) {
					ExceptionUtil.processException(e);
				}
				long after = System.currentTimeMillis();
				long lastSleep = sleep-(after-before);
				if(lastSleep > 0)
				{
					try {
						Thread.sleep(lastSleep);
					} catch (InterruptedException e) {
						ExceptionUtil.processException(e);
					}
				}
		}
	}

	private void initNormalSceneMapRooms() {
		List<ScenePo> scenePos = GameDataTemplate.getDataList(ScenePo.class);
		for (ScenePo scenePo : scenePos) {
			if(scenePo.getSceneAttribute().intValue() == 0 || scenePo.getSceneAttribute().intValue() == 2){
				MapRoom mapRoom = MapWorld.createStaticMapRoom(scenePo);				
			}
		}
	}
	
//	private void initRobotToMapRoom(){
//		 MapWorld.mapRooms.get(20101099).buildRobotFighterInfoVos();
//	}
	
}
