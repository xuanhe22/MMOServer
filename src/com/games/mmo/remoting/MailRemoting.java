/**
 *
 */
package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.dao.MailDAO;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.MailOfOneRolePo;
import com.games.mmo.po.MailPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.MailService;
import com.games.mmo.service.RoleService;
import com.games.mmo.service.ServerService;
import com.games.mmo.task.MailSendTask;
import com.games.mmo.util.BackendUtil;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.SimpleMailHeadVo;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.text.TextUtil;
import com.storm.lib.exception.InvalidParamException;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 类功能: 邮件请求
 * @author johnny
 * @version 2011-1-4
 */
@Service
public class MailRemoting extends BaseRemoting{
	@Autowired
	private RoleService roleService;
	@Autowired
	private MailService mailService;
	@Autowired
	private CheckService checkService;
	@Autowired
	private MailDAO mailDAO;
	/**
	 *
	 * 方法功能:返回邮件列表 
	 * 更新时间:2011-1-4, 作者:johnny
	 * @return [0]List<SimpleMailHeadVo>
	 */
	public Object mailLoadAll(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		rolePo.checkMailEndTimeRemove();
//		List<IdNumberVo2> awardPos = new ArrayList<IdNumberVo2>();
		List<SimpleMailHeadVo> list = mailService.mailLoadAll(rolePo);
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 *
	 * 方法功能:得到具体邮件 [0]mailPo [1]rolePo.getMailUnread()
	 * 更新时间:2011-1-4, 作者:johnny
	 * @param mailId
	 * @return [0]=MailPo [1]hasUnreadMail (short)
	 */
	public Object mailLoadDetail(Integer mailId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		MailPo mailPo = mailService.getMail(mailId);
		ExceptionUtil.checkIfNone(mailPo);
		if(mailPo.getReceiverRoleId().intValue()!=rolePo.getId()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key153"));
		}
		List<IdNumberVo2> list = new ArrayList<IdNumberVo2>(); 
		List<EqpPo> eqpPoList = new ArrayList<EqpPo>();
		
		
		if(mailPo.awardPos != null && mailPo.awardPos.size() != 0){
			for(IdNumberVo3 inv2 : mailPo.awardPos){
				if(inv2.getInt1().intValue() == 2){
					EqpPo eqpPo = EqpPo.findEntity(inv2.getInt2());
					if(eqpPo != null){
						eqpPoList.add(eqpPo);
						list.add(new IdNumberVo2(eqpPo.getId(),eqpPo.itemPo().getId(), inv2.getInt3()));
					}
				}else if(inv2.getInt1().intValue() == 1){
					ItemPo itemPo = ItemPo.findEntity(inv2.getInt2());
					if(itemPo != null){
						list.add(new IdNumberVo2(0,itemPo.getId(), inv2.getInt3()));
					}
				}
			}			
		}
		mailPo.setIsRead(1);
		mailService.syncMail(mailPo);
		mailService.syncRoleRead(rolePo);
		rolePo.sendUpdateClientMailUnRead();
		SessionUtil.addDataArray(mailPo);
		SessionUtil.addDataArray(list);
		SessionUtil.addDataArray(eqpPoList);
		LogUtil.writeLog(rolePo, 222,mailPo.getSenderRoleId(), 0, 0, GlobalCache.fetchLanguageMap("key2485")+mailPo.getMText(), "");
		return SessionType.MULTYPE_RETURN;

	}

	/**
	 *
	 * 方法功能:删除邮件
	 * 更新时间:2011-1-4, 作者:johnny
	 * @param _mailIds
	 * @return [0]List<SimpleMailHeadVo> [1]rolePo.getMailUnread()
	 */
	public Object mailDeleteByMulti(String mailIds){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		Integer[] mailIdList=StringUtil.getSpliteInts(mailIds, ",");;
		List<MailPo> mails = new ArrayList<MailPo>();
		for (int i = 0; i < mailIdList.length; i++) {
			MailPo mailPo = mailService.getMail(mailIdList[i]);
			ExceptionUtil.checkIfNone(mailPo);
			if(mailPo.awardPos.size()>0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key154"));
			}
			if(mailPo.getReceiverRoleId().intValue()!=rolePo.getId()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key155"));
			}			
			mails.add(mailPo);
		}
		for (int i = 0; i < mails.size(); i++) {
			MailPo mailPo = mails.get(i);
			mailService.removeMail(mailPo,false);
			//没有发送者
		}
		mailService.syncRoleRead(rolePo);
		rolePo.sendUpdateClientMailUnRead();
		
		SessionUtil.addDataArray(mailService.mailLoadAll(rolePo));
//		SessionUtil.addDataArray(rolePo.getMailUnread());
		return SessionType.MULTYPE_RETURN;
	}


	/**
	 *
	 * 方法功能:发送邮件 
	 * 更新时间:2011-1-4, 作者:johnny
	 * @param mailTitle
	 * @param roleName
	 * @param content
	 * @return [0]List<SimpleMailHeadVo>
	 */
	public List<SimpleMailHeadVo> mailSend(Integer targetRoleId,String content){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		checkService.checkExisRolePo(targetRoleId);
		RolePo targetRole = RolePo.findEntity(targetRoleId);
	
		CheckUtil.checkContianFiltedWord(content,false,null);
		CheckUtil.checkValidString(content, 5000);
		if(StringUtil.isEmpty(content)){
			TextUtil.throwConfirmParamCode("r125");
		}
		if(targetRoleId.intValue()==rolePo.getId().intValue()){
			TextUtil.throwConfirmParamCode("r126");
		}
		mailService.sendPrivateMail(rolePo,targetRoleId,"",content,null);
		LogUtil.writeLog(rolePo, 221,targetRoleId, 0, 0, GlobalCache.fetchLanguageMap("key2487")+content, "");
		return mailService.mailLoadAll(rolePo);
	}

	/**
	 *
	 * 方法功能:获得附件道具
	 * 更新时间:2011-2-21, 作者:johnny
	 * @param mailId
	 * @return [0]mailPo
	 */
	public Object mailObtainAttach(Integer mailId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		BaseDAO baseDAO = (BaseDAO) BeanUtil.getBean("baseDAO");
		checkService.checkExisMailPo(mailId);
		MailPo mailPo = mailService.getMail(mailId);
		if(mailPo!=null && mailPo.awardPos!= null && mailPo.awardPos.size()!=0){
			rolePo.checkItemPackFull(mailPo.awardPos.size());			
		}
		if(mailPo.getReceiverRoleId().intValue()!=rolePo.getId()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key153"));
		}
		CheckUtil.checkIsNull(mailPo);

		List<IdNumberVo2> list = mailPo.obtainAttach();
		StringBuilder sb = new StringBuilder();
		for(IdNumberVo2 idNumberVo2 : list){
			ItemPo itemPo = ItemPo.findEntity(idNumberVo2.getInt2());
			if(itemPo != null){
				sb.append(itemPo.getName()).append(":").append(idNumberVo2.getInt3()).append(";");				
			}
		}
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		
		mailService.syncRoleRead(rolePo);
		rolePo.sendUpdateClientMailUnRead();
		SessionUtil.addDataArray(mailPo);
		SessionUtil.addDataArray(list);
		SessionUtil.addDataArray(new ArrayList<EqpPo>());
		LogUtil.writeLog(rolePo, 223,0, 0, 0, GlobalCache.fetchLanguageMap("key2486")+sb.toString(), "");
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键提取附件
	 * @return
	 */
	public Object mailOneKeyAttach(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		List<IdNumberVo2> list =new ArrayList<IdNumberVo2>();
		List<SimpleMailHeadVo> simpleMailHeadVos=mailService.mailLoadAll(rolePo);
		int total = 0;
		for (SimpleMailHeadVo simpleMailHeadVo : simpleMailHeadVos) {
			if(simpleMailHeadVo.getAttaches()!=null){
				MailPo mailPo = MailPo.findRealEntity(MailPo.class, simpleMailHeadVo.getMailId());
				if(mailPo!=null && mailPo.awardPos!= null && mailPo.awardPos.size()!=0){
					total+=	mailPo.awardPos.size();	
				}
			}
		}
		rolePo.checkItemPackFull(total);
		
		for (SimpleMailHeadVo simpleMailHeadVo : simpleMailHeadVos) {
			if(simpleMailHeadVo.getAttaches()!=null){
				MailPo mailPo = MailPo.findRealEntity(MailPo.class, simpleMailHeadVo.getMailId());
				list.addAll(mailPo.obtainAttach());
			}
		}
		rolePo.sendUpdateSkillPoint();
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		simpleMailHeadVos=mailService.mailLoadAll(rolePo);
		mailService.syncRoleRead(rolePo);
		rolePo.sendUpdateClientMailUnRead();
		SessionUtil.addDataArray(simpleMailHeadVos);
		SessionUtil.addDataArray(list);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键删除
	 * @return 
	 */
	public Object mailOneKeyDelete(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		List<SimpleMailHeadVo> simpleMailHeadVos=mailService.mailLoadAll(rolePo);
		
		for (SimpleMailHeadVo simpleMailHeadVo : simpleMailHeadVos) {
			MailPo mailPo = mailService.getMail(simpleMailHeadVo.getMailId());
			if(mailPo.awardPos.size()>0){
				continue;
			}
			mailService.removeMail(mailPo,false);
		}
		simpleMailHeadVos=mailService.mailLoadAll(rolePo);
		mailService.syncRoleRead(rolePo);
		rolePo.sendUpdateClientMailUnRead();
		SessionUtil.addDataArray(simpleMailHeadVos);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 有新邮件
	 * @return
	 */
	public Object clientNewMail(){
		return null;
	}

	public Object send(String mailTitle,String mailText,Long startTime,Long endTime,String mailAttach,Integer mailGroup,String mailNames,Integer minLv,Integer minVipLv,Integer maxLv,Integer maxVipLv,String ChannelsDiv,String server_ids,String adminName){
		System.out.println("MailRemoting.send() "+"mailTitle="+mailTitle+"; mailText="+mailText+"; startTime="+startTime+"; endTime="+endTime+"; mailAttach="+mailAttach
				+"; mailGroup="+mailGroup+"; mailNames="+mailNames+"; minLv="+minLv+"; minVipLv="+minVipLv+"; maxLv="+maxLv+"; maxVipLv="+maxVipLv+"; ChannelsDiv="+ChannelsDiv+"; server_ids="+server_ids+"; adminName="+adminName);
		if(mailAttach==null || mailAttach.equals("")){
			mailAttach=null;
		}
		Object [] result = new Object [2];
		result[0]= GlobalCache.fetchLanguageMap("key156");
		result[1]= 1;
		try {
			List<Integer> roleIds = new ArrayList<Integer>();
			if(mailGroup==1){//角色名称
				roleIds = BackendUtil.fetchRoleIds(mailNames,minLv,minVipLv,maxLv,maxVipLv);
			} else if (mailGroup == 4) {//角色ID
				mailNames=mailNames.replaceAll("，", ",");
				for (String id : mailNames.split(",")) {
					int roleId = Integer.valueOf(id);
					RolePo rolePo = RolePo.findEntity(roleId);
					if (rolePo != null) {
						roleIds.add(roleId);
					}else{
						ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key303")+roleId);
					}
				}
			} else if(mailGroup==2){//渠道
				roleIds = roleService.fetchAllChannelRoleIds(ChannelsDiv, minLv,minVipLv,maxLv,maxVipLv);
			}else if(mailGroup==5){//全服
				roleIds = roleService.fetchAllRoleIds(minLv,minVipLv,maxLv,maxVipLv);
			}
			long currentTime = System.currentTimeMillis();
			if (currentTime < startTime) {
				Timer timer = new Timer();
				timer.schedule(new MailSendTask(mailService, roleIds, mailTitle, mailText, mailAttach, startTime, endTime, adminName), startTime - currentTime);
			} else if (currentTime < endTime) {
				mailService.sendDirectSystemMail(roleIds, mailTitle, mailText, mailAttach, startTime, endTime, adminName);
			}
//			mailService.sendDirectSystemMail(roleIds, mailTitle, mailText, mailAttach, startTime, endTime, adminName);
		} catch (Exception e) {
			if(e instanceof InvalidParamException){
				result[0]=((InvalidParamException) e).getExpMessage();
				result[1]= 0;
			} else {
				result[0]=GlobalCache.fetchLanguageMap("key158");
				result[1]= 0;
			}
		}
		return result;
	}
	
	public Object queryList(Long startTime,Long endTime,String mailTitle,String adminName){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		StringBuilder sb = new StringBuilder();
		sb.append(" where 1=1 ");
		sb.append(" and start_time>"+startTime);
		sb.append(" and end_time<"+endTime);
		if(!StringUtil.isEmpty(mailTitle)){
			sb.append(" and m_title='"+mailTitle).append("' ");
		}
		if(!StringUtil.isEmpty(adminName)){
			sb.append(" and admin_name='"+adminName).append("' ");
		}
		String orderBy=" order by start_time desc";
		String countSql = "select count(id) from u_po_mail";
		
		int count = 0;
		SqlRowSet rs =BaseDAO.instance().jdbcTemplate.queryForRowSet(countSql);
		if (rs.next())
			count += rs.getInt(1);
		if(count != 0){
			if(count <= 50){
				String listSql = "select id,m_title,m_text,attaches,admin_name,1 as total_num from "+BaseStormSystemType.USER_DB_NAME+".u_po_mail ";
				String sql=listSql+sb.toString()+orderBy;
				list=BaseDAO.instance().jdbcTemplate.queryForList(sql);
			} else {
				String listSql = "select  id,m_title,m_text,attaches,admin_name,count(1) as total_num from "+BaseStormSystemType.USER_DB_NAME+".u_po_mail  ";
				String groupBy = " group by m_title,m_text,attaches,admin_name ";
				String sql=listSql+sb.toString()+groupBy+orderBy;
				list=BaseDAO.instance().jdbcTemplate.queryForList(sql);
			}
		}
		
		return list;
	}
	
	public Object singleDelete(Integer delcurrentItemId,String delMailTitle,String delAttaches,String delAdminName,Integer delTtotalNum,Long startTime,Long endTime,String mailTitle,String adminName){
		PrintUtil.print("singleDelete() delcurrentItemId="+delcurrentItemId+"; delMailTitle="+delMailTitle+"; delAttaches="+delAttaches+"; delAdminName="+delAdminName+"; delTtotalNum="+"; startTime="+startTime+"; endTime="+endTime+"; mailTitle="+mailTitle+"; adminName="+adminName);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(delTtotalNum.intValue()==1){
			String sql = "delete from "+BaseStormSystemType.USER_DB_NAME+".u_po_mail where id="+delcurrentItemId;
			BaseDAO.instance().execute(sql);	
		}else{
			StringBuilder sb = new StringBuilder();
			sb.append(" where 1=1 ");
			if(!StringUtil.isEmpty(delMailTitle)){
				sb.append(" and m_title='").append(delMailTitle).append("'");
			}
			if(!StringUtil.isEmpty(delAttaches)){
				sb.append(" and attaches='").append(delAttaches).append("'");
			}
			if(!StringUtil.isEmpty(delAdminName)){
				sb.append(" and admin_name='").append(delAdminName).append("'");
			}
			String sql = "delete from "+BaseStormSystemType.USER_DB_NAME+".u_po_mail "+sb.toString();
			PrintUtil.print("sql="+sql);
			BaseDAO.instance().execute(sql);
		}
		list= (List<Map<String, Object>>) queryList(startTime, endTime, mailTitle, adminName);
		return list;
	}
	
	public Object allDelete(String ids,Long startTime,Long endTime,String mailTitle,String adminName){
//		System.out.println("allDelete() ids="+ids+"; startTime="+startTime+"; endTime="+endTime+"; mailTitle="+mailTitle+"; adminName="+adminName);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(!StringUtil.isEmpty(ids)){
			String sql = "delete from "+BaseStormSystemType.USER_DB_NAME+".u_po_mail where id in ("+ids+")";
			PrintUtil.print("sql="+sql);
			BaseDAO.instance().execute(sql);
		}
		list= (List<Map<String, Object>>) queryList(startTime, endTime, mailTitle, adminName);
		return list;
	 }
//	@Autowired
//	private QuartzSchedulerTemplate quartzSchedulerTemplate;
//	//搜索
	public void search() {
		ServerService serverService = new ServerService();
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		String keySql = "select DISTINCT(channel_key) as channel_key from "
				+ BaseStormSystemType.USER_DB_NAME + ".u_po_user";
		List<String> channelKeys = BaseDAO.instance().jdbcTemplate
				.queryForList(keySql, String.class);
		serverService.summaryRemain(calendar.getTimeInMillis(), channelKeys);
//		System.err.println("end");
	}
	public Object searchAll(Long startTime, Long endTime) {
		return mailDAO.findMailList(startTime, endTime);
	}
	public Object searchByRoleId(Long startTime, Long endTime, Integer value) {
		List<MailOfOneRolePo> result = new ArrayList<MailOfOneRolePo>();
		for (MailPo mailPo : mailDAO.findMailList(startTime, endTime, value)) {
			result.add(new MailOfOneRolePo(mailPo));
		}
		return result;
	}
	public Object searchByRoleName(Long startTime, Long endTime, String value) {
		List<MailOfOneRolePo> result = new ArrayList<MailOfOneRolePo>();
		for (MailPo mailPo : mailDAO.findMailList(startTime, endTime, value)) {
			result.add(new MailOfOneRolePo(mailPo));
		}
		return result;
	}
}
