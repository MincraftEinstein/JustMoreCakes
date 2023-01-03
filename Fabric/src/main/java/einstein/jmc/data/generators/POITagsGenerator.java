package einstein.jmc.data.generators;

import einstein.jmc.init.ModVillagers;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class POITagsGenerator extends FabricTagProvider<PoiType> {

    public POITagsGenerator(FabricDataGenerator generator) {
        super(generator, Registry.POINT_OF_INTEREST_TYPE);
    }

    @Override
    protected void generateTags() {
        getOrCreateTagBuilder(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(ModVillagers.CAKE_BAKER_POI.get());
    }
}
