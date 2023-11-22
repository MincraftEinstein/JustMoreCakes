package einstein.jmc.block.cake;

import einstein.jmc.block.entity.GlowstoneCakeBlockEntity;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlowstoneCakeBlock extends BaseEntityCakeBlock {

    public GlowstoneCakeBlock(CakeBuilder builder) {
        super(builder);
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        if (blockEntity instanceof GlowstoneCakeBlockEntity glowstoneCakeBlockEntity) {
            glowstoneCakeBlockEntity.setGlowing();
        }
        return state;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlowstoneCakeBlockEntity(pos, state);
    }
}
