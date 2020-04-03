package io.teamblue.composing.impl;

import io.teamblue.composing.Composing;
import io.teamblue.composing.api.CrystalElement;
import io.teamblue.composing.api.CrystalHolder;
import io.teamblue.composing.api.SlotType;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;

import net.minecraft.nbt.CompoundTag;

import net.fabricmc.fabric.api.util.NbtType;

public class CrystalHolderImpl implements CrystalHolder {
	private SlotType type;
	private int level;
	private CrystalElement primary;
	private CrystalElement secondary;

	public CrystalHolderImpl(SlotType type) {
		this.type = type;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public CrystalElement getPrimary() {
		return primary;
	}

	@Override
	public CrystalElement getSecondary() {
		return secondary;
	}

	@Override
	public SlotType getType() {
		return type;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.level = tag.getInt("Level");
		if (tag.contains("Primary", NbtType.STRING)) this.primary = CrystalElement.forName(tag.getString("Primary"));
		if (tag.contains("Primary", NbtType.STRING)) this.secondary = CrystalElement.forName(tag.getString("Secondary"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("Level", level);
		if (primary != null) tag.putString("Primary", primary.asString());
		if (secondary != null) tag.putString("Secondary", secondary.asString());
		return tag;
	}

	@Override
	public boolean isComponentEqual(Component component) {
		if (component instanceof CrystalHolder) {
			CrystalHolder holder = (CrystalHolder)component;
			//TODO: are crystals ordered?
			return this.getLevel() == holder.getLevel() && this.getPrimary() == holder.getPrimary() && this.getSecondary() == holder.getSecondary();
		}
		return false;
	}

	@Override
	public ComponentType<CrystalHolder> getComponentType() {
		return Composing.CRYSTAL_SLOT_COMPONENT;
	}
}
