package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue CAKE_BAKERY_GENERATION_WEIGHT = BUILDER.comment("How often do cake bakeries generate in villages")
            .defineInRange("cakeBakeryGenerationWeight", 1, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
