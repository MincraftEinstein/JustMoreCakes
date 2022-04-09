package einstein.jmc.data;

import java.util.function.Consumer;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.Tag;
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
		cakeRecipe(consumer, Items.APPLE, ModBlocks.getBlock(ModBlocks.RL("apple_cake")));
		cakeRecipe(consumer, Items.BEETROOT, ModBlocks.getBlock(ModBlocks.RL("beetroot_cake")));
		cakeRecipe(consumer, Items.MILK_BUCKET, Blocks.TORCH, ModBlocks.BIRTHDAY_CAKE);
		cakeRecipe(consumer, Blocks.BROWN_MUSHROOM, ModBlocks.BROWN_MUSHROOM_CAKE);
		cakeRecipe(consumer, Items.CARROT, ModBlocks.getBlock(ModBlocks.RL("carrot_cake")));
		cakeRecipe(consumer, ItemTagsGenerator.CHEESE, ModBlocks.getBlock(ModBlocks.RL("cheesecake")));
		cakeRecipe(consumer, Items.COCOA_BEANS, ModBlocks.getBlock(ModBlocks.RL("chocolate_cake")));
		cakeRecipe(consumer, Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT, ModBlocks.CHORUS_CAKE);
		cakeRecipe(consumer, Items.COOKIE, ModBlocks.getBlock(ModBlocks.RL("cookie_cake")));
		cakeRecipe(consumer, Items.MILK_BUCKET, Blocks.CREEPER_HEAD, ModBlocks.getBlock(ModBlocks.RL("creeper_cake")));
		cakeRecipe(consumer, Blocks.CRIMSON_FUNGUS, ModBlocks.CRIMSON_FUNGUS_CAKE);
		blazeCakeRecipe(consumer, Items.ENDER_PEARL, ModBlocks.getBlock(ModBlocks.RL("ender_cake")));
		blazeCakeRecipe(consumer, Items.MAGMA_CREAM, ModBlocks.getBlock(ModBlocks.RL("firey_cake")));
		cakeRecipe(consumer, Items.GLOWSTONE_DUST, ModBlocks.GLOWSTONE_CAKE);
		cakeRecipe(consumer, Items.GOLDEN_APPLE, ModBlocks.getBlock(ModBlocks.RL("golden_apple_cake")));
		cakeRecipe(consumer, Items.HONEY_BOTTLE, Items.HONEYCOMB, ModBlocks.getBlock(ModBlocks.RL("honey_cake")));
		cakeRecipe(consumer, Blocks.ICE, ModBlocks.getBlock(ModBlocks.RL("ice_cake")));
		cakeRecipe(consumer, Items.LAVA_BUCKET, ModBlocks.getBlock(ModBlocks.RL("lava_cake")));
		cakeRecipe(consumer, Items.MELON_SLICE, ModBlocks.getBlock(ModBlocks.RL("melon_cake")));
		cakeRecipe(consumer, Items.SPIDER_EYE, ModBlocks.getBlock(ModBlocks.RL("poison_cake")));
		cakeRecipe(consumer, Blocks.PUMPKIN, ModBlocks.getBlock(ModBlocks.RL("pumpkin_cake")));
		cakeRecipe(consumer, Blocks.RED_MUSHROOM, ModBlocks.RED_MUSHROOM_CAKE);
		cakeRecipe(consumer, Items.REDSTONE, ModBlocks.getBlock(ModBlocks.RL("redstone_cake")));
		cakeRecipe(consumer, Tags.Items.SEEDS, ModBlocks.getBlock(ModBlocks.RL("seed_cake")));
		cakeRecipe(consumer, Tags.Items.SLIMEBALLS, ModBlocks.getBlock(ModBlocks.RL("slime_cake")));
		cakeRecipe(consumer, Items.SWEET_BERRIES, ModBlocks.getBlock(ModBlocks.RL("sweet_berry_cake")));
		cakeRecipe(consumer, Blocks.TNT, ModBlocks.TNT_CAKE);
		cakeRecipe(consumer, Blocks.WARPED_FUNGUS, ModBlocks.getBlock(ModBlocks.RL("warped_fungus_cake")));
		
		ShapedRecipeBuilder.shaped(ModBlocks.getBlock(ModBlocks.RL("christmas_cake")))
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
			.save(consumer, ModBlocks.getBlock(ModBlocks.RL("christmas_cake")).getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModItems.CUPCAKE)
			.pattern(" # ")
			.pattern("$~$")
			.pattern(" @ ")
			.define('#', Items.MILK_BUCKET)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy("has_item", has(Items.EGG))
			.save(consumer, ModItems.CUPCAKE.getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.getBlock(ModBlocks.RL("sprinkle_cake")))
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
			.save(consumer, ModBlocks.getBlock(ModBlocks.RL("sprinkle_cake")).getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.getBlock(ModBlocks.RL("three_tiered_cake")))
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Items.CAKE)
			.unlockedBy("has_item", has(Items.CAKE))
			.save(consumer, ModBlocks.getBlock(ModBlocks.RL("three_tiered_cake")).getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModItems.CHEESE, 3)
			.pattern("###")
			.pattern("###")
			.define('#', Items.MILK_BUCKET)
			.unlockedBy("has_item", has(Items.MILK_BUCKET))
			.save(consumer, ModItems.CHEESE.getRegistryName());
		
		ShapedRecipeBuilder.shaped(ModBlocks.CAKE_OVEN.asItem())
			.pattern("###")
			.pattern("#$#")
			.pattern("~~~")
			.define('#', Items.BRICK)
			.define('$', Blocks.FURNACE)
			.define('~', Blocks.SMOOTH_STONE)
			.unlockedBy("has_item", has(Blocks.SMOOTH_STONE))
			.save(consumer, ModBlocks.CAKE_OVEN.getRegistryName());
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
	
	public void cakeRecipe(Consumer<FinishedRecipe> consumer, Tag<Item> topping, ItemLike cake) {
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
