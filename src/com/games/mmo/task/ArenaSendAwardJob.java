package com.games.mmo.task;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.games.mmo.service.MapService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo2;

/**
 * 
 * 类功能:竞技场发送奖励
 * @author johnny
 * @version 2014-6-27
 */
public class ArenaSendAwardJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		PrintUtil.print("ArenaSendAwardJob start:"+new Date().toLocaleString());
		try {
			MapService mapService = (MapService) BeanUtil.getBean("mapService");
			mapService.sendArenaAward();
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		PrintUtil.print("ArenaSendAwardJob end:"+new Date().toLocaleString());
	}

}
