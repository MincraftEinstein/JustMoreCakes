package einstein.jmc.block.cake;

import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class RedstoneCakeBlock extends BaseCakeBlock {

    public RedstoneCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        return 7 - state.getValue(getBites());
    }
}
