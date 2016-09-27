package com.games.mmo.vo.role;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.util.ExpressUtil;
import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo3;

/**
 * 运营活动
 * @author Administrator
 *
 */
public class RoleLiveActivityVo extends BasePropertyVo{
	/**
	 * 运营活动ID
	 */
	public int liveActivityId=0;
	/**
	 * 累计充值排行/累计消费
	 */
	public int addValue=0;

	/**
	 * 条目列表 1 索引 2进度 3可完成次数 4 已完成次数
	 */	
	public List<IdNumberVo3> objs = new ArrayList<IdNumberVo3>();
	
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{objs,liveActivityId,addValue};
	}
	
	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val,spliter);
		if(fetchProperyItems().length != vals.length){
			return;
		}
		String objsStr = DBFieldUtil.fetchImpodString(vals[0]);
		this.objs = IdNumberVo3.createList(objsStr);
		this.liveActivityId = DBFieldUtil.fetchImpodInt(vals[1]);
		this.addValue=DBFieldUtil.fetchImpodInt(vals[2]);
	}
	
	
	public int updateProgress(int rank) {
		LiveActivityPo liveActivityPo = LiveActivityPo.findEntity(liveActivityId);
		if(liveActivityPo==null){
			return 0;
		}
		int index=1;
		int val=0;
		for (IdNumberVo3 idNumberVo3 : objs) {
			int value = liveActivityPo.listConditions.get(index-1).getNum();
			index++;
			
			//模式为2
			if(liveActivityPo.listConditionModes.size()>0 && liveActivityPo.listConditionModes.get(index-2)==2){
				value=rank;
			}
			else{
				value=Math.min(value, rank);
			}			
			idNumberVo3.setInt2(value);
			val=Math.max(val, value);
		}
		return val;
	}

	public int addProgress(int count) {
		LiveActivityPo liveActivityPo = LiveActivityPo.findEntity(liveActivityId);
		if(liveActivityPo==null){
			return 0;
		}
		int index=1;
		int val=0;
		for (IdNumberVo3 idNumberVo3 : objs) {
			//TODO 【业务标记】java.lang.ArrayIndexOutOfBoundsException: 5
			if(index>liveActivityPo.listConditions.size()){
				continue;
			}
			int value = liveActivityPo.listConditions.get(index-1).getNum();
			index++;
			//模式为2
			if(liveActivityPo.listConditionModes.size()>0 && liveActivityPo.listConditionModes.get(index-2)==2){
				value=count+idNumberVo3.getInt2();				
			}
			else{
//				System.out.println("id="+idNumberVo3.get(0) + "; size="+(objs.size()));
				if(idNumberVo3.getInt1().intValue()== (objs.size()-1)){
					value= count+idNumberVo3.getInt2();
				}else{
					value=Math.min(value, count+idNumberVo3.getInt2());										
				}
			}
			idNumberVo3.setInt2(value);
			val=Math.max(val, value);
		}
		return val;
	}

	public int adjustAddValue(Integer i){
		if(i < 0){
			return 0;
		}
		addValue = addValue +i;
		return addValue;
	}

	@Override
	public String toString() {
		return "RoleLiveActivityVo [liveActivityId=" + liveActivityId
				+ ", addValue=" + addValue + ", objs=" + objs + "]";
	}
	
	
}
