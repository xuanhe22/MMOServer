package com.games.mmo.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.games.mmo.vo.GuildInvitionVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.role.RoleBriefVo;
import com.games.mmo.vo.role.RolePackItemVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo3;

public class MergeUtil {

	public static String dbField(String main_pack_item_vos) {
		if(main_pack_item_vos==null){
			return "null";
		}
		if(main_pack_item_vos.equals(DBFieldUtil.emptySignore)){
			return "null";
		}
		return "'"+main_pack_item_vos+"'";
	}
	
	public static String dbFieldInteger(int num){
		if(num!=-2){
			return String.valueOf(num);
		}
		return "null";
	}
	
	public static int dbFieldInteger2(int num){
		if(num!=-2){
			return num;
		}
		return 0;
	}
	

	public static void resetServerData(String targetDb) {
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_live_activity");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_product");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_rank");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_rank_arena");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_abroad");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_block");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_forbid");
    	BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+".u_po_copy_scene_activity");
    	BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_flag set guild_id=null,flag_status=0");
    	BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_global set value_str=null where id in (2,3,5,6,7,8)");
    	BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_global set value_str='"+System.currentTimeMillis()+"' where id=7");
    	BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_role set special_title_lv=null");
		BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_flag set guild_id=null");
		BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_guild set applys=null");
	}

	public static void inserToTables(String[] tables, String sourceDb, String targetDb) {
		for (String tableName : tables) {
			PrintUtil.print("process Table:"+tableName);

			PrintUtil.print("select * from "+sourceDb+"."+tableName);
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select * from "+sourceDb+"."+tableName);
			SqlRowSetMetaData sqlRsmd = rs.getMetaData();  
			int columnCount = sqlRsmd.getColumnCount();  
			List<String> titles = new ArrayList<String>();
			for (int i = 1; i <= columnCount; i++) {  
				titles.add(sqlRsmd.getColumnName(i));
			}
			for (String title : titles) {
				if(title.equals("old_id")){
					titles.remove(title);
					break;
				}
			}
			
			
			String fromColumns = StringUtil.implode(titles, ",");
			PrintUtil.print("fromColumns="+fromColumns);
			for(int i=0;i<titles.size();i++){
				if(titles.get(i).equals("id")){
					titles.set(i, "old_id");
				}
			}
			String toColumns = StringUtil.implode(titles, ",");
			PrintUtil.print("toColumns="+toColumns);
			String sql="insert into "+targetDb+"."+tableName+"("+toColumns+") select "+fromColumns+" from "+sourceDb+"."+tableName;
			PrintUtil.print("sql="+sql);
			BaseDAO.instance().jdbcTemplate.execute(sql);
			PrintUtil.print("  ");
		}
	}

	public static void cleanTables(String[] tables, String targetDb) {
		PrintUtil.print("cleaning Tables...");
		for (String tableName : tables) {
			BaseDAO.instance().jdbcTemplate.execute("truncate "+targetDb+"."+tableName);
			BaseDAO.instance().jdbcTemplate.execute("ALTER TABLE "+targetDb+"."+tableName+" AUTO_INCREMENT=1");
		}
	}

	public static HashMap<String, HashMap<Integer, Integer>> fetchOldIds(String[] tables, String targetDb) {
		HashMap<String, HashMap<Integer, Integer>> oldNewIdMap=new HashMap<String, HashMap<Integer,Integer>>();
		for (String tableName : tables) {
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,old_id from "+targetDb+"."+tableName+" where old_id is not null");
			oldNewIdMap.put(tableName, new HashMap<Integer, Integer>());
			oldNewIdMap.get(tableName).put(0, -2);
			while(rs.next()){
				oldNewIdMap.get(tableName).put(rs.getInt(2), rs.getInt(1));
//				PrintUtil.print("tablename: "+tableName+"; old_id: "+rs.getInt(2)+"; id: "+rs.getInt(1));
			}
		}
		return oldNewIdMap;
	}

	public static List<String> replacePoAuctionItem(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		List<String> sqls=new ArrayList<String>();
		String toChangeTableName="u_po_auction_item";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,equip_id,seller_role_id,seller_role_name from "+targetDb+"."+toChangeTableName+" where old_id is not null");
		while(rs.next()){
			int id = rs.getInt(1);
			int equip_id =rs.getInt(2);
			int seller_role_id=rs.getInt(3);
			String seller_role_name=rs.getString(4);
			equip_id=oldNewIdMap.get("u_po_equip").get(equip_id);
			seller_role_id=oldNewIdMap.get("u_po_role").get(seller_role_id);
			BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+"."+toChangeTableName+" set equip_id="+equip_id+",seller_role_id="+seller_role_id+" where id="+id);
		}

		return sqls;
	}

	public static List<String> replacePoRole(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		List<String> sqls=new ArrayList<String>();
		String toChangeTableName="u_po_role";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,user_id,equip_weapon_id,equip_ring_id,equip_pants_id,equip_bracelet_id,equip_armor_id,equip_necklace_id,equip_bracer_id,equip_shoe_id,equip_helmet_id,equip_belt_id,main_pack_item_vos,warehouse_pack_item_vos,guild_id,guild_invitions,rpets,rpet_fighter_id,role_infor_id,invitation_friend,friends,blocks,enemys from "+targetDb+"."+toChangeTableName +" where old_id is not null");
//		BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+"."+toChangeTableName+" set product_today_buyed=null,");
		while(rs.next()){
			int id = rs.getInt(1);
			int user_id =rs.getInt(2);
			int equip_weapon_id =rs.getInt(3);

			int equip_ring_id =rs.getInt(4);
			int equip_pants_id =rs.getInt(5);
			int equip_bracelet_id =rs.getInt(6);
			int equip_armor_id =rs.getInt(7);
			int equip_necklace_id =rs.getInt(8);
			int equip_bracer_id =rs.getInt(9);
			int equip_shoe_id =rs.getInt(10);
			int equip_helmet_id =rs.getInt(11);
			int equip_belt_id =rs.getInt(12);
			String main_pack_item_vos =rs.getString(13);
			String warehouse_pack_item_vos = rs.getString(14);
			int guild_id =rs.getInt(15);
			String guild_invitions =rs.getString(16);
			String rpets =rs.getString(17);
			int rpet_fighter_id =rs.getInt(18);
			int role_infor_id =rs.getInt(19);
			String invitation_friend =rs.getString(20);
			String friends=rs.getString(21);
			String blocks=rs.getString(22);
			String enemys=rs.getString(23);
			

			
			user_id=oldNewIdMap.get("u_po_user").get(user_id);
			equip_weapon_id=oldNewIdMap.get("u_po_equip").get(equip_weapon_id);
			equip_ring_id=oldNewIdMap.get("u_po_equip").get(equip_ring_id);
			equip_pants_id=oldNewIdMap.get("u_po_equip").get(equip_pants_id);
			equip_bracelet_id=oldNewIdMap.get("u_po_equip").get(equip_bracelet_id);
			equip_armor_id=oldNewIdMap.get("u_po_equip").get(equip_armor_id);
			equip_necklace_id=oldNewIdMap.get("u_po_equip").get(equip_necklace_id);
			equip_bracer_id=oldNewIdMap.get("u_po_equip").get(equip_bracer_id);
			equip_shoe_id=oldNewIdMap.get("u_po_equip").get(equip_shoe_id);
			equip_helmet_id=oldNewIdMap.get("u_po_equip").get(equip_helmet_id);
			equip_belt_id=oldNewIdMap.get("u_po_equip").get(equip_belt_id);
			
			
			HashMap<String, RolePackItemVo> mainPackItemVosMap=new HashMap<String, RolePackItemVo>();
			if (main_pack_item_vos != null) {
				String[] items = StringUtil.split(main_pack_item_vos, ",");
				if(items!=null && items.length >0){
					mainPackItemVosMap= new HashMap<String, RolePackItemVo>();
				}
				for (String itemStr : items) {
					RolePackItemVo rolePackItemVo = new RolePackItemVo();
					if (itemStr != null && itemStr.length() > 0) {
						rolePackItemVo.loadProperty(itemStr, "|");
						mainPackItemVosMap.put(rolePackItemVo.getIndex()
								.toString(), rolePackItemVo);
					}
				}			
			}
			for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
				if(rolePackItemVo.wasEquip()){
					rolePackItemVo.setEquipId(oldNewIdMap.get("u_po_equip").get(rolePackItemVo.getEquipId()));
				}
			}
			
			String[] objs = new String[mainPackItemVosMap.size()];
			int i = 0;
			for (RolePackItemVo rolePackItemVo : mainPackItemVosMap.values()) {
				Object[] props = rolePackItemVo.fetchProperyItems();
				String targetStr = DBFieldUtil.createPropertyImplod(props, "|");
				objs[i] = targetStr;
				i++;
			}
			main_pack_item_vos=StringUtil.implode(objs, ",");
			
			HashMap<String, RolePackItemVo> warehousePackItemVosMap=new HashMap<String, RolePackItemVo>();
			if (warehouse_pack_item_vos != null) {
				String[] items = StringUtil.split(warehouse_pack_item_vos, ",");
				if(items!=null && items.length >0){
					warehousePackItemVosMap= new HashMap<String, RolePackItemVo>();
				}
				for (String itemStr : items) {
					RolePackItemVo rolePackItemVo = new RolePackItemVo();
					if (itemStr != null && itemStr.length() > 0) {
						rolePackItemVo.loadProperty(itemStr, "|");
						warehousePackItemVosMap.put(rolePackItemVo.getIndex()
								.toString(), rolePackItemVo);
					}
				}			
			}
			for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
				if(rolePackItemVo.wasEquip()){
					rolePackItemVo.setEquipId(oldNewIdMap.get("u_po_equip").get(rolePackItemVo.getEquipId()));
				}
			}
			
			String[] warehouseObjs = new String[warehousePackItemVosMap.size()];
			int n = 0;
			for (RolePackItemVo rolePackItemVo : warehousePackItemVosMap.values()) {
				Object[] props = rolePackItemVo.fetchProperyItems();
				String targetStr = DBFieldUtil.createPropertyImplod(props, "|");
				warehouseObjs[n] = targetStr;
				n++;
			}
			warehouse_pack_item_vos=StringUtil.implode(warehouseObjs, ",");

			
			guild_id=oldNewIdMap.get("u_po_guild").get(guild_id);

			List<GuildInvitionVo> listGuildInvitions = new ArrayList<GuildInvitionVo>();
			if (guild_invitions != null) {
				String[] items = StringUtil.split(guild_invitions, ",");
				if(items!=null && items.length>0){
					listGuildInvitions=new CopyOnWriteArrayList<GuildInvitionVo>();
				}
				for (String itemStr : items) {
					GuildInvitionVo guildInvitionVo = new GuildInvitionVo();
					guildInvitionVo.loadProperty(itemStr, "|");
					listGuildInvitions.add(guildInvitionVo);
				}			
			}

			for (GuildInvitionVo guildInvitionVo : listGuildInvitions) {
				guildInvitionVo.guildLeaderRoleId=oldNewIdMap.get("u_po_role").get(guildInvitionVo.guildLeaderRoleId);
			}
			

			if (listGuildInvitions.size() <= 0) {
				guild_invitions = null;
			}
			else{
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < listGuildInvitions.size(); j++) {
					GuildInvitionVo guildInvitionVo = listGuildInvitions.get(j);
					Object[] objs2 = guildInvitionVo.fetchProperyItems();
					String targetStr = DBFieldUtil.createPropertyImplod(objs2, "|");
					list.add(targetStr);
				}
				guild_invitions=StringUtil.implode(list, ",");
			}

			List<Integer> listRpetIds = DBFieldUtil.getIntegerListByCommer(rpets);
			
			for(int j=0;j<listRpetIds.size();j++){
				Integer rPetId=oldNewIdMap.get("u_po_rpet").get(listRpetIds.get(j));
				if(rPetId==null){
					ExceptionUtil.throwConfirmParamException("listRpetIds.get(j):"+listRpetIds.get(j));
				}
				listRpetIds.set(j, rPetId);
			}
			rpets=DBFieldUtil.implod(listRpetIds, ",");
			if(rpets!=null &&rpets.equals("0")){
				rpets=null;
			}
