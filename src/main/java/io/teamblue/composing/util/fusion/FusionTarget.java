package io.teamblue.composing.util.fusion;

import net.minecraft.item.Item;

public class FusionTarget {
    private final int level;
    private final FusionType type;
    private final FusionModifier modifier;
    private final double chance;
    private final Item itemTarget;

    public FusionTarget(Item itemTarget, double chance) {
        this.level = -1;
        this.type = FusionType.UPGRADE_ITEM;
        this.modifier = null;
        this.itemTarget = itemTarget;
        this.chance = chance;
    }

    public FusionTarget(FusionModifier modifier, int level, double chance) {
        this.level = level;
        this.type = FusionType.UPGRADE_TOOL;
        this.modifier = modifier;
        this.itemTarget = null;
        this.chance = chance;
    }

    public int getLevel() {
        return level;
    }

    public FusionType getType() {
        return type;
    }

    public FusionModifier getModifier() {
        return modifier;
    }

    public Item getItemTarget() {
        return itemTarget;
    }

    public double getChance() {
        return chance;
    }
}