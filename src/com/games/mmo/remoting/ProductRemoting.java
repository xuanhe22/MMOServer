package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.games.backend.vo.ProductVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.ProductPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.util.CheckUtil;
import com.games.mmo.util.SessionUtil;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.StringUtil;
@Controller
public class ProductRemoting extends BaseRemoting{

	public List<ProductVo> list(){
		return ProductVo.createFromProducts();
	}

	public Integer addEdit(Integer id,Integer itemId,Long itemSell,Long timeOff,Integer moneyType,Integer originalPrice,Integer discountsPrice,Integer buyCount,String dayCount,Integer promotions,Integer onetimeCount,Integer shopTab,Integer buyViplv,Integer totalCount,Integer minLv,String lotNumber,String servers){
		ProductPo productPo =null;
		if(id==0){
			productPo = new ProductPo();
			productPo.setTotalCountBuyed(0);
//			productPo.setDayCount(null);
		}
		else{
			productPo = ProductPo.findEntity(id);
		}
		if(StringUtil.isEmpty(dayCount)){
			productPo.setDayCount(null);
		}
		else{
			productPo.setDayCount(dayCount);
		}
		CheckUtil.checkIsNull(ItemPo.findEntity(itemId));
		productPo.setBuyCount(buyCount);
		productPo.setBuyViplv(buyViplv);
		productPo.setDiscountsPrice(discountsPrice);
		productPo.setItemId(itemId);
		productPo.setItemSell(itemSell/1000+"");
		productPo.setMoneyType(moneyType);
		productPo.setOnetimeCount(onetimeCount);
		productPo.setOriginalPrice(originalPrice);
		productPo.setPromotions(promotions);
		productPo.setShopTab(shopTab);
		productPo.setTimeOff(timeOff/1000+"");
		productPo.setTotalCount(totalCount);
		productPo.setMinLv(minLv);
		productPo.reloadByStrs();
		if(id==0){
			productPo.setLotNumber(lotNumber);
			productPo.setServers(servers);
			productPo.setCreateTime(System.currentTimeMillis());
			productPo = (ProductPo) BaseDAO.instance().insert(productPo);
			id = productPo.getId();
		}else{
			BaseDAO.instance().syncToDB(productPo);
		}
		GlobalCache.synProducts();
		return id;
	}
	
	
	public ProductVo show(Integer id){
		return ProductVo.fromProduct(ProductPo.findEntity(id));
	}
	
	
	public String delete(Integer id) throws Exception{
		ProductPo productPo=ProductPo.findEntity(id);
		BaseDAO.instance().remove(productPo);
		GlobalCache.synProducts();
		return "";
	}
	
	
	public Object fetchProductList(){
		RolePo rolePo = SessionUtil.getCurrentSessionRole();
		SessionUtil.checkSessionLost(rolePo);
		String key = rolePo.getId() +"_ProductRemoting.fetchProductList";
		GlobalCache.checkProtocolFrequencyResponse(key, 250l,true);	
		List<ProductPo> resulsts=new ArrayList<ProductPo>();
		List<ProductPo> allProducts = GlobalCache.fetchAllPruducts();
//		for (ProductPo productPo : allProducts) {
//			Long startTime =Long.valueOf(productPo.getItemSell()+"000");
//			Long endTime =Long.valueOf(productPo.getTimeOff()+"000");
//			if(System.currentTimeMillis()>=startTime && System.currentTimeMillis()<=endTime){
//				resulsts.add(productPo);
//			}
//		}
		SessionUtil.addDataArray(allProducts);
		return SessionType.MULTYPE_RETURN;
	}

	/**
	 * 查找批次号和服务区ID
	 * @param ids
	 * @return 批次号和服务区ID
	 */
	public Object[] fetchLotNumberByIds(String ids){
		Object [] objs = new Object [2];
		List<String> list = new ArrayList<String>();//批次号
		List<Integer> list2 = new ArrayList<Integer>();//服务器
		if(StringUtil.isNotEmpty(ids)){
			List<Map<String, Object>> ll = BaseDAO.instance().jdbcTemplate.queryForList("select lot_number, servers from u_po_product where id in(" + ids + ")");
			for(Map<String, Object> map : ll){
				if(map.get("lot_number") != null) list.add((String) map.get("lot_number"));
				if(map.get("servers") != null){
					List<Integer> servers = StringUtil.getListByStr((String) map.get("servers"), ",");
					for(Integer server : servers){
						if(!list2.contains(server)){
							list2.add(server);
						}
					}
				}
			}
		}
		objs[0] = list;
		objs[1] = list2;
		return objs;
	}

	/**
	 * 删除商品
	 * @param lotNumber 批次号("'aa','bb'")
	 * @return
	 */
	public Integer deleteByLotNumber(String lotNumber){
		List<ProductPo> list = BaseDAO.instance().dBfind("from ProductPo where lotNumber in ("+ lotNumber +")");
		boolean needSync = false;
		for(ProductPo productPo : list){
			if(productPo != null){
				BaseDAO.instance().remove(productPo);
				if(!needSync) needSync = true;
			}
		}
		if(needSync) GlobalCache.synProducts();
		return SessionType.SUCCESS;
	}

	/**
	 * 批量删除商品(GMT)
	 * @param ids 活动ID
	 * @return
	 */
	public Integer batchDelete(String ids){
		List<Integer> list = StringUtil.getListByStr(ids);
		boolean needSync = false;
		for(Integer id : list){
			ProductPo roductPo = ProductPo.findEntity(id);
			if(roductPo != null){
				BaseDAO.instance().remove(roductPo);
				if(!needSync) needSync = true;
			}
		}
		if(needSync) GlobalCache.synProducts();
		return SessionType.SUCCESS;
	}
}
