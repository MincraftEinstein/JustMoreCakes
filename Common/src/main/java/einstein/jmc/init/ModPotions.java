package einstein.jmc.init;

import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.platform.Services;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ModPotions {

    public static final Supplier<MobEffect> BOUNCING_EFFECT = Services.REGISTRY.registerMobEffect("bouncing", () -> new MobEffect(MobEffectCategory.NEUTRAL, 7056483));
    public static final Supplier<MobEffect> FREEZING_EFFECT = Services.REGISTRY.registerMobEffect("freezing", () -> new FreezingEffect(MobEffectCategory.NEUTRAL, 12114935));
    public static final Supplier<MobEffect> STEALTH_EFFECT = Services.REGISTRY.registerMobEffect("stealth", () -> new MobEffect(MobEffectCategory.BENEFICIAL, 556673));

    public static final Supplier<Potion> BOUNCING_POTION = Services.REGISTRY.registerPotion("bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 2400)));
    public static final Supplier<Potion> LONG_BOUNCING_POTION = Services.REGISTRY.registerPotion("long_bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 4800)));
    public static final Supplier<Potion> FREEZING_POTION = Services.REGISTRY.registerPotion("freezing", () -> new Potion(new MobEffectInstance(FREEZING_EFFECT.get())));
    public static final Supplier<Potion> STEALTH_POTION = Services.REGISTRY.registerPotion("stealth", () -> new Potion(new MobEffectInstance(STEALTH_EFFECT.get(), 600)));

    public static void registerPotionRecipes() {
        Services.HOOKS.registerBrewingRecipe(Potions.AWKWARD, Ingredient.of(Items.SLIME_BALL), ModPotions.BOUNCING_POTION.get());
        Services.HOOKS.registerBrewingRecipe(BOUNCING_POTION.get(), Ingredient.of(Items.REDSTONE), LONG_BOUNCING_POTION.get());
        Services.HOOKS.registerBrewingRecipe(Potions.AWKWARD, Ingredient.of(Items.PACKED_ICE), FREEZING_POTION.get());
        Services.HOOKS.registerBrewingRecipe(Potions.SLOWNESS, Ingredient.of(Items.ECHO_SHARD), STEALTH_POTION.get());
    }

    public static void init() {
    }
}
