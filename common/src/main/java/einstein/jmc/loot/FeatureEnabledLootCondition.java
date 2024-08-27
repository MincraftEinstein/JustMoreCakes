package einstein.jmc.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import einstein.jmc.init.ModLootConditionTypes;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record FeatureEnabledLootCondition(FeatureFlagSet set) implements LootItemCondition {

    public static final MapCodec<FeatureEnabledLootCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            FeatureFlags.CODEC.fieldOf("features").forGetter(FeatureEnabledLootCondition::set)
    ).apply(instance, FeatureEnabledLootCondition::new));

    public static LootItemCondition.Builder create(FeatureFlagSet set) {
        return () -> new FeatureEnabledLootCondition(set);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootConditionTypes.FEATURE_ENABLED.get();
    }

    @Override
    public boolean test(LootContext context) {
        return set().isSubsetOf(context.getLevel().enabledFeatures());
    }
}
