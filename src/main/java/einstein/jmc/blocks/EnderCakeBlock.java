package einstein.jmc.blocks;

import java.util.Random;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModClientConfigs;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCakeBlock extends BaseCakeBlock
{
    public EnderCakeBlock(final BlockBehaviour.Properties properties) {
        super(properties);
    }
    
    @Override
    public InteractionResult eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player) {
        if (!player.canEat(false)) {
            return InteractionResult.PASS;
        }
        player.awardStat(Stats.EAT_CAKE_SLICE);
		player.getFoodData().eat(2, 0.1F);
        Actions.teleportRandomly(player, ModServerConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
        player.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
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
    
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
    	if (ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
	       for (int i = 0; i < 3; ++i) {
	           int j = rand.nextInt(2) * 2 - 1;
	           int k = rand.nextInt(2) * 2 - 1;
	           double d0 = pos.getX() + 0.5D + 0.25D * j;
	           double d1 = (pos.getY() + rand.nextFloat());
	           double d2 = pos.getZ() + 0.5D + 0.25D * k;
	           double d3 = (rand.nextFloat() * j);
	           double d4 = (rand.nextFloat() - 0.5D) * 0.125D;
	           double d5 = (rand.nextFloat() * k);
	           worldIn.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
	        }
    	}
    }
}
