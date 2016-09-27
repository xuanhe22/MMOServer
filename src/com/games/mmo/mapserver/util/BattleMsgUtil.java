package com.games.mmo.mapserver.util;

import io.netty.channel.ChannelHandlerContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.Entity;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.bean.Treasure;
import com.games.mmo.mapserver.cell.Cell;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.MonsterType;
import com.games.mmo.type.SystemType;
import com.games.mmo.vo.AgreementCountVo;
import com.google.common.primitives.Doubles;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.component.socket.netty.newServer.MMOByteEncoder;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.util.ThreadLocalUtil;
import com.storm.lib.vo.IdNumberVo;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

public class BattleMsgUtil {

	public static int DEFAULT_MAX_limit=3;
	public static int DEFAULT_limit=3;
	public static int BIGGER_limit=5;
	public static ConcurrentHashMap<String, Long> msgCheckTime=new ConcurrentHashMap<String, Long>();
	public static List<Fighter> abroadBattleMsg(MapRoom stage, byte[] results,RolePo except,Fighter fighter,boolean all,boolean flush,int limit,int msgType) {
		//TODO 【业务标记】stage==null 可能丢消息，或许怪先死了
		
		List<Fighter> abroadPlayers=new ArrayList<Fighter>();
		List<Cell> cellList =fetchCellList(all,fighter,stage,limit);
		if(cellList!=null){
			for (Cell cell: cellList) {
				List<Fighter> cells;
				if(all){
					cells=cell.getAllPlayers();
				}
				else{
					cells=cell.buildAbroadList(fighter, msgType);
				}
				if(cells!=null){
					abroadPlayers.addAll(cells);
				}
			}
		}
		BattleMsgUtil.sendToAll(stage,abroadPlayers,(Entity)fighter,results, except,flush,msgType);
		return abroadPlayers;
	}
	
	public static List<Cell> fetchCellList(boolean all,Fighter fighter, MapRoom stage,int limit) {
		List<Cell> cellList ;
		if(null == fighter || fighter.cell == null || stage==null){
			return null;
		}
		if(all){
			cellList = stage.cellData.allCellList;
		}
		else{
			cellList = stage.cellData.getNearByCells(fighter.cell,limit);
		}
		if(cellList == null){
			return null;
		}
		return cellList;
	}

	public static List<Fighter> abroadBattleMsg(MapRoom stage, byte[] msg,Fighter fighter,Boolean flush,int limit,int msgType,boolean all) {
		return abroadBattleMsg(stage,msg,null,fighter,all,flush,limit,msgType);
	}
	

