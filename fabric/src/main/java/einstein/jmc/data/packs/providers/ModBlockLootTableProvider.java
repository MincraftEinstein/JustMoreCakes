package einstein.jmc.data.packs.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        add(ModBlocks.CAKE_OVEN.get(), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));
        add(ModBlocks.CAKE_STAND.get(), createSingleItemTable(ModBlocks.CAKE_STAND.get()));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            Block cakeBlock = cake.get();
            if (cake == ModBlocks.TWO_TIERED_CAKE) {
                add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 2));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 2));
                });
                return;
            }
            else if (cake == ModBlocks.THREE_TIERED_CAKE) {
                add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 3));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(
                            LootTable.lootTable().withPool(
                                    Util.addHalfConditionToPool(LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(candle)), block)
                            ), block, Blocks.CAKE, 3, true));
                });
                return;
            }

            add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), cakeBlock));

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cakeBlock));
            });
        });
    }
}
