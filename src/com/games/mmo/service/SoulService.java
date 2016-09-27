package com.games.mmo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import com.games.mmo.po.game.SoulElementPo;
import com.games.mmo.po.game.SoulStepPo;
import com.games.mmo.type.SoulType;
import com.games.mmo.type.SoulType.Type;
import com.storm.lib.base.BaseService;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.IntUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

@Controller
public class SoulService extends BaseService{

	/**
	 * 随机出未满级属性
	 * @param soulAtbs
	 * @param soulLv
	 * @return
	 */
	public IdNumberVo randomaSoulAtb(Map<Integer,List<IdNumberVo2>> soulMap){
		IdNumberVo idNumberVo = new IdNumberVo();
		List<IdNumberVo> randomList = fetchCanUpSoulAtb(soulMap);
		if(randomList.isEmpty()){
			return null;
		}
		int index = 0;
		if(randomList.size()>1){
			index = IntUtil.getRandomInt(0, randomList.size()-1);
		}
		idNumberVo = randomList.get(index);
		return idNumberVo;
	}
	
	/**
	 * 获取某种属性等级
	 * @param soulAtbs
	 * @param type
	 * @return
	 */
	public int fetchSoulAtbLv(Map<Integer,List<IdNumberVo2>> soulMap, SoulType.Type type){
		IdNumberVo2 idNumberVo2 = fetchSoulAtb(soulMap, type);
		return idNumberVo2==null?0:idNumberVo2.getInt2().intValue();
	}
	
	/**
	 * 获取某种属性值对象
	 * @param soulAtbs
	 * @param type
	 * @return
	 */
	public IdNumberVo2 fetchSoulAtb(Map<Integer,List<IdNumberVo2>> soulMap, SoulType.Type type){
		int soulId = type.getId();
		int soulLv = soulMap.size() - 1;
		List<IdNumberVo2> soulAtbs = soulMap.get(soulLv);
		for(IdNumberVo2 idNumberVo2: soulAtbs){
			if(idNumberVo2.getInt1().intValue()==soulId){
				return idNumberVo2;
			}
		}
		return null;
	}
	
	/**
	 * 获取某等级灵魂需求经验
	 * @param soulLv
	 * @param soulAtbLv
	 * @return
	 */
	public int fetchAddExpByAtbLv(int soulLv, int soulAtbLv){
		SoulElementPo soulElementPo = SoulElementPo.findEntityByStepAndLv(soulLv, soulAtbLv);
		if(soulElementPo==null){
			return 0;
		}
		return soulElementPo.getExp().intValue();
	} 
	
	/**
	 * 给某种属性灵魂加经验
	 * @param soulAtbs
	 * @param type
	 * @param exp
	 * @return
	 */
	public IdNumberVo2 addSoulAtbExp(Map<Integer,List<IdNumberVo2>> soulMap, SoulType.Type type, int exp){
		int soulLv = soulMap.size() - 1;
		IdNumberVo2 idNumberVo2 = fetchSoulAtb(soulMap, type);
		if(idNumberVo2==null){
			return null;
		}
		int maxLv = fetchSoulMaxByStep(soulLv);
		int currentLv = idNumberVo2.getInt2().intValue();
		if(currentLv>=maxLv){
			return null;
		}
		int currentExp = idNumberVo2.getInt3().intValue();
		currentExp += exp;
		while(true){
			int needExp = fetchAddExpByAtbLv(soulLv, currentLv);
			if(currentExp<needExp){
				break;
			}
			if(currentExp==needExp){
				currentExp = 0;
				currentLv++;
				break;
			}
			currentExp -= needExp;
			currentLv ++;
			if(currentLv==maxLv){
				break;
			}
		}
		idNumberVo2.setInt2(currentLv);
		idNumberVo2.setInt3(currentExp);
		return idNumberVo2;
	}
	
	/**
	 * 修改五行属性类型
	 * @param soulMap
	 * @param currentType
	 * @return
	 */
	public int changeSoulAtbType(Map<Integer,List<IdNumberVo2>> soulMap, int currentType){
		int soulLv = soulMap.size() - 1;
		List<IdNumberVo2> idNumberVo2s = soulMap.get(soulLv);
		IdNumberVo2 maxIdNumberVo2 = null;
		if(currentType>0){
			IdNumberVo2 currentIdNumberVo2 = fetchSoulAtb(soulMap, SoulType.Type.getType(currentType));
			maxIdNumberVo2 = currentIdNumberVo2;
			for(IdNumberVo2 idNumberVo2:idNumberVo2s){
				if(idNumberVo2.getInt1().intValue()==currentIdNumberVo2.getInt1().intValue()){
					continue;
				}
				if(idNumberVo2.getInt2().intValue()>currentIdNumberVo2.getInt2().intValue()){
					maxIdNumberVo2 = idNumberVo2;
				}
			}
		}else{
			for(IdNumberVo2 idNumberVo2:idNumberVo2s){
				if(maxIdNumberVo2==null){
					maxIdNumberVo2 = idNumberVo2;
					continue;
				}
				if(idNumberVo2.getInt2().intValue()>maxIdNumberVo2.getInt2().intValue()){
					maxIdNumberVo2 = idNumberVo2;
				}
			}
		}
		return maxIdNumberVo2.getInt1().intValue();
	}
	
