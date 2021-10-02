package einstein.jmc.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class ModDataGenerators {

	@SubscribeEvent
	public static void DataGenerator(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
//		generator.addProvider(new CraftingRecipesGenerator(generator));
		generator.addProvider(new ModLootTableProvider(generator));
		generator.addProvider(new BlockAssetsGenerator(generator, event.getExistingFileHelper()));
//		generator.addProvider(new ItemAssetsGenerator(generator, event.getExistingFileHelper()));
//		BlockTagsProvider blockTags = new BlockTagsGenerator(generator, event.getExistingFileHelper()); // Used for both item and block tags
//		generator.addProvider(blockTags);
//		generator.addProvider(new ItemTagsGenerator(generator, blockTags, event.getExistingFileHelper()));
	}
}
