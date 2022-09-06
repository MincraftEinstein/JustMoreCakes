package einstein.jmc.blocks;

import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCandleCakeBlock extends BaseCandleCakeBlock {

	public EnderCandleCakeBlock(BaseCakeBlock originalCake, BlockBehaviour.Properties properties) {
		super(originalCake, properties);
	}
	
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
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
