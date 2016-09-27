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
import com.games.mmo.service.ChatService;
import com.games.mmo.type.ColourType;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * PK之王开始
 * @author Administrator
 *
 */
public class KillingTowerBeginJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			beginKillingTowerContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	public void beginKillingTowerContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		beginKillingTowerDispose(id, nextTime);
	}
	
	public void beginKillingTowerDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(1);
		copySceneActivityPo.setBeginTime(System.currentTimeMillis());
		copySceneActivityPo.setBeginTimeNext(nextTime);
		
		StringBuffer sb = new StringBuffer();
		sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key248"));
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		chatService.sendHorse(sb.toString());
		chatService.sendSystemWorldChat(sb.toString());
//		System.out.println("极限乱斗 开启：" +new Date().toLocaleString()+" " +copySceneActivityPo);
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
		
		
		
	}

}
