package einstein.jmc.mixin;

import einstein.jmc.data.providers.ModBlockTags;
import einstein.jmc.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.world.level.block.Block.dropResources;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Redirect(method = "playerDestroy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;Z)V"))
    private void playerDestroy(BlockState state, Level level, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, boolean dropXP) {
        if (stack.is(ModItems.CAKE_SPATULA.get()) && state.is(ModBlockTags.CAKE_SPATULA_USABLE)) {
            dropResources(state, level, pos, blockEntity, entity, ItemStack.EMPTY, dropXP);
        }
        else {
            dropResources(state, level, pos, blockEntity, entity, stack, dropXP);
        }
    }
}
