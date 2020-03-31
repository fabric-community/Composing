package io.teamblue.composing.api;

import nerdhub.cardinal.components.api.util.ItemComponent;

public interface CrystalHolder extends ItemComponent<CrystalHolder> {
	int getLevel();
	CrystalElement getPrimary();
	CrystalElement getSecondary();
	SlotType getType();
}
