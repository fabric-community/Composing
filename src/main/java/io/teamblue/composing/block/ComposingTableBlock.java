package io.teamblue.composing.block;

import dev.emi.trinkets.api.ITrinket;
import io.teamblue.composing.blockentity.ComposingTableBlockEntity;
import io.teamblue.composing.item.CrystalItem;
import io.teamblue.composing.item.StoneItem;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class ComposingTableBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;

    private double[] slot1Area = new double[] { 0, 0.33, 0.33, 0.66 };
    private double[] slot2Area = new double[] { 0.33, 0, 0.66, 0.33 };
    private double[] slot3Area = new double[] { 0.66, 0.33, 1, 0.66};
    private double[] centerArea = new double[] { 0.33, 0.33, 0.66, 0.66 };

    protected ComposingTableBlock() {
        super(Settings.of(Material.STONE).strength(2.0f, 6.0f));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ComposingTableBlockEntity();
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            ((ComposingTableBlockEntity) world.getBlockEntity(pos)).dropItems();
            super.onBlockRemoved(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            if (hit.getSide() == Direction.UP) {
                return ActionResult.PASS;
            } else {
                return ActionResult.FAIL;
            }
        }

        if (hit.getSide() == Direction.UP) {

            Vec3d hitPos = hit.getPos().subtract(new Vec3d(hit.getBlockPos()));

            switch (state.get(FACING)) {
                case WEST:
                    hitPos = new Vec3d(1 - hitPos.getZ(), hitPos.getY(), hitPos.getX());
                    break;
                case SOUTH:
                    hitPos = new Vec3d(1 - hitPos.getX(), hitPos.getY(), 1 - hitPos.getZ());
                    break;
                case EAST:
                    hitPos = new Vec3d(hitPos.getZ(), hitPos.getY(), 1 - hitPos.getX());
                    break;
            }

            ComposingTableBlockEntity be = (ComposingTableBlockEntity) world.getBlockEntity(pos);
            be.cleanAir();
            ItemStack playerItemStack = (hand == Hand.MAIN_HAND) ? player.getMainHandStack() : player.getOffHandStack();

            if (centerArea[0] < hitPos.getX() && hitPos.getX() < centerArea[2] && centerArea[1] < hitPos.getZ() &&  hitPos.getZ() < centerArea[3]) {
                // Center slot
                if (player.isSneaking()) {
                    if (!be.tool.isEmpty()) {
                        // Remove item
                        ItemEntity e = new ItemEntity(world, hitPos.getX() + hit.getBlockPos().getX(), pos.getY() + hit.getBlockPos().getY(), pos.getZ() + hit.getBlockPos().getZ(), be.tool.copy());
                        //e.setVelocity(0, 1, 0);
                        world.spawnEntity(e);
                        be.tool = ItemStack.EMPTY;
                    }
                } else  {
                    if (be.tool.isEmpty() && validComposeItem(playerItemStack)) {
                        // Remove item
                        be.tool = playerItemStack.split(1);
                    }
                }
            } else if (slot1Area[0] < hitPos.getX() && hitPos.getX() < slot1Area[2] && slot1Area[1] < hitPos.getZ() &&  hitPos.getZ() < slot1Area[3]) {
                // Center slot
                if (player.isSneaking()) {
                    if (be.slot1 != null) {
                        // Remove item
                        ItemEntity e = new ItemEntity(world, hitPos.getX() + hit.getBlockPos().getX(), pos.getY() + hit.getBlockPos().getY(), pos.getZ() + hit.getBlockPos().getZ(), new ItemStack(be.slot1));
                        e.setVelocity(0, .1, 0);
                        world.spawnEntity(e);
                        be.slot1 = null;
                    }
                } else {
                    // Add item
                    if (be.slot1 == null && validComposeUtil(playerItemStack)) {
                        // Remove item
                        be.slot1 = playerItemStack.split(1).getItem();
                    }
                }
            } else if (slot2Area[0] < hitPos.getX() && hitPos.getX() < slot2Area[2] && slot2Area[1] < hitPos.getZ() &&  hitPos.getZ() < slot2Area[3]) {
                // Center slot
                if (player.isSneaking()) {
                    if (be.slot2 != null) {
                        // Remove item
                        ItemEntity e = new ItemEntity(world, hitPos.getX() + hit.getBlockPos().getX(), pos.getY() + hit.getBlockPos().getY(), pos.getZ() + hit.getBlockPos().getZ(), new ItemStack(be.slot2));
                        e.setVelocity(0, .1, 0);
                        world.spawnEntity(e);
                        be.slot2 = null;
                    }
                } else {
                    // Add item
                    if (be.slot2 == null && validComposeUtil(playerItemStack)) {
                        // Remove item
                        be.slot2 = playerItemStack.split(1).getItem();
                    }
                }
            } else if (slot3Area[0] < hitPos.getX() && hitPos.getX() < slot3Area[2] && slot3Area[1] < hitPos.getZ() &&  hitPos.getZ() < slot3Area[3]) {
                // Center slot
                if (player.isSneaking()) {
                    if (be.slot3 != null) {
                        // Remove item
                        ItemEntity e = new ItemEntity(world, hitPos.getX() + hit.getBlockPos().getX(), pos.getY() + hit.getBlockPos().getY(), pos.getZ() + hit.getBlockPos().getZ(), new ItemStack(be.slot3));
                        e.setVelocity(0, .1, 0);
                        world.spawnEntity(e);
                        be.slot3 = null;
                    }
                } else {
                    // Add item
                    if (be.slot3 == null && validComposeUtil(playerItemStack)) {
                        // Remove item
                        be.slot3 = playerItemStack.split(1).getItem();
                    }
                }
            } else {
                return ActionResult.PASS;
            }
            be.markDirty();
            be.sync();
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (((World)world).isReceivingRedstonePower(pos)) {
            ComposingTableBlockEntity be = (ComposingTableBlockEntity) world.getBlockEntity(pos);
            be.craft();
        }
        return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
    }

    private boolean validComposeUtil(ItemStack playerItemStack) {
        Item i = playerItemStack.getItem();
        return i instanceof StoneItem || i instanceof CrystalItem;
    }

    private boolean validComposeItem(ItemStack stack) {
        Item i = stack.getItem();
        return i instanceof ToolItem || i instanceof RangedWeaponItem || i instanceof TridentItem || i instanceof ArmorItem || i instanceof ITrinket;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
    	return BlockRenderType.MODEL;
    }

    // Rotation utilities

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
}
