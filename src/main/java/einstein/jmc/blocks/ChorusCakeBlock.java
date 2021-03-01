package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.einsteins_library.util.Actions;
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

public class ChorusCakeBlock extends CakeBlockBase
{
    public ChorusCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(ChorusCakeBlock.BITES, 0));
    }
    
    @Override
    public ActionResultType onBlockActivated(final BlockState state, final World worldIn, final BlockPos pos, final PlayerEntity player, final Hand handIn, final BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            final ItemStack itemstack = player.getHeldItem(handIn);
            if (this.eatSlice(worldIn, pos, state, player) == ActionResultType.SUCCESS) {
                return ActionResultType.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }
        return this.eatSlice(worldIn, pos, state, player);
    }
    
    private ActionResultType eatSlice(final IWorld worldIn, final BlockPos pos, final BlockState state, final PlayerEntity playerIn) {
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1f);
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
