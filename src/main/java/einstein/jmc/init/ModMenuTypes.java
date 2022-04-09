package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.menu.CakeOvenMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModMenuTypes {

	public static final MenuType<CakeOvenMenu> CAKE_OVEN = RegistryHandler.registerMenuType(JustMoreCakes.MODID, "cake_oven", CakeOvenMenu::new);	
}
