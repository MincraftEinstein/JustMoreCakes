package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.init.ModConfigs.ModServerConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BeetrootCakeBlock extends CakeBlockBase
{
    public BeetrootCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(BeetrootCakeBlock.BITES, 0));
    }
    
    @Override
    public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTrace) {
        if (world.isRemote) {
            final ItemStack itemstack = player.getHeldItem(hand);
            if (this.onEaten(world, pos, state, player) == ActionResultType.SUCCESS) {
                return ActionResultType.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }
        return this.onEaten(world, pos, state, player);
    }
    
    private ActionResultType onEaten(final IWorld world, final BlockPos pos, final BlockState state, final PlayerEntity player) {
        if (!player.canEat(false)) {
            return ActionResultType.PASS;
        }
        player.addStat(Stats.EAT_CAKE_SLICE);
        player.getFoodStats().addStats(2, 0.1f);
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
