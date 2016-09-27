package com.games.mmo.remoting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.AbroadVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.AbroadPo;
import com.games.mmo.service.ChatService;
import com.games.mmo.service.RoleService;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.SessionType;
import com.storm.lib.util.StringUtil;

@Controller
public class AbroadRemoting  extends BaseRemoting{
	@Autowired
	private ChatService chatService;
	@Autowired
	private RoleService roleService;
	
	public Object send(String msg){
		chatService.sendHorse(msg);
		return GlobalCache.fetchLanguageMap("key76");
	}
	
	public Object sendChat(String msg){
		chatService.sendSystemWorldChat(msg);
		return GlobalCache.fetchLanguageMap("key76");
	}
	
	public List<AbroadVo> list(){
		List<AbroadVo> list =new ArrayList<AbroadVo>();
		list.addAll(AbroadVo.createFromPos());
		return list;
	}

	
	public String addEdit(Integer id,String noticeTime,String noticeInfo,Integer type,Integer timeType,Long startTime,Long endTime,Integer repeatMinutes,String lotNumber,String servers){
		int newId = id;
		if(id==0){
			AbroadPo abroadPo = new AbroadPo();
			abroadPo.setNoticeInfo(noticeInfo);
			abroadPo.setNoticeTime(noticeTime);
			abroadPo.setTimeType(timeType);
			abroadPo.setStartTime(startTime);
			abroadPo.setEndTime(endTime);
			abroadPo.setType(type);
			abroadPo.setRepeatMinutes(repeatMinutes);
			abroadPo.setLotNumber(lotNumber);
			abroadPo.setServers(servers);
			abroadPo.setCreateTime(System.currentTimeMillis());
			newId = BaseDAO.instance().insert(abroadPo).getId();
		} else{
			AbroadPo abroadPo = AbroadPo.findEntity(id);
			abroadPo.setNoticeInfo(noticeInfo);
			abroadPo.setNoticeTime(noticeTime);
			abroadPo.setType(type);
			abroadPo.setTimeType(timeType);
			abroadPo.setStartTime(startTime);
			abroadPo.setEndTime(endTime);
			abroadPo.setRepeatMinutes(repeatMinutes);
			BaseDAO.instance().syncToDB(abroadPo);
		}

		AbroadPo.freshAllNotice();
		
		return "" + newId;
	}
	
	
	public AbroadVo show(Integer id){
		return AbroadVo.fromPo(AbroadPo.findEntity(id));
	}
	
	
	public String delete(Integer id) throws Exception{
		BaseDAO.instance().remove(AbroadPo.findEntity(id));
		AbroadPo.freshAllNotice();
		return "";
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
			List<Map<String, Object>> ll = BaseDAO.instance().jdbcTemplate.queryForList("select lot_number, servers from u_po_abroad where id in(" + ids + ")");
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
	 * 删除商品(GMT)
	 * @param lotNumber 批次号("'aa','bb'")
	 * @return
	 */
	public Integer deleteByLotNumber(String lotNumber){
		List<AbroadPo> list = BaseDAO.instance().dBfind("from AbroadPo where lotNumber in ("+ lotNumber +")");
		boolean needSync = false;
		for(AbroadPo abroadPo : list){
			if(abroadPo != null){
				BaseDAO.instance().remove(abroadPo);
				if(!needSync) needSync = true;
			}
		}
		if(needSync) AbroadPo.freshAllNotice();
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
			AbroadPo abroadPo = AbroadPo.findEntity(id);
			if(abroadPo != null){
				BaseDAO.instance().remove(abroadPo);
				if(!needSync) needSync = true;
			}
		}
		if(needSync) AbroadPo.freshAllNotice();
		return SessionType.SUCCESS;
	}
}
