package einstein.jmc.data.packs;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.CakeVariant;
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
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariant variant = builder.getVariant();
            Block baseCake = builder.getFamily().getBaseCake().get();

            knownBlocks.add(cakeBlock);
            switch (variant) {
                case BASE -> {
                    add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), cakeBlock));

                    builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cakeBlock));
                        knownBlocks.add(candleCake.get());
                    });
                }
                case TWO_TIERED -> {
                    add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), baseCake, 2));

                    builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), baseCake, 2));
                        knownBlocks.add(candleCake.get());
                    });
                }
                case THREE_TIERED -> {
                    add(cakeBlock, addDropWhenCakeSpatulaPool(LootTable.lootTable(), baseCake, 3));

                    builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenCakeSpatulaPool(
                                LootTable.lootTable().withPool(
                                        Util.addHalfConditionToPool(LootPool.lootPool()
                                                .setRolls(ConstantValue.exactly(1))
                                                .add(LootItem.lootTableItem(candle)), block)
                                ), block, baseCake, 3, true
                        ));
                        knownBlocks.add(candleCake.get());
                    });
                }
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        knownBlocks.add(ModBlocks.CAKE_OVEN.get());
        knownBlocks.add(ModBlocks.CAKE_STAND.get());
        return knownBlocks;
    }
}
