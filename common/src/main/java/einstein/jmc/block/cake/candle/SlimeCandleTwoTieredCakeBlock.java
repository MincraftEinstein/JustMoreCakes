package einstein.jmc.block.cake.candle;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("deprecation")
public class SlimeCandleTwoTieredCakeBlock extends BaseTwoTieredCandleCakeBlock {

    public SlimeCandleTwoTieredCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(parentCake, candle, properties);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce() || isAboveCake(pos, entity)) {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
        else {
            entity.causeFallDamage(fallDistance, 0, level.damageSources().fall());
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
        if (entity.isSuppressingBounce() || isAboveCake(entity.getOnPosLegacy(), entity)) {
            super.updateEntityAfterFallOn(getter, entity);
        }
        else {
            Util.bounceUp(entity);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1D && !(entity.isSteppingCarefully() || isAboveCake(pos, entity))) {
            double d1 = 0.4D + d0 * 0.2D;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1, d1));
        }
        super.stepOn(level, pos, state, entity);
    }

    private boolean isAboveCake(BlockPos pos, Entity entity) {
        return entity.getY() - pos.getY() > getCandleHeight();
    }
}
