package com.games.mmo.vo;

import com.games.mmo.po.RolePo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class GuildMemberVo extends BasePropertyVo {
	public Integer roleId;
	public String roleName;
	public Integer roleLv;
	public Integer roleBattlePower = 0;
	public Integer roleCareer;
	/**
	 *  1:会长；2:副会长; 3:长老; 4：会员
	 */
	public Integer guildPosition;
	/**
	 * 是否在线：1：在线 ； 0： 离线
	 */
	public Integer wasOnline;
	/** 公会战功 */
	public Integer guildHonor = 0;
	/** 公会总战功*/
	public Integer guildTatolHonor=0;
	
	/**
	 * 离线时间
	 */
	public String offlineTime = "0";

	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{roleId,roleName,roleLv,roleBattlePower,roleCareer,guildPosition,wasOnline,guildHonor,guildTatolHonor,offlineTime};
	}

	@Override
	public void loadProperty(String val,String spliter) {
		String[] vals = StringUtil.split(val,spliter);


		roleId=DBFieldUtil.fetchImpodInt(vals[0]);
		roleName=DBFieldUtil.fetchImpodString(vals[1]);
		roleLv=DBFieldUtil.fetchImpodInt(vals[2]);
		roleBattlePower=DBFieldUtil.fetchImpodInt(vals[3]);
		roleCareer=DBFieldUtil.fetchImpodInt(vals[4]);
		guildPosition=DBFieldUtil.fetchImpodInt(vals[5]);
		wasOnline=DBFieldUtil.fetchImpodInt(vals[6]);
		guildHonor=DBFieldUtil.fetchImpodInt(vals[7]);
		if(fetchProperyItems().length == vals.length){
			guildTatolHonor=DBFieldUtil.fetchImpodInt(vals[8]);	
			offlineTime=DBFieldUtil.fetchImpodString(vals[9]);
		}
	}
	
	/** 增加公会功勋*/
	public void adjustHonor(Integer num){
		if(num == null|| num < 0){
			return;
		}
		guildHonor = guildHonor + num.intValue();
		guildTatolHonor = guildTatolHonor+num.intValue();
	}

	@Override
	public String toString() {
		return "GuildMemberVo [roleId=" + roleId + ", roleName=" + roleName
				+ ", roleLv=" + roleLv + ", roleBattlePower=" + roleBattlePower
				+ ", roleCareer=" + roleCareer + ", guildPosition="
				+ guildPosition + ", wasOnline=" + wasOnline + ", guildHonor="
				+ guildHonor + ", guildTatolHonor=" + guildTatolHonor + "]";
	}
	



	


	
	
}
