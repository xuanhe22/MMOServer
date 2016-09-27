package com.games.mmo.vo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.EqpPo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.type.RoleType;
import com.games.mmo.util.ExpressUtil;
import com.games.mmo.vo.role.RoleFashionVo;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdStringVo;

public class CommonAvatarVo {
	public String modelAvatar="";
	public String wingAvatar="";
	public String weaponAvatar="";
	
	
	public static CommonAvatarVo build(Integer equipWeaponId,Integer equipArmorId, String fashion,Integer career,Integer wingHidden,Integer wingLv, List<IdNumberVo> hiddenFashion) {
		CopyOnWriteArrayList<RoleFashionVo> roleFashion = new CopyOnWriteArrayList<RoleFashionVo>();
		if (StringUtil.isNotEmpty(fashion)) {
			String[] items = StringUtil.split(fashion, ",");
			for (String itemStr : items) {
				RoleFashionVo roleFashionVo = new RoleFashionVo();
				roleFashionVo.loadProperty(itemStr, "|");
				roleFashion.add(roleFashionVo);
			}
		}	
//		PrintUtil.print("equipWeaponId: "+equipWeaponId+"; equipArmorId: "+equipArmorId+"; roleFashionVo: "+fashion+"; wingHidden: "+wingHidden+"; wingLv: "+wingLv+"; hiddenFashion: "+IdNumberVo.createStr(hiddenFashion));
		CommonAvatarVo commonAvatarVo=new CommonAvatarVo();
//		System.out.println("rolePo.getWingStar() =="+rolePo.getWingStar());
		int num = (wingLv/11);
		String avatarModelWing = XmlCache.xmlFiles.constantFile.wing.wingLooks.wingLook.get(num).model;
		List<List<Integer>> listAvatarModelWing  = ExpressUtil.buildBattleExpressList(avatarModelWing);

			if(career==RoleType.CAREER_WARRIOR){
				commonAvatarVo.modelAvatar="911001";
				commonAvatarVo.weaponAvatar="zswq0011";
				commonAvatarVo.wingAvatar = listAvatarModelWing.get(0).get(0).toString();
			}
			else if(career==RoleType.CAREER_RANGER){
				commonAvatarVo.modelAvatar="921001";
				commonAvatarVo.weaponAvatar="sswq0011";
				commonAvatarVo.wingAvatar = listAvatarModelWing.get(0).get(1).toString();
			}
			else if(career==RoleType.CAREER_MAGE){
				commonAvatarVo.modelAvatar="931001";
				commonAvatarVo.weaponAvatar="fswq0011";
				commonAvatarVo.wingAvatar = listAvatarModelWing.get(0).get(2).toString();
			}
			
			boolean hasType1 = false;
			boolean hasType2 = false;
			if(!roleFashion.isEmpty()){
				for(RoleFashionVo rFashionVo:roleFashion){
					if(rFashionVo.isUse == 1){
						int type = rFashionVo.fashionPo.getType().intValue();
						switch (type) {
						case 1:
							//胸甲时装
							for(IdNumberVo idNumberVo: hiddenFashion){
								if(idNumberVo.getId().intValue()==type&&idNumberVo.getNum()==0){
//									PrintUtil.print("rFashionVo.id: "+rFashionVo.id+"; rFashionVo.fashionPo.getId(): "+rFashionVo.fashionPo.getId()+"; rFashionVo.fashionPo.getFashionId(): "+rFashionVo.fashionPo.getFashionId());
									commonAvatarVo.modelAvatar=rFashionVo.fashionPo.fecthFashionIdByCareer(career).toString();
//									PrintUtil.print("1111111111111111111commonAvatarVo.modelAvatar: "+commonAvatarVo.modelAvatar);
									hasType1 = true;
								}
							}
							break;
						case 2:
							//翅膀时装
							for(IdNumberVo idNumberVo: hiddenFashion){
								if(idNumberVo.getId().intValue()==type&&idNumberVo.getNum()==0){
									commonAvatarVo.wingAvatar=rFashionVo.fashionPo.fecthFashionIdByCareer(career).toString();
									hasType2 = true;
								}
							}
							break;
						default:
							break;
						}
					}
				}
			}
			
			if(!hasType1&&equipArmorId!=null){
				if(equipArmorId.intValue() != 0){
					EqpPo eqpPo=EqpPo.findEntity(equipArmorId);
					if(eqpPo!=null){
						ItemPo itemPo=EqpPo.findEntity(equipArmorId).itemPo();
						for (IdStringVo idStringVo : itemPo.listIntensifyEffects) {
							if(eqpPo.getPowerLv().intValue()>=idStringVo.getId().intValue()){
								commonAvatarVo.modelAvatar=idStringVo.getStr();
//								PrintUtil.print("222222222222commonAvatarVo.modelAvatar: "+commonAvatarVo.modelAvatar);
							}
						}
					}
				}
			}

			
			if(equipWeaponId != null && equipWeaponId.intValue() != 0){
				EqpPo eqpPo=EqpPo.findEntity(equipWeaponId);
				if(eqpPo!=null){
					ItemPo itemPo=eqpPo.itemPo();
					for (IdStringVo idStringVo : itemPo.listIntensifyEffects) {
						if(eqpPo.getPowerLv().intValue()>=idStringVo.getId().intValue()){
							commonAvatarVo.weaponAvatar=idStringVo.getStr();
						}
					}
				}

			}

				
			if(wingHidden == 0){
				commonAvatarVo.wingAvatar = "0";
			}
//			PrintUtil.print("modelAvatar: "+commonAvatarVo.modelAvatar+"; weaponAvatar: "+commonAvatarVo.weaponAvatar+"; wingAvatar: "+commonAvatarVo.wingAvatar);
		return commonAvatarVo;
	}	
}
