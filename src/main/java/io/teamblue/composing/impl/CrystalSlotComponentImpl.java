package io.teamblue.composing.impl;

import io.teamblue.composing.api.AttributeCrystal;
import io.teamblue.composing.api.CrystalSlotComponent;
import io.teamblue.composing.api.SlotType;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.component.Component;

import net.minecraft.nbt.CompoundTag;

public class CrystalSlotComponentImpl implements CrystalSlotComponent {
	private SlotType type;

	public CrystalSlotComponentImpl(SlotType type) {
		this.type = type;
	}

	@Override
	public int getSlots() {
		return 0;
	}

	@Override
	public AttributeCrystal getCrystal(int slot) {
		return null;
	}

	@Override
	public void fromTag(CompoundTag compoundTag) {

	}

	@Override
	public CompoundTag toTag(CompoundTag compoundTag) {
		return null;
	}

	@Override
	public boolean isComponentEqual(Component component) {
		return false;
	}

	@Override
	public ComponentType<CrystalSlotComponent> getComponentType() {
		return null;
	}
}
