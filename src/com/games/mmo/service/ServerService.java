package com.games.mmo.service;

import io.netty.channel.ChannelHandlerContext;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.ProductPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.RechargePo;
import com.games.mmo.po.game.SoulElementPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.thread.LogWriteTask;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.DbUtil;
import com.games.mmo.util.TimeUtil;
import com.games.mmo.vo.global.SiegeBidVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BaseGenerator;
import com.storm.lib.component.entity.DbBuilder;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.entity.MemoryTemplate;
import com.storm.lib.component.ftp.FtpServerListener;
import com.storm.lib.component.quartz.QuartzSchedulerTemplate;
import com.storm.lib.component.socket.netty.BaseNettySocketServer;
import com.storm.lib.init.BaseInitProcessor;
import com.storm.lib.runner.ExitThread;
import com.storm.lib.service.BaseServerService;
import com.storm.lib.template.BaseGameServerTemplate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo;
@Service
public class ServerService extends BaseServerService{
	@Autowired
	private RoleTemplate roleTemplate;
	
	@Autowired
	private CheckService checkService;
	
	public String syncData(String[] tableNames,XSSFWorkbook xSSFWorkbook,Boolean reCre) throws Exception {
		PrintUtil.print("开始导表:"+tableNames);

		BaseGenerator baseGenerator = (BaseGenerator) BeanUtil.getBean("baseGenerator");
		DbBuilder dbBuilder = (DbBuilder) BeanUtil.getBean("dbBuilder");
		for (String tableName : tableNames) {
			if(reCre){
				baseGenerator.generatorPoAndDB(new String[]{tableName}, true,xSSFWorkbook);
			}
			else{
				dbBuilder.execute(false, tableName,xSSFWorkbook);
			}

		}
		GameDataTemplate gameDataTemplate = (GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
		gameDataTemplate.reloadAll();
//		GlobalCache.loadAndClean();

		PrintUtil.print("完成导表:"+tableNames);
		return "ok";
	}
	
	@Override
	public void awardRoleByAwardExp(Integer roleId, String awardExp) {

		
	}

	@Override
	public String stopServer(boolean withResetData) {

		return null;
	}

	@Override
	public String stopService(boolean withResetData) {
		String lang="zh_cn";
		List<IdNumberVo> optionsList = null;
		String serverId = "0";
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		if(gp!=null){
			lang=gp.valueObj.toString();
		}
		GlobalPo gpOptions = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyOptions);
		if(gpOptions!=null){
			optionsList=(List<IdNumberVo>) gpOptions.valueObj;
		}
		GlobalPo gpServer = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyServerId);
		if(gpServer!=null){
			serverId=gpServer.valueObj.toString();
		}

