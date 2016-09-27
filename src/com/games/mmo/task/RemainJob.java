package com.games.mmo.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.games.mmo.service.ServerService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.PrintUtil;

/**
 * @author 王叶峰
 * @date:2016-5-17 上午9:27:54
 * 
 */
public class RemainJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		PrintUtil.print("RemainJob start:"+new Date().toLocaleString());
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE) + 1, 0, 0, 0);
		ServerService serverService = new ServerService();
		String keySql ="select DISTINCT(channel_key) as channel_key from "+BaseStormSystemType.USER_DB_NAME+".u_po_user";
		List<String> channelKeys = BaseDAO.instance().jdbcTemplate.queryForList(keySql, String.class);
		serverService.summaryRemain(calendar.getTimeInMillis(), channelKeys);
		PrintUtil.print("RemainJob end:"+new Date().toLocaleString());
	}

}
