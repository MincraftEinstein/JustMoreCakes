package einstein.jmc.data.generators;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.blocks.ThreeTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class BlockAssetsGenerator extends BlockStateProvider {

    public BlockAssetsGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, JustMoreCakes.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ENCASING_ICE.get(), models().withExistingParent("encasing_ice", mcLoc("ice")).renderType("translucent"));

        defaultCake(ModBlocks.TNT_CAKE.get());
        defaultCake(ModBlocks.POISON_CAKE.get());
        crossCakeBlock(ModBlocks.BROWN_MUSHROOM_CAKE.get(), mcLoc("block/brown_mushroom"));
        crossCakeBlock(ModBlocks.RED_MUSHROOM_CAKE.get(), mcLoc("block/red_mushroom"));
        crossCakeBlock(ModBlocks.CHORUS_CAKE.get(), modLoc("block/chorus_cake_flower"));
        crossCakeBlock(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), mcLoc("block/crimson_fungus"));

        for (RegistryObject<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = CakeBuilder.BUILDER_BY_CAKE.get(cake);
            if (!builder.hasCustomBlockModel()) {
                cakeBlock(cake.get(), builder.getCakeName());

                if (builder.allowsCandles()) {
                    for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                        RegistryObject<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                        candleCakeBlock(candleCake.get(), ModBlocks.SUPPORTED_CANDLES.get(candle), builder.getCakeName());
                    }
                }
            }
        }

        CakeBuilder cakeBuilder = ModBlocks.THREE_TIERED_CAKE.get().getBuilder();
        for (Block candle : cakeBuilder.getCandleCakeByCandle().keySet()) {
            ResourceLocation type = ModBlocks.SUPPORTED_CANDLES.get(candle);
            getVariantBuilder(cakeBuilder.getCandleCakeByCandle().get(candle).get())
                    .partialState().with(ThreeTieredCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().withExistingParent(type.getPath() + "candle_three_tiered_cake", modLoc("template_three_tiered_candle_cake"))
                            .texture("candle", new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle"))))
                    .partialState().with(ThreeTieredCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().withExistingParent(type.getPath() + "candle_three_tiered_cake_lit", modLoc("template_three_tiered_candle_cake"))
                            .texture("candle", new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle_lit"))));
        }
    }

    private void cakeBlock(Block block, String name) {
        String side = "block/" + name + "_side";
        String top = "block/" + name + "_top";
        String bottom = "block/" + name + "_bottom";
        String inside = "block/" + name + "_inner";
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(models().withExistingParent(name, modLoc("template_cake"))
                .texture("side", side)
                .texture("top", top)
                .texture("bottom", bottom)));

        for (int i = 1; i < 7; i++) {
            builder.partialState().with(BaseCakeBlock.BITES, i).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice" + i, modLoc("template_cake_slice" + i))
                    .texture("side", side)
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("inside", inside)));
        }
    }

    private void candleCakeBlock(Block block, ResourceLocation type, String name) {
        String fileName = type.getPath() + "candle_" + name;
        String path = "block/" + name;
        getVariantBuilder(block)
                .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().withExistingParent(fileName, mcLoc("template_cake_with_candle"))
                        .texture("side", path + "_side")
                        .texture("top", path + "_top")
                        .texture("bottom", path + "_bottom")
                        .texture("candle", new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle"))
                        .texture("particle", "#side")))
                .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().withExistingParent(fileName + "_lit", mcLoc("template_cake_with_candle"))
                        .texture("side", path + "_side")
                        .texture("top", path + "_top")
                        .texture("bottom", path + "_bottom")
                        .texture("candle", new ResourceLocation(type.getNamespace(), "block/" + type.getPath() + "candle_lit"))
                        .texture("particle", "#side")));
    }

    private void crossCakeBlock(BaseCakeBlock cake, ResourceLocation cross) {
        String name = cake.getBuilder().getCakeName();
        String side = "block/" + name + "_side";
        String top = "block/" + name + "_top";
        String bottom = "block/" + name + "_bottom";
        String inside = "block/" + name + "_inner";
        VariantBlockStateBuilder builder = getVariantBuilder(cake);
        builder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(models().withExistingParent(name, modLoc("template_cross_cake"))
                .texture("side", side)
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("cross", cross).renderType(mcLoc("cutout"))));

        for (int i = 1; i < 7; i++) {
            BlockModelBuilder model = models().withExistingParent(name + "_slice" + i, modLoc("template_cross_cake_slice" + i))
                    .renderType(mcLoc("cutout"))
                    .texture("side", side)
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("inside", inside);

            if (i < 4) {
                model.texture("cross", cross);
            }

            builder.partialState().with(BaseCakeBlock.BITES, i).addModels(new ConfiguredModel(model));
        }
    }

    private void defaultCake(BaseCakeBlock cake) {
        VariantBlockStateBuilder builder = getVariantBuilder(cake);
        CakeBuilder cakeBuilder = cake.getBuilder();
        String name = cakeBuilder.getCakeName();

        builder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("cake"))));
        for (int i1 = 1; i1 < 7; i1++) {
            builder.partialState().with(BaseCakeBlock.BITES, i1).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("cake_slice" + i1))));
        }

        for (Block candle : cakeBuilder.getCandleCakeByCandle().keySet()) {
            ResourceLocation type = ModBlocks.SUPPORTED_CANDLES.get(candle);
            getVariantBuilder(cakeBuilder.getCandleCakeByCandle().get(candle).get())
                    .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().getExistingFile(new ResourceLocation(type.getNamespace(), type.getPath() + "candle_cake"))))
                    .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().getExistingFile(new ResourceLocation(type.getNamespace(), type.getPath() + "candle_cake_lit"))));
        }
    }

    @Override
    public String getName() {
        return "JustMoreCakes block assets";
    }
}
