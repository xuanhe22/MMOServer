package com.games.mmo.mapserver.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.DropPo;
import com.games.mmo.vo.team.TeamMemberVo;
import com.games.rpg.fight.vo.BattleMonsterVo;
import com.storm.lib.component.socket.ServerPack;
import com.storm.lib.util.ByteUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.util.ThreadLocalUtil;

public class Treasure extends Entity{

	public Integer treaureItemId;
	public Integer num;
	public Integer burstFighterId;
	public DropPo dropPo;
	public Long dropTime=0l;
	public List<RolePo> avaPickers = new ArrayList<RolePo>();
//	public static ConcurrentHashMap<Integer, Treasure> treasureTokenMaps = new ConcurrentHashMap<Integer, Treasure>();

	
	public static Treasure create(List<RolePo> avaRoles,int treaureShowType,int num,DropPo dropPo,MapRoom stage,float x,float y,float z){
		Treasure treasure=new Treasure();
		treasure.x=x;
		treasure.y=y;
		treasure.z=z;
		treasure.mapRoom=stage;
		treasure.mapUniqId=stage.entityIndex++;
		treasure.treaureItemId=treaureShowType;
		treasure.num=num;
		treasure.dropPo = dropPo;
		treasure.dropTime=System.currentTimeMillis();
		for (RolePo rolePo : avaRoles) {
			treasure.avaPickers.add(rolePo);
		}
		stage.treasures.add(treasure);
		return treasure;
	}

	public byte[] makeDrop() {
//		treasureTokenMaps.put(mapUniqId, this);
		Integer roleId = 0;
		if(avaPickers!= null && avaPickers.size()>0){
			roleId = avaPickers.get(0).getId();
		}
		ByteArrayOutputStream out=new ByteArrayOutputStream(16);
		try {
			out.write(ByteUtil.int2Byte(mapUniqId));
			out.write(ByteUtil.int2Byte(treaureItemId));
			out.write(ByteUtil.int2Byte(num));
			out.write(ByteUtil.int2Byte(roleId));
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
		return out.toByteArray();
	}
	
	
	public static final int TREASURE_SHOW_TYPE_COIN_1=1;
	public static final int TREASURE_SHOW_TYPE_COIN_2=2;
	public static final int TREASURE_SHOW_TYPE_COIN_3=3;
	public static final int TREASURE_SHOW_TYPE_COIN_4=4;
	public static final int TREASURE_SHOW_TYPE_COIN_5=5;
	public static final int TREASURE_SHOW_TYPE_HP_1=6;
	public static final int TREASURE_SHOW_TYPE_HP_2=7;
	public static final int TREASURE_SHOW_TYPE_HP_3=8;
	public static final int TREASURE_SHOW_TYPE_HP_4=9;
	public static final int TREASURE_SHOW_TYPE_HP_5=10;
	public static final int TREASURE_SHOW_TYPE_MP_1=11;
	public static final int TREASURE_SHOW_TYPE_MP_2=12;
	public static final int TREASURE_SHOW_TYPE_MP_3=13;
	public static final int TREASURE_SHOW_TYPE_MP_4=14;
	public static final int TREASURE_SHOW_TYPE_MP_5=15;


	

	@Override
	public boolean realPlayer() {
		return false;
	}
	

}
