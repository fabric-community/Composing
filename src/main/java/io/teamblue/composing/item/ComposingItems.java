package io.teamblue.composing.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import io.teamblue.composing.Composing;
import static io.teamblue.composing.api.CrystalElement.*;

public class ComposingItems {

	public static final CrystalItem SMALL_FIRE_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), FIRE, 0), "small_fire_crystal");
	public static final CrystalItem MEDIUM_FIRE_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), FIRE, 1), "medium_fire_crystal");
	public static final CrystalItem LARGE_FIRE_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), FIRE, 2), "large_fire_crystal");
	
	public static final CrystalItem SMALL_EARTH_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), EARTH, 0), "small_earth_crystal");
	public static final CrystalItem MEDIUM_EARTH_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), EARTH, 1), "medium_earth_crystal");
	public static final CrystalItem LARGE_EARTH_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), EARTH, 2), "large_earth_crystal");
	
	public static final CrystalItem SMALL_WIND_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WIND, 0), "small_wind_crystal");
	public static final CrystalItem MEDIUM_WIND_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WIND, 1), "medium_wind_crystal");
	public static final CrystalItem LARGE_WIND_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WIND, 2), "large_wind_crystal");
	
	public static final CrystalItem SMALL_WATER_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WATER, 0), "small_water_crystal");
	public static final CrystalItem MEDIUM_WATER_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WATER, 1), "medium_water_crystal");
	public static final CrystalItem LARGE_WATER_CRYSTAL = register(new CrystalItem(new Item.Settings().group(Composing.ITEMGROUP), WATER, 2), "large_water_crystal");
	
	public static final StoneItem BLESSING_STONE = register(new StoneItem(new Item.Settings().group(Composing.ITEMGROUP), 0), "blessing_stone");
	public static final StoneItem SOUL_STONE = register(new StoneItem(new Item.Settings().group(Composing.ITEMGROUP), 0), "soul_stone");
	public static final StoneItem HOLY_STONE = register(new StoneItem(new Item.Settings().group(Composing.ITEMGROUP), 0), "holy_stone");
	
	private static <T extends Item> T register(T item, String id) {
		return Registry.register(Registry.ITEM, new Identifier(Composing.MODID, id), item);
	}
	
	public static void init() { }
}
