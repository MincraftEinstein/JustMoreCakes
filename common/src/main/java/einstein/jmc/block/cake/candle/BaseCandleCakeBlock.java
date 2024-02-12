package einstein.jmc.block.cake.candle;

import com.google.common.collect.ImmutableList;
import einstein.jmc.block.cake.BaseCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BaseCandleCakeBlock extends AbstractCandleBlock {

    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 8, 15),
            Block.box(7, 8, 7, 9, 14, 9));

    private final BaseCakeBlock originalCake;
    private final Block candle;

    public BaseCandleCakeBlock(BaseCakeBlock originalCake, Block candle, Properties properties) {
        super(properties);
        this.originalCake = originalCake;
        this.candle = candle;
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
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(Items.FLINT_AND_STEEL) && !stack.is(Items.FIRE_CHARGE)) {
            if (candleHit(hitResult, state) && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
                extinguish(player, state, level, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            InteractionResult result = originalCake.eat(level, pos, originalCake.defaultBlockState(), player);
            if (result.consumesAction()) {
                dropResources(state, level, pos);
                afterEaten(state, pos, level, player);
            }
            return result;
        }

        if (!state.getValue(LIT)) {
            level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, defaultBlockState().setValue(BlockStateProperties.LIT, true), Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            stack.hurtAndBreak(1, player, broadcaster -> broadcaster.broadcastBreakEvent(player.getUsedItemHand()));

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return InteractionResult.PASS;
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
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        ItemStack stack = new ItemStack(originalCake.asItem());
        if (stack.isEmpty() && originalCake.getFamily() != null) {
            return new ItemStack(originalCake.getFamily().getBaseCake().get());
        }
        return stack;
    }

    @SuppressWarnings("deprecation")
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
        return 14;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType computation) {
        return false;
    }

    public void spawnCandleFlames(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        spawnCandleFlames(state, level, pos, random);
        originalCake.animateTick(originalCake.defaultBlockState(), level, pos, random);
    }

    protected double getCandleHeight() {
        return 0.5D;
    }

    public BaseCakeBlock getOriginalCake() {
        return originalCake;
    }

    public Block getCandle() {
        return candle;
    }
}