package einstein.jmc.block;

import einstein.jmc.data.cakeeffect.CakeEffects;
import org.jetbrains.annotations.Nullable;

public interface CakeEffectsHolder {

    @Nullable
    CakeEffects getCakeEffects();

    void setCakeEffects(@Nullable CakeEffects effects);
}
