package einstein.jmc.init;

import einstein.einsteins_library.util.PropertyRegistry;
import einstein.jmc.JustMoreCakes;
import einstein.jmc.effects.BouncingEffect;
import einstein.jmc.effects.FreezingEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
	
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, JustMoreCakes.MODID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, JustMoreCakes.MODID);

	public static final RegistryObject<MobEffect> BOUNCING_EFFECT = MOB_EFFECTS.register("bouncing", () -> new BouncingEffect(MobEffectCategory.NEUTRAL, 7056483));
	public static final RegistryObject<MobEffect> FREEZING_EFFECT = MOB_EFFECTS.register("freezing", () -> new FreezingEffect(MobEffectCategory.NEUTRAL, 12114935));
	
	public static final RegistryObject<Potion> BOUNCING_POTION = POTIONS.register("bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 2400)));
	public static final RegistryObject<Potion> LONG_BOUNCING_POTION = POTIONS.register("long_bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 4800)));
	public static final RegistryObject<Potion> FREEZING_POTION = POTIONS.register("freezing", () -> new Potion(new MobEffectInstance(FREEZING_EFFECT.get())));
	
	public static void registerPotionRecipes() {
		PropertyRegistry.registerPotionRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.BOUNCING_POTION.get());
		PropertyRegistry.registerPotionRecipe(ModPotions.BOUNCING_POTION.get(), Items.REDSTONE, ModPotions.LONG_BOUNCING_POTION.get());
		PropertyRegistry.registerPotionRecipe(Potions.AWKWARD, Items.PACKED_ICE, ModPotions.FREEZING_POTION.get());
	}	
}