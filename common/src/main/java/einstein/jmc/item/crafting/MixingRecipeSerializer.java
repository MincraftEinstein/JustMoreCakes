package einstein.jmc.item.crafting;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MixingRecipeSerializer implements RecipeSerializer<MixingRecipe> {

    public static final MapCodec<MixingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(ingredients -> {
                Ingredient[] filteredIngredients = ingredients.stream()
                        .filter(ingredient -> !ingredient.isEmpty())
                        .toArray(Ingredient[]::new);

                if (filteredIngredients.length == 0) {
                    return DataResult.error(() -> "No ingredients found for mixing recipe");
                }
                return filteredIngredients.length > 4 ? DataResult.error(() -> "Too many ingredients for mixing recipe. The max is 4")
                        : DataResult.success(NonNullList.of(Ingredient.EMPTY, filteredIngredients));
            }, DataResult::success).forGetter(MixingRecipe::getIngredients),
            ItemStack.STRICT_SINGLE_ITEM_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            ResourceLocation.CODEC.fieldOf("contents").forGetter(MixingRecipe::getContentsId),
            ExtraCodecs.POSITIVE_INT.fieldOf("mixingTime").forGetter(MixingRecipe::getMixingTime)
    ).apply(instance, MixingRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, MixingRecipe> STREAM_CODEC = StreamCodec.of(
            (buf, recipe) -> {
                ItemStack.STREAM_CODEC.encode(buf, recipe.result);
                buf.writeResourceLocation(recipe.contentsId);
                buf.writeInt(recipe.mixingTime);
                buf.writeByte(recipe.ingredients.size());
                recipe.ingredients.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient));
            }, buf -> {
                ItemStack resultStack = ItemStack.STREAM_CODEC.decode(buf);
                ResourceLocation contentsId = buf.readResourceLocation();
                int mixingTime = buf.readInt();
                int ingredientCount = buf.readByte();
                NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientCount, Ingredient.EMPTY);

                for (int i = 0; i < ingredientCount; i++) {
                    ingredients.set(i, Ingredient.CONTENTS_STREAM_CODEC.decode(buf));
                }

                return new MixingRecipe(ingredients, resultStack, contentsId, mixingTime);
            }
    );

    @Override
    public MapCodec<MixingRecipe> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MixingRecipe> streamCodec() {
        return STREAM_CODEC;
    }
}
