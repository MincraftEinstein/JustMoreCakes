package einstein.jmc.data.packs;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.registration.family.CakeFamily;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.data.CakeOvenRecipeBuilder.cakeBaking;
import static einstein.jmc.data.MixingRecipeBuilder.mixing;
import static einstein.jmc.util.Util.getItemId;
import static net.minecraft.data.recipes.RecipeProvider.has;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.shaped;

public class ModRecipes {

    private static final String HAS = "has_item";

    public static void init(RecipeOutput output) {
        craftingRecipes(output);
        cakeOvenRecipes(output);
        mixingRecipes(output);
    }

    private static void craftingRecipes(RecipeOutput output) {
        shaped(RecipeCategory.DECORATIONS, ModBlocks.CAKE_OVEN.get().asItem())
                .pattern("###")
                .pattern("#$#")
                .pattern("~~~")
                .define('#', Items.BRICK)
                .define('$', Blocks.FURNACE)
                .define('~', Blocks.SMOOTH_STONE)
                .unlockedBy(HAS, has(Blocks.SMOOTH_STONE))
                .save(output, craftingLoc(ModBlocks.CAKE_OVEN));

        shaped(RecipeCategory.TOOLS, ModItems.CAKE_SPATULA.get())
                .pattern(" ##")
                .pattern(" ##")
                .pattern("$  ")
                .define('#', Items.IRON_INGOT)
                .define('$', Items.STICK)
                .unlockedBy(HAS, has(Items.IRON_INGOT))
                .save(output, craftingLoc(ModItems.CAKE_SPATULA));

        shaped(RecipeCategory.MISC, ModBlocks.CAKE_STAND.get(), 2)
                .pattern("###")
                .pattern("$$$")
                .pattern(" $ ")
                .define('#', Blocks.GLASS)
                .define('$', Blocks.QUARTZ_SLAB)
                .unlockedBy(HAS, has(Blocks.QUARTZ_SLAB))
                .save(output, craftingLoc(ModBlocks.CAKE_STAND));

        shaped(RecipeCategory.MISC, ModBlocks.CERAMIC_BOWL.get())
                .pattern("# #")
                .pattern("###")
                .define('#', Blocks.TERRACOTTA)
                .unlockedBy(HAS, has(Blocks.TERRACOTTA))
                .save(output, craftingLoc(ModBlocks.CERAMIC_BOWL));

        shaped(RecipeCategory.TOOLS, ModItems.WHISK.get())
                .pattern("#")
                .pattern("$")
                .pattern("~")
                .define('#', Items.IRON_INGOT)
                .define('$', Items.IRON_NUGGET)
                .define('~', Items.STICK)
                .unlockedBy(HAS, has(Items.IRON_INGOT))
                .save(output, craftingLoc(ModItems.WHISK));
    }

    private static void cakeOvenRecipes(RecipeOutput output) {
        cakeBaking(Blocks.CAKE, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()))
                .unlockedBy(HAS, has(Items.EGG))
                .save(output, loc("cake_from_cake_oven"));

