package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockAssetsGenerator extends BlockStateProvider {

    public BlockAssetsGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, JustMoreCakes.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ModBlocks.ENCASING_ICE.get(), models().withExistingParent("encasing_ice", mcLoc("ice")).renderType("translucent"));

        for (int i = 0; i < JustMoreCakes.CAKE_TYPES.size(); i++) {
            String name = JustMoreCakes.CAKE_TYPES.get(i);
            Block cake = ModBlocks.getBlock(modLoc(name));
            Block candleCake = ModBlocks.getBlock(modLoc("candle_" + name));

            if (name.equals("poison_cake") || name.equals("tnt_cake")) {
                VariantBlockStateBuilder builder = getVariantBuilder(cake);

                builder.partialState().with(BaseCakeBlock.BITES, 0).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("cake"))));
                for (int i1 = 1; i1 < 7; i1++) {
                    builder.partialState().with(BaseCakeBlock.BITES, i1).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("cake_slice" + i1))));
                }

                getVariantBuilder(candleCake)
                        .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("candle_cake"))))
                        .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().getExistingFile(mcLoc("candle_cake_lit"))));

                for (DyeColor color : DyeColor.values()) {
                    getVariantBuilder(ModBlocks.getBlock(ModBlocks.loc(color + "_candle_" + name)))
                            .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().getExistingFile(mcLoc(color + "_candle_cake"))))
                            .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().getExistingFile(mcLoc(color + "_candle_cake"))));
                }
            }
            else if (name.equals("three_tiered_cake")) {
                getVariantBuilder(candleCake)
                        .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name, modLoc("template_three_tiered_candle_cake"))
                                .texture("candle", mcLoc("block/" + "candle"))))
                        .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name + "_lit", modLoc("template_three_tiered_candle_cake"))
                                .texture("candle", mcLoc("block/" + "candle_lit"))));

                for (DyeColor color : DyeColor.values()) {
                    getVariantBuilder(ModBlocks.getBlock(ModBlocks.loc(color + "_candle_" + name)))
                            .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name, modLoc("template_three_tiered_candle_cake"))
                                    .texture("candle", mcLoc("block/" + color + "_candle"))))
                            .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name + "_lit", modLoc("template_three_tiered_candle_cake"))
                                    .texture("candle", mcLoc("block/" + color + "_candle_lit"))));
                }
            }
            else {
                cakeBlock(cake);
                candleCakeBlock(candleCake, "", name);
                for (DyeColor color : DyeColor.values()) {
                    candleCakeBlock(ModBlocks.getBlock(modLoc(color + "_candle_" + name)), color + "_", name);
                }
            }
        }

        crossCakeBlock(ModBlocks.BROWN_MUSHROOM_CAKE.get(), mcLoc("block/brown_mushroom"));
        crossCakeBlock(ModBlocks.RED_MUSHROOM_CAKE.get(), mcLoc("block/red_mushroom"));
        crossCakeBlock(ModBlocks.CHORUS_CAKE.get(), modLoc("block/chorus_cake_flower"));
        crossCakeBlock(ModBlocks.CRIMSON_FUNGUS_CAKE.get(), mcLoc("block/crimson_fungus"));
    }

    private void cakeBlock(Block block) {
        String name = Util.getBlockRegistryName(block).getPath();
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

    private void candleCakeBlock(Block block, String color, String name) {
        String fileName = color + "candle_" + name;
        String path = "block/" + name;
        getVariantBuilder(block)
                .partialState().with(BaseCandleCakeBlock.LIT, false).addModels(new ConfiguredModel(models().withExistingParent(fileName, mcLoc("template_cake_with_candle"))
                        .texture("side", path + "_side")
                        .texture("top", path + "_top")
                        .texture("bottom", path + "_bottom")
                        .texture("candle", mcLoc("block/" + color + "candle"))
                        .texture("particle", "#side")))
                .partialState().with(BaseCandleCakeBlock.LIT, true).addModels(new ConfiguredModel(models().withExistingParent(fileName + "_lit", mcLoc("template_cake_with_candle"))
                        .texture("side", path + "_side")
                        .texture("top", path + "_top")
                        .texture("bottom", path + "_bottom")
                        .texture("candle", mcLoc("block/" + color + "candle_lit"))
                        .texture("particle", "#side")));
    }

    private void crossCakeBlock(Block block, ResourceLocation cross) {
        String name = Util.getBlockRegistryName(block).getPath();
        String side = "block/" + name + "_side";
        String top = "block/" + name + "_top";
        String bottom = "block/" + name + "_bottom";
        String inside = "block/" + name + "_inner";
        VariantBlockStateBuilder builder = getVariantBuilder(block);
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

    @Override
    public String getName() {
        return "JustMoreCakes block assets";
    }
}
