package io.teamblue.composing;

import io.teamblue.composing.block.ComposingBlocks;
import io.teamblue.composing.blockentity.ComposingTableBlockEntity;
import io.teamblue.composing.item.ComposingItems;
import io.teamblue.composing.recipe.ComposingRecipes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

public class Composing implements ModInitializer {
	public static final String MODID = "composing";
	public static final Logger logger = LogManager.getLogger();
	public static final ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier(Composing.MODID, "tab"), () -> new ItemStack(ComposingItems.BLESSING_STONE));


	public static final BlockEntityType<ComposingTableBlockEntity> COMPOSING_TABLE_BLOCK_ENTITY_TYPE = Registry.register(
			Registry.BLOCK_ENTITY_TYPE,
			new Identifier(Composing.MODID, "table_type"),
			BlockEntityType.Builder.create(ComposingTableBlockEntity::new, ComposingBlocks.COMPOSING_TABLE).build(null)
	);

	@Override
	public void onInitialize() {
		ComposingItems.init();
		ComposingRecipes.init();
		ComposingBlocks.init();
	}
}
