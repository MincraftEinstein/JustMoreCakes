package einstein.jmc.data.generators;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModVillagers;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class POITagsGenerator extends PoiTypeTagsProvider {

    public POITagsGenerator(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(PoiTypeTags.ACQUIRABLE_JOB_SITE).add(ModVillagers.CAKE_BAKER_POI.get());
    }

    @Override
    public String getName() {
        return "JustMoreCakes POI tags";
    }
}
