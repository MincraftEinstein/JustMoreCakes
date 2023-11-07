package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.CakeOvenRecipeSerializer;
import einstein.jmc.platform.Services;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class ModRecipes {

    public static final Supplier<RecipeSerializer<CakeOvenRecipe>> CAKE_OVEN_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("cake_baking", CakeOvenRecipeSerializer::new);
    public static final Supplier<RecipeType<CakeOvenRecipe>> CAKE_OVEN_RECIPE = Services.REGISTRY.registerRecipeType("cake_baking", () -> new RecipeType<>() {

        @Override
        public String toString() {
            return JustMoreCakes.MOD_ID + ":cake_baking";
        }
    });

    public static void init() {
    }
}
