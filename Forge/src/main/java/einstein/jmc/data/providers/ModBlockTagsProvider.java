package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public static final TagKey<Block> F_CAKES = BlockTags.create(new ResourceLocation("forge", "cakes"));
    public static final TagKey<Block> F_CANDLE_CAKES = BlockTags.create(new ResourceLocation("forge", "candle_cakes"));

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JustMoreCakes.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.CAKE_OVEN.get(), ModBlocks.OBSIDIAN_CAKE.get());
        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, cakeBuilder) -> {
            tag(ModBlockTags.CAKES).add(cake.get());
            cakeBuilder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                tag(ModBlockTags.CANDLE_CAKES).add(candleCake.get());
            });
        });

        tag(ModBlockTags.C_CAKES).addTag(ModBlockTags.CAKES);
        tag(ModBlockTags.C_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(F_CAKES).addTag(ModBlockTags.CAKES);
        tag(F_CANDLE_CAKES).addTag(ModBlockTags.CANDLE_CAKES);
        tag(ModBlockTags.CAKE_SPATULA_USABLE).add(Blocks.CAKE, Blocks.CANDLE_CAKE, Blocks.WHITE_CANDLE_CAKE,
                        Blocks.ORANGE_CANDLE_CAKE, Blocks.MAGENTA_CANDLE_CAKE, Blocks.LIGHT_BLUE_CANDLE_CAKE,
                        Blocks.YELLOW_CANDLE_CAKE, Blocks.LIME_CANDLE_CAKE, Blocks.PINK_CANDLE_CAKE, Blocks.GRAY_CANDLE_CAKE,
                        Blocks.LIGHT_GRAY_CANDLE_CAKE, Blocks.CYAN_CANDLE_CAKE, Blocks.PURPLE_CANDLE_CAKE, Blocks.BLUE_CANDLE_CAKE,
                        Blocks.BROWN_CANDLE_CAKE, Blocks.GREEN_CANDLE_CAKE, Blocks.RED_CANDLE_CAKE, Blocks.BLACK_CANDLE_CAKE)
                .addTag(F_CAKES).addTag(F_CANDLE_CAKES);
    }
}
