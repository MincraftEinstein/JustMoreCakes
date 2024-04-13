package einstein.jmc.compat.rei;

import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.util.CakeOvenConstants;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CakeOvenDisplay extends BasicDisplay implements SimpleGridMenuDisplay {

    private final float experience;
    private final int cookingTime;

    public CakeOvenDisplay(CakeOvenRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()),
                Collections.singletonList(EntryIngredients.of(recipe.getResultItem(registryAccess()))),
                recipe, recipe.getExperience(), recipe.getCookingTime());
    }

    public CakeOvenDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> results, CompoundTag tag) {
        this(ingredients, results, (CakeOvenRecipe) RecipeManagerContext.getInstance()
                .byId(tag, "location"), tag.getFloat("experience"), tag.getInt("cookingTime"));
    }

    public CakeOvenDisplay(List<EntryIngredient> ingredients, List<EntryIngredient> results, CakeOvenRecipe recipe, float experience, int cookingTime) {
        super(fillWithEmpty(ingredients), results, Optional.ofNullable(recipe).map(CakeOvenRecipe::getId));
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public static List<EntryIngredient> fillWithEmpty(List<EntryIngredient> inputs) {
        if (inputs.size() == CakeOvenConstants.INGREDIENT_SLOT_COUNT) {
            return inputs;
        }

        List<EntryIngredient> ingredients = new ArrayList<>(inputs);
        for (int i = 0; i < CakeOvenConstants.INGREDIENT_SLOT_COUNT - inputs.size(); i++) {
            ingredients.add(EntryIngredients.of(ItemStack.EMPTY));
        }

        return ingredients;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ModREICommonPlugin.CAKE_OVEN;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public static BasicDisplay.Serializer<CakeOvenDisplay> serializer() {
        return Serializer.ofRecipeLess(CakeOvenDisplay::new, (display, tag) -> {
            tag.putFloat("experience", display.getExperience());
            tag.putInt("cookingTime", display.getCookingTime());
        });
    }
}
