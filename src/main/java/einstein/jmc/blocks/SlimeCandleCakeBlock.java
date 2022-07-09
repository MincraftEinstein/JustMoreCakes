package einstein.jmc.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlimeCandleCakeBlock extends BaseCandleCakeBlock {

    public SlimeCandleCakeBlock(Block block, BaseCakeBlock originalCake, BlockBehaviour.Properties properties) {
        super(block, originalCake, properties);
    }
    
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float f) {
		if (entity.isSuppressingBounce()) {
			super.fallOn(level, state, pos, entity, f);
		} else {
			entity.causeFallDamage(f, 0, DamageSource.FALL);
		}
	}
	
	public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(getter, entity);
		} else {
			this.bounceUp(entity);
		}
	}
	
	private void bounceUp(Entity entity) {
		Vec3 vec3 = entity.getDeltaMovement();
		if (vec3.y < 0) {
			double d0 = entity instanceof LivingEntity ? 1 : 0.8D;
			entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
		}
	}
	
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		double d0 = Math.abs(entity.getDeltaMovement().y);
		if (d0 < 0.1D && !entity.isSteppingCarefully()) {
			double d1 = 0.4D + d0 * 0.2D;
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1, d1));
		}
		super.stepOn(level, pos, state, entity);
	}
}
