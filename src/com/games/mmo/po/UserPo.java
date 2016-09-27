package com.games.mmo.po;

import io.netty.channel.ChannelHandlerContext;

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
import com.games.mmo.cache.XmlCache;
import com.games.mmo.service.CheckService;
import com.games.mmo.service.RoleService;
import com.games.mmo.service.UserService;
import com.games.mmo.util.LogUtil;
import com.games.mmo.util.SessionUtil;
import com.games.mmo.vo.role.RoleBriefVo;
import com.games.mmo.vo.xml.ConstantFile.DiamondBasins.DiamondBasin;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.component.remoting.BasePushTemplate;
import com.storm.lib.template.RoleTemplate;
import com.storm.lib.util.BaseSessionUtil;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DBFieldUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo2;

@Entity
@Table(name = "u_po_user")
public class UserPo extends BaseUserDBPo {
	private Integer id;
	private Long lastLoginTime=System.currentTimeMillis();
	private Long lastLogoffTime=System.currentTimeMillis();
	private Long createTime=System.currentTimeMillis();
	private String iuid;
	private String pssd;
	private Integer diamond;
	private Integer vipLv;
	private Integer cumulativeRechargeNum;
	
	/**
	 * 渠道key
	 */
	private String channelKey;  
	
	/**
	 * 留存信息
	 */
	private String keepInformation;  

	
	/**
	 * 设备ID
	 */
	private String deviceId;
	
	/**
	 * 服务器ID
	 */
	private Integer serverId;
	
	/**
	 * 摇一摇(抽取钻石)
	 */
	private String diamondBasins;
	
	/**
	 * facebook绑定
	 */
	private String token;
	
	@JSONField(serialize=false)
	private Long loginResetTime=System.currentTimeMillis();
	
	@JSONField(serialize=false)
	private String roleBriefs;
	
	/**
	 * 用户首次注册
	 */
	@JSONField(serialize=false)
	private Integer firstRegisterState;
	
	/**
	 * 记录是否奖励钻石（清档之后老玩家奖励）
	 */
	private Integer wasRewardDiamond;
	
	private String idfa;
	
	private String packageName;
	
	/**
	 * 角色
	 */
	@JSONField(serialize=false)
	public List<RoleBriefVo> listRoleBriefVo = new CopyOnWriteArrayList<RoleBriefVo>();
	
