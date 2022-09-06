package einstein.jmc.blocks;

import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCakeBlock extends BaseCakeBlock {

    public EnderCakeBlock(Properties properties) {
        super(properties);
    }
    
	@Override
	public void eatActions(Player player, BlockPos pos, BlockState state) {
		super.eatActions(player, pos, state);
        Util.teleportRandomly(player, ModServerConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1, 1);
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
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
	           level.addParticle(ParticleTypes.PORTAL, x, y, z, XSpeed , YSpeed, ZSpeed);
	        }
    	}
    }
}
