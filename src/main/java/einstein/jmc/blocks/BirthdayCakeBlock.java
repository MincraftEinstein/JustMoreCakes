package einstein.jmc.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
public class BirthdayCakeBlock extends Block
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 8);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] { 
    		Block.makeCuboidShape(1, 0, 1, 15, 8, 15), //0 uneaten
    		Block.makeCuboidShape(1, 0, 1, 15, 8, 15), //1
    		VoxelShapes.or(Block.makeCuboidShape(14, 0, 1, 15, 8, 2), //2
					Block.makeCuboidShape(1, 0, 1, 8, 8, 15),
					Block.makeCuboidShape(8, 0, 7, 15, 8, 15),
					Block.makeCuboidShape(11, 0, 4, 15, 8, 7),
					Block.makeCuboidShape(9, 0, 6, 11, 8, 7),
					Block.makeCuboidShape(13, 0, 2, 15, 8, 4),
					Block.makeCuboidShape(10, 0, 5, 11, 8, 6),
					Block.makeCuboidShape(12, 0, 3, 13, 8, 4)),
    		VoxelShapes.or(Block.makeCuboidShape(1, 0, 1, 8, 8, 15), //3
    				Block.makeCuboidShape(8, 0, 8, 15, 8, 15)),
    		VoxelShapes.or(Block.makeCuboidShape(14, 0, 14, 15, 8, 15), //4
					Block.makeCuboidShape(1, 0, 1, 8, 8, 15),
					Block.makeCuboidShape(8, 0, 11, 12, 8, 15),
					Block.makeCuboidShape(12, 0, 13, 14, 8, 15),
					Block.makeCuboidShape(8, 0, 9, 10, 8, 11),
					Block.makeCuboidShape(8, 0, 8, 9, 8, 9),
					Block.makeCuboidShape(10, 0, 10, 11, 8, 11),
					Block.makeCuboidShape(12, 0, 12, 13, 8, 13)),
    		Block.makeCuboidShape(1, 0, 1, 8, 8, 15), //5
    		VoxelShapes.or(Block.makeCuboidShape(5, 0, 10, 6, 8, 11), //6
					Block.makeCuboidShape(1, 0, 1, 8, 8, 9),
					Block.makeCuboidShape(1, 0, 9, 5, 8, 12),
					Block.makeCuboidShape(1, 0, 12, 3, 8, 14),
					Block.makeCuboidShape(5, 0, 9, 7, 8, 10),
					Block.makeCuboidShape(1, 0, 14, 2, 8, 15),
					Block.makeCuboidShape(3, 0, 12, 4, 8, 13)),
    		Block.makeCuboidShape(1.0, 0.0, 1.0, 8.0, 8.0, 8.0), //7
    		VoxelShapes.or(Block.makeCuboidShape(7, 0, 7, 8, 8, 8), //8
					Block.makeCuboidShape(4, 0, 1, 8, 8, 5),
					Block.makeCuboidShape(2, 0, 1, 4, 8, 3),
					Block.makeCuboidShape(6, 0, 5, 8, 8, 7),
					Block.makeCuboidShape(1, 0, 1, 2, 8, 2),
					Block.makeCuboidShape(3, 0, 3, 4, 8, 4),
					Block.makeCuboidShape(5, 0, 5, 6, 8, 6))
    };
    
    public BirthdayCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(BirthdayCakeBlock.BITES, 0));
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return BirthdayCakeBlock.SHAPES[state.get(BirthdayCakeBlock.BITES)];
    }
    
    public boolean isNormalCube(final BlockState state, final IBlockReader worldIn, final BlockPos pos) {
        return false;
    }
    
    public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            final ItemStack itemstack = player.getHeldItem(handIn);
            if (this.eatSlice(worldIn, pos, state, player) == ActionResultType.SUCCESS) {
                return ActionResultType.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }
        return this.eatSlice(worldIn, pos, state, player);
    }
    
    private ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
    	final int i = state.get(BirthdayCakeBlock.BITES);
    	if (i != 0) {
            if (!playerIn.canEat(false)) {
                return ActionResultType.PASS;
            }
            playerIn.addStat(Stats.EAT_CAKE_SLICE);
            playerIn.getFoodStats().addStats(2, 0.1f);
    	}
    	else if (i == 0) {
    		worldIn.playSound(playerIn, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.1F, 1.0F);
    	}
        if (i < 8) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(BirthdayCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void animateTick(final BlockState stateIn, final World worldIn, final BlockPos pos, final Random rand) {
        final int i = stateIn.get(BirthdayCakeBlock.BITES);
        if (i < 1) {
            final double d0 = pos.getX() + 0.5;
            final double d1 = pos.getY() + 0.95;
            final double d2 = pos.getZ() + 0.5;
            worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
            worldIn.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
        }
    }
    
	public BlockState updatePostPlacement(final BlockState stateIn, final Direction facing, final BlockState facingState, final IWorld worldIn, final BlockPos currentPos, final BlockPos facingPos) {
        return (facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
    
    public boolean isValidPosition(final BlockState state, final IWorldReader worldIn, final BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }
    
    protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BITES);
    }
    
    @Deprecated
    public int getComparatorInputOverride(final BlockState blockState, final World worldIn, final BlockPos pos) {
        return (8 - blockState.get(BirthdayCakeBlock.BITES)) * 2;
    }
    
    @Deprecated
    public boolean hasComparatorInputOverride(final BlockState state) {
        return true;
    }
    
    public boolean allowsMovement(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
}
