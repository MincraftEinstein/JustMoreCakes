package einstein.jmc.data.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
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

import static einstein.jmc.util.Util.HAS_CAKE_SPATULA;
import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;

public class ModBlockLootTables extends BlockLootSubProvider {

    private static final List<Block> knownBlocks = new ArrayList<>();

    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CAKE_OVEN.get());

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            dropWhenCakeSpatula(cake.get());

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cake.get()));
                knownBlocks.add(candleCake.get());
            });
        });

        dropWhenCakeSpatula(ModBlocks.CUPCAKE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        knownBlocks.add(ModBlocks.CAKE_OVEN.get());
        return knownBlocks;
    }

    private void dropWhenCakeSpatula(Block block) {
        add(block, addDropWhenCakeSpatulaPool(LootTable.lootTable(), block));
        knownBlocks.add(block);
    }
}
