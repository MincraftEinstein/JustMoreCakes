package einstein.jmc.item.crafting;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MixingRecipe implements Recipe<ContainerRecipeInput> {

    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack result;
    protected final ResourceLocation contentsId;
    protected final int mixingTime;

    public MixingRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation contentsId, int mixingTime) {
        this.ingredients = ingredients;
        this.result = result;
        this.contentsId = contentsId;
        this.mixingTime = mixingTime;
    }

    @Override
    public boolean matches(ContainerRecipeInput input, Level level) {
        StackedContents contents = new StackedContents();
        int stacks = 0;

        for (int i = 0; i < input.size(); i++) {
            if (i != CeramicBowlBlockEntity.RESULT_SLOT) {
                ItemStack stack = input.getItem(i);
                if (!stack.isEmpty()) {
                    stacks++;
                    contents.accountStack(stack, 1);
                }
            }
        }

        return stacks == ingredients.size() && contents.canCraft(this, null);
    }

    @Override
    public ItemStack assemble(ContainerRecipeInput input, HolderLookup.Provider provider) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int x, int y) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return result;
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
        return new ItemStack(ModBlocks.CERAMIC_BOWL.get());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public int getMixingTime() {
        return mixingTime;
    }

    public ResourceLocation getContentsId() {
        return contentsId;
    }

    public void consumeIngredients(CeramicBowlBlockEntity container) {
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (i != CeramicBowlBlockEntity.RESULT_SLOT) {
                    ItemStack stack = container.getItem(i);
                    if (!stack.isEmpty() && ingredient.test(stack)) {
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
