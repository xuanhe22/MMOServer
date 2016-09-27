package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;

/**
 * 运镖任务信息
 * @author Administrator
 *
 */
public class YunDartTaskInfoVo  extends BasePropertyVo {
	/**
	 * 每天当前完成次数
	 */
	public Integer dailyCurrentFinishYunDartCount = 0;
	
	/**
	 * 每天当前免费刷新了的镖车次数
	 */
	public Integer dailyCurrentFreeFlushYunDartCarCount = 0;
	
	/**
	 * 当前运镖车Id -1：没有运镖车
	 */
	public Integer currentYunDartCarId = -1;
	
	/**
	 * 当前运镖车的品质 -1：没有运镖车 0：破损的物资车 1：白色物资车 2：绿色物资车 3：蓝色物资车 4：紫色物资车 5：橙色物资车 
	 */
	public Integer currentYunDartCarQuality = -1;
	
	/**
	 * 消除镖车时间
	 */
	public Integer currentYunDartCarClearAwayTime = 0;
	
	/**
	 * 运镖状态
	 */
	public Integer yunDartState = 0;
	

	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{
				dailyCurrentFinishYunDartCount,
				dailyCurrentFreeFlushYunDartCarCount,
				currentYunDartCarId,
				currentYunDartCarQuality,
				currentYunDartCarClearAwayTime,
				yunDartState
		};
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);	

		dailyCurrentFinishYunDartCount = DBFieldUtil.fetchImpodInt(vals[0]);
		dailyCurrentFreeFlushYunDartCarCount = DBFieldUtil.fetchImpodInt(vals[1]);
		currentYunDartCarId = DBFieldUtil.fetchImpodInt(vals[2]);
		currentYunDartCarQuality = DBFieldUtil.fetchImpodInt(vals[3]);
		currentYunDartCarClearAwayTime= DBFieldUtil.fetchImpodInt(vals[4]);
		if(fetchProperyItems().length == vals.length){
			yunDartState=DBFieldUtil.fetchImpodInt(vals[5]);			
		}
	}
	
	/**
	 * 调整每天完成数量
	 * @param i
	 */
	public void adjustDailyCurrentFinishYunDartCount(Integer i){
		if(i == null|| i < 0){
			return;
		}
		dailyCurrentFinishYunDartCount = dailyCurrentFinishYunDartCount + i.intValue();
	}
	
	/**
	 * 调整每天免费刷新次数
	 * @param i
	 */
	public void adjustDailyCurrentFreeFlushYunDartCarCount(Integer i){
		if(i == null|| i < 0){
			return;
		}
		dailyCurrentFreeFlushYunDartCarCount = dailyCurrentFreeFlushYunDartCarCount + i.intValue();
	}
}
