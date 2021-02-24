package einstein.jmc.init;

import java.util.Random;

import einstein.einsteins_library.blocks.CakeBlockBase;
import net.minecraft.block.BlockState;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RedstoneCake extends CakeBlockBase {

	public RedstoneCake(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		for(int i = 0; i < 2; ++i) {
	        double d0 = pos.getX() + rand.nextDouble();
	        double d1 = pos.getY() + rand.nextDouble() * 0.5D + 0.25D;
	        double d2 = pos.getZ() + rand.nextDouble();
			worldIn.addParticle(RedstoneParticleData.REDSTONE_DUST, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}
}
