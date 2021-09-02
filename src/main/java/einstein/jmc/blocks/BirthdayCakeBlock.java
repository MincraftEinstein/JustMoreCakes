package einstein.jmc.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BirthdayCakeBlock extends Block
{
    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 8);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] { 
    		Block.box(1, 0, 1, 15, 8, 15), //0 uneaten lit
    		Block.box(1, 0, 1, 15, 8, 15), //1 uneaten unlit
    		Shapes.or(Block.box(14, 0, 1, 15, 8, 2),
    				Block.box(1, 0, 1, 8, 8, 15),
    				Block.box(8, 0, 7, 15, 8, 15),
    				Block.box(11, 0, 4, 15, 8, 7),
    				Block.box(9, 0, 6, 11, 8, 7),
    				Block.box(13, 0, 2, 15, 8, 4),
    				Block.box(10, 0, 5, 11, 8, 6),
    				Block.box(12, 0, 3, 13, 8, 4)),
    		Shapes.or(Block.box(1, 0, 1, 8, 8, 15), //3
    				Block.box(8, 0, 8, 15, 8, 15)),
    		Shapes.or(Block.box(14, 0, 14, 15, 8, 15), //4
					Block.box(1, 0, 1, 8, 8, 15),
					Block.box(8, 0, 11, 12, 8, 15),
					Block.box(12, 0, 13, 14, 8, 15),
					Block.box(8, 0, 9, 10, 8, 11),
					Block.box(8, 0, 8, 9, 8, 9),
					Block.box(10, 0, 10, 11, 8, 11),
					Block.box(12, 0, 12, 13, 8, 13)),
    		Block.box(1, 0, 1, 8, 8, 15), //5
    		Shapes.or(Block.box(5, 0, 10, 6, 8, 11), //6
					Block.box(1, 0, 1, 8, 8, 9),
					Block.box(1, 0, 9, 5, 8, 12),
					Block.box(1, 0, 12, 3, 8, 14),
					Block.box(5, 0, 9, 7, 8, 10),
					Block.box(1, 0, 14, 2, 8, 15),
					Block.box(3, 0, 12, 4, 8, 13)),
    		Block.box(1.0, 0.0, 1.0, 8.0, 8.0, 8.0), //7
    		Shapes.or(Block.box(7, 0, 7, 8, 8, 8), //8
					Block.box(4, 0, 1, 8, 8, 5),
					Block.box(2, 0, 1, 4, 8, 3),
					Block.box(6, 0, 5, 8, 8, 7),
					Block.box(1, 0, 1, 2, 8, 2),
					Block.box(3, 0, 3, 4, 8, 4),
					Block.box(5, 0, 5, 6, 8, 6))
    };
    
    public BirthdayCakeBlock(final Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BITES, Integer.valueOf(0)));
    }
    
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(BITES)];
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
    	int i = state.getValue(BITES);
    	if (i != 0) {
	        if (!player.canEat(false)) {
	            return InteractionResult.PASS;
	        }
	        player.awardStat(Stats.EAT_CAKE_SLICE);
			player.getFoodData().eat(2, 0.1F);
			accessor.gameEvent(player, GameEvent.EAT, pos);
    	}
    	else if (i == 0) {
    		accessor.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1F, 1.0F);
    	}
		if (i < 8) { // Number must be same as BITES
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
		return (8 - state.getValue(BITES)) * 2;
	}
	
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computaion) {
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
        final int i = state.getValue(BirthdayCakeBlock.BITES);
        if (i < 1) {
            final double d0 = pos.getX() + 0.5;
            final double d1 = pos.getY() + 0.95;
            final double d2 = pos.getZ() + 0.5;
            level.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, 0, 0);
            level.addParticle(ParticleTypes.FLAME, d0, d1, d2, 0, 0, 0);
        }
    }
}
