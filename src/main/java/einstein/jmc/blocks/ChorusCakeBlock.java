package einstein.jmc.blocks;

import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModServerConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class ChorusCakeBlock extends CakeBlock
{
    public ChorusCakeBlock(final Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1F);
        playerIn.addPotionEffect(new EffectInstance(Effects.LEVITATION, ModServerConfigs.CHORUS_CAKE_LEVITATION_DUR.get(), ModServerConfigs.CHORUS_CAKE_LEVITATION_STRENGTH.get()));
        Actions.teleportRandomly(playerIn, ModServerConfigs.CHORUS_CAKE_TELEPORT_RADIUS.get());
        final int i = state.get(ChorusCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(ChorusCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
}
