package io.teamblue.composing.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Quaternion;

import io.teamblue.composing.blockentity.ComposingTableBlockEntity;

public class ComposingTableBlockEntityRenderer extends BlockEntityRenderer<ComposingTableBlockEntity> {

	// static quaternion because each creation is two calls to the extremely slow java.lang.Math trig functions
	private static final Quaternion MAKE_ITEM_SIDEWAYS = Vector3f.POSITIVE_X.getDegreesQuaternion(90);
	
	public ComposingTableBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(ComposingTableBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		int upLight = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());
		matrices.push();
		matrices.translate(0.5, 33/32d, 0.5); // have to move the item an extra half-voxel up.
		matrices.multiply(MAKE_ITEM_SIDEWAYS);
		matrices.scale(0.3f, 0.3f, 0.3f);
		MinecraftClient.getInstance().getItemRenderer().renderItem(blockEntity.tool, ModelTransformation.Mode.FIXED, upLight, overlay, matrices, vertexConsumers);
		matrices.scale(0.7f, 0.7f, 0.7f);
		matrices.translate(0, 1.6, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(blockEntity.slot1), ModelTransformation.Mode.FIXED, upLight, overlay, matrices, vertexConsumers);
		matrices.translate(-1.6, -3.2, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(blockEntity.slot2), ModelTransformation.Mode.FIXED, upLight, overlay, matrices, vertexConsumers);
		matrices.translate(3.2, 0, 0);
		MinecraftClient.getInstance().getItemRenderer().renderItem(new ItemStack(blockEntity.slot3), ModelTransformation.Mode.FIXED, upLight, overlay, matrices, vertexConsumers);
		matrices.pop();
	}

}
