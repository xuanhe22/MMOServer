package com.games.mmo.task;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;

public class TimerBossJob  implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			timerBossContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		
	}

	
	public void timerBossContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		timerBossDispose(id, nextTime);
	}
	
	public void timerBossDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(1);
		copySceneActivityPo.setBeginTime(System.currentTimeMillis());
		copySceneActivityPo.setBeginTimeNext(nextTime);	
		PrintUtil.print("TimerBossJob() " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		GlobalCache.checkTimerBossFresh();
	}
}
