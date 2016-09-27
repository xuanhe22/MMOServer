package com.games.mmo.remoting;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Controller;

import com.games.backend.vo.DayKpiVo;
import com.games.backend.vo.HourKpiVo;
import com.games.backend.vo.MonthKpiVo;
import com.games.backend.vo.TableVo;
import com.games.backend.vo.UserSummaryVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.game.LvConfigPo;
import com.games.mmo.po.game.VipPo;
import com.games.mmo.service.ServerService;
import com.games.mmo.util.GameUtil;
import com.games.mmo.vo.DistributeComparator;
import com.games.mmo.vo.DistributeIntComparator;
import com.games.mmo.vo.RemainVo;
import com.games.mmo.vo.StatisticalAnalysisVo;
import com.games.mmo.vo.SummaryDayPo;
import com.games.mmo.vo.SummaryHourPo;
import com.games.mmo.vo.SummaryMonthPo;
import com.games.mmo.vo.SystemPartPo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;
import com.storm.lib.vo.IdNumberVo;

@Controller
public class DataRemoting {
	
	@Autowired
	private ServerService serverService;
	/**
	 * 获取小时KPI数据
	 * 2016-08-19废弃
	 * @param startTime
	 * @param endTime
	 * @param channelKeys
	 * @param type（0：所有，1：设备，2：账号）
	 * @return 
	 */
	public List<HourKpiVo> kpiHour(Long startTime,Long endTime,String channelKeys, Integer type){
		String sql = "";
		if (type == 0)
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,type,total from "+BaseStormSystemType.LOG_DB_NAME+".summary_hour where summary_time>="+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") limit 1000";
		else
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,hour_user_old,hour_user_new,pu,pu_new,charge_currency,remain,type,total from "+BaseStormSystemType.LOG_DB_NAME+".summary_hour where summary_time>="+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") and type = " + (type == 1 ? 2 : 6) + " limit 1000";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		List<HourKpiVo> result = new ArrayList<HourKpiVo>();
		while (rs.next()) {
			HourKpiVo hourKpiVo=new HourKpiVo();
			hourKpiVo.summary_time=rs.getLong(1);
			hourKpiVo.channel_key=rs.getString(2);
			hourKpiVo.hour_user_old=rs.getInt(3);
			hourKpiVo.hour_user_new=rs.getInt(4);
			hourKpiVo.pu=rs.getInt(5);
			hourKpiVo.pu_new=rs.getInt(6);
			hourKpiVo.charge_currency=rs.getInt(7);
			hourKpiVo.remain = rs.getInt(8);
			hourKpiVo.type = rs.getInt(9);
			hourKpiVo.total = rs.getInt(10);
			result.add(hourKpiVo);
		}
		return result;
	}	
	
	/**
	 * 日KPI(2016-08-19废弃)
	 * @param startTime
	 * @param endTime
	 * @param channelKeys
	 * @param type
	 * @return
	 */
	public Object kpiDay(Long startTime,Long endTime,String channelKeys,Integer type){
		String sql="SELECT (floor(summary_time/1000)*1000) as summary_time,channel_key,SUM(new_user_role_new) AS new_user_role_new,SUM(new_user_role_exist) AS new_user_role_exist,SUM(dau) AS dau,SUM(day_user_old) AS day_user_old,SUM(day_user_new) AS day_user_new," +
				"SUM(pu) AS pu,SUM(pu_new) AS pu_new,SUM(pu_percentage) AS pu_percentage,SUM(darpu) AS darpu,SUM(darppu) AS darppu,SUM(charge_currency) AS charge_currency,SUM(consume_diamond) AS consume_diamond,SUM(avg_online) AS avg_online,SUM(max_online) AS max_online,SUM(remain_diamond) " +
				"FROM "+BaseStormSystemType.LOG_DB_NAME+".summary_day WHERE type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+
				" and channel_key in ("+channelKeys+") GROUP BY channel_key,summary_time LIMIT 1000";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		HashMap<String, List<DayKpiVo>> dayKpiVoMap=new HashMap<String, List<DayKpiVo>>();
		while (rs.next()) {
			DayKpiVo dayKpiVo=new DayKpiVo();
			dayKpiVo.summary_time=rs.getLong(1);
			dayKpiVo.channel_key=rs.getString(2);
			dayKpiVo.new_user_role_new=rs.getInt(3);
			dayKpiVo.new_user_role_exist=rs.getInt(4);
			dayKpiVo.dau=rs.getInt(5);
			dayKpiVo.day_user_old=rs.getInt(6);
			dayKpiVo.day_user_new=rs.getInt(7);
			dayKpiVo.pu=rs.getInt(8);
			dayKpiVo.pu_new=rs.getInt(9);
			dayKpiVo.pu_percentage= (int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.pu * 10000.0 / dayKpiVo.dau);
			dayKpiVo.darpu=rs.getInt(11);
			dayKpiVo.darppu=rs.getInt(12);
			dayKpiVo.charge_currency=rs.getInt(13);
			dayKpiVo.consume_diamond=rs.getInt(14);
			dayKpiVo.avg_online=rs.getInt(15);
			dayKpiVo.max_online=rs.getInt(16);
			dayKpiVo.remainDiamond=rs.getInt(17);
			if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
				dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
			}
			dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
			//content+=dayKpiVo.toString();
		}
		
		if(channelKeys.contains("'0'")){
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,sum(new_user_role_new),sum(new_user_role_exist),sum(dau),sum(day_user_old),sum(day_user_new),sum(pu),sum(pu_new),sum(pu_percentage),sum(darpu),sum(darppu),sum(charge_currency),sum(consume_diamond),sum(avg_online),sum(max_online),sum(remain_diamond) from "+BaseStormSystemType.LOG_DB_NAME+".summary_day where type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") group by summary_time limit 1000";
			rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while (rs.next()) {
				DayKpiVo dayKpiVo=new DayKpiVo();
				dayKpiVo.summary_time=rs.getLong(1);
				dayKpiVo.channel_key="all";
				dayKpiVo.new_user_role_new=rs.getInt(3);
				dayKpiVo.new_user_role_exist=rs.getInt(4);
				dayKpiVo.dau=rs.getInt(5);
				dayKpiVo.day_user_old=rs.getInt(6);
				dayKpiVo.day_user_new=rs.getInt(7);
				dayKpiVo.pu=rs.getInt(8);
				dayKpiVo.pu_new=rs.getInt(9);
				dayKpiVo.pu_percentage = (int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.pu * 10000.0 / dayKpiVo.dau);
				dayKpiVo.charge_currency=rs.getInt(13);
				dayKpiVo.consume_diamond=rs.getInt(14);
				dayKpiVo.avg_online=rs.getInt(15);
				dayKpiVo.max_online=rs.getInt(16);
				dayKpiVo.remainDiamond=rs.getInt(17);
				dayKpiVo.darpu=(int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.charge_currency * 100.0 / dayKpiVo.dau);
				dayKpiVo.darppu=(int)(dayKpiVo.pu == 0 ? 0 : dayKpiVo.charge_currency * 100.0/ dayKpiVo.pu);
				if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
					dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
				}
				dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
			}	
		}
		
