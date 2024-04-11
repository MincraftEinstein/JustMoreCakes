package einstein.jmc.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import einstein.jmc.block.entity.CeramicBowlBlockEntity;
import net.minecraft.core.NonNullList;
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
            throw new JsonSyntaxException("No Ingredients found for mixing recipe: " + recipeId);
        }
        else if (ingredients.size() > CeramicBowlBlockEntity.SLOT_COUNT) {
            throw new JsonSyntaxException("Too many ingredients for mixing recipe: " + recipeId + ". The max is 4");
        }

        ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        ResourceLocation contentsId = ResourceLocation.tryParse(GsonHelper.getAsString(json, "contents"));

        if (contentsId == null) {
            throw new JsonSyntaxException("Invalid contents for mixing recipe: " + recipeId);
        }

        int mixingTime = GsonHelper.getAsInt(json, "mixingTime", CeramicBowlBlockEntity.DEFAULT_MIXING_PROGRESS);
        if (mixingTime < 1) {
            throw new JsonSyntaxException("mixingTime must be a positive number for recipe: " + recipeId);
        }

        return new MixingRecipe(recipeId, ingredients, result, contentsId, mixingTime);
    }

    @Override
    public MixingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
        ItemStack resultStack = buf.readItem();
        ResourceLocation contentsId = buf.readResourceLocation();
        int mixingTime = buf.readInt();
        int ingredientCount = buf.readByte();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        return new MixingRecipe(recipeId, ingredients, resultStack, contentsId, mixingTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, MixingRecipe recipe) {
        buf.writeItem(recipe.result);
        buf.writeResourceLocation(recipe.contentsId);
        buf.writeInt(recipe.mixingTime);
        buf.writeByte(recipe.ingredients.size());
        recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buf));
    }
}
