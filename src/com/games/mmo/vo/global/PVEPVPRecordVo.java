package com.games.mmo.vo.global;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.type.CopySceneType;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

public class PVEPVPRecordVo extends BasePropertyVo{
	public Integer BloodSeekerMaxScore=0;
	public Integer PKKingMaxScore=0;
	public Integer DemonizationCrisisScore=0;
	public Integer ZaphieHaramRoomMaxScore=0;
	public String BloodSeekerMaxScoreName="";
	public String PKKingMaxScoreName="";
	public String DemonizationCrisisScoreName="";
	public String ZaphieHaramRoomMaxScoreName="";
	public String lastKingOfPKWinPlayerName="";
	
	public Integer killingTowerLastTopKillerFloor=0;
	public String killingTowerLastTopKillerName="";
	public Integer killingTowerLastTopKillerKillTotal=0;
	
	public static PVEPVPRecordVo instance=new PVEPVPRecordVo();
	
	public PVEPVPRecordVo(){
		instance=this;
		init();
	}
	
	public void init(){

	}
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{BloodSeekerMaxScore,PKKingMaxScore,DemonizationCrisisScore,ZaphieHaramRoomMaxScore,BloodSeekerMaxScoreName,PKKingMaxScoreName,DemonizationCrisisScoreName,ZaphieHaramRoomMaxScoreName,lastKingOfPKWinPlayerName,killingTowerLastTopKillerFloor,killingTowerLastTopKillerName,killingTowerLastTopKillerKillTotal};
	}
	@Override
	public void loadProperty(String val, String spliter) {
		if(val==null){
			return;
		}
		String[] vals = StringUtil.split(val,spliter);
		BloodSeekerMaxScore=DBFieldUtil.fetchImpodInt(vals[0]);
		PKKingMaxScore=DBFieldUtil.fetchImpodInt(vals[1]);
		DemonizationCrisisScore=DBFieldUtil.fetchImpodInt(vals[2]);
		ZaphieHaramRoomMaxScore=DBFieldUtil.fetchImpodInt(vals[3]);
		BloodSeekerMaxScoreName=DBFieldUtil.fetchImpodString(vals[4]);
		PKKingMaxScoreName=DBFieldUtil.fetchImpodString(vals[5]);
		DemonizationCrisisScoreName=DBFieldUtil.fetchImpodString(vals[6]);
		ZaphieHaramRoomMaxScoreName=DBFieldUtil.fetchImpodString(vals[7]);
		lastKingOfPKWinPlayerName=DBFieldUtil.fetchImpodString(vals[8]);
		if(vals.length>9){
			killingTowerLastTopKillerFloor=DBFieldUtil.fetchImpodInt(vals[9]);
			killingTowerLastTopKillerName=DBFieldUtil.fetchImpodString(vals[10]);
			killingTowerLastTopKillerKillTotal=DBFieldUtil.fetchImpodInt(vals[11]);
		}

	}

	public void checkHighLiveActivity(String name, Integer score,int type,int floor,int killcount) {
		if(type==CopySceneType.COPY_SCENE_TYPE_BloodSeekerBastionRoom){
			if(score>=BloodSeekerMaxScore){
				BloodSeekerMaxScore=score;
				BloodSeekerMaxScoreName=name;
				PVEPVPRecordVo.instance.save();
			}
		}
		if(type==CopySceneType.COPY_SCENE_TYPE_KING_OF_PK){
			if(score>=PKKingMaxScore){
				PKKingMaxScore=score;
				PKKingMaxScoreName=name;
				PVEPVPRecordVo.instance.save();
			}
		}
		if(type==CopySceneType.COPY_SCENE_TYPE_DemonizationCrisisRoom){
			if(score>=BloodSeekerMaxScore){
				DemonizationCrisisScore=score;
				DemonizationCrisisScoreName=name;
				PVEPVPRecordVo.instance.save();
			}
		}
		if(type==CopySceneType.COPY_SCENE_TYPE_ZaphieHaramRoom){
			if(score>=ZaphieHaramRoomMaxScore){
				ZaphieHaramRoomMaxScore=score;
				ZaphieHaramRoomMaxScoreName=name;
				PVEPVPRecordVo.instance.save();
			}
		}
		if(type==CopySceneType.COPY_SCENE_TYPE_KILLING_TOWER){
			killingTowerLastTopKillerFloor=floor;
			killingTowerLastTopKillerKillTotal=killcount;
			killingTowerLastTopKillerName=name;
			PVEPVPRecordVo.instance.save();
		}
	}

	public void save() {
		((BaseUserDBPo)GlobalPo.keyGlobalPoMap.get(GlobalPo.keyPVEPVPRecordVo)).saveData();
	}
}
