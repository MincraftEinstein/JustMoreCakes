package einstein.jmc.registration.family;

import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.CakeModel;
import einstein.jmc.init.ModFeatureFlags;
import einstein.jmc.item.CakeSliceItem;
import einstein.jmc.platform.Services;
import einstein.jmc.registration.CakeVariant;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Consumer;
import java.util.function.Function;
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
        private Function<FoodProperties.Builder, Item> sliceItem = builder -> new CakeSliceItem(new Item.Properties().food(builder.build()).requiredFeatures(ModFeatureFlags.FD_SUPPORT), family);

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

        public Builder sliceItem(Function<FoodProperties.Builder, Item> function) {
            sliceItem = function;
            return this;
        }

        @Override
        public DefaultCakeFamily build() {
            FoodProperties.Builder builder = new FoodProperties.Builder()
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier);

            if (family.canAlwaysEat) {
                builder.alwaysEdible();
                baseVariantBuilder.alwaysEat();
                twoTieredVariantBuilder.alwaysEat();
                threeTieredVariantBuilder.alwaysEat();
            }

            family.sliceItem = Services.REGISTRY.registerItem(family.getBaseCakeName() + "_slice",
                    () -> sliceItem.apply(builder));
            family.baseVariant = baseVariantBuilder
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .sliceItem(family.sliceItem)
                    .build();
            family.twoTieredVariant = twoTieredVariantBuilder
                    .noItem()
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .sliceItem(family.sliceItem)
                    .build();
            family.threeTieredVariant = threeTieredVariantBuilder
                    .noItem()
                    .cakeProperties(cakeProperties)
                    .candleCakeProperties(candleCakeProperties)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .models(family.cakeModel, family.candleCakeModel)
                    .sliceItem(family.sliceItem)
                    .build();

            return super.build();
        }
    }
}
