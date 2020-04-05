package io.teamblue.composing.blockentity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dev.emi.trinkets.api.ITrinket;
import io.teamblue.composing.Composing;
import io.teamblue.composing.api.CrystalElement;
import io.teamblue.composing.item.CrystalItem;
import io.teamblue.composing.item.StoneItem;
import io.teamblue.composing.util.fusion.EntityAttributeModifiers;
import io.teamblue.composing.util.fusion.FusionModifier;
import io.teamblue.composing.util.fusion.FusionTarget;
import io.teamblue.composing.util.fusion.FusionType;
import io.teamblue.composing.util.fusion.ModifierEntry;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.registry.Registry;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.util.NbtType;

public class ComposingTableBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public Item slot1;
    public Item slot2;
    public Item slot3;
    public ItemStack tool = ItemStack.EMPTY;

    private Random rand = new Random();

    public ComposingTableBlockEntity() {
        super(Composing.COMPOSING_TABLE_BLOCK_ENTITY_TYPE);
    }

    private FusionTarget getFusionTarget() {
        int crystalLevel = -1;
        int crystalCount = 0;
        Set<CrystalElement> uniqueCrystals = new HashSet<>();
        int stoneLevel = -1;
        int stoneCount = 0;

        for (Item it : new Item[] { slot1, slot2, slot3 }) {
            if (it instanceof CrystalItem) {
                crystalCount++;
                if (crystalLevel >= 0 && ((CrystalItem) it).getLevel() != crystalLevel) {
                    return null;
                } else {
                    crystalLevel = ((CrystalItem) it).getLevel();
                    uniqueCrystals.add(((CrystalItem) it).getElement());
                }
            } else if (it instanceof StoneItem) {
                stoneCount++;
                if (stoneLevel >= 0 && ((StoneItem) it).getLevel() != stoneLevel) {
                    return null;
                } else {
                    stoneLevel = ((StoneItem) it).getLevel();
                }
            }
        }

        if (stoneLevel == -1 && crystalCount > 0) {
            // upgrade crystals
            if (crystalLevel == 2 || !tool.isEmpty() || uniqueCrystals.size() == 2) {
                return null;
            }
            CrystalElement crystal = CrystalElement.WIND; // default, shouldn't matter

            if (uniqueCrystals.size() == 1) {
                crystal = uniqueCrystals.iterator().next();
            } else {
                for (CrystalElement search : CrystalElement.values()) {
                    if (!uniqueCrystals.contains(search)) {
                        crystal = search;
                        break;
                    }
                }
            }
            return new FusionTarget(CrystalItem.fromData(crystal, crystalLevel+1), (crystalCount == 3) ? 1 : (crystalCount == 2) ? 0.5 : 0.25);

        } else if (crystalLevel == -1 && stoneCount > 0) {
            // Upgrade stones
            if (stoneLevel == 2 || !tool.isEmpty()) {
                return null;
            }
            return new FusionTarget(StoneItem.fromData(stoneLevel+1), (stoneCount == 3) ? 1 : (stoneCount == 2) ? 0.5 : 0.25);
        } else if (stoneCount > 0 && crystalCount > 0){
            // compose items
            if (tool.isEmpty()){
                return null;
            }

            if (crystalCount != 2) {
                return null;
            }

            switch (stoneLevel) {
                case 0:
                    if (crystalLevel > 1) {
                        return null;
                    }
                    return new FusionTarget(getFusionModifier(uniqueCrystals), crystalLevel, 1);
                case 1:
                    if (crystalLevel != 2) {
                        return null;
                    }
                    return new FusionTarget(getFusionModifier(uniqueCrystals), 2, 1);
                case 2:
                    if (crystalLevel != 2) {
                        return null;
                    }
                    return new FusionTarget(getFusionModifier(uniqueCrystals), 3, 0.5);
                default:
                    throw new AssertionError("Should not happen");
            }
        } else {
            return null;
        }
    }

    private FusionModifier getFusionModifier(Set<CrystalElement> crystals) {
        if (crystals.size() == 1) {
            CrystalElement crystal = crystals.iterator().next();
            switch (crystal) {
                case WIND:
                    return FusionModifier.WIND_WIND;
                case EARTH:
                    return FusionModifier.EARTH_EARTH;
                case FIRE:
                    return FusionModifier.FIRE_FIRE;
                case WATER:
                    return FusionModifier.WATER_WATER;
            }
        } else {
            Iterator<CrystalElement> getter = crystals.iterator();
            CrystalElement crystal_a = getter.next();
            CrystalElement crystal_b = getter.next();
            // multiplex the types
            switch (crystal_a) {
                case WIND:
                    switch (crystal_b) {
                        case EARTH:
                            return FusionModifier.WIND_EARTH;
                        case FIRE:
                            return FusionModifier.FIRE_WIND;
                        case WATER:
                            return FusionModifier.WATER_WIND;
                    }
                case EARTH:
                    switch (crystal_b) {
                        case WIND:
                            return FusionModifier.WIND_EARTH;
                        case FIRE:
                            return FusionModifier.FIRE_EARTH;
                        case WATER:
                            return FusionModifier.WATER_EARTH;
                    }
                case FIRE:
                    switch (crystal_b) {
                        case WIND:
                            return FusionModifier.FIRE_WIND;
                        case EARTH:
                            return FusionModifier.FIRE_EARTH;
                        case WATER:
                            return FusionModifier.FIRE_WATER;
                    }
                case WATER:
                    switch (crystal_b) {
                        case WIND:
                            return FusionModifier.WATER_WIND;
                        case EARTH:
                            return FusionModifier.WATER_EARTH;
                        case FIRE:
                            return FusionModifier.FIRE_WATER;
                    }
            }
        }
        return null;
    }

    // TODO: Add more EAMs
    private ModifierEntry getTargetModifier(FusionTarget target) {
        FusionModifier modifier = target.getModifier();

        if (!tool.isEmpty()) {
            Item item = tool.getItem();
            if (item instanceof ArmorItem) {
                // Armor modifiers
                switch(modifier) {
                    case EARTH_EARTH:
                        return new ModifierEntry(
                                EntityAttributes.ARMOR.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.ARMOR,
                                        "Armor",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));
                    case WIND_EARTH:
                        return new ModifierEntry(
                                EntityAttributes.ARMOR_TOUGHNESS.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.ARMOR_TOUGHNESS,
                                        "Armor Toughness",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));
                    case WATER_WATER:
                        return new ModifierEntry(
                                EntityAttributes.MAX_HEALTH.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.MAX_HEALTH,
                                        "Health",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));
                }
            } else if (item instanceof SwordItem || item instanceof RangedWeaponItem || item instanceof TridentItem) {
                // Weapon modifiers
                switch (modifier) {
                    case FIRE_EARTH:
                        return new ModifierEntry(
                                EntityAttributes.ATTACK_DAMAGE.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.WEAPON_DAMAGE,
                                        "Weapon Damage",
                                        target.getLevel()+1,  // 2-8
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));
                    case WIND_EARTH:
                        return new ModifierEntry(
                                EntityAttributes.ATTACK_DAMAGE.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.WEAPON_DAMAGE,
                                        "Weapon Damage",
                                        .2*target.getLevel()+1,  // 1.2-1.8
                                        EntityAttributeModifier.Operation.MULTIPLY_TOTAL),
                                getEquipmentSlot(item));
                    case WATER_WATER:
                        return new ModifierEntry(
                                EntityAttributes.LUCK.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.LUCK,
                                        "Looting",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));
                }
            } else if (item instanceof ToolItem) {
                // Tool modifiers
                switch (modifier) {
                    case WATER_EARTH:
                        return new ModifierEntry(
                                EntityAttributes.LUCK.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.LUCK,
                                        "Fortune",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                getEquipmentSlot(item));

                }
            } else if (item instanceof ITrinket) {
                // Trinket modifiers
                switch (modifier) {
                    case WATER_WATER:
                        return new ModifierEntry(
                                EntityAttributes.LUCK.getId(),
                                new EntityAttributeModifier(
                                        EntityAttributeModifiers.LUCK,
                                        "Luck",
                                        target.getLevel()+1,  // 1-4
                                        EntityAttributeModifier.Operation.ADDITION),
                                null);
                }
            }
        }
        return null;
    }

    private EquipmentSlot getEquipmentSlot(Item item) {
        if (item instanceof ArmorItem) {
            return ((ArmorItem) item).getSlotType();
        } else {
            return EquipmentSlot.MAINHAND;
        }
    }

    public void craft() {
        if (world.isClient()) {
            return;
        }

        FusionTarget target = getFusionTarget();
        if (target != null) {
            if (rand.nextDouble() <= target.getChance()) {
                ItemStack stackToSpawn;

                if (target.getType() == FusionType.UPGRADE_TOOL) {
                    ModifierEntry modifier = getTargetModifier(target);
                    if (modifier == null) {
                        return;
                    }
                    CompoundTag toolTag = tool.getOrCreateTag();
                    if (toolTag.contains("AttributeModifiers", NbtType.LIST)) {
                        ListTag modTag = toolTag.getList("AttributeModifiers", NbtType.COMPOUND);
                        ListTag newModTag = new ListTag();
                        for (Tag tag : modTag) {
                            CompoundTag compound = (CompoundTag)tag;
                            if (!compound.getUuid("UUID").equals(modifier.modifier.getId())) {
                                newModTag.add(tag);
                            }
                        }
                        toolTag.put("AttributeModifiers", newModTag);
                    }
                    tool.addAttributeModifier(modifier.name, modifier.modifier, modifier.slot);
                    stackToSpawn = tool.copy();
                } else if (target.getType() == FusionType.UPGRADE_ITEM) {
                    stackToSpawn = new ItemStack(target.getItemTarget(), 1);
                } else {
                    throw new AssertionError("Should not happen");
                }

                ItemScatterer.spawn(world, pos.getX(), pos.getY()+1, pos.getZ(), stackToSpawn);
                tool = ItemStack.EMPTY;
            }

            // Clear items
            slot1 = null;
            slot2 = null;
            slot3 = null;
            this.markDirty();
            this.sync();
        }
    }

    public void dropItems() {
        List<ItemStack> items = new ArrayList<>();
        if (slot1 != null) {
            items.add(new ItemStack(slot1, 1));
        }
        if (slot2 != null) {
            items.add(new ItemStack(slot2, 1));
        }
        if (slot3 != null) {
            items.add(new ItemStack(slot3, 1));
        }
        if (!tool.isEmpty()) {
            items.add(tool);
        }

        for (ItemStack stack : items)
            ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }

    @Override
    public CompoundTag toInitialChunkDataTag() {
        return this.toTag(new CompoundTag());
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
    	super.toTag(tag);
    	tag.putString("Slot1", Registry.ITEM.getId(slot1).toString());
    	tag.putString("Slot2", Registry.ITEM.getId(slot2).toString());
    	tag.putString("Slot3", Registry.ITEM.getId(slot3).toString());
    	tag.put("Tool", tool.toTag(new CompoundTag()));
    	return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
    	super.fromTag(tag);
    	if(tag.contains("Slot1", 8)) {
    		slot1 = Registry.ITEM.get(new Identifier(tag.getString("Slot1")));
    	}
    	if(tag.contains("Slot2", 8)) {
    		slot2 = Registry.ITEM.get(new Identifier(tag.getString("Slot2")));
    	}
    	if(tag.contains("Slot3", 8)) {
    		slot3 = Registry.ITEM.get(new Identifier(tag.getString("Slot3")));
    	}
    	if(tag.contains("Tool", 10)) {
    		tool = ItemStack.fromTag(tag.getCompound("Tool"));
    	} else {
    		tool = ItemStack.EMPTY;
    	}
    }

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		return toTag(tag);
	}

    public void cleanAir() {
        if (slot1 == Items.AIR) {
            slot1 = null;
        }
        if (slot2 == Items.AIR) {
            slot2 = null;
        }
        if (slot3 == Items.AIR) {
            slot3 = null;
        }
    }
}
