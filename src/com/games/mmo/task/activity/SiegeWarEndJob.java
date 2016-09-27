package com.games.mmo.task.activity;

import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.TitlePo;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildwar.Territory;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 沙巴克结束
 * @author Administrator
 *
 */
public class SiegeWarEndJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			endSiegeWarEndContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	public void endSiegeWarEndContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		endSiegeWarEndContextDispose(id, nextTime);
	}
	
	public void endSiegeWarEndContextDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(0);
		copySceneActivityPo.setEndTime(System.currentTimeMillis());
		copySceneActivityPo.setEndTimeNext(nextTime);
		for (Territory territory : XmlCache.xmlFiles.constantFile.guild.guildwar.territory) {
			if(territory.sceneid==MapType.SIEGE_SCENE_MAP){
				FlagPo flagPo = FlagPo.findFlagBySceneId(territory.sceneid);
				flagPo.setFlagStatus(0);
				break;
			}
		}
		GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keySiegeBid);
		SiegeBidVo siegeBidVo = (SiegeBidVo) globalPo.valueObj;
		
		RolePo lastRole = RolePo.findEntity(siegeBidVo.lastGuildLeaderId);
		if(lastRole != null){
			lastRole.checkRemoveSpecialTitleVo(2000);
		}
		siegeBidVo.lastGuildId=siegeBidVo.ownerGuidId;
		siegeBidVo.lastGuildLeaderId=siegeBidVo.ownerGuildLeaderId;
		
		RolePo ownerRole = RolePo.findEntity(siegeBidVo.ownerGuildLeaderId);
		if(ownerRole != null){
			// 重新获取一次，怕出现意外情况，比如换城主，解散公会
			GuildPo guildPo = GuildPo.findEntity(ownerRole.getGuildId());
			if(guildPo != null){
				RolePo leaderRole = RolePo.findEntity(guildPo.getLeaderRoleId());
				if(leaderRole != null){
					siegeBidVo.ownerGuildLeaderName=guildPo.getLeaderRoleName();
					siegeBidVo.ownerGuildName=guildPo.getName();
					siegeBidVo.ownerGuidId=guildPo.getId();
					siegeBidVo.ownerGuildLeaderId=guildPo.getLeaderRoleId();
					CommonAvatarVo commonAvatarVo = CommonAvatarVo.build(
							leaderRole.getEquipWeaponId(), leaderRole.getEquipArmorId(),
							leaderRole.getFashion(),
							leaderRole.getCareer(), leaderRole.getWingWasHidden(), leaderRole.getWingStar(),leaderRole.hiddenFashions);
					siegeBidVo.ownerGuildLeaderWeaponAvatar=commonAvatarVo.weaponAvatar;
					siegeBidVo.ownerGuildLeaderModelAvatar=commonAvatarVo.modelAvatar;
					siegeBidVo.ownerGuildLeaderWingAvatar=commonAvatarVo.wingAvatar;
					TitlePo titlePo =  GlobalCache.titlePoMap.get(2000);
					leaderRole.addSpecialTitle(titlePo);
				}
			}else{
				siegeBidVo.init();
			}
		}
		siegeBidVo.save();
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