		BaseStormSystemType.isRunningShutdown=true;
		try {
			//初始化
//			RmiTemplate rmiTemplate = (RmiTemplate) BeanUtil.getBean("rmiTemplate");
			BaseGameServerTemplate systemTemplate = (BaseGameServerTemplate) BeanUtil.getBean("systemTemplate");
			MemoryTemplate memoryTemplate = (MemoryTemplate) BeanUtil.getBean("memoryTemplate");
			FtpServerListener ftpServerListener = (FtpServerListener) BeanUtil.getBean("ftpServerListener");
			BaseInitProcessor initProcessor = (BaseInitProcessor) BeanUtil.getBean("initProcessor");
			QuartzSchedulerTemplate quartzSchedulerTemplate = (QuartzSchedulerTemplate) BeanUtil.getBean("quartzSchedulerTemplate");
			
			//设置状态
			//Sout.println("设置为停机状态");
			systemTemplate.SERVER_STATUS = BaseStormSystemType.SERVER_STATUS_STOPPED;
			try {
				//关闭定点任务
				//Sout.println("关闭Quartz任务");
				quartzSchedulerTemplate.endQuartz();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			//强迫用户下线
			//Sout.println("强迫所有玩家下线");
			int count =0;
			while(true){
				for (Object ioSession : roleTemplate.getAllSession()) {
					try {
						((ChannelHandlerContext)ioSession).channel().disconnect();
					} catch (Exception e) {
						ExceptionUtil.processException(e);
					}
				}
				if(roleTemplate.roleIdIuidMapping.size()>0){
					//Sout.println("还存在未下线用户"+roleTemplate.roleIdIuidMapping.size());
					Thread.sleep(200);
				}
				else{
					//Sout.println("所有用户均已下线");
					Thread.sleep(5000);
					break;
				}
				count++;
				if(count>20){
					break;
				}
			}
			
			//停止所有线程
			//Sout.println("开始停止所有线程");
			if (!stopAllTasks(false)) {
				return "停机失败,存在运行的线程无法关闭";
			}
			//保存数据
			PrintUtil.print("保存数据");
			try {
				memoryTemplate.doSyncCallData();
				LogWriteTask.callSync();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
				return "停机失败,无法保存数据";
			}
			//保存BattleId			
			//Sout.println("保存BattleId");		
			//关闭FTP
			//Sout.println("关闭FTP");
			try {
				ftpServerListener.close();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			//关闭GameRmi
			//Sout.println("关闭GameRmi");

			//关闭Ehcached
			//Sout.println("关闭Ehcached");
			try {
//				memoryTemplate.closeEhCached();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			//清理数据

			try {

				if (withResetData == true) {
					BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
					SiegeBidVo siegeBidVo = new SiegeBidVo();
					Object[] objs = siegeBidVo.fetchProperyItems();
					String siegeBidVoStr=DBFieldUtil.createPropertyImplod(objs,"|");
					
					List<String> sqls = new ArrayList<String>();
					for (String tbTable : MemoryTemplate.tableNames.values()) {
						if(tbTable.equals("u_po_flag") || 
						 "u_po_live_activity".equals(tbTable) ||
						 "u_po_product".equals(tbTable)){
							continue;
						}	
						sqls.add("truncate " + BaseStormSystemType.USER_DB_NAME+ "."+tbTable);
					}	
					
					if(optionsList != null){
						String optionsStr=IdNumberVo.createStr(optionsList, "|", ",");
						sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keyOptions+"','"+optionsStr+"')");
					}else{
						if(lang.equals("ko")){
							sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keyOptions+"','1|1,2|1,3|1,4|1,5|1,7|1,8|1,9|1,10|1,11|1')");
						}
						else{
							sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keyOptions+"','1|1,2|1,3|1,4|1,5|1,6|1,8|1,10|1,11|1,12|1,14|1')");
						}						
					}
					
					sqls.add("alter table "+BaseStormSystemType.USER_DB_NAME+".u_po_role auto_increment=100000");
					sqls.add("update "+BaseStormSystemType.USER_DB_NAME+".u_po_flag set guild_id=null,flag_status=0");
					sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keySiegeBid+"','"+siegeBidVoStr+"')");
					sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`) values('"+GlobalPo.keyPVEPVPRecordVo+"')");
					sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keyLanguage+"','"+lang+"')");
					sqls.add("insert into "+BaseStormSystemType.USER_DB_NAME+".u_po_global(`key_str`,`value_str`) values('"+GlobalPo.keyServerId+"','"+serverId+"')");
					
					List<LiveActivityPo> list = BaseDAO.instance().getDBList(LiveActivityPo.class);
					if(list.size() > 0){
						sqls.add("update "+BaseStormSystemType.USER_DB_NAME+".u_po_live_activity set rank_items=null");						
					}else{
						sqls.add("truncate table "+BaseStormSystemType.USER_DB_NAME+".u_po_live_activity");
						sqls.add("insert "+BaseStormSystemType.USER_DB_NAME+".u_po_live_activity select * from "+BaseStormSystemType.USER_DB_NAME+".po_static_live_activity");						
					}
					
					List<ProductPo> listProduct =  BaseDAO.instance().getDBList(ProductPo.class);
					if(listProduct.size()>0){
						sqls.add("update "+BaseStormSystemType.USER_DB_NAME+".u_po_product set total_count_buyed=0");
					}else{
						sqls.add("truncate table "+BaseStormSystemType.USER_DB_NAME+".u_po_product");
						sqls.add("insert "+BaseStormSystemType.USER_DB_NAME+".u_po_product select * from "+BaseStormSystemType.USER_DB_NAME+".po_static_product");
					}
					
					
					for (String sql : sqls) {
						PrintUtil.print("sql="+sql);
						baseDAO.executeSpecialTestSql(sql, new Object[] {});
					}
					// 清理活动里面后续添加的活动
					//userDataTemplate.deleteLiveActivityByType(ActivityType.ACTIVITY_LIVE_TYPE_SUBSEQUENT_ADD);
					PrintUtil.print("完成清档");
				}
				
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			//关闭SocketServer
			//Sout.println("关闭SocketServer");
			try {
				BaseNettySocketServer.f.channel().closeFuture();
				BaseNettySocketServer.workerGroup.shutdownGracefully();
				BaseNettySocketServer.bossGroup.shutdownGracefully();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			

			//调度CloseServerThread
//			//Sout.println("调度CloseServerThread");			
			 ExitThread exitThread =new ExitThread();
			 exitThread.start();
//			closeServerThread.start();
			return "恭喜你,系统运行正常,已经发出停服指令,请过找运维确认已经安全停止服务(为确保数据安全,如果运维确认关不了服务请找研发)";
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}		
		finally{
			BaseStormSystemType.isRunningShutdown=false;
		}
		return null;
	}

	
	
	
	/**
	 * 根据充值id发放钻石
	 * @param rechargeId 充值id
	 * @param rolePo
	 * @param amount 充值数量
	 * @param percent 额外奖励（如果没有奖励填0）
	 */
	public void rechargeSendByRechargeId(Integer rechargeId, RolePo rolePo, Integer amount, double percent){
		checkService.checkExisRechargePo(rechargeId);
		RechargePo rechargePo = RechargePo.findEntity(rechargeId);		
		if(rechargePo.getRechargeNum().intValue() != amount.intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key245")+rolePo.getName()+" : "+rechargeId+" : "+amount);
		}
		// type = 3月卡
		if(rechargePo.getChannelType().intValue() == 3){
			rolePo.listRechargeInfo.get(0).wasMonthCard = 1;
			rolePo.listRechargeInfo.get(0).monthCardRechargeBeginTime =System.currentTimeMillis();
			rolePo.listRechargeInfo.get(0).remainMonthCardDay = 30;
			rolePo.listRechargeInfo.get(0).todayWasTakeMonthCard = 0;
			rolePo.listRechargeInfo.get(0).adjustCumulativeRechargeNum(rechargePo.getRechargeNum());
			rolePo.adjustNumberByType(rechargePo.getRechargeNum().intValue() , RoleType.RESOURCE_DIAMOND);
		}else{
			int totalDiamond = 0;
			for(IdNumberVo inv : rolePo.listEachFirstRechargeStatus){
				if(rechargePo.getGroupId().intValue() == inv.getId().intValue() && inv.getNum().intValue() == 0){
					totalDiamond += rechargePo.getRechargeNum()*2;
					inv.setNum(1);
					break;
				}
			}
			totalDiamond += rechargePo.getRechargeNum() + (rechargePo.getRechargeNum()*percent);
			rolePo.listRechargeInfo.get(0).adjustCumulativeRechargeNum(rechargePo.getRechargeNum());
			rolePo.adjustNumberByType(totalDiamond, RoleType.RESOURCE_DIAMOND);
		}
		List<VipPo> listVipPo = GlobalCache.listVipPo;
		int vipLv = 0;
		for(int i = 0; i < listVipPo.size(); i++){
			VipPo vipPo = listVipPo.get(i);
			if(rolePo.listRechargeInfo.get(0).cumulativeRechargeNum.intValue() >= vipPo.getRmbNeed().intValue()*10){
				vipLv = vipPo.getVipLv();
			}
		}
		rolePo.setVipLv(vipLv);
		if(rolePo.listRechargeInfo.get(0).wasFirstRecharge == 0){
			rolePo.listRechargeInfo.get(0).wasFirstRecharge = 1;
		}
		rolePo.sendUpdateRechargeInfo();
		rolePo.sendUpdateTreasure(true);
	}
	
	public void summaryRemain(long startTime, List<String> channelKeys){
		long startDate = DateUtil.getInitialDate(startTime);
		Long[] times = new Long[]{3600L*24*1000*2,
				  3600L*24*1000*3,
				  3600L*24*1000*4,
				  3600L*24*1000*5,
				  3600L*24*1000*6,
				  3600L*24*1000*7,
				  3600L*24*1000*8,
				  3600L*24*1000*14,
				  3600L*24*1000*15,
				  3600L*24*1000*30,
				  3600L*24*1000*31,
				  3600L*24*1000*45,
				  3600L*24*1000*46,
				  3600L*24*1000*60,
				  3600L*24*1000*61,
				  3600L*24*1000*90,
				  3600L*24*1000*91};
		int types[]=new int[]{1,2};
		for (int type : types) {
			for (String channelKey : channelKeys) {
				List<Integer> list =keepResult(type, channelKey, startDate-3600L*24*1000);
				if(list.size()>0){
					StringBuilder sb = new StringBuilder();
					sb.append("select id from ").append(BaseStormSystemType.LOG_DB_NAME).append(".summary_remain where type='").append(type).append("' and summary_time='").append(startDate-3600L*24*1000).append("'").append(" and channel_key = '" + channelKey + "'");
					List<Map<String,Object>> updateId = BaseDAO.instance().jdbcTemplate.queryForList(sb.toString());
					if(updateId!=null && updateId.size()!=0){
						if (updateId.size() > 1) {
							throw new RuntimeException();
						}
						Map<String,Object> map = updateId.get(0);
						StringBuilder updateSql = new StringBuilder();
						updateSql.append("update ").append(BaseStormSystemType.LOG_DB_NAME).append(".summary_remain set equipment_new_role='").append(list.get(0)).append("',");
						updateSql.append("day2='").append(list.get(1)).append("',");
						updateSql.append("day3='").append(list.get(2)).append("',");
						updateSql.append("day4='").append(list.get(3)).append("',");
						updateSql.append("day5='").append(list.get(4)).append("',");
						updateSql.append("day6='").append(list.get(5)).append("',");
						updateSql.append("day7='").append(list.get(6)).append("',");
						updateSql.append("day8='").append(list.get(7)).append("',");
						updateSql.append("day14='").append(list.get(8)).append("',");
						updateSql.append("day15='").append(list.get(9)).append("',");
						updateSql.append("day30='").append(list.get(10)).append("',");
						updateSql.append("day31='").append(list.get(11)).append("',");
						updateSql.append("day45='").append(list.get(12)).append("',");
						updateSql.append("day46='").append(list.get(13)).append("',");
						updateSql.append("day60='").append(list.get(14)).append("',");
						updateSql.append("day61='").append(list.get(15)).append("',");
						updateSql.append("day90='").append(list.get(16)).append("',");
						updateSql.append("day91='").append(list.get(17)).append("'");
						updateSql.append(" where id ='").append(map.get("id").toString()).append("'");
						BaseDAO.instance().execute(updateSql.toString());	
						
					}else{
						GlobalPo gp3 = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyServerId);
						String serverId = "0";
						if(gp3 != null){
							serverId = gp3.valueObj.toString();
						}
						StringBuilder sql = new StringBuilder();
						sql.append("INSERT INTO ").append(BaseStormSystemType.LOG_DB_NAME).append(".summary_remain(type,channel_key,summary_time,equipment_new_role,day2,day3,day4,day5,day6,day7,day8,day14,day15,day30,day31,day45,day46,day60,day61,day90,day91,server_id) VALUES (");
						sql.append("'").append(type).append("',");
						sql.append("'").append(channelKey).append("',");
						sql.append("'").append(startDate-3600L*24*1000).append("',");
						sql.append("'").append(list.get(0)).append("',");
						sql.append("'").append(list.get(1)).append("',");
						sql.append("'").append(list.get(2)).append("',");
						sql.append("'").append(list.get(3)).append("',");
						sql.append("'").append(list.get(4)).append("',");
						sql.append("'").append(list.get(5)).append("',");
						sql.append("'").append(list.get(6)).append("',");
						sql.append("'").append(list.get(7)).append("',");
						sql.append("'").append(list.get(8)).append("',");
						sql.append("'").append(list.get(9)).append("',");
						sql.append("'").append(list.get(10)).append("',");
						sql.append("'").append(list.get(11)).append("',");
						sql.append("'").append(list.get(12)).append("',");
						sql.append("'").append(list.get(13)).append("',");
						sql.append("'").append(list.get(14)).append("',");
						sql.append("'").append(list.get(15)).append("',");
						sql.append("'").append(list.get(16)).append("',");
						sql.append("'").append(list.get(17)).append("',");
						sql.append("'").append(serverId).append("');");
						BaseDAO.instance().execute(sql.toString());								
						
					}
				}
				
				for(Long time : times){
//					PrintUtil.print("time = "+time);
					long currentTime = startDate-time;
//					PrintUtil.print("currentTime="+DateUtil.getFormatDateBytimestamp(currentTime));
					List<Integer> updateList =keepResult(type, channelKey, currentTime);
					if(updateList.size()>0){
						StringBuilder sql = new StringBuilder();
						sql.append("select id from ").append(BaseStormSystemType.LOG_DB_NAME).append(".summary_remain where type='").append(type).append("' and summary_time='").append(currentTime).append("'").append(" and channel_key = '" + channelKey + "'");
						List<Map<String,Object>> updateId = BaseDAO.instance().jdbcTemplate.queryForList(sql.toString());
						if(updateId!=null && updateId.size()!=0){
							if (updateId.size() > 1) {
								throw new RuntimeException();
							}
//							PrintUtil.print("updateId.size()="+updateId.size());
							Map<String,Object> map = updateId.get(0);
							StringBuilder updateSql = new StringBuilder();
							updateSql.append("update ").append(BaseStormSystemType.LOG_DB_NAME).append(".summary_remain set equipment_new_role='").append(updateList.get(0)).append("',");
							updateSql.append("day2='").append(updateList.get(1)).append("',");
							updateSql.append("day3='").append(updateList.get(2)).append("',");
							updateSql.append("day4='").append(updateList.get(3)).append("',");
							updateSql.append("day5='").append(updateList.get(4)).append("',");
							updateSql.append("day6='").append(updateList.get(5)).append("',");
							updateSql.append("day7='").append(updateList.get(6)).append("',");
							updateSql.append("day8='").append(updateList.get(7)).append("',");
							updateSql.append("day14='").append(updateList.get(8)).append("',");
							updateSql.append("day15='").append(updateList.get(9)).append("',");
							updateSql.append("day30='").append(updateList.get(10)).append("',");
							updateSql.append("day31='").append(updateList.get(11)).append("',");
							updateSql.append("day45='").append(updateList.get(12)).append("',");
							updateSql.append("day46='").append(updateList.get(13)).append("',");
							updateSql.append("day60='").append(updateList.get(14)).append("',");
							updateSql.append("day61='").append(updateList.get(15)).append("',");
							updateSql.append("day90='").append(updateList.get(16)).append("',");
							updateSql.append("day91='").append(updateList.get(17)).append("'");
							updateSql.append(" where id ='").append(map.get("id").toString()).append("'");
							BaseDAO.instance().execute(updateSql.toString());
						}						
					}
				}
			}	
		}	
	}
	
	
	/**
	 *  type 1=设备系统参与率  2=账号系统参与率
	 * @param type
	 * @param startTime
	 * @return
	 */
	public static List<Point> systemPartResult(int type,String channel,long startTime,int vipLv) {
    	Date date = new Date(startTime);
    	String tableName="role_log_"+DateUtil.formatDate(date, "yyyy_MM_dd");
    	String dbName=BaseStormSystemType.LOG_DB_NAME+"."+tableName;
    	int tableResult=DbUtil.getCountByTableAndDbName(tableName, BaseStormSystemType.LOG_DB_NAME);
    	if(tableResult>1){
    		throw new RuntimeException("查询到2个以上的表了");
    	}
    	if(tableResult==0){
    		return null;
    	}
    	
    	List<Point> points=new ArrayList<Point>();
    	//领取签到奖励			,每日在线奖励		,幸运转盘（免费）		,幸运转盘（付费、一次）	,幸运转盘（十次）
    	//贵族每日福利领取		,月卡领取			,翅膀系统（升星、升阶）	,装备强化				,装备升星
    	//装备洗炼（使用道具）	,装备洗炼（使用钻石）,宝石镶嵌				,升级技能				,竞技场挑战
    	//招募伙伴（免费）		,招募伙伴（金币一次）,招募伙伴（金币十次）	,招募伙伴（钻石一次）	,招募伙伴（钻石十次）
    	//通天塔				,福利副本（金币）	,福利副本（经验）		,福利副本（材料）		,福利副本（伙伴）
    	//遗失圣境			,恶魔入侵			,守护水晶				,血魔堡垒				,恶灵禁地				
    	//魔化危机			,PK之王			,极限乱斗				,交易行（购买）			,交易行（上架）
    	//商城购买（金币）		,商城购买（钻石）	,商城购买（绑钻）		,组队				,添加好友	
    	//运镖				,活跃度领奖		,普通膜拜				,钻石膜拜				,领取离线奖励		,钻石领取离线奖励
    	List<Integer> logTypes=new ArrayList<Integer>();
    	List<Integer> systemTaskIds=new ArrayList<Integer>(Arrays.asList(new Integer[]{0,0,0,0,0,0,0,610100026,610100038,610100150,610100162,610100162,610100168,610100052,610100045,610100088,610100088,610100088,610100088,610100088,610100110,610100112,610100102,610100152,610100133,610100179,610100136,0,610100085,610100086,0,0,0,0,0,0,0,0,0,0,610100173,0,0,0,0,0}));
    	List<Integer> systemLvs=new ArrayList<Integer>(Arrays.asList(new Integer[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,40,0,0,35,45,40,0,0,0,0,0,0,0,0,0,0,0,0,0}));
    	for(int i=0;i<46;i++){
    		logTypes.add(i+311);
    	}
    	if(type==1){
	    	for(int i=0;i<46;i++){
	    		Point result=new Point();
	    		int count1=0;
	    		int count2=0;
	    		
	    		StringBuilder sb1 = new StringBuilder();
	    		sb1.append("select COUNT(DISTINCT(remark_txt)) from ").append(dbName).append(" where log_type='");
	    		sb1.append(logTypes.get(i)).append("'");
		    	sb1.append(" and channel='");
		    	sb1.append(channel).append("'");
		    	sb1.append(" and log_par3='");
		    	sb1.append(vipLv).append("'");
		    	count1 =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
		    	result.x=count1;
		    	
		    	//select count(DISTINCT(m.remark_txt)) from (select remark_txt,a.role_id from mmo_log_db.role_log_2016_03_25 a where a.log_type='201') m,mmo_db.u_po_role n where m.role_id=n.id and  n.lv > 10 
		    	StringBuilder sb2 = new StringBuilder();
		    	sb2.append("select count(DISTINCT(m.remark_txt)) from (");
	    		sb2.append("select remark_txt,a.role_id from ").append(dbName).append(" a where a.log_type='303' or a.log_type='306'");
		    	sb2.append(" and a.channel='");
		    	sb2.append(channel).append("'");
		    	sb2.append(" and log_par3='");
		    	sb2.append(vipLv).append("' )m, ");
		    	sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role n ");
		    	sb2.append(" where m.role_id=n.id ");
		    	sb2.append(" and n.lv>=");
		    	sb2.append(systemLvs.get(i));
		    	sb2.append(" and n.main_task_id>");
		    	sb2.append(systemTaskIds.get(i));
		    	count2 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
		    	result.y=count2;
		    	
		    	points.add(result);
	    	}
    	}else if(type==2){
    		for(int i=0;i<46;i++){
	    		Point result=new Point();
	    		int count1=0;
	    		int count2=0;
	    		
	    		StringBuilder sb1 = new StringBuilder();
	    		sb1.append("select COUNT(DISTINCT(user_id)) from ").append(dbName).append(" where log_type='");
	    		sb1.append(logTypes.get(i)).append("'");
		    	sb1.append(" and channel='");
		    	sb1.append(channel).append("'");
		    	sb1.append(" and log_par3='");
		    	sb1.append(vipLv).append("'");
		    	count1 =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
		    	result.x=count1;
		    	
		    	//select count(DISTINCT(m.remark_txt)) from (select remark_txt,a.role_id from mmo_log_db.role_log_2016_03_25 a where a.log_type='201') m,mmo_db.u_po_role n where m.role_id=n.id and  n.lv > 10 
		    	StringBuilder sb2 = new StringBuilder();
		    	sb2.append("select count(DISTINCT(m.user_id)) from (");
	    		sb2.append("select user_id,a.role_id from ").append(dbName).append(" a where a.log_type='303' or a.log_type='306'");
		    	sb2.append(" and a.channel='");
		    	sb2.append(channel).append("'");
		    	sb2.append(" and log_par3='");
		    	sb2.append(vipLv).append("'");
		    	sb2.append(") m, ");
		    	sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role n ");
		    	sb2.append(" where m.role_id=n.id ");
		    	sb2.append(" and n.lv>=");
		    	sb2.append(systemLvs.get(i));
		    	sb2.append(" and n.main_task_id>");
		    	sb2.append(systemTaskIds.get(i));
		    	count2 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
		    	result.y=count2;
		    	
		    	points.add(result);
	    	}
    	}
    	return points;
	}
	
	
	
	
	
	/**
	 *  type 1=设备留存 2=账号留存
	 * @param type
	 * @param startTime
	 * @return
	 */
	public List<Integer> keepResult(int type,String channel,long startTime) {
		long overTime=startTime+3600l*24*1000;
    	List<Integer> days=new ArrayList<Integer>();
    	List<Integer> result=new ArrayList<Integer>();
    	
    	days.add(1);//次日
    	days.add(2);//3日
    	days.add(3);//4日
    	days.add(4);//5日
    	days.add(5);//6日
    	days.add(6);//7日
    	days.add(7);//8日
    	days.add(13);//14日
    	days.add(14);//15日
    	days.add(29);//30日
    	days.add(30);//31日
    	days.add(44);//45日
    	days.add(45);//46日
    	days.add(59);//60日
    	days.add(60);//61日
    	days.add(89);//90日
    	days.add(90);//91日
    	
    	int count1=0;
    	if(type==1){
    		StringBuilder sb1 = new StringBuilder();
    		sb1.append("select count(DISTINCT(device_id)) from ");
    		sb1.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    		sb1.append(channel).append("'");
	    	sb1.append(" and create_time>'");
	    	sb1.append(startTime).append("'");
	    	sb1.append(" and create_time<'");
	    	sb1.append(overTime).append("'");
	    	sb1.append(" and first_register_state='");
	    	sb1.append(1).append("'");
	    	count1 =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
    	}else if(type==2){
    		StringBuilder sb1 = new StringBuilder();
    		sb1.append("select count(id) from ");
    		sb1.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    		sb1.append(channel).append("'");
	    	sb1.append(" and create_time>'");
	    	sb1.append(startTime).append("'");
	    	sb1.append(" and create_time<'");
	    	sb1.append(overTime).append("'");
	    	count1 =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
    	}
    	if(count1==0){
    		return new ArrayList<Integer>();
    	}
    	result.add(count1);
    	for(int i=0;i<days.size();i++){
    		if(type==1){
    			int count2=0;
    			StringBuilder sb2 = new StringBuilder();
    			sb2.append("select count(DISTINCT(device_id)) from ");
    			sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    			sb2.append(channel).append("'");
    			sb2.append(" and create_time>'");
    			sb2.append(startTime).append("'");
    			sb2.append(" and create_time<'");
    			sb2.append(overTime).append("'");
    			sb2.append(" and first_register_state='");
    			sb2.append(1).append("'");
    			sb2.append(" and length(keep_information)>='");
    			sb2.append(days.get(i)).append("'");
    			sb2.append(" and right(left(keep_information,");
    			sb2.append(days.get(i)).append("),1)='");
    			sb2.append(1).append("'");
    	    	count2 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
    	    	result.add(count2*10000/count1);
    		}else if(type==2){
    			int count2=0;
    			StringBuilder sb2 = new StringBuilder();
    			sb2.append("select count(id) from ");
    			sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    			sb2.append(channel).append("'");
    			sb2.append(" and create_time>'");
    			sb2.append(startTime).append("'");
    			sb2.append(" and create_time<'");
    			sb2.append(overTime).append("'");
    			sb2.append(" and length(keep_information)>='");
    			sb2.append(days.get(i)).append("'");
    			sb2.append(" and right(left(keep_information,");
    			sb2.append(days.get(i)).append("),1)='");
    			sb2.append(1).append("'");
    	    	count2 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
    	    	result.add(count2*10000/count1);
    		}
    	}
		return result;
	}
	
	/**
	 *  type 1=日设备kpi 2=小时设备kpi 3=日账号kpi 4=月设备 5=月账号 6=小时账号
	 * @param type
	 * @param startTime
	 * @return
	 */
	public List<Integer> kpiResult(int type,String channel,long startTime) {
//		PrintUtil.print("type="+type+"; channel="+channel+"; startTime="+DateUtil.getFormatDateBytimestamp(startTime));
    	//新用户（注册）	新用户（有角色）	设备DAU	设备老用户	设备新用户	设备PU	设备新PU	设备PU%	设备DARPU	设备DARPPU	充值（RMB）	"消耗（钻石）"	平均在线	最高在线
    	//1新用户（注册）	注册类型日志，time满足的个数
    	//2新用户（有角色）	注册类型日志，time满足，创角类型日志，time满足；日志1id=日志2id  日志1的个数
    	//3设备DAU	登录类型日志和登出类型日志和注册日志，time满足，的设备的去重个数（的账号的去重个数）
    	//4日设备老用户	设备老用户=设备DAU-设备新用户                       （账号老用户=账号DAU-账号新用户）
    	//5日设备新用户	注册类型日志，time满足，且设备首次注册=0 的计数（不需要限制设备首次注册=0 的计数）
    	//6设备PU		充值类型日志,time满足，设备的去重个数（账号的去重个数）
    	//7设备新PU	充值类型日志，time满足，且设备首次充值=0 的计数（账号首次充值=0的个数）
    	//8设备PU%	设备PU*10000/设备DAU （万分比）    （账号PU*10000/账号DAU）
    	//9设备DARPU	充值（当地货币）/设备DAU           （充值（当地货币）/账号DAU）  （以分为单位）
    	//10设备DARPPU	充值（当地货币）/设备PU     （充值（当地货币）/账号PU）   （以分为单位）
    	//11充值（当地货币）	充值类型日志,time满足，求和充值   （以元为单位）
    	//12消耗（非绑钻）	将钻石消耗的日志在时间内的求和统计
		//13平均在线
		//14最大在线
    	

		
		//日设备  月设备
    	List<Integer> result=new ArrayList<Integer>();
    	
    	//小时设备
    	List<Integer> result2=new ArrayList<Integer>();
    	
    	//日账号 月账号
    	List<Integer> result3=new ArrayList<Integer>();
    	
    	
    	
    	
    	
    	long overTime=0;
    	if(type==1){
    		overTime=startTime+3600L*24*1000;
    	}else if(type==2||type==6){
    		overTime=startTime+3600L*1000;
    	}else if(type==3){
    		overTime=startTime+3600L*24*1000;
    	}else if(type==4||type==5){
    		overTime=startTime+3600L*24*1000*30;
    	}
    	
    	
    	if(type==2 || type==6){
    		Calendar startDate = Calendar.getInstance();
    		startDate.setTime(new Date(startTime));
    		startDate.set(Calendar.MINUTE, 0);
    		startDate.set(Calendar.HOUR_OF_DAY, 0);
    		startDate.set(Calendar.SECOND, 0);
    		startTime=startDate.getTimeInMillis();
    	}
    	
    	
    	if(type==1 || type==3||type==4||type==5){
	    	//新用户（注册）	注册类型日志，time满足的个数
    		StringBuilder sb = new StringBuilder();
    		sb.append(" and a.log_type='301'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
    		String dbName=fecthQueryTable(startTime, overTime,sb.toString());
    		if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(t.id) from " +dbName;	
//	    	PrintUtil.print("sql1="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	int count1 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result.add(count1);
	    	result3.add(count1);
    	}
    	if(type==1|| type==3||type==4||type==5){
	    	//新用户（有角色）	注册类型日志，time满足，创角类型日志，time满足；日志1id=日志2id  日志1的个数
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and a.log_type='301'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	sb.append(" and exists (select 1 from ").append(BaseStormSystemType.LOG_DB_NAME).append(".####").append(" b where b.log_type='302' and a.user_id=b.user_id");
	    	sb.append(" and b.log_time>'").append(startTime).append("'");
	    	sb.append(" and b.log_time<'").append(overTime).append("')");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(t.id) from "+dbName;
//	    	PrintUtil.print("sql2="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	int count2 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result.add(count2);
	    	result3.add(count2);
    	}
    	int count3=0;
    	if(type==1|| type==2||type==4){
	    	//设备DAU	登录类型日志和登出类型日志，time满足，的设备的去重个数
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and (a.log_type='301' or a.log_type='303' or a.log_type='306')");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select COUNT(DISTINCT(t.remark_txt)) from "+dbName;
//	    	PrintUtil.print("sql3="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count3 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result.add(count3);
    	}else if(type==3||type==5||type==6){
    		StringBuilder sb = new StringBuilder();
    		sb.append(" and (a.log_type='301' or a.log_type='303' or a.log_type='306')");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select COUNT(DISTINCT(t.user_id)) from "+dbName;
//	    	PrintUtil.print("sql4="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count3 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result3.add(count3);
    	}
    	int count5=0;
    	if(type==1|| type==2||type==4){
	    	//日设备新用户	注册类型日志，time满足，且设备首次注册=0 的计数
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and a.log_type='301' and a.log_par1='0'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(t.id) from "+dbName;
//	    	PrintUtil.print("sql5="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count5 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
    	}else if(type==3||type==5||type==6){
    		StringBuilder sb = new StringBuilder();
    		sb.append(" and a.log_type='301'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(t.id) from "+dbName;
//	    	PrintUtil.print("sql6="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count5 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
    	}
    	//日设备老用户	设备老用户=设备DAU-设备新用户
    	int count4=count3-count5;

    	result.add(count4);
    	result2.add(count4);
    	result3.add(count4);
    	result.add(count5);
    	result2.add(count5);
    	result3.add(count5);
    	
    	int count6=0;
    	if(type==1|| type==2||type==4){
	    	//设备PU	充值类型日志,time满足，设备的去重个数
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and a.log_type='307'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select COUNT(DISTINCT(t.remark_txt)) from "+dbName;
//	    	PrintUtil.print("sql7="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count6 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result.add(count6);
	    	result2.add(count6);
    	}else if(type==3||type==5||type==6){
    		StringBuilder sb = new StringBuilder();
    		sb.append(" and a.log_type='307'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select COUNT(DISTINCT(t.user_id)) from "+dbName;
//	    	PrintUtil.print("sql8="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count6 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result3.add(count6);
	    	result2.add(count6);
    	}
    	
    	
    	int count7=0;
    	if(type==1|| type==2||type==4){
	    	//设备新PU	充值类型日志，time满足，且设备首次充值=0 的计数
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and a.log_type='307' and a.log_par2='0'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(t.id) from "+dbName;
//	    	PrintUtil.print("sql9="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count7 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result.add(count7);
	    	result2.add(count7);
    	}else if(type==3||type==5||type==6){
    		StringBuilder sb = new StringBuilder();
    		sb.append(" and a.log_type='307' and a.log_par1='0'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
    			return new ArrayList<Integer>();
    		}
	    	String sql = "select count(DISTINCT(t.user_id)) from "+dbName;
//	    	PrintUtil.print("sql10="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	count7 =BaseDAO.instance().queryIntForSql(sql, null);
//	    	PrintUtil.print("end:"+sql+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	result3.add(count7);
	    	result2.add(count7);
    	}
    	//设备PU%	设备PU*10000/设备DAU （万分比）
    	int count8;
    	if(count3==0){
    		count8=0;
    	}else{
    		count8=count6*10000/count3;
    	}
    	result.add(count8);
    	result3.add(count8);
    	
    	//充值（当地货币）	充值类型日志,time满足，求和充值
    	StringBuilder sbAll = new StringBuilder();
    	sbAll.append(" and a.log_type='307'");
    	sbAll.append(" and a.channel='").append(channel).append("'");
    	sbAll.append(" and a.log_time>'").append(startTime).append("'");
    	sbAll.append(" and a.log_time<'").append(overTime).append("'");
    	String dbNameAll=fecthQueryTable(startTime, overTime, sbAll.toString());
    	if(dbNameAll==null){
			return new ArrayList<Integer>();
		}
    	String sqlAll = "select sum(t.log_par3/100) from "+dbNameAll;
//    	PrintUtil.print("sql11="+sqlAll);
    	Integer count11 =BaseDAO.instance().queryIntForSql(sqlAll, null);
    	if(count11==null){
    		count11=0;
    	}
    	//9设备DARPU	充值（当地货币）/设备DAU
    	int count9;
    	if(count3==0){
    		count9=0;
    	}else{
    		count9=count11*100/count3;
    	}
    	result.add(count9);
    	result3.add(count9);
    	
    	//10设备DARPPU	充值（当地货币）/付费设备DAU
    	int count10;
    	if(count6==0){
    		count10=0;
    	}else{
    		count10=count11*100/count6;
    	}
    	result.add(count10);
    	result3.add(count10);
    	result.add(count11);
    	result2.add(count11);
    	result3.add(count11);
    	if(type==1 || type==3||type==4||type==5){
	    	//12消耗（非绑钻）	将钻石消耗的日志在时间内的求和统计
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" and a.log_type='1' and a.log_par1='4' and a.log_par3<'0'");
	    	sb.append(" and a.channel='").append(channel).append("'");
	    	sb.append(" and a.log_time>'").append(startTime).append("'");
	    	sb.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName=fecthQueryTable(startTime, overTime, sb.toString());
	    	if(dbName==null){
				return new ArrayList<Integer>();
			}
	    	String sql = "select sum(t.log_par3) from "+dbName;
//	    	PrintUtil.print("sql12="+sql);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql);
	    	Integer count12 =BaseDAO.instance().queryIntForSql(sql, null);
	    	if(count12==null){
	    		count12=0;
	    	}
	    	result.add(-count12);
	    	result3.add(-count12);
	    	
	    	
    	}
    	if(type==1 || type==3){
	    	//13平均在线人数
	    	StringBuilder sb1 = new StringBuilder();
	    	sb1.append(" and a.log_type='309'");
	    	sb1.append(" and a.channel='").append(channel).append("'");
	    	sb1.append(" and a.log_time>'").append(startTime).append("'");
	    	sb1.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName1=fecthQueryTable(startTime, overTime, sb1.toString());
	    	if(dbName1==null){
				return new ArrayList<Integer>();
			}
	    	String sql1 = "select count(t.log_par1) from "+dbName1;
//	    	PrintUtil.print("sql13="+sql1);
	    	int num13 =BaseDAO.instance().queryIntForSql(sql1, null);
	    	
	    	StringBuilder sb2 = new StringBuilder();
	    	sb2.append(" and a.log_type='309'");
	    	sb2.append(" and a.channel='").append(channel).append("'");
	    	sb2.append(" and a.log_time>'").append(startTime).append("'");
	    	sb2.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName2=fecthQueryTable(startTime, overTime, sb2.toString());
	    	if(dbName2==null){
				return new ArrayList<Integer>();
			}
	    	String sql2 = "select sum(t.log_par1) from "+dbName2;
//	    	PrintUtil.print("sql14="+sql2);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql2);
	    	Integer sum13 =BaseDAO.instance().queryIntForSql(sql2, null);
//	    	PrintUtil.print("end:"+sql2+" cost:"+(System.currentTimeMillis()-runStartTime));
	    	if(sum13==null){
	    		sum13=0;
	    	}
	    	int count13;
	    	if(num13==0){
	    		count13=0;
	    	}else{
	    		count13=sum13/num13;
	    	}
	    	result.add(count13);
	    	result3.add(count13);
    	}
    	if(type==1 || type==3){
	    	//14最高在线人数
	    	StringBuilder sb1 = new StringBuilder();
	    	sb1.append(" and a.log_type='309'");
	    	sb1.append(" and a.channel='").append(channel).append("'");
	    	sb1.append(" and a.log_time>'").append(startTime).append("'");
	    	sb1.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName1=fecthQueryTable(startTime, overTime, sb1.toString());
	    	if(dbName1==null){
				return new ArrayList<Integer>();
			}
	    	String sql1 = "select max(t.log_par1) from "+dbName1;
//	    	PrintUtil.print("sql15="+sql1);
	    	Integer count14 =BaseDAO.instance().queryIntForSql(sql1, null);
	    	if(count14==null){
	    		count14=0;
	    	}
	    	result.add(count14);
	    	result3.add(count14);
	    	
	    	StringBuilder sb2 = new StringBuilder();
	    	sb2.append(" and a.log_type='310'");
	    	sb2.append(" and a.channel='").append(channel).append("'");
	    	sb2.append(" and a.log_time>'").append(startTime).append("'");
	    	sb2.append(" and a.log_time<'").append(overTime).append("'");
	    	String dbName2=fecthQueryTable(startTime, overTime, sb2.toString());
	    	if(dbName2==null){
				return new ArrayList<Integer>();
			}
	    	String sql2 = "select max(t.log_par1) from "+dbName2;
//	    	PrintUtil.print("sql15="+sql2);
//	    	long runStartTime=System.currentTimeMillis();
//	    	PrintUtil.print("start:"+sql2);
	    	Integer count15 =BaseDAO.instance().queryIntForSql(sql2, null);
	    	if(count15==null){
	    		count15=0;
	    	}
	    	result.add(count15);
	    	result3.add(count15);
    	}
    	if(type==2){
    		Date lastDayDate = new Date(startTime-24*3600*1000);
    		Date todayDate = new Date(startTime);
	    	String lastDayTableName=BaseStormSystemType.LOG_DB_NAME+"."+"role_log_"+DateUtil.formatDate(lastDayDate, "yyyy_MM_dd");
	    	String todayTableName=BaseStormSystemType.LOG_DB_NAME+"."+"role_log_"+DateUtil.formatDate(todayDate, "yyyy_MM_dd");
    		

	    	String last = "role_log_"+DateUtil.formatDate(lastDayDate, "yyyy_MM_dd");
	    	int tableResult=DbUtil.getCountByTableAndDbName(last, BaseStormSystemType.LOG_DB_NAME);
	    	if(tableResult>1){
	    		throw new RuntimeException("查询到2个以上的表了");
	    	}
	    	if(tableResult==0) {
	    		result2.add(0);
				result2.add(0);
	    	}else{
	    	
	    	
	    		//设备新用户
	    		int newPlayerNum=0;
	    		StringBuilder sb1 = new StringBuilder();
		    	sb1.append("select count(id) from ").append(lastDayTableName).append(" where log_type='301' and log_par1='0'");
		    	sb1.append(" and channel='");
		    	sb1.append(channel).append("'");
		    	newPlayerNum =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
		    	
	    		//留存人数
	    		int count16=0;
//				StringBuilder sb2 = new StringBuilder();
//		    	sb2.append("select COUNT(DISTINCT(remark_txt)) from ").append(todayTableName).append(" a where (a.log_type='303' or a.log_type='306')");
//		    	sb2.append(" and a.channel='");
//		    	sb2.append(channel).append("'");
//		    	sb2.append("and exists (select 1 from ").append(lastDayTableName).append(" b where b.log_type=301 and b.log_par1='0' and a.user_id=b.user_id)");
//		    	count16 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);	
		    	
    			StringBuilder sb2 = new StringBuilder();
    			sb2.append("select count(DISTINCT(device_id)) from ");
    			sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    			sb2.append(channel).append("'");
    			sb2.append(" and create_time>'");
    			sb2.append(startTime-24l*3600*1000).append("'");
    			sb2.append(" and create_time<'");
    			sb2.append(startTime).append("'");
    			sb2.append(" and first_register_state='");
    			sb2.append(1).append("'");
    			sb2.append(" and length(keep_information)>='");
    			sb2.append(1).append("'");
    			sb2.append(" and right(left(keep_information,");
    			sb2.append(1).append("),1)='");
    			sb2.append(1).append("'");
    			count16 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
    	    	
				result2.add(count16);
				result2.add(newPlayerNum);
	    	}
		}else if(type==6){
			
			Date lastDayDate = new Date(startTime-24*3600*1000);
    		Date todayDate = new Date(startTime);
	    	String lastDayTableName=BaseStormSystemType.LOG_DB_NAME+"."+"role_log_"+DateUtil.formatDate(lastDayDate, "yyyy_MM_dd");
	    	String todayTableName=BaseStormSystemType.LOG_DB_NAME+"."+"role_log_"+DateUtil.formatDate(todayDate, "yyyy_MM_dd");
	    	

	    	String last = "role_log_"+DateUtil.formatDate(lastDayDate, "yyyy_MM_dd");
	    	int tableResult=DbUtil.getCountByTableAndDbName(last, BaseStormSystemType.LOG_DB_NAME);
	    	if(tableResult>1){
	    		throw new RuntimeException("查询到2个以上的表了");
	    	}
	    	if(tableResult==0) {
	    		result2.add(0);
				result2.add(0);
	    	}else{
	    		//账号新用户
	    		int newPlayerNum=0;
	    		StringBuilder sb1 = new StringBuilder();
		    	sb1.append("select count(id) from ").append(lastDayTableName).append(" where log_type='301' ");
		    	sb1.append(" and channel='");
		    	sb1.append(channel).append("'");
		    	newPlayerNum =BaseDAO.instance().queryIntForSql(sb1.toString(), null);
		    	
		    	//留存人数
	    		int count16=0;
//				StringBuilder sb2 = new StringBuilder();
//		    	sb2.append("select COUNT(DISTINCT(user_id)) from ").append(todayTableName).append(" a where (a.log_type='303' or a.log_type='306')");
//		    	sb2.append(" and a.channel='");
//		    	sb2.append(channel).append("'");
//		    	sb2.append("and exists (select 1 from ").append(lastDayTableName).append(" b where b.log_type=301 and a.user_id=b.user_id)");
//		    	count16 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
		    	
		    	StringBuilder sb2 = new StringBuilder();
    			sb2.append("select count(DISTINCT(id)) from ");
    			sb2.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_user where channel_key='");
    			sb2.append(channel).append("'");
    			sb2.append(" and create_time>'");
    			sb2.append(startTime-24l*3600*1000).append("'");
    			sb2.append(" and create_time<'");
    			sb2.append(startTime).append("'");
    			sb2.append(" and first_register_state='");
    			sb2.append(1).append("'");
    			sb2.append(" and length(keep_information)>='");
    			sb2.append(1).append("'");
    			sb2.append(" and right(left(keep_information,");
    			sb2.append(1).append("),1)='");
    			sb2.append(1).append("'");
    			count16 =BaseDAO.instance().queryIntForSql(sb2.toString(), null);
		    	
		    	result2.add(count16);
				result2.add(newPlayerNum);
	    	}
		}
    	
    	
    	
    	if(type==1||type==4){
    		return result;
    	}else if(type==2||type==6){
    		return result2;
    	}else if(type==3||type==5){
    		return result3;
    	}
    	return null;
	}
	
	
	public String fecthQueryTable(Long startTime,Long endTime, String sqlWhere){
//		PrintUtil.print("startTime="+DateUtil.getFormatDateBytimestamp(startTime));
//		PrintUtil.print("endTime="+DateUtil.getFormatDateBytimestamp(endTime));
		
		long startTimeInitial = DateUtil.getInitialDate(startTime);
		long endTimeInitial = DateUtil.getInitialDate(endTime);
		
//		Date start=new Date(startTime);
//		Date end=new Date(endTime);
		
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select table_name from information_schema.tables where table_schema='"+BaseStormSystemType.LOG_DB_NAME+"'");
		StringBuilder sb = new StringBuilder();
		int index =0;
		sb.append("(");
		while (rs.next()){
			String tableName = rs.getString(1);
			
			if ("summary_day".equals(tableName) || "summary_hour".equals(tableName) || "summary_month".equals(tableName) || "summary_remain".equals(tableName)) {
				continue;
			}
			String[] dateNames = tableName.split("_");
			if(dateNames==null || dateNames.length != 5){
				continue;
			}
			int year = Integer.valueOf(dateNames[2]);
			int month = Integer.valueOf(dateNames[3]);
			int day = Integer.valueOf(dateNames[4]);
			long tableNameTime = DateUtil.getInitialDate(year+"-"+month+"-"+day);
			if(startTimeInitial==endTimeInitial){
				if(tableNameTime!=startTimeInitial){
					continue;
				}
			}else{
				if(tableNameTime<startTimeInitial){
					continue;
				}
				if(tableNameTime>=endTimeInitial){
					continue;
				}				
			}
			if(index!=0){
				sb.append(" union ");    				
			}
			sb.append("select a.id,a.role_id,a.role_name,a.user_id,a.log_type,a.log_time,a.log_par1,a.log_par2,a.log_par3,a.source_type,a.source_txt,a.remark_txt,a.channel from ").append(BaseStormSystemType.LOG_DB_NAME).append(".").append(tableName).append(" a ");
			sb.append(" where 1=1 ");
			if(sqlWhere!=null){
				String where = sqlWhere.replaceAll("####", tableName);
				sb.append(where);
			}
			
			index++;
		}
		sb.append(") t ");
		
		if(index == 0){
			return null;
		}
		
		return sb.toString();
	}
	/**
	 * 查询钻石消耗日志
	 * @param role_id(角色ID),role_name,user_id,user_iuid,log_time（日志打印时间）,source_type（消耗来源）
	 * @return
	 */
	public List<Object[]> findMoneyConsumeLog(long startTime, long endTime)
	{
		List<Object[]> result = new ArrayList<Object[]>();
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(startTime);
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis(endTime);
		int day = TimeUtil.dateCompare(startDate, endDate);
		String dbName=BaseStormSystemType.LOG_DB_NAME+"."+"role_log_";
		String sql = "select role_id,role_name,user_id,user_iuid,log_time,source_type from %s where log_type=1 and log_par1=4 and log_par3<0";
		JdbcTemplate jdbcTemplate = BaseDAO.instance().jdbcTemplate;
		if(TimeUtil.dateCompare(startDate, endDate) > 0)
		{
			for(int i=0;i<day;i++)
			{
				String sql2 = "";
				if(i == 0)
				{
					
					String tableName=dbName+DateUtil.formatDate(startDate.getTime(), "yyyy_MM_dd");
					sql2 = String.format(sql, tableName);
					sql2 = sql2 + " and log_time >= "+startTime;
				}
				else if(i == day-1)
				{
					String tableName=dbName+DateUtil.formatDate(endDate.getTime(), "yyyy_MM_dd");
					sql2 = String.format(sql, tableName);
					sql2 = sql2 + " and log_time <= "+endTime;
				}
				else
				{
					String tableName=dbName+DateUtil.formatDate(new Date(startTime+i*86400000), "yyyy_MM_dd");
					sql2 = String.format(sql, tableName);
				}
				List<Object[]> datas = jdbcTemplate.query(sql2, new ResultSetExtractor<List<Object[]>>() {
					@Override
					public List<Object[]> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<Object[]> datas = new ArrayList<Object[]>();
						while(rs.next())
						{
							Object[] data = new Object[]{rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getLong(5),rs.getString(6)};
							datas.add(data);
						}
						return datas;
					}
				});
				result.addAll(datas);
			}
		}
		else
		{
			String tableName=dbName+DateUtil.formatDate(startDate.getTime(), "yyyy_MM_dd");
			sql = String.format(sql, tableName);
			String sql2 = sql + " and log_time >= "+startTime+" and log_time <= "+endTime;
			List<Object[]> datas = jdbcTemplate.query(sql2, new ResultSetExtractor<List<Object[]>>() {
				@Override
				public List<Object[]> extractData(ResultSet rs)
						throws SQLException, DataAccessException {
					List<Object[]> datas = new ArrayList<Object[]>();
					while(rs.next())
					{
						Object[] data = new Object[]{rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getLong(5),rs.getString(6)};
						datas.add(data);
					}
					return datas;
				}
			});
			result.addAll(datas);
		}
		return result;
	}

	public List createList() {
		List list=new ArrayList();
		list.add(GameDataTemplate.gameDataListCache.get("MonsterPo"));
		list.add(GameDataTemplate.gameDataListCache.get("ScenePo"));
		list.add(GameDataTemplate.gameDataListCache.get("MonsterFreshPo"));
		list.add(GameDataTemplate.gameDataListCache.get("ItemPo"));
		list.add(GameDataTemplate.gameDataListCache.get("SkillPo"));
		list.add(GameDataTemplate.gameDataListCache.get("BuffPo"));
		list.add(GameDataTemplate.gameDataListCache.get("VisualPo"));
		list.add(GameDataTemplate.gameDataListCache.get("LvConfigPo"));
		list.add(GameDataTemplate.gameDataListCache.get("CopyScenePo"));
		list.add(GameDataTemplate.gameDataListCache.get("CopySceneConfPo"));
		list.add(GameDataTemplate.gameDataListCache.get("CastPosePo"));
		list.add(GameDataTemplate.gameDataListCache.get("GatewayPo"));
		list.add(GameDataTemplate.gameDataListCache.get("UpgradeSkillPo"));
		list.add(GameDataTemplate.gameDataListCache.get("TaskPo"));
		list.add(GameDataTemplate.gameDataListCache.get("SkillDescriptionPo"));
		list.add(GameDataTemplate.gameDataListCache.get("LinesPo"));
		list.add(GameDataTemplate.gameDataListCache.get("PetPo"));
		list.add(GameDataTemplate.gameDataListCache.get("PetUpgradePo"));
		list.add(GameDataTemplate.gameDataListCache.get("PetRollPo"));
		list.add(GameDataTemplate.gameDataListCache.get("MergePo"));
		list.add(GameDataTemplate.gameDataListCache.get("ConsumPo"));
		list.add(GameDataTemplate.gameDataListCache.get("PetTalentPo"));
		list.add(GameDataTemplate.gameDataListCache.get("PetSkillPo"));
		list.add(GameDataTemplate.gameDataListCache.get("StoryActionPo"));
		list.add(GameDataTemplate.gameDataListCache.get("StoryTriggerPo"));
		list.add(GameDataTemplate.gameDataListCache.get("NewbiePo"));
		list.add(GameDataTemplate.gameDataListCache.get("TitlePo"));
		list.add(GameDataTemplate.gameDataListCache.get("GrowthPo"));
		list.add(GameDataTemplate.gameDataListCache.get("FashionPo"));
		list.add(GameDataTemplate.gameDataListCache.get("RechargePo"));
		list.add(GameDataTemplate.gameDataListCache.get("VipPo"));
		list.add(GameDataTemplate.gameDataListCache.get("OpenfunctionPo"));
		list.add(GameDataTemplate.gameDataListCache.get("RedremindPo"));	
		list.add(GameDataTemplate.gameDataListCache.get("MilitaryRankPo"));
		list.add(GameDataTemplate.gameDataListCache.get("InvitationPo"));
		list.add(GameDataTemplate.gameDataListCache.get("SoulElementPo"));
		list.add(GameDataTemplate.gameDataListCache.get("SoulStepPo"));
		return list;
	}

}
