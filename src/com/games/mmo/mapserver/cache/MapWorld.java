package com.games.mmo.mapserver.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.map.activity.BloodSeekerBastionRoom;
import com.games.mmo.mapserver.bean.map.activity.DemonizationCrisisRoom;
import com.games.mmo.mapserver.bean.map.activity.FreeWarRoom;
import com.games.mmo.mapserver.bean.map.activity.GuildBossRoom;
import com.games.mmo.mapserver.bean.map.activity.GuildPriestRoom;
import com.games.mmo.mapserver.bean.map.activity.KilllingTowerRoom;
import com.games.mmo.mapserver.bean.map.activity.PKGreatRoom;
import com.games.mmo.mapserver.bean.map.activity.ZaphieHaramRoom;
import com.games.mmo.mapserver.bean.map.single.Single20LevelRoom;
import com.games.mmo.mapserver.bean.map.single.Single40LevelRoom;
import com.games.mmo.mapserver.bean.map.single.Single60LevelRoom;
import com.games.mmo.mapserver.bean.map.single.Single80LevelRoom;
import com.games.mmo.mapserver.bean.map.single.SingleArenaRoom;
import com.games.mmo.mapserver.bean.map.single.SingleExpRoom;
import com.games.mmo.mapserver.bean.map.single.SingleGoldRoom;
import com.games.mmo.mapserver.bean.map.single.SingleMatRoom;
import com.games.mmo.mapserver.bean.map.single.SingleNewbieBattleRoom;
import com.games.mmo.mapserver.bean.map.single.SinglePetRoom;
import com.games.mmo.mapserver.bean.map.single.SinglePlotRoom;
import com.games.mmo.mapserver.bean.map.single.SingleTowerRoom;
import com.games.mmo.mapserver.bean.map.single.temp.SingleKillPkPlayerRoom;
import com.games.mmo.mapserver.bean.map.team.TeamLiveOrDeadRoom;
import com.games.mmo.mapserver.bean.map.team.TeamMonstertInvadeRoom;
import com.games.mmo.mapserver.bean.map.team.TeamTowerRoom;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.vo.MapRoomParVo;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Single;

public class MapWorld {
	public static ConcurrentHashMap<Integer,Fighter> yunDartFighterMap = new ConcurrentHashMap<Integer,Fighter>();
	public static ConcurrentHashMap<Integer, MapRoom> mapRooms=new ConcurrentHashMap<Integer, MapRoom>();
	private static int dynamicMapRoomIdIndex=1;

	public static ConcurrentHashMap<Integer,ConcurrentHashMap<Integer, MapRoom>> activityMapRooms = new ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,MapRoom>>();
	
    public static MapRoom findStage(Integer stageId){
    	return mapRooms.get(stageId);
    }
    
    public static MapRoom findActivityStage(Integer copySceneConfPoId, int stageId){
    	return activityMapRooms.get(copySceneConfPoId).get(stageId);
    }
    
    public static  ConcurrentHashMap<Integer, MapRoom> findActivityRoom(Integer copySceneConfPoId){
    	return activityMapRooms.get(copySceneConfPoId);
    }
    
    /**
     * 创建静态房间
     * @param scenePo
     * @return
     */
	public static MapRoom createStaticMapRoom(ScenePo scenePo) {
		MapRoom mapRoom = new MapRoom(scenePo);
		mapRoom.sceneId=scenePo.getId();
		mapRoom.mapRoomId=scenePo.getId();
		mapRoom.buildMonsterFreshInforVos();
		MapWorld.mapRooms.put(mapRoom.mapRoomId,mapRoom);
		return mapRoom;
	}
	
