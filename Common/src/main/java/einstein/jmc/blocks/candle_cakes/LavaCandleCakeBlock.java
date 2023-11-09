package einstein.jmc.blocks.candle_cakes;

import einstein.jmc.blocks.cakes.BaseCakeBlock;
import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LavaCandleCakeBlock extends BaseCandleCakeBlock {

    public LavaCandleCakeBlock(BaseCakeBlock originalCake, Properties properties) {
        super(originalCake, properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            livingEntity.hurt(level.damageSources().hotFloor(), 1);
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);
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
