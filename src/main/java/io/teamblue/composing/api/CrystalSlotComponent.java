package io.teamblue.composing.api;

import nerdhub.cardinal.components.api.util.ItemComponent;

public interface CrystalSlotComponent extends ItemComponent<CrystalSlotComponent> {
	int getSlots();
	AttributeCrystal getCrystal(int slot);
	SlotType getType();
}
