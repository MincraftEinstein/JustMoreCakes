package einstein.jmc.blocks;

import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LavaCakeBlock extends BaseCakeBlock {

    public LavaCakeBlock(CakeBuilder builder) {
        super(builder);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            livingEntity.hurt(DamageSource.HOT_FLOOR, 1);
        }

        super.stepOn(level, pos, state, entity);
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
    	if (ModClientConfigs.LAVA_CAKE_PARTICLES.get()) {
        	if (rand.nextInt(10) == 0) {
    	        double x = pos.getX() + rand.nextDouble();
    	        double y = pos.getY() + 1;
    	        double z = pos.getZ() + rand.nextDouble();
    	    	level.addParticle(ParticleTypes.LAVA, x, y, z, 0, 0, 0);
        	}
    	}
    }
}
