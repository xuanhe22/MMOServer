package com.games.mmo.po;

import io.netty.channel.ChannelHandlerContext;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.mapserver.bean.skilleffect.BaseSkillEffect;
import com.games.mmo.mapserver.util.BattleMsgUtil;
import com.games.mmo.mapserver.vo.BufferStatusVo;
import com.games.mmo.po.game.BuffPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.SkillPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.CheckService;
import com.games.mmo.type.ColourType;
import com.games.mmo.type.GuildType;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.GuildApplyVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.global.SiegeBidVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Establish;
import com.games.mmo.vo.xml.ConstantFile.Guild.Buildings.Building;
import com.games.mmo.vo.xml.ConstantFile.Guild.Buildings.Building.Upgrade;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.games.mmo.vo.xml.ConstantFile.Guild.Positions.Position;
import com.games.mmo.vo.xml.ConstantFile.Guild.PriestFresh.FreshQuality;
import com.storm.lib.component.chat.ChatTempate;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;
import com.storm.lib.vo.LongStringVo;
	/**
	 *
	 * 类功能: 角色装备
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "u_po_guild")

	public class GuildPo extends BaseUserDBPo {
		
	/**
	*主键
	**/

	private Integer id;
	
	/**
	 * 资金
	 */
	private Integer gold;
	
	/**
	 * 等级
	 */
	private Integer lv;
	/**
	 * 自动收人
	 */
	private Integer autoJoin=0;
	
	/**
	 * 名字
	 */
	private String name;
	
	/**
	 * 创建时间
	 */
	private Long createdTime;
	
	/**
	 * 首领ID
	 */
	private Integer leaderRoleId;
	
	/**
	 * 首领名字
	 */
	private String leaderRoleName;
	
	/**
	 * 当前人数
	 */
	private Integer memberCount=0;
	/**
	 * 战力
	 */
	private Integer battlePower=0;
	
	/**
	 * 公告
	 */
	private String board="这个家伙很懒";
	
	/**
	 * 建筑列表
	 */
	private String buildings;
	
	/**
	 * 公会事件列表
	 */
	private String events;
	
	/**
	 * 申请列表
	 */
	private String applys;	
	
	/**
	 * 会员列表
	 */
	private String members;
	
	/**
	 * 公会背包
	 */
	private String itemPack;
	
	private String bossInfo;
	
	/** 报价金币 */
	private Integer bidGold = 0;
	
	private Integer bidCount1 = 0;
	private Integer bidCount2 = 0;
	private Integer bidCount3 = 0;
	
	/** 公会boss刷新时间 */
	private  Long guildBossFlushTime = 0l;
	
	/**
	 * 祭酒刷新次数（需要每天清除）
	 */
	private Integer priestFreshCount = 0;
	
	/**
	 * 祭酒的品质（需要每天清除）
	 */
	private Integer priestFreshQuality = 1;
	
	/**
	 * 祭酒状态（需要每天清除）0:没有开启； 1：开启
	 */
	private Integer priestFreshState = 0;
	
	/**
	 * 祭祀开始时间
	 */
	private Long priestFreshStartTime = 0L;
	
	@Id @GeneratedValue
	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="gold")
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		changed("gold",gold,this.gold);
		this.gold = gold;
	}
	
	@Column(name="lv")
	public Integer getLv() {
		return lv;
	}
	public void setLv(Integer lv) {
		changed("lv",lv,this.lv);
		this.lv = lv;
	}
	
	@Column(name="events")
	public String getEvents() {
		return events;
	}
	public void setEvents(String events) {
		changed("events",events,this.events);
		this.events = events;
	}
	
	@JSONField(serialize=false)
	@Column(name="applys")
	public String getApplys() {
		return applys;
	}
	public void setApplys(String applys) {
		changed("applys",applys,this.applys);
		this.applys = applys;
	}
	
	@Column(name="auto_join")
	public Integer getAutoJoin() {
		return autoJoin;
	}
	public void setAutoJoin(Integer autoJoin) {
		changed("auto_join",autoJoin,this.autoJoin);
		this.autoJoin = autoJoin;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		changed("name",name,this.name);
		this.name = name;
	}
	
	@Column(name="created_time")
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		changed("created_time",createdTime,this.createdTime);
		this.createdTime = createdTime;
	}
	
	@Column(name="leader_role_id")
	public Integer getLeaderRoleId() {
		return leaderRoleId;
	}
	public void setLeaderRoleId(Integer leaderRoleId) {
		changed("leader_role_id",leaderRoleId,this.leaderRoleId);
		this.leaderRoleId = leaderRoleId;
	}
	
	@Column(name="leader_role_name")
	public String getLeaderRoleName() {
		return leaderRoleName;
	}
	public void setLeaderRoleName(String leaderRoleName) {
		changed("leader_role_name",leaderRoleName,this.leaderRoleName);
		this.leaderRoleName = leaderRoleName;
	}
	
	@Column(name="member_count")
	public Integer getMemberCount() {
		return memberCount;
	}
	public void setMemberCount(Integer memberCount) {
		changed("member_count",memberCount,this.memberCount);
		this.memberCount = memberCount;
	}
	
	@Column(name="battle_power")
	public Integer getBattlePower() {
		return battlePower;
	}
	public void setBattlePower(Integer battlePower) {
		changed("battle_power",battlePower,this.battlePower);
		this.battlePower = battlePower;
	}
	
	@Column(name="board")
	public String getBoard() {
		return board;
	}
	public void setBoard(String board) {
		changed("board",board,this.board);
		this.board = board;
	}
	
	@JSONField(serialize=false)
	@Column(name="buildings")
	public String getBuildings() {
		return buildings;
	}
	public void setBuildings(String buildings) {
		changed("buildings",buildings,this.buildings);
		this.buildings = buildings;
	}
	
	@Column(name="members")
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		changed("members",members,this.members);
		this.members = members;
	}
	
	
	@Column(name="itemPack")
	public String getItemPack() {
		return itemPack;
	}
	public void setItemPack(String itemPack) {
		changed("itemPack",itemPack,this.itemPack);
		this.itemPack = itemPack;
	}

	@Column(name="boss_info")
	public String getBossInfo() {
		return bossInfo;
	}
	public void setBossInfo(String bossInfo) {
		changed("boss_info",bossInfo,this.bossInfo);
		this.bossInfo = bossInfo;
	}

	@Column(name="bid_gold")
	public Integer getBidGold() {
		return bidGold;
	}
	public void setBidGold(Integer bidGold) {
		changed("bid_gold",bidGold,this.bidGold);
		this.bidGold = bidGold;
	}

	@Column(name="bid_count1")
	public Integer getBidCount1() {
		return bidCount1;
	}
	public void setBidCount1(Integer bidCount1) {
		changed("bid_count1",bidCount1,this.bidCount1);
		this.bidCount1 = bidCount1;
	}
	@Column(name="bid_count2")
	public Integer getBidCount2() {
		return bidCount2;
	}
	public void setBidCount2(Integer bidCount2) {
		changed("bid_count2",bidCount2,this.bidCount2);
		this.bidCount2 = bidCount2;
	}
	@Column(name="bid_count3")
	public Integer getBidCount3() {
		return bidCount3;
	}
	public void setBidCount3(Integer bidCount3) {
		changed("bid_count3",bidCount3,this.bidCount3);
		this.bidCount3 = bidCount3;
	}

	@Column(name="guild_boss_flush_time")
	public Long getGuildBossFlushTime() {
		return guildBossFlushTime;
	}
	public void setGuildBossFlushTime(Long guildBossFlushTime) {
		changed("guild_boss_flush_time",guildBossFlushTime,this.guildBossFlushTime);
		this.guildBossFlushTime = guildBossFlushTime;
	}

	@Column(name="priest_fresh_count")
	public Integer getPriestFreshCount() {
		return priestFreshCount;
	}
	public void setPriestFreshCount(Integer priestFreshCount) {
		changed("priest_fresh_count",priestFreshCount,this.priestFreshCount);
		this.priestFreshCount = priestFreshCount;
	}
	@Column(name="priest_fresh_quality")
	public Integer getPriestFreshQuality() {
		return priestFreshQuality;
	}
	public void setPriestFreshQuality(Integer priestFreshQuality) {
		changed("priest_fresh_quality",priestFreshQuality,this.priestFreshQuality);
		this.priestFreshQuality = priestFreshQuality;
	}
	@Column(name="priest_fresh_start_time")
	public Long getPriestFreshStartTime() {
		return priestFreshStartTime;
	}
	public void setPriestFreshStartTime(Long priestFreshStartTime) {
		changed("priest_fresh_start_time",priestFreshStartTime,this.priestFreshStartTime);
		this.priestFreshStartTime = priestFreshStartTime;
	}
	
	@Column(name="priest_fresh_state")
	public Integer getPriestFreshState() {
		return priestFreshState;
	}
	public void setPriestFreshState(Integer priestFreshState) {
		changed("priest_fresh_state",priestFreshState,this.priestFreshState);
		this.priestFreshState = priestFreshState;
	}

	public CopyOnWriteArrayList<LongStringVo> listEvents=new CopyOnWriteArrayList<LongStringVo>();
	
	public CopyOnWriteArrayList<IdNumberVo> listBuildings=new CopyOnWriteArrayList<IdNumberVo>();
	
	public CopyOnWriteArrayList<GuildApplyVo> listApplys=new CopyOnWriteArrayList<GuildApplyVo>();
	
	public CopyOnWriteArrayList<GuildMemberVo> listMembers=new CopyOnWriteArrayList<GuildMemberVo>();
	
	/**
	 * boss信息
	 * int1：lv
	 * int2：是否可以打
	 * int3：状态：1杀了，0没杀
	 */
	public CopyOnWriteArrayList<IdNumberVo2> listBossInfo = new CopyOnWriteArrayList<IdNumberVo2>();
	/**
	 * 公会背包
	 */
	public CopyOnWriteArrayList<IdNumberVo> listItemPack = new CopyOnWriteArrayList<IdNumberVo>();
	
	private void updateGuildMemberVos() {
		if(this.listMembers.size()<=0){
			this.members = null;
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<this.listMembers.size(); i++){
			GuildMemberVo guildMemberVo = listMembers.get(i);
			Object[] objs = guildMemberVo.fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			list.add(targetStr);
		}
		setMembers(StringUtil.implode(list,","));
	}
	
	private void loadGuildMemberVos() {
		if(this.members==null){
			return;
		}
		String[] items = StringUtil.split(this.members,",");
		for (String itemStr : items) {
			GuildMemberVo guildMemberVo = new GuildMemberVo();
			guildMemberVo.loadProperty(itemStr, "|");
			listMembers.add(guildMemberVo);
		}
	}
	
	private void updateGuildApplyVos() {
		if(this.listApplys.size()<=0){
			this.applys = null;
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<this.listApplys.size(); i++){
			GuildApplyVo guildApplyVo = listApplys.get(i);
			Object[] objs = guildApplyVo.fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			list.add(targetStr);
		}
		setApplys(StringUtil.implode(list,","));
	}
	
	private void loadGuildApplyVos() {
		if(this.applys==null){
			return;
		}
		String[] items = StringUtil.split(this.applys,",");
		for (String itemStr : items) {
			GuildApplyVo guildApplyVo = new GuildApplyVo();
			guildApplyVo.loadProperty(itemStr, "|");
			listApplys.add(guildApplyVo);
		}
	}
	
	/**
	 * 加载公会背包
	 */
	private void loadGuildItemPack(){
		if(this.itemPack == null){
			return;
		}
		listItemPack = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(itemPack)) ;
	}
	
	public void updateGuildItemPack(){
		String str = IdNumberVo.createStr(listItemPack, "|", ",");
		setItemPack(str);
	}
	
	public void loadGuildBuildings(){
		if(this.buildings==null){
			return;
		}
		listBuildings = new CopyOnWriteArrayList<IdNumberVo>(IdNumberVo.createList(buildings)) ;
	}
	
	public void updateGuildBuildings(){
		String str = IdNumberVo.createStr(listBuildings, "|", ",");
		setBuildings(str);
	}
	
	public void loadGuildEvents(){
		if(this.events == null)
		{
			return;
		}
		listEvents = new CopyOnWriteArrayList<LongStringVo>(LongStringVo.createComplexList(events)); 
	}
	public void updateGuildEvents(){
		String str = LongStringVo.createStr(listEvents, "|", "@");
		setEvents(str);
	}
	
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			listBossInfo = new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(bossInfo));
			loadGuildApplyVos();
			loadGuildMemberVos();
			loadGuildItemPack();
			loadGuildBuildings();
			loadGuildEvents();
			unChanged();
			loaded =true;
		}
	}
	
	@Override
	public void saveData() {
		setBossInfo(IdNumberVo2.createStr(listBossInfo, "|", ","));
		updateGuildApplyVos();
		updateGuildMemberVos();
		updateGuildItemPack();
		updateGuildBuildings();
		updateGuildEvents();
	}

	
	public static GuildPo findEntity(Integer id){
		return findRealEntity(GuildPo.class,id);
	}
	
	/**
	 * 解散公会
	 */
	public void dimiss() {
		for (GuildMemberVo guildMemberVo : listMembers) {
			RolePo targetRole = RolePo.findEntity(guildMemberVo.roleId);
			if(targetRole!=null){
				targetRole.setGuildId(0);
				targetRole.setGuildName(null);				
				if(targetRole.fighter != null){
					targetRole.fighter.changeGuildName("");
					targetRole.fighter.changeGuildPosition();
				}
				targetRole.setGuildExitTime(System.currentTimeMillis());
				targetRole.sendUpdateClientGuildExitTime();
				updateFighterGuildName(targetRole);
				targetRole.sendUpdateGuildInfor();
				targetRole.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key2275"));
			}
		}
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		chatService.roomDimissChannel(id, ChatTempate.chatGuildRooms);
		BaseDAO.instance().remove(this);
	}
	
	/**
	 * 玩家公会变更通知地图上周边玩家
	 */
	private void updateFighterGuildName(RolePo rolePo)
	{
		if(rolePo.fighter != null && rolePo.fighter.mapRoom != null && rolePo.fighter.cell != null)
		{
			GuildPo guildPo = GuildPo.findEntity(rolePo.getGuildId());
			if(rolePo!=null && rolePo.fighter!=null){
				rolePo.fighter.changeWasSiegeBid();
				rolePo.fighter.changeGuildPosition();
				if(guildPo==null){
					rolePo.fighter.changeGuildName("");
				}
				else{
					rolePo.fighter.changeGuildName(guildPo.getName());
				}

			}
			BattleMsgUtil.abroadGuildName(rolePo.fighter);
		}
	}
	
	/**
	 * 删除公会会员
	 * @param roleId
	 */
	public void removeMember(Integer roleId, Integer typeId) {
		for (GuildMemberVo guildMemberVo : listMembers) {
			if(roleId==guildMemberVo.roleId.intValue()){
				RolePo targetRole = RolePo.findEntity(roleId);
				int guildId = targetRole.getGuildId();
				targetRole.setGuildId(0);
				targetRole.setGuildName(null);
				if(targetRole.fighter != null){
					targetRole.fighter.changeGuildName("");
					targetRole.fighter.changeGuildPosition();				
				}
				listMembers.remove(guildMemberVo);
				targetRole.setGuildExitTime(System.currentTimeMillis());
				targetRole.sendUpdateClientGuildExitTime();
				targetRole.sendUpdateGuildInfor();
				ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
				RoleTemplate roleTemplate = (RoleTemplate) BeanUtil.getBean("roleTemplate");
				ChannelHandlerContext ioSession = targetRole.fetchSession();
				chatService.leaveRoomChannel(guildId, targetRole.getIuid(), ioSession, ChatTempate.chatGuildRooms);
				// 广播会员开除信息
				StringBuffer sb = new StringBuffer();
				

				
				if(typeId.intValue() == GuildType.GUILD_NOTICE_TYPE_EXIT)
				{
					String str =GlobalCache.fetchLanguageMap("key2701");
					String content = MessageFormat.format(str,targetRole.getName(),getName());
					sb.append(content);					
				}
				else if(typeId.intValue() == GuildType.GUILD_NOTICE_TYPE_EXPELLED)
				{
					sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key2704")).append(this.name).append(GlobalCache.fetchLanguageMap("key2705"));	
					targetRole.sendUpdateShowMsg(sb.toString());
				}
				updateFighterGuildName(targetRole);
				chatService.sendGuild(sb.toString(), this.id);
				writeEvent(sb.toString());
				
				if(targetRole!=null && targetRole.fighter!=null){
					targetRole.fighter.changeWasSiegeBid();
					targetRole.fighter.changeGuildPosition();
				}
				
				break;
			}
		}
		setMemberCount(listMembers.size());
		setBattlePower(fetchGuildBattlePower());
	}
	/**
	 * 添加会员
	 * @param roleId
	 */
	public void addMember(Integer roleId) {
		Establish establish =XmlCache.xmlFiles.constantFile.guild.establish;
		List<Integer> list =StringUtil.getListByStr(establish.num,"|");
		Integer num = list.get(lv-1);
		if(getMemberCount().intValue() >= num){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2279"));
		}
		RolePo targetRole = RolePo.findEntity(roleId);
		targetRole.setGuildId(getId());
		targetRole.setGuildName(getName());
		if(targetRole.getGuildPriestState().intValue() !=0){
			targetRole.setGuildPriestState(2);
		}
		GuildMemberVo guildMemberVo = new GuildMemberVo();
		if(leaderRoleId.intValue() == roleId.intValue()){
			guildMemberVo.guildPosition= GuildType.GUILD_POSITION_CHAIRMAN;
		}else{
			guildMemberVo.guildPosition= GuildType.GUILD_POSITION_MEMBER;
		}
		guildMemberVo.roleBattlePower=targetRole.getBattlePower();
		guildMemberVo.roleCareer=targetRole.getCareer();
		guildMemberVo.roleId=targetRole.getId();
		guildMemberVo.roleLv=targetRole.getLv();
		guildMemberVo.roleName=targetRole.getName();
		guildMemberVo.wasOnline=1;
		guildMemberVo.guildHonor=0;
		listMembers.add(guildMemberVo);
		setMemberCount(listMembers.size());
		setBattlePower(fetchGuildBattlePower());
		targetRole.sendUpdateGuildInfor();
		targetRole.sendUpdateAddGuildTips();
		updateFighterGuildName(targetRole);
		// 加入公会聊天频道
		ChatService chatService = (ChatService) BeanUtil.getBean("chatService");
		ChannelHandlerContext ioSession = targetRole.fetchSession();
		chatService.joinRoomChannel(targetRole.getGuildId(),targetRole.getIuid(), ioSession, ChatTempate.chatGuildRooms);
		// 广播会员加入公会信息
		StringBuffer sb = new StringBuffer();
		sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key2274")).append(this.name).append(GlobalCache.fetchLanguageMap("key254"));
		chatService.sendGuild(sb.toString(), id);
		writeEvent(sb.toString());

//		System.out.println("===" + listEvents);
	}
	
	public void writeEvent(String string) {

		listEvents.add(new LongStringVo(fetchEventsMaxIndex(), string));
		if(listEvents.size()>40){
			listEvents.remove(0);
		}
	}
	/**
	 * 查找公会会员
	 * @return
	 */
	public GuildMemberVo fetchGuildMemberVoInfo(Integer roleId){
		GuildMemberVo gmv = null;
		for (GuildMemberVo guildMemberVo : listMembers) {
			if(roleId.intValue()==guildMemberVo.roleId.intValue()){
				gmv = guildMemberVo;
				break;
			}
		}
		return gmv;
	}
	
	/**
	 * 修改会员职务
	 */
	public void updateGuildMemberPosition(Integer roleId, Integer position){
		GuildMemberVo gmv = fetchGuildMemberVoInfo(roleId);
		gmv.guildPosition = position;
		updateFighterGuildName(RolePo.findEntity(roleId));
		
		if(gmv!=null){
			RolePo rolePo = RolePo.findEntity(gmv.roleId);
			if(rolePo!=null && rolePo.fighter!=null){
				rolePo.fighter.changeGuildPosition();
			}
		}
		

	}
	
	/**
	 * 根据职务查找会员数量
	 * @param position
	 * @return
	 */
	public int fetchGuildMemberNumByPosition(Integer position){
		int num = 0;
		for(GuildMemberVo gmv : listMembers){
			if(gmv.guildPosition.intValue() == position.intValue()){
				num++;
			}
		} 
		return num;
	}
	
	
	/**
	 * 获得公会战力
	 * @return
	 */
	public Integer fetchGuildBattlePower(){
		int totalBattlePower = 0;
		for(GuildMemberVo gmv : listMembers){
			if(gmv.roleBattlePower != null){
				totalBattlePower+=gmv.roleBattlePower;
			}
		} 
		return totalBattlePower;
	}
	
	/** 检查金币并消耗 */
	public void checkHasAndConsumeGold(int val) {
		checkHasGuildGold(val);
		consumeGuildGold(val);
	}
	
	public void checkHasAndConsumeBidGold(int val){
		checkHasGuildBidGold(val);
		consumeGuildBidGold(val);
	}
	
	public void checkHasAndConsumeGuildItem(int itemId, int num){
		checkHasGuildItem(itemId, num);
		consumeGuildItem(itemId, num);
	}
	
	public void consumeGuildGold(int val) {
		adjustMoney(-val);
	}
	
	public void consumeGuildBidGold(int val) {
		adjustBidGold(-val);
	}
	
	private void adjustMoney(int i) {
		setGold(gold+i);
	}
	
	private void adjustBidGold(int i){
		setBidGold(bidGold + i);
		if(bidGold < 0){
			setBidGold(0);
		}
	}
	
	public void adjustPriestFreshCount(int num){
		setPriestFreshCount(priestFreshCount + num);
		if(priestFreshCount < 0){
			setPriestFreshCount(0);
		}
	}
	
	public void checkHasGuildGold(int val) {
		if(val>gold){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key27"));
		}
	}
	
	public void checkHasGuildBidGold(int val) {
		if(val>bidGold){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key28"));
		}
	}
	
	public void addMoney(int i){
		adjustMoney(i);
	}
	
	public void addBidGold(int i){
		adjustBidGold(i);
	}
	
	public void addBidCount1(int i){
		setBidCount1(bidCount1 + i);
	}
	
	public void addBidCount2(int i){
		setBidCount2(bidCount2 + i);
	}
	
	public void addBidCount3(int i){
		setBidCount3(bidCount3 + i);
	}
	
	/**
	 * 添加公会道具
	 * @param itemId
	 */
	public void addGuildItem(Integer itemId, Integer num){
//		System.out.println("itemId == " +itemId);
		if(itemId == null){
			return;
		}
//		System.out.println("===="+listItemPack);
		for(IdNumberVo iv : listItemPack){
//			System.out.println(iv.getId().intValue() +"||"+ itemId.intValue());
			if(iv.getId().intValue() == itemId.intValue()){
				iv.addNum(num);
				return;
			}
		}
	}
	
	/**
	 * 检查公会道具数量
	 * @param itemId
	 * @param num
	 */
	 public void checkHasGuildItem(int itemId, int num){
		for(IdNumberVo inv : listItemPack){
			if(inv.getId().intValue()==itemId){
				if(inv.getNum() < num){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key29"));
					return;
				}
			}
		}
	 }

	/**
	 * 消耗公会道具
	 * @param itemId
	 * @param num
	 */
	public void consumeGuildItem(Integer itemId, Integer num){
		for(IdNumberVo inv : listItemPack){
			if(inv.getId().intValue()==itemId){
				if(inv.getNum()>=num){
					inv.setNum(inv.getNum()-num);
					return;
				}
			}
		}
	}
	
	/**
	 * 获取会员列表
	 * @return
	 */
	public List<GuildMemberVo> fetchListMembers(){
		for(GuildMemberVo gmv : listMembers)
		{
			RolePo rolePo = RolePo.findEntity(gmv.roleId);
			if(RoleTemplate.roleIdIuidMapping.containsKey(gmv.roleId))
			{
				gmv.roleLv = rolePo.getLv();
				gmv.roleBattlePower = rolePo.getBattlePower();
				gmv.wasOnline = 1;
			}
			else
			{
				gmv.wasOnline = 0;
			}
			
			if(rolePo.getLastLogoffTime() != null){
				long offlineTime = System.currentTimeMillis() - rolePo.getLastLogoffTime();
				if(offlineTime <= 0){
					offlineTime=0;
				}
				gmv.offlineTime = String.valueOf(offlineTime);				
			}
		}
		return listMembers;
	}
	
	/**
	 * 获取事件最大id
	 * @return
	 */
	public Long fetchEventsMaxIndex(){
//		long index = 1;
//		LongStringVo ls = null;
//		if(listEvents.size() != 0){
//			ls = listEvents.get(listEvents.size() - 1);			
//		}
//		if(ls != null){
//			index = ls.getId().intValue() + 1;			
//		}
		long index = System.currentTimeMillis();
		return index;
	}
	
	/**
	 * 获取Boss对象
	 * @param copySceneConfId
	 * @return
	 */
	public static Boss fetchBossInfoByCopySceneConfId(Integer copySceneConfId){
		List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
		for(Boss boss : listBoss){
			if(copySceneConfId.intValue() == boss.copysceneconfid.intValue()){
				return boss;
			}
		}
		return null;
	}
	
	/**
	 * 给所有会员推送boss信息
	 */
	public void sendUpdateGuildBossInfo(){
		for(GuildMemberVo gmv : listMembers){
			RolePo rolePo = RolePo.findEntity(gmv.roleId);
			if(rolePo != null){
				rolePo.sendUpdateGuildBossInfo(listBossInfo);				
			}
		}
	}
	
	/** 领取公会boss奖励*/
	public synchronized void fetchGuildBossAward(Integer copysceneconfId, RolePo rolePo){
		IdNumberVo2 currentINV2 = IdNumberVo2.findIdNumber(copysceneconfId, listBossInfo);
		if(currentINV2.getInt3() != 1){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key119")+copysceneconfId);
		}	
		IdNumberVo currentINV = IdNumberVo.findIdNumber(copysceneconfId, rolePo.listGuildBossAward);
		if(currentINV.getNum() != 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key120")+copysceneconfId);
		}
		Boss boss = rolePo.fetchGuildBoss(copysceneconfId);
		rolePo.addItem(IdNumberVo.createList(boss.award), 1,GlobalCache.fetchLanguageMap("key2608"));
		for(IdNumberVo idNumberVo : rolePo.listGuildBossAward){
			if(idNumberVo.getId().intValue() == copysceneconfId){
				idNumberVo.setNum(1);
				break;
			}
		}
	}
	
	/** 捐献金币*/
	public synchronized void guildContributeGold(Integer num, RolePo rolePo){
		int addGold = num.intValue()*20000;
		rolePo.publicCheckHasResource(RoleType.RESOURCE_PRIORITY_BINDGOLD_THEN_GOLD,addGold);
		LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_BINDGOLDTHENGOLD, 0, -addGold, GlobalCache.fetchLanguageMap("key2347"), "");
		rolePo.checkHasAndConsumeBindGoldThenGold(addGold);
		
		int baseGH = num*200;
		//已经有捐献绑金的日志了，这个行为日志看不懂 去掉
