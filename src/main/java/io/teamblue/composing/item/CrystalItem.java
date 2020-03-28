package io.teamblue.composing.item;

import net.minecraft.item.Item;

import io.teamblue.composing.api.CrystalElement;

public class CrystalItem extends Item {

	private final CrystalElement element;
	private final int level;
	
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
