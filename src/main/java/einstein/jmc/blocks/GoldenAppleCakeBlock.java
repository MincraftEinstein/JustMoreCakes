package einstein.jmc.blocks;

import einstein.jmc.init.ModServerConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class GoldenAppleCakeBlock extends BaseCakeBlock
{
    public GoldenAppleCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
		player.getFoodData().eat(2, 0.1F);
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, ModServerConfigs.GAPPLE_CAKE_REGEN_DUR.get(), ModServerConfigs.GAPPLE_CAKE_REGEN_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ModServerConfigs.GAPPLE_CAKE_RES_DUR.get(), ModServerConfigs.GAPPLE_CAKE_RES_STRENGTH.get()));
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, ModServerConfigs.GAPPLE_CAKE_ABSORPTION_DUR.get(), ModServerConfigs.GAPPLE_CAKE_ABSORPTION_STRENGTH.get()));
        int i = state.getValue(BITES);
        accessor.gameEvent(player, GameEvent.EAT, pos);
		if (i < 6) { // Number must be same as BITES
			accessor.setBlock(pos, state.setValue(BITES, Integer.valueOf(i + 1)), 3);
		} else {
			accessor.removeBlock(pos, false);
			accessor.gameEvent(player, GameEvent.BLOCK_DESTROY, pos);
		}
		return InteractionResult.SUCCESS;
	}
}