//		HashMap<String, TableVo> results=new HashMap<String, TableVo>();
//		Iterator iter = dayKpiVoMap.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry entry = (Map.Entry) iter.next();
//			String key = (String)entry.getKey();
//			TableVo tableVo = null;
//			if (type == 1) {//设备
//				tableVo=new TableVo(new String[]{"日期","新账号(注册)","新账号(有角色)","设备DAU","设备老用户","设备新用户","设备PU","设备新PU","设备PU%","设备DARPU","设备DARPPU","充值(RMB)","消耗(非绑钻)","平均在线","最高在线","剩余钻石(非绑钻)"});
//			} else {
//				tableVo=new TableVo(new String[]{"日期","新账号(注册)","新账号(有角色)","账号DAU","账号老用户","账号新用户","账号PU","账号新PU","账号PU%","账号DARPU","账号DARPPU","充值(RMB)","消耗(非绑钻)","平均在线","最高在线","剩余钻石(非绑钻)"});
//			}
//			List<DayKpiVo> dayKpiVos = (List<DayKpiVo>)entry.getValue();
//			Collections.sort(dayKpiVos, new DayKpiComparator());
//			for (DayKpiVo dayKpiVo2 : dayKpiVos) {
//				tableVo.addRow(dayKpiVo2.toObjs());
//			}
//			results.put(key, tableVo);
//		}

		return dayKpiVoMap;
	}
	
	public Object kpiMonth(Long startTime,Long endTime,String channelKeys,Integer type){
		String content="";
		String sql="select summary_time,channel_key,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,pu_percentage,darpu,darppu,charge_currency,consume_diamond from "+BaseStormSystemType.LOG_DB_NAME+".summary_month where type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") limit 1000";
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		HashMap<String, List<DayKpiVo>> dayKpiVoMap=new HashMap<String, List<DayKpiVo>>();
		while (rs.next()) {
			DayKpiVo dayKpiVo=new DayKpiVo();
			dayKpiVo.summary_time=rs.getLong(1);
			dayKpiVo.channel_key=rs.getString(2);
			dayKpiVo.new_user_role_new=rs.getInt(3);
			dayKpiVo.new_user_role_exist=rs.getInt(4);
			dayKpiVo.dau=rs.getInt(5);
			dayKpiVo.day_user_old=rs.getInt(6);
			dayKpiVo.day_user_new=rs.getInt(7);
			dayKpiVo.pu=rs.getInt(8);
			dayKpiVo.pu_new=rs.getInt(9);
			dayKpiVo.pu_percentage=rs.getInt(10);
			dayKpiVo.darpu=rs.getInt(11);
			dayKpiVo.darppu=rs.getInt(12);
			dayKpiVo.charge_currency=rs.getInt(13);
			dayKpiVo.consume_diamond=rs.getInt(14);
			if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
				dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
			}
			dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
		}
		
		if(channelKeys.contains("'0'")){
			sql="select summary_time,channel_key,new_user_role_new,new_user_role_exist,dau,day_user_old,day_user_new,pu,pu_new,pu_percentage,darpu,darppu,charge_currency,consume_diamond from "+BaseStormSystemType.LOG_DB_NAME+".summary_month where type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+" group by summary_time limit 1000";
			rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while (rs.next()) {
				DayKpiVo dayKpiVo=new DayKpiVo();
				dayKpiVo.summary_time=rs.getLong(1);
				dayKpiVo.channel_key="所有";
				dayKpiVo.new_user_role_new=rs.getInt(3);
				dayKpiVo.new_user_role_exist=rs.getInt(4);
				dayKpiVo.dau=rs.getInt(5);
				dayKpiVo.day_user_old=rs.getInt(6);
				dayKpiVo.day_user_new=rs.getInt(7);
				dayKpiVo.pu=rs.getInt(8);
				dayKpiVo.pu_new=rs.getInt(9);
				dayKpiVo.pu_percentage=rs.getInt(10);
				dayKpiVo.darpu=rs.getInt(11);
				dayKpiVo.darppu=rs.getInt(12);
				dayKpiVo.charge_currency=rs.getInt(13);
				dayKpiVo.consume_diamond=rs.getInt(14);
				if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
					dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
				}
				dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
			}	
		}
		
		HashMap<String, TableVo> results=new HashMap<String, TableVo>();
		Iterator iter = dayKpiVoMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			TableVo tableVo = null;
			if (type == 4) {//月设备
				tableVo=new TableVo(new String[]{"日期","新账号(注册)","新账号(有角色)","设备MAU","设备老用户","设备新用户","设备MPU","设备新MPU","设备MPU%","设备MARPU","设备MARPPU","充值（RMB）","月消耗(非绑钻)"});
			} else {//月账号
				tableVo=new TableVo(new String[]{"日期","新账号(注册)","新账号(有角色)","账号MAU","账号老用户","账号新用户","账号MPU","账号新MPU","账号MPU%","账号MARPU","账号MARPPU","充值（RMB）","月消耗(非绑钻)"});
			}
			List<DayKpiVo> dayKpiVos = (List<DayKpiVo>)entry.getValue();
			for (DayKpiVo dayKpiVo2 : dayKpiVos) {
				MonthKpiVo monthKpiVo = new MonthKpiVo(dayKpiVo2);
				tableVo.addRow(monthKpiVo.toObjs());
			}
			results.put(key, tableVo);
		}

		return results;
	}
	
	/**
	 * 留存统计(废弃方法)
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @param channelKeys
	 * @return
	 */
	public Object summaryRemain(Long startTime,Long endTime,Integer type, String channelKeys){
		StringBuilder channelString = new StringBuilder();
		String[] channels = channelKeys.split(",");
		boolean b = true;
		for (String string : channels) {
			if (b)
				b = false;
			else
				channelString.append(",");
			channelString.append("'" + string + "'");
		}
		String sql ="select id,type,summary_time,channel_key,equipment_new_role,day2,day3,day4,day5,day6,day7,day8,day14,day15,day30,day31,day45,day46,day60,day61,day90,day91 from "+BaseStormSystemType.LOG_DB_NAME+".summary_remain where type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+ " and channel_key in ("+channelString.toString()+") limit 1000";
//		List<Map<String,Object>> list = BaseDAO.instance().
		SqlRowSet rs1 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		TableVo tableVo = null;
		if(type.intValue()==1){ //设备留存
			tableVo=new TableVo(new String[]{"日期","新设备","次日<br/>留存率","第3日<br/>留存率","第4日<br/>留存率","第5日<br/>留存率","第6日<br/>留存率","第7日<br/>留存率","第8日<br/>留存率","第14日<br/>留存率","第15日<br/>留存率","第30日<br/>留存率","第31日<br/>留存率","第45日<br/>留存率","第46日<br/>留存率","第60日<br/>留存率","第61日<br/>留存率","第90日<br/>留存率","第91日<br/>留存率"});
		}else{
			tableVo=new TableVo(new String[]{"日期","新用户","次日<br/>留存率","第3日<br/>留存率","第4日<br/>留存率","第5日<br/>留存率","第6日<br/>留存率","第7日<br/>留存率","第8日<br/>留存率","第14日<br/>留存率","第15日<br/>留存率","第30日<br/>留存率","第31日<br/>留存率","第45日<br/>留存率","第46日<br/>留存率","第60日<br/>留存率","第61日<br/>留存率","第90日<br/>留存率","第91日<br/>留存率"});
		}
		Map<Long, RemainVo> resultMap = new TreeMap<Long, RemainVo>();
		while (rs1.next()) {
			RemainVo remainVo = new RemainVo(rs1);
			if (resultMap.containsKey(remainVo.summary_time)) {
				RemainVo remainVo1 = resultMap.get(remainVo.summary_time);
				resultMap.put(remainVo.summary_time, remainVo1.add(remainVo));
			} else {
				resultMap.put(remainVo.summary_time, remainVo);
			}
		}
		DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (RemainVo remainVo : resultMap.values()) {
			tableVo.addRow(new Object[]{df3.format(new Date(remainVo.summary_time)), remainVo.equipment_new_role, 
					remainVo.day2 / 100d + "%", remainVo.day3 / 100d + "%", remainVo.day4 / 100d + "%", remainVo.day5 / 100d + "%", remainVo.day6 / 100d + "%", remainVo.day7 / 100d + "%", remainVo.day8 / 100d + "%", remainVo.day14 / 100d + "%"
					, remainVo.day15 / 100d + "%", remainVo.day30 / 100d + "%", remainVo.day31 / 100d + "%", remainVo.day45 / 100d + "%", remainVo.day46 / 100d + "%", remainVo.day60 / 100d + "%", remainVo.day61 / 100d + "%", remainVo.day90 / 100d + "%", remainVo.day91 / 100d + "%"});
		}
//			String date = DateUtil.getFormatDateBytimestamp(Long.valueOf(map.get("summary_time").toString()));
//			if (resultMap.containsKey(date)) {
//				List<Object> list2 = resultMap.get(date);
//				List<Object> list3 = new ArrayList<Object>();
//				list3.set(0, element)
//			} else {
//				
//			}
//			Object[] objs = new Object[]{date,map.get("equipment_new_role"),Double.valueOf(map.get("day2").toString())/100+"%",
//					Double.valueOf(map.get("day3").toString())/100+"%",
//					Double.valueOf(map.get("day4").toString())/100+"%",
//					Double.valueOf(map.get("day5").toString())/100+"%",
//					Double.valueOf(map.get("day6").toString())/100+"%",
//					Double.valueOf(map.get("day7").toString())/100+"%",
//					Double.valueOf(map.get("day8").toString())/100+"%",
//					Double.valueOf(map.get("day14").toString())/100+"%",
//					Double.valueOf(map.get("day15").toString())/100+"%",
//					Double.valueOf(map.get("day30").toString())/100+"%",
//					Double.valueOf(map.get("day31").toString())/100+"%",
//					Double.valueOf(map.get("day45").toString())/100+"%",
//					Double.valueOf(map.get("day46").toString())/100+"%",
//					Double.valueOf(map.get("day60").toString())/100+"%",
//					Double.valueOf(map.get("day61").toString())/100+"%",
//					Double.valueOf(map.get("day90").toString())/100+"%",
//					Double.valueOf(map.get("day91").toString())/100+"%"};
//			tableVo.addRow(objs);
//		}
		return tableVo;
	}
	
	
	/**
	 * 获取用户数据
	 * @param startTime
	 * @param endTime
	 * @param filter
	 * @param device
	 * @param chargeStart
	 * @param chargeEnd
	 * @param roleId
	 * @param userIuid
	 * @param roleName
	 * @param iCurrPage（当前页）
	 * @param type（排序规则）
	 * @param sort（升序ture）
	 * @return 
	 */
	public Object userSummary(Long startTime,Long endTime,Integer filter,Integer device,Integer chargeStart,Integer chargeEnd,Integer roleId,String userIuid,String roleName, Integer iCurrPage, String orderBy, Integer sort){
    	List<UserSummaryVo> userSummaryVos = GameUtil.findUserSummary(startTime,endTime, filter, device, chargeStart, chargeEnd, roleId,userIuid, roleName, iCurrPage, orderBy, sort);
    	return userSummaryVos;
	}


	/**
	 *  后台查询分布信息接口
	 *  	已废弃，改为distributeInfo接口
	 * @param type
	 * @param startTime
	 * @param endTime
	 * @param filter
	 * @param ChannelsDiv
	 * @param vipStart
	 * @param vipEnd
	 * @param career
	 * @param selectUser
	 * @param selectDevice
	 * @param unLoginDays
	 * @return
	 */
	public Object distribute(Integer type,Long startTime,Long endTime,Integer filter,String ChannelsDiv,Integer vipStart,Integer vipEnd,Integer career,Integer selectUser,Integer selectDevice, Integer unLoginDays){
		String finalResult="";
		HashMap<String, String> idNameList = new HashMap<String, String>();
		
		if(type==1){//不会进入的条件判断
			finalResult="【在线时间查询结果】</br>";
			List<IdNumberVo> list =new ArrayList<IdNumberVo>();
			IdNumberVo idNumberVo = new IdNumberVo();
			idNumberVo.setId(0);
			idNumberVo.setNum(10);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "0-10s");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(11);
			idNumberVo.setNum(30);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "11-30s");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(31);
			idNumberVo.setNum(60);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "31s-1min");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(61);
			idNumberVo.setNum(300);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "1min-5min");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(301);
			idNumberVo.setNum(600);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "5min-10min");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(601);
			idNumberVo.setNum(1800);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "10min-30min");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(1801);
			idNumberVo.setNum(3600);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "30min-1h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(3601);
			idNumberVo.setNum(7200);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "1h-2h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(7201);
			idNumberVo.setNum(10800);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "2h-3h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(10801);
			idNumberVo.setNum(14400);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "3h-4h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(14401);
			idNumberVo.setNum(18000);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "4h-5h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(18001);
			idNumberVo.setNum(21600);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "5h-6h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(21601);
			idNumberVo.setNum(25200);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "5h-6h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(25201);
			idNumberVo.setNum(28800);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "6h-7h");
			list.add(idNumberVo);

			idNumberVo = new IdNumberVo();
			idNumberVo.setId(28801);
			idNumberVo.setNum(32400);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "7h-8h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(32401);
			idNumberVo.setNum(36000);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "8h-9h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(36001);
			idNumberVo.setNum(39600);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "9h-10h");
			list.add(idNumberVo);
			
			idNumberVo = new IdNumberVo();
			idNumberVo.setId(39601);
			idNumberVo.setNum(31536000);
			idNameList.put(idNumberVo.getId()+"~"+idNumberVo.getNum(), "10h+");
			list.add(idNumberVo);
			
			StringBuffer sb=new StringBuffer();
			sb.append("select total_online,user_id from "+BaseStormSystemType.USER_DB_NAME+".u_po_role where vip_lv>="+vipStart+" and vip_lv<="+vipEnd);
			if(career!=0){
				sb.append(" and career="+career);
			}
			if(filter==1){
				sb.append(" and user_created_time>="+startTime+" and user_created_time<="+endTime); 
			}
			if(filter==2){
				sb.append(" and create_time>="+startTime+" and create_time<="+endTime);
			}
			sb.append(" and channel_key in ("+ChannelsDiv+")");
