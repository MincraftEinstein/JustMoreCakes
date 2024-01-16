package einstein.jmc.util;

import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.effects.CakeEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CakeFamily implements CakeEffectsHolder {

    public static final Map<ResourceLocation, CakeFamily> REGISTERED_CAKE_FAMILIES = new HashMap<>();

    private final ResourceLocation registryKey;
    private final String baseCakeName;
    @Nullable
    protected CakeBuilder baseBuilder;
    protected final CakeBuilder twoTieredBuilder;
    protected final CakeBuilder threeTieredBuilder;
    protected Supplier<BaseCakeBlock> baseCake;
    protected Supplier<BaseCakeBlock> twoTieredCake;
    protected Supplier<BaseCakeBlock> threeTieredCake;
    protected int nutrition = BaseCakeBlock.DEFAULT_NUTRITION;
    protected float saturationModifier = BaseCakeBlock.DEFAULT_SATURATION_MODIFIER;
    protected CakeModel model = CakeModel.DEFAULT;
    protected CakeEffects cakeEffects;

    CakeFamily(ResourceLocation registryKey, String baseCakeName) {
        this.registryKey = registryKey;
        this.baseCakeName = baseCakeName;

        baseBuilder = new CakeBuilder(baseCakeName)
                .setFamily(this)
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .models(model, model);

        twoTieredBuilder = new CakeBuilder("two_tiered_" + baseCakeName, CakeVariant.TWO_TIERED)
                .setFamily(this)
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .models(model, model);

        threeTieredBuilder = new CakeBuilder("three_tiered_" + baseCakeName, CakeVariant.THREE_TIERED)
                .setFamily(this)
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .models(model, model);
    }

    public static Builder create(String flavorName) {
        return new Builder(flavorName);
    }

    public final ResourceLocation getRegistryKey() {
        return registryKey;
    }

    public String getBaseCakeName() {
        return baseCakeName;
    }

    @Nullable
    public CakeBuilder getBaseBuilder() {
        return baseBuilder;
    }

    public CakeBuilder getTwoTieredBuilder() {
        return twoTieredBuilder;
    }

    public CakeBuilder getThreeTieredBuilder() {
        return threeTieredBuilder;
    }

    public Supplier<? extends Block> getBaseCake() {
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

    public CakeModel getModel() {
        return model;
    }

    @Nullable
    @Override
    public CakeEffects justMoreCakes$getCakeEffects() {
        return cakeEffects;
    }

    @Override
    public void justMoreCakes$setCakeEffects(@Nullable CakeEffects effects) {
        cakeEffects = effects;
    }

    @Override
    public String toString() {
        return "CakeFamily{" + getRegistryKey() + "}";
    }

    public static class Builder {

        private final DefaultCakeFamily family;

        private Builder(String flavorName) {
            family = new DefaultCakeFamily(flavorName);
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

        public Builder model(CakeModel model) {
            family.model = model;
            return this;
        }

        public DefaultCakeFamily build() {
            family.baseCake = family.baseBuilder.build();
            family.twoTieredCake = family.twoTieredBuilder.noItem().build();
            family.threeTieredCake = family.threeTieredBuilder.noItem().build();
            REGISTERED_CAKE_FAMILIES.put(family.getRegistryKey(), family);
            return family;
        }
    }
}
