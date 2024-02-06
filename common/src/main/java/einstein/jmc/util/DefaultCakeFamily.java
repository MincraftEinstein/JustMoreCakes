package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;

public class DefaultCakeFamily extends CakeFamily {

    DefaultCakeFamily(String flavorName, boolean noSuffix) {
        super(loc(flavorName), flavorName + (noSuffix ? "" : "_cake"));
    }

    public static Builder create(String flavorName) {
        return create(flavorName, false);
    }

    public static Builder create(String flavorName, boolean noSuffix) {
        return new Builder(flavorName, noSuffix);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Supplier<BaseCakeBlock> getBaseCake() {
        return (Supplier<BaseCakeBlock>) super.getBaseCake();
    }

    public static class Builder {

        private final DefaultCakeFamily family;
        private BlockBehaviour.Properties cakeProperties;
        private BlockBehaviour.Properties candleCakeProperties;

        private Builder(String flavorName, boolean noSuffix) {
            family = new DefaultCakeFamily(flavorName, noSuffix);
        }

        public Builder modifyBaseBuilder(Consumer<CakeBuilder> consumer) {
            consumer.accept(family.baseBuilder);
            return this;
        }

        public Builder modifyTwoTieredBuilder(Consumer<CakeBuilder> consumer) {
            consumer.accept(family.twoTieredBuilder);
            return this;
        }

        public Builder modifyThreeTieredBuilder(Consumer<CakeBuilder> consumer) {
            consumer.accept(family.threeTieredBuilder);
            return this;
        }

        public Builder cakeProperties(BlockBehaviour.Properties properties) {
            cakeProperties = properties;
            return this;
        }

        public Builder candleCakeProperties(BlockBehaviour.Properties properties) {
            candleCakeProperties = properties;
            return this;
        }

        public Builder nutrition(int nutrition) {
            family.nutrition = nutrition;
            return this;
        }

        public Builder saturationModifier(float modifier) {
            family.saturationModifier = modifier;
            return this;
        }

        public Builder alwaysEat() {
            family.canAlwaysEat = true;
            return this;
        }

        public Builder model(CakeModel model) {
            return models(model, CakeModel.DEFAULT);
        }

        public Builder models(CakeModel cakeModel, CakeModel candleCakeModel) {
            family.cakeModel = cakeModel;
            family.candleCakeModel = candleCakeModel;
            return this;
        }

        public DefaultCakeFamily build() {
            if (family.canAlwaysEat) {
                family.baseBuilder.alwaysEat();
                family.twoTieredBuilder.alwaysEat();
                family.threeTieredBuilder.alwaysEat();
            }

            family.baseCake = family.baseBuilder
                    .setCakeProperties(cakeProperties)
                    .setCandleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();
            family.twoTieredCake = family.twoTieredBuilder
                    .noItem()
                    .setCakeProperties(cakeProperties)
                    .setCandleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();
            family.threeTieredCake = family.threeTieredBuilder
                    .noItem()
                    .setCakeProperties(cakeProperties)
                    .setCandleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();
            REGISTERED_CAKE_FAMILIES.put(family.getRegistryKey(), family);
            return family;
        }
    }
}
