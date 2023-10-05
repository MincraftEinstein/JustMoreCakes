package einstein.jmc.blocks;

import einstein.jmc.blockentity.CakeStandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CakeStandBlock extends BaseEntityBlock {

    public CakeStandBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
                    if (!player.isCreative()) {
                        stack.shrink(1);
                    }

                    cakeStandBlockEntity.setStoredBlock(blockItem.getBlock());
                    return InteractionResult.SUCCESS;
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

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CakeStandBlockEntity(pos, state);
    }
}
