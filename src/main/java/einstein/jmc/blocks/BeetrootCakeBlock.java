package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BeetrootCakeBlock extends BaseCakeBlock
{
    public BeetrootCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.BEETROOT_CAKE_REGEN_DUR.get(), ModServerConfigs.BEETROOT_CAKE_REGEN_STRENGTH.get()));
	}
}
