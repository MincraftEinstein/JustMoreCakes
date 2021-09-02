package einstein.jmc.blockentity;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModTileEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TNTCakeBlockEntity extends BlockEntity {

	public TNTCakeBlockEntity(BlockPos pos, BlockState state) {
		super(ModTileEntityType.TNT_CAKE, pos, state);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		return super.save(compound);
	}

	@Override
	public void load(CompoundTag compound) {
		super.load(compound);
	}

	public void explode() {
		Actions.createExplosion(getLevel(), getBlockPos(), 5);
	}
}