//			PrintUtil.print(rpet_fighter_id);
			if(oldNewIdMap.get("u_po_rpet").containsKey(rpet_fighter_id)){
				rpet_fighter_id=oldNewIdMap.get("u_po_rpet").get(rpet_fighter_id);
			}
			else{
				rpet_fighter_id=-1;
			}

			role_infor_id=oldNewIdMap.get("u_po_role_infor").get(role_infor_id);
			
//			List<InvitationRoleVo> listInvitationFriend=new ArrayList<InvitationRoleVo>();
//			if(!StringUtil.isEmpty(invitation_friend)){
//				String[] items = StringUtil.split(invitation_friend, ",");
//				if(items != null && items.length>0){
//					listInvitationFriend = new CopyOnWriteArrayList<InvitationRoleVo>();			
//				}
//				for (String itemStr : items) {
//					InvitationRoleVo invitationRoleVo = new InvitationRoleVo();
//					invitationRoleVo.loadProperty(itemStr, "|");
//					listInvitationFriend.add(invitationRoleVo);
//				}			
//			}
//			
//			for (InvitationRoleVo invitationRoleVo : listInvitationFriend) {
//				PrintUtil.print(invitationRoleVo.id);
//				invitationRoleVo.id=oldNewIdMap.get("u_po_role").get(invitationRoleVo.id);
//			}
			
			
//			if(listInvitationFriend== null ||listInvitationFriend.size() == 0){
//				invitation_friend = null;
//			}
//			List<String> list = new ArrayList<String>();
//			for (int j = 0; j < listInvitationFriend.size(); j++) {
//				InvitationRoleVo invitationRoleVo = listInvitationFriend.get(j);
//				Object[] objs3 = invitationRoleVo.fetchProperyItems();
//				String targetStr = DBFieldUtil.createPropertyImplod(objs3, "|");
//				list.add(targetStr);
//			}
//			invitation_friend=StringUtil.implode(list, ",");	
			
			List<Integer> newListFriends=new ArrayList<Integer>();
			List<Integer> newListBlocks=new ArrayList<Integer>();
			List<Integer> newListEnemys=new ArrayList<Integer>();
			
			List<Integer> oldListFriends = DBFieldUtil.getIntegerListByCommer(friends);
			List<Integer> oldListBlocks = DBFieldUtil.getIntegerListByCommer(blocks);
			List<Integer> oldListEnemys = DBFieldUtil.getIntegerListByCommer(enemys);
			if(oldListFriends!=null && oldListFriends.size()>0){
				for(Integer friendId : oldListFriends){
					Integer newId = oldNewIdMap.get("u_po_role_infor").get(friendId);
					if(newId!=null){
						newListFriends.add(newId);						
					}
				}
			}
			
			if(oldListBlocks!=null && oldListBlocks.size()>0){
				for(Integer blockId : oldListBlocks){
					Integer newId =  oldNewIdMap.get("u_po_role_infor").get(blockId);
					if(newId != null){
						newListBlocks.add(newId);						
					}
				}
			}
			
			if(oldListEnemys!=null && oldListEnemys.size()>0){
				for(Integer enemyId : oldListEnemys){
					Integer newId =  oldNewIdMap.get("u_po_role_infor").get(enemyId);
					if(newId != null){
						newListEnemys.add(newId);						
					}
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("update ").append(targetDb).append(".").append(toChangeTableName).append(" set ");
			sb.append(" user_id=").append(user_id).append(", ");
			sb.append(" equip_weapon_id=").append(MergeUtil.dbFieldInteger(equip_weapon_id)).append(", ");
			sb.append(" equip_ring_id=").append(MergeUtil.dbFieldInteger(equip_ring_id)).append(", ");
			sb.append(" equip_pants_id=").append(MergeUtil.dbFieldInteger(equip_pants_id)).append(", ");
			sb.append(" equip_bracelet_id=").append(MergeUtil.dbFieldInteger(equip_bracelet_id)).append(", ");
			sb.append(" equip_armor_id=").append(MergeUtil.dbFieldInteger(equip_armor_id)).append(", ");
			sb.append(" equip_necklace_id=").append(MergeUtil.dbFieldInteger(equip_necklace_id)).append(", ");
			sb.append(" equip_bracer_id=").append(MergeUtil.dbFieldInteger(equip_bracer_id)).append(", ");
			sb.append(" equip_shoe_id=").append(MergeUtil.dbFieldInteger(equip_shoe_id)).append(", ");
			sb.append(" equip_helmet_id=").append(MergeUtil.dbFieldInteger(equip_helmet_id)).append(", ");
			sb.append(" equip_belt_id=").append(MergeUtil.dbFieldInteger(equip_belt_id)).append(", ");
			sb.append(" main_pack_item_vos=").append(MergeUtil.dbField(main_pack_item_vos)).append(", ");
			sb.append(" warehouse_pack_item_vos=").append(MergeUtil.dbField(warehouse_pack_item_vos)).append(", ");
			sb.append(" guild_id=").append(MergeUtil.dbFieldInteger2(guild_id)).append(", ");
			sb.append(" guild_invitions=").append(MergeUtil.dbField(guild_invitions)).append(", ");
			sb.append(" rpets=").append(MergeUtil.dbField(rpets)).append(", ");
			sb.append(" rpet_fighter_id=").append(MergeUtil.dbFieldInteger2(rpet_fighter_id)).append(", ");
			sb.append(" role_infor_id=").append(MergeUtil.dbFieldInteger(role_infor_id)).append(", ");
			sb.append(" invitation_friend=").append(MergeUtil.dbField(invitation_friend)).append(", ");
			sb.append(" friends=").append(MergeUtil.dbField(DBFieldUtil.implod(newListFriends, ","))).append(", ");
			sb.append(" blocks=").append(MergeUtil.dbField(DBFieldUtil.implod(newListBlocks, ","))).append(", ");
			sb.append(" enemys=").append(MergeUtil.dbField(DBFieldUtil.implod(newListEnemys, ","))).append(" ");
			sb.append(" where id=").append(id);
			sqls.add(sb.toString());
			
//			sqls.add("update "+targetDb+"."+toChangeTableName+" set user_id="+user_id+"," +
//					"equip_weapon_id="+equip_weapon_id+"," +
//					"equip_ring_id="+equip_ring_id+"," +
//					"equip_pants_id="+equip_pants_id+"," +
//				    "equip_bracelet_id="+equip_bracelet_id+"," +
//				    "equip_armor_id="+equip_armor_id+"," +
//				    "equip_necklace_id="+equip_necklace_id+"," +
//				    "equip_bracer_id="+equip_bracer_id+"," +
//				    "equip_shoe_id="+equip_shoe_id+"," +
//				    "equip_helmet_id="+equip_helmet_id+"," +
//				    "equip_belt_id="+equip_belt_id+"," +
//				    "main_pack_item_vos="+MergeUtil.dbField(main_pack_item_vos)+"," +
//				    "guild_id="+guild_id+"," +
//				    "guild_invitions="+MergeUtil.dbField(guild_invitions)+"," +
//				    "rpets="+MergeUtil.dbField(rpets)+"," +
//				    "rpet_fighter_id="+rpet_fighter_id+"," +
//				    "role_infor_id="+role_infor_id+"," +
//				    "invitation_friend="+MergeUtil.dbField(invitation_friend)+" where id="+id);

		
			
		}
		return sqls;
	}

	public static List<String> replacePoRoleInfor(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		List<String> sqls=new ArrayList<String>();
		String toChangeTableName="u_po_role_infor";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,role_id from "+targetDb+"."+toChangeTableName +" where old_id is not null");
		while(rs.next()){
			int id = rs.getInt(1);
			int role_id =rs.getInt(2);
			if(oldNewIdMap.get("u_po_role").containsKey(role_id)){
				role_id=oldNewIdMap.get("u_po_role").get(role_id);
			}
			else{
				PrintUtil.print("---------error: skip role_id:"+role_id);
				role_id=-3;
			}

			sqls.add("update "+targetDb+"."+toChangeTableName+" set role_id="+role_id+" where id="+id);
		}
		return sqls;
	}

	public static List<String> replacePoUser(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		List<String> sqls=new ArrayList<String>();
		String toChangeTableName="u_po_user";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,role_briefs from "+targetDb+"."+toChangeTableName +" where old_id is not null");
		while(rs.next()){
			int id = rs.getInt(1);
			String role_briefs=rs.getString(2);
			List<RoleBriefVo> listRoleBriefVo=new ArrayList<RoleBriefVo>();
			
			if(role_briefs!=null){
				String[] items = StringUtil.split(role_briefs,",");
				for (String itemStr : items) {
					RoleBriefVo roleBriefVo = new RoleBriefVo();
					roleBriefVo.loadProperty(itemStr, "|");
					listRoleBriefVo.add(roleBriefVo);
				}	
			}
			
			for (RoleBriefVo roleBriefVo : listRoleBriefVo) {
				roleBriefVo.setRoleId(oldNewIdMap.get("u_po_role").get(roleBriefVo.getRoleId()));
			}
			
			if(listRoleBriefVo.size()<=0){
				role_briefs = null;
			}
			List<String> list = new ArrayList<String>();
			for(int i=0; i<listRoleBriefVo.size(); i++){
				RoleBriefVo roleBrief = listRoleBriefVo.get(i);
				Object[] objs = roleBrief.fetchProperyItems();
				String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
				list.add(targetStr);
			}
			role_briefs=StringUtil.implode(list,",");
			sqls.add("update "+targetDb+"."+toChangeTableName+" set role_briefs="+MergeUtil.dbField(role_briefs)+" where id="+id);
		}
		return sqls;

	}

	public static List<String> replacePoMail(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		List<String> sqls=new ArrayList<String>();
		String toChangeTableName="u_po_mail";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,sender_role_id,receiver_role_id,attaches from "+targetDb+"."+toChangeTableName +" where old_id is not null");
		while(rs.next()){
			int id = rs.getInt(1);
			int sender_role_id =rs.getInt(2);
			int receiver_role_id=rs.getInt(3);
			String attaches=rs.getString(4);
			List<IdNumberVo3> awardPos = IdNumberVo3.createList(attaches);
			for (IdNumberVo3 idNumberVo3 : awardPos) {
				if(idNumberVo3.getInt1().intValue() == 2){
					if(oldNewIdMap.get("u_po_equip").containsKey(idNumberVo3.getInt2())){
						idNumberVo3.setInt2(oldNewIdMap.get("u_po_equip").get(idNumberVo3.getInt2()));
					}
					else{
						idNumberVo3.setInt2(-4);
					}

				}
					
			}
			attaches=IdNumberVo3.createStr(awardPos, "|", ",");
			if(oldNewIdMap.get("u_po_role").get(sender_role_id) !=null){
				sender_role_id=oldNewIdMap.get("u_po_role").get(sender_role_id);
			}
			if(oldNewIdMap.get("u_po_role").get(receiver_role_id)!=null){
				receiver_role_id=oldNewIdMap.get("u_po_role").get(receiver_role_id);
			}
			sqls.add("update "+targetDb+"."+toChangeTableName+" set sender_role_id="+sender_role_id+",receiver_role_id="+receiver_role_id+",attaches="+MergeUtil.dbField(attaches)+" where id="+id);

		}
		return sqls;
	}

	public static List<String> replacePoGuild(HashMap<String, HashMap<Integer, Integer>> oldNewIdMap,String targetDb) {
		String toChangeTableName="u_po_guild";
		List<String> sqls=new ArrayList<String>();
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select id,leader_role_id,members from "+targetDb+"."+toChangeTableName +" where old_id is not null");
		while(rs.next()){
			int id = rs.getInt(1);
			int leader_role_id =rs.getInt(2);
			String members=rs.getString(3);
			leader_role_id=oldNewIdMap.get("u_po_role").get(leader_role_id);
			
			List<GuildMemberVo> listMembers =new ArrayList<GuildMemberVo>();
			
			if(members!=null){
				String[] items = StringUtil.split(members,",");
				for (String itemStr : items) {
					GuildMemberVo guildMemberVo = new GuildMemberVo();
					guildMemberVo.loadProperty(itemStr, "|");
					listMembers.add(guildMemberVo);
				}
			}
			for (GuildMemberVo guildMemberVo : listMembers) {
				guildMemberVo.roleId=oldNewIdMap.get("u_po_role").get(guildMemberVo.roleId);
			}
			
			
			if(listMembers.size()<=0){
				members = null;
			}
			else{
				List<String> list = new ArrayList<String>();
				for(int i=0; i<listMembers.size(); i++){
					GuildMemberVo guildMemberVo = listMembers.get(i);
					Object[] objs = guildMemberVo.fetchProperyItems();
					String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
					list.add(targetStr);
				}
				members=StringUtil.implode(list,",");
			}
			sqls.add("update "+targetDb+"."+toChangeTableName+" set leader_role_id="+leader_role_id+",members="+MergeUtil.dbField(members)+" where id="+id);
		}		
		return sqls;
	}

	public static void resetOldId(String targetDb,Integer targetServerId, String[] tables) {
		BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+"."+"u_po_user"+" set server_id="+targetServerId+" where old_id is not null");
		for (String tableName : tables) {
			BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+"."+tableName+" set old_id=null");
		}
		
	}

	public static HashMap<String,Integer> fetchRoleNames(String sourceDb) {
		HashMap<String,Integer> names=new HashMap<String, Integer>();
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select name,id from "+sourceDb+".u_po_role where abandom_state is null");
		while(rs.next()){
			names.put(rs.getString(1), rs.getInt(2));
		}
		return names;
	}

	public static HashMap<String,Integer> fetchGuildNames(String sourceDb) {
		HashMap<String,Integer> names=new HashMap<String, Integer>();
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select name,id from "+sourceDb+".u_po_guild");
		while(rs.next()){
			names.put(rs.getString(1), rs.getInt(2));
		}
		return names;
	}

	public static void exportMerge(String targetDb, String sourceDb,Integer targetServerId, String targetServerPrefix1, String targetServerPrefix2, Integer clean) {
//    	a)	角色重名：合服后如有重名的角色，在服务器合并完成后将在角色名前加上【XX】，例如，4、5区均有角色名为奇迹无双的俩个玩家，则合服后4区的玩家名字变为“【S4】奇迹无双”，5区的玩家名字为“【S5】奇迹无双”。考虑到会增加角色名长度，是否会将现有玩家取名规定的长度上再缩减4-6个字符。
//    	b)	帮派重名：合服后如有重名的工会，在服务器合并完成后将在工会名前加上【XX】，例如，4、5区均有工会名为龙破九天的俩个帮派，则合服后4区的工会名字变为“【S4】龙破九天”，5区的工会名字为“【S5】龙破九天”。考虑到会增加工会名长度，是否会将现有工会取名规定的长度上再缩减4-6个字符。
		String[] tables = new String[]{"u_po_auction_item","u_po_role_infor","u_po_equip","u_po_mail","u_po_guild","u_po_rpet","u_po_user","u_po_role"};
		if(clean==1){
			MergeUtil.cleanTables(tables,targetDb);
			for (String tableName : tables) {
				String alterSql="ALTER TABLE "+targetDb+"."+tableName+"	ADD COLUMN `old_id`  int(11)";
				try {
					BaseDAO.instance().jdbcTemplate.execute(alterSql);
				} catch (DataAccessException e) {
					ExceptionUtil.throwConfirmParamException(e.toString());
				}				
			}
			PrintUtil.print(" cleaned tables ...");
			return;
		}
    	HashMap<String,Integer> oldRoleNames = MergeUtil.fetchRoleNames(sourceDb);
    	HashMap<String,Integer> oldGuildNames = MergeUtil.fetchGuildNames(sourceDb);
    	
    	HashMap<String,Integer> newRoleNames = MergeUtil.fetchRoleNames(targetDb);
    	HashMap<String,Integer> newGuildNames = MergeUtil.fetchGuildNames(targetDb);
    	
    	PrintUtil.print("start exportMerge "+sourceDb+" -->> "+targetDb);
    	PrintUtil.print("reseting data...");
    	MergeUtil.resetServerData(targetDb);


		
    	PrintUtil.print("inserting tables... begin");
    	MergeUtil.inserToTables(tables,sourceDb,targetDb);
    	PrintUtil.print("inserting tables... end");
		//合服账号角色合并,角色重名处理
    	for (String oldRoleName : oldRoleNames.keySet()) {
    		if(newRoleNames.containsKey(oldRoleName)){
    			String newName="【"+targetServerPrefix1+"】"+oldRoleName;
				if(newName.length()>12){
    				newName=newName.substring(0, 12);
				}
				Integer newId = newRoleNames.get(oldRoleName);
				Integer oldId = oldRoleNames.get(oldRoleName);
				String oldName="【"+targetServerPrefix2+"】"+oldRoleName;
				if(oldName.length()>12){
					oldName=oldName.substring(0, 12);
				}
				BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_role set name='"+newName+"' where id="+newId);
				BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_role set name='"+oldName+"' where old_id="+oldId);
//				PrintUtil.print("update "+targetDb+".u_po_role set name='"+newName+"' where id="+newId);
//				PrintUtil.print("update "+targetDb+".u_po_role set name='"+oldName+"' where old_id="+oldId);
    		}
		}
    	
    	for (String oldGuildName : oldGuildNames.keySet()) {
    		if(newGuildNames.containsKey(oldGuildName)){
    			String newName="【"+targetServerPrefix1+"】"+oldGuildName;
				if(newName.length()>12){
    				newName=newName.substring(0, 12);
				}
				Integer newId = newGuildNames.get(oldGuildName);
				Integer oldId = oldGuildNames.get(oldGuildName);
				String oldName="【"+targetServerPrefix2+"】"+oldGuildName;
				if(oldName.length()>12){
					oldName=oldName.substring(0, 12);
				}
				BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_guild set name='"+newName+"' where id="+newId);
				BaseDAO.instance().jdbcTemplate.execute("update "+targetDb+".u_po_guild set name='"+oldName+"' where old_id="+oldId);
//				PrintUtil.print("update "+targetDb+".u_po_guild set name='"+newName+"' where id="+newId);
//				PrintUtil.print("update "+targetDb+".u_po_guild set name='"+oldName+"' where old_id="+oldId);
    		}
		}
    	
    	
		PrintUtil.print("fetching old ids");
		HashMap<String, HashMap<Integer, Integer>> oldNewIdMap=MergeUtil.fetchOldIds(tables,targetDb);

		PrintUtil.print("replacing u_po_auction_item");
		
		List<String> sqlsToRun=new ArrayList<String>();


		List<String> sqls =MergeUtil.replacePoAuctionItem(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);

		
		PrintUtil.print("replacing u_po_role");
		
		sqls =MergeUtil.replacePoRole(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);
		
		
		PrintUtil.print("replacing u_po_role_infor");
		sqls =MergeUtil.replacePoRoleInfor(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);

				
		PrintUtil.print("replacing u_po_user");
		sqls =MergeUtil.replacePoUser(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);
		
		
		
		PrintUtil.print("replacing u_po_mail");
		sqls =MergeUtil.replacePoMail(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);
		

		PrintUtil.print("replacing u_po_guild");
		sqls =MergeUtil.replacePoGuild(oldNewIdMap,targetDb);
		sqlsToRun.addAll(sqls);

		MergeUtil.resetOldId(targetDb,targetServerId,tables);
		
		PrintUtil.print("start to run sql:"+sqlsToRun.size());
		String sqlString = "";
		try {
			DataSource datasource =(DataSource) BeanUtil.getBean("dataSource");
			Connection conn = datasource.getConnection();
			conn.setAutoCommit(false);
			Statement stat = conn.createStatement();
			for (String sql : sqlsToRun) {
				sqlString = sql;
				stat.execute(sql);
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			PrintUtil.print("sqlString= "+sqlString);
			ExceptionUtil.processException(e);
		}
		
		PrintUtil.print("finish export "+sourceDb);
	}


}
