package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static einstein.jmc.JustMoreCakes.loc;

public class DefaultCakeFamily extends CakeFamily {

    protected DefaultCakeFamily(String flavorName, boolean noSuffix) {
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

    public static class Builder extends CakeFamily.Builder<DefaultCakeFamily> {

        private BlockBehaviour.Properties cakeProperties;
        private BlockBehaviour.Properties candleCakeProperties;

        private Builder(String flavorName, boolean noSuffix) {
            super(new DefaultCakeFamily(flavorName, noSuffix));
        }

        public Builder modifyBaseBuilder(Consumer<CakeVariant.Builder> consumer) {
            consumer.accept(baseVariantBuilder);
            return this;
        }

        public Builder modifyTwoTieredBuilder(Consumer<CakeVariant.Builder> consumer) {
            consumer.accept(twoTieredVariantBuilder);
            return this;
        }

        public Builder modifyThreeTieredBuilder(Consumer<CakeVariant.Builder> consumer) {
            consumer.accept(threeTieredVariantBuilder);
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

        @Override
        public DefaultCakeFamily build() {
            if (family.canAlwaysEat) {
                baseVariantBuilder.alwaysEat();
                twoTieredVariantBuilder.alwaysEat();
                threeTieredVariantBuilder.alwaysEat();
            }

            family.baseVariant = baseVariantBuilder
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();
            family.twoTieredVariant = twoTieredVariantBuilder
                    .noItem()
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();
            family.threeTieredVariant = threeTieredVariantBuilder
                    .noItem()
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .build();

            return super.build();
        }
    }
}
