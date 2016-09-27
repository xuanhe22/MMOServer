/**
 *
 */
package com.games.mmo.vo.role;

import java.io.Serializable;
import java.util.Comparator;

import com.games.mmo.po.game.ItemPo;
import com.storm.lib.util.ArrayUtil;
import com.storm.lib.util.BeanUtil;

/**
 * 类功能:
 * @author johnny
 * @version 2011-3-10
 */
public class RolePackItemVoCompartar implements Comparator<RolePackItemVo>,Serializable{

	
	
	public RolePackItemVoCompartar(int[] advantage) {
		this.advantage = advantage;
	}
	
	public int[] advantage= new int[]{101002,105076,111011};
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
//	1. 将可重合的物品重合 
//	2. 道具目录编号越小排在越前面
//	3. 道具目录相同的，道具编号越小排在越前面
	public int compare(RolePackItemVo o1, RolePackItemVo o2) {
//		ItemService itemService = (ItemService) BeanUtil.getBean("itemService");
		ItemPo itemPo1 = ItemPo.findEntity(o1.getItemId());
		ItemPo itemPo2 = ItemPo.findEntity(o2.getItemId());;
//		if(o1.wasEquip()){
//			itemPo1 = itemService.getEqpItemPo(o1.getId());
//		}
//		else{
//			itemPo1 = itemService.getItemPo(o1.getId());
//		}
//		
//		if(o2.getIsEquip()){
//			itemPo2 = itemService.getEqpItemPo(o2.getId());
//		}
//		else{
//			itemPo2 = itemService.getItemPo(o2.getId());
//		}
		
		if(ArrayUtil.intInArray(itemPo1.getId(), advantage) && ArrayUtil.intInArray(itemPo2.getId(), advantage)){
			int item1Index=0;
			int item2Index=0;
			for (int i=0;i<advantage.length;i++) {
				if(itemPo1.getId().intValue()==advantage[i]){
					item1Index=i;
				}
				if(itemPo2.getId().intValue()==advantage[i]){
					item2Index=i;
				}
			}
			if(item1Index>item2Index){
				return -1;
			}
			else if(item1Index<item2Index){
				return 1;
			}
			else{
				return 0;
			}
		}
		else{
			if(ArrayUtil.intInArray(itemPo1.getId(), advantage)){
				return -1;
			}
			else if(ArrayUtil.intInArray(itemPo2.getId(), advantage)){
				return 1;
			}	
		}
//        if (itemPo1.getCategory() > itemPo2.getCategory()) {
//            return 1; // 第一个大于第二个
//        } else if (itemPo1.getCategory() < itemPo2.getCategory()) {
//            return -1;// 第一个小于第二个
//        } else {
        if (itemPo1.getItemIndex() > itemPo2.getItemIndex()) {
            return 1; // 第一个大于第二个
        }
        else if (itemPo1.getItemIndex() < itemPo2.getItemIndex()) {
            return -1; // 第一个大于第二个
        }
        else{
        	
            return o2.bindStatus-o1.bindStatus; // 等于
        }
//        }
	}

}
