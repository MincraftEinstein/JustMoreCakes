package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.SculkCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;

public class SculkCandleThreeTieredCakeBlock extends BaseThreeTieredCandleCakeBlock {

    public SculkCandleThreeTieredCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(parentCake, candle, properties);
    }

    @Override
    protected void afterEaten(BlockState state, BlockPos pos, Level level, Player player) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        BlockState newState = level.getBlockState(pos);

        if (belowState.is(newState.getBlock()) && belowState.getValue(HALF) == LOWER) {
            SculkCakeBlock.activate(player, level, belowPos, belowState, false);
        }
    }
}
