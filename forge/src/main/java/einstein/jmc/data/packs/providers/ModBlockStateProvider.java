package einstein.jmc.data.packs.providers;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseTwoTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.CakeModel;
import einstein.jmc.util.CakeVariant;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static einstein.jmc.util.CakeBuilder.SUPPORTED_CANDLES;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.LOWER;
import static net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, JustMoreCakes.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ENCASING_ICE.get(), models().withExistingParent("encasing_ice", mcLoc("ice")).renderType("translucent"));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariant variant = builder.getVariant();
            CakeModel cakeModel = builder.getCakeModel();
            CakeModel candleCakeModel = builder.getCandleCakeModel();
            String cakeName = builder.getCakeName();
            String texturePrefix = builder.getFamily() != null ? builder.getFamily().getBaseCakeName() : builder.getCakeName();

            if (cakeModel != CakeModel.CUSTOM) {
                if (cakeModel == CakeModel.DEFAULT) {
                    switch (variant) {
                        case BASE -> createCake(cakeBlock, texturePrefix, cakeName, null);
                        case TWO_TIERED -> createTwoTieredCake(cakeBlock, texturePrefix, cakeName, null);
                        case THREE_TIERED -> createThreeTieredCake(cakeBlock, texturePrefix, cakeName, null);
                    }
                }
                else if (cakeModel == CakeModel.FROM_VANILLA) {
                    switch (variant) {
                        case BASE -> createFromVanillaCake(cakeBlock, 7, mcLoc("cake"));
                        case TWO_TIERED -> createFromVanillaCake(cakeBlock, 10, modLoc("two_tiered_cake"));
                        case THREE_TIERED -> {
                            VariantBlockStateBuilder variantBuilder = getVariantBuilder(cakeBlock);
                            List<Pair<DoubleBlockHalf, Integer>> halves = List.of(Pair.of(UPPER, 5), Pair.of(LOWER, 11));

                            for (Pair<DoubleBlockHalf, Integer> halfPair : halves) {
                                DoubleBlockHalf half = halfPair.getFirst();

                                VariantBlockStateBuilder.PartialBlockstate halfBuilder = variantBuilder.partialState()
                                        .addModels(new ConfiguredModel(models().getExistingFile(modLoc("three_tiered_cake_" + half))));

                                if (cakeBlock.hasBites()) {
                                    halfBuilder.with(BaseThreeTieredCakeBlock.BITES, 0);

                                    for (int i = 1; i < halfPair.getSecond(); i++) {
                                        variantBuilder.partialState().with(BaseThreeTieredCakeBlock.BITES, i)
                                                .addModels(new ConfiguredModel(models()
                                                        .getExistingFile(modLoc("three_tiered_cake_" + half + "_slice" + (half == LOWER ? i - 5 : i)))));
                                    }
                                }
                                else {
                                    halfBuilder.with(BaseThreeTieredCakeBlock.HALF, half);
                                }
                            }
                        }
                    }
                }
                else if (cakeModel instanceof CakeModel.CrossCakeModel crossModel) {
                    switch (variant) {
                        case BASE -> createCake(cakeBlock, texturePrefix, cakeName, crossModel.crossTexture());
                        case TWO_TIERED -> createTwoTieredCake(cakeBlock, texturePrefix, cakeName, crossModel.crossTexture());
                        case THREE_TIERED -> createThreeTieredCake(cakeBlock, texturePrefix, cakeName, crossModel.crossTexture());
                    }
                }
            }

            if (candleCakeModel != CakeModel.CUSTOM && builder.allowsCandles()) {
                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    ResourceLocation candleType = SUPPORTED_CANDLES.get(candle);
                    BaseCandleCakeBlock candleCakeBlock = candleCake.get();
                    VariantBlockStateBuilder variantBuilder = getVariantBuilder(candleCakeBlock);

                    if (candleCakeModel == CakeModel.FROM_VANILLA) {
                        switch (variant) {
                            case BASE -> {
                                createFromVanillaCandleCake(variantBuilder.partialState(), candleType, candleType.getNamespace(), "cake", true);
                                createFromVanillaCandleCake(variantBuilder.partialState(), candleType, candleType.getNamespace(), "cake", false);
                            }
                            case TWO_TIERED -> {
                                createFromVanillaCandleCake(variantBuilder.partialState(), candleType, JustMoreCakes.MOD_ID, "two_tiered_cake", true);
                                createFromVanillaCandleCake(variantBuilder.partialState(), candleType, JustMoreCakes.MOD_ID, "two_tiered_cake", false);
                            }
                            case THREE_TIERED -> {
                                createFromVanillaThreeTieredCandleCake(variantBuilder.partialState(), candleType, UPPER, true);
                                createFromVanillaThreeTieredCandleCake(variantBuilder.partialState(), candleType, UPPER, false);
                                createFromVanillaThreeTieredCandleCake(variantBuilder.partialState(), candleType, LOWER, true);
                                createFromVanillaThreeTieredCandleCake(variantBuilder.partialState(), candleType, LOWER, false);
                            }
                        }
                    }
                    else if (candleCakeModel instanceof CakeModel.CrossCakeModel) {
                        throw new IllegalArgumentException("Cannot have a cross model candle cake");
                    }
                    else {
                        switch (variant) {
                            case BASE -> {
                                addVariant(variantBuilder, candleType, texturePrefix, cakeName, true);
                                addVariant(variantBuilder, candleType, texturePrefix, cakeName, false);
                            }
                            case TWO_TIERED -> {
                                addTwoTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, true);
                                addTwoTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, false);
                            }
                            case THREE_TIERED -> {
                                addThreeTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, true, LOWER);
                                addThreeTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, false, LOWER);
                                addThreeTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, true, UPPER);
                                addThreeTieredVariant(variantBuilder, candleType, texturePrefix, cakeName, false, UPPER);
                            }
                        }
                    }
                });
            }
        });
    }

    private void addVariant(VariantBlockStateBuilder builder, ResourceLocation candleType, String texturePrefix, String name, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        builder.partialState()
                .with(AbstractCandleBlock.LIT, isLit)
                .addModels(new ConfiguredModel(models()
                        .withExistingParent(candleType.getPath() + "candle_" + name + litName, mcLoc("template_cake_with_candle"))
                        .texture("candle", getCandleTexture(candleType, litName))
                        .texture("side", blockLoc(texturePrefix + "_side"))
                        .texture("top", blockLoc(texturePrefix + "_top"))
                        .texture("bottom", blockLoc(texturePrefix + "_bottom"))
                        .texture("particle", "#side")
                ));
    }

    private void addTwoTieredVariant(VariantBlockStateBuilder builder, ResourceLocation candleType, String texturePrefix, String name, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        builder.partialState()
                .with(BaseTwoTieredCandleCakeBlock.LIT, isLit)
                .addModels(new ConfiguredModel(models()
                        .withExistingParent(candleType.getPath() + "candle_" + name + litName, modLoc("template_two_tiered_candle_cake"))
                        .texture("candle", getCandleTexture(candleType, litName))
                        .texture("bottom", blockLoc(texturePrefix + "_bottom"))
                        .texture("top_side", blockLoc(texturePrefix + "_medium_side"))
                        .texture("top", blockLoc(texturePrefix + "_medium_top"))
                        .texture("lower_side", blockLoc(texturePrefix + "_side"))
                        .texture("lower_top", blockLoc(texturePrefix + "_top_covered"))
                ));
    }

    private void addThreeTieredVariant(VariantBlockStateBuilder builder, ResourceLocation candleType, String texturePrefix, String name, boolean isLit, DoubleBlockHalf half) {
        String litName = isLit ? "_lit" : "";
        String halfName = "_" + half;

        BlockModelBuilder modelBuilder = models()
                .withExistingParent(candleType.getPath() + "candle_" + name + halfName + litName, modLoc("template_three_tiered_candle_cake" + halfName))
                .texture("candle", getCandleTexture(candleType, litName))
                .texture("bottom", blockLoc(texturePrefix + "_bottom"));

        if (half == LOWER) {
            modelBuilder.texture("middle_side", blockLoc(texturePrefix + "_medium_side"))
                    .texture("middle_top", blockLoc(texturePrefix + "_medium_top_covered"))
                    .texture("lower_side", blockLoc(texturePrefix + "_side"))
                    .texture("lower_top", blockLoc(texturePrefix + "_top_covered"));
        }
        else {
            modelBuilder.texture("top_side", blockLoc(texturePrefix + "_small_side"))
                    .texture("top", blockLoc(texturePrefix + "_small_top"));
        }

        builder.partialState()
                .with(BaseThreeTieredCandleCakeBlock.LIT, isLit)
                .with(BaseThreeTieredCandleCakeBlock.HALF, half)
                .addModels(new ConfiguredModel(modelBuilder));
    }

    private static ResourceLocation getCandleTexture(ResourceLocation candleType, String litName) {
        return new ResourceLocation(candleType.getNamespace(), blockLoc(candleType.getPath() + "candle" + litName));
    }

    private void createCake(BaseCakeBlock cake, String texturePrefix, String name, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "template_cross_cake" : "template_cake";
        String side = blockLoc(texturePrefix + "_side");
        String top = blockLoc(texturePrefix + "_top");
        String bottom = blockLoc(texturePrefix + "_bottom");
        String inside = blockLoc(texturePrefix + "_inner");
        BlockModelBuilder modelBuilder;

        if (cake.hasBites()) {
            VariantBlockStateBuilder builder = getVariantBuilder(cake);
            modelBuilder = models().withExistingParent(name, modLoc(parentModel))
                    .texture("side", side)
                    .texture("top", top)
                    .texture("bottom", bottom);

            addCrossTexture(modelBuilder, crossTexture);
            builder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(modelBuilder));

            for (int i = 1; i < 7; i++) {
                modelBuilder = models().withExistingParent(name + "_slice" + i, modLoc(parentModel + "_slice" + i))
                        .texture("side", side)
                        .texture("top", top)
                        .texture("bottom", bottom)
                        .texture("inside", inside);

                if (i < 4) {
                    addCrossTexture(modelBuilder, crossTexture);
                }

                builder.partialState().with(BaseCakeBlock.BITES, i).addModels(new ConfiguredModel(modelBuilder));
            }
        }
        else {
            modelBuilder = models().withExistingParent(name, modLoc(parentModel))
                    .texture("side", side)
                    .texture("top", top)
                    .texture("bottom", bottom);

            addCrossTexture(modelBuilder, crossTexture);
            simpleBlock(cake, modelBuilder);
        }
    }

    private void createTwoTieredCake(BaseCakeBlock cake, String texturePrefix, String name, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "template_cross_two_tiered_cake" : "template_two_tiered_cake";
        String mediumTop = blockLoc(texturePrefix + "_medium_top");
        String mediumSide = blockLoc(texturePrefix + "_medium_side");
        String mediumInner = blockLoc(texturePrefix + "_medium_inner");
        String top = blockLoc(texturePrefix + "_top_covered");
        String side = blockLoc(texturePrefix + "_side");
        String inner = blockLoc(texturePrefix + "_inner_covered");
        String bottom = blockLoc(texturePrefix + "_bottom");
        BlockModelBuilder modelBuilder;

        if (cake.hasBites()) {
            VariantBlockStateBuilder builder = getVariantBuilder(cake);
            modelBuilder = models().withExistingParent(name, modLoc(parentModel))
                    .texture("top", mediumTop)
                    .texture("top_side", mediumSide)
                    .texture("lower_side", side)
                    .texture("lower_top", top)
                    .texture("bottom", bottom);

            addCrossTexture(modelBuilder, crossTexture);
            builder.partialState().with(BaseTwoTieredCakeBlock.BITES, 0).addModels(new ConfiguredModel(modelBuilder));

            for (int i = 1; i < 11; i++) {
                modelBuilder = models().withExistingParent(name + "_slice" + i, modLoc(parentModel + "_slice" + i))
                        .texture("lower_side", side)
                        .texture("lower_top", top)
                        .texture("bottom", bottom);

                if (i < 5) {
                    modelBuilder.texture("top", mediumTop)
                            .texture("top_side", mediumSide)
                            .texture("top_inner", mediumInner);
                }

                if (i > 5) {
                    modelBuilder.texture("lower_inner", inner);
                }

                if (i < 3) {
                    addCrossTexture(modelBuilder, crossTexture);
                }

                builder.partialState().with(BaseTwoTieredCakeBlock.BITES, i).addModels(new ConfiguredModel(modelBuilder));
            }
        }
        else {
            modelBuilder = models().withExistingParent(name, modLoc(parentModel))
                    .texture("top", mediumTop)
                    .texture("top_side", mediumSide)
                    .texture("lower_side", side)
                    .texture("lower_top", top)
                    .texture("bottom", bottom);

            addCrossTexture(modelBuilder, crossTexture);
            simpleBlock(cake, modelBuilder);
        }
    }

    private void createThreeTieredCake(BaseCakeBlock cake, String texturePrefix, String name, @Nullable ResourceLocation crossTexture) {
        boolean addCross = crossTexture != null;
        String parentModel = addCross ? "template_cross_three_tiered_cake" : "template_three_tiered_cake";
        String smallTop = blockLoc(texturePrefix + "_small_top");
        String smallSide = blockLoc(texturePrefix + "_small_side");
        String smallInner = blockLoc(texturePrefix + "_small_inner");
        String mediumTop = blockLoc(texturePrefix + "_medium_top_covered");
        String mediumSide = blockLoc(texturePrefix + "_medium_side");
        String mediumInner = blockLoc(texturePrefix + "_medium_inner_covered");
        String top = blockLoc(texturePrefix + "_top_covered");
        String side = blockLoc(texturePrefix + "_side");
        String inner = blockLoc(texturePrefix + "_inner_covered");
        String bottom = blockLoc(texturePrefix + "_bottom");
        VariantBlockStateBuilder builder = getVariantBuilder(cake);

        VariantBlockStateBuilder.PartialBlockstate upperBuilder = builder.partialState();
        VariantBlockStateBuilder.PartialBlockstate lowerBuilder = builder.partialState();

        if (cake.hasBites()) {
            upperBuilder = upperBuilder.with(BaseThreeTieredCakeBlock.BITES, 0);
            lowerBuilder = lowerBuilder.with(BaseThreeTieredCakeBlock.BITES, 5);
            BlockModelBuilder modelBuilder;

            for (int i = 1; i < 5; i++) {
                modelBuilder = models().withExistingParent(name + "_upper_slice" + i, modLoc(parentModel + "_upper_slice" + i))
                        .texture("top", smallTop)
                        .texture("top_side", smallSide)
                        .texture("top_inner", smallInner)
                        .texture("bottom", bottom);

                if (i < 3) {
                    addCrossTexture(modelBuilder, crossTexture);
                }

                builder.partialState().with(BaseThreeTieredCakeBlock.BITES, i)
                        .addModels(new ConfiguredModel(modelBuilder));
            }

            for (int i = 1; i < 11; i++) {
                modelBuilder = models().withExistingParent(name + "_lower_slice" + i, modLoc(parentModel + "_lower_slice" + i))
                        .texture("lower_top", top)
                        .texture("lower_side", side)
                        .texture("bottom", bottom);

                if (i < 5) {
                    modelBuilder.texture("middle_top", mediumTop)
                            .texture("middle_side", mediumSide)
                            .texture("middle_inner", mediumInner);
                }
                else if (i > 5) {
                    modelBuilder.texture("lower_inner", inner);
                }

                builder.partialState().with(BaseThreeTieredCakeBlock.BITES, i + 5)
                        .addModels(new ConfiguredModel(modelBuilder));
            }
        }
        else {
            upperBuilder = upperBuilder.with(BaseThreeTieredCakeBlock.HALF, UPPER);
            lowerBuilder = lowerBuilder.with(BaseThreeTieredCakeBlock.HALF, LOWER);
        }

        BlockModelBuilder upperModelBuilder = models().withExistingParent(name + "_upper", modLoc(parentModel + "_upper"))
                .texture("top", smallTop)
                .texture("top_side", smallSide)
                .texture("bottom", bottom);

        addCrossTexture(upperModelBuilder, crossTexture);
        upperBuilder.addModels(new ConfiguredModel(upperModelBuilder));

        BlockModelBuilder lowerModelBuilder = models().withExistingParent(name + "_lower", modLoc(parentModel + "_lower"))
                .texture("middle_top", mediumTop)
                .texture("middle_side", mediumSide)
                .texture("lower_top", top)
                .texture("lower_side", side)
                .texture("bottom", bottom);

        lowerBuilder.addModels(new ConfiguredModel(lowerModelBuilder));
    }

    private void addCrossTexture(BlockModelBuilder modelBuilder, @Nullable ResourceLocation texture) {
        if (texture != null) {
            modelBuilder.texture("cross", texture);
        }
    }

    private void createFromVanillaCake(BaseCakeBlock cake, int bites, ResourceLocation parentModel) {
        if (cake.hasBites()) {
            VariantBlockStateBuilder variantBuilder = getVariantBuilder(cake);
            variantBuilder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(models().getExistingFile(parentModel)));

            for (int i = 1; i < bites; i++) {
                variantBuilder.partialState().with(BaseCakeBlock.BITES, i).addModels(new ConfiguredModel(models().getExistingFile(parentModel.withSuffix("_slice" + i))));
            }
        }
        else {
            simpleBlock(cake, models().getExistingFile(parentModel));
        }
    }

    private void createFromVanillaCandleCake(VariantBlockStateBuilder.PartialBlockstate partialState, ResourceLocation candleType, String modId, String name, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        partialState.with(AbstractCandleBlock.LIT, isLit)
                .addModels(new ConfiguredModel(models().getExistingFile(new ResourceLocation(modId, candleType.getPath() + "candle_" + name + litName))));
    }

    private void createFromVanillaThreeTieredCandleCake(VariantBlockStateBuilder.PartialBlockstate partialState, ResourceLocation candleType, DoubleBlockHalf half, boolean isLit) {
        String litName = isLit ? "_lit" : "";
        partialState.with(AbstractCandleBlock.LIT, isLit).with(BaseThreeTieredCandleCakeBlock.HALF, half)
                .addModels(new ConfiguredModel(models().getExistingFile(modLoc(candleType.getPath() + "candle_three_tiered_cake" + half + litName))));
    }

    private static String blockLoc(String string) {
        return "block/" + string;
    }
}
