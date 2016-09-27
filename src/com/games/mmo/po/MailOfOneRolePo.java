package com.games.mmo.po;

import java.io.Serializable;

import com.games.mmo.dao.RoleDAO;

public class MailOfOneRolePo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String receiveRoleName;
	
	private long sendTime;
	
	private long receiveTime;
	
	private String title;
	
	private String content;
	
	private String attachment;
	
	private int isRead;

	public MailOfOneRolePo(MailPo mailPo) {
		super();
		this.id = mailPo.getId();
		RolePo rolePo = (RolePo) RoleDAO.instance().getEntityPo(mailPo.getReceiverRoleId(), RolePo.class);
		this.receiveRoleName = rolePo.getName();
		this.sendTime = mailPo.getStartTime();
		this.receiveTime = mailPo.getEndTime();
		this.title = mailPo.getMTitle();
		this.content = mailPo.getMText();
		this.attachment = mailPo.getAttaches();
		this.setIsRead(mailPo.getIsRead());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceiveRoleName() {
		return receiveRoleName;
	}

	public void setReceiveRoleName(String receiveRoleName) {
		this.receiveRoleName = receiveRoleName;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}
	
	
}
