package einstein.jmc.blocks;

import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GlowstoneCakeBlock extends BaseEntityCakeBlock {

    public GlowstoneCakeBlock(Properties properties, CakeBuilder builder) {
        super(properties, builder);
    }

    @Override
    public void eatActions(Player player, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        if (blockEntity instanceof GlowstoneCakeBlockEntity glowstoneCakeBlockEntity) {
            glowstoneCakeBlockEntity.setGlowing();
        }
    }

    @Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GlowstoneCakeBlockEntity(pos, state);
	}
}
