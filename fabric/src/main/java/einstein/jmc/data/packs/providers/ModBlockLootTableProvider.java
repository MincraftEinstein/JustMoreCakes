package einstein.jmc.data.packs.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        add(ModBlocks.CAKE_OVEN.get(), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            if (cake == ModBlocks.TWO_TIERED_CAKE) {
                add(cake.get(), addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 2));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 2));
                });
                return;
            }
            else if (cake == ModBlocks.THREE_TIERED_CAKE) {
                add(cake.get(), addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 3));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 3));
                });
                return;
            }

            dropWhenCakeSpatula(cake.get());

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cake.get()));
            });
        });
    }

    private void dropWhenCakeSpatula(Block block) {
        add(block, addDropWhenCakeSpatulaPool(LootTable.lootTable(), block));
    }
}
