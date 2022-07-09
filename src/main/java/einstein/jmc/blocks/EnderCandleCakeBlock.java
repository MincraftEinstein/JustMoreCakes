package einstein.jmc.blocks;

import java.util.Random;

import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCandleCakeBlock extends BaseCandleCakeBlock {

	public EnderCandleCakeBlock(Block candle, BaseCakeBlock originalCake, BlockBehaviour.Properties properties) {
		super(candle, originalCake, properties);
	}
	
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    	if (ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
	       for (int i = 0; i < 3; ++i) {
	           int j = rand.nextInt(2) * 2 - 1;
	           int k = rand.nextInt(2) * 2 - 1;
	           double d0 = pos.getX() + 0.5D + 0.25D * j;
	           double d1 = (pos.getY() + rand.nextFloat());
	           double d2 = pos.getZ() + 0.5D + 0.25D * k;
	           double d3 = (rand.nextFloat() * j);
	           double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
	           double d5 = (rand.nextFloat() * k);
	           level.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
	        }
    	}
    }
}
