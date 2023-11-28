package einstein.jmc.block;

import einstein.jmc.data.cakeeffect.CakeEffects;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface CakeEffectsHolder {

    @Nullable
    CakeEffects getCakeEffects();

    void setCakeEffects(CakeEffects effects);

    default void addCakeEffects(CakeEffects effects) {
        if (getCakeEffects() == null || getCakeEffects().mobEffects().isEmpty()) {
            setCakeEffects(effects);
            return;
        }

        List<CakeEffects.MobEffectHolder> holders = new ArrayList<>();

        effects.mobEffects().forEach(holder -> {
            boolean foundMatch = false;
            for (CakeEffects.MobEffectHolder currentHolder : getCakeEffects().mobEffects()) {
                if (holder.effect().equals(currentHolder.effect())) {
                    holders.add(new CakeEffects.MobEffectHolder(holder.effect(),
                            Math.max(holder.duration().orElse(0), currentHolder.duration().orElse(0)),
                            Math.max(holder.amplifier().orElse(0), currentHolder.amplifier().orElse(0))
                    ));
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                holders.add(holder);
            }
        });

        setCakeEffects(new CakeEffects(effects.cake(), holders));
    }
}
