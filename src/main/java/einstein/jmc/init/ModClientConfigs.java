package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs {
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder().comment("Particle effects emitted from cakes").push("Cake particles");
	
	public static final ForgeConfigSpec.BooleanValue REDSTONE_CAKE_PARTICLES = BUILDER.define("redstoneCakeParticles", true);
	public static final ForgeConfigSpec.BooleanValue ENDER_CAKE_PARTICLES = BUILDER.define("enderCakeParticles", true);
	public static final ForgeConfigSpec.BooleanValue LAVA_CAKE_PARTICLES = BUILDER.define("lavaCakeParticles", false);

	public static final ForgeConfigSpec SPEC = BUILDER.build();
}