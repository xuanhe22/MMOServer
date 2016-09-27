package com.games.mmo.vo.role;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.po.EqpPo;
import com.games.mmo.po.RpetPo;
import com.games.mmo.vo.CommonAvatarVo;
import com.games.mmo.vo.PetConstellVo;
import com.games.mmo.vo.SlotSoulVo;

public class RoleViewInforVo {
	/**
	 * 武器
	 */
	public EqpPo equipWeapon;
	/**
	 * 右戒指
	 */
	public EqpPo equipRing;
	/**
	 * 胸甲
	 */
	public EqpPo equipArmor;
	/**
	 * 项链
	 */
	public EqpPo equipNecklace;
	/**
	 * 护腕
	 */
	public EqpPo equipBracer;
	
	/**
	 * 头盔
	 */
	public EqpPo equipHelmet;
	
	/**
	 * 鞋子
	 */
	public EqpPo equipShoe;
	
	/**
	 * 腰带
	 */
	public EqpPo equipBelt;

	/**
	 * 手镯
	 */
	public EqpPo equipBracelet;
	
	/**
	 * 裤子
	 */
	public EqpPo equipPants;
	
	public Integer roleId;
	
	public String roleName="J";
	
	public Integer roleLv=1;
	
	public Integer roleVip=1;
	
	public Integer roleBattlePower=1;
	
	public Integer roleCareer=1;
	
	public String roleGuildName="g";
	
	public List<SlotSoulVo> listSlotSouls;
	
	public String wingAvatar="0";
	
	public String weaponAvatar="0";
	
	public String modelAvatar="0";
	
	public RpetPo rpetPo;
	
	public void makeAvatars(CommonAvatarVo commonAvatarVo) {
		weaponAvatar=commonAvatarVo.weaponAvatar;
		wingAvatar=commonAvatarVo.wingAvatar;
		modelAvatar=commonAvatarVo.modelAvatar;
	}
	
	/**
	 * 强化套装加成到达最大等级
	 */
	public Integer powerSuitPlusArriveMaxLevel = 0;

	/**
	 * 当前强化套装加成等级
	 */
	public Integer currentPowerSuitPlusLevel = 0;

	/**
	 * 升星套装加成到达最大等级
	 */
	public Integer starSuitPlusArriveMaxLevel = 0;

	/**
	 * 当前升星套装加成等级
	 */
	public Integer currentStarSuitPlusArriveLevle = 0;
	
	/**
	 * 宠物星座
	 */
	public List<List<PetConstellVo>> petConstellsList = new ArrayList<List<PetConstellVo>>();
	
	/**
	 * 洗练套装加成达到最大等级
	 */
	public Integer washSuitPlusMaxLevel = 0;
	
	/**
	 * 当前洗练套装加成等级
	 */
	public Integer washSuitPlusCurrentLevel = 0;
}
