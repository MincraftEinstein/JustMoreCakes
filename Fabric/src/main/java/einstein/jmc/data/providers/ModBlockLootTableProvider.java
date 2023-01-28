package einstein.jmc.data.providers;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.CakeBuilder;
import einstein.jmc.util.Util;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {

    public ModBlockLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {}

    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        consumer.accept(toBlockLoc(ModBlocks.CAKE_OVEN.get()), createSingleItemTable(ModBlocks.CAKE_OVEN.get()));

        for (Supplier<BaseCakeBlock> cake : CakeBuilder.BUILDER_BY_CAKE.keySet()) {
            CakeBuilder builder = cake.get().getBuilder();
            for (Block candle : builder.getCandleCakeByCandle().keySet()) {
                Supplier<BaseCandleCakeBlock> candleCake = builder.getCandleCakeByCandle().get(candle);
                consumer.accept(toBlockLoc(candleCake.get()), createCandleCakeDrops(candle));
            }
        }
    }

    private ResourceLocation toBlockLoc(Block block) {
        ResourceLocation id = Util.getBlockId(block);
        return new ResourceLocation(id.getNamespace(), "blocks/" + id.getPath());
    }
}
