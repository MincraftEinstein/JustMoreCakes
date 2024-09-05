package einstein.jmc.block.cake;

import einstein.jmc.compat.FarmersDelightCompat;
import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ObsidianTwoTieredCakeBlock extends BaseTwoTieredCakeBlock {

    public ObsidianTwoTieredCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        ObsidianCakeBlock.damage(level, player);
        level.setBlockAndUpdate(pos, state);

        if (player instanceof ServerPlayer serverPlayer) {
            ModTriggerTypes.CAKE_EATEN.get().trigger(serverPlayer, this);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean cutSlice(Level level, BlockPos pos, BlockState state, Player player, ItemStack stack) {
        if (stack.isCorrectToolForDrops(state)) {
            return super.cutSlice(level, pos, state, player, stack);
        }
        return false;
    }
}
