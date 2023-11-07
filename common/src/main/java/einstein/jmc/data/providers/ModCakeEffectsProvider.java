package einstein.jmc.data.providers;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.data.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.init.ModPotions;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffects;

public class ModCakeEffectsProvider extends CakeEffectsProvider {

    public ModCakeEffectsProvider(PackOutput output) {
        super(output, JustMoreCakes.MOD_ID);
    }

    @Override
    protected void addCakeEffects() {
        add(ModBlocks.POISON_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.POISON, 300, 1));
        add(ModBlocks.GOLDEN_APPLE_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.REGENERATION, 200, 1),
                new CakeEffects.MobEffectHolder(MobEffects.DAMAGE_RESISTANCE, 3000),
                new CakeEffects.MobEffectHolder(MobEffects.ABSORPTION, 1200, 1));
        add(ModBlocks.FIREY_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.FIRE_RESISTANCE, 300, 1));
        add(ModBlocks.SLIME_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.JUMP, 1200, 0),
                new CakeEffects.MobEffectHolder(MobEffects.DAMAGE_RESISTANCE, 1200),
                new CakeEffects.MobEffectHolder(ModPotions.BOUNCING_EFFECT.get(), 1200));
        add(ModBlocks.BEETROOT_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.REGENERATION, 100, 1));
        add(ModBlocks.LAVA_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.REGENERATION, 300, 1),
                new CakeEffects.MobEffectHolder(MobEffects.DAMAGE_BOOST, 300, 1));
        add(ModBlocks.ICE_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.NIGHT_VISION, 2400),
                new CakeEffects.MobEffectHolder(ModPotions.FREEZING_EFFECT.get()));
        add(ModBlocks.CHORUS_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.LEVITATION, 300, 1));
        add(ModBlocks.GLOW_BERRY_CAKE,
                new CakeEffects.MobEffectHolder(MobEffects.NIGHT_VISION, 1200),
                new CakeEffects.MobEffectHolder(MobEffects.GLOWING, 1800));
        add(ModBlocks.SCULK_CAKE,
                new CakeEffects.MobEffectHolder(ModPotions.STEALTH_EFFECT.get(), 600));
    }

    @Override
    public String getName() {
        return "JustMoreCakes cake effects";
    }
}
