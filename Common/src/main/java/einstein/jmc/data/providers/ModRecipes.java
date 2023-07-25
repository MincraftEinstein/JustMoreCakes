package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.item.crafting.builders.CakeOvenRecipeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ModRecipes {

    private static final String has = "has_item";

    public static void init(Consumer<FinishedRecipe> consumer) {
        craftingRecipes(consumer);
        cakeOvenRecipes(consumer);
    }

    private static void craftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModBlocks.CUPCAKE.get(), 4)
                .requires(Items.EGG)
                .requires(Items.MILK_BUCKET)
                .requires(Items.WHEAT)
                .requires(Items.SUGAR)
                .unlockedBy(has, RecipeProvider.has(Items.EGG))
                .save(consumer, craftingLoc(ModBlocks.CUPCAKE));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModBlocks.THREE_TIERED_CAKE.get())
                .pattern("#")
                .pattern("#")
                .pattern("#")
                .define('#', Items.CAKE)
                .unlockedBy(has, RecipeProvider.has(Items.CAKE))
                .save(consumer, craftingLoc(ModBlocks.THREE_TIERED_CAKE));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CREAM_CHEESE.get(), 3)
                .pattern("###")
                .pattern("###")
                .define('#', Items.MILK_BUCKET)
                .unlockedBy(has, RecipeProvider.has(Items.MILK_BUCKET))
                .save(consumer, craftingLoc(ModItems.CREAM_CHEESE));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CAKE_OVEN.get().asItem())
                .pattern("###")
                .pattern("#$#")
                .pattern("~~~")
                .define('#', Items.BRICK)
                .define('$', Blocks.FURNACE)
                .define('~', Blocks.SMOOTH_STONE)
                .unlockedBy(has, RecipeProvider.has(Blocks.SMOOTH_STONE))
                .save(consumer, craftingLoc(ModBlocks.CAKE_OVEN));
    }

    private static void cakeOvenRecipes(Consumer<FinishedRecipe> consumer) {
        CakeOvenRecipeBuilder.cakeBaking(Blocks.CAKE, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.EGG))
                .save(consumer, JustMoreCakes.loc("cake_from_cake_oven"));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CARROT_CAKE.get(), 0.6F, 300, RecipeCategory.FOOD, Ingredient.of(Items.CARROT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.CARROT))
                .save(consumer, cakeOvenLoc(ModBlocks.CARROT_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHEESECAKE.get(), 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(ModItemTags.CHEESE), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(ModItems.CREAM_CHEESE.get()))
                .save(consumer, cakeOvenLoc(ModBlocks.CHEESECAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SWEET_BERRY_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.SWEET_BERRIES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.SWEET_BERRIES))
                .save(consumer, cakeOvenLoc(ModBlocks.SWEET_BERRY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHOCOLATE_CAKE.get(), 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.COCOA_BEANS))
                .save(consumer, cakeOvenLoc(ModBlocks.CHOCOLATE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.HONEY_CAKE.get(), 0.7F, 300, RecipeCategory.FOOD, Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.HONEY_BOTTLE))
                .save(consumer, cakeOvenLoc(ModBlocks.HONEY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.APPLE_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.APPLE))
                .save(consumer, cakeOvenLoc(ModBlocks.APPLE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.POISON_CAKE.get(), 0.5F, 300, RecipeCategory.FOOD, Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.SPIDER_EYE))
                .save(consumer, cakeOvenLoc(ModBlocks.POISON_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.TNT_CAKE.get(), 0.4F, 350, RecipeCategory.FOOD, Ingredient.of(Blocks.TNT), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Blocks.TNT))
                .save(consumer, cakeOvenLoc(ModBlocks.TNT_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.PUMPKIN_CAKE.get(), 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.PUMPKIN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.PUMPKIN))
                .save(consumer, cakeOvenLoc(ModBlocks.PUMPKIN_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_VELVET_CAKE.get(), 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.RED_DYE), Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.COCOA_BEANS))
                .save(consumer, cakeOvenLoc(ModBlocks.RED_VELVET_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOW_BERRY_CAKE.get(), 0.5F, 350, RecipeCategory.FOOD, Ingredient.of(Items.GLOW_BERRIES), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.GLOW_BERRIES))
                .save(consumer, cakeOvenLoc(ModBlocks.GLOW_BERRY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BROWN_MUSHROOM_CAKE.get(), 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.BROWN_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.BROWN_MUSHROOM))
                .save(consumer, cakeOvenLoc(ModBlocks.BROWN_MUSHROOM_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_MUSHROOM_CAKE.get(), 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.RED_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.RED_MUSHROOM))
                .save(consumer, cakeOvenLoc(ModBlocks.RED_MUSHROOM_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.REDSTONE_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.REDSTONE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.REDSTONE))
                .save(consumer, cakeOvenLoc(ModBlocks.REDSTONE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SEED_CAKE.get(), 0.3F, 150, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SEEDS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(ModItemTags.SEEDS))
                .save(consumer, cakeOvenLoc(ModBlocks.SEED_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SLIME_CAKE.get(), 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SLIME_BALLS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(ModItemTags.SLIME_BALLS))
                .save(consumer, cakeOvenLoc(ModBlocks.SLIME_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHORUS_CAKE.get(), 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CHORUS_FRUIT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.CHORUS_FRUIT))
                .save(consumer, cakeOvenLoc(ModBlocks.CHORUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.COOKIE_CAKE.get(), 0.6F, 200, RecipeCategory.FOOD, Ingredient.of(Items.COOKIE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.COOKIE))
                .save(consumer, cakeOvenLoc(ModBlocks.COOKIE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ENDER_CAKE.get(), 0.8F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.WHEAT), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.ENDER_PEARL))
                .save(consumer, cakeOvenLoc(ModBlocks.ENDER_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOWSTONE_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.GLOWSTONE_DUST))
                .save(consumer, cakeOvenLoc(ModBlocks.GLOWSTONE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GOLDEN_APPLE_CAKE.get(), 0.8F, 400, RecipeCategory.FOOD, Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.GOLDEN_APPLE))
                .save(consumer, cakeOvenLoc(ModBlocks.GOLDEN_APPLE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ICE_CAKE.get(), 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.ICE), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Blocks.ICE))
                .save(consumer, cakeOvenLoc(ModBlocks.ICE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CRIMSON_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.CRIMSON_FUNGUS))
                .save(consumer, cakeOvenLoc(ModBlocks.CRIMSON_FUNGUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.WARPED_FUNGUS_CAKE.get(), 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.WARPED_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.WARPED_FUNGUS))
                .save(consumer, cakeOvenLoc(ModBlocks.WARPED_FUNGUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.MELON_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.MELON_SLICE))
                .save(consumer, cakeOvenLoc(ModBlocks.MELON_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BEETROOT_CAKE.get(), 0.4F, 300, RecipeCategory.FOOD, Ingredient.of(Items.BEETROOT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.BEETROOT))
                .save(consumer, cakeOvenLoc(ModBlocks.BEETROOT_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.LAVA_CAKE.get(), 0.4F, 200, RecipeCategory.FOOD, Ingredient.of(Items.LAVA_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.LAVA_BUCKET))
                .save(consumer, cakeOvenLoc(ModBlocks.LAVA_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.FIREY_CAKE.get(), 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.MAGMA_CREAM))
                .save(consumer, cakeOvenLoc(ModBlocks.FIREY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.OBSIDIAN_CAKE.get(), 1, 800, RecipeCategory.FOOD, Ingredient.of(Blocks.OBSIDIAN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Blocks.OBSIDIAN))
                .save(consumer, cakeOvenLoc(ModBlocks.OBSIDIAN_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SCULK_CAKE.get(), 0.7F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ECHO_SHARD), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.ECHO_SHARD))
                .save(consumer, cakeOvenLoc(ModBlocks.SCULK_CAKE));
    }

    private static ResourceLocation craftingLoc(Supplier<? extends ItemLike> item) {
        return JustMoreCakes.loc(Util.getItemId(item.get().asItem()).getPath() + "_from_crafting");
    }

    private static ResourceLocation cakeOvenLoc(Supplier<? extends ItemLike> item) {
        return JustMoreCakes.loc(Util.getItemId(item.get().asItem()).getPath() + "_from_cake_oven");
    }
}
