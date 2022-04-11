package einstein.jmc.blockentity;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TNTCakeBlockEntity extends BlockEntity {

	public TNTCakeBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntityTypes.TNT_CAKE.get(), pos, state);
	}
	
	public void explode() {
		Actions.createExplosion(getLevel(), getBlockPos(), 5);
	}
}
