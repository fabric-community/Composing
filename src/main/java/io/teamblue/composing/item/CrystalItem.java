package io.teamblue.composing.item;

import net.minecraft.item.Item;

import io.teamblue.composing.api.CrystalElement;

public class CrystalItem extends Item {

	private final CrystalElement element;
	private final int level;
	
	public static CrystalItem fromData(CrystalElement element, int level) {
		switch (element) {
			case WIND:
				if (level == 0)
					return ComposingItems.SMALL_WIND_CRYSTAL;
				else if (level == 1)
					return ComposingItems.MEDIUM_WIND_CRYSTAL;
				else if (level == 2)
					return ComposingItems.LARGE_WIND_CRYSTAL;
				break;
			case EARTH:
				if (level == 0)
					return ComposingItems.SMALL_EARTH_CRYSTAL;
				else if (level == 1)
					return ComposingItems.MEDIUM_EARTH_CRYSTAL;
				else if (level == 2)
					return ComposingItems.LARGE_EARTH_CRYSTAL;
				break;
			case FIRE:
				if (level == 0)
					return ComposingItems.SMALL_FIRE_CRYSTAL;
				else if (level == 1)
					return ComposingItems.MEDIUM_FIRE_CRYSTAL;
				else if (level == 2)
					return ComposingItems.LARGE_FIRE_CRYSTAL;
				break;
			case WATER:
				if (level == 0)
					return ComposingItems.SMALL_WATER_CRYSTAL;
				else if (level == 1)
					return ComposingItems.MEDIUM_WATER_CRYSTAL;
				else if (level == 2)
					return ComposingItems.LARGE_WATER_CRYSTAL;
				break;
		}
		return null;
	}
	
	public CrystalItem(Settings settings, CrystalElement element, int level) {
		super(settings);
		this.element = element;
		this.level = level;
	}

	public CrystalElement getElement() {
		return element;
	}
	
	public int getLevel() {
		return level;
	}
}
