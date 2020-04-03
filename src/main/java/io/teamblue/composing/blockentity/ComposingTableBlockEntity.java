package io.teamblue.composing.blockentity;

import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.ITrinket;
import io.teamblue.composing.Composing;
import io.teamblue.composing.item.ComposingItems;
import io.teamblue.composing.item.CrystalItem;
import io.teamblue.composing.item.StoneItem;
import io.teamblue.composing.util.fusion.EntityAttributeModifiers;
import io.teamblue.composing.util.fusion.FusionTarget;
import io.teamblue.composing.util.fusion.FusionType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;

import java.util.*;
import java.util.stream.Collectors;

public class ComposingTableBlockEntity extends BlockEntity {
    public Item slot1;
    public Item slot2;
    public Item slot3;
    public ItemStack tool;

    private Random rand = new Random();

    // Fields so we dont have to pass them around
    private int crystalLevel;
    private Set<Item> uniqueCrystals;
    private Set<Item> uniqueStones;

    public ComposingTableBlockEntity() {
        super(Composing.COMPOSING_TABLE_BLOCK_ENTITY_TYPE);
        this.tool = ItemStack.EMPTY;
    }

    private FusionTarget getFusionTarget() {
        crystalLevel = -1;
        int crystalCount = 0;
        uniqueCrystals = new HashSet<>();
        int stoneLevel = -1;
        int stoneCount = 0;
        uniqueStones = new HashSet<>();

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
            if (crystalLevel == 2 || !tool.isEmpty() || uniqueCrystals.size() == 2) {
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }
            return new FusionTarget(crystalLevel, FusionType.UPGRADE_CRYSTAL, (crystalCount == 3) ? 1 : (crystalCount == 2) ? 0.5 : 0.25);
        } else if (crystalLevel == -1) {
            // Upgrade stones
            if (stoneLevel == 2 || !tool.isEmpty() || uniqueStones.size() != 1) {
                return new FusionTarget(-1, FusionType.INVALID, 0);
            }
            return new FusionTarget(stoneLevel, FusionType.UPGRADE_STONE, (stoneCount == 3) ? 1 : (stoneCount == 2) ? 0.5 : 0.25);
        } else {
            // compose items
            if (tool.isEmpty()){
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
    private Pair<String, EntityAttributeModifier> getTargetModifier(FusionTarget target) {
        // Size 1: 1 crystal type
        // Size 2: 2 crystal types
        List<CrystalItem> crystals = uniqueCrystals.stream().map(i -> (CrystalItem)i).collect(Collectors.toList());

        // Crystals to check for
        CrystalItem earth = new CrystalItem[] { ComposingItems.SMALL_EARTH_CRYSTAL, ComposingItems.MEDIUM_EARTH_CRYSTAL, ComposingItems.LARGE_EARTH_CRYSTAL }[crystalLevel];
        CrystalItem water = new CrystalItem[] { ComposingItems.SMALL_WATER_CRYSTAL, ComposingItems.MEDIUM_WATER_CRYSTAL, ComposingItems.LARGE_WATER_CRYSTAL }[crystalLevel];
        CrystalItem wind = new CrystalItem[] { ComposingItems.SMALL_WIND_CRYSTAL, ComposingItems.MEDIUM_WIND_CRYSTAL, ComposingItems.LARGE_WIND_CRYSTAL }[crystalLevel];
        CrystalItem fire = new CrystalItem[] { ComposingItems.SMALL_FIRE_CRYSTAL, ComposingItems.MEDIUM_FIRE_CRYSTAL, ComposingItems.LARGE_FIRE_CRYSTAL }[crystalLevel];

        if (!tool.isEmpty()) {
            Item item = tool.getItem();
            if (item instanceof ArmorItem) {
                // Armor modifiers
            } else if (item instanceof SwordItem || item instanceof RangedWeaponItem || item instanceof TridentItem) {
                // Weapon modifiers
                if (crystals.contains(earth) && crystals.contains(fire)) {
                    // Attack bonus
                    return new Pair<>(
                            EntityAttributes.ATTACK_DAMAGE.getId(),
                            new EntityAttributeModifier(
                                    EntityAttributeModifiers.WEAPON_DAMAGE,
                                    "Weapon Damage",
                                    target.getLevel()+1,  // 1-4
                                    EntityAttributeModifier.Operation.ADDITION));
                }
            } else if (item instanceof ToolItem) {
                // Tool modifiers
            } else if (item instanceof ITrinket) {
                // Trinket modifiers
            }
        }
        return null;
    }

    private Item getTargetItem(FusionTarget target) {
        // Stone or Crystal based on inputs
        switch (target.getType()) {

            case UPGRADE_CRYSTAL:
                // All crystals the same?
                Set<CrystalItem> items = uniqueCrystals.stream().map(i -> (CrystalItem)i).collect(Collectors.toSet());

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
                StoneItem stone = uniqueStones.stream().filter(i -> i instanceof StoneItem).map(i -> (StoneItem)i).findFirst().get();
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
                    Pair<String, EntityAttributeModifier> modifier = getTargetModifier(target);
                    if (modifier == null) {
                        return;
                    }
                    tool.addAttributeModifier(modifier.getFirst(), modifier.getSecond(), null);
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

    public void dropItems() {
        List<ItemEntity> items = new ArrayList<>();
        if (slot1 != null) {
            items.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(slot1, 1)));
        }
        if (slot2 != null) {
            items.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(slot2, 1)));
        }
        if (slot3 != null) {
            items.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(slot3, 1)));
        }
        if (tool != null && !tool.isEmpty()) {
            items.add(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), tool));
        }

        for (ItemEntity item : items) {
            world.spawnEntity(item);
        }
    }
}
