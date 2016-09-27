package com.games.mmo.vo;

import java.io.Serializable;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class RemainVo implements Serializable, Comparable<RemainVo>{
	public int id;
	public int type;
	public long summary_time;
	public String channel_Key;
	public int equipment_new_role;
	public int day2;
	public int day3;
	public int day4;
	public int day5;
	public int day6;
	public int day7;
	public int day8;
	public int day14;
	public int day15;
	public int day30;
	public int day31;
	public int day45;
	public int day46;
	public int day60;
	public int day61;
	public int day90;
	public int day91;
	public RemainVo (SqlRowSet rowSet) {
		id = rowSet.getInt(1);
		type = rowSet.getInt(2);
		summary_time = rowSet.getLong(3);
		channel_Key = rowSet.getString(4);
		equipment_new_role = rowSet.getInt(5);
		day2 = rowSet.getInt(6);
		day3 = rowSet.getInt(7);
		day4 = rowSet.getInt(8);
		day5 = rowSet.getInt(9);
		day6 = rowSet.getInt(10);
		day7 = rowSet.getInt(11);
		day8 = rowSet.getInt(12);
		day14 = rowSet.getInt(13);
		day15 = rowSet.getInt(14);
		day30 = rowSet.getInt(15);
		day31 = rowSet.getInt(16);
		day45 = rowSet.getInt(17);
		day46 = rowSet.getInt(18);
		day60 = rowSet.getInt(19);
		day61 = rowSet.getInt(20);
		day90 = rowSet.getInt(21);
		day91 = rowSet.getInt(22);
	}

	public RemainVo() {
	}

	public RemainVo add(RemainVo remainVo) {
		int t = this.equipment_new_role + remainVo.equipment_new_role;
		if (t != 0) {
			this.day2 = (this.day2 * this.equipment_new_role + remainVo.day2 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day3 = (this.day3 * this.equipment_new_role + remainVo.day3 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day4 = (this.day4 * this.equipment_new_role + remainVo.day4 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day5 = (this.day5 * this.equipment_new_role + remainVo.day5 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day6 = (this.day6 * this.equipment_new_role + remainVo.day6 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day7 = (this.day7 * this.equipment_new_role + remainVo.day7 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day8 = (this.day8 * this.equipment_new_role + remainVo.day8 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day14 = (this.day14 * this.equipment_new_role + remainVo.day14 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day15 = (this.day15 * this.equipment_new_role + remainVo.day15 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day30 = (this.day30 * this.equipment_new_role + remainVo.day30 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day31 = (this.day31 * this.equipment_new_role + remainVo.day31 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day45 = (this.day45 * this.equipment_new_role + remainVo.day45 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day46 = (this.day46 * this.equipment_new_role + remainVo.day46 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day60 = (this.day60 * this.equipment_new_role + remainVo.day60 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day61 = (this.day61 * this.equipment_new_role + remainVo.day61 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day90 = (this.day90 * this.equipment_new_role + remainVo.day90 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
			this.day91 = (this.day91 * this.equipment_new_role + remainVo.day91 * remainVo.equipment_new_role) / (this.equipment_new_role + remainVo.equipment_new_role); 
		}
		this.equipment_new_role += remainVo.equipment_new_role;
		return this;
	}

	@Override
	public int compareTo(RemainVo remainVo) {
		return (this.summary_time - remainVo.summary_time)>0?1:(this.summary_time - remainVo.summary_time)==0?0:-1;
	}
}
