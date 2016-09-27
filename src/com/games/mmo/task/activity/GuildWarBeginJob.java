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
import com.games.mmo.po.RolePo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildwar.Territory;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

/**
 * 公会领地战开始
 * @author Administrator
 *
 */
public class GuildWarBeginJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			beginGuildWarContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	public void beginGuildWarContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		beginGuildWarContextDispose(id, nextTime);
	}
	
	public void beginGuildWarContextDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(1);
		copySceneActivityPo.setBeginTime(System.currentTimeMillis());
		copySceneActivityPo.setBeginTimeNext(nextTime);
		for (Territory territory : XmlCache.xmlFiles.constantFile.guild.guildwar.territory) {
			if(territory.sceneid!=MapType.SIEGE_SCENE_MAP){
				FlagPo flagPo = FlagPo.findFlagBySceneId(territory.sceneid);
				flagPo.setFlagStatus(1);
			}
		}
		
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}

}