	/**
	 * 创建动态房间
	 * @param copySceneConfPo
	 * @param scenePo
	 * @return
	 */
	public static synchronized MapRoom createDynalicMapRoom(CopySceneConfPo copySceneConfPo,ScenePo scenePo,MapRoomParVo mapRoomParVo) {
		int roomId=dynamicMapRoomIdIndex++;
		MapRoom mapRoom = newMapRoom(scenePo,mapRoomParVo);
		mapRoom.sceneId=scenePo.getId();
		mapRoom.mapRoomId=roomId;
//		System.out.println("create mapRoomId == "+mapRoom.mapRoomId);
		mapRoom.isDynamic=true;
		MapWorld.mapRooms.put(mapRoom.mapRoomId,mapRoom);
		mapRoom.copySceneConfPo=copySceneConfPo;
		if(copySceneConfPo==null){
			ExceptionUtil.throwConfirmParamException("不存在copySceneConfPo");
		}
		joinAcitvityMapRoom(copySceneConfPo.getId(), mapRoom);
		return mapRoom;
	}
	
	public static synchronized MapRoom createDynalicMapRoom(CopySceneConfPo copySceneConfPo,ScenePo scenePo) {
		return createDynalicMapRoom(copySceneConfPo,scenePo,new MapRoomParVo());
	}
	
	/**
	 * 创建公会副本
	 * @param copySceneConfPo
	 * @param scenePo
	 * @param guildId
	 * @return
	 */
	public static MapRoom createGuildPoDynalicMapRoom(CopySceneConfPo copySceneConfPo,ScenePo scenePo, Integer guildId){
		MapRoomParVo mapRoomParVo=new MapRoomParVo();
		mapRoomParVo.isLeaderRooom=true;
		MapRoom mapRoom = createDynalicMapRoom(copySceneConfPo, scenePo,new MapRoomParVo());
		mapRoom.guildId = guildId;
		return mapRoom;
	}
	
	/**
	 * 地图创建
	 * @param scenePo
	 * @return
	 */
	private static MapRoom newMapRoom(ScenePo scenePo,MapRoomParVo mapRoomParVo)
	{

		MapRoom mapRoom = null;
		Integer sceneId = scenePo.getId();
		//公会房间id
		Integer[] guildBossSceneIds = new Integer[]{20100019,20100020,20100021,20100022,20100023,20100024,20100025};
		for(int i = 0;  i< guildBossSceneIds.length; i++){
			if(sceneId.intValue() == guildBossSceneIds[i]){
				sceneId = GuildBossRoom.SCENE_ID;
				break;
			}
		}
		switch (sceneId) {
		case BloodSeekerBastionRoom.SCENE_ID:
			mapRoom = new BloodSeekerBastionRoom(scenePo);
			break;
		case DemonizationCrisisRoom.SCENE_ID:
			mapRoom = new DemonizationCrisisRoom(scenePo);
			break;
		case PKGreatRoom.SCENE_ID:
			mapRoom = new PKGreatRoom(scenePo);
			break;
		case ZaphieHaramRoom.SCENE_ID:
			mapRoom = new ZaphieHaramRoom(scenePo);
			break;
		case KilllingTowerRoom.SCENE_ID:
			mapRoom = new KilllingTowerRoom(scenePo,mapRoomParVo.isLeaderRooom);
			break;
		case GuildBossRoom.SCENE_ID:
			mapRoom = new GuildBossRoom(scenePo);
			break;
		case TeamTowerRoom.SCENE_ID:
			mapRoom = new TeamTowerRoom(scenePo);
			break;
		case TeamMonstertInvadeRoom.SCENE_ID:
			mapRoom = new TeamMonstertInvadeRoom(scenePo);
			break;
		case SingleExpRoom.SCENE_ID:
			mapRoom = new SingleExpRoom(scenePo);
			break;	
		case SingleGoldRoom.SCENE_ID:
			mapRoom = new SingleGoldRoom(scenePo);
			break;	
		case SingleMatRoom.SCENE_ID:
			mapRoom = new SingleMatRoom(scenePo);
			break;	
		case SinglePetRoom.SCENE_ID:
			mapRoom = new SinglePetRoom(scenePo);
			break;	
		case SingleTowerRoom.SCENE_ID:
			mapRoom = new SingleTowerRoom(scenePo);
			break;	
		case SingleArenaRoom.SCENE_ID:
			mapRoom = new SingleArenaRoom(scenePo);
			break;			
		case SingleKillPkPlayerRoom.SCENE_ID:
			mapRoom = new SingleKillPkPlayerRoom(scenePo);
			break;	
		case SingleNewbieBattleRoom.SCENE_ID:
			mapRoom = new SingleNewbieBattleRoom(scenePo);
			break;	
		case SinglePlotRoom.SCENE_ID:
			mapRoom = new SinglePlotRoom(scenePo);
			break;
		case FreeWarRoom.SCENE_ID:
			mapRoom = new FreeWarRoom(scenePo);
			break;	
		case Single20LevelRoom.SCENE_ID:
			mapRoom = new Single20LevelRoom(scenePo);
			break;
		case Single40LevelRoom.SCENE_ID:
			mapRoom = new Single40LevelRoom(scenePo);
			break;
		case Single60LevelRoom.SCENE_ID:
			mapRoom = new Single60LevelRoom(scenePo);
			break;
		case Single80LevelRoom.SCENE_ID:
			mapRoom = new Single80LevelRoom(scenePo);
			break;
		case GuildPriestRoom.SCENE_ID:
			mapRoom = new GuildPriestRoom(scenePo);
			break;
		case TeamLiveOrDeadRoom.SCENE_ID:
			mapRoom = new TeamLiveOrDeadRoom(scenePo);
			break;
		default:
			ExceptionUtil.throwConfirmParamException("没这个场景ID:"+sceneId);
			break;
		}
		mapRoom.createdTime=System.currentTimeMillis();
		return mapRoom;
	}
	
