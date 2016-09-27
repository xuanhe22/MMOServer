package com.games.mmo.mapserver.bean.skilleffect;

import java.util.List;

import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.game.SoulStepPo;
import com.games.mmo.service.SoulService;
import com.games.mmo.type.SoulType;
import com.storm.lib.util.PrintUtil;

public class BaseSkillEffect {
	//技能参数     (技能id,技能类型，技能比例，升级比例）
	//技能id
	//技能类型  1=物理技能 2=法术技能
	//技能比例 （100=造成100%的技能伤害）
	//升级比例  （1=每级增加1%的技能伤害）
	public static int[][] skillPara={
			//野怪&宠物物理攻击技能
			{100005,1,600,0},//野怪技能
			{100006,1,1000,0},//宠物技能
			{100007,1,1000,0},//恶魔猎手普攻
			{100008,1,1000,0},//灭世魔龙普攻技能进程2
			{100009,1,1000,0},//宠物普攻攻击
			{100010,1,1000,0},//宠物普通攻击
			{100011,1,600,0},//炎魔普通攻击
			{100012,1,1000,0},
			{100013,1,1000,0},
			{100014,1,900,0},
			{100015,1,900,0},
			{100016,1,1000,0},
			{100017,1,1000,0},
			
			//野怪&宠物魔法攻击技能
			{100101,2,600,0},//野怪技能远程
			{100102,2,600,0},//野怪技能远程2
			{100103,2,600,0},//野怪技能远程3
			{100104,2,600,0},//九尾狐技能进程2
			{100105,2,600,0},//花仙子远程技能
			{100110,2,900,0},
			{100111,2,900,0},
			{100112,2,900,0},

			
			{110001,1,320,1},//普通攻击
			{110002,1,1500,8},//风雷击
			{110008,1,800,5},//愤怒斩击
			{110005,1,1000,5},//裂地斩
			{110006,1,800,4},//冲锋
			{110007,1,350,2},//旋风斩伤害
			
			{120001,1,320,1},//普通攻击
			{120002,1,1500,8},//穿透箭
			{120009,1,600,3},//箭雨伤害
			{120007,1,350,2},//急速打击伤害
			{120008,1,1000,5},//寒冰箭伤害
			
			
			{130001,2,320,1},//普通攻击
			{130002,2,1000,5},//真空波
			{130007,2,500,3},//激光
			{130008,2,500,2},//炎爆术
			{130005,2,1000,5},//冰魂之力
			
			//怪物技能
			{140001,1,0,5},
			{140002,1,0,5},
			{140003,1,1800,5},
			{140004,1,600,5},
			{140005,1,1800,5},
			{140006,1,600,5},
			{140007,1,1800,5},
			{140008,1,600,5},
			{140009,1,1800,5},
			{140010,1,600,5},
			{140011,1,1800,5},
			{140013,1,1800,5},
			{140014,2,1800,5}, //炎魔法术伤害
			{140015,1,800,5},
			{140017,1,800,5},
			{140018,2,1800,5},
			{140019,1,1000,0},
			{140020,2,1500,0},
			{140021,2,1500,0},
			{140022,2,1500,0},
			{140023,1,1800,0},
			{140024,1,1800,0},
			{140025,1,2000,0},
			
			{140101,1,600,5},
			{140102,1,600,5},
			{140103,1,600,5},
			{140104,1,600,5},
			{140105,1,600,5},
			{140106,1,600,5},
			{140107,1,600,5},
			
			{140201,2,1000,0},
			{140202,2,1000,0},
			{140203,2,1000,0},
			{140204,1,1000,0},
			{140205,1,1000,0},
			{140206,1,1000,0},
			{140207,1,1000,0},
			{140208,2,1000,0},
			
			{170006,2,3000,0},//宠物-献祭
			{170012,2,5000,0}//宠物高级-献祭
	};
	//BUFF技能（技能id,buffID，buff类型1=自己 2=对手)
	 public static int[][] skillBuff={
			{120006,6,1,0},//神射手
			{130006,4,1,0},//魔法盾
			{152001,7,1,0},
			{152002,8,1,0},
			{152003,9,1,0},
			{152004,10,1,0},
			{154001,11,1,0},
			{154002,12,1,0},
			{154003,13,1,0},
			{154004,14,1,0},
			
			{120008,3,2,0},//寒冰箭减速
			{110006,3,2,0},//冲锋减速
			{130005,3,2,0},	//冰魂之力减速
			
			//宠物技能
			{170001,15,2,0},
			{170002,16,2,0},
			{170003,17,2,0},
			{170004,18,2,0},
			{170005,19,1,0},
			
			
			//公会建筑技能
			{160001,42,1,0},
			{160002,43,1,0},
			{160003,44,1,0},
			{160004,45,1,0},
			{160005,46,1,0},
			{160006,47,1,0},
			{160007,48,1,0},
			{160008,49,1,0},
			{160009,50,1,0},
			{160010,51,1,0},
			{160011,52,1,0},
			{160012,53,1,0},
			{160013,54,1,0},
			{160014,55,1,0},
			{160015,56,1,0},
			{160016,57,1,0},
			{160017,58,1,0},
			{160018,59,1,0},
			{160019,60,1,0},
			{160020,61,1,0},
			{160021,62,1,0},
			{160022,63,1,0},
			{160023,64,1,0},
			{160024,65,1,0},
			{160025,66,1,0},
			{160026,67,1,0},
			{160027,68,1,0},
			{160028,69,1,0},
			{160029,70,1,0},
			{160030,71,1,0},
			{160031,72,1,0},
			{160032,73,1,0},
			{160033,74,1,0},
			{160034,75,1,0},
			{160035,76,1,0},
			{160036,77,1,0},
			{160037,78,1,0},
			{160038,79,1,0},
			{160039,80,1,0},
			{160040,81,1,0},
			{140027,99,1,0}
	};
	
	
	/**
	 * 基础技能效果
	 * @param skillId
	 * @param caster
	 * @param target
	 */
	public static void baseSkill(int skillId,Fighter caster,Fighter target){
//		PrintUtil.print("baseSkill() skillId ="+skillId+"; caster.name = " +caster.name +"; target.name = "+target.name);
		int type = 0;
		int skillValue=0;
		int upValue=0;
		int attack=0;
		int denfence=0;
		for(int i=0;i<skillPara.length;i++){
			if(skillPara[i][0]==skillId){
				type=skillPara[i][1];
				skillValue=skillPara[i][2];
				upValue=skillPara[i][3];
				break;
			}
		}
		if(type==1){
			attack=caster.callReadMeleeAttack();
			denfence=target.callReadMeleeDefence();
		}else if(type==2){
			attack=caster.callReadMagicAttack();
			denfence=target.callReadMagicDefence();
		}
		int value=skillValue+upValue*(caster.callSkillLv(skillId)-1);
		int damege=0;
		if(denfence>99999999){
			damege=1;
		}else{
			//五行属性相克
			int soulDamageResistRate = 0;
			if(caster.rolePo!=null&&caster.rolePo.getSoulType()!=null&&target.rolePo!=null&&target.rolePo.getSoulType()!=null){
				int casterSoulType = caster.rolePo.getSoulType().intValue();
				int targetSoulType = target.rolePo.getSoulType().intValue();
				if(casterSoulType>0&&targetSoulType>0){
					int possessType = SoulType.fetchPossessAtbById(SoulType.Type.getType(targetSoulType));
					if(possessType==casterSoulType){
						int targetSoulLv = target.rolePo.soulAtbMap.size()-1;
						SoulStepPo soulStepPo = SoulStepPo.findEntityByStep(targetSoulLv);
						soulDamageResistRate = soulStepPo.getMatchValue().intValue()*10;
					}
				}
			}
			
			damege=(int)(Math.max(attack-denfence, (int)(attack*0.2))*value/1000d*((1000+caster.batDamageRate-target.batDamageResistRate-soulDamageResistRate)/1000d));
		}
		if(target.mapRoom!=null){
//			System.out.println("baseSkill() skillId = "+skillId+" 攻caster.name="+caster.name +";  受target.name="+target.name);
			target.callHpChange(caster, -damege,false);
		}

	}
	
