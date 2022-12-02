package einstein.jmc.blocks;

import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlimeCakeBlock extends BaseCakeBlock {

    public SlimeCakeBlock(CakeBuilder builder) {
        super(builder);
    }
    
    @Override
    public void eatActions(Player player, BlockPos pos, BlockState state) {
		super.eatActions(player, pos, state);
        player.addEffect(new MobEffectInstance(MobEffects.JUMP, ModServerConfigs.SLIME_CAKE_JUMP_BOOST_DUR.get(), ModServerConfigs.SLIME_CAKE_JUMP_BOOST_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ModServerConfigs.SLIME_CAKE_RES_DUR.get(), ModServerConfigs.SLIME_CAKE_RES_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(ModPotions.BOUNCING_EFFECT.get(), ModServerConfigs.SLIME_CAKE_BOUNCING_DUR.get(), ModServerConfigs.SLIME_CAKE_BOUNCING_STRENGTH.get()));
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
		if (entity.isSuppressingBounce()) {
			super.fallOn(level, state, pos, entity, fallDistance);
		} else {
			entity.causeFallDamage(fallDistance, 0, DamageSource.FALL);
		}
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter getter, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(getter, entity);
		} else {
			bounceUp(entity);
		}
	}
	
	static void bounceUp(Entity entity) {
		Vec3 vec3 = entity.getDeltaMovement();
		if (vec3.y < 0) {
			double d0 = entity instanceof LivingEntity ? 0.5 : 0.3D;
			entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		double d0 = Math.abs(entity.getDeltaMovement().y);
		if (d0 < 0.1D && !entity.isSteppingCarefully()) {
			double d1 = 0.4D + d0 * 0.2D;
			entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1, d1));
		}
		super.stepOn(level, pos, state, entity);
	}
}
