package einstein.jmc.item.crafting;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CakeOvenRecipe implements Recipe<ContainerRecipeInput>, CakeOvenConstants {

    protected final ItemStack result;
    protected final float experience;
    protected final int cookingTime;
    protected final NonNullList<Ingredient> ingredients;

    public CakeOvenRecipe(NonNullList<Ingredient> ingredients, ItemStack result, float experience, int cookingTime) {
        this.ingredients = ingredients;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(ContainerRecipeInput input, Level level) {
        StackedContents contents = new StackedContents();
        int stacks = 0;

        for (int i = 0; i < input.size(); i++) {
            if (i != RESULT_SLOT && i != FUEL_SLOT) {
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

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CAKE_OVEN_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CAKE_OVEN_RECIPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.CAKE_OVEN.get());
    }

    public void consumeIngredients(Container container, NonNullList<ItemStack> remainingItems) {
        for (Ingredient ingredient : ingredients) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                if (i != RESULT_SLOT && i != FUEL_SLOT) {
                    ItemStack stack = container.getItem(i);
                    if (!stack.isEmpty() && ingredient.test(stack)) {
                        // Remaining item info must be gotten before shrinking otherwise if this is the last item in the stack, the stack will forget
                        boolean hasRemainingItem = stack.getItem().hasCraftingRemainingItem();
                        Item v = stack.getItem().getCraftingRemainingItem();
                        ItemStack remainingStack = v == null ? ItemStack.EMPTY : new ItemStack(v);
                        int i2 = 0;

                        for (int i3 = 0; i3 < remainingItems.size(); i3++) {
                            ItemStack remainingItem = remainingItems.get(i3);
                            if (!remainingItem.isEmpty()) {
                                if (ItemStack.isSameItemSameComponents(remainingStack, remainingItem)) {
                                    remainingItem.grow(remainingStack.getCount());
                                    remainingItems.set(i3, remainingItem);
                                    remainingStack = remainingItems.get(i3);
                                    i2 = i3;
                                    break;
                                }
                            }
                            else {
                                remainingItem = remainingStack.copy();
                                remainingItems.set(i3, remainingItem);
                                remainingStack = remainingItems.get(i3);
                                i2 = i3;
                                break;
                            }
                        }

                        stack.shrink(1);

                        if (stack.isEmpty() && hasRemainingItem) {
                            container.setItem(i, remainingStack.copy());
                            remainingItems.set(i2, ItemStack.EMPTY);
                        }
                        else if (stack.isEmpty()) {
                            container.setItem(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }
    }
}
