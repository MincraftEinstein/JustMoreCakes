package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EnderCandleCakeBlock extends BaseCandleCakeBlock {

    public EnderCandleCakeBlock(BaseCakeBlock originalCake, Block candle, Properties properties) {
        super(originalCake, candle, properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);
        if (ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
            for (int i = 0; i < 3; ++i) {
                int i1 = rand.nextInt(2) * 2 - 1;
                int i2 = rand.nextInt(2) * 2 - 1;
                double x = pos.getX() + 0.5D + 0.25D * i1;
                double y = (pos.getY() + rand.nextFloat());
                double z = pos.getZ() + 0.5D + 0.25D * i2;
                double XSpeed = (rand.nextFloat() * i1);
                double YSpeed = (rand.nextFloat() - 0.5D) * 0.125D;
                double ZSpeed = (rand.nextFloat() * i2);
                level.addParticle(ParticleTypes.PORTAL, x, y, z, XSpeed, YSpeed, ZSpeed);
            }
        }
    }
}
