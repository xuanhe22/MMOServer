package com.games.mmo.task.activity;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 魔化危机结束
 * @author Administrator
 *
 */
public class MonsterCrisisEndJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			endMonsterCrisisContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

	public void endMonsterCrisisContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		endMonsterCrisisDispose(id, nextTime);
	}
	
	public void endMonsterCrisisDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(0);
		copySceneActivityPo.setEndTime(System.currentTimeMillis());
		copySceneActivityPo.setEndTimeNext(nextTime);
//		System.out.println("魔化危机 关闭：" +new Date().toLocaleString()+" " +copySceneActivityPo);
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
