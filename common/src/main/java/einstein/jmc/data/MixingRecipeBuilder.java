package einstein.jmc.data;

import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import einstein.jmc.item.crafting.MixingRecipe;
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
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class MixingRecipeBuilder implements RecipeBuilder {

    private final RecipeCategory category;
    private final NonNullList<Ingredient> ingredients;
    private final Item result;
    private final ResourceLocation contents;
    private final int count;
    private final int mixingTime;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();

    private MixingRecipeBuilder(RecipeCategory category, NonNullList<Ingredient> ingredients, ItemLike result, ResourceLocation contents, int count, int mixingTime) {
        this.category = category;
        this.ingredients = ingredients;
        this.result = result.asItem();
        this.contents = contents;
        this.count = count;
        this.mixingTime = mixingTime;
    }

    public static MixingRecipeBuilder mixing(RecipeCategory category, ItemLike result, ResourceLocation contents, int mixingTime, ItemLike... ingredients) {
        return mixing(category, result, contents, 1, mixingTime, ingredients);
    }

    public static MixingRecipeBuilder mixing(RecipeCategory category, ItemLike result, ResourceLocation contents, int count, int mixingTime, ItemLike... ingredients) {
        return mixing(category, result, contents, count, mixingTime, Arrays.stream(ingredients).map(Ingredient::of).toArray(Ingredient[]::new));
    }

    public static MixingRecipeBuilder mixing(RecipeCategory category, ItemLike result, ResourceLocation contents, int mixingTime, Ingredient... ingredients) {
        return mixing(category, result, contents, 1, mixingTime, ingredients);
    }

    public static MixingRecipeBuilder mixing(RecipeCategory category, ItemLike result, ResourceLocation contents, int count, int mixingTime, Ingredient... ingredients) {
        if (mixingTime < 1) {
            throw new IllegalStateException("mixingTime must be a positive number");
        }

        if (ingredients.length > CeramicBowlBlockEntity.INGREDIENT_SLOT_COUNT) {
            throw new IllegalStateException("Too many ingredients for mixing recipe. The max is 4");
        }

        NonNullList<Ingredient> ingredientsList = NonNullList.create();
        Collections.addAll(ingredientsList, ingredients);
        return new MixingRecipeBuilder(category, ingredientsList, result, contents, count, mixingTime);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        criteria.put(name, criterion);
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
                new MixingRecipe(ingredients, new ItemStack(result, count), contents, mixingTime),
                builder.build(recipeId.withPrefix("recipes/" + category.getFolderName() + "/"))
        );
    }
}