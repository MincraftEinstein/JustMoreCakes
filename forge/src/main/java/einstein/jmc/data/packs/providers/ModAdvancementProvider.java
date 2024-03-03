package einstein.jmc.data.packs.providers;

import einstein.jmc.data.packs.ModAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends ForgeAdvancementProvider {

    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, fileHelper, List.of(new JMCAdvancements()));
    }

    public static class JMCAdvancements implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
            ModAdvancements.generate(consumer);
        }
    }
}
