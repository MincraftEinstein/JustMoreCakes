package einstein.jmc.init;

import einstein.jmc.platform.Services;
import einstein.jmc.platform.services.RegistryHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final Supplier<CreativeModeTab> JMC_TAB = Services.REGISTRY.registerCreativeModeTab("jmc_tab", builder ->
            builder.icon(() -> new ItemStack(ModBlocks.CHOCOLATE_CAKE.get())).title(Component.translatable("itemGroup.jmc.jmc_tab"))
                    .displayItems((displayParameters, output) ->
                            RegistryHelper.CREATIVE_TAB_ITEMS.forEach(cake -> output.accept(cake.get()))).build());

    public static void init() {
    }
}
