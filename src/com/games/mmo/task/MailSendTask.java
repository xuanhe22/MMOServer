package com.games.mmo.task;

import java.util.List;
import java.util.TimerTask;

import com.games.mmo.service.MailService;

public class MailSendTask extends TimerTask{
	
	private MailService mailService;
	private List<Integer> roleIds;
	private String attach;
	private String content;
	private String title;
	private long startTime;
	private long endTime;
	private String adminName;
	public MailSendTask(MailService mailService, List<Integer> roleIds, String title,String content, String attach,Long startTime, Long endTime, String adminName){
		this.mailService = mailService;
		this.roleIds = roleIds;
		this.attach = attach;
		this.content = content;
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.adminName = adminName;
	}
	@Override
	public void run() {
		mailService.sendDirectSystemMail(roleIds, title, content, attach, startTime, endTime, adminName);
	}

}
