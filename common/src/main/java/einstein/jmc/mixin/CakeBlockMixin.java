package einstein.jmc.mixin;

import einstein.jmc.JustMoreCakes;
import einstein.jmc.block.CakeEffectsHolder;
import einstein.jmc.block.cake.BaseCakeBlock;
import einstein.jmc.block.cake.BaseTwoTieredCakeBlock;
import einstein.jmc.data.effects.CakeEffects;
import einstein.jmc.init.ModBlocks;
import einstein.jmc.data.SerializableMobEffectInstance;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class CakeBlockMixin implements CakeEffectsHolder {

    @Unique
    @Nullable
    private CakeEffects justMoreCakes$cakeEffects;

    @Unique
    private final CakeBlock justMoreCakes$me = (CakeBlock) (Object) this;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = player.getItemInHand(hand);
        Item item = stack.getItem();

        if (stack.is(ItemTags.CANDLES) && state.getValue(CakeBlock.BITES) == 0) {
            Block block = Block.byItem(item);
            if (block instanceof CandleBlock) {
                Block.pushEntitiesUp(state, CandleCakeBlock.byCandle(block), level, pos);
            }
        }

        if (justMoreCakes$me.equals(Blocks.CAKE)) {  // Need to check that this is the default cake, so that things won't break with inheritance
            if (stack.is(Items.CAKE)) {
                if (BaseTwoTieredCakeBlock.convertTo(ModBlocks.VANILLA_CAKE_FAMILY, state, pos, level, player, stack).consumesAction()) {
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @Inject(method = "eat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"))
    private static void eat(LevelAccessor accessor, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<InteractionResult> cir) {
        CakeBlock cake = (CakeBlock) state.getBlock(); // Don't replace with a reference to Blocks.CAKE, so that this will work with inheritance
        CakeEffects cakeEffects = ((CakeEffectsHolder) cake).justMoreCakes$getCakeEffects();
        if (!player.level().isClientSide && cakeEffects != null) {
            for (SerializableMobEffectInstance holder : cakeEffects.mobEffects()) {
                Util.applyEffectFromInstance(holder, player);
            }
        }

        if (player instanceof ServerPlayer serverPlayer) {
            JustMoreCakes.CAKE_EATEN_TRIGGER.trigger(serverPlayer, cake);
        }
    }

    @Inject(method = "getAnalogOutputSignal", at = @At("HEAD"), cancellable = true)
    private void getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(BaseCakeBlock.getMultipliedSignal(true, 7));
    }

    @Nullable
    @Override
    public CakeEffects justMoreCakes$getCakeEffects() {
        if (justMoreCakes$cakeEffects != null) {
            return justMoreCakes$cakeEffects;
        }

        if (justMoreCakes$me.equals(Blocks.CAKE)) {
            return ModBlocks.VANILLA_CAKE_FAMILY.justMoreCakes$getCakeEffects();
        }

        return null;
    }

    @Override
    public void justMoreCakes$setCakeEffects(CakeEffects effects) {
        justMoreCakes$cakeEffects = effects;
    }
}
