package einstein.jmc.data.packs.providers;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

import static einstein.jmc.util.Util.addDropWhenCakeSpatulaPool;
import static einstein.jmc.util.Util.addDropWhenKnifePool;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void generate() {
        add(ModBlocks.CAKE_OVEN.get(), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));
        add(ModBlocks.CAKE_STAND.get(), createSingleItemTable(ModBlocks.CAKE_STAND.get()));
        add(ModBlocks.CERAMIC_BOWL.get(), createSingleItemTable(ModBlocks.CERAMIC_BOWL.get()));

        CakeVariant.VARIANT_BY_CAKE.forEach((cake, variant) -> {
            BaseCakeBlock cakeBlock = cake.get();
            CakeVariant.Type variantType = variant.getType();
            Item sliceItem = cakeBlock.getVariant().getSliceItem().get();

            switch (variantType) {
                case BASE -> {
                    add(cakeBlock, addDropWhenKnifePool(
                            addDropWhenCakeSpatulaPool(LootTable.lootTable(), cakeBlock),
                            sliceItem, 7
                    ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenKnifePool(
                                addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), cakeBlock),
                                sliceItem, 7
                        ));
                    });
                }
                case TWO_TIERED -> {
                    Block baseCake = variant.getFamily().getBaseCake().get();
                    add(cakeBlock, addDropWhenKnifePool(
                            addDropWhenCakeSpatulaPool(LootTable.lootTable(), baseCake, 2),
                            sliceItem, 11
                    ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block ->
                                addDropWhenKnifePool(
                                        addDropWhenCakeSpatulaPool(createCandleCakeDrops(candle), baseCake, 2),
                                        sliceItem, 11
                                ));
                    });
                }
                case THREE_TIERED -> {
                    Block baseCake = variant.getFamily().getBaseCake().get();
                    add(cakeBlock, block ->
                            addDropWhenKnifePool(
                                    addDropWhenCakeSpatulaPool(LootTable.lootTable(), block, baseCake, 3, true),
                                    sliceItem, 16
                            ));

                    variant.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                        add(candleCake.get(), block -> addDropWhenKnifePool(addDropWhenCakeSpatulaPool(
                                        LootTable.lootTable().withPool(
                                                Util.addHalfConditionToPool(LootPool.lootPool()
                                                        .setRolls(ConstantValue.exactly(1))
                                                        .add(LootItem.lootTableItem(candle)), block)
                                        ), block, baseCake, 3, true
                                ), sliceItem, 16
                        ));
                    });
                }
            }
        });
    }
}
