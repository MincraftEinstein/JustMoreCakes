package einstein.jmc.mixin;

import einstein.jmc.util.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo info) {
        Util.livingEntityTick(level, (LivingEntity) (Object) this);
    }

    @Inject(at = @At("RETURN"), method = "jumpFromGround()V")
    public void jumpFromGround(CallbackInfo info) {
        Util.livingEntityJump((LivingEntity) (Object) this);
    }
}