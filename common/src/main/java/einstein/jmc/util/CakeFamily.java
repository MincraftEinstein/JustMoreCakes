package einstein.jmc.util;

import einstein.jmc.block.cake.BaseCakeBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CakeFamily {

    public static final List<CakeFamily> REGISTERED_CAKE_FAMILIES = new ArrayList<>();

    private final String flavorName;
    private final CakeBuilder baseBuilder;
    private final CakeBuilder twoTieredBuilder;
    private final CakeBuilder threeTieredBuilder;
    private Supplier<BaseCakeBlock> baseCake;
    private Supplier<BaseCakeBlock> twoTieredCake;
    private Supplier<BaseCakeBlock> threeTieredCake;
    private int nutrition = BaseCakeBlock.DEFAULT_NUTRITION;
    private float saturationModifier = BaseCakeBlock.DEFAULT_SATURATION_MODIFIER;

    private CakeFamily(String flavorName) {
        this.flavorName = flavorName;

        String cakeName = flavorName + "_cake";
        baseBuilder = new CakeBuilder(cakeName);
        twoTieredBuilder = new CakeBuilder("two_tiered_" + cakeName);
        threeTieredBuilder = new CakeBuilder("three_tiered_" + cakeName);
    }

    public static Builder create(String flavorName) {
        return new Builder(flavorName);
    }

    public String getFlavorName() {
        return flavorName;
    }

    public CakeBuilder getBaseBuilder() {
        return baseBuilder;
    }

    public CakeBuilder getTwoTieredBuilder() {
        return twoTieredBuilder;
    }

    public CakeBuilder getThreeTieredBuilder() {
        return threeTieredBuilder;
    }

    public Supplier<BaseCakeBlock> getBaseCake() {
        return baseCake;
    }

    public Supplier<BaseCakeBlock> getTwoTieredCake() {
        return twoTieredCake;
    }

    public Supplier<BaseCakeBlock> getThreeTieredCake() {
        return threeTieredCake;
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public static class Builder {

        private final CakeFamily family;

        private Builder(String flavorName) {
            family = new CakeFamily(flavorName);
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

        public Builder nutrition(int nutrition) {
            family.nutrition = nutrition;
            return this;
        }

        public Builder saturationModifier(float modifier) {
            family.saturationModifier = modifier;
            return this;
        }

        public CakeFamily build() {
            family.baseCake = family.baseBuilder
                    .setFamily(family)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .build();

            family.twoTieredCake = family.twoTieredBuilder
                    .setFamily(family)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .build();

            family.threeTieredCake = family.threeTieredBuilder
                    .setFamily(family)
                    .nutrition(family.nutrition)
                    .saturationModifier(family.saturationModifier)
                    .build();

            REGISTERED_CAKE_FAMILIES.add(family);
            return family;
        }
    }
}
