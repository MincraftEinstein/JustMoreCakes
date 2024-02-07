package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModClientConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LavaCandleCakeBlock extends BaseCandleCakeBlock {

    public LavaCandleCakeBlock(BaseCakeBlock originalCake, Block candle, Properties properties) {
        super(originalCake, candle, properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity livingEntity && !EnchantmentHelper.hasFrostWalker(livingEntity)) {
            livingEntity.hurt(level.damageSources().hotFloor(), 1);
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (ModClientConfigs.LAVA_CAKE_PARTICLES.get()) {
            if (random.nextInt(10) == 0) {
                double x = pos.getX() + random.nextDouble();
                double y = pos.getY() + 1;
                double z = pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.LAVA, x, y, z, 0, 0, 0);
            }
        }
    }
}
