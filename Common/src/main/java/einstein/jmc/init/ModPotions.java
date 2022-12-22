package einstein.jmc.init;

import einstein.jmc.effects.BouncingEffect;
import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.platform.Services;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import java.util.function.Supplier;

public class ModPotions {

    public static final Supplier<MobEffect> BOUNCING_EFFECT = Services.REGISTRY.registerMobEffect("bouncing", () -> new BouncingEffect(MobEffectCategory.NEUTRAL, 7056483));
    public static final Supplier<MobEffect> FREEZING_EFFECT = Services.REGISTRY.registerMobEffect("freezing", () -> new FreezingEffect(MobEffectCategory.NEUTRAL, 12114935));

    public static final Supplier<Potion> BOUNCING_POTION = Services.REGISTRY.registerPotion("bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 2400)));
    public static final Supplier<Potion> LONG_BOUNCING_POTION = Services.REGISTRY.registerPotion("long_bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 4800)));
    public static final Supplier<Potion> FREEZING_POTION = Services.REGISTRY.registerPotion("freezing", () -> new Potion(new MobEffectInstance(FREEZING_EFFECT.get())));

    public static void registerPotionRecipes() {
        PotionBrewing.addMix(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.BOUNCING_POTION.get());
        PotionBrewing.addMix(ModPotions.BOUNCING_POTION.get(), Items.REDSTONE, ModPotions.LONG_BOUNCING_POTION.get());
        PotionBrewing.addMix(Potions.AWKWARD, Items.PACKED_ICE, ModPotions.FREEZING_POTION.get());
    }

    public static void init() {}
}
