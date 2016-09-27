package com.games.mmo.remoting;

import io.netty.channel.ChannelHandlerContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.CopyScenePo;
import com.games.mmo.po.game.FashionPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.ScenePo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.RoleService;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RankType;
import com.games.mmo.type.RoleType;
import com.games.mmo.type.TaskType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.DbUtil;
import com.games.mmo.util.GameUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.LogVo;
import com.games.mmo.vo.RankVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.role.RoleBriefVo;
import com.games.mmo.vo.role.RoleFashionVo;
import com.games.mmo.vo.xml.ConstantFile.Global.CareerItems.ReplaceItem;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.uniqcall.client.UniqCallClientTemplate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
@Controller
public class RoleRemoting extends BaseRemoting{
	@Autowired
	private RoleDAO roleDAO;
	@Autowired
	private RoleService roleService;
	@Autowired
	private BaseDAO baseDAO;
	@Autowired
	private RoleTemplate roleTemplate;
	@Autowired
	private ChatService chatService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private UniqCallClientTemplate uniqCallTemplate;
	/**
	 * 创建角色
	 * @param userId
	 * @param name
	 * @param modelId
	 * @return
	 */
	public Object createRole(Integer userId,String name,Integer modelId){ 
//		PrintUtil.print("createRole() userId = " +userId + ";  name="+name + "; modelId = "+modelId);
		name=CheckUtil.checkIllegelName(name);
		//TODO 【业务标记】暂时去掉
		if(!BaseStormSystemType.ALLOW_CHEAT){
			CheckUtil.checkValidString(name, 12);
			CheckUtil.checkContianFiltedWord(name, true,RoleType.ROLE_FILTERS);
		}
		
		if(roleService.existRoleName(name)){
			SessionUtil.addDataArray(null);
			SessionUtil.addDataArray(null);
			SessionUtil.addDataArray(2);
			return 1;
		}
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		if((name == null || CheckUtil.checkRolePoNameWasIllegal(name,true)) && (!"ko".equals(String.valueOf(gp.valueObj)))){
			SessionUtil.addDataArray(null);
			SessionUtil.addDataArray(null);
			SessionUtil.addDataArray(3);
			return 1;
		}

		RolePo rolePo =	roleService.creteRole(userId,name,modelId);
		rolePo.skills.add(1);
		UserPo userPo = UserPo.findEntity(userId);

		
		BaseDAO.instance().syncToDB(userPo);
		rolePo=roleService.login(rolePo);
		SessionUtil.addDataArray(rolePo);
		SessionUtil.addDataArray(userPo);
		SessionUtil.addDataArray(1);
		SessionUtil.addDataArray(rolePo.getId());
		return userPo;
	}
	
	public Object checkDeleteRole(Integer userId){
		UserPo userPo = UserPo.findEntity(userId);
		userPo.checkAndRemove();
		return userPo;
	}
	
	//TODO 【业务标记】低优先级-userId可以传入风险有点大,考虑session检测
	public Object deleteRole(Integer userId,Integer roleId){
		UserPo userPo = UserPo.findEntity(userId);
		RoleBriefVo roleBriefVo = userPo.findRoleBriefByRoleId(roleId);
		if(roleBriefVo==null){
			return null;
		}
		if(roleBriefVo.getRoleLv()>=20){
			if(roleBriefVo.removeTime.equals("0")){
				roleBriefVo.removeTime=""+(System.currentTimeMillis()+48*3600*1000);
			}
			else{
				ExceptionUtil.throwConfirmParamException("already marked");
			}
		}
		else{
			RolePo rolePo = RolePo.findEntity(roleBriefVo.getRoleId());
			if(rolePo!=null){
				rolePo.setAbandomState(1);
			}
			userPo.listRoleBriefVo.remove(roleBriefVo);
		}
		userPo.srotListRoleBriefVo();
		return userPo;
	}
	
