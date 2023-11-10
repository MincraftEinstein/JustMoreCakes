package einstein.jmc.data.packs;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;

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

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            if (cake == ModBlocks.TWO_TIERED_CAKE) {
                add(cake.get(), addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 2));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 2));
                    knownBlocks.add(candleCake.get());
                });
                return;
            }
            else if (cake == ModBlocks.THREE_TIERED_CAKE) {
                add(cake.get(), addDropWhenCakeSpatulaPool(LootTable.lootTable(), Blocks.CAKE, 3));

                builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                    add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), Blocks.CAKE, 3));
                    knownBlocks.add(candleCake.get());
                });
                return;
            }

            dropWhenCakeSpatula(cake.get());

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cake.get()));
                knownBlocks.add(candleCake.get());
            });
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        knownBlocks.add(ModBlocks.CAKE_OVEN.get());
        knownBlocks.add(ModBlocks.TWO_TIERED_CAKE.get());
        knownBlocks.add(ModBlocks.THREE_TIERED_CAKE.get());
        return knownBlocks;
    }

    private void dropWhenCakeSpatula(Block block) {
        add(block, addDropWhenCakeSpatulaPool(LootTable.lootTable(), block));
        knownBlocks.add(block);
    }
}
