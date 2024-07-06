package einstein.jmc.block;

import com.mojang.serialization.MapCodec;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CeramicBowlBlock extends BaseEntityBlock {

    private static final MapCodec<CeramicBowlBlock> CODEC = simpleCodec(CeramicBowlBlock::new);
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(3, 0, 3, 13, 1, 13),
            Block.box(2, 0, 2, 14, 9, 3),
            Block.box(2, 0, 13, 14, 9, 14),
            Block.box(2, 0, 3, 3, 9, 13),
            Block.box(13, 0, 3, 14, 9, 13)
    );
    public static final IntegerProperty FILL_LEVEL = IntegerProperty.create("fill_level", 0, 4);

    public CeramicBowlBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FILL_LEVEL, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CeramicBowlBlockEntity ceramicBowlBlockEntity) {
            if (level.isClientSide) {
                return ItemInteractionResult.CONSUME;
            }

            if (stack.is(ModItems.WHISK.get())) {
                if (ceramicBowlBlockEntity.tryCraft(player)) {
                    stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                    return ItemInteractionResult.SUCCESS;
                }
            }
            else if (ceramicBowlBlockEntity.addItem(player, player.isCreative() ? stack.copy() : stack)) {
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CeramicBowlBlockEntity ceramicBowlBlockEntity) {
            if (level.isClientSide) {
                return InteractionResult.CONSUME;
            }

            if (state.getValue(FILL_LEVEL) == 4) {
                return ceramicBowlBlockEntity.takeResult(player) ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }

            if (ceramicBowlBlockEntity.takeItem(player)) {
                return InteractionResult.SUCCESS;
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CeramicBowlBlockEntity ceramicBowlBlockEntity) {
                if (level instanceof ServerLevel) {
                    ceramicBowlBlockEntity.dropItems(level, pos);
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FILL_LEVEL);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CeramicBowlBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
