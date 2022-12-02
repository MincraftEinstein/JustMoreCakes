package einstein.jmc.util;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class CakeBuilder {

    public static Map<RegistryObject<BaseCakeBlock>, CakeBuilder> BUILDER_BY_CAKE = new HashMap<>();

    private final String cakeName;
    private final boolean allowsCandles;
    private final boolean customBlockModel;
    private final boolean customItemModel;
    private final Map<Block, RegistryObject<BaseCandleCakeBlock>> candleCakeByCandle = new HashMap<>();
    private CakeClazzSupplier<?> cakeClazz;
    private CandleCakeClazzSupplier<?> candleCakeClazz;
    private BlockBehaviour.Properties cakeProperties;
    private BlockBehaviour.Properties candleCakeProperties;

    public CakeBuilder(String cakeName, boolean allowsCandles, boolean customBlockModel, boolean customItemModel) {
        this.cakeName = cakeName;
        this.allowsCandles = allowsCandles;
        this.customBlockModel = customBlockModel;
        this.customItemModel = customItemModel;
    }

    public CakeBuilder(String cakeName, boolean allowsCandles, boolean customBlockModel) {
        this(cakeName, allowsCandles, customBlockModel, false);
    }

    public CakeBuilder(String cakeName, boolean allowsCandles) {
        this(cakeName, allowsCandles, false, false);
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
        if (ModBlocks.SUPPORTED_CANDLES.isEmpty()) {
            JustMoreCakes.AddSupportedCandles();
        }

        if (cakeClazz == null) {
            cakeClazz = BaseCakeBlock::new;
        }

        if (cakeProperties == null) {
            cakeProperties = ModBlocks.cakeProperties();
        }

        RegistryObject<BaseCakeBlock> cake = ModBlocks.register(cakeName, () -> cakeClazz.get(cakeProperties, allowsCandles, this));

        if (allowsCandles) {
            if (candleCakeClazz == null) {
                candleCakeClazz = BaseCandleCakeBlock::new;
            }

            if (candleCakeProperties == null) {
                candleCakeProperties = ModBlocks.candleCakeProperties();
            }

            for (Block candle : ModBlocks.SUPPORTED_CANDLES.keySet()) {
                String type = ModBlocks.SUPPORTED_CANDLES.get(candle).getPath();
                RegistryObject<BaseCandleCakeBlock> coloredCandleCake = ModBlocks.registerNoItem(type + "candle_" + cakeName, () -> candleCakeClazz.get(cake.get(), candleCakeProperties));
                candleCakeByCandle.put(candle, coloredCandleCake);
            }
        }

        BUILDER_BY_CAKE.put(cake, this);
        return cake;
    }

    public String getCakeName() {
        return cakeName;
    }

    public Map<Block, RegistryObject<BaseCandleCakeBlock>> getCandleCakeByCandle() {
        return candleCakeByCandle;
    }

    public boolean allowsCandles() {
        return allowsCandles;
    }

    public boolean hasCustomBlockModel() {
        return customBlockModel;
    }

    public boolean hasCustomItemModel() {
        return customItemModel;
    }

    @FunctionalInterface
    public interface CakeClazzSupplier<T extends BaseCakeBlock> {
        T get(BlockBehaviour.Properties properties, boolean allowsCandles, CakeBuilder builder);
    }

    @FunctionalInterface
    public interface CandleCakeClazzSupplier<T extends BaseCandleCakeBlock> {
        T get(BaseCakeBlock originalCake, BlockBehaviour.Properties properties);
    }
}
