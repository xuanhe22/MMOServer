package com.games.mmo.vo;

import java.lang.Thread.State;

public class ThreadInforVo implements Comparable<ThreadInforVo>{
	public String name;
	public State state;
	public StackTraceElement[] trace;
	@Override
	public int compareTo(ThreadInforVo o) {
		return o.name.compareTo(name);
	}
}	
