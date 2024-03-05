package einstein.jmc.compat;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.Util;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.common.block.DirectionalCakeBlock;
import net.mehvahdjukaar.amendments.common.block.DoubleCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class AmendmentsCompat {

    public static InteractionResult cakeUsedOnBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof DoubleCakeBlock doubleCake) {
            if (doubleCake.type.equals(CakeRegistry.VANILLA)) {
                if (BaseCakeBlock.isUneaten(state, pos, level)) {
                    BlockPos abovePos = pos.above();
                    if (level.getBlockState(abovePos).canBeReplaced()) {
                        BlockState newState = ModBlocks.VANILLA_CAKE_FAMILY.getThreeTieredCake().get().defaultBlockState();

                        level.setBlockAndUpdate(abovePos, newState);
                        level.setBlockAndUpdate(pos, BaseThreeTieredCakeBlock.createLowerState(newState.getBlock(), true));
                        Block.pushEntitiesUp(Blocks.AIR.defaultBlockState(), newState, level, abovePos);
                        level.gameEvent(player, GameEvent.BLOCK_CHANGE, abovePos);
                        level.playSound(null, abovePos, newState.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 1, 1);
                        player.awardStat(Stats.ITEM_USED.get(Items.CAKE));

                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }

                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        else if (block instanceof DirectionalCakeBlock directionalCake) {
            if (directionalCake.type.equals(CakeRegistry.VANILLA)) {
                return Util.convertToTwoTieredCake(state, pos, level, player, stack);
            }
        }

        return InteractionResult.PASS;
    }
}
