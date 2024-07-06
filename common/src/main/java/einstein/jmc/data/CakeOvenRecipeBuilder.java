package einstein.jmc.data;

import einstein.jmc.item.crafting.CakeOvenRecipe;
import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CakeOvenRecipeBuilder implements RecipeBuilder, CakeOvenConstants {

    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final NonNullList<Ingredient> ingredients;
    private final float experience;
    private final int cookingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private CakeOvenRecipeBuilder(RecipeCategory category, NonNullList<Ingredient> ingredients, ItemLike result, int count, float experience, int cookingTime) {
        this.category = category;
        this.result = result.asItem();
        this.count = count;
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public static CakeOvenRecipeBuilder cakeBaking(ItemLike result, int count, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        if (ingredients.length > INGREDIENT_SLOT_COUNT) {
            throw new IllegalArgumentException("Too many ingredients for cake oven recipe. The max is 4");
        }

        NonNullList<Ingredient> ingredientsList = NonNullList.create();
        Collections.addAll(ingredientsList, ingredients);
        return new CakeOvenRecipeBuilder(category, ingredientsList, result, count, experience, cookingTime);
    }

    public static CakeOvenRecipeBuilder cakeBaking(ItemLike result, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        return cakeBaking(result, 1, experience, cookingTime, category, ingredients);
    }

    public static CakeOvenRecipeBuilder cakeBaking(CakeFamily family, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        return cakeBaking(family, 1, experience, cookingTime, category, ingredients);
    }

    public static CakeOvenRecipeBuilder cakeBaking(CakeFamily family, int count, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        return cakeBaking(family.getBaseCake().get(), count, experience, cookingTime, category, ingredients);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe: " + recipeId);
        }

        Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId))
                .requirements(AdvancementRequirements.Strategy.OR);

        criteria.forEach(builder::addCriterion);

        output.accept(recipeId,
                new CakeOvenRecipe(ingredients, new ItemStack(result, count), experience, cookingTime),
                builder.build(recipeId.withPrefix("recipes/" + category.getFolderName() + "/"))
        );
    }
}
