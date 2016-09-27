package com.games.mmo.type;

import com.games.mmo.cache.GlobalCache;

public class ColourType {
	/**
	 * 颜色：白色
	 */
	public static final String COLOUR_WHITE = "[e6e1da]";
	public static final String COLOUR_WHITE_STR = GlobalCache.fetchLanguageMap("key2665");
	
	/**
	 * 颜色：绿色
	 */
	public static final String COLOUR_GREEN = "[18ff00]";
	public static final String COLOUR_GREEN_STR = GlobalCache.fetchLanguageMap("key2666");
	
	/**
	 * 颜色：蓝色
	 */
	public static final String COLOUR_BLUE = "[228aff]";
	public static final String COLOUR_BLUE_STR = GlobalCache.fetchLanguageMap("key2667");
	
	/**
	 * 颜色：紫色
	 */
	public static final String COLOUR_PURPLE = "[ff02fc]";
	public static final String COLOUR_PURPLE_STR = GlobalCache.fetchLanguageMap("key2668");
	
	/**
	 * 颜色：橙色
	 */
	public static final String COLOUR_ORANGE = "[c55800]";
	public static final String COLOUR_ORANGE_STR = GlobalCache.fetchLanguageMap("key2669");
	
	/**
	 * 颜色：金色
	 */
	public static final String COLOUR_GOLDEN = "[fffc00]";
	public static final String COLOUR_GOLDEN_STR = GlobalCache.fetchLanguageMap("key2670");
	
	/**
	 * 颜色：黄色
	 */
	public static final String COLOUR_YELLOW ="[D3C700]";
	public static final String COLOUR_YELLOW_STR = GlobalCache.fetchLanguageMap("key2671");
	
	/**
	 * 颜色：红色
	 */
	public static final String COLOUR_RED ="[ff0000]";
	public static final String COLOUR_RED_STR = GlobalCache.fetchLanguageMap("key2672");
	
	/**
	 *  根据品质获取颜色
	 * @param quality
	 * @return
	 */
	public static String fetchColourByQuality(Integer quality){
		String colour = COLOUR_WHITE;
		if(quality == null){
			return colour;
		}
		if(quality.intValue() == 1){
			colour = COLOUR_WHITE;
		}
		else if(quality.intValue() == 2)
		{
			colour = COLOUR_GREEN;
		}
		else if(quality.intValue() == 3)
		{
			colour = COLOUR_BLUE;
		}
		else if(quality.intValue() == 4)
		{
			colour = COLOUR_PURPLE;
		}
		else if(quality.intValue() == 5)
		{
			colour = COLOUR_ORANGE;
		}
		else if(quality.intValue() == 6)
		{
			colour = COLOUR_GOLDEN;
		}
		return colour;
	}
	
	
	/**
	 *  根据品质获取颜色
	 * @param quality
	 * @return
	 */
	public static String fetchColourByQualityStr(Integer quality){
		String colour = COLOUR_WHITE_STR;
		if(quality == null){
			return colour;
		}
		if(quality.intValue() == 1){
			colour = COLOUR_WHITE_STR;
		}
		else if(quality.intValue() == 2)
		{
			colour = COLOUR_GREEN_STR;
		}
		else if(quality.intValue() == 3)
		{
			colour = COLOUR_BLUE_STR;
		}
		else if(quality.intValue() == 4)
		{
			colour = COLOUR_PURPLE_STR;
		}
		else if(quality.intValue() == 5)
		{
			colour = COLOUR_ORANGE_STR;
		}
		else if(quality.intValue() == 6)
		{
			colour = COLOUR_GOLDEN_STR;
		}
		return colour;
	}
}
