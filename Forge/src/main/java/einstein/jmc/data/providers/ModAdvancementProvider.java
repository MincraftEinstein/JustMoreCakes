package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {

	public ModAdvancementProvider(PackOutput generator, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
		super(generator, lookupProvider, List.of(new JMCAdvancements(fileHelper)));
	}

	public static class JMCAdvancements implements AdvancementSubProvider {

		private final ExistingFileHelper fileHelper;

		public JMCAdvancements(ExistingFileHelper fileHelper) {
			this.fileHelper = fileHelper;
		}

		@Override
		public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer) {
			Advancement craftCake = ModAdvancements.craftCake().parent(JustMoreCakes.mcLoc("husbandry/plant_seed")).save(consumer, JustMoreCakes.loc("husbandry/craft_cake"), fileHelper);
			ModAdvancements.craftAllCakes(craftCake).save(consumer, JustMoreCakes.loc("husbandry/craft_all_cakes"), fileHelper);
			ModAdvancements.eatObsidianCake(craftCake).save(consumer, JustMoreCakes.loc("husbandry/eat_obsidian_cake"), fileHelper);
		}
	}
}
