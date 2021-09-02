package einstein.jmc.effects;

import einstein.jmc.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FreezingEffect extends InstantenousMobEffect {

	public FreezingEffect(MobEffectCategory type, int liquidColor) {
		super(type, liquidColor);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entityLiving, int amplifier, double health) {
		freezeEntity(entityLiving);	
	}
	
	public static void freezeEntity(LivingEntity entityLiving) {
		if (entityLiving instanceof Player && (entityLiving).isSpectator()) {
			return;
		}
		AABB boundingBox = entityLiving.getBoundingBox().inflate(1);
		final Level level = entityLiving.getCommandSenderWorld();
		Block block = ModBlocks.ENCASING_ICE;
		final BlockState state = block.defaultBlockState();
		for (int x = (int) Math.floor(boundingBox.minX); x < Math.ceil(boundingBox.maxX); x++) {
			for (int y = (int) Math.floor(boundingBox.minY); y < Math.ceil(boundingBox.maxY); y++) {
				for (int z = (int) Math.floor(boundingBox.minZ); z < Math.ceil(boundingBox.maxZ); z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (entityLiving.getCommandSenderWorld().isEmptyBlock(pos)) {
						level.setBlockAndUpdate(pos, state);
					}
				}
			}
		}
	}
}
