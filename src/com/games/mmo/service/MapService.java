package com.games.mmo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.cache.XmlCache;
import com.games.mmo.po.RankArenaPo;
import com.games.mmo.po.RolePo;
import com.games.mmo.type.ArenaType;
import com.games.mmo.type.MailType;
import com.games.mmo.vo.xml.ConstantFile.RankAwards.RankAward;
import com.games.mmo.vo.xml.XmlFiles;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.DateUtil;

@Controller
public class MapService {
	@Autowired
	RoleService roleService;
	@Autowired
	MailService mailService;
	/**
	 * 
	 * 方法功能:发送竞技场奖励
	 * 更新时间:2014-6-27, 作者:johnny
	 */
	public void sendArenaAward() {
		List<RankAward> rankAward = XmlCache.xmlFiles.constantFile.rankAwards.rankAward;
		int goldItemId = XmlCache.xmlFiles.constantFile.rankAwards.goldItemId;
		int skillPointItemId = XmlCache.xmlFiles.constantFile.rankAwards.skillPointItemId;
		String time = DateUtil.getFormatDateBytimestamp(System.currentTimeMillis());
		for(RankArenaPo rp : GlobalCache.rankArenaMaps.values()){
			// 数据异常的时候做的保护
			if(rp.getWasRobot() == null){
				RolePo rolePo = RolePo.findEntity(rp.getRoleId());
				if(rolePo != null){
					rp.setWasFirstArena(0);
				}else{
					rp.setWasFirstArena(1);
				}
			}
			int arenaRank = rp.getArenaRank();
			for(RankAward ra : rankAward){
				if(rp.getWasRobot().intValue() == 0 && ra.minRank<= arenaRank && arenaRank <= ra.maxRank){
					int gold = (ra.maxRank -arenaRank)*ra.goldAdd + ra.gold;
					int skillPoint =(ra.maxRank - arenaRank)*ra.skillPointAdd + ra.skillPoint;
					StringBuilder sb = new StringBuilder();
					 sb.append(1);
					 sb.append("|");
					 sb.append(goldItemId);
					 sb.append("|");
					 sb.append(gold);
					 sb.append("|");
					 sb.append(1);
					 sb.append(",");
					 sb.append(1);
					 sb.append("|");
					 sb.append(skillPointItemId);
					 sb.append("|");
					 sb.append(skillPoint);
					 sb.append("|");
					 sb.append(1);
					 mailService.sendSystemMail(GlobalCache.fetchLanguageMap("key239"), rp.getRoleId(), null, GlobalCache.fetchLanguageMap("key240")+GlobalCache.fetchLanguageMap("key241")+arenaRank+"，"+GlobalCache.fetchLanguageMap("key242")+skillPoint+GlobalCache.fetchLanguageMap("key243"), sb.toString(), MailType.MAIL_TYPE_SYSTEM);
				}
			}
		}
			
	}
}
