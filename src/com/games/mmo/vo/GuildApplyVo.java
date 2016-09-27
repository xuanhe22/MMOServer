package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class GuildApplyVo extends BasePropertyVo{
	public Integer roleId;
	public String roleName;
	public Integer roleLv;
	public Integer roleBattlePower;
	public Integer roleCareer;
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{roleId,roleName,roleLv,roleBattlePower,roleCareer};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		roleId=DBFieldUtil.fetchImpodInt(vals[0]);
		roleName=DBFieldUtil.fetchImpodString(vals[1]);
		roleLv=DBFieldUtil.fetchImpodInt(vals[2]);
		roleBattlePower=DBFieldUtil.fetchImpodInt(vals[3]);
		roleCareer=DBFieldUtil.fetchImpodInt(vals[4]);
	}
	@Override
	public String toString() {
		return "GuildApplyVo [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleLv=" + roleLv + ", roleBattlePower=" + roleBattlePower
				+ ", roleCareer=" + roleCareer + "]";
	}

}
