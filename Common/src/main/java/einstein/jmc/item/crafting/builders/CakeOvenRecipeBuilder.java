package einstein.jmc.item.crafting.builders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import einstein.jmc.init.ModRecipes;
import einstein.jmc.util.CakeOvenConstants;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.function.Consumer;

public class CakeOvenRecipeBuilder implements RecipeBuilder, CakeOvenConstants {

	private final RecipeCategory category;
	private final Item result;
	private final NonNullList<Ingredient> ingredients;
	private final float experience;
	private final int cookingTime;
	private final Advancement.Builder advancement = Advancement.Builder.advancement();
	
	public CakeOvenRecipeBuilder(RecipeCategory category, NonNullList<Ingredient> ingredients, ItemLike result, float experience, int cookingTime) {
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
	
	@Override
	public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance trigger) {
		advancement.addCriterion(name, trigger);
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
	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeId) {
		ensureValid(recipeId);
		advancement.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId)).rewards(AdvancementRewards.Builder.recipe(recipeId))
			.requirements(RequirementsStrategy.OR);
		consumer.accept(new Result(recipeId, ingredients, result, experience, cookingTime, advancement, recipeId.withPrefix("recipes/" + category.getFolderName() + "/")));
	}
	
	private void ensureValid(ResourceLocation recipeId) {
		if (advancement.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + recipeId);
		}
	}
	
	public static class Result implements FinishedRecipe {

		private final ResourceLocation recipeId;
		private final NonNullList<Ingredient> ingredients;
		private final Item result;
		private final float experience;
		private final int cookingTime;
		private final Advancement.Builder advancementBuilder;
		private final ResourceLocation advancementId;
		
		public Result(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, Item result, float experience, int cookingTime, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
			this.recipeId = recipeId;
			this.ingredients = ingredients;
			this.result = result;
			this.experience = experience;
			this.cookingTime = cookingTime;
			this.advancementBuilder = advancementBuilder;
			this.advancementId = advancementId;
		}
		
		@Override
		public void serializeRecipeData(JsonObject json) {
			JsonArray jsonIngredients = new JsonArray(INGREDIENT_SLOT_COUNT);
			
			for (Ingredient ingredient : ingredients) {
				jsonIngredients.add(ingredient.toJson());
			}
			
			json.add("ingredients", jsonIngredients);
			json.addProperty("result", BuiltInRegistries.ITEM.getKey(result).toString());
			json.addProperty("experience", experience);
			json.addProperty("cookingTime", cookingTime);
		}
		
		@Override
		public ResourceLocation getId() {
			return recipeId;
		}
		
		@Override
		public RecipeSerializer<?> getType() {
			return ModRecipes.CAKE_OVEN_SERIALIZER.get();
		}
		
		@Nullable
		@Override
		public JsonObject serializeAdvancement() {
			return advancementBuilder.serializeToJson();
		}
		
		@Nullable
		@Override
		public ResourceLocation getAdvancementId() {
			return advancementId;
		}
	}
}
