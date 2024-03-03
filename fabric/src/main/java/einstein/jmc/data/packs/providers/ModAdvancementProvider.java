package einstein.jmc.data.packs.providers;

import einstein.jmc.data.packs.ModAdvancements;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        ModAdvancements.generate(consumer);
    }
}
