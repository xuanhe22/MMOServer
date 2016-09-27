package com.games.mmo.process;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csvreader.CsvWriter;
import com.games.mmo.TestVoice;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.thread.WorldRoomsRunner;
import com.games.mmo.mapserver.type.MapType;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.FlagPo;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.NoticePo;
import com.games.mmo.task.ArenaSendAwardJob;
import com.games.mmo.task.EveryNight24Job;
import com.games.mmo.task.FreshRoleLoginResetJob;
import com.games.mmo.task.KPIJob;
import com.games.mmo.task.NoticeJob;
import com.games.mmo.task.RankJob;
import com.games.mmo.task.RemainJob;
import com.games.mmo.task.SystemPartResultJob;
import com.games.mmo.task.TimerBossJob;
import com.games.mmo.task.WeeklyJob;
import com.games.mmo.task.activity.BloodMagicFortressBeginJob;
import com.games.mmo.task.activity.BloodMagicFortressEndJob;
import com.games.mmo.task.activity.CollectCrystalBeginJob;
import com.games.mmo.task.activity.CollectCrystalEndJob;
import com.games.mmo.task.activity.EvilSoulForbiddenSpaceBeginJob;
import com.games.mmo.task.activity.EvilSoulForbiddenSpaceEndJob;
import com.games.mmo.task.activity.FreeWarBeginJob;
import com.games.mmo.task.activity.FreeWarEndJob;
import com.games.mmo.task.activity.GuildBidSiegeBeginJob;
import com.games.mmo.task.activity.GuildBidSiegeEndJob;
import com.games.mmo.task.activity.GuildWarBeginJob;
import com.games.mmo.task.activity.GuildWarEndJob;
import com.games.mmo.task.activity.KillingTowerBeginJob;
import com.games.mmo.task.activity.KillingTowerEndJob;
import com.games.mmo.task.activity.KingOfPkBeginJob;
import com.games.mmo.task.activity.KingOfPkEndJob;
import com.games.mmo.task.activity.MonsterCrisisBeginJob;
import com.games.mmo.task.activity.MonsterCrisisEndJob;
import com.games.mmo.task.activity.SiegeWarBeginJob;
import com.games.mmo.task.activity.SiegeWarEndJob;
import com.games.mmo.task.activity.YunDartTaskBeginJob;
import com.games.mmo.task.activity.YunDartTaskEndJob;
import com.games.mmo.thread.CheckSessionSaveThread;
import com.games.mmo.thread.CleanCacheTaskThread;
import com.games.mmo.thread.Every5minutesLogWrite;
import com.games.mmo.thread.LiveActivitySyncThread;
import com.games.mmo.thread.LogWriteTask;
import com.games.mmo.thread.LoginQueueThread;
import com.games.mmo.thread.SimpleAbroadThread;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.SystemType;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildwar.Territory;
import com.storm.lib.component.entity.GameDataTemplate;
import com.storm.lib.component.quartz.QuartzSchedulerTemplate;
import com.storm.lib.init.BaseInitProcessor;
import com.storm.lib.template.TaskTemplate;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.Md5Util;
import com.storm.lib.util.ZipCompressor;


@Service
public class InitProcessor extends BaseInitProcessor{
	@Autowired
	private TaskTemplate taskTemplate;

	@Autowired
	private GameDataTemplate gameDataTemplate;	
	@Autowired
	private QuartzSchedulerTemplate quartzSchedulerTemplate;
	
