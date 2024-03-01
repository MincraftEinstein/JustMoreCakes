package einstein.jmc.block.cake.candle;

import com.google.common.collect.ImmutableList;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class BaseThreeTieredCandleCakeBlock extends BaseCandleCakeBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape UPPER_SHAPE = Shapes.or(
            box(3, -1, 3, 13, 5, 13), // Top
            box(7, 5, 7, 9, 11, 9) // Candle
    );
    protected static final VoxelShape LOWER_SHAPE = Shapes.or(
            box(1, 0, 1, 15, 8, 15), // Lower
            box(2, 8, 2, 14, 15, 14) // Middle
    );

    public BaseThreeTieredCandleCakeBlock(BaseCakeBlock originalCake, Block candle, Properties properties) {
        super(originalCake, candle, properties);
        registerDefaultState(defaultBlockState().setValue(HALF, UPPER));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == LOWER ? LOWER_SHAPE : UPPER_SHAPE;
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
    protected void afterEaten(BlockState state, BlockPos pos, Level level, Player player) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(this) && belowState.getValue(HALF) == LOWER) {
            level.setBlockAndUpdate(belowPos, createLowerState());
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf blockHalf = state.getValue(HALF);

        if (direction.getAxis() != Direction.Axis.Y || blockHalf == LOWER != (direction == Direction.UP)) {
            return blockHalf == LOWER && direction == Direction.DOWN && !state.canSurvive(accessor, pos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
        }

        return neighborState.is(this) && neighborState.getValue(HALF) != blockHalf
                ? state
                : blockHalf == LOWER ? createLowerState() : Blocks.AIR.defaultBlockState();
    }

    protected BlockState createLowerState() {
        return BaseThreeTieredCakeBlock.createLowerState(getOriginalCake(), true);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            BaseThreeTieredCakeBlock.destroyOppositeHalf(state, pos, level, ItemStack.EMPTY, !player.isCreative());
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return defaultBlockState().setValue(HALF, LOWER);
        }

        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        level.setBlockAndUpdate(pos.above(), state.setValue(HALF, UPPER));
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
    public void spawnCandleFlames(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HALF) == UPPER) {
            super.spawnCandleFlames(state, level, pos, random);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        spawnCandleFlames(state, level, pos, random);
        getOriginalCake().animateTick(getOriginalCake().defaultBlockState().setValue(HALF, state.getValue(HALF)), level, pos, random);
    }

    @Override
    protected boolean candleHit(BlockHitResult hitResult, BlockState state) {
        if (state.getValue(HALF) == UPPER) {
            return super.candleHit(hitResult, state);
        }

        return false;
    }

    @Override
    protected double getCandleHeight() {
        return 0.3125D;
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return ImmutableList.of(new Vec3(0.5D, 0.8125D, 0.5D));
    }

    @Override
    public BaseThreeTieredCakeBlock getOriginalCake() {
        return (BaseThreeTieredCakeBlock) super.getOriginalCake();
    }
}