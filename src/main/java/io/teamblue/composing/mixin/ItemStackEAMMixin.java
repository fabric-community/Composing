package io.teamblue.composing.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStack.class)
public class ItemStackEAMMixin {
    @Shadow
    public Item getItem() {
        throw new AssertionError("Should not happen!");
    }

    @Redirect(method="getAttributeModifiers", at=@At(value="INVOKE", target="Lcom/google/common/collect/HashMultimap;create()Lcom/google/common/collect/HashMultimap;"))
    public HashMultimap getItemModifiers(EquipmentSlot slot) {
        return (HashMultimap) this.getItem().getAttributeModifiers(slot);
    }
}
