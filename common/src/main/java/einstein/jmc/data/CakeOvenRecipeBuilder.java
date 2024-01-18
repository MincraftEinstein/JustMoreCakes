package einstein.jmc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.util.CakeFamily;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class CakeOvenRecipeBuilder implements RecipeBuilder, CakeOvenConstants {

    private final RecipeCategory category;
    private final Item result;
    private final NonNullList<Ingredient> ingredients;
    private final float experience;
    private final int cookingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private CakeOvenRecipeBuilder(RecipeCategory category, NonNullList<Ingredient> ingredients, ItemLike result, float experience, int cookingTime) {
        this.category = category;
        this.result = result.asItem();
        this.ingredients = ingredients;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    public static CakeOvenRecipeBuilder cakeBaking(ItemLike result, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        if (ingredients.length > INGREDIENT_SLOT_COUNT) {
            throw new IllegalArgumentException("Too many ingredients for cake oven recipe. The max is 4");
        }

        NonNullList<Ingredient> ingredientsList = NonNullList.create();
        Collections.addAll(ingredientsList, ingredients);
        return new CakeOvenRecipeBuilder(category, ingredientsList, result, experience, cookingTime);
    }

    public static CakeOvenRecipeBuilder cakeBaking(CakeFamily family, float experience, int cookingTime, RecipeCategory category, Ingredient... ingredients) {
        return cakeBaking(family.getBaseCake().get(), experience, cookingTime, category, ingredients);
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
    public void save(RecipeOutput output, ResourceLocation id) {
        ensureValid(id);
        Advancement.Builder builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(AdvancementRequirements.Strategy.OR);
        criteria.forEach(builder::addCriterion);
        output.accept(new Result(id, ingredients, result, experience, cookingTime, builder.build(id.withPrefix("recipes/" + category.getFolderName() + "/"))));
    }

    private void ensureValid(ResourceLocation recipeId) {
        if (criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }
    }

    public record Result(ResourceLocation id, NonNullList<Ingredient> ingredients, Item result, float experience, int cookingTime, AdvancementHolder advancement) implements FinishedRecipe {

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray jsonIngredients = new JsonArray(INGREDIENT_SLOT_COUNT);

            for (Ingredient ingredient : ingredients) {
                jsonIngredients.add(ingredient.toJson(false));
            }

            json.add("ingredients", jsonIngredients);
            json.addProperty("result", BuiltInRegistries.ITEM.getKey(result).toString());
            json.addProperty("experience", experience);
            json.addProperty("cookingTime", cookingTime);
        }

        @Override
        public RecipeSerializer<?> type() {
            return ModRecipes.CAKE_OVEN_SERIALIZER.get();
        }
    }
}
