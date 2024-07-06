package einstein.jmc.init;

import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import einstein.jmc.platform.Services;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class ModMenuTypes {

    public static final Supplier<MenuType<CakeOvenMenu>> CAKE_OVEN = Services.REGISTRY.registerMenuType("cake_oven", () -> Services.REGISTRY.createMenuType(CakeOvenMenu::new));

    public static void init() {
    }
}
