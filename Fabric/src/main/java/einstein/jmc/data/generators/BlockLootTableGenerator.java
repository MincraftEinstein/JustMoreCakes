package einstein.jmc.data.generators;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockLootTableGenerator extends SimpleFabricLootTableProvider {

    public BlockLootTableGenerator(FabricDataGenerator generator) {
        super(generator, LootContextParamSets.BLOCK);
    }

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(toBlockLoc(ModBlocks.CAKE_OVEN.get()), BlockLoot.createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = cake.get().getBuilder();
            for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                consumer.accept(toBlockLoc(candleCake.get()), BlockLoot.createCandleCakeDrops(candle));
            }
        }
    }

    private ResourceLocation toBlockLoc(Block block) {
        ResourceLocation id = Util.getBlockId(block);
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }
}
