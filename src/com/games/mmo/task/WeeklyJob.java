package com.games.mmo.task;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.games.mmo.service.GuildService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;

public class WeeklyJob  implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		PrintUtil.print("每周调用时间：" + System.currentTimeMillis());
		try {
			PrintUtil.print("WeeklyJob:"+new Date().toLocaleString()+" start");	
			GuildService guildService = (GuildService) BeanUtil.getBean("guildService");
			guildService.checkGuildGoldWeeklyDeduct();
			PrintUtil.print("WeeklyJob:"+new Date().toLocaleString()+" end");	
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

}
