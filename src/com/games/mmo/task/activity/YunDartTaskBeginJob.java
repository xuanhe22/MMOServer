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
import com.games.mmo.service.ChatService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.MonsterType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 运镖任务开启
 * @author Administrator
 *
 */
public class YunDartTaskBeginJob  implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			beginYunDartTaskContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	public void beginYunDartTaskContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		beginYunDartTaskDispose(id, nextTime);
	}
	
	public void beginYunDartTaskDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(1);
		copySceneActivityPo.setBeginTime(System.currentTimeMillis());
		copySceneActivityPo.setBeginTimeNext(nextTime);
		StringBuffer sb = new StringBuffer();
		sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key251"));
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		chatService.sendHorse(sb.toString());
		chatService.sendSystemWorldChat(sb.toString());
//		System.out.println("运送物资任务 开启：" +new Date().toLocaleString()+" " +copySceneActivityPo);

		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime =0;
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId = -1;
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality = -1;
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
