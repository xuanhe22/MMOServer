package com.games.mmo.task;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.games.mmo.po.GlobalPo;
import com.games.mmo.service.ServerService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
/**
 * 
 * 类功能:KPI
 * @author guole
 * @version 2014-7-11
 */
public class KPIJob implements Job{
//	public static String[] channelKeys=new String[]{"inner_test","korea_google","korea_ios","vienan_google","vienan_ios","china_andriod","china_ios","hd_ios","hd_andriod"};
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			PrintUtil.print("KPIJob start:"+new Date().toLocaleString());
			long startTime=System.currentTimeMillis();
			ServerService serverService = (ServerService) BeanUtil.getBean("serverService");
			Date date =new Date();
			date.setMinutes(0);
			date.setSeconds(0);
			Long time =date.getTime();
			String keySql ="select DISTINCT(channel_key) as channel_key from "+BaseStormSystemType.USER_DB_NAME+".u_po_user";
			List<String> channelKeys = BaseDAO.instance().jdbcTemplate.queryForList(keySql, String.class);
//			PrintUtil.print(channelKeys);
			
			BaseDAO.instance().execute("delete from "+BaseStormSystemType.LOG_DB_NAME+".summary_day where summary_time="+time);
			BaseDAO.instance().execute("delete from "+BaseStormSystemType.LOG_DB_NAME+".summary_hour where summary_time="+time);
			BaseDAO.instance().execute("delete from "+BaseStormSystemType.LOG_DB_NAME+".summary_month where summary_time="+time);
			BaseDAO.instance().execute("delete from "+BaseStormSystemType.LOG_DB_NAME+".summary_remain where summary_time="+time);
			int types[]=new int[]{1,3};
			int types2[]=new int[]{4,5};
			GlobalPo gp3 = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyServerId);
			String serverId = "0";
			if(gp3 != null){
				serverId = gp3.valueObj.toString();
			}
			
			//凌晨
			if(date.getHours()==0){
				for (int type : types) {
					for (String channelKey : channelKeys) {
						List<Integer> dayAccountKips =serverService.kpiResult(type, channelKey, time-24L*3600*1000);
						if(dayAccountKips.size()>0){
							String sql="";
							sql+="insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_day(type,channel_key,summary_time,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,pu_percentage,darpu,darppu,charge_currency,consume_diamond,avg_online,max_online,remain_diamond,server_id) values";
							sql+="("+type+",'"+channelKey+"',"+time+","+dayAccountKips.get(0)+","+dayAccountKips.get(1)+","+dayAccountKips.get(2)+","+dayAccountKips.get(3)+","+dayAccountKips.get(4)+","+dayAccountKips.get(5)+","+dayAccountKips.get(6)+","+dayAccountKips.get(7)+","+dayAccountKips.get(8)+","+dayAccountKips.get(9)+","+dayAccountKips.get(10)+","+dayAccountKips.get(11)+","+dayAccountKips.get(12)+","+dayAccountKips.get(13)+","+dayAccountKips.get(14)+","+serverId+")";
							BaseDAO.instance().execute(sql);
						} else {
							String sql="";
							sql += "insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_day(type,channel_key,summary_time,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,pu_percentage,darpu,darppu,charge_currency,consume_diamond,avg_online,max_online,remain_diamond,server_id) values";
							sql += "("+type+",'"+channelKey+"',"+time+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"+serverId+")";
							BaseDAO.instance().execute(sql);
						}
					}
				}
			}

			//每个小时
			for (String channelKey : channelKeys) {
				//检测hourkpi数据表中是否有留存率
				String sql1 = "SELECT TABLE_NAME FROM information_schema.`COLUMNS` WHERE TABLE_NAME = 'summary_hour' and COLUMN_NAME = 'remain' and TABLE_SCHEMA='" + BaseStormSystemType.LOG_DB_NAME + "'";
				SqlRowSet rs1 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql1);
				if (!rs1.next()) {
					BaseDAO.instance().execute("alter table "+BaseStormSystemType.LOG_DB_NAME+".summary_hour add remain int(11)");
				}
				//检测hourkpi数据表中是否有留存人数
				String sql2 = "SELECT TABLE_NAME FROM information_schema.`COLUMNS` WHERE TABLE_NAME = 'summary_hour' and COLUMN_NAME = 'total' and TABLE_SCHEMA='" + BaseStormSystemType.LOG_DB_NAME + "'";
				SqlRowSet rs2 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql2);
				if (!rs2.next()) {
					BaseDAO.instance().execute("alter table "+BaseStormSystemType.LOG_DB_NAME+".summary_hour add total int(11)");
				}
				//设备kpi
				List<Integer> dayDeviceKips =serverService.kpiResult(2, channelKey,time-3600*1000);
				if(dayDeviceKips.size()>0){
					String sql="";
					sql+="insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_hour(type,channel_key,summary_time,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,total,server_id) values";
					sql+="(2,'"+channelKey+"',"+time+","+dayDeviceKips.get(0)+","+dayDeviceKips.get(1)+","+dayDeviceKips.get(2)+","+dayDeviceKips.get(3)+","+dayDeviceKips.get(4)+ "," + dayDeviceKips.get(5) + "," + dayDeviceKips.get(6) + "," + serverId + ")";
					BaseDAO.instance().execute(sql);	
				} else {
					String sql="";
					sql+="insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_hour(type,channel_key,summary_time,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,total,server_id) values";
					sql+="(2,'"+channelKey+"',"+time+",0,0,0,0,0,0,0,"+serverId+")";
					BaseDAO.instance().execute(sql);
				}
				//账号KPI
				List<Integer> dayAccountKips =serverService.kpiResult(6, channelKey,time-3600*1000);
				if(dayAccountKips.size()>0){
					String sql="";
					sql+="insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_hour(type,channel_key,summary_time,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,total,server_id) values";
					sql+="(6,'"+channelKey+"',"+time+","+dayAccountKips.get(0)+","+dayAccountKips.get(1)+","+dayAccountKips.get(2)+","+dayAccountKips.get(3)+","+dayAccountKips.get(4)+ "," + dayAccountKips.get(5) + "," + dayAccountKips.get(6) + "," + serverId  + ")";
					BaseDAO.instance().execute(sql);	
				} else {
					String sql="";
					sql+="insert into "+BaseStormSystemType.LOG_DB_NAME+".summary_hour(type,channel_key,summary_time,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,total,server_id) values";
					sql+="(6,'"+channelKey+"',"+time+",0,0,0,0,0,0,0,"+serverId+")";
					BaseDAO.instance().execute(sql);
				}
			}
			long endTime=System.currentTimeMillis();
			PrintUtil.print("KPIJob end:"+new Date().toLocaleString());	
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		
		
	}
	

	
}