//		LogUtil.writeLog(rolePo, 219, num*20000, rolePo.getBindGold(), 0, GlobalCache.fetchLanguageMap("key2348"), "");
		GuildMemberVo guildMemberVo = fetchGuildMemberVoInfo(rolePo.getId());
		if((baseGH + rolePo.getDailyGuildContributeGoldCount().intValue()) <= 2000  )
		{
			rolePo.adjustPublicGuildHonor(baseGH);
			rolePo.adjustDailyGuildContributeGoldCount(baseGH);
			rolePo.adjustGuildTodayContributed(baseGH);
			if(guildMemberVo != null){
				guildMemberVo.adjustHonor(baseGH);				
			}
			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, baseGH, GlobalCache.fetchLanguageMap("key2349"), "");
		}
		else
		{
			baseGH = 2000 - rolePo.getDailyGuildContributeGoldCount().intValue();
			if(baseGH > 0){
				rolePo.adjustPublicGuildHonor(baseGH);
				rolePo.adjustDailyGuildContributeGoldCount(baseGH);
				rolePo.adjustGuildTodayContributed(baseGH);
				if(guildMemberVo != null){
					guildMemberVo.adjustHonor(baseGH);				
				}
				LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, baseGH, GlobalCache.fetchLanguageMap("key2350"), "");
			}else{
				// TODO 【业务标记】捐献成功，金币功勋已达上限，未获得功勋值
				rolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key2288"));
			}
			
		}
		StringBuffer sb = new StringBuffer();
		sb.append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key110")).append(addGold).append("!");
		sb.append(GlobalCache.fetchLanguageMap("key111")).append(addGold/100).append(GlobalCache.fetchLanguageMap("key112"));
		writeEvent(sb.toString());
		addMoney(addGold/100);
	}
	
	public synchronized void guildContributeItems(Integer itemId, RolePo rolePo, Integer num){
		
		ItemPo itemPo = ItemPo.findEntity(itemId);
		rolePo.checkHasItem(itemId, num);
		if(rolePo.getDailyGuildContributeItemCount().intValue() < 200){
			GuildMemberVo guildMemberVo = fetchGuildMemberVoInfo(rolePo.getId());
			if((rolePo.getDailyGuildContributeItemCount().intValue() + num.intValue()) > 200){
				int currentNum = 200 - rolePo.getDailyGuildContributeItemCount();
				rolePo.adjustGuildTodayContributed(20*currentNum);
				rolePo.adjustPublicGuildHonor(20*currentNum);
				if(guildMemberVo != null){
					guildMemberVo.adjustHonor(20*currentNum);				
				}
			}else{
				rolePo.adjustGuildTodayContributed(20*num);
				rolePo.adjustPublicGuildHonor(20*num);		
				if(guildMemberVo != null){
					guildMemberVo.adjustHonor(20*num);				
				}
			}
			

			LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, 20*num, GlobalCache.fetchLanguageMap("key2351"), "");
			int numLeft=rolePo.fetchItemCount(itemId);
			LogUtil.writeLog(rolePo, 219, itemId, num, numLeft, GlobalCache.fetchLanguageMap("key2352"), "");
		}else{
			// 捐献成功，道具功勋已达上限，未获得功勋值
			rolePo.sendUpdateShowMsg(GlobalCache.fetchLanguageMap("key2288"));
		}
		rolePo.checkHasAndConsumeItem(itemId, num,GlobalCache.fetchLanguageMap("key104"));
		rolePo.adjustDailyGuildContributeItemCount(num);
		addGuildItem(itemId, num);	
		StringBuffer sb = new StringBuffer();
		sb.append(rolePo.getName()).append(GlobalCache.fetchLanguageMap("key104")).append(itemPo.getName()).append("!");
		writeEvent(sb.toString());
	}
	/**
	 * 兑换道具
	 * @param itemId
	 * @return
	 */
	public synchronized void guildExchangeItem(Integer itemId,RolePo rolePo){
		rolePo.checkItemPackFull(1);
		List<List<Integer>> itemList =ExpressUtil.buildBattleExpressList(XmlCache.xmlFiles.constantFile.guild.exchange.items);
		// 获得当前的兑换列表
		List<Integer> currentList = new ArrayList<Integer>();	
		for(List<Integer> list : itemList){
			if(list.get(0).intValue() == itemId.intValue()){
				currentList = list;
				break;
			}
		}
		if(currentList.size() == 0){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key101"));
		}
		if(getLv().intValue() < currentList.get(1).intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key102"));
		}
		int count = 0;
//				System.out.println("rolePo.listGuildTodayExchangeItems="+rolePo.listGuildTodayExchangeItems);
		for(IdNumberVo inv : rolePo.listGuildTodayExchangeItems){
			if(inv.getId().intValue()==itemId.intValue()){
				count = inv.getNum();
				break;
			}
		}
		if(count >= currentList.get(2).intValue()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key103"));
		}
		
		rolePo.checkHasAndConsumeResource(RoleType.RESOURCE_GUILD_HONOR, currentList.get(3));
		LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, -currentList.get(3), GlobalCache.fetchLanguageMap("key2353"), "");
		
		for (IdNumberVo inv : rolePo.listGuildTodayExchangeItems) {
			if(inv.getId().intValue()==itemId){
				inv.addNum(1);
				break;
			}
		}
		rolePo.addItem(itemId, 1,1);
		LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_ITEM, itemId, 1, GlobalCache.fetchLanguageMap("key2353"), "");
	}
	
	/**
	 * 升级建筑
	 * @param buildingId
	 * @return
	 */
	public synchronized void guildUpgradeBuilding(Integer buildingId,RolePo rolePo){
		List<Building> building = XmlCache.xmlFiles.constantFile.guild.buildings.building;
		if(buildingId == null || buildingId.intValue()<1 || buildingId.intValue() > building.size()){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key105"));
		}
		
		for (IdNumberVo idNumberVo : listBuildings) {
			if(buildingId.intValue()==idNumberVo.getId().intValue()){
				if(idNumberVo.getNum().intValue() > 0  &&  idNumberVo.getNum().intValue() < 10){
					Building bd =  building.get(idNumberVo.getId().intValue()-1);
					Upgrade upgrade = bd.upgrade.get(idNumberVo.getNum().intValue()-1);
					
					List<List<Integer>> itemList = ExpressUtil.buildBattleExpressList(upgrade.itemList);
					// 先验证
					checkHasGuildGold(upgrade.costGold);
					for(List<Integer> list : itemList){
						checkHasGuildItem(list.get(0), list.get(1));
					}
					// 再消耗金币道具
					consumeGuildGold(upgrade.costGold);
					for(List<Integer> list : itemList){
						consumeGuildItem(list.get(0), list.get(1));
					}
					idNumberVo.addNum(1);
					LogUtil.writeLog(rolePo, 218, idNumberVo.getNum().intValue()-1, getGold(), 0, GlobalCache.fetchLanguageMap("key2354")+buildingId, "");
					if(idNumberVo.getId().intValue() == 1){
						setLv(idNumberVo.getNum());
					}
					
					StringBuffer sb = new StringBuffer();
					sb.append(ColourType.COLOUR_WHITE).append(GlobalCache.fetchLanguageMap("key20")).append(getName()).append(GlobalCache.fetchLanguageMap("key106")).append(bd.name).append(GlobalCache.fetchLanguageMap("key107")).append(idNumberVo.getNum()).append(GlobalCache.fetchLanguageMap("key108"));
					ChatService chatService=ChatService.instance();
					chatService.sendGuild(sb.toString(), getId());
					writeEvent(sb.toString());
					break;
				}
			}
		}
	}
	/**
	 * 使用公会技能
	 * @param buildingId
	 * @return
	 */
	public synchronized void guildBuildingSkill(Integer buildingId,RolePo rolePo){
		if(rolePo==null || rolePo.fighter == null){
			return;
		}
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		List<Building> building = XmlCache.xmlFiles.constantFile.guild.buildings.building;
		for (IdNumberVo idNumberVo : listBuildings) {
			if(buildingId.intValue()==idNumberVo.getId().intValue()){
				Building bd =  building.get(idNumberVo.getId().intValue()-1);
				Upgrade upgrade = bd.upgrade.get(idNumberVo.getNum().intValue()-1);
				checkService.checkExisSkillPo(upgrade.skillId);
				rolePo.checkHasAndConsumeGuildHonor(upgrade.skillCostHonor);
				SkillPo skillPo = SkillPo.findEntity(upgrade.skillId);
				Integer buffId = BaseSkillEffect.fetchBuffIdBySkillId(skillPo.getId());
				BufferStatusVo bufferStatusVo = rolePo.fighter.findBufferStatus(buffId);
				if(bufferStatusVo != null){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key2595"));
				}
				LogUtil.writeLog(rolePo, 1, ItemType.LOG_TYPE_GUILDPOINT, 0, -upgrade.skillCostHonor, GlobalCache.fetchLanguageMap("key2346")+idNumberVo.getId().intValue(), "");
				rolePo.fighter.makeSkillEffect(upgrade.skillId, rolePo.fighter.mapUniqId,rolePo.fighter.mapUniqId.toString(),0,null,null);
				checkService.checkExisBuffPo(buffId);
				BuffPo buffPo = BuffPo.findEntity(buffId);
//				StringBuilder sb = new StringBuilder();
//				sb.append(GlobalCache.fetchLanguageMap("key2281")).append(buffPo.getDurationValexp()/60).append(GlobalCache.fetchLanguageMap("key2282")).append(skillPo.getName());
				String str = MessageFormat.format(GlobalCache.fetchLanguageMap("key2613"), buffPo.getDurationValexp()/60, skillPo.getName());
				
				chatService.sendSystemMsg(str, rolePo.getId());
				rolePo.sendUpdateShowMsg(str);
				break;
			}
		}
	}
	/**
	 * 会员任命
	 * @param roldId 玩家Id
 	 * @param position 职位
	 * @return
	 */
	public synchronized void guildAppoint(Integer targetRoleId, Integer position,RolePo rolePo){
		CheckService checkService = (CheckService) BeanUtil.getBean("checkService");
		ChatService chatService=(ChatService) BeanUtil.getBean("chatService");
		RolePo targetRole = RolePo.findEntity(targetRoleId);
		GuildMemberVo gmv = fetchGuildMemberVoInfo(rolePo.getId());
		GuildMemberVo targetGmv = fetchGuildMemberVoInfo(targetRoleId);
		if(gmv == null || targetGmv == null){
			ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key25")+"targetRoleId："+targetRoleId);
		}
		if(gmv.guildPosition.intValue() == GuildType.GUILD_POSITION_CHAIRMAN && position.intValue() == GuildType.GUILD_POSITION_CHAIRMAN){
			LogUtil.writeLog(rolePo, 216, targetRoleId, 0, 0, GlobalCache.fetchLanguageMap("key2344"), "");
			updateGuildMemberPosition(rolePo.getId(), GuildType.GUILD_POSITION_MEMBER);
			setLeaderRoleId(targetRole.getId());
			setLeaderRoleName(targetRole.getName());
			updateGuildMemberPosition(targetRoleId, position);
		}else{
			if(gmv.guildPosition.intValue() >= position.intValue() && gmv.guildPosition.intValue() < targetGmv.guildPosition.intValue()){
				ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key114"));
			}
			if(position<4){
				Position positionInfo = XmlCache.xmlFiles.constantFile.guild.positions.position.get(position.intValue() - 1);
				int num = fetchGuildMemberNumByPosition(position);
				if(num >= positionInfo.allow){
					ExceptionUtil.throwConfirmParamException(GlobalCache.fetchLanguageMap("key115"));
				}
			}
			LogUtil.writeLog(rolePo, 217, targetRoleId, position, gmv.guildPosition, GlobalCache.fetchLanguageMap("key2345"), "");
			updateGuildMemberPosition(targetRoleId, position);
		}
		
		// 广播会员任命信息
		StringBuffer sb = new StringBuffer();
		sb.append(targetRole.getName()).append(GlobalCache.fetchLanguageMap("key116")).append(GuildType.fetchNameByMemeberType(position));
		chatService.sendGuild(sb.toString(), getId());
	}
	
	/** 清除会员战功 */
	public void cleanGuildMemberVoHonor(){
		for(GuildMemberVo guildMemberVo : listMembers){
			guildMemberVo.guildHonor=0;
		}
	}
	
	/**
	 * 获取公会祭祀品质
	 * @param gouldLv
	 * @param type 0:免费； 1：金币
	 * @return
	 */
	public FreshQuality fecthFreshQuality(int type){
		FreshQuality freshQuality = null;
		List<FreshQuality> freshQualitys = XmlCache.xmlFiles.constantFile.guild.priestFresh.freshQuality;
		List<List<Integer>> baseList = new ArrayList<List<Integer>>();
		for (FreshQuality fq : freshQualitys) {
			if(lv >= fq.needGuildLv){
				List<Integer> list = new ArrayList<Integer>();
				list.add(fq.id);
				list.add(1);
				if(type == 0){
					list.add(fq.freeWeight);
				}
				else if(type == 1){
					list.add(fq.weight);					
				}
				baseList.add(list);				
			}
		}
		IdNumberVo qualityId = RandomUtil.calcWeightOverCardAward(baseList,null);
		freshQuality = freshQualitys.get(qualityId.getId()-1);
		return freshQuality;
	}
	
	
	/**
	 * 根据品质查找祭酒对象
	 * @param quality
	 * @return
	 */
	public FreshQuality fecthFreshQualityByQuality(int quality){
		List<FreshQuality> freshQualitys = XmlCache.xmlFiles.constantFile.guild.priestFresh.freshQuality;
		for (FreshQuality fq : freshQualitys) {
			if(quality == fq.id){
				return fq; 
			}
		}
		return null;
	}
	
}
