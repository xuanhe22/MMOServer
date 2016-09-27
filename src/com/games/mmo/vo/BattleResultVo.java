package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;

import com.games.mmo.po.game.CopySceneConfPo;
import com.games.mmo.po.game.DropPo;
import com.storm.lib.base.BaseVo;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

public class BattleResultVo extends BaseVo{
	public Integer exp = 0;
	public Integer gold = 0;
	public Integer skillPoint = 0;
	public Integer copySceneConfId = 0;
	/**
	 * [0]=道具编号 [1]数量
	 */
	public List<List<Integer>> itemList=new ArrayList<List<Integer>>();
	/**
	 * [0]index1-4 [1]状态0=未翻到1=翻到 [2]道具编号 [3]数量
	 */
	public List<List<Integer>> rollAwardList = new ArrayList<List<Integer>>();
	
	/**
	 * 翻到的卡牌奖励
	 */
//	public IdNumberVo reward;
	
	public Integer arenaRank=10;
	public Integer prestige=0;
	public Integer success=1;
	public Integer achievePoint=0;
	public Integer guildHonor=0;
	
	public void loadItemList(CopySceneConfPo cscp) {

		List<IdNumberVo2> list =cscp.listItemDrop;
		List<DropPo> drops = DropPo.makeDropListByExp(list);
		for (DropPo dropPo : drops) {
			List<Integer>  val = new ArrayList<Integer>();
			val.add(dropPo.getItemId());
			val.add(dropPo.getNum());
			itemList.add(val);
		}
	}

	/**
	 * 获取卡牌奖励
	 * @param copySceneConfPo
	 * @param battleResultVo
	 */
	public void loadCardAward(CopySceneConfPo copySceneConfPo)
	{
		if(success == 1)
		{
			if(copySceneConfPo.listItemAwardExp.size()>0 && copySceneConfPo.listItemAwardShow.size()>0){
	//			 * [0]index1-4 [1]状态0=未翻到1=翻到 [2]道具编号 [3]数量
				List<Integer> val1 =new ArrayList<Integer>();
				List<IdNumberVo> filterList = new ArrayList<IdNumberVo>();
				IdNumberVo idNumberVo = RandomUtil.calcWeightOverCardAward(copySceneConfPo.listItemAwardExp, filterList);
				val1.add(1);
				val1.add(1);
				val1.add(idNumberVo.getId());
				val1.add(idNumberVo.getNum());
				rollAwardList.add(val1);
				filterList.add(idNumberVo);
				filterList.clear();
				List<IdNumberVo> list = RandomUtil.fetchOverCardAwardByCount(copySceneConfPo.listItemAwardShow, filterList, 3);
				for(int i=0;i<list.size();i++){
					val1=new ArrayList<Integer>();
					val1.add(i+2);
					val1.add(0);
					val1.add(list.get(i).getId());
					val1.add(list.get(i).getNum());
					rollAwardList.add(val1);
				}
			}
		}
		
//		List<IdNumberVo2> cardAwardList = copySceneConfPo.listAwardExp;
//		if(copySceneConfPo.cardRandom<=0){
//			return;
//		}
//		int random = RandomUtil.randomInteger(copySceneConfPo.cardRandom)+1;
//		for(IdNumberVo2 cardAward:cardAwardList)
//		{
//			if(random <= cardAward.getInt3())
//			{
//				battleResultVo.reward = new IdNumberVo(cardAward.getInt1(), cardAward.getInt2());
//				break;
//			}
//			random-=cardAward.getInt3();
//		}
	}

	@Override
	public String toString() {
		return "BattleResultVo [exp=" + exp + ", gold=" + gold
				+ ", skillPoint=" + skillPoint + ", copySceneConfId="
				+ copySceneConfId + ", itemList=" + itemList
				+ ", rollAwardList=" + rollAwardList + ", arenaRank="
				+ arenaRank + ", prestige=" + prestige + ", success=" + success
				+ ", achievePoint=" + achievePoint + ", guildHonor=" + guildHonor + "]";
	}
	
	

}
