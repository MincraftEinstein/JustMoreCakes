package einstein.jmc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockLootTableGenerator extends BlockLoot {

	private List<Block> candleCakes = new ArrayList<Block>(ForgeRegistries.BLOCKS.getValues().stream()
			.filter((block) -> block.getRegistryName().getNamespace().equals(JustMoreCakes.MODID) &&
					block.getRegistryName().getPath().contains("candle"))
			.collect(Collectors.toList()));
	
	@Override
	protected void addTables() {
		dropSelf(ModBlocks.CAKE_OVEN.get());
		for (int i = 0; i < candleCakes.size(); i++) {
			Block block = candleCakes.get(i);
			String name = block.getRegistryName().getPath();
			String color = name.substring(0, name.indexOf("candle"));
			add(block, createCandleCakeDrops(ModBlocks.getBlock(ModBlocks.MCRL(color + "candle"))));
		}
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		List<Block> knownBlocks = new ArrayList<Block>(candleCakes);
		knownBlocks.add(ModBlocks.CAKE_OVEN.get());
		return knownBlocks;
	}
}
