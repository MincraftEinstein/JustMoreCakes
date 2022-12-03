package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.item.crafting.builders.CakeOvenRecipeBuilder;
import einstein.jmc.tags.ItemTagsGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class CakeOvenRecipesGenerator extends RecipeProvider {

	public CakeOvenRecipesGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		String has = "has_item";

		CakeOvenRecipeBuilder.cakeBaking(Blocks.CAKE, 0.5F, 200, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.EGG))
				.save(consumer, JustMoreCakes.loc("cake_from_cake_oven"));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CARROT_CAKE.get(), 0.6F, 300, Ingredient.of(Items.CARROT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.CARROT))
				.save(consumer, location(ModBlocks.CARROT_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHEESECAKE.get(), 0.7F, 250, Ingredient.of(ItemTagsGenerator.CHEESE), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(ModItems.CREAM_CHEESE.get()))
				.save(consumer, location(ModBlocks.CHEESECAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SWEET_BERRY_CAKE.get(), 0.5F, 200, Ingredient.of(Items.SWEET_BERRIES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Items.SWEET_BERRIES))
				.save(consumer, location(ModBlocks.SWEET_BERRY_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHOCOLATE_CAKE.get(), 0.6F, 250, Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.COCOA_BEANS))
				.save(consumer, location(ModBlocks.CHOCOLATE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.HONEY_CAKE.get(), 0.7F, 300, Ingredient.of(Items.HONEY_BOTTLE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.HONEY_BOTTLE))
				.save(consumer, location(ModBlocks.HONEY_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.APPLE_CAKE.get(), 0.5F, 200, Ingredient.of(Items.APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.APPLE))
				.save(consumer, location(ModBlocks.APPLE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.POISON_CAKE.get(), 0.5F, 300, Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Items.SPIDER_EYE))
				.save(consumer, location(ModBlocks.POISON_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.TNT_CAKE.get(), 0.4F, 350, Ingredient.of(Blocks.TNT), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Blocks.TNT))
				.save(consumer, location(ModBlocks.TNT_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.PUMPKIN_CAKE.get(), 0.6F, 250, Ingredient.of(Blocks.PUMPKIN), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Blocks.PUMPKIN))
				.save(consumer, location(ModBlocks.PUMPKIN_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_VELVET_CAKE.get(), 0.8F, 300, Ingredient.of(Tags.Items.DYES_RED), Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.COCOA_BEANS))
				.save(consumer, location(ModBlocks.RED_VELVET_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOW_BERRY_CAKE.get(), 0.5F, 350, Ingredient.of(Items.GLOW_BERRIES), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.GLOW_BERRIES))
				.save(consumer, location(ModBlocks.GLOW_BERRY_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BROWN_MUSHROOM_CAKE.get(), 0.4F, 250, Ingredient.of(Blocks.BROWN_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Blocks.BROWN_MUSHROOM))
				.save(consumer, location(ModBlocks.BROWN_MUSHROOM_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_MUSHROOM_CAKE.get(), 0.4F, 250, Ingredient.of(Blocks.RED_MUSHROOM), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Blocks.RED_MUSHROOM))
				.save(consumer, location(ModBlocks.RED_MUSHROOM_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.REDSTONE_CAKE.get(), 0.5F, 200, Ingredient.of(Items.REDSTONE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Items.REDSTONE))
				.save(consumer, location(ModBlocks.REDSTONE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SEED_CAKE.get(), 0.3F, 150, Ingredient.of(Tags.Items.SEEDS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Tags.Items.SEEDS))
				.save(consumer, location(ModBlocks.SEED_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SLIME_CAKE.get(), 0.8F, 300, Ingredient.of(Tags.Items.SLIMEBALLS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Tags.Items.SLIMEBALLS))
				.save(consumer, location(ModBlocks.SLIME_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHORUS_CAKE.get(), 0.7F, 250, Ingredient.of(Items.CHORUS_FRUIT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.CHORUS_FRUIT))
				.save(consumer, location(ModBlocks.CHORUS_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.COOKIE_CAKE.get(), 0.6F, 200, Ingredient.of(Items.COOKIE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.COOKIE))
				.save(consumer, location(ModBlocks.COOKIE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ENDER_CAKE.get(), 0.8F, 350, Ingredient.of(Items.ENDER_PEARL), Ingredient.of(Items.WHEAT), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Items.ENDER_PEARL))
				.save(consumer, location(ModBlocks.ENDER_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GLOWSTONE_CAKE.get(), 0.5F, 200, Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Items.GLOWSTONE_DUST))
				.save(consumer, location(ModBlocks.GLOWSTONE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.GOLDEN_APPLE_CAKE.get(), 0.8F, 400, Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.GOLDEN_APPLE))
				.save(consumer, location(ModBlocks.GOLDEN_APPLE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.ICE_CAKE.get(), 0.5F, 250, Ingredient.of(Items.ICE), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Blocks.ICE))
				.save(consumer, location(ModBlocks.ICE_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), 0.5F, 250, Ingredient.of(Items.CRIMSON_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Blocks.CRIMSON_FUNGUS))
				.save(consumer, location(ModBlocks.CRIMSON_FUNGUS_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.WARPED_FUNGUS_CAKE.get(), 0.5F, 250, Ingredient.of(Items.WARPED_FUNGUS), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.WARPED_FUNGUS))
				.save(consumer, location(ModBlocks.WARPED_FUNGUS_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.MELON_CAKE.get(), 0.5F, 200, Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
				.unlockedBy(has, has(Items.MELON_SLICE))
				.save(consumer, location(ModBlocks.MELON_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.BEETROOT_CAKE.get(), 0.4F, 300, Ingredient.of(Items.BEETROOT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.BEETROOT))
				.save(consumer, location(ModBlocks.BEETROOT_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.LAVA_CAKE.get(), 0.4F, 200, Ingredient.of(Items.LAVA_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Items.LAVA_BUCKET))
				.save(consumer, location(ModBlocks.LAVA_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.FIREY_CAKE.get(), 0.5F, 200, Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Items.WHEAT), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Items.MAGMA_CREAM))
				.save(consumer, location(ModBlocks.FIREY_CAKE));
	}
	
	private ResourceLocation location(RegistryObject<? extends ItemLike> item) {
		return JustMoreCakes.loc(item.getId().getPath() + "_from_cake_oven");
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes cake oven recipes";
	}
}
