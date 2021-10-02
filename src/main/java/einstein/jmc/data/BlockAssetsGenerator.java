package einstein.jmc.data;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockAssetsGenerator extends BlockStateProvider {

	public BlockAssetsGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, JustMoreCakes.MODID, exFileHelper);
	}
	
	@Override
	protected void registerStatesAndModels() {
		cubeAll(ModBlocks.ENCASING_ICE);
	}
	
	private void cakeBlock(Block block, boolean hasCross) {
		String name = block.getRegistryName().getPath();
		String side = name + "_side";
		String top = name + "_top";
		String bottom = name + "_bottom";
		String inside = name + "_inner";
		String parent;
		if (hasCross) {
			parent = "template_cross_cake";
		}
		else {
			parent = "template_cake";
		}
		getVariantBuilder(block)
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(0)).addModels(new ConfiguredModel(models().withExistingParent(name, modLoc(parent))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(1)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice1", modLoc(parent + "_slice1"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(2)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice2", modLoc(parent + "_slice2"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(3)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice3", modLoc(parent + "_slice3"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(4)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice4", modLoc(parent + "_slice4"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(5)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice5", modLoc(parent + "_slice5"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)))
		.partialState().with(BaseCakeBlock.BITES, Integer.valueOf(6)).addModels(new ConfiguredModel(models().withExistingParent(name + "_slice6", modLoc(parent + "_slice6"))
				.texture("side", side)
				.texture("top", top)
				.texture("bottom", bottom)
				.texture("inside", inside)));
	}
	
	private void candleCakeBlock(Block block) {
		String name = block.getRegistryName().getPath();
		simpleBlock(block, new ConfiguredModel(models().withExistingParent(name, "template_cake_with_candle")));
	}
	
	@Override
	public String getName() {
		return "JustMoreCakes block assets";
	}
}
