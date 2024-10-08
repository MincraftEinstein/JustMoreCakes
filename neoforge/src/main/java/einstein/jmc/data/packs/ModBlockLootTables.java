package einstein.jmc.data.packs;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;
import static einstein.jmc.util.Util.addDropWhenKnifePool;

public class ModBlockLootTables extends BlockLootSubProvider {

    private static final List<Block> KNOWN_BLOCKS = new ArrayList<>();

    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.CAKE_OVEN.get());
        dropSelf(ModBlocks.CAKE_STAND.get());
        dropSelf(ModBlocks.CERAMIC_BOWL.get());

        CakeVariant.VARIANT_BY_CAKE.forEach((cake, variant) -> {
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariant.Type variantType = variant.getType();
            Item sliceItem = cakeBlock.getVariant().getSliceItem().get();

            KNOWN_BLOCKS.add(cakeBlock);
            switch (variantType) {
                case BASE -> {
                    add(cakeBlock, addDropWhenKnifePool(
                            addDropWhenCakeSpatulaPool(LootTable.lootTable(), cakeBlock),
                            sliceItem, 7, false
                    ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenKnifePool(
                                addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cakeBlock),
                                sliceItem, 7, false
                        ));
                        KNOWN_BLOCKS.add(candleCake.get());
                    });
                }
                case TWO_TIERED -> {
                    Block baseCake = variant.getFamily().getBaseCake().get();
                    add(cakeBlock, addDropWhenKnifePool(
                            addDropWhenCakeSpatulaPool(LootTable.lootTable(), baseCake, 2),
                            sliceItem, 11, false
                    ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block ->
                                addDropWhenKnifePool(
                                        addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), baseCake, 2),
                                        sliceItem, 11, false
                                ));
                        KNOWN_BLOCKS.add(candleCake.get());
                    });
                }
                case THREE_TIERED -> {
                    Block baseCake = variant.getFamily().getBaseCake().get();
                    add(cakeBlock, block -> addDropWhenKnifePool(
                            addDropWhenCakeSpatulaPool(LootTable.lootTable(), block, baseCake, 3, true),
                            sliceItem, 16, false
                    ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenKnifePool(
                                addDropWhenCakeSpatulaPool(
                                        LootTable.lootTable().withPool(
                                                Util.addHalfConditionToPool(LootPool.lootPool()
                                                        .setRolls(ConstantValue.exactly(1))
                                                        .add(LootItem.lootTableItem(candle)), block)
                                        ), block, baseCake, 3, true
                                ), sliceItem, 16, false
                        ));
                        KNOWN_BLOCKS.add(candleCake.get());
                    });
                }
            }
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        KNOWN_BLOCKS.add(ModBlocks.CAKE_OVEN.get());
        KNOWN_BLOCKS.add(ModBlocks.CAKE_STAND.get());
        KNOWN_BLOCKS.add(ModBlocks.CERAMIC_BOWL.get());
        return KNOWN_BLOCKS;
    }
}
