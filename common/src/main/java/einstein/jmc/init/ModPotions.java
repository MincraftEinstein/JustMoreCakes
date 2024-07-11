package einstein.jmc.init;

import einstein.jmc.effect.CustomMobEffect;
import einstein.jmc.effect.FreezingEffect;
import einstein.jmc.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;

import java.util.function.Supplier;

public class ModPotions {

    public static final Supplier<Holder<MobEffect>> BOUNCING_EFFECT = Services.REGISTRY.registerMobEffect("bouncing", () -> new CustomMobEffect(MobEffectCategory.NEUTRAL, 7056483));
    public static final Supplier<Holder<MobEffect>> FREEZING_EFFECT = Services.REGISTRY.registerMobEffect("freezing", () -> new FreezingEffect(MobEffectCategory.NEUTRAL, 12114935));
    public static final Supplier<Holder<MobEffect>> STEALTH_EFFECT = Services.REGISTRY.registerMobEffect("stealth", () -> new CustomMobEffect(MobEffectCategory.BENEFICIAL, 556673));

    public static final Supplier<Holder<Potion>> BOUNCING_POTION = Services.REGISTRY.registerPotion("bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 2400)));
    public static final Supplier<Holder<Potion>> LONG_BOUNCING_POTION = Services.REGISTRY.registerPotion("long_bouncing", () -> new Potion(new MobEffectInstance(BOUNCING_EFFECT.get(), 4800)));
    public static final Supplier<Holder<Potion>> FREEZING_POTION = Services.REGISTRY.registerPotion("freezing", () -> new Potion(new MobEffectInstance(FREEZING_EFFECT.get())));
    public static final Supplier<Holder<Potion>> STEALTH_POTION = Services.REGISTRY.registerPotion("stealth", () -> new Potion(new MobEffectInstance(STEALTH_EFFECT.get(), 600)));

    public static void registerPotionRecipes(PotionBrewing.Builder builder) {
        builder.addStartMix(Items.SLIME_BALL, ModPotions.BOUNCING_POTION.get());
        builder.addMix(BOUNCING_POTION.get(), Items.REDSTONE, LONG_BOUNCING_POTION.get());
        builder.addStartMix(Items.PACKED_ICE, FREEZING_POTION.get());
        builder.addMix(Potions.SLOWNESS, Items.ECHO_SHARD, STEALTH_POTION.get());
    }

    public static void init() {
    }
}
