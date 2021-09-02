package einstein.jmc.init;

import einstein.einsteins_library.util.PropertyRegistry;
import einstein.einsteins_library.util.RegistryHandler;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.effects.BouncingEffect;
import einstein.jmc.effects.FreezingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Bus.MOD)
public class ModPotions {

	public static final MobEffect BOUNCING_EFFECT = RegistryHandler.registerMobEffect(JustMoreCakes.MODID, "bouncing", new BouncingEffect(MobEffectCategory.NEUTRAL, 7056483));
	public static final MobEffect FREEZING_EFFECT = RegistryHandler.registerMobEffect(JustMoreCakes.MODID, "freezing", new FreezingEffect(MobEffectCategory.NEUTRAL, 12114935));
	
	public static final Potion BOUNCING_POTION = RegistryHandler.registerPotion(JustMoreCakes.MODID, "bouncing", new Potion(new MobEffectInstance(BOUNCING_EFFECT, 2400)));
	public static final Potion LONG_BOUNCING_POTION = RegistryHandler.registerPotion(JustMoreCakes.MODID, "long_bouncing", new Potion(new MobEffectInstance(BOUNCING_EFFECT, 4800)));
	public static final Potion FREEZING_POTION = RegistryHandler.registerPotion(JustMoreCakes.MODID, "freezing", new Potion(new MobEffectInstance(FREEZING_EFFECT)));
	
	public static void registerPotionRecipes() {
		PropertyRegistry.registerPotionRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.BOUNCING_POTION);
		PropertyRegistry.registerPotionRecipe(ModPotions.BOUNCING_POTION, Items.REDSTONE, ModPotions.LONG_BOUNCING_POTION);
		PropertyRegistry.registerPotionRecipe(Potions.AWKWARD, Items.PACKED_ICE, ModPotions.FREEZING_POTION);
	}	
}