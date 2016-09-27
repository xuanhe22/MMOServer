package com.games.mmo.task.activity;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.StringUtil;

public class GuildBidSiegeEndJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			endGuildBidSiegeContext(context);
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
	}
	
	public void endGuildBidSiegeContext(JobExecutionContext context){
		Trigger trigger = context.getTrigger();
		Date date = trigger.getNextFireTime();
		String  jobName = context.getJobDetail().getName();		
		List<String> list = StringUtil.getStringListByStr(jobName, "_");	
		int id = Integer.parseInt(list.get(1));
		long nextTime =0l;
		if(date != null){
			nextTime = date.getTime();
		}
		endGuildBidSiegeDispose(id, nextTime);
	}
	
	public void endGuildBidSiegeDispose(Integer id, Long nextTime){
		CopySceneActivityPo copySceneActivityPo = GlobalCache.mapCopySceneActivityPos.get(id);
		copySceneActivityPo.setActivityWasOpen(0);
		copySceneActivityPo.setEndTime(System.currentTimeMillis());
		copySceneActivityPo.setEndTimeNext(nextTime);
		
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild where bid_gold !=0 order by battle_power desc");
		Iterator it = rows.iterator();
		while(it.hasNext()) 
		{
			Map userMap = (Map) it.next();  
			Integer guildId  =Integer.valueOf(userMap.get("id").toString());
			if(guildId != null && guildId !=0)
			{
				GuildPo guildPo = GuildPo.findEntity(guildId);
				if(guildPo.getId().intValue() == 10){
				}
				if(guildPo != null){
					if(guildPo.getId().intValue() == SiegeBidVo.instance.guildId1){
						guildPo.consumeGuildBidGold(SiegeBidVo.instance.maxBid1);
					}
					if(guildPo.getId().intValue() == SiegeBidVo.instance.guildId2){
						guildPo.consumeGuildBidGold(SiegeBidVo.instance.maxBid2);
					}
					if(guildPo.getId().intValue() == SiegeBidVo.instance.guildId3){
						guildPo.consumeGuildBidGold(SiegeBidVo.instance.maxBid3);
					}
					guildPo.addMoney(guildPo.getBidGold());
					guildPo.setBidGold(0);
					guildPo.setBidCount1(0);
					guildPo.setBidCount2(0);
					guildPo.setBidCount3(0);
				}
			}
		}
		
		
		
//		System.out.println("竞标 结束：" +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+" " +copySceneActivityPo);
		for(Integer roleId : RoleTemplate.roleIdIuidMapping.keySet()){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.sendUpdateCopySceneAativityWasOpenInfo();
		}
	}
}
