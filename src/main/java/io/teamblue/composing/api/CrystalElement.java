package io.teamblue.composing.api;

import net.minecraft.util.StringIdentifiable;

public enum CrystalElement implements StringIdentifiable {
	WIND("wind"),
	EARTH("earth"),
	FIRE("fire"),
	WATER("water");

	private String name;

	private CrystalElement(String name) {
		this.name = name;
	}

	@Override
	public String asString() {
		return name;
	}

	public static CrystalElement forName(String name) {
		for (CrystalElement elem : values()) {
			if (elem.asString().equals(name)) return elem;
		}
		return WIND;
	}
}
