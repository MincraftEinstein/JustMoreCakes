package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

import static einstein.jmc.init.ModCommonConfigs.categoryKey;
import static einstein.jmc.init.ModCommonConfigs.key;

public class ModServerConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static {
        BUILDER.comment("Configs relating to interactions with the Jade mod")
                .translation(categoryKey("jade_plugin"))
                .push("Jade Plugin");
    }

    public static final ForgeConfigSpec.BooleanValue ALLOW_DISPLAYING_CAKE_EFFECTS = BUILDER
            .comment("Allows Jade to display the effects a cake will apply when eaten",
                    "If false this will override the value of the client's plugin equivalent")
            .translation(key("jade.allow_displaying_cake_effects"))
            .define("allowDisplayingCakeEffects", true);

    public static final ForgeConfigSpec.BooleanValue HIDE_TRAPPED_CAKES = BUILDER
            .comment("Hide trapped cakes in Jade by making them display as normal cakes",
                    "If true this will override the value of the client's plugin equivalent")
            .translation(key("jade.hide_trapped_cakes"))
            .define("hideTrappedCakes", false);

    static {
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
