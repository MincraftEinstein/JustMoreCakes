package einstein.jmc.compat.rei;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleMenuInfoProvider;

public class REIPlugin implements REIServerPlugin {

    public static final CategoryIdentifier<CakeOvenDisplay> CAKE_OVEN = CategoryIdentifier.of(JustMoreCakes.MOD_ID, "cake_oven");

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(CAKE_OVEN, CakeOvenDisplay.serializer());
    }

    @Override
    public void registerMenuInfo(MenuInfoRegistry registry) {
        registry.register(CAKE_OVEN, CakeOvenMenu.class, SimpleMenuInfoProvider.of(CakeOvenMenuInfo::new));
    }
}
