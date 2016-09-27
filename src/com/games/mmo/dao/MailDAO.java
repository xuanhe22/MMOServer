/**
 *
 */
package com.games.mmo.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.mmo.po.MailPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.vo.SimpleMailHeadVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.MemoryTemplate;
import com.storm.lib.type.BaseStormSystemType;

/**
 * 类功能: 邮件DAO
 * @author johnny
 * @version 2011-2-22
 */
@Service
public class MailDAO extends BaseDAO{
	@Autowired
	private MemoryTemplate memoryTemplate;
	/**
	 * 方法功能:发送邮件
	 * 更新时间:2011-2-22, 作者:johnny
	 * @param mailPo
	 */
	public void sendMail(MailPo mailPo) {
		mailPo.saveData();
		insert(mailPo);
	}


	/**
	 * 
	 * 方法功能:得到邮件列表
	 * 更新时间:2014-6-25, 作者:johnny
	 * @param rolePo
	 * @return
	 */
	public List<SimpleMailHeadVo> mailLoadAll(RolePo rolePo) {
		List<SimpleMailHeadVo> list = new ArrayList<SimpleMailHeadVo>();
		int roleId = rolePo.getId();
		List rows = jdbcTemplate.queryForList("SELECT id,m_type as mType,concat( left(m_text,15),'....' ) as mTitle,is_read as isRead,attaches,send_time as sendTime,sender_name as senderName,receiver_role_id as receiverRoleId,m_title as realTitle  FROM "+BaseStormSystemType.USER_DB_NAME+".u_po_mail   where receiver_role_id="+roleId+" ORDER BY is_read  asc,send_time desc");
		Iterator it = rows.iterator();      
		while(it.hasNext()) {
			Map userMap = (Map) it.next();   
			SimpleMailHeadVo simpleMailHeadVo = new SimpleMailHeadVo();
			simpleMailHeadVo.setMailId(((Long) userMap.get("id")).intValue());
			simpleMailHeadVo.setMailIsRead(Integer.valueOf(userMap.get("isRead").toString()));
			simpleMailHeadVo.setMailTime(((BigInteger) userMap.get("sendTime")).longValue());
			simpleMailHeadVo.setMailTitle((String) userMap.get("mTitle"));
			simpleMailHeadVo.setMailType(Integer.valueOf(userMap.get("mType").toString()));
			simpleMailHeadVo.setReceiverId(((Integer) userMap.get("receiverRoleId")).intValue());
			simpleMailHeadVo.setSenderName((String) userMap.get("senderName"));
			if(userMap.get("attaches")!=null){
				simpleMailHeadVo.setAttaches(((String)userMap.get("attaches")));
			}
			else{
				simpleMailHeadVo.setAttaches("");
			}
			if(userMap.get("realTitle")!=null){
				simpleMailHeadVo.setRealTitle((String)userMap.get("realTitle"));				
			}else{
				simpleMailHeadVo.setRealTitle("");
			}
			list.add(simpleMailHeadVo);
		} 
		return list;
	}

	
	/**
	 * 
	 * 方法功能:获取邮件
	 * 更新时间:2014-6-25, 作者:johnny
	 * @param mailId
	 * @return
	 */
	public MailPo getMail(Integer mailId) {
		return (MailPo) getEntityPo(mailId, MailPo.class);
	}

	
	/**
	 * 
	 * 方法功能:群发邮件
	 * 更新时间:2014-6-25, 作者:johnny
	 * @param roleIds
	 * @param sendMailTitle
	 * @param sendMailContent
	 * @param items
	 */
	public void sendGroupSystemMail(String senderName,List<Integer> roleIds,String sendMailTitle, String sendMailContent,String items,int type,Long startTime,Long endTime, String adminName) {
		memoryTemplate.trySendGroupSystemMail(senderName,roleIds,sendMailTitle,sendMailContent,items,type,startTime,endTime, adminName);
	}


	public Integer findMailCount(int roleId) {
		return queryIntForSql("SELECT count(id) FROM "+BaseStormSystemType.USER_DB_NAME+".u_po_mail where receiver_role_id="+roleId+" and is_read=0",new Object[0]);
	}
	
	public List<MailPo> findMailList(long startTime, long endTime) {
		return dBfind("from MailPo where startTime > " + startTime + " and endTime < " + endTime);
	}
	public List<MailPo> findMailList(long startTime, long endTime, String roleName) {
		Integer roleId = RoleDAO.instance().fetchRoleIdByRoleName(roleName);
		if (roleId == null) 
			return null;
		return findMailList(startTime, endTime, roleId);
	}
	public List<MailPo> findMailList(long startTime, long endTime, int roleId) {
		return dBfind("from MailPo where startTime > " + startTime + " and endTime < " + endTime + " and receiverRoleId = " + roleId);
	}
}