	/**
	 * 初始化灵魂各属性值
	 * @return
	 */
	public Map<Integer,List<IdNumberVo2>> buildSoulAtb(String soulAtb){
		Map<Integer, List<IdNumberVo2>> soulMap = new HashMap<Integer, List<IdNumberVo2>>();
		if(soulAtb==null){
			List<IdNumberVo2> soulAtbVos = new ArrayList<IdNumberVo2>();
			for(Type key:SoulType.Type.values()){
				if(key.getId()==0){
					continue;
				}
				IdNumberVo2 idNumberVo2 = new IdNumberVo2();
				idNumberVo2.setInt1(key.getId());
				idNumberVo2.setInt2(1);
				idNumberVo2.setInt3(0);
				soulAtbVos.add(idNumberVo2);
			}
			soulMap.put(0, soulAtbVos);
		}else{
			String[] soulAtbs = soulAtb.split(";");
			for(String atb:soulAtbs){
				String[] atbs = atb.split("@");
				Integer soulLv = Integer.valueOf(atbs[0]);
				List<IdNumberVo2> soulAtbVos = IdNumberVo2.createList(atbs[1]);
				soulMap.put(soulLv, soulAtbVos);
			}
		}
		return soulMap;
	}
	
	/**
	 * 灵魂值生成可保存String
	 * @param soulMap
	 * @return
	 */
	public String createSoulAtb(Map<Integer,List<IdNumberVo2>> soulMap){
		StringBuffer soulAtb = null;
		if(!soulMap.isEmpty()){
			soulAtb = new StringBuffer();
			int cnt = 0;
			for(Integer soulLv:soulMap.keySet()){
				List<IdNumberVo2> soulAtbVos = soulMap.get(soulLv);
				soulAtb.append(soulLv).append("@");
				soulAtb.append(IdNumberVo2.createStr(soulAtbVos));
				cnt++;
				if(cnt<soulMap.size()){
					soulAtb.append(";");
				}
			}
		}
		return soulAtb==null?null:soulAtb.toString();
	}
	
	/**
	 * 计算灵魂属性
	 * @param soulAtbs
	 * @param soulLv
	 * @return
	 */
	public List<List<Integer>> caculateBatExp(Map<Integer,List<IdNumberVo2>> soulMap){
		List<List<Integer>> batExp = new ArrayList<List<Integer>>();
		for(Integer soulLv:soulMap.keySet()){
			List<IdNumberVo2> soulAtbs = soulMap.get(soulLv);
			for(IdNumberVo2 idNumberVo2:soulAtbs){
				int id = idNumberVo2.getInt1().intValue();
				int lv = idNumberVo2.getInt2().intValue();
				SoulElementPo soulElementPo = SoulElementPo.findEntityByStepAndLv(soulLv, lv);
				List<IdNumberVo> idNumberVos = soulElementPo.addAtbMap.get(id);
				for(IdNumberVo idNumberVo:idNumberVos){
					List<Integer> list = new ArrayList<Integer>();
					list.add(idNumberVo.getId());
					list.add(idNumberVo.getNum());
					batExp.add(list);
				}	
			}	
			if(soulLv==soulMap.size()-1){
				continue;
			}
			SoulStepPo soulStepPo = SoulStepPo.findEntityByStep(soulLv);
			List<IdNumberVo> idNumberVos = soulStepPo.addAtbs;
			for(IdNumberVo idNumberVo:idNumberVos){
				List<Integer> list = new ArrayList<Integer>();
				list.add(idNumberVo.getId());
				list.add(idNumberVo.getNum());
				batExp.add(list);
			}
		}
		return batExp;
	}
	
	/**
	 * 获取可以用来随机选取的灵魂属性列表
	 * @param soulAtbs
	 * @param soulLv
	 * @return
	 */
	public List<IdNumberVo> fetchCanUpSoulAtb(Map<Integer,List<IdNumberVo2>> soulMap){
		List<IdNumberVo> randomList = new ArrayList<IdNumberVo>();
		int soulLv = soulMap.size() - 1;
		List<IdNumberVo2> soulAtbs = soulMap.get(soulLv);
		int maxLv = fetchSoulMaxByStep(soulLv);
		for(IdNumberVo2 idNumberVo2:soulAtbs){
			int id = idNumberVo2.getInt1().intValue();
			int lv = idNumberVo2.getInt2().intValue();
			if(lv>=maxLv){
				continue;
			}
			randomList.add(new IdNumberVo(id, lv));
		}
		return randomList;
	}
	
