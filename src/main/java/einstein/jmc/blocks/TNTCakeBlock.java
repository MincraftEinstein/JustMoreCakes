package einstein.jmc.blocks;

import einstein.jmc.blockentity.TNTCakeBlockEntity;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TNTCakeBlock extends BaseEntityCakeBlock
{
    public TNTCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
		player.getFoodData().eat(2, 0.1F);
		Level level = player.getCommandSenderWorld();
		explode(level, pos);
        int i = state.getValue(BITES);
        accessor.gameEvent(player, GameEvent.EAT, pos);
		if (i < 6) { // Number must be same as BITES
			accessor.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
		} else {
			accessor.removeBlock(pos, false);
			accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
		}
		return InteractionResult.SUCCESS;
	}
    
	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (worldIn.hasNeighborSignal(pos)) {
			if (ModServerConfigs.EFFECTED_BY_REDSTONE.get()) {
				explode(worldIn, pos);
			}
		}
	}
	
	@Override
	public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (worldIn.hasNeighborSignal(pos)) {
			if (ModServerConfigs.EFFECTED_BY_REDSTONE.get()) {
				explode(worldIn, pos);
			}
		}
	}
	
	private void explode(Level world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		((TNTCakeBlockEntity) blockEntity).explode();
	}
    
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new TNTCakeBlockEntity(pos, state);
	}
}
