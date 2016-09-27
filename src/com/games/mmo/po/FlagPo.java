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
import com.storm.lib.component.entity.BasePo;
import com.storm.lib.component.entity.BaseUserDBPo;
import com.storm.lib.util.StringUtil;

@Entity
@Table(name = "u_po_flag")
public class FlagPo extends BaseUserDBPo{

	/**
	*主键
	**/
	private Integer id;

	private Integer sceneId;
	
	private Integer guildId;
	
	private Integer flagStatus;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="scene_id")
	public Integer getSceneId() {
		return sceneId;
	}
	public void setSceneId(Integer sceneId) {
		changed("scene_id",sceneId,this.sceneId);
		this.sceneId = sceneId;
	}
	
	@Column(name="guild_id")
	public Integer getGuildId() {
		return guildId;
	}
	public void setGuildId(Integer guildId) {
		changed("guild_id",guildId,this.guildId);
		this.guildId = guildId;
	}
	
	@Column(name="flag_status")
	public Integer getFlagStatus() {
		return flagStatus;
	}
	public void setFlagStatus(Integer flagStatus) {
		changed("flag_status",flagStatus,this.flagStatus);
		this.flagStatus = flagStatus;
	}
	
	public void loadData(BasePo basePo) {
		if(loaded==false){
			unChanged();

			loaded =true;
		}
	}
	
	
	public static FlagPo findFlagBySceneId(Integer sceneId) {
		return GlobalCache.sceneIdFlagMap.get(sceneId);
	}


	

}
