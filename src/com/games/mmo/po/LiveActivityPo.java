package com.games.mmo.po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.service.MailService;
import com.games.mmo.type.LiveActivityType;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.RankVo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.IdNumberVo3;
	/**
	 *
	 * 类功能: 运营活动
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_live_activity")

	public class LiveActivityPo extends BaseUserDBPo implements Comparable<LiveActivityPo>{
		
	/**
	*主键
	**/

	private Integer id;
	
	/**
	 * 名字
	 */
	private String name;
	
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 可完成次数
	 */
	private String loopFinishTimes;
	
	/**
	 * 类型
	 */
	private Integer type;
	/**
	 * 起始时间
	 */
	
	private Long startTime=0l;
	
	/**
	 * 结束时间
	 */
	private Long endTime=0l;
	
	/**
	 * 时效文本描述
	 */
	private String timeTxt="";
	
	/**
	 * 目录
	 */
	private Integer category=0;
	
	/**
	 * 0=未结算 1=已结算
	 */
	private Integer status=0; 
	
	private Integer rank=0;
	private String url;
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 服务器
	 */
	private String servers;
	/**
	 * 创建时间
	 */
	private Long createTime=System.currentTimeMillis();
	/**
	 * 完成条目列表,文本，进度,道具奖励
	 */
	@JSONField(serialize=false)
	private String conditionItems;
	
	@JSONField(serialize=false)
	private String exchangeItems;
	
	@JSONField(serialize=false)
	private String awardItems;
	
	/**
	 * 角色ID,角色名字,排名
	 */
	@JSONField(serialize=false)
	private String rankItems;
	
	/**
	 * 调节率类型,调节率数值
	 */
	@JSONField(serialize=false)
	private String rateItems;
	
	/**
	 * 条件模式
	 */
	private String conditionModes;

	/**
	 * 1=总数 2=每日 3=每周
	 */
	private Integer loopWay;
	
	/**
	 * 奖励列表
	 */
	public List<List<IdNumberVo2>> listAwardItems=new ArrayList<List<IdNumberVo2>>();
	
	/**
	 * 完成条件
	 */
	public List<IdNumberVo> listConditions=new ArrayList<IdNumberVo>();	
	
	/**
	 * 兑换条件
	 */
	public List<List<IdNumberVo>> listExchangeItems=new ArrayList<List<IdNumberVo>>();	
	
	/**
	 * 排名列表
	 */
	@JSONField(serialize=false)
	public CopyOnWriteArrayList<RankVo> listRankItems=new CopyOnWriteArrayList<RankVo>();
	
	/**
	 * 调节率列表
	 */

	public List<IdNumberVo> listRateItems=new ArrayList<IdNumberVo>();
	
	public List<Integer> listConditionModes=new ArrayList<Integer>();	
	
	public CopyOnWriteArrayList<Integer> listLoopFinishTimes = new CopyOnWriteArrayList<Integer>();
	
	//以上为前端需要关心的字段
	
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
		changed("name",name,this.name);
		this.name = name;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		changed("description",description,this.description);
		this.description = description;
	}
	
	@Column(name="loop_finish_times")
	public String getLoopFinishTimes() {
		return loopFinishTimes;
	}
	public void setLoopFinishTimes(String loopFinishTimes) {
		changed("loop_finish_times",loopFinishTimes,this.loopFinishTimes);
		this.loopFinishTimes = loopFinishTimes;
	}
	
	@Column(name="type")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		changed("type",type,this.type);
		this.type = type;
	}
	
	@Column(name="start_time")
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		changed("start_time",startTime,this.startTime);
		this.startTime = startTime;
	}
	
	@Column(name="end_time")
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		changed("end_time",endTime,this.endTime);
		this.endTime = endTime;
	}
	
	@Column(name="time_txt")
	public String getTimeTxt() {
		return timeTxt;
	}
	public void setTimeTxt(String timeTxt) {
		changed("time_txt",timeTxt,this.timeTxt);
		this.timeTxt = timeTxt;
	}
	
	@Column(name="category")
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		changed("category",category,this.category);
		this.category = category;
	}
	
	
	@Column(name="condition_items")
	public String getConditionItems() {
		return conditionItems;
	}
	public void setConditionItems(String conditionItems) {
		changed("condition_items",conditionItems,this.conditionItems);
		this.conditionItems = conditionItems;
	}
	
	@Column(name="exchange_items")
	public String getExchangeItems() {
		return exchangeItems;
	}
	public void setExchangeItems(String exchangeItems) {
		changed("exchange_items",exchangeItems,this.exchangeItems);
		this.exchangeItems = exchangeItems;
	}
	
	
	@Column(name="award_items")
	public String getAwardItems() {
		return awardItems;
	}
	public void setAwardItems(String awardItems) {
		changed("award_items",awardItems,this.awardItems);
		this.awardItems = awardItems;
	}
	@Column(name="rank_items")
	public String getRankItems() {
		return rankItems;
	}
	public void setRankItems(String rankItems) {
		changed("rank_items",rankItems,this.rankItems);
		this.rankItems = rankItems;
	}
	
	@Column(name="rate_items")
	public String getRateItems() {
		return rateItems;
	}
	public void setRateItems(String rateItems) {
		changed("rate_items",rateItems,this.rateItems);
		this.rateItems = rateItems;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		changed("status",status,this.status);
		this.status = status;
	}
	
	
	@Column(name="rank")
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		changed("rank",rank,this.rank);
		this.rank = rank;
	}

	@Column(name="loop_way")
	public Integer getLoopWay() {
		return loopWay;
	}
	public void setLoopWay(Integer loopWay) {
		changed("loop_way",loopWay,this.loopWay);
		this.loopWay = loopWay;
	}
	
	@Column(name="condition_modes")
	public String getConditionModes() {
		return conditionModes;
	}
	public void setConditionModes(String conditionModes) {
		changed("condition_modes",conditionModes,this.conditionModes);
		this.conditionModes = conditionModes;
	}
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loadRanks();
			reloadByStrs();

			
			loaded =true;
		}
	}
	
	
	public void reloadByStrs() {
		listAwardItems=new CopyOnWriteArrayList<List<IdNumberVo2>>();
		listConditions=new CopyOnWriteArrayList(IdNumberVo.createList(conditionItems));
		listRateItems=new CopyOnWriteArrayList(IdNumberVo.createList(rateItems));
		listExchangeItems=new CopyOnWriteArrayList<List<IdNumberVo>>();
		listConditionModes=new CopyOnWriteArrayList<Integer>();
		listLoopFinishTimes= new CopyOnWriteArrayList<Integer>();
		if(awardItems!=null){
			String[] items = StringUtil.split(awardItems, ";");
			
			for (String item : items) {
				List<IdNumberVo2> listIdNumberVo2 = new ArrayList<IdNumberVo2>();
				String[] itemstrs = StringUtil.split(item, ",");
				for(int i=0; i<itemstrs.length; i++){
					String[] strs = StringUtil.split(itemstrs[i], "|");
					if(strs.length ==2){
						listIdNumberVo2.add(new IdNumberVo2(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), 1));
					}else if(strs.length ==3){
						listIdNumberVo2.add(new IdNumberVo2(Integer.valueOf(strs[0]), Integer.valueOf(strs[1]), Integer.valueOf(strs[2])));
					}
				}
				listAwardItems.add(listIdNumberVo2);
				
			}
		}
		if(exchangeItems!=null){
			String[] items = StringUtil.split(exchangeItems, ";");
			for (String item : items) {
				listExchangeItems.add(IdNumberVo.createList(item));
			}
		}			
		if(conditionModes!=null){
			listConditionModes=DBFieldUtil.getIntegerListBySplitter(conditionModes, "|");
		}
		if(loopFinishTimes!= null){
			listLoopFinishTimes=new CopyOnWriteArrayList<Integer>(DBFieldUtil.getIntegerListBySplitter(loopFinishTimes, "|"));
		}
	}
	private void loadRanks() {
		if(this.rankItems==null){
			return;
		}
		String[] items = StringUtil.split(this.rankItems,",");
		for (String itemStr : items) {
			RankVo rankVo = new RankVo();
			rankVo.loadProperty(itemStr, "|");
			listRankItems.add(rankVo);
		}
	}
	
	private void updateRanks() {
		if(this.listRankItems.size()<=0){
			setRankItems(null);
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<this.listRankItems.size(); i++){
			RankVo rankVo = listRankItems.get(i);
			Object[] objs = rankVo.fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			list.add(targetStr);
		}
		setRankItems(StringUtil.implode(list,","));
	}
	
	@Override
	public void saveData() {
		setConditionItems(IdNumberVo.createStr(listConditions, "|", ","));
		setRateItems(IdNumberVo.createStr(listRateItems, "|", ","));
		updateRanks();
		List<String> vals = new ArrayList<String>();
		for (List<IdNumberVo2> item : listAwardItems) {
			vals.add(IdNumberVo2.createStr(item, "|", ","));
		}
		setAwardItems(DBFieldUtil.implodStringListWithMao(vals));
		setConditionModes(DBFieldUtil.implod(listConditionModes, "|"));
		
		vals = new ArrayList<String>();
		for (List<IdNumberVo> item : listExchangeItems) {
			vals.add(IdNumberVo.createStr(item, "|", ","));
		}
		setExchangeItems(DBFieldUtil.implodStringListWithMao(vals));
		setLoopFinishTimes(DBFieldUtil.implod(listLoopFinishTimes, "|"));
		
	}
	
	public static LiveActivityPo findEntity(Integer id){
		return findRealEntity(LiveActivityPo.class,id);
	}
	
	public synchronized int addRoleRankVal(int val,RolePo rolePo) {
		int rank=0;
		boolean resort=false;
		boolean matched=false;
		for (RankVo rankVo : listRankItems) {
			if(rankVo.roleId==rolePo.getId().intValue()){
				rankVo.rolePower=val;
				matched=true;
				resort=true;
				break;
			}
		}
		boolean flag = false;
		// 竞技场排行数字越小，排的越高
		if(type.intValue()==LiveActivityType.LiveActivity_ARENA_RANK){
			if(matched==false && (listRankItems.size()<100 || val < listRankItems.get(99).rolePower)){
				flag=true;
			}
		}else{
			if(matched==false && (listRankItems.size()<100 || val> listRankItems.get(99).rolePower)){
				flag=true;
			}
		}
		
		if(flag){
			RankVo rankVo = new RankVo();
			rankVo.id=0;
			rankVo.roleId=rolePo.getId();
			rankVo.roleName=rolePo.getName();
			rankVo.type=type;
			rankVo.roleCareer=rolePo.getCareer();
			rankVo.wingStar=rolePo.getWingStar();
			rankVo.rankGold=rolePo.getGold();
			rankVo.roleLv=rolePo.getLv();
			rankVo.rankLv=0;
			rankVo.rolePower=val;
			rankVo.weaponAvatar="0";
			if(rolePo.getRpetFighterId()!=null && rolePo.getRpetFighterId().intValue() != 0){
				RpetPo rpetPo = RpetPo.findEntity(rolePo.getRpetFighterId());
				if(rpetPo != null){
					PetPo petPo = PetPo.findEntity(rpetPo.getPetId());
					if(petPo != null){
						rankVo.petId=petPo.getId();
						rankVo.petAvatar= petPo.getModel();		
//						System.out.println("petId="+rankVo.petId + "; petAvatar="+rankVo.petAvatar);
					}
				}
			}
			CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
			rankVo.makeAvatars(commonAvatarVo);
			listRankItems.add(rankVo);
			resort=true;
		}
		if(resort){
			List<RankVo> listTemp = new ArrayList<RankVo>(listRankItems);
			List<RankVo> limitTemp = new ArrayList<RankVo>();
			Collections.sort(listTemp);
			if(type.intValue() == LiveActivityType.LiveActivity_ARENA_RANK){
				int index =0;
				int size = listTemp.size();
				for(int i=size-1;i>=0;i--){
					RankVo rv =  listTemp.get(i);
//					System.out.println("i="+i+"; index="+index+" "+rv);
					rv.rankLv=index+1;
					if(rv.roleId==rolePo.getId().intValue()){
						rank=rv.rankLv;
					}
					RolePo rp = RolePo.findEntity(rv.roleId);
					if(rp != null){
						CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rp.getEquipWeaponId(), rp.getEquipArmorId(), rp.getFashion(), rp.getCareer(), rp.getWingWasHidden(),rp.getWingStar(),rp.hiddenFashions);
						rv.makeAvatars(commonAvatarVo);				
//					System.out.println(rv.rankLv+ "name:"+rv.roleName+"; weaponAvatar:"+rv.weaponAvatar+"; armorAvatar:"+rv.armorAvatar);
					}
					limitTemp.add(rv);
					if(index>=99){
						break;
					}
					index++;
				}
			}else{
				for(int i=0;i<listTemp.size();i++){
					RankVo rv =  listTemp.get(i);
					rv.rankLv=i+1;
					if(rv.roleId==rolePo.getId().intValue()){
						rank=rv.rankLv;
					}
					RolePo rp = RolePo.findEntity(rv.roleId);
					if(rp != null){
						CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rp.getEquipWeaponId(), rp.getEquipArmorId(), rp.getFashion(), rp.getCareer(), rp.getWingWasHidden(),rp.getWingStar(),rp.hiddenFashions);
						rv.makeAvatars(commonAvatarVo);				
//					System.out.println(rv.rankLv+ "name:"+rv.roleName+"; weaponAvatar:"+rv.weaponAvatar+"; armorAvatar:"+rv.armorAvatar);
					}
					limitTemp.add(rv);
					if(i>=99){
						break;
					}
				}
			}
			listRankItems=new CopyOnWriteArrayList<RankVo>(limitTemp);
		}
		return rank;
	}
	
	public Object findRoleRank(RolePo rolePo) {
		return null;
	}
	
	/** 活动奖励处理 */
	public void processResult() {
//		System.out.println("活动"+name+"处理奖励");
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		setStatus(1);
		if(IntUtil.checkInInts(type, LiveActivityType.LiveActivity_TYPE)){
			for (int i=0;i<listConditions.size();i++){
				IdNumberVo idNumberVo=listConditions.get(i);
				List<IdNumberVo2> award = listAwardItems.get(i);
				List<IdNumberVo3> list = new ArrayList<IdNumberVo3>();
				for(IdNumberVo2 inv2 : award){
					list.add(new IdNumberVo3(1, inv2.getInt1(), inv2.getInt2(), inv2.getInt3()));
				}
				if(idNumberVo.getNum()<idNumberVo.getId()){
					continue;
				}
				for(int j=idNumberVo.getId();j<=idNumberVo.getNum();j++){
					RankVo rankVo = fetchRankVo(j);
					if(rankVo!=null){
						mailService.sendAwardSystemMail(rankVo.roleId, GlobalCache.languageMap.get("key30"), GlobalCache.languageMap.get("key31")+"【"+name+"】"+GlobalCache.languageMap.get("key32")+j+GlobalCache.languageMap.get("key33"),list);
					}
				}
			}
		}
	}
	private RankVo fetchRankVo(int rank) {
		for(int i=0;i<listRankItems.size();i++){
			if(listRankItems.get(i).rankLv==rank){
				return listRankItems.get(i);
			}
		}
		return null;
	}
	
	/** 运营活动是否开启  根据运营Id取*/
	public boolean wasLiveActivityOpen(Integer liveActivityId){
		LiveActivityPo liveActivityPo = LiveActivityPo.findEntity(liveActivityId);
		if(liveActivityPo != null){
			return liveActivityPo.wasLiveActivityOpen();
		}
		return false;
	}
	
	/** 运营活动是否开启 */
	public boolean wasLiveActivityOpen(){
		boolean flag = false;
		long currentTime = System.currentTimeMillis();
		if(currentTime > getStartTime().longValue()  && currentTime < getEndTime().longValue()){
			flag = true;
		}
		return flag;
	}
	@Override
	public int compareTo(LiveActivityPo o) {
		return o.getRank()-getRank();
	}
	
	@Column(name="url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		changed("url", url, this.url);
		this.url = url;
	}
	@Column(name="lot_number")
	public String getLotNumber() {
		return lotNumber;
	}
	public void setLotNumber(String lotNumber) {
		changed("lot_number", lotNumber, this.lotNumber);
		this.lotNumber = lotNumber;
	}
	@Column(name="servers")
	public String getServers() {
		return servers;
	}
	public void setServers(String servers) {
		changed("servers", servers, this.servers);
		this.servers = servers;
	}
	@Column(name="create_time")
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		changed("create_time",createTime,this.createTime);
		this.createTime = createTime;
	}


}