	public static void abroadRemoveTreasure(Fighter fighter, List<Treasure> treasure) {
		if(fighter==null || fighter.mapRoom==null){
			return;
		}
		Integer pickerId = 0;
		if(fighter!=null){
			pickerId=fighter.mapUniqId;
		}
		List<byte[]> ids = new ArrayList<byte[]>();
		for (Treasure treasure2 : treasure) {
			ids.add(ByteUtil.int2Byte(treasure2.mapUniqId));
		}
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream();
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_REMOVE_TREASURE);
			out.write(ByteUtil.int2Byte(pickerId));
			out.write(ByteUtil.int2Byte(ids.size()));
			for (byte[] b : ids) {
				out.write(b);
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_limit,BATTLE_MSG_TYPE_REMOVE_TREASURE,false);
	}



	public static void abroadAddBuff(Fighter fighter, Integer buffId) {
		if(fighter==null || fighter.mapUniqId==null){
			return;
		}
//		System.out.println("fighter.name = " + fighter.name +"; buffId = " +buffId);
		MapRoom stage = fighter.mapRoom;
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_BUFF);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(buffId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(stage,results,fighter,true,DEFAULT_limit,BATTLE_MSG_TYPE_BUFF,false);
	}

	public static void abroadRemoveBuffer(Fighter fighter, Integer bufferId) {
		MapRoom stage = fighter.mapRoom;
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream();
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_REMOVE_BUFF);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(bufferId));
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(stage,results,fighter,true,DEFAULT_limit,BATTLE_MSG_TYPE_REMOVE_BUFF,false);
	}
	

	public static List<Fighter> abroadSkill(boolean makeSkill,Fighter fighter, Integer skillId,Integer selectEntityId, String targetEntityIds,Integer attractIndex,Fighter spellCaster,Integer petSkillId,float x,float y, float z,List<Integer> entityIds) {
//		PrintUtil.print("abroadSkill() fighter.name = " +fighter.name);
		
		MapRoom stage = fighter.mapRoom;
		byte[] results = null;
		ByteArrayOutputStream  out =new ByteArrayOutputStream();
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_FIGHTER_SKILL);
			out.write(ByteUtil.int2Byte(spellCaster.mapUniqId));
			out.write(ByteUtil.int2Byte(skillId));
			out.write(ByteUtil.int2Byte(selectEntityId));
			Integer val = 0;
			if(entityIds != null){
				val=entityIds.size();
				out.write(ByteUtil.short2Byte(val.shortValue()));
				for (Integer targetId : entityIds) {
					out.write(ByteUtil.int2Byte(targetId));
				}
			}else{
				out.write(ByteUtil.ZERO_SHORT_BYTE);
			}
			
			out.write(ByteUtil.int2SingleByte(attractIndex));
			out.write(ByteUtil.floatToBytes(x));
			out.write(ByteUtil.floatToBytes(y));
			out.write(ByteUtil.floatToBytes(z));
			out.write(ByteUtil.int2Byte(petSkillId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		
		List<Fighter> abroadPlayers = abroadBattleMsg(stage,results,fighter,false,DEFAULT_limit,BATTLE_MSG_TYPE_FIGHTER_SKILL,false);
//		if(stage.findMoverId(selectEntityId)!= null){
//			System.out.println("abroadSkill() skillId = "+skillId+" 攻fighter.name = " + fighter.name + "; 受  selectEntityName = " +stage.findMoverId(selectEntityId).name);			
//		}
		if(makeSkill){
			fighter.makeSkillEffect(skillId,selectEntityId,targetEntityIds,attractIndex,entityIds,abroadPlayers);
		}
		return abroadPlayers;
	}
	
	
	public static void abroadPkStautsChange(Fighter fighter, Integer status) {
		MapRoom stage = fighter.mapRoom;
		fighter.changePkStatus(status);
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_PK_STATUS_CHANGE);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(status));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(stage,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_PK_STATUS_CHANGE,false);
	}

	public static void abroadMoverAttrChange(Fighter fighter) {
		MapRoom stage = fighter.mapRoom;
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_ATTR_CHANGE);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(fighter.lv));
			out.write(ByteUtil.int2Byte(fighter.batMaxHp));
			out.write(ByteUtil.int2Byte(fighter.getBatHp()));
			out.write(ByteUtil.int2Byte(fighter.batMaxMp));
			out.write(ByteUtil.int2Byte(fighter.batMp));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(stage,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_ATTR_CHANGE,false);
		
	}

	public static List<Fighter> abroadBattleMsgWithFilter(MapRoom stage, byte[] msg,RolePo filter,Fighter fighter,Boolean flush,Integer limit,byte msgType) {
		return abroadBattleMsg(stage, msg,filter,fighter,false,flush,limit,msgType);
	}


	public static void abroadHpChange(Fighter mover, int change,int times,int showNum,boolean critical,Fighter caster,boolean flush) {
		if(change==0){
			return;
		}
		Integer casterId=0;
		if(caster!=null){
			casterId=caster.mapUniqId;
		}
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_FIGHTER_HP_CHANGE);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
			out.write(ByteUtil.int2Byte(change));
			out.write(ByteUtil.int2SingleByte(times));
			out.write(ByteUtil.int2Byte(showNum));
			out.write(ByteUtil.boolean2Byte(critical));
			out.write(ByteUtil.int2Byte(casterId));
			out.write(ByteUtil.int2Byte(mover.getBatHp()));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(mover.mapRoom,results,mover.cell!=null?mover:caster,flush,DEFAULT_limit,BATTLE_MSG_TYPE_FIGHTER_HP_CHANGE,false);
		if(mover.rolePo!=null){
			mover.rolePo.tryUpdateTeamMembersHpChange();
		}

	}

	public static void singleAbroadDisappearYunCartCat(ChannelHandlerContext session,List<byte[]> items, boolean flush) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT);
			out.write(ByteUtil.int2Byte(items.size()));
			for (byte[] item : items) {
				out.write(item);
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		if(session!=null){
			BaseSessionUtil.send(session, results, flush);
		}
	}

	public static void singleAbroadDisappearLiving(ChannelHandlerContext session, List<byte[]> items, boolean flush) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DISAPPEAR_LIVINGS);
			Integer val =items.size();
			out.write(ByteUtil.short2Byte(val.shortValue()));
			for (byte[] item : items) {
				out.write(item);
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
//		System.out.println("singleAbroadDisappearLiving:"+out.toByteArray().length);
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		if(session!=null){
			BaseSessionUtil.send(session, results, flush);
		}
	}

	
	public static void singleAbroadMpChange(Fighter mover, int change,boolean flush) {
		if(change==0){
			return;
		}
		if(mover.rolePo==null || mover.robot){
			return;
		}
		byte[] results = null;

		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_FIGHTER_MP_CHANGE);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
			out.write(ByteUtil.int2Byte(change));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		
		ChannelHandlerContext ctx= mover.rolePo.fetchSession();
		if(ctx!=null){
			BaseSessionUtil.send(ctx, results, flush);
		}
	}
	
	public static void abroadMonsterBlink(Fighter mover) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_FIGHTER_BLINK);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
			out.write(ByteUtil.floatToBytes(mover.x));
			out.write(ByteUtil.floatToBytes(mover.y));
			out.write(ByteUtil.floatToBytes(mover.z));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(mover.mapRoom,results,mover,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_FIGHTER_BLINK,false);
	}
	
	public static List<Fighter> abroadMonsterMove(Fighter mover, Float newX, Float newY, Float newZ) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_FIGHTER_MOVE);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
			out.write(ByteUtil.floatToBytes(newX));
			out.write(ByteUtil.floatToBytes(newY));
			out.write(ByteUtil.floatToBytes(newZ));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results=MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		int limit = DEFAULT_limit;
		if(mover.type==Entity.MOVER_TYPE_PLAYER){
			limit=BIGGER_limit;
		}
		else if(mover.type==Entity.MOVER_TYPE_PET){
			limit=BIGGER_limit;
		}
		else if(mover.wasYunDart==1){
			limit=BIGGER_limit;
		}
		return abroadBattleMsg(mover.mapRoom,results,mover.rolePo,mover,false,true,limit,BATTLE_MSG_TYPE_FIGHTER_MOVE);

	}
	

	
	public static void appendBattleHeader(ByteArrayOutputStream out) throws IOException{
		out.write(ByteUtil.int2Byte(ThreadLocalUtil.fetchCurrentThreadId().intValue()));
	}
	
	public static byte[] appendBattleHeader(){
		return ByteUtil.int2Byte(ThreadLocalUtil.fetchCurrentThreadId().intValue());
	}

	public static void abroadFighterRemove(Fighter mover) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DISAPPEAR_LIVING);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(mover.mapRoom,results,mover,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_DISAPPEAR_LIVING,false);
	}
	
	public static void abroadAvatarInfor(Fighter mover) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(32);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_AVATAR_INFOR);
			out.write(ByteUtil.int2Byte(mover.mapUniqId));
			out.write(ByteUtil.StringToBytes(mover.modelAvatar));
			out.write(ByteUtil.StringToBytes(mover.weaponAvatar));
			out.write(ByteUtil.StringToBytes(mover.wingAvatar));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
