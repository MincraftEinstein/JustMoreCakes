package einstein.jmc.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import einstein.jmc.init.ModFeatureFlags;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagRegistry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureFlags.class)
public class FeatureFlagsMixin {

    @Inject(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/flag/FeatureFlagRegistry$Builder;build()Lnet/minecraft/world/flag/FeatureFlagRegistry;"))
    private static void clInit(CallbackInfo ci, @Local FeatureFlagRegistry.Builder builder) {
        ModFeatureFlags.init(builder);
    }
}
