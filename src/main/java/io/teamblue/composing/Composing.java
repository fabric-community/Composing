package io.teamblue.composing;

import net.fabricmc.api.ModInitializer;

import io.teamblue.composing.item.ComposingItems;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Composing implements ModInitializer {
	public static final String MODID = "composing";
	public static final Logger logger = LogManager.getLogger();
	public static final ItemGroup ITEMGROUP = FabricItemGroupBuilder.build(new Identifier(Composing.MODID, "tab"), () -> new ItemStack(ComposingItems.BLESSING_STONE));

	@Override
	public void onInitialize() {
		ComposingItems.init();
	}
}
