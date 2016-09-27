package com.games.mmo.task.activity;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.service.ChatService;
import com.games.mmo.type.ColourType;
import com.games.mmo.vo.global.SiegeBidVo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

public class GuildBidSiegeBeginJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			beginGuildBidSiegeContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

	public void beginGuildBidSiegeContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		beginGuildBidSiegeContextDispose(id, nextTime);
	}
	
	public void beginGuildBidSiegeContextDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(1);
		copySceneActivityPo.setBeginTime(System.currentTimeMillis());
		copySceneActivityPo.setBeginTimeNext(nextTime);
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
		SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
		siegeBidVo.init2();
		
//		System.out.println("竞标 开启：" +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+" " +copySceneActivityPo);
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
