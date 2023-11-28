package einstein.jmc.data.cakeeffect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public record CakeEffects(Block cake, List<MobEffectHolder> mobEffects) {

    public static final Codec<CakeEffects> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("cake").forGetter(CakeEffects::cake),
            MobEffectHolder.MOB_EFFECT_CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(inst, CakeEffects::new));

    public record MobEffectHolder(MobEffect effect, Optional<Integer> duration, Optional<Integer> amplifier) {

        public static final Codec<MobEffectHolder> MOB_EFFECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
                BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("mob_effect").forGetter(MobEffectHolder::effect),
                Codec.intRange(-1, Integer.MAX_VALUE).optionalFieldOf("duration").forGetter(MobEffectHolder::duration),
                Codec.intRange(0, 255).optionalFieldOf("amplifier").forGetter(MobEffectHolder::amplifier)
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
