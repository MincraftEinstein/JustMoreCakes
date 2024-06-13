package einstein.jmc.block.cake;

import einstein.jmc.init.ModGameEvents;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
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

    public SculkCakeBlock(CakeVariant builder) {
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
        return activate(player, player.level(), pos, state, true);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (getPhase(state) != SculkSensorPhase.ACTIVE) {
            if (getPhase(state) == SculkSensorPhase.COOLDOWN) {
                level.setBlockAndUpdate(pos, state.setValue(PHASE, SculkSensorPhase.INACTIVE));
                level.playSound(null, pos, SoundEvents.SCULK_CLICKING_STOP, SoundSource.BLOCKS, 1, level.random.nextFloat() * 0.2F + 0.8F);
            }
            return;
        }
        deactivate(level, pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (getPhase(state) == SculkSensorPhase.ACTIVE) {
                updateNeighbours(level, pos, state);
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
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

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return super.getAnalogOutputSignal(state, level, pos) + (getPhase(state) == SculkSensorPhase.ACTIVE ? 1 : 0);
    }

    public static void updateNeighbours(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();
        level.updateNeighborsAt(pos, block);
        level.updateNeighborsAt(pos.below(), block);
    }

    public static boolean canActivate(BlockState state) {
        return getPhase(state) == SculkSensorPhase.INACTIVE;
    }

    public static void deactivate(Level level, BlockPos pos, BlockState state) {
        level.setBlockAndUpdate(pos, state.setValue(PHASE, SculkSensorPhase.COOLDOWN));
        level.scheduleTick(pos, state.getBlock(), 10);
        updateNeighbours(level, pos, state);
    }

    public static BlockState activate(@Nullable Entity entity, Level level, BlockPos pos, BlockState state, boolean sendEvent) {
        state = state.setValue(PHASE, SculkSensorPhase.ACTIVE);
        level.setBlockAndUpdate(pos, state);
        level.scheduleTick(pos, state.getBlock(), 30);
        updateNeighbours(level, pos, state);

        if (sendEvent) {
            level.gameEvent(entity, GameEvent.SCULK_SENSOR_TENDRILS_CLICKING, pos);
            level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 1, level.random.nextFloat() * 0.2F + 0.8F);
        }
        return state;
    }
}