	public Object unDeleteRole(Integer userId,Integer roleId){
		UserPo userPo = UserPo.findEntity(userId);
		RoleBriefVo roleBriefVo = userPo.findRoleBriefByRoleId(roleId);
		if(!roleBriefVo.removeTime.equals("0")){
			roleBriefVo.removeTime="0";
		}
		userPo.srotListRoleBriefVo();
		return userPo;
	}
	
	
	/**
	 * 登陆
	 * @param iuid	账号
	 * @param pass	密码
	 * @return rolePo
	 */
	public Object login(Integer roleId){
		CheckUtil.checkIsNull(roleId);
		RolePo rolePo = RolePo.findEntity(roleId);
//		System.out.println("getResourceSceneTime="+rolePo.getResourceSceneTime()+" | "+rolePo.getName());
		String key = rolePo.getId() +"_RoleRemoting.login";
		GlobalCache.checkProtocolFrequencyResponse(key, 2500l,true);
		RolePo rolePo2=roleService.login(rolePo);
		SessionUtil.addDataArray(rolePo2);
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object logoff(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		if(rolePo == null){
			return 1;
		}
		UserPo userPo=UserPo.findEntity(rolePo.getUserId());
		ChannelHandlerContext ioSession = SessionUtil.getCurrentSession();
		roleService.logoff(ioSession, 0);
		SessionUtil.addDataArray(userPo);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 
	 * 方法功能:获得角色任务
	 * 更新时间:2014-6-27, 作者:johnny
	 * @return
	 */
	public Object fetchRoleTasks(){
//		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		//rolePo.checkFreshRoleTaskFinish();
//		SessionUtil.addDataArray(rolePo.listRoleTaskArrays);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 提升翅膀(升星+升阶)
	 * @param consumeType 是否用钻石 0:用道具; 1：用钻石
	 * @param wasAuto 是否自动消耗道具 0：手动; 1:自动
	 * @return
	 */
	public Object upgradeWing(Integer consumeType, Integer wasAuto){
		CheckUtil.checkIsNull(consumeType);
		CheckUtil.checkIsNull(wasAuto);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.upgradeWing";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.upgradeWing(consumeType, wasAuto);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateWingInfor();
		rolePo.sendUpdateMainPack(false);
		SessionUtil.addDataArray(1);
		LogUtil.writeLog(rolePo, 318, rolePo.getLv(), rolePo.getCareer(),rolePo.getVipLv(), "", "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 翅膀隐藏和显示
	 * @param type
	 * @return
	 */
	public Object hiddenOrShowWing(Integer type){
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.hiddenOrShowWing";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(type.intValue() == 0){
			rolePo.setWingWasHidden(0);
		}else if(type.intValue() == 1){
			rolePo.setWingWasHidden(1);
		}
		rolePo.sendUpdateWingInfor();
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
		rolePo.fighter.makeAvatars(commonAvatarVo,true);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 
	 * 方法功能:获得排行信息
	 * 更新时间:2014-6-27, 作者:johnny
	 * @return
	 */
	public Object getRankInfor(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.getRankInfor";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		SessionUtil.addDataArray(GlobalCache.rankMap);
		return SessionType.MULTYPE_RETURN;	
	}
	
	
	/**
	 * 角色根据类型设置状态
	 * @param optionsType
	 * @param optionsStatus 1:默认； 0:拒绝
	 * @return
	 */
	public Object roleOptionsStatusByType(Integer optionsType,Integer optionsStatus){
		CheckUtil.checkIsNull(optionsType);
		CheckUtil.checkIsNull(optionsStatus);
		if(optionsType == null || optionsStatus == null){
			return 0;
		}
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.roleOptionsStatusByType";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		for(IdNumberVo inv : rolePo.listRoleOptions){
			if(inv.getId().intValue() == optionsType.intValue()){
				inv.setNum(optionsStatus);
				break;
			}
		}
		rolePo.sendUpdateRoleOptions();
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 升级称号等级
	 * @return 
	 */
	public Object upgradeTitle(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.upgradeTitle";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.upgradeTitle();
		rolePo.sendUpdateRoleBatProps(false);
		SessionUtil.addDataArray(rolePo.getAchievePoint());
		SessionUtil.addDataArray(rolePo.getTitleLv());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 奖励找回
	 * @param retrieveId 奖励找回ID
	 * @param type 奖励找回类型（1金币找回2钻石找回）
	 * @return
	 */
	public Object awardRetrieve(Integer retrieveId, Integer type)
	{
		CheckUtil.checkIsNull(retrieveId);
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.awardRetrieve";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.awardRetrieve(retrieveId,  type);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateSkillPoint();
		SessionUtil.addDataArray(rolePo.listAwardRetrieve);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 奖励找回（全部）
	 * @param retrieveId 奖励找回ID
	 * @param type 奖励找回类型（1金币找回2钻石找回）
	 * @return
	 */
	public Object allRetrieve(Integer type)
	{
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.allRetrieve";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.allRetrieve(type);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateSkillPoint();
		SessionUtil.addDataArray(rolePo.listAwardRetrieve);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 时装够买
	 * @param id 时装ID
	 * @return
	 */
	public Object buyFashion(Integer id, Integer time)
	{
		CheckUtil.checkIsNull(id);
		CheckUtil.checkIsNull(time);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.buyFashion";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		checkService.checkExisFashionPo(id);
//		System.out.println("buyFashion() " +rolePo.getName() + " id = " +id +"; time = " +time);
		rolePo.buyFashion( id, time);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 穿戴时装
	 * @param id 时装ID
	 * @return
	 */
	public Object useFashion(Integer id)
	{
		CheckUtil.checkIsNull(id);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.useFashion";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.removeFashion();
		FashionPo fashionPo = FashionPo.findEntity(id);
		int type = fashionPo.getType().intValue();
		for(RoleFashionVo roleFashionVo : rolePo.roleFashions){
			if(roleFashionVo.id == id.intValue()){
				roleFashionVo.isUse = 1;
			}else{
				if(roleFashionVo.fashionPo.getType().intValue()==type){
					roleFashionVo.isUse = 0;
				}
			}
		}
		rolePo.roleFashion = new CopyOnWriteArrayList<RoleFashionVo>();
		for(RoleFashionVo roleFashionVo:rolePo.roleFashions){
			if(roleFashionVo.isUse==1){
				rolePo.roleFashion.add(roleFashionVo);
			}
		}
		rolePo.updateRoleFashion();
		if(rolePo.fighter != null && rolePo.fighter.mapRoom != null && rolePo.fighter.cell != null){
			CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
			rolePo.fighter.makeAvatars(commonAvatarVo,true);
		}
		rolePo.sendAddFashion();
		rolePo.calculateBat(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 检查时装到期
	 * @return
	 */
	public Object checkRemoveFashion(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.useFashion";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println("checkRemoveFashion() role.name = " + rolePo.getName());
		rolePo.removeFashion();
		rolePo.calculateBat(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 是否隐藏时装
	 * @param id
	 * @return
	 */
	public Object wasHiddenFashion(Integer type){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.undressFashion";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		for(IdNumberVo idNumberVo:rolePo.hiddenFashions){
			if(idNumberVo.getId().intValue()==type.intValue()){
				if(idNumberVo.getNum().intValue() == 0){
					idNumberVo.setNum(1);
				}else{
					idNumberVo.setNum(0);
				}
				break;
			}
		}
		rolePo.setWasHiddenFashion(IdNumberVo.createStr(rolePo.hiddenFashions));
		rolePo.sendAvatars();
		SessionUtil.addDataArray(rolePo.getWasHiddenFashion());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	public String fetchRoleInfor(String name){
		RolePo rolePo = roleService.findRoleByName(name);
		if(rolePo==null){
			return "不存在角色:"+name;
		}
		String str2=rolePo.fetchRoleInfor();
		
		return str2;
	} 
	
	public String fetchRoleInforById(Integer roleId){
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo==null){
			return "不存在角色Id:"+roleId;
		}
		String str2=rolePo.fetchRoleInfor();
		return str2;
	}
	
	/**
	 * 通过角色Id获取iuid
	 * @param roleId
	 * @return
	 */
	public String fetchIuidById(Integer roleId){
		RolePo rolePo = RolePo.findEntity(roleId);
		if(rolePo==null){
			return StringUtil.EMPTY;
		}
		return rolePo.getUserIuid();
	}
	/**
	 * 通过角色名获取iuid
	 * @param roleId
	 * @return
	 */
	public String fetchIuidByName(String name){
		RolePo rolePo = roleService.findRoleByName(name);
		if(rolePo==null){
			return StringUtil.EMPTY;
		}
		return rolePo.getUserIuid();
	}
	
	
	public String recover(String name){
		RolePo rolePo = roleService.findRoleByName(name);
		if(rolePo==null){
			return name+"不存在角色";
		}
		
		String finalMsg="";
		if(rolePo.getAbandomState()==null){
			return name+"未删除的角色";
		}
		if(UserPo.findEntity(rolePo.getUserId()).listRoleBriefVo.size()>=4){
			return name+"角色栏满了,无法恢复";
		}
		rolePo.setAbandomState(null);
		
		RoleBriefVo roleBriefVo = new RoleBriefVo();
		roleBriefVo.syncFromRolePo(rolePo);
		UserPo.findEntity(rolePo.getUserId()).listRoleBriefVo.add(roleBriefVo);
		return name+"角色恢复成功";
	}
	
	public String taskFinish(String type, String value) {
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null){
			return "不存在角色";
		}
		for (IdNumberVo2 idNumberVo2 : rolePo.listRoleTasks) {
			TaskPo taskPo= TaskPo.findEntity(idNumberVo2.getInt1());
			if(taskPo.getTaskType().intValue()==TaskType.TASK_TYPE_MAIN){
				idNumberVo2.setInt2(999);
				rolePo.sendUpdateTaskProgress(idNumberVo2.getInt1(), idNumberVo2.getInt2());
			}
		}
		return "角色主线完成成功";
	}
	
	public String taskSubFinish(String type,String taskName, String value){
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null){
			return "不存在角色";
		}
		boolean matched=false;
		for (IdNumberVo2 idNumberVo2 : rolePo.listRoleTasks) {
			TaskPo taskPo= TaskPo.findEntity(idNumberVo2.getInt1()); 
			if(taskPo.getName().equals(taskName)){
				if(taskPo.getTaskType().intValue()==TaskType.TASK_TYPE_SUB){
					matched=true;
					idNumberVo2.setInt2(999);
					rolePo.sendUpdateTaskProgress(idNumberVo2.getInt1(), idNumberVo2.getInt2());
				}
			}
		}
		if(matched){
			return "角色支线完成成功:"+taskName;
		}
		else{
			return "角色支线不存在:"+taskName;
		}

	}
	public Integer fetchRoleActionNum(String value, Long startTime,Long endTime,Integer logCat,Integer resType,Integer resChange,Integer actionType,Integer requireExport, String type) {
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return 0;
		if (endTime <= startTime)
			return 0;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1=" + resType;
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else 
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type=" + actionType + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		int result = 0;
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(i) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		for (String tableName :tableNames) {
			PrintUtil.print("fetchRoleActionNum dateTableName:"+tableName);
			String sql = "SELECT COUNT(id) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while(rs.next())
				result += rs.getInt(1);
		}
		return result;
	}
	public Object fetchRoleAction(String value, Long startTime,Long endTime,Integer logCat,Integer resType,Integer resChange,Integer actionType,Integer requireExport, String type, Integer currentPage, Integer num, Integer timezoneOffset){
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return "不存在角色";
		List<LogVo> logs = new ArrayList<LogVo>();
		int min = num * (currentPage - 1);
		int max = min + num;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1=" + resType;
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type=" + actionType + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(i) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		for (String tableName :tableNames) {
			PrintUtil.print("fetchRoleAction dateTableName:"+tableName);
			String sql = "SELECT COUNT(1) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			int count = BaseDAO.instance().jdbcTemplate.queryForInt(sql);
			if (count <= min) {
				min -= count;
				max -= count;
			} else if (count < max) {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (count - min);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				max -= count;
				min = 0;
			} else {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (max - min);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				break;
			}
		}
		String finalMsg="";
		List<String> list = new ArrayList<String>();
		finalMsg+="<hr>";
		int realOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)+timezoneOffset*60*1000;
		for(int i=0;i<logs.size();i++){
			String s = "";
			LogVo logVo=logs.get(i);
			if(logVo.logType<311){
				finalMsg+=DateUtil.getFormatDateBytimestamp(logVo.logTime)+" ";
				s+=DateUtil.getFormatDateBytimestamp(logVo.logTime-realOffset)+" ";
				finalMsg+=logVo.sourceTxt+": ";
				s+=logVo.sourceTxt+": ";
				//1=绑金 2=金币 3=绑钻 4=钻石 5=道具 6=技能点 7=公会荣誉 8=声望 9=成就点 10=宠物魔魂 11=经验 12=宠物 13=pk点 
				String[] resouceTypes={GlobalCache.fetchLanguageMap("key2508"),GlobalCache.fetchLanguageMap("key2509"),GlobalCache.fetchLanguageMap("key2510"),GlobalCache.fetchLanguageMap("key2511"),GlobalCache.fetchLanguageMap("key2512"),GlobalCache.fetchLanguageMap("key2513"),GlobalCache.fetchLanguageMap("key2514"),GlobalCache.fetchLanguageMap("key2515"),GlobalCache.fetchLanguageMap("key2516"),GlobalCache.fetchLanguageMap("key2517"),GlobalCache.fetchLanguageMap("key2518"),GlobalCache.fetchLanguageMap("key2519"),GlobalCache.fetchLanguageMap("key2520")};
				finalMsg+="<font color=#FF00FF>";
	
				if(logVo.logType==1){
					if(logVo.logPar1==5){
						ItemPo itemPo=ItemPo.findEntity(logVo.logPar2);
						if(itemPo!=null){
							finalMsg+=itemPo.getName()+"*"+logVo.logPar3;
							s+=itemPo.getName()+"*"+logVo.logPar3;
						}else{
							for(int j=0;j<XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.size();j++){
								ReplaceItem replaceItem = XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.get(j);
								List<Integer> ids = StringUtil.getListByStr(replaceItem.replaceItems,"|");
								if(logVo.logPar2.equals(replaceItem.itemId)){
									ItemPo itemPo2=ItemPo.findEntity(ids.get(rolePo.getCareer()-1));
									if(itemPo2!=null){
										finalMsg+=itemPo2.getName()+"*"+logVo.logPar3;
										s+=itemPo2.getName()+"*"+logVo.logPar3;
										break;
									}
								}
							}
						}
					}else if(logVo.logPar1==12){
						PetPo petPo=PetPo.findEntity(logVo.logPar2);
						if(petPo!=null){
							finalMsg+=petPo.getName()+"*"+logVo.logPar3;
							s+=petPo.getName()+"*"+logVo.logPar3;
						}
	
					}else{
						finalMsg+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
						s+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
					}
				}else if(logVo.logType==201){
					finalMsg+="to"+(logVo.logPar1+1)+"level";
					s+="to"+(logVo.logPar1+1)+"level";
				}else if(logVo.logType==202){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
				}else if(logVo.logType==203){
					finalMsg+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
				}else if(logVo.logType==204){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
				}else if(logVo.logType==205){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
				}else if(logVo.logType==206){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==207){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==208){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==209){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
				}else if(logVo.logType==210){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==211){
					finalMsg+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==212){
					finalMsg+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==234){
					finalMsg+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}
				else if(logVo.logType==216){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==217){
					finalMsg+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
					s+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
				}else if(logVo.logType==218){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
				}else if(logVo.logType==219){
					if(logVo.sourceTxt.equals(GlobalCache.fetchLanguageMap("key2348"))){
						//ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
						finalMsg+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
						s+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
					}else{
						finalMsg+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
						s+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
					}
				}else if(logVo.logType==220){
					finalMsg+="killerRoleId："+logVo.logPar1;
					s+="killerRoleId："+logVo.logPar1;
				}else if(logVo.logType==221){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==222){
					finalMsg+="fromRoleId："+logVo.logPar1;
					s+="fromRoleId："+logVo.logPar1;
				}else if(logVo.logType==223){
					finalMsg+="fromRoleId：："+logVo.logPar1;
					s+="fromRoleId：："+logVo.logPar1;
				}else if(logVo.logType==224){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==225){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==226){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==228){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==236||logVo.logType==237){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}else if(logVo.logType==235){
					ScenePo scenePo=ScenePo.findEntity(logVo.logPar1);
					if(scenePo!=null){
						finalMsg+="："+scenePo.getName();
						s+="："+scenePo.getName();
					}
				}else if(logVo.logType==238){
					finalMsg+="："+logVo.logPar1;
					s+="："+logVo.logPar1;
				}else if(logVo.logType==239){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}
				
				else if(logVo.logType==307){
					finalMsg+="num："+(logVo.logPar3/100);
					s+="num："+(logVo.logPar3/100);
				}
				finalMsg+="</font><br>";
				list.add(s);
				finalMsg+="</table><br>";
			}
		}
		if (requireExport == 1) {
			return list;
		}
		return finalMsg;
	}
	
	/**
	 * 
	 * 方法功能:更新新手步骤
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param step
	 */
	public void updateNewbieStep(Integer step){
		CheckUtil.checkIsNull(step);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.setNewbieStepGroup(step);
		rolePo.sendUpdateNewbieStepGroup();
	}
	
	/**
	 * 取前三名战力最高的
	 * @return
	 */
	public Object fetchFirstThreeRanks(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.fetchFirstThreeRanks";		
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		RankVo maxWarrior =null;
		RankVo maxMage =null;
		RankVo maxRanger =null;
		List<RankVo> allranks = GlobalCache.rankMap.get(String.valueOf(RankType.BATTLE_RANK_TYPE_POWER));
		List<RankVo> players = new ArrayList<RankVo>();
		if(allranks!=null){
			int maxSize = Math.min(allranks.size(), 50);
			for(int i=1;i<=maxSize;i++){
				if(maxWarrior!=null && maxMage!=null && maxRanger!=null){
					break;
				}
				if(maxWarrior==null && allranks.get(i-1).roleCareer==RoleType.CAREER_WARRIOR){
					maxWarrior=allranks.get(i-1);
				}
				if(maxMage==null && allranks.get(i-1).roleCareer==RoleType.CAREER_MAGE){
					maxMage=allranks.get(i-1);
				}
				if(maxRanger==null && allranks.get(i-1).roleCareer==RoleType.CAREER_RANGER){
					maxRanger=allranks.get(i-1);
				}
			}
		}
		RankVo rankVo = new RankVo();
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
		SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
		GuildPo guildPo = GuildPo.findEntity(siegeBidVo.ownerGuidId);
		if(guildPo != null){
			RolePo leaderRole = RolePo.findEntity(guildPo.getLeaderRoleId());
			if(leaderRole != null){
				rankVo.roleName = leaderRole.getName();
				rankVo.roleId=leaderRole.getId();
				rankVo.roleCareer = leaderRole.getCareer();
				CommonAvatarVo commonAvatarVo = CommonAvatarVo.build(
						leaderRole.getEquipWeaponId(), leaderRole.getEquipArmorId(),
						leaderRole.getFashion(),
						leaderRole.getCareer(), leaderRole.getWingWasHidden(), leaderRole.getWingStar(),leaderRole.hiddenFashions);
				rankVo.weaponAvatar = commonAvatarVo.weaponAvatar;
				rankVo.armorAvatar = commonAvatarVo.modelAvatar;
				rankVo.wingAvatar = commonAvatarVo.wingAvatar;
				rankVo.rolePower = leaderRole.getBattlePower();
			}else{
				SiegeBidVo.instance.init3();
			}
		}else{
			SiegeBidVo.instance.init3();
		}
		if(maxWarrior == null){
			maxWarrior = GlobalCache.fetchRankVoByRobotCareer(RoleType.CAREER_WARRIOR);
		}
		if(maxRanger == null){
			maxRanger = GlobalCache.fetchRankVoByRobotCareer(RoleType.CAREER_RANGER);
		}
		if(maxMage == null){
			maxMage=GlobalCache.fetchRankVoByRobotCareer(RoleType.CAREER_MAGE);
		}
		GlobalPo globalPoFTRAC = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyFirstThreeRankAndCastellan);
		players.add(maxWarrior);
		players.add(maxMage);
		players.add(maxRanger);
		if(siegeBidVo.ownerGuidId.intValue() != 0){
			players.add(rankVo);
			IdNumberVo2 idNumberVo2 = globalPoFTRAC.fetchFirstThreeRankAndCastellanByCareer(RoleType.CAREER_CASTELLAN);
			idNumberVo2.setInt3(rankVo.roleId);
			rankVo.roleId = idNumberVo2.getInt1();
		}

		for(RankVo vo : players){
			RolePo po = RolePo.findEntity(vo.roleId);
			if(po != null){
				CommonAvatarVo caVo = CommonAvatarVo.build(
						po.getEquipWeaponId(), po.getEquipArmorId(),
						po.getFashion(),
						po.getCareer(), po.getWingWasHidden(), po.getWingStar(),po.hiddenFashions);
				vo.weaponAvatar = caVo.weaponAvatar;
				vo.armorAvatar = caVo.modelAvatar;
				vo.wingAvatar = caVo.wingAvatar;
				
			}
		}

		SessionUtil.addDataArray(players);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 升级军衔等级
	 * @param militaryRankId 需要升级的军衔Id
	 * @return
	 */
	public Object upgradeMilitaryRank(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.upgradeMilitaryRank";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.upgradeMilitaryRank();
		SessionUtil.addDataArray(rolePo.getCurrentMilitaryRankId());
		SessionUtil.addDataArray(rolePo.getPrestige());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 购买各种次数
	 * @param timesType
	 * @return
	 */
	public Object buyPlayTimesByType(Integer timesType){
//		System.out.println("buyPlayTimesByType() timesType="+timesType);
		CheckUtil.checkIsNull(timesType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.buyPlayTimesByType";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.buyPlayTimesByType(timesType);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.listBuyPlayTimes);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 领取宝物
	 * @param treasureId
	 * @return
	 */
	public Object takeTreasureAward(Integer treasureId){
		CheckUtil.checkIsNull(treasureId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.takeTreasureAward";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		rolePo.takeTreasureAwardRemoting( treasureId);
		SessionUtil.addDataArray(treasureId);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 礼品码
	 * @param code
	 * @param serverId
	 * @param roleId
	 * @return
	 */
	public Object exchangeGiftCode(String code,Integer serverId,Integer roleId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		List<IdNumberVo> list=new ArrayList<IdNumberVo>();
		Object[] objs =null;
		try {
			objs = (Object[]) uniqCallTemplate.sendTicketSingleResult(BaseStormSystemType.GM_RMI_HOST, BaseStormSystemType.GM_RMI_PORT, "GiftCodeRemoting.exchangeGiftCode", new Object[]{code,serverId,roleId},9000l);
		}
		catch (Exception e) {
			ExceptionUtil.processException(e);
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2499"));
		}			
		Object[] returnVals =(Object[]) objs[0];
		if(returnVals==null || returnVals.length!=2){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2500"));
		}
		list=(List<IdNumberVo>) returnVals[1];
		int status =(Integer) returnVals[0];
		if(status!=1){
			if(status==2){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2500"));
			}
			if(status==3){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2501"));
			}	
			if(status==4){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2502"));
			}
			if(status==5){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2503"));
			}
			if(status==6){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2504"));
			}	
		}
		rolePo.checkItemPackFull(list.size());
		
		rolePo.addItem(list, 1,GlobalCache.fetchLanguageMap("key2609"));
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 修改玩家名称
	 * @param name
	 * @return
	 */
	public Object changeRoleName(String name){
		CheckUtil.checkIsNull(name);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_RoleRemoting.changeRoleName";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		if(roleService.existRoleName(name)){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2330"));
		}
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		if((name == null || CheckUtil.checkRolePoNameWasIllegal(name,true)) && (!"ko".equals(String.valueOf(gp.valueObj)))){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2331"));
		}
		name=CheckUtil.checkIllegelName(name);
		CheckUtil.checkValidString(name, 12);
		CheckUtil.checkContianFiltedWord(name, true,RoleType.ROLE_FILTERS);

		rolePo.changeRoleName(name);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.getName());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 记录最后离线时间(用于切账号，切换角色)
	 * @return
	 */
	public Object lastOutOnline(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		if(rolePo != null){
//			rolePo.setLastLoginTime(System.currentTimeMillis());
//			System.out.println(rolePo.getName()+" lastOutOnline()");
		}
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 管理服务器修改角色属性
	 * @param type
	 * @param value
	 * @return
	 */
	public Object manageServerUpdateRolePoAttributeByType(Integer type, Integer value, String roleName, Integer roleId){
//		System.out.println("manageServerUpdateRolePoAttributeByType() type="+type+"; value="+value+"; roleName="+roleName+"; roleId="+roleId);
		boolean bool = false;
		if((roleId == null && roleName == null) || type==null || value==null || value.intValue() <0){
			return bool;
		}
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(roleName, roleId);
		if(rolePo == null){
			return bool;
		}
		
		int oldValue = 0;
		if(RoleType.RESOURCE_GOLD == type.intValue()){
			oldValue=rolePo.getGold();
			rolePo.setGold(value);
			rolePo.sendUpdateTreasure(false);
			bool = true;
		}else if(RoleType.RESOURCE_BIND_GOLD == type.intValue()){
			oldValue=rolePo.getBindGold();
			rolePo.setBindGold(value);
			rolePo.sendUpdateTreasure(false);
			bool = true;
		}else if(RoleType.RESOURCE_DIAMOND == type.intValue()){
			UserPo userPo = UserPo.findEntity(rolePo.getUserId());
			oldValue=userPo.getDiamond();
			userPo.setDiamond(value);
			rolePo.setDiamond(value);
			rolePo.sendUpdateTreasure(false);
			bool = true;
		}else if(RoleType.RESOURCE_BIND_DIAMOND == type.intValue()){
			oldValue=rolePo.getBindDiamond();
			rolePo.setBindDiamond(value);
			rolePo.sendUpdateTreasure(false);
			bool = true;
		}else if(RoleType.RESOURCE_VIP_LV == type.intValue()){
			UserPo userPo = UserPo.findEntity(rolePo.getUserId());
			oldValue=userPo.getVipLv();
			userPo.setVipLv(value);
			rolePo.setVipLv(value);
			rolePo.sendUpdateRechargeInfo();
			bool = true;
		}else if(RoleType.RESOURCE_VIP_EXP == type.intValue()){
			UserPo userPo = UserPo.findEntity(rolePo.getUserId());
			oldValue=userPo.getCumulativeRechargeNum();
			userPo.setCumulativeRechargeNum(value);
			rolePo.listRechargeInfo.get(0).cumulativeRechargeNum=value;
			rolePo.checkVipLv();
			rolePo.sendUpdateRechargeInfo();
			bool = true;
		}
		LogUtil.writeLog(rolePo, 401, type, oldValue, value,GlobalCache.fetchLanguageMap("key2590"),"");
		return bool;
	}
	
	/**
	 * 管理服务器修改角色属性
	 * @param type
	 * @param value
	 * @return
	 */
	public Object resetRolePo(String roleName, Integer roleId){
		if(roleId == null && roleName == null){
			return false;
		}
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(roleName, roleId);
		if(rolePo == null){
			return false;
		}
		rolePo.setX(389500);
		rolePo.setY(221589);
		rolePo.setZ(958451);
		rolePo.setRoomId(20101006);
		rolePo.setStaticRoomId(20101006);
		rolePo.sendUpdateRole();
		return true;
	}
	
	/**
	 * 管理服务器修改角色属性
	 * @param type
	 * @param value
	 * @return
	 */
	public Object modifyName(String value, String roleName, Integer roleId) {

		boolean bool = false;
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(roleName, roleId);
		if(rolePo == null){
			return bool;
		}
		value=CheckUtil.checkIllegelName(value);
		CheckUtil.checkValidString(value, 12);
		CheckUtil.checkContianFiltedWord(value, true,RoleType.ROLE_FILTERS);
		rolePo.backChangeRoleName(value);
		baseDAO.instance().syncToDB(rolePo);
		return true;
	}
	/**
	 * 删除某个道具
	 * @param deleteType（删除类型（1道具，2装备，3宠物））
	 * @param itemId（道具ID）
	 * @param bindOrNot（是否绑定）
	 * @param playerName（角色名）
	 * @param playerId（角色ID）
	 * @return 删除成功信息
	 */
	public Object deleteOneItem(Integer deleteType, Integer itemId, Integer bindOrNot, String playerName, Integer playerId,Integer num) {
//		System.out.println("deleteOneItem() deleteType="+deleteType+"; itemId="+itemId+"; bindOrNot="+bindOrNot+"; playerName="+playerName+"; playerId="+playerId+"; num="+num);
		boolean bool = false;
		if(deleteType == null || itemId == null || bindOrNot == null){
			return bool;
		}
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(playerName, playerId);
		if(rolePo == null){
			return bool;
		}
		if(deleteType.intValue() == 1){
			if(!rolePo.checkHasItembyBindOrNot(itemId, num, bindOrNot)){
				return bool;
			}
			rolePo.fetchMainPackItemRemoveKey(itemId, num, bindOrNot,"不会调用的方法");
			rolePo.sendUpdateMainPack(false);
			bool=true;
		}else if(deleteType.intValue() == 2){
			if(num.intValue() != 1){
				return bool;
			}
			num = rolePo.fetchMainPackItemRemoveKey(itemId, num, bindOrNot,"不会调用的方法");
			
			if(num!=0){
				if(rolePo.equipWeapon!= null && rolePo.equipWeapon.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_WEAPON, true);
				}else if(rolePo.equipArmor!=null && rolePo.equipArmor.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_ARMOR, true);
				}else if(rolePo.equipRing!=null && rolePo.equipRing.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_RING, true);
				}else if(rolePo.equipBracer!=null && rolePo.equipBracer.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_BRACER, true);
				}else if(rolePo.equipNecklace!=null && rolePo.equipNecklace.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_NECKLACE, true);
				}else if(rolePo.equipHelmet!=null && rolePo.equipHelmet.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_HELMET, true);
				}else if(rolePo.equipShoe!=null && rolePo.equipShoe.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_SHOE, true);
				}else if(rolePo.equipBracelet!=null && rolePo.equipBracelet.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_BRACELET, true);
				}else if(rolePo.equipBelt!=null && rolePo.equipBelt.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_BELT, true);
				}else if(rolePo.equipPants!=null && rolePo.equipPants.getId().intValue()==itemId){
					rolePo.unEquipBySlot(ItemType.ITEM_CATEGORY_PANTS, true);
				}
				num = rolePo.fetchMainPackItemRemoveKey(itemId, num, bindOrNot,"不会调用的方法");
				if(num != 0){
					return bool;
				}else{
					bool=true;
				}
			}else{
				bool=true;
			}
			rolePo.sendUpdateMainPack(false);
		}else if(deleteType.intValue() == 3){
			RpetPo removeRpetPo = null;
			for(RpetPo rpetPo : rolePo.listRpets){
				if(itemId.intValue()==rpetPo.getId().intValue()){
					removeRpetPo =rpetPo;
					break;
				}
			}
			if(removeRpetPo == null){
				return bool;
			}
			if(removeRpetPo.getId().intValue() == rolePo.getRpetFighterId().intValue()){
				rolePo.setRpetFighterId(0);
				rolePo.sendUpdatePetInfor();
			}
			rolePo.listRpets.remove(removeRpetPo);
			BaseDAO.instance().remove(removeRpetPo);
			rolePo.sendUpdatePetList();
			rolePo.sendUpdateMainPack(false);
			bool=true;
		}
		return bool;
	}
	
	/**
	 * 删除指定角色某个道具
	 * @param deleteType（删除类型（1道具，2装备，3宠物））
	 * @param itemId（道具ID/装备唯一ID/宠物唯一ID）
	 * @param bindOrNot（是否绑定）
	 * @param playerName（角色名）
	 * @param playerId（角色ID）
	 * @param num 删除数量
	 * @return 删除成功信息（true:成功,false:失败）
	 */
	public Object deleteRoleItem(Integer deleteType, Integer itemId, Integer bindOrNot, String playerName, Integer playerId,Integer num) {
//		System.out.println("deleteOneItem() deleteType="+deleteType+"; itemId="+itemId+"; bindOrNot="+bindOrNot+"; playerName="+playerName+"; playerId="+playerId+"; num="+num);
		//[是否删除成功,提示消息(0:无,1:角色为空,2:删除类型为空,3:道具ID为空,4:是否绑定为空,5:删除数量为空,6:角色不存在,7:道具数量不够,8:装备不存在,9:宠物不存在)]
		Object [] objs = new Object[2];
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(playerName, playerId);
		if(rolePo == null){
			objs[0] = false;
			objs[1] = 6;
			return objs;
		}
		if(deleteType.intValue() == 1){
			if(!rolePo.checkHasItembyBindOrNot(itemId, num, bindOrNot)){
				objs[0] = false;
				objs[1] = 7;
				return objs;
			}
			int removedCount = rolePo.removePackItem(itemId, num, bindOrNot,"不会调用的方法");
			if(num - removedCount>0) rolePo.removeWarehouseItem(itemId, num - removedCount, bindOrNot);
			//rolePo.sendUpdateMainPack(false);
			rolePo.singleRole("ItemRemoting.takeOutByWarehouse",new Object[] { rolePo.mainPackItemVosMap,rolePo.warehousePackItemVosMap }, false);
			objs[0] = true;
			objs[1] = 0;
		}else if(deleteType.intValue() == 2){
			int equipSlot = 0;
			if(rolePo.equipWeapon!= null && rolePo.equipWeapon.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_WEAPON;
			}else if(rolePo.equipArmor!=null && rolePo.equipArmor.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_ARMOR;
			}else if(rolePo.equipRing!=null && rolePo.equipRing.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_RING;
			}else if(rolePo.equipBracer!=null && rolePo.equipBracer.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_BRACER;
			}else if(rolePo.equipNecklace!=null && rolePo.equipNecklace.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_NECKLACE;
			}else if(rolePo.equipHelmet!=null && rolePo.equipHelmet.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_HELMET;
			}else if(rolePo.equipShoe!=null && rolePo.equipShoe.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_SHOE;
			}else if(rolePo.equipBracelet!=null && rolePo.equipBracelet.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_BRACELET;
			}else if(rolePo.equipBelt!=null && rolePo.equipBelt.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_BELT;
			}else if(rolePo.equipPants!=null && rolePo.equipPants.getId().intValue()==itemId){
				equipSlot = ItemType.ITEM_CATEGORY_PANTS;
			}
			rolePo.unEquipBySlot(equipSlot, false);
			if(equipSlot!=0){
				if(ItemType.ITEM_CATEGORY_WEAPON==equipSlot || ItemType.ITEM_CATEGORY_ARMOR==equipSlot){
					rolePo.sendAvatars();
				}
				rolePo.calculateBat(1);
				rolePo.sendUpdateRoleBatProps(false);	
				rolePo.sendUpdateRoleEquipSlot(equipSlot);
			}
			
			boolean packHasEquip = rolePo.checkPackHasEquip(itemId);
			boolean warehouseHasEquip = rolePo.checkWarehouseHasEquip(itemId);
			if(!packHasEquip && !warehouseHasEquip && equipSlot==0){
				objs[0] = false;
				objs[1] = 8;
				return objs;
			}
			if(packHasEquip) rolePo.removePackEquip(itemId,"不会调用的方法");
			if(warehouseHasEquip) rolePo.removeWarehouseEquip(itemId);
			rolePo.singleRole("ItemRemoting.takeOutByWarehouse",new Object[] {rolePo.mainPackItemVosMap,rolePo.warehousePackItemVosMap}, false);
			objs[0] = true;
			objs[1] = 0;
		}else if(deleteType.intValue() == 3){
			RpetPo removeRpetPo = null;
			for(RpetPo rpetPo : rolePo.listRpets){
				if(itemId.intValue()==rpetPo.getId().intValue()){
					removeRpetPo =rpetPo;
					break;
				}
			}
			if(removeRpetPo == null){
				objs[0] = false;
				objs[1] = 9;
				return objs;
			}
			if(removeRpetPo.getId().intValue() == rolePo.getRpetFighterId().intValue()){
				rolePo.setRpetFighterId(0);
				rolePo.sendUpdatePetInfor();
			}
			rolePo.listRpets.remove(removeRpetPo);
			BaseDAO.instance().remove(removeRpetPo);
			rolePo.sendUpdatePetList();
			rolePo.sendUpdateMainPack(false);
			objs[0] = true;
			objs[1] = 0;
		}
		return objs;
	}
	
	public Integer fetchRoleActionNumWithKeyword(String value, Long startTime,Long endTime,Integer logCat,Integer resType,Integer resChange,Integer actionType,Integer requireExport, String type, String keyword, Integer itemId) {
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return 0;
		if (endTime <= startTime)
			return 0;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1=" + resType;
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else 
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type=" + actionType + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		if(StringUtil.isNotEmpty(keyword)){
			limitSql += " and source_txt like'%"+keyword+"%'";
		}
		if(itemId != null&&itemId!=0){
			limitSql += " and log_par2="+itemId;
		}
		int result = 0;
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(i) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		for (String tableName : tableNames) {
			String sql = "SELECT COUNT(id) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			PrintUtil.print(sql);
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while(rs.next())
				result += rs.getInt(1);
		}
		return result;
	}
	public Object fetchRoleActionWithKeyword(String value, Long startTime,Long endTime,Integer logCat,Integer resType,Integer resChange,Integer actionType,Integer requireExport, String type, Integer currentPage, Integer num, Integer timezoneOffset, String keyword, Integer itemId){
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return "不存在角色";
		List<LogVo> logs = new ArrayList<LogVo>();
		int min = num * (currentPage - 1);
		int max = min + num;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1=" + resType;
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type=" + actionType + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		if(StringUtil.isNotEmpty(keyword)){
			limitSql += " and source_txt like'%"+keyword+"%'";
		}
		if(itemId != null && itemId!=0){
			limitSql += " and log_par2="+itemId;
		}
		
		
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(start.getTime()) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		
		
		for (String tableName:tableNames) {
			String sql = "SELECT COUNT(1) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			PrintUtil.print(sql);
			int count = BaseDAO.instance().jdbcTemplate.queryForInt(sql);
			if (count <= min) {
				min -= count;
				max -= count;
			} else if (count < max) {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (count - min);
				PrintUtil.print(sql2);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				max -= count;
				min = 0;
			} else {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (max - min);
				PrintUtil.print(sql2);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				break;
			}
		}
		String finalMsg="";
		List<String> list = new ArrayList<String>();
		finalMsg+="<hr>";
		int realOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)+timezoneOffset*60*1000;
		for(int i=0;i<logs.size();i++){
			String s = "";
			LogVo logVo=logs.get(i);
			if(logVo.logType<311){
				finalMsg+=DateUtil.getFormatDateBytimestamp(logVo.logTime)+" ";
				s+=DateUtil.getFormatDateBytimestamp(logVo.logTime-realOffset)+" ";
				finalMsg+=logVo.sourceTxt+": ";
				s+=logVo.sourceTxt+": ";
				//1=绑金 2=金币 3=绑钻 4=钻石 5=道具 6=技能点 7=公会荣誉 8=声望 9=成就点 10=宠物魔魂 11=经验 12=宠物 13=pk点 
				String[] resouceTypes={GlobalCache.fetchLanguageMap("key2508"),GlobalCache.fetchLanguageMap("key2509"),GlobalCache.fetchLanguageMap("key2510"),GlobalCache.fetchLanguageMap("key2511"),GlobalCache.fetchLanguageMap("key2512"),GlobalCache.fetchLanguageMap("key2513"),GlobalCache.fetchLanguageMap("key2514"),GlobalCache.fetchLanguageMap("key2515"),GlobalCache.fetchLanguageMap("key2516"),GlobalCache.fetchLanguageMap("key2517"),GlobalCache.fetchLanguageMap("key2518"),GlobalCache.fetchLanguageMap("key2519"),GlobalCache.fetchLanguageMap("key2520")};
				finalMsg+="<font color=#FF00FF>";
	
				if(logVo.logType==1){
					if(logVo.logPar1==5){
						ItemPo itemPo=ItemPo.findEntity(logVo.logPar2);
						if(itemPo!=null){
							finalMsg+=itemPo.getName()+"*"+logVo.logPar3;
							s+=itemPo.getName()+"*"+logVo.logPar3;
						}else{
							for(int j=0;j<XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.size();j++){
								ReplaceItem replaceItem = XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.get(j);
								List<Integer> ids = StringUtil.getListByStr(replaceItem.replaceItems,"|");
								if(logVo.logPar2.equals(replaceItem.itemId)){
									ItemPo itemPo2=ItemPo.findEntity(ids.get(rolePo.getCareer()-1));
									if(itemPo2!=null){
										finalMsg+=itemPo2.getName()+"*"+logVo.logPar3;
										s+=itemPo2.getName()+"*"+logVo.logPar3;
										break;
									}
								}
							}
						}
					}else if(logVo.logPar1==12){
						PetPo petPo=PetPo.findEntity(logVo.logPar2);
						if(petPo!=null){
							finalMsg+=petPo.getName()+"*"+logVo.logPar3;
							s+=petPo.getName()+"*"+logVo.logPar3;
						}
	
					}else{
						finalMsg+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
						s+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
					}
				}else if(logVo.logType==201){
					finalMsg+="to"+(logVo.logPar1+1)+"level";
					s+="to"+(logVo.logPar1+1)+"level";
				}else if(logVo.logType==202){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
				}else if(logVo.logType==203){
					finalMsg+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
				}else if(logVo.logType==204){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
				}else if(logVo.logType==205){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
				}else if(logVo.logType==206){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==207){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==208){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==209){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
				}else if(logVo.logType==210){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==211){
					finalMsg+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==212){
					finalMsg+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==234){
					finalMsg+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}
				else if(logVo.logType==216){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==217){
					finalMsg+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
					s+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
				}else if(logVo.logType==218){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
				}else if(logVo.logType==219){
					if(logVo.sourceTxt.equals(GlobalCache.fetchLanguageMap("key2348"))){
						//ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
						finalMsg+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
						s+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
					}else{
						finalMsg+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
						s+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
					}
				}else if(logVo.logType==220){
					finalMsg+="killerRoleId："+logVo.logPar1;
					s+="killerRoleId："+logVo.logPar1;
				}else if(logVo.logType==221){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==222){
					finalMsg+="fromRoleId："+logVo.logPar1;
					s+="fromRoleId："+logVo.logPar1;
				}else if(logVo.logType==223){
					finalMsg+="fromRoleId：："+logVo.logPar1;
					s+="fromRoleId：："+logVo.logPar1;
				}else if(logVo.logType==224){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==225){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==226){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==228){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==236||logVo.logType==237){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}else if(logVo.logType==235){
					ScenePo scenePo=ScenePo.findEntity(logVo.logPar1);
					if(scenePo!=null){
						finalMsg+="："+scenePo.getName();
						s+="："+scenePo.getName();
					}
				}else if(logVo.logType==238){
					finalMsg+="："+logVo.logPar1;
					s+="："+logVo.logPar1;
				}else if(logVo.logType==239){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}
				
				else if(logVo.logType==307){
					finalMsg+="num："+(logVo.logPar3/100);
					s+="num："+(logVo.logPar3/100);
				}
				finalMsg+="</font><br>";
				list.add(s);
				finalMsg+="</table><br>";
			}
		}
		if (requireExport == 1) {
			return list;
		}
		return finalMsg;
	}
	public Object fetchRole(Integer roleId){
		RolePo rolePo = RolePo.findEntity(roleId);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtil.isEmpty(rolePo.getUserIuid())){
			resultMap.put("iuid", UserPo.findEntity(rolePo.getUserId()).getToken());
		}else{
			resultMap.put("iuid", rolePo.getUserIuid());
		}
		resultMap.put("lv", rolePo.getLv());
		return resultMap;
	}
	
	public Integer fetchRoleActionNumWithKeywordAndResTypes(String value, Long startTime,Long endTime,Integer logCat,String resTypes,Integer resChange,String actionType,Integer requireExport, String type, String keyword, Integer itemId) {
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return 0;
		if (endTime <= startTime)
			return 0;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1 in (" + resTypes + ")";
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else 
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type in (" + actionType + ")" + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		if(StringUtil.isNotEmpty(keyword)){
			limitSql += " and source_txt like'%"+keyword+"%'";
		}
		if(itemId != null&&itemId!=0){
			limitSql += " and log_par2="+itemId;
		}
		int result = 0;
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(i) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		for (String tableName :tableNames) {
			String sql = "SELECT COUNT(id) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			PrintUtil.print(sql);
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while(rs.next())
				result += rs.getInt(1);
		}
		return result;
	}
	public Object fetchRoleActionWithKeywordAndResTypes(String value, Long startTime,Long endTime,Integer logCat,String resTypes,Integer resChange,String actionType,Integer requireExport, String type, Integer currentPage, Integer num, Integer timezoneOffset, String keyword, Integer itemId){
		RolePo rolePo = null;
		if ("id".equals(type))
			rolePo = (RolePo) RoleDAO.instance().getEntityPo(Integer.valueOf(value), RolePo.class);
		else
			rolePo = roleService.findRoleByName(value);
		if(rolePo==null)
			return "不存在角色";
		List<LogVo> logs = new ArrayList<LogVo>();
		int min = num * (currentPage - 1);
		int max = min + num;
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(startTime);
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(endTime);
		DateFormat format = new SimpleDateFormat("yyyy_MM_dd");
		String limitSql="";
		if (logCat == 0) {//全部（资源/行为）
			limitSql += " and log_type > 0 and log_type < 311 and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		} else if (logCat == 1) {//资源
			limitSql += " and log_type=1 and log_par1 in(" + resTypes + ")";
			if (resChange == 1)
				limitSql += " and log_par3>=0";
			else if (resChange == 2)
				limitSql += " and log_par3<0";
			else
				limitSql += " and (log_par3<0 or log_par3 >= 0)";
		} else if (logCat == 2) {//行为
			limitSql += " and log_type in (" + actionType + ")" + " and log_par1 >= 0 and (log_par3 >= 0 or log_par3 < 0)";
		}
		if(StringUtil.isNotEmpty(keyword)){
			limitSql += " and source_txt like'%"+keyword+"%'";
		}
		if(itemId != null && itemId!=0){
			limitSql += " and log_par2="+itemId;
		}
		int k=0;
		String dateTableNames =StringUtil.EMPTY;
		for(long i=DateUtil.getInitialDate(startTime); i<=endTime; i+=1000*60*60*24){
			if(k!=0)
				dateTableNames += ", ";
			dateTableNames += "'role_log_" + format.format(i) +"'";
			k++;
			if(k>1000) break;
		}
		List <String> tableNames = DbUtil.getTableNameByTableAndDbName(dateTableNames, BaseStormSystemType.LOG_DB_NAME);
		for (String tableName :tableNames) {
			PrintUtil.print("fetchRoleActionWithKeywordAndResTypes dateTableName:"+tableName);
			String sql = "SELECT COUNT(1) FROM "
					+ BaseStormSystemType.LOG_DB_NAME + "." + tableName
					+ " WHERE role_id = " + rolePo.getId()
					+ " AND log_time>=" + startTime 
					+ " and log_time<=" + endTime + limitSql;
			PrintUtil.print(sql);
			int count = BaseDAO.instance().jdbcTemplate.queryForInt(sql);
			if (count <= min) {
				min -= count;
				max -= count;
			} else if (count < max) {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (count - min);
				PrintUtil.print(sql2);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				max -= count;
				min = 0;
			} else {
				String sql2 = "select * from "
						+ BaseStormSystemType.LOG_DB_NAME + "."
						+ tableName + " where role_id="
						+ rolePo.getId() + " and log_time>=" + startTime
						+ " and log_time<=" + endTime + " " + limitSql
						+ " order by log_time"
						+ " LIMIT " + min + ", " + (max - min);
				PrintUtil.print(sql2);
				List rows = BaseDAO.instance().jdbcTemplate.queryForList(sql2);
				for (Object object : rows) {
					LogVo logVo=new LogVo();
					Map userMap = (Map) object;
					logVo.logPar1=Integer.valueOf(userMap.get("log_par1").toString());
					logVo.logPar2=Integer.valueOf(userMap.get("log_par2").toString());
					logVo.logPar3=Integer.valueOf(userMap.get("log_par3").toString());
					logVo.logTime=Long.valueOf(userMap.get("log_time").toString());
					logVo.logType=Integer.valueOf(userMap.get("log_type").toString());
					logVo.remartTxt=userMap.get("remark_txt").toString();
					logVo.roleId=Integer.valueOf(userMap.get("role_id").toString());
					logVo.roleName=userMap.get("role_name").toString();
					logVo.sourceTxt=userMap.get("source_txt").toString();
					logVo.sourceType=Integer.valueOf(userMap.get("source_type").toString());
					logVo.userId=Integer.valueOf(userMap.get("user_id").toString());
					logVo.userIuid=userMap.get("user_iuid").toString();
					logs.add(logVo);
				}
				break;
			}
		}
		String finalMsg="";
		List<String> list = new ArrayList<String>();
		finalMsg+="<hr>";
		int realOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)+timezoneOffset*60*1000;
		for(int i=0;i<logs.size();i++){
			String s = "";
			LogVo logVo=logs.get(i);
			if(logVo.logType<311){
				finalMsg+=DateUtil.getFormatDateBytimestamp(logVo.logTime)+" ";
				s+=DateUtil.getFormatDateBytimestamp(logVo.logTime-realOffset)+" ";
				finalMsg+=logVo.sourceTxt+": ";
				s+=logVo.sourceTxt+": ";
				//1=绑金 2=金币 3=绑钻 4=钻石 5=道具 6=技能点 7=公会荣誉 8=声望 9=成就点 10=宠物魔魂 11=经验 12=宠物 13=pk点 
				String[] resouceTypes={GlobalCache.fetchLanguageMap("key2508"),GlobalCache.fetchLanguageMap("key2509"),GlobalCache.fetchLanguageMap("key2510"),GlobalCache.fetchLanguageMap("key2511"),GlobalCache.fetchLanguageMap("key2512"),GlobalCache.fetchLanguageMap("key2513"),GlobalCache.fetchLanguageMap("key2514"),GlobalCache.fetchLanguageMap("key2515"),GlobalCache.fetchLanguageMap("key2516"),GlobalCache.fetchLanguageMap("key2517"),GlobalCache.fetchLanguageMap("key2518"),GlobalCache.fetchLanguageMap("key2519"),GlobalCache.fetchLanguageMap("key2520")};
				finalMsg+="<font color=#FF00FF>";
	
				if(logVo.logType==1){
					if(logVo.logPar1==5){
						ItemPo itemPo=ItemPo.findEntity(logVo.logPar2);
						if(itemPo!=null){
							finalMsg+=itemPo.getName()+"*"+logVo.logPar3;
							s+=itemPo.getName()+"*"+logVo.logPar3;
						}else{
							for(int j=0;j<XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.size();j++){
								ReplaceItem replaceItem = XmlCache.xmlFiles.constantFile.global.careerItems.replaceItem.get(j);
								List<Integer> ids = StringUtil.getListByStr(replaceItem.replaceItems,"|");
								if(logVo.logPar2.equals(replaceItem.itemId)){
									ItemPo itemPo2=ItemPo.findEntity(ids.get(rolePo.getCareer()-1));
									if(itemPo2!=null){
										finalMsg+=itemPo2.getName()+"*"+logVo.logPar3;
										s+=itemPo2.getName()+"*"+logVo.logPar3;
										break;
									}
								}
							}
						}
					}else if(logVo.logPar1==12){
						PetPo petPo=PetPo.findEntity(logVo.logPar2);
						if(petPo!=null){
							finalMsg+=petPo.getName()+"*"+logVo.logPar3;
							s+=petPo.getName()+"*"+logVo.logPar3;
						}
	
					}else{
						finalMsg+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
						s+=resouceTypes[logVo.logPar1-1]+"*"+logVo.logPar3;
					}
				}else if(logVo.logType==201){
					finalMsg+="to"+(logVo.logPar1+1)+"level";
					s+="to"+(logVo.logPar1+1)+"level";
				}else if(logVo.logType==202){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2521")+logVo.logPar2;
				}else if(logVo.logType==203){
					finalMsg+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"step；left"+GlobalCache.fetchLanguageMap("key2522")+logVo.logPar2;
				}else if(logVo.logType==204){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2523")+logVo.logPar2;
				}else if(logVo.logType==205){
					finalMsg+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"star；left"+GlobalCache.fetchLanguageMap("key2524")+logVo.logPar2;
				}else if(logVo.logType==206){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==207){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
				}else if(logVo.logType==208){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==209){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3;
				}else if(logVo.logType==210){
					ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
					finalMsg+=itemPo.getName();
					s+=itemPo.getName();
					finalMsg+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
					s+=GlobalCache.fetchLanguageMap("key2525")+logVo.logPar3+GlobalCache.fetchLanguageMap("key2526")+logVo.logPar2;
				}else if(logVo.logType==211){
					finalMsg+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"upTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==212){
					finalMsg+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"downTo"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}else if(logVo.logType==234){
					finalMsg+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
					s+="type："+logVo.logPar3+"Still"+logVo.logPar1+"level；left"+GlobalCache.fetchLanguageMap("key2527")+logVo.logPar2;
				}
				else if(logVo.logType==216){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==217){
					finalMsg+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
					s+="RoleId："+logVo.logPar1+"from"+logVo.logPar2+"to"+logVo.logPar3;
				}else if(logVo.logType==218){
					finalMsg+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
					s+="to"+(logVo.logPar1+1)+"level；left"+GlobalCache.fetchLanguageMap("key2528")+logVo.logPar2;
				}else if(logVo.logType==219){
					if(logVo.sourceTxt.equals(GlobalCache.fetchLanguageMap("key2348"))){
						//ItemPo itemPo=ItemPo.findEntity(logVo.logPar1);
						finalMsg+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
						s+="*"+logVo.logPar2+"num；left"+logVo.logPar3;
					}else{
						finalMsg+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
						s+="bindGold："+logVo.logPar1+"leftBindGold："+logVo.logPar2;
					}
				}else if(logVo.logType==220){
					finalMsg+="killerRoleId："+logVo.logPar1;
					s+="killerRoleId："+logVo.logPar1;
				}else if(logVo.logType==221){
					finalMsg+="toRoleId："+logVo.logPar1;
					s+="toRoleId："+logVo.logPar1;
				}else if(logVo.logType==222){
					finalMsg+="fromRoleId："+logVo.logPar1;
					s+="fromRoleId："+logVo.logPar1;
				}else if(logVo.logType==223){
					finalMsg+="fromRoleId：："+logVo.logPar1;
					s+="fromRoleId：："+logVo.logPar1;
				}else if(logVo.logType==224){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==225){
					String atbDescription="";
					List<IdNumberVo2> idNo2List=IdNumberVo2.createList(logVo.remartTxt);
					for(int i1=0;i1<idNo2List.size();i1++){
						if(idNo2List.get(i1).getInt1().intValue()!=0){
							atbDescription+=GameUtil.getAtbDescripeByAtbType(idNo2List.get(i1).getInt1(), idNo2List.get(i1).getInt2());
							atbDescription+=" "+idNo2List.get(i1).getInt3()+"★"+"  ";
						}
					}
					finalMsg+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
					s+="id: "+logVo.logPar2+" qualityLevel："+logVo.logPar1+" ATb:"+atbDescription;
				}else if(logVo.logType==226){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==228){
					finalMsg+="RoleId："+logVo.logPar1;
					s+="RoleId："+logVo.logPar1;
				}else if(logVo.logType==236||logVo.logType==237){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}else if(logVo.logType==235){
					ScenePo scenePo=ScenePo.findEntity(logVo.logPar1);
					if(scenePo!=null){
						finalMsg+="："+scenePo.getName();
						s+="："+scenePo.getName();
					}
				}else if(logVo.logType==238){
					finalMsg+="："+logVo.logPar1;
					s+="："+logVo.logPar1;
				}else if(logVo.logType==239){
					CopySceneConfPo realCopySceneConfPo=CopySceneConfPo.findEntity(logVo.logPar1);
					CopyScenePo copyScenePo=CopyScenePo.findEntity(realCopySceneConfPo.getCopySceneId());
					finalMsg+="："+copyScenePo.getName();
					s+="："+copyScenePo.getName();
				}
				
				else if(logVo.logType==307){
					finalMsg+="num："+(logVo.logPar3/100);
					s+="num："+(logVo.logPar3/100);
				}
				finalMsg+="</font><br>";
				list.add(s);
				finalMsg+="</table><br>";
			}
		}
		if (requireExport == 1) {
			return list;
		}
		return finalMsg;
	}
	
	/**
	 * 修改角色装备(后台接口调用)
	 * @param equipId
	 * @param powerLv
	 * @param attach
	 * @param playerName
	 * @param playerId
	 * @return
	 */
	public Object updateRoleEquip(Integer equipId,  Integer powerLv, String attach, String playerName, Integer playerId) {
		PrintUtil.print("updateRoleEquip() equipId="+equipId+"; playerName="+playerName+"; playerId="+playerId);
		//[是否修改成功(0:否,1:是), 提示消息(0:无,1:角色不存在,2:装备不存在)]
		Object [] objs = new Object[2];
		RolePo rolePo = GlobalCache.fetchRolePoByRoleNameOrRoleId(playerName, playerId);
		if(rolePo == null){
			objs[0] = 0;
			objs[1] = 1;
			return objs;
		}

		int equipSlot = 0;
		if(rolePo.equipWeapon!= null && rolePo.equipWeapon.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_WEAPON;
			if(powerLv >=0) rolePo.equipWeapon.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {//-1代表清空随机属性
				if(StringUtil.equal("-1", attach)){
					rolePo.equipWeapon.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipWeapon.setAttach(attach);
				}
				rolePo.equipWeapon.updateAttach();
			}
			
		}else if(rolePo.equipArmor!=null && rolePo.equipArmor.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_ARMOR;
			if(powerLv >=0) rolePo.equipArmor.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipArmor.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipArmor.setAttach(attach);
				}
				rolePo.equipArmor.updateAttach();
			}
		}else if(rolePo.equipRing!=null && rolePo.equipRing.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_RING;
			if(powerLv >= 0) rolePo.equipRing.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipRing.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipRing.setAttach(attach);
				}
				rolePo.equipRing.updateAttach();
			}
		}else if(rolePo.equipBracer!=null && rolePo.equipBracer.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_BRACER;
			if(powerLv >= 0) rolePo.equipBracer.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipBracer.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipBracer.setAttach(attach);
				}
				rolePo.equipBracer.updateAttach();
			}
		}else if(rolePo.equipNecklace!=null && rolePo.equipNecklace.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_NECKLACE;
			if(StringUtil.isNotEmpty(attach)) if(powerLv >= 0) rolePo.equipNecklace.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipNecklace.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipNecklace.setAttach(attach);
				}
				rolePo.equipNecklace.updateAttach();
			}
		}else if(rolePo.equipHelmet!=null && rolePo.equipHelmet.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_HELMET;
			if(powerLv >= 0) rolePo.equipHelmet.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipHelmet.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipHelmet.setAttach(attach);
				}
				rolePo.equipHelmet.updateAttach();
			}
		}else if(rolePo.equipShoe!=null && rolePo.equipShoe.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_SHOE;
			if(powerLv >= 0) rolePo.equipShoe.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipShoe.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipShoe.setAttach(attach);
				}
				rolePo.equipShoe.updateAttach();
			}
		}else if(rolePo.equipBracelet!=null && rolePo.equipBracelet.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_BRACELET;
			if(powerLv >= 0) rolePo.equipBracelet.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipBracelet.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipBracelet.setAttach(attach);
				}
				rolePo.equipBracelet.updateAttach();
			}
		}else if(rolePo.equipBelt!=null && rolePo.equipBelt.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_BELT;
			if(powerLv >= 0) rolePo.equipBelt.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipBelt.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipBelt.setAttach(attach);
				}
				rolePo.equipBelt.updateAttach();
			}
		}else if(rolePo.equipPants!=null && rolePo.equipPants.getId().intValue()==equipId){
			equipSlot = ItemType.ITEM_CATEGORY_PANTS;
			if(powerLv >= 0) rolePo.equipPants.setPowerLv(powerLv);
			if(StringUtil.isNotEmpty(attach)) {
				if(StringUtil.equal("-1", attach)){//-1代表清空随机属性
					rolePo.equipPants.setAttach(StringUtil.EMPTY);
				}else{
					rolePo.equipPants.setAttach(attach);
				}
				rolePo.equipPants.updateAttach();
			}
		}
		if(equipSlot!=0){
			if(ItemType.ITEM_CATEGORY_WEAPON==equipSlot || ItemType.ITEM_CATEGORY_ARMOR==equipSlot){
				rolePo.sendAvatars();
			}
			rolePo.calculateBat(1);
			rolePo.sendUpdateRoleBatProps(false);	
			rolePo.sendUpdateRoleEquipSlot(equipSlot);
			objs[0] = 1;
			objs[1] = 0;
			return objs;
		}
		
		boolean packHasEquip = rolePo.checkPackHasEquip(equipId);
		boolean warehouseHasEquip = rolePo.checkWarehouseHasEquip(equipId);
		if(!packHasEquip && !warehouseHasEquip){
			objs[0] = 0;
			objs[1] = 2;
			return objs;
		}
		if(packHasEquip) {
			rolePo.modifyPackEquip(equipId, powerLv, attach);
			rolePo.singleRole("ItemRemoting.takeOutByWarehouse",new Object[] {rolePo.mainPackItemVosMap,rolePo.warehousePackItemVosMap}, false);
			objs[0] = 1;
			objs[1] = 0;
			return objs;
		}else{
			rolePo.modifyWarehouseEquip(equipId, powerLv, attach);
			rolePo.singleRole("ItemRemoting.takeOutByWarehouse",new Object[] {rolePo.mainPackItemVosMap,rolePo.warehousePackItemVosMap}, false);
			objs[0] = 1;
			objs[1] = 0;
			return objs;
		}
	}
}
