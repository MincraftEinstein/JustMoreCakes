package einstein.jmc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import einstein.jmc.data.effects.CakeEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public interface CakeEffectsHolder {

    Codec<CakeEffectsHolder> BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec().flatXmap(block -> {
        if (block instanceof CakeEffectsHolder holder) {
            return DataResult.success(holder);
        }
        return DataResult.error(() -> block + " is not a valid block for cake effects");
    }, holder -> DataResult.success((Block) holder));

    @Nullable
    CakeEffects justMoreCakes$getCakeEffects();

    void justMoreCakes$setCakeEffects(@Nullable CakeEffects effects);

    default void clear() {
        justMoreCakes$setCakeEffects(null);
    }
}
