
package com.games.mmo.po;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.MailService;
import com.games.mmo.type.ItemType;
import com.games.mmo.util.LogUtil;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;
	/**
	 *
	 * 类功能: 邮件
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_mail")
	public class MailPo extends BaseUserDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*发送时间无备住
	**/

	private Long sendTime;
	/**
	*发送者名字无备住
	**/

	private String senderName;
	/**
	*发送者角色编号无备住
	**/

	private Integer senderRoleId;
	/**
	*标题无备住
	**/

	private String mTitle;
	/**
	*类型无备住
	**/

	private Integer mType;
	/**
	*主体无备住
	**/

	private String mText;
	/**
	*是否已阅无备住
	**/

	private Integer isRead;
	/**
	*接受者编号无备住
	**/

	private Integer receiverRoleId;

	/**
	 * 管理后台邮件发送时间
	 */
	private Long startTime;
	
	/**
	 *  管理后台邮件结束时间
	 */
	private Long endTime;
	
	/**
	 * 管理后台发送人
	 */
	private String adminName;
	
	/**
	*附件无备住
	**/
	public List<IdNumberVo3> awardPos;
	
	
	//以上为前端需要关心的字段
	@JSONField(serialize=false)
	private String attaches;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="send_time")
	 public Long getSendTime() {
		return this.sendTime;
	}
	public void setSendTime(Long sendTime) {
		changed("send_time",sendTime,this.sendTime);
		this.sendTime = sendTime;
	}

	@Column(name="sender_name")
	 public String getSenderName() {
		return this.senderName;
	}
	public void setSenderName(String senderName) {
		changed("sender_name",senderName,this.senderName);
		this.senderName = senderName;
	}

	@Column(name="sender_role_id")
	 public Integer getSenderRoleId() {
		return this.senderRoleId;
	}
	public void setSenderRoleId(Integer senderRoleId) {
		changed("sender_role_id",senderRoleId,this.senderRoleId);
		this.senderRoleId = senderRoleId;
	}

	@Column(name="m_title")
	 public String getMTitle() {
		return this.mTitle;
	}
	public void setMTitle(String mTitle) {
		changed("m_title",mTitle,this.mTitle);
		this.mTitle = mTitle;
	}

	@Column(name="m_type")
	 public Integer getMType() {
		return this.mType;
	}
	public void setMType(Integer mType) {
		changed("m_type",mType,this.mType);
		this.mType = mType;
	}

	@Column(name="m_text")
	 public String getMText() {
		return this.mText;
	}
	public void setMText(String mText) {
		changed("m_text",mText,this.mText);
		this.mText = mText;
	}

	@Column(name="is_read")
	 public Integer getIsRead() {
		return this.isRead;
	}
	public void setIsRead(Integer isRead) {
		changed("is_read",isRead,this.isRead);
		this.isRead = isRead;
	}

	@Column(name="receiver_role_id")
	 public Integer getReceiverRoleId() {
		return this.receiverRoleId;
	}
	public void setReceiverRoleId(Integer receiverRoleId) {
		changed("receiver_role_id",receiverRoleId,this.receiverRoleId);
		this.receiverRoleId = receiverRoleId;
	}

	@Column(name="attaches")
	 public String getAttaches() {
		return this.attaches;
	}
	public void setAttaches(String attaches) {
		changed("attaches",attaches,this.attaches);
		this.attaches = attaches;
	}
	
	

	@Column(name="start_time")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		changed("start_time",startTime,this.startTime);
		this.startTime = startTime;
	}
	
	@Column(name="end_time")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		changed("end_time",endTime,this.endTime);
		this.endTime = endTime;
	}
	@Column(name="admin_name")
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		changed("admin_name",adminName,this.adminName);
		this.adminName = adminName;
	}
	public static MailPo findEntity(Integer id){
		return findRealEntity(MailPo.class,id);
	}
	
	
	
	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loaded =true;
			awardPos = IdNumberVo3.createList(attaches);
		}
	}
	
	
	@Override
	public void saveData() {
		setAttaches(IdNumberVo3.createStr(awardPos, "|", ","));
	}
	
	
	public List<IdNumberVo2> obtainAttach() {
		List<IdNumberVo2> list =new ArrayList<IdNumberVo2>();
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		RolePo rolePo = RolePo.findEntity(getReceiverRoleId());
//		for(int i = 0; i < awardPos.size(); i++){
//			checkService.checkExistItemPo(awardPos.get(i).getInt2());
//		}
		for (int i = 0; i < awardPos.size(); i++) {
			IdNumberVo3 inv2 = awardPos.get(i);
			if(inv2.getInt1().intValue() == 2){
				EqpPo eqpPo = EqpPo.findEntity(inv2.getInt2());
				if(eqpPo != null){
					list.add(new IdNumberVo2(2,eqpPo.itemPo().getId(), inv2.getInt3()));						
					rolePo.addEquip(eqpPo.getId(), eqpPo.getBindStatus());
					LogUtil.writeLog(rolePo,1, 5, eqpPo.getId(), 1, GlobalCache.fetchLanguageMap("key2355"), "");
				}
			}else if(inv2.getInt1().intValue() == 1){
				ItemPo itemPo = ItemPo.findEntity(inv2.getInt2());
				if(itemPo != null){
					list.add(new IdNumberVo2(1,itemPo.getId(), inv2.getInt3()));
					rolePo.addItem(itemPo.getId(),inv2.getInt3(),inv2.getInt4());
					LogUtil.writeLog(rolePo,1, 5, itemPo.getId(), inv2.getInt3(), GlobalCache.fetchLanguageMap("key2356"), "");
				}
			}
		}
		awardPos.clear();
		saveData();
		mailService.syncMail(this);
		return list;
	}
	@Override
	public String toString() {
		return "MailPo [id=" + id + ", sendTime=" + sendTime + ", senderName="
				+ senderName + ", senderRoleId=" + senderRoleId + ", mTitle="
				+ mTitle + ", mType=" + mType + ", mText=" + mText
				+ ", isRead=" + isRead + ", receiverRoleId=" + receiverRoleId
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", adminName=" + adminName + ", attaches=" + attaches + "]";
	}
	
	
}
