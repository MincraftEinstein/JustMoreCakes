package einstein.jmc.data.generators;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsGenerator extends ItemTagsProvider {

	public static final TagKey<Item> CHEESE = ItemTags.create(new ResourceLocation("forge", "cheese"));
	
	public ItemTagsGenerator(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
		super(generator, blockTags, JustMoreCakes.MOD_ID, existingFileHelper);
	}
	
	@Override
	protected void addTags() {
		tag(CHEESE).add(ModItems.CREAM_CHEESE.get());
		tag(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
		tag(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
		tag(ModItemTags.CHEESE_CAKES).add(ModBlocks.CHEESECAKE.get().asItem());
		tag(ModItemTags.CHEESECAKES).add(ModBlocks.CHEESECAKE.get().asItem());
		tag(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED.location())
				.addOptionalTag(ModItemTags.RED_DYES.location())
				.addOptionalTag(Tags.Items.DYES_RED.location());
		tag(ModItemTags.SEEDS).addTag(Tags.Items.SEEDS);
		tag(ModItemTags.SLIME_BALLS).addTag(Tags.Items.SLIMEBALLS);
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes item tags";
	}
}
