package einstein.jmc.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CeramicBlockTransferBlockEntity extends CeramicBowlBlockEntity {

    private final BlockEntityTransferHandler<CeramicBlockTransferBlockEntity> handler = new BlockEntityTransferHandler<>(() -> this);

    public CeramicBlockTransferBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
        LazyOptional<T> optional = handler.getCapability(capability, side);
        return optional != null ? optional : super.getCapability(capability, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        handler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        handler.refreshHandlers();
    }
}
