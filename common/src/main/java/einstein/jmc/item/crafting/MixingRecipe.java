package einstein.jmc.item.crafting;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.util.RecipeMatcher;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class MixingRecipe implements Recipe<CeramicBowlBlockEntity> {

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;

    public MixingRecipe(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
    }

    @Override
    public boolean matches(CeramicBowlBlockEntity container, Level level) {
        List<ItemStack> inputs = new ArrayList<>();
        int stackCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                stackCount++;
                inputs.add(stack);
            }
        }

        return stackCount == ingredients.size() && RecipeMatcher.findMatches(inputs, ingredients) != null;
    }

    @Override
    public ItemStack assemble(CeramicBowlBlockEntity container, RegistryAccess access) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.MIXING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.MIXING_RECIPE.get();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.CERAMIC_BOWL.get()); // TODO change to whisk?
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public void consumeIngredients(CeramicBowlBlockEntity container) {
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
