package einstein.jmc.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import einstein.jmc.init.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.entity.ai.behavior.HarvestFarmland.SPEED_MODIFIER;

public class HarvestSugarCane extends Behavior<Villager> {

    @Nullable
    private BlockPos sugarCanePos;
    private long nextStartTime;
    private int timeWorked;
    private final List<BlockPos> validSugarCanePositions = new ArrayList<>();

    public HarvestSugarCane() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Villager villager) {
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        }

        if (villager.getVillagerData().getProfession() != ModVillagers.CAKE_BAKER.get()) {
            return false;
        }
        validSugarCanePositions.clear();

        for (int x = -1; x <= 1; ++x) {
            for (int y = -1; y <= 1; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    BlockPos pos = new BlockPos.MutableBlockPos(villager.getX() + x, villager.getY() + y, villager.getZ() + z);
                    if (validPos(pos, level)) {
                        validSugarCanePositions.add(pos);
                    }
                }
            }
        }

        sugarCanePos = getValidSugarCane(level);
        return sugarCanePos != null;
    }

    private boolean validPos(BlockPos pos, ServerLevel level) {
        BlockState state = level.getBlockState(pos);
        BlockState belowState = level.getBlockState(pos.below());
        return state.getBlock() instanceof SugarCaneBlock && belowState.getBlock() instanceof SugarCaneBlock;
    }

    @Nullable
    private BlockPos getValidSugarCane(ServerLevel level) {
        return validSugarCanePositions.isEmpty() ? null : validSugarCanePositions.get(level.getRandom().nextInt(validSugarCanePositions.size()));
    }

    @Override
    protected void start(ServerLevel level, Villager villager, long gameTime) {
        if (gameTime > nextStartTime && sugarCanePos != null) {
            setMemories(villager);
        }
    }

    @Override
    protected void stop(ServerLevel level, Villager villager, long gameTime) {
        Brain<Villager> brain = villager.getBrain();
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        timeWorked = 0;
        nextStartTime = gameTime + 40;
    }

    @Override
    protected void tick(ServerLevel level, Villager villager, long gameTime) {
        if (sugarCanePos == null || sugarCanePos.closerToCenterThan(villager.position().add(0, 1, 0), 1)) {
            if (sugarCanePos != null && gameTime > nextStartTime) {
                BlockState state = level.getBlockState(sugarCanePos);
                Block block = state.getBlock();

                if (block instanceof SugarCaneBlock) {
                    level.destroyBlock(sugarCanePos, true, villager);
                    validSugarCanePositions.remove(sugarCanePos);
                    sugarCanePos = getValidSugarCane(level);
                    if (sugarCanePos != null) {
                        nextStartTime = gameTime + 20;
                        setMemories(villager);
                    }
                }
            }
            ++timeWorked;
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel level, Villager villager, long gameTime) {
        return timeWorked < 200;
    }

    private void setMemories(Villager villager) {
        if (sugarCanePos != null) {
            Brain<Villager> brain = villager.getBrain();
            BlockPosTracker tracker = new BlockPosTracker(sugarCanePos);
            brain.setMemory(MemoryModuleType.LOOK_TARGET, tracker);
            brain.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(tracker, SPEED_MODIFIER, 1));
        }
    }
}
