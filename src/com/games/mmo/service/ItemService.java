package com.games.mmo.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Controller;

import com.games.mmo.po.game.ItemPo;
import com.storm.lib.base.BaseService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

@Controller
public class ItemService extends BaseService {

	/**
	 * 添加列表元素数量
	 * @param pId
	 * @param num
	 * @param list
	 */
	public void addListElement(Integer pId,Integer num,CopyOnWriteArrayList<IdNumberVo> list){
		boolean flag = false;
		for(IdNumberVo iv : list){
			if(pId.intValue() == iv.getId().intValue()){
				iv.addNum(num);
				flag = true;
				break;
			}
		}
		if(!flag){
			list.add(new IdNumberVo(pId, num));					
		}
	}
	
	/**
	 * 查找列表元素数量
	 * @param pId
	 * @param list
	 * @return
	 */
	public int fetchListElementCount(Integer pId,CopyOnWriteArrayList<IdNumberVo> list){
		int count = 0;
		for(IdNumberVo iv : list){
			if(pId == iv.getId().intValue()){
				count = iv.getNum().intValue();
				break;
			}
		}
		return count;
	}
	
	public List<ItemPo> findItemByIdOrName(String term, Integer maxLength){
		StringBuffer sb = new StringBuffer();
		sb.append("from ItemPo where 1=1");
		if(StringUtil.isNumeric(term)){
			sb.append(" and (id like ").append("'%").append(term).append("%' or name like ").append("'%").append(term).append("%')");
		}else{
			sb.append(" and name like ").append("'%").append(term).append("%'");
		}
		return BaseDAO.instance().dBfindLimit(sb.toString(), maxLength == null ? 20 : maxLength);
	}
}
