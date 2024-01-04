package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseTwoTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;
import static einstein.jmc.init.ModBlocks.register;

public class CakeBuilder {

    public static final Map<Supplier<BaseCakeBlock>, CakeBuilder> BUILDER_BY_CAKE = new HashMap<>();
    public static final Map<Block, ResourceLocation> SUPPORTED_CANDLES = Util.make(new HashMap<>(), map -> {
        // Is not using a for loop to prevent modded colors from being added,
        // the main reason for this is adding unsupported candle cakes with missing models
        map.put(Blocks.CANDLE, mcLoc(""));
        map.put(Blocks.WHITE_CANDLE, mcLoc("white_"));
        map.put(Blocks.ORANGE_CANDLE, mcLoc("orange_"));
        map.put(Blocks.MAGENTA_CANDLE, mcLoc("magenta_"));
        map.put(Blocks.LIGHT_BLUE_CANDLE, mcLoc("light_blue_"));
        map.put(Blocks.YELLOW_CANDLE, mcLoc("yellow_"));
        map.put(Blocks.LIME_CANDLE, mcLoc("lime_"));
        map.put(Blocks.PINK_CANDLE, mcLoc("pink_"));
        map.put(Blocks.GRAY_CANDLE, mcLoc("gray_"));
        map.put(Blocks.LIGHT_GRAY_CANDLE, mcLoc("light_gray_"));
        map.put(Blocks.CYAN_CANDLE, mcLoc("cyan_"));
        map.put(Blocks.PURPLE_CANDLE, mcLoc("purple_"));
        map.put(Blocks.BLUE_CANDLE, mcLoc("blue_"));
        map.put(Blocks.BROWN_CANDLE, mcLoc("brown_"));
        map.put(Blocks.GREEN_CANDLE, mcLoc("green_"));
        map.put(Blocks.RED_CANDLE, mcLoc("red_"));
        map.put(Blocks.BLACK_CANDLE, mcLoc("black_"));
    });

    private final String cakeName;
    private final CakeVariant variant;
    private final Map<Block, Supplier<BaseCandleCakeBlock>> candleCakeByCandle = new HashMap<>();
    private boolean canAlwaysEat;
    private boolean allowsCandles = true;
    private boolean noItem = false;
    private boolean customItemModel;
    private int nutrition = BaseCakeBlock.DEFAULT_NUTRITION;
    private float saturationModifier = BaseCakeBlock.DEFAULT_SATURATION_MODIFIER;
    private CakeClazzSupplier<?> cakeClazz;
    private CandleCakeClazzSupplier<?> candleCakeClazz;
    private BlockBehaviour.Properties cakeProperties;
    private BlockBehaviour.Properties candleCakeProperties;
    private CakeModel cakeModel = CakeModel.DEFAULT;
    private CakeModel candleCakeModel = CakeModel.DEFAULT;
    private CakeFamily family;

    public CakeBuilder(String cakeName) {
        this(cakeName, CakeVariant.BASE);
    }

    public CakeBuilder(String cakeName, CakeVariant variant) {
        this.cakeName = cakeName;
        this.variant = variant;
    }

    public <T extends BaseCakeBlock> CakeBuilder setCakeClass(CakeClazzSupplier<T> clazz) {
        cakeClazz = clazz;
        return this;
    }

    public <T extends BaseCandleCakeBlock> CakeBuilder setCandleCakeClass(CandleCakeClazzSupplier<T> clazz) {
        candleCakeClazz = clazz;
        return this;
    }

    public <T extends BaseCakeBlock, V extends BaseCandleCakeBlock> CakeBuilder setBothClasses(CakeClazzSupplier<T> cakeClazz, CandleCakeClazzSupplier<V> candleCakeClazz) {
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

    public CakeBuilder disallowCandles() {
        allowsCandles = false;
        return this;
    }

    public CakeBuilder noItem() {
        noItem = true;
        return this;
    }

    public CakeBuilder customItemModel() {
        customItemModel = true;
        return this;
    }

    public CakeBuilder nutrition(int nutrition) {
        this.nutrition = nutrition;
        return this;
    }

    public CakeBuilder saturationModifier(float modifier) {
        saturationModifier = modifier;
        return this;
    }

    public CakeBuilder model(CakeModel cakeModel) {
        return models(cakeModel, CakeModel.DEFAULT);
    }

    public CakeBuilder models(CakeModel cakeModel, CakeModel candleCakeModel) {
        this.cakeModel = cakeModel;
        this.candleCakeModel = candleCakeModel;
        return this;
    }

    CakeBuilder setFamily(CakeFamily family) {
        this.family = family;
        return this;
    }

    public Supplier<BaseCakeBlock> build() {
        if (cakeClazz == null) {
            cakeClazz = switch (variant) {
                case BASE -> BaseCakeBlock::new;
                case TWO_TIERED -> BaseTwoTieredCakeBlock::new;
                case THREE_TIERED -> BaseThreeTieredCakeBlock::new;
            };
        }

        if (cakeProperties == null) {
            cakeProperties = ModBlocks.cakeProperties();
        }

        Supplier<BaseCakeBlock> cake = register(cakeName, () -> cakeClazz.get(this), hasItem());

        if (allowsCandles) {
            if (candleCakeClazz == null) {
                candleCakeClazz = switch (variant) {
                    case BASE -> BaseCandleCakeBlock::new;
                    case TWO_TIERED -> BaseTwoTieredCandleCakeBlock::new;
                    case THREE_TIERED -> BaseThreeTieredCandleCakeBlock::new;
                };
            }

            if (candleCakeProperties == null) {
                candleCakeProperties = ModBlocks.candleCakeProperties();
            }

            for (Block candle : SUPPORTED_CANDLES.keySet()) {
                String type = SUPPORTED_CANDLES.get(candle).getPath();
                Supplier<BaseCandleCakeBlock> coloredCandleCake = register(type + "candle_" + cakeName, () -> candleCakeClazz.get(cake.get(), candle, candleCakeProperties), false);
                candleCakeByCandle.put(candle, coloredCandleCake);
            }
        }

        BUILDER_BY_CAKE.put(cake, this);
        return cake;
    }

    public String getCakeName() {
        return cakeName;
    }

    public CakeVariant getVariant() {
        return variant;
    }

    public Map<Block, Supplier<BaseCandleCakeBlock>> getCandleCakeByCandle() {
        return candleCakeByCandle;
    }

    public boolean canAlwaysEat() {
        return canAlwaysEat;
    }

    public boolean allowsCandles() {
        return allowsCandles;
    }

    public boolean hasItem() {
        return !noItem;
    }

    public boolean hasCustomItemModel() {
        return customItemModel;
    }

    public BlockBehaviour.Properties getCakeProperties() {
        return cakeProperties;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public CakeModel getCakeModel() {
        return cakeModel;
    }

    public CakeModel getCandleCakeModel() {
        return candleCakeModel;
    }

    @Nullable
    public CakeFamily getFamily() {
        return family;
    }

    @FunctionalInterface
    public interface CakeClazzSupplier<T extends BaseCakeBlock> {

        T get(CakeBuilder builder);
    }

    @FunctionalInterface
    public interface CandleCakeClazzSupplier<T extends BaseCandleCakeBlock> {

        T get(BaseCakeBlock originalCake, Block candle, BlockBehaviour.Properties properties);
    }
}
