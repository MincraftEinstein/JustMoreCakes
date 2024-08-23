package einstein.jmc.data.packs.providers.farmersdelight;

import einstein.jmc.data.packs.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.concurrent.CompletableFuture;

public class FDSupportRecipeProvider extends RecipeProvider {

    public FDSupportRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(RecipeOutput output) {
        ModRecipes.fdSupportRecipes(output);
    }
}
