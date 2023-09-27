package einstein.jmc.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CakeOvenRecipeSerializer implements RecipeSerializer<CakeOvenRecipe>, CakeOvenConstants {

    private static final Codec<CakeOvenRecipe> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                        Ingredient[] ingredients1 = ingredients.stream().filter(i -> !i.isEmpty()).toArray(Ingredient[]::new);
                        if (ingredients1.length == 0) {
                            return DataResult.error(() -> "No ingredients for cake oven recipe");
                        }
                        return ingredients1.length > INGREDIENT_SLOT_COUNT
                                ? DataResult.error(() -> "Too many ingredients for cake oven recipe. The max is 4")
                                : DataResult.success(NonNullList.of(Ingredient.EMPTY, ingredients1));
                    }, DataResult::success).forGetter(t -> t.ingredients),
                    BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(t -> t.result),
                    Codec.FLOAT.fieldOf("experience").orElse(0F).forGetter(t -> t.experience),
                    Codec.INT.fieldOf("cookingTime").orElse(DEFAULT_COOK_TIME).forGetter(t -> t.cookingTime)
            ).apply(instance, CakeOvenRecipe::new)
    );

    @Override
    public Codec<CakeOvenRecipe> codec() {
        return CODEC;
    }

    @Override
    public CakeOvenRecipe fromNetwork(FriendlyByteBuf buf) {
        ItemStack resultStack = buf.readItem();
        float experience = buf.readFloat();
        int cookTime = buf.readVarInt();
        int ingredientCount = buf.readByte();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        return new CakeOvenRecipe(ingredients, resultStack, experience, cookTime);
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
