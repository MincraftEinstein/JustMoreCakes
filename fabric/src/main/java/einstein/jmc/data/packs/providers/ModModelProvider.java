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
import einstein.jmc.util.CakeVariant;
import einstein.jmc.util.CakeModel;
import einstein.jmc.util.CakeVariantType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static einstein.jmc.JustMoreCakes.loc;
import static einstein.jmc.JustMoreCakes.mcLoc;
import static einstein.jmc.util.CakeVariant.SUPPORTED_CANDLES;
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
        generators.generateFlatItem(ModItems.WHISK.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        generators.generateFlatItem(ModBlocks.POISON_CAKE_VARIANT.getItem().get(), Items.CAKE, ModelTemplates.FLAT_ITEM);
        generators.generateFlatItem(ModBlocks.TNT_CAKE_VARIANT.getItem().get(), Items.CAKE, ModelTemplates.FLAT_ITEM);

        generators.output.accept(loc("item/encasing_ice"), new DelegatedModel(mcBlockLoc("ice")));
        generators.output.accept(loc("item/cake_oven"), new DelegatedModel(blockLoc("cake_oven")));
        generators.output.accept(loc("item/cake_stand"), new DelegatedModel(blockLoc("cake_stand")));
        generators.output.accept(loc("item/ceramic_bowl"), new DelegatedModel(blockLoc("ceramic_bowl")));

        CakeVariant.VARIANT_BY_CAKE.forEach((cake, variant) -> {
            if (variant.hasItem() && !variant.hasCustomItemModel()) {
                generators.generateFlatItem(cake.get().asItem(), ModelTemplates.FLAT_ITEM);
            }
        });
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generators) {
        generators.createTrivialBlock(ModBlocks.ENCASING_ICE.get(), new TextureMapping(), new ModelTemplate(Optional.of(mcBlockLoc("ice")), Optional.empty()));

        CakeVariant.VARIANT_BY_CAKE.forEach((cake, variant) -> {
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariantType variantType = variant.getType();
            CakeModel cakeModel = variant.getCakeModel();
            CakeModel candleCakeModel = variant.getCandleCakeModel();
            String cakeName = variant.getCakeName();
            String texturePrefix = variant.getFamily() != null ? variant.getFamily().getBaseCakeName() : variant.getCakeName();

            if (cakeModel != CakeModel.CUSTOM) {
                if (cakeModel == CakeModel.DEFAULT) {
                    switch (variantType) {
                        case BASE -> createCake(generators, cakeBlock, texturePrefix, null);
                        case TWO_TIERED -> createTwoTieredCake(generators, cakeBlock, texturePrefix, null);
                        case THREE_TIERED -> createThreeTieredCake(generators, cakeBlock, texturePrefix, null);
                    }
                }
                else if (cakeModel == CakeModel.FROM_VANILLA) {
                    switch (variantType) {
                        case BASE -> createFromVanillaCake(generators, cakeBlock, 7, mcBlockLoc("cake"));
                        case TWO_TIERED ->
                                createFromVanillaCake(generators, cakeBlock, 10, blockLoc("two_tiered_cake"));
                        case THREE_TIERED -> {
                            List<Pair<DoubleBlockHalf, Integer>> halves = List.of(Pair.of(UPPER, 5), Pair.of(LOWER, 11));

                            for (Pair<DoubleBlockHalf, Integer> halfPair : halves) {
                                DoubleBlockHalf half = halfPair.getFirst();
                                Variant propertyVariant = Variant.variant().with(VariantProperties.MODEL, loc("three_tiered_cake_" + half));

                                if (cakeBlock.hasBites()) {
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
                    switch (variantType) {
                        case BASE -> createCake(generators, cakeBlock, texturePrefix, crossModel.crossTexture());
                        case TWO_TIERED -> createTwoTieredCake(generators, cakeBlock, texturePrefix, crossModel.crossTexture());
                        case THREE_TIERED -> createThreeTieredCake(generators, cakeBlock, texturePrefix, crossModel.crossTexture());
                    }
                }
            }

            if (candleCakeModel != CakeModel.CUSTOM && variant.allowsCandles()) {
                variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    ResourceLocation candleType = SUPPORTED_CANDLES.get(candle);
                    BaseCandleCakeBlock candleCakeBlock = candleCake.get();
                    PropertyDispatch.C1<Boolean> dispatch = PropertyDispatch.property(BaseCandleCakeBlock.LIT);

                    if (candleCakeModel == CakeModel.FROM_VANILLA) {
                        switch (variantType) {
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
                        switch (variantType) {
                            case BASE -> {
                                addVariant(generators, dispatch, candleCakeBlock, candle, texturePrefix, true);
                                addVariant(generators, dispatch, candleCakeBlock, candle, texturePrefix, false);
                            }
                            case TWO_TIERED -> {
                                addTwoTieredVariant(generators, dispatch, candleCakeBlock, candle, texturePrefix, true);
                                addTwoTieredVariant(generators, dispatch, candleCakeBlock, candle, texturePrefix, false);
                            }
                            case THREE_TIERED -> {
                                PropertyDispatch.C2<Boolean, DoubleBlockHalf> threeTieredDispatch = PropertyDispatch.properties(BaseThreeTieredCandleCakeBlock.LIT, BaseThreeTieredCandleCakeBlock.HALF);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, texturePrefix, true, LOWER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, texturePrefix, false, LOWER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, texturePrefix, true, UPPER);
                                addThreeTieredVariant(generators, threeTieredDispatch, candleCakeBlock, candle, texturePrefix, false, UPPER);
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

    private void addVariant(BlockModelGenerators generators, PropertyDispatch.C1<Boolean> dispatch, Block candleCake, Block candle, String texturePrefix, boolean lit) {
        String litName = lit ? "_lit" : "";
        ResourceLocation side = blockLoc(texturePrefix + "_side");

        TextureSet set = new TextureSet()
                .add(TextureSlot.CANDLE, getBlockTexture(candle, litName))
                .add(TextureSlot.BOTTOM, blockLoc(texturePrefix + "_bottom"))
                .add(TextureSlot.TOP, blockLoc(texturePrefix + "_top"))
                .add(TextureSlot.SIDE, side)
                .add(TextureSlot.PARTICLE, side);

        dispatch.select(lit, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(mcBlockLoc("template_cake_with_candle")), Optional.of(litName), set.getSlots())
                        .create(candleCake, set.getMapping(),
                                generators.modelOutput)
        ));
    }

    private void addTwoTieredVariant(BlockModelGenerators generators, PropertyDispatch.C1<Boolean> dispatch, Block candleCake, Block candle, String texturePrefix, boolean lit) {
        String litName = lit ? "_lit" : "";

        TextureSet set = new TextureSet()
                .add(TextureSlot.CANDLE, getBlockTexture(candle, litName))
                .add(TextureSlot.BOTTOM, blockLoc(texturePrefix + "_bottom"))
                .add(TextureSlot.TOP, blockLoc(texturePrefix + "_medium_top"))
                .add(TOP_SIDE, blockLoc(texturePrefix + "_medium_side"))
                .add(LOWER_TOP, blockLoc(texturePrefix + "_top_covered"))
                .add(LOWER_SIDE, blockLoc(texturePrefix + "_side"));

        dispatch.select(lit, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(blockLoc("template_two_tiered_candle_cake")), Optional.of(litName), set.getSlots())
                        .create(candleCake, set.getMapping(), generators.modelOutput)
        ));
    }

    private void addThreeTieredVariant(BlockModelGenerators generators, PropertyDispatch.C2<Boolean, DoubleBlockHalf> dispatch, Block candleCake, Block candle, String texturePrefix, boolean lit, DoubleBlockHalf half) {
        String litName = lit ? "_lit" : "";
        String halfName = "_" + half;

        TextureSet set = new TextureSet()
                .add(TextureSlot.BOTTOM, blockLoc(texturePrefix + "_bottom"))
                .add(TextureSlot.CANDLE, getBlockTexture(candle, litName));

        if (half == LOWER) {
            set.add(MIDDLE_SIDE, blockLoc(texturePrefix + "_medium_side"))
                    .add(MIDDLE_TOP, blockLoc(texturePrefix + "_medium_top_covered"))
                    .add(LOWER_SIDE, blockLoc(texturePrefix + "_side"))
                    .add(LOWER_TOP, blockLoc(texturePrefix + "_top_covered"));
        }
        else {
            set.add(TOP_SIDE, blockLoc(texturePrefix + "_small_side"))
                    .add(TextureSlot.TOP, blockLoc(texturePrefix + "_small_top"));
        }

        dispatch.select(lit, half, Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(blockLoc("template_three_tiered_candle_cake" + halfName)), Optional.of(halfName + litName), set.getSlots())
                        .create(candleCake, set.getMapping(), generators.modelOutput))
        );
    }

    private void createCake(BlockModelGenerators generators, BaseCakeBlock cake, String texturePrefix, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_cake" : "block/template_cake";
        ResourceLocation bottom = blockLoc(texturePrefix + "_bottom");
        ResourceLocation top = blockLoc(texturePrefix + "_top");
        ResourceLocation side = blockLoc(texturePrefix + "_side");
        ResourceLocation inside = blockLoc(texturePrefix + "_inner");

        if (cake.hasBites()) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseCakeBlock.BITES);

            TextureSet set = new TextureSet()
                    .add(TextureSlot.BOTTOM, bottom)
                    .add(TextureSlot.TOP, top)
                    .add(TextureSlot.SIDE, side);

            addCross(set, crossTexture);
            dispatch.select(0, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), set.getSlots())
                            .create(cake, set.getMapping(), generators.modelOutput)));

            for (int i = 1; i < 7; i++) {
                TextureSet sliceSet = new TextureSet()
                        .add(TextureSlot.BOTTOM, bottom)
                        .add(TextureSlot.TOP, top)
                        .add(TextureSlot.INSIDE, inside)
                        .add(TextureSlot.SIDE, side);

                if (i < 4) {
                    addCross(sliceSet, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), sliceSet.getSlots())
                                .create(cake, sliceSet.getMapping(), generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            TextureSet set = new TextureSet()
                    .add(TextureSlot.BOTTOM, bottom)
                    .add(TextureSlot.TOP, top)
                    .add(TextureSlot.SIDE, side);

            addCross(set, crossTexture);
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), set.getSlots())
                            .create(cake, set.getMapping(), generators.modelOutput))));
        }
    }

    private void createTwoTieredCake(BlockModelGenerators generators, BaseCakeBlock cake, String texturePrefix, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_two_tiered_cake" : "block/template_two_tiered_cake";
        ResourceLocation bottom = blockLoc(texturePrefix + "_bottom");
        ResourceLocation mediumTop = blockLoc(texturePrefix + "_medium_top");
        ResourceLocation mediumSide = blockLoc(texturePrefix + "_medium_side");
        ResourceLocation mediumInner = blockLoc(texturePrefix + "_medium_inner");
        ResourceLocation top = blockLoc(texturePrefix + "_top_covered");
        ResourceLocation side = blockLoc(texturePrefix + "_side");
        ResourceLocation inner = blockLoc(texturePrefix + "_inner_covered");

        if (cake.hasBites()) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseTwoTieredCakeBlock.BITES);
            TextureSet set = new TextureSet()
                    .add(TextureSlot.BOTTOM, bottom)
                    .add(TextureSlot.TOP, mediumTop)
                    .add(TOP_SIDE, mediumSide)
                    .add(LOWER_SIDE, side)
                    .add(LOWER_TOP, top);

            addCross(set, crossTexture);
            dispatch.select(0, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), set.getSlots())
                            .create(cake, set.getMapping(), generators.modelOutput)));

            for (int i = 1; i < 11; i++) {
                TextureSet sliceSet = new TextureSet()
                        .add(TextureSlot.BOTTOM, bottom)
                        .add(LOWER_SIDE, side)
                        .add(LOWER_TOP, top);

                if (i < 5) {
                    sliceSet.add(TextureSlot.TOP, mediumTop)
                            .add(TOP_SIDE, mediumSide)
                            .add(TOP_INNER, mediumInner);
                }

                if (i > 5) {
                    sliceSet.add(LOWER_INNER, inner);
                }

                if (i < 3) {
                    addCross(sliceSet, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_slice" + i)), Optional.of("_slice" + i), sliceSet.getSlots())
                                .create(cake, sliceSet.getMapping(), generators.modelOutput)));
            }
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake).with(dispatch));
        }
        else {
            TextureSet set = new TextureSet()
                    .add(TextureSlot.BOTTOM, bottom)
                    .add(TextureSlot.TOP, mediumTop)
                    .add(TOP_SIDE, mediumSide)
                    .add(LOWER_TOP, top)
                    .add(LOWER_SIDE, side);

            addCross(set, crossTexture);
            generators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(cake, Variant.variant().with(VariantProperties.MODEL,
                    new ModelTemplate(Optional.of(loc(parentModel)), Optional.empty(), set.getSlots())
                            .create(cake, set.getMapping(), generators.modelOutput))));
        }
    }

    private void createThreeTieredCake(BlockModelGenerators generators, BaseCakeBlock cake, String texturePrefix, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "block/template_cross_three_tiered_cake" : "block/template_three_tiered_cake";
        ResourceLocation bottom = blockLoc(texturePrefix + "_bottom");
        ResourceLocation smallTop = blockLoc(texturePrefix + "_small_top");
        ResourceLocation smallSide = blockLoc(texturePrefix + "_small_side");
        ResourceLocation smallInner = blockLoc(texturePrefix + "_small_inner");
        ResourceLocation mediumTop = blockLoc(texturePrefix + "_medium_top_covered");
        ResourceLocation mediumSide = blockLoc(texturePrefix + "_medium_side");
        ResourceLocation mediumInner = blockLoc(texturePrefix + "_medium_inner_covered");
        ResourceLocation top = blockLoc(texturePrefix + "_top_covered");
        ResourceLocation side = blockLoc(texturePrefix + "_side");
        ResourceLocation inner = blockLoc(texturePrefix + "_inner_covered");

        TextureSet upperSet = new TextureSet()
                .add(TextureSlot.BOTTOM, bottom)
                .add(TextureSlot.TOP, smallTop)
                .add(TOP_SIDE, smallSide);
        addCross(upperSet, crossTexture);
        Variant upperVariant = Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc(parentModel + "_upper")), Optional.of("_upper"), upperSet.getSlots())
                        .create(cake, upperSet.getMapping(), generators.modelOutput));

        TextureSet lowerSet = new TextureSet()
                .add(TextureSlot.BOTTOM, bottom)
                .add(MIDDLE_TOP, mediumTop)
                .add(MIDDLE_SIDE, mediumSide)
                .add(LOWER_TOP, top)
                .add(LOWER_SIDE, side);
        addCross(lowerSet, crossTexture);
        Variant lowerVariant = Variant.variant().with(VariantProperties.MODEL,
                new ModelTemplate(Optional.of(loc(parentModel + "_lower")), Optional.of("_lower"), lowerSet.getSlots())
                        .create(cake, lowerSet.getMapping(), generators.modelOutput));

        if (cake.hasBites()) {
            PropertyDispatch.C1<Integer> dispatch = PropertyDispatch.property(BaseThreeTieredCakeBlock.BITES);
            dispatch.select(0, upperVariant);
            dispatch.select(5, lowerVariant);

            for (int i = 1; i < 5; i++) {
                TextureSet set = new TextureSet()
                        .add(TextureSlot.BOTTOM, bottom)
                        .add(TextureSlot.TOP, smallTop)
                        .add(TOP_SIDE, smallSide)
                        .add(TOP_INNER, smallInner);

                if (i < 3) {
                    addCross(set, crossTexture);
                }

                dispatch.select(i, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_upper_slice" + i)), Optional.of("_upper_slice" + i), set.getSlots())
                                .create(cake, set.getMapping(), generators.modelOutput)));
            }

            for (int i = 1; i < 11; i++) {
                TextureSet set = new TextureSet()
                        .add(TextureSlot.BOTTOM, bottom)
                        .add(LOWER_TOP, top)
                        .add(LOWER_SIDE, side);

                if (i < 5) {
                    set.add(MIDDLE_TOP, mediumTop)
                            .add(MIDDLE_SIDE, mediumSide)
                            .add(MIDDLE_INNER, mediumInner);
                }
                else if (i > 5) {
                    set.add(LOWER_INNER, inner);
                }

                dispatch.select(i + 5, Variant.variant().with(VariantProperties.MODEL,
                        new ModelTemplate(Optional.of(loc(parentModel + "_lower_slice" + i)), Optional.of("_lower_slice" + i), set.getSlots())
                                .create(cake, set.getMapping(), generators.modelOutput)));
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

    private static void addCross(TextureSet set, @Nullable ResourceLocation texture) {
        if (texture != null) {
            set.add(TextureSlot.CROSS, texture);
        }
    }

    private void createFromVanillaCake(BlockModelGenerators generators, BaseCakeBlock cake, int bites, ResourceLocation parentModel) {
        if (cake.hasBites()) {
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
        dispatch.select(isLit, Variant.variant().with(VariantProperties.MODEL, new ResourceLocation(modId, "block/" + candleType.getPath() + "candle_" + name + litName)));
    }

    private void createFromVanillaThreeTieredCandleCake(PropertyDispatch.C2<Boolean, DoubleBlockHalf> dispatch, ResourceLocation candleType, DoubleBlockHalf half, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        dispatch.select(isLit, half, Variant.variant().with(VariantProperties.MODEL, blockLoc(candleType.getPath() + "candle_three_tiered_cake" + half + litName)));
    }

    private static ResourceLocation blockLoc(String string) {
        return loc("block/" + string);
    }

    private static ResourceLocation mcBlockLoc(String string) {
        return mcLoc("block/" + string);
    }

    private static class TextureSet {

        private final List<TextureSlot> slots = new ArrayList<>();
        private final TextureMapping mapping = new TextureMapping();

        public TextureSet add(TextureSlot slot, ResourceLocation texture) {
            slots.add(slot);
            mapping.put(slot, texture);
            return this;
        }

        public TextureSlot[] getSlots() {
            return slots.toArray(TextureSlot[]::new);
        }

        public TextureMapping getMapping() {
            return mapping;
        }
    }
}
