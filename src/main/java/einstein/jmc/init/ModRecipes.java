package einstein.jmc.init;

import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.CakeOvenRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@SuppressWarnings({"unchecked", "rawtypes"})
@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModRecipes {
	
	public static final RecipeType<CakeOvenRecipe> CAKE_OVEN_RECIPE = RegistryHandler.registerRecipeType(JustMoreCakes.MODID, "cake_baking");
	public static final RecipeSerializer<CakeOvenRecipe> CAKE_OVEN_SERIALIZER = RegistryHandler.registerSerializer(JustMoreCakes.MODID, "cake_baking", new CakeOvenRecipeSerializer(CakeOvenRecipe::new, 100));
}
