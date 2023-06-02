package einstein.jmc.data.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.OBSIDIAN_CAKE.get());
        getOrCreateTagBuilder(ModBlockTags.CAKE_SPATULA_USABLE).add(ModBlocks.CHOCOLATE_CAKE.get());
        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, cakeBuilder) -> {
            getOrCreateTagBuilder(ModBlockTags.CAKES).add(cake.get());
            cakeBuilder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                getOrCreateTagBuilder(ModBlockTags.CANDLE_CAKES).add(candleCake.get());
            });
        });

        getOrCreateTagBuilder(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        getOrCreateTagBuilder(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
    }
}
