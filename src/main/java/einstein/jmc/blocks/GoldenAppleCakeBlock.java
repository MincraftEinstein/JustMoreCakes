package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class GoldenAppleCakeBlock extends BaseCakeBlock
{
    public GoldenAppleCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.GAPPLE_CAKE_REGEN_DUR.get(), ModServerConfigs.GAPPLE_CAKE_REGEN_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ModServerConfigs.GAPPLE_CAKE_RES_DUR.get(), ModServerConfigs.GAPPLE_CAKE_RES_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ModServerConfigs.GAPPLE_CAKE_ABSORPTION_DUR.get(), ModServerConfigs.GAPPLE_CAKE_ABSORPTION_STRENGTH.get()));
    }
}
