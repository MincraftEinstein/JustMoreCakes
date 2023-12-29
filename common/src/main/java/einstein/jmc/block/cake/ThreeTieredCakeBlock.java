package einstein.jmc.block.cake;

import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class ThreeTieredCakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final VoxelShape[] SHAPE_BY_BITE_UPPER = new VoxelShape[] {
            Block.box(3, -1, 3, 13, 5, 13),
            Block.box(5, -1, 3, 13, 5, 13),
            Block.box(7, -1, 3, 13, 5, 13),
            Block.box(9, -1, 3, 13, 5, 13),
            Block.box(11, -1, 3, 13, 5, 13),
            Shapes.block(), Shapes.block(), Shapes.block(),
            Shapes.block(), Shapes.block(), Shapes.block(),
            Shapes.block(), Shapes.block(), Shapes.block(),
            Shapes.block(), Shapes.block(),
    };

    private static final VoxelShape[] SHAPE_BY_BITE_LOWER = new VoxelShape[] {
            Shapes.block(), Shapes.block(), Shapes.block(),
            Shapes.block(), Shapes.block(),
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

    public ThreeTieredCakeBlock(CakeBuilder builder) {
        super(builder, 15);
        registerDefaultState(defaultBlockState().setValue(HALF, UPPER));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(HALF) == LOWER) {
            BlockPos abovePos = pos.above();
            BlockState aboveState = level.getBlockState(abovePos);
            if (aboveState.is(this) && aboveState.getValue(HALF) == UPPER) {
                return aboveState.use(level, player, hand, hitResult.withPosition(abovePos));
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public void afterCandlePlaced(Level level, BlockState state, BlockPos pos, BaseCandleCakeBlock candleCake) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(this) && belowState.getValue(HALF) == LOWER) {
            level.setBlockAndUpdate(belowPos, createLowerState(candleCake, false));
        }
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        InteractionResult result = super.eat(level, pos, state, player);

        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        if (belowState.is(this) && belowState.getValue(HALF) == LOWER) {
            level.updateNeighbourForOutputSignal(belowPos, belowState.getBlock());
        }

        return result;
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == UPPER) {
            if (state.getValue(BITES) >= 4) {
                return state.setValue(BITES, getSlices());
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf blockHalf = state.getValue(HALF);

        if (direction.getAxis() != Direction.Axis.Y || blockHalf == LOWER != (direction == Direction.UP)) {
            return blockHalf == LOWER && direction == Direction.DOWN && !state.canSurvive(accessor, pos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
        }

        if (state.getValue(HALF) == UPPER) {
            return neighborState.is(this) ? state : Blocks.AIR.defaultBlockState();
        }

        return super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            destroyOppositeHalf(state, pos, level, player);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return createLowerState(this, true);
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        level.setBlockAndUpdate(pos.above(), state.setValue(HALF, UPPER).setValue(BITES, 0));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return state.getValue(HALF) == LOWER
                ? super.canSurvive(state, reader, pos)
                : reader.getBlockState(pos.below()).is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    public IntegerProperty getBites() {
        return BITES;
    }

    @Override
    public VoxelShape[] getShapeByBite(BlockState state) {
        return state.getValue(HALF) == LOWER ? SHAPE_BY_BITE_LOWER : SHAPE_BY_BITE_UPPER;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockPos abovePos = pos.above();
        BlockState aboveState = level.getBlockState(abovePos);
        int slices = getSlices() + 1;

        if (aboveState.is(this) && aboveState.getValue(HALF) == UPPER) {
            return slices - aboveState.getValue(getBites());
        }

        return slices - state.getValue(getBites());
    }

    public static BlockState createLowerState(Block block, boolean hasBites) {
        BlockState newState = block.defaultBlockState().setValue(HALF, LOWER);

        if (hasBites) {
            BaseCakeBlock cakeBlock = ((BaseCakeBlock) block);
            if (cakeBlock.getSlices() > 0) {
                newState = newState.setValue(cakeBlock.getBites(), 5);
            }
        }

        return newState;
    }

    public static void destroyOppositeHalf(BlockState state, BlockPos pos, Level level, Player player) {
        boolean isLower = state.getValue(HALF) == LOWER;
        BlockPos otherPos = isLower ? pos.above() : pos.below();
        BlockState otherState = level.getBlockState(otherPos);
        if (otherState.is(state.getBlock()) && otherState.getValue(HALF) == (isLower ? UPPER : LOWER)) {
            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
            level.levelEvent(player, 2001, otherPos, Block.getId(otherState));
        }
    }
}
