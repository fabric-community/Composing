package io.teamblue.composing;

import net.fabricmc.api.ModInitializer;

import dev.emi.trinkets.api.ITrinket;
import io.teamblue.composing.api.CrystalSlotComponent;
import io.teamblue.composing.api.SlotType;
import io.teamblue.composing.impl.CrystalSlotComponentImpl;
import io.teamblue.composing.item.ComposingItems;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.ItemComponentCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Composing implements ModInitializer {
	public static final String MODID = "composing";
	public static final Logger logger = LogManager.getLogger();
	public static final ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier(Composing.MODID, "tab"), () -> new ItemStack(ComposingItems.BLESSING_STONE));

	public static final ComponentType<CrystalSlotComponent> CRYSTAL_SLOT_COMPONENT = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(MODID, "crystal_slot"), CrystalSlotComponent.class);

	@Override
	public void onInitialize() {
		ComposingItems.init();
		ItemComponentCallback.event(null).register((stack, container) -> {
			if (stack.getItem() instanceof ToolItem) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalSlotComponentImpl(SlotType.WEAPON));
			} else if (stack.getItem() instanceof ArmorItem) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalSlotComponentImpl(SlotType.ARMOR));
			} else if (stack.getItem() instanceof ITrinket) {
				container.put(CRYSTAL_SLOT_COMPONENT, new CrystalSlotComponentImpl(SlotType.ACCESSORY));
			}
		});
	}
}
