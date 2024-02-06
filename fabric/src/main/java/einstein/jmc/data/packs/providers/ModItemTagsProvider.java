package einstein.jmc.data.packs.providers;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.packs.ModItemTags;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Items;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagProvider blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Map<Supplier<BaseCakeBlock>, CakeBuilder> sortedCakes = Util.createValueSortedMap(CakeBuilder.BUILDER_BY_CAKE, Comparator.comparing(CakeBuilder::getCakeName));

        sortedCakes.forEach((cake, cakeBuilder) -> {
            if (!cake.get().asItem().equals(Items.AIR)) {
                getOrCreateTagBuilder(ModItemTags.CAKES).add(cake.get().asItem());
            }
        });

        ModBlocks.CHEESECAKE_FAMILY.forEach(cake -> {
            getOrCreateTagBuilder(ModItemTags.CHEESE_CAKES).add(cake.get().asItem());
            getOrCreateTagBuilder(ModItemTags.CHEESECAKES).add(cake.get().asItem());
        });

        getOrCreateTagBuilder(ModItemTags.CHEESE).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(ModItemTags.CHEESES).add(ModItems.CREAM_CHEESE.get());
        getOrCreateTagBuilder(ModItemTags.RED_DYE).addOptionalTag(ModItemTags.DYE_RED)
                .addOptionalTag(ConventionalItemTags.RED_DYES);
        getOrCreateTagBuilder(ModItemTags.SEEDS).add(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS);
        getOrCreateTagBuilder(ModItemTags.SLIME_BALLS).add(Items.SLIME_BALL);
        getOrCreateTagBuilder(ModItemTags.C_CAKES).addTag(ModItemTags.CAKES);
    }
}
