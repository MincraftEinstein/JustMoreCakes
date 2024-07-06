package einstein.jmc.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.util.CakeOvenConstants;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CakeOvenRecipeSerializer implements RecipeSerializer<CakeOvenRecipe>, CakeOvenConstants {

    private static final MapCodec<CakeOvenRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                Ingredient[] filteredIngredients = ingredients.stream()
                        .filter(ingredient -> !ingredient.isEmpty())
                        .toArray(Ingredient[]::new);

                if (filteredIngredients.length == 0) {
                    return DataResult.error(() -> "No ingredients for cake oven recipe");
                }
                return filteredIngredients.length > 4 ? DataResult.error(() -> "Too many ingredients for cake oven recipe. The max is 4")
                        : DataResult.success(NonNullList.of(Ingredient.EMPTY, filteredIngredients));
            }, DataResult::success).forGetter(recipe -> recipe.ingredients),
            ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("experience").orElse(0F).forGetter(recipe -> recipe.experience),
            ExtraCodecs.POSITIVE_INT.fieldOf("cookingTime").orElse(DEFAULT_COOK_TIME).forGetter(recipe -> recipe.cookingTime)
    ).apply(instance, CakeOvenRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, CakeOvenRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                buf.writeFloat(recipe.experience);
                buf.writeVarInt(recipe.cookingTime);
                buf.writeByte(recipe.ingredients.size());
                recipe.ingredients.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient));
            }, buf -> {
                ItemStack resultStack = ItemStack.STREAM_CODEC.decode(buf);
                float experience = buf.readFloat();
                int cookTime = buf.readVarInt();
                int ingredientCount = buf.readByte();
                NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

                ingredients.replaceAll(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                return new CakeOvenRecipe(ingredients, resultStack, experience, cookTime);
            }
    );

    @Override
    public MapCodec<CakeOvenRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CakeOvenRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}