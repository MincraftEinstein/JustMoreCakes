package einstein.jmc.jei;

import java.util.Objects;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.client.gui.screens.inventory.CakeOvenScreen;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModMenuTypes;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.menu.cakeoven.CakeOvenMenu;
import einstein.jmc.util.CakeOvenConstants;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

	public static final RecipeType<CakeOvenRecipe> CAKE_OVEN = RecipeType.create(JustMoreCakes.MOD_ID, "cake_oven", CakeOvenRecipe.class);
	
	@Override
	public ResourceLocation getPluginUid() {
		return JustMoreCakes.loc("jei_plugin");
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		registration.addRecipeCategories(new CakeOvenRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
	}
	
	@Override
	@SuppressWarnings("resource")
	public void registerRecipes(IRecipeRegistration registration) {
		RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
		registration.addRecipes(CAKE_OVEN, recipeManager.getAllRecipesFor(ModRecipes.CAKE_OVEN_RECIPE.get()));
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.CAKE_OVEN.get()), CAKE_OVEN, RecipeTypes.FUELING);
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CakeOvenScreen.class, /*X*/ 92, /*Y*/ 32, /*Width*/ 28, /*Height*/ 23, CAKE_OVEN, RecipeTypes.FUELING);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(CakeOvenMenu.class, ModMenuTypes.CAKE_OVEN.get(), CAKE_OVEN, /*StartIndex*/ 0, /*SlotCount*/ CakeOvenConstants.INGREDIENT_SLOT_COUNT,
				/*InventoryStart*/ CakeOvenConstants.SLOT_COUNT, /*InventoryEnd*/ 36);
		registration.addRecipeTransferHandler(CakeOvenMenu.class, ModMenuTypes.CAKE_OVEN.get(), RecipeTypes.FUELING, /*StartIndex*/ CakeOvenConstants.FUEL_SLOT, /*SlotCount*/ 1,
				/*InventoryStart*/ CakeOvenConstants.SLOT_COUNT, /*InventoryEnd*/ 36);
	}
}
