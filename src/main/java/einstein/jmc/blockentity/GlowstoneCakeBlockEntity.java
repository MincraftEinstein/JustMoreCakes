package einstein.jmc.blockentity;

import java.util.List;

import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.init.ModTileEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class GlowstoneCakeBlockEntity extends BlockEntity {

	public GlowstoneCakeBlockEntity(BlockPos pos, BlockState state) {
		super(ModTileEntityType.GLOWSTONE_CAKE, pos, state);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		return super.save(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
	}

	public void setGlowing() {
		if (!this.level.isClientSide) {
			final double d0 = 10 + 10;
			final AABB aabb = new AABB(getBlockPos().below()).inflate(d0).expandTowards(0.0F, level.getHeight(), 0.0F);
			final List<Mob> mobList = level.getEntitiesOfClass(Mob.class, aabb);
			for (final Mob mob : mobList) {
				mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, ModServerConfigs.GLOWSTONE_CAKE_GLOWING_DUR.get(), 0, true, true));
			}
		}
	}
}
