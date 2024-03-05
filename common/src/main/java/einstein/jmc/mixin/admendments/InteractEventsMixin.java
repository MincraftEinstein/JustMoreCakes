package einstein.jmc.mixin.admendments;

import net.mehvahdjukaar.amendments.events.behaviors.InteractEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InteractEvents.class)
public class InteractEventsMixin {

    @Inject(method = "onItemUsedOnBlock", at = @At(
            value = "INVOKE",
            target = "Lnet/mehvahdjukaar/amendments/events/behaviors/ItemUseOnBlock;tryPerformingAction(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
            shift = At.Shift.BEFORE
    ), remap = false, cancellable = true)
    private static void beforeTryPreformAction(Player player, Level level, ItemStack stack, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (state.is(Blocks.CAKE) && stack.is(Blocks.CAKE.asItem())) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
