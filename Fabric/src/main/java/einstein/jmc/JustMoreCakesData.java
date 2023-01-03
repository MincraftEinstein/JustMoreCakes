package einstein.jmc;

import einstein.jmc.data.generators.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class JustMoreCakesData implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        generator.addProvider(ItemTagsGenerator::new);
        generator.addProvider(BlockTagsGenerator::new);
        generator.addProvider(POITagsGenerator::new);
        generator.addProvider(AdvancementsGenerator::new);
        generator.addProvider(RecipesGenerator::new);
        generator.addProvider(ModelsGenerator::new);
        generator.addProvider(BlockLootTableGenerator::new);
        generator.addProvider(CakeEffectsGenerator::new);
    }
}
