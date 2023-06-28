package einstein.jmc.data.providers;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Supplier;

import static einstein.jmc.util.Util.HAS_CAKE_SPATULA;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        add(ModBlocks.CAKE_OVEN.get(), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = cake.get().getBuilder();
            add(cake.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(cake.get()).when(HAS_CAKE_SPATULA))));
            for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                add(candleCake.get(), createCandleCakeDrops(candle));
            }
        }
    }
}
