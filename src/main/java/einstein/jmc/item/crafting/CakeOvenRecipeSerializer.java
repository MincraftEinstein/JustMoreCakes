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
	public T fromJson(ResourceLocation id, JsonObject json) {
		NonNullList<Ingredient> ingredients = itemsFromJson(GsonHelper.getAsJsonArray(json, "ingredients"));
		
		if (ingredients.isEmpty()) {
			throw new JsonParseException("No ingredients for cake oven recipe");
		}
		else if (ingredients.size() > INGREDIENT_SLOT_COUNT) {
			throw new JsonParseException("Too many ingredients for cake oven recipe. The max is 4");
		}
		else {
			if (!json.has("result")) {
				throw new JsonSyntaxException("Missing result, expected to find a string or object");
			}
			
			ItemStack resultStack;
			if (json.get("result").isJsonObject()) {
				resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
			}
			else {
				String resultString = GsonHelper.getAsString(json, "result");
				ResourceLocation resourceLocation = new ResourceLocation(resultString);
				resultStack = new ItemStack(Registry.ITEM.getOptional(resourceLocation).orElseThrow(() -> {
					return new IllegalStateException("Item: " + resultString + " does not exist");
				}));
			}
			
			float experience = GsonHelper.getAsFloat(json, "experience", 0);
			int cookingTime = GsonHelper.getAsInt(json, "cookingTime", this.defaultCookingTime);
			return factory.create(id, ingredients, resultStack, experience, cookingTime);
		}
	}
	
    private static NonNullList<Ingredient> itemsFromJson(JsonArray array) {
        NonNullList<Ingredient> nonNullList = NonNullList.create();

        for(int i = 0; i < array.size(); ++i) {
           Ingredient ingredient = Ingredient.fromJson(array.get(i));
           if (!ingredient.isEmpty()) {
              nonNullList.add(ingredient);
           }
        }
        return nonNullList;
     }
	
	@Override
	public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
		int size = buf.readVarInt();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(size, Ingredient.EMPTY);
		
		for (int i = 0; i < ingredients.size(); ++i) {
			ingredients.set(i, Ingredient.fromNetwork(buf));
		}
		
		ItemStack resultStack = buf.readItem();
		float experience = buf.readFloat();
		int cookingTime = buf.readVarInt();
		return factory.create(id, ingredients, resultStack, experience, cookingTime);
	}
	
	@Override
	public void toNetwork(FriendlyByteBuf buf, T recipe) {
		for (Ingredient ingredient : recipe.ingredients) {
			ingredient.toNetwork(buf);
		}
		buf.writeItem(recipe.result);
		buf.writeFloat(recipe.experience);
		buf.writeVarInt(recipe.cookingTime);
		
	}
	
	public interface CookieBaker<T extends CakeOvenRecipe> {
		T create(ResourceLocation id, NonNullList<Ingredient> ingredients, ItemStack result, float experience, int cookingTime);
	}
}
