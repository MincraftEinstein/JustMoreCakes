package einstein.jmc.block;

import einstein.jmc.data.effects.CakeEffects;
import org.jetbrains.annotations.Nullable;

public interface CakeEffectsHolder {

    @Nullable
    CakeEffects justMoreCakes$getCakeEffects();

    void justMoreCakes$setCakeEffects(@Nullable CakeEffects effects);

    default void clear() {
        justMoreCakes$setCakeEffects(null);
    }
}
