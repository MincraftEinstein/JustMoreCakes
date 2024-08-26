package einstein.jmc.data.packs.providers.farmersdelight;

import einstein.jmc.JustMoreCakes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

import static einstein.jmc.data.packs.providers.ModLootModifiersProvider.addCakeSpatulaDrop;

public class FDSupportLootModifiers extends GlobalLootModifierProvider {

    public FDSupportLootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID);
    }

    @Override
    protected void start() {
        add("sweet_berry_cheesecake", addCakeSpatulaDrop(ModBlocks.SWEET_BERRY_CHEESECAKE.get()));
        add("apple_pie", addCakeSpatulaDrop(ModBlocks.APPLE_PIE.get()));
        add("chocolate_pie", addCakeSpatulaDrop(ModBlocks.CHOCOLATE_PIE.get()));
    }
}
