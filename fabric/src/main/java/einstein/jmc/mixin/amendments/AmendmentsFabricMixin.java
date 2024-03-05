package einstein.jmc.mixin.amendments;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeFamily;
import net.mehvahdjukaar.amendments.Amendments;
import net.mehvahdjukaar.amendments.fabric.AmendmentsFabric;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static einstein.jmc.JustMoreCakes.loc;

@Mixin(AmendmentsFabric.class)
public class AmendmentsFabricMixin {

    @Inject(method = "shouldRemap", at = @At("HEAD"), cancellable = true)
    private static void shouldRemap(String namespace, String path, CallbackInfoReturnable<ResourceLocation> cir) {
        if (namespace.equals(Amendments.MOD_ID)) {
            if (path.equals("jmc/double_poison_cake")) {
                cir.setReturnValue(loc(ModBlocks.POISON_CAKE_VARIANT.getCakeName()));
            }
            else if (path.equals("jmc/double_tnt_cake")) {
                cir.setReturnValue(loc(ModBlocks.TNT_CAKE_VARIANT.getCakeName()));
            }

            for (CakeFamily family : CakeFamily.REGISTERED_CAKE_FAMILIES.values()) {
                if (!family.equals(ModBlocks.VANILLA_CAKE_FAMILY)) {
                    if (path.equals("jmc/double_" + family.getBaseCakeName())) {
                        cir.setReturnValue(loc(family.getTwoTieredVariant().getCakeName()));
                    }
                }
            }
        }
    }
}
