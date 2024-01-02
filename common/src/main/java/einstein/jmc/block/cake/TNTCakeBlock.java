package einstein.jmc.block.cake;

import einstein.jmc.block.entity.TNTCakeBlockEntity;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class TNTCakeBlock extends BaseEntityCakeBlock {

    public TNTCakeBlock(CakeBuilder builder) {
        super(builder);
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        InteractionResult result = super.eat(level, pos, state, player);
        explode(level, pos);
        return result;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        explodeIfAllowed(level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        explodeIfAllowed(level, pos);
    }

    public static void explodeIfAllowed(Level level, BlockPos pos) {
        if (ModCommonConfigs.EFFECTED_BY_REDSTONE.get() && level.hasNeighborSignal(pos)) {
            explode(level, pos);
        }
    }

    public static void explode(Level level, BlockPos pos) {
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
