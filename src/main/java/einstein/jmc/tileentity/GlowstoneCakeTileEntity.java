package einstein.jmc.tileentity;

import java.util.List;

import einstein.jmc.init.ModConfigs.ModServerConfigs;
import einstein.jmc.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class GlowstoneCakeTileEntity extends TileEntity {

	public GlowstoneCakeTileEntity() {
		super(ModTileEntityType.GLOWSTONE_CAKE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
	}

	public void setGlowing() {
		if (!this.world.isRemote) {
			final double d0 = 10 + 10;
			final AxisAlignedBB aabb = new AxisAlignedBB(this.getPos().down()).grow(d0).expand(0.0F, this.world.getHeight(), 0.0F);
			final List<MobEntity> mobList = this.world.getEntitiesWithinAABB(MobEntity.class, aabb);
			for (final MobEntity mob : mobList) {
				mob.addPotionEffect(new EffectInstance(Effects.GLOWING, ModServerConfigs.GLOWSTONE_CAKE_GLOWING_DUR.get(), 0, true, true));
			}
		}
	}
}
