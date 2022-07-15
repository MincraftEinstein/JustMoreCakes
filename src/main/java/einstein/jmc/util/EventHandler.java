package einstein.jmc.util;

import einstein.jmc.blocks.*;
import einstein.jmc.init.ModPotions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;

import java.util.Random;

public class EventHandler {

	protected final Random random = new Random();
	
	@SubscribeEvent
	void cakeEaten(RightClickBlock event) {
		if (ModList.get().isLoaded("cakechomps")) {
			Level level = event.getLevel();
			Player player = event.getEntity();
			BlockPos pos = event.getPos();
			BlockHitResult rayTraceResult = event.getHitVec();
			InteractionHand hand = event.getHand();
			BlockState state = level.getBlockState(pos);
			Block block = state.getBlock();
			
			if (!(block instanceof BirthdayCakeBlock || block instanceof CupcakeBlock
					|| block instanceof BaseEntityCakeBlock || block instanceof ThreeTieredCakeBlock || block instanceof ThreeTieredCandleCakeBlock
					|| block instanceof BaseCakeBlock || block instanceof BaseCandleCakeBlock) || !player.canEat(false)) {
				return;
			}
			
			UseOnContext useoncontext = new UseOnContext(player, hand, rayTraceResult);
			ItemStack stack = player.getItemInHand(hand);
			
			if (event.getUseItem() != Result.DENY) {
				InteractionResult result = stack.onItemUseFirst(useoncontext);

				if (result != InteractionResult.PASS) {
					return;
				}
			}
			
			boolean flag = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
			boolean flag1 = (player.isSecondaryUseActive() && flag)
					&& !(player.getMainHandItem().doesSneakBypassUse(level, pos, player)
							&& player.getOffhandItem().doesSneakBypassUse(level, pos, player));
			
			if ((event.getUseBlock() == Result.ALLOW || (event.getUseBlock() != Result.DENY && !flag1)) && flag2(player, block, state)) {
				ItemStack blockStack = block.getCloneItemStack(state, null, level, pos, player);
				
				for (int i = 0; i < 16; ++i) {
					Vec3 vec3 = new Vec3((random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
					vec3 = vec3.xRot(-player.getXRot() * ((float) Math.PI / 180F));
					vec3 = vec3.yRot(-player.getYRot() * ((float) Math.PI / 180F));
					double d0 = (double) (-random.nextFloat()) * 0.6D - 0.3D;
					Vec3 vec31 = new Vec3((random.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
					vec31 = vec31.xRot(-player.getXRot() * ((float) Math.PI / 180F));
					vec31 = vec31.yRot(-player.getYRot() * ((float) Math.PI / 180F));
					vec31 = vec31.add(player.getX(), player.getEyeY(), player.getZ());
					ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, blockStack);
					
					if (player.level instanceof ServerLevel serverWorld) {
						serverWorld.sendParticles(particle, vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05D, vec3.z, 0.0D);
					} else {
						level.addParticle(particle, vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
					}
				}
				player.playSound(player.getEatingSound(blockStack), 0.5F + 0.5F * random.nextInt(2), (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
			}
		}
	}
	
	private boolean flag2(Player player, Block block, BlockState state) {
		String name = Util.getBlockRegistryName(block).getPath();
		ItemStack heldItem = player.getMainHandItem();
		
		if ((block instanceof BaseCakeBlock && 
				(!name.contains("red_mushroom_cake") && !name.contains("brown_mushroom_cake") && !name.contains("chorus_cake") && !name.contains("crimson_fungus_cake"))) ||
				block instanceof ThreeTieredCakeBlock || block instanceof BirthdayCakeBlock) {
			if (state == block.defaultBlockState()) {
				if ((block instanceof BirthdayCakeBlock)) {
					return false;
				}
				else if (heldItem.is(ItemTags.CANDLES)) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return true;
			}
		}
		else {
			if (block instanceof BaseCandleCakeBlock || block instanceof ThreeTieredCandleCakeBlock) {
				if (heldItem.is(Items.FLINT_AND_STEEL) || heldItem.is(Items.FIRE_CHARGE)) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				return true;	
			}
		}
	}
	
	@SubscribeEvent
	void onEntityJump(final LivingJumpEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player player = (Player)event.getEntity();
			if (player.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
				player.push(0, 0.15F, 0);
			}
		}
	}
	
	@SubscribeEvent
	void onEntityTick(final LivingEvent.LivingTickEvent event) {
		final Level level = event.getEntity().getCommandSenderWorld();
		final LivingEntity entity = event.getEntity();
		if (entity.hasEffect(ModPotions.BOUNCING_EFFECT.get())) {
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
