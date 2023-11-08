package einstein.jmc.util;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.platform.Services;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CakeBuilder {

    public static final Map<Supplier<BaseCakeBlock>, CakeBuilder> BUILDER_BY_CAKE = new HashMap<>();

    private final String cakeName;
    private final boolean allowsCandles;
    private final boolean customBlockModel;
    private final boolean customItemModel;
    private final Map<Block, Supplier<BaseCandleCakeBlock>> candleCakeByCandle = new HashMap<>();
    private boolean canAlwaysEat;
    private int nutrition = 2;
    private float saturation = 0.1F;
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
        this(cakeName, allowsCandles, false);
    }

    public <T extends BaseCakeBlock> CakeBuilder setCakeClass(CakeClazzSupplier<T> clazz) {
        cakeClazz = clazz;
        return this;
    }

    public <T extends BaseCandleCakeBlock> CakeBuilder setCandleCakeClass(CandleCakeClazzSupplier<T> clazz) {
        candleCakeClazz = clazz;
        return this;
    }

    public <T extends BaseCakeBlock, T2 extends BaseCandleCakeBlock> CakeBuilder setBothClasses(CakeClazzSupplier<T> cakeClazz, CandleCakeClazzSupplier<T2> candleCakeClazz) {
        this.cakeClazz = cakeClazz;
        this.candleCakeClazz = candleCakeClazz;
        return this;
    }

    public CakeBuilder setCakeProperties(BlockBehaviour.Properties properties) {
        cakeProperties = properties;
        return this;
    }

    public CakeBuilder setCandleCakeProperties(BlockBehaviour.Properties properties) {
        candleCakeProperties = properties;
        return this;
    }

    public CakeBuilder alwaysEat() {
        this.canAlwaysEat = true;
        return this;
    }

    public CakeBuilder nutrition(int nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public CakeBuilder saturation(float saturation) {
        this.saturation = saturation;
        return this;
    }

    public Supplier<BaseCakeBlock> build() {
        if (ModBlocks.SUPPORTED_CANDLES.isEmpty()) {
            JustMoreCakes.addSupportedCandles();
        }

        if (cakeClazz == null) {
            cakeClazz = BaseCakeBlock::new;
        }

        if (cakeProperties == null) {
            cakeProperties = ModBlocks.cakeProperties();
        }

        Supplier<BaseCakeBlock> cake = Services.REGISTRY.registerBlock(cakeName, () -> cakeClazz.get(this));

        if (allowsCandles) {
            if (candleCakeClazz == null) {
                candleCakeClazz = BaseCandleCakeBlock::new;
            }

            if (candleCakeProperties == null) {
                candleCakeProperties = ModBlocks.candleCakeProperties();
            }

            for (Block candle : ModBlocks.SUPPORTED_CANDLES.keySet()) {
                String type = ModBlocks.SUPPORTED_CANDLES.get(candle).getPath();
                Supplier<BaseCandleCakeBlock> coloredCandleCake = Services.REGISTRY.registerBlockNoItem(type + "candle_" + cakeName, () -> candleCakeClazz.get(cake.get(), candleCakeProperties));
                candleCakeByCandle.put(candle, coloredCandleCake);
            }
        }

        BUILDER_BY_CAKE.put(cake, this);
        return cake;
    }

    public String getCakeName() {
        return cakeName;
    }

    public Map<Block, Supplier<BaseCandleCakeBlock>> getCandleCakeByCandle() {
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

    public BlockBehaviour.Properties getCakeProperties() {
        return cakeProperties;
    }

    public boolean canAlwaysEat() {
        return canAlwaysEat;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturation() {
        return saturation;
    }

    @FunctionalInterface
    public interface CakeClazzSupplier<T extends BaseCakeBlock> {

        T get(CakeBuilder builder);
    }

    @FunctionalInterface
    public interface CandleCakeClazzSupplier<T extends BaseCandleCakeBlock> {

        T get(BaseCakeBlock originalCake, BlockBehaviour.Properties properties);
    }
}
