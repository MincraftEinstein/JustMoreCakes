package einstein.jmc.mixin;

import einstein.jmc.data.providers.ModGameEventTags;
import einstein.jmc.init.ModPotions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VibrationSystem.Listener.class)
public class VibrationSystemListenerMixin {

    @Redirect(method = "handleGameEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/gameevent/vibrations/VibrationSystem$User;isValidVibration(Lnet/minecraft/world/level/gameevent/GameEvent;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)Z"))
    private boolean isValidVibration(VibrationSystem.User instance, GameEvent event, GameEvent.Context context) {
        Entity entity = context.sourceEntity();
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(ModPotions.STEALTH_EFFECT.get())) {
                if (event.is(ModGameEventTags.STEALTH_EFFECT_BLOCKS)) {
                    return false;
                }
            }
        }
        return instance.isValidVibration(event, context);
    }
}