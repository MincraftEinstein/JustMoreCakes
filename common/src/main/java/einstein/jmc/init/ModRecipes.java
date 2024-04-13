package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.item.crafting.CakeOvenRecipeSerializer;
import einstein.jmc.item.crafting.MixingRecipe;
import einstein.jmc.item.crafting.MixingRecipeSerializer;
import einstein.jmc.platform.Services;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class ModRecipes {

    public static final Supplier<RecipeSerializer<CakeOvenRecipe>> CAKE_OVEN_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("cake_baking", CakeOvenRecipeSerializer::new);
    public static final Supplier<RecipeSerializer<MixingRecipe>> MIXING_SERIALIZER = Services.REGISTRY.registerRecipeSerializer("mixing", MixingRecipeSerializer::new);
    public static final Supplier<RecipeType<CakeOvenRecipe>> CAKE_OVEN_RECIPE = registerType("cake_baking");
    public static final Supplier<RecipeType<MixingRecipe>> MIXING_RECIPE = registerType("mixing");

    public static void init() {
    }

    private static <T extends Recipe<?>> Supplier<RecipeType<T>> registerType(String name) {
        return Services.REGISTRY.registerRecipeType(name, () -> new RecipeType<>() {

            @Override
            public String toString() {
                return JustMoreCakes.MOD_ID + ":" + name;
            }
        });
    }
}
