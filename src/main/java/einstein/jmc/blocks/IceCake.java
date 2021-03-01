package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.effects.FreezingEffect;
import einstein.jmc.init.ModConfigs.ModServerConfigs;
import einstein.jmc.init.ModPotions;
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

public class IceCake extends CakeBlockBase
{
    public IceCake(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(IceCake.BITES, 0));
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
        playerIn.extinguish();
        playerIn.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, ModServerConfigs.ICE_CAKE_NIGHT_VISION_DUR.get(), ModServerConfigs.ICE_CAKE_NIGHT_VISION_STRENGTH.get()));
        playerIn.addPotionEffect(new EffectInstance(ModPotions.FREEZING_EFFECT.get()));
        FreezingEffect.freezeEntity(playerIn);
        final int i = state.get(IceCake.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(IceCake.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
}
