package com.games.mmo.type;

import com.storm.lib.util.IntUtil;

public class SoulType {

	public enum Type {
		NONE(0),FIRE(1), WIND(2), WATER(3), SOIL(4), THUNDER(5);
		private final int id;

		private Type(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public static int size() {
			return values().length + 1;
		}

		public static Type getType(int id) {
			if(IntUtil.inRange(id, 0, Type.size()-1)){
				for (Type type : values()) {
					if (type.id == id) {
						return type;
					}
				}
			}
			return Type.NONE;
		}

		public static Type getType(String name) {
			for (Type type : values()) {
				if (type.name().equals(name)) {
					return type;
				}
			}
			return Type.NONE;
		}
	}

	private int[] types;

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

	public SoulType() {
		types = new int[Type.size()];
	}

	public int getValue(int index) {
		return (index < 0 || types.length <= index) ? 0 : types[index];
	}

	public int getValue(Type type) {
		return getValue(type.getId());
	}

	/**
	 * 五行属性相克
	 * 
	 * @param type
	 * @return
	 */
	public static int fetchPossessAtbById(Type type) {
		switch (type) {
		case FIRE:
			return Type.SOIL.id;
		case WIND:
			return Type.THUNDER.id;
		case WATER:
			return Type.FIRE.id;
		case SOIL:
			return Type.WIND.id;
		case THUNDER:
			return Type.WATER.id;
		default:
			return 0;
		}
	}

	/**
	 * 五行属性被克
	 * @param type
	 * @return
	 */
	public static int fetchPossessAtbToId(Type type) {
		switch (type) {
		case FIRE:
			return Type.WATER.id;
		case WIND:
			return Type.SOIL.id;
		case WATER:
			return Type.THUNDER.id;
		case SOIL:
			return Type.FIRE.id;
		case THUNDER:
			return Type.WIND.id;
		default:
			return 0;
		}
	}

}
