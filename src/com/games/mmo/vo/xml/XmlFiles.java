package com.games.mmo.vo.xml;

import com.alibaba.fastjson.annotation.JSONField;

public class XmlFiles {
	public ConstantFile constantFile;
	@JSONField(serialize = false)
	public LanguageFile languageFile;
	@JSONField(serialize = false)
	public PlayerFile playerFile;
}
