package einstein.jmc.effects;

import einstein.jmc.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.InstantEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FreezingEffect extends InstantEffect {
	
	public FreezingEffect(EffectType type, int liquidColor) {
		super(type, liquidColor);
	}
	
	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}
	
	@Override
	public void affectEntity(Entity source, Entity indirectSource, LivingEntity entityLiving, int amplifier, double health) {
		FreezingEffect.freezeEntity(entityLiving);
	}
	
	public static void freezeEntity(LivingEntity entityLiving) {
		if (entityLiving instanceof PlayerEntity && (entityLiving).isSpectator()) return;
		AxisAlignedBB boundingBox = entityLiving.getBoundingBox().grow(1);
		final World world = entityLiving.getEntityWorld() ;
		Block block = ModBlocks.ENCASING_ICE.getBlock();
		final BlockState state = block.getDefaultState();
		for (int x = (int) Math.floor(boundingBox.minX); x < Math.ceil(boundingBox.maxX); x++) {
			for (int y = (int) Math.floor(boundingBox.minY); y < Math.ceil(boundingBox.maxY); y++) {
				for (int z = (int) Math.floor(boundingBox.minZ); z < Math.ceil(boundingBox.maxZ); z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (entityLiving.getEntityWorld().isAirBlock(pos)) {
	                     world.setBlockState(pos, state);
					}
				}
			}
		}
	}
}
