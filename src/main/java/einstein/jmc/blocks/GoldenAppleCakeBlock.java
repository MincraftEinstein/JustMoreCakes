package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.init.ModConfigs;
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

public class GoldenAppleCakeBlock extends CakeBlockBase
{
    public GoldenAppleCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(GoldenAppleCakeBlock.BITES, 0));
    }
    
    @Override
    public ActionResultType onBlockActivated(final BlockState p_225533_1_, final World p_225533_2_, final BlockPos p_225533_3_, final PlayerEntity p_225533_4_, final Hand p_225533_5_, final BlockRayTraceResult p_225533_6_) {
        if (p_225533_2_.isRemote) {
            final ItemStack itemstack = p_225533_4_.getHeldItem(p_225533_5_);
            if (this.func_226911_a_(p_225533_2_, p_225533_3_, p_225533_1_, p_225533_4_) == ActionResultType.SUCCESS) {
                return ActionResultType.SUCCESS;
            }
            if (itemstack.isEmpty()) {
                return ActionResultType.CONSUME;
            }
        }
        return this.func_226911_a_(p_225533_2_, p_225533_3_, p_225533_1_, p_225533_4_);
    }
    
    private ActionResultType func_226911_a_(final IWorld p_226911_1_, final BlockPos p_226911_2_, final BlockState p_226911_3_, final PlayerEntity p_226911_4_) {
        if (!p_226911_4_.canEat(false)) {
            return ActionResultType.PASS;
        }
        p_226911_4_.addStat(Stats.EAT_CAKE_SLICE);
        p_226911_4_.getFoodStats().addStats(2, 0.1f);
        p_226911_4_.addPotionEffect(new EffectInstance(Effects.REGENERATION, ModConfigs.GAPPLE_CAKE_REGEN_DUR.get(), ModConfigs.GAPPLE_CAKE_REGEN_STRENGTH.get()));
        p_226911_4_.addPotionEffect(new EffectInstance(Effects.RESISTANCE, ModConfigs.GAPPLE_CAKE_RES_DUR.get(), ModConfigs.GAPPLE_CAKE_RES_STRENGTH.get()));
        p_226911_4_.addPotionEffect(new EffectInstance(Effects.ABSORPTION, ModConfigs.GAPPLE_CAKE_ABSORPTION_DUR.get(), ModConfigs.GAPPLE_CAKE_ABSORPTION_STRENGTH.get()));
        final int i = p_226911_3_.get(GoldenAppleCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            p_226911_1_.setBlockState(p_226911_2_, p_226911_3_.with(GoldenAppleCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            p_226911_1_.removeBlock(p_226911_2_, false);
        }
        return ActionResultType.SUCCESS;
    }
}
