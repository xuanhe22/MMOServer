package com.games.backend.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.games.mmo.po.ForbidPo;
import com.storm.lib.component.entity.BaseDAO;

public class ForbidVo implements Serializable{
	
	public Integer id;
	public Integer roleId;
	
	public String roleName;
	
	public Long startTime;
	
	public Long endTime;
	
	public String note;
	
	public Integer type;
	
	public static List<ForbidVo> createFromPos() {
		List<ForbidVo> vos =new ArrayList<ForbidVo>();
		for (Object obj : BaseDAO.instance().getDBList(ForbidPo.class)) {
			vos.add(fromPo((ForbidPo)obj));
		}
		return vos;
	}

	public static ForbidVo fromPo(ForbidPo po) {
		ForbidVo vo =new ForbidVo();
		if(po != null){
			vo.endTime=po.getEndTime();
			vo.roleId=po.getRoleId();
			vo.roleName=po.getRoleName();
			vo.startTime=po.getStartTime();
			vo.note=po.getNote();
			vo.id=po.getId();
			vo.type=po.getType();
		}
		return vo;
	}
}
