package einstein.jmc.blocks;

import einstein.jmc.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CupcakeBlock extends Block
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 1);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] {
    		Shapes.or(Block.box(6, 0, 6, 10, 5, 10),
    				Block.box(6, 3, 5, 10, 4, 6),
    				Block.box(6, 3, 10, 10, 4, 11),
    				Block.box(10, 3, 6, 11, 4, 10),
    				Block.box(5, 3, 6, 6, 4, 10),
    				Block.box(7, 5, 7, 9, 6, 9)),
    		Shapes.or(Block.box(8, 0, 6, 10, 5, 10),
    				Block.box(8, 3, 5, 10, 4, 6),
    				Block.box(8, 3, 10, 10, 4, 11),
    				Block.box(10, 3, 6, 11, 4, 10),
    				Block.box(8, 5, 7, 9, 6, 9))
    };
    
    public CupcakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
    }
    
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
    }
    
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
    	return new ItemStack(ModItems.CUPCAKE.get());
    }
    
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (level.isClientSide) {
			if (eat(level, pos, state, player).consumesAction()) {
				return InteractionResult.SUCCESS;
			}
			
			if (itemstack.isEmpty()) {
				return InteractionResult.CONSUME;
			}
		}
		return eat(level, pos, state, player);
	}
    
    public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
		player.getFoodData().eat(2, 0.1F);
        int i = state.getValue(BITES);
        accessor.gameEvent(player, GameEvent.EAT, pos);
		if (i < 1) { // Number must be same as BITES
			accessor.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
		} else {
			accessor.removeBlock(pos, false);
			accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
		}
		return InteractionResult.SUCCESS;
	}
    
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState state, Direction direction, BlockState p_51215_, LevelAccessor accessor, BlockPos pos, BlockPos p_51218_) {
		return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, p_51215_, accessor, pos, p_51218_);
	}
	
	public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
		return reader.getBlockState(pos.below()).getMaterial().isSolid();
	}
    
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    	builder.add(BITES);
	}
    
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return (2 - state.getValue(BITES)) * 2;
	}
	
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computaion) {
		return false;
	}
}
