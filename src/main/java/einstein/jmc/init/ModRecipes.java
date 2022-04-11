package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.CakeOvenRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ModRecipes {
	
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JustMoreCakes.MODID);

	public static final RegistryObject<RecipeSerializer<CakeOvenRecipe>> CAKE_OVEN_SERIALIZER = RECIPE_SERIALIZERS.register("cake_baking", () -> new CakeOvenRecipeSerializer(CakeOvenRecipe::new, 100));
	public static final RecipeType<CakeOvenRecipe> CAKE_OVEN_RECIPE = new RecipeType<CakeOvenRecipe>() {
		@Override
		public String toString() {
			return "cake_baking";
		}
	};
}
