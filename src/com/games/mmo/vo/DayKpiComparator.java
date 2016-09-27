package com.games.mmo.vo;

import java.util.Comparator;

import com.games.backend.vo.DayKpiVo;

public class DayKpiComparator implements Comparator<DayKpiVo>{

		public int compare(DayKpiVo o1, DayKpiVo o2) {
			return  (o1.summary_time - o2.summary_time)>0?1:(o1.summary_time - o2.summary_time)==0?0:-1;
		}


}
