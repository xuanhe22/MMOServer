package com.games.backend.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.po.LiveActivityPo;
import com.games.mmo.po.game.ItemPo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

	public class LiveActivityVo extends BaseVo{
		
	/**
	*主键
	**/

	public Integer id;
	
	/**
	 * 名字
	 */
	public String name;
	
	/**
	 * 描述
	 */
	public String description;

	/**
	 * 可完成次数
	 */
	public String loopFinishTimes;
	
	/**
	 * 类型
	 */
	public Integer type;
	/**
	 * 起始时间
	 */
	
	public Long startTime=0l;
	
	/**
	 * 结束时间
	 */
	public Long endTime=0l;
	
	/**
	 * 时效文本描述
	 */
	public String timeTxt="";
	
	/**
	 * 目录
	 */
	public Integer category=0;
	
	/**
	 * 0=未结算 1=已结算
	 */
	public Integer status=0; 
	
	public Integer rank=0; 
	/**
	 * 完成条目列表,文本，进度,道具奖励
	 */

	public String conditionItems;
	

	public String exchangeItems;
	

	public String awardItems;
	
	
	/**
	 * 调节率类型,调节率数值
	 */
	public String rateItems;
	
	/**
	 * 奖励列表
	 */
	public List<List<IdNumberVo>> listAwardItems=new ArrayList<List<IdNumberVo>>();
	
	/**
	 * 完成条件
	 */
	public List<IdNumberVo> listConditions=new ArrayList<IdNumberVo>();	
	
	/**
	 * 兑换条件
	 */
	public List<List<IdNumberVo>> listExchangeItems=new ArrayList<List<IdNumberVo>>();	
	
	/**
	 * 批次号
	 */
	public String lotNumber;
	

	/**
	 * 调节率列表
	 */

	public List<IdNumberVo> listRateItems=new ArrayList<IdNumberVo>();
	
	/**
	 * 创建时间
	 */
	public Long createTime; 
	

	public void loadData(BasePo basePo) {
		listConditions=new CopyOnWriteArrayList(IdNumberVo.createList(conditionItems));
		listRateItems=new CopyOnWriteArrayList(IdNumberVo.createList(rateItems));
		if(awardItems!=null){
			String[] items = StringUtil.split(awardItems, ";");
			for (String item : items) {
				listAwardItems.add(IdNumberVo.createList(item));
			}
		}
		if(exchangeItems!=null){
			String[] items = StringUtil.split(exchangeItems, ";");
			for (String item : items) {
				listExchangeItems.add(IdNumberVo.createList(item));
			}
		}			
	}

	
	

	public void saveData() {
		conditionItems=IdNumberVo.createStr(listConditions, "|", ",");
		rateItems=IdNumberVo.createStr(listRateItems, "|", ",");
		List<String> vals = new ArrayList<String>();
		for (List<IdNumberVo> item : listAwardItems) {
			vals.add(IdNumberVo.createStr(item, "|", ","));
		}
		awardItems=DBFieldUtil.implodStringListWithMao(vals);
		vals = new ArrayList<String>();
		for (List<IdNumberVo> item : listExchangeItems) {
			vals.add(IdNumberVo.createStr(item, "|", ","));
		}
		exchangeItems=DBFieldUtil.implodStringListWithMao(vals);
	}




	public static List<LiveActivityVo> createFromPos() {
		List<LiveActivityVo> vos =new ArrayList<LiveActivityVo>();
		for (Object obj : BaseDAO.instance().dBfind("from LiveActivityPo order by rank desc")) {
			vos.add(fromPo((LiveActivityPo)obj));
		}
		return vos;
	}

	public static LiveActivityVo fromPo(LiveActivityPo po) {
		LiveActivityVo vo =new LiveActivityVo();
		List<String> valsList =new ArrayList<String>();
		for (List<IdNumberVo2> idNumberVo2 : po.listAwardItems) {
			List<String> vals =new ArrayList<String>();
			for (IdNumberVo2 idNumberVo22 : idNumberVo2) {
				String val =ItemPo.findEntity(idNumberVo22.getInt1()).getName()+"*"+idNumberVo22.getInt2()+(idNumberVo22.getInt3()==1?"":"*ub");
				vals.add(val);
			}
			String vals2= StringUtil.implode(vals, ",");
			valsList.add(vals2);
		}
		String finalString = StringUtil.implode(valsList,";\n");
		vo.awardItems=finalString;
		vo.category=po.getCategory();
		vo.conditionItems=po.getConditionItems();
		vo.description=po.getDescription();
		vo.endTime=po.getEndTime();
		vo.exchangeItems=po.getExchangeItems();
		vo.id=po.getId();
		vo.loopFinishTimes=po.getLoopFinishTimes();
		vo.name=po.getName();
		vo.startTime=po.getStartTime();
		vo.status=po.getStatus();
		vo.timeTxt=po.getTimeTxt();
		vo.type=po.getType();
		vo.rateItems=po.getRateItems();
		vo.rank=po.getRank();
		vo.lotNumber = po.getLotNumber();
		vo.createTime = po.getCreateTime();
		return vo;
	}




}
