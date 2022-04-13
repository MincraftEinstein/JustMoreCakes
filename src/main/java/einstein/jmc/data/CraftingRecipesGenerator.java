package einstein.jmc.data;

import java.util.function.Consumer;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

public class CraftingRecipesGenerator extends RecipeProvider {

	public CraftingRecipesGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		cakeRecipe(consumer, Items.APPLE, ModBlocks.APPLE_CAKE.get());
		cakeRecipe(consumer, Items.BEETROOT, ModBlocks.BEETROOT_CAKE.get());
		cakeRecipe(consumer, Items.MILK_BUCKET, Blocks.TORCH, ModBlocks.BIRTHDAY_CAKE.get());
		cakeRecipe(consumer, Blocks.BROWN_MUSHROOM, ModBlocks.BROWN_MUSHROOM_CAKE.get());
		cakeRecipe(consumer, Items.CARROT, ModBlocks.CARROT_CAKE.get());
		cakeRecipe(consumer, ItemTagsGenerator.CHEESE, ModBlocks.CHEESECAKE.get());
		cakeRecipe(consumer, Items.COCOA_BEANS, ModBlocks.CHOCOLATE_CAKE.get());
		cakeRecipe(consumer, Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, ModBlocks.CHORUS_CAKE.get());
		cakeRecipe(consumer, Items.COOKIE, ModBlocks.COOKIE_CAKE.get());
		cakeRecipe(consumer, Items.MILK_BUCKET, Blocks.CREEPER_HEAD, ModBlocks.CREEPER_CAKE.get());
		cakeRecipe(consumer, Blocks.CRIMSON_FUNGUS, ModBlocks.CRIMSON_FUNGUS_CAKE.get());
		blazeCakeRecipe(consumer, Items.ENDER_PEARL, ModBlocks.ENDER_CAKE.get());
		blazeCakeRecipe(consumer, Items.MAGMA_CREAM, ModBlocks.FIREY_CAKE.get());
		cakeRecipe(consumer, Items.GLOWSTONE_DUST, ModBlocks.GLOWSTONE_CAKE.get());
		cakeRecipe(consumer, Items.GOLDEN_APPLE, ModBlocks.GOLDEN_APPLE_CAKE.get());
		cakeRecipe(consumer, Items.HONEY_BOTTLE, Items.HONEYCOMB, ModBlocks.HONEY_CAKE.get());
		cakeRecipe(consumer, Blocks.ICE, ModBlocks.ICE_CAKE.get());
		cakeRecipe(consumer, Items.LAVA_BUCKET, ModBlocks.LAVA_CAKE.get());
		cakeRecipe(consumer, Items.MELON_SLICE, ModBlocks.MELON_CAKE.get());
		cakeRecipe(consumer, Items.SPIDER_EYE, ModBlocks.POISON_CAKE.get());
		cakeRecipe(consumer, Blocks.PUMPKIN, ModBlocks.PUMPKIN_CAKE.get());
		cakeRecipe(consumer, Blocks.RED_MUSHROOM, ModBlocks.RED_MUSHROOM_CAKE.get());
		cakeRecipe(consumer, Items.REDSTONE, ModBlocks.REDSTONE_CAKE.get());
		cakeRecipe(consumer, Tags.Items.SEEDS, ModBlocks.SEED_CAKE.get());
		cakeRecipe(consumer, Tags.Items.SLIMEBALLS, ModBlocks.SLIME_CAKE.get());
		cakeRecipe(consumer, Items.SWEET_BERRIES, ModBlocks.SWEET_BERRY_CAKE.get());
		cakeRecipe(consumer, Blocks.TNT, ModBlocks.TNT_CAKE.get());
		cakeRecipe(consumer, Blocks.WARPED_FUNGUS, ModBlocks.WARPED_FUNGUS_CAKE.get());
		
		ShapedRecipeBuilder.shaped(ModBlocks.CHIRSTMAS_CAKE.get())
			.pattern("¡#¢")
			.pattern("$~$")
			.pattern(" @ ")
			.define('#', Items.MILK_BUCKET)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.define('¡', Tags.Items.DYES_GREEN)
			.define('¢', Tags.Items.DYES_RED)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, ModBlocks.CHIRSTMAS_CAKE.get().getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModItems.CUPCAKE.get())
			.pattern(" # ")
			.pattern("$~$")
			.pattern(" @ ")
			.define('#', Items.MILK_BUCKET)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, ModItems.CUPCAKE.get().getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.SPRINKLE_CAKE.get())
			.pattern("#$~")
			.pattern("@-¡")
			.pattern("¢£¤")
			.define('#', Tags.Items.DYES_PINK)
			.define('$', Tags.Items.DYES_RED)
			.define('~', Tags.Items.DYES_ORANGE)
			.define('@', Tags.Items.DYES_YELLOW)
			.define('-', Items.CAKE)
			.define('¡', Tags.Items.DYES_GREEN)
			.define('¢', Tags.Items.DYES_LIME)
			.define('£', Tags.Items.DYES_BLUE)
			.define('¤', Tags.Items.DYES_PURPLE)
			.unlockedBy("has_item", has(Blocks.CAKE))
			.save(consumer, ModBlocks.SPRINKLE_CAKE.get().getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.THREE_TIERED_CAKE.get())
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Items.CAKE)
			.unlockedBy("has_item", has(Items.CAKE))
			.save(consumer, ModBlocks.THREE_TIERED_CAKE.get().getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModItems.CHEESE.get(), 3)
			.pattern("###")
			.pattern("###")
			.define('#', Items.MILK_BUCKET)
			.unlockedBy("has_item", has(Items.MILK_BUCKET))
			.save(consumer, ModItems.CHEESE.get().getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.CAKE_OVEN.get().asItem())
			.pattern("###")
			.pattern("#$#")
			.pattern("~~~")
			.define('#', Items.BRICK)
			.define('$', Blocks.FURNACE)
			.define('~', Blocks.SMOOTH_STONE)
			.unlockedBy("has_item", has(Blocks.SMOOTH_STONE))
			.save(consumer, ModBlocks.CAKE_OVEN.get().getRegistryName());
	}
	
	public void cakeRecipe(Consumer<FinishedRecipe> consumer, ItemLike topping, ItemLike cake) {
		ShapedRecipeBuilder.shaped(cake)
			.pattern("###")
			.pattern("$~$")
			.pattern("@@@")
			.define('#', topping)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, cake.asItem().getRegistryName());
	}
	
	public void cakeRecipe(Consumer<FinishedRecipe> consumer, ItemLike topping1, ItemLike topping2, ItemLike cake) {
		ShapedRecipeBuilder.shaped(cake)
			.pattern("#-#")
			.pattern("$~$")
			.pattern("@@@")
			.define('#', topping1)
			.define('-', topping2)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, cake.asItem().getRegistryName());
	}
	
	public void cakeRecipe(Consumer<FinishedRecipe> consumer, TagKey<Item> topping, ItemLike cake) {
		ShapedRecipeBuilder.shaped(cake)
			.pattern("###")
			.pattern("$~$")
			.pattern("@@@")
			.define('#', topping)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, cake.asItem().getRegistryName());
	}
	
	public void blazeCakeRecipe(Consumer<FinishedRecipe> consumer, ItemLike topping, ItemLike cake) {
		ShapedRecipeBuilder.shaped(cake)
			.pattern("###")
			.pattern("$~$")
			.pattern("@@@")
			.define('#', topping)
			.define('$', Items.BLAZE_POWDER)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, cake.asItem().getRegistryName());
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes crafting recipes";
	}
}
