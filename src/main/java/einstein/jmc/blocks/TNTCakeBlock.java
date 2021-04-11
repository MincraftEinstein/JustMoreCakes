package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.tileentity.TNTCakeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class TNTCakeBlock extends ContainerCakeBlock {

	public TNTCakeBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
		if (!playerIn.canEat(false)) {
			return ActionResultType.PASS;
		}
		playerIn.addStat(Stats.EAT_CAKE_SLICE);
		playerIn.getFoodStats().addStats(2, 0.1F);
		World world = playerIn.getEntityWorld();
		explode(world, pos);
		final int i = state.get(TNTCakeBlock.BITES);
		if (i < 6) { // Number must be same as BITES
			worldIn.setBlockState(pos, state.with(TNTCakeBlock.BITES, (i + 1)), 3);
		} else {
			worldIn.removeBlock(pos, false);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TNTCakeTileEntity();
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if (worldIn.isBlockPowered(pos)) {
			if (ModServerConfigs.EFFECTED_BY_REDSTONE.get()) {
				explode(worldIn, pos);
			}
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		if (worldIn.isBlockPowered(pos)) {
			if (ModServerConfigs.EFFECTED_BY_REDSTONE.get()) {
				explode(worldIn, pos);
			}
		}
	}

	private void explode(World world, BlockPos pos) {
		TileEntity tileentity = world.getTileEntity(pos);
		((TNTCakeTileEntity) tileentity).explode();
	}
}
