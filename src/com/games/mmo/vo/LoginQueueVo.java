package com.games.mmo.vo;

public class LoginQueueVo implements Comparable<LoginQueueVo> {
	public int userId;
	public String iuid;
	public int rankNum;
	public int loginTime;
	
	
	public LoginQueueVo() {
		super();
	}

	

	public LoginQueueVo(int userId, String iuid, int rankNum,int loginTime) {
		super();
		this.userId = userId;
		this.iuid = iuid;
		this.rankNum = rankNum;
		this.loginTime=loginTime;
	}



	@Override
	public int compareTo(LoginQueueVo o) {
		return loginTime-o.loginTime;
	}
	
}
