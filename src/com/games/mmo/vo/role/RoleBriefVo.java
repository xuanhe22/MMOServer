package com.games.mmo.vo.role;

import com.games.mmo.po.EqpPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.type.ItemType;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.RankVo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class RoleBriefVo extends BasePropertyVo implements Comparable<RoleBriefVo> {

	private Integer roleId=0;
	
	private String roleIuid="";
	
	private String roleName="";
	
	private Integer roleLv=0;
	

	private String roleClothAvatar="0";

	private String roleWingAvatar="0";
	
	private String roleWeaponAvatar="0";
	
	private Integer career;

	public Long createTime = 0l;
	
	public Long lastLoginTime = 0l;

	public String removeTime ="0";
	
	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


	public String getRoleIuid() {
		return roleIuid;
	}


	public void setRoleIuid(String roleIuid) {
		this.roleIuid = roleIuid;
	}


	public String getRoleName() {
		return roleName;
	}


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	public Integer getRoleLv() {
		return roleLv;
	}


	public void setRoleLv(Integer roleLv) {
		this.roleLv = roleLv;
	}


	public String getRoleClothAvatar() {
		return roleClothAvatar;
	}


	public void setRoleClothAvatar(String roleClothAvatar) {
		this.roleClothAvatar = roleClothAvatar;
	}


	public String getRoleWingAvatar() {
		return roleWingAvatar;
	}


	public void setRoleWingAvatar(String roleWingAvatar) {
		this.roleWingAvatar = roleWingAvatar;
	}


	public String getRoleWeaponAvatar() {
		return roleWeaponAvatar;
	}


	public void setRoleWeaponAvatar(String roleWeaponAvatar) {
		this.roleWeaponAvatar = roleWeaponAvatar;
	}

	
	
	
	public Integer getCareer() {
		return career;
	}


	public void setCareer(Integer career) {
		this.career = career;
	}
	
	


	public String getRemoveTime() {
		return removeTime;
	}


	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
	}


	/**
	 * index;equipId;itemId;num;bindStatus;
	 */
	public Object[] fetchProperyItems() {
		return new Object[]{roleId,roleIuid,roleName,roleLv,roleClothAvatar,roleWingAvatar,roleWeaponAvatar,career,removeTime};
	}


	@Override
	public void loadProperty(String val,String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		setRoleId(DBFieldUtil.fetchImpodInt(vals[0]));
		setRoleIuid(DBFieldUtil.fetchImpodString(vals[1]));
		setRoleName(DBFieldUtil.fetchImpodString(vals[2]));
		setRoleLv(DBFieldUtil.fetchImpodInt(vals[3]));
		setRoleClothAvatar(DBFieldUtil.fetchImpodString(vals[4]));
		setRoleWingAvatar(DBFieldUtil.fetchImpodString(vals[5]));
		setRoleWeaponAvatar(DBFieldUtil.fetchImpodString(vals[6]));
		setCareer(DBFieldUtil.fetchImpodInt(vals[7]));
		setRemoveTime(DBFieldUtil.fetchImpodString(vals[8]));
	}


	public void syncFromRolePo(RolePo rolePo) {
		setRoleId(rolePo.getId());
		setRoleIuid(rolePo.getIuid());
		setRoleName(rolePo.getName());
		setRoleLv(rolePo.getLv());
		setCareer(rolePo.getCareer());
		
//		int[] results = rolePo.findAvatars(); 
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
		
		setRoleClothAvatar(commonAvatarVo.modelAvatar);
		setRoleWeaponAvatar(commonAvatarVo.weaponAvatar);
		setRoleWingAvatar(commonAvatarVo.wingAvatar);

	}

	
	

	@Override
	public String toString() {
		return "RoleBriefVo [roleId=" + roleId + ", roleIuid=" + roleIuid
				+ ", roleName=" + roleName + ", roleLv=" + roleLv
				+ ", roleClothAvatar=" + roleClothAvatar + ", roleWingAvatar="
				+ roleWingAvatar + ", roleWeaponAvatar=" + roleWeaponAvatar
				+ ", career=" + career + "]";
	}


	@Override
	public int compareTo(RoleBriefVo o) {
		long result = o.createTime.longValue() - createTime.longValue();
		return result>0 ? -1:1;
	}

}
