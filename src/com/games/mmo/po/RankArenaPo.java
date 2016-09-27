package com.games.mmo.po;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.type.ItemType;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.vo.CommonAvatarVo;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;

@Entity
@Table(name = "u_po_rank_arena")
public class RankArenaPo extends BaseUserDBPo{
	/**
	*主键
	**/
	private Integer id;
	/**
	 * 排行
	 */
	private Integer arenaRank;
	
	/**
	 * 角色名字
	 */
	private String roleName;
	
	/**
	 * 角色战力
	 */
	private Integer rolePower;
	
	/**
	 * 角色等级
	 */
	private Integer roleLv; 
	
	//以上为前端需要关心的字段
	
	private Integer roleId;
	
	private Integer roleCareer;
	
	/**
	 * 模型avatar
	 */
	private String modelAvatar;
	
	/**
	 * 翅膀avatar
	 */
	private String wingAvatar;
	
	/**
	 * 武器avatar
	 */
	private String weaponAvatar;
	
	/** 是否是机器人 */
	@JSONField(serialize = false)
	private Integer wasRobot=0;
	
	/** 是否第一次打竞技场*/
	@JSONField(serialize = false)
	private Integer wasFirstArena=0;
	
	
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="arena_rank")
	public Integer getArenaRank() {
		return arenaRank;
	}
	public void setArenaRank(Integer arenaRank) {
		changed("arena_rank",arenaRank,this.arenaRank);
		this.arenaRank = arenaRank;
	}
	
	@Column(name="role_name")
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		changed("role_name",roleName,this.roleName);
		this.roleName = roleName;
	}
	
	@Column(name="role_power")
	public Integer getRolePower() {
		return rolePower;
	}
	public void setRolePower(Integer rolePower) {
		changed("role_power",rolePower,this.rolePower);
		this.rolePower = rolePower;
	}
	
	@Column(name="role_lv")
	public Integer getRoleLv() {
		return roleLv;
	}
	public void setRoleLv(Integer roleLv) {
		changed("role_lv",roleLv,this.roleLv);
		this.roleLv = roleLv;
	}
	
	
	@Column(name="role_id")
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		changed("role_id",roleId,this.roleId);
		this.roleId = roleId;
	}
	
	
	@Column(name="role_career")
	public Integer getRoleCareer() {
		return roleCareer;
	}

	public void setRoleCareer(Integer roleCareer) {
		changed("role_career",roleCareer,this.roleCareer);
		this.roleCareer = roleCareer;
	}

	
	@Column(name="model_avatar")
	public String getModelAvatar() {
		return modelAvatar;
	}

	public void setModelAvatar(String modelAvatar) {
		changed("model_avatar",modelAvatar,this.modelAvatar);
		this.modelAvatar = modelAvatar;
	}

	@Column(name="wing_avatar")
	public String getWingAvatar() {
		return wingAvatar;
	}

	public void setWingAvatar(String wingAvatar) {
		changed("wing_avatar",wingAvatar,this.wingAvatar);
		this.wingAvatar = wingAvatar;
	}

	@Column(name="weapon_avatar")
	public String getWeaponAvatar() {
		return weaponAvatar;
	}

	public void setWeaponAvatar(String weaponAvatar) {
		changed("weapon_avatar",weaponAvatar,this.weaponAvatar);
		this.weaponAvatar = weaponAvatar;
	}
	
	
	
	@Column(name="was_robot")
	public Integer getWasRobot() {
		return wasRobot;
	}

	public void setWasRobot(Integer wasRobot) {
		changed("was_robot",wasRobot,this.wasRobot);
		this.wasRobot = wasRobot;
	}

	@Column(name="was_first_arena")
	public Integer getWasFirstArena() {
		return wasFirstArena;
	}

	public void setWasFirstArena(Integer wasFirstArena) {
		changed("was_first_arena",wasFirstArena,this.wasFirstArena);
		this.wasFirstArena = wasFirstArena;
	}

	@Override
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();
			loaded =true;
		}
	}
	
	@Override
	public void saveData() {
		
		
	}

	public static List<RankArenaPo> findRanksToShow(RolePo rolePo, Integer wasFirstArena) {
		List<RankArenaPo> rankArenaPos = new ArrayList<RankArenaPo>();
		RankArenaPo rankArenaPo =GlobalCache.rankArenaRoleIdMaps.get(rolePo.getId());
//		System.out.println(rankArenaPo.getRoleName()+"||"+rankArenaPo.getArenaRank());
		
		if(wasFirstArena==null || wasFirstArena.intValue() == 0){
			for(int i =GlobalCache.rankArenaMaps.size(); 0 < GlobalCache.rankArenaMaps.size(); i--){
				if(GlobalCache.rankArenaMaps.containsKey(i)){
					if(!rankArenaPos.contains(GlobalCache.rankArenaMaps.get(i)))
					{
						if(GlobalCache.rankArenaMaps.get(i).getArenaRank().intValue() != rankArenaPo.getArenaRank().intValue() && 
								GlobalCache.rankArenaMaps.get(i).getWasRobot().intValue()==1 &&
								GlobalCache.rankArenaMaps.get(i).getRolePower().intValue() < 1000){
							rankArenaPos.add(GlobalCache.rankArenaMaps.get(i));		
							if(rankArenaPos.size() >=12){
								break;
							}
						}
					}
				}
			}
		}else{
			if(rankArenaPo.getArenaRank().intValue()<=10){
				for(int i=1;i<=13;i++)
				{
					if(GlobalCache.rankArenaMaps.containsKey(i))
					{
						if(!rankArenaPos.contains(GlobalCache.rankArenaMaps.get(i)))
						{
							if(GlobalCache.rankArenaMaps.get(i).getArenaRank().intValue() != rankArenaPo.getArenaRank().intValue()){
								rankArenaPos.add(GlobalCache.rankArenaMaps.get(i));							
							}
						}
					}
				}
			}
			else{
				for(int i=0;i>=-3;i--){
					if(GlobalCache.rankArenaMaps.containsKey(rankArenaPo.getArenaRank()-i))
					{
						if(!rankArenaPos.contains(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-i)))
						{
							if(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-i).getArenaRank().intValue() != rankArenaPo.getArenaRank().intValue())
							{
								rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-i));							
							}
						}
					}
				}
				if(rankArenaPo.getArenaRank()>=50){
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*98/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*96/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*94/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*92/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*90/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*88/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*86/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*84/100));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()*82/100));
				}else{
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-1));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-2));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-3));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-4));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-5));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-6));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-7));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-8));
					rankArenaPos.add(GlobalCache.rankArenaMaps.get(rankArenaPo.getArenaRank()-9));
				}

				if(rankArenaPos.size() < 12){
					int start = rankArenaPo.getArenaRank().intValue() - 7;
					int end = start - (12 - rankArenaPos.size());
//				System.out.println("start = "+start + "; end = " +end );
					for(int i = start; i > end; i--)
					{
						if(GlobalCache.rankArenaMaps.containsKey(i))
						{
							if(!rankArenaPos.contains(GlobalCache.rankArenaMaps.get(i)))
							{
								if(GlobalCache.rankArenaMaps.get(i).getArenaRank().intValue() != rankArenaPo.getArenaRank().intValue()){
									rankArenaPos.add(GlobalCache.rankArenaMaps.get(i));							
								}
							}
						}
					}
				}		
			}	
		}
		
		
