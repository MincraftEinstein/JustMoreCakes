package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.entity.GlowstoneCakeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlowstoneCandleTwoTieredCakeBlock extends BaseTwoTieredCandleCakeBlock implements EntityBlock {

    public GlowstoneCandleTwoTieredCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(parentCake, candle, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlowstoneCakeBlockEntity(pos, state);
    }
}
