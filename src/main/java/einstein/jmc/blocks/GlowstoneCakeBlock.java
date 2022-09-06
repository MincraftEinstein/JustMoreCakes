package einstein.jmc.blocks;

import einstein.jmc.blockentity.GlowstoneCakeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class GlowstoneCakeBlock extends BaseEntityCakeBlock {

    public GlowstoneCakeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void eatActions(Player player, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = player.getCommandSenderWorld().getBlockEntity(pos);
        if (blockEntity instanceof GlowstoneCakeBlockEntity glowstoneCakeBlockEntity) {
            glowstoneCakeBlockEntity.setGlowing();
        }
    }

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new GlowstoneCakeBlockEntity(pos, state);
	}
}
