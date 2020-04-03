package io.teamblue.composing.mixin;

import io.teamblue.composing.Composing;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
abstract public class HostileEntityDropsMixin extends Entity {
    public HostileEntityDropsMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected LootContext.Builder getLootContextBuilder(boolean causedByPlayer, DamageSource source) {
        return null;
    }

    @Inject(method = "drop", at = @At(value="INVOKE", target = "Lnet/minecraft/entity/LivingEntity;dropLoot(Lnet/minecraft/entity/damage/DamageSource;Z)V"))
    void extraLootTable(DamageSource source, CallbackInfo ci) {
        if ((Entity)this instanceof HostileEntity) {
            Identifier identifier = new Identifier(Composing.MODID, "entities/hostile");  // this.getLootTable();
            LootTable lootTable = this.world.getServer().getLootManager().getSupplier(identifier);
            LootContext.Builder builder = this.getLootContextBuilder(source.getAttacker() instanceof PlayerEntity, source);
            lootTable.dropLimited(builder.build(LootContextTypes.ENTITY), this::dropStack);
        }
    }
}
