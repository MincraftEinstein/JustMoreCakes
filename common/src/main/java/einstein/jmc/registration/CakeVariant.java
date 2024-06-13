package einstein.jmc.registration;

import einstein.jmc.registration.family.CakeFamily;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseThreeTieredCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.block.cake.candle.BaseCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseThreeTieredCandleCakeBlock;
import einstein.jmc.block.cake.candle.BaseTwoTieredCandleCakeBlock;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.platform.Services;
import einstein.jmc.data.CakeModel;
import einstein.jmc.util.CakeUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CakeVariant {

    public static final Map<Supplier<BaseCakeBlock>, CakeVariant> VARIANT_BY_CAKE = new HashMap<>();

    private final String cakeName;
    private final Type type;
    private final Map<Block, Supplier<BaseCandleCakeBlock>> candleCakeByCandle = new HashMap<>();
    private boolean canAlwaysEat;
    private boolean allowsCandles = true;
    private boolean noItem = false;
    private boolean customItemModel;
    private int nutrition = CakeUtil.DEFAULT_NUTRITION;
    private float saturationModifier = CakeUtil.DEFAULT_SATURATION_MODIFIER;
    private BlockBehaviour.Properties cakeProperties;
    private BlockBehaviour.Properties candleCakeProperties;
    private CakeModel cakeModel = CakeModel.DEFAULT;
    private CakeModel candleCakeModel = CakeModel.DEFAULT;
    private CakeFamily family;
    private Supplier<BaseCakeBlock> cake;
    private Supplier<Item> item = () -> Items.AIR;

    private CakeVariant(String cakeName, Type type) {
        this.cakeName = cakeName;
        this.type = type;
    }

    public static Builder create(String cakeName) {
        return create(cakeName, Type.BASE);
    }

    public static Builder create(String cakeName, Type type) {
        return new Builder(cakeName, type);
    }

    public String getCakeName() {
        return cakeName;
    }

    public Type getType() {
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
        private CakeSupplier<?> cakeSupplier;
        private CandleCakeSupplier<?> candleCakeSupplier;

        private Builder(String cakeName, Type type) {
            variant = new CakeVariant(cakeName, type);
        }

        public <T extends BaseCakeBlock> Builder cakeSupplier(CakeSupplier<T> supplier) {
            cakeSupplier = supplier;
            return this;
        }

        public <T extends BaseCandleCakeBlock> Builder candleCakeSupplier(CandleCakeSupplier<T> supplier) {
            candleCakeSupplier = supplier;
            return this;
        }

        public <T extends BaseCakeBlock, V extends BaseCandleCakeBlock> Builder bothSuppliers(CakeSupplier<T> cakeSupplier, CandleCakeSupplier<V> candleCakeSupplier) {
            this.cakeSupplier = cakeSupplier;
            this.candleCakeSupplier = candleCakeSupplier;
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

        public Builder setFamily(CakeFamily family) {
            variant.family = family;
            return this;
        }

        public CakeVariant build() {
            if (cakeSupplier == null) {
                cakeSupplier = switch (variant.type) {
                    case BASE -> BaseCakeBlock::new;
                    case TWO_TIERED -> BaseTwoTieredCakeBlock::new;
                    case THREE_TIERED -> BaseThreeTieredCakeBlock::new;
                };
            }

            if (variant.cakeProperties == null) {
                variant.cakeProperties = ModBlocks.cakeProperties();
            }

            Supplier<BaseCakeBlock> cake = Services.REGISTRY.registerBlockNoItem(variant.cakeName, () -> cakeSupplier.create(variant));

            if (variant.hasItem()) {
                variant.item = Services.REGISTRY.registerItem(variant.cakeName, () -> new BlockItem(cake.get(), new Item.Properties()));
            }

            if (variant.allowsCandles) {
                if (candleCakeSupplier == null) {
                    candleCakeSupplier = switch (variant.type) {
                        case BASE -> BaseCandleCakeBlock::new;
                        case TWO_TIERED -> BaseTwoTieredCandleCakeBlock::new;
                        case THREE_TIERED -> BaseThreeTieredCandleCakeBlock::new;
                    };
                }

                if (variant.candleCakeProperties == null) {
                    variant.candleCakeProperties = ModBlocks.candleCakeProperties();
                }

                for (Block candle : CakeUtil.SUPPORTED_CANDLES.keySet()) {
                    String type = CakeUtil.SUPPORTED_CANDLES.get(candle).getPath();
                    Supplier<BaseCandleCakeBlock> candleCake = Services.REGISTRY.registerBlockNoItem(type + "candle_" + variant.cakeName, () -> candleCakeSupplier.create(cake.get(), candle, variant.candleCakeProperties));
                    variant.candleCakeByCandle.put(candle, candleCake);
                }
            }

            variant.cake = cake;
            VARIANT_BY_CAKE.put(cake, variant);
            return variant;
        }
    }

    @FunctionalInterface
    public interface CakeSupplier<T extends BaseCakeBlock> {

        T create(CakeVariant variant);
    }

    @FunctionalInterface
    public interface CandleCakeSupplier<T extends BaseCandleCakeBlock> {

        T create(BaseCakeBlock parentCake, Block candle, BlockBehaviour.Properties properties);
    }

    public enum Type {
        BASE,
        TWO_TIERED,
        THREE_TIERED
    }
}
