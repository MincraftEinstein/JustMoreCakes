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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
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
		
		if (ModList.get().isLoaded("cakechomps")) {
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
			if (player.isPotionActive(ModPotions.BOUNCING_EFFECT.get())) {
				player.addVelocity(0, 0.15F, 0);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(final LivingUpdateEvent event) {
		final World world = event.getEntity().getEntityWorld();
		final LivingEntity entity = event.getEntityLiving();
		if (entity.isPotionActive(ModPotions.BOUNCING_EFFECT.get())) {
			if (entity.collidedVertically && entity.isOnGround() && !entity.isSleeping()) {
				float j = 0.65F;
				if (entity.isPotionActive(Effects.JUMP_BOOST)) {
			         j += 0.1F * (entity.getActivePotionEffect(Effects.JUMP_BOOST).getAmplifier() + 1);
			    }
				entity.addVelocity(0, j, 0);
				entity.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F);
				if (world.isRemote) {
		            for (int i = 0; i < 8; ++i) {
						float f = random.nextFloat() * ((float)Math.PI * 2F);
			            float f1 = random.nextFloat() * 0.5F + 0.5F;
			            float f2 = MathHelper.sin(f) * 1 * 0.5F * f1;
			            float f3 = MathHelper.cos(f) * 1 * 0.5F * f1;
						world.addParticle(ParticleTypes.ITEM_SLIME, entity.getPosX() + f2, entity.getPosY(), entity.getPosZ() + f3, 0.0D, 0.0D, 0.0D);
		            }
				}
			}
		}
	}
}
