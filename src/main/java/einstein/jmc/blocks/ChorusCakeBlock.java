package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class ChorusCakeBlock extends BaseCakeBlock {

    public ChorusCakeBlock(Properties properties) {
        super(properties, false);
    }
    
	@Override
	public void eatActions(Player player, BlockPos pos, BlockState state) {
		super.eatActions(player, pos, state);
        player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, ModServerConfigs.CHORUS_CAKE_LEVITATION_DUR.get(), ModServerConfigs.CHORUS_CAKE_LEVITATION_STRENGTH.get()));
        Util.teleportRandomly(player, ModServerConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
	}
}
