package com.games.mmo.vo.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LanguageFile {
	public Languages languages;
	public static class Languages{
		public List<Words> words;
		public static class Words{
			@XmlAttribute
			public String key;
			@XmlAttribute
			public String value;
		}
		
	}
	
}
