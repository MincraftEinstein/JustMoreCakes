package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue FIREY_CAKE_ON_FIRE_DUR = BUILDER
            .comment("How long the cake eater is set on fire for in ticks")
            .translation(key("firey_cake_fire_duration"))
            .defineInRange("fireyCakeOnFireDuration", 20, 0, 32000);
    public static final ForgeConfigSpec.IntValue GLOWSTONE_CAKE_GLOWING_DUR = BUILDER
            .comment("How long mobs affected by the glowstone cake glow for in ticks")
            .translation(key("glowstone_cake_glowing_duration"))
            .defineInRange("glowstoneCakeGlowingDuration", 85, 0, 32000);
    public static final ForgeConfigSpec.DoubleValue CHORUS_CAKE_TELEPORT_RADIUS = BUILDER
            .comment("The radius around the player in which the they will be randomly teleported")
            .translation(key("chorus_cake_teleport_radius"))
            .defineInRange("chorusCakeTeleportRadius", 10, 0, 10000D);
    public static final ForgeConfigSpec.DoubleValue ENDER_CAKE_TELEPORT_RADIUS = BUILDER
            .comment("The radius around the player in which the they will be randomly teleported")
            .translation(key("ender_cake_teleport_radius"))
            .defineInRange("enderCakeTeleportRadius", 50, 0, 10000D);
    public static final ForgeConfigSpec.BooleanValue EFFECTED_BY_REDSTONE = BUILDER
            .comment("Whether the TNT cake will explode when powered by Redstone")
            .translation(key("effected_by_redstone"))
            .define("effectedByRedstone", false);
    public static final ForgeConfigSpec.IntValue TNT_CAKE_EXPLOSION_SIZE = BUILDER
            .comment("How big the explosion for TNT cake is")
            .translation(key("tnt_cake_explosion_size"))
            .defineInRange("tntCakeExplosionSize", 5, 1, 50);
    public static final ForgeConfigSpec.IntValue ENCASING_ICE_MELT_SPEED = BUILDER
            .comment("How long it takes for encasing ice to melt. Larger numbers is slower")
            .translation(key("encasing_ice_melt_speed"))
            .defineInRange("encasingIceMeltSpeed", 1, 0, Integer.MAX_VALUE);
    public static final ForgeConfigSpec.IntValue CAKE_BAKERY_GENERATION_WEIGHT = BUILDER
            .comment("How often do cake bakeries generate in villages. 0 to disable")
            .translation(key("cake_bakery_generation_weight"))
            .defineInRange("cakeBakeryGenerationWeight", 1, 0, 150);
    public static final ForgeConfigSpec.IntValue GLOWSTONE_CAKE_EFFECT_RADIUS = BUILDER
            .comment("The radius in blocks at which the glowstone cake will effect mobs")
            .translation(key("glowstone_cake_effect_radius"))
            .defineInRange("glowstoneCakeEffectRadius", 20, 0, 100);
    public static final ForgeConfigSpec.BooleanValue DISABLE_DEFAULT_CAKE_RECIPE = BUILDER
            .comment("Disables the game's default cake crafting recipe", "Requires resources to be reloaded")
            .translation(key("disable_cake_crafting_recipe"))
            .define("disableCakeCraftingRecipe", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String key(String name) {
        return "config." + JustMoreCakes.MOD_ID + "." + name;
    }

    public static String categoryKey(String name) {
        return "config.category" + JustMoreCakes.MOD_ID + "." + name;
    }
}
