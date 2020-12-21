package einstein.jmc.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BreakableBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class EncasingIceBlock extends BreakableBlock {

	public EncasingIceBlock(AbstractBlock.Properties properties) {
		super(properties);
	}

	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		return;
	}

	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		if (timeGoneBy(worldIn, 1)) {
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
		}

	}

	@Override
	public PushReaction getPushReaction(BlockState state) {
		return PushReaction.NORMAL;
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		entityIn.setSprinting(false);
		entityIn.setMotionMultiplier(state, new Vector3d(0.001D, 0.001D, 0.001D));
	}

	private static boolean timeGoneBy(World world, int ticks) {
		if (ticks == 0)
			return true;
		return world.getGameTime() % (ticks) == 0;
	}

}
