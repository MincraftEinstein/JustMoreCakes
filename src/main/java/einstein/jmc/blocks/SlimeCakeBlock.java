package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.init.ModConfigs;
import einstein.jmc.init.ModPotions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class SlimeCakeBlock extends CakeBlockBase
{
    public SlimeCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(SlimeCakeBlock.BITES, 0));
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
        p_226911_4_.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, ModConfigs.SLIME_CAKE_JUMP_BOOST_DUR.get(), ModConfigs.SLIME_CAKE_JUMP_BOOST_STRENGTH.get()));
        p_226911_4_.addPotionEffect(new EffectInstance(Effects.RESISTANCE, ModConfigs.SLIME_CAKE_RES_DUR.get(), ModConfigs.SLIME_CAKE_RES_STRENGTH.get()));
        p_226911_4_.addPotionEffect(new EffectInstance(ModPotions.BOUNCING_EFFECT.get(), ModConfigs.SLIME_CAKE_BOUNCING_DUR.get(), ModConfigs.SLIME_CAKE_BOUNCING_STRENGTH.get()));
        final int i = p_226911_3_.get(SlimeCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            p_226911_1_.setBlockState(p_226911_2_, p_226911_3_.with(SlimeCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            p_226911_1_.removeBlock(p_226911_2_, false);
        }
        return ActionResultType.SUCCESS;
    }
    
    public void onFallenUpon(final World worldIn, final BlockPos pos, final Entity entityIn, final float fallDistance) {
        if (entityIn.isSuppressingBounce()) {
            super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
        }
        else {
            entityIn.onLivingFall(fallDistance, 0.0f);
        }
    }
    
    public void onLanded(final IBlockReader worldIn, final Entity entityIn) {
        if (entityIn.isSuppressingBounce()) {
            super.onLanded(worldIn, entityIn);
        }
        else {
            this.func_226946_a_(entityIn);
        }
    }
    
    private void func_226946_a_(final Entity p_226946_1_) {
        final Vector3d vec3d = p_226946_1_.getMotion();
        if (vec3d.y < 0.0) {
            final double d0 = (p_226946_1_ instanceof LivingEntity) ? 1.0 : 0.8;
            p_226946_1_.setMotion(vec3d.x, -vec3d.y * d0, vec3d.z);
        }
    }
    
    public void onEntityWalk(final World worldIn, final BlockPos pos, final Entity entityIn) {
        final double d0 = Math.abs(entityIn.getMotion().y);
        if (d0 < 0.1 && !entityIn.isSteppingCarefully()) {
            final double d2 = 0.4 + d0 * 0.2;
            entityIn.setMotion(entityIn.getMotion().mul(d2, 1.0, d2));
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }
}
