package einstein.jmc.item.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public record CountedIngredient(Ingredient ingredient, int count) {

    public static final CountedIngredient EMPTY = new CountedIngredient(Ingredient.EMPTY, 0);

    public CountedIngredient(Ingredient ingredient) {
        this(ingredient, 1);
    }

    public static CountedIngredient of(int count, ItemLike... items) {
        return new CountedIngredient(Ingredient.of(items), count);
    }

    public static CountedIngredient of(ItemLike... items) {
        return new CountedIngredient(Ingredient.of(items));
    }

    public static CountedIngredient of(TagKey<Item> tag, int count) {
        return new CountedIngredient(Ingredient.of(tag), count);
    }

    public static CountedIngredient of(int count, ItemStack... stacks) {
        return new CountedIngredient(Ingredient.of(stacks), count);
    }

    public boolean isEmpty() {
        return ingredient.isEmpty() || count == 0;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.add("ingredient", ingredient.toJson());
        object.addProperty("count", count);
        return object;
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
