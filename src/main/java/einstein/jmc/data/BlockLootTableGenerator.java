package einstein.jmc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.Util;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockLootTableGenerator extends BlockLoot {

	private final List<Block> candleCakes = new ArrayList<>(ForgeRegistries.BLOCKS.getValues().stream()
			.filter((block) -> Util.getBlockRegistryName(block).getNamespace().equals(JustMoreCakes.MOD_ID) &&
					Util.getBlockRegistryName(block).getPath().contains("candle"))
			.collect(Collectors.toList()));
	
	@Override
	protected void addTables() {
		dropSelf(ModBlocks.CAKE_OVEN.get());
		for (Block block : candleCakes) {
			String name = Util.getBlockRegistryName(block).getPath();
			String color = name.substring(0, name.indexOf("candle"));
			add(block, createCandleCakeDrops(ModBlocks.getBlock(ModBlocks.mcLoc(color + "candle"))));
		}
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		List<Block> knownBlocks = new ArrayList<>(candleCakes);
		knownBlocks.add(ModBlocks.CAKE_OVEN.get());
		return knownBlocks;
	}
}
