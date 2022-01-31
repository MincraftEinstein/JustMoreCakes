package einstein.jmc.blocks;

import java.util.Random;

import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class LavaCandleCakeBlock extends BaseCandleCakeBlock {

	public LavaCandleCakeBlock(Block candle, BaseCakeBlock originalCake, BlockBehaviour.Properties properties) {
		super(candle, originalCake, properties);
	}
	
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(DamageSource.HOT_FLOOR, 1.0F);
        }
        super.stepOn(level, pos, state, entity);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
    	if (ModClientConfigs.LAVA_CAKE_PARTICLES.get()) {
        	if (rand.nextInt(10) == 0) {
    	        double d0 = pos.getX() + rand.nextDouble();
    	        double d1 = pos.getY() + 1.0D;
    	        double d2 = pos.getZ() + rand.nextDouble();
    	    	level.addParticle(ParticleTypes.LAVA, d0, d1, d2, 0, 0, 0);
        	}
    	}
    }
}
