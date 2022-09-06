package einstein.jmc.blocks;

import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TNTCakeBlock extends BaseEntityCakeBlock {

    public TNTCakeBlock(Properties properties) {
        super(properties);
    }

	@Override
	public void eatActions(Player player, BlockPos pos, BlockState state) {
		super.eatActions(player, pos, state);
		explode(player.getCommandSenderWorld(), pos);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos oldPos, boolean isMoving) {
		if (level.hasNeighborSignal(pos)) {
			explodeIfAllowed(level, pos);
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (level.hasNeighborSignal(pos)) {
			explodeIfAllowed(level, pos);
		}
	}

	private void explodeIfAllowed(Level level, BlockPos pos) {
		if (ModServerConfigs.EFFECTED_BY_REDSTONE.get()) {
			explode(level, pos);
		}
	}

	private void explode(Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof TNTCakeBlockEntity tntCakeBlockEntity) {
			tntCakeBlockEntity.explode();
		}
	}
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TNTCakeBlockEntity(pos, state);
	}
}
