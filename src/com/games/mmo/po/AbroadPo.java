package com.games.mmo.po;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.service.ChatService;
import com.games.mmo.task.AbroadJob;
import com.games.mmo.thread.SimpleAbroadThread;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.component.quartz.QuartzSchedulerTemplate;
import com.storm.lib.util.BeanUtil;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_abroad")
	public class AbroadPo extends BaseUserDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*公告时间时间表达式
	**/

	private String noticeTime;
	/**
	*公告内容中文内容
	**/

	private String noticeInfo;
	

	private Integer type;
	
	/**
	 * 1=base 2=advance quartz
	 */
	private Integer timeType;
	
	/**
	 * 起始时间
	 */
	private Long startTime;
	
	/**
	 * 结束时间
	 */
	private Long endTime;
	
	public Long lastExecuteTime=0l;
	
	private Integer repeatMinutes=0;
	
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 服务器
	 */
	private String servers;
	/**
	 * 创建时间
	 */
	private Long createTime=System.currentTimeMillis();

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="notice_time")
	 public String getNoticeTime() {
		return this.noticeTime;
	}
	public void setNoticeTime(String noticeTime) {
		changed("notice_time",noticeTime,this.noticeTime);
		this.noticeTime = noticeTime;
	}

	@Column(name="notice_info")
	 public String getNoticeInfo() {
		return this.noticeInfo;
	}
	public void setNoticeInfo(String noticeInfo) {
		changed("notice_info",noticeInfo,this.noticeInfo);
		this.noticeInfo = noticeInfo;
	}
	
	@Column(name="type")
	 public Integer getType() {
		return this.type;
	}
	public void setType(Integer type) {
		changed("type",type,this.type);
		this.type = type;
	}

	
	@Column(name="time_type")
	public Integer getTimeType() {
		return timeType;
	}
	public void setTimeType(Integer timeType) {
		changed("time_type",timeType,this.timeType);
		this.timeType = timeType;
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
	
	
	
	@Column(name="repeat_minutes")
	public Integer getRepeatMinutes() {
		return repeatMinutes;
	}
	public void setRepeatMinutes(Integer repeatMinutes) {
		changed("repeat_minutes",repeatMinutes,this.repeatMinutes);
		this.repeatMinutes = repeatMinutes;
	}
	
	@Column(name="lot_number")
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		changed("lot_number", lotNumber, this.lotNumber);
		this.lotNumber = lotNumber;
	}
	
	@Column(name="servers")
	public String getServers() {
		return servers;
	}
	public void setServers(String servers) {
		changed("servers", servers, this.servers);
		this.servers = servers;
	}
	
	@Column(name="create_time")
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		changed("create_time", createTime, this.createTime);
		this.createTime = createTime;
	}
	
	public static AbroadPo findEntity(Integer id){
		return findRealEntity(AbroadPo.class,id);
	}
	
	public static List<Integer> runningIds=new ArrayList<Integer>();
	
	
	public static void freshAllNotice() {
		QuartzSchedulerTemplate quartzSchedulerTemplate=(QuartzSchedulerTemplate) BeanUtil.getBean("quartzSchedulerTemplate");
		for (Integer id : runningIds) {
			String jobName1 = "AbroadJob_"+id;
			String triggerName1 = "TriggerAbroadJob_"+id;
			quartzSchedulerTemplate.deleteSchedulerJob(jobName1, triggerName1);
		}
		SimpleAbroadThread.simpleAbroads.clear();
		runningIds.clear();
		for(Object obj : BaseDAO.instance().getDBList(AbroadPo.class)){
			AbroadPo abroadPo= (AbroadPo) obj;
			if(abroadPo.getTimeType()==1){
				SimpleAbroadThread.simpleAbroads.add(abroadPo);
				abroadPo.lastExecuteTime=System.currentTimeMillis();
			}
			else if(abroadPo.getTimeType()==2){
				String jobName1 = "AbroadJob_"+abroadPo.getId();
				String jobGroup1 = "TaskAbroadJob_"+abroadPo.getId();
				String triggerName1 = "TriggerAbroadJob_"+abroadPo.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, AbroadJob.class,abroadPo.getNoticeTime());
				runningIds.add(abroadPo.id);
			}

		}
	}
	public void executeAbroad() {
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		if(getType()==1){
			chatService.sendHorse(getNoticeInfo());
		}
		else if(getType()==2){
			chatService.sendSystemWorldChat(getNoticeInfo());
		}
		else{
			chatService.sendHorse(getNoticeInfo());
			chatService.sendSystemWorldChat(getNoticeInfo());
		}
	}

}
