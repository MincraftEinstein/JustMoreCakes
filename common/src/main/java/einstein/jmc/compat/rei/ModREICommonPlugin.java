package einstein.jmc.compat.rei;

import einstein.jmc.JustMoreCakes;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

public class ModREICommonPlugin implements REIServerPlugin {

    public static final CategoryIdentifier<CakeOvenDisplay> CAKE_OVEN = CategoryIdentifier.of(JustMoreCakes.MOD_ID, "cake_oven");
    public static final CategoryIdentifier<MixingDisplay> MIXING = CategoryIdentifier.of(JustMoreCakes.MOD_ID, "mixing");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CAKE_OVEN, CakeOvenDisplay.serializer());
        registry.register(MIXING, MixingDisplay.serializer());
    }
}
