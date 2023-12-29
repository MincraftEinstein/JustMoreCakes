package einstein.jmc.block.cake.candle;

import com.google.common.collect.ImmutableList;
import einstein.jmc.block.cake.BaseCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BaseCandleCakeBlock extends AbstractCandleBlock {

    public static final BooleanProperty LIT = AbstractCandleBlock.LIT;
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
            if (candleHit(hitResult) && player.getItemInHand(hand).isEmpty() && state.getValue(LIT)) {
                extinguish(player, state, level, pos);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            else {
                beforeEaten(state, pos, level);
                InteractionResult interactionresult = originalCake.eat(level, pos, originalCake.defaultBlockState(), player);
                if (interactionresult.consumesAction()) {
                    dropResources(state, level, pos);
                }
                return interactionresult;
            }
        }
        else {
            if (!state.getValue(LIT)) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1, level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, defaultBlockState().setValue(BlockStateProperties.LIT, true), 11);
                level.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
                stack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(player.getUsedItemHand()));

                return InteractionResult.sidedSuccess(level.isClientSide());
            }
            else {
                Item heldItem = player.getMainHandItem().getItem();
                if (heldItem instanceof FlintAndSteelItem || heldItem instanceof FireChargeItem) {
                    return heldItem.useOn(new UseOnContext(player, hand, hitResult));
                }
                else {
                    return InteractionResult.PASS;
                }
            }
        }
    }

    protected void beforeEaten(BlockState state, BlockPos pos, Level level) {
    }

    protected boolean candleHit(BlockHitResult hitResult) {
        return hitResult.getLocation().y - hitResult.getBlockPos().getY() > getCandleHeight();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) {
        return new ItemStack(originalCake.asItem());
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