        cakeBaking(ModBlocks.CUPCAKE_VARIANT.getCake().get(), 2, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CUPCAKE_DOUGH.get()), Ingredient.of(ModItems.CUPCAKE_FROSTING.get()))
                .unlockedBy(HAS, has(Items.EGG))
                .save(output, cakeOvenLoc(ModBlocks.CUPCAKE_VARIANT.getCake()));

        cakeBaking(ModBlocks.CARROT_CAKE_FAMILY, 0.6F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.CARROT))
                .unlockedBy(HAS, has(Items.CARROT))
                .save(output, cakeOvenLoc(ModBlocks.CARROT_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHEESECAKE_FAMILY, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(ModItemTags.CHEESE))
                .unlockedBy(HAS, has(ModItems.CREAM_CHEESE.get()))
                .save(output, cakeOvenLoc(ModBlocks.CHEESECAKE_FAMILY));

        cakeBaking(ModBlocks.SWEET_BERRY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.SWEET_BERRIES))
                .unlockedBy(HAS, has(Items.SWEET_BERRIES))
                .save(output, cakeOvenLoc(ModBlocks.SWEET_BERRY_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHOCOLATE_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.COCOA_BEANS))
                .unlockedBy(HAS, has(Items.COCOA_BEANS))
                .save(output, cakeOvenLoc(ModBlocks.CHOCOLATE_CAKE_FAMILY));

        cakeBaking(ModBlocks.HONEY_CAKE_FAMILY, 0.7F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.HONEY_BOTTLE))
                .unlockedBy(HAS, has(Items.HONEY_BOTTLE))
                .save(output, cakeOvenLoc(ModBlocks.HONEY_CAKE_FAMILY));

        cakeBaking(ModBlocks.APPLE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.APPLE))
                .unlockedBy(HAS, has(Items.APPLE))
                .save(output, cakeOvenLoc(ModBlocks.APPLE_CAKE_FAMILY));

        cakeBaking(ModBlocks.POISON_CAKE_VARIANT.getCake().get(), 0.5F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.SPIDER_EYE))
                .unlockedBy(HAS, has(Items.SPIDER_EYE))
                .save(output, cakeOvenLoc(ModBlocks.POISON_CAKE_VARIANT.getCake()));

        cakeBaking(ModBlocks.TNT_CAKE_VARIANT.getCake().get(), 0.4F, 350, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Blocks.TNT))
                .unlockedBy(HAS, has(Blocks.TNT))
                .save(output, cakeOvenLoc(ModBlocks.TNT_CAKE_VARIANT.getCake()));

        cakeBaking(ModBlocks.PUMPKIN_CAKE_FAMILY, 0.6F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Blocks.PUMPKIN))
                .unlockedBy(HAS, has(Blocks.PUMPKIN))
                .save(output, cakeOvenLoc(ModBlocks.PUMPKIN_CAKE_FAMILY));

        cakeBaking(ModBlocks.RED_VELVET_CAKE_FAMILY, 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(ModItemTags.RED_DYE), Ingredient.of(Items.COCOA_BEANS))
                .unlockedBy(HAS, has(Items.COCOA_BEANS))
                .save(output, cakeOvenLoc(ModBlocks.RED_VELVET_CAKE_FAMILY));

        cakeBaking(ModBlocks.GLOW_BERRY_CAKE_FAMILY, 0.5F, 350, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.GLOW_BERRIES))
                .unlockedBy(HAS, has(Items.GLOW_BERRIES))
                .save(output, cakeOvenLoc(ModBlocks.GLOW_BERRY_CAKE_FAMILY));

        cakeBaking(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Blocks.BROWN_MUSHROOM), Ingredient.of(Blocks.BROWN_MUSHROOM))
                .unlockedBy(HAS, has(Blocks.BROWN_MUSHROOM))
                .save(output, cakeOvenLoc(ModBlocks.BROWN_MUSHROOM_CAKE_FAMILY));

        cakeBaking(ModBlocks.RED_MUSHROOM_CAKE_FAMILY, 0.4F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Blocks.RED_MUSHROOM), Ingredient.of(Blocks.RED_MUSHROOM))
                .unlockedBy(HAS, has(Blocks.RED_MUSHROOM))
                .save(output, cakeOvenLoc(ModBlocks.RED_MUSHROOM_CAKE_FAMILY));

        cakeBaking(ModBlocks.REDSTONE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.REDSTONE))
                .unlockedBy(HAS, has(Items.REDSTONE))
                .save(output, cakeOvenLoc(ModBlocks.REDSTONE_CAKE_FAMILY));

        cakeBaking(ModBlocks.SEED_CAKE_FAMILY, 0.3F, 150, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(ModItemTags.SEEDS), Ingredient.of(ModItemTags.SEEDS))
                .unlockedBy(HAS, has(ModItemTags.SEEDS))
                .save(output, cakeOvenLoc(ModBlocks.SEED_CAKE_FAMILY));

        cakeBaking(ModBlocks.SLIME_CAKE_FAMILY, 0.8F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(ModItemTags.SLIME_BALLS), Ingredient.of(ModItemTags.SLIME_BALLS))
                .unlockedBy(HAS, has(ModItemTags.SLIME_BALLS))
                .save(output, cakeOvenLoc(ModBlocks.SLIME_CAKE_FAMILY));

        cakeBaking(ModBlocks.CHORUS_CAKE_FAMILY, 0.7F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.CHORUS_FRUIT))
                .unlockedBy(HAS, has(Items.CHORUS_FRUIT))
                .save(output, cakeOvenLoc(ModBlocks.CHORUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.COOKIE_CAKE_FAMILY, 0.6F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.COOKIE))
                .unlockedBy(HAS, has(Items.COOKIE))
                .save(output, cakeOvenLoc(ModBlocks.COOKIE_CAKE_FAMILY));

        cakeBaking(ModBlocks.ENDER_CAKE_FAMILY, 0.8F, 350, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.BLAZE_POWDER))
                .unlockedBy(HAS, has(Items.ENDER_PEARL))
                .save(output, cakeOvenLoc(ModBlocks.ENDER_CAKE_FAMILY));

        cakeBaking(ModBlocks.GLOWSTONE_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.GLOWSTONE_DUST))
                .unlockedBy(HAS, has(Items.GLOWSTONE_DUST))
                .save(output, cakeOvenLoc(ModBlocks.GLOWSTONE_CAKE_FAMILY));

        cakeBaking(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY, 0.8F, 400, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.GOLDEN_APPLE))
                .unlockedBy(HAS, has(Items.GOLDEN_APPLE))
                .save(output, cakeOvenLoc(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY));

        cakeBaking(ModBlocks.ICE_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.ICE))
                .unlockedBy(HAS, has(Blocks.ICE))
                .save(output, cakeOvenLoc(ModBlocks.ICE_CAKE_FAMILY));

        cakeBaking(ModBlocks.CRIMSON_FUNGUS_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.CRIMSON_FUNGUS), Ingredient.of(Items.CRIMSON_FUNGUS))
                .unlockedBy(HAS, has(Blocks.CRIMSON_FUNGUS))
                .save(output, cakeOvenLoc(ModBlocks.CRIMSON_FUNGUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.WARPED_FUNGUS_CAKE_FAMILY, 0.5F, 250, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.WARPED_FUNGUS), Ingredient.of(Items.WARPED_FUNGUS))
                .unlockedBy(HAS, has(Items.WARPED_FUNGUS))
                .save(output, cakeOvenLoc(ModBlocks.WARPED_FUNGUS_CAKE_FAMILY));

        cakeBaking(ModBlocks.MELON_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.MELON_SLICE))
                .unlockedBy(HAS, has(Items.MELON_SLICE))
                .save(output, cakeOvenLoc(ModBlocks.MELON_CAKE_FAMILY));

        cakeBaking(ModBlocks.BEETROOT_CAKE_FAMILY, 0.4F, 300, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.BEETROOT))
                .unlockedBy(HAS, has(Items.BEETROOT))
                .save(output, cakeOvenLoc(ModBlocks.BEETROOT_CAKE_FAMILY));

        cakeBaking(ModBlocks.LAVA_CAKE_FAMILY, 0.4F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(Items.LAVA_BUCKET))
                .unlockedBy(HAS, has(Items.LAVA_BUCKET))
                .save(output, cakeOvenLoc(ModBlocks.LAVA_CAKE_FAMILY));

        cakeBaking(ModBlocks.FIREY_CAKE_FAMILY, 0.5F, 200, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.MAGMA_CREAM))
                .unlockedBy(HAS, has(Items.MAGMA_CREAM))
                .save(output, cakeOvenLoc(ModBlocks.FIREY_CAKE_FAMILY));

        cakeBaking(ModBlocks.OBSIDIAN_CAKE_FAMILY, 1, 800, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Blocks.OBSIDIAN))
                .unlockedBy(HAS, has(Blocks.OBSIDIAN))
                .save(output, cakeOvenLoc(ModBlocks.OBSIDIAN_CAKE_FAMILY));

        cakeBaking(ModBlocks.SCULK_CAKE_FAMILY, 0.7F, 350, RecipeCategory.FOOD, Ingredient.of(ModItems.CAKE_DOUGH.get()), Ingredient.of(ModItems.CAKE_FROSTING.get()), Ingredient.of(Items.ECHO_SHARD))
                .unlockedBy(HAS, has(Items.ECHO_SHARD))
                .save(output, cakeOvenLoc(ModBlocks.SCULK_CAKE_FAMILY));
    }

    private static void mixingRecipes(RecipeOutput output) {
        mixing(RecipeCategory.FOOD, ModItems.CAKE_DOUGH.get(), loc("cake_dough"), 12, Items.WHEAT, Items.WHEAT, Items.EGG, Items.SUGAR)
                .unlockedBy(HAS, has(Items.EGG))
                .save(output, mixingLoc(ModItems.CAKE_DOUGH));

        mixing(RecipeCategory.FOOD, ModItems.CUPCAKE_DOUGH.get(), loc("cake_dough"), 2, 5, Items.WHEAT, Items.EGG, Items.SUGAR)
                .unlockedBy(HAS, has(Items.EGG))
                .save(output, mixingLoc(ModItems.CUPCAKE_DOUGH));

        mixing(RecipeCategory.FOOD, ModItems.CREAM_CHEESE.get(), loc("cream_cheese"), 2, 9, Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET, Items.MILK_BUCKET)
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(output, mixingLoc(ModItems.CREAM_CHEESE));

        mixing(RecipeCategory.FOOD, ModItems.CAKE_FROSTING.get(), loc("frosting"), 12, Items.MILK_BUCKET, Items.MILK_BUCKET, Items.SUGAR)
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(output, mixingLoc(ModItems.CAKE_FROSTING));

        mixing(RecipeCategory.FOOD, ModItems.CUPCAKE_FROSTING.get(), loc("frosting"), 2, 7, Items.MILK_BUCKET, Items.SUGAR)
                .unlockedBy(HAS, has(Items.MILK_BUCKET))
                .save(output, mixingLoc(ModItems.CUPCAKE_FROSTING));
    }

    private static ResourceLocation craftingLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_crafting");
    }

    private static ResourceLocation cakeOvenLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_cake_oven");
    }

    private static ResourceLocation cakeOvenLoc(CakeFamily family) {
        return cakeOvenLoc(family.getBaseItem());
    }

    private static ResourceLocation mixingLoc(Supplier<? extends ItemLike> item) {
        return getItemId(item.get().asItem()).withSuffix("_from_mixing");
    }
}
