package com.games.mmo.util;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.games.mmo.po.RolePo;
import com.games.mmo.po.UserPo;
import com.games.mmo.vo.LogVo;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.DateUtil;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.StringUtil;

public class LogUtil {
	 
		public static long todayTime=0l;
		public static String todayTimeStr="";
		public static String todayTableName="";
		public static ConcurrentLinkedQueue<LogVo> log= new ConcurrentLinkedQueue<LogVo>();
		public static long logSize=0;
		public static long index=0;
	/**
	 * 
	 * @param rolePo
	 * @param type 日志类型（1=资源获取 2=行为）
	 * @param par1  获取类型（资源获取 1=绑金 2=金币 3=绑钻 4=钻石 5=道具 6=技能点 7=公会荣誉 8=声望 9=成就点 10=宠物魔魂 11=经验 12=宠物 13=pk点 98=先绑金再金币 99=先绑钻再钻石）
	 * @param par2 获取id
	 * @param par3 获取数量
	 * @param source 来源
	 * @param remark 其他备注
	 */
	public static void writeLog(RolePo rolePo,int type,int par1,int par2,int par3,String source,String remark){
		index++;
		if(index%1024==0){
			logSize=log.size();
		}
		try {
			if(logSize>=100000){
				try {
					PrintUtil.print(type+" "+par1+" "+par2+" "+par3+" "+source+" "+remark);
					ExceptionUtil.throwConfirmParamException("error");
				} catch (Exception e) {
					ExceptionUtil.processException(e);
				}
				return;
			}
			if(rolePo!=null){
				UserPo userPo = UserPo.findEntity(rolePo.getUserId());
				if(par1==98){
					if(rolePo.getBindGold()+par3>=0){
						writeLog(rolePo, type, 1, par2, par3, source, remark);
					}else if(rolePo.getBindGold()+rolePo.getGold()+par3>=0){
						if(rolePo.getBindGold()>0){
							writeLog(rolePo, type, 1, par2, -rolePo.getBindGold(), source, remark);
						}
						writeLog(rolePo, type, 2, par2, (par3+rolePo.getBindGold()), source, remark);
					}
					return;
				}else if(par1==99){
					if(rolePo.getBindDiamond()+par3>=0){
						writeLog(rolePo, type, 3, par2, par3, source, remark);
					}else if(rolePo.getBindDiamond()+userPo.getDiamond()+par3>=0){
						if(rolePo.getBindDiamond()>0){
							writeLog(rolePo, type, 3, par2, -rolePo.getBindDiamond(), source, remark);
						}
						writeLog(rolePo, type, 4, par2, (par3+rolePo.getBindDiamond()), source, remark);
					}
					return;
				}
			}
			Long currentTime =checkTodayTime();
			LogVo logVo = new LogVo();
			if(rolePo!=null){
				logVo.roleId=rolePo.getId();
				logVo.roleName=rolePo.getName();
			}else{
				logVo.roleId=0;
				logVo.roleName="";
			}
			logVo.logPar1=par1;
			logVo.logPar2=par2;
			logVo.logPar3=par3;
			logVo.logTime=System.currentTimeMillis();
			logVo.logType=type;
			logVo.remartTxt=remark;
			logVo.sourceTxt=source;
			logVo.sourceType=0;
			if(rolePo!=null){
				UserPo userPo= UserPo.findEntity(rolePo.getUserId());
				logVo.userId=userPo.getId();
				logVo.userIuid=userPo.getIuid();
				logVo.channel=rolePo.getChannelKey();
			}
			if(type==301||type==303){
				String[] str =StringUtil.split(logVo.sourceTxt, "|");
				logVo.userId=Integer.valueOf(str[0]);
				logVo.channel=str[2];
			}if(type==309||type==310){
				logVo.channel=remark;
			}if(type>=311 && type<=356 && rolePo!=null){
				logVo.remartTxt=rolePo.getDeviceId();
			}
			log.offer(logVo);
			if(rolePo!=null){
				if(type==1 && par1==4 && (rolePo.getFirstConsumeRoleLv()==null  ||rolePo.getFirstConsumeRoleLv()==0)){
					rolePo.setFirstConsumeRoleLv(rolePo.getLv());
					rolePo.setFirstConsumeDesc(source);
				}
			}
			//发送给前端talkingData
			if(logVo.logType==1 && (logVo.logPar1==3 || logVo.logPar1==4) && logVo.logPar3<0){
				rolePo.sendUpdateLogVo(logVo);
			}
		} catch (Exception e) {
			ExceptionUtil.processException(e);
		}

		
	}

	
	public static Long checkTodayTime(){
		Long currentTime = System.currentTimeMillis();
		if(todayTime==0l || currentTime-todayTime>((24l*3600)*1000)){
			Calendar c = Calendar.getInstance();
			c.setTime(new Date(currentTime));
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			todayTime=c.getTime().getTime();
			todayTimeStr=DateUtil.formatNow("yyyy_MM_dd");
			todayTableName="role_log_"+todayTimeStr;
			PrintUtil.print("---------------checkTodayTime修改今天时间:"+c.getTime().toLocaleString()+" "+todayTimeStr);
			
			String sql="CREATE DATABASE IF NOT EXISTS "+BaseStormSystemType.LOG_DB_NAME+" default character set utf8 COLLATE utf8_general_ci;";
			BaseDAO.instance().execute(sql);
						
			sql="CREATE TABLE if not exists "+BaseStormSystemType.LOG_DB_NAME+".summary_day (`id` int(10) unsigned NOT NULL AUTO_INCREMENT,`type` int(11) DEFAULT NULL COMMENT '描述无',`channel_key` varchar(255) DEFAULT NULL,`summary_time` bigint(22) DEFAULT NULL,`new_user_role_new` int(11) DEFAULT NULL,`new_user_role_exist` int(11) DEFAULT NULL,`dau` int(11) DEFAULT NULL,`day_user_old` int(11) DEFAULT NULL,`day_user_new` int(11) DEFAULT NULL,`pu` int(11) DEFAULT NULL,`pu_new` int(11) DEFAULT NULL,`pu_percentage` int(11) DEFAULT NULL,`darpu` int(11) DEFAULT NULL,`darppu` int(11) DEFAULT NULL,`charge_currency` int(11) DEFAULT NULL,`consume_diamond` int(11) DEFAULT NULL,`avg_online` int(11) DEFAULT NULL,`max_online` int(11) DEFAULT NULL,`remain_diamond` int(11) DEFAULT NULL,`server_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '服务器Id',PRIMARY KEY (`id`),KEY `role_time` (`type`,`day_user_new`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
			BaseDAO.instance().execute(sql);
			sql="CREATE TABLE if not exists "+BaseStormSystemType.LOG_DB_NAME+".summary_hour (`id` int(10) unsigned NOT NULL AUTO_INCREMENT,`type` int(11) DEFAULT NULL, `channel_key` varchar(255) DEFAULT NULL,  `summary_time` bigint(22) DEFAULT NULL, `hour_user_old` int(11) DEFAULT NULL ,  `hour_user_new` int(11) DEFAULT NULL, `pu` int(11) DEFAULT NULL,  `pu_new` int(11) DEFAULT NULL, `charge_currency` int(11) DEFAULT NULL,`server_id`  int(11) UNSIGNED NULL DEFAULT NULL COMMENT '服务器Id', PRIMARY KEY (`id`),  KEY `role_time` (`type`,`hour_user_new`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
			BaseDAO.instance().execute(sql);			
			sql="CREATE TABLE if not exists "+BaseStormSystemType.LOG_DB_NAME+".summary_month (`id` int(10) unsigned NOT NULL AUTO_INCREMENT,`type` int(11) DEFAULT NULL COMMENT '描述无',`channel_key` varchar(255) DEFAULT NULL,`summary_time` bigint(22) DEFAULT NULL,`new_user_role_new` int(11) DEFAULT NULL,`new_user_role_exist` int(11) DEFAULT NULL,`dau` int(11) DEFAULT NULL,`day_user_old` int(11) DEFAULT NULL,`day_user_new` int(11) DEFAULT NULL,`pu` int(11) DEFAULT NULL,`pu_new` int(11) DEFAULT NULL,`pu_percentage` int(11) DEFAULT NULL,`darpu` int(11) DEFAULT NULL,`darppu` int(11) DEFAULT NULL,`charge_currency` int(11) DEFAULT NULL,`consume_diamond` int(11) DEFAULT NULL,`server_id` int(11) UNSIGNED NULL DEFAULT NULL COMMENT '服务器Id',PRIMARY KEY (`id`),KEY `role_time` (`type`,`day_user_new`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
			BaseDAO.instance().execute(sql);
			sql="CREATE TABLE if not exists "+BaseStormSystemType.LOG_DB_NAME+".summary_remain ( `id` int(11)  unsigned NOT NULL AUTO_INCREMENT," +
					"`type` int(11) DEFAULT NULL COMMENT '描述无', " +
					"`channel_key` varchar(255) DEFAULT NULL," +
					"`summary_time` bigint(22) DEFAULT NULL," +
					"`equipment_new_role` int(11) DEFAULT NULL," +
					"`day2` int(11) DEFAULT NULL, " +
					"`day3` int(11) DEFAULT NULL," +
					"`day4` int(11) DEFAULT NULL," +
					"`day5` int(11) DEFAULT NULL," +
					"`day6` int(11) DEFAULT NULL," +
					"`day7` int(11) DEFAULT NULL," +
					"`day8` int(11) DEFAULT NULL," +
					"`day14` int(11) DEFAULT NULL," +
					"`day15` int(11) DEFAULT NULL," +
					"`day30` int(11) DEFAULT NULL," +
					"`day31` int(11) DEFAULT NULL," +
					"`day45` int(11) DEFAULT NULL," +
					"`day46` int(11) DEFAULT NULL," +
					"`day60` int(11) DEFAULT NULL," +
					"`day61` int(11) DEFAULT NULL," +
					"`day90` int(11) DEFAULT NULL, " +
					"`day91` int(11) DEFAULT NULL, " +
					"`server_id`  int(11) UNSIGNED NULL DEFAULT NULL COMMENT '服务器Id' ," +
					"PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
			BaseDAO.instance().execute(sql);
			sql="CREATE TABLE IF NOT EXISTS "+BaseStormSystemType.LOG_DB_NAME+"."+todayTableName+" (`id` int(10) unsigned NOT NULL AUTO_INCREMENT,`role_id` int(11) DEFAULT NULL,`role_name` varchar(255) DEFAULT NULL, `user_id` int(11) DEFAULT NULL, `user_iuid` varchar(255) DEFAULT NULL,  `log_type` int(11) DEFAULT NULL,  `log_time` bigint(22) DEFAULT NULL,  `log_par1` int(11) DEFAULT NULL,  `log_par2` int(11) DEFAULT NULL,  `log_par3` int(11) DEFAULT NULL,  `source_type` int(11) DEFAULT NULL,  `source_txt` varchar(255) DEFAULT NULL,  `remark_txt` varchar(255) DEFAULT NULL, `channel` varchar(255) DEFAULT NULL,  PRIMARY KEY (`id`),INDEX `user_id` (`user_id`) ,INDEX `log_time` (`log_time`),INDEX `log_type` (`log_type`), INDEX `log_all` (`role_id`, `log_type`, `log_par1`, `log_par3`, `log_time`))ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
//			PrintUtil.print(sql);
			BaseDAO.instance().execute(sql);
		}
		return currentTime;
	}
	
	public static void main(String[] args) {
		while(true){
			Long currentTime = checkTodayTime();

			PrintUtil.print("当前时间:"+new Date(currentTime).toLocaleString());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
