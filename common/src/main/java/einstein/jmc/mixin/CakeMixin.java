package einstein.jmc.mixin;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.blocks.CakeEffectsHolder;
import einstein.jmc.data.cake_effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeMixin implements CakeEffectsHolder {

    @Unique
    private CakeEffects justMoreCakes$cakeEffects;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (state.getBlock() instanceof CakeBlock && state.getValue(CakeBlock.BITES) == 0) {
            if (stack.is(Items.CAKE)) {
                BlockState newState = ModBlocks.TWO_TIERED_CAKE.get().defaultBlockState();
                Block.pushEntitiesUp(state, newState, level, pos);
                level.setBlockAndUpdate(pos, newState);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1, 1);
                player.awardStat(Stats.ITEM_USED.get(Items.CAKE));
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CakeBlock;eat(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/InteractionResult;"))
    private void eatInvoked(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (player instanceof ServerPlayer serverPlayer) {
            JustMoreCakes.CAKE_EATEN_TRIGGER.trigger(serverPlayer, ((CakeBlock) (Object) this));
        }
    }

    @Inject(method = "eat", at = @At(value = "RETURN", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private static void eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        CakeBlock cake = (CakeBlock) state.getBlock();
        CakeEffects effects = ((CakeMixin) (Object) cake).justMoreCakes$cakeEffects;
        if (effects != null) {
            for (CakeEffects.MobEffectHolder holder : effects.mobEffects()) {
                MobEffectInstance instance = new MobEffectInstance(holder.effect(), holder.duration().orElse(0), holder.amplifier().orElse(0));
                if (holder.effect().isInstantenous()) {
                    instance.getEffect().applyInstantenousEffect(player, player, player, instance.getAmplifier(), 1);
                }
                else {
                    player.addEffect(instance);
                }
            }
        }
    }

    @Override
    public @Nullable CakeEffects getCakeEffects() {
        return justMoreCakes$cakeEffects;
    }

    @Override
    public void setCakeEffects(CakeEffects effects) {
        justMoreCakes$cakeEffects = effects;
    }
}
