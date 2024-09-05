package einstein.jmc.block.cake;

import einstein.jmc.damagesource.BlockDamageSource;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModTriggerTypes;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ObsidianCakeBlock extends BaseCakeBlock {

    public ObsidianCakeBlock(CakeVariant builder) {
        super(builder);
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

    @Override
    public boolean cutSlice(Level level, BlockPos pos, BlockState state, Player player, ItemStack stack) {
        if (stack.isCorrectToolForDrops(state)) {
            return super.cutSlice(level, pos, state, player, stack);
        }
        return false;
    }

    public static void damage(Level level, LivingEntity entity) {
        entity.hurt(new BlockDamageSource(level.registryAccess(), level.getRandom().nextBoolean() ? ModBlocks.OBSIDIAN_CAKE_FAMILY.getBaseCake().get() : null), 2);
    }
}
