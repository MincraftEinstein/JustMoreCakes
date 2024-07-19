package einstein.jmc.block.cake;

import einstein.jmc.damagesource.BlockDamageSource;
import einstein.jmc.init.ModBlocks;
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

public class ObsidianCakeBlock extends BaseCakeBlock {

    public ObsidianCakeBlock(CakeVariant builder) {
        super(builder, 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[0];
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        damage(level, player);
        level.setBlockAndUpdate(pos, state);

        if (player instanceof ServerPlayer serverPlayer) {
            ModTriggerTypes.CAKE_EATEN.get().trigger(serverPlayer, this);
        }

        return InteractionResult.SUCCESS;
    }

    public static void damage(Level level, Player player) {
        player.hurt(new BlockDamageSource(level.registryAccess(), level.getRandom().nextBoolean() ? ModBlocks.OBSIDIAN_CAKE_FAMILY.getBaseCake().get() : null), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    public IntegerProperty getBites() {
        return null;
    }
}
