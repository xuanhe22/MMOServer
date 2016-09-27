package com.games.mmo.vo;

import com.storm.lib.base.BasePropertyVo;

public class SystemPartPo extends BasePropertyVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int useNum;
	public int openNum;
	public double useRate;
	public int type;
	public SystemPartPo(int useNum, int openNum, double useRate, int type) {
		this.useNum = useNum;
		this.openNum = openNum;
		this.useRate = useRate;
		this.type = type;
	}
	@Override
	public Object[] fetchProperyItems() {
		return new Object[]{useNum, openNum, useRate, type};
	}
	@Override
	public void loadProperty(String val, String spliter) {
	}
	public SystemPartPo add(SystemPartPo systemPartPo) {
		if (systemPartPo.type == this.type) {
			this.openNum += systemPartPo.openNum;
			this.useNum += systemPartPo.useNum;
			this.useRate = this.useNum == 0 ? 0 : this.useNum * 1.0 / this.openNum;
		}
		return this;
	}
}
