package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.entity.GlowstoneCakeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlowstoneCandleCakeBlock extends BaseCandleCakeBlock implements EntityBlock {

    public GlowstoneCandleCakeBlock(BaseCakeBlock originalCake, Block candle, Properties properties) {
        super(originalCake, candle, properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlowstoneCakeBlockEntity(pos, state);
    }
}
