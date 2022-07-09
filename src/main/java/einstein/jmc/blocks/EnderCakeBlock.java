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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCakeBlock extends BaseCakeBlock {

    public EnderCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
	@Override
	public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        Util.teleportRandomly(player, ModServerConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
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
