package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

import static einstein.jmc.init.ModCommonConfigs.key;

public class ModClientConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().comment("Particle effects emitted from cakes").push("Cake particles");

    public static final ForgeConfigSpec.BooleanValue REDSTONE_CAKE_PARTICLES = BUILDER
            .translation(key("config.jmc.redstone_cake_particles"))
            .define("redstoneCakeParticles", true);
    public static final ForgeConfigSpec.BooleanValue ENDER_CAKE_PARTICLES = BUILDER
            .translation(key("config.jmc.ender_cake_particles"))
            .define("enderCakeParticles", true);
    public static final ForgeConfigSpec.BooleanValue LAVA_CAKE_PARTICLES = BUILDER
            .translation(key("lava_cake_particles"))
            .define("lavaCakeParticles", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
