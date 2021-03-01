package einstein.jmc.blocks;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.init.ModConfigs.ModServerConfigs;
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
        playerIn.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, ModServerConfigs.SLIME_CAKE_JUMP_BOOST_DUR.get(), ModServerConfigs.SLIME_CAKE_JUMP_BOOST_STRENGTH.get()));
        playerIn.addPotionEffect(new EffectInstance(Effects.RESISTANCE, ModServerConfigs.SLIME_CAKE_RES_DUR.get(), ModServerConfigs.SLIME_CAKE_RES_STRENGTH.get()));
        playerIn.addPotionEffect(new EffectInstance(ModPotions.BOUNCING_EFFECT.get(), ModServerConfigs.SLIME_CAKE_BOUNCING_DUR.get(), ModServerConfigs.SLIME_CAKE_BOUNCING_STRENGTH.get()));
        final int i = state.get(SlimeCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(SlimeCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
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
            this.bounceEntity(entityIn);
        }
    }
    
    private void bounceEntity(final Entity entityIn) {
        final Vector3d vector3d = entityIn.getMotion();
        if (vector3d.y < 0.0) {
            final double d0 = (entityIn instanceof LivingEntity) ? 1.0 : 0.8;
            entityIn.setMotion(vector3d.x, -vector3d.y * d0, vector3d.z);
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
