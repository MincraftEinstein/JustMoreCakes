package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.init.ModVillagers;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.RaidHeroGift;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {

    public ModDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void gather() {
        DataMapProvider.Builder<Compostable, Item> compostableBuilder = builder(NeoForgeDataMaps.COMPOSTABLES);
        for (CakeVariant variant : CakeVariant.VARIANT_BY_CAKE.values()) {
            if (variant.hasItem()) {
                compostableBuilder.add(loc(variant.getItem().get()), new Compostable(1), false);
            }
        }

        compostableBuilder.add(loc(ModBlocks.CUPCAKE_VARIANT.getCake().get()), new Compostable(0.25F), false);
        compostableBuilder.add(loc(ModItems.CREAM_CHEESE.get()), new Compostable(0.65F), false);

        builder(NeoForgeDataMaps.RAID_HERO_GIFTS)
                .add(BuiltInRegistries.VILLAGER_PROFESSION.getKey(ModVillagers.CAKE_BAKER.get()),
                        new RaidHeroGift(JustMoreCakes.CAKE_BAKER_GIFT), false);
    }

    private static ResourceLocation loc(ItemLike item) {
        return Util.getItemId(item.asItem());
    }
}
