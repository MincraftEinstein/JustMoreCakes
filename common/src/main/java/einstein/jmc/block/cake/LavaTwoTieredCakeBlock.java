package einstein.jmc.block.cake;

import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LavaTwoTieredCakeBlock extends BaseTwoTieredCakeBlock {

    public LavaTwoTieredCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.fireImmune() && entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(level.damageSources().hotFloor(), 1);
        }

        super.stepOn(level, pos, state, entity);
    }
}
