package einstein.jmc.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class CakeOvenRecipeSerializer implements RecipeSerializer<CakeOvenRecipe>, CakeOvenConstants {

    @Override
    public CakeOvenRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));

        if (ingredients.isEmpty()) {
            throw new JsonSyntaxException("No ingredients found for cake oven recipe: " + recipeId);
        }
        else if (ingredients.size() > INGREDIENT_SLOT_COUNT) {
            throw new JsonSyntaxException("Too many ingredients for cake oven recipe: " + recipeId + ". The max is 4");
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        float experience = GsonHelper.getAsFloat(json, "experience", 0);
        int cookingTime = GsonHelper.getAsInt(json, "cookingTime", DEFAULT_COOK_TIME);
        return new CakeOvenRecipe(recipeId, ingredients, result, experience, cookingTime);
    }

    public static NonNullList<Ingredient> itemsFromJson(JsonArray array) {
        NonNullList<Ingredient> ingredients = NonNullList.create();

        for (JsonElement element : array) {
            Ingredient ingredient = Ingredient.fromJson(element);
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public CakeOvenRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        ItemStack resultStack = buf.readItem();
        float experience = buf.readFloat();
        int cookTime = buf.readVarInt();
        int ingredientCount = buf.readByte();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        return new CakeOvenRecipe(recipeId, ingredients, resultStack, experience, cookTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, CakeOvenRecipe recipe) {
        buf.writeItem(recipe.result);
        buf.writeFloat(recipe.experience);
        buf.writeVarInt(recipe.cookingTime);
        buf.writeByte(recipe.ingredients.size());
        recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buf));
    }
}