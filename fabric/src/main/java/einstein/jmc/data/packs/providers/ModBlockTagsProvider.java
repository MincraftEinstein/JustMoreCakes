package einstein.jmc.data.packs.providers;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.data.packs.ModBlockTags;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeVariant;
import einstein.jmc.util.CakeStyle;
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
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.CAKE_STAND.get());
        ModBlocks.OBSIDIAN_CAKE_FAMILY.forEach(cake -> getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(cake.get()));

        getOrCreateTagBuilder(BlockTags.ICE).add(ModBlocks.ENCASING_ICE.get());

        Map<Supplier<BaseCakeBlock>, CakeVariant> sortedCakes = Util.createValueSortedMap(CakeVariant.VARIANT_BY_CAKE, Comparator.comparing(CakeVariant::getCakeName));

        sortedCakes.forEach((cake, variant) -> {
            CakeStyle style = variant.getStyle();

            switch (style) {
                case BASE -> getOrCreateTagBuilder(ModBlockTags.BASE_CAKES).add(cake.get());
                case TWO_TIERED -> getOrCreateTagBuilder(ModBlockTags.TWO_TIERED_CAKES).add(cake.get());
                case THREE_TIERED -> getOrCreateTagBuilder(ModBlockTags.THREE_TIERED_CAKES).add(cake.get());
            }

            Map<Block, Supplier<BaseCandleCakeBlock>> sortedCandleCakes = Util.createKeySortedMap(variant.getCandleCakeByCandle(), Comparator.comparing(o -> o.getName().toString()));
            sortedCandleCakes.forEach((candle, candleCake) -> {
                switch (style) {
                    case BASE -> getOrCreateTagBuilder(ModBlockTags.BASE_CANDLE_CAKES).add(candleCake.get());
                    case TWO_TIERED -> getOrCreateTagBuilder(ModBlockTags.TWO_TIERED_CANDLE_CAKES).add(candleCake.get());
                    case THREE_TIERED -> getOrCreateTagBuilder(ModBlockTags.THREE_TIERED_CANDLE_CAKES).add(candleCake.get());
                }
            });
        });

        getOrCreateTagBuilder(ModBlockTags.CAKES).addTag(ModBlockTags.BASE_CAKES).addTag(ModBlockTags.TWO_TIERED_CAKES).addTag(ModBlockTags.THREE_TIERED_CAKES);
        getOrCreateTagBuilder(ModBlockTags.CANDLE_CAKES).addTag(ModBlockTags.BASE_CANDLE_CAKES).addTag(ModBlockTags.TWO_TIERED_CANDLE_CAKES).addTag(ModBlockTags.THREE_TIERED_CANDLE_CAKES);
        getOrCreateTagBuilder(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        getOrCreateTagBuilder(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        getOrCreateTagBuilder(ModBlockTags.CAKE_SPATULA_USABLE).add(Blocks.CAKE).addTag(ModBlockTags.C_CAKES).addTag(ModBlockTags.C_CANDLE_CAKES);
        Util.getVanillaCandleCakes().forEach(cake -> getOrCreateTagBuilder(ModBlockTags.CAKE_SPATULA_USABLE).add(cake));
        getOrCreateTagBuilder(ModBlockTags.CAKE_STAND_STORABLES).add(Blocks.CAKE).addTag(ModBlockTags.BASE_CAKES);
    }
}
