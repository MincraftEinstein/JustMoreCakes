package einstein.jmc.block;

import einstein.jmc.data.cakeeffect.CakeEffects;
import einstein.jmc.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface CakeEffectsHolder {

    @Nullable
    CakeEffects getCakeEffects();

    void setCakeEffects(CakeEffects effects);

    default void addCakeEffects(CakeEffects effects) {
        if (getCakeEffects() == null) {
            setCakeEffects(effects);
        }
        else {
            List<CakeEffects.MobEffectHolder> list = new ArrayList<>();
            getCakeEffects().mobEffects().forEach(currentEffects -> effects.mobEffects().forEach(newEffects -> {
                if (Util.getMobEffectId(currentEffects.effect()).equals(Util.getMobEffectId(newEffects.effect()))) {
                    list.add(new CakeEffects.MobEffectHolder(currentEffects.effect(),
                            Math.max(currentEffects.duration().orElse(0), newEffects.duration().orElse(0)),
                            Math.max(currentEffects.amplifier().orElse(0), newEffects.amplifier().orElse(0))));
                }
                else {
                    if (!list.contains(currentEffects)) {
                        list.add(currentEffects);
                    }
                    if (!list.contains(newEffects)) {
                        list.add(newEffects);
                    }
                }
            }));
            setCakeEffects(new CakeEffects(effects.cake(), list));
        }
    }
}
