package com.games.mmo.task;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.po.game.NoticePo;
import com.games.mmo.service.ChatService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

public class NoticeJob implements Job  {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			noticeContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}

	public void noticeContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		noticeDispose(id, nextTime);
	}
	
	public void noticeDispose(Integer id, Long nextTime){
		NoticePo noticePo = NoticePo.findEntity(id);
//		System.out.println("====="+noticePo.getNoticeInfo());
		if(noticePo != null){
			ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
			chatService.sendHorse(noticePo.getNoticeInfo());
		}
	}
}
