package com.games.mmo.vo;

import java.io.Serializable;

public class LogVo implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
//	  `role_id` int(11) DEFAULT NULL COMMENT '描述无',
//	  `role_name` varchar(255) DEFAULT NULL COMMENT '消耗类型（1=只金币，2=只钻石，7=优先金币，8=优先钻石）无',
//	  `user_id` int(11) DEFAULT NULL COMMENT '消耗数量无',
//	  `user_iuid` varchar(255) DEFAULT NULL COMMENT '每天免费次数无',
//	  `log_type` int(11) DEFAULT NULL COMMENT '等价道具无',
//	  `log_time` bigint(22) DEFAULT NULL,
//	  `log_par1` int(11) DEFAULT NULL COMMENT '是否提示无',
//	  `log_par2` int(11) DEFAULT NULL,
//	  `log_par3` int(11) DEFAULT NULL,
//	  `source_type` int(11) DEFAULT NULL,
//	  `source_txt` varchar(255) DEFAULT NULL,
//	  `remark_txt` varchar(255) DEFAULT NULL,
	public Integer roleId;
	public String roleName;
	public Integer userId;
	public String userIuid;
	public Integer logType;
	public Long logTime;
	public Integer logPar1;
	public Integer logPar2;
	public Integer logPar3;
	public Integer sourceType=0;
	public String sourceTxt;
	public String remartTxt;
	public String channel;
}
