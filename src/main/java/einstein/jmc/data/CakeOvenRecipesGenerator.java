package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.item.crafting.builders.CakeOvenRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class CakeOvenRecipesGenerator extends RecipeProvider {

	public CakeOvenRecipesGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		String has = "has_item";

//		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CUPCAKE.get(), 0.2F, 200, Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.MILK_BUCKET))
//			.unlockedBy(has, has(Items.EGG))
//			.save(consumer, location(ModBlocks.CUPCAKE.get()));

		CakeOvenRecipeBuilder.cakeBaking(Blocks.CAKE, 0.5F, 200, Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.WHEAT), Ingredient.of(Items.SUGAR), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.EGG))
				.save(consumer, new ResourceLocation(JustMoreCakes.MOD_ID, "cake_from_cake_oven"));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CARROT_CAKE.get(), 0.6F, 300, Ingredient.of(Items.CARROT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(Items.CARROT))
				.save(consumer, location(ModBlocks.CARROT_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHEESECAKE.get(), 0.7F, 250, Ingredient.of(ModItems.CHEESE.get()), Ingredient.of(Items.EGG), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
				.unlockedBy(has, has(ModItems.CHEESE.get()))
				.save(consumer, location(ModBlocks.CHEESECAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.SWEET_BERRY_CAKE.get(), 0.5F, 200, Ingredient.of(Items.SWEET_BERRIES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR))
				.unlockedBy(has, has(Items.SWEET_BERRIES))
				.save(consumer, location(ModBlocks.SWEET_BERRY_CAKE));

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.CHOCOLATE_CAKE.get(), 0.8F, 250, Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.SUGAR), Ingredient.of(Items.WHEAT))
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

		CakeOvenRecipeBuilder.cakeBaking(ModBlocks.RED_VELVET_CAKE.get(), 0.8F, 300, Ingredient.of(Items.RED_DYE), Ingredient.of(Items.COCOA_BEANS), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(Items.EGG))
				.unlockedBy(has, has(Items.COCOA_BEANS))
				.save(consumer, location(ModBlocks.RED_VELVET_CAKE));
	}
	
	private ResourceLocation location(RegistryObject<? extends ItemLike> item) {
		return new ResourceLocation(JustMoreCakes.MOD_ID, item.getId().getPath() + "_from_cake_oven");
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes cake oven recipes";
	}
}
