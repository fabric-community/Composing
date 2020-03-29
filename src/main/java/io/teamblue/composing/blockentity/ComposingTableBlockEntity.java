package io.teamblue.composing.blockentity;

import io.teamblue.composing.Composing;
import io.teamblue.composing.item.ComposingItems;
import io.teamblue.composing.item.CrystalItem;
import io.teamblue.composing.item.StoneItem;
import io.teamblue.composing.util.fusion.FusionTarget;
import io.teamblue.composing.util.fusion.FusionType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

public class ComposingTableBlockEntity extends BlockEntity {
    private Item slot1;
    private Item slot2;
    private Item slot3;
    private ItemStack tool;

    private Random rand = new Random();

    public ComposingTableBlockEntity() {
        super(Composing.COMPOSING_TABLE_BLOCK_ENTITY_TYPE);
    }

    private FusionTarget getFusionTarget() {
        int crystalLevel = -1;
        int crystalCount = 0;
        Set<Item> uniqueCrystals = new HashSet<>();
        int stoneLevel = -1;
        int stoneCount = 0;
        Set<Item> uniqueStones = new HashSet<>();

        for (Item it : new Item[] { slot1, slot2, slot3 }) {
            if (it instanceof CrystalItem) {
                crystalCount++;
                if (crystalLevel >= 0 && ((CrystalItem) it).getLevel() != crystalLevel) {
                    return new FusionTarget(-1, FusionType.INVALID, 0);
                } else {
                    crystalLevel = ((CrystalItem) it).getLevel();
                    uniqueCrystals.add(it);
                }
            } else if (it instanceof StoneItem) {
                stoneCount++;
                if (stoneLevel >= 0 && ((StoneItem) it).getLevel() != stoneLevel) {
                    return new FusionTarget(-1, FusionType.INVALID, 0);
                } else {
                    stoneLevel = ((StoneItem) it).getLevel();
                    uniqueStones.add(it);
                }
            }
        }

        if (stoneLevel == -1) {
            // upgrade crystals
            if (crystalLevel == 2 || (tool != null && !tool.isEmpty()) || uniqueCrystals.size() == 2) {
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }
            return new FusionTarget(crystalLevel, FusionType.UPGRADE_CRYSTAL, (crystalCount == 3) ? 1 : (crystalCount == 2) ? 0.5 : 0.25);
        } else if (crystalLevel == -1) {
            // Upgrade stones
            if (stoneLevel == 2 || (tool != null && !tool.isEmpty())|| uniqueStones.size() != 1) {
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }
            return new FusionTarget(stoneLevel, FusionType.UPGRADE_STONE, (stoneCount == 3) ? 1 : (stoneCount == 2) ? 0.5 : 0.25);
        } else {
            // compose items
            if (tool == null || tool.isEmpty()){
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }

            if (crystalCount != 2) {
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }

            switch (stoneLevel) {
                case 0:
                    if (crystalLevel > 1) {
                        return new FusionTarget(-1, FusionType.INVALID, 0);
                    }
                    return new FusionTarget(crystalLevel, FusionType.UPGRADE_TOOL, 1);
                case 1:
                    if (crystalLevel != 2) {
                        return new FusionTarget(-1, FusionType.INVALID, 0);
                    }
                    return new FusionTarget(2, FusionType.UPGRADE_TOOL, 1);
                case 2:
                    if (crystalLevel != 2) {
                        return new FusionTarget(-1, FusionType.INVALID, 0);
                    }
                    return new FusionTarget(3, FusionType.UPGRADE_TOOL, 0.5);
                default:
                    throw new AssertionError("Should not happen");
            }
        }
    }

    // TODO
    // - Get available modifiers for item
    // - If invalid, return null
    // - If valid, return modifier matching crystals
    private EntityAttributeModifier getTargetModifier() {
        return null;
    }

