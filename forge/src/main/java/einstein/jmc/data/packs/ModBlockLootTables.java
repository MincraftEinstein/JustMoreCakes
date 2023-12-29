package einstein.jmc.data.packs;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;

public class ModBlockLootTables extends BlockLootSubProvider {

    private static final List<Block> knownBlocks = new ArrayList<>();

    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CAKE_OVEN.get());
        dropSelf(ModBlocks.CAKE_STAND.get());

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            Block cakeBlock = cake.get();
            if (cake == ModBlocks.TWO_TIERED_CAKE) {
                add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 2));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 2));
                    knownBlocks.add(candleCake.get());
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
                            ), block, Blocks.CAKE, 3, true
                    ));
                    knownBlocks.add(candleCake.get());
                });
                return;
            }

            add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), cakeBlock));
            knownBlocks.add(cakeBlock);

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cakeBlock));
                knownBlocks.add(candleCake.get());
            });
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        knownBlocks.add(ModBlocks.CAKE_OVEN.get());
        knownBlocks.add(ModBlocks.CAKE_STAND.get());
        knownBlocks.add(ModBlocks.TWO_TIERED_CAKE.get());
        knownBlocks.add(ModBlocks.THREE_TIERED_CAKE.get());
        return knownBlocks;
    }
}
