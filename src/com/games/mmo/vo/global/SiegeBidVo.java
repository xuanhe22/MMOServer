package com.games.mmo.vo.global;

import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.GlobalPo;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

public class SiegeBidVo extends BasePropertyVo{

	public String ownerGuildLeaderName;
	public String ownerGuildName;
	public Integer ownerGuidId=0;
	
	public Integer guildId1=0;
	public String guildName1;
	public Integer maxBid1=0;
	
	public Integer guildId2=0;
	public String guildName2;
	public Integer maxBid2=0;
	
	public Integer guildId3=0;
	public String guildName3;
	public Integer maxBid3=0;
	
	public String ownerGuildLeaderWeaponAvatar="";
	public String ownerGuildLeaderModelAvatar="";
	public String ownerGuildLeaderWingAvatar="";
	public String awards;
	public Integer ownerGuildLeaderId;
	public Integer lastGuildId;
	public Integer lastGuildLeaderId;
	
	public static SiegeBidVo instance;
	
	public SiegeBidVo(){
		instance=this;
		init();
	}
	
	public void init(){
		if(XmlCache.xmlFiles.constantFile!=null){
			awards= XmlCache.xmlFiles.constantFile.guild.guildwar.territory.get(5).award;
		}

		guildName1=GlobalCache.fetchLanguageMap("key304");
		guildName2=GlobalCache.fetchLanguageMap("key304");
		guildName3=GlobalCache.fetchLanguageMap("key304");
		ownerGuildLeaderName=GlobalCache.fetchLanguageMap("key305");
		ownerGuildName=GlobalCache.fetchLanguageMap("key306");
		ownerGuidId=0;
		guildId1=0;
		guildId2=0;
		guildId3=0;
		maxBid1=10000;
		maxBid2=10000;
		maxBid3=10000;
		ownerGuildLeaderWeaponAvatar="";
		ownerGuildLeaderModelAvatar="";
		ownerGuildLeaderWingAvatar="";
		ownerGuildLeaderId=0;
		lastGuildId=0;
		lastGuildLeaderId=0;
	}
	
	public void init2(){
		guildName1=GlobalCache.fetchLanguageMap("key304");
		guildName2=GlobalCache.fetchLanguageMap("key304");
		guildName3=GlobalCache.fetchLanguageMap("key304");
		guildId1=0;
		guildId2=0;
		guildId3=0;
		maxBid1=10000;
		maxBid2=10000;
		maxBid3=10000;
	}
	
	public void init3(){
		ownerGuildLeaderName=GlobalCache.fetchLanguageMap("key305");
		ownerGuildName=GlobalCache.fetchLanguageMap("key306");
		ownerGuidId=0;
		guildId1=0;
		guildId2=0;
		guildId3=0;
		maxBid1=10000;
		maxBid2=10000;
		maxBid3=10000;
		ownerGuildLeaderWeaponAvatar="";
		ownerGuildLeaderModelAvatar="";
		ownerGuildLeaderWingAvatar="";
	}
	
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{guildId1,
										   guildName1,
										   maxBid1,
										   guildId2,
										   guildName2,
										   maxBid2,
										   guildId3,
										   guildName3,
										   maxBid3,
										   ownerGuildLeaderName,
										   ownerGuildName,
										   ownerGuidId,
										   ownerGuildLeaderWeaponAvatar,
										   ownerGuildLeaderModelAvatar,
										   ownerGuildLeaderWingAvatar,
										   ownerGuildLeaderId,
										   lastGuildId,
										   lastGuildLeaderId};

	}
	@Override
	public void loadProperty(String val, String spliter) {
		if(val==null){
			return;
		}
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		guildId1=DBFieldUtil.fetchImpodInt(vals[0]);
		guildName1=DBFieldUtil.fetchImpodString(vals[1]);
		maxBid1=DBFieldUtil.fetchImpodInt(vals[2]);
		
		guildId2=DBFieldUtil.fetchImpodInt(vals[3]);
		guildName2=DBFieldUtil.fetchImpodString(vals[4]);
		maxBid2=DBFieldUtil.fetchImpodInt(vals[5]);
		
		guildId3=DBFieldUtil.fetchImpodInt(vals[6]);
		guildName3=DBFieldUtil.fetchImpodString(vals[7]);
		maxBid3=DBFieldUtil.fetchImpodInt(vals[8]);
		
		ownerGuildLeaderName=DBFieldUtil.fetchImpodString(vals[9]);
		ownerGuildName=DBFieldUtil.fetchImpodString(vals[10]);
		ownerGuidId=DBFieldUtil.fetchImpodInt(vals[11]);
		ownerGuildLeaderWeaponAvatar=DBFieldUtil.fetchImpodString(vals[12]);
		ownerGuildLeaderModelAvatar=DBFieldUtil.fetchImpodString(vals[13]);
		ownerGuildLeaderWingAvatar=DBFieldUtil.fetchImpodString(vals[14]);
		ownerGuildLeaderId=DBFieldUtil.fetchImpodInt(vals[15]);
		lastGuildId=DBFieldUtil.fetchImpodInt(vals[16]);
		lastGuildLeaderId=DBFieldUtil.fetchImpodInt(vals[17]);
	}

	public void save() {
		((BaseUserDBPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid)).saveData();
	}

	@Override
	public String toString() {
		return "SiegeBidVo [ownerGuildLeaderName=" + ownerGuildLeaderName
				+ ", ownerGuildName=" + ownerGuildName + ", ownerGuidId="
				+ ownerGuidId + ", guildId1=" + guildId1 + ", guildName1="
				+ guildName1 + ", maxBid1=" + maxBid1 + ", guildId2="
				+ guildId2 + ", guildName2=" + guildName2 + ", maxBid2="
				+ maxBid2 + ", guildId3=" + guildId3 + ", guildName3="
				+ guildName3 + ", maxBid3=" + maxBid3
				+ ", ownerGuildLeaderWeaponAvatar="
				+ ownerGuildLeaderWeaponAvatar
				+ ", ownerGuildLeaderModelAvatar="
				+ ownerGuildLeaderModelAvatar + ", ownerGuildLeaderWingAvatar="
				+ ownerGuildLeaderWingAvatar + ", ownerGuildLeaderId="
				+ ownerGuildLeaderId + ", lastGuildId=" + lastGuildId
				+ ", lastGuildLeaderId=" + lastGuildLeaderId + "]";
	}


	
	
}
