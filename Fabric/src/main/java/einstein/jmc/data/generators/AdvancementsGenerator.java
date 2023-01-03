package einstein.jmc.data.generators;

import einstein.jmc.JustMoreCakes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class AdvancementsGenerator extends FabricAdvancementProvider {

    public AdvancementsGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement craftCake = ModAdvancements.craftCake().save(consumer, JustMoreCakes.loc("husbandry/craft_cake").toString());
        ModAdvancements.craftAllCakes(craftCake).save(consumer, JustMoreCakes.loc("husbandry/craft_all_cakes").toString());
    }
}
