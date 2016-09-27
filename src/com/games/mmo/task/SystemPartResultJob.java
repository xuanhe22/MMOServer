package com.games.mmo.task;

import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.games.mmo.service.ServerService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
/**
 * 
 * 类功能:KPI
 * @author guole
 * @version 2014-7-11
 */
public class SystemPartResultJob implements Job{
//	public static String[] channelKeys=new String[]{"inner_test","korea_google","korea_ios","vienan_google","vienan_ios","china_andriod","china_ios","hd_ios","hd_andriod"};
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			PrintUtil.print("SystemPartResultJob:"+new Date().toLocaleString()+" start");	
			long t = System.currentTimeMillis();
			String keySql ="select DISTINCT(channel_key) as channel_key from "+BaseStormSystemType.USER_DB_NAME+".u_po_user";
			List<String> channelKeys = BaseDAO.instance().jdbcTemplate.queryForList(keySql, String.class);
			StringBuilder sql = new StringBuilder("INSERT INTO " + BaseStormSystemType.LOG_DB_NAME + ".system_part (date, type, use_num, open_num, use_rate, account_device_type, vip_level, channel) VALUES ");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String day = format.format(new Date(calendar.getTimeInMillis()));
			long startTime = calendar.getTimeInMillis();
			boolean b = true;
			out:
			for (int i = 1; i < 3; i++) {
				for (String channel : channelKeys) {
					for (int j = 0; j <= 10; j++) {
						List<Point> list = ServerService.systemPartResult(i, channel, startTime, j);
						if (list == null) {//没有这一天数据
							b = false;
							break out;
						}
						for (int k = 0; k < list.size(); k++) {
							boolean have = false;
							SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet("select table_name from information_schema.tables where table_schema='"+BaseStormSystemType.LOG_DB_NAME+"'" + " and TABLE_NAME='system_part'");
							if (rs.next())
								have = true;
							if (!have)
								BaseDAO.instance().execute("create table " + BaseStormSystemType.LOG_DB_NAME + ".system_part(open_num INT NOT NULL, use_num INT NOT NULL,id int NOT NULL AUTO_INCREMENT, type INT, date DATE, use_rate DOUBLE, account_device_type INT,vip_level INT, channel VARCHAR(255), PRIMARY KEY ( id ));");
							Point point = list.get(k);
							sql.append("('" + day + "', " + k + ", " + point.x + ", " + point.y + ", " + (point.y != 0 ? point.x * 1.0 / point.y : 0) + ", " + i + ", " + j + ", '" + channel + "'),");
						}
					}
				}
			}
			PrintUtil.print(System.currentTimeMillis() - t);
			if (b) {
				BaseDAO.instance().execute(sql.toString().substring(0, sql.length() - 1));
			}
			PrintUtil.print("SystemPartResultJob:"+new Date().toLocaleString()+" end");	
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		
		
	}
	

	
}
