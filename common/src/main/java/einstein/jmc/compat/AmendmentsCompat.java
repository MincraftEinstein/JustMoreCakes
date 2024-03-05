package einstein.jmc.compat;

import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.common.block.DirectionalCakeBlock;
import net.mehvahdjukaar.amendments.common.block.DoubleCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AmendmentsCompat {

    public static InteractionResult cakeUsedOnBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof DoubleCakeBlock doubleCake) {
            if (doubleCake.type.equals(CakeRegistry.VANILLA)) {
                return BaseThreeTieredCakeBlock.convertTo(ModBlocks.VANILLA_CAKE_FAMILY, state, pos, level, player, stack);
            }
        }
        else if (block instanceof DirectionalCakeBlock directionalCake) {
            if (directionalCake.type.equals(CakeRegistry.VANILLA)) {
                return BaseTwoTieredCakeBlock.convertTo(ModBlocks.VANILLA_CAKE_FAMILY, state, pos, level, player, stack);
            }
        }

        return InteractionResult.PASS;
    }
}
