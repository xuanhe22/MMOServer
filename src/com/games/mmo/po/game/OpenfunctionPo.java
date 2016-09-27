package com.games.mmo.po.game;


import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.storm.lib.component.entity.BaseGameDBPo;

	/**
	 *
	 * 类功能: 
	 *
	 * @author Johnny
	 * @version 
	 */
	@Entity
	@Table(name = "po_openfunction")
	public class OpenfunctionPo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
系统1 翅膀 
2 寄售
3 升星
4 洗练
5 宝石
6 伙伴
7 伙伴获得
8 伙伴升级
9 伙伴星盘
10 伙伴技能
11 伙伴装备
12 伙伴天赋
13 公会
14 装备强化
15 名人挑战赛
16 血色城堡
17 通天塔
18 幽冥之境
19 遗失圣境
20 合成
21 环任务
22 活动
23 世界boss
24 组队副本
25 守卫水晶
26 日常活动
27 血魔城堡
28 恶灵禁地
29 魔化危机
30 PK之王
31 日常
32 每日活跃
33 福利副本
34 经验本
35 金币本
36 材料本
37 伙伴本
38 称号
39 活动任务
40 时装
41 附魂
42 技能
43 资源找回
101 解锁技能1
102 解锁技能2
103 解锁技能3
104 解锁技能4
105 解锁技能5
	**/

	private Integer openFunction;
	/**
	*开启提示未开启弹的提示
	**/

	private String prompt;

	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="open_function")
	 public Integer getOpenFunction() {
		return this.openFunction;
	}
	public void setOpenFunction(Integer openFunction) {
		this.openFunction = openFunction;
	}

	@Column(name="prompt")
	 public String getPrompt() {
		return this.prompt;
	}
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	// openfunctionPo.setOpenFunction(openFunction)
	// openfunctionPo.setPrompt(prompt)

	/**
	 *系统生成代码和自定义代码的分隔符
	 */
}
