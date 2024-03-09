package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseTwoTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.platform.Services;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.mcLoc;

public class CakeVariant {

    public static final Map<Supplier<BaseCakeBlock>, CakeVariant> VARIANT_BY_CAKE = new HashMap<>();
    public static final Map<Block, ResourceLocation> SUPPORTED_CANDLES = Util.make(new HashMap<>(), map -> {
        // Not using a for loop to prevent modded colors from being added,
        // the main reason for this is so unsupported candles won't create candle cakes with missing assets
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
    private final CakeVariantType type;
    private final Map<Block, Supplier<BaseCandleCakeBlock>> candleCakeByCandle = new HashMap<>();
    private boolean canAlwaysEat;
    private boolean allowsCandles = true;
    private boolean noItem = false;
    private boolean customItemModel;
    private int nutrition = BaseCakeBlock.DEFAULT_NUTRITION;
    private float saturationModifier = BaseCakeBlock.DEFAULT_SATURATION_MODIFIER;
    private BlockBehaviour.Properties cakeProperties;
    private BlockBehaviour.Properties candleCakeProperties;
    private CakeModel cakeModel = CakeModel.DEFAULT;
    private CakeModel candleCakeModel = CakeModel.DEFAULT;
    private CakeFamily family;
    private Supplier<BaseCakeBlock> cake;
    private Supplier<Item> item = () -> Items.AIR;

    private CakeVariant(String cakeName, CakeVariantType type) {
        this.cakeName = cakeName;
        this.type = type;
    }

    public static Builder create(String cakeName) {
        return create(cakeName, CakeVariantType.BASE);
    }

    public static Builder create(String cakeName, CakeVariantType type) {
        return new Builder(cakeName, type);
    }

    public String getCakeName() {
        return cakeName;
    }

    public CakeVariantType getType() {
        return type;
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

    public Supplier<BaseCakeBlock> getCake() {
        return cake;
    }

    public Supplier<Item> getItem() {
        return item;
    }

    public static class Builder {

        private final CakeVariant variant;
        private CakeClazzSupplier<?> cakeClazz;
        private CandleCakeClazzSupplier<?> candleCakeClazz;

        private Builder(String cakeName, CakeVariantType type) {
            variant = new CakeVariant(cakeName, type);
        }

        public <T extends BaseCakeBlock> Builder cakeClass(CakeClazzSupplier<T> clazz) {
            cakeClazz = clazz;
            return this;
        }

        public <T extends BaseCandleCakeBlock> Builder candleCakeClass(CandleCakeClazzSupplier<T> clazz) {
            candleCakeClazz = clazz;
            return this;
        }

        public <T extends BaseCakeBlock, V extends BaseCandleCakeBlock> Builder bothClasses(CakeClazzSupplier<T> cakeClazz, CandleCakeClazzSupplier<V> candleCakeClazz) {
            this.cakeClazz = cakeClazz;
            this.candleCakeClazz = candleCakeClazz;
            return this;
        }

        public Builder cakeProperties(BlockBehaviour.Properties properties) {
            variant.cakeProperties = properties;
            return this;
        }

        public Builder candleCakeProperties(BlockBehaviour.Properties properties) {
            variant.candleCakeProperties = properties;
            return this;
        }

        public Builder alwaysEat() {
            variant.canAlwaysEat = true;
            return this;
        }

        public Builder disallowCandles() {
            variant.allowsCandles = false;
            return this;
        }

        public Builder noItem() {
            variant.noItem = true;
            return this;
        }

        public Builder customItemModel() {
            variant.customItemModel = true;
            return this;
        }

        public Builder nutrition(int nutrition) {
            variant.nutrition = nutrition;
            return this;
        }

        public Builder saturationModifier(float modifier) {
            variant.saturationModifier = modifier;
            return this;
        }

        public Builder model(CakeModel cakeModel) {
            return models(cakeModel, CakeModel.DEFAULT);
        }

        public Builder models(CakeModel cakeModel, CakeModel candleCakeModel) {
            variant.cakeModel = cakeModel;
            variant.candleCakeModel = candleCakeModel;
            return this;
        }

        Builder setFamily(CakeFamily family) {
            variant.family = family;
            return this;
        }

        public CakeVariant build() {
            if (cakeClazz == null) {
                cakeClazz = switch (variant.type) {
                    case BASE -> BaseCakeBlock::new;
                    case TWO_TIERED -> BaseTwoTieredCakeBlock::new;
                    case THREE_TIERED -> BaseThreeTieredCakeBlock::new;
                };
            }

            if (variant.cakeProperties == null) {
                variant.cakeProperties = ModBlocks.cakeProperties();
            }

            Supplier<BaseCakeBlock> cake = Services.REGISTRY.registerBlockNoItem(variant.cakeName, () -> cakeClazz.get(variant));

            if (variant.hasItem()) {
                variant.item = Services.REGISTRY.registerItem(variant.cakeName, () -> new BlockItem(cake.get(), new Item.Properties()));
            }

            if (variant.allowsCandles) {
                if (candleCakeClazz == null) {
                    candleCakeClazz = switch (variant.type) {
                        case BASE -> BaseCandleCakeBlock::new;
                        case TWO_TIERED -> BaseTwoTieredCandleCakeBlock::new;
                        case THREE_TIERED -> BaseThreeTieredCandleCakeBlock::new;
                    };
                }

                if (variant.candleCakeProperties == null) {
                    variant.candleCakeProperties = ModBlocks.candleCakeProperties();
                }

                for (Block candle : SUPPORTED_CANDLES.keySet()) {
                    String type = SUPPORTED_CANDLES.get(candle).getPath();
                    Supplier<BaseCandleCakeBlock> candleCake = Services.REGISTRY.registerBlockNoItem(type + "candle_" + variant.cakeName, () -> candleCakeClazz.get(cake.get(), candle, variant.candleCakeProperties));
                    variant.candleCakeByCandle.put(candle, candleCake);
                }
            }

            variant.cake = cake;
            VARIANT_BY_CAKE.put(cake, variant);
            return variant;
        }
    }

    @FunctionalInterface
    public interface CakeClazzSupplier<T extends BaseCakeBlock> {

        T get(CakeVariant variant);
    }

    @FunctionalInterface
    public interface CandleCakeClazzSupplier<T extends BaseCandleCakeBlock> {

        T get(BaseCakeBlock originalCake, Block candle, BlockBehaviour.Properties properties);
    }
}