//		System.out.println("1竞技场数量：" + rankArenaPos.size());
//		System.out.println("1 "+rankArenaPos);
//		rankArenaPos.remove(rankArenaPo);
		
//		System.out.println("2竞技场数量：" + rankArenaPos.size());
//		System.out.println("2 "+rankArenaPos);
		return rankArenaPos;
	}
	
	
	

	public void loadByRolePo(RolePo rolePo) {
		if(rolePo !=null){
			setRoleLv(rolePo.getLv());
			if(rolePo.getName()!=null){
				setRoleName(rolePo.getName());
			}
			else{
				setRoleName(GlobalCache.fetchLanguageMap("key34"));
			}
			setRolePower(rolePo.getBattlePower());
			setRoleId(rolePo.getId());
			setRoleCareer(rolePo.getCareer());
			if(rolePo.wasRobot()){
				setWasRobot(1);	
				setWasFirstArena(1);
			}else{
				setWasRobot(0);
			}
			
			makeAvatars(rolePo);			
		}
	}
	
	public static RankArenaPo findEntity(Integer id){
		return findRealEntity(RankArenaPo.class,id);
	}
	
	public  void makeAvatars(RolePo rolePo) {
		CommonAvatarVo commonAvatarVo=CommonAvatarVo.build(rolePo.getEquipWeaponId(), rolePo.getEquipArmorId(), rolePo.getFashion(), rolePo.getCareer(), rolePo.getWingWasHidden(),rolePo.getWingStar(),rolePo.hiddenFashions);
		setModelAvatar(commonAvatarVo.modelAvatar);
		setWeaponAvatar(commonAvatarVo.weaponAvatar);
		setWingAvatar(commonAvatarVo.wingAvatar);
	}

	@Override
	public String toString() {
		return "RankArenaPo [id=" + id + ", arenaRank=" + arenaRank
				+ ", roleName=" + roleName + ", rolePower=" + rolePower
				+ ", roleLv=" + roleLv + ", roleId=" + roleId + ", roleCareer="
				+ roleCareer + ", modelAvatar=" + modelAvatar + ", wingAvatar="
				+ wingAvatar + ", weaponAvatar=" + weaponAvatar + ", wasRobot="
				+ wasRobot + ", wasFirstArena=" + wasFirstArena + "]";
	}	




	
}
