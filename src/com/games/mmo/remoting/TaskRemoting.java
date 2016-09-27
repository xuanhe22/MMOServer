package com.games.mmo.remoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.event.listener.TaskSuccessListener;
import com.games.mmo.mapserver.bean.MapRoom;
import com.games.mmo.mapserver.cache.MapWorld;
import com.games.mmo.po.CopySceneActivityPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.TaskPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.CopySceneType;
import com.games.mmo.type.PlayTimesType;
import com.games.mmo.type.TaskType;
import com.games.mmo.type.UserEventType;
import com.games.mmo.type.VipType;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.xml.ConstantFile.Trade.Cart;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.event.EventArg;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo2;
@Controller
public class TaskRemoting extends BaseRemoting{
	@Autowired
	private CheckService checkService;	
	@Autowired
	private ChatService chatService;
	/**
	 * 事件初始化
	 */
	@Override
	protected void initListener() {
		addListener(UserEventType.TASK_SUCCESS.getValue(), (TaskSuccessListener)BeanUtil.getBean("taskSuccessListener"));
	}
	
	
	/**
	 * 任务接取
	 * @param taskId
	 * @return 任务列表
	 */
	public Object taskAccept(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.taskAccept";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println(rolePo.getName() +"taskAccept() "+ DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+ " taskId = " +taskId);
		checkService.checkExisTaskPo(taskId);
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730){
			
			if(rolePo.getLv().intValue() < taskPo.listActiveLv.get(0).intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key206")+taskPo.listActiveLv.get(0).intValue());
			}
			CopySceneActivityPo csap = CopySceneActivityPo.findEntity(CopySceneType.COPY_SCENE_CONF_YUN_DART);
			if(csap.getActivityWasOpen() == 0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key207"));
			}
//			System.out.println("taskAccept() rolePo.name = "+rolePo.getName() +"; taskId="+taskId+"; rolePo.listYunDartTaskInfoVo.get(0).yunDartState = " +rolePo.listYunDartTaskInfoVo.get(0).yunDartState +"; "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
			checkService.checkExisTaskPo(taskId);

			
			rolePo.checkPlayTimes(rolePo.listYunDartTaskInfoVo.get(0).dailyCurrentFinishYunDartCount, PlayTimesType.PLAYTIMES_TYPE_13, "key2648");
			
			if(rolePo.listYunDartTaskInfoVo.get(0).yunDartState !=0){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2689"));
			}
			rolePo.listYunDartTaskInfoVo.get(0).yunDartState = 1;
			
			MapRoom mapRoom = MapWorld.findStage(rolePo.getRoomId());
			if(mapRoom == null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"mapRoomId:  "+rolePo.getRoomId());
			}
			
			Cart cart = rolePo.fetchYunCart();
			checkService.checkExisMonsterPo(cart.monsterId);
			
			rolePo.acceptedRoleTask(taskId);
			mapRoom.spawnYunDartFighter(cart.monsterId, rolePo);
			int endTime = (int) (System.currentTimeMillis()/1000 + taskPo.getTimeLimit());
//			System.out.println("运镖结束时间="+DateUtil.getFormatDateBytimestamp(endTime*1000l));
			IdNumberVo2 idNumberVo =  rolePo.fetchRoleTask(taskId);
			idNumberVo.setInt3(endTime);
//			System.out.println(rolePo.getName() + "; taskAccept()镖车结束时间："+DateUtil.getFormatDateBytimestamp(1L*1000*idNumberVo.getInt3()));
			rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime=endTime;
			rolePo.listYunDartTaskInfoVo.get(0).adjustDailyCurrentFinishYunDartCount(1);
			rolePo.updateAwardRetrieveCount(PlayTimesType.PLAYTIMES_TYPE_13, 1);
			notifyListeners(new EventArg(rolePo, UserEventType.YUN_DART.getValue(), 1));//发送运镖通知
			StringBuffer sb = new StringBuffer();
			sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key48")).append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key209")).append(MonsterPo.findEntity(cart.monsterId).getName()).append(GlobalCache.fetchLanguageMap("key210"));
			chatService.sendHorse(sb.toString());
			chatService.sendSystemWorldChat(sb.toString());
			rolePo.sendUpdateYunDartTaskInfo();
		}else{
			rolePo.acceptedRoleTask(taskId);
		}
