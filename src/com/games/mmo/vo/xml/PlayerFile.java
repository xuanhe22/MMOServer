package com.games.mmo.vo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class PlayerFile {
	public UserInfos userInfos;
	public static class UserInfos{
		public List<UserInfo> userInfo;
		public static class UserInfo{
			@XmlAttribute
			public String iuid;
			@XmlAttribute
			public int totalDiamond;
		}
		
	}
}