    private Item getTargetItem(FusionTarget target) {
        // Stone or Crystal based on inputs
        switch (target.getType()) {

            case UPGRADE_CRYSTAL:
                // All crystals the same?
                Set<CrystalItem> items = new HashSet<>();
                for (Item i : new Item[] { slot1, slot2, slot3 }) {
                    if (i != null) {
                        items.add((CrystalItem)i);
                    }
                }
                
                if (items.size() == 1) {
                    CrystalItem crystalItem = items.stream().findFirst().get();
                    if (ComposingItems.SMALL_EARTH_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.MEDIUM_EARTH_CRYSTAL;
                    } else if (ComposingItems.SMALL_WATER_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.MEDIUM_WATER_CRYSTAL;
                    } else if (ComposingItems.SMALL_WIND_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.MEDIUM_WIND_CRYSTAL;
                    } else if (ComposingItems.SMALL_FIRE_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.MEDIUM_FIRE_CRYSTAL;
                    } else if (ComposingItems.MEDIUM_EARTH_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.LARGE_EARTH_CRYSTAL;
                    } else if (ComposingItems.MEDIUM_WATER_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.LARGE_WATER_CRYSTAL;
                    } else if (ComposingItems.MEDIUM_WIND_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.LARGE_WIND_CRYSTAL;
                    } else if (ComposingItems.MEDIUM_FIRE_CRYSTAL.equals(crystalItem)) {
                        return ComposingItems.LARGE_FIRE_CRYSTAL;
                    }
                } else if (items.size() == 3) {
                    // Get the crystal not in here one tier higher
                    // E.g. small water, earth and wind gives medium fire
                    if (items.contains(ComposingItems.SMALL_WATER_CRYSTAL) && items.contains(ComposingItems.SMALL_EARTH_CRYSTAL) && items.contains(ComposingItems.SMALL_FIRE_CRYSTAL)) {
                        return ComposingItems.MEDIUM_WIND_CRYSTAL;
                    } else if (items.contains(ComposingItems.SMALL_WATER_CRYSTAL) && items.contains(ComposingItems.SMALL_EARTH_CRYSTAL) && items.contains(ComposingItems.SMALL_WIND_CRYSTAL)) {
                        return ComposingItems.MEDIUM_FIRE_CRYSTAL;
                    } else if (items.contains(ComposingItems.SMALL_WATER_CRYSTAL) && items.contains(ComposingItems.SMALL_WIND_CRYSTAL) && items.contains(ComposingItems.SMALL_FIRE_CRYSTAL)) {
                        return ComposingItems.MEDIUM_EARTH_CRYSTAL;
                    } else if (items.contains(ComposingItems.SMALL_WIND_CRYSTAL) && items.contains(ComposingItems.SMALL_EARTH_CRYSTAL) && items.contains(ComposingItems.SMALL_FIRE_CRYSTAL)) {
                        return ComposingItems.MEDIUM_WATER_CRYSTAL;
                    } else if (items.contains(ComposingItems.MEDIUM_WATER_CRYSTAL) && items.contains(ComposingItems.MEDIUM_EARTH_CRYSTAL) && items.contains(ComposingItems.MEDIUM_FIRE_CRYSTAL)) {
                        return ComposingItems.LARGE_WIND_CRYSTAL;
                    } else if (items.contains(ComposingItems.MEDIUM_WATER_CRYSTAL) && items.contains(ComposingItems.MEDIUM_EARTH_CRYSTAL) && items.contains(ComposingItems.MEDIUM_WIND_CRYSTAL)) {
                        return ComposingItems.LARGE_FIRE_CRYSTAL;
                    } else if (items.contains(ComposingItems.MEDIUM_WATER_CRYSTAL) && items.contains(ComposingItems.MEDIUM_WIND_CRYSTAL) && items.contains(ComposingItems.MEDIUM_FIRE_CRYSTAL)) {
                        return ComposingItems.LARGE_EARTH_CRYSTAL;
                    } else if (items.contains(ComposingItems.MEDIUM_WIND_CRYSTAL) && items.contains(ComposingItems.MEDIUM_EARTH_CRYSTAL) && items.contains(ComposingItems.MEDIUM_FIRE_CRYSTAL)) {
                        return ComposingItems.LARGE_WATER_CRYSTAL;
                    }
                } else {
                    throw new AssertionError("Should not happen");
                }
                
            case UPGRADE_STONE:
                StoneItem stone = (StoneItem) Arrays.stream(new Item[] { slot1, slot2, slot3 }).findFirst().get();
                if (stone == ComposingItems.BLESSING_STONE) {
                    return ComposingItems.SOUL_STONE;
                } else if (stone == ComposingItems.SOUL_STONE) {
                    return ComposingItems.HOLY_STONE;
                } else {
                    throw new AssertionError("Should not happen");
                }
            default:
                throw new AssertionError("Should not happen");
        }
    }

    private void craft() {
        FusionTarget target = getFusionTarget();
        if (target.getType() != FusionType.INVALID) {
            if (rand.nextDouble() <= target.getChance()) {
                ItemStack stackToSpawn;

                if (target.getType() == FusionType.UPGRADE_TOOL) {
                    EntityAttributeModifier modifier = getTargetModifier();
                    if (modifier == null) {
                        return;
                    }
                    tool.addAttributeModifier(modifier.getName(), modifier, null);
                    stackToSpawn = tool;
                    tool = null;
                } else if (target.getType() == FusionType.UPGRADE_CRYSTAL || target.getType() == FusionType.UPGRADE_STONE) {
                    stackToSpawn = new ItemStack(getTargetItem(target), 1);
                } else {
                    throw new AssertionError("Should not happen");
                }

                ItemEntity e = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stackToSpawn);
                e.setVelocity(0, 1, 0);
                world.spawnEntity(e);
            }

            // Clear items used
            slot1 = null;
            slot2 = null;
            slot3 = null;
        }
    }
}
