package com.games.mmo.vo;

import java.util.List;

import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdStringVo;

public class RankVo extends BasePropertyVo implements Comparable<RankVo>{
	   public Integer id=0;
		/**
		 * 角色Id
		 */
	    public Integer roleId=0;
		/**
		 * 结束等级
		 */
	    public Integer roleLv=0;
	 	/**
	 	 * 职业
	 	 */
	    public Integer roleCareer=0;
	    /**
	     * 角色名称
	     */
		public String roleName="";
		/**
		 * 排名等级
		 */
		public Integer rankLv=0;
		/**
		 * 战力
		 */
		public Integer rolePower=0;
		/**
		 * 类型
		 */
		public Integer type=0;
		/**
		 * 金币
		 */
		public Integer rankGold=0;		
		/**
		 * 武器模型
		 */
		public String weaponAvatar="";
		/**
		 * 胸甲模型
		 */
		public String armorAvatar="";
		/**
		 * 翅膀模型
		 */
		public String wingAvatar= "933001";
		/**
		 * 翅膀星级
		 */
		public Integer wingStar = 0;
		
		/** doat当前等级 */
		public Integer towerCurrentLv=0;
		
		/** 宠物模型*/
		public String petAvatar="";
		
		/** 宠物Id*/
		public Integer petId=0;
		
		/** 公会Id */
		public Integer guildId=0;
		
		/** 公会名称*/
		public String guildName="";
		
		@Override
		public Object[] fetchProperyItems() {
			return new Object[]{id,roleId,roleLv,roleCareer,roleName,rankLv,rolePower,type,rankGold,weaponAvatar,armorAvatar,wingAvatar,wingStar};
		}
		@Override
		public void loadProperty(String val, String spliter) {
			String[] vals = StringUtil.split(val,spliter);
			id=DBFieldUtil.fetchImpodInt(vals[0]);
			roleId=DBFieldUtil.fetchImpodInt(vals[1]);
			roleLv=DBFieldUtil.fetchImpodInt(vals[2]);
			roleCareer=DBFieldUtil.fetchImpodInt(vals[3]);
			roleName=DBFieldUtil.fetchImpodString(vals[4]);
			rankLv=DBFieldUtil.fetchImpodInt(vals[5]);
			rolePower=DBFieldUtil.fetchImpodInt(vals[6]);
			type=DBFieldUtil.fetchImpodInt(vals[7]);
			
			rankGold=DBFieldUtil.fetchImpodInt(vals[8]);
			weaponAvatar=DBFieldUtil.fetchImpodString(vals[9]);
			armorAvatar=DBFieldUtil.fetchImpodString(vals[10]);
			wingAvatar=DBFieldUtil.fetchImpodString(vals[11]);
			wingStar=DBFieldUtil.fetchImpodInt(vals[12]);

		}

		@Override
		public int compareTo(RankVo o) {
			return o.rolePower-rolePower;
		}


		public void makeAvatars(CommonAvatarVo commonAvatarVo) {
			weaponAvatar=commonAvatarVo.weaponAvatar;
			wingAvatar=commonAvatarVo.wingAvatar;
			armorAvatar=commonAvatarVo.modelAvatar;
			
		}
		@Override
		public String toString() {
			return "RankVo [id=" + id + ", roleId=" + roleId + ", roleLv="
					+ roleLv + ", roleCareer=" + roleCareer + ", roleName="
					+ roleName + ", rankLv=" + rankLv + ", rolePower="
					+ rolePower + ", type=" + type + ", rankGold=" + rankGold
					+ ", weaponAvatar=" + weaponAvatar + ", armorAvatar="
					+ armorAvatar + ", wingAvatar=" + wingAvatar
					+ ", wingStar=" + wingStar + ", towerCurrentLv="
					+ towerCurrentLv + ", petAvatar=" + petAvatar + ", petId="
					+ petId + ", guildId=" + guildId + ", guildName="
					+ guildName + "]";
		}
		
		
}
