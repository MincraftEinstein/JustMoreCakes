package einstein.jmc.mixin.admendments;

import einstein.jmc.block.cake.BaseCakeBlock;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(CakeRegistry.class)
public class CakeRegistryMixin {

    @Inject(method = "detectTypeFromBlock", at = @At("HEAD"), remap = false, cancellable = true)
    private void detectTypeFromBlock(Block block, ResourceLocation blockId, CallbackInfoReturnable<Optional<CakeRegistry.CakeType>> cir) {
        if (block instanceof BaseCakeBlock) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
