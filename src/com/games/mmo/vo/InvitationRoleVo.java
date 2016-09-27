package com.games.mmo.vo;

import com.games.mmo.po.RolePo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class InvitationRoleVo extends BasePropertyVo {
	public int id;
	public String roleName;
	public int career;
	public int lv;
	public int vipLv;
	public int wasOnLine;
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{id,roleName,career,lv,vipLv,wasOnLine};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		id=DBFieldUtil.fetchImpodInt(vals[0]);
		roleName=DBFieldUtil.fetchImpodString(vals[1]);
		career=DBFieldUtil.fetchImpodInt(vals[2]);
		lv=DBFieldUtil.fetchImpodInt(vals[3]);
		vipLv=DBFieldUtil.fetchImpodInt(vals[4]);
		wasOnLine=DBFieldUtil.fetchImpodInt(vals[5]);
	}
	
	
	public void initProperty(RolePo rolePo){
		id=rolePo.getId();
		roleName = rolePo.getName();
		career= rolePo.getCareer();
		lv=rolePo.getLv();
		vipLv=rolePo.getVipLv();
		wasOnLine=rolePo.fetchRoleOnlineStatus()? 1:0;
	}
}
