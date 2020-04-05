package io.teamblue.composing;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

import io.teamblue.composing.client.ComposingTableBlockEntityRenderer;

public class ComposingClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(Composing.COMPOSING_TABLE_BLOCK_ENTITY_TYPE, ComposingTableBlockEntityRenderer::new);
	}

}
