package einstein.jmc.init;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.effects.BouncingEffect;
import einstein.jmc.effects.FreezingEffect;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.Potions;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = JustMoreCakes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModPotions {
	
	public static final DeferredRegister<Effect> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, JustMoreCakes.MODID);
	public static final DeferredRegister<Potion> POTION_EFFECTS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, JustMoreCakes.MODID);

	public static final RegistryObject<Effect> BOUNCING_EFFECT = POTIONS.register("bouncing", () -> new BouncingEffect(EffectType.NEUTRAL, 7056483));//.addAttributesModifier(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 1.0D, Operation.ADDITION));
	public static final RegistryObject<Potion> BOUNCING_POTION = POTION_EFFECTS.register("bouncing", () -> new Potion(new EffectInstance(BOUNCING_EFFECT.get(), 2400)));
	public static final RegistryObject<Potion> LONG_BOUNCING_POTION = POTION_EFFECTS.register("long_bouncing", () -> new Potion(new EffectInstance(BOUNCING_EFFECT.get(), 4800)));
	public static final RegistryObject<Effect> FREEZING_EFFECT = POTIONS.register("freezing", () -> new FreezingEffect(EffectType.NEUTRAL, 12114935));
	public static final RegistryObject<Potion> FREEZING_POTION = POTION_EFFECTS.register("freezing", () -> new Potion(new EffectInstance(FREEZING_EFFECT.get())));
	
	public static void registerPotionRecipes() {
		PotionBrewing.addMix(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.BOUNCING_POTION.get());
		PotionBrewing.addMix(ModPotions.BOUNCING_POTION.get(), Items.REDSTONE, ModPotions.LONG_BOUNCING_POTION.get());
		PotionBrewing.addMix(Potions.AWKWARD, Blocks.PACKED_ICE.asItem(), ModPotions.FREEZING_POTION.get());
	}
	
}