package einstein.jmc.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModServerConfigs {
	
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	public static final ForgeConfigSpec.IntValue FIREY_CAKE_ON_FIRE_DUR = registerPotionDur("fireyCakeOnFireDur", 20);
	public static final ForgeConfigSpec.IntValue GLOWSTONE_CAKE_GLOWING_DUR = registerPotionDur("glowstoneCakeGlowingDur", 85);
	public static final ForgeConfigSpec.DoubleValue CHORUS_CAKE_TELEPORT_RADIUS = BUILDER.comment("The radius in which the player will be randomly teleported").defineInRange("chorusCakeTeleportRadius", 10, 0, 10000D);
	public static final ForgeConfigSpec.DoubleValue ENDER_CAKE_TELEPORT_RADIUS = BUILDER.comment("The radius in which the player will be randomly teleported").defineInRange("enderCakeTeleportRadius", 50, 0, 10000D);
	public static final ForgeConfigSpec.BooleanValue EFFECTED_BY_REDSTONE = BUILDER.comment("Whether the TNT cake will explode when powered by Redstone").define("effectedByRedstone", false);
	public static final ForgeConfigSpec.IntValue TNT_CAKE_EXPLOSION_SIZE = BUILDER.comment("How big the explosion for TNT cake is").defineInRange("TNTCakeExplosionSize", 5, 1, 50);

	public static final ForgeConfigSpec SPEC = BUILDER.build();
	
	private static ForgeConfigSpec.IntValue registerPotionDur(final String field, final int defaultInt) {
		return BUILDER.defineInRange(field, defaultInt, 0, 32000);
	}
}