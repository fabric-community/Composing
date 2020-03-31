package io.teamblue.composing;

import io.teamblue.composing.blockentity.ComposingTableBlockEntity;
import net.fabricmc.api.ModInitializer;

import dev.emi.trinkets.api.ITrinket;
import io.teamblue.composing.api.CrystalHolder;
import io.teamblue.composing.api.SlotType;
import io.teamblue.composing.impl.CrystalHolderImpl;
import io.teamblue.composing.item.ComposingItems;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Composing implements ModInitializer {
	public static final String MODID = "composing";
	public static final Logger logger = LogManager.getLogger();
	public static final ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier(Composing.MODID, "tab"), () -> new ItemStack(ComposingItems.BLESSING_STONE));

	public static final ComponentType<CrystalHolder> CRYSTAL_SLOT_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "crystal_slot"), CrystalHolder.class);

	public static final Block COMPOSING_TABLE = null;

	public static final BlockEntityType<ComposingTableBlockEntity> COMPOSING_TABLE_BLOCK_ENTITY_TYPE = Registry.register(
			Registry.BLOCK_ENTITY_TYPE,
			new Identifier(Composing.MODID, "table_type"),
			BlockEntityType.Builder.create(ComposingTableBlockEntity::new, COMPOSING_TABLE).build(null)
	);

	@Override
	public void onInitialize() {
		ComposingItems.init();
		ItemComponentCallback.event(null).register((stack, container) -> {
			if (stack.getItem() instanceof ToolItem) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalHolderImpl(SlotType.WEAPON));
			} else if (stack.getItem() instanceof ArmorItem) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalHolderImpl(SlotType.ARMOR));
			} else if (stack.getItem() instanceof ITrinket) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalHolderImpl(SlotType.ACCESSORY));
			}
		});
	}
}
