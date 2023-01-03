package einstein.jmc.data.generators;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public class RecipesGenerator extends FabricRecipeProvider {

    public RecipesGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> consumer) {
        ModRecipes.init(consumer);
    }
}
