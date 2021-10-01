package einstein.jmc.blocks;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ChorusCakeBlock extends BaseCakeBlock
{
    public ChorusCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
	@Override
	public void eatActions(Player player) {
		player.getFoodData().eat(2, 0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, ModServerConfigs.CHORUS_CAKE_LEVITATION_DUR.get(), ModServerConfigs.CHORUS_CAKE_LEVITATION_STRENGTH.get()));
        Actions.teleportRandomly(player, ModServerConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
	}
}
