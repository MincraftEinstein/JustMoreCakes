package einstein.jmc.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CakeOvenRecipeSerializer<T extends CakeOvenRecipe> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T>, CakeOvenConstants {

	private final int defaultCookingTime;
	private final CookieBaker<T> factory;
	
	public CakeOvenRecipeSerializer(CookieBaker<T> factory, int defaultCooingTime) {
		this.defaultCookingTime = defaultCooingTime;
		this.factory = factory;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public T fromJson(ResourceLocation recipeId, JsonObject json) {
		NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
		
		if (ingredients.isEmpty()) {
			throw new JsonParseException("No ingredients for cake oven recipe");
		}
		else if (ingredients.size() > INGREDIENT_SLOT_COUNT) {
			throw new JsonParseException("Too many ingredients for cake oven recipe. The max is 4");
		}
		else {
			String r = "result";
			if (!json.has(r)) {
				throw new JsonSyntaxException("Missing result, expected to find a string or object");
			}
			
			ItemStack resultStack;
			if (json.get(r).isJsonObject()) {
				resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, r));
			}
			else {
				String resultString = GsonHelper.getAsString(json, r);
				ResourceLocation resourceLocation = new ResourceLocation(resultString);
				resultStack = new ItemStack(Registry.ITEM.getOptional(resourceLocation).orElseThrow(
						() -> new IllegalStateException("Item: " + resultString + " does not exist")));
			}
			
			float experience = GsonHelper.getAsFloat(json, "experience", 0);
			int cookingTime = GsonHelper.getAsInt(json, "cookingTime", this.defaultCookingTime);
			return factory.create(recipeId, ingredients, resultStack, experience, cookingTime);
		}
	}
	
    private static NonNullList<Ingredient> itemsFromJson(JsonArray array) {
        NonNullList<Ingredient> nonNullList = NonNullList.create();

        for (int i = 0; i < array.size(); ++i) {
           Ingredient ingredient = Ingredient.fromJson(array.get(i));
           if (!ingredient.isEmpty()) {
              nonNullList.add(ingredient);
           }
        }
        return nonNullList;
     }
	
	@Override
	public T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
		ItemStack resultStack = buf.readItem();
		float experience = buf.readFloat();
		int cookTime = buf.readVarInt();
		int ingredientCount = buf.readByte();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);
		
		for (int i = 0; i < ingredientCount; i++) {
			ingredients.set(i, Ingredient.fromNetwork(buf));
		}
		
		return factory.create(recipeId, ingredients, resultStack, experience, cookTime);
	}
	
	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		buf.writeItem(recipe.result);
		buf.writeFloat(recipe.experience);
		buf.writeVarInt(recipe.cookingTime);
		buf.writeByte(recipe.ingredients.size());
		recipe.ingredients.forEach(ingredient -> ingredient.toNetwork(buf));
	}
	
	public interface CookieBaker<T extends CakeOvenRecipe> {
		T create(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack result, float experience, int cookTime);
	}
}
