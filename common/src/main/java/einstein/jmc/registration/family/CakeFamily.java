package einstein.jmc.registration.family;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.data.CakeModel;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.registration.CakeVariant;
import einstein.jmc.util.CakeUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CakeFamily implements CakeEffectsHolder {

    public static final Map<ResourceLocation, CakeFamily> REGISTERED_CAKE_FAMILIES = new HashMap<>();
    public static final Codec<CakeFamily> CODEC = ResourceLocation.CODEC.flatXmap(key -> {
        if (REGISTERED_CAKE_FAMILIES.containsKey(key)) {
            return DataResult.success(REGISTERED_CAKE_FAMILIES.get(key));
        }
        return DataResult.error(() -> "Could not find cake family with registry key: {" + key + "}");
    }, family -> DataResult.success(family.getRegistryKey()));

    private final ResourceLocation registryKey;
    private final String baseCakeName;
    @Nullable
    protected CakeVariant baseVariant;
    protected CakeVariant twoTieredVariant;
    protected CakeVariant threeTieredVariant;
    protected Supplier<BaseCakeBlock> baseCake;
    protected Supplier<BaseCakeBlock> twoTieredCake;
    protected Supplier<BaseCakeBlock> threeTieredCake;
    protected int nutrition = CakeUtil.DEFAULT_NUTRITION;
    protected float saturationModifier = CakeUtil.DEFAULT_SATURATION_MODIFIER;
    protected boolean canAlwaysEat;
    protected CakeModel cakeModel = CakeModel.DEFAULT;
    protected CakeModel candleCakeModel = CakeModel.DEFAULT;
    @Nullable
    protected CakeEffects cakeEffects;

    public CakeFamily(ResourceLocation registryKey, String baseCakeName) {
        this.registryKey = registryKey;
        this.baseCakeName = baseCakeName;
    }

    public final ResourceLocation getRegistryKey() {
        return registryKey;
    }

    public String getBaseCakeName() {
        return baseCakeName;
    }

    @Nullable
    public CakeVariant getBaseVariant() {
        return baseVariant;
    }

    public CakeVariant getTwoTieredVariant() {
        return twoTieredVariant;
    }

    public CakeVariant getThreeTieredVariant() {
        return threeTieredVariant;
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

    public Supplier<Item> getBaseItem() {
        if (baseVariant != null) {
            return baseVariant.getItem();
        }
        return () -> Items.AIR;
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

    protected abstract static class Builder<T extends CakeFamily> {

        protected final T family;
        protected final CakeVariant.Builder baseVariantBuilder;
        protected final CakeVariant.Builder twoTieredVariantBuilder;
        protected final CakeVariant.Builder threeTieredVariantBuilder;

        protected Builder(T family) {
            this.family = family;
            baseVariantBuilder = CakeVariant.create(family.getBaseCakeName()).setFamily(family);
            twoTieredVariantBuilder = CakeVariant.create("two_tiered_" + family.getBaseCakeName(), CakeVariant.Type.TWO_TIERED).setFamily(family);
            threeTieredVariantBuilder = CakeVariant.create("three_tiered_" + family.getBaseCakeName(), CakeVariant.Type.THREE_TIERED).setFamily(family);
        }

        public T build() {
            if (family.baseVariant != null) {
                family.baseCake = family.baseVariant.getCake();
            }

            family.twoTieredCake = family.twoTieredVariant.getCake();
            family.threeTieredCake = family.threeTieredVariant.getCake();
            REGISTERED_CAKE_FAMILIES.put(family.getRegistryKey(), family);
            return family;
        }
    }
}
