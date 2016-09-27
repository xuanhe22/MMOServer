package com.games.mmo.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.storm.lib.base.BasePropertyVo;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;

public class ConstellAttachVo extends BasePropertyVo {

	private int sumRandom;
	
	private List<Attach> attachs;
	
	private Random random;
	
	@Override
	public Object[] fetchProperyItems() {
		return null;
	}

	@Override
	public void loadProperty(String val, String spliter) {
		String[] vals = StringUtil.split(val, spliter);
		random = RandomUtil.fetchRandom();
		sumRandom = 0;
		attachs = new ArrayList<ConstellAttachVo.Attach>();
		for(String str:vals)
		{
			String[] strs = StringUtil.split(str, "|");
			int attachId = Integer.parseInt(strs[0]);
			int ran = Integer.parseInt(strs[1]);
			sumRandom+=ran;
			Attach attach = new Attach();
			attach.random = ran;
			attach.attachId = attachId;
			attachs.add(attach);
		}
	}
	
	public int getRandomAttach()
	{
		int nowRan = random.nextInt(sumRandom)+1;
		for(Attach attach:attachs)
		{
			if(nowRan <= attach.random)
				return attach.attachId;
			nowRan-=attach.random;
		}
		return attachs.get(0).attachId;
	}
	
	class Attach{
		public int random;
		public int attachId;
		
	}

}
