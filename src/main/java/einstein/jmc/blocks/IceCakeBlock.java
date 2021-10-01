package einstein.jmc.blocks;

import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.init.ModPotions;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class IceCakeBlock extends BaseCakeBlock
{
    public IceCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
	@Override
	public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        player.clearFire();
        player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, ModServerConfigs.ICE_CAKE_NIGHT_VISION_DUR.get(), ModServerConfigs.ICE_CAKE_NIGHT_VISION_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(ModPotions.FREEZING_EFFECT));
        FreezingEffect.freezeEntity(player);
	}
}
