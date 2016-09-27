package com.games.backend.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.games.mmo.po.AbroadPo;
import com.games.mmo.po.BlockPo;
import com.storm.lib.component.entity.BaseDAO;

public class AbroadVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer id;
	/**
	*公告时间时间表达式
	**/

	public String noticeTime;
	/**
	*公告内容中文内容
	**/

	public String noticeInfo;
	

	public Integer type;
	
	/**
	 * 1=base 2=advance quartz
	 */
	public Integer timeType;
	
	/**
	 * 起始时间
	 */
	public Long startTime;
	
	/**
	 * 结束时间
	 */
	public Long endTime;
	
	
	public Integer repeatMinutes=0;

	public String lotNumber;

	public String servers;

	public Long createTime;

	public static List<AbroadVo> createFromPos() {
		List<AbroadVo> vos =new ArrayList<AbroadVo>();
		for (Object obj : BaseDAO.instance().getDBList(AbroadPo.class)) {
			vos.add(fromPo((AbroadPo)obj));
		}
		return vos;
	}

	public static AbroadVo fromPo(AbroadPo po) {
		AbroadVo vo =new AbroadVo();
		vo.noticeInfo=po.getNoticeInfo();
		vo.noticeTime=po.getNoticeTime();
		vo.type=po.getType();
		vo.timeType=po.getTimeType();
		vo.startTime=po.getStartTime();
		vo.endTime=po.getEndTime();
		vo.repeatMinutes=po.getRepeatMinutes();
		vo.id=po.getId();
		vo.lotNumber = po.getLotNumber();
		vo.servers = po.getServers();
		vo.createTime = po.getCreateTime();
		return vo;
	}
}
