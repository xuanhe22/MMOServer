package com.games.mmo.thread;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.LogVo;
import com.storm.lib.component.entity.MemoryTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

/**
 * 
 * 类功能:数据库同步线程
 * @author johnny
 * @version 2012-6-20
 */
public class LogWriteTask extends Thread{
	/**
	 * 标记当前有一个实例在运行
	 */
	public static boolean isRunning=false;
	public static StringBuilder sql=new StringBuilder(400);
	@Override
	public void run() {
		setName("LogWriteTask");
		MemoryTemplate memoryTemplate = (MemoryTemplate) BeanUtil.getBean("memoryTemplate");
		while(true){
			if(isRunning){
				PrintUtil.print("正在运行同步,跳过");
			}
			else{
				isRunning=true;
				callSync();
				isRunning=false;
			}
			try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
				ExceptionUtil.processException(e);
			}
		}
	}

	public static void callSync() {
		try {
			Connection conn = null;
			try {
				Statement stat = null;
				try {
					DataSource datasource =(DataSource) BeanUtil.getBean("dataSource");
					conn = datasource.getConnection();
					conn.setAutoCommit(false);
					stat = conn.createStatement();
				} catch (Exception e) {
					ExceptionUtil.processException(e);
				}
				LogVo logVo=null;
				while((logVo=LogUtil.log.poll())!=null){
					tryUpdatePo(stat, logVo);
				}	
				conn.commit();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			
			try {
				conn.close();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
			
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}

	}

	private static void tryUpdatePo(Statement stat, LogVo logVo) {
		try{
			sql.setLength(0);
			sql.append("insert into ");
			sql.append(BaseStormSystemType.LOG_DB_NAME);
			sql.append(".").append(LogUtil.todayTableName).append("(`role_id`, `role_name`,  `user_id`,  `user_iuid`,  `log_type`, `log_time`,  `log_par1`, `log_par2`,  `log_par3`, `source_type`, `source_txt`,  `remark_txt`,`channel`) values (");
			sql.append(logVo.roleId).append(", '").append(logVo.roleName).append("', ").append(logVo.userId).append(", '").append(logVo.userIuid).append("', ").append(logVo.logType).append(", ").append(logVo.logTime).append(", ").append(logVo.logPar1).append(", ").append(logVo.logPar2).append(", ").append(logVo.logPar3).append(", ").append(logVo.sourceType).append(", '").append(logVo.sourceTxt).append("', '").append(logVo.remartTxt).append("','"+logVo.channel).append("')");
			stat.execute(sql.toString());	
		} catch (Exception e) {	
			PrintUtil.print("error db sql="+new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" sql = " +sql.toString());
			ExceptionUtil.processException(e);
		}

	}
	
//	public LogWriteTask() {
//		this.setRepeatDelayMS(1000*30);
//		this.setFirstDelayMS(1000);
//		
//	}
}
