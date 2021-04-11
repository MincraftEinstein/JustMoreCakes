package einstein.jmc.blocks;

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

public class FireyCakeBlock extends CakeBlock
{
    public FireyCakeBlock(final Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1F);
        playerIn.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, ModServerConfigs.FIREY_CAKE_FIRE_RES_DUR.get(), ModServerConfigs.FIREY_CAKE_FIRE_RES_STRENGTH.get()));
        playerIn.setFire(ModServerConfigs.FIREY_CAKE_ON_FIRE_DUR.get());
        final int i = state.get(FireyCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(FireyCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
}
