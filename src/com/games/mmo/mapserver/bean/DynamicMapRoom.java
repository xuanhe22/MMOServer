package com.games.mmo.mapserver.bean;

import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.service.ChatService;
import com.storm.lib.util.PrintUtil;

public class DynamicMapRoom extends MapRoom{
	
	public static final int ACTIVITY_STATUS_OPEN=1;
	public static final int ACTIVITY_STATUS_PLAY=2;
	public static final int ACTIVITY_STATUS_CLOSE=3;
	public static final int ACTIVITY_STATUS_DESTROYING=4;
	public ActivityMapRoomThread fightThread;
	public int status=ACTIVITY_STATUS_OPEN;
	public int currentFreshIndex=0;
	public long startCloseTime=0l;
	public int autoDestroySeconds=300;
	public boolean success=true;
	public boolean anyPlayerJoined=false;
	/**
	 * 玩家等待结束，开始刷怪进行业务
	 */
	public long startRoomGameTime = 0l;
	public CopyOnWriteArrayList<RolePo> joinPlayers = new CopyOnWriteArrayList<RolePo>();
	
	
	public boolean playerJoinRoom(RolePo rolePo){
		boolean firstJoin=false;
		if(!joinPlayers.contains(rolePo)){
			joinPlayers.add(rolePo);
			firstJoin=true;
		}
		anyPlayerJoined=true;
		return firstJoin;
	}
	
	public void playerLeaveRoom(RolePo rolePo){
		joinPlayers.remove(rolePo);
	}
	
	public void play(){
		
	}
	
	public void checkDoPlay() {
		if(status==ACTIVITY_STATUS_PLAY){
			play();
		}
	}

	
	public void checkActivitySwitchStart() {
		if(activityOpen()){
			return;
		}
		if(status==ACTIVITY_STATUS_OPEN){
			startRoomGameTime = System.currentTimeMillis();
			onStart();
			status=ACTIVITY_STATUS_PLAY;
		}
	}

	public boolean activityOpen() {
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(copySceneConfPo.getId());
		if(copySceneActivityPo.getActivityWasOpen().intValue() != 0){
			return true;
		}
		return false;
	}
	
	public void startThread(){
		fightThread = new ActivityMapRoomThread(this);
		fightThread.start();
	}
	
	@Override
	public void onAddMover(Fighter fighter){
		super.onAddMover(fighter);
		if(fighter.type == Entity.MOVER_TYPE_PLAYER){
			playerJoinRoom(fighter.rolePo);
		}
	}
	
	@Override
	public void logoff(RolePo rolePo) {
		super.logoff(rolePo);
		playerLeaveRoom(rolePo);
	}
	
	public void switchToStart(){
		status=ACTIVITY_STATUS_OPEN;
	}
	
	public void onStart(){

	}
	
	public DynamicMapRoom(ScenePo scenePo) {
		super(scenePo);
		createdTime=System.currentTimeMillis();
	}
	
	
	public void sendRoomPlayerHorseChat(String msg) {
		for (Fighter player : cellData.getAllCellPlayers()) {
			player.rolePo.sendUpdateHorse(msg);
		}
	}

	
	/**
	 * 常规检测怪物消灭完结束
	 */
	public void checkGameOver() {
		checkMonsterAllDieGameOver();
		checkTimeExpiredGameOver();
		checkPlayerAllLeaveGameOver();
	}

	private void checkPlayerAllLeaveGameOver() {
		if(anyPlayerJoined && cellData.getAllCellPlayers().size()<=0 &&joinPlayers.size()<=0){
			gameOver();
			if(copySceneConfPo!=null){
				PrintUtil.print("没有玩家了，游戏结束:"+copySceneConfPo.getId()+" "+mapRoomId);
			}

		}
	}

