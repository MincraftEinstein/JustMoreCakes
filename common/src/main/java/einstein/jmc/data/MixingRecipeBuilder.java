package einstein.jmc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.init.ModRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.function.Consumer;

public class MixingRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;
    private final NonNullList<Ingredient> ingredients;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public MixingRecipeBuilder(RecipeCategory category, NonNullList<Ingredient> ingredients, ItemLike result) {
        this.category = category;
        this.ingredients = ingredients;
        this.result = result.asItem();
    }

    public static MixingRecipeBuilder mixing(RecipeCategory category, ItemLike result, Ingredient... ingredients) {
        if (ingredients.length > CeramicBowlBlockEntity.SLOT_COUNT) {
            throw new IllegalStateException("Too many ingredients for mixing recipe. The max is 4");
        }

        NonNullList<Ingredient> ingredientsList = NonNullList.create();
        Collections.addAll(ingredientsList, ingredients);
        return new MixingRecipeBuilder(category, ingredientsList, result);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
        advancement.addCriterion(name, trigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
        if (advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipeId);
        }

        advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
                .rewards(AdvancementRewards.Builder.recipe(recipeId)).requirements(RequirementsStrategy.OR);
        consumer.accept(new Result(recipeId, ingredients, result, recipeId.withPrefix("recipes/" + category.getFolderName() + "/"), advancement));
    }

    public record Result(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, Item result,
                         ResourceLocation advancementId, Advancement.Builder advancement) implements FinishedRecipe{

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray jsonIngredients = new JsonArray(CeramicBowlBlockEntity.SLOT_COUNT);

            for (Ingredient ingredient : ingredients) {
                jsonIngredients.add(ingredient.toJson());
            }

            json.add("ingredients", jsonIngredients);
            json.addProperty("result", BuiltInRegistries.ITEM.getKey(result).toString());
        }

        @Override
        public ResourceLocation getId() {
            return recipeId;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return ModRecipes.MIXING_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
