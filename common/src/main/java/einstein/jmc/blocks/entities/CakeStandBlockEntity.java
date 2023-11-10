package einstein.jmc.blocks.entities;

import einstein.jmc.init.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CakeStandBlockEntity extends BlockEntity {

    private Block storedBlock = Blocks.AIR;

    public CakeStandBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.CAKE_STAND.get(), pos, state);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveStoredBlock(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ResourceLocation blockKey = ResourceLocation.tryParse(tag.getString("StoredBlock"));
        if (blockKey != null) {
            storedBlock = BuiltInRegistries.BLOCK.get(blockKey);
            setChanged();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        saveStoredBlock(tag);
    }

    private void saveStoredBlock(CompoundTag tag) {
        tag.putString("StoredBlock", BuiltInRegistries.BLOCK.getKey(storedBlock).toString());
    }

    public Block getStoredBlock() {
        return storedBlock;
    }

    public void setStoredBlock(Block storedBlock) {
        this.storedBlock = storedBlock;
        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
        setChanged();
    }

    public boolean isEmpty() {
        return storedBlock == null || storedBlock.equals(Blocks.AIR);
    }
}
