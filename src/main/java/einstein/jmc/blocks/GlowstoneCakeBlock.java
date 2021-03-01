package einstein.jmc.blocks;

import einstein.jmc.tileentity.GlowstoneCakeTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class GlowstoneCakeBlock extends ContainerBlock
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 6);
    protected static final VoxelShape[] SHAPES = new VoxelShape[]{
    		Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(3.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(5.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(7.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(9.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(11.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D), 
    		Block.makeCuboidShape(13.0D, 0.0D, 1.0D, 15.0D, 8.0D, 15.0D)
    };
    
    public GlowstoneCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(GlowstoneCakeBlock.BITES, 0));
    }
    
    public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
        return GlowstoneCakeBlock.SHAPES[state.get(GlowstoneCakeBlock.BITES)];
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
        World world = playerIn.getEntityWorld();
        TileEntity tileentity = world.getTileEntity(pos);
        ((GlowstoneCakeTileEntity)tileentity).setGlowing();
        final int i = state.get(GlowstoneCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(GlowstoneCakeBlock.BITES, (i + 1)), 3);
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
        return (7 - blockState.get(GlowstoneCakeBlock.BITES)) * 2;
    }
    
    @Deprecated
    public boolean hasComparatorInputOverride(final BlockState state) {
        return true;
    }
    
    public boolean allowsMovement(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
    	return BlockRenderType.MODEL; 
    }
    
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new GlowstoneCakeTileEntity();
	}
}
