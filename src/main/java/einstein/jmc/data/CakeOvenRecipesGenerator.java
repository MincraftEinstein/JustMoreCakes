package einstein.jmc.data;

import java.util.function.Consumer;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.item.crafting.builders.CakeOvenRecipeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

public class CakeOvenRecipesGenerator extends RecipeProvider {

	public CakeOvenRecipesGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		String h = "has_item";
		
		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHOCOLATE_CAKE.get(), 0.5F, 200, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.COCOA_BEANS))
			.unlockedBy(h, has(Items.COCOA_BEANS))
			.save(consumer, location(ModBlocks.CHOCOLATE_CAKE.get()));
		
		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.APPLE_CAKE.get(), 0.5F, 200, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.APPLE), Ingredient.of(Items.EGG))
			.unlockedBy(h, has(Items.APPLE))
			.save(consumer, location(ModBlocks.APPLE_CAKE.get()));
		
		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CUPCAKE.get(), 0.2F, 200, Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.MILK_BUCKET))
			.unlockedBy(h, has(Items.EGG))
			.save(consumer, location(ModBlocks.CUPCAKE.get()));
		
		CakeOvenRecipeBuilder.cakeBaking(Blocks.CAKE, 0.5F, 200, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
			.unlockedBy(h, has(Items.EGG))
			.save(consumer, location(Blocks.CAKE));
		
		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.POISON_CAKE.get(), 0.7F, 250, Ingredient.of(Items.WHEAT), Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.EGG), Ingredient.of(Items.MILK_BUCKET))
			.unlockedBy(h, has(Items.SPIDER_EYE))
			.save(consumer, location(ModBlocks.POISON_CAKE.get()));
	}
	
	private ResourceLocation location(ItemLike itemLike) {
		return new ResourceLocation(JustMoreCakes.MODID, Util.getItemRegistryName(itemLike.asItem()).getPath() + "_from_cake_oven");
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes cake oven recipes";
	}
}
