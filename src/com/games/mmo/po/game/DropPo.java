
package com.games.mmo.po.game;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.tools.ant.Task;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.type.TaskType;
import com.storm.lib.bean.CheckcCircleBean;
import com.storm.lib.component.entity.BaseGameDBPo;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_drop")
	public class DropPo extends BaseGameDBPo {
//	public static final int GLOBAL_AWARD_TYPE_ITEM = 1;
	/**
	*主键
	**/

	private Integer id;
	/**
	*掉落名无备住
	**/

	private String name;
	/**
	*掉落种类无备住
	**/

	private Integer type;
	/**
	*掉落ID无备住
	**/

	private Integer itemId;
	/**
	*权重无备住
	**/

	private Integer roll;
	/**
	*掉落组无备住
	**/

	private Integer groupsId;
	
	/**
	 * 数量
	 */
	private Integer num;
	
	private Integer bind=0;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name="item_id")
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
	@Column(name="roll")
	public Integer getRoll() {
		return roll;
	}
	public void setRoll(Integer roll) {
		this.roll = roll;
	}
	
	@Column(name="groups_id")
	public Integer getGroupsId() {
		return groupsId;
	}
	public void setGroupsId(Integer groupsId) {
		this.groupsId = groupsId;
	}
	
	@Column(name="num")
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
	@Column(name="bind")
	public Integer getBind() {
		return bind;
	}
	public void setBind(Integer bind) {
		this.bind = bind;
	}
	public static List<DropPo> makeDrop(int dropGroupId,int times) {

		List<DropPo> drops = new ArrayList<DropPo>();
		CheckcCircleBean checkcCircleBean = new CheckcCircleBean();
		while(true){
			times--;
			checkcCircleBean.count();
			if(times<0){
				break;
			}
			List<DropPo> list =GlobalCache.idTcGroupsMap.get(dropGroupId);
			if(list==null){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"List<DropPo>:"+dropGroupId);
			}
			List objs =new ArrayList();
			List weights = new ArrayList();
			for (DropPo tcDropPo : list) {
				objs.add(tcDropPo);
				weights.add(tcDropPo.getRoll());
			}
			DropPo dropPo=  (DropPo) RandomUtil.randomObjectByPecentage(objs, weights, null);
			drops.add(dropPo);
		}
		return drops;
	}
	
	public static DropPo findEntity(Integer id){
		return findRealEntity(DropPo.class,id);
	}
	
	
	public static List<DropPo> makeDropListByExp(List<IdNumberVo2> listItemDrop) {
		List<DropPo> finalDrops = new ArrayList<DropPo>();
		Integer liveDropPercent =GlobalCache.liveActivityDoubleType(LiveActivityType.RATE_DROP);
		for (IdNumberVo2 idNumberVo2: listItemDrop) {	
			for (int i=1;i<=idNumberVo2.getInt3();i++) {
					if(RandomUtil.random1W(idNumberVo2.getInt2()+(idNumberVo2.getInt2()*liveDropPercent/100))){
						finalDrops.addAll(DropPo.makeDrop(idNumberVo2.getInt1(),1));
					}
				}
		}
		return finalDrops;
	}
	
	public static List<DropPo> makeDropListByListExp(RolePo rolePo,List<List<Integer>> listItemTaskDrop) {
//		任务编号|掉落组|掉落概率|掉落次数
		List<DropPo> finalDrops = new ArrayList<DropPo>();
		for (IdNumberVo2 idNumberVo : rolePo.listRoleTasks) {
			if(idNumberVo.getInt2()>=TaskType.TASK_STATUS_ACCEPTED){
				TaskPo taskPo = TaskPo.findEntity(idNumberVo.getInt1());
				int requireNum =taskPo.conditionVals.get(0);
				int currentNum =idNumberVo.getInt2();
				int lackNum = requireNum-currentNum;
				for (List<Integer> valList: listItemTaskDrop) {
					if(valList.get(0).intValue()==idNumberVo.getInt1()){
						for (int i=1;i<=valList.get(3);i++) {
							if(lackNum>0){ 
								if(RandomUtil.random1W(valList.get(2))){
									List<DropPo> items = DropPo.makeDrop(valList.get(1),1);
									int finalCount=Math.min(lackNum, items.get(0).num);
									lackNum =lackNum-finalCount;
									items.get(0).num=finalCount;
									finalDrops.addAll(items);
								}
							}
						}
					}
				}
			}
		}
		return finalDrops;
	}
}
