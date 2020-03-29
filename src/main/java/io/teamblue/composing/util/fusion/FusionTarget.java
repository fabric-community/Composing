package io.teamblue.composing.util.fusion;

public class FusionTarget {
    private final int level;
    private final FusionType type;
    private final double chance;

    public FusionTarget(int level, FusionType type, double chance) {
        this.level = level;
        this.type = type;
        this.chance = chance;
    }

    public int getLevel() {
        return level;
    }

    public FusionType getType() {
        return type;
    }

    public double getChance() {
        return chance;
    }
}