	/**
	 * 加入活动副本
	 * @param copySceneConfPoId
	 * @param mapRoom
	 */
	public static void joinAcitvityMapRoom(Integer copySceneConfPoId, MapRoom mapRoom){
		if(IntUtil.checkInInts(copySceneConfPoId.intValue(), CopySceneType.COPY_SCENE_CONF_ACTIVITY_MAP_ROOM) ||
			IntUtil.checkInInts(copySceneConfPoId.intValue(), CopySceneType.COPY_SCENE_CONF_GOUILD_BOSS)){
			if(activityMapRooms.get(copySceneConfPoId) == null){
				activityMapRooms.put(copySceneConfPoId, new ConcurrentHashMap<Integer, MapRoom>());
			}
			activityMapRooms.get(copySceneConfPoId).put(mapRoom.mapRoomId,mapRoom);			
		}
	}
	
	
	
	/**
	 * 删除动态房间
	 * @param dynalicMapRoomId
	 */
	public static void removeMapRoom(Integer dynalicMapRoomId){
		if(dynalicMapRoomId != null){
			MapRoom mapRoom =MapRoom.findStage(dynalicMapRoomId);
			if(mapRoom != null && mapRoom.isDynamic){
				MapWorld.mapRooms.remove(dynalicMapRoomId);	
				for(Integer i : MapWorld.activityMapRooms.keySet()){
					MapRoom mp = MapWorld.activityMapRooms.get(i).get(dynalicMapRoomId);
					if(mp != null){
						MapWorld.activityMapRooms.get(i).remove(dynalicMapRoomId);
						break;
					}
				}
			}
		}
	}
	
	/** 添加镖车 */
	public static void addYunDartFighter(Fighter fighter, RolePo rolePo){
		yunDartFighterMap.put(rolePo.getId(), fighter);
	}
	/** 删除镖车 */
	public static void removeYunDartFighter(RolePo rolePo){
		yunDartFighterMap.remove(rolePo.getId());
	}
	
	/** 删除所有运镖车 */
	public static void removeAllYunDartFighter(){
//		System.out.println("removeAllYunDartFighter() ");
		for(Integer roleId : yunDartFighterMap.keySet()){
			Fighter fighter =  yunDartFighterMap.get(roleId);
			RolePo rolePo = RolePo.findEntity(roleId);
			if(rolePo != null){
				rolePo.initYunDartInfo(null);
			}
		}
		yunDartFighterMap.clear();
		for(MapRoom mapRoom : MapWorld.mapRooms.values()){
			if(!mapRoom.isDynamic){
				mapRoom.cellData.removeAllYunDartFighter();
			}
		}
	}
}
