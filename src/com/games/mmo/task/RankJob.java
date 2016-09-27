package com.games.mmo.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
 
import com.games.mmo.dao.RoleDAO;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
/**
 * 
 * 类功能:排行榜定时刷新
 * @author guole
 * @version 2014-7-11
 */
public class RankJob implements Job{

	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			System.out.println("RankJob start:"+new Date().toLocaleString());	
			System.out.println("RankJob() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
			RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
			roleDAO.generateRank();
			System.out.println("RankJob end:"+new Date().toLocaleString());	
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
}
