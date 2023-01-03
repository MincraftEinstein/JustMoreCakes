package einstein.jmc.data.generators;

import einstein.jmc.JustMoreCakes;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AdvancementsGenerator extends AdvancementProvider {

	public AdvancementsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, existingFileHelper);
	}
	
	@Override
	protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
		Advancement craftCake = ModAdvancements.craftCake().save(consumer, JustMoreCakes.loc("husbandry/craft_cake"), fileHelper);
		ModAdvancements.craftAllCakes(craftCake).save(consumer, JustMoreCakes.loc("husbandry/craft_all_cakes"), fileHelper);
	}

	@Override
	public String getName() {
		return "JustMoreCakes advancements";
	}
}
