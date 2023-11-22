package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class RedstoneCandleCakeBlock extends BaseCandleCakeBlock {

    public RedstoneCandleCakeBlock(BaseCakeBlock originalCake, Properties properties) {
        super(originalCake, properties);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return 7;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);
        if (ModClientConfigs.REDSTONE_CAKE_PARTICLES.get()) {
            for (int i = 0; i < 2; ++i) {
                double x = pos.getX() + rand.nextDouble();
                double y = pos.getY() + rand.nextDouble() * 0.5D + 0.25D;
                double z = pos.getZ() + rand.nextDouble();
                level.addParticle(DustParticleOptions.REDSTONE, x, y, z, 0, 0, 0);
            }
        }
    }
}
