package einstein.jmc.data;

import java.util.function.Consumer;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.tags.ItemTagsGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;

public class CraftingRecipesGenerator extends RecipeProvider {

	public CraftingRecipesGenerator(DataGenerator generator) {
		super(generator);
	}
	
	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		String has = "has_item";
		ShapedRecipeBuilder.shaped(ModItems.CUPCAKE.get())
			.pattern(" # ")
			.pattern("$~$")
			.pattern(" @ ")
			.define('#', Items.MILK_BUCKET)
			.define('$', Items.SUGAR)
			.define('~', Items.EGG)
			.define('@', Items.WHEAT)
			.unlockedBy(has, has(Items.EGG))
			.save(consumer, ModItems.CUPCAKE.getId());
		
		ShapedRecipeBuilder.shaped(ModBlocks.THREE_TIERED_CAKE.get())
			.pattern("#")
			.pattern("#")
			.pattern("#")
			.define('#', Items.CAKE)
			.unlockedBy(has, has(Items.CAKE))
			.save(consumer, ModBlocks.THREE_TIERED_CAKE.getId());
		
		ShapedRecipeBuilder.shaped(ModItems.CHEESE.get(), 3)
			.pattern("###")
			.pattern("###")
			.define('#', Items.MILK_BUCKET)
			.unlockedBy(has, has(Items.MILK_BUCKET))
			.save(consumer, ModItems.CHEESE.getId());
		
		ShapedRecipeBuilder.shaped(ModBlocks.CAKE_OVEN.get().asItem())
			.pattern("###")
			.pattern("#$#")
			.pattern("~~~")
			.define('#', Items.BRICK)
			.define('$', Blocks.FURNACE)
			.define('~', Blocks.SMOOTH_STONE)
			.unlockedBy(has, has(Blocks.SMOOTH_STONE))
			.save(consumer, ModBlocks.CAKE_OVEN.getId());
	}

	@Override
	public String getName() {
		return "JustMoreCakes crafting recipes";
	}
}
