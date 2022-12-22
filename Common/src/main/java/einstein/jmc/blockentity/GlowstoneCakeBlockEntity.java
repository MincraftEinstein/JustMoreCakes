package einstein.jmc.blockentity;

import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class GlowstoneCakeBlockEntity extends BlockEntity {

	public GlowstoneCakeBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.GLOWSTONE_CAKE.get(), pos, state);
	}
	
	public void setGlowing() {
		if (!level.isClientSide) {
			double boundingBox = 10 + 10;
			AABB aabb = new AABB(getBlockPos().below()).inflate(boundingBox).expandTowards(0, level.getHeight(), 0);
			List<Mob> mobList = level.getEntitiesOfClass(Mob.class, aabb);
			for (Mob mob : mobList) {
				mob.addEffect(new MobEffectInstance(MobEffects.GLOWING, ModServerConfigs.GLOWSTONE_CAKE_GLOWING_DUR.get(), 0, true, true));
			}
		}
	}
}