//			System.out.println("sql="+sb.toString());
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			HashMap<Integer, Integer> userOnlines =new HashMap<Integer, Integer>();
			while (rs.next()) {
				int totalOnline = rs.getInt(1);
				int userId = rs.getInt(2);	
				if(!userOnlines.containsKey(userId)){
					userOnlines.put(userId, 0);
				}
				userOnlines.put(userId, userOnlines.get(userId)+totalOnline);
			}
			
//			TreeMap<String, Integer> finalCount =new TreeMap<String, Integer>();
			
			TreeMap<String, Integer> finalCount = new TreeMap<String, Integer>(new DistributeComparator());
			
			Iterator iter = userOnlines.entrySet().iterator();
			int total=0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Integer val = (Integer)entry.getValue();
				for (IdNumberVo idNumberVo2 : list) {
					if(!finalCount.containsKey(idNumberVo2.getId()+"~"+idNumberVo2.getNum())){
						finalCount.put(idNumberVo2.getId()+"~"+idNumberVo2.getNum(),0);
					}
					if(val>=idNumberVo2.getId() && val<=idNumberVo2.getNum()){
						finalCount.put(idNumberVo2.getId()+"~"+idNumberVo2.getNum(), finalCount.get(idNumberVo2.getId()+"~"+idNumberVo2.getNum())+1);
						total++;
					}
				}
			}
		
			iter = finalCount.entrySet().iterator();
			finalResult+="<table border=2 style='width:50%'><tr><td>Time</td><td>count</td><td>percentage</td></tr>";
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				Integer val = (Integer)entry.getValue();
				double per=val*100d/total;
				String perStr= new DecimalFormat("0.00").format(per);
				finalResult+="<tr><td>"+idNameList.get(key)+"</td><td>"+val+"</td><td>"+perStr+"%</td></tr>";
			}
			finalResult+="</table>";
		}else if(type==2){ //lv分布
			StringBuffer sb=new StringBuffer();
			// 按账号查询
			if(selectUser!=0){
				sb.append("select user_id,lv,career,last_login_time from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}else if(selectDevice!=0){ // 按设备查询
				sb.append("select device_id,lv,career,last_login_time from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}
			
//			if(career!=0){
//				sb.append(" and career="+career);
//			}
			if(vipStart!=0){
				sb.append(" and vip_lv >=").append(vipStart);
			}
			if(vipEnd!=0){
				sb.append(" and vip_lv <=").append(vipEnd);
			}
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
			}
			if(filter==1){
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}
			if(filter==2){
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}
			if(filter==3){
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
	
			sb.append(" and channel_key in ("+ChannelsDiv+")");
			
			// 1.过滤，取最大等级
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			HashMap<String, StatisticalAnalysisVo> userLv =new HashMap<String, StatisticalAnalysisVo>();
			while (rs.next()) {
				String selectId = rs.getString(1);
				int lv = rs.getInt(2);
				int currentCareer=rs.getInt(3);
				long lastLoginTime=rs.getLong(4);
				StatisticalAnalysisVo statisticalAnalysisVo = new StatisticalAnalysisVo();
				statisticalAnalysisVo.selectId=selectId;
				statisticalAnalysisVo.lv = lv;
				statisticalAnalysisVo.career =currentCareer;
				statisticalAnalysisVo.lastLoginTime = lastLoginTime;
				if(!userLv.containsKey(selectId)){
					userLv.put(selectId, statisticalAnalysisVo);
				}
				if(lv > userLv.get(selectId).lv){
					userLv.put(selectId, statisticalAnalysisVo);
				}else if(lv == userLv.get(selectId).lv){
					if(lastLoginTime > userLv.get(selectId).lastLoginTime){
						userLv.put(selectId, statisticalAnalysisVo);
					}
				}
			}
			// 2.合并数据,排序
			TreeMap<Integer, IdNumberVo> finalCount = new TreeMap<Integer, IdNumberVo>(new DistributeIntComparator());  
			Iterator iter = userLv.entrySet().iterator();
			int total=0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				StatisticalAnalysisVo statisticalAnalysisVo = (StatisticalAnalysisVo) entry.getValue();
				int lv = statisticalAnalysisVo.lv;
				
				if(career !=0 && career != statisticalAnalysisVo.career.intValue()){
					continue;
				}
				if(finalCount.containsKey(lv)){
					IdNumberVo idNumberVo = finalCount.get(lv);
					idNumberVo.addNum(1);
				}else{
					finalCount.put(lv, new IdNumberVo(lv,1));
				}
				
				total++;
			}
			// 3.计算
//			Iterator iter2 = finalCount.entrySet().iterator();
			double totalPer = 0L;
			TableVo tableVo=new TableVo(new String[]{"等级","当前人数","等级停留","累积停留"});
//			while (iter2.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter2.next();
//				IdNumberVo idNumberVo = (IdNumberVo) entry.getValue();
//				double per1 = (1d*idNumberVo.getNum()/total)*100;
//				totalPer+=per1;
//				double per2 = totalPer;
//				String perStr1= new DecimalFormat("00.00").format(per1);
//				String perStr2= new DecimalFormat("00.00").format(per2);
//				Object[] objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%",perStr2+"%"};
//				tableVo.addRow(objs);
//			}
			for(LvConfigPo lvConfigPo:GlobalCache.lvConfigPos)
			{
				IdNumberVo idNumberVo = finalCount.get(lvConfigPo.getPlayerLevel());
				Object[] objs;
				if(idNumberVo != null)
				{
					double per1 = (1d*idNumberVo.getNum()/total)*100;
					totalPer+=per1;
					double per2 = totalPer;
					String perStr1= new DecimalFormat("00.00").format(per1);
					String perStr2= new DecimalFormat("00.00").format(per2);
					objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%",perStr2+"%"};
				}
				else
					objs = new Object[]{lvConfigPo.getPlayerLevel(),0,"00.00%","00.00%"};
				tableVo.addRow(objs);
			}
			finalResult=tableVo.toHtmlTable(1,600);
		}else if(type==3){ // vip分布
			StringBuffer sb=new StringBuffer();
			// 按账号查询
			if(selectUser!=0){
				sb.append("select user_id,vip_lv from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}else if(selectDevice!=0){ // 按设备查询
				sb.append("select device_id,vip_lv from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}
			
			if(career!=0){
				sb.append(" and career="+career);
			}
			if(vipStart!=0){
				sb.append(" and vip_lv >=").append(vipStart);
			}
			if(vipEnd!=0){
				sb.append(" and vip_lv <=").append(vipEnd);
			}
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
			}
			if(filter==1){
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}
			if(filter==2){
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}
			if(filter==3){
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
			sb.append(" and channel_key in ("+ChannelsDiv+")");
			
			// 1.过滤，取最大等级
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			HashMap<String, Integer> userLv =new HashMap<String, Integer>();
			while (rs.next()) {
				String selectId = rs.getString(1);
				int vipLv = rs.getInt(2);
				if(!userLv.containsKey(selectId)){
					userLv.put(selectId, vipLv);
				}
				if(vipLv > userLv.get(selectId)){
					userLv.put(selectId, vipLv);
				}
			}
			// 2.合并数据,排序
			TreeMap<Integer, IdNumberVo> finalCount = new TreeMap<Integer, IdNumberVo>(new DistributeIntComparator());
			Iterator iter = userLv.entrySet().iterator();
			int total=0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				Integer val = (Integer)entry.getValue();
				if(finalCount.containsKey(val)){
					IdNumberVo idNumberVo = finalCount.get(val);
					idNumberVo.addNum(1);
				}else{
					finalCount.put(val, new IdNumberVo(val,1));
				}
				total++;
			}
			// 3.计算
//			Iterator iter2 = finalCount.entrySet().iterator();
			TableVo tableVo=new TableVo(new String[]{"vip等级","账号数","占比"});
//			while (iter2.hasNext()) {
//				Map.Entry entry = (Map.Entry) iter2.next();
//				IdNumberVo idNumberVo = (IdNumberVo) entry.getValue();
//				double per1 = (1d*idNumberVo.getNum()/total)*100;
//				String perStr1= new DecimalFormat("00.00").format(per1);
//				Object[] objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%"};
//				tableVo.addRow(objs);
//			}
			for(VipPo vipPo:GlobalCache.listVipPo)
			{
				IdNumberVo idNumberVo = finalCount.get(vipPo.getVipLv());
				Object[] objs;
				if(idNumberVo != null)
				{
					double per1 = (1d*idNumberVo.getNum()/total)*100;
					String perStr1= new DecimalFormat("00.00").format(per1);
					objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%"};
				}
				else
					objs = new Object[]{vipPo.getVipLv(),0,"00.00%"};
				tableVo.addRow(objs);
			}
			finalResult=tableVo.toHtmlTable(1,600);
		}else if(type == 4){ // 职业分布
			StringBuffer sb=new StringBuffer();
			sb.append("select career,count(1) as num from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			if(career!=0){
				sb.append(" and career="+career);
			}
			if(vipStart!=0){
				sb.append(" and vip_lv >=").append(vipStart);
			}
			if(vipEnd!=0){
				sb.append(" and vip_lv <=").append(vipEnd);
			}
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
			}
			if(filter==1){
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}
			if(filter==2){
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}
			if(filter==3){
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
			sb.append(" and channel_key in ("+ChannelsDiv+")");
			sb.append(" group by career");
			
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			TreeMap<Integer, Integer> finalCount = new TreeMap<Integer, Integer>(new DistributeIntComparator()); 
			int total=0;
			while (rs.next()) {
				int careerId = rs.getInt(1);
				int num = rs.getInt(2);
				finalCount.put(careerId, num);
				total+=num;
			}
			 
			Iterator iter = finalCount.entrySet().iterator();
			TableVo tableVo=new TableVo(new String[]{"职业","数量","占比"});
			Object[] objs1 = new Object[]{"战士",0,"00.00%"};
			Object[] objs2 = new Object[]{"弓箭手",0,"00.00%"};
			Object[] objs3 = new Object[]{"法师",0,"00.00%"};
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int careerId = (Integer) entry.getKey();
				Object[] objs = null;
				switch (careerId) {
				case 1: objs = objs1; break;
				case 2: objs = objs2; break;
				case 3: objs = objs3; break;
				}
				if(objs != null)
				{
					int num = (Integer) entry.getValue();
					double per1 = (1d*num/total)*100;
					String perStr1= new DecimalFormat("00.00").format(per1);
					objs[1] = num;
					objs[2] = perStr1+"%";
				}
			}
			tableVo.addRow(objs1);
			tableVo.addRow(objs2);
			tableVo.addRow(objs3);
			finalResult=tableVo.toHtmlTable(1,600);
			
		}
		
		return finalResult;
	}
	
	/**
	 * 后台查询分布信息接口
	 * @param type 
	 * @param startTime
	 * @param endTime
	 * @param filter
	 * @param vipStart
	 * @param vipEnd
	 * @param career
	 * @param selectUser
	 * @param selectDevice
	 * @param unLoginDays
	 * @return
	 */
	public Object distributeInfo(Integer type,Long startTime,Long endTime,Integer filter,Integer vipStart,Integer vipEnd,Integer career,Integer selectUser,Integer selectDevice, Integer unLoginDays){
		List<Object[]> list = new ArrayList<Object[]>();
		if(type==2){ //等级分布
			StringBuffer sb=new StringBuffer();
			
			//搜索类型
			if(selectUser!=0){//按账号查询
				sb.append("select user_id,lv,career,last_login_time from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}else if(selectDevice!=0){//按设备查询
				sb.append("select device_id,lv,career,last_login_time from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}
			
			//过滤器
			if(filter==1){//按创建账号方式过滤
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}else if(filter==2){//按创建角色方式过滤
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}else if(filter==3){//按角色最近登录方式
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
			
			//VIP
			if(vipStart!=0){
				sb.append(" and vip_lv >=").append(vipStart);
			}
			if(vipEnd!=0){
				sb.append(" and vip_lv <=").append(vipEnd);
			}
			
			//至今连续几天未登录
			List<String> excludeUser = null;
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
				String excludeSql = null;
				if(selectUser!=0){//按账号查询
					excludeSql = "select user_id from " + BaseStormSystemType.USER_DB_NAME + ".u_po_role where last_login_time>"+time;
				}else if(selectDevice!=0){//按设备查询
					excludeSql = "select device_id from " + BaseStormSystemType.USER_DB_NAME + ".u_po_role where last_login_time>"+time;
				}
				excludeUser = BaseDAO.instance().jdbcTemplate.queryForList(excludeSql, String.class);
			}
			
			// 1.过滤，取最大等级
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			HashMap<String, StatisticalAnalysisVo> userLv =new HashMap<String, StatisticalAnalysisVo>();
			while (rs.next()) {
				if(excludeUser!=null && excludeUser.contains(rs.getString(1))) continue;
				String selectId = rs.getString(1);
				int lv = rs.getInt(2);
				int currentCareer=rs.getInt(3);
				long lastLoginTime=rs.getLong(4);
				StatisticalAnalysisVo statisticalAnalysisVo = new StatisticalAnalysisVo();
				statisticalAnalysisVo.selectId=selectId;
				statisticalAnalysisVo.lv = lv;
				statisticalAnalysisVo.career =currentCareer;
				statisticalAnalysisVo.lastLoginTime = lastLoginTime;
				if(!userLv.containsKey(selectId)){
					userLv.put(selectId, statisticalAnalysisVo);
				}
				if(lv > userLv.get(selectId).lv){
					userLv.put(selectId, statisticalAnalysisVo);
				}else if(lv == userLv.get(selectId).lv){
					if(lastLoginTime > userLv.get(selectId).lastLoginTime){
						userLv.put(selectId, statisticalAnalysisVo);
					}
				}
			}
			// 2.合并数据,排序
			TreeMap<Integer, IdNumberVo> finalCount = new TreeMap<Integer, IdNumberVo>(new DistributeIntComparator());  
			Iterator iter = userLv.entrySet().iterator();
			int total=0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				StatisticalAnalysisVo statisticalAnalysisVo = (StatisticalAnalysisVo) entry.getValue();
				int lv = statisticalAnalysisVo.lv;
				
				if(career !=0 && career != statisticalAnalysisVo.career.intValue()){
					continue;
				}
				if(finalCount.containsKey(lv)){
					IdNumberVo idNumberVo = finalCount.get(lv);
					idNumberVo.addNum(1);
				}else{
					finalCount.put(lv, new IdNumberVo(lv,1));
				}
				
				total++;
			}
			// 3.计算
			double totalPer = 0L;
			for(LvConfigPo lvConfigPo:GlobalCache.lvConfigPos){
				IdNumberVo idNumberVo = finalCount.get(lvConfigPo.getPlayerLevel());
				Object[] objs;
				if(idNumberVo != null){
					double per1 = (1d*idNumberVo.getNum()/total)*100;
					totalPer+=per1;
					double per2 = totalPer;
					String perStr1= new DecimalFormat("00.00").format(per1);
					String perStr2= new DecimalFormat("00.00").format(per2);
					objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%",perStr2+"%"};
				}else{
					objs = new Object[]{lvConfigPo.getPlayerLevel(),0,"00.00%","00.00%"};
				}
				list.add(objs);
			}
		}else if(type==3){ //vip分布
			StringBuffer sb=new StringBuffer();
			
			//搜索类型
			if(selectUser!=0){//按账号查询
				sb.append("select user_id,vip_lv from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}else if(selectDevice!=0){ // 按设备查询
				sb.append("select device_id,vip_lv from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			}
			
			//过滤器
			if(filter==1){//按创建账号方式过滤
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}else if(filter==2){//按创建角色方式过滤
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}else if(filter==3){//按角色最近登录方式
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
			
			//职业
			if(career!=0){
				sb.append(" and career="+career);
			}
			
//			if(vipStart!=0){
//				sb.append(" and vip_lv >=").append(vipStart);
//			}
//			if(vipEnd!=0){
//				sb.append(" and vip_lv <=").append(vipEnd);
//			}
			//至今连续几天未登录
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
			}
			
			// 1.过滤，取最大等级
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			HashMap<String, Integer> userLv =new HashMap<String, Integer>();
			while (rs.next()) {
				String selectId = rs.getString(1);
				int vipLv = rs.getInt(2);
				if(!userLv.containsKey(selectId)){
					userLv.put(selectId, vipLv);
				}
				if(vipLv > userLv.get(selectId)){
					userLv.put(selectId, vipLv);
				}
			}
			// 2.合并数据,排序
			TreeMap<Integer, IdNumberVo> finalCount = new TreeMap<Integer, IdNumberVo>(new DistributeIntComparator());
			Iterator iter = userLv.entrySet().iterator();
			int total=0;
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String key = (String)entry.getKey();
				Integer val = (Integer)entry.getValue();
				if(finalCount.containsKey(val)){
					IdNumberVo idNumberVo = finalCount.get(val);
					idNumberVo.addNum(1);
				}else{
					finalCount.put(val, new IdNumberVo(val,1));
				}
				total++;
			}
			// 3.计算
			for(VipPo vipPo:GlobalCache.listVipPo)
			{
				IdNumberVo idNumberVo = finalCount.get(vipPo.getVipLv());
				Object[] objs;
				if(idNumberVo != null){
					double per1 = (1d*idNumberVo.getNum()/total)*100;
					String perStr1= new DecimalFormat("00.00").format(per1);
					objs = new Object[]{idNumberVo.getId(),idNumberVo.getNum(),perStr1+"%"};
				}else{
					objs = new Object[]{vipPo.getVipLv(),0,"00.00%"};
				}
				list.add(objs);
			}
		}else if(type == 4){//职业分布
			StringBuffer sb=new StringBuffer();
			sb.append("select career,count(1) as num from ").append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role where 1=1 ");
			
			//过滤器
			if(filter==1){//按创建账号方式过滤
				sb.append(" and user_created_time>=").append(startTime).append(" and user_created_time<=").append(endTime); 
			}else if(filter==2){//按创建角色方式过滤
				sb.append(" and create_time>=").append(startTime).append(" and create_time<=").append(endTime);
			}else if(filter==3){//按角色最近登录方式
				sb.append(" and last_login_time>=").append(startTime).append(" and last_login_time<=").append(endTime);
			}
			
			//VIP
			if(vipStart!=0){
				sb.append(" and vip_lv >=").append(vipStart);
			}
			if(vipEnd!=0){
				sb.append(" and vip_lv <=").append(vipEnd);
			}
			
			//至今连续几天未登录
			if(unLoginDays!=0){
				long time = System.currentTimeMillis() - 1000l*60*60*24*unLoginDays;
				sb.append(" and last_login_time<=").append(time);
			}
			sb.append(" group by career");
			
			SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
			TreeMap<Integer, Integer> finalCount = new TreeMap<Integer, Integer>(new DistributeIntComparator()); 
			int total=0;
			while (rs.next()) {
				int careerId = rs.getInt(1);
				int num = rs.getInt(2);
				finalCount.put(careerId, num);
				total+=num;
			}
			 
			Iterator iter = finalCount.entrySet().iterator();
			Object[] objs1 = new Object[]{GlobalCache.fetchLanguageMap("key2529"),0,"00.00%"};
			Object[] objs2 = new Object[]{GlobalCache.fetchLanguageMap("key2530"),0,"00.00%"};
			Object[] objs3 = new Object[]{GlobalCache.fetchLanguageMap("key2531"),0,"00.00%"};
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				int careerId = (Integer) entry.getKey();
				Object[] objs = null;
				switch (careerId) {
				case 1: objs = objs1; break;
				case 2: objs = objs2; break;
				case 3: objs = objs3; break;
				}
				if(objs != null)
				{
					int num = (Integer) entry.getValue();
					double per1 = (1d*num/total)*100;
					String perStr1= new DecimalFormat("00.00").format(per1);
					objs[1] = num;
					objs[2] = perStr1+"%";
				}
			}
			list.add(objs1);
			list.add(objs2);
			list.add(objs3);
		}
		return list;
	}
	
	public Object getUserSummarySize(Long startTime,Long endTime,Integer filter,Integer device,Integer chargeStart,Integer chargeEnd,Integer roleId,String userIuid,String roleName){
		StringBuffer sql = new StringBuffer("select count("+ (device == 0 ? "" : "distinct ") + "user_id) from ").
				append(BaseStormSystemType.GAME_DB_NAME).append(".u_po_role where total_diamond_charged>=").
				append(chargeStart).append(" and total_diamond_charged<=").append(chargeEnd);
		if(filter==1){
			sql.append(" and user_created_time>="+startTime+" and user_created_time<="+endTime); 
		}
		if(filter==2){
			sql.append(" and create_time>="+startTime+" and create_time<="+endTime);
		}
		if(filter==3){
			sql.append(" and last_login_time>="+startTime+" and last_logoff_time<="+endTime);
		}
		if(roleId!=0){
			sql.append(" and id="+roleId);
		}
		if(StringUtil.isNotEmpty(roleName)){
			sql.append(" and name='"+roleName+"'");
		}
		if(StringUtil.isNotEmpty(userIuid)){
			sql.append(" and user_iuid='"+userIuid+"'");
		}
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql.toString());
		PrintUtil.print("countSql:"+sql.toString());
		while (rs.next()) {
			return rs .getInt(1); 
		}
		throw new RuntimeException();
	}
	
	public Object getServerTime(){
		return System.currentTimeMillis();
	}
	
	public List<Object[]> findMoneyConsumeLog(Long startTime, Long endTime) {
		return serverService.findMoneyConsumeLog(startTime, endTime);
	}
	/**
	 * 获取系统参与率
	 * @param AccountDeviceSelector
	 * @param timerSelector
	 * @param channels
	 * @param minVip
	 * @param maxVip
	 * @return
	 */
	public List<SystemPartPo> getSystemParticipation(Integer AccountDeviceSelector, String timerSelector, String channels, Integer minVip, Integer maxVip) {
		List<SystemPartPo> result = new ArrayList<SystemPartPo>();
		String sql = "select type, use_num, open_num, use_rate from " + BaseStormSystemType.LOG_DB_NAME + ".system_part where account_device_type =" + AccountDeviceSelector + " and date='" + timerSelector + "' and " + "vip_level >= " + minVip + " and vip_level <= " + maxVip;
		if (channels != null && !channels.isEmpty()) {
			String[] channelStrings = channels.split(",");
			for (int i = 0; i < channelStrings.length; i++) {
				if (i == 0) {
					sql = sql.concat(" and (channel = '" + channelStrings[i] + "'");
				} else {
					sql = sql.concat(" or channel = '" + channelStrings[i] + "'");
				}
			}
			sql = sql.concat(")");
		}
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		while(rs.next()) {
			SystemPartPo systemPartPo = new SystemPartPo(rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getInt(1));
			result.add(systemPartPo);
		}
		return result;
	}
	/**
	 * 获取当日KIP数据
	 * @param tableName
	 * @return
	 */
	public Object[] getEveryDayKPIData()
	{
		Object[] result = new Object[3];
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long startTime = calendar.getTimeInMillis()-1000*60*60*24*3000;
		calendar.add(Calendar.DATE, 1);
		long endTime = calendar.getTimeInMillis();
		String sql = "select * from " + BaseStormSystemType.LOG_DB_NAME +"."+SummaryDayPo.getTableName()+ " where summary_time >=" + startTime + " and summary_time<" + endTime;
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		List<SummaryDayPo> summaryDayPos = new ArrayList<SummaryDayPo>();
		while(rs.next()) {
			summaryDayPos.add(new SummaryDayPo(rs));
		}
		result[0] = summaryDayPos;
		sql = "select * from " + BaseStormSystemType.LOG_DB_NAME +"."+SummaryHourPo.getTableName()+ " where summary_time >=" + startTime + " and summary_time<" + endTime;
		rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		List<SummaryHourPo> summaryHourPos = new ArrayList<SummaryHourPo>();
		while(rs.next()) {
			summaryHourPos.add(new SummaryHourPo(rs));
		}
		result[1] = summaryHourPos;
		sql = "select * from " + BaseStormSystemType.LOG_DB_NAME +"."+SummaryMonthPo.getTableName()+ " where summary_time >=" + startTime + " and summary_time<" + endTime;
		rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		List<SummaryMonthPo> summaryMonthPos = new ArrayList<SummaryMonthPo>();
		while(rs.next()) {
			summaryMonthPos.add(new SummaryMonthPo(rs));
		}
		result[2] = summaryMonthPos;
		return result;
	}
	
	/**
	 * 统计留存(2016-08-23废弃)
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @since 2016-06-24
	 */
	public Object remainSummary(Long startTime,Long endTime,Integer type) {
		String sql ="select id,type,summary_time,channel_key,equipment_new_role,day2,day3,day4,day5,day6,day7,day8,day14,day15,day30,day31,day45,day46,day60,day61,day90,day91 from "+
				BaseStormSystemType.LOG_DB_NAME+".summary_remain where type="+ type +" and summary_time>="+ startTime +" and summary_time<="+ endTime +" limit 1000";
		SqlRowSet rs1 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		Map<Long, RemainVo> resultMap = new TreeMap<Long, RemainVo>();
		while (rs1.next()) {
			RemainVo remainVo = new RemainVo(rs1);
			if (resultMap.containsKey(remainVo.summary_time)) {
				RemainVo remainVo1 = resultMap.get(remainVo.summary_time);
				resultMap.put(remainVo.summary_time, remainVo1.add(remainVo));
			} else {
				resultMap.put(remainVo.summary_time, remainVo);
			}
		}
		return resultMap;
	}
	/**
	 * 日KPI(GTM)
	 * @param startTime
	 * @param endTime
	 * @param channelKeys
	 * @param type
	 * @param servers
	 * @return
	 */
	public Object kpiDayWithServer(Long startTime,Long endTime,String channelKeys,Integer type, String servers){
		String sql="SELECT (floor(summary_time/1000)*1000) as summary_time,channel_key,SUM(new_user_role_new) AS new_user_role_new,SUM(new_user_role_exist) AS new_user_role_exist,SUM(dau) AS dau,SUM(day_user_old) AS day_user_old,SUM(day_user_new) AS day_user_new," +
				"SUM(pu) AS pu,SUM(pu_new) AS pu_new,SUM(pu_percentage) AS pu_percentage,SUM(darpu) AS darpu,SUM(darppu) AS darppu,SUM(charge_currency) AS charge_currency,SUM(consume_diamond) AS consume_diamond,SUM(avg_online) AS avg_online,SUM(max_online) AS max_online,SUM(remain_diamond) " +
				"FROM "+BaseStormSystemType.LOG_DB_NAME+".summary_day WHERE type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+
				" and channel_key in ("+channelKeys+") and server_id in("+servers+") GROUP BY channel_key,summary_time LIMIT 1000";
		PrintUtil.print("kpiDay sql1:"+sql);
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		HashMap<String, List<DayKpiVo>> dayKpiVoMap=new HashMap<String, List<DayKpiVo>>();
		while (rs.next()) {
			DayKpiVo dayKpiVo=new DayKpiVo();
			dayKpiVo.summary_time=rs.getLong(1);
			dayKpiVo.channel_key=rs.getString(2);
			dayKpiVo.new_user_role_new=rs.getInt(3);
			dayKpiVo.new_user_role_exist=rs.getInt(4);
			dayKpiVo.dau=rs.getInt(5);
			dayKpiVo.day_user_old=rs.getInt(6);
			dayKpiVo.day_user_new=rs.getInt(7);
			dayKpiVo.pu=rs.getInt(8);
			dayKpiVo.pu_new=rs.getInt(9);
			dayKpiVo.pu_percentage= (int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.pu * 10000.0 / dayKpiVo.dau);
			dayKpiVo.darpu=rs.getInt(11);
			dayKpiVo.darppu=rs.getInt(12);
			dayKpiVo.charge_currency=rs.getInt(13);
			dayKpiVo.consume_diamond=rs.getInt(14);
			dayKpiVo.avg_online=rs.getInt(15);
			dayKpiVo.max_online=rs.getInt(16);
			dayKpiVo.remainDiamond=rs.getInt(17);
			if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
				dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
			}
			dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
			//content+=dayKpiVo.toString();
		}
		
		if(channelKeys.contains("'0'")){
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,sum(new_user_role_new),sum(new_user_role_exist),sum(dau),sum(day_user_old),sum(day_user_new),sum(pu),sum(pu_new),sum(pu_percentage),sum(darpu),sum(darppu),sum(charge_currency),sum(consume_diamond),sum(avg_online),sum(max_online),sum(remain_diamond) from "+BaseStormSystemType.LOG_DB_NAME+".summary_day where type="+type+" and summary_time>="+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") and server_id in("+servers+") group by summary_time limit 1000";
			PrintUtil.print("kpiDay sql2:"+sql);
			rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
			while (rs.next()) {
				DayKpiVo dayKpiVo=new DayKpiVo();
				dayKpiVo.summary_time=rs.getLong(1);
				dayKpiVo.channel_key="all";
				dayKpiVo.new_user_role_new=rs.getInt(3);
				dayKpiVo.new_user_role_exist=rs.getInt(4);
				dayKpiVo.dau=rs.getInt(5);
				dayKpiVo.day_user_old=rs.getInt(6);
				dayKpiVo.day_user_new=rs.getInt(7);
				dayKpiVo.pu=rs.getInt(8);
				dayKpiVo.pu_new=rs.getInt(9);
				dayKpiVo.pu_percentage = (int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.pu * 10000.0 / dayKpiVo.dau);
				dayKpiVo.charge_currency=rs.getInt(13);
				dayKpiVo.consume_diamond=rs.getInt(14);
				dayKpiVo.avg_online=rs.getInt(15);
				dayKpiVo.max_online=rs.getInt(16);
				dayKpiVo.remainDiamond=rs.getInt(17);
				dayKpiVo.darpu=(int)(dayKpiVo.dau == 0 ? 0 : dayKpiVo.charge_currency * 100.0 / dayKpiVo.dau);
				dayKpiVo.darppu=(int)(dayKpiVo.pu == 0 ? 0 : dayKpiVo.charge_currency * 100.0/ dayKpiVo.pu);
				if(!dayKpiVoMap.containsKey(dayKpiVo.channel_key)){
					dayKpiVoMap.put(dayKpiVo.channel_key, new ArrayList<DayKpiVo>());
				}
				dayKpiVoMap.get(dayKpiVo.channel_key).add(dayKpiVo);
			}	
		}
		return dayKpiVoMap;
	}
	
	/**
	 * 小时KPI(GTM)
	 * @param startTime
	 * @param endTime
	 * @param channelKeys
	 * @param type（0：所有，1：设备，2：账号）
	 * @return 
	 */
	public List<HourKpiVo> kpiHourWithServer(Long startTime,Long endTime,String channelKeys, Integer type, String servers){
		String sql = "";
		if (type == 0)
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,hour_user_old,hour_user_new,pu,pu_new," +
					"charge_currency,remain,type,total from "+BaseStormSystemType.LOG_DB_NAME+".summary_hour where summary_time>="
					+startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") and server_id in("+servers+") limit 1000";
		else
			sql="select (floor(summary_time/1000)*1000) as summary_time,channel_key,hour_user_old,hour_user_new,pu,pu_new," +
					"charge_currency,remain,type,total from "+BaseStormSystemType.LOG_DB_NAME+".summary_hour where summary_time>="+
					startTime+" and summary_time<="+endTime+" and channel_key in ("+channelKeys+") and server_id in("+servers+")  and type = " + (type == 1 ? 2 : 6) + " limit 1000";
		PrintUtil.print("kpiHour sql:"+sql);
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		List<HourKpiVo> result = new ArrayList<HourKpiVo>();
		while (rs.next()) {
			HourKpiVo hourKpiVo=new HourKpiVo();
			hourKpiVo.summary_time=rs.getLong(1);
			hourKpiVo.channel_key=rs.getString(2);
			hourKpiVo.hour_user_old=rs.getInt(3);
			hourKpiVo.hour_user_new=rs.getInt(4);
			hourKpiVo.pu=rs.getInt(5);
			hourKpiVo.pu_new=rs.getInt(6);
			hourKpiVo.charge_currency=rs.getInt(7);
			hourKpiVo.remain = rs.getInt(8);
			hourKpiVo.type = rs.getInt(9);
			hourKpiVo.total = rs.getInt(10);
			result.add(hourKpiVo);
		}
		return result;
	}
	
	/**
	 * 统计留存(GMT)
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @since 2016-08-23
	 */
	public Object remainSummaryWithServer(Long startTime,Long endTime,Integer type, String servers) {
		String sql ="select id,type,summary_time,channel_key,equipment_new_role,day2,day3,day4,day5,day6,day7,day8,day14,day15,day30,day31,day45,day46,day60,day61,day90,day91 from "+
				BaseStormSystemType.LOG_DB_NAME+".summary_remain where type="+ type +" and summary_time>="+ startTime +" and summary_time<="+ endTime +" and server_id in ("+servers+") limit 1000";
		PrintUtil.print("remainSummaryWithServer sql:"+sql);
		SqlRowSet rs1 = BaseDAO.instance().jdbcTemplate.queryForRowSet(sql);
		Map<Long, RemainVo> resultMap = new TreeMap<Long, RemainVo>();
		while (rs1.next()) {
			RemainVo remainVo = new RemainVo(rs1);
			if (resultMap.containsKey(remainVo.summary_time)) {
				RemainVo remainVo1 = resultMap.get(remainVo.summary_time);
				resultMap.put(remainVo.summary_time, remainVo1.add(remainVo));
			} else {
				resultMap.put(remainVo.summary_time, remainVo);
			}
		}
		return resultMap;
	}
}
