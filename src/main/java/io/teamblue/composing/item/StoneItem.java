package io.teamblue.composing.item;

import net.minecraft.item.Item;

public class StoneItem extends Item {

	private final int level;

	public static StoneItem fromData(int level) {
		if (level == 0)
			return ComposingItems.BLESSING_STONE;
		else if (level == 1)
			return ComposingItems.SOUL_STONE;
		else if (level == 2)
			return ComposingItems.HOLY_STONE;
		else
			return null;
	}
	
	public StoneItem(Settings settings, int level) {
		super(settings);
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
