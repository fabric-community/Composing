package io.teamblue.composing.item;

import net.minecraft.item.Item;

public class StoneItem extends Item {

	private final int level;
	
	public StoneItem(Settings settings, int level) {
		super(settings);
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}
