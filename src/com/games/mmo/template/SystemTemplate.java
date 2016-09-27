package com.games.mmo.template;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;

import rmi.gm.vo.BlockSpeakVo;

import com.storm.lib.template.BaseGameServerTemplate;

@Controller
public class SystemTemplate extends BaseGameServerTemplate{

	@Override
	public Object getSpeakVos(String start, String limit, int type) {
		return null;
	}

	@Override
	public int getSpeakVosCount(int type) {
		return 0;
	}

	@Override
	public List<BlockSpeakVo> getAllBlockSpeak(String start, String limit,
			int type) {
		return null;
	}

	@Override
	public void insertBlockSpeak(String iuid, Integer min, int type) {
		
	}

	@Override
	public void removeBlockSpeak(String ids, String roleNames) {
		
	}

	@Override
	public ConcurrentHashMap<Integer, BlockSpeakVo> getBlockSpeak() {

		return null;
	}

	@Override
	public List<BlockSpeakVo> findAllBlockSpeak(String start, String limit,
			int type) {

		return null;
	}

	@Override
	public void insertIpBlock(String blockStartIp, String blockEndIp, int type) {

		
	}

	@Override
	public void removeIpBlock(String ids) {

		
	}

	@Override
	public Object getBlockIps(String start, String limit, int type) {

		return null;
	}


	
	
	
	
}
