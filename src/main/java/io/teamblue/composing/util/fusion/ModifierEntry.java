package io.teamblue.composing.util.fusion;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

public class ModifierEntry {
    public final EntityAttribute name;
    public final EntityAttributeModifier modifier;
    public final EquipmentSlot slot;

    public ModifierEntry(EntityAttribute name, EntityAttributeModifier modifier, EquipmentSlot slot) {
        this.name = name;
        this.modifier = modifier;
        this.slot = slot;
    }
}
