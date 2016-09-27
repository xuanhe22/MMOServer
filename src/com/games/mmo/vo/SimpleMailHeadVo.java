/**
 *
 */
package com.games.mmo.vo;

import java.sql.Timestamp;
import java.util.Date;

import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.StringUtil;


/**
 * 类功能:
 * @author johnny
 * @version 2011-1-4
 */
public class SimpleMailHeadVo extends BaseVo {
	/**
	 * 邮件编号
	 */
	private Integer mailId;

	/**
	 * 邮件类型
	 */
	private Integer mailType;

	/**
	 * 邮件标题
	 */
	private String mailTitle;

	/**
	 * 邮件时间
	 */
	private Long mailTime;

	/**
	 * 邮件是否已读
	 */
	private Integer mailIsRead;

	/**
	 * 发送者名字
	 */
	private String senderName;


	/**
	 * 邮件是否有附件
	 */
	private String attaches;

	/**
	 * 接收方ID
	 */
	private Integer receiverId;

	
	/**
	 * 真实标题
	 */
	private String realTitle;

	//id,MType,MTitle,isRead,attaches,sendTime
	public SimpleMailHeadVo(Integer id,Short MType,String mailTitle,Short isRead,String attaches,Date sendTime,String senderName,Integer receiverRoleId,String realTitle) {
		this.mailId=id;
		this.mailType=MType.intValue();
		this.mailTitle= mailTitle;
		this.mailIsRead=isRead.intValue();
		this.attaches=attaches;
		this.mailTime = sendTime.getTime();
		this.receiverId=receiverRoleId;
		this.senderName = senderName;
		this.realTitle=realTitle;
//			sendTime,senderRoleId,senderName,receiverRoleId

	}

	
	public SimpleMailHeadVo() {
		super();
	}


	/**
	 * @return the mailId
	 */
	public Integer getMailId() {
		return mailId;
	}

	/**
	 * @param mailId the mailId to set
	 */
	public void setMailId(Integer mailId) {
		this.mailId = mailId;
	}

	/**
	 * @return the mailType
	 */
	public Integer getMailType() {
		return mailType;
	}

	/**
	 * @param mailType the mailType to set
	 */
	public void setMailType(Integer mailType) {
		this.mailType = mailType;
	}

	/**
	 * @return the mailTitle
	 */
	public String getMailTitle() {
		return mailTitle;
	}

	/**
	 * @param mailTitle the mailTitle to set
	 */
	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	/**
	 * @return the mailTime
	 */
	public Long getMailTime() {
		return mailTime;
	}

	/**
	 * @param mailTime the mailTime to set
	 */
	public void setMailTime(Long mailTime) {
		this.mailTime = mailTime;
	}

	/**
	 * @return the mailIsRead
	 */
	public Integer getMailIsRead() {
		return mailIsRead;
	}

	/**
	 * @param mailIsRead the mailIsRead to set
	 */
	public void setMailIsRead(Integer mailIsRead) {
		this.mailIsRead = mailIsRead;
	}

	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}


	public String getAttaches() {
		return attaches;
	}


	public void setAttaches(String attaches) {
		this.attaches = attaches;
	}


	/**
	 * @return the receiverId
	 */
	public Integer getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId the receiverId to set
	 */
	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}


	public String getRealTitle() {
		return realTitle;
	}


	public void setRealTitle(String realTitle) {
		this.realTitle = realTitle;
	}


}
