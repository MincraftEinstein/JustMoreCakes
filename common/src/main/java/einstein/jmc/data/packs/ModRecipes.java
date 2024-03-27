package einstein.jmc.data.packs;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.item.crafting.CountedIngredient;
import einstein.jmc.util.CakeFamily;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.data.CakeOvenRecipeBuilder.cakeBaking;
import static einstein.jmc.data.MixingRecipeBuilder.mixing;
import static einstein.jmc.util.Util.getItemId;
import static net.minecraft.data.recipes.RecipeProvider.has;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class ModRecipes {

    private static final String HAS = "has_item";

    public static void init(Consumer<FinishedRecipe> consumer) {
        craftingRecipes(consumer);
        cakeOvenRecipes(consumer);
        mixingRecipes(consumer);
    }

    private static void craftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModBlocks.CUPCAKE_VARIANT.getCake().get(), 4)
                .requires(Items.EGG)
                .requires(Items.MILK_BUCKET)
                .requires(Items.WHEAT)
                .requires(Items.SUGAR)
                .unlockedBy(HAS, has(Items.EGG))
                .save(consumer, craftingLoc(ModBlocks.CUPCAKE_VARIANT.getCake()));

        shaped(RecipeCategory.FOOD, ModItems.CREAM_CHEESE.get(), 3)
                .pattern("###")
                .pattern("###")
                .define('#', Items.MILK_BUCKET)
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(consumer, craftingLoc(ModItems.CREAM_CHEESE));

        shaped(RecipeCategory.DECORATIONS, ModBlocks.CAKE_OVEN.get().asItem())
                .pattern("###")
                .pattern("#$#")
                .pattern("~~~")
                .define('#', Items.BRICK)
                .define('$', Blocks.FURNACE)
                .define('~', Blocks.SMOOTH_STONE)
                .unlockedBy(HAS, has(Blocks.SMOOTH_STONE))
                .save(consumer, craftingLoc(ModBlocks.CAKE_OVEN));

        shaped(RecipeCategory.TOOLS, ModItems.CAKE_SPATULA.get())
                .pattern(" ##")
                .pattern(" ##")
                .pattern("$  ")
                .define('#', Items.IRON_INGOT)
                .define('$', Items.STICK)
                .unlockedBy(HAS, has(Items.IRON_INGOT))
                .save(consumer, craftingLoc(ModItems.CAKE_SPATULA));

        shaped(RecipeCategory.MISC, ModBlocks.CAKE_STAND.get(), 2)
                .pattern("###")
                .pattern("$$$")
                .pattern(" $ ")
                .define('#', Blocks.GLASS)
                .define('$', Blocks.QUARTZ_SLAB)
                .unlockedBy(HAS, has(Blocks.QUARTZ_SLAB))
                .save(consumer, craftingLoc(ModBlocks.CAKE_STAND));
    }

    private static void cakeOvenRecipes(Consumer<FinishedRecipe> consumer) {
        cakeBaking(Blocks.CAKE, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.EGG))
                .save(consumer, loc("cake_from_cake_oven"));

        cakeBaking(ModBlocks.CARROT_CAKE_FAMILY, 0.6F, 300, RecipeCategory.FOOD, Ingredient.of(Items.CARROT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.CARROT))
                .save(consumer, cakeOvenLoc(ModBlocks.CARROT_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHEESECAKE_FAMILY, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(ModItemTags.CHEESE), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(ModItems.CREAM_CHEESE.get()))
                .save(consumer, cakeOvenLoc(ModBlocks.CHEESECAKE_FAMILY));

        cakeBaking(ModBlocks.SWEET_BERRY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.SWEET_BERRIES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.SWEET_BERRIES))
                .save(consumer, cakeOvenLoc(ModBlocks.SWEET_BERRY_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHOCOLATE_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.COCOA_BEANS))
                .save(consumer, cakeOvenLoc(ModBlocks.CHOCOLATE_CAKE_FAMILY));

        cakeBaking(ModBlocks.HONEY_CAKE_FAMILY, 0.7F, 300, RecipeCategory.FOOD, Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.HONEY_BOTTLE))
                .save(consumer, cakeOvenLoc(ModBlocks.HONEY_CAKE_FAMILY));

        cakeBaking(ModBlocks.APPLE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.APPLE))
                .save(consumer, cakeOvenLoc(ModBlocks.APPLE_CAKE_FAMILY));

        cakeBaking(ModBlocks.POISON_CAKE_VARIANT.getCake().get(), 0.5F, 300, RecipeCategory.FOOD, Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.SPIDER_EYE))
                .save(consumer, cakeOvenLoc(ModBlocks.POISON_CAKE_VARIANT.getCake()));

        cakeBaking(ModBlocks.TNT_CAKE_VARIANT.getCake().get(), 0.4F, 350, RecipeCategory.FOOD, Ingredient.of(Blocks.TNT), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Blocks.TNT))
                .save(consumer, cakeOvenLoc(ModBlocks.TNT_CAKE_VARIANT.getCake()));

        cakeBaking(ModBlocks.PUMPKIN_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.PUMPKIN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Blocks.PUMPKIN))
                .save(consumer, cakeOvenLoc(ModBlocks.PUMPKIN_CAKE_FAMILY));

        cakeBaking(ModBlocks.RED_VELVET_CAKE_FAMILY, 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.RED_DYE), Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.COCOA_BEANS))
                .save(consumer, cakeOvenLoc(ModBlocks.RED_VELVET_CAKE_FAMILY));

        cakeBaking(ModBlocks.GLOW_BERRY_CAKE_FAMILY, 0.5F, 350, RecipeCategory.FOOD, Ingredient.of(Items.GLOW_BERRIES), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.GLOW_BERRIES))
                .save(consumer, cakeOvenLoc(ModBlocks.GLOW_BERRY_CAKE_FAMILY));

        cakeBaking(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.BROWN_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Blocks.BROWN_MUSHROOM))
                .save(consumer, cakeOvenLoc(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY));

        cakeBaking(ModBlocks.RED_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.RED_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Blocks.RED_MUSHROOM))
                .save(consumer, cakeOvenLoc(ModBlocks.RED_MUSHROOM_CAKE_FAMILY));

        cakeBaking(ModBlocks.REDSTONE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.REDSTONE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.REDSTONE))
                .save(consumer, cakeOvenLoc(ModBlocks.REDSTONE_CAKE_FAMILY));

        cakeBaking(ModBlocks.SEED_CAKE_FAMILY, 0.3F, 150, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SEEDS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(ModItemTags.SEEDS))
                .save(consumer, cakeOvenLoc(ModBlocks.SEED_CAKE_FAMILY));

        cakeBaking(ModBlocks.SLIME_CAKE_FAMILY, 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SLIME_BALLS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(ModItemTags.SLIME_BALLS))
                .save(consumer, cakeOvenLoc(ModBlocks.SLIME_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHORUS_CAKE_FAMILY, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CHORUS_FRUIT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.CHORUS_FRUIT))
                .save(consumer, cakeOvenLoc(ModBlocks.CHORUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.COOKIE_CAKE_FAMILY, 0.6F, 200, RecipeCategory.FOOD, Ingredient.of(Items.COOKIE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.COOKIE))
                .save(consumer, cakeOvenLoc(ModBlocks.COOKIE_CAKE_FAMILY));

        cakeBaking(ModBlocks.ENDER_CAKE_FAMILY, 0.8F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.WHEAT), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.ENDER_PEARL))
                .save(consumer, cakeOvenLoc(ModBlocks.ENDER_CAKE_FAMILY));

        cakeBaking(ModBlocks.GLOWSTONE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.GLOWSTONE_DUST))
                .save(consumer, cakeOvenLoc(ModBlocks.GLOWSTONE_CAKE_FAMILY));

        cakeBaking(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY, 0.8F, 400, RecipeCategory.FOOD, Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.GOLDEN_APPLE))
                .save(consumer, cakeOvenLoc(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY));

        cakeBaking(ModBlocks.ICE_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.ICE), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Blocks.ICE))
                .save(consumer, cakeOvenLoc(ModBlocks.ICE_CAKE_FAMILY));

        cakeBaking(ModBlocks.CRIMSON_FUNGUS_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CRIMSON_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Blocks.CRIMSON_FUNGUS))
                .save(consumer, cakeOvenLoc(ModBlocks.CRIMSON_FUNGUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.WARPED_FUNGUS_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.WARPED_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(HAS, has(Items.WARPED_FUNGUS))
                .save(consumer, cakeOvenLoc(ModBlocks.WARPED_FUNGUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.MELON_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.MELON_SLICE))
                .save(consumer, cakeOvenLoc(ModBlocks.MELON_CAKE_FAMILY));

        cakeBaking(ModBlocks.BEETROOT_CAKE_FAMILY, 0.4F, 300, RecipeCategory.FOOD, Ingredient.of(Items.BEETROOT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Items.BEETROOT))
                .save(consumer, cakeOvenLoc(ModBlocks.BEETROOT_CAKE_FAMILY));

        cakeBaking(ModBlocks.LAVA_CAKE_FAMILY, 0.4F, 200, RecipeCategory.FOOD, Ingredient.of(Items.LAVA_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.LAVA_BUCKET))
                .save(consumer, cakeOvenLoc(ModBlocks.LAVA_CAKE_FAMILY));

        cakeBaking(ModBlocks.FIREY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.MAGMA_CREAM))
                .save(consumer, cakeOvenLoc(ModBlocks.FIREY_CAKE_FAMILY));

        cakeBaking(ModBlocks.OBSIDIAN_CAKE_FAMILY, 1, 800, RecipeCategory.FOOD, Ingredient.of(Blocks.OBSIDIAN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(HAS, has(Blocks.OBSIDIAN))
                .save(consumer, cakeOvenLoc(ModBlocks.OBSIDIAN_CAKE_FAMILY));

        cakeBaking(ModBlocks.SCULK_CAKE_FAMILY, 0.7F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ECHO_SHARD), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.ECHO_SHARD))
                .save(consumer, cakeOvenLoc(ModBlocks.SCULK_CAKE_FAMILY));
    }

    private static void mixingRecipes(Consumer<FinishedRecipe> consumer) {
        mixing(RecipeCategory.FOOD, ModItems.CAKE_DOUGH.get(), 5, CountedIngredient.of(3, Items.WHEAT), CountedIngredient.of(Items.EGG), CountedIngredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.EGG))
                .save(consumer, mixingLoc(ModItems.CAKE_DOUGH));

        mixing(RecipeCategory.FOOD, ModItems.CUPCAKE_DOUGH.get(), 5, Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.EGG))
                .save(consumer, mixingLoc(ModItems.CUPCAKE_DOUGH));

        mixing(RecipeCategory.FOOD, ModItems.CREAM_CHEESE.get(), 10, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(consumer, mixingLoc(ModItems.CREAM_CHEESE));

        mixing(RecipeCategory.FOOD, ModItems.FROSTING.get(), 7, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(consumer, mixingLoc(ModItems.FROSTING));
    }

    private static ResourceLocation craftingLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_crafting");
    }

    private static ResourceLocation cakeOvenLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_cake_oven");
    }

    private static ResourceLocation cakeOvenLoc(CakeFamily family) {
        return getItemId(family.getBaseCake().get().asItem()).withSuffix("_from_cake_oven");
    }

    private static ResourceLocation mixingLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_mixing");
    }
}
