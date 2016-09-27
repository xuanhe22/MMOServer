package com.games.mmo.task;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.GuildPo;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.RetrieveSystemVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.PrintUtil;

public class EveryNight24Job implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		PrintUtil.print("EveryNight24Job start:"+new Date().toLocaleString());
		fetchServerSumDiamond();
		checkGoildInfo();
		PrintUtil.print("EveryNight24Job end:"+new Date().toLocaleString());
	}
	
	private void fetchServerSumDiamond(){
		String sql = "select channel_key,sum(diamond) as sum_diamond from "+BaseStormSystemType.USER_DB_NAME+".u_po_user GROUP BY channel_key ";
//		PrintUtil.print("sql="+sql);
		List<Map<String,Object>> list = BaseDAO.instance().jdbcTemplate.queryForList(sql);
//		PrintUtil.print("list="+list);
		for(Map<String,Object> sumMap : list ){
			String channelKey = String.valueOf(sumMap.get("channel_key")!=null?sumMap.get("channel_key"):"");

			Long sumValue = Long.valueOf(sumMap.get("sum_diamond")!=null?sumMap.get("sum_diamond").toString() : "0");
//			PrintUtil.print("channelKey="+channelKey+"; sumValue="+sumValue);
			Integer diamond = sumValue.intValue();
			LogUtil.writeLog(null, 310, diamond, 0, 0,GlobalCache.fetchLanguageMap("key2589"),channelKey);			
		}
		
		checkRetrieve();
		// 重置资源找回（PK之王，魔化危机）
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyAwardRetrieve);
		List<RetrieveSystemVo> listRetrieveSystemVo = (List<RetrieveSystemVo>) gp.valueObj;
		for(RetrieveSystemVo retrieveSystemVo : listRetrieveSystemVo){
			if(retrieveSystemVo.time != 0L){
				long begin = DateUtil.getInitialDate(retrieveSystemVo.time);
				if(System.currentTimeMillis() > (begin+1000L*60*60*24)){
					retrieveSystemVo.time=0l;
					retrieveSystemVo.todayState=0;
				}			
			}
		}
		PrintUtil.print("22listRetrieveSystemVo="+listRetrieveSystemVo);
	}
	
	// 检查找回资源是否开启
	private void checkRetrieve(){
		PrintUtil.print("检查找回资源是否开启:"+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyRecordTime);
		long recordTime = Long.valueOf(gp.valueObj.toString());
		long nowTime = DateUtil.getInitialDate(System.currentTimeMillis()+1000L*60*5);
		PrintUtil.print("");
		if(recordTime!=nowTime){
			GlobalPo globalPo = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyAwardRetrieve);
			List<RetrieveSystemVo> listRetrieveSystemVo = (List<RetrieveSystemVo>) globalPo.valueObj;
			for(RetrieveSystemVo retrieveSystemVo : listRetrieveSystemVo){
				retrieveSystemVo.yesterdayState = retrieveSystemVo.todayState;
				retrieveSystemVo.todayState=0;
			}
			gp.valueObj=DateUtil.getInitialDate(System.currentTimeMillis());
			PrintUtil.print("11listRetrieveSystemVo="+listRetrieveSystemVo);
		}
	}
	
	private void checkGoildInfo(){
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild order by battle_power desc");
		Iterator it = rows.iterator();
		while(it.hasNext()) 
		{
			Map userMap = (Map) it.next();  
			Integer guildId  =Integer.valueOf(userMap.get("id").toString());
			if(guildId != null && guildId !=0){
				GuildPo guildPo = GuildPo.findEntity(guildId);
				if(guildPo != null){
					guildPo.setPriestFreshCount(0);
					guildPo.setPriestFreshQuality(1);
					guildPo.setPriestFreshState(0);
				}
			}
		}
	}

}