	/** 客户端排序显示 */
	public List<RoleBriefVo> clientListRoleBriefVo = new ArrayList<RoleBriefVo>();
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="last_login_time")
	public Long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Long lastLoginTime) {
		changed("last_login_time",lastLoginTime,this.lastLoginTime);
		this.lastLoginTime = lastLoginTime;
	}

	@Column(name="last_logoff_time")
	public Long getLastLogoffTime() {
		return lastLogoffTime;
	}

	public void setLastLogoffTime(Long lastLogoffTime) {
		changed("last_logoff_time",lastLogoffTime,this.lastLogoffTime);
		this.lastLogoffTime = lastLogoffTime;
	}

	@Column(name="create_time")
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		changed("create_time",createTime,this.createTime);
		this.createTime = createTime;
	}

	@Column(name="iuid")
	public String getIuid() {
		return iuid;
	}

	public void setIuid(String iuid) {
		changed("iuid",iuid,this.iuid);
		this.iuid = iuid;
	}

	@Column(name="pssd")
	public String getPssd() {
		return pssd;
	}

	public void setPssd(String pssd) {
		changed("pssd",pssd,this.pssd);
		this.pssd = pssd;
	}

	@Column(name="diamond")
	public Integer getDiamond() {
		return diamond;
	}

	public void setDiamond(Integer diamond) {
		changed("diamond",diamond,this.diamond);
		this.diamond = diamond;
	}

	@Column(name="vip_lv")
	public Integer getVipLv() {
		return vipLv;
	}

	public void setVipLv(Integer vipLv) {
		changed("vip_lv",vipLv,this.vipLv);
		this.vipLv = vipLv;
	}

	
	@Column(name="role_briefs")
	public String getRoleBriefs() {
		return roleBriefs;
	}

	public void setRoleBriefs(String roleBriefs) {
		changed("role_briefs",roleBriefs,this.roleBriefs);
		this.roleBriefs = roleBriefs;
	}
	
	@Column(name = "channel_key")
	public String getChannelKey() {
		return channelKey;
	}

	public void setChannelKey(String channelKey) {
		changed("channel_key", channelKey, this.channelKey);
		this.channelKey = channelKey;
	}

	
	
	@Column(name="device_id")
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		changed("device_id", deviceId, this.deviceId);
		this.deviceId = deviceId;
	}

	
	
	@Column(name="first_register_state")
	public Integer getFirstRegisterState() {
		return firstRegisterState;
	}

	public void setFirstRegisterState(Integer firstRegisterState) {
		changed("first_register_state", firstRegisterState, this.firstRegisterState);
		this.firstRegisterState = firstRegisterState;
	}
	
	@Column(name="cumulative_recharge_num")
	public Integer getCumulativeRechargeNum() {
		return cumulativeRechargeNum;
	}

	public void setCumulativeRechargeNum(Integer cumulativeRechargeNum) {
		changed("cumulative_recharge_num", cumulativeRechargeNum, this.cumulativeRechargeNum);
		this.cumulativeRechargeNum = cumulativeRechargeNum;
	}
	
	
	@Column(name="server_id")
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		changed("server_id", serverId, this.serverId);
		this.serverId = serverId;
	}
	
	
	
	@Column(name="diamond_basins")
	public String getDiamondBasins() {
		return diamondBasins;
	}

	public void setDiamondBasins(String diamondBasins) {
		changed("diamond_basins", diamondBasins, this.diamondBasins);
		this.diamondBasins = diamondBasins;
	}
	
	
	@Column(name="login_reset_time")
	public Long getLoginResetTime() {
		return loginResetTime;
	}

	public void setLoginResetTime(Long loginResetTime) {
		changed("login_reset_time", loginResetTime, this.loginResetTime);
		this.loginResetTime = loginResetTime;
	}
	
	@Column(name="token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		changed("token", token, this.token);
		this.token = token;
	}

	@Column(name="was_reward_diamond")
	public Integer getWasRewardDiamond() {
		return wasRewardDiamond;
	}

	public void setWasRewardDiamond(Integer wasRewardDiamond) {
		changed("was_reward_diamond", wasRewardDiamond, this.wasRewardDiamond);
		this.wasRewardDiamond = wasRewardDiamond;
	}

	


	@Column(name="keep_information")
	public String getKeepInformation() {
		return keepInformation;
	}

	public void setKeepInformation(String keepInformation) {
		changed("keep_information", keepInformation, this.keepInformation);
		this.keepInformation = keepInformation;
	}

	@Column(name="idfa")
	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		changed("idfa", idfa, this.idfa);
		this.idfa = idfa;
	}

	@Column(name="package_name")
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		changed("package_name", packageName, this.packageName);
		this.packageName = packageName;
	}





	/**
	 * 钻石摇一摇 1:id 2：是否可以领取 3：是否已经领取
	 */
	public List<IdNumberVo2> listDiamondBasins = new CopyOnWriteArrayList<IdNumberVo2>();
	

	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loadRoleBriefs();
			listDiamondBasins=new CopyOnWriteArrayList<IdNumberVo2>(IdNumberVo2.createList(diamondBasins));
			loaded =true;
		}
	}
	
	public void saveData() {
		updateRoleBriefVo();
		setDiamondBasins(IdNumberVo2.createStr(listDiamondBasins, "|", ","));
	}

	
	private void loadRoleBriefs() {
		if(this.roleBriefs==null){
			return;
		}
		String[] items = StringUtil.split(this.roleBriefs,",");
		for (String itemStr : items) {
			RoleBriefVo roleBriefVo = new RoleBriefVo();
			roleBriefVo.loadProperty(itemStr, "|");
			listRoleBriefVo.add(roleBriefVo);
		}
	}
	
	public void updateRoleBriefVo() {
		if(this.listRoleBriefVo.size()<=0){
			this.roleBriefs = null;
			return;
		}
		List<String> list = new ArrayList<String>();
		for(int i=0; i<this.listRoleBriefVo.size(); i++){
			RoleBriefVo roleBrief = listRoleBriefVo.get(i);
			Object[] objs = roleBrief.fetchProperyItems();
			String targetStr=DBFieldUtil.createPropertyImplod(objs,"|");
			list.add(targetStr);
		}
		setRoleBriefs(StringUtil.implode(list,","));
	}
	

	public static UserPo findEntity(Integer id){
		return findRealEntity(UserPo.class,id);
	}

	public RoleBriefVo findRoleBriefByRoleId(Integer roleId) {
		for (RoleBriefVo roleBriefVo : listRoleBriefVo) {
			if(roleBriefVo.getRoleId().intValue()==roleId){
				return roleBriefVo;
			}
		}
		return null;
	}
	
	public List<RoleBriefVo> srotListRoleBriefVo(){
		clientListRoleBriefVo = new ArrayList<RoleBriefVo>();
		RoleBriefVo lastLoginRBV = null;
		List<RoleBriefVo> listRBV = new ArrayList<RoleBriefVo>(listRoleBriefVo);
		for(RoleBriefVo roleBriefVo : listRBV){
			RolePo rolePo = RolePo.findEntity(roleBriefVo.getRoleId());
			roleBriefVo.createTime = rolePo.getCreateTime();
			roleBriefVo.lastLoginTime = rolePo.getLastLoginTime();
			if(lastLoginRBV == null || roleBriefVo.lastLoginTime.longValue() > lastLoginRBV.lastLoginTime.longValue()){
				lastLoginRBV = roleBriefVo;
			}
		}
		if(lastLoginRBV == null){
			return null;
		}
		clientListRoleBriefVo.add(lastLoginRBV);
		listRBV.remove(lastLoginRBV);
		Collections.sort(listRBV);
		clientListRoleBriefVo.addAll(listRBV);
		return clientListRoleBriefVo;
	}

	public void checkAndRemove() {
		List<RoleBriefVo> toRemove = new ArrayList<RoleBriefVo>();
		for (RoleBriefVo roleBriefVo : listRoleBriefVo) {
			if(!roleBriefVo.removeTime.equals("0")){
				Long time = Long.valueOf(roleBriefVo.removeTime);
				if(System.currentTimeMillis()>time){
					toRemove.add(roleBriefVo);
				}
			}
		}
		
		for (RoleBriefVo roleBriefVo : toRemove) {
			RolePo rolePo = RolePo.findEntity(roleBriefVo.getRoleId());
			if(rolePo!=null){
				rolePo.setAbandomState(1);
			}
			listRoleBriefVo.remove(roleBriefVo);

		}
		if(toRemove.size()>0){
			srotListRoleBriefVo();
		}

	}
	
	/**
	 * 调整累计充值数量
	 * @param i
	 */
	public void adjustCumulativeRechargeNum(Integer i){
		if(i == null|| i < 0){
			return;
		}
		setCumulativeRechargeNum(cumulativeRechargeNum + i.intValue());
	}
	
	/**
	 * 检查登陆重置
	 */
	public void checkUserLoginReset() {
		long current = System.currentTimeMillis();
		if (CheckService.checkRequireDailyFresh(getLoginResetTime())) {
			initCreateUserAttribute();
		}
		
		setLoginResetTime(current);
	}
	
	/**
	 * 初始化创建角色的属性
	 */
	public void initCreateUserAttribute() {
		initListDiamondBasins();
		checkDiamondBasinsState();
	}
	
	
	public void initListDiamondBasins(){
		if(listDiamondBasins == null || listDiamondBasins.size() == 0){
			listDiamondBasins = new CopyOnWriteArrayList<IdNumberVo2>();
			List<DiamondBasin> list= XmlCache.xmlFiles.constantFile.diamondBasins.diamondBasin;
			for(DiamondBasin diamondBasin : list){
				IdNumberVo2 idNumberVo2 = new IdNumberVo2(diamondBasin.times,0,0);
				listDiamondBasins.add(idNumberVo2);
			}
		}
	}
	
	/**
	 * 检查钻石幸运抽取可领状态
	 */
	public void checkDiamondBasinsState(){
		for(IdNumberVo2 idNumberVo2 : listDiamondBasins){
			DiamondBasin diamondBasin = GlobalCache.fetchDiamondBasinBytimes(idNumberVo2.getInt1());
//				System.out.println("userPo.getCumulativeRechargeNum()="+userPo.getCumulativeRechargeNum()+"; diamondBasin.needVipExp="+diamondBasin.needVipExp);
			if(diamondBasin!=null && getCumulativeRechargeNum()!=null && idNumberVo2.getInt2()==0 && getCumulativeRechargeNum().intValue() >=  diamondBasin.needVipExp){
				idNumberVo2.setInt2(1);
			}
		}			
	}
	
	
	public void createAndCheckUserAccount(String channleKey){
		//账号登陆时做检测在线角色处理，防止周账号不同角色同时登陆
				RoleTemplate roleTemplate=(RoleTemplate) BeanUtil.getBean("roleTemplate");
				UserService userService = (UserService) BeanUtil.getBean("userService");
				
				ChannelHandlerContext userOldSession = roleTemplate.getSessionByUserIuid(fetchKey());
				List<RoleBriefVo> roleBriefVoList = listRoleBriefVo;
				checkAndRemove();
				

				
				if(roleBriefVoList.size() > 0)
				{
					for(RoleBriefVo roleBriefVo:roleBriefVoList)
					{
						ChannelHandlerContext oldSession = roleTemplate.getSessionByIuid(roleBriefVo.getRoleIuid());
						if(oldSession!=null && ((ChannelHandlerContext)oldSession).channel().isActive()){
							RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
							BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
							basePushTemplate.singleSession("PushRemoting.sendIAmKicked", oldSession, new Object[]{0}, null,true);
							BaseSessionUtil.flushSession(userOldSession);
							roleService.logoff(oldSession,0);
							((ChannelHandlerContext)oldSession).channel().disconnect();
						}
					}
				}
				if(userOldSession!=null && ((ChannelHandlerContext)userOldSession).channel().isActive()){
					RoleService roleService = (RoleService) BeanUtil.getBean("roleService");
					BasePushTemplate basePushTemplate = (BasePushTemplate) BeanUtil.getBean("basePushTemplate");
					basePushTemplate.singleSession("PushRemoting.sendIAmKicked", userOldSession, new Object[]{0}, null,true);
					BaseSessionUtil.flushSession(userOldSession);
					roleService.logoff(userOldSession,0);
					((ChannelHandlerContext)userOldSession).channel().close();
				}
				userService.userLogin(this,SessionUtil.getCurrentSession());
				srotListRoleBriefVo();
				LogUtil.writeLog(null, 303, 0, 0, 0,  getId()+GlobalCache.fetchLanguageMap("key2490")+this.channelKey,getDeviceId());
	}
	
	
	
	
	
	public void singleRole(String order, Object[] objs) {
		BasePushTemplate pushTemplate = BasePushTemplate.instance();
		pushTemplate.singleUser(order, fetchKey(), objs, null,true);
	}
	
	/**
	 * 登录排队推送
	 * @param state 状态
	 * @param totalNum 排队总人数
	 * @param cunnectNum 当前排行
	 */
	public void sendUpdateLoginQueueInfo(int state, int totalNum,int cunnectNum){
//		System.out.println(getIuid() + " sendUpdateLoginQueueInfo() state=" +state+"; totalNum="+totalNum+"; cunnectNum="+cunnectNum );
		singleRole("PushRemoting.sendUpdateLoginQueueInfo", new Object[]{state,totalNum,cunnectNum});
	}
	
	
	public String fetchKey(){
		String key = getIuid();
		if(StringUtil.isEmpty(key)){
			key = getToken();
		}
		return key;
	}
	
}
