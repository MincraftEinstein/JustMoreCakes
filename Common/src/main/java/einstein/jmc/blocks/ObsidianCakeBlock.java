package einstein.jmc.blocks;

import com.mojang.datafixers.util.Pair;
import einstein.jmc.util.CakeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ObsidianCakeBlock extends BaseCakeBlock {


    public ObsidianCakeBlock(CakeBuilder builder) {
        super(builder, 0);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_BITE[0];
    }

    @Override
    public InteractionResult eat(Level level, BlockPos pos, BlockState state, Player player) {
        player.hurt(player.level.damageSources().generic(), 2);
        level.setBlock(pos, state, 3);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 14;
    }

    @Override
    public IntegerProperty getBites() {
        return null;
    }

    @Override
    protected Pair<Integer, Float> getNourishment() {
        return Pair.of(0, 0F);
    }
}
