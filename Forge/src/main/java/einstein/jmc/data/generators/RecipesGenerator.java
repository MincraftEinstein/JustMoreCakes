package einstein.jmc.data.generators;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.function.Consumer;

public class RecipesGenerator extends RecipeProvider {

    public RecipesGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ModRecipes.init(consumer);
    }

    @Override
    public String getName() {
        return "JustMoreCakes crafting recipes";
    }
}
