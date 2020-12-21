package einstein.jmc.tileentity;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class TNTCakeTileEntity extends TileEntity {

	public TNTCakeTileEntity() {
		super(ModTileEntityType.TNT_CAKE.get());
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return super.write(compound);
	}

	@Override
	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
	}

	public void explode() {
		Actions.createExplosion(getWorld(), getPos(), 5);
	}

}
