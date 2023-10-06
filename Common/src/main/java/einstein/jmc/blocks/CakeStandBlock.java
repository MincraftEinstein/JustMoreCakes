package einstein.jmc.blocks;

import einstein.jmc.blockentity.CakeStandBlockEntity;
import einstein.jmc.data.providers.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CakeStandBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(7, 15, 7, 9, 16, 9),
            Block.box(1, 6, 1, 15, 15, 15),
            Block.box(0, 5, 0, 16, 6, 16),
            Block.box(4, 1, 4, 12, 5, 12),
            Block.box(3, 0, 3, 13, 1, 13)
    );

    public CakeStandBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CakeStandBlockEntity cakeStandBlockEntity) {
            ItemStack stack = player.getItemInHand(hand);
            if (level.isClientSide) {
                return InteractionResult.CONSUME;
            }

            if (cakeStandBlockEntity.isEmpty()) {
                if (!stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
                    Block block = blockItem.getBlock();
                    if (block.defaultBlockState().is(ModBlockTags.CAKE_STAND_STORABLES)) {
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        cakeStandBlockEntity.setStoredBlock(block);
                        return InteractionResult.SUCCESS;
                    }
                }
                return InteractionResult.CONSUME;
            }

            if (!player.isCreative()) {
                Block.popResourceFromFace(level, pos, Direction.UP, new ItemStack(cakeStandBlockEntity.getStoredBlock().asItem()));
            }
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            cakeStandBlockEntity.setStoredBlock(Blocks.AIR);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CakeStandBlockEntity cakeStandBlockEntity && !cakeStandBlockEntity.isEmpty()) {
                Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(cakeStandBlockEntity.getStoredBlock()));
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CakeStandBlockEntity(pos, state);
    }
}
