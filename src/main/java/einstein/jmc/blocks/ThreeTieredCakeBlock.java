package einstein.jmc.blocks;

import einstein.jmc.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThreeTieredCakeBlock extends Block
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] { 
    		Shapes.or(Block.box(3, 15, 3, 13, 21, 13), //0 uneaten
    				Block.box(1, 0, 1, 15, 8, 15),
    				Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(5, 15, 3, 13, 21, 13), //1
    				Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(7, 15, 3, 13, 21, 13), //2
    				Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(9, 15, 3, 13, 21, 13), //3
    				Block.box(1, 0, 1, 15, 8, 15),
    				Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(11, 15, 3, 13, 21, 13), //4
					Block.box(1, 0, 1, 15, 8, 15),
					Block.box(2, 8, 2, 14, 15, 14)),
    		Shapes.or(Block.box(2, 8, 2, 14, 15, 14), //5
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(4, 8, 2, 14, 15, 14), //6
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(6, 8, 2, 14, 15, 14), //7
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(8, 8, 2, 14, 15, 14), //8
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Shapes.or(Block.box(10, 8, 2, 14, 15, 14), //9
    				Block.box(1, 0, 1, 15, 8, 15)),
    		Block.box(1, 0, 1, 15, 8, 15), //10
    		Block.box(4, 0, 1, 15, 8, 15), //11
    		Block.box(5, 0, 1, 15, 8, 15), //12
    		Block.box(7, 0, 1, 15, 8, 15), //13
    		Block.box(9, 0, 1, 15, 8, 15), //14
    		Block.box(11, 0, 1, 15, 8, 15) //15
    };
    
    public ThreeTieredCakeBlock(final Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
    }
    
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
    }
    
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();
		if (itemstack.is(ItemTags.CANDLES) && state.getValue(BITES) == 0) {
			Block block = Block.byItem(item);
			if (block instanceof CandleBlock) {
				if (!player.isCreative()) {
					itemstack.shrink(1);
				}

				level.playSound((Player) null, pos, SoundEvents.CAKE_ADD_CANDLE, SoundSource.BLOCKS, 1.0F, 1.0F);
				String candle = block.getRegistryName().getPath();
				Block candleBlock = ModBlocks.getBlock(ModBlocks.RL(candle + "_three_tiered_cake"));
				((ThreeTieredCandleCakeBlock) candleBlock).setOriginalCake(this);
				level.setBlockAndUpdate(pos, candleBlock.defaultBlockState());
				level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				player.awardStat(Stats.ITEM_USED.get(item));
				return InteractionResult.SUCCESS;
			}
		}
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
		if (i < 15) { // Number must be same as BITES
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
    
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
    	definition.add(BITES);
	}
    
	public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return (15 - state.getValue(BITES)) * 2;
	}
	
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computaion) {
		return false;
	}
}