//		PrintUtil.print("abroadAvatarInfor:"+out.toByteArray().length);
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(mover.mapRoom,results,mover,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_AVATAR_INFOR,false);
	}
	
	
	public static void abroadClientAgentChange(MapRoom stage,Fighter player,Fighter monster) {
		byte[] results = null;
		int playerId =0;
		int monsterId = 0;
		if(player != null ){
			playerId = player.mapUniqId;			
		}
		if(monster != null){
			monsterId = monster.mapUniqId;			
		}
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_AGENT_CHANGE);
			out.write(ByteUtil.int2Byte(playerId));
			out.write(ByteUtil.int2Byte(monsterId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(stage,results,monster,true,DEFAULT_limit,BATTLE_MSG_TYPE_AGENT_CHANGE,false);
	}
	
	
	public static void singleAbroadDie(ChannelHandlerContext ctx,Integer targetEntityId) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DIE);
			out.write(ByteUtil.int2Byte(targetEntityId));
			out.write(ByteUtil.int2Byte(0));
			out.write(ByteUtil.int2Byte(0));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		BaseSessionUtil.send(ctx, results, true);	
	}

	
	public static void abroadDie(Fighter deadFighter, Fighter killFighter, List<byte[]> dropInfor) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DIE);
			if(deadFighter==null){
				out.write(ByteUtil.int2Byte(0));
			}else{
				out.write(ByteUtil.int2Byte(deadFighter.mapUniqId));				
			}
			if(killFighter==null){
				out.write(ByteUtil.int2Byte(0));
			}
			else{
				out.write(ByteUtil.int2Byte(killFighter.mapUniqId));
			}

			if(dropInfor!=null&&!dropInfor.isEmpty()){
				out.write(ByteUtil.int2Byte(dropInfor.size()));
				for (byte[] bytes : dropInfor) {
					out.write(bytes);
				}
			}
			else{
				out.write(ByteUtil.int2Byte(0));
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		if(deadFighter!=null && deadFighter.monsterPo!=null){
			if(deadFighter.monsterPo.getMonsterType()==MonsterType.MONSTER_TYPE10){
//				PrintUtil.print("flag all die:"+deadFighter.mapUniqId);
				abroadBattleMsg(deadFighter.mapRoom, results,deadFighter,true,3,BATTLE_MSG_TYPE_DIE,true);
			}
			else{
				abroadBattleMsg(deadFighter.mapRoom, results,deadFighter,true,3,BATTLE_MSG_TYPE_DIE,false);
			}
		}
		else{
			abroadBattleMsg(deadFighter.mapRoom, results,deadFighter,true,3,BATTLE_MSG_TYPE_DIE,false);
		}

		
	}
	
	// 交任务强行推送前端删除
	public static void abroadyunDartCarRemove(Fighter yunDartFighter, Fighter master){
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT);
			out.write(ByteUtil.int2Byte(1));
			out.write(ByteUtil.int2Byte(yunDartFighter.mapUniqId));
