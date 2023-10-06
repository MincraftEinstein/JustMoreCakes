package einstein.jmc.data.providers;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.OBSIDIAN_CAKE.get());

        Map<Supplier<BaseCakeBlock>, CakeBuilder> sortedCakes = Util.createValueSortedMap(CakeBuilder.BUILDER_BY_CAKE, Comparator.comparing(CakeBuilder::getCakeName));

        sortedCakes.forEach((cake, cakeBuilder) -> {
            getOrCreateTagBuilder(ModBlockTags.CAKES).add(cake.get());
            Map<Block, Supplier<BaseCandleCakeBlock>> sortedCandleCakes = Util.createKeySortedMap(cakeBuilder.getCandleCakeByCandle(), Comparator.comparing(o -> o.getName().toString()));
            sortedCandleCakes.forEach((candle, candleCake) -> {
                getOrCreateTagBuilder(ModBlockTags.CANDLE_CAKES).add(candleCake.get());
            });
        });

        getOrCreateTagBuilder(ModBlockTags.CAKES).add(ModBlocks.CUPCAKE.get());
        getOrCreateTagBuilder(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        getOrCreateTagBuilder(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        getOrCreateTagBuilder(ModBlockTags.CAKE_SPATULA_USABLE).add(Blocks.CAKE).addTag(ModBlockTags.C_CAKES).addTag(ModBlockTags.C_CANDLE_CAKES);
        Util.getVanillaCandleCakes().forEach(cake -> getOrCreateTagBuilder(ModBlockTags.CAKE_SPATULA_USABLE).add(cake));
    }
}
