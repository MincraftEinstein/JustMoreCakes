package einstein.jmc.compat.rei;

import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

import static einstein.jmc.compat.rei.ModREICommonPlugin.CAKE_OVEN;

public class ModREIClientPlugin implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new CakeOvenRecipeCategory());
        registry.addWorkstations(CAKE_OVEN, EntryStacks.of(ModBlocks.CAKE_OVEN.get()));
        registry.addWorkstations(BuiltinPlugin.FUEL, EntryStacks.of(ModBlocks.CAKE_OVEN.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(CakeOvenRecipe.class, ModRecipes.CAKE_OVEN_RECIPE.get(), CakeOvenDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(92, 32, 28, 23), CakeOvenScreen.class, CAKE_OVEN);
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(SimpleTransferHandler.create(CakeOvenMenu.class, CAKE_OVEN, new SimpleTransferHandler.IntRange(0, 4)));
    }
}
