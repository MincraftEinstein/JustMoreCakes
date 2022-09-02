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
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockAssetsGenerator extends BlockStateProvider {

	public BlockAssetsGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, JustMoreCakes.MOD_ID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.ENCASING_ICE.get(), models().withExistingParent("encasing_ice", mcLoc("ice")).renderType("translucent"));
		
		for (int i = 0; i < ModDataGenerators.CAKE_TYPES.size(); i++) {
			String name = ModDataGenerators.CAKE_TYPES.get(i);
			Block cake = ModBlocks.getBlock(modLoc(name));
			Block candleCake = ModBlocks.getBlock(modLoc("candle_" + name));
			
			if (name == "poison_cake") {
				getVariantBuilder(cake)
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(0)).addModels(new ConfiguredModel(models().withExistingParent(name, mcLoc("cake"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(1)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice1", mcLoc("cake_slice1"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(2)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice2", mcLoc("cake_slice2"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(3)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice3", mcLoc("cake_slice3"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(4)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice4", mcLoc("cake_slice4"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(5)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice5", mcLoc("cake_slice5"))))
				.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(6)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice6", mcLoc("cake_slice6"))));
				getVariantBuilder(candleCake)
				.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(false)).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name, mcLoc("candle_cake"))))
				.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(true)).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name + "_lit", mcLoc("candle_cake_lit"))));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					getVariantBuilder(ModBlocks.getBlock(ModBlocks.RL(color + "_candle_" + name)))
					.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(false)).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name, mcLoc(color + "_candle_cake"))))
					.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(true)).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name + "_lit", mcLoc(color + "_candle_cake"))));
				}
			}
			else if (name == "three_tiered_cake") {
				getVariantBuilder(candleCake)
				.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(false)).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name, modLoc("template_three_tiered_candle_cake"))
						.texture("candle", mcLoc("block/" + "candle"))))
				.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(true)).addModels(new ConfiguredModel(models().withExistingParent("candle_" + name + "_lit", modLoc("template_three_tiered_candle_cake"))
						.texture("candle", mcLoc("block/" + "candle_lit"))));
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					getVariantBuilder(ModBlocks.getBlock(ModBlocks.RL(color + "_candle_" + name)))
					.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(false)).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name, modLoc("template_three_tiered_candle_cake"))
							.texture("candle", mcLoc("block/" + color + "_candle"))))
					.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(true)).addModels(new ConfiguredModel(models().withExistingParent(color + "_candle_" + name + "_lit", modLoc("template_three_tiered_candle_cake"))
							.texture("candle", mcLoc("block/" + color + "_candle_lit"))));
				}
			}
			else {
				cakeBlock(cake);
				candleCakeBlock(candleCake, "", name);
				for (int i2 = 0; i2 < DyeColor.values().length; i2++) {
					String color = DyeColor.byId(i2).getName();
					candleCakeBlock(ModBlocks.getBlock(modLoc(color + "_candle_" + name)), color + "_", name);
				}
			}
		}
		cakeBlock(ModBlocks.GLOWSTONE_CAKE.get());
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
		getVariantBuilder(block)
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(0)).addModels(new ConfiguredModel(models().withExistingParent(name, modLoc("template_cake"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(1)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice1", modLoc("template_cake_slice1"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(2)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice2", modLoc("template_cake_slice2"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(3)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice3", modLoc("template_cake_slice3"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(4)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice4", modLoc("template_cake_slice4"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(5)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice5", modLoc("template_cake_slice5"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(6)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice6", modLoc("template_cake_slice6"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)));
	}
	
	private void candleCakeBlock(Block block, String color, String name) {
		String fileName = color + "candle_" + name;
		String path = "block/" + name;
		getVariantBuilder(block)
		.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(false)).addModels(new ConfiguredModel(models().withExistingParent(fileName, mcLoc("template_cake_with_candle"))
				.texture("side", path + "_side")
				.texture("top", path + "_top")
				.texture("bottom", path + "_bottom")
				.texture("candle", mcLoc("block/" + color + "candle"))
				.texture("particle", "#side")))
		.partialState().with(BaseCandleCakeBlock.LIT, Boolean.valueOf(true)).addModels(new ConfiguredModel(models().withExistingParent(fileName + "_lit", mcLoc("template_cake_with_candle"))
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
		getVariantBuilder(block)
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(0)).addModels(new ConfiguredModel(models().withExistingParent(name, modLoc("template_cross_cake"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("cross", cross).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(1)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice1", modLoc("template_cross_cake_slice1"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)
				.texture("cross", cross).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(2)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice2", modLoc("template_cross_cake_slice2"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)
				.texture("cross", cross).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(3)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice3", modLoc("template_cross_cake_slice3"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)
				.texture("cross", cross).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(4)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice4", modLoc("template_cross_cake_slice4"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(5)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice5", modLoc("template_cross_cake_slice5"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside).renderType(mcLoc("cutout"))))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(6)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice6", modLoc("template_cross_cake_slice6"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside).renderType(mcLoc("cutout"))));
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes block assets";
	}
}
