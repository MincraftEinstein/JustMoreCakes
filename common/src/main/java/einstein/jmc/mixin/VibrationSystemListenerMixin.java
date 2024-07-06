package einstein.jmc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import einstein.jmc.data.packs.ModGameEventTags;
import einstein.jmc.init.ModPotions;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VibrationSystem.Listener.class)
public class VibrationSystemListenerMixin {

    @ModifyExpressionValue(method = "handleGameEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$User;isValidVibration(Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z"))
    private boolean isValidVibration(boolean original, ServerLevel level, Holder<GameEvent> event, GameEvent.Context context) {
        Entity entity = context.sourceEntity();
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(ModPotions.STEALTH_EFFECT.get())) {
                if (event.is(ModGameEventTags.STEALTH_EFFECT_BLOCKS)) {
                    return false;
                }
            }
        }
        return original;
    }
}
