package com.games.backend.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.games.mmo.po.BlockPo;
import com.games.mmo.po.LiveActivityPo;
import com.storm.lib.component.entity.BaseDAO;

public class BlockVo implements Serializable{

	public Integer id;
	private static final long serialVersionUID = 1L;

	public Integer roleId;
	
	public String roleName;
	
	public Long startTime;
	
	public Long endTime;

	public String note;
	
	public static List<BlockVo> createFromPos() {
		List<BlockVo> vos =new ArrayList<BlockVo>();
		for (Object obj : BaseDAO.instance().getDBList(BlockPo.class)) {
			vos.add(fromPo((BlockPo)obj));
		}
		return vos;
	}

	public static BlockVo fromPo(BlockPo po) {
		BlockVo vo =new BlockVo();
		vo.endTime=po.getEndTime();
		vo.roleId=po.getRoleId();
		vo.roleName=po.getRoleName();
		vo.startTime=po.getStartTime();
		vo.note=po.getNote();
		vo.id=po.getId();
		return vo;
	}
}
