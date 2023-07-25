package einstein.jmc.blocks;

import einstein.jmc.init.ModGameEvents;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.SculkSensorBlock.getPhase;

@SuppressWarnings("deprecation")
public class SculkCakeBlock extends BaseCakeBlock {

    public static final EnumProperty<SculkSensorPhase> PHASE = BlockStateProperties.SCULK_SENSOR_PHASE;

    public SculkCakeBlock(CakeBuilder builder) {
        super(builder);
        registerDefaultState(defaultBlockState().setValue(PHASE, SculkSensorPhase.INACTIVE));
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        if (canActivate(state)) {
            return super.eat(level, pos, state, player);
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        player.level().gameEvent(player, ModGameEvents.SCULK_CAKE_EATEN.get(), pos);
        return activate(player, player.level(), pos, state);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getPhase(state) != SculkSensorPhase.ACTIVE) {
            if (getPhase(state) == SculkSensorPhase.COOLDOWN) {
                level.setBlock(pos, state.setValue(PHASE, SculkSensorPhase.INACTIVE), 3);
                level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1, level.random.nextFloat() * 0.2F + 0.8F);
            }
        }
        else {
            deactivate(level, pos, state);
        }
    }

    @Override
    public void onRemove(BlockState $$0, Level $$1, BlockPos $$2, BlockState $$3, boolean $$4) {
        if (!$$0.is($$3.getBlock())) {
            if (getPhase($$0) == SculkSensorPhase.ACTIVE) {
                updateNeighbours($$1, $$2, $$0);
            }

            super.onRemove($$0, $$1, $$2, $$3, $$4);
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (getPhase(state) == SculkSensorPhase.ACTIVE) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP && direction != Direction.DOWN) {
                double x = pos.getX() + 0.5 + (direction.getStepX() == 0 ? 0.5 - random.nextDouble() : direction.getStepX() * 0.6);
                double y = pos.getY() + 0.25;
                double z = pos.getZ() + 0.5 + (direction.getStepZ() == 0 ? 0.5 - random.nextDouble() : direction.getStepZ() * 0.6);
                double ySpeed = random.nextFloat() * 0.04;
                level.addParticle(DustColorTransitionOptions.SCULK_TO_REDSTONE, x, y, z, 0, ySpeed, 0);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(PHASE));
    }

    @Override
    public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, stack, dropExperience);
        if (dropExperience && !stack.is(ModItems.CAKE_SPATULA.get())) {
            tryDropExperience(level, pos, stack, ConstantInt.of(5));
        }
    }

    private static void updateNeighbours(Level level, BlockPos pos, BlockState $$2) {
        Block block = $$2.getBlock();
        level.updateNeighborsAt(pos, block);
        level.updateNeighborsAt(pos.below(), block);
    }

    public static boolean canActivate(BlockState blockState) {
        return getPhase(blockState) == SculkSensorPhase.INACTIVE;
    }

    public static void deactivate(Level level, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(PHASE, SculkSensorPhase.COOLDOWN), 3);
        level.scheduleTick(pos, state.getBlock(), 10);
        updateNeighbours(level, pos, state);
    }

    public static BlockState activate(@Nullable Entity entity, Level level, BlockPos pos, BlockState state) {
        state = state.setValue(PHASE, SculkSensorPhase.ACTIVE);
        level.setBlock(pos, state, 3);
        level.scheduleTick(pos, state.getBlock(), 30);
        updateNeighbours(level, pos, state);
        level.gameEvent(entity, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1.0F, level.random.nextFloat() * 0.2F + 0.8F);
        return state;
    }
}