	@Override
	public void initAdditionalTasks() {
		GlobalCache.SERVER_CURRENT_START_KEY=new Long(System.currentTimeMillis()/10).intValue();
		
		if(BaseStormSystemType.ALLOW_BAIDU_VOICE){
			HttpURLConnection conn=null;
			try {
				conn=TestVoice.getToken();
			} catch (Exception e) {
				if(conn!=null){
					conn.disconnect();
				}
				ExceptionUtil.processException(e);
			}
		}
//		initGameLog();
		try {
			initComponent("coreComponent");
			initComponent("entityComponent");
			initComponent("socketComponent",2);
			initComponent("remotingComponent");
			initComponent("quartzComponent");
			if(BaseStormSystemType.developMode()){
				//initComponent("ftpComponent");
			}
			initComponent("chatComponent");		
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}
		
		
		LogUtil.checkTodayTime();
		GlobalPo.init(0);
		

		GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
		
		XmlCache.loadAndClean(gp.getValueStr());

		GlobalCache.loadAndClean();
		new CleanCacheTaskThread().start();
	 	new LiveActivitySyncThread().start();
	 	new CheckSessionSaveThread().start();
	 	new LogWriteTask().start();
	 	new Every5minutesLogWrite().start();
	 	new SimpleAbroadThread().start();
	 	new LoginQueueThread().start();
		RoleDAO roleDAO = (RoleDAO) BeanUtil.getBean("roleDAO");
		roleDAO.generateRank();	

		quartzSchedulerTemplate.schedulerJob("ArenaSendAwardJob", "TasArenaSendAwardJob", "TriggerArenaSendAwardJob", ArenaSendAwardJob.class,"0 00 21 * * ?");
		quartzSchedulerTemplate.schedulerJob("RankJob", "TaskRankJob", "TriggerRankJob", RankJob.class,"0 55 0/2 * * ?");
		quartzSchedulerTemplate.schedulerJob("WeeklyJob", "TaskWeeklyJob", "TriggerRankWeeklyJob", WeeklyJob.class, "0 15 00 ? * SUN");
		quartzSchedulerTemplate.schedulerJob("KPIJob", "TaskKPIJob", "TriggerKPIJob", KPIJob.class,"0 0 * * * ?");
		quartzSchedulerTemplate.schedulerJob("EveryNight24Job", "TaskEveryNight24Job", "TriggerEveryNight24Job", EveryNight24Job.class,"0 45 23 * * ?");
//		quartzSchedulerTemplate.schedulerJob("SystemPartResultJob", "TaskSystemPartResultJob", "TriggerSystemPartResultJob", SystemPartResultJob.class,"0 20 00 * * ?");
		quartzSchedulerTemplate.schedulerJob("RemainJob", "TaskRemainJob", "TriggerRemainJob", RemainJob.class,"0 0/30 * * * ?");
		quartzSchedulerTemplate.schedulerJob("FreshRoleLoginReset", "TaskFreshRoleLoginReset", "TriggerFreshRoleLoginReset", FreshRoleLoginResetJob.class, "0 1 * * * ?");
//		//系统留存率定时器
//		Timer timer = new Timer();
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.SECOND, 0);
//		timer.schedule(new SystemPartResult(), calendar.getTime(), 24 * 60 * 60 * 1000);
//		
		initActivitiesJobs();
		noticeJobs();
		updateCopySceneActivityInfo("TriggerRankWeeklyJob", "TaskWeeklyJob", null, null, 20206032);
		new WorldRoomsRunner().start();
		GlobalCache.inited=true;
	}

	@Override
	public void updateVersion(){

//			GameDataTemplate gameDataTemplate = (GameDataTemplate) BeanUtil.getBean("gameDataTemplate");
			byte[] content=null;
			File uploadDir =  new File(SystemType.PathOutputUpload);
			String contentStr="";
			Class[] clazz = gameDataTemplate.GAMECLASS;
			for (Class class1 : clazz) {
				List gamePos = gameDataTemplate.getDataList(class1);
		        String path = SystemType.PathOutputUpload+class1.getSimpleName()+".csv";
		        CsvWriter wr =new CsvWriter(path,',',Charset.forName("UTF-8"));
		        
		        Class heroPo = class1;
		        List<Method> avaMethods =new ArrayList<Method>();
		        Method [] methods = heroPo.getDeclaredMethods();
		        for (Method method : methods) {
		        	
					if(method.isAnnotationPresent(Column.class)){
						avaMethods.add(method);
					}
				}
		        String[] fieldType =new String[avaMethods.size()];
		        String[] fieldName =new String[avaMethods.size()];
		        
		        
		        
		        for (int i=0;i<avaMethods.size();i++) {
					if(avaMethods.get(i).getReturnType() == Integer.class){
						fieldType[i]="int";
					}
					else{
						fieldType[i]="string";
					}
					Column column = avaMethods.get(i).getAnnotation(Column.class);
					fieldName[i]=column.name();	
				}
		        
//		        System.out.println(heroPo.getSimpleName());

		        try {
					wr.writeRecord(fieldType);
					wr.writeRecord(fieldName);
					String[] values =new String[avaMethods.size()];
					for (Object heroPo2 : gamePos) {
					    for (int i=0;i<avaMethods.size();i++) {
					        //System.out.println(avaMethods.get(i).getName());
					    	Object value = avaMethods.get(i).invoke(heroPo2, new Object[]{});
					    	if(value==null){
					    		values[i]="";
					    	}
					    	else{
					    		values[i]=value.toString();
					    	}
					    }
						wr.writeRecord(values);
					}
				} catch (IllegalArgumentException e) {
					ExceptionUtil.processException(e);
				} catch (IOException e) {
					ExceptionUtil.processException(e);
				} catch (IllegalAccessException e) {
					ExceptionUtil.processException(e);
				} catch (InvocationTargetException e) {
					ExceptionUtil.processException(e);
				}
		        wr.close();
			}
			File zipFile =null;
			try {
				ZipCompressor zc = new  ZipCompressor(SystemType.PathOutput+"game.zip");  
				zc.compressExe(SystemType.PathOutputUpload);
				zipFile = new File(SystemType.PathOutput+"game.zip");
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(zipFile));
				ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
				byte[] temp = new byte[1024];
				int size = 0;
				while ((size = in.read(temp)) != -1) {
					out.write(temp, 0, size);
				}
				in.close();
				content = out.toByteArray();
				GameDataTemplate.VERSION_CONTENT=content;
				GameDataTemplate.STATIC_DATA_VERSION_KEY=Md5Util.getFileMD5String(zipFile);

				for (File file : uploadDir.listFiles()) {
					file.delete();
				}
				zipFile.delete();
			} catch (Exception e) {
				ExceptionUtil.processException(e);
			}
	}
	
	/**
	 * 
	 * 方法功能:初始化活动
	 * 更新时间:2014-9-22, 作者:johnny
	 */
	private void initActivitiesJobs() {
		List<CopySceneConfPo> copySceneConfPos = gameDataTemplate.getDataList(CopySceneConfPo.class);
		for(CopySceneConfPo cscp : copySceneConfPos){
			// PK之王
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_KINGOFPK){
				String jobName1 = "KingOfPkBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskKingOfPkBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerKingOfPkBeginJob_"+cscp.getId();
				String jobName2 = "KingOfPkEndJob_"+cscp.getId();
				String jobGroup2 = "TaskKingOfPkEndJob_"+cscp.getId();
				String triggerName2 = "TriggerKingOfPkEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, KingOfPkBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, KingOfPkEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
				
			}
			// 魔化危机
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_MONSTERCRISIS){
				String jobName1 = "MonsterCrisisBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskMonsterCrisisBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerMonsterCrisisBeginJob_"+cscp.getId();
				String jobName2 = "MonsterCrisisEndJob_"+cscp.getId();
				String jobGroup2 = "TaskMonsterCrisisEndJob_"+cscp.getId();
				String triggerName2 = "TriggerMonsterCrisisEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, MonsterCrisisBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, MonsterCrisisEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			// 恶灵禁地
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_EVILSOULFORBIDDENSPACE){
				String jobName1 = "EvilSoulForbiddenSpaceBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskEvilSoulForbiddenSpaceBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerEvilSoulForbiddenSpaceBeginJob_"+cscp.getId();
				String jobName2 = "EvilSoulForbiddenSpaceEndJob_"+cscp.getId();
				String jobGroup2 = "TaskEvilSoulForbiddenSpaceEndJob_"+cscp.getId();
				String triggerName2 = "TriggerEvilSoulForbiddenSpaceEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, EvilSoulForbiddenSpaceBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, EvilSoulForbiddenSpaceEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			// 血魔堡垒
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_BLOODMAGICFORTRESS){
				String jobName1 = "BloodMagicFortressBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskBloodMagicFortressBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerBloodMagicFortressBeginJob_"+cscp.getId();
				String jobName2 = "BloodMagicFortressEndJob_"+cscp.getId();
				String jobGroup2 = "TaskBloodMagicFortressEndJob_"+cscp.getId();
				String triggerName2 = "TriggerBloodMagicFortressEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, BloodMagicFortressBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, BloodMagicFortressEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			// 极限之塔
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_KILLINGTOWER_START){
				String jobName1 = "KillingTowerBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskKillingTowerBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerKillingTowerBeginJob_"+cscp.getId();
				String jobName2 = "KillingTowerEndJob_"+cscp.getId();
				String jobGroup2 = "TaskKillingTowerEndJob_"+cscp.getId();
				String triggerName2 = "TriggerKillingTowerEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, KillingTowerBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, KillingTowerEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			
			// 运镖任务
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_YUN_DART){
				String jobName1 = "YunDartTaskBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskYunDartTaskBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerYunDartTaskBeginJob_"+cscp.getId();
				String jobName2 = "YunDartTaskEndJob_"+cscp.getId();
				String jobGroup2 = "TaskYunDartTaskEndJob_"+cscp.getId();
				String triggerName2 = "TriggerYunDartTaskEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, YunDartTaskBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, YunDartTaskEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_GUILD_BID){
				String jobName1 = "GuildBidSiegeBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskGuildBidSiegeBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerGuildBidSiegeBeginJob_"+cscp.getId();
				String jobName2 = "GuildBidSiegeEndJob_"+cscp.getId();
				String jobGroup2 = "TaskGuildBidSiegeEndJob_"+cscp.getId();
				String triggerName2 = "TriggerGuildBidSiegeEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, GuildBidSiegeBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, GuildBidSiegeEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_FREE_WAR){
				String jobName1 = "FreeWarBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskFreeWarBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerFreeWarBeginJob_"+cscp.getId();
				String jobName2 = "FreeWarEndJob_"+cscp.getId();
				String jobGroup2 = "TaskFreeWarEndJob_"+cscp.getId();
				String triggerName2 = "TriggerFreeWarEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, FreeWarBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, FreeWarEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			// 公会领地战
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_GUILD_WAR){
				String jobName1 = "GuildWarBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskGuildWarBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerGuildWarBeginJob_"+cscp.getId();
				String jobName2 = "GuildWarEndJob_"+cscp.getId();
				String jobGroup2 = "TaskGuildWarEndJob_"+cscp.getId();
				String triggerName2 = "TriggerGuildWarEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, GuildWarBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, GuildWarEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			// 沙巴克
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_SIEGE_WAR){
				String jobName1 = "SiegeWarBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskSiegeWarBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerSiegeWarBeginJob_"+cscp.getId();
				String jobName2 = "SiegeWarEndJob_"+cscp.getId();
				String jobGroup2 = "TaskSiegeWarEndJob_"+cscp.getId();
				String triggerName2 = "TriggerSiegeWarEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, SiegeWarBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, SiegeWarEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			
			// 定时刷boss
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_TIMER_BOSS){
				String jobName1 = "TimerBossJob_"+cscp.getId();
				String jobGroup1 = "TaskTimerBossJob_"+cscp.getId();
				String triggerName1 = "TriggerTimerBossJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, TimerBossJob.class,cscp.getOpenTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,null, null,cscp.getId());
			}
			
			if(cscp.getId().intValue() == CopySceneType.COPY_SCENE_CONF_COLLECT_CRYSTAL){
				String jobName1 = "CollectCrystalBeginJob_"+cscp.getId();
				String jobGroup1 = "TaskCollectCrystalBeginJob_"+cscp.getId();
				String triggerName1 = "TriggerCollectCrystalBeginJob_"+cscp.getId();
				String jobName2 = "CollectCrystalEndJob_"+cscp.getId();
				String jobGroup2 = "TaskCollectCrystalEndJob_"+cscp.getId();
				String triggerName2 = "TriggerCollectCrystalEndJob_"+cscp.getId();
				quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, CollectCrystalBeginJob.class,cscp.getOpenTimeExp());
				quartzSchedulerTemplate.schedulerJob(jobName2, jobGroup2, triggerName2, CollectCrystalEndJob.class,cscp.getCloseTimeExp());
				updateCopySceneActivityInfo(triggerName1,jobGroup1,triggerName2,jobGroup2,cscp.getId());
			}
			
		}
	}
	
	private void noticeJobs(){
		List<NoticePo> noticePos = gameDataTemplate.getDataList(NoticePo.class);
		for(NoticePo noticePo : noticePos){
			String jobName1 = "NoticeJob_"+noticePo.getId();
			String jobGroup1 = "TaskNoticeJob_"+noticePo.getId();
			String triggerName1 = "TriggerNoticeJob_"+noticePo.getId();
			quartzSchedulerTemplate.schedulerJob(jobName1, jobGroup1, triggerName1, NoticeJob.class,noticePo.getNoticeTime());
//			System.out.println("noticePo = " + noticePo.getId());
		}
	}
	
	private void updateCopySceneActivityInfo(String triggerName1,String jobGroup1,String triggerName2, String jobGroup2, Integer copySceneActivityId){
		CopySceneActivityPo copySceneActivityPo = CopySceneActivityPo.findEntity(copySceneActivityId);
		if(triggerName1 != null && jobGroup1 !=null){
			Trigger trigger1 = quartzSchedulerTemplate.fetchTriggerByTriggerName(triggerName1, jobGroup1);
			Date date1 = trigger1.getNextFireTime();
//			System.out.println("trigger1 :"+trigger1.getName() +"  "+ date1.toLocaleString());
			copySceneActivityPo.setBeginTimeNext(date1.getTime());			
		}
		if(triggerName2 !=null && jobGroup2 != null){
			Trigger trigger2 = quartzSchedulerTemplate.fetchTriggerByTriggerName(triggerName2, jobGroup2);
			Date date2 = trigger2.getNextFireTime();
//			System.out.println("trigger2 :"+trigger2.getName() +"  "+ date2.toLocaleString());
			copySceneActivityPo.setEndTimeNext(date2.getTime());			
		}
		if(copySceneActivityPo.getBeginTimeNext() != null && copySceneActivityPo.getEndTimeNext() != null ){
			long currentTime = System.currentTimeMillis();
//			System.out.println(copySceneActivityPo.getId() + "下次开启时间："+copySceneActivityPo.getBeginTimeNext().longValue());
//			System.out.println(copySceneActivityPo.getId() + "下次结束时间："+copySceneActivityPo.getEndTimeNext().longValue());
//			System.out.println(copySceneActivityPo.getId()+" :: " +((copySceneActivityPo.getBeginTimeNext().longValue() > copySceneActivityPo.getEndTimeNext().longValue())));
			if((copySceneActivityPo.getBeginTimeNext().longValue() > copySceneActivityPo.getEndTimeNext().longValue())){
				copySceneActivityPo.setActivityWasOpen(1);
			}else{
				copySceneActivityPo.setActivityWasOpen(0);
			}
//			System.out.println(copySceneActivityPo.getId()+" :: 是否开启："+copySceneActivityPo.getActivityWasOpen() );
		}
		if(copySceneActivityPo.getId().intValue() == CopySceneType.COPY_SCENE_CONF_SIEGE_WAR){
			for (Territory territory : XmlCache.xmlFiles.constantFile.guild.guildwar.territory) {
				if(territory.sceneid==MapType.SIEGE_SCENE_MAP){
					FlagPo flagPo = FlagPo.findFlagBySceneId(territory.sceneid);
					flagPo.setFlagStatus(copySceneActivityPo.getActivityWasOpen());
					break;
				}
			}
		}
		if(copySceneActivityPo.getId().intValue() == CopySceneType.COPY_SCENE_CONF_GUILD_WAR){
			for (Territory territory : XmlCache.xmlFiles.constantFile.guild.guildwar.territory) {
				if(territory.sceneid!=MapType.SIEGE_SCENE_MAP){
					FlagPo flagPo = FlagPo.findFlagBySceneId(territory.sceneid);
					flagPo.setFlagStatus(copySceneActivityPo.getActivityWasOpen());
				}
			}
		}
	}
}