//			System.out.println("删除镖车："+BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT+"|"+1+"|"+yunDartFighter.mapUniqId);
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(yunDartFighter.mapRoom, results, master,true,DEFAULT_limit,BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT,false);
	}
	
	
	public static void abroadDodge(Fighter fighter,Fighter caster) {
		Integer casterId=0;
		if(caster!=null){
			casterId=caster.mapUniqId;
		}
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_DODGE);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(casterId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_DODGE,false);
		
	}
	
	public static void abroadGuildName(Fighter fighter) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(64);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_GUILD_NAME);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.StringToBytes(fighter.guildName));
			out.write(ByteUtil.int2Byte(fighter.guildPosition));
			out.write(ByteUtil.int2Byte(fighter.wasSiegeBid));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_GUILD_NAME,false);
	}
	
	public static void abroadkillNum(Fighter fighter) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_KILL_NUM);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(fighter.killNum));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_KILL_NUM,false);
	}
	
	public static void abroadNowTitleLv(Fighter fighter) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_NOW_TITLE_LV);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(fighter.nowTitleLv));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_NOW_TITLE_LV,false);
	}
	
	public static void abroadMilitaryRankId(Fighter fighter){
		int militaryRankId = 0;
		if(fighter.rolePo != null){
			militaryRankId = fighter.rolePo.getCurrentMilitaryRankId();
		}
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(16);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_MILITARY_RANK);
			out.write(ByteUtil.int2Byte(fighter.mapUniqId));
			out.write(ByteUtil.int2Byte(militaryRankId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
		results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_MILITARY_RANK,false);
	}
	
	public static void abroadRoleChangeName(Fighter fighter){
		if(fighter.rolePo != null){
			byte[] results = null;
			ByteArrayOutputStream out =new ByteArrayOutputStream(32);
			try {
				BattleMsgUtil.appendBattleHeader(out);
				out.write(BATTLE_MSG_TYPE_ROLE_CHANGE_NAME);
				out.write(ByteUtil.int2Byte(fighter.mapUniqId));
				out.write(ByteUtil.StringToBytes(fighter.rolePo.getName()));
			} 
			catch (IOException e) {
				ExceptionUtil.processException(e);
			}
			results=out.toByteArray();
			results =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
			abroadBattleMsg(fighter.mapRoom,results,fighter,true,DEFAULT_MAX_limit,BATTLE_MSG_TYPE_ROLE_CHANGE_NAME,false);	
		}
	}
	
	public static void singleAbroadAppearLivings(ChannelHandlerContext session,List<byte[]> livingInfos, boolean flush) {
		byte[] results = null;
		ByteArrayOutputStream out =new ByteArrayOutputStream(1024);
		try {
			BattleMsgUtil.appendBattleHeader(out);
			out.write(BATTLE_MSG_TYPE_APPEAR_LIVINGS);
			Integer val =livingInfos.size();
			out.write(ByteUtil.short2Byte(val.shortValue()));
			for (byte[] bytes : livingInfos) {
				out.write(bytes);
			}
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		results=out.toByteArray();
//		System.out.println("singleAbroadAppearLivings:"+out.toByteArray().length);
		byte[] newResults =MMOByteEncoder.buildSocketPackage(results,ServerPack.SPECIAL_ORDER_ID_BATTLE);
		BaseSessionUtil.send(session, newResults, flush);
	}

	
    /**
     * 给当前游戏中所有玩家发数据包，排除except玩家
     * 
     * @param pkg
     *            数据包
     * @param except
     *            排除的玩家引用
     */
    public static void sendToAll(MapRoom stage,List<Fighter> abroadPlayerList,Entity fighter,byte[] resultBytes, RolePo except,Boolean flush,int msgType)
    {

        if (abroadPlayerList==null){
            return;
        }
		if(resultBytes==null){
			return;
		}

		try {
			if(GlobalCache.battleMsg==1){
				Integer mapRoomId=stage.mapRoomId;
				Integer sceneId=stage.sceneId;
				if(mapRoomId!=null && sceneId!=null){
					if(Cell.countMap.containsKey(mapRoomId)){
						AgreementCountVo agreementCountVo = Cell.countMap.get(mapRoomId);
						if(agreementCountVo != null){
							agreementCountVo.addTotalCount(abroadPlayerList.size());  
							if(agreementCountVo.agreementMap.containsKey(msgType)){
								IdNumberVo idNumberVo = agreementCountVo.agreementMap.get(msgType);
								if(idNumberVo != null){
									idNumberVo.addNum(abroadPlayerList.size());
								}
							}else{
								IdNumberVo idNumberVo = new IdNumberVo(msgType, abroadPlayerList.size());
								agreementCountVo.agreementMap.put(msgType, idNumberVo);
							}
						}
					}else{
						AgreementCountVo agreementCountVo = new AgreementCountVo();
						agreementCountVo.mapRoomId=mapRoomId;
						agreementCountVo.sceneId=sceneId;
						agreementCountVo.totalCount=1;
						Cell.countMap.put(mapRoomId, agreementCountVo);
					}       					
				}else{
					PrintUtil.print("房间为空");
				}
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		
        RoleTemplate roleTemplate=RoleTemplate.instance();
        for (Fighter player : abroadPlayerList)
        {
        	if(player.rolePo == except){
        		continue;
        	}
        		ChannelHandlerContext session = roleTemplate.iuidSessionMapping.get(player.rolePo.getIuid());
        		if(session != null){
        			if(player.mapRoom!= null){
        				BaseSessionUtil.send(session, resultBytes, flush);	
        			}
        		}
        	}
		}	

	
	/**
	 * 代理改变(客户端管理怪物)
	 */
	public static final byte BATTLE_MSG_TYPE_AGENT_CHANGE=23;

	/**
	 * 生物添加
	 */
	public static final byte BATTLE_MSG_TYPE_APPEAR_LIVING=1;
	/**
	 * 生物移除
	 */
	public static final byte BATTLE_MSG_TYPE_DISAPPEAR_LIVING=2;
	/**
	 * 角色移动
	 */
	public static final byte BATTLE_MSG_TYPE_FIGHTER_MOVE=3;
	/**
	 * 死亡
	 */
	public static final byte BATTLE_MSG_TYPE_DIE=5;

	/**
	 * 技能
	 */
	public static final byte BATTLE_MSG_TYPE_FIGHTER_SKILL=7;
	/**
	 * mp改变
	 */
	public static final byte BATTLE_MSG_TYPE_FIGHTER_MP_CHANGE=9;

	/**
	 * hp改变
	 */
	public static final byte BATTLE_MSG_TYPE_FIGHTER_HP_CHANGE=8;
	/**
	 * 增加buff
	 */
	public static final byte BATTLE_MSG_TYPE_ADD_BUFF=11;
	/**
	 * 移除buff
	 */
	public static final byte BATTLE_MSG_TYPE_REMOVE_BUFF=12;
	/**
	 * 捡东西(删除宝物)
	 */
	public static final byte BATTLE_MSG_TYPE_REMOVE_TREASURE=21;
	/**
	 * pk状态改变
	 */
	public static final byte BATTLE_MSG_TYPE_PK_STATUS_CHANGE=22;
	/**
	 * 属性改变
	 */
	public static final byte BATTLE_MSG_TYPE_ATTR_CHANGE=24;
	/**
	 * avatar改变
	 */
	public static final byte BATTLE_MSG_TYPE_AVATAR_INFOR=30;
	/**
	 * 显示所有宝物
	 */
	public static final byte BATTLE_MSG_TYPE_TREASURE_INFOR=31;
	/**
	 * 闪避
	 */
	public static final byte BATTLE_MSG_TYPE_DODGE=32;
	
	/**
	 * 角色BLINK
	 */
	public static final byte BATTLE_MSG_TYPE_FIGHTER_BLINK=33;

	/**
	 * 生物批量添加
	 */
	public static final byte BATTLE_MSG_TYPE_APPEAR_LIVINGS=35;
	/**
	 * 生物批量移除
	 */
	public static final byte BATTLE_MSG_TYPE_DISAPPEAR_LIVINGS=36;
	/**
	 * 公会ID变更
	 */
	public static final byte BATTLE_MSG_TYPE_GUILD_NAME=37;
	/**
	 * 连斩更新
	 */
	public static final byte BATTLE_MSG_TYPE_KILL_NUM=38;
	
	/**
	 * BUFF效果（新）
	 */
	public static final byte BATTLE_MSG_TYPE_BUFF=39;
	/**
	 * 当前称号更新
	 */
	public static final byte BATTLE_MSG_TYPE_NOW_TITLE_LV=40;
	
	/**
	 * 移除镖车
	 */
	public static final byte BATTLE_MSG_TYPE_DISAPPEAR_YUNCARTCAT =41;
	
	/**
	 * 军衔
	 */
	public static final byte BATTLE_MSG_TYPE_MILITARY_RANK=42;
	
	/**
	 * 角色名修改
	 */
	public static final byte BATTLE_MSG_TYPE_ROLE_CHANGE_NAME=43;


	public static void main(String[] args) {
		int a=126;
		byte[] vals = ByteUtil.int2Byte(a);
		for (byte b : vals) {
			System.out.println(b);
		}
	}




	
	

}