	/**增加buff的技能
	 * 
	 * @param skillId
	 * @param caster
	 * @param target
	 */
	public static void buffSkill(int skillId,Fighter caster,Fighter target){
//		System.out.println("buffSkill() skillId = " +skillId +"; caster.name = "+caster.name +"; target.name = "+target.name);
		int buffId = 0;
		int buffType=0;
		for(int i=0;i<skillBuff.length;i++){
			if(skillBuff[i][0]==skillId){
				buffId=skillBuff[i][1];
				buffType=skillBuff[i][2];
				break;
			}
		}
//		System.out.println("buffId = "+buffId);
//		System.out.println("buffType = "+buffType);
		if(buffType==1){
			caster.callAddBuff(caster,buffId);
		}else if(buffType==2){
			caster.callAddBuff(target,buffId);
		}
	
	}
	
	
	//下面具体技能效果配置
	//下面具体技能效果配置
	//下面具体技能效果配置
	
//	//旋风斩
//	public static void SKILL110003(Fighter caster,Fighter target){
//		caster.callStartPoseCast(1001);
//	}
	//寒冰箭伤害
	public static void SKILL120008(Fighter caster,Fighter target){
//		System.out.println("SKILL120008() 触发者caster.name = "+caster.name +"; target = " +target);
		baseSkill(120008,caster,target);
		buffSkill(120008,caster,target);
	}
	//冲锋
	public static void SKILL110006(Fighter caster,Fighter target){
		baseSkill(110006,caster,target);
		buffSkill(110006,caster,target);
	}
	//冰魂之力
	public static void SKILL130005(Fighter caster,Fighter target){
		baseSkill(130005,caster,target);
		buffSkill(130005,caster,target);
	}
	
	//血瓶类型
	public static void SKILL151001(Fighter caster,Fighter target){
		caster.callHpChange(caster,2000,false);
	}
	public static void SKILL151002(Fighter caster,Fighter target){
		caster.callHpChange(caster,3600,false);
	}
	public static void SKILL151003(Fighter caster,Fighter target){
		caster.callHpChange(caster,12000,false);
	}
	public static void SKILL151004(Fighter caster,Fighter target){
		caster.callHpChange(caster,24000,false);
	}
	public static void SKILL151005(Fighter caster,Fighter target){
		caster.callHpChange(caster,7500,false);
	}
	public static void SKILL151006(Fighter caster,Fighter target){
		caster.callHpChange(caster,18000,false);
	}
	public static void SKILL153001(Fighter caster,Fighter target){
		caster.callMpChange(caster,150,false);
	}
	public static void SKILL153002(Fighter caster,Fighter target){
		caster.callMpChange(caster,150,false);
	}
	public static void SKILL153003(Fighter caster,Fighter target){
		caster.callMpChange(caster,150,false);
	}
	public static void SKILL153004(Fighter caster,Fighter target){
		caster.callMpChange(caster,150,false);
	}
	
	public static Integer fetchBuffIdBySkillId(Integer skillId){
		Integer buffId = null;
		for(int i=0;i<skillBuff.length;i++){
			if(skillBuff[i][0]==skillId){
				buffId=skillBuff[i][1];
				break;
			}
		}
		return buffId;
	}
	
}
