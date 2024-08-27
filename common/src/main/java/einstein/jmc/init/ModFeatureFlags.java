package einstein.jmc.init;

import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagRegistry;

import static einstein.jmc.JustMoreCakes.loc;

public class ModFeatureFlags {

    public static FeatureFlag FD_SUPPORT;

    public static void init(FeatureFlagRegistry.Builder builder) {
        FD_SUPPORT = builder.create(loc("fd_support"));
    }
}
