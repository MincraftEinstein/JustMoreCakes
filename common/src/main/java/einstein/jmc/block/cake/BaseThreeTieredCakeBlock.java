package einstein.jmc.block.cake;

import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.util.CakeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
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

public class BaseThreeTieredCakeBlock extends BaseCakeBlock {

    public static final IntegerProperty BITES = IntegerProperty.create("bites", 0, 15);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape[] SHAPE_BY_BITE_UPPER = new VoxelShape[] {
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

    protected static final VoxelShape[] SHAPE_BY_BITE_LOWER = new VoxelShape[] {
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

    public BaseThreeTieredCakeBlock(CakeVariant builder) {
        super(builder, 15);
        registerDefaultState(defaultBlockState().setValue(HALF, UPPER));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (state.getValue(HALF) == LOWER) {
            CakeFamily family = getFamily();

            if (family != null) {
                if (stack.is(family.getBaseCake().get().asItem())) {
                    int bites = state.getValue(getBites());

                    if (bites == 5) {
                        if (CakeUtil.convertToThreeTiered(family, state, pos, level, player, stack, true).consumesAction()) {
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                    else if (bites == 10) {
                        if (CakeUtil.convertToTwoTiered(family, state, pos, level, player, stack, true).consumesAction()) {
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }

        return CakeUtil.redirectUse(this, state, level, pos, (aboveState, abovePos) -> aboveState.useItemOn(stack, level, player, hand, hitResult.withPosition(abovePos)),
                () -> super.useItemOn(stack, state, level, pos, player, hand, hitResult));
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return CakeUtil.redirectUse(this, state, level, pos, (aboveState, abovePos) -> aboveState.useWithoutItem(level, player, hitResult.withPosition(abovePos)),
                () -> super.useWithoutItem(state, level, pos, player, hitResult));
    }

    @Override
    public void afterCandlePlaced(Level level, BlockState state, BlockPos pos, BaseCandleCakeBlock candleCake) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);

        if (belowState.is(this) && belowState.getValue(HALF) == LOWER) {
            level.setBlockAndUpdate(belowPos, CakeUtil.createLowerState(candleCake, false));
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
        state = super.eatActions(player, pos, state);
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
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()) {
            CakeUtil.destroyOppositeHalf(state, pos, level, ItemStack.EMPTY, !player.isCreative());
        }

        return super.playerWillDestroy(level, pos, state, player);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();

        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return CakeUtil.createLowerState(this, true);
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
}