	public void checkTimeExpiredGameOver() {
		long time = startRoomGameTime;
		if(time == 0l){
			time = createdTime;
		}
		
		if((System.currentTimeMillis()-time)>=(CopyScenePo.findEntity(copySceneConfPo.getCopySceneId()).getWaitCloseSeconds()*1000)){
			gameOver();
			if(copySceneConfPo!=null){
				PrintUtil.print("时间到了游戏结束:"+copySceneConfPo.getId()+" "+mapRoomId);
			}
		}
	}

	public void checkMonsterAllDieGameOver() {
		int totalWaveSize = copySceneConfPo.listRefreshTime.size();		
		if(cellData.getAllCellMonsterss().size()<=0 && currentFreshIndex>=totalWaveSize){
			gameOver();
			if(copySceneConfPo!=null){
				PrintUtil.print("怪物死光游戏结束:"+copySceneConfPo.getId()+" "+mapRoomId);
			}
		}
		
	}
	
	/**
	 * 0 时间刷怪
	 * 1 波次刷怪
	 * @param reMainMonsterCount
	 */
	public void freshMonster(int reMainMonsterCount) {
		int freshType = copySceneConfPo.getRefreshWay();
		if(freshType==0){
			timeFreshMonster();
		}else if(freshType==1){
			groupFreshMonster(reMainMonsterCount);
		}
	}

	/**
	 * 常规刷怪
	 */
	private void timeFreshMonster() {
		int totalWaveSize = copySceneConfPo.listRefreshTime.size();			
		if(currentFreshIndex<totalWaveSize){
			int currentWaveIndex = copySceneConfPo.listRefreshTime.get(currentFreshIndex).getId();
			int currentWaveWaitSecond = copySceneConfPo.listRefreshTime.get(currentFreshIndex).getNum();
			if((System.currentTimeMillis()-(createdTime))>=1000*currentWaveWaitSecond){
				roomRequireFreshMonsterGroup(currentWaveIndex);
				int currentMonsterCount = cellData.getAllCellMonsterss().size();
				for (Fighter fighter : cellData.getAllCellPlayers()) {
					fighter.rolePo.pVPPVEActivityStatusVo.currentWave=currentWaveIndex;
					fighter.rolePo.pVPPVEActivityStatusVo.remainMonsterCount=currentMonsterCount;
					fighter.rolePo.sendUpdatePVPPVEActivity();
				}
				currentFreshIndex++;
			}			
		}
	}
	
	private void groupFreshMonster(int reMainMonsterCount) {
		int totalWaveSize = copySceneConfPo.listRefreshTime.size();			
		int currentMonsterCount=cellData.getAllCellMonsterss().size();
		if(currentFreshIndex<totalWaveSize){
			int currentWaveIndex = copySceneConfPo.listRefreshTime.get(currentFreshIndex).getId();
			if(currentMonsterCount<=reMainMonsterCount){
				roomRequireFreshMonsterGroup(currentWaveIndex);
				for (Fighter fighter : cellData.getAllCellPlayers()) {
					fighter.rolePo.pVPPVEActivityStatusVo.currentWave=currentWaveIndex;
					fighter.rolePo.pVPPVEActivityStatusVo.remainMonsterCount=currentMonsterCount;
					fighter.rolePo.sendUpdatePVPPVEActivity();
				}
				currentFreshIndex++;
			}			
		}
	}

	public void update(){
		checkActivitySwitchStart();
		checkDoPlay();
		checkCloseTimeExpiredDestroyRoom();
	}
	
	
	public void onGameOver(){
		sendRoomPlayerHorseChat(GlobalCache.fetchLanguageMap("key12"));
	}
	
	/**
	 * 房间活动结束后检测定期销毁房间
	 */
	private void checkCloseTimeExpiredDestroyRoom() {
		if(status==ACTIVITY_STATUS_CLOSE){
			if(startCloseTime!=0l && (System.currentTimeMillis()-startCloseTime)>=(autoDestroySeconds*1000)){
				status=ACTIVITY_STATUS_DESTROYING;
				destroyMeRoom(true);
			}
		}
	}

	public void gameOver() {
		status=ACTIVITY_STATUS_CLOSE;
		startCloseTime=System.currentTimeMillis();
		onGameOver();
	}


}
