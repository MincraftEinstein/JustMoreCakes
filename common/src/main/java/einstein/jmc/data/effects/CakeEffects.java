package einstein.jmc.data.effects;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.registration.family.CakeFamily;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;

public record CakeEffects(CakeEffectsHolder holder, List<MobEffectInstance> mobEffects) {

    private static final Codec<CakeEffects> BLOCK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CakeEffectsHolder.BLOCK_CODEC.fieldOf("block").forGetter(CakeEffects::holder),
            MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(instance, CakeEffects::new));

    private static final Codec<CakeEffects> FAMILY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CakeFamily.CODEC.fieldOf("family").forGetter(effects -> (CakeFamily) effects.holder()),
            MobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(instance, CakeEffects::new));

    public static final Codec<CakeEffects> CODEC = Codec.xor(BLOCK_CODEC, FAMILY_CODEC).xmap(either ->
            either.map(cakeEffects -> cakeEffects, cakeEffects -> cakeEffects), cakeEffects -> {
        if (cakeEffects.holder() instanceof Block) {
            return Either.left(cakeEffects);
        }
        else if (cakeEffects.holder() instanceof CakeFamily) {
            return Either.right(cakeEffects);
        }
        throw new UnsupportedOperationException("This is neither a block nor a family");
    });

    public static final StreamCodec<ByteBuf, CakeEffects> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public CakeEffects(CakeEffectsHolder holder, MobEffectInstance... instances) {
        this(holder, List.of(instances));
    }

    public CakeEffects(Supplier<? extends CakeEffectsHolder> supplier, MobEffectInstance... instances) {
        this(supplier.get(), instances);
    }
}
