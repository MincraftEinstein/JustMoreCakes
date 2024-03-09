package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.entity.TNTCakeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static einstein.jmc.block.cake.TNTCakeBlock.explodeIfAllowed;

@SuppressWarnings("deprecation")
public class TNTCandleCakeBlock extends BaseCandleCakeBlock implements EntityBlock {

    public TNTCandleCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(parentCake, candle, properties);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        explodeIfAllowed(level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        explodeIfAllowed(level, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TNTCakeBlockEntity(pos, state);
    }
}
