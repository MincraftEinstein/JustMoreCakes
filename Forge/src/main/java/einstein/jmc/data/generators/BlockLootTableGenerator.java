package einstein.jmc.data.generators;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class BlockLootTableGenerator extends BlockLoot {

	private static final List<Block> knownBlocks = new ArrayList<>();

	@Override
	protected void addTables() {
		dropSelf(ModBlocks.CAKE_OVEN.get());

		for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
			CakeBuilder builder = cake.get().getBuilder();
			for (Block candle : builder.getCandleCakeByCandle().keySet()) {
				Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
				add(candleCake.get(), createCandleCakeDrops(candle));
				knownBlocks.add(candleCake.get());
			}
		}
	}
	
	@Override
	protected Iterable<Block> getKnownBlocks() {
		knownBlocks.add(ModBlocks.CAKE_OVEN.get());
		return knownBlocks;
	}
}
