package einstein.jmc.data.packs.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.CakeEffectsProvider;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModPotions;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.concurrent.CompletableFuture;

public class ModCakeEffectsProvider extends CakeEffectsProvider {

    public ModCakeEffectsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, JustMoreCakes.MOD_ID, lookupProvider);
    }

    @Override
    protected void generate() {
        add(ModBlocks.POISON_CAKE_VARIANT.getCake(),
                new MobEffectInstance(MobEffects.POISON, 300, 1));
        add(ModBlocks.GOLDEN_APPLE_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.REGENERATION, 200, 1),
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000),
                new MobEffectInstance(MobEffects.ABSORPTION, 1200, 1));
        add(ModBlocks.FIREY_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 1));
        add(ModBlocks.SLIME_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.JUMP, 1200, 0),
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1200),
                new MobEffectInstance(ModPotions.BOUNCING_EFFECT.get(), 1200));
        add(ModBlocks.BEETROOT_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.REGENERATION, 100, 1));
        add(ModBlocks.LAVA_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.REGENERATION, 300, 1),
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1));
        add(ModBlocks.ICE_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.NIGHT_VISION, 2400),
                new MobEffectInstance(ModPotions.FREEZING_EFFECT.get()));
        add(ModBlocks.CHORUS_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.LEVITATION, 300, 1));
        add(ModBlocks.GLOW_BERRY_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.NIGHT_VISION, 1200),
                new MobEffectInstance(MobEffects.GLOWING, 1800));
        add(ModBlocks.SCULK_CAKE_FAMILY,
                new MobEffectInstance(ModPotions.STEALTH_EFFECT.get(), 600));
        add(ModBlocks.CREEPER_CAKE_FAMILY,
                new MobEffectInstance(MobEffects.LUCK, 600, 1),
                new MobEffectInstance(MobEffects.UNLUCK, 600, 1));
    }
}
