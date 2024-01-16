package einstein.jmc.block;

import einstein.jmc.block.cake.effects.CakeEffects;
import org.jetbrains.annotations.Nullable;

public interface CakeEffectsHolder {

    @Nullable
    CakeEffects justMoreCakes$getCakeEffects();

    void justMoreCakes$setCakeEffects(@Nullable CakeEffects effects);
}
