package einstein.jmc.block.cake.candle;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.util.CakeUtil;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BaseCandleCakeBlock extends AbstractCandleBlock {

    private static final MapCodec<BaseCandleCakeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    Util.blockOfTypeCodec(BaseCakeBlock.class).fieldOf("parent").forGetter(BaseCandleCakeBlock::getParentCake),
                    Util.blockOfTypeCodec(CandleBlock.class).fieldOf("candle").forGetter(BaseCandleCakeBlock::getCandle),
                    propertiesCodec()
            ).apply(instance, BaseCandleCakeBlock::new)
    );

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 8, 15),
            Block.box(7, 8, 7, 9, 14, 9)
    );

    private final BaseCakeBlock parentCake;
    private final CandleBlock candle;

    public BaseCandleCakeBlock(BaseCakeBlock parentCake, Block candle, Properties properties) {
        super(properties);
        this.parentCake = parentCake;
        this.candle = (CandleBlock) candle;
        registerDefaultState(stateDefinition.any().setValue(LIT, false));
    }

    @Override
    protected Iterable<Vec3> getParticleOffsets(BlockState state) {
        return ImmutableList.of(new Vec3(0.5D, 1, 0.5D));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.FLINT_AND_STEEL) || stack.is(Items.FIRE_CHARGE)) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }

        if (candleHit(hitResult, state) && stack.isEmpty() && state.getValue(LIT)) {
            extinguish(player, state, level, pos);
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        InteractionResult result = parentCake.eat(level, pos, parentCake.defaultBlockState(), player);
        if (result.consumesAction()) {
            dropResources(state, level, pos);
            afterEaten(state, pos, level, player);
        }
        return result;
    }

    protected void afterEaten(BlockState state, BlockPos pos, Level level, Player player) {
    }

    protected boolean candleHit(BlockHitResult hitResult, BlockState state) {
        return hitResult.getLocation().y - hitResult.getBlockPos().getY() > getCandleHeight();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader reader, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(parentCake.asItem());
        if (stack.isEmpty() && parentCake.getFamily() != null) {
            return new ItemStack(parentCake.getFamily().getBaseCake().get());
        }
        return stack;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor accessor, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN && !state.canSurvive(accessor, pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, direction, neighborState, accessor, pos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader reader, BlockPos pos) {
        return reader.getBlockState(pos.below()).isSolid();
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return CakeUtil.getComparatorOutput(parentCake.defaultBlockState());
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType computationType) {
        return false;
    }

    public void spawnCandleFlames(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        spawnCandleFlames(state, level, pos, random);
        parentCake.animateTick(parentCake.defaultBlockState(), level, pos, random);
    }

    @Override
    protected MapCodec<? extends AbstractCandleBlock> codec() {
        return CODEC;
    }

    protected double getCandleHeight() {
        return 0.5D;
    }

    public BaseCakeBlock getParentCake() {
        return parentCake;
    }

    public CandleBlock getCandle() {
        return candle;
    }
}