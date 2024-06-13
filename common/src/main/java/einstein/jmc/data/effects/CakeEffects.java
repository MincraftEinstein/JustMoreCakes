package einstein.jmc.data.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.data.SerializableMobEffectInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record CakeEffects(CakeEffectsHolder holder, List<SerializableMobEffectInstance> mobEffects) {

    public static final Codec<CakeEffects> BLOCK_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").flatXmap(block -> {
                if (block instanceof CakeEffectsHolder holder) {
                    return DataResult.success(holder);
                }
                return DataResult.error(() -> block + " is not a valid block for cake effects");
            }, holder -> DataResult.success((Block) holder)).forGetter(CakeEffects::holder),
            SerializableMobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(instance, CakeEffects::new));

    public static final Codec<CakeEffects> FAMILY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("family").flatXmap(key -> {
                if (CakeFamily.REGISTERED_CAKE_FAMILIES.containsKey(key)) {
                    return DataResult.success(CakeFamily.REGISTERED_CAKE_FAMILIES.get(key));
                }
                return DataResult.error(() -> "Could not find cake family with registry key: {" + key + "}");
            }, family -> DataResult.success(family.getRegistryKey())).forGetter(effects -> (CakeFamily) effects.holder()),
            SerializableMobEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(CakeEffects::mobEffects)
    ).apply(instance, CakeEffects::new));
}
