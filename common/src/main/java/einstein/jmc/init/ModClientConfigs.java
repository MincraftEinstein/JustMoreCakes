package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

import static einstein.jmc.init.ModCommonConfigs.categoryKey;
import static einstein.jmc.init.ModCommonConfigs.key;

public class ModClientConfigs {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static {
        BUILDER.comment("Particle effects emitted from cakes")
                .translation(categoryKey("cake_particles"))
                .push("Cake Particles");
    }

    public static final ForgeConfigSpec.BooleanValue REDSTONE_CAKE_PARTICLES = BUILDER
            .translation(key("cake_particles.redstone_cake_particles"))
            .define("redstoneCakeParticles", true);
    public static final ForgeConfigSpec.BooleanValue ENDER_CAKE_PARTICLES = BUILDER
            .translation(key("cake_particles.ender_cake_particles"))
            .define("enderCakeParticles", true);
    public static final ForgeConfigSpec.BooleanValue LAVA_CAKE_PARTICLES = BUILDER
            .translation(key("cake_particles.lava_cake_particles"))
            .define("lavaCakeParticles", false);

    static {
        BUILDER.pop();
    }

    public static final ForgeConfigSpec SPEC = BUILDER.build();
}
