package einstein.jmc.data.providers;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        add(ModBlocks.CAKE_OVEN.get(), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = cake.get().getBuilder();
            for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                add(candleCake.get(), createCandleCakeDrops(candle));
            }
        }
    }
}
