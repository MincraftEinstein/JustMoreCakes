package einstein.jmc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import einstein.jmc.data.packs.ModItemTags;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Panda.class)
public class PandaMixin {

    @ModifyExpressionValue(method = "lambda$static$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z", ordinal = 1))
    private static boolean isPandaItem(boolean original, ItemEntity itemEntity) {
        return original || itemEntity.getItem().is(ModItemTags.CAKES);
    }

    @ModifyReturnValue(method = "isFoodOrCake", at = @At("RETURN"))
    private boolean isFoodOrCake(boolean original, ItemStack stack) {
        return original || stack.is(ModItemTags.CAKES);
    }
}
