/**
 * Copied and slightly modified from {@link com.illusivesoulworks.cakechomps.CakeChompsMod}
 * and {@link com.illusivesoulworks.cakechomps.CakeChompsForgeMod}
 * both created by: Illusive Soulworks
 *
 * Copied, modified and used for the purpose of allowing JustMoreCakes! and CakeChomps to work together.
 */
package einstein.jmc.util;

import einstein.jmc.blocks.BaseCakeBlock;
import einstein.jmc.blocks.BaseCandleCakeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.Event.Result;

import java.util.Random;

public class CakeChompsEvents {

	private static final Random RANDOM = new Random();

	public static void onCakeEaten(RightClickBlock event) {
		Player player = event.getEntity();
		BlockPos pos = event.getPos();
		InteractionHand hand = event.getHand();
		BlockHitResult hitResult = event.getHitVec();
		Level level = player.getLevel();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();

		if (block instanceof BaseCakeBlock || block instanceof BaseCandleCakeBlock) {
			if (!player.getItemInHand(hand).is(ItemTags.CANDLES) || !(block instanceof BaseCakeBlock) || !state.getOptionalValue(BaseCakeBlock.BITES).map(value -> value == 0).orElse(false)) {

				UseOnContext useoncontext = new UseOnContext(player, hand, hitResult);
				ItemStack stack = player.getItemInHand(hand);

				if (event.getUseItem() != Result.DENY) {
					InteractionResult interact = stack.onItemUseFirst(useoncontext);

					if (interact != InteractionResult.PASS) {
						return;
					}
				}

				boolean flag = !player.getMainHandItem().isEmpty() || !player.getOffhandItem().isEmpty();
				boolean flag1 = player.isSecondaryUseActive() && flag && (!player.getMainHandItem().doesSneakBypassUse(player.getLevel(), pos, player) || !player.getOffhandItem().doesSneakBypassUse(player.getLevel(), pos, player));
				if (event.getUseBlock() == Result.ALLOW || event.getUseBlock() != Result.DENY && !flag1) {
					ItemStack blockStack = block.getCloneItemStack(level, pos, state);

					for (int i = 0; i < 16; ++i) {
						Vec3 vec3 = new Vec3((RANDOM.nextFloat() - 0.5F) * 0.1F, Math.random() * 0.1F + 0.1F, 0);
						vec3 = vec3.xRot(-player.getXRot() * 0.017453292F);
						vec3 = vec3.yRot(-player.getYRot() * 0.017453292F);
						double d0 = (-RANDOM.nextFloat()) * 0.6F - 0.3F;
						Vec3 vec31 = new Vec3((RANDOM.nextFloat() - 0.5F) * 0.3F, d0, 0.6F);
						vec31 = vec31.xRot(-player.getXRot() * 0.017453292F);
						vec31 = vec31.yRot(-player.getYRot() * 0.017453292F);
						vec31 = vec31.add(player.getX(), player.getEyeY(), player.getZ());
						ParticleOptions particle = new ItemParticleOption(ParticleTypes.ITEM, blockStack);
						Level var15 = player.level;
						if (var15 instanceof ServerLevel server) {
							server.sendParticles(particle, vec31.x, vec31.y, vec31.z, 1, vec3.x, vec3.y + 0.05F, vec3.z, 0);
						}
						else {
							level.addParticle(particle, vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05, vec3.z);
						}
					}

					player.playSound(player.getEatingSound(blockStack), 0.5F + 0.5F * RANDOM.nextInt(2), (RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.2F + 1.0F);
				}
			}
		}
	}
}
