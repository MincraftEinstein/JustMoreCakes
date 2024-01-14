package einstein.jmc.data.packs.providers;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModItems;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.CakeModel;
import einstein.jmc.util.CakeVariant;
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
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.JustMoreCakes.mcLoc;
import static einstein.jmc.util.CakeBuilder.SUPPORTED_CANDLES;
import static net.minecraft.data.models.model.TextureMapping.getBlockTexture;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class ModModelProvider extends FabricModelProvider {

    private static final TextureSlot TOP_SIDE = TextureSlot.create("top_side");
    private static final TextureSlot TOP_INNER = TextureSlot.create("top_inner");
    private static final TextureSlot MIDDLE_TOP = TextureSlot.create("middle_top");
    private static final TextureSlot MIDDLE_SIDE = TextureSlot.create("middle_side");
    private static final TextureSlot MIDDLE_INNER = TextureSlot.create("middle_inner");
    private static final TextureSlot LOWER_TOP = TextureSlot.create("lower_top");
    private static final TextureSlot LOWER_SIDE = TextureSlot.create("lower_side");
    private static final TextureSlot LOWER_INNER = TextureSlot.create("lower_inner");

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateItemModels(ItemModelGenerators generators) {
        generators.generateFlatItem(ModItems.CUPCAKE.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModItems.CREAM_CHEESE.get(), ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModBlocks.POISON_CAKE.get().asItem(), Items.CAKE, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModBlocks.TNT_CAKE.get().asItem(), Items.CAKE, ModelTemplates.FLAT_ITEM);

        generators.output.accept(loc("item/encasing_ice"), new DelegatedModel(mcLoc("block/ice")));
        generators.output.accept(loc("item/cake_oven"), new DelegatedModel(loc("block/cake_oven")));
        generators.output.accept(loc("item/cake_stand"), new DelegatedModel(loc("block/cake_stand")));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            if (builder.hasItem() && !builder.hasCustomItemModel()) {
                generators.generateFlatItem(cake.get().asItem(), ModelTemplates.FLAT_ITEM);
            }
        });
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createTrivialBlock(ModBlocks.ENCASING_ICE.get(), new TextureMapping(), new ModelTemplate(Optional.of(mcLoc("block/ice")), Optional.empty()));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariant variant = builder.getVariant();
            CakeModel cakeModel = builder.getCakeModel();
            CakeModel candleCakeModel = builder.getCandleCakeModel();
            String cakeName = builder.getCakeName();

            if (cakeModel != CakeModel.CUSTOM) {
                if (cakeModel == CakeModel.DEFAULT) {
                    switch (variant) {
                        case BASE -> createCake(generators, cakeBlock, null);
                        case TWO_TIERED -> createTwoTieredCake(generators, cakeBlock, null);
                        case THREE_TIERED -> createThreeTieredCake(generators, cakeBlock, null);
                    }
                }
                else if (cakeModel == CakeModel.FROM_VANILLA) {
                    switch (variant) {
                        case BASE -> createFromVanillaCake(generators, cakeBlock, 7, mcLoc("block/cake"));
                        case TWO_TIERED -> createFromVanillaCake(generators, cakeBlock, 10, loc("block/two_tiered_cake"));
                        case THREE_TIERED -> {
                            List<Pair<DoubleBlockHalf, Integer>> halves = List.of(Pair.of(UPPER, 5), Pair.of(LOWER, 11));

                            for (Pair<DoubleBlockHalf, Integer> halfPair : halves) {
                                DoubleBlockHalf half = halfPair.getFirst();
                                Variant propertyVariant = Variant.variant().with(VariantProperties.MODEL, loc("three_tiered_cake_" + half));

                                if (cakeBlock.getSlices() > 0) {
                                    PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseThreeTieredCakeBlock.BITES);

                                    for (int i = 1; i < halfPair.getSecond(); i++) {
                                        dispatch.select(i, Variant.variant().with(VariantProperties.MODEL, loc("three_tiered_cake_" + half + "_slice" + (half == LOWER ? i - 5 : i))));
                                    }
                                    generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cakeBlock).with(dispatch));
                                }
                                else {
                                    PropertyDispatch.C1<DoubleBlockHalf> dispatch = PropertyDispatch.property(BaseThreeTieredCakeBlock.HALF);
                                    dispatch.select(half, propertyVariant);
                                    generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cakeBlock).with(dispatch));
                                }
                            }
                        }
                    }
                }
                else if (cakeModel instanceof CakeModel.CrossCakeModel crossModel) {
                    switch (variant) {
                        case BASE -> createCake(generators, cakeBlock, crossModel.crossTexture());
                        case TWO_TIERED -> createTwoTieredCake(generators, cakeBlock, crossModel.crossTexture());
                        case THREE_TIERED -> createThreeTieredCake(generators, cakeBlock, crossModel.crossTexture());
                    }
                }
            }

            if (candleCakeModel != CakeModel.CUSTOM && builder.allowsCandles()) {
                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    ResourceLocation candleType = SUPPORTED_CANDLES.get(candle);
                    BaseCandleCakeBlock candleCakeBlock = candleCake.get();
                    PropertyDispatch.C1<Boolean> dispatch = PropertyDispatch.property(BaseCandleCakeBlock.LIT);

                    if (candleCakeModel == CakeModel.FROM_VANILLA) {
                        switch (variant) {
                            case BASE -> {
                                createFromVanillaCandleCake(dispatch, candleType, candleType.getNamespace(), "cake", true);
                                createFromVanillaCandleCake(dispatch, candleType, candleType.getNamespace(), "cake", false);
                            }
                            case TWO_TIERED -> {
                                createFromVanillaCandleCake(dispatch, candleType, JustMoreCakes.MOD_ID, "two_tiered_cake", true);
                                createFromVanillaCandleCake(dispatch, candleType, JustMoreCakes.MOD_ID, "two_tiered_cake", false);
                            }
                            case THREE_TIERED -> {
                                PropertyDispatch.C2<Boolean, DoubleBlockHalf> threeTieredDispatch = PropertyDispatch.properties(BaseThreeTieredCandleCakeBlock.LIT, BaseThreeTieredCandleCakeBlock.HALF);
                                createFromVanillaThreeTieredCandleCake(threeTieredDispatch, candleType, UPPER, true);
                                createFromVanillaThreeTieredCandleCake(threeTieredDispatch, candleType, UPPER, false);
                                createFromVanillaThreeTieredCandleCake(threeTieredDispatch, candleType, LOWER, true);
                                createFromVanillaThreeTieredCandleCake(threeTieredDispatch, candleType, LOWER, false);
                                generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(candleCakeBlock).with(threeTieredDispatch));
                                return;
                            }
                        }
                    }
                    else if (candleCakeModel instanceof CakeModel.CrossCakeModel) {
                        throw new IllegalArgumentException("Cannot have a cross model candle cake");
                    }
                    else {
                        switch (variant) {
                            case BASE -> {
                                addVariant(generators, dispatch, candleCakeBlock, candle, true);
                                addVariant(generators, dispatch, candleCakeBlock, candle, false);
                            }
                            case TWO_TIERED -> {
                                addTwoTieredVariant(generators, dispatch, candleCakeBlock, candle, true);
                                addTwoTieredVariant(generators, dispatch, candleCakeBlock, candle, false);
                            }
                            case THREE_TIERED -> {
                                PropertyDispatch.C2<Boolean, DoubleBlockHalf> threeTieredDispatch = PropertyDispatch.properties(BaseThreeTieredCandleCakeBlock.LIT, BaseThreeTieredCandleCakeBlock.HALF);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, true, LOWER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, false, LOWER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, true, UPPER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, false, UPPER);
                                generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(candleCakeBlock).with(threeTieredDispatch));
                                return;
                            }
                        }
                    }
                    generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(candleCakeBlock).with(dispatch));
                });
            }
        });
    }

    private void addVariant(BlockModelGenerators generators, PropertyDispatch.C1<Boolean> dispatch, Block candleCake, Block candle, boolean lit) {
        String litName = lit ? "_lit" : "";

        dispatch.select(lit, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc("block/template_cake_with_candle")), Optional.of(litName), TextureSlot.CANDLE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.PARTICLE)
                        .create(candleCake, new TextureMapping()
                                        .put(TextureSlot.CANDLE, getBlockTexture(candle, litName))
                                        .put(TextureSlot.BOTTOM, getBlockTexture(candleCake, "_bottom"))
                                        .put(TextureSlot.TOP, getBlockTexture(candleCake, "_top"))
                                        .put(TextureSlot.SIDE, getBlockTexture(candleCake, "_side"))
                                        .put(TextureSlot.PARTICLE, new ResourceLocation("", "#side")),
                                generators.modelOutput)
        ));
    }

    private void addTwoTieredVariant(BlockModelGenerators generators, PropertyDispatch.C1<Boolean> dispatch, Block candleCake, Block candle, boolean lit) {
        String litName = lit ? "_lit" : "";

        dispatch.select(lit, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc("block/template_two_tiered_candle_cake")), Optional.of(litName), TextureSlot.CANDLE, TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, LOWER_TOP, LOWER_SIDE)
                        .create(candleCake, new TextureMapping()
                                        .put(TextureSlot.CANDLE, getBlockTexture(candle, litName))
                                        .put(TextureSlot.BOTTOM, getBlockTexture(candleCake, "_bottom"))
                                        .put(TextureSlot.TOP, getBlockTexture(candleCake, "_top"))
                                        .put(TOP_SIDE, getBlockTexture(candleCake, "_top_side"))
                                        .put(LOWER_TOP, getBlockTexture(candleCake, "_lower_top"))
                                        .put(LOWER_SIDE, getBlockTexture(candleCake, "_lower_side")),
                                generators.modelOutput)
        ));
    }

    private void addThreeTieredVariant(BlockModelGenerators generators, PropertyDispatch.C2<Boolean, DoubleBlockHalf> dispatch, Block candleCake, Block candle, boolean lit, DoubleBlockHalf half) {
        String litName = lit ? "_lit" : "";
        String halfName = "_" + half;

        TextureMapping mapping = new TextureMapping().put(TextureSlot.BOTTOM, getBlockTexture(candleCake, "_bottom"))
                .put(TextureSlot.CANDLE, getBlockTexture(candle, litName));

        if (half == LOWER) {
            mapping.put(MIDDLE_SIDE, getBlockTexture(candleCake, "_middle_side"))
                    .put(MIDDLE_TOP, getBlockTexture(candleCake, "_middle_top"))
                    .put(LOWER_SIDE, getBlockTexture(candleCake, "_lower_side"))
                    .put(LOWER_TOP, getBlockTexture(candleCake, "_lower_top"));
        }
        else {
            mapping.put(TOP_SIDE, getBlockTexture(candleCake, "_top_side"))
                    .put(TextureSlot.TOP, getBlockTexture(candleCake, "_top"));
        }

        dispatch.select(lit, half, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc("block/template_three_tiered_candle_cake" + halfName)), Optional.of(halfName + litName), TextureSlot.CANDLE, TextureSlot.BOTTOM, MIDDLE_SIDE, MIDDLE_TOP, LOWER_SIDE, LOWER_TOP, TOP_SIDE, TextureSlot.TOP)
                        .create(candleCake, mapping, generators.modelOutput))
        );
    }

    private void createCake(BlockModelGenerators generators, BaseCakeBlock cake, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_cake" : "block/template_cake";
        ResourceLocation bottom = getBlockTexture(cake, "_bottom");
        ResourceLocation top = getBlockTexture(cake, "_top");
        ResourceLocation side = getBlockTexture(cake, "_side");
        ResourceLocation inside = getBlockTexture(cake, "_inner");

        if (cake.getSlices() > 0) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseCakeBlock.BITES);

            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottom)
                    .put(TextureSlot.TOP, top)
                    .put(TextureSlot.SIDE, side);

            addCross(mapping, crossTexture);
            dispatch.select(0, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.CROSS)
                            .create(cake, mapping, generators.modelOutput)));

            for (int i = 1; i < 7; i++) {
                TextureMapping sliceMapping = new TextureMapping()
                        .put(TextureSlot.BOTTOM, bottom)
                        .put(TextureSlot.TOP, top)
                        .put(TextureSlot.INSIDE, inside)
                        .put(TextureSlot.SIDE, side);

                if (i < 4) {
                    addCross(sliceMapping, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.INSIDE, TextureSlot.SIDE, TextureSlot.CROSS)
                                .create(cake, sliceMapping, generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottom)
                    .put(TextureSlot.TOP, top)
                    .put(TextureSlot.SIDE, side);

            addCross(mapping, crossTexture);
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.CROSS)
                            .create(cake, mapping, generators.modelOutput))));
        }
    }

    private void createTwoTieredCake(BlockModelGenerators generators, BaseCakeBlock cake, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_two_tiered_cake" : "block/template_two_tiered_cake";
        ResourceLocation bottom = getBlockTexture(cake, "_bottom");
        ResourceLocation top = getBlockTexture(cake, "_top");
        ResourceLocation topSide = getBlockTexture(cake, "_top_side");
        ResourceLocation lowerTop = getBlockTexture(cake, "_lower_top");
        ResourceLocation lowerSide = getBlockTexture(cake, "_lower_side");

        if (cake.getSlices() > 0) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseTwoTieredCakeBlock.BITES);
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottom)
                    .put(TextureSlot.TOP, top)
                    .put(TOP_SIDE, topSide)
                    .put(LOWER_SIDE, lowerSide)
                    .put(LOWER_TOP, lowerTop);

            addCross(mapping, crossTexture);
            dispatch.select(0, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, LOWER_SIDE, LOWER_TOP, TextureSlot.CROSS)
                            .create(cake, mapping, generators.modelOutput)));

            for (int i = 1; i < 11; i++) {
                TextureMapping sliceMapping = new TextureMapping()
                        .put(TextureSlot.BOTTOM, bottom)
                        .put(LOWER_SIDE, lowerSide)
                        .put(LOWER_TOP, lowerTop);

                if (i < 5) {
                    sliceMapping.put(TextureSlot.TOP, top)
                            .put(TOP_SIDE, topSide)
                            .put(TOP_INNER, getBlockTexture(cake, "_top_inner"));
                }

                if (i > 5) {
                    sliceMapping.put(LOWER_INNER, getBlockTexture(cake, "_lower_inner"));
                }

                if (i < 3) {
                    addCross(sliceMapping, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, TOP_INNER, LOWER_TOP, LOWER_SIDE, LOWER_INNER, TextureSlot.CROSS)
                                .create(cake, sliceMapping, generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            TextureMapping mapping = new TextureMapping()
                    .put(TextureSlot.BOTTOM, bottom)
                    .put(TextureSlot.TOP, top)
                    .put(TOP_SIDE, topSide)
                    .put(LOWER_TOP, lowerTop)
                    .put(LOWER_SIDE, lowerSide);

            addCross(mapping, crossTexture);
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, LOWER_TOP, LOWER_SIDE, TextureSlot.CROSS)
                            .create(cake, mapping, generators.modelOutput))));
        }
    }

    private void createThreeTieredCake(BlockModelGenerators generators, BaseCakeBlock cake, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_three_tiered_cake" : "block/template_three_tiered_cake";
        ResourceLocation bottom = getBlockTexture(cake, "_bottom");
        ResourceLocation top = getBlockTexture(cake, "_top");
        ResourceLocation topSide = getBlockTexture(cake, "_top_side");
        ResourceLocation topInner = getBlockTexture(cake, "_top_inner");
        ResourceLocation middleTop = getBlockTexture(cake, "_middle_top");
        ResourceLocation middleSide = getBlockTexture(cake, "_middle_side");
        ResourceLocation middleInner = getBlockTexture(cake, "_top_inner");
        ResourceLocation lowerTop = getBlockTexture(cake, "_lower_top");
        ResourceLocation lowerSide = getBlockTexture(cake, "_lower_side");
        ResourceLocation lowerInner = getBlockTexture(cake, "_top_inner");

        TextureMapping upperMapping = new TextureMapping()
                .put(TextureSlot.BOTTOM, bottom)
                .put(TextureSlot.TOP, top)
                .put(TOP_SIDE, topSide);
        addCross(upperMapping, crossTexture);
        Variant upperVariant = Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc(parentModel + "_upper")), Optional.of("_upper"), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, TextureSlot.CROSS)
                        .create(cake, upperMapping, generators.modelOutput));

        TextureMapping lowerMapping = new TextureMapping()
                .put(TextureSlot.BOTTOM, bottom)
                .put(MIDDLE_TOP, middleTop)
                .put(MIDDLE_SIDE, middleSide)
                .put(LOWER_TOP, lowerTop)
                .put(LOWER_SIDE, lowerSide);
        addCross(lowerMapping, crossTexture);
        Variant lowerVariant = Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc(parentModel + "_lower")), Optional.of("_lower"), TextureSlot.BOTTOM, MIDDLE_TOP, MIDDLE_SIDE, LOWER_TOP, LOWER_SIDE)
                        .create(cake, lowerMapping, generators.modelOutput));

        if (cake.getSlices() > 0) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseThreeTieredCakeBlock.BITES);
            dispatch.select(0, upperVariant);
            dispatch.select(5, lowerVariant);

            for (int i = 1; i < 5; i++) {
                TextureMapping sliceMapping = new TextureMapping()
                        .put(TextureSlot.BOTTOM, bottom)
                        .put(TextureSlot.TOP, top)
                        .put(TOP_SIDE, topSide)
                        .put(TOP_INNER, topInner);

                if (i < 3) {
                    addCross(sliceMapping, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, TOP_INNER, TextureSlot.CROSS)
                                .create(cake, sliceMapping, generators.modelOutput)));
            }

            for (int i = 1; i < 11; i++) {
                TextureMapping sliceMapping = new TextureMapping()
                        .put(TextureSlot.BOTTOM, bottom)
                        .put(LOWER_TOP, lowerTop)
                        .put(LOWER_SIDE, lowerSide);

                if (i < 5) {
                    sliceMapping.put(MIDDLE_TOP, middleTop)
                            .put(MIDDLE_SIDE, middleSide)
                            .put(MIDDLE_INNER, middleInner);
                }
                else if (i > 5) {
                    sliceMapping.put(LOWER_INNER, lowerInner);
                }

                dispatch.select(i + 5, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), TextureSlot.BOTTOM, TextureSlot.TOP, TOP_SIDE, TOP_INNER, TextureSlot.CROSS)
                                .create(cake, sliceMapping, generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            PropertyDispatch.C1<DoubleBlockHalf> dispatch = PropertyDispatch.property(BaseThreeTieredCakeBlock.HALF);
            dispatch.select(UPPER, upperVariant);
            dispatch.select(LOWER, lowerVariant);
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
    }

    private static void addCross(TextureMapping mapping, @Nullable ResourceLocation texture) {
        if (texture != null) {
            mapping.put(TextureSlot.CROSS, texture);
        }
    }

    private void createFromVanillaCake(BlockModelGenerators generators, BaseCakeBlock cake, int bites, ResourceLocation parentModel) {
        if (cake.getSlices() > 0) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseCakeBlock.BITES);

            dispatch.select(0, Variant.variant().with(VariantProperties.MODEL, parentModel));

            for (int i = 1; i < bites; i++) {
                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL, parentModel.withSuffix("_slice" + i)));
            }

            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL, parentModel)));
        }
    }

    private void createFromVanillaCandleCake(PropertyDispatch.C1<Boolean> dispatch, ResourceLocation candleType, String modId, String name, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        dispatch.select(isLit, Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(modId, "block/" + candleType.getPath() + "candle" + name + litName)));
    }

    private void createFromVanillaThreeTieredCandleCake(PropertyDispatch.C2<Boolean, DoubleBlockHalf> dispatch, ResourceLocation candleType, DoubleBlockHalf half, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        dispatch.select(isLit, half, Variant.variant().with(VariantProperties.MODEL, loc("block/" + candleType.getPath() + "candle_three_tiered_cake" + half + litName)));
    }
}
