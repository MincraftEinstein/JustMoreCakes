package einstein.jmc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Optional;

public record CakeEffects(List<MobEffectHolder> mobEffects) {

    public static final Codec<CakeEffects> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            MobEffectHolder.MOB_EFFECT_CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(inst, CakeEffects::new));

    public record MobEffectHolder(MobEffect effect, Optional<Integer> duration, Optional<Integer> amplifier) {

        public static final Codec<MobEffectHolder> MOB_EFFECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                ForgeRegistries.MOB_EFFECTS.getCodec().fieldOf("mob_effect").forGetter(MobEffectHolder::effect),
                Codec.INT.optionalFieldOf("duration").forGetter(MobEffectHolder::duration),
                Codec.INT.optionalFieldOf("amplifier").forGetter(MobEffectHolder::amplifier)
        ).apply(inst, MobEffectHolder::new));

        public MobEffectHolder(MobEffect effect, int duration, int amplifier) {
            this(effect, Optional.of(duration), Optional.of(amplifier));
        }

        public MobEffectHolder(MobEffect effect, int duration) {
            this(effect, Optional.of(duration), Optional.empty());
        }

        public MobEffectHolder(MobEffect effect) {
            this(effect, Optional.empty(), Optional.empty());
        }
    }
}
