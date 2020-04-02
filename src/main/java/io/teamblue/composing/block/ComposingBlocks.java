package io.teamblue.composing.block;

import io.teamblue.composing.Composing;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ComposingBlocks {
    public static final ComposingTableBlock COMPOSING_TABLE = register(new ComposingTableBlock(), "composing_table");

    private static <T extends Block> T register(T block, String id) {
        return Registry.register(Registry.BLOCK, new Identifier(Composing.MODID, id), block);
    }

    public static void init() { }
}
