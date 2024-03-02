package einstein.jmc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.jmc.data.packs.ModItemTags;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(Panda.class)
public class PandaMixin {

    @ModifyExpressionValue(method = "pickUpItem", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"))
    private boolean isPandaItem(boolean original, ItemEntity itemEntity) {
        return original || itemEntity.getItem().is(ModItemTags.CAKES);
    }

    @ModifyReturnValue(method = "isFoodOrCake", at = @At("RETURN"))
    private boolean isFoodOrCake(boolean original, ItemStack stack) {
        return original || stack.is(ModItemTags.CAKES);
    }

    @Mixin(Panda.PandaSitGoal.class)
    public static class PandaSitGoal {

        @Redirect(method = {"canUse", "start"}, at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Panda;PANDA_ITEMS:Ljava/util/function/Predicate;"))
        private Predicate<ItemEntity> isPandaItem() {
            return Panda.PANDA_ITEMS.or(itemEntity -> itemEntity.getItem().is(ModItemTags.CAKES));
        }
    }
}
