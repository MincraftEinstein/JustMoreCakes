package einstein.jmc.data;

import java.util.ArrayList;
import java.util.List;

import einstein.jmc.JustMoreCakes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModDataGenerators {

	public static List<String> CAKE_TYPES = new ArrayList<>();
	
	@SubscribeEvent
	public static void DataGenerator(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		generator.addProvider(new CraftingRecipesGenerator(generator));
		generator.addProvider(new CakeOvenRecipesGenerator(generator));
		generator.addProvider(new ModLootTableProvider(generator));
		generator.addProvider(new BlockAssetsGenerator(generator, event.getExistingFileHelper()));
		generator.addProvider(new ItemAssetsGenerator(generator, event.getExistingFileHelper()));
		BlockTagsProvider blockTags = new BlockTagsGenerator(generator, event.getExistingFileHelper()); // Used for both item and block tags
		generator.addProvider(blockTags);
		generator.addProvider(new ItemTagsGenerator(generator, blockTags, event.getExistingFileHelper()));
	}
}
