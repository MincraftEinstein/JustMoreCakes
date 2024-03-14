package einstein.jmc.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class MixingRecipeSerializer implements RecipeSerializer<MixingRecipe> {

    @Override
    public MixingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        NonNullList<Ingredient> ingredients = CakeOvenRecipeSerializer.itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));

        if (ingredients.isEmpty()) {
            throw new JsonParseException("No Ingredients for mixing recipe");
        }
        else if (ingredients.size() > CeramicBowlBlockEntity.SLOT_COUNT) {
            throw new JsonParseException("Too many ingredients for mixing recipe. The max is 4");
        }

        if (!json.has("result")) {
            throw new JsonParseException("Missing result, expected to find a string or object");
        }

        ItemStack result;
        if (json.get("result").isJsonObject()) {
            result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        }
        else {
            String resultString = GsonHelper.getAsString(json, "result");
            result = new ItemStack(BuiltInRegistries.ITEM.getOptional(new ResourceLocation(resultString))
                    .orElseThrow(() -> new IllegalStateException("Item: " + resultString + " does not exist")));
        }

        return new MixingRecipe(recipeId, ingredients, result);
    }

    @Override
    public MixingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        ItemStack resultStack = buf.readItem();
        int ingredientCount = buf.readByte();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        return new MixingRecipe(recipeId, ingredients, resultStack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, MixingRecipe recipe) {
        buf.writeItem(recipe.result);
        buf.writeByte(recipe.ingredients.size());
        recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buf));
    }
}
