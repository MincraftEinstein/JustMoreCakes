package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;

import java.util.HashMap;
import java.util.function.Consumer;

public class ModAdvancementProvider extends FabricAdvancementProvider {

    public ModAdvancementProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        Advancement plantSeedsDummy = new Advancement(JustMoreCakes.mcLoc("husbandry/plant_seed"), null, null, AdvancementRewards.EMPTY, new HashMap<>(), null);

        Advancement craftCake = ModAdvancements.craftCake().parent(plantSeedsDummy).save(consumer, JustMoreCakes.loc("husbandry/craft_cake").toString());
        ModAdvancements.craftAllCakes(craftCake).save(consumer, JustMoreCakes.loc("husbandry/craft_all_cakes").toString());
        ModAdvancements.eatObsidianCake(craftCake).save(consumer, JustMoreCakes.loc("husbandry/eat_obsidian_cake").toString());
    }
}
