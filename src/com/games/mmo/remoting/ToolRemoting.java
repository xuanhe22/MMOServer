package com.games.mmo.remoting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.games.mmo.po.game.MonsterFreshPo;
import com.games.mmo.vo.xml.MonsterFreshXml;
import com.storm.lib.base.BaseRemoting;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.DoubleUtil;
import com.storm.lib.util.XMLUtil;
@Controller
public class ToolRemoting extends BaseRemoting{
	@Autowired
	private BaseDAO baseDAO;
	/**
	 * 
	 * 方法功能:生成怪物信息
	 * 更新时间:2014-12-2, 作者:johnny
	 * @param xmlStriing
	 * @return
	 */
	public Object generateSceneMonsterInfor(String xmlStriing,Integer sceneId){
		MonsterFreshXml monsterFreshXml=XMLUtil.createObject(xmlStriing, MonsterFreshXml.class);
		String cleanSql ="delete from "+BaseStormSystemType.GAME_DB_NAME+".po_monster_fresh where scene_id ="+sceneId;
		baseDAO.execute(cleanSql);
		
		for(int i=0;i<monsterFreshXml.monsters.monster.size();i++){
			MonsterFreshPo monsterFreshPo = new MonsterFreshPo();
			monsterFreshPo.setFreshSeconds(monsterFreshXml.monsters.monster.get(i).interval);
			monsterFreshPo.setGroupId(monsterFreshXml.monsters.monster.get(i).groupId);
			monsterFreshPo.setMonsterId(monsterFreshXml.monsters.monster.get(i).id);
			monsterFreshPo.setRotation(monsterFreshXml.monsters.monster.get(i).rotate);
			monsterFreshPo.setSceneId(sceneId);
			monsterFreshPo.setSceneUniqId(monsterFreshXml.monsters.monster.get(i).onlyId);
			monsterFreshPo.setX(DoubleUtil.toInt(monsterFreshXml.monsters.monster.get(i).x*10000));
			monsterFreshPo.setY(DoubleUtil.toInt(monsterFreshXml.monsters.monster.get(i).y*10000));
			monsterFreshPo.setZ(DoubleUtil.toInt(monsterFreshXml.monsters.monster.get(i).z*10000));
			monsterFreshPo.setTag(monsterFreshXml.monsters.monster.get(i).tag);
			baseDAO.hibernateTemplate.save(monsterFreshPo);
		}
		return 1;
	}
}
