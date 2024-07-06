package einstein.jmc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Optional;

public record SerializableMobEffectInstance(Holder<MobEffect> effect, Optional<Integer> duration, Optional<Integer> amplifier) {

    public static final Codec<SerializableMobEffectInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("mob_effect").forGetter(SerializableMobEffectInstance::effect),
            Codec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("duration").forGetter(SerializableMobEffectInstance::duration),
            Codec.intRange(0, 255).optionalFieldOf("amplifier").forGetter(SerializableMobEffectInstance::amplifier)
    ).apply(instance, SerializableMobEffectInstance::new));

    public SerializableMobEffectInstance(Holder<MobEffect> effect, int duration, int amplifier) {
        this(effect, Optional.of(duration), Optional.of(amplifier));
    }

    public SerializableMobEffectInstance(Holder<MobEffect> effect, int duration) {
        this(effect, duration, 0);
    }

    public SerializableMobEffectInstance(Holder<MobEffect> effect) {
        this(effect, 0);
    }

    public MobEffect getEffect() {
        return effect.value();
    }

    public MobEffectInstance toInstance() {
        return new MobEffectInstance(effect(), duration().orElse(0), amplifier().orElse(0));
    }
}
