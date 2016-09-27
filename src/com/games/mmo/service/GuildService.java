package com.games.mmo.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.po.GuildPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.MailType;
import com.games.mmo.type.UsuallyType;
import com.games.mmo.util.LogUtil;
import com.games.mmo.vo.GuildInvitionVo;
import com.games.mmo.vo.GuildMemberVo;
import com.games.mmo.vo.xml.ConstantFile.Guild.Guildboss.Boss;
import com.games.mmo.vo.xml.ConstantFile.Guild.Maintiance.Cost;
import com.storm.lib.base.BaseService;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.vo.IdNumberVo;
import com.storm.lib.vo.IdNumberVo2;

@Controller
public class GuildService extends BaseService {
	@Autowired
	RoleDAO roleDAO;
	
	/**
	 * 检查公会资金每星期扣除
	 */
	public void checkGuildGoldWeeklyDeduct(){
 		List<Cost> costs = XmlCache.xmlFiles.constantFile.guild.maintiance.cost;
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild order by battle_power desc");
		Iterator it = rows.iterator();
		MailService mailService = (MailService) BeanUtil.getBean("mailService");
		while(it.hasNext()) 
		{
			Map userMap = (Map) it.next();  
			Integer guildId  =Integer.valueOf(userMap.get("id").toString());
			if(guildId != null && guildId !=0)
			{
				GuildPo guildPo = GuildPo.findEntity(guildId);
				if(guildPo != null){
					guildPo.cleanGuildMemberVoHonor();
				}
				if((System.currentTimeMillis() - guildPo.getCreatedTime().longValue()) >= (UsuallyType.USUALLY_TYPE_ONE_DAY_TIME*7))
				{
					if(guildPo != null)
					{
						Cost cost = costs.get(guildPo.getLv() - 1);
						if(guildPo.getGold().intValue() < cost.gold.intValue() && guildPo.getGold().intValue()<0)
						{
							PrintUtil.print("guildPo.getName() = " +guildPo.getName());
							for(GuildMemberVo guildMemberVo : guildPo.listMembers){
								RolePo rolePo = RolePo.findEntity(guildMemberVo.roleId);
								if(rolePo != null){
									mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key2697"), rolePo.getId(), null, GlobalCache.fetchLanguageMap("key2698"), null, MailType.MAIL_TYPE_SYSTEM);
								}
							}
							guildPo.dimiss();
							LogUtil.writeLog(null, 2, 0, 0, 0, guildPo.getName()+GlobalCache.fetchLanguageMap("key2494"), "");
						}
						else
						{
							PrintUtil.print(guildPo.getName() +" 11== "+ guildPo.getGold());
							guildPo.consumeGuildGold(cost.gold);	
							PrintUtil.print(guildPo.getName() +" 22== "+ guildPo.getGold());
						}
					}			
				}	
			}
		} 
	}
	
	/**
	 * 重置公会boss信息
	 */
	public void checkResetGuildBossInfo(){
		List rows = BaseDAO.instance().jdbcTemplate.queryForList("select id from "+BaseStormSystemType.USER_DB_NAME+".u_po_guild order by battle_power desc");
		List<Boss> listBoss = XmlCache.xmlFiles.constantFile.guild.guildboss.boss;
		Iterator it = rows.iterator();
		while(it.hasNext()) 
		{
			Map userMap = (Map) it.next();  
			Integer guildId  =Integer.valueOf(userMap.get("id").toString());
			if(guildId != null && guildId !=0)
			{
				GuildPo guildPo = GuildPo.findEntity(guildId);
				guildPo.listBossInfo.clear();
				for(Boss boss : listBoss){
					if(boss.lv == 1){
						guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 1, 0));				
					}else{
						guildPo.listBossInfo.add(new IdNumberVo2(boss.copysceneconfid, 0, 0));	
					}
				}
			}
		}
		
		List<Integer> list =  roleDAO.fetchAllRoleIds(0,0,100,100);
		for(Integer roleId : list){
			RolePo rolePo = RolePo.findEntity(roleId);
			rolePo.listGuildBossAward = new CopyOnWriteArrayList<IdNumberVo>();
			for(int i=0; i < listBoss.size(); i++){
				rolePo.listGuildBossAward.add(new IdNumberVo(listBoss.get(i).copysceneconfid, 0));
			}
			if(rolePo.fetchRoleOnlineStatus()){
				rolePo.sendUpdateListGuildBossAward();				
			}
		}
	}
	
}
