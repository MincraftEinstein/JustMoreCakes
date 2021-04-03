package einstein.jmc.blocks;

import java.util.Random;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.einsteins_library.util.Actions;
import einstein.jmc.init.ModConfigs.ModClientConfigs;
import einstein.jmc.init.ModConfigs.ModServerConfigs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderCakeBlock extends CakeBlockBase
{
    public EnderCakeBlock(final Block.Properties properties) {
        super(properties);
        this.setDefaultState((this.stateContainer.getBaseState()).with(EnderCakeBlock.BITES, 0));
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
    	final World world = playerIn.world;
    	final Random rand = new Random();
        if (!playerIn.canEat(false)) {
            return ActionResultType.PASS;
        }
        playerIn.addStat(Stats.EAT_CAKE_SLICE);
        playerIn.getFoodStats().addStats(2, 0.1F);
        Actions.teleportRandomly(playerIn, ModServerConfigs.ENDER_CAKE_TELEPORT_RADIUS.get());
        playerIn.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        if (world.isRemote) {
        	if (ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
                for(float f = 0; f < 2.5F; ++f) {
                    world.addParticle(ParticleTypes.PORTAL, playerIn.getPosXRandom(0.5D), playerIn.getPosYRandom() - 0.25D, playerIn.getPosZRandom(0.5D), (rand.nextDouble() - 0.5D) * 2.0D, -rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
                 }	
        	}
        }
        final int i = state.get(EnderCakeBlock.BITES);
        if (i < 6) { // Number must be same as BITES
            worldIn.setBlockState(pos, state.with(EnderCakeBlock.BITES, (i + 1)), 3);
        }
        else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    	if (ModClientConfigs.ENDER_CAKE_PARTICLES.get()) {
	       for(int i = 0; i < 3; ++i) {
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
