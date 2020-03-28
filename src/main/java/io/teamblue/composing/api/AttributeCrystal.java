package io.teamblue.composing.api;

import com.google.common.collect.Multimap;

import net.minecraft.entity.attribute.EntityAttributeModifier;

public interface AttributeCrystal {
	Multimap<String, EntityAttributeModifier> getCrystalModifiers(); //TODO: do we want to avoid mojang's multimap hell? *can* we avoid mojang's multimap hell?
}
