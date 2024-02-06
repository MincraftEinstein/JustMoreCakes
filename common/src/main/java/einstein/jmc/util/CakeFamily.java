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

public abstract class CakeFamily implements CakeEffectsHolder {

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
    protected boolean canAlwaysEat;
    protected CakeModel cakeModel = CakeModel.DEFAULT;
    protected CakeModel candleCakeModel = CakeModel.DEFAULT;
    @Nullable
    protected CakeEffects cakeEffects;

    public CakeFamily(ResourceLocation registryKey, String baseCakeName) {
        this.registryKey = registryKey;
        this.baseCakeName = baseCakeName;

        baseBuilder = new CakeBuilder(baseCakeName).setFamily(this);
        twoTieredBuilder = new CakeBuilder("two_tiered_" + baseCakeName, CakeVariant.TWO_TIERED).setFamily(this);
        threeTieredBuilder = new CakeBuilder("three_tiered_" + baseCakeName, CakeVariant.THREE_TIERED).setFamily(this);
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

    public CakeModel getCakeModel() {
        return cakeModel;
    }

    public CakeModel getCandleCakeModel() {
        return candleCakeModel;
    }

    public void forEach(Consumer<Supplier<BaseCakeBlock>> consumer) {
        consumer.accept(baseCake);
        consumer.accept(twoTieredCake);
        consumer.accept(threeTieredCake);
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
}
