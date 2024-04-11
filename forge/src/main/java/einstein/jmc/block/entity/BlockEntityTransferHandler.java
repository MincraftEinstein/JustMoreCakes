package einstein.jmc.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockEntityTransferHandler<T extends BlockEntity & WorldlyContainer> {

    private final Supplier<T> blockEntity;
    private LazyOptional<? extends IItemHandler>[] handlers;

    public BlockEntityTransferHandler(Supplier<T> blockEntity) {
        this.blockEntity = blockEntity;
        refreshHandlers();
    }

    @Nullable
    public <V> LazyOptional<V> getCapability(Capability<V> capability, @Nullable Direction side) {
        if (!blockEntity.get().isRemoved() && side != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            return switch (side) {
                case UP -> handlers[0].cast();
                case DOWN -> handlers[1].cast();
                default -> handlers[2].cast();
            };
        }
        return null;
    }

    public void invalidate() {
        for (LazyOptional<? extends IItemHandler> handler : handlers) {
            handler.invalidate();
        }
    }

    public void refreshHandlers() {
        handlers = SidedInvWrapper.create(blockEntity.get(), Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
