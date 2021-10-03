package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagsGenerator extends ItemTagsProvider {

	public static final Tag.Named<Item> CHEESE = ItemTags.bind("forge:cheese");
	
	public ItemTagsGenerator(DataGenerator generator, BlockTagsProvider blockTags, ExistingFileHelper existingFileHelper) {
		super(generator, blockTags, JustMoreCakes.MODID, existingFileHelper);
	}
	
	@Override
	protected void addTags() {
		tag(CHEESE).add(ModItems.CHEESE);
	}
}
