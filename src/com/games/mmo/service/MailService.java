/**
 *
 */
package com.games.mmo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.annotations.IdBagBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.dao.MailDAO;
import com.games.mmo.po.MailPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.MailType;
import com.games.mmo.vo.SimpleMailHeadVo;
import com.games.mmo.vo.xml.ConstantFile.Global;
import com.storm.lib.base.BaseService;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 类功能: 邮件服务类
 * @author johnny
 * @version 2011-1-5
 */
@Service
public class MailService extends BaseService{

	@Autowired
	private RoleService roleService;
	@Autowired
	private MailDAO mailDAO;
	@Autowired
	private RoleTemplate roleTemplate;

	/**
	 * 方法功能:发送私人邮件
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param rolePo
	 * @param targetRole
	 * @param mailTitle
	 * @param content
	 */
	public void sendPrivateMail(RolePo rolePo, Integer targetRoleId,String mailTitle, String content, String awards) {
		MailPo mailPo = new MailPo();
		mailPo.setIsRead(0);
		mailPo.setMText(content);
		mailPo.setMTitle(mailTitle);
		mailPo.setMType(MailType.MAIL_TYPE_PRIVATE);
		mailPo.setReceiverRoleId(targetRoleId);
		mailPo.setSenderRoleId(rolePo.getId());
		mailPo.setSenderName(rolePo.getName());
		mailPo.setSendTime(System.currentTimeMillis());
		mailPo.awardPos=IdNumberVo3.createList(awards);
		mailDAO.sendMail(mailPo);
		notifyNewMail(targetRoleId);
	}


	/**
	 * 
	 * 方法功能:通知有新邮件到来
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param targetRoleId
	 */
	private void notifyNewMail(Integer targetRoleId) {
		if(roleTemplate.roleIsOnLine(targetRoleId)){
			RolePo rolePo = roleService.getRolePo(targetRoleId);
			rolePo.setMailUnread(1);
			rolePo.sendUpdateClientMailUnRead();
			
		}
	}


	/**
	 * 
	 * 方法功能:批量发送邮件
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param sendeName
	 * @param mailTitle
	 * @param content
	 * @param items
	 * @param roleId
	 */
	public void sendGroupSystemMail(String sendeName, String mailTitle, String content,String items,Integer roleId) {
		MailPo mailPo = new MailPo();
		mailPo.setIsRead(0);
		mailPo.setMText(content);
		mailPo.setMTitle(mailTitle);
		mailPo.setMType(MailType.MAIL_TYPE_SYSTEM);
		mailPo.setReceiverRoleId(roleId);
		mailPo.setSenderRoleId(null);
		mailPo.setSendTime(System.currentTimeMillis());
		mailPo.setSenderName(sendeName);
		mailPo.awardPos=IdNumberVo3.createList(items);
		mailDAO.sendMail(mailPo);
		notifyNewMail(roleId);
	}
	
	/**
	 * 方法功能:得到邮件列表
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param rolePo
	 * @return
	 */
	public List<SimpleMailHeadVo> mailLoadAll(RolePo rolePo) {
		return mailDAO.mailLoadAll(rolePo);
	}

	/**
	 * 方法功能:得到邮件
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param mailId
	 * @return
	 */
	public MailPo getMail(Integer mailId) {
		return mailDAO.getMail(mailId);
	}

	/**
	 * 方法功能:移除邮件
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param mailPo
	 * @param isSender
	 */
	public void removeMail(MailPo mailPo, boolean isSender) {
		mailDAO.remove(mailPo);
	}

	/**
	 * 方法功能:更新邮件
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param mailPo
	 */
	public void syncMail(MailPo mailPo) {
		mailDAO.syncToDB(mailPo);
	}

	/**
	 * 删除所有邮件
	 * @param rolePo
	 */
	public void deleteAllMail(RolePo rolePo) {
		List<SimpleMailHeadVo> list = mailLoadAll(rolePo);
		for(SimpleMailHeadVo po:list){
			if(null!=po){
				MailPo mailPo = getMail(po.getMailId());
				if(null!=mailPo){
					removeMail(mailPo, false);//收方是自己
					//removeMail(mailPo, true);//发方是自己
				}
			}
		}

	}

	/**
	 * 
	 * 方法功能:同步已经阅读
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param rolePo
	 */
	public void syncRoleRead(RolePo rolePo) {
//		int unreadMail= mailDAO.dBfind(string);
		Integer unreadMailCount = mailDAO.findMailCount(rolePo.getId());
		if(unreadMailCount!=null){
			rolePo.setMailUnread(unreadMailCount);
		}
		else{
			rolePo.setMailUnread(0);
		}

	}

	/**
	 * 
	 * 方法功能:发送群发邮件
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param roleIds
	 * @param sendMailTitle
	 * @param sendMailContent
	 * @param items
	 */
	public void sendGroupSystemMail(String senderName,List<Integer> roleIds,String sendMailTitle, String sendMailContent,String items,int type,Long startTime,Long endTime, String adminName) {
		mailDAO.sendGroupSystemMail(senderName,roleIds,sendMailTitle,sendMailContent,items,type,startTime,endTime,adminName);
		for(int i=0;i<roleIds.size();i++){
			try {
				notifyNewMail(roleIds.get(i));
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
		}
	}
	
	/**
	 * 
	 * 方法功能:发送群发邮件
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param roleIds
	 * @param items
	 */
	public void sendGroupSystemMail(String senderName,List<Integer> roleIds, String items,int type) {
		String sendMailTitle = GlobalCache.fetchLanguageMap("key237");
		String sendMailContent = GlobalCache.fetchLanguageMap("key238");
		mailDAO.sendGroupSystemMail(senderName,roleIds,sendMailTitle,sendMailContent,items,type,0l,0l,GlobalCache.fetchLanguageMap("key239"));
		for(int i=0;i<roleIds.size();i++){
			try {
				notifyNewMail(roleIds.get(i));
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
		}
	}
	
	public void sendAwardSystemMail(Integer roleId, String title,String content,List<IdNumberVo3> items) {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(roleId);
		mailDAO.sendGroupSystemMail(GlobalCache.fetchLanguageMap("key239"),ids,title,content,IdNumberVo3.createStr(items, "|", ","),MailType.MAIL_TYPE_SYSTEM,0l,0l,GlobalCache.fetchLanguageMap("key239"));
		notifyNewMail(roleId);
	}
	
	

	
	/**
	 * 
	 * 方法功能:发送群发邮件
	 * 更新时间:2014-6-27, 作者:johnny
	 * @param roleId
	 * @param sendMailTitle
	 * @param sendMailContent
	 * @param attach
	 */
	public void sendSystemMail(String senderName,Integer roleId,String sendMailTitle, String sendMailContent,String attach,int type) {
		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(roleId);
		sendGroupSystemMail(senderName,roleIds, sendMailTitle, sendMailContent, attach,type, 0l,0l,GlobalCache.fetchLanguageMap("key239"));
	}


	public void sendDirectSystemMail(List<Integer> roleIds, String title,String content, String attach,Long startTime, Long endTime, String adminName) {
		sendGroupSystemMail(GlobalCache.fetchLanguageMap("key239"),roleIds, title, content, attach,MailType.MAIL_TYPE_SYSTEM, startTime, endTime, adminName);
		
	}

}
