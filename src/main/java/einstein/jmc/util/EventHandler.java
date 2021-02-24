package einstein.jmc.util;

import java.util.Random;

import einstein.einsteins_library.blocks.CakeBlockBase;
import einstein.jmc.blocks.BirthdayCakeBlock;
import einstein.jmc.blocks.CupcakeBlock;
import einstein.jmc.blocks.GlowstoneCakeBlock;
import einstein.jmc.blocks.TripleDeckerCakeBlock;
import einstein.jmc.init.ModPotions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EventHandler {

	protected final Random random = new Random();

	@SubscribeEvent
	public void cakeEaten(PlayerInteractEvent.RightClickBlock event) {
	    World world = event.getWorld();
	    PlayerEntity player = event.getPlayer();
	    BlockPos pos = event.getPos();
	    BlockState state = world.getBlockState(pos);
	    Block block = state.getBlock();
		
		if(ModList.get().isLoaded("cakechomps")) {
		    if (!(block instanceof CakeBlockBase || block instanceof BirthdayCakeBlock || block instanceof CupcakeBlock || block instanceof GlowstoneCakeBlock || block instanceof TripleDeckerCakeBlock) || !player.canEat(false)) {
		        return;
		      }
		      ItemStack stack = block.getPickBlock(state, null, world, pos, player);

		      for (int i = 0; i < 16; ++i) {
		        Vector3d vec3d = new Vector3d(((double) random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
		        vec3d = vec3d.rotatePitch(-player.rotationPitch * ((float) Math.PI / 180F));
		        vec3d = vec3d.rotateYaw(-player.rotationYaw * ((float) Math.PI / 180F));
		        double d0 = (double) (-random.nextFloat()) * 0.6D - 0.3D;
		        Vector3d vec3d1 = new Vector3d(((double) random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
		        vec3d1 = vec3d1.rotatePitch(-player.rotationPitch * ((float) Math.PI / 180F));
		        vec3d1 = vec3d1.rotateYaw(-player.rotationYaw * ((float) Math.PI / 180F));
		        vec3d1 = vec3d1.add(player.getPosX(), player.getPosYEye(), player.getPosZ());
		        ItemParticleData particle = new ItemParticleData(ParticleTypes.ITEM, stack);

		        if (player.world instanceof ServerWorld) {
		          ServerWorld serverWorld = (ServerWorld) player.world;
		          serverWorld.spawnParticle(particle, vec3d1.x, vec3d1.y, vec3d1.z, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
		        } else {
		          world.addParticle(particle, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
		        }
		      }
		      player.playSound(player.getEatSound(stack), 0.5F + 0.5F * (float) random.nextInt(2),
		          (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
		    }
		}
	
	@SubscribeEvent
	public void onEntityJump(final LivingJumpEvent event) {
		if (event.getEntity() instanceof PlayerEntity) {
			final PlayerEntity player = (PlayerEntity)event.getEntity();
			if(player.isPotionActive(ModPotions.BOUNCING_EFFECT.get())) {
				player.prevPosY = 0.800000011920929;
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(final LivingUpdateEvent event) {
		final World world = event.getEntity().getEntityWorld();
		if (event.getEntityLiving().isPotionActive(ModPotions.BOUNCING_EFFECT.get())) {
			final BlockPos topPlayer = new BlockPos(event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 2.0, event.getEntityLiving().getPosX());
			if (!world.getBlockState(topPlayer).getBlock().equals(Blocks.AIR) && !event.getEntityLiving().isSneaking()) {
				event.getEntityLiving().prevPosY = 0.0F;
				event.getEntityLiving().lastTickPosY = event.getEntityLiving().lastTickPosY;
			}
			if (event.getEntityLiving().collidedVertically && event.getEntityLiving().isOnGround()) {
                double velX3;
                if (this.random.nextInt(10) <= 5) {
                    velX3 = this.random.nextDouble() * 0.25 - 0.25;
                }
                else {
                    velX3 = this.random.nextDouble() * 0.25;
                }
                double velZ3;
                if (this.random.nextInt(10) <= 5) {
                    velZ3 = this.random.nextDouble() * 0.25 - 0.25;
                }
                else {
                    velZ3 = this.random.nextDouble() * 0.25;
                }
                if (world.isRemote) {
                    world.addParticle(ParticleTypes.ITEM_SLIME, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 0.4000000059604645, event.getEntityLiving().getPosZ(), velX3, 0.2, velZ3);
                }
                if (this.random.nextInt(10) <= 5) {
                    velX3 = this.random.nextDouble() * 0.25 - 0.25;
                }
                else {
                    velX3 = this.random.nextDouble() * 0.25;
                }
                if (world.isRemote) {
                    world.addParticle(ParticleTypes.ITEM_SLIME, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 0.4000000059604645, event.getEntityLiving().getPosZ(), velZ3, 0.2, velZ3);
                }
                if (this.random.nextInt(10) <= 5) {
                    velZ3 = this.random.nextDouble() * 0.25 - 0.25;
                }
                else {
                    velZ3 = this.random.nextDouble() * 0.25;
                }
                if (world.isRemote) {
                    world.addParticle(ParticleTypes.ITEM_SLIME, event.getEntityLiving().getPosX(), event.getEntityLiving().getPosY() + 0.4000000059604645, event.getEntityLiving().getPosZ(), velZ3, 0.2, velZ3);
                }
                event.getEntityLiving().playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F);
                event.getEntityLiving().lastTickPosY = 0.5F;
			}
		}
	}
}
