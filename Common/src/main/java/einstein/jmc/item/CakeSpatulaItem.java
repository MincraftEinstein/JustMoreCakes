package einstein.jmc.item;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.data.providers.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class CakeSpatulaItem extends Item {

    public CakeSpatulaItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            BlockPos lookPos = result.getBlockPos();
            BlockState state = level.getBlockState(lookPos);
            Block block = state.getBlock();
            if (state.is(ModBlockTags.CAKE_SPATULA_USABLE)) {
//                if (block instanceof BaseCakeBlock || block instanceof CakeBlock) {
//                    if () {
//
//                    }
//                }
                level.destroyBlock(lookPos, true);
                return InteractionResultHolder.success(player.getItemInHand(hand));
            }
        }
        return super.use(level, player, hand);
    }
}
