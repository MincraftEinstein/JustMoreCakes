package einstein.jmc.data.providers;

import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

import static einstein.jmc.JustMoreCakes.HAS_CAKE_SPATULA;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(toBlockLoc(ModBlocks.CAKE_OVEN.get()), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        CakeBuilder.BUILDER_BY_CAKE.forEach((cake, builder) -> {
            consumer.accept(toBlockLoc(cake.get()), LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(cake.get()).when(HAS_CAKE_SPATULA))));

            builder.getCandleCakeByCandle().forEach((candle, candleCake) -> {
                consumer.accept(toBlockLoc(candleCake.get()), createCandleCakeDrops(candle).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(candleCake.get()).when(HAS_CAKE_SPATULA))));
            });
        });
    }

    private ResourceLocation toBlockLoc(Block block) {
        ResourceLocation id = Util.getBlockId(block);
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }
}
