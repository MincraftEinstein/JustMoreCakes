package einstein.jmc.init;

import einstein.jmc.loot.FeatureEnabledLootCondition;
import einstein.jmc.platform.Services;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.Supplier;

public class ModLootConditionTypes {

    public static final Supplier<LootItemConditionType> FEATURE_ENABLED = Services.REGISTRY.registerLootConditionType("feature_enabled", FeatureEnabledLootCondition.CODEC);

    public static void init() {
    }
}
