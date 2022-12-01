package einstein.jmc.util;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class CakeBuilder {

    private final String name;
    private final boolean allowsCandles;
    private final List<RegistryObject<BaseCandleCakeBlock>> candleCakes = new ArrayList<>();
    private CakeClazzSupplier<?> cakeClazz;
    private CandleCakeClazzSupplier<?> candleCakeClazz;
    private BlockBehaviour.Properties cakeProperties;
    private BlockBehaviour.Properties candleCakeProperties;

    public CakeBuilder(String name, boolean allowsCandles) {
        this.name = name;
        this.allowsCandles = allowsCandles;
    }

    public <T extends BaseCakeBlock> CakeBuilder setCakeClass(CakeClazzSupplier<T> clazz) {
        cakeClazz = clazz;
        return this;
    }

    public <T extends BaseCandleCakeBlock> CakeBuilder setCandleCakeClass(CandleCakeClazzSupplier<T> clazz) {
        candleCakeClazz = clazz;
        return this;
    }

    public CakeBuilder setCakeProperties(BlockBehaviour.Properties properties) {
        this.cakeProperties = properties;
        return this;
    }

    public CakeBuilder setCandleCakeProperties(BlockBehaviour.Properties properties) {
        this.candleCakeProperties = properties;
        return this;
    }

    public RegistryObject<BaseCakeBlock> build() {
        if (cakeClazz == null) {
            cakeClazz = BaseCakeBlock::new;
        }

        if (cakeProperties == null) {
            cakeProperties = ModBlocks.cakeProperties();
        }

        RegistryObject<BaseCakeBlock> cake = ModBlocks.register(name, () -> cakeClazz.get(cakeProperties, allowsCandles));

        if (allowsCandles) {
            if (candleCakeClazz == null) {
                candleCakeClazz = BaseCandleCakeBlock::new;
            }

            if (candleCakeProperties == null) {
                candleCakeProperties = ModBlocks.candleCakeProperties();
            }

            RegistryObject<BaseCandleCakeBlock> candleCake = ModBlocks.registerNoItem("candle_" + name, () -> candleCakeClazz.get(cake.get(), candleCakeProperties));
            candleCakes.add(candleCake);
            for (DyeColor color : DyeColor.values()) {
                RegistryObject<BaseCandleCakeBlock> coloredCandleCake = ModBlocks.registerNoItem(color.getName() + "_candle_" + name, () -> candleCakeClazz.get(cake.get(), candleCakeProperties));
                candleCakes.add(coloredCandleCake);
            }
        }

        return cake;
    }

    public List<RegistryObject<BaseCandleCakeBlock>> getCandleCakes() {
        return candleCakes;
    }

    @FunctionalInterface
    public interface CakeClazzSupplier<T extends BaseCakeBlock> {
        T get(BlockBehaviour.Properties properties, boolean allowsCandles);
    }

    @FunctionalInterface
    public interface CandleCakeClazzSupplier<T extends BaseCandleCakeBlock> {
        T get(BaseCakeBlock originalCake, BlockBehaviour.Properties properties);
    }
}
