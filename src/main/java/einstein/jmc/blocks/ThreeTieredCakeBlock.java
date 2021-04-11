package einstein.jmc.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ThreeTieredCakeBlock extends Block
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] { 
    		VoxelShapes.or(Block.makeCuboidShape(3, 15, 3, 13, 21, 13), //0 uneaten
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15),
    				Block.makeCuboidShape(2, 8, 2, 14, 15, 14)),
    		VoxelShapes.or(Block.makeCuboidShape(5, 15, 3, 13, 21, 13), //1
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15),
					Block.makeCuboidShape(2, 8, 2, 14, 15, 14)),
    		VoxelShapes.or(Block.makeCuboidShape(7, 15, 3, 13, 21, 13), //2
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15),
					Block.makeCuboidShape(2, 8, 2, 14, 15, 14)),
    		VoxelShapes.or(Block.makeCuboidShape(9, 15, 3, 13, 21, 13), //3
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15),
    				Block.makeCuboidShape(2, 8, 2, 14, 15, 14)),
    		VoxelShapes.or(Block.makeCuboidShape(11, 15, 3, 13, 21, 13), //4
					Block.makeCuboidShape(1, 0, 1, 15, 8, 15),
					Block.makeCuboidShape(2, 8, 2, 14, 15, 14)),
    		VoxelShapes.or(Block.makeCuboidShape(2, 8, 2, 14, 15, 14), //5
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15)),
    		VoxelShapes.or(Block.makeCuboidShape(4, 8, 2, 14, 15, 14), //6
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15)),
    		VoxelShapes.or(Block.makeCuboidShape(6, 8, 2, 14, 15, 14), //7
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15)),
    		VoxelShapes.or(Block.makeCuboidShape(8, 8, 2, 14, 15, 14), //8
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15)),
    		VoxelShapes.or(Block.makeCuboidShape(10, 8, 2, 14, 15, 14), //9
    				Block.makeCuboidShape(1, 0, 1, 15, 8, 15)),
    		Block.makeCuboidShape(1, 0, 1, 15, 8, 15), //10
    		Block.makeCuboidShape(4, 0, 1, 15, 8, 15), //11
    		Block.makeCuboidShape(5, 0, 1, 15, 8, 15), //12
    		Block.makeCuboidShape(7, 0, 1, 15, 8, 15), //13
    		Block.makeCuboidShape(9, 0, 1, 15, 8, 15), //14
    		Block.makeCuboidShape(11, 0, 1, 15, 8, 15) //15
    };
    
    public ThreeTieredCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(ThreeTieredCakeBlock.BITES, 0));
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return ThreeTieredCakeBlock.SHAPES[state.get(ThreeTieredCakeBlock.BITES)];
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
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1F);
        final int i = state.get(ThreeTieredCakeBlock.BITES);
        if (i < 15) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(ThreeTieredCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
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
        return (15 - blockState.get(ThreeTieredCakeBlock.BITES)) * 2;
    }
    
    @Deprecated
    public boolean hasComparatorInputOverride(final BlockState state) {
        return true;
    }
    
    public boolean allowsMovement(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
}
