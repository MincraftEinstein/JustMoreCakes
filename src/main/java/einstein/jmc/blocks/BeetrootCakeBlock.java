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

public class BeetrootCakeBlock extends CakeBlock
{
    public BeetrootCakeBlock(final Block.Properties properties) {
        super(properties);
    }
    
    @Override
    public ActionResultType eatSlice(final IWorld world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!player.canEat(false)) {
            return ActionResultType.PASS;
        }
        player.addStat(Stats.EAT_CAKE_SLICE);
        player.getFoodStats().addStats(2, 0.1F);
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, ModServerConfigs.BEETROOT_CAKE_REGEN_DUR.get(), ModServerConfigs.BEETROOT_CAKE_REGEN_STRENGTH.get()));
        final int i = state.get(BeetrootCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            world.setBlockState(pos, state.with(BeetrootCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            world.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
}
