
package com.games.mmo.po.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	@Table(name = "po_scene")
	public class ScenePo extends BaseGameDBPo {
	/**
	*主键
	**/

	private Integer id;
	/**
	*无备注
	**/

	private String name;
	
	private String sceneMap;
	
	private Integer x;

	private Integer y;
	
	private Integer z;
	
	/**
	 * 可以PK
	 */
	private Integer pkAble;
	
	/**
	 * 可以掉落
	 */
	private Integer dropAble;
	
	private String weather;
	
	private Integer miniMap;
	
	private Integer roleToward;
	
	/**
	 * 可视范围
	 */
	private Integer range;
	/**
	 * 值 0 无法复活 1 允许复活
	 */
	private Integer revive;
	/**
	 * 值 0 普通地图 1 副本地图
	 */
	private Integer sceneAttribute;
    /**
	 * 地图总像素宽
	 */
    private Integer mapPixelWidth; 
	/**
	 * 地图总像素高
	 */
	private Integer mapPixelHeight;
	/**
	 * 单个Cell像素宽
	 */
	private Integer cellPixelX; 
	/**
	 * 单个Cell像素高
	 */
	private Integer cellPixelY;
	
	private String mapLv;
	
	/**
	 * 剧情编号
	 */
	private Integer storyId;
	
	private String mapIcon;
	
	private Integer redName;
	
	private String safeArea;
	
	@Id @GeneratedValue

	@Column(name="id", unique=true, nullable=false)
	 public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="name")
	 public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	@Column(name="scene_map")
	public String getSceneMap() {
		return sceneMap;
	}
	public void setSceneMap(String sceneMap) {
		this.sceneMap = sceneMap;
	}
	
	
	
	@Column(name="x")
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	
	@Column(name="y")
	public Integer getY() {
		return y;
	}
	

	public void setY(Integer y) {
		this.y = y;
	}
	@Column(name="z")
	public Integer getZ() {
		return z;
	}
	

	public void setZ(Integer z) {
		this.z = z;
	}
	
	
	@Column(name="pk_able")
	public Integer getPkAble() {
		return pkAble;
	}
	public void setPkAble(Integer pkAble) {
		this.pkAble = pkAble;
	}
	
	@Column(name="drop_able")
	public Integer getDropAble() {
		return dropAble;
	}
	public void setDropAble(Integer dropAble) {
		this.dropAble = dropAble;
	}
	
	
	@Column(name="weather")
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	
	@Column(name="mini_map")
	public Integer getMiniMap() {
		return miniMap;
	}
	public void setMiniMap(Integer miniMap) {
		this.miniMap = miniMap;
	}
	
	@Column(name="role_toward")
	public Integer getRoleToward() {
		return roleToward;
	}
	public void setRoleToward(Integer roleToward) {
		this.roleToward = roleToward;
	}
	
	
	
	@Column(name="range")
	public Integer getRange() {
		return range;
	}
	public void setRange(Integer range) {
		this.range = range;
	}
	
	@Column(name="revive")
	public Integer getRevive() {
		return revive;
	}
	public void setRevive(Integer revive) {
		this.revive = revive;
	}
	@Column(name="scene_attribute")
	public Integer getSceneAttribute() {
		return sceneAttribute;
	}
	public void setSceneAttribute(Integer sceneAttribute) {
		this.sceneAttribute = sceneAttribute;
	}
	@Column(name="map_pixel_width")
	public Integer getMapPixelWidth() {
		return mapPixelWidth;
	}
	public void setMapPixelWidth(Integer mapPixelWidth) {
		this.mapPixelWidth = mapPixelWidth;
	}
	@Column(name="map_pixel_height")
	public Integer getMapPixelHeight() {
		return mapPixelHeight;
	}
	public void setMapPixelHeight(Integer mapPixelHeight) {
		this.mapPixelHeight = mapPixelHeight;
	}
	@Column(name="cell_pixel_x")
	public Integer getCellPixelX() {
		return cellPixelX;
	}
	public void setCellPixelX(Integer cellPixelX) {
		this.cellPixelX = cellPixelX;
	}
	@Column(name="cell_pixel_y")
	public Integer getCellPixelY() {
		return cellPixelY;
	}
	public void setCellPixelY(Integer cellPixelY) {
		this.cellPixelY = cellPixelY;
	}
	
	@Column(name="map_lv")
	public String getMapLv() {
		return mapLv;
	}
	public void setMapLv(String mapLv) {
		this.mapLv = mapLv;
	}
	
	@Column(name="story_id")
	public Integer getStoryId() {
		return storyId;
	}
	public void setStoryId(Integer storyId) {
		this.storyId = storyId;
	}
	
	
	@Column(name="map_icon")
	public String getMapIcon() {
		return mapIcon;
	}
	public void setMapIcon(String mapIcon) {
		this.mapIcon = mapIcon;
	}
	
	
	@Column(name="red_name")
	public Integer getRedName() {
		return redName;
	}
	public void setRedName(Integer redName) {
		this.redName = redName;
	}
	
	@Column(name="safe_area")
	public String getSafeArea() {
		return safeArea;
	}
	public void setSafeArea(String safeArea) {
		this.safeArea = safeArea;
	}
	
	
	public static ScenePo findEntity(Integer id){
		return findRealEntity(ScenePo.class,id);
	}

}
