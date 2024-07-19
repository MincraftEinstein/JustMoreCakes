package einstein.jmc.block.cake;

import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[0];
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    public IntegerProperty getBites() {
        return null;
    }

    @Override
    public int getSlices() {
        return 0;
    }
}
