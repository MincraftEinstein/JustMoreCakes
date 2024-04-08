package einstein.jmc.item.crafting;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MixingRecipe implements Recipe<CeramicBowlBlockEntity> {

    protected final ResourceLocation id;
    protected final NonNullList<CountedIngredient> ingredients;
    protected final ItemStack result;
    protected final ResourceLocation contentsId;
    protected final int mixingTime;

    public MixingRecipe(ResourceLocation id, NonNullList<CountedIngredient> ingredients, ItemStack result, ResourceLocation contentsId, int mixingTime) {
        this.id = id;
        this.ingredients = ingredients;
        this.result = result;
        this.contentsId = contentsId;
        this.mixingTime = mixingTime;
    }

    @Override
    public boolean matches(CeramicBowlBlockEntity container, Level level) {
        int matches = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (ingredients.stream().anyMatch(ingredient -> ingredient.ingredient().test(stack)
                        && (!stack.isStackable() || ingredient.count() <= stack.getCount()))) {
                    matches++;
                }
            }
        }

        return matches == ingredients.size();
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

    public int getMixingTime() {
        return mixingTime;
    }

    public ResourceLocation getContentsId() {
        return contentsId;
    }

    public void consumeIngredients(CeramicBowlBlockEntity container) {
        for (CountedIngredient countedIngredient : ingredients) {
            Ingredient ingredient = countedIngredient.ingredient();
            int count = countedIngredient.count();
            for (int i = 0; i < container.getContainerSize(); i++) {
                ItemStack stack = container.getItem(i);
                if (!stack.isEmpty() && ingredient.test(stack)) {
                    stack.shrink(count);
                    if (stack.isEmpty()) {
                        container.setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
