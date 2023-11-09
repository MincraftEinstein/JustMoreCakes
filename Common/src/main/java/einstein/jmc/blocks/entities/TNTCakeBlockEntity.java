package einstein.jmc.blocks.entities;

import einstein.jmc.init.ModBlockEntityTypes;
import einstein.jmc.init.ModCommonConfigs;
import einstein.jmc.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TNTCakeBlockEntity extends BlockEntity {

    public TNTCakeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TNT_CAKE.get(), pos, state);
    }

    public void explode() {
        Util.createExplosion(level, getBlockPos(), ModCommonConfigs.TNT_CAKE_EXPLOSION_SIZE.get());
    }
}
