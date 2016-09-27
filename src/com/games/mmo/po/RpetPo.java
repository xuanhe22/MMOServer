package com.games.mmo.po;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.games.mmo.cache.GlobalCache;
import com.games.mmo.dao.RoleDAO;
import com.games.mmo.mapserver.bean.Fighter;
import com.games.mmo.po.game.ItemPo;
import com.games.mmo.po.game.MonsterPo;
import com.games.mmo.po.game.PetPo;
import com.games.mmo.po.game.PetSkillPo;
import com.games.mmo.vo.xml.ConstantFile.Pet.PetSkill;
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.BeanUtil;
import com.storm.lib.util.StringUtil;

/**
 *
 * 类功能: 宠物
 *
 * @author Johnny
 * @version
 */
@Entity
@Table(name = "u_po_rpet")
public class RpetPo extends BaseUserDBPo {

	/**
	 * 主键
	 **/

	private Integer id;
	/**
	 * 宠物ID
	 */
	private Integer petId;
	/**
	 * 等级
	 */
	private Integer lv;
	/**
	 * 名字
	 */
	private String name;
	/**
	 * 经验
	 */
	private Integer exp;
	/**
	 * 转世阶数
	 */
	private Integer step;

	/**
	 * 宠物当前使用的技能栏位
	 */
	private String skillPlaces;
	// 以上为前端需要关心的字段

	/**
	 * 当前宠物技能列表
	 */
	public List<Integer> skillIds = new ArrayList<Integer>();

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "pet_id")
	public Integer getPetId() {
		return petId;
	}

	public void setPetId(Integer petId) {
		changed("pet_id", petId, this.petId);
		this.petId = petId;
	}

	@Column(name = "lv")
	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		changed("lv", lv, this.lv);
		this.lv = lv;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		changed("name", name, this.name);
		this.name = name;
	}

	@Column(name = "exp")
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		changed("exp", exp, this.exp);
		this.exp = exp;
	}

	@Column(name = "step")
	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		changed("step", step, this.step);
		this.step = step;
	}

	@Column(name = "skill_places")
	public String getSkillPlaces() {
		return skillPlaces;
	}

	public void setSkillPlaces(String skillPlaces) {
		changed("skill_places", skillPlaces, this.skillPlaces);
		this.skillPlaces = skillPlaces;
	}


	public void loadData(BasePo basePo) {
		if (loaded == false) {
			unChanged();
			if (!StringUtil.isEmpty(skillPlaces)) {
				String[] strs = StringUtil.split(skillPlaces, "|");
				for (String str : strs) {
					skillIds.add(Integer.parseInt(str));
				}
			}
			loaded = true;
		}
	}

	public static RpetPo findEntity(Integer id) {
		RpetPo rpetPo = findRealEntity(RpetPo.class, id);
		if (rpetPo == null) {
			if (id == null) {
				return null;
			}
			if (GlobalCache.robotPet.containsKey(id)) {
				return GlobalCache.robotPet.get(id);
			} else {
				return null;
			}
		}
		return rpetPo;
	}

	public PetPo petPo() {
		return PetPo.findEntity(petId);
	}

	/**
	 * 调整宠物等级
	 * 
	 * @param num
	 */
	public void adjustPetLv(Integer num) {
		if (num == null) {
			return;
		}
		setLv(lv + num);
	}

	public void adjustPetStep(Integer num){
		if(num == null){
			return;
		}
		setStep(step + num);
	}
	
	/**
	 * 更新宠物技能栏位
	 */
	public void updateSkillPlace() {
		 StringBuilder sb = new StringBuilder();
		 for(int i=0,max=skillIds.size(); i<max; i++) {
			 if(i!=0)
				 sb.append("|");
			 sb.append(skillIds.get(i));
		 }
		 this.setSkillPlaces(sb.toString());
	}

	@Override
	public String toString() {
		return "RpetPo [id=" + id + ", petId=" + petId + ", lv=" + lv
				+ ", name=" + name + ", exp=" + exp + ", step=" + step
				+ ", skillPlaces=" + skillPlaces + ", skillIds=" + skillIds
				+ "]";
	}

	
	
}
