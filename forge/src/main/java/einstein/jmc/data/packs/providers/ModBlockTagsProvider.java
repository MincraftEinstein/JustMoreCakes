package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.data.packs.ModBlockTags;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public static final TagKey<Block> F_CAKES = BlockTags.create(new ResourceLocation("forge", "cakes"));
    public static final TagKey<Block> F_CANDLE_CAKES = BlockTags.create(new ResourceLocation("forge", "candle_cakes"));

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.CAKE_STAND.get());
        ModBlocks.OBSIDIAN_CAKE_FAMILY.forEach(cake -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(cake.get()));

        tag(BlockTags.ICE).add(ModBlocks.ENCASING_ICE.get());

        Map<Supplier<BaseCakeBlock>, CakeBuilder> sortedCakes = Util.createValueSortedMap(CakeBuilder.BUILDER_BY_CAKE, Comparator.comparing(CakeBuilder::getCakeName));

        sortedCakes.forEach((cake, builder) -> {
            CakeVariant variant = builder.getVariant();

            switch (variant) {
                case BASE -> tag(ModBlockTags.BASE_CAKES).add(cake.get());
                case TWO_TIERED -> tag(ModBlockTags.TWO_TIERED_CAKES).add(cake.get());
                case THREE_TIERED -> tag(ModBlockTags.THREE_TIERED_CAKES).add(cake.get());
            }

            Map<Block, Supplier<BaseCandleCakeBlock>> sortedCandleCakes = Util.createKeySortedMap(builder.getCandleCakeByCandle(), Comparator.comparing(o -> o.getName().toString()));
            sortedCandleCakes.forEach((candle, candleCake) -> {
                switch (variant) {
                    case BASE -> tag(ModBlockTags.BASE_CANDLE_CAKES).add(candleCake.get());
                    case TWO_TIERED -> tag(ModBlockTags.TWO_TIERED_CANDLE_CAKES).add(candleCake.get());
                    case THREE_TIERED -> tag(ModBlockTags.THREE_TIERED_CANDLE_CAKES).add(candleCake.get());
                }
            });
        });

        tag(ModBlockTags.CAKES).addTag(ModBlockTags.BASE_CAKES).addTag(ModBlockTags.TWO_TIERED_CAKES).addTag(ModBlockTags.THREE_TIERED_CAKES);
        tag(ModBlockTags.CANDLE_CAKES).addTag(ModBlockTags.BASE_CANDLE_CAKES).addTag(ModBlockTags.TWO_TIERED_CANDLE_CAKES).addTag(ModBlockTags.THREE_TIERED_CANDLE_CAKES);
        tag(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        tag(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(F_CAKES).addTag(ModBlockTags.CAKES);
        tag(F_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(ModBlockTags.CAKE_SPATULA_USABLE).add(Blocks.CAKE).addTag(F_CAKES).addTag(F_CANDLE_CAKES);
        Util.getVanillaCandleCakes().forEach(cake -> tag(ModBlockTags.CAKE_SPATULA_USABLE).add(cake));
        tag(ModBlockTags.CAKE_STAND_STORABLES).add(Blocks.CAKE).addTag(ModBlockTags.BASE_CAKES);
    }
}
