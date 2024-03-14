package einstein.jmc.block;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CeramicBowlBlock extends BaseEntityBlock {

    public CeramicBowlBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CeramicBowlBlockEntity ceramicBowlBlockEntity) {
            ItemStack stack = player.getItemInHand(hand);

            if (level.isClientSide) {
                return InteractionResult.CONSUME;
            }

            if (stack.isEmpty()) {
                if (ceramicBowlBlockEntity.takeItem(player)) {
                    return InteractionResult.SUCCESS;
                }
            }
            else if (stack.is(Items.IRON_SHOVEL)) { // TODO change to whisk item
                if (ceramicBowlBlockEntity.tryCraft(player)) {
                    stack.hurtAndBreak(1, player, broadcaster -> broadcaster.broadcastBreakEvent(hand));
                    return InteractionResult.SUCCESS;
                }
            }
            else if (ceramicBowlBlockEntity.addItem(player, player.isCreative() ? stack.copy() : stack)) {
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CeramicBowlBlockEntity ceramicBowlBlockEntity) {
                if (level instanceof ServerLevel) {
                    Containers.dropContents(level, pos, ceramicBowlBlockEntity);
                }
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
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
}
