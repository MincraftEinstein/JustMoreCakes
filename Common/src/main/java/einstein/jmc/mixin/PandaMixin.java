package einstein.jmc.mixin;

import einstein.jmc.util.Util;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(Panda.class)
public class PandaMixin {

    @Redirect(method = "pickUpItem", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Panda;PANDA_ITEMS:Ljava/util/function/Predicate;"))
    private Predicate<ItemEntity> redirectPandaItems() {
        return Util.pandaItems();
    }

    @Inject(method = "isFoodOrCake", at = @At("RETURN"), cancellable = true)
    private void isFoodOrCake(ItemStack stack, CallbackInfoReturnable<Boolean> returnable) {
        returnable.setReturnValue(returnable.getReturnValue() || Util.isCake().test(stack));
    }

    @Mixin(Panda.PandaSitGoal.class)
    public static class PandaSitGoalMixin {

        @Redirect(method = {"canUse", "start"}, at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Panda;PANDA_ITEMS:Ljava/util/function/Predicate;"))
        private Predicate<ItemEntity> redirectPandaItems() {
            return Util.pandaItems();
        }
    }
}
