package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.tags.BlockTagsGenerator;
import einstein.jmc.tags.ItemTagsGenerator;
import einstein.jmc.tags.POITagsGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MOD_ID, bus = Bus.MOD)
public class ModDataGenerators {

	@SubscribeEvent
	public static void DataGenerator(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		// Server providers
		generator.addProvider(event.includeServer(), new CraftingRecipesGenerator(generator));
		generator.addProvider(event.includeServer(), new CakeOvenRecipesGenerator(generator));
		BlockTagsProvider blockTags = new BlockTagsGenerator(generator, event.getExistingFileHelper()); // Used for both item and block tags
		generator.addProvider(event.includeServer(), blockTags);
		generator.addProvider(event.includeServer(), new ItemTagsGenerator(generator, blockTags, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new POITagsGenerator(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new ModAdvancementsGenerator(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(generator));

		// Client providers
		generator.addProvider(event.includeClient(), new BlockAssetsGenerator(generator, event.getExistingFileHelper()));
		generator.addProvider(event.includeClient(), new ItemAssetsGenerator(generator, event.getExistingFileHelper()));
	}
}