	/**
	 * 获取灵魂转生层的最大灵魂属性等级
	 * @param step
	 * @return
	 */
	private int fetchSoulMaxByStep(Integer step){
		List<SoulElementPo> soulElementPos = SoulElementPo.findEntityByStep(step);
		int maxLv = 0;
		for(SoulElementPo soulElementPo:soulElementPos){
			maxLv = Math.max(maxLv, soulElementPo.getLv().intValue());
		}
		return maxLv;
	}
	
	private static SoulService instance;
	public static SoulService instance() {
		if(instance==null){
			instance=(SoulService) BeanUtil.getBean("soulService");
		}
		return instance;
	}
	
	/**
	 * 将灵魂等级信息转化为属性信息
	 * @return
	 */
	public Map<String, List<IdNumberVo>> fetchSoulList(Map<Integer,List<IdNumberVo2>> soulAtbMap){
		Map<String, List<IdNumberVo>> soulMap = new HashMap<String, List<IdNumberVo>>();
		int soulLv = soulAtbMap.size()-1;
		List<IdNumberVo2> soulAtbVos = soulAtbMap.get(soulLv);
		for(IdNumberVo2 idNumberVo2:soulAtbVos){
			int id = idNumberVo2.getInt1().intValue();
			int lv = idNumberVo2.getInt2().intValue();
			SoulElementPo soulElementPo = SoulElementPo.findEntityByStepAndLv(soulLv, lv);
			List<IdNumberVo> idNumberVos = soulElementPo.addAtbMap.get(id);
			soulMap.put(String.valueOf(id), idNumberVos);
		}
		return soulMap;
	}
	
	/**
	 * 计算当前灵魂属性等级之和
	 * @param soulAtbMap
	 * @return
	 */
	public int sumSoulAtbLv(Map<Integer,List<IdNumberVo2>> soulAtbMap){
		int soulLv = soulAtbMap.size() - 1;
		List<IdNumberVo2> soulAtbs = soulAtbMap.get(soulLv);
		int sumLv = 0;
		for(IdNumberVo2 idNumberVo2:soulAtbs){
			sumLv += idNumberVo2.getInt2().intValue();
		}
		return sumLv;
	}
	
	/**
	 * 灵魂转生
	 * @param soulAtbMap
	 */
	public void resetSoul(Map<Integer,List<IdNumberVo2>> soulAtbMap){
		int soulLv = soulAtbMap.size();
		List<IdNumberVo2> soulAtbVos = new ArrayList<IdNumberVo2>();
		for(Type key:SoulType.Type.values()){
			if(key.getId()==0){
				continue;
			}
			IdNumberVo2 idNumberVo2 = new IdNumberVo2();
			idNumberVo2.setInt1(key.getId());
			idNumberVo2.setInt2(1);
			idNumberVo2.setInt3(0);
			soulAtbVos.add(idNumberVo2);
		}
		soulAtbMap.put(soulLv, soulAtbVos);
	}
	
	/**
	 * 灵魂重置
	 * @return
	 */
	public Map<Integer,List<IdNumberVo2>> backSoul(){
		Map<Integer,List<IdNumberVo2>> soulMap = new HashMap<Integer, List<IdNumberVo2>>();
		List<IdNumberVo2> soulAtbVos = new ArrayList<IdNumberVo2>();
		for(Type key:SoulType.Type.values()){
			if(key.getId()==0){
				continue;
			}
			IdNumberVo2 idNumberVo2 = new IdNumberVo2();
			idNumberVo2.setInt1(key.getId());
			idNumberVo2.setInt2(1);
			idNumberVo2.setInt3(0);
			soulAtbVos.add(idNumberVo2);
		}
		soulMap.put(0, soulAtbVos);
		return soulMap;
	}
	
	/**
	 * 查询当前消耗灵魂经验
	 * @param soulAtbMap
	 * @return
	 */
	public int fetchCurrentSoulCost(Map<Integer,List<IdNumberVo2>> soulAtbMap){
		int expTotal = 0;
		for(Integer soulLv:soulAtbMap.keySet()){
			List<IdNumberVo2> idNumberVo2s = soulAtbMap.get(soulLv);
			for(IdNumberVo2 idNumberVo2:idNumberVo2s){
				int lv = idNumberVo2.getInt2().intValue();
				int exp = idNumberVo2.getInt3().intValue();
				expTotal += exp;
				for(int i=1;i<lv;i++){
					int needExp = fetchAddExpByAtbLv(soulLv, i);
					expTotal += needExp;
				}
			}
		}
		return expTotal;
	}
}
