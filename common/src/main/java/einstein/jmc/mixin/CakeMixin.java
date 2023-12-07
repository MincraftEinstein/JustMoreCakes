package einstein.jmc.mixin;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

        if (stack.is(Items.CAKE) && BaseCakeBlock.isUneaten(state)) {
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

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private static void eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        CakeBlock cake = (CakeBlock) state.getBlock(); // Don't replace with a reference to Blocks.CAKE, so that this will work with inheritors
        CakeEffects cakeEffects = ((CakeEffectsHolder) cake).getCakeEffects();
        if (!player.level().isClientSide && cakeEffects != null) {
            for (CakeEffects.MobEffectHolder holder : cakeEffects.mobEffects()) {
                Util.applyEffectFromHolder(holder, player);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            JustMoreCakes.CAKE_EATEN_TRIGGER.trigger(serverPlayer, cake);
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
