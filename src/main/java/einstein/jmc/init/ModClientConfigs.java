package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModClientConfigs {
	
	public static ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder().comment("Particle effects emitted from cakes").push("Cake particles");
	
	public static final ForgeConfigSpec.BooleanValue REDSTONE_CAKE_PARTICLES = CLIENT_BUILDER.define("redstoneCakeParticles", true);
	public static final ForgeConfigSpec.BooleanValue ENDER_CAKE_PARTICLES = CLIENT_BUILDER.define("enderCakeParticles", true);
	public static final ForgeConfigSpec.BooleanValue LAVA_CAKE_PARTICLES = CLIENT_BUILDER.define("lavaCakeParticles", false);
	
	public static final ForgeConfigSpec CLIENTSPEC = CLIENT_BUILDER.build();
}