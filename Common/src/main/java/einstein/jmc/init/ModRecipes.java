package einstein.jmc.init;

import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.CakeOvenRecipeSerializer;
import einstein.jmc.platform.Services;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class ModRecipes {

    public static final Supplier<RecipeSerializer<CakeOvenRecipe>> CAKE_OVEN_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("cake_baking", () -> new CakeOvenRecipeSerializer<>(CakeOvenRecipe::new, 100));
    public static final Supplier<RecipeType<CakeOvenRecipe>> CAKE_OVEN_RECIPE = Services.REGISTRY.registerRecipeType("", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return "cake_baking";
        }
    });

    public static void init() {}
}
