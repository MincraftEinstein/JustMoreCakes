package einstein.jmc.blocks;

import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LavaCakeBlock extends BaseCakeBlock {

    public LavaCakeBlock(Properties properties, CakeBuilder builder) {
        super(properties, builder);
    }
    
    @Override
    public void eatActions(Player player, BlockPos pos, BlockState state) {
		super.eatActions(player, pos, state);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.LAVA_CAKE_REGEN_DUR.get(), ModServerConfigs.LAVA_CAKE_REGEN_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, ModServerConfigs.LAVA_CAKE_STRENGTH_DUR.get(), ModServerConfigs.LAVA_CAKE_STRENGTH_STRENGTH.get()));
	}

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            livingEntity.hurt(DamageSource.HOT_FLOOR, 1);
        }

        super.stepOn(level, pos, state, entity);
    }
    
    @OnlyIn(Dist.CLIENT)
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
