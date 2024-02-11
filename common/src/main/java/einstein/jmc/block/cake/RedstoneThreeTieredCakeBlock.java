package einstein.jmc.block.cake;

import einstein.jmc.util.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

@SuppressWarnings("deprecation")
public class RedstoneThreeTieredCakeBlock extends BaseThreeTieredCakeBlock {

    public RedstoneThreeTieredCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == UPPER) {
            Level level = player.level();
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (belowState.is(this) && belowState.getValue(HALF) == LOWER) {
                level.blockUpdated(belowPos, belowState.getBlock());
            }
        }
        return super.eatActions(player, pos, state);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = getter.getBlockState(abovePos);
        int slices = getSlices();

        if (aboveState.is(this) && aboveState.getValue(HALF) == UPPER) {
            return slices - aboveState.getValue(getBites());
        }

        return slices - (state.getValue(getBites()) - 1);
    }
}
