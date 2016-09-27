package com.games.mmo.vo;

import java.util.concurrent.ConcurrentHashMap;

import com.storm.lib.vo.IdNumberVo;


public class AgreementCountVo {
	public int mapRoomId=0;
	public int sceneId=0;
	public int totalCount=0;
	public  ConcurrentHashMap<Integer,IdNumberVo> agreementMap = new ConcurrentHashMap<Integer,IdNumberVo>();
	
	
	public void addTotalCount(int num){
		totalCount+=num;
	}
}
