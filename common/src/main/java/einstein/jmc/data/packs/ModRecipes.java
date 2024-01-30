package einstein.jmc.data.packs;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.data.CakeOvenRecipeBuilder;
import einstein.jmc.util.CakeFamily;
import einstein.jmc.util.Util;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

public class ModRecipes {

    private static final String has = "has_item";

    public static void init(RecipeOutput output) {
        craftingRecipes(output);
        cakeOvenRecipes(output);
    }

    private static void craftingRecipes(RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModBlocks.CUPCAKE.get(), 4)
                .requires(Items.EGG)
                .requires(Items.MILK_BUCKET)
                .requires(Items.WHEAT)
                .requires(Items.SUGAR)
                .unlockedBy(has, RecipeProvider.has(Items.EGG))
                .save(output, craftingLoc(ModBlocks.CUPCAKE));

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.CREAM_CHEESE.get(), 3)
                .pattern("###")
                .pattern("###")
                .define('#', Items.MILK_BUCKET)
                .unlockedBy(has, RecipeProvider.has(Items.MILK_BUCKET))
                .save(output, craftingLoc(ModItems.CREAM_CHEESE));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.CAKE_OVEN.get().asItem())
                .pattern("###")
                .pattern("#$#")
                .pattern("~~~")
                .define('#', Items.BRICK)
                .define('$', Blocks.FURNACE)
                .define('~', Blocks.SMOOTH_STONE)
                .unlockedBy(has, RecipeProvider.has(Blocks.SMOOTH_STONE))
                .save(output, craftingLoc(ModBlocks.CAKE_OVEN));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.CAKE_SPATULA.get())
                .pattern(" ##")
                .pattern(" ##")
                .pattern("$  ")
                .define('#', Items.IRON_INGOT)
                .define('$', Items.STICK)
                .unlockedBy(has, RecipeProvider.has(Items.IRON_INGOT))
                .save(output, craftingLoc(ModItems.CAKE_SPATULA));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CAKE_STAND.get(), 2)
                .pattern("###")
                .pattern("$$$")
                .pattern(" $ ")
                .define('#', Blocks.GLASS)
                .define('$', Blocks.QUARTZ_SLAB)
                .unlockedBy(has, RecipeProvider.has(Blocks.QUARTZ_SLAB))
                .save(output, craftingLoc(ModBlocks.CAKE_STAND));
    }

    private static void cakeOvenRecipes(RecipeOutput output) {
        CakeOvenRecipeBuilder.cakeBaking(Blocks.CAKE, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.EGG))
                .save(output, JustMoreCakes.loc("cake_from_cake_oven"));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CARROT_CAKE_FAMILY, 0.6F, 300, RecipeCategory.FOOD, Ingredient.of(Items.CARROT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.CARROT))
                .save(output, cakeOvenLoc(ModBlocks.CARROT_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHEESECAKE_FAMILY, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(ModItemTags.CHEESE), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(ModItems.CREAM_CHEESE.get()))
                .save(output, cakeOvenLoc(ModBlocks.CHEESECAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SWEET_BERRY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.SWEET_BERRIES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.SWEET_BERRIES))
                .save(output, cakeOvenLoc(ModBlocks.SWEET_BERRY_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHOCOLATE_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.COCOA_BEANS))
                .save(output, cakeOvenLoc(ModBlocks.CHOCOLATE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.HONEY_CAKE, 0.7F, 300, RecipeCategory.FOOD, Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.HONEY_BOTTLE))
                .save(output, cakeOvenLoc(ModBlocks.HONEY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.APPLE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.APPLE))
                .save(output, cakeOvenLoc(ModBlocks.APPLE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.POISON_CAKE.get(), 0.5F, 300, RecipeCategory.FOOD, Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.SPIDER_EYE))
                .save(output, cakeOvenLoc(ModBlocks.POISON_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.TNT_CAKE.get(), 0.4F, 350, RecipeCategory.FOOD, Ingredient.of(Blocks.TNT), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Blocks.TNT))
                .save(output, cakeOvenLoc(ModBlocks.TNT_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.PUMPKIN_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.PUMPKIN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.PUMPKIN))
                .save(output, cakeOvenLoc(ModBlocks.PUMPKIN_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_VELVET_CAKE.get(), 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.RED_DYE), Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.COCOA_BEANS))
                .save(output, cakeOvenLoc(ModBlocks.RED_VELVET_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOW_BERRY_CAKE.get(), 0.5F, 350, RecipeCategory.FOOD, Ingredient.of(Items.GLOW_BERRIES), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.GLOW_BERRIES))
                .save(output, cakeOvenLoc(ModBlocks.GLOW_BERRY_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.BROWN_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.BROWN_MUSHROOM))
                .save(output, cakeOvenLoc(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(Blocks.RED_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.RED_MUSHROOM))
                .save(output, cakeOvenLoc(ModBlocks.RED_MUSHROOM_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.REDSTONE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.REDSTONE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.REDSTONE))
                .save(output, cakeOvenLoc(ModBlocks.REDSTONE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SEED_CAKE_FAMILY, 0.3F, 150, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SEEDS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(ModItemTags.SEEDS))
                .save(output, cakeOvenLoc(ModBlocks.SEED_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SLIME_CAKE_FAMILY, 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItemTags.SLIME_BALLS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(ModItemTags.SLIME_BALLS))
                .save(output, cakeOvenLoc(ModBlocks.SLIME_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHORUS_CAKE, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CHORUS_FRUIT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.CHORUS_FRUIT))
                .save(output, cakeOvenLoc(ModBlocks.CHORUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.COOKIE_CAKE_FAMILY, 0.6F, 200, RecipeCategory.FOOD, Ingredient.of(Items.COOKIE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.COOKIE))
                .save(output, cakeOvenLoc(ModBlocks.COOKIE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ENDER_CAKE_FAMILY, 0.8F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.WHEAT), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.ENDER_PEARL))
                .save(output, cakeOvenLoc(ModBlocks.ENDER_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOWSTONE_CAKE, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.GLOWSTONE_DUST))
                .save(output, cakeOvenLoc(ModBlocks.GLOWSTONE_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY, 0.8F, 400, RecipeCategory.FOOD, Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.GOLDEN_APPLE))
                .save(output, cakeOvenLoc(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ICE_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.ICE), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Blocks.ICE))
                .save(output, cakeOvenLoc(ModBlocks.ICE_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.CRIMSON_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Blocks.CRIMSON_FUNGUS))
                .save(output, cakeOvenLoc(ModBlocks.CRIMSON_FUNGUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.WARPED_FUNGUS_CAKE.get(), 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(Items.WARPED_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
                .unlockedBy(has, RecipeProvider.has(Items.WARPED_FUNGUS))
                .save(output, cakeOvenLoc(ModBlocks.WARPED_FUNGUS_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.MELON_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
                .unlockedBy(has, RecipeProvider.has(Items.MELON_SLICE))
                .save(output, cakeOvenLoc(ModBlocks.MELON_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BEETROOT_CAKE_FAMILY, 0.4F, 300, RecipeCategory.FOOD, Ingredient.of(Items.BEETROOT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Items.BEETROOT))
                .save(output, cakeOvenLoc(ModBlocks.BEETROOT_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.LAVA_CAKE_FAMILY, 0.4F, 200, RecipeCategory.FOOD, Ingredient.of(Items.LAVA_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.LAVA_BUCKET))
                .save(output, cakeOvenLoc(ModBlocks.LAVA_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.FIREY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.MAGMA_CREAM))
                .save(output, cakeOvenLoc(ModBlocks.FIREY_CAKE_FAMILY));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.OBSIDIAN_CAKE.get(), 1, 800, RecipeCategory.FOOD, Ingredient.of(Blocks.OBSIDIAN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
                .unlockedBy(has, RecipeProvider.has(Blocks.OBSIDIAN))
                .save(output, cakeOvenLoc(ModBlocks.OBSIDIAN_CAKE));

        CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SCULK_CAKE.get(), 0.7F, 350, RecipeCategory.FOOD, Ingredient.of(Items.ECHO_SHARD), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
                .unlockedBy(has, RecipeProvider.has(Items.ECHO_SHARD))
                .save(output, cakeOvenLoc(ModBlocks.SCULK_CAKE));
    }

    private static ResourceLocation craftingLoc(Supplier<? extends ItemLike> item) {
        return JustMoreCakes.loc(Util.getItemId(item.get().asItem()).getPath() + "_from_crafting");
    }

    private static ResourceLocation cakeOvenLoc(Supplier<? extends ItemLike> item) {
        return JustMoreCakes.loc(Util.getItemId(item.get().asItem()).getPath() + "_from_cake_oven");
    }

    private static ResourceLocation cakeOvenLoc(CakeFamily family) {
        return JustMoreCakes.loc(Util.getItemId(family.getBaseCake().get().asItem()).getPath() + "_from_cake_oven");
    }
}
