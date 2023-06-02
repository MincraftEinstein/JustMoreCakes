package einstein.jmc.data.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static einstein.jmc.JustMoreCakes.HAS_CAKE_SPATULA;

public class ModBlockLootTables extends BlockLootSubProvider {

    private static final List<Block> knownBlocks = new ArrayList<>();

    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CAKE_OVEN.get());

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            add(cake.get(), block -> LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(block).when(HAS_CAKE_SPATULA))));
            knownBlocks.add(cake.get());

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> createCandleCakeDrops(candle).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(block).when(HAS_CAKE_SPATULA))));
                knownBlocks.add(candleCake.get());
            });
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        knownBlocks.add(ModBlocks.CAKE_OVEN.get());
        return knownBlocks;
    }
}
