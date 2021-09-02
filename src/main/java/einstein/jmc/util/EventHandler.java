package einstein.jmc.util;

import java.util.Random;

import einstein.jmc.blocks.BirthdayCakeBlock;
import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseEntityCakeBlock;
import einstein.jmc.blocks.CupcakeBlock;
import einstein.jmc.blocks.ThreeTieredCakeBlock;
import einstein.jmc.init.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

public class EventHandler {

	protected final Random random = new Random();
	
	@SubscribeEvent
	public void cakeEaten(RightClickBlock event) {
		Level level = event.getWorld();
		Player player = event.getPlayer();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		if (ModList.get().isLoaded("cakechomps")) {
			if (!(block instanceof BirthdayCakeBlock || block instanceof CupcakeBlock || block instanceof BaseEntityCakeBlock || block instanceof ThreeTieredCakeBlock || block instanceof BaseCakeBlock) || !player.canEat(false)) {
				return;
			}
			ItemStack stack = block.getPickBlock(state, null, level, pos, player);

			for (int i = 0; i < 16; ++i) {
				Vec3 vec3d = new Vec3((random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
				vec3d = vec3d.xRot(-player.getXRot() * ((float) Math.PI / 180F));
				vec3d = vec3d.yRot(-player.getYRot() * ((float) Math.PI / 180F));
				double d0 = (-random.nextFloat()) * 0.6D - 0.3D;
				Vec3 vec3d1 = new Vec3((random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
				vec3d1 = vec3d1.xRot(-player.getXRot() * ((float) Math.PI / 180F));
				vec3d1 = vec3d1.yRot(-player.getYRot() * ((float) Math.PI / 180F));
				vec3d1 = vec3d1.add(player.getX(), player.getEyeY(), player.getZ());
				ItemParticleOption particle = new ItemParticleOption(ParticleTypes.ITEM, stack);

				if (player.level instanceof ServerLevel) {
					ServerLevel serverWorld = (ServerLevel) player.level;
					serverWorld.sendParticles(particle, vec3d1.x, vec3d1.y, vec3d1.z, 1, vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D);
				} else {
					level.addParticle(particle, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z);
				}
			}
			player.playSound(player.getEatingSound(stack), 0.5F + 0.5F * random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
		}
	}
	
	@SubscribeEvent
	public void onEntityJump(final LivingJumpEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player)event.getEntity();
			if (player.hasEffect(ModPotions.BOUNCING_EFFECT)) {
				player.push(0, 0.15F, 0);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(final LivingUpdateEvent event) {
		final Level level = event.getEntity().getCommandSenderWorld();
		final LivingEntity entity = event.getEntityLiving();
		if (entity.hasEffect(ModPotions.BOUNCING_EFFECT)) {
			if (entity.verticalCollision && entity.isOnGround() && !entity.isSleeping()) {
				float j = 0.65F;
				if (entity.hasEffect(MobEffects.JUMP)) {
			         j += 0.1F * (entity.getEffect(MobEffects.JUMP).getAmplifier() + 1);
			    }
				entity.push(0, j, 0);
				entity.playSound(SoundEvents.SLIME_SQUISH, 1.0F, 1.0F);
				if (level.isClientSide) {
		            for (int i = 0; i < 8; ++i) {
						float f = random.nextFloat() * ((float)Math.PI * 2F);
			            float f1 = random.nextFloat() * 0.5F + 0.5F;
			            float f2 = Mth.sin(f) * 1 * 0.5F * f1;
			            float f3 = Mth.cos(f) * 1 * 0.5F * f1;
						level.addParticle(ParticleTypes.ITEM_SLIME, entity.getX() + f2, entity.getY(), entity.getZ() + f3, 0.0D, 0.0D, 0.0D);
		            }
				}
			}
		}
	}
}
