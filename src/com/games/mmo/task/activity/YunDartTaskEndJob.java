package com.games.mmo.task.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.type.MonsterType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 运镖任务结束
 * @author Administrator
 *
 */
public class YunDartTaskEndJob implements Job  {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			endYunDartTaskContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	public void endYunDartTaskContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		endYunDartTaskEndDispose(id, nextTime);
	}
	
	public void endYunDartTaskEndDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(0);
		copySceneActivityPo.setEndTime(System.currentTimeMillis());
		copySceneActivityPo.setEndTimeNext(nextTime);
//		System.out.println("运送物资任务 关闭：" +new Date().toLocaleString()+" " +copySceneActivityPo);

		
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
