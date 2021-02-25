package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModConfigs {

	public static void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ModServerConfigs.SERVERSPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModClientConfigs.CLIENTSPEC);
	}
	
	/******************* SERVER CONFIG *******************/
	public static class ModServerConfigs {
		
		public static final ForgeConfigSpec.Builder SERVER_BUILDER = new ForgeConfigSpec.Builder().comment("Server configuration settings").push("server");
		
		// Poison Cake
		public static final ForgeConfigSpec.IntValue POISON_CAKE_POISON_DURATION = registerPotionDur("The duration of the poison potion effect", "poisonCakePoisonDur", 300);
		public static final ForgeConfigSpec.IntValue POISON_CAKE_POISON_STRENGTH = registerPotionDur("The strength of the poison potion effect", "poisonCakePoisonStrength", 1);

		// TNT Cake
		public static final ForgeConfigSpec.BooleanValue EFFECTED_BY_REDSTONE = SERVER_BUILDER.comment("Whether the TNT cake will explode when powered by Redstone").define("effectedByRedstone", false);
		
		// Golden Apple Cake
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_REGEN_DUR = registerPotionDur("The duration of the regeneration potion effect", "goldenAppleCakeRegenDur", 200);
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_REGEN_STRENGTH = registerPotionDur("The strength of the regeneration potion effect", "goldenAppleCakeRegenStrength", 1);
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_RES_DUR = registerPotionDur("The duration of the resistance potion effect", "goldenAppleCakeResDur", 3000);
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_RES_STRENGTH = registerPotionDur("The strength of the resistance potion effect", "goldenAppleCakeResStrength", 0);
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_ABSORPTION_DUR = registerPotionDur("The duration of the absorption potion effect", "goldenAppleCakeAbsorptionDur", 1200);
		public static final ForgeConfigSpec.IntValue GAPPLE_CAKE_ABSORPTION_STRENGTH = registerPotionDur("The strength of the absorption potion effect", "goldenAppleCakeAbsorptionStrength", 1);

		// Firey Cake
		public static final ForgeConfigSpec.IntValue FIREY_CAKE_FIRE_RES_DUR = registerPotionDur("The duration of the fire resistance potion effect", "fireyCakeFireResDur", 300);
		public static final ForgeConfigSpec.IntValue FIREY_CAKE_FIRE_RES_STRENGTH = registerPotionDur("The strength of the fire resistance potion effect", "fireyCakeFireResStrength", 1);
		public static final ForgeConfigSpec.IntValue FIREY_CAKE_ON_FIRE_DUR = registerPotionDur("The duration that the player is on fire for", "fireyCakeOnFireDur", 20);

		// Ender Cake
		public static final ForgeConfigSpec.DoubleValue ENDER_CAKE_TELEPORT_RADIUS = registerDoubleValues("The radius in which the player will be randomly teleported", "enderCakeTeleportRadius", 50, 0, 10000);

		// Slime Cake
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_JUMP_BOOST_DUR = registerPotionDur("The duration of the jump boost potion effect", "slimeCakeJumpBoostDur", 1200);
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_JUMP_BOOST_STRENGTH = registerPotionDur("The strength of the jump boost potion effect", "slimeCakeJumpBoostStrength", 0);
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_RES_DUR = registerPotionDur("The duration of the resistance potion effect", "slimeCakeResDur", 1200);
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_RES_STRENGTH = registerPotionDur("The strength of the resistance potion effect", "slimeCakeResStrength", 0);
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_BOUNCING_DUR = registerPotionDur("The duration of the bouncing potion effect", "slimeCakeBouncingDur", 1200);
		public static final ForgeConfigSpec.IntValue SLIME_CAKE_BOUNCING_STRENGTH = registerPotionDur("The strength of the bouncing potion effect", "slimeCakeBouncingStrength", 0);

		// Beetroot Cake
		public static final ForgeConfigSpec.IntValue BEETROOT_CAKE_REGEN_DUR = registerPotionDur("The duration of the regeneration potion effect", "beetrootCakeRegenDur", 100);
		public static final ForgeConfigSpec.IntValue BEETROOT_CAKE_REGEN_STRENGTH = registerPotionStrength("The strength of the regeneration potion effect", "beetrootCakeRegenStrength", 1);

		// Lava Cake
		public static final ForgeConfigSpec.IntValue LAVA_CAKE_REGEN_DUR = registerPotionDur("The duration of the regeneration potion effect", "lavaCakeRegenDur", 300);
		public static final ForgeConfigSpec.IntValue LAVA_CAKE_REGEN_STRENGTH = registerPotionStrength("The strength of the regeneration potion effect", "lavaCakeRegenStrength", 1);
		public static final ForgeConfigSpec.IntValue LAVA_CAKE_STRENGTH_DUR = registerPotionDur("The duration of the strength potion effect", "lavaCakeStrengthDur", 300);
		public static final ForgeConfigSpec.IntValue LAVA_CAKE_STRENGTH_STRENGTH = registerPotionStrength("The strength of the strength potion effect", "lavaCakeStrengthStrength", 1);

		// Ice Cake
		public static final ForgeConfigSpec.IntValue ICE_CAKE_NIGHT_VISION_DUR = registerPotionDur("The duration of the night vision potion effect", "iceCakeNightVisionDur", 2400);
		public static final ForgeConfigSpec.IntValue ICE_CAKE_NIGHT_VISION_STRENGTH = registerPotionStrength("The strength of the night vision potion effect", "iceCakeNightVisionStrength", 0);

		// Chorus Cake
		public static final ForgeConfigSpec.IntValue CHORUS_CAKE_LEVITATION_DUR = registerPotionDur("The duration of the levitation potion effect", "chorusCakeLevitationDur", 300);
		public static final ForgeConfigSpec.IntValue CHORUS_CAKE_LEVITATION_STRENGTH = registerPotionStrength("The strength of the levitation potion effect", "chorusCakeLevitationStrength", 1);
		public static final ForgeConfigSpec.DoubleValue CHORUS_CAKE_TELEPORT_RADIUS = registerDoubleValues("The radius in which the player will be randomly teleported", "chorusCakeTeleportRadius", 10, 0, 10000);

		public static final ForgeConfigSpec.IntValue GLOWSTONE_CAKE_GLOWING_DUR = registerPotionDur("The duration of the glowing potion effect", "glowstoneCakeGlowingDur", 85);
		
		private static final ForgeConfigSpec SERVERSPEC = SERVER_BUILDER.build();

		public static IntValue registerPotionDur(final String comment, final String field, final int defaultInt) {
			return registerIntValues(comment, field, defaultInt, 0, 32000);
		}

		public static IntValue registerPotionStrength(final String comment, final String field, final int defaultInt) {
			return registerIntValues(comment, field, defaultInt, 0, 9);
		}

		public static IntValue registerIntValues(final String comment, final String field, final int defaultInt, final int minInt, final int maxInt) {
			return SERVER_BUILDER.comment(comment).defineInRange(field, defaultInt, minInt, maxInt);
		}

		public static DoubleValue registerDoubleValues(final String comment, final String field, final double defaultInt, final double minInt, final double maxInt) {
			return SERVER_BUILDER.comment(comment).defineInRange(field, defaultInt, minInt, maxInt);
		}
	}

	public static class ModClientConfigs {
		public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

		public static final ForgeConfigSpec.BooleanValue REDSTONE_CAKE_PARTICLES = CLIENT_BUILDER/*.comment("")*/.define("redstoneCakeParticles", true);
		public static final ForgeConfigSpec.BooleanValue ENDER_CAKE_PARTICLES = CLIENT_BUILDER.define("enderCakeParticles", true);
		public static final ForgeConfigSpec.BooleanValue LAVA_CAKE_PARTICLES = CLIENT_BUILDER.define("lavaCakeParticles", true);
		
		private static final ForgeConfigSpec CLIENTSPEC = CLIENT_BUILDER.build();
	}
}
