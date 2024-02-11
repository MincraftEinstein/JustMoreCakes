package einstein.jmc.block.cake;

import einstein.jmc.block.entity.GlowstoneCakeBlockEntity;
import einstein.jmc.util.CakeVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlowstoneThreeTieredCakeBlock extends BaseThreeTieredCakeBlock implements EntityBlock {

    public GlowstoneThreeTieredCakeBlock(CakeVariant builder) {
        super(builder);
    }

    @Override
    public BlockState eatActions(Player player, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = player.level().getBlockEntity(pos);
        if (blockEntity instanceof GlowstoneCakeBlockEntity glowstoneCakeBlockEntity) {
            glowstoneCakeBlockEntity.setGlowing();
        }
        return super.eatActions(player, pos, state);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GlowstoneCakeBlockEntity(pos, state);
    }
}
