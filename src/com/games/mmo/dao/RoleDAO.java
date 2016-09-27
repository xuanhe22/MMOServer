package com.games.mmo.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RankType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.GuildInvitionVo;
import com.games.mmo.vo.RankVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
@Controller
public class RoleDAO extends BaseDAO{
	public boolean existRoleName(String name) {
//		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where name=? and abandom_state is null",new Object[]{name});
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where name=? ",new Object[]{name});
		if(roleId!=null){
			return true;
		}
		return false;
	}
	
	public Integer fetchRoleIdByRoleName(String name){
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where name=? and abandom_state is null",new Object[]{name});
		return roleId;
	}
	
	public boolean existRoleiuid(String iuid) {
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where iuid=? and abandom_state is null",new Object[]{iuid});
		if(roleId!=null){
			return true;
		}
		return false;
	}
	
	public boolean existRoleiuidAndpass(String iuid,String password) {
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where iuid=? and pssd=? and abandom_state is null",new Object[]{iuid,password});
		if(roleId!=null){
			return true;
		}
		return false;
	}
	public Boolean existRole(Integer id) {
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_role where id=? and abandom_state is null",new Object[]{id});
		if(roleId!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据用户名查找id
	 * @param name
	 * @return
	 */
	public Integer getRoleIdByName(String name,Integer minLv,Integer minVipLv,Integer maxLv,Integer maxVipLv) {
		return queryIntForSql("select id from {#USER_DB}.u_po_role where name=? and abandom_state is null and lv<="+maxLv+" and vip_lv<="+maxVipLv+" and lv>="+minLv+" and vip_lv>="+minVipLv,new Object[]{name});
	}

	public Integer getRoleIdByToken(String token) {
		return queryIntForSql("select id from {#USER_DB}.u_po_role where token=? and abandom_state is null",new Object[]{token});
	}

	

	
	public Integer findRoleIdById(Integer keyword) {
		return queryIntForSql("select id from {#USER_DB}.u_po_role where id=? and abandom_state is null",new Object[]{keyword});
	}

	public Integer findRoleIdByName(String keyword) {
		return queryIntForSql("select id from {#USER_DB}.u_po_role where name=? and abandom_state is null",new Object[]{keyword});
	}
	
	public Integer findRoleIdByInvitationCode(String invitationCode){
		return queryIntForSql("select id from {#USER_DB}.u_po_role where invitation_code=? and abandom_state is null",new Object[]{invitationCode});
	}
	
	public List queryForList(String string) {
		return jdbcTemplate.queryForList(string);
	}
	
	public void generateRank(){
		PrintUtil.print("生成排行数据");
		String[] sqls=new String[6];
		String cleanSql="truncate table "+BaseStormSystemType.USER_DB_NAME+".u_po_rank";
		sqls[0]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_LV+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null order by a.lv DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		sqls[1]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_POWER+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null order by a.battle_power DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		sqls[2]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_TOTAL_GOLD+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null order by a.gold DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		sqls[3]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_DOTA_LV+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null order by a.tower_current_lv DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		sqls[4]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_PET+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null and rpet_fighter_id!=0 order by a.pet_prower DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		sqls[5]="insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_rank(`role_id`,`role_lv`,`role_career`,`role_name`,`rank_lv`,`role_power`,`type`,`rank_gold`,`wing_star`,`equip_weapon_id`,`equip_armor_id`,`tower_current_lv`,`rpet_fighter_id`,`pet_prower`,`achievement_sum`) select a.id as role_id,a.lv as role_lv,a.career as role_career,a.name as role_name,(@rownum := @rownum+1) as rank_lv,a.battle_power as role_power,"+RankType.BATTLE_RANK_TYPE_ACHIEVEPOINT+" as type,a.gold as rank_gold,a.wing_star,a.equip_weapon_id,a.equip_armor_id,a.tower_current_lv,a.rpet_fighter_id,a.pet_prower,a.achievement_sum from "+BaseStormSystemType.USER_DB_NAME+".u_po_role a,(SELECT @rownum:=0) b  where a.name is not null and a.abandom_state is null order by a.achievement_sum DESC LIMIT "+RankType.BATTLE_RANK_LIMIT;
		execute(cleanSql);
		for (String sql : sqls) {
			if(StringUtil.isEmpty(sql)){
				continue;
			}
			
			execute(sql);
		}

		String[] currentRankTypes= new String[]{String.valueOf(RankType.BATTLE_RANK_TYPE_LV),
												String.valueOf(RankType.BATTLE_RANK_TYPE_POWER),
												String.valueOf(RankType.BATTLE_RANK_TYPE_TOTAL_GOLD),
												String.valueOf(RankType.BATTLE_RANK_TYPE_DOTA_LV),
												String.valueOf(RankType.BATTLE_RANK_TYPE_PET),
												String.valueOf(RankType.BATTLE_RANK_TYPE_ACHIEVEPOINT)};
		for (String currentRankType : currentRankTypes) {
			List<RankVo> rankVos =new ArrayList<RankVo>();
			List rows = queryForList("select * from "+BaseStormSystemType.USER_DB_NAME+".u_po_rank where type="+currentRankType+" order by id");
			Iterator it = rows.iterator();      
			while(it.hasNext()) {
				Map userMap = (Map) it.next();   
				RankVo rankVo = new RankVo();
				rankVo.id=Integer.valueOf(userMap.get("id").toString());
				rankVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
				rankVo.roleLv=Integer.valueOf(userMap.get("role_lv").toString());
				rankVo.roleCareer=userMap.get("role_career")!=null?Integer.valueOf(userMap.get("role_career").toString()):0;
				rankVo.roleName=userMap.get("role_name")!=null?userMap.get("role_name").toString():"unNamed";
				rankVo.rankLv=Integer.valueOf(userMap.get("rank_lv").toString());
				rankVo.rolePower=userMap.get("role_power")!=null?Integer.valueOf(userMap.get("role_power").toString()):0;
				rankVo.rankGold=userMap.get("rank_gold")!=null?Integer.valueOf(userMap.get("rank_gold").toString()):0;
				rankVo.type=Integer.valueOf(userMap.get("type").toString());
				rankVo.wingStar = Integer.valueOf(userMap.get("wing_star").toString());
				Integer equipWeaponId = userMap.get("equip_weapon_id")!=null?Integer.valueOf(userMap.get("equip_weapon_id").toString()):0;
				Integer equipArmorId =  userMap.get("equip_armor_id")!=null?Integer.valueOf(userMap.get("equip_armor_id").toString()):0;
				rankVo.towerCurrentLv= userMap.get("tower_current_lv")!=null?Integer.valueOf(userMap.get("tower_current_lv").toString()):0;
				if(String.valueOf(RankType.BATTLE_RANK_TYPE_DOTA_LV).equals(currentRankType)){
					rankVo.rolePower=rankVo.towerCurrentLv;
				}else if(String.valueOf(RankType.BATTLE_RANK_TYPE_PET).equals(currentRankType)){
					rankVo.rolePower = userMap.get("pet_prower")!=null?Integer.valueOf(userMap.get("pet_prower").toString()):0;
					int rPetFighterId = userMap.get("rpet_fighter_id")!=null?Integer.valueOf(userMap.get("rpet_fighter_id").toString()):0;
					if( rPetFighterId != 0){
						RpetPo rpetPo = RpetPo.findEntity(rPetFighterId);
						if(rpetPo != null){
							PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
							if(petPo != null){
								rankVo.petId=petPo.getId();
								rankVo.petAvatar= petPo.getModel();		
							}
						}
					}
				}else if(String.valueOf(RankType.BATTLE_RANK_TYPE_ACHIEVEPOINT).equals(currentRankType)){
					rankVo.rolePower = userMap.get("achievement_sum")!=null?Integer.valueOf(userMap.get("achievement_sum").toString()):0;
				}
				
				try {
					List<IdNumberVo> hiddenFashions = new ArrayList<IdNumberVo>();
					hiddenFashions.add(new IdNumberVo(1, 1));
					hiddenFashions.add(new IdNumberVo(2, 1));
					CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(equipWeaponId, equipArmorId, null, rankVo.roleCareer, 1,rankVo.wingStar,hiddenFashions);
					rankVo.makeAvatars(commonAvatarVo);
					
				} catch (Exception e) {
					ExceptionUtil.processException(e);
					continue;
				}

				rankVos.add(rankVo);
			} 
			GlobalCache.rankMap.put(currentRankType,rankVos);	 
		}
		
		// 公会排行
		String sql = "select a.id,a.name,a.lv,a.battle_power,a.leader_role_id,a.leader_role_name,a.member_count,a.auto_join,(@rownum := @rownum+1) as rank_lv from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild a,(SELECT @rownum:=0) b order by a.battle_power desc LIMIT "+RankType.BATTLE_RANK_LIMIT;
//		System.out.println("sql="+sql);
		List rows = queryForList(sql);
		Iterator it = rows.iterator(); 
		List<RankVo> GuildRankVos =new ArrayList<RankVo>();
		while(it.hasNext()) {
			Map userMap = (Map) it.next();  
			RankVo rankVo = new RankVo();
			rankVo.guildId = Integer.valueOf(userMap.get("id").toString());
			rankVo.guildName = userMap.get("name")!=null?userMap.get("name").toString():"unNamed";
			rankVo.rolePower= userMap.get("battle_power")!=null?Integer.valueOf(userMap.get("battle_power").toString()):0;
			rankVo.rankLv= Double.valueOf(userMap.get("rank_lv").toString()).intValue();
			rankVo.type= RankType.BATTLE_RANK_TYPE_GOUILD;
			int leaderRoleId = userMap.get("leader_role_id")!=null?Integer.valueOf(userMap.get("leader_role_id").toString()):0;
			RolePo rolePo = RolePo.findEntity(leaderRoleId);
			
			if(rolePo != null){
				rankVo.roleId=rolePo.getId();
				rankVo.roleName=rolePo.getName();
				try {
					CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
					rankVo.makeAvatars(commonAvatarVo);
					
				} catch (Exception e) {
					ExceptionUtil.processException(e);
					continue;
				}
			}
			
			GuildRankVos.add(rankVo);
		}
		GlobalCache.rankMap.put(String.valueOf(RankType.BATTLE_RANK_TYPE_GOUILD),GuildRankVos);
		
		List<RankVo> listDR = new ArrayList<RankVo>();
		for(RankVo rankVo : GlobalCache.rankMap.get(String.valueOf(RankType.BATTLE_RANK_TYPE_DOTA_LV))){
			RankVo rv = new RankVo();
			rv.id=rankVo.id;
			rv.roleId=rankVo.roleId;
			rv.roleLv=rankVo.roleLv;
			rv.roleCareer=rankVo.roleCareer;
			rv.roleName=rankVo.roleName;
			rv.rankLv=rankVo.rankLv;
			rv.rolePower=rankVo.rolePower;
			rv.type=rankVo.type;
			rv.rankGold=rankVo.rankGold;
			rv.weaponAvatar=rankVo.weaponAvatar;
			rv.armorAvatar=rankVo.armorAvatar;
			rv.wingAvatar=rankVo.wingAvatar;
			rv.wingStar=rankVo.wingStar;
			rv.towerCurrentLv=rankVo.towerCurrentLv;
			rv.petAvatar=rankVo.petAvatar;
			rv.petId=rankVo.petId;
			rv.guildId=rankVo.guildId;
			rv.guildName=rankVo.guildName;
			listDR.add(rv);
		}
		
		GlobalCache.listDotaRank= new CopyOnWriteArrayList<RankVo>(listDR);
	}
	
	/**
	 * 根据账号密码查看User是否存在
	 * @param iuid
	 * @param password
	 * @return
	 */
	public boolean existUseriuidAndpass(String iuid,String password) {
		Integer userId= queryIntForSql("select id from {#USER_DB}.u_po_user where iuid=? and pssd=?",new Object[]{iuid,password});
		if(userId!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * 根据iuid查看账户名是否重复
	 * @param iuid
	 * @return
	 */
	public boolean existUseriuid(String iuid) {
		Integer userId= queryIntForSql("select id from {#USER_DB}.u_po_user where iuid=?",new Object[]{iuid});
		if(userId!=null){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * 方法功能:找到所有的玩家
	 * 更新时间:2014-6-25, 作者:johnny
	 * @return
	 */
	public List<Integer> fetchAllRoleIds(Integer minlv,Integer minVipLv,Integer maxLv,Integer maxVipLv) {
		return jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where abandom_state is null and lv<="+maxLv+" and vip_lv<="+maxVipLv +" and lv>="+minlv+" and vip_lv>="+minVipLv,Integer.class);
	}	
	

	public boolean existGuildName(String name) {
		Integer roleId= queryIntForSql("select id from {#USER_DB}.u_po_guild where name=?",new Object[]{name});
		if(roleId!=null){
			return true;
		}
		return false;
	}

	public List<Integer> fetchAllChannelRoleIds(String channels, Integer minLv,Integer minVipLv, Integer maxLv, Integer maxVipLv) {
		return jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where channel_key in ("+channels+") and abandom_state is null and lv<="+maxLv+" and vip_lv<="+maxVipLv +" and lv>="+minLv+" and vip_lv>="+minVipLv,Integer.class);
	}
	
	public static RoleDAO instance() {
		RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
		return roleDAO;
	}

}
