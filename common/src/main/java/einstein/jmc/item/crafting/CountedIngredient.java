package einstein.jmc.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public record CountedIngredient(Ingredient ingredient, int count) {

    public static final CountedIngredient EMPTY = new CountedIngredient(Ingredient.EMPTY, 0);

    public boolean isEmpty() {
        return ingredient.isEmpty() || count == 0;
    }

    public void toJson(JsonObject json) {
        json.add("ingredient", ingredient.toJson());
        json.addProperty("count", count);
    }

    public void toNetwork(FriendlyByteBuf buf) {
        ingredient.toNetwork(buf);
        buf.writeInt(count);
    }

    public static CountedIngredient fromJson(JsonObject json) {
        if (json.has("ingredient")) {
            return new CountedIngredient(Ingredient.fromJson(json.get("ingredient"), false), GsonHelper.getAsInt(json, "count", 1));
        }
        throw new JsonSyntaxException("Missing ingredient, expected to find and object or array");
    }

    public static CountedIngredient fromNetwork(FriendlyByteBuf buf) {
        return new CountedIngredient(Ingredient.fromNetwork(buf), buf.readInt());
    }
}