//		System.out.println("33 rolePo.listRoleTasks="+rolePo.listRoleTasks);
		rolePo.freshTaskNewStatus(taskId);
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 刷新镖车
	 * @param type 1:普通； 2：一键橙车
	 * @return
	 */
	public Object flushYunDartCar(Integer type){
		CheckUtil.checkIsNull(type);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.flushYunDartCar";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println(rolePo.getName() +"; flushYunDartCar() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) + "; type = "+type);

		if(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2267")+"：currentYunDartCarId:  "+rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId);
		}
		if(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId == -1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key211")+"：currentYunDartCarId:  "+rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarId);
		}
		if(rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarQuality == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2267"));
		}
		rolePo.flushYunDartCar(type);
		rolePo.sendUpdateYunDartTaskInfo();
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 检查运镖车完成时间
	 * @return
	 */
	public Object checkYunDartCarFinishTime(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.checkYunDartCarFinishTime";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println(rolePo.getName() +"; checkYunDartCarFinishTime() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis())+ "; taskId = "+taskId);
		checkService.checkExisTaskPo(taskId);	
		rolePo.checkYunCartTaskTime(taskId);
		rolePo.sendUpdateYunDartTaskInfo();
		rolePo.sendUpdateRoleTasks(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 放弃任务
	 * @param taskId
	 * @return
	 */
	public Object taskCancel(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.taskCancel";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
//		System.out.println(rolePo.getName() +"; taskCancel() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) + "; taskId = "+taskId);
		checkService.checkExisTaskPo(taskId);
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if(taskPo.getTaskType().intValue() == 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key212"));
		}
		if(taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730){
			rolePo.initYunDartInfo(taskId);			
		}else{
			IdNumberVo2 idNumberVo = rolePo.fetchRoleTask(taskId);
			idNumberVo.setInt2(TaskType.TASK_STATUS_ACTIVED);
		}
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		return SessionType.MULTYPE_RETURN;
	}
	/**
	 * 提交任务
	 * @param taskId
	 * @return
	 */
	public Object taskSubmit(Integer taskId){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		IdNumberVo2 idNumberVo =  rolePo.fetchRoleTask(taskId);
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if(taskPo != null && taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_730){
			PrintUtil.print("taskSubmit() rolePo.name = " +rolePo.getName() + "; taskId = " +taskId +"; " +DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()));
			int time = (int) (System.currentTimeMillis()/1000);
			if(time > idNumberVo.getInt3()){
				SessionUtil.addDataArray(rolePo.listRoleTasks);
				SessionUtil.addDataArray(rolePo.getCurrentFinishTaskId());
				SessionUtil.addDataArray(rolePo.getCurrentFinishTaskId());
				return 1;
			}
		}
		Object[] returnVals=rolePo.submitTask(taskId);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateSkillPoint();
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		SessionUtil.addDataArray(rolePo.getCurrentFinishTaskId());
		SessionUtil.addDataArray(returnVals[0]);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 提交环任务
	 * @param awardType 普通=1
	 * @return
	 */
	public Object taskRingSubmit(Integer awardType){
		CheckUtil.checkIsNull(awardType);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.taskRingSubmit";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);
		
		rolePo.taskRingSubmit(awardType);
		rolePo.sendUpdateExpAndLv(false);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		SessionUtil.addDataArray(rolePo.getRingTaskCurrentIndex());
		SessionUtil.addDataArray(rolePo.getRingTaskCurrentQuality());
		SessionUtil.addDataArray(rolePo.getCurrentFinishTaskId());
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 一键满星
	 * @return
	 */
	public Object taskRingQualityUp(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.taskRingQualityUp";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.taskRingQualityUp();
		rolePo.sendUpdateTreasure(false);
		SessionUtil.addDataArray(rolePo.getRingTaskCurrentQuality());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 一键全部完成
	 * @return
	 */
	public Object taskRingAllFinish(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.taskRingAllFinish";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println("11diamond = " + rolePo.getDiamond());
		VipPo vipPo = GlobalCache.mapVipPo.get(rolePo.getVipLv());
		int flag = vipPo.fetchTypeNumByType(VipType.VIP_PRIVILEGE_TYPE_8);
		if(flag == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2593"));
		}
		rolePo.taskRingAllFinish(taskId);
		rolePo.sendUpdateTreasure(false);
		rolePo.sendUpdateMainPack(false);
		rolePo.sendUpdateExpAndLv(false);
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		SessionUtil.addDataArray(rolePo.getRingTaskCurrentIndex());
		SessionUtil.addDataArray(rolePo.getRingTaskCurrentQuality());
		SessionUtil.addDataArray(rolePo.getCurrentFinishTaskId());
//		System.out.println("11diamond = " + rolePo.getDiamond());
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 成就领奖
	 * @return
	 */
	public Object achieveFinish(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.achieveFinish";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.achieveFinish(taskId);
		SessionUtil.addDataArray(1);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 作弊加任务完成
	 * @param taskId
	 * @param count
	 * @return
	 */
	public Object cheatAddTaskFinish(Integer taskId,Integer count){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		TaskPo taskPo =TaskPo.findEntity(taskId);
		int taskRequireCount = taskPo.conditionVals.get(1);
		IdNumberVo2 idNumberVo2 = rolePo.fetchRoleTask(taskId);
		if(idNumberVo2==null){
			ExceptionUtil.throwConfirmParamException("no task:"+count);
		}
		idNumberVo2.setInt2(Math.min(taskRequireCount,idNumberVo2.getInt2()+count));
		rolePo.sendUpdateTaskProgress(idNumberVo2.getInt1(), idNumberVo2.getInt2());
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 方法功能:打开任务活跃度面板
	 * 更新时间:2015-7-10, 作者:peter
	 * 
	 */
	public Object openLivelyTaskPanel(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.openLivelyTaskPanel";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		rolePo.fetchDailyLivelyTaskFinishScore(0);
		SessionUtil.addDataArray(rolePo.getDailyLivelyTaskFinishScore());
		SessionUtil.addDataArray(rolePo.listDailyLivelyAwardRecord);
		SessionUtil.addDataArray(rolePo.listRoleTasks);
		return SessionType.MULTYPE_RETURN;
	}
	
	public Object openAhievePanel(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.openAhievePanel";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		SessionUtil.addDataArray(rolePo.listRoleAchievesTasks);
		return SessionType.MULTYPE_RETURN;
	}
	
	
	/**
	 * 修改任务状态
	 * @param taskId
	 * @return
	 */
	public Object updateTaskState(Integer taskId){
//		System.out.println("updateTaskState() taskId="+taskId);
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.updateTaskState";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
//		System.out.println(rolePo.getName() +"; updateTaskState() "+DateUtil.getFormatDateBytimestamp(System.currentTimeMillis()) + "; taskId = "+taskId);
		checkService.checkExisTaskPo(taskId);
		TaskPo taskPo = TaskPo.findEntity(taskId);
		int currentTime = (int) (System.currentTimeMillis()/1000);
		
//		IdNumberVo2 idNumberVo =  rolePo.fetchRoleTask(taskId);
		if(currentTime <  rolePo.listYunDartTaskInfoVo.get(0).currentYunDartCarClearAwayTime ){
			IdNumberVo2 idNumberVo2 = rolePo.fetchRoleTask(taskId);
			idNumberVo2.setInt2(taskPo.conditionVals.get(1));
		}
//		System.out.println("===="+rolePo.listRoleTasks);
		rolePo.sendUpdateRoleTasks(false);
		return SessionType.MULTYPE_RETURN;
	}
	
	/**
	 * 粉丝团
	 * @param taskId
	 * @return
	 */
	public Object fansTask(Integer taskId){
		CheckUtil.checkIsNull(taskId);
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_TaskRemoting.fansTask";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		
		checkService.checkExisTaskPo(taskId);
		TaskPo taskPo = TaskPo.findEntity(taskId);
		if (taskPo.conditionVals.get(0).intValue() == TaskType.TASK_TYPE_CONDITION_737) {
			rolePo.taskConditionProgressReplace(1,TaskType.TASK_TYPE_CONDITION_737,null,null);
		}
		return SessionType.MULTYPE_RETURN;
	}
	
}
