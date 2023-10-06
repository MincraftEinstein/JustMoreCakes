package einstein.jmc.data.providers;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.blocks.ThreeTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.JustMoreCakes.mcLoc;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createTrivialBlock(ModBlocks.ENCASING_ICE.get(), new TextureMapping(), new ModelTemplate(Optional.of(mcLoc("block/ice")), Optional.empty()));

        defaultCakeBlock(generators, ModBlocks.TNT_CAKE.get());
        defaultCakeBlock(generators, ModBlocks.POISON_CAKE.get());
        crossCakeBlock(generators, ModBlocks.BROWN_MUSHROOM_CAKE.get(), mcLoc("block/brown_mushroom"));
        crossCakeBlock(generators, ModBlocks.RED_MUSHROOM_CAKE.get(), mcLoc("block/red_mushroom"));
        crossCakeBlock(generators, ModBlocks.CHORUS_CAKE.get(), loc("block/chorus_cake_flower"));
        crossCakeBlock(generators, ModBlocks.CRIMSON_FUNGUS_CAKE.get(), mcLoc("block/crimson_fungus"));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = CakeBuilder.BUILDER_BY_CAKE.get(cake);
            if (!builder.hasCustomBlockModel()) {
                cakeBlock(generators, cake.get());

                if (builder.allowsCandles()) {
                    for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                        Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                        candleCakeBlock(generators, candleCake.get(), candle, cake.get());
                    }
                }
            }
        }

        CakeBuilder threeTieredBuilder = ModBlocks.THREE_TIERED_CAKE.get().getBuilder();
        for (Block candle : threeTieredBuilder.getCandleCakeByCandle().keySet()) {
            BaseCandleCakeBlock cake = threeTieredBuilder.getCandleCakeByCandle().get(candle).get();
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(PropertyDispatch.property(ThreeTieredCandleCakeBlock.LIT)
                    .select(false, Variant.variant().with(VariantProperties.MODEL,
                            new ModelTemplate(Optional.of(loc("block/template_three_tiered_candle_cake")), Optional.empty(), TextureSlot.CANDLE)
                                    .create(cake, new TextureMapping()
                                            .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle)), generators.modelOutput)))
                    .select(true, Variant.variant().with(VariantProperties.MODEL,
                            new ModelTemplate(Optional.of(loc("block/template_three_tiered_candle_cake")), Optional.empty(), TextureSlot.CANDLE)
                                    .createWithSuffix(cake, "_lit", new TextureMapping()
                                            .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle, "_lit")), generators.modelOutput)))));
        }

        CakeBuilder sculkBuilder = ModBlocks.SCULK_CAKE.get().getBuilder();
        for (Block candle : sculkBuilder.getCandleCakeByCandle().keySet()) {
            Supplier<BaseCandleCakeBlock> candleCake = sculkBuilder.getCandleCakeByCandle().get(candle);
            candleCakeBlock(generators, candleCake.get(), candle, ModBlocks.SCULK_CAKE.get());
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.CUPCAKE.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModItems.CREAM_CHEESE.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModBlocks.POISON_CAKE.get().asItem(), Items.CAKE, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModBlocks.TNT_CAKE.get().asItem(), Items.CAKE, ModelTemplates.FLAT_ITEM);

        generators.output.accept(loc("item/encasing_ice"), new DelegatedModel(mcLoc("block/ice")));
        generators.output.accept(loc("item/cake_oven"), new DelegatedModel(loc("block/cake_oven")));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            if (!cake.get().getBuilder().hasCustomItemModel()) {
                generators.generateFlatItem(cake.get().asItem(), ModelTemplates.FLAT_ITEM);
            }
        }
    }

    private void cakeBlock(BlockModelGenerators generators, BaseCakeBlock cake) {
        ResourceLocation bottom = TextureMapping.getBlockTexture(cake, "_bottom");
        ResourceLocation top = TextureMapping.getBlockTexture(cake, "_top");
        ResourceLocation side = TextureMapping.getBlockTexture(cake, "_side");
        ResourceLocation inside = TextureMapping.getBlockTexture(cake, "_inner");

        if (cake.getBiteCount() > 0) {
            var builder = PropertyDispatch.property(BlockStateProperties.BITES);

            builder.select(0, blankCake(generators, cake, bottom, top, side));

            for (int i = 1; i < 7; i++) {
                builder.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc("block/template_cake_slice" + i)), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.INSIDE, TextureSlot.SIDE)
                                .createWithSuffix(cake, "_slice" + i, new TextureMapping()
                                        .put(TextureSlot.BOTTOM, bottom)
                                        .put(TextureSlot.TOP, top)
                                        .put(TextureSlot.INSIDE, inside)
                                        .put(TextureSlot.SIDE, side), generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(builder));
        }
        else {
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, blankCake(generators, cake, bottom, top, side)));
        }
    }

    private Variant blankCake(BlockModelGenerators generators, BaseCakeBlock cake, ResourceLocation bottom, ResourceLocation top, ResourceLocation side) {
        return Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc("block/template_cake")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE)
                        .create(cake, new TextureMapping()
                                .put(TextureSlot.BOTTOM, bottom)
                                .put(TextureSlot.TOP, top)
                                .put(TextureSlot.SIDE, side), generators.modelOutput));
    }

    private void candleCakeBlock(BlockModelGenerators generators, BaseCandleCakeBlock candleCake, Block candle, BaseCakeBlock cake) {
        ResourceLocation bottom = TextureMapping.getBlockTexture(cake, "_bottom");
        ResourceLocation top = TextureMapping.getBlockTexture(cake, "_top");
        ResourceLocation side = TextureMapping.getBlockTexture(cake, "_side");
        generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(candleCake).with(PropertyDispatch.property(BaseCandleCakeBlock.LIT)
                .select(false, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(mcLoc("block/template_cake_with_candle")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.CANDLE, TextureSlot.PARTICLE)
                                .create(candleCake, new TextureMapping()
                                        .put(TextureSlot.BOTTOM, bottom)
                                        .put(TextureSlot.TOP, top)
                                        .put(TextureSlot.SIDE, side)
                                        .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle))
                                        .put(TextureSlot.PARTICLE, side), generators.modelOutput)))
                .select(true, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(mcLoc("block/template_cake_with_candle")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.CANDLE, TextureSlot.PARTICLE)
                                .createWithSuffix(candleCake, "_lit", new TextureMapping()
                                        .put(TextureSlot.BOTTOM, bottom)
                                        .put(TextureSlot.TOP, top)
                                        .put(TextureSlot.SIDE, side)
                                        .put(TextureSlot.CANDLE, TextureMapping.getBlockTexture(candle, "_lit"))
                                        .put(TextureSlot.PARTICLE, side), generators.modelOutput)))));
    }

    private void crossCakeBlock(BlockModelGenerators generators, BaseCakeBlock cake, ResourceLocation cross) {
        ResourceLocation bottom = TextureMapping.getBlockTexture(cake, "_bottom");
        ResourceLocation top = TextureMapping.getBlockTexture(cake, "_top");
        ResourceLocation side = TextureMapping.getBlockTexture(cake, "_side");
        ResourceLocation inside = TextureMapping.getBlockTexture(cake, "_inner");

        if (cake.getBiteCount() > 0) {
            var builder = PropertyDispatch.property(BlockStateProperties.BITES);

            builder.select(0, blankCrossCake(generators, cake, bottom, top, side, cross));

            for (int i = 1; i < 7; i++) {
                TextureMapping mapping = new TextureMapping()
                        .put(TextureSlot.BOTTOM, bottom)
                        .put(TextureSlot.TOP, top)
                        .put(TextureSlot.INSIDE, inside)
                        .put(TextureSlot.SIDE, side);
                List<TextureSlot> slots = new ArrayList<>(List.of(TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.INSIDE, TextureSlot.SIDE));

                if (i < 4) {
                    slots.add(TextureSlot.CROSS);
                    mapping.put(TextureSlot.CROSS, cross);
                }

                builder.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc("block/template_cross_cake_slice" + i)), Optional.empty(), slots.toArray(new TextureSlot[]{}))
                                .createWithSuffix(cake, "_slice" + i, mapping, generators.modelOutput)));
            }

            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(builder));
        }
        else {
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, blankCrossCake(generators, cake, bottom, top, side, cross)));
        }
    }

    private Variant blankCrossCake(BlockModelGenerators generators, BaseCakeBlock cake, ResourceLocation bottom, ResourceLocation top, ResourceLocation side, ResourceLocation cross) {
        return Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc("block/template_cross_cake")), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.CROSS)
                        .create(cake, new TextureMapping()
                                .put(TextureSlot.BOTTOM, bottom)
                                .put(TextureSlot.TOP, top)
                                .put(TextureSlot.SIDE, side)
                                .put(TextureSlot.CROSS, cross), generators.modelOutput));
    }

    private void defaultCakeBlock(BlockModelGenerators generators, BaseCakeBlock cake) {
        CakeBuilder cakeBuilder = cake.getBuilder();

        if (cake.getBiteCount() > 0) {
            var builder = PropertyDispatch.property(BaseCakeBlock.BITES);

            builder.select(0, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE)));

            for (int i = 1; i < 7; i++) {
                builder.select(i, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE, "_slice" + i)));
            }

            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(builder));
        }
        else {
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL, ModelLocationUtils.getModelLocation(Blocks.CAKE))));
        }

        if (cakeBuilder.allowsCandles()) {
            for (Block candle : cakeBuilder.getCandleCakeByCandle().keySet()) {
                ResourceLocation type = ModBlocks.SUPPORTED_CANDLES.get(candle);
                BaseCandleCakeBlock candleCake = cakeBuilder.getCandleCakeByCandle().get(candle).get();
                generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(candleCake).with(BlockModelGenerators.createBooleanModelDispatch(BaseCandleCakeBlock.LIT,
                        new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle_cake_lit"),
                        new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle_cake"))));
            }
        }
    }
}
