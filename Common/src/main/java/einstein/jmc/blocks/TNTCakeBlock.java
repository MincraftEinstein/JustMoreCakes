package einstein.jmc.blocks;

import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TNTCakeBlock extends BaseEntityCakeBlock {

    public TNTCakeBlock(CakeBuilder builder) {
        super(builder);
    }

    @Override
    public void eatActions(Player player, BlockPos pos, BlockState state) {
        explode(player.getCommandSenderWorld(), pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos oldPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            explodeIfAllowed(level, pos);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            explodeIfAllowed(level, pos);
        }
    }

    private void explodeIfAllowed(Level level, BlockPos pos) {
        if (ModCommonConfigs.EFFECTED_BY_REDSTONE.get()) {
            explode(level, pos);
        }
    }

    private void explode(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TNTCakeBlockEntity tntCakeBlockEntity) {
            tntCakeBlockEntity.explode();
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TNTCakeBlockEntity(pos, state);
    }
}
