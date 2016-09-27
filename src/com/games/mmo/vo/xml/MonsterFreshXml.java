package com.games.mmo.vo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class MonsterFreshXml {
	public Monsters monsters;
	public static class Monsters{
		public List<Monster> monster;
		
		public static class Monster {
			@XmlAttribute
			public int groupId;
			@XmlAttribute
			public int onlyId;
			@XmlAttribute
			public int id;
			@XmlAttribute
			public int rotate;
			@XmlAttribute
			public Double x;
			@XmlAttribute
			public Double y;
			@XmlAttribute
			public Double z;
			@XmlAttribute
			public int interval;
			@XmlAttribute
			public int tag;
		}
	}
}
